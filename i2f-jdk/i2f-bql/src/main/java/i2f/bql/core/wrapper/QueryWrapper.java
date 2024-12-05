package i2f.bql.core.wrapper;


import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/12/5 9:24
 */
public class QueryWrapper<H extends QueryWrapper<H>> extends ConditionWrapper<H> {
    protected Object table;
    protected Map<Serializable, String> colMap = new LinkedHashMap<>();
    protected List<Serializable> groupCols = new ArrayList<>();
    protected List<Serializable> orderCols = new ArrayList<>();

    public static QueryWrapper<?> select() {
        return new QueryWrapper<>();
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
        ret.$mapSelect(alias, ret.lm2names(colMap))
                .$from(tableName, alias)
                .$where(super::get)
                .$mapGroupBy(alias, ret.lm2names(groupCols))
                .$mapOrderBy(alias, ret.lm2names(orderCols));
        return ret;
    }

    public H alias(String str) {
        this.alias = str;
        return (H) this;
    }

    public H from(String table) {
        this.table = table;
        return (H) this;
    }

    public H from(Class<?> table) {
        this.table = table;
        return (H) this;
    }

    public H col(Serializable column, String alias) {
        colMap.put(column, alias);
        return (H) this;
    }

    public H col(IFunctional func, String value) {
        return col((Serializable) func, value);
    }

    public H col(String col, String value) {
        return col((Serializable) col, value);
    }

    public <R, T> H col(IGetter<R, T> getter, String value) {
        return col((Serializable) getter, value);
    }

    public <T, V> H col(ISetter<T, V> setter, String value) {
        return col((Serializable) setter, value);
    }

    public <R, T, V> H col(IBuilder<R, T, V> builder, String value) {
        return col((Serializable) builder, value);
    }

    public <T> H col(IExecute<T> exec, String value) {
        return col((Serializable) exec, value);
    }


    public H col(Serializable column) {
        colMap.put(column, null);
        return (H) this;
    }

    public H col(IFunctional func) {
        return col((Serializable) func);
    }

    public H col(String col) {
        return col((Serializable) col);
    }

    public <R, T> H col(IGetter<R, T> getter) {
        return col((Serializable) getter);
    }

    public <T, V> H col(ISetter<T, V> setter) {
        return col((Serializable) setter);
    }

    public <R, T, V> H col(IBuilder<R, T, V> builder) {
        return col((Serializable) builder);
    }

    public <T> H col(IExecute<T> exec) {
        return col((Serializable) exec);
    }

    public H groupBy(Serializable col) {
        groupCols.add(col);
        return (H) this;
    }

    public H groupBy(IFunctional func) {
        return groupBy((Serializable) func);
    }

    public H groupBy(String col) {
        return groupBy((Serializable) col);
    }

    public <R, T> H groupBy(IGetter<R, T> getter) {
        return groupBy((Serializable) getter);
    }

    public <T, V> H groupBy(ISetter<T, V> setter) {
        return groupBy((Serializable) setter);
    }

    public <R, T, V> H groupBy(IBuilder<R, T, V> builder) {
        return groupBy((Serializable) builder);
    }

    public <T> H groupBy(IExecute<T> exec) {
        return groupBy((Serializable) exec);
    }


    public H orderBy(Serializable col) {
        orderCols.add(col);
        return (H) this;
    }

    public H orderBy(IFunctional func) {
        return orderBy((Serializable) func);
    }

    public H orderBy(String col) {
        return orderBy((Serializable) col);
    }

    public <R, T> H orderBy(IGetter<R, T> getter) {
        return orderBy((Serializable) getter);
    }

    public <T, V> H orderBy(ISetter<T, V> setter) {
        return orderBy((Serializable) setter);
    }

    public <R, T, V> H orderBy(IBuilder<R, T, V> builder) {
        return orderBy((Serializable) builder);
    }

    public <T> H orderBy(IExecute<T> exec) {
        return orderBy((Serializable) exec);
    }
}
