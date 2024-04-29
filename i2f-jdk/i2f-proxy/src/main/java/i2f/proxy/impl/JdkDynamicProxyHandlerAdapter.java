package i2f.proxy.impl;

import i2f.proxy.IInvokable;
import i2f.proxy.IProxyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:33
 * @desc
 */
public class JdkDynamicProxyHandlerAdapter implements InvocationHandler {
    private IProxyHandler handler;

    public JdkDynamicProxyHandlerAdapter(IProxyHandler handler) {
        this.handler = handler;
    }

    @Override
    final public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        IInvokable invokable = new MethodInvoker(method);
        Object context = handler.initContext();
        try {
            Object retVal = handler.before(context, proxy, invokable, args);
            retVal = handler.after(context, proxy, invokable, retVal, args);
            return retVal;
        } catch (Throwable e) {
            throw handler.except(context, proxy, invokable, e, args);
        } finally {
            handler.onFinally(context, proxy, invokable, args);
        }
    }
}
