package i2f.extension.cglib;

import i2f.extension.cglib.impl.CglibProxyInvocationHandlerAdapter;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.proxy.std.IProxyProvider;
import net.sf.cglib.proxy.Enhancer;

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
