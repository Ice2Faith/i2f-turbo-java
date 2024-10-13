package i2f.bql.core.lambda;


import i2f.bindsql.BindSql;
import i2f.bql.lambda.IClassTableNameResolver;
import i2f.bql.lambda.IFieldColumnNameResolver;
import i2f.bql.lambda.impl.DefaultClassTableNameResolver;
import i2f.bql.lambda.impl.DefaultFieldColumnNameResolver;
import i2f.container.builder.Builders;
import i2f.container.builder.collection.ListBuilder;
import i2f.container.builder.map.MapBuilder;
import i2f.functional.IFunctional;
import i2f.functional.adapt.IBuilder;
import i2f.functional.adapt.IExecute;
import i2f.functional.adapt.IGetter;
import i2f.functional.adapt.ISetter;
import i2f.lambda.inflater.LambdaInflater;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/23 10:38
 * @desc
 */
public class Bql<H extends Bql<H>> extends i2f.bql.core.Bql<H> {

    public static <H extends i2f.bql.core.bean.Bql<H>> i2f.bql.core.bean.Bql<H> $bean() {
        return new i2f.bql.core.bean.Bql();
    }

    public static IFieldColumnNameResolver GLOBAL_FIELD_NAME_RESOLVER = DefaultFieldColumnNameResolver.DEFAULT;
    public static IClassTableNameResolver GLOBAL_CLASS_NAME_RESOLVER = DefaultClassTableNameResolver.DEFAULT;

    public static ThreadLocal<IFieldColumnNameResolver> fieldNameResolverHolder = ThreadLocal.withInitial(() -> DefaultFieldColumnNameResolver.DEFAULT);
    public static ThreadLocal<IClassTableNameResolver> tableNameResolverHolder = ThreadLocal.withInitial(() -> DefaultClassTableNameResolver.DEFAULT);


    protected IFieldColumnNameResolver fieldNameResolver = DefaultFieldColumnNameResolver.DEFAULT;
    protected IClassTableNameResolver tableNameResolver = DefaultClassTableNameResolver.DEFAULT;

    public static final InheritableThreadLocal<IFieldColumnNameResolver> localFieldNameResolver = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<IClassTableNameResolver> localTableNameResolver = new InheritableThreadLocal<>();

    public Bql() {
        // 初始化，
        // 查找顺序，静态变量->ThreadLocal
        // 优先级，ThreadLocal->静态变量
        if (GLOBAL_FIELD_NAME_RESOLVER != null) {
            this.fieldNameResolver = GLOBAL_FIELD_NAME_RESOLVER;
        }
        if (fieldNameResolverHolder.get() != null) {
            this.fieldNameResolver = fieldNameResolverHolder.get();
        }


        if (GLOBAL_CLASS_NAME_RESOLVER != null) {
            this.tableNameResolver = GLOBAL_CLASS_NAME_RESOLVER;
        }
        if (tableNameResolverHolder.get() != null) {
            this.tableNameResolver = tableNameResolverHolder.get();
        }
    }

    @Override
    public H inherit() {
        super.inherit();
        Optional.ofNullable(localFieldNameResolver.get()).ifPresent((v) -> fieldNameResolver = v);
        Optional.ofNullable(localTableNameResolver.get()).ifPresent((v) -> tableNameResolver = v);
        return (H) this;
    }

    @Override
    public H store() {
        super.store();
        localFieldNameResolver.set(fieldNameResolver);
        localTableNameResolver.set(tableNameResolver);
        return (H) this;
    }

    @Override
    public H unset() {
        super.unset();
        localFieldNameResolver.set(null);
        localTableNameResolver.set(null);
        return (H) this;
    }

    public static <H extends Bql<H>> Bql<H> $lambda() {
        return new Bql();
    }

    public static <H extends Bql<H>> Bql<H> $lambda(BindSql bql) {
        return (Bql<H>) new Bql().$(bql);
    }

    public static <H extends Bql<H>> Bql<H> $lambda(i2f.bql.core.Bql<?> val) {
        return (Bql<H>) new Bql().$(val);
    }

    public static <H extends Bql<H>> Bql<H> $lambda(Supplier<i2f.bql.core.Bql<?>> caller) {
        return (Bql<H>) new Bql().$(caller);
    }

    public static <H extends Bql<H>> Bql<H> $lambda(String sql, Object args) {
        return (Bql<H>) new Bql().$(sql, args);
    }


    public <CT, CR> H $(IGetter<CT, CR> column) {
        return $(lambdaFieldName(column));
    }

    public <CT, CV> H $(ISetter<CT, CV> column) {
        return $(lambdaFieldName(column));
    }

    public <CT, CV, CR> H $(IBuilder<CT, CV, CR> column) {
        return $(lambdaFieldName(column));
    }

    public <CT> H $(IExecute<CT> column) {
        return $(lambdaFieldName(column));
    }

    public H $(Class<?> clazz) {
        return $(classTableName(clazz));
    }


    public static <T, R> IFunctional $lm(IGetter<T, R> column) {
        return column;
    }

    public static <T, V> IFunctional $lm(ISetter<T, V> column) {
        return column;
    }

    public static <T, V, R> IFunctional $lm(IBuilder<T, V, R> column) {
        return column;
    }

    public static <T> IFunctional $lm(IExecute<T> column) {
        return column;
    }

    public List<String> lm2names(Collection<? extends Serializable> list) {
        if (list == null) {
            return null;
        }
        List<String> ret = new ArrayList<>();
        for (Serializable lambda : list) {
            if (lambda instanceof IFunctional) {
                ret.add(lambdaFieldName((IFunctional) lambda));
            } else {
                ret.add(String.valueOf(lambda));
            }
        }
        return ret;
    }

    public <T> Map<String, T> lm2names(Map<? extends Serializable, T> map) {
        if (map == null) {
            return null;
        }
        Map<String, T> ret = new LinkedHashMap<>();
        for (Map.Entry<? extends Serializable, T> entry : map.entrySet()) {
            Serializable lambda = entry.getKey();
            if (lambda instanceof IFunctional) {
                ret.put(lambdaFieldName((IFunctional) lambda), entry.getValue());
            } else {
                ret.put(String.valueOf(lambda), entry.getValue());
            }
        }
        return ret;
    }

    public <T> List<Map<String, T>> lm2names(List<Map<? extends Serializable, T>> list) {
        if (list == null) {
            return null;
        }
        List<Map<String, T>> ret = new LinkedList<>();
        for (Map<? extends Serializable, T> item : list) {
            ret.add(lm2names(item));
        }
        return ret;
    }

    public List<String> lm2names(Serializable... lambdas) {
        return lm2names(Arrays.asList(lambdas));
    }

    public static MapBuilder<Serializable, String, LinkedHashMap<Serializable, String>> $colMapLambda() {
        return Builders.newMap(LinkedHashMap::new, Serializable.class, String.class);
    }

    public static MapBuilder<Serializable, Object, LinkedHashMap<Serializable, Object>> $valueMapLambda() {
        return Builders.newMap(LinkedHashMap::new, Serializable.class, Object.class);
    }

    public static ListBuilder<Serializable, LinkedList<Serializable>> $colListLambda() {
        return Builders.newList(LinkedList::new, Serializable.class);
    }

    public H $tableName(Class<?> clazz) {
        return $(decorateTableName(classTableName(clazz)));
    }

    public <T, R> H $columnName(IGetter<T, R> column) {
        return $(decorateColumnName(lambdaFieldName(column)));
    }

    public <T, V> H $columnName(ISetter<T, V> column) {
        return $(decorateColumnName(lambdaFieldName(column)));
    }

    public <T, V, R> H $columnName(IBuilder<T, V, R> column) {
        return $(decorateColumnName(lambdaFieldName(column)));
    }

    public <T> H $columnName(IExecute<T> column) {
        return $(decorateColumnName(lambdaFieldName(column)));
    }

    public H $lambdaInsert(Class<?> table, Map<? extends Serializable, Object> valueMap) {
        return $mapInsert(classTableName(table),
                lm2names(valueMap));
    }

    public H $lambdaInsertBatchValues(Class<?> table, List<Map<? extends Serializable, Object>> values) {
        return $mapInsertBatchValues(classTableName(table),
                lm2names(values));
    }

    public H $lambdaInsertBatchUnionAll(Class<?> table, List<Map<? extends Serializable, Object>> values) {
        return $mapInsertBatchUnionAll(classTableName(table),
                lm2names(values));
    }

    public H $lambdaDelete(Class<?> table,
                           Map<? extends Serializable, Object> whereMap) {
        return $mapDelete(classTableName(table),
                lm2names(whereMap),
                null,
                null);
    }

    public H $lambdaDelete(Class<?> table,
                           Map<? extends Serializable, Object> whereMap,
                           Collection<? extends Serializable> whereIsNullCols) {
        return $mapDelete(classTableName(table),
                lm2names(whereMap),
                lm2names(whereIsNullCols),
                null);
    }

    public H $lambdaDelete(Class<?> table,
                           Map<? extends Serializable, Object> whereMap,
                           Collection<? extends Serializable> whereIsNullCols,
                           Collection<? extends Serializable> whereIsNotNullCols) {
        return $mapDelete(classTableName(table),
                lm2names(whereMap),
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }

    public H $lambdaUpdate(Class<?> table,
                           Map<? extends Serializable, Object> updateMap,
                           Map<? extends Serializable, Object> whereMap) {
        return $mapUpdate(classTableName(table),
                lm2names(updateMap),
                null,
                lm2names(whereMap),
                null,
                null);
    }

    public H $lambdaUpdate(Class<?> table,
                           Map<? extends Serializable, Object> updateMap,
                           Collection<? extends Serializable> updateNullCols,
                           Map<? extends Serializable, Object> whereMap) {
        return $mapUpdate(classTableName(table),
                lm2names(updateMap),
                lm2names(updateNullCols),
                lm2names(whereMap),
                null,
                null);
    }

    public H $lambdaUpdate(Class<?> table,
                           Map<? extends Serializable, Object> updateMap,
                           Collection<? extends Serializable> updateNullCols,
                           Map<? extends Serializable, Object> whereMap,
                           Collection<? extends Serializable> whereIsNullCols) {
        return $mapUpdate(classTableName(table),
                lm2names(updateMap),
                lm2names(updateNullCols),
                lm2names(whereMap),
                lm2names(whereIsNullCols),
                null);
    }

    public H $lambdaUpdate(Class<?> table,
                           Map<? extends Serializable, Object> updateMap,
                           Collection<? extends Serializable> updateNullCols,
                           Map<? extends Serializable, Object> whereMap,
                           Collection<? extends Serializable> whereIsNullCols,
                           Collection<? extends Serializable> whereIsNotNullCols) {
        return $mapUpdate(classTableName(table),
                lm2names(updateMap),
                lm2names(updateNullCols),
                lm2names(whereMap),
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }

    public H $lambdaSet(Map<? extends Serializable, Object> updateMap) {
        return $mapSet(lm2names(updateMap),
                null);
    }

    public H $lambdaSet(Map<? extends Serializable, Object> updateMap,
                        Collection<? extends Serializable> updateNullCols) {
        return $mapSet(lm2names(updateMap),
                lm2names(updateNullCols));
    }

    public H $lambdaQuery(Class<?> table,
                          Collection<? extends Serializable> cols,
                          Map<? extends Serializable, Object> whereMap) {
        return $mapQuery(classTableName(table),
                lm2names(cols),
                lm2names(whereMap));
    }

    public H $lambdaQuery(Class<?> table,
                          String tableAlias,
                          Map<? extends Serializable, String> colMap,
                          Map<? extends Serializable, Object> whereMap,
                          Collection<? extends Serializable> whereIsNullCols,
                          Collection<? extends Serializable> whereIsNotNullCols,
                          Collection<? extends Serializable> groupCols,
                          Collection<? extends Serializable> orderCols) {
        return $mapQuery(classTableName(table),
                tableAlias,
                lm2names(colMap),
                lm2names(whereMap),
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols),
                lm2names(groupCols),
                lm2names(orderCols));
    }

    public H $lambdaSelect(Collection<? extends Serializable> cols) {
        return $mapSelect(lm2names(cols));
    }

    public H $lambdaSelect(String tableAlias,
                           Collection<? extends Serializable> cols) {
        return $mapSelect(tableAlias,
                lm2names(cols));
    }

    public H $lambdaSelect(String tableAlias,
                           Map<? extends Serializable, String> colMap) {
        return $mapSelect(tableAlias,
                lm2names(colMap));
    }

    public H $lambdaWhere(Map<? extends Serializable, Object> whereMap) {
        return $mapWhere(lm2names(whereMap));
    }

    public H $lambdaWhere(String tableAlias,
                          Map<? extends Serializable, Object> whereMap) {
        return $mapWhere(tableAlias,
                lm2names(whereMap));
    }

    public H $lambdaWhere(Map<? extends Serializable, Object> whereMap,
                          Collection<? extends Serializable> whereIsNullCols) {
        return $mapWhere(lm2names(whereMap),
                lm2names(whereIsNullCols));
    }

    public H $lambdaWhere(String tableAlias,
                          Map<? extends Serializable, Object> whereMap,
                          Collection<? extends Serializable> whereIsNullCols) {
        return $mapWhere(tableAlias,
                lm2names(whereMap),
                lm2names(whereIsNullCols));
    }

    public H $lambdaWhere(String tableAlias,
                          Map<? extends Serializable, Object> whereMap,
                          Collection<? extends Serializable> whereIsNullCols,
                          Collection<? extends Serializable> whereIsNotNullCols) {
        return $mapWhere(tableAlias,
                lm2names(whereMap),
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }

    public H $lambdaGroupBy(String tableAlias, Serializable... groupCols) {
        return $lambdaGroupBy(tableAlias, $arr2list(groupCols));
    }

    public H $lambdaGroupBy(String tableAlias, Collection<? extends Serializable> groupCols) {
        return $mapGroupBy(tableAlias, lm2names(groupCols));
    }

    public H $lambdaOrderBy(String tableAlias, Serializable... orderCols) {
        return $lambdaOrderBy(tableAlias, $arr2list(orderCols));
    }

    public H $lambdaOrderBy(String tableAlias, Collection<? extends Serializable> orderCols) {
        return $mapOrderBy(tableAlias, lm2names(orderCols));
    }

    public H $from(Class<?> table, String tableAlias) {
        return $from(classTableName(table), tableAlias);
    }


    public H $from(Class<?> clazz) {
        return $from(classTableName(clazz));
    }


    public H $join(Class<?> table, String tableAlias) {
        return $join(classTableName(table), tableAlias);
    }


    public H $join(Class<?> table) {
        return $join(classTableName(table));
    }

    public H $selectLambdas(Serializable... list) {
        return $selectLambdas(Bql.$arr2list(list));
    }

    public H $selectLambdas(Collection<? extends Serializable> list) {
        return $select(() -> {
            Bql<?> ret = Bql.$lambda().$sepComma();
            for (Serializable lambda : list) {
                if (lambda == null) {
                    continue;
                }
                if (lambda instanceof IFunctional) {
                    ret.$col(lambdaFieldName((IFunctional) lambda));
                } else {
                    ret.$col(String.valueOf(lambda));
                }
            }
            return ret;
        });
    }


    public H $selectMixes(Serializable... list) {
        return $selectMixes(Bql.$arr2list(list));
    }

    public H $selectMixes(Collection<? extends Serializable> list) {
        return $select(() -> {
            Bql<?> ret = Bql.$lambda().$sepComma();
            for (Object lambda : list) {
                if (lambda == null) {
                    continue;
                }
                if (lambda instanceof IFunctional) {
                    ret.$col(lambdaFieldName((IFunctional) lambda));
                } else {
                    ret.$col(String.valueOf(lambda));
                }
            }
            return ret;
        });
    }


    public <CT, CV, CR> H $colAs(String column, IGetter<CT, CR> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public <CT, CV> H $colAs(String column, ISetter<CT, CV> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public <CT, CV, CR> H $colAs(String column, IBuilder<CT, CV, CR> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }

    public <CT> H $colAs(String column, IExecute<CT> asAlias, Object... args) {
        return $colAs(column, lambdaFieldName(asAlias), args);
    }


    public <T, R> H $col(String tableAlias, IGetter<T, R> column, String asAlias) {
        return $col(tableAlias, lambdaFieldName(column), asAlias);
    }

    public <T, V> H $col(String tableAlias, ISetter<T, V> column, String asAlias) {
        return $col(tableAlias, lambdaFieldName(column), asAlias);
    }

    public <T, V, R> H $col(String tableAlias, IBuilder<T, V, R> column, String asAlias) {
        return $col(tableAlias, lambdaFieldName(column), asAlias);
    }

    public <T> H $col(String tableAlias, IExecute<T> column, String asAlias) {
        return $col(tableAlias, lambdaFieldName(column), asAlias);
    }


    public <T, R> H $col(String tableAlias, String column, IGetter<T, R> asAlias) {
        return $col(tableAlias, column, lambdaFieldName(asAlias));
    }

    public <T, V> H $col(String tableAlias, String column, ISetter<T, V> asAlias) {
        return $col(tableAlias, column, lambdaFieldName(asAlias));
    }

    public <T, V, R> H $col(String tableAlias, String column, IBuilder<T, V, R> asAlias) {
        return $col(tableAlias, column, lambdaFieldName(asAlias));
    }

    public <T> H $col(String tableAlias, String column, IExecute<T> asAlias) {
        return $col(tableAlias, column, lambdaFieldName(asAlias));
    }


    public <T, R> H $col(IGetter<T, R> column, String asAlias) {
        return $col(lambdaFieldName(column), asAlias);
    }

    public <T, V> H $col(ISetter<T, V> column, String asAlias) {
        return $col(lambdaFieldName(column), asAlias);
    }

    public <T, V, R> H $col(IBuilder<T, V, R> column, String asAlias) {
        return $col(lambdaFieldName(column), asAlias);
    }

    public <T> H $col(IExecute<T> column, String asAlias) {
        return $col(lambdaFieldName(column), asAlias);
    }


    public <T, R> H $col(String column, IGetter<T, R> asAlias) {
        return $col(column, lambdaFieldName(asAlias));
    }

    public <T, V> H $col(String column, ISetter<T, V> asAlias) {
        return $col(column, lambdaFieldName(asAlias));
    }

    public <T, V, R> H $col(String column, IBuilder<T, V, R> asAlias) {
        return $col(column, lambdaFieldName(asAlias));
    }

    public <T> H $col(String column, IExecute<T> asAlias) {
        return $col(column, lambdaFieldName(asAlias));
    }


    public <T, R> H $col(IGetter<T, R> column) {
        return $col(lambdaFieldName(column));
    }

    public <T, V> H $col(ISetter<T, V> column) {
        return $col(lambdaFieldName(column));
    }

    public <T, V, R> H $col(IBuilder<T, V, R> column) {
        return $col(lambdaFieldName(column));
    }

    public <T> H $col(IExecute<T> column) {
        return $col(lambdaFieldName(column));
    }


    public String lambdaFieldName(IFunctional lambda) {
        Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
        return fieldNameResolver.getName(field);
    }

    public String classTableName(Class<?> clazz) {
        return tableNameResolver.getName(clazz);
    }


    public <T, CT, CR> H $eq(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $eq(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $eq(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $eq(IExecute<CT> column, T val, Predicate<T> filter) {
        return $eq(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $eq(IGetter<CT, CR> column, T val) {
        return $eq(lambdaFieldName(column), val, defaultFilter());
    }

    public <T, CT, CV> H $eq(ISetter<CT, CV> column, T val) {
        return $eq(lambdaFieldName(column), val, defaultFilter());
    }

    public <T, CT, CV, CR> H $eq(IBuilder<CT, CV, CR> column, T val) {
        return $eq(lambdaFieldName(column), val, defaultFilter());
    }

    public <T, CT> H $eq(IExecute<CT> column, T val) {
        return $eq(lambdaFieldName(column), val, defaultFilter());
    }


    public <CT, CR> H $eqNull(IGetter<CT, CR> column) {
        return $eqNull(lambdaFieldName(column));
    }

    public <CT, CV> H $eqNull(ISetter<CT, CV> column) {
        return $eqNull(lambdaFieldName(column));
    }

    public <CT, CV, CR> H $eqNull(IBuilder<CT, CV, CR> column) {
        return $eqNull(lambdaFieldName(column));
    }

    public <CT> H $eqNull(IExecute<CT> column) {
        return $eqNull(lambdaFieldName(column));
    }

    public <T, CT, CR> H $like(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $like(ISetter<CT, CT> column, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CR, CV> H $like(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $like(IExecute<CT> column, T val, Predicate<T> filter) {
        return $like(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $like(IGetter<CT, CR> column, T val) {
        return $like(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $like(ISetter<CT, CV> column, T val) {
        return $like(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $like(IBuilder<CT, CV, CR> column, T val) {
        return $like(lambdaFieldName(column), val);
    }

    public <T, CT> H $like(IExecute<CT> column, T val) {
        return $like(lambdaFieldName(column), val);
    }


    public <T, CT, CR> H $instr(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $instr(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $instr(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $instr(IExecute<CT> column, T val, Predicate<T> filter) {
        return $instr(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $instr(IGetter<CT, CR> column, T val) {
        return $instr(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $instr(ISetter<CT, CV> column, T val) {
        return $instr(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $instr(IBuilder<CT, CV, CR> column, T val) {
        return $instr(lambdaFieldName(column), val);
    }

    public <T, CT> H $instr(IExecute<CT> column, T val) {
        return $instr(lambdaFieldName(column), val);
    }

    public <T, CT, CR> H $gt(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $gt(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $gt(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $gt(IExecute<CT> column, T val, Predicate<T> filter) {
        return $gt(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $gt(IGetter<CT, CR> column, T val) {
        return $gt(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $gt(ISetter<CT, CV> column, T val) {
        return $gt(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $gt(IBuilder<CT, CV, CR> column, T val) {
        return $gt(lambdaFieldName(column), val);
    }

    public <T, CT> H $gt(IExecute<CT> column, T val) {
        return $gt(lambdaFieldName(column), val);
    }


    public <T, CT, CR> H $lt(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $lt(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $lt(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $lt(IExecute<CT> column, T val, Predicate<T> filter) {
        return $lt(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $lt(IGetter<CT, CR> column, T val) {
        return $lt(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $lt(ISetter<CT, CV> column, T val) {
        return $lt(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $lt(IBuilder<CT, CV, CR> column, T val) {
        return $lt(lambdaFieldName(column), val);
    }

    public <T, CT> H $lt(IExecute<CT> column, T val) {
        return $lt(lambdaFieldName(column), val);
    }

    public <T, CT, CR> H $gte(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $gte(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $gte(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $gte(IExecute<CT> column, T val, Predicate<T> filter) {
        return $gte(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $gte(IGetter<CT, CR> column, T val) {
        return $gte(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $gte(ISetter<CT, CV> column, T val) {
        return $gte(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $gte(IBuilder<CT, CV, CR> column, T val) {
        return $gte(lambdaFieldName(column), val);
    }

    public <T, CT> H $gte(IExecute<CT> column, T val) {
        return $gte(lambdaFieldName(column), val);
    }


    public <T, CT, CR> H $lte(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $lte(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $lte(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $lte(IExecute<CT> column, T val, Predicate<T> filter) {
        return $lte(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $lte(IGetter<CT, CR> column, T val) {
        return $lte(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $lte(ISetter<CT, CV> column, T val) {
        return $lte(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $lte(IBuilder<CT, CV, CR> column, T val) {
        return $lte(lambdaFieldName(column), val);
    }

    public <T, CT> H $lte(IExecute<CT> column, T val) {
        return $lte(lambdaFieldName(column), val);
    }


    public <T, CT, CR> H $neq(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $neq(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $neq(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $neq(IExecute<CT> column, T val, Predicate<T> filter) {
        return $neq(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $neq(IGetter<CT, CR> column, T val) {
        return $neq(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $neq(ISetter<CT, CV> column, T val) {
        return $neq(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $neq(IBuilder<CT, CV, CR> column, T val) {
        return $neq(lambdaFieldName(column), val);
    }

    public <T, CT> H $neq(IExecute<CT> column, T val) {
        return $neq(lambdaFieldName(column), val);
    }


    public <T, CT, CR> H $ne(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $ne(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $ne(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $ne(IExecute<CT> column, T val, Predicate<T> filter) {
        return $ne(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CR> H $ne(IGetter<CT, CR> column, T val) {
        return $ne(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $ne(ISetter<CT, CV> column, T val) {
        return $ne(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $ne(IBuilder<CT, CV, CR> column, T val) {
        return $ne(lambdaFieldName(column), val);
    }

    public <T, CT> H $ne(IExecute<CT> column, T val) {
        return $ne(lambdaFieldName(column), val);
    }


    public <T, CT, CR> H $isNull(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $isNull(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $isNull(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $isNull(IExecute<CT> column, T val, Predicate<T> filter) {
        return $isNull(lambdaFieldName(column), val, filter);
    }


    public <T, CT, CR> H $isNull(IGetter<CT, CR> column, T val) {
        return $isNull(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $isNull(ISetter<CT, CV> column, T val) {
        return $isNull(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $isNull(IBuilder<CT, CV, CR> column, T val) {
        return $isNull(lambdaFieldName(column), val);
    }

    public <T, CT> H $isNull(IExecute<CT> column, T val) {
        return $isNull(lambdaFieldName(column), val);
    }


    public <CT, CR> H $isNull(IGetter<CT, CR> column) {
        return $isNull(lambdaFieldName(column));
    }

    public <CT, CV> H $isNull(ISetter<CT, CV> column) {
        return $isNull(lambdaFieldName(column));
    }

    public <CT, CV, CR> H $isNull(IBuilder<CT, CV, CR> column) {
        return $isNull(lambdaFieldName(column));
    }

    public <CT> H $isNull(IExecute<CT> column) {
        return $isNull(lambdaFieldName(column));
    }


    public <T, CT, CR> H $isNotNull(IGetter<CT, CR> column, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV> H $isNotNull(ISetter<CT, CV> column, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CV, CR> H $isNotNull(IBuilder<CT, CV, CR> column, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT> H $isNotNull(IExecute<CT> column, T val, Predicate<T> filter) {
        return $isNotNull(lambdaFieldName(column), val, filter);
    }

    public <T, CT, CR> H $isNotNull(IGetter<CT, CR> column, T val) {
        return $isNotNull(lambdaFieldName(column), val);
    }

    public <T, CT, CV> H $isNotNull(ISetter<CT, CV> column, T val) {
        return $isNotNull(lambdaFieldName(column), val);
    }

    public <T, CT, CV, CR> H $isNotNull(IBuilder<CT, CV, CR> column, T val) {
        return $isNotNull(lambdaFieldName(column), val);
    }

    public <T, CT> H $isNotNull(IExecute<CT> column, T val) {
        return $isNotNull(lambdaFieldName(column), val);
    }

    public <CT, CR> H $isNotNull(IGetter<CT, CR> column) {
        return $isNotNull(lambdaFieldName(column));
    }

    public <CT, CV> H $isNotNull(ISetter<CT, CV> column) {
        return $isNotNull(lambdaFieldName(column));
    }

    public <CT, CV, CR> H $isNotNull(IBuilder<CT, CV, CR> column) {
        return $isNotNull(lambdaFieldName(column));
    }

    public <CT> H $isNotNull(IExecute<CT> column) {
        return $isNotNull(lambdaFieldName(column));
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $in(IGetter<CT, CR> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> H $in(ISetter<CT, CV> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $in(IBuilder<CT, CV, CR> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT> H $in(IExecute<CT> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, filter, itemFilter);
    }


    public <T, C extends Collection<T>, CT, CR> H $in(IGetter<CT, CR> column, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> H $in(ISetter<CT, CV> column, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $in(IBuilder<CT, CV, CR> column, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT> H $in(IExecute<CT> column, C val, Predicate<T> itemFilter) {
        return $in(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CR> H $in(IGetter<CT, CR> column, C val) {
        return $in(lambdaFieldName(column), val);
    }

    public <T, C extends Collection<T>, CT, CV> H $in(ISetter<CT, CV> column, C val) {
        return $in(lambdaFieldName(column), val);
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $in(IBuilder<CT, CV, CR> column, C val) {
        return $in(lambdaFieldName(column), val);
    }

    public <T, C extends Collection<T>, CT> H $in(IExecute<CT> column, C val) {
        return $in(lambdaFieldName(column), val);
    }


    public <T, C extends Collection<T>, CT, CV, CR> H $notIn(IGetter<CT, CR> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> H $notIn(ISetter<CT, CV> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $notIn(IBuilder<CT, CV, CR> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, filter, itemFilter);
    }

    public <T, C extends Collection<T>, CT> H $notIn(IExecute<CT> column, C val, Predicate<C> filter, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, filter, itemFilter);
    }


    public <T, C extends Collection<T>, CT, CV, CR> H $notIn(IGetter<CT, CR> column, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV> H $notIn(ISetter<CT, CV> column, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $notIn(IBuilder<CT, CV, CR> column, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT> H $notIn(IExecute<CT> column, C val, Predicate<T> itemFilter) {
        return $notIn(lambdaFieldName(column), val, itemFilter);
    }

    public <T, C extends Collection<T>, CT, CR> H $notIn(IGetter<CT, CR> column, C val) {
        return $notIn(lambdaFieldName(column), val);
    }

    public <T, C extends Collection<T>, CT, CV> H $notIn(ISetter<CT, CV> column, C val) {
        return $notIn(lambdaFieldName(column), val);
    }

    public <T, C extends Collection<T>, CT, CV, CR> H $notIn(IBuilder<CT, CV, CR> column, C val) {
        return $notIn(lambdaFieldName(column), val);
    }

    public <T, C extends Collection<T>, CT> H $notIn(IExecute<CT> column, C val) {
        return $notIn(lambdaFieldName(column), val);
    }

    public <T, CT, CR> H $between(IGetter<CT, CR> column, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(column), min, max, filter);
    }

    public <T, CT, CV> H $between(ISetter<CT, CV> column, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(column), min, max, filter);
    }

    public <T, CT, CV, CR> H $between(IBuilder<CT, CV, CR> column, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(column), min, max, filter);
    }

    public <T, CT> H $between(IExecute<CT> column, T min, T max, Predicate<T> filter) {
        return $between(lambdaFieldName(column), min, max, filter);
    }


    public <T, CT, CR> H $between(IGetter<CT, CR> column, T min, T max) {
        return $between(lambdaFieldName(column), min, max);
    }

    public <T, CT, CV> H $between(ISetter<CT, CV> column, T min, T max) {
        return $between(lambdaFieldName(column), min, max);
    }

    public <T, CT, CV, CR> H $between(IBuilder<CT, CV, CR> column, T min, T max) {
        return $between(lambdaFieldName(column), min, max);
    }

    public <T, CT> H $between(IExecute<CT> column, T min, T max) {
        return $between(lambdaFieldName(column), min, max);
    }

    public H $into(Class<?> table) {
        return $into(classTableName(table));
    }


    public H $into(Class<?> table, Supplier<i2f.bql.core.Bql<?>> caller) {
        return $into(classTableName(table), caller);
    }

    public H $update(Class<?> table) {
        return $update(classTableName(table));
    }


    public H $deleteFrom(Class<?> table) {
        return $deleteFrom(classTableName(table));
    }

    public H $table(Class<?> table) {
        return $table(classTableName(table));
    }


    public <CT, CR> H $foreignKeyReferences(Class<?> table, IGetter<CT, CR> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public <CT, CV> H $foreignKeyReferences(Class<?> table, ISetter<CT, CV> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public <CT, CV, CR> H $foreignKeyReferences(Class<?> table, IBuilder<CT, CV, CR> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public <CT> H $foreignKeyReferences(Class<?> table, IExecute<CT> column) {
        return $foreignKeyReferences(classTableName(table), lambdaFieldName(column));
    }

    public H $truncateTable(Class<?> table) {
        return $truncateTable(classTableName(table));
    }

}

