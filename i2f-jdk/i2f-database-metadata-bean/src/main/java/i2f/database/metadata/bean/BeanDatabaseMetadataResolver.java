package i2f.database.metadata.bean;

import i2f.annotations.core.base.NotNull;
import i2f.annotations.core.base.Nullable;
import i2f.annotations.core.doc.Comment;
import i2f.annotations.core.value.Default;
import i2f.annotations.db.*;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.std.StdType;
import i2f.lru.LruMap;
import i2f.reflect.ReflectResolver;
import i2f.text.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * @author ltb
 * @date 2022/3/24 18:02
 * @desc
 */
public class BeanDatabaseMetadataResolver {

    private static LruMap<String, String> CACHE_GET_TABLE_NAME = new LruMap<>(8192);

    public static String getTableName(Class<?> clazz) {
        String key = "#" + clazz;
        return (String) ReflectResolver.cacheDelegate((ReflectResolver.ENABLE_CACHE.get() ? CACHE_GET_TABLE_NAME : null), key, (k) -> {
            return getTableName0(clazz, BeanDatabaseMetadataResolver::defaultTableNameMapper);
        }, e -> e);
    }

    public static String defaultTableNameMapper(Class<?> clazz) {
        return StringUtils.toUnderScore(clazz.getSimpleName());
    }

    public static String getTableName0(Class<?> clazz) {
        return getTableName0(clazz, BeanDatabaseMetadataResolver::defaultTableNameMapper);
    }

    public static String getTableName0(Class<?> clazz, Function<Class<?>, String> defaultNameMapper) {
        Table ann = ReflectResolver.getAnnotation(clazz, Table.class);
        if (ann != null) {
            if (!StringUtils.isEmpty(ann.value())) {
                return ann.value();
            }
        }
        return defaultNameMapper == null ? clazz.getSimpleName() : defaultNameMapper.apply(clazz);
    }

    private static LruMap<String, Map<Field, String>> CACHE_GET_TABLE_COLUMNS = new LruMap<>(8192);

    public static Map<Field, String> getTableColumns(Class<?> clazz) {
        String key = "#" + clazz;
        return (Map<Field, String>) ReflectResolver.cacheDelegate((ReflectResolver.ENABLE_CACHE.get() ? CACHE_GET_TABLE_COLUMNS : null), key, (k) -> {
            return getTableColumns0(clazz, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
        }, e -> new LinkedHashMap<>(e));
    }

    public static String defaultColumnNameMapper(Field field) {
        return StringUtils.toUnderScore(field.getName());
    }

    public static Map<Field, String> getTableColumns0(Class<?> clazz) {
        return getTableColumns0(clazz, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
    }

    public static Map<Field, String> getTableColumns0(Class<?> clazz, Function<Field, String> defaultNameWrapper) {
        Map<Field, String> ret = new LinkedHashMap<>();
        DbIgnoreUnAnnotated ignoreUnAnnotated = ReflectResolver.getAnnotation(clazz, DbIgnoreUnAnnotated.class);
        boolean ignoreUnAnnotatedFlag = ignoreUnAnnotated != null && ignoreUnAnnotated.value();
        Map<Field, Class<?>> fields = ReflectResolver.getFields(clazz, (field) -> {
            DbIgnore anIgnore = ReflectResolver.getAnnotation(field, DbIgnore.class);
            if (anIgnore != null && anIgnore.value()) {
                return false;
            }
            Column cnn = ReflectResolver.getAnnotation(field, Column.class);
            if (cnn != null) {
                return true;
            }
            Primary pnn = ReflectResolver.getAnnotation(field, Primary.class);
            if (pnn != null) {
                return true;
            }
            return !ignoreUnAnnotatedFlag;
        });
        for (Map.Entry<Field, Class<?>> entry : fields.entrySet()) {
            Field field = entry.getKey();
            String name = getColumnName(field);
            ret.put(field, name);
        }
        return ret;
    }

    private static LruMap<String, String> CACHE_GET_COLUMN_NAME = new LruMap<>(8192);

    public static String getColumnName(Field field) {
        String key = "#" + field;
        return (String) ReflectResolver.cacheDelegate((ReflectResolver.ENABLE_CACHE.get() ? CACHE_GET_COLUMN_NAME : null), key, (k) -> {
            return getColumnName0(field, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
        }, e -> e);
    }

    public static String getColumnName0(Field field) {
        return getColumnName0(field, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
    }

    public static String getColumnName0(Field field, Function<Field, String> defaultNameWrapper) {
        String name = null;
        Column cnn = ReflectResolver.getAnnotation(field, Column.class);
        if (cnn != null) {
            name = cnn.value();
        }
        if (StringUtils.isEmpty(name)) {
            name = defaultNameWrapper == null ? field.getName() : defaultNameWrapper.apply(field);
        }
        return name;
    }

    private static LruMap<String, Map<Field, String>> CACHE_GET_TABLE_PRIMARIES = new LruMap<>(8192);

    public static Map<Field, String> getTablePrimaries(Class<?> clazz) {
        String key = "#" + clazz;
        return (Map<Field, String>) ReflectResolver.cacheDelegate((ReflectResolver.ENABLE_CACHE.get() ? CACHE_GET_TABLE_PRIMARIES : null), key, (k) -> {
            return getTablePrimaries0(clazz, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
        }, e -> new LinkedHashMap<>(e));
    }

    public static Map<Field, String> getTablePrimaries0(Class<?> clazz) {
        return getTablePrimaries0(clazz, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
    }

    public static Map<Field, String> getTablePrimaries0(Class<?> clazz, Function<Field, String> defaultNameWrapper) {
        Map<Field, String> ret = new LinkedHashMap<>();
        Map<Field, Set<Primary>> map = ReflectResolver.getFieldsWithAnnotation(clazz, Primary.class);
        for (Field field : map.keySet()) {
            String name = getColumnName0(field, defaultNameWrapper);
            ret.put(field, name);
        }
        return ret;
    }


    private static LruMap<String, Map.Entry<Field, String>> CACHE_GET_TABLE_PRIMARY = new LruMap<>(8192);

    public static Map.Entry<Field, String> getTablePrimary(Class<?> clazz) {
        String key = "#" + clazz;
        return (Map.Entry<Field, String>) ReflectResolver.cacheDelegate((ReflectResolver.ENABLE_CACHE.get() ? CACHE_GET_TABLE_PRIMARY : null), key, (k) -> {
            return getTablePrimary0(clazz, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
        }, e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()));
    }

    public static Map.Entry<Field, String> getTablePrimary0(Class<?> clazz) {
        return getTablePrimary0(clazz, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
    }

    public static Map.Entry<Field, String> getTablePrimary0(Class<?> clazz, Function<Field, String> defaultNameWrapper) {
        Map<Field, String> map = getTablePrimaries0(clazz, defaultNameWrapper);
        if (map.isEmpty()) {
            return null;
        }
        for (Map.Entry<Field, String> entry : map.entrySet()) {
            return new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue());
        }
        return null;
    }


    public static TableMeta getTableMeta(Class<?> clazz) {
        return getTableMeta(clazz, BeanDatabaseMetadataResolver::defaultTableNameMapper, BeanDatabaseMetadataResolver::defaultColumnNameMapper);
    }

    public static TableMeta getTableMeta(Class<?> clazz,
                                         Function<Class<?>, String> defaultTableNameMapper,
                                         Function<Field, String> defaultColumnNameMapper) {
        if (clazz == null) {
            return null;
        }
        TableMeta meta = new TableMeta();
        List<ColumnMeta> columns = new ArrayList<>();
        meta.setColumns(columns);

        IndexMeta primary = null;
        List<IndexMeta> uniqueIndexes = new ArrayList<>();
        meta.setUniqueIndexes(uniqueIndexes);
        List<IndexMeta> indexes = new ArrayList<>();
        meta.setIndexes(indexes);

        Set<Annotation> classAnns = ReflectResolver.findAllAnnotation(clazz, null);
        for (Annotation item : classAnns) {
            if (item instanceof Catalog) {
                Catalog dban = (Catalog) item;
                meta.setCatalog(dban.value());
            }
            if (item instanceof Schema) {
                Schema dban = (Schema) item;
                meta.setSchema(dban.value());
            }
            if (item instanceof Database) {
                Database dban = (Database) item;
                meta.setDatabase(dban.value());
            }
            if (item instanceof Table) {
                Table dban = (Table) item;
                meta.setName(dban.value());
            }
            if (item instanceof Comment) {
                Comment dban = (Comment) item;
                StringBuilder builder = new StringBuilder();
                for (String line : dban.value()) {
                    builder.append(line).append(";");
                }
                meta.setComment(builder.toString());
            }
            if (item instanceof TableType) {
                TableType dban = (TableType) item;
                meta.setType(dban.value());
            }
        }

        if (StringUtils.isEmpty(meta.getName())) {
            meta.setName(defaultTableNameMapper == null ? clazz.getSimpleName() : defaultTableNameMapper.apply(clazz));
        }

        Map<Field, String> fields = getTableColumns(clazz);

        int idx = 0;
        for (Field field : fields.keySet()) {
            if (field == null) {
                continue;
            }

            Set<Annotation> fieldAnns = ReflectResolver.findAllAnnotation(field, null);
            ColumnMeta col = new ColumnMeta();
            col.setIndex(idx++);
            for (Annotation ann : fieldAnns) {
                if (ann instanceof Column) {
                    Column dban = (Column) ann;
                    col.setName(dban.value());
                    break;
                }
            }
            if (StringUtils.isEmpty(col.getName())) {
                col.setName(defaultColumnNameMapper == null ? field.getName() : defaultColumnNameMapper.apply(field));
            }
            for (Annotation ann : fieldAnns) {
                if (ann instanceof Comment) {
                    Comment dban = (Comment) ann;
                    StringBuilder builder = new StringBuilder();
                    for (String line : dban.value()) {
                        builder.append(line).append(";");
                    }
                    col.setComment(builder.toString());
                }
                if (ann instanceof DataType) {
                    DataType dban = (DataType) ann;
                    col.setType(StringUtils.substringBeforeIndexOf(dban.value(), "("));
                    col.setPrecision(dban.precision());
                    col.setScale(dban.scale());
                }
                if (ann instanceof JdbcType) {
                    JdbcType dban = (JdbcType) ann;
                    col.setJdbcType(dban.value().getName());
                }
                if (ann instanceof Nullable) {
                    col.setNullable(true);
                }
                if (ann instanceof NotNull) {
                    col.setNullable(false);
                }
                if (ann instanceof AutoIncrement) {
                    AutoIncrement dban = (AutoIncrement) ann;
                    col.setAutoIncrement(true);
                }
                if (ann instanceof Default) {
                    Default dban = (Default) ann;
                    col.setDefaultValue(dban.value());
                }
                if (ann instanceof Primary) {
                    Primary dban = (Primary) ann;
                    String name = dban.value();
                    if (name == null || name.isEmpty()) {
                        name = "pk_" + meta.getName();
                    }
                    if (primary == null) {
                        primary = new IndexMeta();
                        primary.setUnique(true);
                        primary.setName(name);
                        primary.setColumns(new ArrayList<>());
                    }
                    IndexColumnMeta columnMeta = new IndexColumnMeta();
                    columnMeta.setName(col.getName());
                    columnMeta.setIndex(dban.order());
                    columnMeta.setType(dban.type());
                    primary.getColumns().add(columnMeta);
                }
                if (ann instanceof Unique) {
                    Unique dban = (Unique) ann;
                    String name = dban.value();
                    if (name == null || name.isEmpty()) {
                        name = "unq_" + col.getName();
                    }
                    IndexMeta index = null;
                    for (IndexMeta item : uniqueIndexes) {
                        if (Objects.equals(item.getName(), name)) {
                            index = item;
                            break;
                        }
                    }
                    if (index == null) {
                        index = new IndexMeta();
                        index.setUnique(true);
                        index.setName(name);
                        index.setColumns(new ArrayList<>());
                        uniqueIndexes.add(index);
                    }
                    IndexColumnMeta columnMeta = new IndexColumnMeta();
                    columnMeta.setName(col.getName());
                    columnMeta.setIndex(dban.order());
                    columnMeta.setType(dban.type());
                    index.getColumns().add(columnMeta);
                }
                if (ann instanceof Index) {
                    Index dban = (Index) ann;
                    String name = dban.value();
                    if (name == null || name.isEmpty()) {
                        name = "idx_" + col.getName();
                    }
                    IndexMeta index = null;
                    for (IndexMeta item : uniqueIndexes) {
                        if (Objects.equals(item.getName(), name)) {
                            index = item;
                            break;
                        }
                    }
                    if (index == null) {
                        index = new IndexMeta();
                        index.setUnique(true);
                        index.setName(name);
                        index.setColumns(new ArrayList<>());
                        indexes.add(index);
                    }
                    IndexColumnMeta columnMeta = new IndexColumnMeta();
                    columnMeta.setName(col.getName());
                    columnMeta.setIndex(dban.order());
                    columnMeta.setType(dban.type());
                    index.getColumns().add(columnMeta);
                }

            }

            Set<String> indexNames = new LinkedHashSet<>();
            if (primary != null) {
                indexNames.add(primary.getName());
            }

            List<IndexMeta> tmpUnqIndexes = new ArrayList<>(uniqueIndexes);
            uniqueIndexes.clear();
            for (IndexMeta item : tmpUnqIndexes) {
                if (!indexNames.contains(item.getName())) {
                    uniqueIndexes.add(item);
                    indexNames.add(item.getName());
                }
            }

            List<IndexMeta> tmpIndexes = new ArrayList<>(indexes);
            indexes.clear();
            for (IndexMeta item : tmpIndexes) {
                if (!indexNames.contains(item.getName())) {
                    indexes.add(item);
                    indexNames.add(item.getName());
                }
            }

            String columnType = col.getType();
            if (col.getPrecision() > 0) {
                columnType += "(" + col.getPrecision();
                if (col.getScale() > 0) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
            col.setColumnType(columnType);
            col.setJavaType(field.getType().getSimpleName());
            StdType stdType = StdType.detectType(col.getType(), col.getJavaType());
            if (stdType == null) {
                stdType = StdType.VARCHAR;
            }
            col.setJdbcType(stdType.jdbcType().getName());
            col.setStdType(stdType.text());
            col.setLooseJavaType(stdType.looseJavaType().getSimpleName());
            col.setLooseJdbcType(stdType.looseJdbcType().getName());
            columns.add(col);
        }

        columns.sort(ColumnMeta::compare);

        if (primary != null) {
            meta.setPrimary(primary);
            primary.getColumns().sort(IndexColumnMeta::compare);
        }

        for (IndexMeta item : uniqueIndexes) {
            item.getColumns().sort(IndexColumnMeta::compare);
        }

        for (IndexMeta item : indexes) {
            item.getColumns().sort(IndexColumnMeta::compare);
        }

        return meta;
    }

}
