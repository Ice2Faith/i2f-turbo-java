package i2f.extension.httpclient;


/**
 * @author Ice2Faith
 * @date 2022/3/24 8:30
 * @desc
 */
public class HttpClientUtil {
    public static volatile HttpClientProvider httpProvider = new HttpClientProvider();

    public static HttpClientProvider http() {
        return httpProvider;
    }

}
