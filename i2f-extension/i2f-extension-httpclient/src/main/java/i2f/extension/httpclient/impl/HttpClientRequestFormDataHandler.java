package i2f.extension.httpclient.impl;

import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.MultipartFile;
import i2f.net.http.interfaces.IHttpRequestBodyHandler;
import i2f.reflect.ReflectResolver;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:42
 * @desc
 */
public class HttpClientRequestFormDataHandler implements IHttpRequestBodyHandler {
    @Override
    public void writeBody(Object data, HttpRequest request, Object output, Object... args) throws IOException {
        List<MultipartFile> files = request.getFiles();
        if (files != null && !files.isEmpty()) {
            new HttpClientRequestMultipartFormDataHandler().writeBody(data, request, output, args);
            return;
        }

        HttpEntityEnclosingRequestBase httpContext = (HttpEntityEnclosingRequestBase) output;

        List<NameValuePair> paramsList = new ArrayList<>();

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
            NameValuePair pair = new BasicNameValuePair(item.getKey(), val);
            paramsList.add(pair);
        }

        httpContext.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
    }
}
