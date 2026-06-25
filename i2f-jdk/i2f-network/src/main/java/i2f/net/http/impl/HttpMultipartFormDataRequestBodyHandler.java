package i2f.net.http.impl;


import i2f.io.stream.StreamUtil;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.MultipartFile;
import i2f.reflect.ReflectResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:25
 * @desc
 */
public class HttpMultipartFormDataRequestBodyHandler implements IOutputStreamHttpRequestBodyHandler {
    @Override
    public void writeBody(Object data, HttpRequest request, OutputStream output, Object... args) throws IOException {
        if (data instanceof byte[]) {
            new HttpRawBytesRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        if (data instanceof InputStream) {
            new HttpRawInputStreamRequestBodyHandler().writeBody(data, request, output, args);
            return;
        }
        String boundary = getBoundary();
        String boundaryLine = "--" + boundary + "\r\n";
        String boundaryEndLine = "--" + boundary + "--\r\n";
        OutputStream os = output;
        HttpURLConnection conn = (HttpURLConnection) args[0];

        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        StringBuilder builder = new StringBuilder();
        // 表单参数
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
                builder.append(boundaryLine);
                builder.append("Content-Disposition:form-data;name=\"").append(item.getKey()).append("\"");
                builder.append("\r\n\r\n");
                builder.append(item.getValue());
                builder.append("\r\n");
            }
            os.write(builder.toString().getBytes());
        }

        //文件参数
        List<MultipartFile> files = request.getFiles();
        for (MultipartFile item : files) {
            builder.setLength(0);
            builder.append(boundaryLine);
            builder.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"").append(item.getName()).append("\";filename=\"").append(item.getFileName()).append("\"");
            builder.append("\r\n\r\n");
            os.write(builder.toString().getBytes());

            StreamUtil.streamCopy(item.getInputStream(), os, false, true);

            os.write("\r\n\r\n".getBytes());
        }

        os.write(boundaryEndLine.getBytes());
        os.flush();

    }

    public static final String DEFAULT_BOUNDARY = UUID.randomUUID().toString().replaceAll("-", "");

    public static String getBoundary() {
        return DEFAULT_BOUNDARY;
    }
}
