package i2f.bql.core.bean;

import i2f.bindsql.BindSql;
import i2f.bql.bean.BeanResolver;
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

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        for (Field field : fields) {
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

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        List<Map<String, Object>> values = new LinkedList<>();

        for (T item : list) {

            Map<String, Object> valueMap = new LinkedHashMap<>();
            for (Field field : fields) {
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

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        List<Map<String, Object>> values = new LinkedList<>();

        for (T item : list) {

            Map<String, Object> valueMap = new LinkedHashMap<>();
            for (Field field : fields) {
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

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        for (Field field : fields) {
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

        Set<Field> fields = BeanResolver.getDbFields(clazz);

        for (Field field : fields) {
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
        Set<Field> fields = BeanResolver.getDbFields(clazz);
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
        for (Field field : fields) {
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

}
