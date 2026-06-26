package i2f.extension.okhttp.impl;


import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpRequest;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class OkHttpRawBytesRequestBodyHandler implements IOkHttpHttpRequestBodyHandler {

    @Override
    public void writeBody(Object data, HttpRequest request, Request.Builder output, Object... args) throws IOException {
        Request.Builder builder = output;

        byte[] is = (byte[]) data;
        RequestBody body = RequestBody.create(is, MediaType.parse(request.getHeader().getContentType()));

        String method = request.getMethod().toUpperCase();
        if (HttpMethodConstants.POST.equals(method)) {
            builder.post(body);
        } else if (HttpMethodConstants.PUT.equals(method)) {
            builder.put(body);
        }
    }
}
