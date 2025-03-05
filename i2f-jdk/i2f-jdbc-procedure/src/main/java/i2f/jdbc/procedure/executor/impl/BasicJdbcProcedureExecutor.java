package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.type.DatabaseType;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ContextHolder;
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
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;
import i2f.uid.SnowflakeLongUid;
import lombok.Data;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:40
 */
@Data
public class BasicJdbcProcedureExecutor implements JdbcProcedureExecutor {
    protected final CopyOnWriteArrayList<ExecutorNode> nodes = new CopyOnWriteArrayList<>();
    protected final AtomicBoolean debug = new AtomicBoolean(true);
    protected final DateTimeFormatter logTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS");

    {
        this.nodes.addAll(defaultExecutorNodes());
    }

    public static List<ExecutorNode> defaultExecutorNodes() {
        List<ExecutorNode> ret = new ArrayList<>();
        ret.add(new ContextConvertMethodClassNode());
        ret.add(new ContextInvokeMethodClassNode());
        ret.add(new ContextLoadPackageNode());
        ret.add(new DebuggerNode());
        ret.add(new FunctionCallNode());
        ret.add(new JavaCallNode());
        ret.add(new LangAsyncAllNode());
        ret.add(new LangAsyncNode());
        ret.add(new LangBreakNode());
        ret.add(new LangChooseNode());
        ret.add(new LangContinueNode());
        ret.add(new LangEvalGroovyNode());
        ret.add(new LangEvalJavaNode());
        ret.add(new LangEvalJavascriptNode());
        ret.add(new LangEvalTinyScriptNode());
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
        ret.add(new LangReturnNode());
        ret.add(new LangSetNode());
        ret.add(new LangSleepNode());
        ret.add(new LangStringJoinNode());
        ret.add(new LangStringNode());
        ret.add(new LangSynchronizedNode());
        ret.add(new LangThrowNode());
        ret.add(new LangTryNode());
        ret.add(new LangWhenNode());
        ret.add(new LangWhileNode());
        ret.add(new ProcedureCallNode());
        ret.add(new ProcedureNode());
        ret.add(new ScriptIncludeNode());
        ret.add(new ScriptSegmentNode());
        ret.add(new SqlCursorNode());
        ret.add(new SqlEtlNode());
        ret.add(new SqlQueryListNode());
        ret.add(new SqlQueryObjectNode());
        ret.add(new SqlQueryRowNode());
        ret.add(new SqlScopeNode());
        ret.add(new SqlScriptNode());
        ret.add(new SqlTransBeginNode());
        ret.add(new SqlTransCommitNode());
        ret.add(new SqlTransNoneNode());
        ret.add(new SqlTransRollbackNode());
        ret.add(new SqlUpdateNode());
        ret.add(new TextNode());

        ServiceLoader<ExecutorNode> nodes = ServiceLoader.load(ExecutorNode.class);
        for (ExecutorNode item : nodes) {
            ret.add(item);
        }
        return ret;
    }

    @Override
    public List<ExecutorNode> getNodes() {
        return nodes;
    }

    @Override
    public void debug(boolean enable) {
        this.debug.set(enable);
    }

    @Override
    public void debugLog(Supplier<Object> supplier) {
        if (debug.get()) {
            System.out.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "DEBUG", Thread.currentThread().getName(), String.valueOf(supplier.get())));
        }
    }

    @Override
    public void errorLog(Supplier<Object> supplier, Throwable e) {
        System.err.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "ERROR", Thread.currentThread().getName(), String.valueOf(supplier.get())));
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void openDebugger(String tag, Object context, String conditionExpression) {
        if (debug.get()) {
            System.out.println("debugger [" + tag + "] [" + conditionExpression + "] wait for input line to continue.");
            System.out.println("continue.");
        }
    }

    @Override
    public Map<String, Object> exec(XmlNode node, ExecuteContext context, boolean beforeNewConnection, boolean afterCloseConnection) {
        try {
            for (ExecutorNode item : getNodes()) {
                if (item.support(node)) {
                    debugLog(() -> "exec " + node.getTagName() + " by " + item.getClass().getSimpleName());
                    Map<String, Connection> bakConnection = context.paramsComputeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
                    if (beforeNewConnection) {
                        context.getParams().put(ParamsConsts.CONNECTIONS, new HashMap<>());
                    }
                    try {
                        item.exec(node, context, this);
                    } finally {
                        if (beforeNewConnection) {
                            closeConnections(context);
                            context.paramsSet(ParamsConsts.CONNECTIONS, bakConnection);
                        }
                        if (afterCloseConnection) {
                            closeConnections(context);
                        }
                    }
                    return context.getParams();
                }
            }
            debugLog(() -> "waring! tag " + node.getTagName() + " not found any executor!");
        } catch (ReturnSignalException e) {
            debugLog(() -> "return signal");
        }
        return context.getParams();
    }

    public void closeConnections(Map<String, Object> params) {
        if (params == null) {
            return;
        }
        Map<String, Connection> closeConnections = (Map<String, Connection>) params.computeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
        for (Map.Entry<String, Connection> entry : closeConnections.entrySet()) {
            Connection conn = entry.getValue();
            if (conn != null) {
                try {
                    debugLog(() -> "close connection : " + entry.getKey());
                    conn.close();
                } catch (SQLException e) {

                }
            }
        }
        params.put(ParamsConsts.CONNECTIONS, new HashMap<>());
    }

    public void closeConnections(ExecuteContext context) {
        closeConnections(context.getParams());
    }

    @Override
    public Map<String, Object> execAsProcedure(XmlNode node, ExecuteContext context, boolean beforeNewConnection, boolean afterCloseConnection) {
        try {
            ProcedureNode execNode = null;
            for (ExecutorNode item : getNodes()) {
                if (item instanceof ProcedureNode) {
                    execNode = (ProcedureNode) item;
                    break;
                }
            }
            if (execNode == null) {
                execNode = ProcedureNode.INSTANCE;
            }
            debugLog(() -> "exec as procedure " + node.getTagName());

            Map<String, Connection> bakConnection = context.paramsComputeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
            if (beforeNewConnection) {
                context.getParams().put(ParamsConsts.CONNECTIONS, new HashMap<>());
            }
            try {
                execNode.exec(node, context, this);
            } finally {
                if (beforeNewConnection) {
                    closeConnections(context);
                    context.paramsSet(ParamsConsts.CONNECTIONS, bakConnection);
                }
                if (afterCloseConnection) {
                    closeConnections(context);
                }
            }
        } catch (ReturnSignalException e) {
            debugLog(() -> "return signal");
        }
        return context.getParams();
    }

    @Override
    public Object attrValue(String attr, String action, XmlNode node, ExecuteContext context) {
        String attrScript = node.getTagAttrMap().get(attr);
        if (attrScript == null) {
            return null;
        }
        Object value = attrScript;

        String radixText = node.getTagAttrMap().get(AttrConsts.RADIX);
        if (radixText != null && !radixText.isEmpty()) {
            try {
                Object radixObj = attrValue(AttrConsts.RADIX, FeatureConsts.VISIT, node, context);
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
    public Map<String, Object> createParams() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put(ParamsConsts.CONTEXT, new HashMap<>());
        ret.put(ParamsConsts.ENVIRONMENT, new HashMap<>());
        ret.put(ParamsConsts.BEANS, new HashMap<>());

        ret.put(ParamsConsts.DATASOURCES, new HashMap<>());

        ret.put(ParamsConsts.CONNECTIONS, new HashMap<>());

        ret.put(ParamsConsts.GLOBAL, new HashMap<>());

        ret.put(ParamsConsts.EXECUTOR, this);
        return ret;
    }

    @Override
    public Map<String, Object> newParams(ExecuteContext context) {
        Map<String, Object> ret = new LinkedHashMap<>();
        if (context == null) {
            return createParams();
        }
        ret.put(ParamsConsts.CONTEXT, context.getParams().get(ParamsConsts.CONTEXT));
        ret.put(ParamsConsts.ENVIRONMENT, context.getParams().get(ParamsConsts.ENVIRONMENT));
        ret.put(ParamsConsts.BEANS, context.getParams().get(ParamsConsts.BEANS));

        ret.put(ParamsConsts.DATASOURCES, context.getParams().get(ParamsConsts.DATASOURCES));

        ret.put(ParamsConsts.GLOBAL, context.getParams().get(ParamsConsts.GLOBAL));

        ret.put(ParamsConsts.EXECUTOR, this);
        return ret;
    }

    public Object resolveFeature(Object value, String feature, XmlNode node, ExecuteContext context) {
        if (feature == null || feature.isEmpty()) {
            return value;
        }
        if (FeatureConsts.INT.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Integer.class);
        } else if (FeatureConsts.DOUBLE.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Double.class);
        } else if (FeatureConsts.FLOAT.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Float.class);
        } else if (FeatureConsts.STRING.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, String.class);
        } else if (FeatureConsts.LONG.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Long.class);
        } else if (FeatureConsts.SHORT.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Short.class);
        } else if (FeatureConsts.CHAR.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Character.class);
        } else if (FeatureConsts.BYTE.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Byte.class);
        } else if (FeatureConsts.BOOLEAN.equals(feature)) {
            return ObjectConvertor.tryConvertAsType(value, Boolean.class);
        } else if (FeatureConsts.RENDER.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return render(text, context.getParams());
        } else if (FeatureConsts.VISIT.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return visit(text, context.getParams());
        } else if (FeatureConsts.EVAL.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return eval(text, context.getParams());
        } else if (FeatureConsts.TEST.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return test(text, context.getParams());
        } else if (FeatureConsts.NULL.equals(feature)) {
            return null;
        } else if (FeatureConsts.DATE.equals(feature)) {
            String text = String.valueOf(value);
            String patternText = node.getTagAttrMap().get(AttrConsts.PATTERN);
            boolean processed = false;
            try {
                if (patternText != null) {
                    Object patternValue = attrValue(AttrConsts.PATTERN, FeatureConsts.VISIT, node, context);
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
        } else if (FeatureConsts.TRIM.equals(feature)) {
            if (value == null) {
                return null;
            }
            return String.valueOf(value).trim();
        } else if (FeatureConsts.ALIGN.equals(feature)) {
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
        } else if (FeatureConsts.BODY_TEXT.equals(feature)) {
            return node.getTextBody();
        } else if (FeatureConsts.BODY_XML.equals(feature)) {
            return node.getTagBody();
        } else if (FeatureConsts.SPACING_LEFT.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return " " + text;
        } else if (FeatureConsts.SPACING_RIGHT.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return text + " ";
        } else if (FeatureConsts.SPACING.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return " " + text + " ";
        } else if (FeatureConsts.EVAL_JAVA.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalJavaNode.evalJava(context, this, "", "", text);
        } else if (FeatureConsts.EVAL_JS.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalJavascriptNode.evalJavascript(text, context, this);
        } else if (FeatureConsts.EVAL_TINYSCRIPT.equals(feature)
                || FeatureConsts.EVAL_TS.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalTinyScriptNode.evalTinyScript(text, context, this);
        } else if (FeatureConsts.EVAL_GROOVY.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalGroovyNode.evalGroovyScript(text, context, this);
        } else if (FeatureConsts.CLASS.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return loadClass(text);
        } else if (FeatureConsts.NOT.equals(feature)) {
            boolean ok = toBoolean(value);
            return !ok;
        } else if (FeatureConsts.DIALECT.equals(feature)) {
            try {
                String databases = value == null ? null : String.valueOf(value);
                String datasource = node.getTagAttrMap().get(AttrConsts.DATASOURCE);
                boolean ok = supportDatabaseDialect(datasource, databases, context.getParams());
                return ok;
            } catch (Exception e) {

            }
            return false;
        } else if (FeatureConsts.IS_NULL.equals(feature)) {
            return value == null;
        } else if (FeatureConsts.IS_NOT_NULL.equals(feature)) {
            return value != null;
        } else if (FeatureConsts.IS_EMPTY.equals(feature)) {
            return value == null || "".equals(value);
        } else if (FeatureConsts.IS_NOT_EMPTY.equals(feature)) {
            return value != null && !"".equals(value);
        } else if (FeatureConsts.DATE_NOW.equals(feature)) {
            return new Date();
        } else if (FeatureConsts.UUID.equals(feature)) {
            return UUID.randomUUID().toString();
        } else if (FeatureConsts.CURRENT_TIME_MILLIS.equals(feature)) {
            return System.currentTimeMillis();
        } else if (FeatureConsts.SNOW_UID.equals(feature)) {
            return SnowflakeLongUid.getId();
        } else {
            try {
                Method method = ContextHolder.CONVERT_METHOD_MAP.get(feature);
                if (method != null) {
                    return method.invoke(null, value);
                }
            } catch (Exception e) {

            }
            try {
                Function function = ContextHolder.CONVERT_FUNC_MAP.get(feature);
                if (function != null) {
                    return function.apply(value);
                }
            } catch (Exception e) {

            }
        }
        return value;
    }

    public boolean toBoolean(Object obj) {
        return ObjectConvertor.toBoolean(obj);
    }


    @Override
    public Class<?> loadClass(String className) {
        Class<?> ret = ReflectResolver.loadClass(className);
        if (ret != null) {
            return ret;
        }

        List<String> prefixes = new ArrayList<>(Arrays.asList(new String[]{
                "",
        }));
        prefixes.addAll(ContextHolder.LOAD_PACKAGE_SET);
        Class<?> clazz = null;
        for (String prefix : prefixes) {
            try {
                clazz = ReflectResolver.loadClass0(prefix + className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {

            }
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
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


    @Override
    public Connection getConnection(String datasource, Map<String, Object> params) {
        if (datasource == null || datasource.isEmpty()) {
            datasource = ParamsConsts.DEFAULT_DATASOURCE;
        }
        Map<String, Connection> connectionMap = (Map<String, Connection>) params.get(ParamsConsts.CONNECTIONS);
        if (connectionMap == null) {
            connectionMap = new LinkedHashMap<>();
            params.put(ParamsConsts.CONNECTIONS, connectionMap);
        }
        Connection conn = connectionMap.get(datasource);
        if (conn != null) {
            return conn;
        }

        Map<String, DataSource> datasourceMap = (Map<String, DataSource>) params.get(ParamsConsts.DATASOURCES);
        DataSource ds = datasourceMap.get(datasource);
        if (ds != null) {
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }


        if (conn != null) {
            connectionMap.put(datasource, conn);
        }

        return conn;
    }

    public BindSql resolveSqlScript(String script, Map<String, Object> params) throws Exception {
        MybatisMapperNode mapperNode = MybatisMapperParser.parseScriptNode(script);
        BindSql bql = MybatisMapperInflater.INSTANCE.inflateSqlNode(mapperNode, params, new HashMap<>());
        return bql;
    }

    public boolean supportDatabaseDialect(String datasource, String databases, Map<String, Object> params) throws Exception {
        if (databases == null) {
            return false;
        }
        Connection conn = getConnection(datasource, params);
        List<String> databaseNames = detectDatabaseType(conn);
        String[] arr = databases.split(",");
        for (String item : arr) {
            for (String databaseName : databaseNames) {
                if (item.equalsIgnoreCase(databaseName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> detectDatabaseType(Connection conn) throws Exception {
        List<String> ret = new ArrayList<>();
        DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
        if (databaseType == null) {
            return ret;
        }
        String name = databaseType.db();
        String enumName = databaseType.name();
        ret.add(name.toLowerCase());
        ret.add(enumName.toLowerCase());
        return ret;
    }

    public Map.Entry<String, String> getDialectSqlScript(List<Map.Entry<String, String>> dialectScriptList, Connection conn) throws Exception {
        List<String> databaseNames = detectDatabaseType(conn);
        Map.Entry<String, String> firstScript = null;
        Map.Entry<String, String> nullScript = null;
        for (Map.Entry<String, String> entry : dialectScriptList) {
            if (firstScript == null) {
                firstScript = entry;
            }
            String key = entry.getKey();
            if (key == null || key.isEmpty()) {
                if (nullScript == null) {
                    nullScript = entry;
                }
            }
            if (key != null) {
                String[] arr = key.split(",");
                for (String item : arr) {
                    for (String databaseName : databaseNames) {
                        if (item.equalsIgnoreCase(databaseName)) {
                            return entry;
                        }
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
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            setParamsObject(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = resolveSqlScript(script, params);
            debugLog(() -> "sqlQueryList:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + bql);
            List<?> list = JdbcResolver.list(conn, bql, resultType, -1, TypeOf.typeOf(resultType, Map.class) ? (String::toUpperCase) : null);
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @Override
    public Object sqlQueryObject(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            setParamsObject(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = resolveSqlScript(script, params);
            debugLog(() -> "sqlQueryObject:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + bql);
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
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            setParamsObject(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = resolveSqlScript(script, params);
            debugLog(() -> "sqlQueryRow:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + bql);
            Object row = JdbcResolver.find(conn, bql, resultType, TypeOf.typeOf(resultType, Map.class) ? (String::toUpperCase) : null);
            return row;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public int sqlUpdate(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            setParamsObject(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = sqlScript(datasource, dialectScriptList, params);
            debugLog(() -> "sqlUpdate:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + bql);
            int num = JdbcResolver.update(conn, bql);
            return num;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public BindSql sqlScript(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            setParamsObject(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = resolveSqlScript(script, params);
            debugLog(() -> "sqlScript:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + bql);
            return bql;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<?> sqlQueryPage(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, Class<?> resultType, int pageIndex, int pageSize) {
        try {
            Connection conn = getConnection(datasource, params);
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            setParamsObject(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = resolveSqlScript(script, params);
            IPageWrapper wrapper = PageWrappers.wrapper(conn);
            BindSql pageBql = wrapper.apply(bql, ApiPage.of(pageIndex, pageSize));
            debugLog(() -> "sqlQueryPage:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + pageBql);
            List<?> list = JdbcResolver.list(conn, pageBql, resultType, -1, TypeOf.typeOf(resultType, Map.class) ? (String::toUpperCase) : null);
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
