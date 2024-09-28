package i2f.reflect.virtual.impl;

import i2f.reflect.virtual.VirtualGetterField;
import i2f.reflect.virtual.VirtualSetterField;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/9/28 11:03
 */
@Data
public class CombineGetterSetterField implements VirtualGetterField, VirtualSetterField {
    protected VirtualGetterField getter;
    protected VirtualSetterField setter;

    public CombineGetterSetterField(VirtualGetterField getter, VirtualSetterField setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public Object get(Object obj) {
        if (getter == null) {
            throw new UnsupportedOperationException("field [" + name() + "] not support get value!");
        }
        return getter.get(obj);
    }

    @Override
    public boolean readable() {
        return getter != null && getter.readable();
    }

    @Override
    public boolean writeable() {
        return setter != null && setter.writeable();
    }

    @Override
    public void set(Object obj, Object value) {
        if (setter == null) {
            throw new UnsupportedOperationException("field [" + name() + "] not support set value!");
        }
        setter.set(obj, value);
    }

    @Override
    public String name() {
        if (getter != null) {
            return getter.name();
        }
        return setter.name();
    }

    @Override
    public Class<?> type() {
        if (getter != null) {
            return getter.type();
        }
        return setter.type();
    }
}
