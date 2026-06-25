package i2f.net.http.impl;


import i2f.io.stream.StreamUtil;
import i2f.net.http.data.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:26
 * @desc
 */
public class HttpRawInputStreamRequestBodyHandler implements IOutputStreamHttpRequestBodyHandler {

    @Override
    public void writeBody(Object data, HttpRequest request, OutputStream output, Object... args) throws IOException {
        OutputStream tos = output;
        InputStream is = (InputStream) data;
        StreamUtil.streamCopy(is, tos, false, true);
        tos.flush();
    }
}
