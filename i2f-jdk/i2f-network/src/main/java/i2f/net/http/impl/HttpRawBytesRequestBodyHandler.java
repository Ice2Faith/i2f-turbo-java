package i2f.net.http.impl;


import i2f.net.http.data.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:26
 * @desc
 */
public class HttpRawBytesRequestBodyHandler implements IOutputStreamHttpRequestBodyHandler {

    @Override
    public void writeBody(Object data, HttpRequest request, OutputStream output, Object... args) throws IOException {
        OutputStream tos = output;
        byte[] is = (byte[]) data;
        tos.write(is);
        tos.flush();
    }
}
