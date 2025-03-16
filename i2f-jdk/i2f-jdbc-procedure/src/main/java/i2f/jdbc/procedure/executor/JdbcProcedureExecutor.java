package i2f.jdbc.procedure.executor;

import i2f.bindsql.BindSql;
import i2f.container.builder.map.MapBuilder;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

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
    JdbcProcedureContext getContext();

    default Map<String,ProcedureMeta> getMetaMap(){
        return getContext().getMetaMap();
    }

    default ProcedureMeta getMeta(String procedureId){
        return getMetaMap().get(procedureId);
    }

    List<ExecutorNode> getNodes();

    default <T> T convertAs(Object obj,Class<?> type){
        return (T)ObjectConvertor.tryConvertAsType(obj,type);
    }

    default MapBuilder<String, Object, ? extends Map<String, Object>> mapBuilder() {
        return new MapBuilder<>(new HashMap<>(), String.class, Object.class);
    }

    default <T> T invoke(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return invoke(procedureId, builder.get());
    }

    <T> T invoke(String procedureId, Map<String, Object> params);

    default <T> T invoke(String procedureId, Object ... args){
        return invoke(procedureId, Arrays.asList(args));
    }

    <T> T invoke(String procedureId, List<Object> args);

    default  Map<String,Object> call(String procedureId, Consumer<MapBuilder<String, Object, ? extends Map<String, Object>>> consumer) {
        MapBuilder<String, Object, HashMap<String, Object>> builder = new MapBuilder<>(new HashMap<String, Object>());
        consumer.accept(builder);
        return exec(procedureId, builder.get());
    }

    default Map<String, Object> call(String procedureId, Map<String,Object> params) {
        return exec(procedureId,params);
    }

    default Map<String,Object> call(String procedureId, Object ... args){
        return call(procedureId, Arrays.asList(args));
    }

    Map<String,Object> call(String procedureId, List<Object> args);


    default Map<String, Object> exec(String procedureId, Map<String,Object> params) {
        ProcedureMeta callNode = getMeta(procedureId);
        if (callNode == null) {
            throw new NotFoundSignalException("not found node: " + procedureId);
        }
        Object target = callNode.getTarget();
        if (callNode.getType() == ProcedureMeta.Type.XML) {
            return exec((XmlNode) target, params);
        } else if (callNode.getType() == ProcedureMeta.Type.JAVA) {
            prepareParams(params);
            JdbcProcedureJavaCaller javaCaller = (JdbcProcedureJavaCaller) target;
            try {
                Object ret = javaCaller.exec(this, params);
                visitSet(params,ParamsConsts.RETURN, ret);
            } catch (ControlSignalException e) {

            } catch (Throwable e) {
                if (e instanceof SignalException) {
                    throw (SignalException) e;
                } else {
                    throw new ThrowSignalException(e.getMessage(), e);
                }
            }
            return params;
        } else {
            throw new ThrowSignalException("not supported node type: " + callNode.getType());
        }
    }

    Map<String,Object> prepareParams(Map<String,Object> params);

    default Map<String, Object> exec(XmlNode node, Map<String,Object> params) {
        return exec(node, params, false, true);
    }

    Map<String, Object> exec(XmlNode node, Map<String,Object> params, boolean beforeNewConnection, boolean afterCloseConnection);

    default Map<String, Object> execAsProcedure(String procedureId, Map<String,Object> params) {
        ProcedureMeta callNode = getMeta(procedureId);
        if (callNode == null) {
            throw new NotFoundSignalException("not found node: " + procedureId);
        }
        Object target = callNode.getTarget();
        if (callNode.getType() == ProcedureMeta.Type.XML) {
            return execAsProcedure((XmlNode) target, params);
        } else if (callNode.getType() == ProcedureMeta.Type.JAVA) {
            prepareParams(params);
            JdbcProcedureJavaCaller javaCaller = (JdbcProcedureJavaCaller) target;
            try {
                Object ret = javaCaller.exec(this,params);
                visitSet(params,ParamsConsts.RETURN, ret);
            } catch (ControlSignalException e) {

            } catch (Throwable e) {
                if (e instanceof SignalException) {
                    throw (SignalException) e;
                } else {
                    throw new ThrowSignalException(e.getMessage(), e);
                }
            }
            return params;
        } else {
            throw new ThrowSignalException("not supported node type: " + callNode.getType());
        }
    }

    default Map<String, Object> execAsProcedure(XmlNode node, Map<String,Object> params) {
        return execAsProcedure(node, params, false, true);
    }

    Map<String, Object> execAsProcedure(XmlNode node, Map<String,Object> params, boolean beforeNewConnection, boolean afterCloseConnection);

    Object attrValue(String attr, String action, XmlNode node, Map<String,Object> params);

    Object resultValue(Object value, List<String> features, XmlNode node, Map<String,Object> params);

    void visitSet(Map<String, Object> params, String result, Object value);

    void visitDelete(Map<String,Object> params,String result);

    Map<String, Object> createParams();

    Map<String, Object> newParams(Map<String,Object> params);

    void debug(boolean enable);

    void debugLog(Supplier<Object> supplier);

    default void infoLog(Supplier<Object> supplier) {
        infoLog(supplier, null);
    }

    void infoLog(Supplier<Object> supplier, Throwable e);

    default void warnLog(Supplier<Object> supplier) {
        warnLog(supplier, null);
    }

    void warnLog(Supplier<Object> supplier, Throwable e);


    default void errorLog(Supplier<Object> supplier) {
        errorLog(supplier, null);
    }

    void errorLog(Supplier<Object> supplier, Throwable e);


    void openDebugger(String tag,Object context,String conditionExpression);

    Class<?> loadClass(String className);

    boolean test(String test, Object params);

    default<T> T evalAs(String script,Object params){
        return (T)eval(script,params);
    }

    Object eval(String script, Object params);

    default <T> T visitAs(String script,Object params){
        return (T)visit(script,params);
    }

    Object visit(String script, Object params);

    String render(String script, Object params);

    Connection getConnection(String datasource, Map<String, Object> params);

    List<?> sqlQueryList(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryObject(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryRow(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType);

    int sqlUpdate(String datasource, BindSql bql, Map<String, Object> params);

    BindSql sqlScript(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params);

    List<?> sqlQueryPage(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType, int pageIndex, int pageSize);

    void sqlTransBegin(String datasource, int isolation, Map<String, Object> params);

    void sqlTransCommit(String datasource, Map<String, Object> params);

    void sqlTransRollback(String datasource, Map<String, Object> params);

    void sqlTransNone(String datasource, Map<String, Object> params);

}
