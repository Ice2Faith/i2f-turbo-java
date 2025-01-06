package i2f.bql.core.wrapper;

import i2f.bql.core.Bql;
import i2f.bql.core.condition.Condition;
import i2f.bql.core.condition.ConditionItem;
import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/12/5 8:35
 */
public class ConditionWrapper<H extends ConditionWrapper<H>> implements BqlWrapper {
    protected boolean and = true;
    protected String alias = null;
    protected List<ConditionItem> conditions = new ArrayList<>();

    @Override
    public i2f.bql.core.Bql<?> get() {
        i2f.bql.core.lambda.Bql<?> ret = Bql.$lambda()
                .$reset();
        for (ConditionItem item : conditions) {
            if (item.isAnd()) {
                ret.$and();
            } else {
                ret.$or();
            }
            ret.$alias(item.getAlias());
            Serializable column = item.getColumn();
            Condition value = item.getValue();
            String col = null;
            if (column instanceof IFunctional) {
                col = ret.lambdaFieldName((IFunctional) column);
            } else {
                col = String.valueOf(column);
            }
            value.apply(ret, col);
        }
        return ret;
    }

    public H and() {
        this.and = true;
        return (H) this;
    }

    public H or() {
        this.and = false;
        return (H) this;
    }

    public H cond(Serializable column, Object value) {
        if (value instanceof Condition) {
            conditions.add(new ConditionItem(and, alias, column, (Condition) value));
        } else {
            conditions.add(new ConditionItem(and, alias, column, Condition.def(value)));
        }
        return (H) this;
    }

    public H cond(Serializable column, Condition cond) {
        return cond(column, (Object) cond);
    }

    public H cond(IFunctional func, Condition cond) {
        return cond((Serializable) func, cond);
    }

    public H cond(IFunctional func, Object value) {
        return cond((Serializable) func, value);
    }

    public H cond(String col, Condition cond) {
        return cond((Serializable) col, cond);
    }

    public H cond(String col, Object value) {
        return cond((Serializable) col, value);
    }

    public <R, T> H cond(IGetter<R, T> getter, Condition cond) {
        return cond((Serializable) getter, cond);
    }

    public <R, T> H cond(IGetter<R, T> getter, Object value) {
        return cond((Serializable) getter, value);
    }

    public <T, V> H cond(ISetter<T, V> setter, Condition cond) {
        return cond((Serializable) setter, cond);
    }

    public <T, V> H cond(ISetter<T, V> setter, Object value) {
        return cond((Serializable) setter, value);
    }

    public <R, T, V> H cond(IBuilder<R, T, V> builder, Condition cond) {
        return cond((Serializable) builder, cond);
    }

    public <R, T, V> H cond(IBuilder<R, T, V> builder, Object value) {
        return cond((Serializable) builder, value);
    }

    public <T> H cond(IExecute<T> exec, Condition cond) {
        return cond((Serializable) exec, cond);
    }

    public <T> H cond(IExecute<T> exec, Object value) {
        return cond((Serializable) exec, value);
    }

    public <R, T> H eq(IGetter<R, T> getter, Object value) {
        return cond((Serializable) getter, Condition.$eq(value));
    }

    public <T, V> H eq(ISetter<T, V> setter, Object value) {
        return cond((Serializable) setter, Condition.$eq(value));
    }

    public <R, T> H like(IGetter<R, T> getter, Object value) {
        return cond((Serializable) getter, Condition.$like(value));
    }

    public <T, V> H like(ISetter<T, V> setter, Object value) {
        return cond((Serializable) setter, Condition.$like(value));
    }

    public <R, T> H neq(IGetter<R, T> getter, Object value) {
        return cond((Serializable) getter, Condition.$neq(value));
    }

    public <T, V> H neq(ISetter<T, V> setter, Object value) {
        return cond((Serializable) setter, Condition.$neq(value));
    }

    public <R, T> H ne(IGetter<R, T> getter, Object value) {
        return cond((Serializable) getter, Condition.$ne(value));
    }

    public <T, V> H ne(ISetter<T, V> setter, Object value) {
        return cond((Serializable) setter, Condition.$ne(value));
    }

    public <R, T> H isNull(IGetter<R, T> getter) {
        return cond((Serializable) getter, Condition.$isNull());
    }

    public <T, V> H isNull(ISetter<T, V> setter) {
        return cond((Serializable) setter, Condition.$isNull());
    }

    public <R, T> H isNotNull(IGetter<R, T> getter) {
        return cond((Serializable) getter, Condition.$isNotNull());
    }

    public <T, V> H isNotNull(ISetter<T, V> setter) {
        return cond((Serializable) setter, Condition.$isNotNull());
    }

    public <R, T> H in(IGetter<R, T> getter, Object... value) {
        return cond((Serializable) getter, Condition.$in(value));
    }

    public <T, V> H in(ISetter<T, V> setter, Object... value) {
        return cond((Serializable) setter, Condition.$in(value));
    }

    public <R, T> H in(IGetter<R, T> getter, Collection<?> value) {
        return cond((Serializable) getter, Condition.$in(value));
    }

    public <T, V> H in(ISetter<T, V> setter, Collection<?> value) {
        return cond((Serializable) setter, Condition.$in(value));
    }

    public <R, T> H notIn(IGetter<R, T> getter, Object... value) {
        return cond((Serializable) getter, Condition.$notIn(value));
    }

    public <T, V> H notIn(ISetter<T, V> setter, Object... value) {
        return cond((Serializable) setter, Condition.$notIn(value));
    }

    public <R, T> H notIn(IGetter<R, T> getter, Collection<?> value) {
        return cond((Serializable) getter, Condition.$notIn(value));
    }

    public <T, V> H notIn(ISetter<T, V> setter, Collection<?> value) {
        return cond((Serializable) setter, Condition.$notIn(value));
    }

    public <R, T> H exists(IGetter<R, T> getter, Supplier<Bql<?>> caller) {
        return cond((Serializable) getter, Condition.$exists(caller));
    }

    public <T, V> H exists(ISetter<T, V> setter, Supplier<Bql<?>> caller) {
        return cond((Serializable) setter, Condition.$ne(caller));
    }

    public <R, T> H notExists(IGetter<R, T> getter, Supplier<Bql<?>> caller) {
        return cond((Serializable) getter, Condition.$notExists(caller));
    }

    public <T, V> H notExists(ISetter<T, V> setter, Supplier<Bql<?>> caller) {
        return cond((Serializable) setter, Condition.$notExists(caller));
    }

    public <R, T> H and(IGetter<R, T> getter, Supplier<Bql<?>> caller) {
        return cond((Serializable) getter, Condition.$and(caller));
    }

    public <T, V> H and(ISetter<T, V> setter, Supplier<Bql<?>> caller) {
        return cond((Serializable) setter, Condition.$and(caller));
    }

    public <R, T> H or(IGetter<R, T> getter, Supplier<Bql<?>> caller) {
        return cond((Serializable) getter, Condition.$or(caller));
    }

    public <T, V> H or(ISetter<T, V> setter, Supplier<Bql<?>> caller) {
        return cond((Serializable) setter, Condition.$or(caller));
    }

    public <R, T> H and(IGetter<R, T> getter, ConditionWrapper<?> wrapper) {
        return cond((Serializable) getter, Condition.$and(wrapper));
    }

    public <T, V> H and(ISetter<T, V> setter, ConditionWrapper<?> wrapper) {
        return cond((Serializable) setter, Condition.$and(wrapper));
    }

    public <R, T> H or(IGetter<R, T> getter, ConditionWrapper<?> wrapper) {
        return cond((Serializable) getter, Condition.$or(wrapper));
    }

    public <T, V> H or(ISetter<T, V> setter, ConditionWrapper<?> wrapper) {
        return cond((Serializable) setter, Condition.$or(wrapper));
    }
}
