package i2f.extension.httpclient.impl;


import i2f.net.http.data.HttpRequest;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientRawBytesRequestBodyHandler implements IHttpClientHttpRequestBodyHandler {

    @Override
    public void writeBody(Object data, HttpRequest request, HttpEntityEnclosingRequestBase output, Object... args) throws IOException {
        HttpEntityEnclosingRequestBase httpContext = output;

        byte[] is = (byte[]) data;

        httpContext.setEntity(new ByteArrayEntity(is));
    }
}
