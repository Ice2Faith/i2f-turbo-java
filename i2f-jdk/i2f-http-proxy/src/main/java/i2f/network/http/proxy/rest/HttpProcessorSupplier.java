package i2f.network.http.proxy.rest;

import i2f.net.http.interfaces.IHttpProcessor;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2026/6/26 11:04
 * @desc
 */
@FunctionalInterface
public interface HttpProcessorSupplier {
    IHttpProcessor get() throws IOException;
}
