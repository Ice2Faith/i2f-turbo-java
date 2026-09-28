package i2f.ai.rest.mcp.simple.server.impl;

import i2f.ai.rest.mcp.simple.HttpSimpleMcpConstants;
import i2f.ai.rest.mcp.simple.data.McpCallPayloadDto;
import i2f.ai.rest.mcp.simple.server.HttpSimpleMcpServer;
import i2f.ai.rest.mcp.simple.server.data.HttpSimpleMcpAppItem;
import i2f.ai.rest.mcp.simple.server.data.HttpSimpleMcpRequest;
import i2f.ai.std.mcp.server.McpServerProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.cache.std.expire.IExpireCache;
import i2f.mutator.BaseMutator;
import i2f.net.http.data.HttpHeaders;
import i2f.resp.ApiResp;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
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
    protected long expireWindowMinutes = 30;
    protected IExpireCache<String, Object> expireCache;
    protected McpServerProvider mcpServerProvider;
    protected CopyOnWriteArrayList<HttpSimpleMcpAppItem> appList = new CopyOnWriteArrayList<>();
    protected String hmacName = HttpSimpleMcpConstants.DEFAULT_HMAC_NAME;

    public List<ToolDefinition> listTools() {
        return mcpServerProvider.getTools();
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
        if (Math.abs(System.currentTimeMillis() / 1000 - ts) > TimeUnit.MINUTES.toSeconds(expireWindowMinutes)) {
            throw new IllegalArgumentException("request timestamp too old!");
        }
        String nonce = headers.getFirstHeader(HttpSimpleMcpConstants.HEADER_APP_NONCE);

        // 根据是否初始化配置了缓存次，决定nonce是否进行检查
        String nonceKey = "mcp:nonce:" + appId + ":" + nonce;
        if (expireCache != null) {
            Object exVal = expireCache.get(nonceKey);
            if (exVal != null) {
                throw new IllegalArgumentException("request nonce was invalid!");
            }
        }

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
        McpCallPayloadDto payloadDto = request.getPayloadDto();
        if (payloadDto != null) {
            if (payloadDto.getContent() != null && !payloadDto.getContent().isEmpty()) {
                payload += "#" + payloadDto.getContent();
            }
            if (payloadDto.getContext() != null && !payloadDto.getContext().isEmpty()) {
                payload += "#" + payloadDto.getContext();
            }
        }

        mac.update(payload.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = mac.doFinal();
        String calcSign = Base64.getEncoder().encodeToString(bytes);
        if (!calcSign.equalsIgnoreCase(sign)) {
            throw new IllegalArgumentException("sign verify failure!");
        }

        // 验签通过在存入nonce，避免网络波动的情况下，误杀正常请求
        if (expireCache != null) {
            expireCache.set(nonceKey, nonce, expireWindowMinutes * 2, TimeUnit.MINUTES);
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
            assertValidMcpRequest(mcpRequest);
            Object obj = mcpServerProvider.callTool(request);
            return ApiResp.success(obj);
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiResp.error(e.getMessage());
        }
    }
}
