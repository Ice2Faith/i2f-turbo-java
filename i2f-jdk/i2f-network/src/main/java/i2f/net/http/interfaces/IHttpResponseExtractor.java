package i2f.net.http.interfaces;

import i2f.net.http.data.HttpResponse;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/25 16:26
 * @desc
 */
@FunctionalInterface
public interface IHttpResponseExtractor<T> {
    T extract(HttpResponse response) throws IOException;
}
