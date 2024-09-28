package i2f.reflect.virtual.impl;

import i2f.reflect.virtual.VirtualGetterField;
import i2f.reflect.virtual.VirtualSetterField;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author Ice2Faith
 * @date 2024/9/28 9:05
 */
@Data
public class FieldGetterSetterField implements VirtualGetterField, VirtualSetterField {
    protected String name;
    protected Field field;

    public FieldGetterSetterField(Field field) {
        this.field = field;
    }

    public FieldGetterSetterField(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    @Override
    public boolean readable() {
        return true;
    }

    @Override
    public boolean writeable() {
        return true;
    }

    @Override
    public Object get(Object obj) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void set(Object obj, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public String name() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        return field.getName();
    }

    @Override
    public Class<?> type() {
        return field.getType();
    }
}
