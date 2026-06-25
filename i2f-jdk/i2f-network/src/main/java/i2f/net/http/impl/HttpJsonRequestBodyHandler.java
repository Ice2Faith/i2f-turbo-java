package i2f.net.http.impl;


import i2f.net.http.data.HttpRequest;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:26
 * @desc
 */
public class HttpJsonRequestBodyHandler implements IOutputStreamHttpRequestBodyHandler {
    protected IJsonSerializer processor;

    public HttpJsonRequestBodyHandler() {
        processor = new Json2Serializer();
    }

    public HttpJsonRequestBodyHandler(IJsonSerializer processor) {
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
