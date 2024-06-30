package i2f.net.http.impl;


import i2f.net.http.data.HttpRequest;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.serialize.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:26
 * @desc
 */
public class HttpJsonRequestBodyHandler implements IHttpRequestBodyHandler {
    protected IJsonSerializer processor;

    public HttpJsonRequestBodyHandler() {
        processor = new Json2Serializer();
    }

    public HttpJsonRequestBodyHandler(IJsonSerializer processor) {
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
