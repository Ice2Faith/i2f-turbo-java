package i2f.spring.cglib;

import i2f.proxy.std.IProxyHandler;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.spring.cglib.impl.CglibProxyInvocationHandlerAdapter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;


/**
 * @author Ice2Faith
 * @date 2022/3/26 19:50
 * @desc
 */
public class CglibUtil {
    public static final Enhancer DEFAULT_ENHANCER = new Enhancer();

    public static <T> T proxy(Class<T> clazz, IProxyHandler handler) {
        return proxy(clazz, handler, null);
    }

    public static <T> T proxy(Class<T> clazz, IProxyHandler handler, Enhancer enhancer) {
        return proxy(clazz, IProxyInvocationHandler.of(handler), enhancer);
    }

    public static <T> T proxy(Class<T> clazz, IProxyInvocationHandler handler) {
        return proxy(clazz, handler, null);
    }

    public static <T> T proxy(Class<T> clazz, IProxyInvocationHandler handler, Enhancer enhancer) {
        return proxy(clazz, new CglibProxyInvocationHandlerAdapter(handler), enhancer);
    }

    public static <T> T proxy(Class<T> clazz, MethodInterceptor handler) {
        return proxy(clazz, handler, null);
    }

    public static <T> T proxy(Class<T> clazz, MethodInterceptor handler, Enhancer enhancer) {
        if (enhancer == null) {
            enhancer = DEFAULT_ENHANCER;
        }
        enhancer.setUseFactory(true);
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(handler);
        return (T) enhancer.create();
    }
}
