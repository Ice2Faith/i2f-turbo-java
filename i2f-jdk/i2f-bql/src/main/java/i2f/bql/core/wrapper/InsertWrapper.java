package i2f.bql.core.wrapper;


import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/12/5 9:24
 */
public class InsertWrapper<H extends InsertWrapper<H>> implements BqlWrapper {
    protected Object table;
    protected Map<Serializable, Object> insertValuesMap = new HashMap<>();

    public static InsertWrapper<?> into() {
        return new InsertWrapper<>();
    }

    @Override
    public i2f.bql.core.Bql<?> get() {
        i2f.bql.core.lambda.Bql<?> ret = i2f.bql.core.lambda.Bql.$lambda()
                .$reset();
        String tableName = null;
        if (table instanceof Class) {
            tableName = ret.classTableName((Class<?>) table);
        } else {
            tableName = String.valueOf(table);
        }
        ret.$mapInsert(tableName,
                ret.lm2names(insertValuesMap));
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
        insertValuesMap.put(col, value);
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

}
