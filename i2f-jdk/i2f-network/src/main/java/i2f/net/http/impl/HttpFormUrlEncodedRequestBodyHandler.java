package i2f.net.http.impl;


import i2f.net.http.HttpUtil;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:25
 * @desc
 */
public class HttpFormUrlEncodedRequestBodyHandler implements IOutputStreamHttpRequestBodyHandler {
    @Override
    public void writeBody(Object data, HttpRequest request, OutputStream output, Object... args) throws IOException {
        List<MultipartFile> files = request.getFiles();
        if (files != null && !files.isEmpty()) {
            new HttpMultipartFormDataRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }

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

        OutputStream os = output;
        String formUrlEncodeString = HttpUtil.generateUrlEncodeString(data, new StringBuilder()).toString();
        os.write(formUrlEncodeString.getBytes());
        os.flush();
    }
}
