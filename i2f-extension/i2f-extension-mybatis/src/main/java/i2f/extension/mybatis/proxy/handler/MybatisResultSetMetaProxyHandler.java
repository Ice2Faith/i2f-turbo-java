package i2f.extension.mybatis.proxy.handler;

import i2f.extension.mybatis.data.ColumnMeta;
import i2f.extension.mybatis.interceptor.MybatisHolder;
import i2f.invokable.IInvokable;
import i2f.invokable.Invocation;
import i2f.invokable.method.IMethod;
import i2f.proxy.std.IProxyInvocationHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/7/23 15:52
 */
public class MybatisResultSetMetaProxyHandler implements IProxyInvocationHandler {
    public static final String METHOD_RESULT_SSET = "handleResultSets";
    public static final String METHOD_CURSOR_RESULT_SET = "handleCursorResultSets";

    protected Consumer<Object> infoLogger = System.out::println;
    protected Consumer<Object> errorLogger = System.err::println;

    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof IMethod)) {
            throw new IllegalStateException("un-support proxy invokable type=" + invokable.getClass());
        }

        Invocation invocation = new Invocation(ivkObj, invokable, args);


        if (!enable(invocation)) {
            return invokable.invoke(ivkObj, args);
        }

        ResultSetHandler executor = (ResultSetHandler) invocation.getTarget();
        IMethod method = (IMethod) invokable;

        Statement statement = (Statement) args[0];

        AtomicReference<List<ColumnMeta>> columnsRef = new AtomicReference<>();
        Statement proxy = (Statement) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{Statement.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Object ret = method.invoke(statement, args);
                        Class<?> returnType = method.getReturnType();
                        if (ResultSet.class.isAssignableFrom(returnType)) {
                            ResultSet rs = (ResultSet) ret;
                            List<ColumnMeta> columns = parseResultSetColumns(rs);
                            columnsRef.set(columns);
                            MybatisHolder.setExecColumnsMeta(columns);
                        }
                        return ret;
                    }
                });

        args[0] = proxy;

        Object ret = invokable.invoke(ivkObj, args);


        try {
            Field msField = null;
            Field autoMappingsField = null;
            Field[] fields = executor.getClass().getDeclaredFields();
            for (Field field : fields) {
                Class<?> type = field.getType();
                if (MappedStatement.class.isAssignableFrom(type)) {
                    msField = field;
                }
                if (Map.class.isAssignableFrom(type)) {
                    if ("autoMappingsCache".equals(field.getName())) {
                        autoMappingsField = field;
                    }
                }
            }
            if (msField != null) {
                List<ColumnMeta> metas = columnsRef.get();
                Map<String, ColumnMeta> metaMap = new LinkedHashMap<>();
                for (ColumnMeta meta : metas) {
                    metaMap.put(meta.getName().toUpperCase(), meta);
                }

                msField.setAccessible(true);
                MappedStatement ms = (MappedStatement) msField.get(executor);
                String id = ms.getId();

                if (autoMappingsField != null) {
                    try {
                        String autoMappingKey = id + "-Inline:null";
                        autoMappingsField.setAccessible(true);
                        Map<String, Object> autoMap = (Map<String, Object>) autoMappingsField.get(executor);
                        Object mappingList = autoMap.get(autoMappingKey);
                        if (mappingList == null) {
                            for (Map.Entry<String, Object> entry : autoMap.entrySet()) {
                                mappingList = entry.getValue();
                                break;
                            }
                        }
                        if (mappingList != null) {
                            if (mappingList instanceof List) {
                                List list = (List) mappingList;
                                Class<?> mappingClass = null;
                                Field columnField = null;
                                Field propertyField = null;
                                for (Object mapping : list) {
                                    if (mapping == null) {
                                        continue;
                                    }
                                    if (mappingClass == null) {
                                        mappingClass = mapping.getClass();
                                        columnField = mappingClass.getDeclaredField("column");
                                        columnField.setAccessible(true);
                                        propertyField = mappingClass.getDeclaredField("property");
                                        propertyField.setAccessible(true);
                                    }

                                    String column = String.valueOf(columnField.get(mapping)).toUpperCase();
                                    ColumnMeta meta = metaMap.get(column.toUpperCase());
                                    if (meta == null) {
                                        continue;
                                    }
                                    String property = String.valueOf(propertyField.get(mapping));
                                    meta.setProp(property);
                                }
                            }
                        }
                    } catch (Throwable e) {

                    }
                }

                List<ResultMap> list = ms.getResultMaps();
                if (!list.isEmpty()) {

                    ResultMap resultMap = list.get(0);
                    Class<?> elemType = resultMap.getType();
                    for (ColumnMeta meta : metas) {
                        meta.setElemType(elemType);
                    }

                    List<ResultMapping> propertyResultMappings = resultMap.getPropertyResultMappings();
                    for (ResultMapping mapping : propertyResultMappings) {
                        String column = mapping.getColumn();
                        String property = mapping.getProperty();
                        ColumnMeta meta = metaMap.get(column.toUpperCase());
                        if (meta == null) {
                            continue;
                        }
                        meta.setProp(property);
                    }
                }
            }
        } catch (Exception e) {

        }


        return ret;
    }


    public List<ColumnMeta> parseResultSetColumns(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        List<ColumnMeta> columns = new ArrayList<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i);
            String columnName = metaData.getColumnName(i);
            String columnTypeName = metaData.getColumnTypeName(i);
            int columnType = metaData.getColumnType(i);
            JDBCType jdbcType = null;
            try {
                jdbcType = JDBCType.valueOf(columnType);
            } catch (Exception e) {

            }
            int columnDisplaySize = metaData.getColumnDisplaySize(i);
            int precision = metaData.getPrecision(i);
            int scale = metaData.getScale(i);
            String className = metaData.getColumnClassName(i);
            Class<?> clazz = null;
            if (clazz == null) {
                try {
                    clazz = Class.forName(className);
                } catch (Exception e) {

                }
            }
            if (clazz == null) {
                try {
                    clazz = loader.loadClass(className);
                } catch (Exception e) {

                }
            }
            boolean autoIncrement = metaData.isAutoIncrement(i);
            boolean notNull = metaData.isNullable(i) == ResultSetMetaData.columnNoNulls;

            ColumnMeta meta = new ColumnMeta();
            meta.setName(columnLabel);
            meta.setColumnName(columnName);
            meta.setTypeName(columnTypeName);
            meta.setType(columnType);
            meta.setJdbcType(jdbcType);
            meta.setDisplaySize(columnDisplaySize);
            meta.setPrecision(precision);
            meta.setScale(scale);
            meta.setClassName(className);
            meta.setClazz(clazz);
            meta.setAutoIncrement(autoIncrement);
            meta.setNullable(!notNull);
            columns.add(meta);
        }

        return columns;
    }

    public boolean enable(Invocation invocation) {
        return MybatisHolder.isRecordingMeta();
    }

}
