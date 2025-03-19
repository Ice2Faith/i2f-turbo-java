package i2f.proxy.impl;

import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.IProxyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:33
 * @desc
 */
public class JdkProxyHandlerAdapter<T> implements InvocationHandler {
    private IProxyHandler handler;
    private T instance;

    public JdkProxyHandlerAdapter(T instance, IProxyHandler handler) {
        this.instance = instance;
        this.handler = handler;
    }

    @Override
    final public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        IInvokable invokable = new JdkMethod(method);
        Object context = handler.initContext();
        Object stopObj = handler.before(context, instance, invokable, args);
        if (stopObj != null) {
            return stopObj;
        }
        try {
            Object retVal = method.invoke(instance, args);
            retVal = handler.after(context, instance, invokable, retVal, args);
            return retVal;
        } catch (Throwable e) {
            throw handler.except(context, instance, invokable, e, args);
        } finally {
            handler.onFinally(context, instance, invokable, args);
        }
    }
}
