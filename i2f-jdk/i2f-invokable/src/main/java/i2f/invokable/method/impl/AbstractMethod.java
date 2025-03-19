package i2f.invokable.method.impl;

import i2f.invokable.method.IMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2025/3/19 21:21
 * @desc
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class AbstractMethod implements IMethod {
    public static final int DEFAULT_MODIFIER = Modifier.PUBLIC | Modifier.STATIC;
    protected String name;
    protected Class<?> declaringClass;
    protected int modifiers = DEFAULT_MODIFIER;
    protected Class<?> returnType;
    protected Class<?>[] parameterTypes;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return declaringClass == null ? Object.class : declaringClass;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType == null ? Object.class : returnType;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes == null ? new Class<?>[0] : parameterTypes;
    }

    @Override
    public int getParameterCount() {
        return getParameterTypes().length;
    }

}
