package i2f.springcloud.gateway.swl;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.data.SwlDto;
import i2f.swl.exception.SwlException;
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
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

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

    public static final String KEY_SWAP_PATH = "/swl/swapKey";

    @Autowired
    protected SwlTransfer transfer;

    @Autowired
    protected SwlWebConfigProperties config;

    @Autowired
    protected IJsonSerializer jsonSerializer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!config.isEnable()){
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = SwlGatewayFilter.getTrimContextPathRequestUri(request);
        String method = String.valueOf(request.getMethod());

        if (!"POST".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        String apiKeyPath= config.getApiSwapKeyPath();
        if(apiKeyPath==null || apiKeyPath.isEmpty()){
            apiKeyPath=KEY_SWAP_PATH;
        }

        if (apiKeyPath.equals(path)) {
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

                        String reqJson = new String(dataBytes, charset);
                        SwlData reqHandleShake = null;
                        try {
                            SwlDto dto = (SwlDto) jsonSerializer.deserialize(reqJson, SwlDto.class);
                            String reqPayload = dto.getPayload();
                            reqJson = new String(Base64StringByteCodec.INSTANCE.decode(reqPayload),"UTF-8");
                            reqHandleShake = (SwlData)jsonSerializer.deserialize(reqJson, SwlData.class);
                        } catch (Exception e) {
                        }

                        AsymKeyPair swapKeyPair = transfer.getSelfSwapKey();

                        // ************************服务端接收握手并响应*******************************
                        String obfuscateClientPublicKey = reqHandleShake.getAttaches().get(0);
                        String clientPublicKey = transfer.obfuscateDecode(obfuscateClientPublicKey);

                        SwlData recvReqHandleShake = transfer.receiveByRaw("swap", reqHandleShake,
                                clientPublicKey,
                                swapKeyPair.getPrivateKey());

                        String serverCertId = transfer.acceptOtherSwapKey(obfuscateClientPublicKey);
                        String selfPublicKey = transfer.getSelfPublicKey(serverCertId);

                        SwlData respHandleShake = transfer.sendByRaw(serverCertId,
                                clientPublicKey,
                                swapKeyPair.getPrivateKey(),
                                new ArrayList<>(Collections.singletonList(selfPublicKey))
                        );
                        respHandleShake.setContext(null);


                        byte[] bytes = null;
                        try {
                            SwlDto ret=new SwlDto();
                            String retJson=jsonSerializer.serialize(respHandleShake);
                            String retPayload=Base64StringByteCodec.INSTANCE.encode(retJson.getBytes("UTF-8"));
                            ret.setPayload(retPayload);

                            String respJson = jsonSerializer.serialize(ret);

                            bytes = respJson.getBytes("UTF-8");
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
