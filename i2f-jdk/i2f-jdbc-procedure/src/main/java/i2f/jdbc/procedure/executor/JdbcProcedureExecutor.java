package i2f.jdbc.procedure.executor;

import i2f.bindsql.BindSql;
import i2f.container.builder.map.MapBuilder;
import i2f.context.std.INamingContext;
import i2f.convert.obj.ObjectConvertor;
import i2f.environment.std.IEnvironment;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.page.ApiOffsetSize;
import i2f.reference.Reference;
import i2f.typeof.TypeOf;

import java.sql.Connection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:38
 */
public interface JdbcProcedureExecutor {

    boolean DEFAULT_BEFORE_NEW_CONNECTION = false;
    boolean DEFAULT_AFTER_CLOSE_CONNECTION = true;

    default Reference<?> nop() {
        return Reference.nop();
    }

    default boolean isNop(Object obj) {
        if (obj instanceof Reference) {
            Reference<?> ref = (Reference<?>) obj;
            return !ref.isValue();
        }
        return false;
    }

    JdbcProcedureContext getContext();

    default Map<String, ProcedureMeta> getMetaMap() {
        return getContext().getMetaMap();
    }

    default ProcedureMeta getMeta(String procedureId) {
        return getMetaMap().get(procedureId);
    }

    List<ExecutorNode> getNodes();

    IEnvironment getEnvironment();

    void setEnvironment(IEnvironment environment);

    default String env(String property) {
        return getEnvironment().getProperty(property);
    }

    default String env(String property, String defVal) {
        return getEnvironment().getProperty(property, defVal);
    }

    default <T> T envAs(String property, Class<T> targetType) {
        return envAs(property, targetType, null);
    }

    default <T> T envAs(String property, Class<T> targetType, T defVal) {
        String ret = getEnvironment().getProperty(property);
        if (ret == null) {
            return null;
        }
        if (targetType == null) {
            return (T) ret;
        }
        Object obj = convertAs(ret, targetType);
        if (TypeOf.instanceOf(obj, targetType)) {
            return (T) obj;
        }
        return defVal;
    }

    INamingContext getNamingContext();

    void setNamingContext(INamingContext context);

    default <T> T getBean(Class<T> clazz) {
        return getNamingContext().getBean(clazz);
    }

    default <T> T getBean(String name) {
        return getNamingContext().getBean(name);
    }

    default <T> T convertAs(Object obj, Class<?> type) {
        return (T) ObjectConvertor.tryConvertAsType(obj, type);
    }

    default MapBuilder<String, Object, ? extends Map<String, Object>> mapBuilder() {
        return new MapBuilder<>(new HashMap<>(), String.class, Object.class);
    }

    default MapBuilder<String, Object, ? extends Map<String, Object>> mapBuilder(Map<String,Object> map) {
        return new MapBuilder<>(map, String.class, Object.class);
    }

    default <T> T invoke(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return invoke(procedureId, builder.get());
    }

    <T> T invoke(String procedureId, Map<String, Object> params);

    default <T> T invoke(String procedureId, Object... args) {
        return invoke(procedureId, Arrays.asList(args));
    }

    <T> T invoke(String procedureId, List<Object> args);

    default Map<String, Object> call(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return exec(procedureId, builder.get());
    }

    default Map<String, Object> call(String procedureId, Map<String, Object> params) {
        return exec(procedureId, params);
    }

    default Map<String, Object> call(String procedureId, Object... args) {
        return call(procedureId, Arrays.asList(args));
    }

    Map<String, Object> call(String procedureId, List<Object> args);

    default Map<String, Object> exec(String procedureId, Map<String, Object> params) {
        return exec(procedureId, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    default Map<String, Object> exec(String procedureId, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        ProcedureMeta callNode = getMeta(procedureId);
        if (callNode == null) {
            throw new NotFoundSignalException("not found node: " + procedureId);
        }
        return exec(callNode, params, beforeNewConnection, afterCloseConnection);
    }

    default Map<String, Object> exec(ProcedureMeta meta, Map<String, Object> params) {
        return exec(meta, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    default Map<String, Object> exec(ProcedureMeta meta, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        if (meta == null) {
            return params;
        }
        Object target = meta.getTarget();
        if (meta.getType() == ProcedureMeta.Type.XML) {
            return exec((XmlNode) target, params, beforeNewConnection, afterCloseConnection);
        } else if (meta.getType() == ProcedureMeta.Type.JAVA) {
            JdbcProcedureJavaCaller caller = (JdbcProcedureJavaCaller) target;
            return exec(caller, params, beforeNewConnection, afterCloseConnection);
        } else {
            throw new ThrowSignalException("not supported node type: " + meta.getType());
        }
    }

    default Map<String, Object> exec(JdbcProcedureJavaCaller caller, Map<String, Object> params) {
        return exec(caller, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    Map<String, Object> exec(JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection);

    default Map<String, Object> exec(XmlNode node, Map<String, Object> params) {
        return exec(node, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    Map<String, Object> exec(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection);

    default Map<String, Object> execAsProcedure(String procedureId, Map<String, Object> params) {
        return execAsProcedure(procedureId, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    default Map<String, Object> execAsProcedure(String procedureId, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        ProcedureMeta callNode = getMeta(procedureId);
        if (callNode == null) {
            throw new NotFoundSignalException("not found node: " + procedureId);
        }
        return execAsProcedure(callNode, params, beforeNewConnection, afterCloseConnection);
    }

    default Map<String, Object> execAsProcedure(ProcedureMeta meta, Map<String, Object> params) {
        return execAsProcedure(meta, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    default Map<String, Object> execAsProcedure(ProcedureMeta meta, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        if (meta == null) {
            return params;
        }
        Object target = meta.getTarget();
        if (meta.getType() == ProcedureMeta.Type.XML) {
            return execAsProcedure((XmlNode) target, params, beforeNewConnection, afterCloseConnection);
        } else if (meta.getType() == ProcedureMeta.Type.JAVA) {
            JdbcProcedureJavaCaller caller = (JdbcProcedureJavaCaller) target;
            return exec(caller, params, beforeNewConnection, afterCloseConnection);
        } else {
            throw new ThrowSignalException("not supported node type: " + meta.getType());
        }
    }

    default Map<String, Object> execAsProcedure(JdbcProcedureJavaCaller caller, Map<String, Object> params) {
        return exec(caller, params);
    }

    default Map<String, Object> execAsProcedure(JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        return exec(caller, params, beforeNewConnection, afterCloseConnection);
    }

    default Map<String, Object> execAsProcedure(XmlNode node, Map<String, Object> params) {
        return execAsProcedure(node, params, DEFAULT_BEFORE_NEW_CONNECTION, DEFAULT_AFTER_CLOSE_CONNECTION);
    }

    Map<String, Object> execAsProcedure(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection);

    Map<String, Object> createParams();

    Map<String, Object> prepareParams(Map<String, Object> params);

    Map<String, Object> newParams(Map<String, Object> params);

    Object attrValue(String attr, String action, XmlNode node, Map<String, Object> params);

    Object resultValue(Object value, List<String> features, XmlNode node, Map<String, Object> params);

    void debug(boolean enable);

    boolean isDebug();

    void openDebugger(String tag, Object context, String conditionExpression);

    default void logDebug(Object obj) {
        logDebug(() -> obj);
    }

    void logDebug(Supplier<Object> supplier);

    default void logInfo(Object obj) {
        logInfo(() -> obj);
    }

    default void logInfo(Supplier<Object> supplier) {
        logInfo(supplier, null);
    }

    default void logInfo(Object obj, Throwable e) {
        logInfo(() -> obj, e);
    }

    void logInfo(Supplier<Object> supplier, Throwable e);

    default void logWarn(Object obj) {
        logWarn(() -> obj);
    }

    default void logWarn(Supplier<Object> supplier) {
        logWarn(supplier, null);
    }

    default void logWarn(Object obj, Throwable e) {
        logWarn(() -> obj, e);
    }

    void logWarn(Supplier<Object> supplier, Throwable e);

    default void logError(Object obj) {
        logError(() -> obj);
    }

    default void logError(Supplier<Object> supplier) {
        logError(supplier, null);
    }

    default void logError(Object obj, Throwable e) {
        logError(() -> obj, e);
    }

    void logError(Supplier<Object> supplier, Throwable e);

    String traceLocation();

    Class<?> loadClass(String className);

    boolean test(String test, Object params);

    default <T> T evalAs(String script, Object params) {
        return (T) eval(script, params);
    }

    Object eval(String script, Object params);

    void visitSet(Map<String, Object> params, String result, Object value);

    void visitDelete(Map<String, Object> params, String result);

    default <T> T visitAs(String script, Object params) {
        return (T) visit(script, params);
    }

    Object visit(String script, Object params);

    String render(String script, Object params);

    Connection getConnection(String datasource, Map<String, Object> params);

    List<?> sqlQueryList(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryObject(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryRow(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType);

    int sqlUpdate(String datasource, BindSql bql, Map<String, Object> params);

    BindSql sqlWrapPage(String datasource, BindSql bql, ApiOffsetSize page, Map<String, Object> params);

    BindSql sqlWrapCount(String datasource, BindSql bql, Map<String, Object> params);

    default BindSql sqlScript(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params) {
        return sqlScript(datasource, dialectScriptList, params, null);
    }

    BindSql sqlScript(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, ApiOffsetSize page);

    List<?> sqlQueryPage(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType, ApiOffsetSize page);

    void sqlTransBegin(String datasource, int isolation, Map<String, Object> params);

    void sqlTransCommit(String datasource, Map<String, Object> params);

    void sqlTransRollback(String datasource, Map<String, Object> params);

    void sqlTransNone(String datasource, Map<String, Object> params);

}
