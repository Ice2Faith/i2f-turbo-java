package i2f.spring.cglib;


import i2f.proxy.std.IProxyInvocationHandler;
import i2f.proxy.std.IProxyProvider;
import i2f.spring.cglib.impl.CglibProxyInvocationHandlerAdapter;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @author Ice2Faith
 * @date 2022/3/26 17:50
 * @desc
 */
public class CglibProxyProvider implements IProxyProvider {
    private Enhancer enhancer;

    public CglibProxyProvider() {
        enhancer = new Enhancer();
    }

    public CglibProxyProvider(Enhancer proxy) {
        this.enhancer = proxy;
    }

    @Override
    public <T> T proxy(Object obj, IProxyInvocationHandler handler) {
        return CglibUtil.proxy((Class<T>) obj, new CglibProxyInvocationHandlerAdapter(handler), enhancer);
    }

}
