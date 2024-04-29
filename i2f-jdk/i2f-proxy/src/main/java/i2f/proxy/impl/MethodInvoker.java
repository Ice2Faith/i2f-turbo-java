package i2f.proxy.impl;


import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2022/3/25 20:39
 * @desc
 */

public class MethodInvoker implements IMethodAccessInvokable {
    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;
    }

    @Override
    public Object invoke(Object ivkObj, Object... args) throws Throwable {
        return method.invoke(ivkObj, args);
    }

    @Override
    public Method method() {
        return method;
    }
}
