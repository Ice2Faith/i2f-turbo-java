package i2f.extension.httpclient.impl;


import i2f.net.http.data.HttpRequest;
import i2f.serialize.std.str.xml.IXmlSerializer;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientXmlRequestBodyHandler implements IHttpClientHttpRequestBodyHandler {
    private IXmlSerializer xmlProcessor;

    public HttpClientXmlRequestBodyHandler(IXmlSerializer xmlProcessor) {
        this.xmlProcessor = xmlProcessor;
    }

    @Override
    public void writeBody(Object data, HttpRequest request, HttpEntityEnclosingRequestBase output, Object... args) throws IOException {
        if (data == null) {
            return;
        }
        if (data instanceof byte[]) {
            new HttpClientRawBytesRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        if (data instanceof InputStream) {
            new HttpClientRawInputStreamRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }

        HttpEntityEnclosingRequestBase httpContext = output;

        String content = xmlProcessor.serialize(data);

        httpContext.setEntity(new StringEntity(content, "text/xml", "utf-8"));
    }
}
