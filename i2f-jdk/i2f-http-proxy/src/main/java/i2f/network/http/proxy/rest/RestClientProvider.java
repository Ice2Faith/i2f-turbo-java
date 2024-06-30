package i2f.network.http.proxy.rest;


import i2f.network.http.proxy.rest.core.RestClientProxyHandler;
import i2f.proxy.JdkProxyUtil;
import i2f.serialize.str.IStringObjectSerializer;

/**
 * @author Ice2Faith
 * @date 2022/5/18 9:51
 * @desc
 */
public class RestClientProvider {
    public <T> T getClient(Class<T> interfaces, IStringObjectSerializer processor) {
        return JdkProxyUtil.proxy(interfaces, new RestClientProxyHandler(processor));
    }
}
