package i2f.net.http;


import i2f.net.http.data.HttpHeaders;
import i2f.net.http.data.HttpRequest;
import i2f.net.http.impl.BasicHttpProcessorProvider;
import i2f.net.http.impl.HttpUrlConnectProcessor;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.url.FormUrlEncodedEncoder;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/24 8:30
 * @desc
 */
public class HttpUtil {
    public static volatile BasicHttpProcessorProvider httpProvider = new BasicHttpProcessorProvider(new HttpUrlConnectProcessor());
    public static volatile IJsonSerializer jsonProcessor = new Json2Serializer();

    public static BasicHttpProcessorProvider http() {
        return httpProvider;
    }


    public static void setHttpHeaders(HttpURLConnection conn, HttpRequest request) {
        HttpHeaders header = request.getHeader();
        if (header == null || header.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ArrayList<String>> item : header.entrySet()) {
            ArrayList<String> value = item.getValue();
            if (value == null) {
                value = new ArrayList<>();
                value.add(null);
            }
            for (String v : value) {
                String val = v == null ? "" : v;
                conn.setRequestProperty(item.getKey(), val);
            }
        }
    }

    public static String generateUrl(HttpRequest request) {
        Object params = request.getParams();
        String url = request.getUrl();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        if (params == null) {
            return url;
        }
        if (params instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) params;
            if (map.isEmpty()) {
                return url;
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append(url);
        if (url.indexOf("?") < 0) {
            builder.append("?");
        }
        if ((url.contains("&") || url.contains("=")) && !url.endsWith("&") && !url.endsWith("?")) {
            builder.append("&");
        }
        generateUrlEncodeString(params, builder);

        return builder.toString();
    }

    public static StringBuilder generateUrlEncodeString(Object params, StringBuilder builder) {
        String form = FormUrlEncodedEncoder.toForm(params);
        builder.append(form);
        return builder;
    }
}
