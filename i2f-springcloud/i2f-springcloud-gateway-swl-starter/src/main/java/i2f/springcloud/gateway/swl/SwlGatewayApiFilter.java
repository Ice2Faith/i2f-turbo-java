package i2f.springcloud.gateway.swl;

import i2f.serialize.str.json.IJsonSerializer;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.SwlTransfer;
import i2f.swl.exception.SwlException;
import i2f.web.swl.filter.SwlWebConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2024/7/13 14:39
 * @desc
 */
@ConditionalOnExpression("${i2f.swl.api.enable:true}")
@AutoConfigureAfter(SwlGatewayAutoConfiguration.class)
@Slf4j
@Data
public class SwlGatewayApiFilter implements GlobalFilter, Ordered {

    public static final String KEY_PATH = "/swl/key";
    public static final String KEY_SWAP_PATH = "/swl/swapKey";

    @Autowired
    protected SwlTransfer transfer;

    @Autowired
    protected SwlWebConfig config;

    @Autowired
    protected IJsonSerializer jsonSerializer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = SwlGatewayFilter.getTrimContextPathRequestUri(request);
        String method = String.valueOf(request.getMethod());

        if (!"POST".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        if (KEY_PATH.equals(path)) {
            String selfSwapKey = transfer.getSelfSwapKey();
            selfSwapKey=jsonSerializer.serialize(selfSwapKey);
            byte[] bytes = null;
            try {
                bytes = selfSwapKey.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new SwlException(SwlCode.INTERNAL_EXCEPTION.code(), e.getMessage(), e);
            }
            return responseContent(response, 200, "application/json;charset=UTF-8", bytes);
        } else if (KEY_SWAP_PATH.equals(path)) {
            // 获取请求体
            return request.getBody()
                    .defaultIfEmpty(new DefaultDataBufferFactory().allocateBuffer(0))
                    .collectList()
                    .map(list -> list.get(0)
                            .factory()
                            .join(list)
                    )
                    .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release)
                    .flatMap(dataBuffer -> {
                        // 获取请求体
                        byte[] dataBytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(dataBytes);
                        DataBufferUtils.release(dataBuffer);

                        MediaType mediaType = request.getHeaders().getContentType();
                        Charset charset = mediaType.getCharset();
                        if (charset == null) {
                            charset = StandardCharsets.UTF_8;
                        }

                        String clientSwapKey=new String(dataBytes,charset);

                        try {
                            clientSwapKey = (String)jsonSerializer.deserialize(clientSwapKey);
                        } catch (Exception e) {
                        }

                        transfer.acceptOtherSwapKey(clientSwapKey);

                        String selfSwapKey = transfer.getSelfSwapKey();
                        selfSwapKey=jsonSerializer.serialize(selfSwapKey);

                        byte[] bytes = null;
                        try {
                            bytes = selfSwapKey.getBytes("UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            throw new SwlException(SwlCode.INTERNAL_EXCEPTION.code(), e.getMessage(), e);
                        }
                        return responseContent(response, 200, "application/json;charset=UTF-8", bytes);
                    });
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -999;
    }

    public Mono<Void> responseContent(ServerHttpResponse response, int httpCode, String contentType, byte[] data) {
        response.setRawStatusCode(httpCode);
        DataBuffer buffer = response.bufferFactory().wrap(data);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        return response.writeWith(Mono.just(buffer));
    }
}
