package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.impl.*;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ReturnSignalException;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;
import i2f.page.ApiPage;
import i2f.reflect.vistor.Visitor;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:40
 */
@Data
public class JdbcProcedureExecutorImpl implements JdbcProcedureExecutor {
    protected final CopyOnWriteArrayList<ExecutorNode> nodes = new CopyOnWriteArrayList<>();

    private JdbcProcedureExecutorImpl() {

    }

    public static List<ExecutorNode> defaultExecutorNodes() {
        List<ExecutorNode> ret = new ArrayList<>();
        ret.add(new LangAsyncAllNode());
        ret.add(new LangAsyncNode());
        ret.add(new LangBreakNode());
        ret.add(new LangChooseNode());
        ret.add(new LangContinueNode());
        ret.add(new LangEvalNode());
        ret.add(new LangForeachNode());
        ret.add(new LangForiNode());
        ret.add(new LangFormatDateNode());
        ret.add(new LangFormatNode());
        ret.add(new LangIfNode());
        ret.add(new LangInvokeNode());
        ret.add(new LangLatchAwaitNode());
        ret.add(new LangLatchDownNode());
        ret.add(new LangLatchNode());
        ret.add(new LangNewParamsNode());
        ret.add(new LangPrintfNode());
        ret.add(new LangPrintlnNode());
        ret.add(new LangRenderNode());
        ret.add(new LangSetNode());
        ret.add(new LangStringJoinNode());
        ret.add(new LangStringNode());
        ret.add(new LangThrowNode());
        ret.add(new LangTryNode());
        ret.add(new LangWhenNode());
        ret.add(new LangWhileNode());
        ret.add(new ProcedureCallNode());
        ret.add(new ProcedureNode());
        ret.add(new ScriptIncludeNode());
        ret.add(new ScriptSegmentNode());
        ret.add(new SqlCursorNode());
        ret.add(new SqlQueryListNode());
        ret.add(new SqlQueryObjectNode());
        ret.add(new SqlQueryRowNode());
        ret.add(new SqlTransBeginNode());
        ret.add(new SqlTransCommitNode());
        ret.add(new SqlTransNoneNode());
        ret.add(new SqlTransRollbackNode());
        ret.add(new SqlUpdateNode());
        ret.add(new TextNode());
        return ret;
    }

    public static JdbcProcedureExecutor create() {
        JdbcProcedureExecutorImpl ret = new JdbcProcedureExecutorImpl();
        ret.getNodes().addAll(defaultExecutorNodes());
        return ret;
    }

    @Override
    public void exec(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        try {
            for (ExecutorNode item : nodes) {
                if (item.support(node)) {
                    item.exec(node, params, nodeMap, this);
                    break;
                }
            }
        } catch (ReturnSignalException e) {

        }
    }

    @Override
    public void execAsProducer(XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        ProcedureNode execNode = null;
        for (ExecutorNode item : nodes) {
            if (item instanceof ProcedureNode) {
                execNode = (ProcedureNode) item;
                break;
            }
        }
        if (execNode == null) {
            execNode = ProcedureNode.INSTANCE;
        }
        execNode.exec(node, params, nodeMap, this);
    }

    @Override
    public Object attrValue(String attr, String action, XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        String attrScript = node.getTagAttrMap().get(attr);
        if (attrScript == null) {
            return null;
        }
        Object value = attrScript;

        String radixText = node.getTagAttrMap().get("radix");
        if (radixText != null && !radixText.isEmpty()) {
            try {
                Object radixObj = attrValue("radix", "visit", node, params, nodeMap);
                if (radixObj != null) {
                    radixObj = ObjectConvertor.tryConvertAsType(radixObj, Integer.class);
                    if (radixObj instanceof Integer) {
                        value = Long.parseLong(String.valueOf(value), (int) radixObj);
                    }
                }
            } catch (Exception e) {

            }
        }

        List<String> features = node.getAttrFeatureMap().get(attr);
        if (features != null && !features.isEmpty()) {
            for (String feature : features) {
                if (feature == null || feature.isEmpty()) {
                    continue;
                }
                value = resolveFeature(value, feature, node, params, nodeMap);
            }
        } else {
            value = resolveFeature(attrScript, action, node, params, nodeMap);
        }

        return value;
    }

    @Override
    public Object resultValue(Object value, List<String> features, XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        if (features == null || features.isEmpty()) {
            return value;
        }
        for (String feature : features) {
            if (feature == null || feature.isEmpty()) {
                continue;
            }
            value = resolveFeature(value, feature, node, params, nodeMap);
        }
        return value;
    }

    @Override
    public void setParamsObject(Map<String, Object> params, String result, Object value) {
        Visitor visitor = Visitor.visit(result, params);
        visitor.set(value);
    }

    @Override
    public Map<String, Object> newParams(Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        Map<String, Object> ret = new LinkedHashMap<>();

        return ret;
    }

    public Object resolveFeature(Object value, String feature, XmlNode node, Map<String, Object> params, Map<String, XmlNode> nodeMap) {
        if (feature == null || feature.isEmpty()) {
            return value;
        }
        if ("int".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Integer.class);
        } else if ("double".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Double.class);
        } else if ("float".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Float.class);
        } else if ("string".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, String.class);
        } else if ("long".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Long.class);
        } else if ("short".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Short.class);
        } else if ("char".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Character.class);
        } else if ("byte".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Byte.class);
        } else if ("boolean".equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Boolean.class);
        } else if ("render".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return render(text, params);
        } else if ("visit".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return visit(text, params);
        } else if ("eval".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return eval(text, params);
        } else if ("test".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return test(text, params);
        } else if ("null".equals(feature)) {
            return null;
        } else if ("date".equals(feature)) {
            String text = String.valueOf(value);
            String patternText = node.getTagAttrMap().get("pattern");
            boolean processed = false;
            try {
                if (patternText != null) {
                    Object patternValue = attrValue("radix", "visit", node, params, nodeMap);
                    if (patternValue != null) {
                        value = new SimpleDateFormat(patternText).parse(text);
                        processed = true;
                    }
                }
            } catch (Exception e) {

            }
            if (!processed) {
                value = ObjectConvertor.tryParseDate(text);
            }
            return value;
        } else if ("trim".equals(feature)) {
            if (value == null) {
                return null;
            }
            return String.valueOf(value).trim();
        } else if ("align".equals(feature)) {
            if (value == null) {
                return null;
            }
            String text = String.valueOf(value);
            String[] lines = text.split("\n");
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                int idx = line.indexOf("|");
                if (idx >= 0) {
                    builder.append(line.substring(idx + 1));
                } else {
                    builder.append(line);
                }
                builder.append("\n");
            }
            return builder.toString();
        } else if ("body-text".equals(feature)) {
            return node.getTextBody();
        } else if ("body-xml".equals(feature)) {
            return node.getTagBody();
        }
        return value;
    }

    @Override
    public Class<?> loadClass(String className) {
        if ("long".equals(className)) {
            return Long.class;
        }
        if ("int".equals(className)) {
            return Integer.class;
        }
        if ("short".equals(className)) {
            return Short.class;
        }
        if ("byte".equals(className)) {
            return Byte.class;
        }
        if ("char".equals(className)) {
            return Character.class;
        }
        if ("float".equals(className)) {
            return Float.class;
        }
        if ("double".equals(className)) {
            return Double.class;
        }
        if ("boolean".equals(className)) {
            return Boolean.class;
        }
        String[] prefixes = {
                "",
                "java.lang.",
                "java.util.",
                "java.sql.",
                "java.math.",
                "java.io.",
                "java.time.",
                "java.text.",
                "java.lang.reflect.",
                "java.concurrent.",
                "java.util.regex.",
                "javax.sql.",
                "jakarta.sql.",
                "java.nio.",
                "java.nio.charset.",
                "java.nio.file.",
                "java.concurrent.atomic.",
                "java.concurrent.locks.",
                "java.time.chrono.",
                "java.time.format.",
                "java,time.temporal.",
                "java.time.zone.",
        };
        Class<?> clazz = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        for (String prefix : prefixes) {
            try {
                clazz = loader.loadClass(prefix + className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {

            }
            try {
                clazz = Class.forName(prefix + className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {

            }
        }
        return clazz;
    }


    @Override
    public boolean test(String test, Map<String, Object> params) {
        System.out.println("test:" + test);
        try {
            boolean ok = Boolean.parseBoolean(test);
            return ok;
        } catch (Exception e) {

        }
        return MybatisMapperInflater.INSTANCE.testExpression(test,params);
    }

    @Override
    public Object eval(String script, Map<String, Object> params) {
        System.out.println("eval:" + script);
        try {
            return Integer.parseInt(script);
        } catch (Exception e) {

        }
        try {
            return Long.parseLong(script);
        } catch (Exception e) {

        }
        try {
            return Boolean.parseBoolean(script);
        } catch (Exception e) {

        }
        try {
            return Double.parseDouble(script);
        } catch (Exception e) {

        }
        return MybatisMapperInflater.INSTANCE.evalExpression(script,params);
    }

    @Override
    public Object visit(String script, Map<String, Object> params) {
        System.out.println("visit:" + script);
        if (params.containsKey(script)) {
            return params.get(script);
        }
        return Visitor.visit(script, params).get();
    }

    @Override
    public String render(String script, Map<String, Object> params) {
        System.out.println("render:" + script);
        return script;
    }

    public static final ThreadLocal<Map<String, Connection>> connectionMapHolder = ThreadLocal.withInitial(() -> new HashMap<>());

    public Connection getConnection(String datasource, Map<String, Object> params) {
        if (datasource == null || datasource.isEmpty()) {
            datasource = "primary";
        }
        Map<String, Connection> connectionMap = connectionMapHolder.get();
        Connection conn = connectionMap.get(datasource);
        if (conn != null) {
            return conn;
        }

        Map<String, Connection> connMap = (Map<String, Connection>) params.get("connections");
        if (conn == null) {
            if (connMap != null) {
                conn = connMap.get(datasource);
            }
        }


        if (conn == null) {
            Map<String, DataSource> datasourceMap = (Map<String, DataSource>) params.get("datasources");
            DataSource ds = datasourceMap.get(datasource);
            if (ds != null) {
                try {
                    conn = ds.getConnection();
                } catch (SQLException e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        }

        if (conn != null) {
            if (connectionMap != null) {
                connectionMap.put(datasource, conn);
            }
            if (connMap != null) {
                connMap.put(datasource, conn);
            }
        }

        return conn;
    }

    public BindSql resolveSqlScript(String script,Map<String,Object> params) throws Exception {
        MybatisMapperNode mapperNode = MybatisMapperParser.parseScriptNode(script);
        BindSql bql = MybatisMapperInflater.INSTANCE.inflateSqlNode(mapperNode, params, new HashMap<>());
        return bql;
    }

    @Override
    public List<?> sqlQueryList(String datasource, String script, Map<String, Object> params, Class<?> resultType) {
        System.out.println("sqlQueryList:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            BindSql bql = resolveSqlScript(script, params);
            List<?> list = JdbcResolver.list(conn, bql, resultType);
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @Override
    public Object sqlQueryObject(String datasource, String script, Map<String, Object> params, Class<?> resultType) {
        System.out.println("sqlQueryObject:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            BindSql bql = resolveSqlScript(script, params);
            Object obj = JdbcResolver.get(conn,bql, resultType);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object sqlQueryRow(String datasource, String script, Map<String, Object> params, Class<?> resultType) {
        System.out.println("sqlQueryRow:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            BindSql bql = resolveSqlScript(script, params);
            Object row = JdbcResolver.find(conn, bql, resultType);
            return row;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public int sqlUpdate(String datasource, String script, Map<String, Object> params) {
        System.out.println("sqlUpdate:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            BindSql bql = resolveSqlScript(script, params);
            int num = JdbcResolver.update(conn, bql);
            return num;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<?> sqlQueryPage(String datasource, String script, Map<String, Object> params, Class<?> resultType, int pageIndex, int pageSize) {
        System.out.println("sqlQueryPage:" + datasource + ", " + script);
        try {
            Connection conn = getConnection(datasource, params);
            BindSql bql = resolveSqlScript(script, params);
            IPageWrapper wrapper = PageWrappers.wrapper(conn);
            BindSql pageBql = wrapper.apply(bql, ApiPage.of(pageIndex, pageSize));
            List<?> list = JdbcResolver.list(conn, pageBql, resultType);
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransBegin(String datasource, int isolation, Map<String, Object> params) {
        System.out.println("sqlTransBegin:" + datasource);
        try {
            if (isolation < 0) {
                isolation = Connection.TRANSACTION_READ_COMMITTED;
            }
            Connection conn = getConnection(datasource, params);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(isolation);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransCommit(String datasource, Map<String, Object> params) {
        System.out.println("sqlTransCommit:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.commit();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransRollback(String datasource, Map<String, Object> params) {
        System.out.println("sqlTransRollback:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.rollback();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransNone(String datasource, Map<String, Object> params) {
        System.out.println("sqlTransNone:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


}
