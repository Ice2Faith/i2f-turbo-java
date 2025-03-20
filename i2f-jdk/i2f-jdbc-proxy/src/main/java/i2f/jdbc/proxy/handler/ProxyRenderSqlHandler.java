package i2f.jdbc.proxy.handler;

import i2f.annotations.core.naming.Name;
import i2f.bindsql.BindSql;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.type.DatabaseType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.proxy.annotations.IgnorePage;
import i2f.jdbc.proxy.provider.ProxyRenderSqlProvider;
import i2f.jdbc.std.context.JdbcInvokeContextProvider;
import i2f.lru.LruMap;
import i2f.page.ApiOffsetSize;
import i2f.page.Page;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import i2f.reflect.RichConverter;
import i2f.typeof.TypeOf;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/6 8:56
 * @desc
 */
public class ProxyRenderSqlHandler implements InvocationHandler {

    private Class<?> proxyClass;

    private JdbcInvokeContextProvider<?> contextProvider;

    private ProxyRenderSqlProvider sqlProvider;

    private static LruMap<String, Reference<Type[]>> CACHE_REL_TYPES = new LruMap<>(2048);

    public ProxyRenderSqlHandler(Class<?> proxyClass, JdbcInvokeContextProvider<?> contextProvider, ProxyRenderSqlProvider sqlProvider) {
        this.proxyClass = proxyClass;
        this.contextProvider = contextProvider;
        this.sqlProvider = sqlProvider;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString") && method.getParameterCount() == 0) {
            return proxyClass.getName() + proxy.getClass().getSimpleName();
        }
        if (method.getName().equals("hashCode") && method.getParameterCount() == 0) {
            return proxy.getClass().getName().hashCode();
        }
        Class<?> clazz = method.getDeclaringClass();
        String methodId = clazz.getName().replaceAll("\\$", ".") + "." + method.getName();
        Parameter[] parameters = method.getParameters();
        String[] names = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String name = ReflectResolver.getAnnotationValue(parameter, Name.class, "value");
            if (name != null && !name.isEmpty()) {
                names[i] = name;
            } else {
                names[i] = parameter.getName();
            }
        }

        ApiOffsetSize page = null;
        Map<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Object arg = args[i];
            if (arg instanceof ApiOffsetSize) {
                page = (ApiOffsetSize) arg;
            }
            params.put(name, arg);
        }

        Object context = contextProvider.beginContext();
        Connection conn = contextProvider.getConnectionInner(context);

        try {
            if (params.get("databaseType") == null) {
                DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
                params.put("databaseType", databaseType);
            }
            if (params.get("connection") == null) {
                params.put("connection", conn);
            }

            BindSql sql = sqlProvider.render(methodId, params, method, args);

            Type returnType = method.getGenericReturnType();
            Class<?> returnClass = method.getReturnType();

            BindSql.Type type = sql.getType();
            if (type == null || type == BindSql.Type.UNSET) {
                type = JdbcResolver.detectType(sql.getSql());
            }

            Type[] relTypes = null;
            if (!(returnType instanceof Class)) {
                String cacheKey = proxyClass.getName() + "#" + method.getDeclaringClass().getName();
                Reference<Type[]> ref = CACHE_REL_TYPES.get(cacheKey);

                if (ref != null) {
                    relTypes = ref.get();
                } else {
                    relTypes = RichConverter.fetchRelType(proxyClass, method.getDeclaringClass());
                    CACHE_REL_TYPES.put(cacheKey, Reference.of(relTypes));
                }
            }


            if (type == BindSql.Type.UPDATE) {
                int val = JdbcResolver.update(conn, sql);
                return ObjectConvertor.tryConvertAsType(val, returnClass);
            } else if (type == BindSql.Type.CALL) {
                Map<String, Object> val = JdbcResolver.callNaming(conn, sql);
                return RichConverter.convert2Type(val, returnType, relTypes);
            } else {
                // 原始包装类型
                if (TypeOf.typeOf(returnClass, QueryResult.class)) {
                    QueryResult val = JdbcResolver.query(conn, sql);
                    return val;
                }

                // 单一返回值处理
                if (TypeOf.isBaseType(returnClass)) {
                    Object val = JdbcResolver.get(conn, sql, returnClass);
                    return val;
                }

                if (ObjectConvertor.isNumericType(returnClass)
                        || ObjectConvertor.isBooleanType(returnClass)
                        || ObjectConvertor.isCharType(returnClass)
                        || ObjectConvertor.isDateType(returnClass)) {
                    Object val = JdbcResolver.get(conn, sql, returnClass);
                    return val;
                }

                // 分页处理
                boolean enablePage = false;
                if (page != null) {
                    enablePage = true;
                    Boolean ignore = ReflectResolver.getAnnotationValue(method, IgnorePage.class, "value");
                    if (ignore != null) {
                        enablePage = !ignore;
                    }
                }

                if (enablePage) {
                    if (TypeOf.typeOf(returnClass, Page.class)) {
                        Page<Map<String, Object>> val = JdbcResolver.page(conn, sql, page);
                        return RichConverter.convert2Type(val, returnType, relTypes);
                    } else if (TypeOf.typeOf(returnClass, Collection.class)) {
                        Page<Map<String, Object>> val = JdbcResolver.page(conn, sql, page);
                        List<Map<String, Object>> list = val.getList();
                        return RichConverter.convert2Type(list, returnType, relTypes);
                    } else if (TypeOf.isBaseType(returnClass)) {
                        Page<Map<String, Object>> val = JdbcResolver.page(conn, sql, page);
                        return ObjectConvertor.tryConvertAsType(val.getTotal(), returnClass);
                    } else if (ObjectConvertor.isNumericType(returnClass)
                            || ObjectConvertor.isBooleanType(returnClass)
                            || ObjectConvertor.isCharType(returnClass)
                            || ObjectConvertor.isDateType(returnClass)) {
                        Page<Map<String, Object>> val = JdbcResolver.page(conn, sql, page);
                        return ObjectConvertor.tryConvertAsType(val.getTotal(), returnClass);
                    } else {
                        Page<Map<String, Object>> val = JdbcResolver.page(conn, sql, page);
                        List<Map<String, Object>> list = val.getList();
                        if (list.isEmpty()) {
                            return null;
                        }
                        return RichConverter.convert2Type(list.get(0), returnType, relTypes);
                    }
                }

                // 列表处理
                if (TypeOf.typeOf(returnClass, Collection.class)) {
                    List<Map<String, Object>> val = JdbcResolver.list(conn, sql);
                    return RichConverter.convert2Type(val, returnType, relTypes);
                }

                // 数组处理
                if (returnClass.isArray()) {
                    List<Map<String, Object>> val = JdbcResolver.list(conn, sql);
                    return RichConverter.convert2Type(val, returnType, relTypes);
                }

                // 其他就是单一对象处理
                Map<String, Object> val = JdbcResolver.find(conn, sql);
                return RichConverter.convert2Type(val, returnType, relTypes);
            }
        } finally {
            contextProvider.endContextInner(context, conn);
        }
    }

}
