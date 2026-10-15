package i2f.springboot.ai.mcp.client.official.v2026.stdio.provider;

import i2f.ai.rest.mcp.official.IJsonRpcDto;
import i2f.ai.rest.mcp.official.v2024.data.JsonRpcRequest;
import i2f.ai.rest.mcp.official.v2024.data.result.JsonRpcToolCallParam;
import i2f.ai.rest.mcp.official.v2024.data.result.JsonRpcToolListItem;
import i2f.ai.rest.mcp.official.v2026.consts.OfficialMcpConstantsV2026;
import i2f.ai.rest.mcp.official.v2026.data.JsonRpcErrorV2026;
import i2f.ai.rest.mcp.official.v2026.data.JsonRpcResponseV2026;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcServerDiscoverResult;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcToolCallResultV2026;
import i2f.ai.rest.mcp.official.v2026.data.result.JsonRpcToolListResultV2026;
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
 * @date 2026/9/28 15:50
 * @desc
 */
@Data
@NoArgsConstructor
public class StdioJsonRpcMcpClientV2026ToolProvider implements McpToolProvider, Closeable, BaseMutator<StdioJsonRpcMcpClientV2026ToolProvider> {

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
            JsonRpcRequest<Map<String, Object>> request = wrapJsonRpcHttpBody(OfficialMcpConstantsV2026.METHOD_TOOLS_LIST, null);
            CompletableFuture<Object> send = stdioClient.send(request.getId(), request);
            Object obj = send.get();
            JsonRpcResponseV2026<JsonRpcToolListResultV2026> rest = RichConverter.convert(obj, new TypeToken<JsonRpcResponseV2026<JsonRpcToolListResultV2026>>() {
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

            JsonRpcResponseV2026<JsonRpcToolListResultV2026> body = rest;

            JsonRpcErrorV2026 error = body.getError();
            if (error != null) {
                Integer code = error.getCode();
                if (code != null) {
                    throw new IllegalStateException("mcp server response get tools error, " + error.getCode() + ": " + error.getMessage());
                }
            }

            JsonRpcToolListResultV2026 result = body.getResult();
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

        JsonRpcRequest<Map<String, Object>> request = wrapJsonRpcHttpBody(OfficialMcpConstantsV2026.METHOD_TOOLS_CALL, params);
        CompletableFuture<Object> send = stdioClient.send(request.getId(), request);
        Object obj = send.get();
        JsonRpcResponseV2026<JsonRpcToolCallResultV2026> rest = RichConverter.convert(obj, new TypeToken<JsonRpcResponseV2026<JsonRpcToolCallResultV2026>>() {
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
        JsonRpcResponseV2026<JsonRpcToolCallResultV2026> body = rest;

        JsonRpcErrorV2026 error = body.getError();
        if (error != null) {
            Integer code = error.getCode();
            if (code != null) {
                throw new IllegalStateException("mcp server response call tool error, " + error.getCode() + ": " + error.getMessage());
            }
        }

        JsonRpcToolCallResultV2026 result = body.getResult();
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

            JsonRpcRequest<Map<String, Object>> request = wrapJsonRpcHttpBody(OfficialMcpConstantsV2026.METHOD_SERVER_DISCOVER, null);
            CompletableFuture<Object> send = stdioClient.send(request.getId(), request);
            Object obj = send.get();
            JsonRpcResponseV2026<JsonRpcServerDiscoverResult> rest = RichConverter.convert(obj, new TypeToken<JsonRpcResponseV2026<JsonRpcServerDiscoverResult>>() {
            });

            JsonRpcResponseV2026<JsonRpcServerDiscoverResult> body = rest;

            JsonRpcErrorV2026 error = body.getError();
            if (error != null) {
                Integer code = error.getCode();
                if (code != null) {
                    throw new IllegalStateException("mcp server response get tools error, " + error.getCode() + ": " + error.getMessage());
                }
            }

            JsonRpcServerDiscoverResult result = body.getResult();
            List<String> serverVersion = result.getSupportedVersions();
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

    public JsonRpcRequest<Map<String, Object>> wrapJsonRpcHttpBody(String method, IJsonRpcDto params) {
        JsonRpcRequest<Map<String, Object>> ret = new JsonRpcRequest<>();
        ret.setJsonrpc(OfficialMcpConstantsV2026.JSON_RPC_VERSION);
        ret.setId("" + idGenerator.getAndIncrement());
        ret.setMethod(method);

        Map<String, Object> map = new LinkedHashMap<>();
        if (params != null) {
            map.putAll(params.toMap());
        }

        Map<String, Object> meta = getMeta();
        map.put("_meta", meta);

        ret.setParams(map);

        return ret;
    }

    public Map<String, Object> getMeta() {
        /*
        "_meta": {
          "io.modelcontextprotocol/protocolVersion": "2026-07-28",
          "io.modelcontextprotocol/clientInfo": {
            "name": "MyClient",
            "version": "1.0.0"
          },
          "io.modelcontextprotocol/clientCapabilities": {}
        }
        */
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put(OfficialMcpConstantsV2026.META_PROTOCOL_VERSION, OfficialMcpConstantsV2026.PROTOCOL_VERSION);

        Map<String, Object> clientInfo = new HashMap<>();
        clientInfo.put("name", "java-mcp-bridge");
        clientInfo.put("version", "1.0.0");
        ret.put(OfficialMcpConstantsV2026.META_CLIENT_INFO, clientInfo);

        Map<String, Object> capabilities = new HashMap<>();
        ret.put(OfficialMcpConstantsV2026.META_CLIENT_CAPABILITIES, capabilities);

        return ret;
    }

}
