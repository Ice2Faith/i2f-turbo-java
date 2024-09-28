package i2f.reflect.virtual.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.virtual.VirtualGetterField;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:09
 */
@Data
public class MethodGetterField implements VirtualGetterField {
    protected String name;
    protected Method method;

    public MethodGetterField(Method method) {
        this.method = method;
    }

    public MethodGetterField(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    @Override
    public Object get(Object obj) {
        try {
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                return method.invoke(null);
            }
            return method.invoke(obj);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String name() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        return ReflectResolver.fieldNameByMethodName(method.getName());
    }

    @Override
    public Class<?> type() {
        return method.getReturnType();
    }
}
