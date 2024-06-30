package i2f.net.http.interfaces;


import i2f.net.http.data.HttpRequest;
import i2f.net.http.data.HttpResponse;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2022/3/26 20:05
 * @desc
 */
public interface IHttpProcessor {
    HttpResponse doHttp(HttpRequest request) throws IOException;
}
