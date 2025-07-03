package i2f.extension.cglib.impl;


import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyInvocationHandler;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/26 17:57
 * @desc
 */
public class CglibProxyInvocationHandlerAdapter implements MethodInterceptor {
    private IProxyInvocationHandler handler;

    public CglibProxyInvocationHandlerAdapter(IProxyInvocationHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        IInvokable invokable = new JdkMethod(method);
        return handler.invoke(obj, invokable, args);

    }
}
