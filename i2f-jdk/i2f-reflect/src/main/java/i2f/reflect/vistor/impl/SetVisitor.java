package i2f.reflect.vistor.impl;

import i2f.reflect.vistor.Visitor;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:05
 * @desc
 */
public class SetVisitor implements Visitor {
    private Set col;
    private Object val;

    public SetVisitor(Set col, Object val) {
        this.col = col;
        this.val = val;
    }

    @Override
    public Object get() {
        return val;
    }

    @Override
    public void set(Object value) {
        col.add(value);
    }

    @Override
    public Object parent() {
        return this.col;
    }

    @Override
    public void delete() {
        col.remove(val);
    }
}
