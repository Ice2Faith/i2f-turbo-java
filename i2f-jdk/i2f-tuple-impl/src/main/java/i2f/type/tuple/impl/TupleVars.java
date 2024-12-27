package i2f.type.tuple.impl;

import i2f.type.tuple.std.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/12/27 22:51
 * @desc
 */
public class TupleVars implements Tuple {
    private Object[] vars = new Object[0];

    public TupleVars(Object... vars) {
        this.vars = vars;
    }

    @Override
    public int size() {
        return vars.length;
    }

    @Override
    public Object get(int index) {
        if (index >= vars.length || index < 0) {
            throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + vars.length);
        }
        return vars[index];
    }

    @Override
    public void set(int index, Object value) {
        if (index >= vars.length || index < 0) {
            throw new IndexOutOfBoundsException("index " + index + " out of bounds, size = " + vars.length);
        }
        vars[index] = value;
    }

    @Override
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(vars));
    }

    @Override
    public boolean isEmpty() {
        return vars.length == 0;
    }

    @Override
    public Object[] toArray() {
        return vars;
    }

}
