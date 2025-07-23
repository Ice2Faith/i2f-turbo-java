package i2f.proxy.impl.interfaces;

import i2f.invokable.Invocation;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyHandler;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2022/3/25 21:11
 * @desc
 */
public abstract class BasicDynamicProxyHandler implements IProxyHandler {
    @Override
    public AtomicReference<Object> before(Object context, Invocation invocation) {
        JdkMethod invoker = (JdkMethod) invocation.getInvokable();
        Method method = invoker.getMethod();
        return resolve(context, invocation.getTarget(), method, invocation.getArgs());
    }


    public abstract AtomicReference<Object> resolve(Object context, Object ivkObj, Method method, Object... args);
}
