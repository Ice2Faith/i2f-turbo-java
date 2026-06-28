package i2f.network.http.proxy.rest;

import i2f.net.http.data.HttpRequest;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/26 11:12
 * @desc
 */
@FunctionalInterface
public interface IHttpRequestCustomizer {
    void customizer(HttpRequest request) throws IOException;
}
