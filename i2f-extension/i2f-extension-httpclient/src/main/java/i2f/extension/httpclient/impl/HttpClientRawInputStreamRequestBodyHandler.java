package i2f.extension.httpclient.impl;


import i2f.net.http.data.HttpRequest;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.InputStreamEntity;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientRawInputStreamRequestBodyHandler implements IHttpClientHttpRequestBodyHandler {

    @Override
    public void writeBody(Object data, HttpRequest request, HttpEntityEnclosingRequestBase output, Object... args) throws IOException {
        HttpEntityEnclosingRequestBase httpContext = output;

        InputStream is = (InputStream) data;

        httpContext.setEntity(new InputStreamEntity(is));
    }
}
