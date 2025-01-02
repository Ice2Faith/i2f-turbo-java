package i2f.bql.core.map;

import i2f.bql.core.condition.Condition;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/1/2 19:53
 * @desc
 */
public class Bql<H extends i2f.bql.core.map.Bql<H>> extends i2f.bql.core.Bql<H> {

    public static <H extends i2f.bql.core.lambda.Bql<H>> i2f.bql.core.lambda.Bql<H> $lambda() {
        return new i2f.bql.core.lambda.Bql();
    }


    public static <H extends i2f.bql.core.bean.Bql<H>> i2f.bql.core.bean.Bql<H> $bean() {
        return new i2f.bql.core.bean.Bql();
    }

    public H $mapInsert(String table, Map<String, Object> valueMap) {
        return insert().$into(table, () -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma();
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                ret.$col(entry.getKey());
            }
            return ret;
        }).$values(() -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma();
            for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                ret.$var(entry.getValue());
            }
            return ret;
        });
    }

    public H $mapInsertBatchValues(String table, List<Map<String, Object>> values) {
        return insert().$into(table, () -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma();
            for (Map.Entry<String, Object> entry : values.get(0).entrySet()) {
                ret.$col(entry.getKey());
            }
            return ret;
        }).$trim(values, TRIM_COMMA_LIST,
                TRIM_COMMA_LIST,
                $keywords("values"), null,
                (list) -> i2f.bql.core.Bql.$_().
                        $for(list, ",", null, (i, v) -> i2f.bql.core.Bql.$_()
                                .$bracket(() ->
                                        {
                                            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma();
                                            for (Map.Entry<String, Object> entry : v.entrySet()) {
                                                ret.$var(entry.getValue());
                                            }
                                            return ret;
                                        }
                                )
                        )
        );
    }

    public H $mapInsertBatchUnionAll(String table, List<Map<String, Object>> values) {
        Set<String> colNames = new LinkedHashSet<>();
        for (Map.Entry<String, Object> entry : values.get(0).entrySet()) {
            colNames.add(entry.getKey());
        }
        return insert().$into(table, () -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma();
            for (String colName : colNames) {
                ret.$col(colName);
            }
            return ret;
        }).$trim(values, TRIM_UNION_LIST,
                TRIM_UNION_LIST,
                null, null,
                (list) -> i2f.bql.core.Bql.$_()
                        .$for(list, $keywords(" union all "), null,
                                (i, v) -> i2f.bql.core.Bql.$_()
                                        .$select(() -> {
                                                    i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma();
                                                    for (String colName : colNames) {
                                                        ret.$var(v.get(colName));
                                                    }
                                                    return ret;
                                                }
                                        ).$fromDual()
                        )
        );
    }

    public H $mapDelete(String table,
                        Map<String, Object> whereMap) {
        return $mapDelete(table, whereMap, null, null);
    }

    public H $mapDelete(String table,
                        Map<String, Object> whereMap,
                        Collection<String> whereIsNullCols) {
        return $mapDelete(table, whereMap, whereIsNullCols, null);
    }

    public H $mapDelete(String table,
                        Map<String, Object> whereMap,
                        Collection<String> whereIsNullCols,
                        Collection<String> whereIsNotNullCols) {
        return $deleteFrom(table)
                .$mapWhere(null, whereMap, whereIsNullCols, whereIsNotNullCols);
    }

    public H $mapUpdate(String table,
                        Map<String, Object> updateMap,
                        Map<String, Object> whereMap
    ) {
        return $mapUpdate(table,
                updateMap, null,
                whereMap, null, null);
    }

    public H $mapUpdate(String table,
                        Map<String, Object> updateMap,
                        Collection<String> updateNullCols,
                        Map<String, Object> whereMap
    ) {
        return $mapUpdate(table,
                updateMap, updateNullCols,
                whereMap, null, null);
    }

    public H $mapUpdate(String table,
                        Map<String, Object> updateMap,
                        Collection<String> updateNullCols,
                        Map<String, Object> whereMap,
                        Collection<String> whereIsNullCols
    ) {
        return $mapUpdate(table,
                updateMap, updateNullCols,
                whereMap, whereIsNullCols, null);
    }

    public H $mapUpdate(String table,
                        Map<String, Object> updateMap,
                        Collection<String> updateNullCols,
                        Map<String, Object> whereMap,
                        Collection<String> whereIsNullCols,
                        Collection<String> whereIsNotNullCols) {
        return $update(table)
                .$mapSet(updateMap, updateNullCols)
                .$mapWhere(null, whereMap, whereIsNullCols, whereIsNotNullCols);
    }

    public H $mapSet(Map<String, Object> updateMap) {
        return $mapSet(updateMap, null);
    }

    public H $mapSet(Map<String, Object> updateMap,
                     Collection<String> updateNullCols) {
        return $set(updateMap, v -> {
            return (updateMap != null && !updateMap.isEmpty())
                    || (updateNullCols != null && !updateNullCols.isEmpty());
        }, v -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma().$link();
            if (updateMap != null) {
                for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
                    String col = entry.getKey();
                    Object val = entry.getValue();
                    ret.$eq(col, val);
                }
            }
            if (updateNullCols != null) {
                for (String col : new LinkedHashSet<>(updateNullCols)) {
                    ret.$eqNull(col);
                }
            }
            return ret;
        });
    }


    public H $mapQuery(String table,
                       Collection<String> cols,
                       Map<String, Object> whereMap) {
        Map<String, String> colMap = new LinkedHashMap<>();
        for (String col : cols) {
            if (col == null) {
                continue;
            }
            colMap.put(col, null);
        }
        return $mapQuery(table, null,
                colMap,
                whereMap, null, null,
                null, null);
    }

    public H $mapQuery(String table,
                       String tableAlias,
                       Map<String, String> colMap,
                       Map<String, Object> whereMap,
                       Collection<String> whereIsNullCols,
                       Collection<String> whereIsNotNullCols,
                       Collection<String> groupCols,
                       Collection<String> orderCols) {

        return $mapSelect(tableAlias, colMap)
                .$from(table, tableAlias)
                .$mapWhere(tableAlias, whereMap, whereIsNullCols, whereIsNotNullCols)
                .$mapGroupBy(tableAlias, groupCols)
                .$mapOrderBy(tableAlias, orderCols);
    }

    public H $mapSelect(Collection<String> cols) {
        return $mapSelect(null, cols);
    }

    public H $mapSelect(String tableAlias,
                        Collection<String> cols) {
        Map<String, String> colMap = new LinkedHashMap<>();
        for (String col : cols) {
            if (col == null) {
                continue;
            }
            colMap.put(col, null);
        }
        return $mapSelect(tableAlias, colMap);
    }

    public H $mapSelect(String tableAlias,
                        Map<String, String> colMap) {
        return $select(() -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma().$alias(tableAlias);
            for (Map.Entry<String, String> entry : colMap.entrySet()) {
                String col = entry.getKey();
                String as = entry.getValue();
                ret.$col(col, as);
            }
            return ret;
        });
    }

    public H $mapWhere(Map<String, Object> whereMap) {
        return $mapWhere(null, whereMap,
                null, null);
    }

    public H $mapWhere(String tableAlias,
                       Map<String, Object> whereMap) {
        return $mapWhere(tableAlias, whereMap,
                null, null);
    }

    public H $mapWhere(Map<String, Object> whereMap,
                       Collection<String> whereIsNullCols) {
        return $mapWhere(null, whereMap,
                whereIsNullCols, null);
    }

    public H $mapWhere(String tableAlias,
                       Map<String, Object> whereMap,
                       Collection<String> whereIsNullCols) {
        return $mapWhere(tableAlias, whereMap,
                whereIsNullCols, null);
    }

    public H $mapWhere(String tableAlias,
                       Map<String, Object> whereMap,
                       Collection<String> whereIsNullCols,
                       Collection<String> whereIsNotNullCols) {
        return $where(whereMap, v -> {
            return (whereMap != null && !whereMap.isEmpty())
                    || (whereIsNullCols != null && !whereIsNullCols.isEmpty())
                    || (whereIsNotNullCols != null && !whereIsNotNullCols.isEmpty());
        }, (v) -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$alias(tableAlias);
            if (whereMap != null) {
                for (Map.Entry<String, Object> entry : whereMap.entrySet()) {
                    String col = entry.getKey();
                    Object val = entry.getValue();
                    if (val instanceof Condition) {
                        Condition cond = (Condition) val;
                        cond.apply(ret, col);
                    } else if (val instanceof Collection) {
                        if (val instanceof ArrayList) {
                            ArrayList<?> list = (ArrayList<?>) val;
                            if (list.size() == 1) {
                                ret.$eq(col, list.get(0));
                            } else {
                                ret.$in(col, list);
                            }
                        } else {
                            Collection<?> c = (Collection<?>) val;
                            ret.$in(col, c);
                        }
                    } else if (val instanceof Iterable) {
                        List<Object> list = new ArrayList<>();
                        Iterable<?> iter = (Iterable<?>) val;
                        for (Object o : iter) {
                            list.add(o);
                        }
                        ret.$in(col, list);
                    } else if (val != null && val.getClass().isArray()) {
                        int len = Array.getLength(val);
                        if (len == 1) {
                            ret.$eq(col, Array.get(val, 0));
                        } else {
                            List<Object> list = new ArrayList<>();
                            for (int i = 0; i < len; i++) {
                                list.add(Array.get(val, i));
                            }
                            ret.$in(col, list);
                        }
                    } else {
                        ret.$eq(col, val);
                    }

                }
            }
            if (whereIsNullCols != null) {
                for (String col : new LinkedHashSet<>(whereIsNullCols)) {
                    ret.$isNull(col);
                }
            }
            if (whereIsNotNullCols != null) {
                for (String col : new LinkedHashSet<>(whereIsNotNullCols)) {
                    ret.$isNotNull(col);
                }
            }
            return ret;
        });
    }

    public H $mapGroupBy(String tableAlias, String... groupCols) {
        return $mapGroupBy(tableAlias, $arr2list(groupCols));
    }

    public H $mapGroupBy(String tableAlias, Collection<String> groupCols) {
        return $groupBy(groupCols, (cols) -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma().$alias(tableAlias);
            for (String col : cols) {
                if (col == null) {
                    continue;
                }
                ret.$col(col);
            }
            return ret;
        });
    }

    public H $mapOrderBy(String tableAlias, String... orderCols) {
        return $mapOrderBy(tableAlias, $arr2list(orderCols));
    }

    public H $mapOrderBy(String tableAlias, Collection<String> orderCols) {
        return $orderBy(orderCols, (cols) -> {
            i2f.bql.core.Bql<?> ret = i2f.bql.core.Bql.$_().$sepComma().$alias(tableAlias);
            for (String col : cols) {
                if (col == null) {
                    continue;
                }
                ret.$col(col);
            }
            return ret;
        });
    }

}
