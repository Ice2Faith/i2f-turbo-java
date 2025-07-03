package i2f.proxy.impl.interfaces;

import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:33
 * @desc
 */
public class JdkDynamicProxyInvocationHandlerAdapter implements InvocationHandler {
    private IProxyInvocationHandler handler;

    public JdkDynamicProxyInvocationHandlerAdapter(IProxyInvocationHandler handler) {
        this.handler = handler;
    }

    @Override
    final public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        IInvokable invokable = new JdkMethod(method);
        return handler.invoke(proxy, invokable, args);
    }
}
