package i2f.extension.cglib.impl;


import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.IProxyHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/26 17:57
 * @desc
 */
public class CglibProxyHandlerAdapter implements MethodInterceptor {
    private IProxyHandler handler;

    public CglibProxyHandlerAdapter(IProxyHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        IInvokable invokable = new JdkMethod(method);
        Object context = handler.initContext();
        Object stopVal = handler.before(context, obj, invokable, args);
        if (stopVal != null) {
            return stopVal;
        }

        try {
            Object ret = methodProxy.invokeSuper(obj, args);
            ret = handler.after(context, obj, invokable, ret, args);
            return ret;
        } catch (Throwable e) {
            throw handler.except(context, obj, invokable, e, args);
        } finally {
            handler.onFinally(context, obj, invokable, args);
        }
    }
}
