package i2f.net.http.interfaces;


import i2f.net.http.data.HttpRequest;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/3/24 14:21
 * @desc
 */
public interface IHttpRequestBodyHandler<T> {
    void writeBody(Object data, HttpRequest request, T output, Object... args) throws IOException;
}
