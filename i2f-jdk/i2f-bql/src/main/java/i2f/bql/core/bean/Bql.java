package i2f.bql.core.bean;

import i2f.bindsql.BindSql;
import i2f.database.metadata.bean.BeanDatabaseMetadataResolver;
import i2f.functional.IFunctional;
import i2f.lambda.inflater.LambdaInflater;
import i2f.reflect.ReflectResolver;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/4/23 10:44
 * @desc
 */
public class Bql<H extends Bql<H>> extends i2f.bql.core.lambda.Bql<H> {
    public static <H extends Bql<H>> Bql<H> $bean() {
        return new Bql();
    }

    public static <H extends Bql<H>> Bql<H> $bean(BindSql bql) {
        return (Bql<H>) new Bql().$(bql);
    }

    public static <H extends Bql<H>> Bql<H> $bean(i2f.bql.core.Bql<?> val) {
        return (Bql<H>) new Bql().$(val);
    }

    public static <H extends Bql<H>> Bql<H> $bean(Supplier<i2f.bql.core.Bql<?>> caller) {
        return (Bql<H>) new Bql().$(caller);
    }

    public static <H extends Bql<H>> Bql<H> $bean(String sql, Object args) {
        return (Bql<H>) new Bql().$(sql, args);
    }

    public <T> H $beanInsert(T bean) {
        if (bean == null) {
            return (H) this;
        }
        Class<?> clazz = bean.getClass();
        Map<String, Object> valueMap = new LinkedHashMap<>();

        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);

        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);

            if (bean != null) {
                try {
                    Object val = ReflectResolver.valueGet(bean, field);
                    valueMap.put(colName, val);
                } catch (Exception e) {

                }
            }
        }

        return $mapInsert(classTableName(clazz), valueMap);
    }

    public <T> H $beanInsertBatchValues(List<T> list) {
        if (list == null || list.isEmpty()) {
            return (H) this;
        }
        T bean = list.get(0);
        Class<?> clazz = bean.getClass();

        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);

        List<Map<String, Object>> values = new LinkedList<>();

        for (T item : list) {

            Map<String, Object> valueMap = new LinkedHashMap<>();
            for (Field field : fields.keySet()) {
                String colName = fieldNameResolver.getName(field);
                if (item != null) {
                    try {
                        Object val = ReflectResolver.valueGet(item, field);
                        valueMap.put(colName, val);
                    } catch (Exception e) {

                    }
                }
            }

            values.add(valueMap);
        }

        return $mapInsertBatchValues(classTableName(clazz), values);
    }

    public <T> H $beanInsertBatchUnionAll(List<T> list) {
        if (list == null || list.isEmpty()) {
            return (H) this;
        }
        T bean = list.get(0);
        Class<?> clazz = bean.getClass();

        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);

        List<Map<String, Object>> values = new LinkedList<>();

        for (T item : list) {

            Map<String, Object> valueMap = new LinkedHashMap<>();
            for (Field field : fields.keySet()) {
                String colName = fieldNameResolver.getName(field);

                if (item != null) {
                    try {
                        Object val = ReflectResolver.valueGet(item, field);
                        valueMap.put(colName, val);
                    } catch (Exception e) {

                    }
                }
            }

            values.add(valueMap);
        }

        return $mapInsertBatchUnionAll(classTableName(clazz), values);
    }

    public <T> H $beanDelete(T bean) {
        return $beanDelete(bean, null, null);
    }

    public <T> H $beanDelete(T bean,
                             Collection<? extends Serializable> whereIsNullCols) {
        return $beanDelete(bean, whereIsNullCols, null);
    }

    public <T> H $beanDelete(T bean,
                             Collection<? extends Serializable> whereIsNullCols,
                             Collection<? extends Serializable> whereIsNotNullCols) {
        if (bean == null) {
            return (H) this;
        }
        Class<?> clazz = bean.getClass();
        Map<String, Object> whereMap = new LinkedHashMap<>();

        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);

        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);

            if (bean != null) {
                try {
                    Object val = ReflectResolver.valueGet(bean, field);
                    whereMap.put(colName, val);
                } catch (Exception e) {

                }
            }
        }

        return $mapDelete(classTableName(clazz),
                whereMap,
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }

    public <T, V extends Serializable> H $beanDeleteByPk(Class<T> beanClass, V... pkValue) {
        Map<String, Object> whereMap = new LinkedHashMap<>();
        Map.Entry<Field, String> primary = BeanDatabaseMetadataResolver.getTablePrimary(beanClass);
        if (primary == null) {
            throw new IllegalArgumentException("missing @Primary on bean class : " + beanClass);
        }
        Field field = primary.getKey();
        String name = fieldNameResolver.getName(field);
        whereMap.put(name, pkValue);
        return $mapDelete(classTableName(beanClass),
                whereMap);
    }

    public <T, V extends Serializable> H $beanDeleteByPk(Class<T> beanClass, Collection<V> pkValue) {
        Map<String, Object> whereMap = new LinkedHashMap<>();
        Map.Entry<Field, String> primary = BeanDatabaseMetadataResolver.getTablePrimary(beanClass);
        if (primary == null) {
            throw new IllegalArgumentException("missing @Primary on bean class : " + beanClass);
        }
        Field field = primary.getKey();
        String name = fieldNameResolver.getName(field);
        whereMap.put(name, pkValue);
        return $mapDelete(classTableName(beanClass),
                whereMap);
    }

    public <T> H $beanUpdate(T update, T cond) {
        return $beanUpdate(update, cond, null, null, null);
    }

    public <T> H $beanUpdate(T update, T cond, Collection<? extends Serializable> updateNullCols) {
        return $beanUpdate(update, cond, updateNullCols, null, null);
    }

    public <T> H $beanUpdate(T update, T cond,
                             Collection<? extends Serializable> updateNullCols,
                             Collection<? extends Serializable> whereIsNullCols,
                             Collection<? extends Serializable> whereIsNotNullCols) {
        if (update == null) {
            return (H) this;
        }
        Class<?> clazz = update.getClass();
        Map<String, Object> updateMap = new LinkedHashMap<>();
        Map<String, Object> whereMap = new LinkedHashMap<>();

        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);

        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);
            if (update != null) {
                try {
                    Object val = ReflectResolver.valueGet(update, field);
                    updateMap.put(colName, val);
                } catch (Exception e) {

                }
            }

            if (cond != null) {
                try {
                    Object val = ReflectResolver.valueGet(cond, field);
                    whereMap.put(colName, val);
                } catch (Exception e) {

                }
            }
        }

        return $mapUpdate(classTableName(clazz),
                updateMap,
                lm2names(updateNullCols),
                whereMap,
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols));
    }

    public <T> H $beanUpdateByPk(T update) {
        return $beanUpdateByPk(update, null);
    }

    public <T> H $beanUpdateByPk(T update,
                                 Collection<? extends Serializable> updateNullCols) {
        if (update == null) {
            return (H) this;
        }
        Class<?> clazz = update.getClass();
        Map<String, Object> updateMap = new LinkedHashMap<>();
        Map<String, Object> whereMap = new LinkedHashMap<>();

        Map.Entry<Field, String> primary = BeanDatabaseMetadataResolver.getTablePrimary(clazz);
        if (primary == null) {
            throw new IllegalArgumentException("missing @Primary on bean class : " + clazz);
        }
        if (primary != null) {
            try {
                Field field = primary.getKey();
                String colName = fieldNameResolver.getName(field);
                Object val = ReflectResolver.valueGet(update, field);
                whereMap.put(colName, val);
            } catch (Exception e) {

            }
        }

        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);

        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);
            if (update != null) {
                try {
                    Object val = ReflectResolver.valueGet(update, field);
                    updateMap.put(colName, val);
                } catch (Exception e) {

                }
            }
        }


        return $mapUpdate(classTableName(clazz),
                updateMap,
                lm2names(updateNullCols),
                whereMap,
                null,
                null);
    }


    public <T> H $beanQuery(T bean) {
        return $beanQuery(bean, null, null);
    }

    public <T> H $beanQuery(T bean, Collection<? extends Serializable> selectCols) {
        return $beanQuery(bean, null, selectCols);
    }

    public <T> H $beanQuery(T bean, String alias) {
        return $beanQuery(bean, alias, null);
    }

    public <T> H $beanQuery(T bean, String alias, Collection<? extends Serializable> selectCols) {
        return $beanQuery(bean, alias, selectCols, null);
    }

    public <T> H $beanQuery(T bean, String alias, Collection<? extends Serializable> selectCols, Collection<? extends Serializable> selectExcludeCols) {
        return $beanQuery(bean, alias, selectCols, selectExcludeCols, null, null, null);
    }

    public <T> H $beanQuery(T bean, String alias,
                            Collection<? extends Serializable> selectCols,
                            Collection<? extends Serializable> selectExcludeCols,
                            Collection<? extends Serializable> whereIsNullCols,
                            Collection<? extends Serializable> whereIsNotNullCols,
                            Collection<? extends Serializable> orderCols) {
        if (bean == null) {
            return (H) this;
        }
        Class<?> clazz = bean.getClass();
        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(clazz);
        Map<String, String> colMap = new LinkedHashMap<>();
        if (selectCols != null && !selectCols.isEmpty()) {
            for (Serializable lambda : selectCols) {
                if (lambda instanceof IFunctional) {
                    Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
                    String colName = fieldNameResolver.getName(field);
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                } else {
                    colMap.put(String.valueOf(lambda), null);
                }
            }
        }
        Set<String> excludeNames = new HashSet<>();
        if (selectExcludeCols != null) {
            for (Serializable lambda : selectExcludeCols) {
                if (lambda instanceof IFunctional) {
                    excludeNames.add(lambdaFieldName((IFunctional) lambda));
                } else {
                    excludeNames.add(String.valueOf(lambda));
                }
            }
        }
        Map<String, Object> whereMap = new LinkedHashMap<>();
        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);
            if (selectCols == null || selectCols.isEmpty()) {
                if (!excludeNames.contains(field.getName())) {
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                }
            }

            try {
                Object val = ReflectResolver.valueGet(bean, field);
                whereMap.put(colName, val);
            } catch (Exception e) {

            }
        }
        List<String> orderList = lm2names(orderCols);
        List<String> orderNames = new ArrayList<>();
        if (orderList != null) {
            for (String name : orderList) {
                if (name == null) {
                    continue;
                }
                orderNames.add(name);
            }
        }
        return $mapQuery(classTableName(clazz),
                alias,
                colMap,
                whereMap,
                lm2names(whereIsNullCols),
                lm2names(whereIsNotNullCols),
                null,
                orderNames);
    }

    public <T, V extends Serializable> H $beanQueryByPk(Class<T> beanClass,
                                                        V... pkValue) {
        return $beanQueryByPk(beanClass, null, null, null, null, pkValue);
    }

    public <T, V extends Serializable> H $beanQueryByPk(Class<T> beanClass,
                                                        Collection<V> pkValue) {
        return $beanQueryByPk(beanClass, null, null, null, null, pkValue);
    }

    public <T, V extends Serializable> H $beanQueryByPk(Class<T> beanClass,
                                                        String alias,
                                                        Collection<? extends Serializable> selectCols,
                                                        Collection<? extends Serializable> selectExcludeCols,
                                                        Collection<? extends Serializable> orderCols,
                                                        V... pkValue) {
        Map.Entry<Field, String> primary = BeanDatabaseMetadataResolver.getTablePrimary(beanClass);
        if (primary == null) {
            throw new IllegalArgumentException("missing @Primary on bean class : " + beanClass);
        }
        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(beanClass);
        Map<String, String> colMap = new LinkedHashMap<>();
        if (selectCols != null && !selectCols.isEmpty()) {
            for (Serializable lambda : selectCols) {
                if (lambda instanceof IFunctional) {
                    Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
                    String colName = fieldNameResolver.getName(field);
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                } else {
                    colMap.put(String.valueOf(lambda), null);
                }
            }
        }
        Set<String> excludeNames = new HashSet<>();
        if (selectExcludeCols != null) {
            for (Serializable lambda : selectExcludeCols) {
                if (lambda instanceof IFunctional) {
                    excludeNames.add(lambdaFieldName((IFunctional) lambda));
                } else {
                    excludeNames.add(String.valueOf(lambda));
                }
            }
        }
        Map<String, Object> whereMap = new LinkedHashMap<>();
        if (primary != null) {
            String colName = fieldNameResolver.getName(primary.getKey());
            whereMap.put(colName, pkValue);
        }

        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);
            if (selectCols == null || selectCols.isEmpty()) {
                if (!excludeNames.contains(field.getName())) {
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                }
            }
        }

        List<String> orderList = lm2names(orderCols);
        List<String> orderNames = new ArrayList<>();
        if (orderList != null) {
            for (String name : orderList) {
                if (name == null) {
                    continue;
                }
                orderNames.add(name);
            }
        }
        return $mapQuery(classTableName(beanClass),
                alias,
                colMap,
                whereMap,
                null,
                null,
                null,
                orderNames);
    }

    public <T, V extends Serializable> H $beanQueryByPk(Class<T> beanClass,
                                                        String alias,
                                                        Collection<? extends Serializable> selectCols,
                                                        Collection<? extends Serializable> selectExcludeCols,
                                                        Collection<? extends Serializable> orderCols,
                                                        Collection<V> pkValue) {
        Map.Entry<Field, String> primary = BeanDatabaseMetadataResolver.getTablePrimary(beanClass);
        if (primary == null) {
            throw new IllegalArgumentException("missing @Primary on bean class : " + beanClass);
        }
        Map<Field, String> fields = BeanDatabaseMetadataResolver.getTableColumns(beanClass);
        Map<String, String> colMap = new LinkedHashMap<>();
        if (selectCols != null && !selectCols.isEmpty()) {
            for (Serializable lambda : selectCols) {
                if (lambda instanceof IFunctional) {
                    Field field = LambdaInflater.fastSerializedLambdaFieldNullable(lambda);
                    String colName = fieldNameResolver.getName(field);
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                } else {
                    colMap.put(String.valueOf(lambda), null);
                }
            }
        }
        Set<String> excludeNames = new HashSet<>();
        if (selectExcludeCols != null) {
            for (Serializable lambda : selectExcludeCols) {
                if (lambda instanceof IFunctional) {
                    excludeNames.add(lambdaFieldName((IFunctional) lambda));
                } else {
                    excludeNames.add(String.valueOf(lambda));
                }
            }
        }
        Map<String, Object> whereMap = new LinkedHashMap<>();
        if (primary != null) {
            String colName = fieldNameResolver.getName(primary.getKey());
            whereMap.put(colName, pkValue);
        }

        for (Field field : fields.keySet()) {
            String colName = fieldNameResolver.getName(field);
            if (selectCols == null || selectCols.isEmpty()) {
                if (!excludeNames.contains(field.getName())) {
                    if (field.getName().equals(colName)) {
                        colMap.put(colName, null);
                    } else {
                        colMap.put(colName, field.getName());
                    }
                }
            }
        }

        List<String> orderList = lm2names(orderCols);
        List<String> orderNames = new ArrayList<>();
        if (orderList != null) {
            for (String name : orderList) {
                if (name == null) {
                    continue;
                }
                orderNames.add(name);
            }
        }
        return $mapQuery(classTableName(beanClass),
                alias,
                colMap,
                whereMap,
                null,
                null,
                null,
                orderNames);
    }

}
