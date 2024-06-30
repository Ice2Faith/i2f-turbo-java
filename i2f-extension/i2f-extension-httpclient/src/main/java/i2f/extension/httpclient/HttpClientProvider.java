package i2f.extension.httpclient;

import i2f.extension.httpclient.impl.HttpClientHttpProcessor;
import i2f.extension.httpclient.impl.HttpClientRequestFormDataHandler;
import i2f.extension.httpclient.impl.HttpClientRequestJsonDataHandler;
import i2f.extension.httpclient.impl.HttpClientRequestMultipartFormDataHandler;
import i2f.net.http.impl.BasicHttpProcessorProvider;
import i2f.net.http.interfaces.IHttpProcessor;
import i2f.serialize.str.json.impl.Json2Serializer;

/**
 * @author Ice2Faith
 * @date 2022/3/26 21:07
 * @desc
 */
public class HttpClientProvider extends BasicHttpProcessorProvider {
    public HttpClientProvider() {
        super(new HttpClientHttpProcessor());
        formRequestBodyHandler = new HttpClientRequestFormDataHandler();
        jsonRequestBodyHandler = new HttpClientRequestJsonDataHandler(new Json2Serializer());
        multipartFormDataRequestBodyHandler = new HttpClientRequestMultipartFormDataHandler();
    }

    public HttpClientProvider(IHttpProcessor processor) {
        super(processor);
        formRequestBodyHandler = new HttpClientRequestFormDataHandler();
        jsonRequestBodyHandler = new HttpClientRequestJsonDataHandler(new Json2Serializer());
        multipartFormDataRequestBodyHandler = new HttpClientRequestMultipartFormDataHandler();
    }
}
