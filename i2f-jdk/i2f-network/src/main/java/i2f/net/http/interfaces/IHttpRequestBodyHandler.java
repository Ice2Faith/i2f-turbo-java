package i2f.net.http.interfaces;


import i2f.net.http.data.HttpRequest;

import java.io.IOException;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:21
 * @desc
 */
public interface IHttpRequestBodyHandler {
    void writeBody(Map<String, Object> data, HttpRequest request, Object output, Object... args) throws IOException;
}
