package i2f.invokable.method;

import i2f.invokable.IInvokable;

import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2025/3/19 21:10
 * @desc
 */
public interface IMethod extends IInvokable {
    String getName();

    Class<?> getDeclaringClass();

    default int getModifiers() {
        return Modifier.PUBLIC;
    }

    default Class<?> getReturnType() {
        return Void.class;
    }

    default Class<?>[] getParameterTypes() {
        return new Class<?>[0];
    }

    default int getParameterCount() {
        return getParameterTypes().length;
    }
}
