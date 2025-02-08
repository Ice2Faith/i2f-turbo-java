package i2f.jdbc.procedure.executor;


import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:38
 */
public interface JdbcProcedureExecutor {
    List<ExecutorNode> getNodes();

    default void exec(String nodeId, ExecuteContext context) {
        exec(context.getNodeMap().get(nodeId), context);
    }

    default void exec(XmlNode node, ExecuteContext context) {
        exec(node, context, false, true);
    }

    void exec(XmlNode node, ExecuteContext context, boolean beforeNewConnection, boolean afterCloseConnection);

    default void execAsProcedure(String nodeId, ExecuteContext context) {
        execAsProcedure(context.getNodeMap().get(nodeId), context, false, true);
    }

    default void execAsProcedure(XmlNode node, ExecuteContext context) {
        execAsProcedure(node, context, false, true);
    }

    void execAsProcedure(XmlNode node, ExecuteContext context, boolean beforeNewConnection, boolean afterCloseConnection);

    Object attrValue(String attr, String action, XmlNode node, ExecuteContext context);

    Object resultValue(Object value, List<String> features, XmlNode node, ExecuteContext context);

    void setParamsObject(Map<String, Object> params, String result, Object value);

    Map<String, Object> createParams();

    Map<String, Object> newParams(ExecuteContext context);

    void debugLog(Supplier<String> supplier);

    Class<?> loadClass(String className);

    boolean test(String test, Map<String, Object> params);

    Object eval(String script, Map<String, Object> params);

    Object visit(String collectionScript, Map<String, Object> params);

    String render(String script, Map<String, Object> params);

    List<?> sqlQueryList(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryObject(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryRow(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType);

    int sqlUpdate(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params);

    List<?> sqlQueryPage(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType, int pageIndex, int pageSize);

    void sqlTransBegin(String datasource, int isolation, Map<String, Object> params);

    void sqlTransCommit(String datasource, Map<String, Object> params);

    void sqlTransRollback(String datasource, Map<String, Object> params);

    void sqlTransNone(String datasource, Map<String, Object> params);

}
