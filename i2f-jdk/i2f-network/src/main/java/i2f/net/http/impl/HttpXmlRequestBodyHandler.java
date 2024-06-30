package i2f.net.http.impl;


import i2f.net.http.data.HttpRequest;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.serialize.str.xml.IXmlSerializer;
import i2f.serialize.str.xml.impl.Xml2Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:26
 * @desc
 */
public class HttpXmlRequestBodyHandler implements IHttpRequestBodyHandler {
    protected IXmlSerializer processor;

    public HttpXmlRequestBodyHandler() {
        processor = new Xml2Serializer();
    }

    public HttpXmlRequestBodyHandler(IXmlSerializer processor) {
        this.processor = processor;
    }

    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request, Object output, Object... args) throws IOException {
        OutputStream tos = (OutputStream) output;
        String json = processor.serialize(data);
        tos.write(json.getBytes());
        tos.flush();
    }
}
