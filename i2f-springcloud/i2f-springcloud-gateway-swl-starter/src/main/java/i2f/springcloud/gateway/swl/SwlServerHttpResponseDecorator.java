package i2f.springcloud.gateway.swl;

import i2f.swl.consts.SwlCode;
import i2f.swl.data.SwlData;
import i2f.swl.exception.SwlException;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/12 16:56
 * @desc
 */
public class SwlServerHttpResponseDecorator extends ServerHttpResponseDecorator {
    public SwlServerHttpResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (!(body instanceof Flux)) {
            return super.writeWith(body);
        }

        Flux<DataBuffer> fluxBody = (Flux<DataBuffer>) body;
        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
            ServerHttpResponse delegateResponse = getDelegate();


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
            MediaType responseMediaType = MediaType.parseMediaType("text/plain;charset=" + responseCharset);


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
                return bufferFactory().wrap(responseBody);
            }


            String responseText = null;
            try {
                responseText = new String(responseBody, responseCharset);
            } catch (UnsupportedEncodingException e) {
                throw new SwlException(SwlCode.INTERNAL_EXCEPTION.code(), e.getMessage(), e);
            }

//                                    Object responseString = request.getAttribute(SwlWebConsts.SWL_STRING_RESPONSE);
//                                    if(Boolean.TRUE.equals(responseString)){
//                                        responseText=jsonSerializer.serialize(responseText);
//                                    }


            List<String> attachedHeaders = new ArrayList<>();
            if (config.getAttachedHeaderNames() != null) {
                for (String headerName : config.getAttachedHeaderNames()) {
                    String header = delegateResponse.getHeaders().getFirst(headerName);
                    attachedHeaders.add(header);
                }
            }


            List<String> parts = new ArrayList<>();
            parts.add(responseText);

            SwlData responseData = transfer.response(finalClientAsymSign, parts);
            String responseSwlh = serializeHeader(responseData.getHeader());
            delegateResponse.getHeaders().set(config.getHeaderName(), responseSwlh);
            delegateResponse.getHeaders().set(config.getRemoteAsymSignHeaderName(), responseData.getContext().getLocalAsymSign());

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
            if (finalServerAsymSign != null) {
                if (!selfAsymSign.equalsIgnoreCase(finalServerAsymSign)) {
                    delegateResponse.getHeaders().set(config.getCurrentAsymKeyHeaderName(), transfer.obfuscateEncode(selfPublicKey));
                }
            }
            delegateResponse.getHeaders().setContentType(responseMediaType);

            // 响应数据
            delegateResponse.getHeaders().setContentLength(responseBody.length);
            return bufferFactory().wrap(responseBody);
        }));
    }
}
