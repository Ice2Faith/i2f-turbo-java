package i2f.proxy.impl.interfaces;

import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyHandler;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/25 21:11
 * @desc
 */
public abstract class BasicDynamicProxyHandler implements IProxyHandler {
    @Override
    public Object before(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        JdkMethod invoker = (JdkMethod) invokable;
        Method method = invoker.getMethod();
        return resolve(context, ivkObj, method, args);
    }


    public abstract Object resolve(Object context, Object ivkObj, Method method, Object... args);
}
