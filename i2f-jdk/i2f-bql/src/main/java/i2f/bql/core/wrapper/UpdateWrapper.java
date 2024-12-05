package i2f.bql.core.wrapper;


import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/12/5 9:24
 */
public class UpdateWrapper<H extends UpdateWrapper<H>> extends ConditionWrapper<H> {
    protected Object table;
    protected Map<Serializable, Object> setValuesMap = new HashMap<>();
    protected List<Serializable> setNullCols = new ArrayList<>();

    public static UpdateWrapper<?> update() {
        return new UpdateWrapper<>();
    }

    @Override
    public i2f.bql.core.Bql<?> get() {
        i2f.bql.core.lambda.Bql<?> ret = i2f.bql.core.lambda.Bql.$lambda()
                .$reset();
        if (table instanceof Class) {
            ret.$update((Class) table);
        } else {
            ret.$update(String.valueOf(table));
        }
        ret.$lambdaSet(setValuesMap, setNullCols);
        ret.$where(super::get);
        return ret;
    }

    public H table(String table) {
        this.table = table;
        return (H) this;
    }

    public H table(Class<?> table) {
        this.table = table;
        return (H) this;
    }

    public H set(Serializable col, Object value) {
        setValuesMap.put(col, value);
        return (H) this;
    }

    public H set(IFunctional func, Object value) {
        return set((Serializable) func, value);
    }

    public H set(String col, Object value) {
        return set((Serializable) col, value);
    }

    public <R, T> H set(IGetter<R, T> getter, Object value) {
        return set((Serializable) getter, value);
    }

    public <T, V> H set(ISetter<T, V> setter, Object value) {
        return set((Serializable) setter, value);
    }

    public <R, T, V> H set(IBuilder<R, T, V> builder, Object value) {
        return set((Serializable) builder, value);
    }

    public <T> H set(IExecute<T> exec, Object value) {
        return set((Serializable) exec, value);
    }


    public H setNull(Serializable col) {
        setNullCols.add(col);
        return (H) this;
    }

    public H setNull(IFunctional func) {
        return setNull((Serializable) func);
    }

    public H setNull(String col) {
        return setNull((Serializable) col);
    }

    public <R, T> H setNull(IGetter<R, T> getter) {
        return setNull((Serializable) getter);
    }

    public <T, V> H setNull(ISetter<T, V> setter) {
        return setNull((Serializable) setter);
    }

    public <R, T, V> H setNull(IBuilder<R, T, V> builder) {
        return setNull((Serializable) builder);
    }

    public <T> H setNull(IExecute<T> exec) {
        return setNull((Serializable) exec);
    }
}
