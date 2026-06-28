package i2f.network.http.proxy.rest;


import i2f.environment.std.IEnvironment;
import i2f.network.http.proxy.rest.core.RestClientProxyHandler;
import i2f.proxy.JdkProxyUtil;
import i2f.serialize.std.str.IStringObjectSerializer;

/**
 * @author Ice2Faith
 * @date 2022/5/18 9:51
 * @desc
 */
public class RestClientProvider {
    public <T> T getClient(Class<T> interfaces, IStringObjectSerializer processor, IEnvironment environment) {
        return getClient(interfaces, new RestClientProxyHandler(processor, environment));
    }

    public <T> T getClient(Class<T> interfaces, IStringObjectSerializer processor) {
        return getClient(interfaces, new RestClientProxyHandler(processor));
    }

    public <T> T getClient(Class<T> interfaces, RestClientProxyHandler handler) {
        return JdkProxyUtil.proxy(interfaces, handler);
    }
}
