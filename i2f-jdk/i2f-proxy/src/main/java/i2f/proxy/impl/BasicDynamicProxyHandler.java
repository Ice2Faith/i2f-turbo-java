package i2f.proxy.impl;

import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/25 21:11
 * @desc
 */
public abstract class BasicDynamicProxyHandler extends BasicProxyHandler {
    @Override
    public Object initContext() {
        return null;
    }

    @Override
    public Object before(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        JdkMethod invoker = (JdkMethod) invokable;
        Method method = invoker.getMethod();
        return resolve(context, ivkObj, method, args);
    }

    @Override
    public Object after(Object context, Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        return retVal;
    }

    @Override
    public Throwable except(Object context, Object ivkObj, IInvokable invokable, Throwable ex, Object... args) {
        return ex;
    }

    @Override
    public void onFinally(Object context, Object ivkObj, IInvokable invokable, Object... args) {

    }

    public abstract Object resolve(Object context, Object ivkObj, Method method, Object... args);
}
