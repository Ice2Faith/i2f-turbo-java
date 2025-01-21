package i2f.jdbc.procedure.executor;


import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/1/20 10:38
 */
public interface JdbcProcedureExecutor {
    List<ExecutorNode> getNodes();

    void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap);

    void execAsProducer(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap);

    Object applyFeatures(Object value, List<String> features,
                         Map<String,Object> params,
                         XmlNode node);

    Class<?> loadClass(String className);

    boolean test(String test, Map<String, Object> params);

    Object eval(String script, Map<String, Object> params);

    Object visit(String collectionScript, Map<String, Object> params);

    String render(String script, Map<String, Object> params);

    List<?> sqlQueryList(String datasource, String script, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryObject(String datasource, String script, Map<String, Object> params, Class<?> resultType);

    Object sqlQueryRow(String datasource, String script, Map<String, Object> params, Class<?> resultType);

    int sqlUpdate(String datasource, String script, Map<String, Object> params);

    void sqlTransBegin(String datasource, int isolation, Map<String, Object> params);

    void sqlTransCommit(String datasource, Map<String, Object> params);

    void sqlTransRollback(String datasource, Map<String, Object> params);

    void sqlTransNone(String datasource, Map<String, Object> params);
}
