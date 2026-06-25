package i2f.net.http.impl;


import i2f.net.http.data.HttpRequest;
import i2f.serialize.std.str.xml.IXmlSerializer;
import i2f.serialize.str.xml.impl.Xml2Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:26
 * @desc
 */
public class HttpXmlRequestBodyHandler implements IOutputStreamHttpRequestBodyHandler {
    protected IXmlSerializer processor;

    public HttpXmlRequestBodyHandler() {
        processor = new Xml2Serializer();
    }

    public HttpXmlRequestBodyHandler(IXmlSerializer processor) {
        this.processor = processor;
    }

    @Override
    public void writeBody(Object data, HttpRequest request, OutputStream output, Object... args) throws IOException {
        if (data == null) {
            return;
        }
        if (data instanceof byte[]) {
            new HttpRawBytesRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        if (data instanceof InputStream) {
            new HttpRawInputStreamRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        OutputStream tos = output;
        String json = processor.serialize(data);
        tos.write(json.getBytes());
        tos.flush();
    }
}
