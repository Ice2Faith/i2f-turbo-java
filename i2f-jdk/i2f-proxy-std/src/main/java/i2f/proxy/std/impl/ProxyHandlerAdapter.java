package i2f.proxy.std.impl;

import i2f.invokable.IInvokable;
import i2f.proxy.std.IProxyHandler;
import i2f.proxy.std.IProxyInvocationHandler;

/**
 * @author Ice2Faith
 * @date 2025/7/3 9:30
 */
public class ProxyHandlerAdapter implements IProxyInvocationHandler {
    protected IProxyHandler handler;

    public ProxyHandlerAdapter(IProxyHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        Object context = handler.initContext();
        try {
            Object beforeRet = handler.before(context, ivkObj, invokable, args);
            if (beforeRet != null) {
                return beforeRet;
            }
            Object ret = invokable.invoke(ivkObj, args);
            ret = handler.after(context, ivkObj, invokable, ret, args);
            return ret;
        } catch (Exception e) {
            Throwable ex = handler.except(context, ivkObj, invokable, e, args);
            throw ex;
        } finally {
            handler.onFinally(context, ivkObj, invokable, args);
        }
    }
}
