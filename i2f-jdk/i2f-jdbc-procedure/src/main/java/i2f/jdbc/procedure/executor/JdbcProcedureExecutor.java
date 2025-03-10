package i2f.jdbc.procedure.executor;

import i2f.bindsql.BindSql;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:38
 */
public interface JdbcProcedureExecutor {
    List<ExecutorNode> getNodes();

    default Map<String, Object> exec(String nodeId, ExecuteContext context) {
        ProcedureMeta callNode = context.getNodeMap().get(nodeId);
        if (callNode == null) {
            throw new NotFoundSignalException("not found node: " + nodeId);
        }
        Object target = callNode.getTarget();
        if (callNode.getType() == ProcedureMeta.Type.XML) {
            return exec((XmlNode) target, context);
        } else if (callNode.getType() == ProcedureMeta.Type.JAVA) {
            JdbcProcedureJavaCaller javaCaller = (JdbcProcedureJavaCaller) target;
            try {
                Object ret = javaCaller.exec(context, this, context.getParams());
                context.getParams().put(ParamsConsts.RETURN, ret);
            } catch (ControlSignalException e) {

            } catch (Throwable e) {
                if (e instanceof SignalException) {
                    throw (SignalException) e;
                } else {
                    throw new ThrowSignalException(e.getMessage(), e);
                }
            }
            return context.getParams();
        } else {
            throw new ThrowSignalException("not supported node type: " + callNode.getType());
        }
    }

    default Map<String, Object> exec(XmlNode node, ExecuteContext context) {
        return exec(node, context, false, true);
    }

    Map<String, Object> exec(XmlNode node, ExecuteContext context, boolean beforeNewConnection, boolean afterCloseConnection);

    default Map<String, Object> execAsProcedure(String nodeId, ExecuteContext context) {
        ProcedureMeta callNode = context.getNodeMap().get(nodeId);
        if (callNode == null) {
            throw new NotFoundSignalException("not found node: " + nodeId);
        }
        Object target = callNode.getTarget();
        if (callNode.getType() == ProcedureMeta.Type.XML) {
            return execAsProcedure((XmlNode) target, context);
        } else if (callNode.getType() == ProcedureMeta.Type.JAVA) {
            JdbcProcedureJavaCaller javaCaller = (JdbcProcedureJavaCaller) target;
            try {
                Object ret = javaCaller.exec(context, this, context.getParams());
                context.getParams().put(ParamsConsts.RETURN, ret);
            } catch (ControlSignalException e) {

            } catch (Throwable e) {
                if (e instanceof SignalException) {
                    throw (SignalException) e;
                } else {
                    throw new ThrowSignalException(e.getMessage(), e);
                }
            }
            return context.getParams();
        } else {
            throw new ThrowSignalException("not supported node type: " + callNode.getType());
        }
    }

    default Map<String, Object> execAsProcedure(XmlNode node, ExecuteContext context) {
        return execAsProcedure(node, context, false, true);
    }

    Map<String, Object> execAsProcedure(XmlNode node, ExecuteContext context, boolean beforeNewConnection, boolean afterCloseConnection);

    Object attrValue(String attr, String action, XmlNode node, ExecuteContext context);

    Object resultValue(Object value, List<String> features, XmlNode node, ExecuteContext context);

    void setParamsObject(Map<String, Object> params, String result, Object value);

    Map<String, Object> createParams();

    Map<String, Object> newParams(ExecuteContext context);

    void debug(boolean enable);

    void debugLog(Supplier<Object> supplier);

    default void errorLog(Supplier<Object> supplier) {
        errorLog(supplier, null);
    }

    void errorLog(Supplier<Object> supplier, Throwable e);

    void openDebugger(String tag,Object context,String conditionExpression);

    Class<?> loadClass(String className);

    boolean test(String test, Map<String, Object> params);

    Object eval(String script, Map<String, Object> params);

    Object visit(String collectionScript, Map<String, Object> params);

    String render(String script, Map<String, Object> params);

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
