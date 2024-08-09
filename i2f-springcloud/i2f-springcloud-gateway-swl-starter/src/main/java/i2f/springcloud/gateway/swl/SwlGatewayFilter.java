package i2f.springcloud.gateway.swl;

import i2f.codec.bytes.base64.Base64StringByteCodec;
import i2f.codec.bytes.charset.CharsetStringByteCodec;
import i2f.serialize.str.json.IJsonSerializer;
import i2f.spring.matcher.MatcherUtil;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.SwlTransfer;
import i2f.swl.data.SwlData;
import i2f.swl.data.SwlHeader;
import i2f.swl.exception.SwlException;
import i2f.url.FormUrlEncodedEncoder;
import i2f.web.swl.filter.SwlWebConfig;
import i2f.web.swl.filter.SwlWebConsts;
import i2f.web.swl.filter.SwlWebCtrl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2024/7/12 15:06
 * @desc
 */
@AutoConfigureAfter(SwlGatewayAutoConfiguration.class)
@Slf4j
@Data
public class SwlGatewayFilter implements GlobalFilter, Ordered {

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


        String method = String.valueOf(request.getMethod());
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }

        if ("TRACE".equalsIgnoreCase(method)) {
            return chain.filter(exchange);
        }


        SwlWebCtrl ctrl = parseCtrl(request, this.config);

        MediaType mediaType = request.getHeaders().getContentType();
        String contentType = null;
        if (mediaType != null) {
            contentType = mediaType.toString();
        }

        // 跳过multipart请求
        if (contentType == null) {
            contentType = "";
        }
        contentType = contentType.toLowerCase();

        if (contentType.contains("multipart/form-data")) {
            ctrl.setIn(false);
        }


        // 获取安全头，优先从请求头获取，获取不到则从请求参数获取
        AtomicReference<String> swlh=new AtomicReference<>(null);
        swlh.set(request.getHeaders().getFirst(config.getHeaderName()));
        if (swlh.get() == null || swlh.get().isEmpty()) {
            swlh.set(request.getQueryParams().getFirst(config.getHeaderName()));
        }

        // 如果带有安全头，则强制为输入需要解密
        if (swlh.get() != null && !swlh.get().isEmpty()) {
            ctrl.setIn(true);
        }

        // 没有输入输出控制，直接跳过
        if (!ctrl.isIn() && !ctrl.isOut()) {
            return chain.filter(exchange);
        }


        // 获取客户端IP，用以客户端隔离
        AtomicReference<String> clientIp = new AtomicReference<>(getIp(request));
        if (clientIp.get() != null) {
            clientIp.set(clientIp.get().replaceAll(":", "-"));
        }

        // 获取原始请求的非对称秘钥签名
        // 用于响应时使用，以配对请求
        AtomicReference<String> clientAsymSign = new AtomicReference<>(request.getHeaders().getFirst(config.getRemoteAsymSignHeaderName()));
        AtomicReference<String> serverAsymSign = new AtomicReference<>(null);

        ServerHttpResponse nextResponse = getNextResponse(response, ctrl, clientAsymSign, serverAsymSign);

        if (ctrl.isIn()) {

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

                        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>(request.getHeaders());


                        // 请求参数
                        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>(request.getQueryParams());

                        // 获取加密后的请求参数
                        String swlp = queryParams.getFirst(config.getParameterName());
                        queryParams.remove(config.getParameterName());


                        byte[] body = dataBytes;

                        Charset charset = mediaType.getCharset();
                        if (charset == null) {
                            charset = StandardCharsets.UTF_8;
                        }

                        // 转换请求体为字符串
                        String srcText = null;
                        if (body.length > 0) {
                            srcText = new String(body, charset);
                        }

                        // 如果文本是引号开头，则是被转义为了JSON
                        // 需要进行反序列化字符串
                        if (srcText != null) {
                            srcText = srcText.trim();
                            if (srcText.startsWith("\"")) {
                                srcText = (String) jsonSerializer.deserialize(srcText, String.class);
                            }
                        }

                        List<String> attachedHeaders = new ArrayList<>();
                        if (this.config.getAttachedHeaderNames() != null) {
                            for (String headerName : this.config.getAttachedHeaderNames()) {
                                String header = request.getHeaders().getFirst(headerName);
                                attachedHeaders.add(header);
                            }
                        }

                        // 构造请求数据
                        SwlData data = new SwlData();
                        data.setHeader(deserializeHeader(swlh.get()));
                        data.setParts(Arrays.asList(srcText, swlp));
                        data.setAttaches(attachedHeaders);


                        // 接受数据
                        SwlData receiveData = transfer.receive(clientIp.get(), data);

                        // 保存请求时的非对称公钥签名
                        // 此时还没有进行receive，因此local还是客户端的值，remote是服务端的值
                        clientAsymSign.set(receiveData.getHeader().getRemoteAsymSign());
                        serverAsymSign.set(receiveData.getHeader().getLocalAsymSign());


                        // 获取解密后的数据
                        srcText = receiveData.getParts().get(0);
                        swlp = receiveData.getParts().get(1);

                        // 重新设置请求参数
                        String replaceQueryString = null;
                        Map<String, List<String>> replaceParameterMap = null;
                        if (swlp != null) {
                            replaceQueryString = swlp;
                            replaceParameterMap = FormUrlEncodedEncoder.toMap(swlp);
                        }

                        // 重新构造请求
                        if (srcText != null) {
                            body = srcText.getBytes(charset);
                        }

                        // 覆盖请求参数
                        if(replaceParameterMap!=null) {
                            queryParams.putAll(replaceParameterMap);
                        }

                        // 覆盖真实请求内容类型
                        String realContentType = request.getHeaders().getFirst(config.getRealContentTypeHeaderName());
                        if (realContentType != null && !realContentType.isEmpty()) {
                            headers.set("Content-Type", realContentType);
                        }

                        // 替换请求为包装请求
                        URI uri = request.getURI();
                        String uriStr = uri.toString();
                        int idx = uriStr.indexOf("?");
                        if (idx >= 0) {
                            uriStr = uriStr.substring(0, idx);
                        }

                        URI mutatedUri = UriComponentsBuilder.fromUriString(uriStr).queryParams(queryParams).build().toUri();


                        byte[] mutatedBody = body;
                        ServerHttpRequest nextRequest = new ServerHttpRequestDecorator(request) {

                            @Override
                            public MultiValueMap<String, String> getQueryParams() {
                                return queryParams;
                            }

                            @Override
                            public URI getURI() { // 覆盖getQueryParams方法不生效，需要覆盖URI
                                return mutatedUri;
                            }

                            @Override
                            public HttpHeaders getHeaders() {
                                return new HttpHeaders(headers);
                            }

                            @Override
                            public Flux<DataBuffer> getBody() {
                                return Flux.defer(() -> {
                                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(mutatedBody);
                                    DataBufferUtils.retain(buffer);
                                    return Mono.just(buffer);
                                });
                            }
                        };

                        // 进行下游处理
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(nextRequest)
                                .response(nextResponse)
                                .build();

                        return chain.filter(mutatedExchange);
                    });

        }





        // 进行下游处理
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request)
                .response(nextResponse)
                .build();

        return chain.filter(mutatedExchange);
    }

    public ServerHttpResponse getNextResponse(ServerHttpResponse response,
                                              SwlWebCtrl ctrl,
                                              AtomicReference<String> clientAsymSign,
                                              AtomicReference<String> serverAsymSign
                                              ){
        if (ctrl.isOut()) {
            return wrapperServerHttpResponse(response, clientAsymSign, serverAsymSign);
        }

        return response;
    }


    public ServerHttpResponse wrapperServerHttpResponse(ServerHttpResponse response,
                                                        AtomicReference<String> clientAsymSign,
                                                        AtomicReference<String> serverAsymSign) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                ServerHttpResponse delegateResponse = getDelegate();

                // 设置暴露响应头，以支持前端获取这些值
                applyExposeHeader(delegateResponse);

                if (!(body instanceof Flux)) {
                    return super.writeWith(body);
                }

                // 处理直接下载型接口，推荐还是使用白名单
                // 小文件可以在这里进行自动处理
                // 如果是大文件下载，这里可是内存包装
                // 使用白名单，避免OOM
                boolean specificResponseBody = false;
                Collection<String> headerNames = delegateResponse.getHeaders().keySet();
                for (String item : headerNames) {
                    if (item == null || item.isEmpty()) {
                        continue;
                    }
                    String str = item.toLowerCase().trim();
                    if ("content-disposition".equals(str)) {
                        String header = delegateResponse.getHeaders().getFirst(item);
                        if (header != null && !header.isEmpty()) {
                            specificResponseBody = true;
                            break;
                        }
                    }
                }

                if (specificResponseBody) {
                    return super.writeWith(body);
                }

                Flux<DataBuffer> fluxBody = (Flux<DataBuffer>) body;
                return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                    DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                    DataBuffer join = dataBufferFactory.join(dataBuffers);
                    byte[] responseBody = new byte[join.readableByteCount()];
                    join.read(responseBody);
                    // 释放掉内存
                    DataBufferUtils.release(join);

                    String responseCharset = config.getResponseCharset();
                    if (responseCharset == null || responseCharset.isEmpty()) {
                        MediaType responseMediaType = delegateResponse.getHeaders().getContentType();
                        if (responseMediaType != null) {
                            Charset responseMediaCharset = responseMediaType.getCharset();
                            if (responseMediaCharset != null) {
                                responseCharset = responseMediaCharset.name();
                            }
                        }
                    }
                    if (responseCharset == null || responseCharset.isEmpty()) {
                        responseCharset = "UTF-8";
                    }


                    String responseText = null;
                    try {
                        responseText = new String(responseBody, responseCharset);
                    } catch (UnsupportedEncodingException e) {
                        throw new SwlException(SwlCode.INTERNAL_EXCEPTION.code(), e.getMessage(), e);
                    }

//                    Object responseString = request.getAttribute(SwlWebConsts.SWL_STRING_RESPONSE);
//                    if (Boolean.TRUE.equals(responseString)) {
//                        responseText = jsonSerializer.serialize(responseText);
//                    }


                    List<String> attachedHeaders = new ArrayList<>();
                    if (config.getAttachedHeaderNames() != null) {
                        for (String headerName : config.getAttachedHeaderNames()) {
                            String header = delegateResponse.getHeaders().getFirst(headerName);
                            attachedHeaders.add(header);
                        }
                    }


                    List<String> parts = new ArrayList<>();
                    parts.add(responseText);

                    SwlData responseData = transfer.response(clientAsymSign.get(), parts);
                    String responseSwlh = serializeHeader(responseData.getHeader());
                    delegateResponse.getHeaders().set(config.getHeaderName(), responseSwlh);
                    delegateResponse.getHeaders().set(config.getRemoteAsymSignHeaderName(), responseData.getContext().getSelfAsymSign());

                    responseText = responseData.getParts().get(0);
                    try {
                        responseBody = responseText.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new SwlException(SwlCode.INTERNAL_EXCEPTION.code(), e.getMessage(), e);
                    }

                    MediaType responseContentType = delegateResponse.getHeaders().getContentType();
                    if (responseContentType == null) {
                        responseContentType = MediaType.APPLICATION_JSON;
                    }
                    delegateResponse.getHeaders().set(config.getRealContentTypeHeaderName(), responseContentType.toString());

                    String selfPublicKey = transfer.getSelfPublicKey();
                    String selfAsymSign = transfer.calcKeySign(selfPublicKey);
                    if (serverAsymSign.get() != null) {
                        if (!selfAsymSign.equalsIgnoreCase(serverAsymSign.get())) {
                            delegateResponse.getHeaders().set(config.getCurrentAsymKeyHeaderName(), transfer.obfuscateEncode(selfPublicKey));
                        }
                    }

                    MediaType responseMediaType = MediaType.parseMediaType("text/plain;charset=" + responseCharset);
                    delegateResponse.getHeaders().setContentType(responseMediaType);

                    // 响应数据
                    delegateResponse.getHeaders().setContentLength(responseBody.length);
                    return bufferFactory().wrap(responseBody);
                }));
            }
        };
    }

    @Override
    public int getOrder() {
        return -100;
    }


    public SwlHeader deserializeHeader(String str) {
        str = transfer.obfuscateDecode(str);
        str = CharsetStringByteCodec.UTF8.encode(Base64StringByteCodec.INSTANCE.decode(str));
        SwlHeader ret = FormUrlEncodedEncoder.ofFormBean(str, SwlHeader.class);
        return ret;
    }

    public String serializeHeader(SwlHeader header) {
        String ret = FormUrlEncodedEncoder.toForm(header);
        ret = Base64StringByteCodec.INSTANCE.encode(CharsetStringByteCodec.UTF8.decode(ret));
        ret = transfer.obfuscateEncode(ret);
        return ret;
    }

    public void applyExposeHeader(ServerHttpResponse response) {
        // 将随机Asym加密模糊之后的Symm秘钥放入响应头，并设置可访问权限
        Collection<String> oldHeaders = response.getHeaders().get(SwlWebConsts.ACCESS_CONTROL_EXPOSE_HEADERS);
        Set<String> headers = new LinkedHashSet<>();
        if(oldHeaders!=null) {
            for (String header : oldHeaders) {
                String[] arr = header.split(",");
                for (String item : arr) {
                    String str = item.trim();
                    if (!str.isEmpty()) {
                        headers.add(str);
                    }
                }
            }
        }
        headers.add(config.getHeaderName());
        headers.add(config.getRemoteAsymSignHeaderName());
        headers.add(config.getCurrentAsymKeyHeaderName());
        headers.add(config.getRealContentTypeHeaderName());
        response.getHeaders().set(SwlWebConsts.ACCESS_CONTROL_EXPOSE_HEADERS, toCommaDelimitedString(headers));
    }


    public String toCommaDelimitedString(Collection<String> headerValues) {
        StringJoiner joiner = new StringJoiner(", ");
        Iterator<String> iterator = headerValues.iterator();

        while (iterator.hasNext()) {
            String val = iterator.next();
            if (val != null && !"".equals(val)) {
                joiner.add(val);
            }
        }

        return joiner.toString();
    }


    public static String getIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = "127.0.0.1";
            InetSocketAddress remoteAddress = request.getRemoteAddress();
            if (remoteAddress != null) {
                InetAddress addr = remoteAddress.getAddress();
                if (addr != null) {
                    ip = addr.getHostAddress();
                }
            }
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }
        return ip;
    }


    public static SwlWebCtrl parseCtrl(ServerHttpRequest request, SwlWebConfig config) {
        SwlWebCtrl defaultCtrl = config.getDefaultCtrl();

        MediaType contentType = request.getHeaders().getContentType();
        if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
            return new SwlWebCtrl(false, defaultCtrl.isOut());
        }

        String path = getTrimContextPathRequestUri(request);

        Boolean in = null;
        Boolean out = null;
        List<String> whiteListIn = config.getWhiteListIn();
        if (whiteListIn != null) {
            if (MatcherUtil.antUrlMatchedAny(path, whiteListIn)) {
                in = false;
            }
        }
        List<String> whiteListOut = config.getWhiteListOut();
        if (whiteListOut != null) {
            if (MatcherUtil.antUrlMatchedAny(path, whiteListOut)) {
                out = false;
            }
        }

        return new SwlWebCtrl(in == null ? defaultCtrl.isIn() : in,
                out == null ? defaultCtrl.isOut() : out);
    }


    public static String getTrimContextPathRequestUri(ServerHttpRequest request) {
        String requestUrl = request.getPath().toString();
        String contextPath = request.getPath().contextPath().toString();
        if (StringUtils.isEmpty(contextPath)) {
            contextPath = "/";
        }
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        if (!contextPath.endsWith("/")) {
            contextPath = contextPath + "/";
        }
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        if (requestUrl.startsWith(contextPath)) {
            requestUrl = requestUrl.substring(contextPath.length());
        } else {
            String tmp = requestUrl + "/";
            if (contextPath.equals(tmp)) {
                requestUrl = "/";
            }
        }
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        return requestUrl;
    }
}
