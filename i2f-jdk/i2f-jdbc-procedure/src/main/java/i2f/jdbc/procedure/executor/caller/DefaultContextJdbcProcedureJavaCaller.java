package i2f.jdbc.procedure.executor.caller;

import i2f.bindsql.BindSql;
import i2f.context.std.INamingContext;
import i2f.environment.std.IEnvironment;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.procedure.context.ContextFunctions;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.executor.FeatureFunction;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.log.JdbcProcedureLogger;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.lock.ILockProvider;
import i2f.page.ApiOffsetSize;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/10/1 21:31
 * @desc
 */
public interface DefaultContextJdbcProcedureJavaCaller extends ContextJdbcProcedureJavaCaller, JdbcProcedureExecutor, ContextFunctions {

    @Override
    default void registryExecutorNode(ExecutorNode node) {
        executor().registryExecutorNode(node);
    }

    @Override
    default void registryEvalScriptProvider(EvalScriptProvider provider) {
        executor().registryEvalScriptProvider(provider);
    }

    @Override
    default CopyOnWriteArrayList<EvalScriptProvider> getEvalScriptProviders() {
        return executor().getEvalScriptProviders();
    }

    @Override
    default XProc4jEventHandler getEventHandler() {
        return executor().getEventHandler();
    }

    @Override
    default void sendEvent(XProc4jEvent event) {
        executor().sendEvent(event);
    }

    @Override
    default void publishEvent(XProc4jEvent event) {
        executor().publishEvent(event);
    }

    @Override
    default JdbcProcedureContext getContext() {
        return executor().getContext();
    }

    @Override
    default String nodeTagKey(String key) {
        return executor().nodeTagKey(key);
    }

    @Override
    default ConcurrentHashMap<String, CopyOnWriteArrayList<ExecutorNode>> getNodesMap() {
        return executor().getNodesMap();
    }


    @Override
    default void registryLockProvider(ILockProvider provider) {
        executor().registryLockProvider(provider);
    }

    @Override
    default ConcurrentHashMap<String, ILockProvider> getLockProviders() {
        return executor().getLockProviders();
    }

    @Override
    default void registryFeatureFunction(String name, FeatureFunction function) {
        executor().registryFeatureFunction(name, function);
    }

    @Override
    default ConcurrentHashMap<String, FeatureFunction> getFeatureFunctions() {
        return executor().getFeatureFunctions();
    }

    @Override
    default IEnvironment getEnvironment() {
        return executor().getEnvironment();
    }

    @Override
    default void setEnvironment(IEnvironment environment) {
        executor().setEnvironment(environment);
    }

    @Override
    default INamingContext getNamingContext() {
        return executor().getNamingContext();
    }

    @Override
    default void setNamingContext(INamingContext context) {
        executor().setNamingContext(context);
    }

    @Override
    default <T> T invoke(String procedureId, Map<String, Object> params) {
        return executor().invoke(procedureId, params);
    }

    default <T> T invoke(String procedureId) {
        return invoke(procedureId, params());
    }

    @Override
    default <T> T invoke(String procedureId, List<Object> args) {
        return executor().invoke(procedureId, args);
    }

    @Override
    default Map<String, Object> call(String procedureId, List<Object> args) {
        return executor().call(procedureId, args);
    }

    @Override
    default Map<String, Object> exec(JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        return executor().exec(caller, params, beforeNewConnection, afterCloseConnection);
    }

    default Map<String, Object> exec(JdbcProcedureJavaCaller caller, boolean beforeNewConnection, boolean afterCloseConnection) {
        return exec(caller, params(), beforeNewConnection, afterCloseConnection);
    }

    @Override
    default Map<String, Object> exec(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        return executor().exec(node, params, beforeNewConnection, afterCloseConnection);
    }

    default Map<String, Object> exec(XmlNode node, boolean beforeNewConnection, boolean afterCloseConnection) {
        return exec(node, params(), beforeNewConnection, afterCloseConnection);
    }

    @Override
    default Map<String, Object> execAsProcedure(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        return executor().execAsProcedure(node, params, beforeNewConnection, afterCloseConnection);
    }

    default Map<String, Object> execAsProcedure(XmlNode node, boolean beforeNewConnection, boolean afterCloseConnection) {
        return execAsProcedure(node, params(), beforeNewConnection, afterCloseConnection);
    }

    @Override
    default Map<String, Object> createParams() {
        return executor().createParams();
    }

    @Override
    default Map<String, Object> prepareParams(Map<String, Object> params) {
        return executor().prepareParams(params);
    }

    default Map<String, Object> prepareParams() {
        return prepareParams(params());
    }

    @Override
    default Map<String, Object> newParams(Map<String, Object> params) {
        return executor().newParams(params);
    }

    default Map<String, Object> newParams() {
        return newParams(params());
    }

    @Override
    default Object attrValue(String attr, String action, XmlNode node, Map<String, Object> params) {
        return executor().attrValue(attr, action, node, params);
    }

    default Object attrValue(String attr, String action, XmlNode node) {
        return attrValue(attr, action, node, params());
    }

    @Override
    default Object resultValue(Object value, List<String> features, XmlNode node, Map<String, Object> params) {
        return executor().resultValue(value, features, node, params);
    }

    default Object resultValue(Object value, List<String> features, XmlNode node) {
        return resultValue(value, features, node, params());
    }

    @Override
    default boolean toBoolean(Object obj) {
        return executor().toBoolean(obj);
    }

    @Override
    default void debug(boolean enable) {
        executor().debug(enable);
    }

    @Override
    default boolean isDebug() {
        return executor().isDebug();
    }

    @Override
    default void openDebugger(String tag, Object context, String conditionExpression) {
        executor().openDebugger(tag, context, conditionExpression);
    }

    @Override
    default JdbcProcedureLogger logger() {
        return executor().logger();
    }

    @Override
    default Class<?> loadClass(String className) {
        return executor().loadClass(className);
    }

    @Override
    default boolean test(String test, Object params) {
        return executor().test(test, params);
    }

    default boolean test(String test) {
        return test(test, params());
    }

    @Override
    default Object eval(String script, Object params) {
        return executor().eval(script, params);
    }

    default Object eval(String script) {
        return eval(script, params());
    }

    @Override
    default Object evalScript(String lang, String script, Map<String, Object> params) {
        return executor().evalScript(lang, script, params);
    }

    default Object evalScript(String lang, String script) {
        return evalScript(lang, script, params());
    }

    @Override
    default void visitSet(Map<String, Object> params, String result, Object value) {
        executor().visitSet(params, result, value);
    }

    default void visitSet(String result, Object value) {
        visitSet(params(), result, value);
    }

    @Override
    default void visitDelete(Map<String, Object> params, String result) {
        executor().visitDelete(params, result);
    }

    default void visitDelete(String result) {
        visitDelete(params(), result);
    }

    @Override
    default Object visit(String script, Object params) {
        return executor().visit(script, params);
    }

    default Object visit(String script) {
        return visit(script, params());
    }

    @Override
    default String render(String script, Object params) {
        return executor().render(script, params);
    }

    default String render(String script) {
        return render(script, params());
    }

    @Override
    default Connection getConnection(String datasource, Map<String, Object> params) {
        return executor().getConnection(datasource, params);
    }

    default Connection getConnection(String datasource) {
        return getConnection(datasource, params());
    }

    @Override
    default List<?> sqlQueryList(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        return executor().sqlQueryList(datasource, bql, params, resultType);
    }

    default List<?> sqlQueryList(String datasource, BindSql bql, Class<?> resultType) {
        return sqlQueryList(datasource, bql, params(), resultType);
    }

    @Override
    default List<QueryColumn> sqlQueryColumns(String datasource, BindSql bql, Map<String, Object> params) {
        return executor().sqlQueryColumns(datasource, bql, params);
    }

    default List<QueryColumn> sqlQueryColumns(String datasource, BindSql bql) {
        return sqlQueryColumns(datasource, bql, params());
    }

    @Override
    default Object sqlQueryObject(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        return executor().sqlQueryObject(datasource, bql, params, resultType);
    }

    default Object sqlQueryObject(String datasource, BindSql bql, Class<?> resultType) {
        return sqlQueryObject(datasource, bql, params(), resultType);
    }

    @Override
    default Object sqlQueryRow(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        return executor().sqlQueryRow(datasource, bql, params, resultType);
    }

    default Object sqlQueryRow(String datasource, BindSql bql, Class<?> resultType) {
        return sqlQueryRow(datasource, bql, params(), resultType);
    }

    @Override
    default int sqlUpdate(String datasource, BindSql bql, Map<String, Object> params) {
        return executor().sqlUpdate(datasource, bql, params);
    }

    default int sqlUpdate(String datasource, BindSql bql) {
        return sqlUpdate(datasource, bql, params());
    }

    @Override
    default BindSql sqlWrapPage(String datasource, BindSql bql, ApiOffsetSize page, Map<String, Object> params) {
        return executor().sqlWrapPage(datasource, bql, page, params);
    }

    default BindSql sqlWrapPage(String datasource, BindSql bql, ApiOffsetSize page) {
        return sqlWrapPage(datasource, bql, page, params());
    }

    @Override
    default BindSql sqlWrapCount(String datasource, BindSql bql, Map<String, Object> params) {
        return executor().sqlWrapCount(datasource, bql, params);
    }

    default BindSql sqlWrapCount(String datasource, BindSql bql) {
        return sqlWrapCount(datasource, bql, params());
    }

    @Override
    default boolean sqlAdapt(String datasource, String databases, Map<String, Object> params) {
        return executor().sqlAdapt(datasource, databases, params);
    }

    default boolean sqlAdapt(String datasource, String databases) {
        return sqlAdapt(datasource, databases, params());
    }

    @Override
    default BindSql sqlScript(String datasource, List<Map.Entry<String, Object>> dialectScriptList, Map<String, Object> params, ApiOffsetSize page) {
        return executor().sqlScript(datasource, dialectScriptList, params, page);
    }

    default BindSql sqlScript(String datasource, List<Map.Entry<String, Object>> dialectScriptList, ApiOffsetSize page) {
        return sqlScript(datasource, dialectScriptList, params(), page);
    }

    @Override
    default List<?> sqlQueryPage(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType, ApiOffsetSize page) {
        return executor().sqlQueryPage(datasource, bql, params, resultType, page);
    }

    default List<?> sqlQueryPage(String datasource, BindSql bql, Class<?> resultType, ApiOffsetSize page) {
        return sqlQueryPage(datasource, bql, params(), resultType, page);
    }

    @Override
    default void sqlTransBegin(String datasource, int isolation, Map<String, Object> params) {
        executor().sqlTransBegin(datasource, isolation, params);
    }

    default void sqlTransBegin(String datasource, int isolation) {
        sqlTransBegin(datasource, isolation, params());
    }

    @Override
    default void sqlTransCommit(String datasource, Map<String, Object> params, boolean checked) {
        executor().sqlTransCommit(datasource, params, checked);
    }

    default void sqlTransCommit(String datasource, boolean checked) {
        sqlTransCommit(datasource, params(), checked);
    }

    @Override
    default void sqlTransRollback(String datasource, Map<String, Object> params, boolean checked) {
        executor().sqlTransRollback(datasource, params, checked);
    }

    default void sqlTransRollback(String datasource, boolean checked) {
        sqlTransRollback(datasource, params(), checked);
    }

    @Override
    default void sqlTransNone(String datasource, Map<String, Object> params, boolean checked) {
        executor().sqlTransRollback(datasource, params, checked);
    }

    default void sqlTransNone(String datasource, boolean checked) {
        sqlTransRollback(datasource, params(), checked);
    }


    default ProcedureMeta getMeta(String procedureId) {
        return getMeta(procedureId, params());
    }


    default Map<String, Object> call(String procedureId) {
        return call(procedureId, params());
    }


    default Map<String, Object> exec(String procedureId) {
        return exec(procedureId, params());
    }


    default Map<String, Object> exec(String procedureId, boolean beforeNewConnection, boolean afterCloseConnection) {
        return exec(procedureId, params(), beforeNewConnection, afterCloseConnection);
    }


    default Map<String, Object> exec(ProcedureMeta meta) {
        return exec(meta, params());
    }


    default Map<String, Object> exec(ProcedureMeta meta, boolean beforeNewConnection, boolean afterCloseConnection) {
        return exec(meta, params(), beforeNewConnection, afterCloseConnection);
    }


    default Map<String, Object> exec(JdbcProcedureJavaCaller caller) {
        return exec(caller, params());
    }


    default Map<String, Object> exec(XmlNode node) {
        return exec(node, params());
    }


    default Map<String, Object> execAsProcedure(String procedureId) {
        return execAsProcedure(procedureId, params());
    }


    default Map<String, Object> execAsProcedure(String procedureId, boolean beforeNewConnection, boolean afterCloseConnection) {
        return execAsProcedure(procedureId, params(), beforeNewConnection, afterCloseConnection);
    }


    default Map<String, Object> execAsProcedure(ProcedureMeta meta) {
        return execAsProcedure(meta, params());
    }


    default Map<String, Object> execAsProcedure(ProcedureMeta meta, boolean beforeNewConnection, boolean afterCloseConnection) {
        return execAsProcedure(meta, params(), beforeNewConnection, afterCloseConnection);
    }


    default Map<String, Object> execAsProcedure(JdbcProcedureJavaCaller caller) {
        return execAsProcedure(caller, params());
    }


    default Map<String, Object> execAsProcedure(JdbcProcedureJavaCaller caller, boolean beforeNewConnection, boolean afterCloseConnection) {
        return execAsProcedure(caller, params(), beforeNewConnection, afterCloseConnection);
    }


    default Map<String, Object> execAsProcedure(XmlNode node) {
        return execAsProcedure(node, params());
    }


    default <T> T evalAs(String script) {
        return evalAs(script, params());
    }


    default Object evalScriptUiInput(String lang) {
        return evalScriptUiInput(lang, params());
    }


    default <T> T visitAs(String script) {
        return visitAs(script, params());
    }


    default BindSql sqlScriptString(String datasource, List<Map.Entry<String, String>> dialectScriptList) {
        return sqlScriptString(datasource, dialectScriptList, params());
    }


    default BindSql sqlScriptBindSql(String datasource, List<Map.Entry<String, BindSql>> dialectScriptList) {
        return sqlScriptBindSql(datasource, dialectScriptList, params());
    }


    default BindSql sqlScript(String datasource, List<Map.Entry<String, Object>> dialectScriptList) {
        return sqlScript(datasource, dialectScriptList, params());
    }


    default void sqlTransCommit(String datasource) {
        sqlTransCommit(datasource, params());
    }


    default void sqlTransRollback(String datasource) {
        sqlTransRollback(datasource, params());
    }


    default void sqlTransNone(String datasource) {
        sqlTransNone(datasource, params());
    }
}
