package i2f.springboot.ai.mcp.client.official;

import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.definition.impl.DefaultToolDefinition;
import i2f.ai.std.tool.schema.data.FunctionJsonSchema;
import i2f.mutator.BaseMutator;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.net.http.rest.impl.HttpProcessorRestClient;
import i2f.reflect.RichConverter;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.springboot.ai.mcp.client.official.data.JsonRpcRequest;
import i2f.springboot.ai.mcp.client.official.data.JsonRpcResponse;
import i2f.springboot.ai.mcp.client.official.data.result.JsonRpcInitialResult;
import i2f.springboot.ai.mcp.client.official.data.result.JsonRpcToolCallResult;
import i2f.springboot.ai.mcp.client.official.data.result.JsonRpcToolListItem;
import i2f.springboot.ai.mcp.client.official.data.result.JsonRpcToolListResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
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
public class OfficialJsonRpcMcpClientToolProvider implements McpToolProvider, Closeable, BaseMutator<OfficialJsonRpcMcpClientToolProvider> {
    public static final String HEADER_MCP_SESSION_ID = "Mcp-Session-Id";
    protected IRestClient restClient = new HttpProcessorRestClient();
    protected String baseUrl;
    protected AtomicLong idGenerator = new AtomicLong(1);
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    protected String name;
    protected String description;

    protected ReentrantLock lock = new ReentrantLock();
    protected AtomicBoolean initialized = new AtomicBoolean(false);
    protected String mcpSessionId;

    protected long expireTtl = TimeUnit.SECONDS.toMillis(15);
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
    public List<ToolDefinition> getTools() {
        if (hasCache.get() && System.currentTimeMillis() - expireTs.get() < expireTtl) {
            return new ArrayList<>(cache);
        }
        try {
            initial();
        } catch (IOException e) {
            throw new IllegalStateException("initial mcp client error: " + e.getMessage(), e);
        }
        lock.lock();
        try {

            RestHttpRequest request = new RestHttpRequest();
            request.setUrl(getEndpointUrl());
            request.setMethod(HttpMethodConstants.POST);
            request.setHeaders(HttpHeaders.create().add(HEADER_MCP_SESSION_ID, mcpSessionId));
            request.setBody(wrapJsonRpcHttpBody("tools/list", null));
            RestHttpResponse<JsonRpcResponse> rest = restClient.rest(request, JsonRpcResponse.class);

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

            JsonRpcResponse<?> body = rest.getBody();
            Object obj = body.getResult();
            JsonRpcToolListResult result = RichConverter.convert(obj, JsonRpcToolListResult.class);
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
        List<ToolDefinition> tools = getTools();
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
        Map<String, Object> params = new HashMap<>();
        params.put("name", callRequest.getName());
        params.put("arguments", jsonSerializer.deserializeAsMap(callRequest.getArguments())); // 直接透传 Map

        RestHttpRequest request = new RestHttpRequest();
        request.setUrl(getEndpointUrl());
        request.setMethod(HttpMethodConstants.POST);
        request.setHeaders(HttpHeaders.create().add(HEADER_MCP_SESSION_ID, mcpSessionId));
        request.setBody(wrapJsonRpcHttpBody("tools/call", params));
        RestHttpResponse<JsonRpcResponse> rest = restClient.rest(request, JsonRpcResponse.class);

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
        JsonRpcResponse body = rest.getBody();
        Object obj = body.getResult();
        JsonRpcToolCallResult result = RichConverter.convert(obj, JsonRpcToolCallResult.class);
        if (!result.isError()) {
            throw new IllegalStateException("invoke mcp tool error, cause reason is: ");
        }
        List<Map<String, Object>> contentList = result.getContent();
        return contentList;
    }

    public String getEndpointUrl() {
        String ret = baseUrl;
        if (ret.endsWith("/")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        if (!ret.endsWith("/mcp")) {
            ret = ret + "/mcp";
        }
        return ret;
    }

    public void initial() throws IOException {
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
            params.put("protocolVersion", "2024-11-05");
            params.put("capabilities", new HashMap<>());

            Map<String, Object> clientInfo = new HashMap<>();
            clientInfo.put("name", "java-mcp-bridge");
            clientInfo.put("version", "1.0.0");
            params.put("clientInfo", clientInfo);

            RestHttpRequest request = new RestHttpRequest();
            request.setUrl(getEndpointUrl());
            request.setMethod(HttpMethodConstants.POST);
            request.setBody(wrapJsonRpcHttpBody("initialize", params));
            RestHttpResponse<JsonRpcResponse> rest = restClient.rest(request, JsonRpcResponse.class);
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
            HttpHeaders headers = rest.getHeaders();

            this.mcpSessionId = headers.getFirstHeader(HEADER_MCP_SESSION_ID);

            if (this.mcpSessionId == null) {
                throw new IllegalStateException("MCP Server did not return a valid Mcp-Session-Id in headers!");
            }

            JsonRpcResponse<?> body = rest.getBody();
            Object obj = body.getResult();
            JsonRpcInitialResult result = RichConverter.convert(obj, JsonRpcInitialResult.class);
            String serverVersion = result.getProtocolVersion();
            // TODO: 可在此处校验 response.get("result") 中的 protocolVersion 是否兼容
        } finally {
            initialized.set(true);
            lock.unlock();
        }
    }

    @Override
    public void close() throws IOException {
        if (!initialized.get()) {
            return;
        }
        RestHttpRequest request = new RestHttpRequest();
        request.setUrl(getEndpointUrl());
        request.setMethod(HttpMethodConstants.DELETE);
        request.setBody(null);
        request.setHeaders(HttpHeaders.create().add(HEADER_MCP_SESSION_ID, mcpSessionId));
        RestHttpResponse<String> rest = restClient.rest(request, String.class);

        HttpHeaders headers = rest.getHeaders();
        String body = rest.getBody();

        mcpSessionId = null;
    }

    public <T> JsonRpcRequest<T> wrapJsonRpcHttpBody(String method, T params) {
        JsonRpcRequest<T> ret = new JsonRpcRequest<>();
        ret.setJsonrpc("2.0");
        ret.setId(idGenerator.getAndIncrement());
        ret.setMethod(method);
        ret.setParams(params);

        return ret;
    }
}
