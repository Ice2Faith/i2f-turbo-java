package i2f.invokable.method.impl.jdk;

import i2f.invokable.method.IMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2025/3/19 21:17
 * @desc
 */
public class JdkConstructor implements IMethod {
    public static final String NAME = "<init>";
    protected Constructor<?> constructor;

    public JdkConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
        Executable executable;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return constructor.getDeclaringClass();
    }

    @Override
    public int getModifiers() {
        return constructor.getModifiers() | Modifier.STATIC;
    }

    @Override
    public Class<?> getReturnType() {
        return constructor.getDeclaringClass();
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    @Override
    public int getParameterCount() {
        return constructor.getParameterCount();
    }

    @Override
    public Object invoke(Object obj, Object[] args) throws Throwable {
        return constructor.newInstance(args);
    }
}
