package i2f.reflect.virtual.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.virtual.VirtualGetterField;
import i2f.reflect.virtual.VirtualSetterField;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:15
 */
@Data
public class MethodGetterSetterField implements VirtualGetterField, VirtualSetterField {
    protected String name;
    protected Method methodGetter;
    protected Method methodSetter;

    public MethodGetterSetterField(String name, Method methodGetter, Method methodSetter) {
        this.name = name;
        this.methodGetter = methodGetter;
        this.methodSetter = methodSetter;
    }

    public MethodGetterSetterField(Method methodGetter, Method methodSetter) {
        this.methodGetter = methodGetter;
        this.methodSetter = methodSetter;
    }

    @Override
    public boolean readable() {
        return methodGetter != null;
    }

    @Override
    public boolean writeable() {
        return methodSetter != null;
    }

    @Override
    public Object get(Object obj) {
        if (methodGetter == null) {
            throw new UnsupportedOperationException("field [" + name() + "] not support get value!");
        }
        try {
            methodGetter.setAccessible(true);
            if (Modifier.isStatic(methodGetter.getModifiers())) {
                return methodGetter.invoke(null);
            }
            return methodGetter.invoke(obj);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void set(Object obj, Object value) {
        if (methodSetter == null) {
            throw new UnsupportedOperationException("field [" + name() + "] not support get value!");
        }
        try {
            methodSetter.setAccessible(true);
            if (Modifier.isStatic(methodSetter.getModifiers())) {
                methodSetter.invoke(null, value);
                return;
            }
            methodSetter.invoke(obj, value);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String name() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        if (methodGetter != null) {
            return ReflectResolver.fieldNameByMethodName(methodGetter.getName());
        }
        return ReflectResolver.fieldNameByMethodName(methodSetter.getName());
    }

    @Override
    public Class<?> type() {
        if (methodGetter != null) {
            return methodGetter.getReturnType();
        }
        return methodSetter.getParameterTypes()[0];
    }
}
