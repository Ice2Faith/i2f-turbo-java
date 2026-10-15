package i2f.springboot.ai.mcp.client.official.v2024.stdio.provider;

import i2f.ai.rest.mcp.official.v2024.consts.OfficialMcpConstants;
import i2f.ai.rest.mcp.official.v2024.data.JsonRpcError;
import i2f.ai.rest.mcp.official.v2024.data.JsonRpcRequest;
import i2f.ai.rest.mcp.official.v2024.data.JsonRpcResponse;
import i2f.ai.rest.mcp.official.v2024.data.result.*;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tags.AiTagRule;
import i2f.ai.std.tags.AiTagRuleHelper;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.definition.impl.DefaultToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.mutator.BaseMutator;
import i2f.reflect.RichConverter;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.springboot.ai.mcp.client.official.v2024.stdio.StdioMcpClient;
import i2f.typeof.token.TypeToken;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/7/21 17:42
 * @desc
 */
@Data
@NoArgsConstructor
public class StdioJsonRpcMcpClientToolProvider implements McpToolProvider, Closeable, BaseMutator<StdioJsonRpcMcpClientToolProvider> {

    protected StdioMcpClient stdioClient;

    protected AtomicLong idGenerator = new AtomicLong(1);
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    protected String name;
    protected String description;
    protected List<AiTagRule> tagRules;

    protected ReentrantLock lock = new ReentrantLock();
    protected AtomicBoolean initialized = new AtomicBoolean(false);

    protected long expireTtl = TimeUnit.MINUTES.toMillis(5);
    protected final CopyOnWriteArrayList<ToolDefinition> cache = new CopyOnWriteArrayList<>();
    protected final AtomicLong expireTs = new AtomicLong(0);
    protected final AtomicBoolean hasCache = new AtomicBoolean(false);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<ToolDefinition> listTools() {
        if (hasCache.get() && System.currentTimeMillis() < expireTs.get()) {
            return new ArrayList<>(cache);
        }
        try {
            initial();
        } catch (Throwable e) {
            throw new IllegalStateException("initial mcp client error: " + e.getMessage(), e);
        }
        lock.lock();
        try {
            JsonRpcRequest<Object> request = wrapJsonRpcHttpBody(OfficialMcpConstants.METHOD_TOOLS_LIST, new HashMap<>());
            CompletableFuture<Object> send = stdioClient.send(request.getId(), request);
            Object obj = send.get();
            JsonRpcResponse<JsonRpcToolListResult> rest = RichConverter.convert(obj, new TypeToken<JsonRpcResponse<JsonRpcToolListResult>>() {
            });

            // 【Map 结构示例】tools/list 响应
            // {
            //   "jsonrpc": "2.0",
            //   "id": 2,
            //   "result": {
            //     "tools": [
            //       {
            //         "name": "get_weather",
            //         "description": "Get current weather",
            //         "inputSchema": {
            //           "type": "object",
            //           "properties": { "city": { "type": "string" } },
            //           "required": ["city"]
            //         }
            //       }
            //     ]
            //   }
            // }

            JsonRpcResponse<JsonRpcToolListResult> body = rest;

            JsonRpcError error = body.getError();
            if (error != null) {
                Integer code = error.getCode();
                if (code != null) {
                    throw new IllegalStateException("mcp server response get tools error, " + error.getCode() + ": " + error.getMessage());
                }
            }

            JsonRpcToolListResult result = body.getResult();
            List<JsonRpcToolListItem> tools = result.getTools();

            List<ToolDefinition> ret = new ArrayList<>();
            for (JsonRpcToolListItem item : tools) {
                DefaultToolDefinition def = new DefaultToolDefinition();
                def.setName(item.getName());
                def.setDescription(item.getDescription());
                def.setTags(new HashSet<>());

                FunctionJsonSchema jsonSchema = new FunctionJsonSchema();
                jsonSchema.setName(item.getName());
                jsonSchema.setDescription(item.getDescription());
                jsonSchema.setStrict(true);
                jsonSchema.setParameters(item.getInputSchema());
                def.setJsonSchema(jsonSchema);

                if (tagRules != null) {
                    List<String> tags = AiTagRuleHelper.resolveTags(def.getName(), tagRules);
                    def.getTags().addAll(tags);
                }

                ret.add(def);
            }

            cache.clear();
            cache.addAll(ret);
            expireTs.set(System.currentTimeMillis() + expireTtl);
            hasCache.set(true);

            return new ArrayList<>(ret);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException("mcp client get tools error: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean support(ToolBaseCallRequest request) {
        List<ToolDefinition> tools = listTools();
        for (ToolDefinition tool : tools) {
            if (tool.getName().equals(request.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object callTool(ToolBaseCallRequest callRequest) throws Throwable {
        initial();
        // 【Map 结构示例】tools/call 请求参数
        // {
        //   "name": "get_weather",
        //   "arguments": { "city": "Beijing" }
        // }
        JsonRpcToolCallParam params = new JsonRpcToolCallParam();
        params.setName(callRequest.getName());
        params.setArguments(jsonSerializer.deserializeAsMap(callRequest.getArguments())); // 直接透传 Map

        JsonRpcRequest<JsonRpcToolCallParam> request = wrapJsonRpcHttpBody(OfficialMcpConstants.METHOD_TOOLS_CALL, params);
        CompletableFuture<Object> send = stdioClient.send(request.getId(), request);
        Object obj = send.get();
        JsonRpcResponse<JsonRpcToolCallResult> rest = RichConverter.convert(obj, new TypeToken<JsonRpcResponse<JsonRpcToolCallResult>>() {
        });

        // 【Map 结构示例】tools/call 响应
        // {
        //   "jsonrpc": "2.0",
        //   "id": 3,
        //   "result": {
        //     "content": [
        //       { "type": "text", "text": "Beijing is 25°C and sunny." }
        //     ],
        //     "isError": false
        //   }
        // }
        JsonRpcResponse<JsonRpcToolCallResult> body = rest;

        JsonRpcError error = body.getError();
        if (error != null) {
            Integer code = error.getCode();
            if (code != null) {
                throw new IllegalStateException("mcp server response call tool error, " + error.getCode() + ": " + error.getMessage());
            }
        }

        JsonRpcToolCallResult result = body.getResult();
        List<Map<String, Object>> contentList = result.getContent();

        if (result.isError()) {
            String reason = "";
            if (contentList != null && !contentList.isEmpty()) {
                Map<String, Object> map = contentList.get(0);
                if (map.containsKey("text")) {
                    reason = String.valueOf(map.get("text"));
                }
            }
            throw new IllegalStateException("invoke mcp tool error, cause reason is: " + reason);
        }

        return contentList;
    }


    public void initial() throws Throwable {
        if (initialized.get()) {
            return;
        }
        lock.lock();
        try {
            if (initialized.get()) {
                return;
            }
            // 【Map 结构示例】JSON-RPC initialize 请求
            // {
            //   "jsonrpc": "2.0",
            //   "id": 1,
            //   "method": "initialize",
            //   "params": {
            //     "protocolVersion": "2024-11-05",
            //     "capabilities": {},
            //     "clientInfo": { "name": "java-mcp-bridge", "version": "1.0.0" }
            //   }
            // }
            Map<String, Object> params = new HashMap<>();
            params.put("protocolVersion", OfficialMcpConstants.PROTOCOL_VERSION);
            params.put("capabilities", new HashMap<>());

            Map<String, Object> clientInfo = new HashMap<>();
            clientInfo.put("name", "java-mcp-bridge");
            clientInfo.put("version", "1.0.0");
            params.put("clientInfo", clientInfo);


            JsonRpcRequest<Map<String, Object>> request = wrapJsonRpcHttpBody(OfficialMcpConstants.METHOD_INITIALIZE, params);
            CompletableFuture<Object> send = stdioClient.send(request.getId(), request);
            Object obj = send.get();
            JsonRpcResponse<JsonRpcInitialResult> rest = RichConverter.convert(obj, new TypeToken<JsonRpcResponse<JsonRpcInitialResult>>() {
            });
            // 【Map 结构示例】initialize 响应
            // {
            //   "jsonrpc": "2.0",
            //   "id": 1,
            //   "result": {
            //     "protocolVersion": "2024-11-05",
            //     "capabilities": { "tools": { "listChanged": false } },
            //     "serverInfo": { "name": "xxx-server", "version": "1.0.0" }
            //   }
            // }

            JsonRpcResponse<JsonRpcInitialResult> body = rest;

            JsonRpcError error = body.getError();
            if (error != null) {
                Integer code = error.getCode();
                if (code != null) {
                    throw new IllegalStateException("mcp server response get tools error, " + error.getCode() + ": " + error.getMessage());
                }
            }

            JsonRpcInitialResult result = body.getResult();
            String serverVersion = result.getProtocolVersion();
            // TODO: 可在此处校验 response.get("result") 中的 protocolVersion 是否兼容

            initialized.set(true);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        if (!initialized.get()) {
            return;
        }
        stdioClient.close();

        this.initialized.set(false);
        this.cache.clear();
        this.hasCache.set(false);
    }

    public <T> JsonRpcRequest<T> wrapJsonRpcHttpBody(String method, T params) {
        JsonRpcRequest<T> ret = new JsonRpcRequest<>();
        ret.setJsonrpc(OfficialMcpConstants.JSON_RPC_VERSION);
        ret.setId("" + idGenerator.getAndIncrement());
        ret.setMethod(method);
        ret.setParams(params);

        return ret;
    }
}
