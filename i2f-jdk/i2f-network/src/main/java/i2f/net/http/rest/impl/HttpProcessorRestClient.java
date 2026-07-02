package i2f.net.http.rest.impl;

import i2f.net.http.consts.CharsetConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/24 22:24
 * @desc
 */
@Data
@NoArgsConstructor
public class HttpProcessorRestClient implements IRestClient {
    protected IHttpProcessor httpProcessor = new HttpUrlConnectProcessor();
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    @Override
    public <T> RestHttpResponse<T> rest(RestHttpRequest request, Class<T> responseType) throws IOException {
        HttpRequest req = new HttpRequest().toBuilder()
                .set(u -> u::setUrl, request.getUrl())
                .set(u -> u::setMethod, request.getMethod())
                .set(u -> u::setParams, request.getParams())
                .set(u -> u::setHeader, request.getHeaders())
                .set(u -> u::setData, request.getBody())
                .set(u -> u::json)
                .with2(u -> u::addHeader, HttpHeaderConstants.ContentEncoding, CharsetConstants.Utf8)
                .build();

        RestHttpResponse<T> ret = httpProcessor.http(req, response -> {
            try (HttpResponse resp = response) {
                T obj = resp.getContentAsObject(jsonSerializer, responseType, CharsetConstants.Utf8);
                return new RestHttpResponse<T>().toBuilder()
                        .set(u -> u::setStatusCode, resp.getStatusCode())
                        .set(RestHttpResponse::setBody, obj)
                        .set(u -> u::setStatusMessage, resp.getStatusMessage())
                        .set(u -> u::setHeaders, resp.getHeader())
                        .set(u -> u::setBody, obj)
                        .build();
            }
        });


        return ret;
    }
}
