package i2f.ai.rest.mcp.client;

import i2f.ai.rest.mcp.HttpSimpleMcpConstants;
import i2f.ai.rest.mcp.client.data.SimpleMcpToolDefinition;
import i2f.ai.rest.mcp.client.data.SimpleMcpToolListRespDto;
import i2f.ai.std.mcp.McpToolProvider;
import i2f.ai.std.tool.ToolBaseCallRequest;
import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.mutator.BaseMutator;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpHeaders;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.net.http.rest.impl.HttpProcessorRestClient;
import i2f.resp.ApiCode;
import i2f.resp.ApiResp;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/7/13 10:23
 * @desc
 */
public class HttpSimpleMcpClientToolProvider implements McpToolProvider, BaseMutator<HttpSimpleMcpClientToolProvider> {
    protected IRestClient restClient = new HttpProcessorRestClient();
    protected String baseUrl;

    protected String appId;
    protected String appKey;
    protected String hmacName = HttpSimpleMcpConstants.DEFAULT_HMAC_NAME;

    protected String name;
    protected String description;

    protected long expireTtl = TimeUnit.SECONDS.toMillis(15);
    protected final CopyOnWriteArrayList<ToolDefinition> cache = new CopyOnWriteArrayList<>();
    protected final AtomicLong expireTs = new AtomicLong(0);
    protected final AtomicBoolean hasCache = new AtomicBoolean(false);
    protected final ReentrantLock lock = new ReentrantLock();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String mergeUrl(String baseUrl, String subPath) {
        if (baseUrl.endsWith("/")) {
            if (subPath.startsWith("/")) {
                return baseUrl + subPath.substring(1);
            } else {
                return baseUrl + subPath;
            }
        } else {
            if (subPath.startsWith("/")) {
                return baseUrl + subPath;
            } else {
                return baseUrl + "/" + subPath;
            }
        }
    }

    protected void applyHeader(HttpHeaders headers) {
        try {
            headers.add(HttpSimpleMcpConstants.HEADER_APP_ID, appId);

            String timestamp = Long.toString(System.currentTimeMillis() / 1000, 16);
            headers.add(HttpSimpleMcpConstants.HEADER_APP_DATE, timestamp);

            String nonce = UUID.randomUUID().toString().replace("-", "");
            headers.add(HttpSimpleMcpConstants.HEADER_APP_NONCE, nonce);

            SecretKey skey = new SecretKeySpec(appKey.getBytes(StandardCharsets.UTF_8), hmacName);
            Mac mac = Mac.getInstance(hmacName);
            mac.init(skey);
            String payload = appId + "#" + timestamp + "#" + nonce;
            mac.update(payload.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = mac.doFinal();
            String sign = Base64.getEncoder().encodeToString(bytes);
            headers.add(HttpSimpleMcpConstants.HEADER_APP_SIGN, sign);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<ToolDefinition> getTools() {
        if (hasCache.get() && System.currentTimeMillis() - expireTs.get() < expireTtl) {
            return new ArrayList<>(cache);
        }
        lock.lock();
        try {
            RestHttpResponse<SimpleMcpToolListRespDto> resp = restClient.rest(new RestHttpRequest().toMutator()
                    .set(u -> u::setUrl, mergeUrl(baseUrl, HttpSimpleMcpConstants.URL_PATH_GET_TOOLS))
                    .set(u -> u::setMethod, HttpMethodConstants.GET)
                    .set(u -> u::setHeaders, HttpHeaders.create()
                            .apply(this::applyHeader)
                    )
                    .done(), SimpleMcpToolListRespDto.class);
            SimpleMcpToolListRespDto dto = resp.getBody();
            if (dto.getCode() != ApiCode.SUCCESS) {
                throw new IllegalStateException("get tools error: " + dto.getMsg());
            }
            List<SimpleMcpToolDefinition> list = dto.getData();

            cache.clear();
            cache.addAll(list);
            expireTs.set(System.currentTimeMillis() + expireTtl);
            hasCache.set(true);

            return new ArrayList<>(list);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
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
    public Object callTool(ToolBaseCallRequest request) throws Throwable {
        try {
            RestHttpResponse<ApiResp> resp = restClient.rest(new RestHttpRequest().toMutator()
                    .set(u -> u::setUrl, mergeUrl(baseUrl, HttpSimpleMcpConstants.URL_PATH_CALL_TOOL))
                    .set(u -> u::setMethod, HttpMethodConstants.POST)
                    .set(u -> u::setHeaders, HttpHeaders.create()
                            .apply(this::applyHeader)
                    )
                    .set(u -> u::setBody, new ToolBaseCallRequest().toMutator()
                            .set(e -> e::setId, request.getId())
                            .set(e -> e::setName, request.getName())
                            .set(e -> e::setArguments, request.getArguments())
                            .done())
                    .done(), ApiResp.class);
            ApiResp<?> dto = resp.getBody();
            if (dto.getCode() != ApiCode.SUCCESS) {
                throw new IllegalStateException("call tools error: " + dto.getMsg());
            }
            return dto.getData();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
