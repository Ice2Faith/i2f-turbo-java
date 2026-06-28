package i2f.extension.okhttp.impl;


import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpRequest;
import i2f.serialize.std.str.json.IJsonSerializer;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class OkHttpJsonRequestBodyHandler implements IOkHttpHttpRequestBodyHandler {
    private IJsonSerializer jsonProcessor;

    public OkHttpJsonRequestBodyHandler(IJsonSerializer jsonProcessor) {
        this.jsonProcessor = jsonProcessor;
    }

    @Override
    public void writeBody(Object data, HttpRequest request, Request.Builder output, Object... args) throws IOException {
        if (data == null) {
            return;
        }
        if (data instanceof byte[]) {
            new OkHttpRawBytesRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        if (data instanceof InputStream) {
            new OkHttpRawInputStreamRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        Request.Builder builder = output;

        String content = jsonProcessor.serialize(data);
        RequestBody body = RequestBody.create(content, MediaType.parse(request.getHeader().getContentType()));

        String method = request.getMethod().toUpperCase();
        if (HttpMethodConstants.POST.equals(method)) {
            builder.post(body);
        } else if (HttpMethodConstants.PUT.equals(method)) {
            builder.put(body);
        }
    }
}
