package i2f.invokable.method.impl.jdk;

import i2f.invokable.method.IMethod;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2025/3/19 21:14
 * @desc
 */
public class JdkMethod implements IMethod {
    protected Method method;

    public JdkMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    @Override
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    @Override
    public int getParameterCount() {
        return method.getParameterCount();
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Throwable {
        return method.invoke(obj, args);
    }
}
