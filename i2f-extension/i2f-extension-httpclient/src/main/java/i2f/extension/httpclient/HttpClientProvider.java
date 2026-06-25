package i2f.extension.httpclient;

import i2f.extension.httpclient.impl.HttpClientHttpProcessor;
import i2f.net.http.impl.BasicHttpProcessorProvider;
import i2f.net.http.interfaces.IHttpProcessor;

/**
 * @author Ice2Faith
 * @date 2022/3/26 21:07
 * @desc
 */
public class HttpClientProvider extends BasicHttpProcessorProvider {
    public HttpClientProvider() {
        super(new HttpClientHttpProcessor());
    }

    public HttpClientProvider(IHttpProcessor processor) {
        super(processor);
    }
}
