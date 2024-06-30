package i2f.net.http.impl;


import i2f.net.http.HttpUtil;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.MultipartFile;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:25
 * @desc
 */
public class HttpFormUrlEncodedRequestBodyHandler implements IHttpRequestBodyHandler {
    @Override
    public void writeBody(Map<String, Object> data, HttpRequest request, Object output, Object... args) throws IOException {
        List<MultipartFile> files = request.getFiles();
        if (files != null && files.size() > 0) {
            new HttpMultipartFormDataRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        OutputStream os = (OutputStream) output;
        String formUrlEncodeString = HttpUtil.generateUrlEncodeString(data, new StringBuilder()).toString();
        os.write(formUrlEncodeString.getBytes());
        os.flush();
    }
}
