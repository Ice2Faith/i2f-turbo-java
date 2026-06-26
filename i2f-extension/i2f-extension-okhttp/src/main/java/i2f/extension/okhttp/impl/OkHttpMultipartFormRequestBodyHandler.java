package i2f.extension.okhttp.impl;


import i2f.io.stream.StreamUtil;
import i2f.net.http.consts.ContentTypeConstants;
import i2f.net.http.consts.HttpMethodConstants;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.MultipartFile;
import i2f.reflect.ReflectResolver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class OkHttpMultipartFormRequestBodyHandler implements IOkHttpHttpRequestBodyHandler {

    @Override
    public void writeBody(Object data, HttpRequest request, Request.Builder output, Object... args) throws IOException {
        if (data instanceof byte[]) {
            new OkHttpRawBytesRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        if (data instanceof InputStream) {
            new OkHttpRawInputStreamRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }

        Request.Builder builder = output;

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);

        // 普通表单字段
        if (data != null) {
            Map<String, Object> map = new LinkedHashMap<>();
            if (data instanceof Map) {
                Map<?, ?> dataMap = (Map<?, ?>) data;
                for (Map.Entry<?, ?> entry : dataMap.entrySet()) {
                    map.put(String.valueOf(entry.getKey()), entry.getValue());
                }
            } else {
                ReflectResolver.bean2map(data, map);
            }
            for (Map.Entry<String, Object> item : map.entrySet()) {
                String val = item.getValue() == null ? "" : String.valueOf(item.getValue());
                bodyBuilder.addFormDataPart(item.getKey(), val);
            }
        }

        // 文件字段
        List<MultipartFile> files = request.getFiles();
        if (files != null) {
            for (MultipartFile item : files) {
                InputStream is = item.getInputStream();
                byte[] bytes = StreamUtil.readBytes(is, true);
                RequestBody body = RequestBody.create(bytes, MediaType.parse(ContentTypeConstants.OctetStream));
                bodyBuilder.addFormDataPart(item.getName(), item.getFileName(), body);
            }
        }

        MultipartBody body = bodyBuilder.build();

        String method = request.getMethod().toUpperCase();
        if (HttpMethodConstants.POST.equals(method)) {
            builder.post(body);
        } else if (HttpMethodConstants.PUT.equals(method)) {
            builder.put(body);
        }
    }
}
