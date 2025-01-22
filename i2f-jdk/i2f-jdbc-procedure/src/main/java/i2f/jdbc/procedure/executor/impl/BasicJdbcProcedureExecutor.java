package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.type.DatabaseType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.procedure.context.ExecuteContext;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:40
 */
@Data
public class BasicJdbcProcedureExecutor implements JdbcProcedureExecutor {
    protected final CopyOnWriteArrayList<ExecutorNode> nodes = new CopyOnWriteArrayList<>();
    protected final AtomicBoolean debug = new AtomicBoolean(false);
    protected final DateTimeFormatter logTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS");

    {
        this.nodes.addAll(defaultExecutorNodes());
    }

    public static List<ExecutorNode> defaultExecutorNodes() {
        List<ExecutorNode> ret = new ArrayList<>();
        ret.add(new LangAsyncAllNode());
        ret.add(new LangAsyncNode());
        ret.add(new LangBreakNode());
        ret.add(new LangChooseNode());
        ret.add(new LangContinueNode());
        ret.add(new LangEvalJavaNode());
        ret.add(new LangEvalJavascriptNode());
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

    @Override
    public void debugLog(Supplier<String> supplier) {
        if (debug.get()) {
            System.out.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "DEBUG", Thread.currentThread().getName(), supplier.get()));
        }
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context) {
        try {
            for (ExecutorNode item : nodes) {
                if (item.support(node)) {
                    debugLog(() -> "exec " + node.getTagName() + " by " + item.getClass().getSimpleName());
                    item.exec(node, context, this);
                    return;
                }
            }
            debugLog(() -> "waring! tag " + node.getTagName() + " not found any executor!");
        } catch (ReturnSignalException e) {
            debugLog(() -> "return signal");
        }
    }

    @Override
    public void execAsProcedure(XmlNode node, ExecuteContext context) {
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
        debugLog(() -> "exec as procedure " + node.getTagName());
        execNode.exec(node, context, this);
    }

    @Override
    public Object attrValue(String attr, String action, XmlNode node, ExecuteContext context) {
        String attrScript = node.getTagAttrMap().get(attr);
        if (attrScript == null) {
            return null;
        }
        Object value = attrScript;

        String radixText = node.getTagAttrMap().get("radix");
        if (radixText != null && !radixText.isEmpty()) {
            try {
                Object radixObj = attrValue("radix", "visit", node, context);
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
                value = resolveFeature(value, feature, node, context);
            }
        } else {
            value = resolveFeature(attrScript, action, node, context);
        }

        return value;
    }

    @Override
    public Object resultValue(Object value, List<String> features, XmlNode node, ExecuteContext context) {
        if (features == null || features.isEmpty()) {
            return value;
        }
        for (String feature : features) {
            if (feature == null || feature.isEmpty()) {
                continue;
            }
            value = resolveFeature(value, feature, node, context);
        }
        return value;
    }

    @Override
    public void setParamsObject(Map<String, Object> params, String result, Object value) {
        Visitor visitor = Visitor.visit(result, params);
        visitor.set(value);
    }

    @Override
    public Map<String, Object> newParams(ExecuteContext context) {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put("context", context.getParams().get("context"));
        ret.put("datasources", context.getParams().get("datasources"));
        ret.put("beans", context.getParams().get("beans"));

        return ret;
    }

    public Object resolveFeature(Object value, String feature, XmlNode node, ExecuteContext context) {
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
            return render(text, context.getParams());
        } else if ("visit".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return visit(text, context.getParams());
        } else if ("eval".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return eval(text, context.getParams());
        } else if ("test".equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return test(text, context.getParams());
        } else if ("null".equals(feature)) {
            return null;
        } else if ("date".equals(feature)) {
            String text = String.valueOf(value);
            String patternText = node.getTagAttrMap().get("pattern");
            boolean processed = false;
            try {
                if (patternText != null) {
                    Object patternValue = attrValue("radix", "visit", node, context);
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
        if (clazz == null) {
            clazz = innerLoadClass(className, loader);
        }
        return clazz;
    }

    public Class<?> innerLoadClass(String className, ClassLoader loader) {
        return null;
    }

    public String unescapeTestScript(String script) {
        if (script == null) {
            return script;
        }
        script = script.replaceAll("\\s+gte\\s+", " >= ");
        script = script.replaceAll("\\s+lte\\s+", " <= ");
        script = script.replaceAll("\\s+ge\\s+", " >= ");
        script = script.replaceAll("\\s+le\\s+", " <= ");
        script = script.replaceAll("\\s+gt\\s+", " > ");
        script = script.replaceAll("\\s+lt\\s+", " < ");
        script = script.replaceAll("\\s+eq\\s+", " == ");
        script = script.replaceAll("\\s+ne\\s+", " != ");
        return script;
    }

    public String unescapeXmlScript(String script) {
        if (script == null) {
            return script;
        }
        script = script.replaceAll("&gt;", ">");
        script = script.replaceAll("&lt;", "<");
        script = script.replaceAll("&nbsp;", " ");
        script = script.replaceAll("&emsp;", "\t");
        return script;
    }

    @Override
    public boolean test(String script, Map<String, Object> params) {
        String test = unescapeTestScript(script);
        debugLog(() -> "test:" + test);
        try {
            Boolean ok = ObjectConvertor.tryParseBoolean(test);
            if (ok != null) {
                return ok;
            }
        } catch (Exception e) {

        }
        return innerTest(test, params);
    }

    public boolean innerTest(String test, Map<String, Object> params) {
        return MybatisMapperInflater.INSTANCE.testExpression(test, params);
    }

    @Override
    public Object eval(String script, Map<String, Object> params) {
        debugLog(() -> "eval:" + script);
        try {
            return Integer.parseInt(script);
        } catch (Exception e) {

        }
        try {
            return Long.parseLong(script);
        } catch (Exception e) {

        }
        try {
            Boolean ok = ObjectConvertor.tryParseBoolean(script);
            if (ok != null) {
                return ok;
            }
        } catch (Exception e) {

        }
        try {
            return Double.parseDouble(script);
        } catch (Exception e) {

        }
        return innerEval(script, params);
    }

    public Object innerEval(String script, Map<String, Object> params) {
        return MybatisMapperInflater.INSTANCE.evalExpression(script, params);
    }

    @Override
    public Object visit(String script, Map<String, Object> params) {
        debugLog(() -> "visit:" + script);
        if (params.containsKey(script)) {
            return params.get(script);
        }
        Visitor visitor = Visitor.visit(script, params);
        if (visitor != null) {
            return visitor.get();
        }
        return innerVisit(script, params);
    }

    public Object innerVisit(String script, Map<String, Object> params) {
        return null;
    }

    @Override
    public String render(String script, Map<String, Object> params) {
        debugLog(() -> "render:" + script);
        return innerRender(script, params);
    }

    public String innerRender(String script, Map<String, Object> params) {
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

    public BindSql resolveSqlScript(String script, Map<String, Object> params) throws Exception {
        MybatisMapperNode mapperNode = MybatisMapperParser.parseScriptNode(script);
        BindSql bql = MybatisMapperInflater.INSTANCE.inflateSqlNode(mapperNode, params, new HashMap<>());
        return bql;
    }

    public String getDialectSqlScript(List<Map.Entry<String, String>> dialectScriptList, Connection conn) throws Exception {
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        String name = databaseType.db();
        String enumName = databaseType.name();
        String firstScript = null;
        String nullScript = null;
        for (Map.Entry<String, String> entry : dialectScriptList) {
            if (firstScript == null) {
                firstScript = entry.getValue();
            }
            String key = entry.getKey();
            if (key == null || key.isEmpty()) {
                if (nullScript == null) {
                    nullScript = entry.getValue();
                }
            }
            if (key != null) {
                String[] arr = key.split(",");
                for (String item : arr) {
                    if (item.equalsIgnoreCase(name)) {
                        return entry.getValue();
                    }
                    if (item.equalsIgnoreCase(enumName)) {
                        return entry.getValue();
                    }
                }
            }
        }
        if (nullScript != null) {
            return nullScript;
        }
        return firstScript;
    }

    @Override
    public List<?> sqlQueryList(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            String script = getDialectSqlScript(dialectScriptList, conn);
            debugLog(() -> "sqlQueryList:" + datasource + ", " + script);
            BindSql bql = resolveSqlScript(script, params);
            List<?> list = JdbcResolver.list(conn, bql, resultType);
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @Override
    public Object sqlQueryObject(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            String script = getDialectSqlScript(dialectScriptList, conn);
            debugLog(() -> "sqlQueryObject:" + datasource + ", " + script);
            BindSql bql = resolveSqlScript(script, params);
            Object obj = JdbcResolver.get(conn, bql, resultType);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object sqlQueryRow(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            String script = getDialectSqlScript(dialectScriptList, conn);
            debugLog(() -> "sqlQueryRow:" + datasource + ", " + script);
            BindSql bql = resolveSqlScript(script, params);
            Object row = JdbcResolver.find(conn, bql, resultType);
            return row;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public int sqlUpdate(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            String script = getDialectSqlScript(dialectScriptList, conn);
            debugLog(() -> "sqlUpdate:" + datasource + ", " + script);
            BindSql bql = resolveSqlScript(script, params);
            int num = JdbcResolver.update(conn, bql);
            return num;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<?> sqlQueryPage(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType, int pageIndex, int pageSize) {
        try {
            Connection conn = getConnection(datasource, params);
            String script = getDialectSqlScript(dialectScriptList, conn);
            debugLog(() -> "sqlQueryPage:" + datasource + ", " + script);
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
        debugLog(() -> "sqlTransBegin:" + datasource);
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
        debugLog(() -> "sqlTransCommit:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.commit();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransRollback(String datasource, Map<String, Object> params) {
        debugLog(() -> "sqlTransRollback:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.rollback();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransNone(String datasource, Map<String, Object> params) {
        debugLog(() -> "sqlTransNone:" + datasource);
        try {
            Connection conn = getConnection(datasource, params);
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


}
