package i2f.ai.rest.mcp.server.impl;

import i2f.ai.rest.mcp.HttpSimpleMcpConstants;
import i2f.ai.rest.mcp.server.HttpSimpleMcpServer;
import i2f.ai.rest.mcp.server.data.HttpSimpleMcpAppItem;
import i2f.ai.rest.mcp.server.data.HttpSimpleMcpRequest;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.IContext;
import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.resp.ApiResp;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/7/13 11:10
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpSimpleMcpServerImpl implements HttpSimpleMcpServer, BaseMutator<HttpSimpleMcpServerImpl> {
    protected IContext context;
    protected JsonSchemaAnnotationResolver annotationResolver = JsonSchemaAnnotationResolver.INSTANCE;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected IProxyInvocationHandler invocationHandler;
    protected CopyOnWriteArrayList<HttpSimpleMcpAppItem> appList = new CopyOnWriteArrayList<>();
    protected String hmacName = HttpSimpleMcpConstants.DEFAULT_HMAC_NAME;

    public List<ToolDefinition> listTools() {
        Map<String, ToolRawDefinition> definitionMap = ToolRawHelper.parseTools(annotationResolver, context);
        Collection<ToolRawDefinition> values = definitionMap.values();
        return new ArrayList<>(values);
    }

    public void assertValidMcpRequest(HttpSimpleMcpRequest request) {
        HttpHeaders headers = request.getHeaders();
        if (headers == null) {
            throw new IllegalArgumentException("blank mcp request header!");
        }
        String appId = headers.getFirstHeader(HttpSimpleMcpConstants.HEADER_APP_ID);
        if (appId == null || appId.isEmpty()) {
            throw new IllegalArgumentException("missing " + HttpSimpleMcpConstants.HEADER_APP_ID + " header!");
        }
        HttpSimpleMcpAppItem appItem = null;
        for (HttpSimpleMcpAppItem item : appList) {
            if (appId.equals(item.getAppId())) {
                appItem = item;
                break;
            }
        }
        if (appItem == null) {
            throw new IllegalArgumentException("appId cannot recognized!");
        }
        String timestamp = headers.getFirstHeader(HttpSimpleMcpConstants.HEADER_APP_DATE);
        long ts = Long.parseLong(timestamp, 16);
        if (Math.abs(System.currentTimeMillis() / 1000 - ts) < TimeUnit.MINUTES.toSeconds(30)) {
            throw new IllegalArgumentException("request timestamp too old!");
        }
        String nonce = headers.getFirstHeader(HttpSimpleMcpConstants.HEADER_APP_NONCE);
        // todo check nonce

        String sign = headers.getFirstHeader(HttpSimpleMcpConstants.HEADER_APP_SIGN);
        if (sign == null || sign.isEmpty()) {
            throw new IllegalArgumentException("sign not found!");
        }

        String appKey = appItem.getAppKey();

        Mac mac = null;

        try {
            SecretKey skey = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), hmacName);
            mac = Mac.getInstance(hmacName);
            mac.init(skey);
        } catch (Exception e) {
            throw new IllegalArgumentException("init hmac failure!");
        }
        if (mac == null) {
            throw new IllegalArgumentException("sign calc failure!");
        }
        String payload = appId + "#" + timestamp + "#" + nonce;
        mac.update(payload.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = mac.doFinal();
        String calcSign = Base64.getEncoder().encodeToString(bytes);
        if (!calcSign.equalsIgnoreCase(sign)) {
            throw new IllegalArgumentException("sign verify failure!");
        }
    }


    @Override
    public ApiResp<List<ToolDefinition>> getTools(HttpSimpleMcpRequest mcpRequest) {
        try {
            assertValidMcpRequest(mcpRequest);
            List<ToolDefinition> data = listTools();
            return ApiResp.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResp.error(e.getMessage());
        }
    }

    @Override
    public ApiResp<?> callTool(ToolBaseCallRequest request, HttpSimpleMcpRequest mcpRequest) {
        try {
            List<ToolDefinition> tools = listTools();
            for (ToolDefinition tool : tools) {
                ToolRawDefinition rawTool = ToolRawHelper.extractRawDefinition(tool);
                if (rawTool == null) {
                    continue;
                }
                if (tool.getName().equals(request.getName())) {
                    IJsonSerializer jsonSerializer = getJsonSerializer();
                    IProxyInvocationHandler invocationHandler = getInvocationHandler();
                    Map<String, Object> parameterMap = jsonSerializer.deserializeAsMap(request.getArguments());
                    Object obj = ToolRawHelper.invokeTool(rawTool, parameterMap, invocationHandler);
                    return ApiResp.success(obj);
                }
            }
            throw new IllegalStateException("un-support tool call request!");
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiResp.error(e.getMessage());
        }
    }
}
