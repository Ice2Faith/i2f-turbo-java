package i2f.reflect.virtual.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.virtual.VirtualSetterField;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:09
 */
@Data
public class MethodSetterField implements VirtualSetterField {
    protected String name;
    protected Method method;

    public MethodSetterField(Method method) {
        this.method = method;
    }

    public MethodSetterField(String name, Method method) {
        this.name = name;
        this.method = method;
    }

    @Override
    public void set(Object obj, Object value) {
        try {
            method.setAccessible(true);
            if (Modifier.isStatic(method.getModifiers())) {
                method.invoke(null, value);
                return;
            }
            method.invoke(obj, value);
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
        return method.getParameterTypes()[0];
    }
}
