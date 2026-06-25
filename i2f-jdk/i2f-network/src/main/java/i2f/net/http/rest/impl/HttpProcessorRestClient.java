package i2f.net.http.rest.impl;

import i2f.net.http.consts.CharsetConstants;
import i2f.net.http.consts.HttpHeaderConstants;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.net.http.rest.IRestClient;
import i2f.net.http.rest.data.RestHttpRequest;
import i2f.net.http.rest.data.RestHttpResponse;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/24 22:24
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class HttpProcessorRestClient implements IRestClient {
    protected IHttpProcessor httpProcessor;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();

    @Override
    public <T> RestHttpResponse<T> rest(RestHttpRequest request, Class<T> responseType) throws IOException {
        HttpRequest req = new HttpRequest();
        req.setUrl(request.getUrl());
        req.setMethod(request.getMethod());
        req.setParams(request.getParams());
        req.setHeader(request.getHeaders());
        req.setData(request.getBody());
        req.json(jsonSerializer);
        req.addHeader(HttpHeaderConstants.ContentEncoding, CharsetConstants.Utf8);

        HttpResponse resp = httpProcessor.http(req);

        T obj = resp.getContentAsObject(jsonSerializer, responseType, CharsetConstants.Utf8);
        return (RestHttpResponse<T>) RestHttpResponse.builder()
                .statusCode(resp.getStatusCode())
                .statusMessage(resp.getStatusMessage())
                .headers(resp.getHeader())
                .body(obj)
                .build();
    }
}
