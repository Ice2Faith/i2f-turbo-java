package i2f.proxy.std.impl;

import i2f.invokable.IInvokable;
import i2f.invokable.Invocation;
import i2f.proxy.std.IProxyHandler;
import i2f.proxy.std.IProxyInvocationHandler;

import java.util.concurrent.atomic.AtomicReference;

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
        Invocation invocation = new Invocation(ivkObj, invokable, args);
        Object context = handler.initContext(invocation);
        Object ret = null;
        Throwable ex = null;
        try {
            AtomicReference<Object> ref = handler.before(context, invocation);
            if (ref != null) {
                ret = ref.get();
                return ret;
            }
            ret = invocation.getInvokable().invoke(invocation.getTarget(), invocation.getArgs());
            ret = handler.after(context, invocation, ret);
            return ret;
        } catch (Exception e) {
            ex = handler.except(context, invocation, ex);
            throw ex;
        } finally {
            handler.onFinally(context, invocation, ret, ex);
        }
    }
}
