package i2f.springcloud.gateway.filters.global.repeat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.*;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/4/1 10:05
 * @desc
 */
@ConditionalOnExpression("${i2f.springcloud.gateway.filters.query-repeat.enable:false}")
@Slf4j
@Data
@EnableConfigurationProperties(RequestQueryRepeatProperties.class)
public class RequestQueryRepeatFilter implements GlobalFilter, Ordered {

    @Autowired
    private RequestQueryRepeatProperties config;

    private AntPathMatcher matcher = new AntPathMatcher("/");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getPath().subPath(config.getPrefixCount() * 2).toString();

        return removeRepeatChainFilter(exchange, chain, path);
    }

    private Mono<Void> removeRepeatChainFilter(ServerWebExchange exchange, GatewayFilterChain chain, String path) {

        ServerHttpRequest request = exchange.getRequest();
        MediaType contentType = request.getHeaders().getContentType();
        boolean isTarget = false;
        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
            if (config.isEnableRepeatForm()) {
                isTarget = true;
            }
        } else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
            if (config.isEnableRepeatJson()) {
                isTarget = true;
            }
        } else if (MediaType.TEXT_XML.isCompatibleWith(contentType)) {
            if (config.isEnableRepeatXml()) {
                isTarget = true;
            }
        }

        if (!isTarget) {
            return chain.filter(exchange);
        }

        List<String> repeatListPathExcludePattens = config.getRepeatListPathExcludePattens();
        if (repeatListPathExcludePattens != null) {
            for (String patten : repeatListPathExcludePattens) {
                if (matcher.match(patten, path)) {
                    return chain.filter(exchange);
                }
            }
        }

        List<String> repeatListPathPattens = config.getRepeatListPathPattens();
        if (repeatListPathPattens != null) {
            boolean isMatch = false;
            for (String patten : repeatListPathPattens) {
                if (matcher.match(patten, path)) {
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                return chain.filter(exchange);
            }
        }


        List<String> repeatProps = config.getRepeatProps();
        if (repeatProps == null || repeatProps.isEmpty()) {
            return chain.filter(exchange);
        }

        Set<String> repeatSet = new HashSet<>(repeatProps);

        Set<String> removeProps = new HashSet<>();

        for (String key : request.getQueryParams().keySet()) {
            if (repeatSet.contains(key)) {
                removeProps.add(key);
            }
        }

        if (removeProps.isEmpty()) {
            return chain.filter(exchange);
        }


        log.info("remove props : " + removeProps);

        DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
        DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(0);
        Mono<DataBuffer> dataBufferMono = Flux.from(exchange.getRequest().getBody().defaultIfEmpty(defaultDataBuffer))
                .collectList()
                .map(list -> list.get(0).factory().join(list))
                .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
        return dataBufferMono
                .flatMap(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);

                    Charset charset = contentType.getCharset();
                    if (charset == null) {
                        charset = StandardCharsets.UTF_8;
                    }
                    String str = new String(bytes, charset);
                    str = str.replaceAll("\\s+", "");
                    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(request.getQueryParams());
                    URI uri = request.getURI();
                    for (String prop : removeProps) {
                        boolean isRemove = false;
                        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(contentType)) {
                            if (config.isEnableRepeatForm()) {
                                if (str.contains("&" + prop + "=") || str.startsWith(prop + "=")) {
                                    isRemove = true;
                                }
                            }
                        } else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
                            if (config.isEnableRepeatJson()) {
                                if (str.contains("\"" + prop + "\":") || str.contains(prop + ":")) {
                                    isRemove = true;
                                }
                            }
                        } else if (MediaType.TEXT_XML.isCompatibleWith(contentType)) {
                            if (config.isEnableRepeatXml()) {
                                if (str.contains("<" + prop + ">")) {
                                    isRemove = true;
                                }
                            }
                        }
                        if (isRemove) {
                            queryParams.remove(prop);
                        }
                    }


                    String uriStr = uri.toString();
                    int idx = uriStr.indexOf("?");
                    if (idx >= 0) {
                        uriStr = uriStr.substring(0, idx);
                    }
                    URI mutatedUri = UriComponentsBuilder.fromUriString(uriStr).queryParams(queryParams).build().toUri();
                    ;
                    ServerHttpRequestDecorator mutatedRequest = new ServerHttpRequestDecorator(request) {
                        @Override
                        public MultiValueMap<String, String> getQueryParams() {
                            return queryParams;
                        }

                        @Override
                        public URI getURI() { // 覆盖getQueryParams方法不生效，需要覆盖URI
                            return mutatedUri;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            return Flux.defer(() -> {
                                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                                DataBufferUtils.retain(buffer);
                                return Mono.just(buffer);
                            });
                        }
                    };

                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                    return chain.filter(mutatedExchange);

                });
    }

    @Override
    public int getOrder() {
        return config.getOrder();
    }

}
