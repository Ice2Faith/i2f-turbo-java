package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.count.CountWrappers;
import i2f.bindsql.count.ICountWrapper;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.context.impl.ListableNamingContext;
import i2f.context.std.INamingContext;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.type.DatabaseType;
import i2f.environment.impl.ListableDelegateEnvironment;
import i2f.environment.std.IEnvironment;
import i2f.invokable.method.IMethod;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.impl.*;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;
import i2f.lru.LruMap;
import i2f.page.ApiOffsetSize;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;
import i2f.uid.SnowflakeLongUid;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:40
 */
@Data
public class BasicJdbcProcedureExecutor implements JdbcProcedureExecutor {
    protected transient final AtomicReference<ProcedureNode> procedureNodeHolder=new AtomicReference<>();
    protected volatile JdbcProcedureContext context=new DefaultJdbcProcedureContext();
    protected volatile IEnvironment environment=new ListableDelegateEnvironment();
    protected volatile INamingContext namingContext=new ListableNamingContext();
    protected final CopyOnWriteArrayList<ExecutorNode> nodes = new CopyOnWriteArrayList<>();
    protected final AtomicBoolean debug = new AtomicBoolean(true);
    protected final DateTimeFormatter logTimeFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS");

    {
        addNodes();
        this.nodes.addAll(defaultExecutorNodes());
    }

    public BasicJdbcProcedureExecutor(){

    }

    public BasicJdbcProcedureExecutor(JdbcProcedureContext context){
        this.context=context;
    }

    public BasicJdbcProcedureExecutor(JdbcProcedureContext context, IEnvironment environment, INamingContext namingContext){
        this.context=context;
        this.environment=environment;
        this.namingContext=namingContext;
    }


    protected void addNodes(){

    }

    public static List<ExecutorNode> defaultExecutorNodes() {
        List<ExecutorNode> ret = new ArrayList<>();
        ServiceLoader<ExecutorNode> nodes = ServiceLoader.load(ExecutorNode.class);
        for (ExecutorNode item : nodes) {
            ret.add(item);
        }
        ret.add(new ContextConvertMethodClassNode());
        ret.add(new ContextInvokeMethodClassNode());
        ret.add(new ContextLoadPackageNode());
        ret.add(new DebuggerNode());
        ret.add(new FunctionCallNode());
        ret.add(new JavaCallNode());
        ret.add(new LangAsyncAllNode());
        ret.add(new LangAsyncNode());
        ret.add(new LangBodyNode());
        ret.add(new LangBreakNode());
        ret.add(new LangChooseNode());
        ret.add(new LangContinueNode());
        ret.add(new LangEvalGroovyNode());
        ret.add(new LangEvalJavaNode());
        ret.add(new LangEvalJavascriptNode());
        ret.add(new LangEvalNode());
        ret.add(new LangEvalTinyScriptNode());
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


        return ret;
    }

    @Override
    public JdbcProcedureContext getContext(){
        return context;
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
    public boolean isDebug() {
        return this.debug.get();
    }

    @Override
    public void debugLog(Supplier<Object> supplier) {
        if (debug.get()) {
            System.out.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "DEBUG", Thread.currentThread().getName(), String.valueOf(supplier.get())));
        }
    }

    @Override
    public void infoLog(Supplier<Object> supplier, Throwable e) {
        System.out.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "ERROR", Thread.currentThread().getName(), String.valueOf(supplier.get())));
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void warnLog(Supplier<Object> supplier, Throwable e) {
        System.err.println(String.format("%s [%5s] [%10s] : %s", logTimeFormatter.format(LocalDateTime.now()), "ERROR", Thread.currentThread().getName(), String.valueOf(supplier.get())));
        if (e != null) {
            e.printStackTrace();
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
            String location=null;
            try{
                Object loc = visit(ParamsConsts.TRACE_LOCATION, context);
                Object line = visit(ParamsConsts.TRACE_LINE, context);
                location=loc+":"+line;
            }catch(Exception e){

            }
            System.out.println("debugger [" + tag + "] [" + conditionExpression + "] wait for input line to continue, trace near : "+location);
            System.out.println("continue.");
        }
    }


    @Override
    public <T> T invoke(String procedureId, Map<String, Object> params) {
        Map<String, Object> ret = call(procedureId, params);
        return visitAs(ParamsConsts.RETURN, ret);
    }

    @Override
    public <T> T invoke(String procedureId, List<Object> args) {
        Map<String, Object> params = castArgListAsProcedureMap(procedureId, args);
        return invoke(procedureId,params);
    }

    @Override
    public Map<String, Object> call(String procedureId, List<Object> args) {
        Map<String, Object> params = castArgListAsProcedureMap(procedureId, args);
        return call(procedureId,params);
    }

    public Map<String,Object> castArgListAsProcedureMap(String procedureId,List<Object> args){
        Map<String,Object> ret=new LinkedHashMap<>();
        Map<String, ProcedureMeta> nodeMap = getMetaMap();
        ProcedureMeta node = nodeMap.get(procedureId);
        if(node!=null){
            List<String> arguments = node.getArguments();
            int i=0;
            for (String name : arguments) {
                if(AttrConsts.ID.equals(name)){
                    continue;
                }
                if(ParamsConsts.RETURN.equals(name)){
                    continue;
                }
                Object value=i<args.size()?args.get(i):null;
                ret.put(name,value);
                i++;
            }
            return ret;
        }
        throw new NotFoundSignalException("not found node: " + procedureId);
    }

    @Override
    public Map<String, Object> prepareParams(Map<String, Object> params) {
        Map<String, Object> execParams = createParams();
        for (Map.Entry<String, Object> entry : execParams.entrySet()) {
            params.putIfAbsent(entry.getKey(), entry.getValue());
        }
        return params;
    }

    public String getNodeLocation(XmlNode node){
        if(node==null){
            return "null node";
        }
        return "tag:"+node.getTagName()+", "+node.getLocationFile()+":"+node.getLocationLineNumber();
    }

    @Override
    public Map<String, Object> exec(XmlNode node, Map<String,Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        try {
            for (ExecutorNode item : getNodes()) {
                if (item.support(node)) {
                    prepareParams(params);
                    debugLog(() -> "exec " + node.getTagName() + " by " + item.getClass().getSimpleName()+", at "+getNodeLocation(node));
                    Map<String, Connection> bakConnection = (Map<String, Connection>) params.computeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
                    if (beforeNewConnection) {
                        visitSet(params,ParamsConsts.CONNECTIONS, new HashMap<>());
                    }
                    try {
                        item.exec(node, params,this);
                    } finally {
                        if (beforeNewConnection) {
                            closeConnections(params);
                            visitSet(params,ParamsConsts.CONNECTIONS, bakConnection);
                        }
                        if (afterCloseConnection) {
                            closeConnections(params);
                        }
                    }
                    return params;
                }
            }
            debugLog(() -> "waring! tag " + node.getTagName() + " not found any executor!"+" at "+getNodeLocation(node));
        } catch (ControlSignalException e) {
            if (Arrays.asList(ProcedureNode.TAG_NAME,
                    ProcedureCallNode.TAG_NAME,
                    FunctionCallNode.TAG_NAME
            ).contains(node.getTagName())) {
                debugLog(() -> "control signal:"+e.getClass().getSimpleName());
            }else{
                throw e;
            }
        }
        return params;
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
                    warnLog(()->e.getMessage(),e);
                }
            }
        }
        visitSet(params,ParamsConsts.CONNECTIONS, new HashMap<>());
    }

    public ProcedureNode getProcedureNode(){
        return procedureNodeHolder.updateAndGet((node)->{
            if(node!=null){
                return node;
            }
            for (ExecutorNode item : getNodes()) {
                if (item instanceof ProcedureNode) {
                    return (ProcedureNode) item;
                }
            }
            return ProcedureNode.INSTANCE;
        });
    }


    @Override
    public Map<String, Object> execAsProcedure(XmlNode node, Map<String,Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        try {
            prepareParams(params);
            ProcedureNode execNode = getProcedureNode();

            debugLog(() -> "exec as procedure " + node.getTagName()+", at "+getNodeLocation(node));

            Map<String, Connection> bakConnection = (Map<String, Connection>) params.computeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
            if (beforeNewConnection) {
                visitSet(params,ParamsConsts.CONNECTIONS, new HashMap<>());
            }
            try {
                execNode.exec(node, params, this);
            } finally {
                if (beforeNewConnection) {
                    closeConnections(params);
                    visitSet(params,ParamsConsts.CONNECTIONS, bakConnection);
                }
                if (afterCloseConnection) {
                    closeConnections(params);
                }
            }
        } catch (ControlSignalException e) {
            if (Arrays.asList(ProcedureNode.TAG_NAME,
                    ProcedureCallNode.TAG_NAME,
                    FunctionCallNode.TAG_NAME
            ).contains(node.getTagName())) {
                debugLog(() -> "control signal:"+e.getClass().getSimpleName());
            }else{
                throw e;
            }
        }
        return params;
    }

    @Override
    public Object attrValue(String attr, String action, XmlNode node, Map<String,Object> params) {
        String attrScript = node.getTagAttrMap().get(attr);
        if (attrScript == null) {
            return null;
        }
        Object value = attrScript;

        String radixText = node.getTagAttrMap().get(AttrConsts.RADIX);
        if (radixText != null && !radixText.isEmpty()) {
            try {
                Object radixObj = attrValue(AttrConsts.RADIX, FeatureConsts.VISIT, node, params);
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
                value = resolveFeature(value, feature, node, params);
            }
        } else {
            value = resolveFeature(attrScript, action, node, params);
        }

        return value;
    }

    @Override
    public Object resultValue(Object value, List<String> features, XmlNode node, Map<String,Object> params) {
        if (features == null || features.isEmpty()) {
            return value;
        }
        for (String feature : features) {
            if (feature == null || feature.isEmpty()) {
                continue;
            }
            value = resolveFeature(value, feature, node, params);
        }
        return value;
    }

    @Override
    public void visitSet(Map<String, Object> params, String result, Object value) {
        Visitor visitor = Visitor.visit(result, params);
        visitor.set(value);
    }

    @Override
    public void visitDelete(Map<String, Object> params, String result) {
        Visitor visitor = Visitor.visit(result, params);
        visitor.delete();
    }

    @Override
    public Map<String, Object> createParams() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put(ParamsConsts.CONTEXT, namingContext);
        ret.put(ParamsConsts.ENVIRONMENT, environment);
        ret.put(ParamsConsts.BEANS, new HashMap<>());

        ret.put(ParamsConsts.DATASOURCES, new HashMap<>());
        ret.put(ParamsConsts.DATASOURCES_MAPPING,new HashMap<>());

        ret.put(ParamsConsts.CONNECTIONS, new HashMap<>());

        ret.put(ParamsConsts.GLOBAL, new HashMap<>());

        ret.put(ParamsConsts.TRACE,new HashMap<>());

        ret.put(ParamsConsts.EXECUTOR, this);

        ret.put(ParamsConsts.LRU,new LruMap<>(4096));
        return ret;
    }

    @Override
    public Map<String, Object> newParams(Map<String,Object> params) {
        Map<String, Object> ret = new LinkedHashMap<>();
        if (params == null) {
            return createParams();
        }
        ret.put(ParamsConsts.CONTEXT, params.get(ParamsConsts.CONTEXT));
        ret.put(ParamsConsts.ENVIRONMENT, params.get(ParamsConsts.ENVIRONMENT));
        ret.put(ParamsConsts.BEANS, params.get(ParamsConsts.BEANS));

        ret.put(ParamsConsts.DATASOURCES, params.get(ParamsConsts.DATASOURCES));
        ret.put(ParamsConsts.DATASOURCES_MAPPING,params.get(ParamsConsts.DATASOURCES_MAPPING));

        ret.put(ParamsConsts.GLOBAL, params.get(ParamsConsts.GLOBAL));

        ret.put(ParamsConsts.TRACE, params.get(ParamsConsts.TRACE));

        ret.put(ParamsConsts.EXECUTOR, this);

        ret.put(ParamsConsts.LRU,params.get(ParamsConsts.LRU));
        return ret;
    }

    public Object resolveFeature(Object value, String feature, XmlNode node, Map<String,Object> context) {
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
            return render(text, context);
        } else if (FeatureConsts.VISIT.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return visit(text, context);
        } else if (FeatureConsts.EVAL.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return eval(text, context);
        } else if (FeatureConsts.TEST.equals(feature)) {
            String text = value == null ? "" : String.valueOf(value);
            return test(text, context);
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
                boolean ok = supportDatabaseDialect(datasource, databases, context);
                return ok;
            } catch (Exception e) {
                warnLog(()->e.getMessage(),e);
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
                IMethod method = ContextHolder.CONVERT_METHOD_MAP.get(feature);
                if (method != null) {
                    return method.invoke(null, value);
                }
            } catch (Throwable e) {
                warnLog(() -> e.getMessage(), e);
            }
            try {
                Function function = ContextHolder.CONVERT_FUNC_MAP.get(feature);
                if (function != null) {
                    return function.apply(value);
                }
            } catch (Exception e) {
                warnLog(() -> e.getMessage(), e);
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
    public boolean test(String script, Object params) {
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

    public boolean innerTest(String test, Object params) {
        return MybatisMapperInflater.INSTANCE.testExpression(test, params);
    }

    @Override
    public Object eval(String script, Object params) {
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

    public Object innerEval(String script, Object params) {
        return MybatisMapperInflater.INSTANCE.evalExpression(script, params);
    }

    @Override
    public Object visit(String script, Object params) {
        debugLog(() -> "visit:" + script);
        if(params instanceof Map){
            Map<?, ?> map = (Map<?, ?>) params;
            if(map.containsKey(script)){
                return map.get(script);
            }
        }
        Visitor visitor = Visitor.visit(script, params);
        if (visitor != null) {
            return visitor.get();
        }
        return innerVisit(script, params);
    }

    public Object innerVisit(String script, Object params) {
        return null;
    }

    @Override
    public String render(String script, Object params) {
        debugLog(() -> "render:" + script);
        return innerRender(script, params);
    }

    public String innerRender(String script, Object params) {
        return script;
    }


    @Override
    public Connection getConnection(String datasource, Map<String, Object> params) {
        if (datasource == null || datasource.isEmpty()) {
            datasource = ParamsConsts.DEFAULT_DATASOURCE;
        }
        Map<String,String> datasourcesMapping = (Map<String,String>)params.get(ParamsConsts.DATASOURCES_MAPPING);
        if(datasourcesMapping!=null){
            String mapping = datasourcesMapping.get(datasource);
            if(mapping!=null &&!mapping.isEmpty()){
                datasource=mapping;
            }
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
    public List<?> sqlQueryList(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            debugLog(() -> "sqlQueryList:datasource=" + datasource + " \n\tbql:\n" + bql);
            List<?> list = JdbcResolver.list(conn, bql, resultType, -1, TypeOf.typeOf(resultType, Map.class) ? (String::toUpperCase) : null);
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    @Override
    public Object sqlQueryObject(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            debugLog(() -> "sqlQueryObject:datasource=" + datasource + " \n\tbql:\n" + bql);
            Object obj = JdbcResolver.get(conn, bql, resultType);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public Object sqlQueryRow(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            debugLog(() -> "sqlQueryRow:datasource=" + datasource + " \n\tbql:\n" + bql);
            Object row = JdbcResolver.find(conn, bql, resultType, TypeOf.typeOf(resultType, Map.class) ? (String::toUpperCase) : null);
            return row;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public int sqlUpdate(String datasource, BindSql bql, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            debugLog(() -> "sqlUpdate:datasource=" + datasource + " \n\tbql:\n" + bql);
            int num = JdbcResolver.update(conn, bql);
            return num;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public BindSql sqlWrapPage(String datasource, BindSql bql, ApiOffsetSize page, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            if (page != null && (page.getOffset() != null || page.getSize() != null)) {
                IPageWrapper wrapper = PageWrappers.wrapper(conn);
                bql = wrapper.apply(bql, page);
            }
            BindSql pageSql = bql;
            debugLog(() -> "sqlWrapPage:datasource=" + datasource + ", databaseType=" + databaseType + " \n\tbql:\n" + pageSql);
            return pageSql;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public BindSql sqlWrapCount(String datasource, BindSql bql, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            ICountWrapper wrapper = CountWrappers.wrapper();
            bql = wrapper.apply(bql);
            BindSql pageSql = bql;
            debugLog(() -> "sqlWrapCount:datasource=" + datasource + ", databaseType=" + databaseType + " \n\tbql:\n" + pageSql);
            return pageSql;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public BindSql sqlScript(String datasource, List<Map.Entry<String, String>> dialectScriptList, Map<String, Object> params, ApiOffsetSize page) {
        try {
            Connection conn = getConnection(datasource, params);
            Map.Entry<String, String> entry = getDialectSqlScript(dialectScriptList, conn);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            String script = entry.getValue();
            BindSql bql = resolveSqlScript(script, params);
            if (page != null && (page.getOffset() != null || page.getSize() != null)) {
                IPageWrapper wrapper = PageWrappers.wrapper(conn);
                bql = wrapper.apply(bql, page);
            }
            BindSql pageSql = bql;
            debugLog(() -> "sqlScript:datasource=" + datasource + ", dialect=" + entry.getKey() + ", script=" + script + " \n\tbql:\n" + pageSql);
            return pageSql;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<?> sqlQueryPage(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType, ApiOffsetSize page) {
        try {
            Connection conn = getConnection(datasource, params);
            DatabaseType databaseType = DatabaseType.typeOfConnection(conn);
            visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
            if (page != null && (page.getOffset() != null || page.getSize() != null)) {
                IPageWrapper wrapper = PageWrappers.wrapper(conn);
                bql = wrapper.apply(bql, page);
            }
            BindSql pageBql = bql;
            debugLog(() -> "sqlQueryPage:datasource=" + datasource + " \n\tbql:\n" + pageBql);
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
