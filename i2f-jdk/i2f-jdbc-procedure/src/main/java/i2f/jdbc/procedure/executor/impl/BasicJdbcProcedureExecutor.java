package i2f.jdbc.procedure.executor.impl;

import i2f.bindsql.BindSql;
import i2f.bindsql.count.CountWrappers;
import i2f.bindsql.count.ICountWrapper;
import i2f.bindsql.page.IPageWrapper;
import i2f.bindsql.page.PageWrappers;
import i2f.clock.SystemClock;
import i2f.context.impl.ListableNamingContext;
import i2f.context.std.INamingContext;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.type.DatabaseDialectMapping;
import i2f.database.type.DatabaseType;
import i2f.environment.impl.ListableDelegateEnvironment;
import i2f.environment.std.IEnvironment;
import i2f.invokable.method.IMethod;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.procedure.consts.*;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.JdbcProcedureContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContext;
import i2f.jdbc.procedure.event.XProc4jEvent;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.ContextXProc4jEventHandler;
import i2f.jdbc.procedure.executor.FeatureFunction;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.executor.event.PreparedParamsEvent;
import i2f.jdbc.procedure.executor.event.SlowSqlEvent;
import i2f.jdbc.procedure.executor.event.SqlExecUseTimeEvent;
import i2f.jdbc.procedure.log.JdbcProcedureLogger;
import i2f.jdbc.procedure.log.impl.DefaultJdbcProcedureLogger;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.node.event.XmlExecUseTimeEvent;
import i2f.jdbc.procedure.node.impl.*;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.ControlSignalException;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.jdbc.procedure.signal.impl.ThrowSignalException;
import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;
import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;
import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;
import i2f.lru.LruList;
import i2f.lru.LruMap;
import i2f.page.ApiOffsetSize;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;
import i2f.text.StringUtils;
import i2f.typeof.TypeOf;
import i2f.uid.SnowflakeLongUid;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:40
 */
@Data
public class BasicJdbcProcedureExecutor implements JdbcProcedureExecutor, EvalScriptProvider {
    public static transient final LruMap<String, Object> staticLru = new LruMap<>(4096);
    protected static final AtomicBoolean hasApplyNodes = new AtomicBoolean(false);
    protected transient final AtomicReference<ProcedureNode> procedureNodeHolder = new AtomicReference<>();
    protected transient final LruMap<String, Object> executorLru = new LruMap<>(4096);
    protected final LruList<ExecutorNode> nodes = new LruList<>();
    protected final ConcurrentHashMap<String, FeatureFunction> featuresMap = new ConcurrentHashMap<>();
    protected final LruList<EvalScriptProvider> evalScriptProviders = new LruList<>();
    protected final AtomicBoolean debug = new AtomicBoolean(true);
    protected volatile JdbcProcedureLogger logger = new DefaultJdbcProcedureLogger(debug);
    public volatile Function<String, String> mapTypeColumnNameMapper = StringUtils::toUpper;
    public volatile Function<String, String> otherTypeColumnNameMapper = null;
    protected volatile JdbcProcedureContext context = new DefaultJdbcProcedureContext();
    protected volatile IEnvironment environment = new ListableDelegateEnvironment();
    protected volatile INamingContext namingContext = new ListableNamingContext();
    protected volatile XProc4jEventHandler eventHandler = new ContextXProc4jEventHandler(namingContext);
    protected volatile long slowSqlMinMillsSeconds = TimeUnit.SECONDS.toMillis(5);
    protected volatile long slowProcedureMillsSeconds = TimeUnit.SECONDS.toMillis(30);
    protected volatile long slowNodeMillsSeconds = TimeUnit.SECONDS.toMillis(15);
    protected final AtomicBoolean defaultTransactionalConnection=new AtomicBoolean(true);

    public final DatabaseDialectMapping DIALECT_MAPPING = new DatabaseDialectMapping() {
        @Override
        public void init() {
            redirect(DatabaseType.DM, DatabaseType.ORACLE);
        }
    };

    {
        List<ExecutorNode> list = defaultExecutorNodes();
        for (ExecutorNode node : list) {
            registryExecutorNode(node);
        }
        registryEvalScriptProvider(this);
        initFeatureMap();
    }

    public BasicJdbcProcedureExecutor() {

    }

    public BasicJdbcProcedureExecutor(JdbcProcedureContext context) {
        this.context = context;
    }

    public BasicJdbcProcedureExecutor(JdbcProcedureContext context, IEnvironment environment, INamingContext namingContext) {
        this.context = context;
        this.environment = environment;
        this.namingContext = namingContext;
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
        ret.add(new EventPublishNode());
        ret.add(new EventSendNode());
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
        ret.add(new LangFileDeleteNode());
        ret.add(new LangFileExistsNode());
        ret.add(new LangFileListNode());
        ret.add(new LangFileMkdirsNode());
        ret.add(new LangFileReadTextNode());
        ret.add(new LangFileTreeNode());
        ret.add(new LangFileWriteTextNode());
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
        ret.add(new LangShellNode());
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
        ret.add(new SqlQueryColumnsNode());
        ret.add(new SqlQueryListNode());
        ret.add(new SqlQueryObjectNode());
        ret.add(new SqlQueryRowNode());
        ret.add(new SqlScopeNode());
        ret.add(new SqlScriptNode());
        ret.add(new SqlTransactionalNode());
        ret.add(new SqlTransBeginNode());
        ret.add(new SqlTransCommitNode());
        ret.add(new SqlTransNoneNode());
        ret.add(new SqlTransRollbackNode());
        ret.add(new SqlUpdateNode());
        ret.add(new TextNode());


        return ret;
    }

    public void registryExecutorNode(ExecutorNode node) {
        if (node == null) {
            return;
        }
        if (isDebug()) {
            logger().logDebug("registry executor node:" + node.getClass());
        }
        this.nodes.add(node);
        if (node instanceof EvalScriptProvider) {
            EvalScriptProvider provider = (EvalScriptProvider) node;
            registryEvalScriptProvider(provider);
        }
    }

    @Override
    public void sendEvent(XProc4jEvent event) {
        XProc4jEventHandler handler = getEventHandler();
        if (handler != null) {
            handler.send(event);
        }
    }

    @Override
    public void publishEvent(XProc4jEvent event) {
        if (event instanceof XmlExecUseTimeEvent) {
            XmlExecUseTimeEvent evt = (XmlExecUseTimeEvent) event;
            XmlNode node = evt.getNode();
            long useTs = evt.getUseTs();
            String location = evt.getPreferLocation();

            if (TagConsts.PROCEDURE.equals(node.getTagName())) {
                if (useTs > slowProcedureMillsSeconds) {
                    logger().logWarn(() -> "slow procedure, use " + useTs + "(ms) : " + node.getTagAttrMap().get(AttrConsts.ID));
                }
            } else {
                if (!Arrays.asList(TagConsts.PROCEDURE_CALL,
                        TagConsts.FUNCTION_CALL).contains(node.getTagName())) {
                    if (useTs > slowNodeMillsSeconds) {
                        logger().logWarn(() -> "slow node, use " + useTs + "(ms) : " + location);
                    }
                }
            }
        }

        XProc4jEventHandler handler = getEventHandler();
        if (handler != null) {
            handler.publish(event);
        }
    }

    @Override
    public void registryEvalScriptProvider(EvalScriptProvider provider) {
        if (provider == null) {
            return;
        }
        if (isDebug()) {
            logger().logDebug("registry eval script provider:" + provider.getClass());
        }
        this.evalScriptProviders.add(provider);
    }

    @Override
    public LruList<EvalScriptProvider> getEvalScriptProviders() {
        return this.evalScriptProviders;
    }

    @Override
    public boolean support(String lang) {
        return LangConsts.XPROC4J.equals(lang);
    }

    @Override
    public Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor) {
        XmlNode node = null;
        try {
            node = JdbcProcedureParser.parse(script);
        } catch (Exception e) {

        }
        if (node == null) {
            try {
                script = "<procedure>\n" +
                        script + "\n" +
                        "</procedure>\n";
                node = JdbcProcedureParser.parse(script);
            } catch (Exception e) {

            }
        }
        if (node == null) {
            throw new IllegalArgumentException(LangConsts.XPROC4J + " lang accept xml format script, script format maybe wrong!");
        }
        return executor.exec(node, params, false, false);
    }

    @Override
    public JdbcProcedureContext getContext() {
        return context;
    }

    public void applyNodeExecutorComponents() {
        List<Object> beansList = getNamingContext().getAllBeans();
        for (Object bean : beansList) {
            if (bean instanceof ExecutorNode) {
                this.nodes.add(0, (ExecutorNode) bean);
            }
        }
    }

    @Override
    public LruList<ExecutorNode> getNodes() {
        if (!hasApplyNodes.getAndSet(true)) {
            applyNodeExecutorComponents();
        }
        return nodes;
    }


    @Override
    public <T> T invoke(String procedureId, Map<String, Object> params) {
        if (isDebug()) {
            logger().logDebug("invoke [" + procedureId + "] use Map ... ");
        }
        Map<String, Object> ret = call(procedureId, params);
        return visitAs(ParamsConsts.RETURN, ret);
    }

    @Override
    public <T> T invoke(String procedureId, List<Object> args) {
        if (isDebug()) {
            logger().logDebug("invoke [" + procedureId + "] use List ... ");
        }
        Map<String, Object> params = castArgListAsProcedureMap(procedureId, args);
        return invoke(procedureId, params);
    }


    @Override
    public Map<String, Object> call(String procedureId, List<Object> args) {
        if (isDebug()) {
            logger().logDebug("call [" + procedureId + "] use Map ... ");
        }
        Map<String, Object> params = castArgListAsProcedureMap(procedureId, args);
        return call(procedureId, params);
    }

    public Map<String, Object> castArgListAsProcedureMap(String procedureId, List<Object> args) {
        Map<String, Object> ret = new LinkedHashMap<>();
        ProcedureMeta node = getMeta(procedureId);
        if (node != null) {
            List<String> arguments = node.getArguments();
            Iterator<Object> argIter = args.iterator();
            int i = 0;
            for (String name : arguments) {
                if (AttrConsts.ID.equals(name)) {
                    continue;
                }
                if (ParamsConsts.RETURN.equals(name)) {
                    continue;
                }
                Object value = argIter.hasNext() ? argIter.next() : null;
                ret.put(name, value);
                i++;
            }
            return ret;
        }
        throw new NotFoundSignalException("not found node: " + procedureId);
    }

    public void execXmlNodeDelegate(ExecXmlNodeDelegateTask task, XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        try {
            task.exec(node, params, beforeNewConnection, afterCloseConnection);
        } catch (ControlSignalException e) {
            if (Arrays.asList(ProcedureNode.TAG_NAME,
                    ProcedureCallNode.TAG_NAME,
                    FunctionCallNode.TAG_NAME
            ).contains(node.getTagName())) {
//                if(isDebug()) {
//                    logger().logDebug(() -> "control signal:" + e.getClass().getSimpleName());
//                }
            } else {
                throw e;
            }
        } catch (Throwable e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public Map<String, Object> exec(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
//        if(isDebug()) {
//            logger().logDebug( "exec XmlNode [" + XmlNode.getNodeLocation(node) + "] use Map ... ");
//        }
        execXmlNodeDelegate((vNode, vParams, vBeforeNewConnection, vAfterCloseConnection) -> {
            LruList<ExecutorNode> nodeList = getNodes();
            ExecutorNode execNode = nodeList.touchFirst(e -> e.support(vNode));
            if (execNode != null) {
                execXmlNodeByExecutorNode(execNode, vNode, vParams, vBeforeNewConnection, vAfterCloseConnection);
                return;
            }
            if (isDebug()) {
                logger().logDebug("waring! tag " + vNode.getTagName() + " not found any executor!" + " at " + XmlNode.getNodeLocation(vNode));
            }

        }, node, params, beforeNewConnection, afterCloseConnection);
        return params;
    }

    public void execJavaCallerDelegate(ExecJavaCallerDelegateTask task, JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        try {
            task.exec(caller, params, beforeNewConnection, afterCloseConnection);
        } catch (ControlSignalException e) {

        } catch (Throwable e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
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
                    if (isDebug()) {
                        logger().logDebug("close connection : " + entry.getKey());
                    }
                    if(!conn.isClosed()) {
                        if (!conn.getAutoCommit()) {
                            conn.commit();
                        }
                    }
                } catch (SQLException e) {
                    logger().logWarn(e.getMessage(), e);
                }finally {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        logger().logWarn(e.getMessage(), e);
                    }
                }
            }
        }
        visitSet(params, ParamsConsts.CONNECTIONS, new HashMap<>());
    }

    public void execDelegate(ExecDelegateTask task,
                             Map<String, Object> params,
                             boolean beforeNewConnection,
                             boolean afterCloseConnection,
                             Object... args) throws Throwable {
        prepareParams(params);

        Map<String, Connection> bakConnection = (Map<String, Connection>) params.computeIfAbsent(ParamsConsts.CONNECTIONS, (key) -> new HashMap<>());
        if (beforeNewConnection) {
            visitSet(params, ParamsConsts.CONNECTIONS, new HashMap<>());
        }
        try {
            task.exec(params, args);
        } finally {
            if (beforeNewConnection) {
                closeConnections(params);
                visitSet(params, ParamsConsts.CONNECTIONS, bakConnection);
            }
            if (afterCloseConnection) {
                closeConnections(params);
            }
        }
    }

    public void execJavaCaller(JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) throws Throwable {
        execDelegate((vParams, vArgs) -> {
            JdbcProcedureJavaCaller vCaller = (JdbcProcedureJavaCaller) vArgs[0];
//            if(isDebug()) {
//                logger().logDebug("exec " + vCaller.getClass());
//            }

            Object ret = vCaller.exec(this, vParams);
            boolean hasValue = true;
            if (ret instanceof Reference) {
                Reference<?> ref = (Reference<?>) ret;
                if (ref.isValue()) {
                    ret = ref.get();
                } else {
                    hasValue = false;
                }
            }
            if (hasValue) {
                visitSet(vParams, ParamsConsts.RETURN, ret);
            }
        }, params, beforeNewConnection, afterCloseConnection, caller);
    }

    public void execXmlNodeByExecutorNode(ExecutorNode execNode, XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) throws Throwable {
        execDelegate((vParams, vArgs) -> {
            ExecutorNode vExecNode = (ExecutorNode) vArgs[0];
            XmlNode vNode = (XmlNode) vArgs[1];
//            if(isDebug()) {
//                logger().logDebug("exec " + vNode.getTagName() + " by " + vExecNode.getClass().getSimpleName() + ", at " + getNodeLocation(vNode));
//            }

            vExecNode.exec(vNode, vParams, this);
        }, params, beforeNewConnection, afterCloseConnection, execNode, node);
    }

    @Override
    public Map<String, Object> exec(JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
        if (isDebug()) {
            logger().logDebug("exec JavaCaller [" + caller.getClass() + "] use Map ... ");
        }
        execJavaCallerDelegate((vCaller, vParams, vBeforeNewConnection, vAfterCloseConnection) -> {
            execJavaCaller(vCaller, vParams, vBeforeNewConnection, vAfterCloseConnection);
        }, caller, params, beforeNewConnection, afterCloseConnection);
        return params;
    }

    public ProcedureNode getProcedureNode() {
        return procedureNodeHolder.updateAndGet((node) -> {
            if (node != null) {
                return node;
            }
            LruList<ExecutorNode> nodeList = getNodes();
            ExecutorNode item = nodeList.touchFirst(e -> e instanceof ProcedureNode);
            if (item != null) {
                return (ProcedureNode) item;
            }
            return ProcedureNode.INSTANCE;
        });
    }

    @Override
    public Map<String, Object> execAsProcedure(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) {
//        if(isDebug()) {
//            logger().logDebug("exec XmlNode as procedure [" + XmlNode.getNodeLocation(node) + "] use Map ... ");
//        }
        execXmlNodeDelegate((vNode, vParams, vBeforeNewConnection, vAfterCloseConnection) -> {
            ProcedureNode execNode = getProcedureNode();
            execXmlNodeByExecutorNode(execNode, vNode, vParams, vBeforeNewConnection, vAfterCloseConnection);
        }, node, params, beforeNewConnection, afterCloseConnection);
        return params;
    }

    public void reportPreparedParamsEvent(Map<String, Object> params) {
        PreparedParamsEvent event = new PreparedParamsEvent();
        event.setExecutor(this);
        event.setContext(params);
        sendEvent(event);
    }


    public void detectPrimaryDatasource(Map<String, DataSource> ret) {
        if (ret.isEmpty()) {
            return;
        }
        DataSource primary = ret.get(ParamsConsts.DEFAULT_DATASOURCE);
        if (primary == null) {
            List<String> defaultNames = Arrays.asList("primary", "master", "main", "default", "leader");
            for (Map.Entry<String, DataSource> entry : ret.entrySet()) {
                String name = entry.getKey();
                if (defaultNames.contains(name)) {
                    ret.put(ParamsConsts.DEFAULT_DATASOURCE, entry.getValue());
                    return;
                }
                name = name.toLowerCase();
                if (defaultNames.contains(name)) {
                    ret.put(ParamsConsts.DEFAULT_DATASOURCE, entry.getValue());
                    return;
                }
            }
            if (ret.size() == 1) {
                ret.put(ParamsConsts.DEFAULT_DATASOURCE, ret.get(ret.keySet().iterator().next()));
                return;
            }
        }
    }


    @Override
    public Map<String, Object> createParams() {
        Map<String, Object> ret = new LinkedHashMap<>();
        ret.put(ParamsConsts.CONTEXT, getNamingContext());
        ret.put(ParamsConsts.ENVIRONMENT, getEnvironment());

        Map<String, Object> global = new HashMap<>();
        global.put(ParamsConsts.METAS, new HashMap<>());
        ret.put(ParamsConsts.GLOBAL, global);

        Map<String, Object> trace = new HashMap<>();
        trace.put(ParamsConsts.STACK, new Stack<>());
        trace.put(ParamsConsts.CALLS, new LinkedList<>());
        trace.put(ParamsConsts.ERRORS, new LinkedList<>());
        ret.put(ParamsConsts.TRACE, trace);

        ret.put(ParamsConsts.LRU, new LruMap<>(4096));
        ret.put(ParamsConsts.EXECUTOR_LRU, executorLru);
        ret.put(ParamsConsts.STATIC_LRU, staticLru);

        for (String key : ParamsConsts.KEEP_NAMES) {
            Object val = ret.get(key);
            if (val == null) {
                ret.put(key, new HashMap<>());
            }
        }

        ret.put(ParamsConsts.EXECUTOR, this);

        Map<String, Object> beanMap = getNamingContext().getAllBeansMap();
        Map<String, Object> retBeanMap = (Map<String, Object>) ret.computeIfAbsent(ParamsConsts.BEANS, (key) -> new HashMap<>());
        retBeanMap.putAll(beanMap);

        Map<String, DataSource> datasourceMap = getDatasourceMap();
        Map<String, Object> retDatasourceMap = (Map<String, Object>) ret.computeIfAbsent(ParamsConsts.DATASOURCES, (key) -> new HashMap<>());
        retDatasourceMap.putAll(datasourceMap);

        reportPreparedParamsEvent(ret);
        return ret;
    }

    public Map<String, DataSource> getDatasourceMap() {
        try {
            Map<String, DataSource> ret = new HashMap<>();
            Map<String, DataSource> dataSources = getNamingContext().getBeansMap(DataSource.class);
            for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
                String name = entry.getKey();
                if (name.toLowerCase().endsWith("datasource")) {
                    name = name.substring(0, name.length() - "datasource".length());
                }
                ret.put(name, entry.getValue());
            }
            detectPrimaryDatasource(ret);
            return ret;
        } catch (Exception e) {

        }
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> prepareParams(Map<String, Object> params) {
        Map<String, Object> execParams = null;
        for (String key : ParamsConsts.KEEP_NAMES) {
            Object val = params.get(key);
            if (val == null) {
                if (execParams == null) {
                    execParams = createParams();
                }
                val = execParams.get(key);
                params.put(key, val);
            }
            if (ParamsConsts.TRACE.equals(key)) {
                Map<String, Object> trace = (Map<String, Object>) val;
                if (!trace.containsKey(ParamsConsts.STACK)) {
                    trace.put(ParamsConsts.STACK, new Stack<>());
                }
                if (!trace.containsKey(ParamsConsts.CALLS)) {
                    trace.put(ParamsConsts.CALLS, new LinkedList<>());
                }
                if (!trace.containsKey(ParamsConsts.ERRORS)) {
                    trace.put(ParamsConsts.ERRORS, new LinkedList<>());
                }
            }
        }

        params.put(ParamsConsts.EXECUTOR, this);

        reportPreparedParamsEvent(params);
        return params;
    }

    @Override
    public Map<String, Object> newParams(Map<String, Object> params) {
        Map<String, Object> ret = new LinkedHashMap<>();
        if (params == null) {
            return createParams();
        }

        for (String key : ParamsConsts.KEEP_NAMES) {
            ret.put(key, params.get(key));
        }

        ret.put(ParamsConsts.EXECUTOR, this);

        prepareParams(ret);

        return ret;
    }

    @Override
    public Object attrValue(String attr, String action, XmlNode node, Map<String, Object> params) {
//        if(isDebug()) {
//            logger().logDebug( "attr value [" + attr + "] at [" + XmlNode.getNodeLocation(node) + "] ... ");
//        }
        String attrScript = node.getTagAttrMap().get(attr);
        if (attrScript == null) {
            if (isDebug()) {
                logger().logDebug("attr value [" + attr + "] at [" + XmlNode.getNodeLocation(node) + "] is: null");
            }
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

        Object logObj = value;
//        if(isDebug()) {
//            logger().logDebug("attr value [" + attr + "] at [" + XmlNode.getNodeLocation(node) + "] is: " + stringifyWithType(logObj));
//        }
        return value;
    }

    @Override
    public Object resultValue(Object value, List<String> features, XmlNode node, Map<String, Object> params) {
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

    protected void initFeatureMap() {
        featuresMap.put(FeatureConsts.INT, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Integer.class);
        });
        featuresMap.put(FeatureConsts.DOUBLE, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Double.class);
        });
        featuresMap.put(FeatureConsts.FLOAT, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Float.class);
        });
        featuresMap.put(FeatureConsts.STRING, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, String.class);
        });
        featuresMap.put(FeatureConsts.LONG, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Long.class);
        });
        featuresMap.put(FeatureConsts.SHORT, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Short.class);
        });
        featuresMap.put(FeatureConsts.CHAR, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Character.class);
        });
        featuresMap.put(FeatureConsts.BYTE, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Byte.class);
        });
        featuresMap.put(FeatureConsts.BOOLEAN, (value, node, context) -> {
            return ObjectConvertor.tryConvertAsType(value, Boolean.class);
        });
        featuresMap.put(FeatureConsts.RENDER, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return render(text, context);
        });
        featuresMap.put(FeatureConsts.VISIT, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return visit(text, context);
        });
        featuresMap.put(FeatureConsts.EVAL, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return eval(text, context);
        });
        featuresMap.put(FeatureConsts.TEST, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return test(text, context);
        });
        featuresMap.put(FeatureConsts.NULL, (value, node, context) -> {
            return null;
        });
        featuresMap.put(FeatureConsts.DATE, (value, node, context) -> {
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
        });
        featuresMap.put(FeatureConsts.TRIM, (value, node, context) -> {
            if (value == null) {
                return null;
            }
            return String.valueOf(value).trim();
        });
        featuresMap.put(FeatureConsts.ALIGN, (value, node, context) -> {
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
        });
        featuresMap.put(FeatureConsts.BODY_TEXT, (value, node, context) -> {
            return node.getTextBody();
        });
        featuresMap.put(FeatureConsts.BODY_XML, (value, node, context) -> {
            return node.getTagBody();
        });
        featuresMap.put(FeatureConsts.SPACING_LEFT, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return " " + text;
        });
        featuresMap.put(FeatureConsts.SPACING_RIGHT, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return text + " ";
        });
        featuresMap.put(FeatureConsts.SPACING, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return " " + text + " ";
        });
        featuresMap.put(FeatureConsts.EVAL_JAVA, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalJavaNode.evalJava(context, this, "", "", text);
        });
        featuresMap.put(FeatureConsts.EVAL_JS, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalJavascriptNode.evalJavascript(text, context, this);
        });
        featuresMap.put(FeatureConsts.EVAL_TINYSCRIPT, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalTinyScriptNode.evalTinyScript(text, context, this);
        });
        featuresMap.put(FeatureConsts.EVAL_TS, featuresMap.get(FeatureConsts.EVAL_TINYSCRIPT));

        featuresMap.put(FeatureConsts.EVAL_GROOVY, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return LangEvalGroovyNode.evalGroovyScript(text, context, this);
        });
        featuresMap.put(FeatureConsts.CLASS, (value, node, context) -> {
            String text = value == null ? "" : String.valueOf(value);
            return loadClass(text);
        });
        featuresMap.put(FeatureConsts.NOT, (value, node, context) -> {
            boolean ok = toBoolean(value);
            return !ok;
        });
        featuresMap.put(FeatureConsts.DIALECT, (value, node, context) -> {
            try {
                String databases = value == null ? null : String.valueOf(value);
                String datasource = (String) attrValue(AttrConsts.DATASOURCE, FeatureConsts.STRING, node, context);
                boolean ok = supportDatabaseDialect(datasource, databases, context);
                return ok;
            } catch (Exception e) {
                logger().logWarn(() -> e.getMessage(), e);
            }
            return false;
        });
        featuresMap.put(FeatureConsts.IS_NULL, (value, node, context) -> {
            return value == null;
        });
        featuresMap.put(FeatureConsts.IS_NOT_NULL, (value, node, context) -> {
            return value != null;
        });
        featuresMap.put(FeatureConsts.IS_EMPTY, (value, node, context) -> {
            return value == null || "".equals(value);
        });
        featuresMap.put(FeatureConsts.IS_NOT_EMPTY, (value, node, context) -> {
            return value != null && !"".equals(value);
        });
        featuresMap.put(FeatureConsts.DATE_NOW, (value, node, context) -> {
            return new Date();
        });
        featuresMap.put(FeatureConsts.UUID, (value, node, context) -> {
            return UUID.randomUUID().toString();
        });
        featuresMap.put(FeatureConsts.CURRENT_TIME_MILLIS, (value, node, context) -> {
            return System.currentTimeMillis();
        });
        featuresMap.put(FeatureConsts.SNOW_UID, (value, node, context) -> {
            return SnowflakeLongUid.getId();
        });
    }

    public Object resolveFeature(Object value, String feature, XmlNode node, Map<String, Object> context) {
        if (feature == null || feature.isEmpty()) {
            return value;
        }
        FeatureFunction featureFunction = featuresMap.get(feature);
        if (featureFunction != null) {
            return featureFunction.feature(value, node, context);
        } else {
            try {
                IMethod method = ContextHolder.CONVERT_METHOD_MAP.get(feature);
                if (method != null) {
                    return method.invoke(null, value);
                }
            } catch (Throwable e) {
                logger().logWarn(e.getMessage(), e);
            }
            try {
                Function function = ContextHolder.CONVERT_FUNC_MAP.get(feature);
                if (function != null) {
                    return function.apply(value);
                }
            } catch (Exception e) {
                logger().logWarn(e.getMessage(), e);
            }
        }
        return value;
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
        DatabaseType databaseType = getDatabaseType(conn);
        if (databaseType == null) {
            return ret;
        }
        String name = databaseType.db();
        String enumName = databaseType.name();
        ret.add(name.toLowerCase());
        ret.add(enumName.toLowerCase());
        return ret;
    }

    @Override
    public boolean toBoolean(Object obj) {
        return ObjectConvertor.toBoolean(obj);
    }

    @Override
    public void debug(boolean enable) {
        this.debug.set(enable);
        this.logger.debug(enable);
    }

    @Override
    public boolean isDebug() {
        Boolean ok = ContextHolder.DEBUG_MODE.get();
        if (ok != null && ok) {
            return true;
        }
        return this.debug.get();
    }

    @Override
    public void openDebugger(String tag, Object context, String conditionExpression) {
        if (isDebug()) {
            String location = null;
            try {
                location = String.valueOf(visit(ParamsConsts.TRACE_LOCATION, context));
            } catch (Exception e) {

            }
            System.out.println("debugger [" + tag + "] [" + conditionExpression + "] wait for input line to continue, trace near : " + location);
            System.out.println("continue.");
        }
    }

    @Override
    public JdbcProcedureLogger logger() {
        return this.logger;
    }

    protected LruMap<String, Class<?>> cacheLoadClass = new LruMap<>(512);

    @Override
    public Class<?> loadClass(String className) {
        if (className == null) {
            return null;
        }
        synchronized (this) {
            Class<?> ret = cacheLoadClass.get(className);
            if (ret != null) {
                return ret;
            }
            ret = loadClass0(className);
            if (ret != null) {
                cacheLoadClass.put(className, ret);
            }
            return ret;
        }
    }

    public Class<?> loadClass0(String className) {
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
                clazz = ReflectResolver.loadClass(prefix + className);
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

    @Override
    public boolean test(String script, Object params) {
//        if(isDebug()) {
//            logger().logDebug("test [" + script + "] near [" + ContextHolder.traceLocation() + "] ... ");
//        }
        String test = unescapeTestScript(script);
//        if(isDebug()) {
//            logger().logDebug("test:" + test);
//        }
        try {
            Boolean ok = ObjectConvertor.tryParseBoolean(test);
            if (ok != null) {
//                if(isDebug()) {
//                    logger().logDebug( "test [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ok));
//                }
                return ok;
            }
        } catch (Exception e) {

        }
        boolean ret = innerTest(test, params);
//        if(isDebug()) {
//            logger().logDebug("test [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ret));
//        }
        return ret;
    }

    public String stringifyWithType(Object obj) {
        Object val = obj;
        if (obj != null) {
            Class<?> clazz = obj.getClass();
            if (TypeOf.isBaseType(clazz)) {
                val = obj;
            } else if (TypeOf.typeOfAny(clazz,
                    CharSequence.class, Appendable.class,
                    AtomicInteger.class, AtomicLong.class, AtomicBoolean.class,
                    Date.class, LocalDateTime.class, LocalDate.class, LocalTime.class,
                    Calendar.class)) {
                val = obj;
            } else if (clazz.isEnum() || clazz.isAnnotation()) {
                val = obj;
            } else if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                boolean isContextParam = true;
                for (String keepName : ParamsConsts.KEEP_NAMES) {
                    if (!map.containsKey(keepName)) {
                        isContextParam = false;
                    }
                }
                if (isContextParam) {
                    val = "ContextMap{size=" + map.size() + "}";
                }

            } else if (TypeOf.typeOf(clazz, XmlNode.class)) {
                val = XmlNode.getNodeLocation((XmlNode) obj);
            } else {
                val = String.valueOf(obj);
            }

        }
        String s = String.valueOf(val);
        if (s.length() > 1000) {
            s = s.substring(0, 1000) + "\n...(more +" + (s.length() - 1000) + ")";
        }
        return "(" + (obj == null ? "null" : obj.getClass()) + ")" + s;
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

    public boolean innerTest(String test, Object params) {
        return MybatisMapperInflater.INSTANCE.testExpression(test, params);
    }

    @Override
    public Object eval(String script, Object params) {
//        if(isDebug()) {
//            logger().logDebug("eval [" + script + "] near [" + ContextHolder.traceLocation() + "] ... ");
//        }
        try {
            int v = Integer.parseInt(script);
//            if(isDebug()) {
//                logger().logDebug("eval [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(v));
//            }
        } catch (Exception e) {

        }
        try {
            long v = Long.parseLong(script);
//            if(isDebug()) {
//                logger().logDebug("eval [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(v));
//            }
        } catch (Exception e) {

        }
        try {
            Boolean ok = ObjectConvertor.tryParseBoolean(script);
            if (ok != null) {
//                if(isDebug()) {
//                    logger().logDebug("eval [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ok));
//                }
                return ok;
            }
        } catch (Exception e) {

        }
        try {
            double v = Double.parseDouble(script);
//            if(isDebug()) {
//                logger().logDebug("eval [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(v));
//            }
        } catch (Exception e) {

        }
        Object ret = innerEval(script, params);
//        if(isDebug()) {
//            logger().logDebug("eval [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ret));
//        }
        return ret;
    }

    public Object innerEval(String script, Object params) {
        return MybatisMapperInflater.INSTANCE.evalExpression(script, params);
    }

    @Override
    public Object evalScript(String lang, String script, Map<String, Object> params) {
//        if(isDebug()) {
//            logger().logDebug("eval script [" + script + "] on lang [" + lang + "] near [" + ContextHolder.traceLocation() + "] ... ");
//        }
        EvalScriptProvider provider = getEvalScriptProviders().touchFirst(e -> e.support(lang));
        if (provider == null) {
            throw new ThrowSignalException("eval script provider not found for lang=" + lang);
        }
        prepareParams(params);
        Object ret = provider.eval(script, params, this);
//        if(isDebug()) {
//            logger().logDebug("eval script [" + script + "] on lang [" + lang + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ret));
//        }
        return ret;
    }

    public boolean isVisitKeepValue(String script) {
        if (script == null) {
            return false;
        }
        script = script.trim();
        for (String keepName : ParamsConsts.KEEP_NAMES) {
            if (script.equals(keepName)) {
                return true;
            }
            if (script.startsWith(keepName + ".")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void visitSet(Map<String, Object> params, String result, Object value) {
//        if(isDebug()){
//            if(!isVisitKeepValue(result)){
//                if(isDebug()) {
//                    logger().logDebug("visit set [" + result + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(value));
//                }
//            }
//        }
        Visitor visitor = Visitor.visit(result, params);
        visitor.set(value);
    }

    @Override
    public void visitDelete(Map<String, Object> params, String result) {
//        if(isDebug()) {
//            logger().logDebug("visit delete [" + result + "] near [" + ContextHolder.traceLocation() + "] ");
//        }
        Visitor visitor = Visitor.visit(result, params);
        visitor.delete();
    }

    @Override
    public Object visit(String script, Object params) {
//        if(isDebug()) {
//            logger().logDebug("visit [" + script + "] near [" + ContextHolder.traceLocation() + "] ... ");
//        }
        if (params instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) params;
            if (map.containsKey(script)) {
                Object ret = map.get(script);
//                if(isDebug()) {
//                    if (!isVisitKeepValue(script)) {
//                        if(isDebug()) {
//                            logger().logDebug("visit [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ret));
//                        }
//                    }
//                }
                return ret;
            }
        }
        try {
            Visitor visitor = Visitor.visit(script, params);
            if (visitor != null) {
                Object ret = visitor.get();
//                if(isDebug()) {
//                    if (!isVisitKeepValue(script)) {
//                        if(isDebug()) {
//                            logger().logDebug("visit [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ret));
//                        }
//                    }
//                }
                return ret;
            }
        } catch (Exception e) {
            logger().logWarn(() -> "visitor access error, will fallback to inner visit, visitor error message:" + e.getMessage(), e);
        }
        Object ret = innerVisit(script, params);
//        if(isDebug()) {
//            if (!isVisitKeepValue(script)) {
//                if(isDebug()) {
//                    logger().logDebug("visit [" + script + "] near [" + ContextHolder.traceLocation() + "] is: " + stringifyWithType(ret));
//                }
//            }
//        }
        return ret;
    }

    public Object innerVisit(String script, Object params) {
        return null;
    }

    @Override
    public String render(String script, Object params) {
//        if(isDebug()) {
//            logger().logDebug("render near [" + ContextHolder.traceLocation() + "] script: " + stringifyWithType(script));
//        }
        String ret = innerRender(script, params);
//        if(isDebug()) {
//            logger().logDebug("render near [" + ContextHolder.traceLocation() + "] \nscript: \n" + stringifyWithType(script) + "\nresult: \n" + stringifyWithType(ret));
//        }
        return ret;
    }

    public String innerRender(String script, Object params) {
        return script;
    }

    @Override
    public Connection getConnection(String datasource, Map<String, Object> params) {
        if (datasource == null || datasource.isEmpty()) {
            datasource = ParamsConsts.DEFAULT_DATASOURCE;
        }
        Map<String, String> datasourcesMapping = (Map<String, String>) params.get(ParamsConsts.DATASOURCES_MAPPING);
        if (datasourcesMapping != null) {
            String mapping = datasourcesMapping.get(datasource);
            if (mapping != null && !mapping.isEmpty()) {
                datasource = mapping;
            }
        }

        Map<String, Connection> connectionMap = (Map<String, Connection>) params.get(ParamsConsts.CONNECTIONS);
        if (connectionMap == null) {
            connectionMap = new LinkedHashMap<>();
            params.put(ParamsConsts.CONNECTIONS, connectionMap);
        }
        Connection conn = connectionMap.get(datasource);
        if (conn != null) {
            try {
                if (!conn.isClosed() && conn.isValid(300)) {
                    return conn;
                }
            } catch (Exception e) {
                logger().logWarn(() -> "connection closed or are invalided:" + e.getMessage(), e);
            }
            try {
                if(!conn.isClosed()){
                    if(!conn.getAutoCommit()){
                       conn.commit();
                    }
                }
            } catch (SQLException e) {
                logger().logWarn(e.getMessage(), e);
            }finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger().logWarn(() -> e.getMessage(), e);
                }
            }
        }

        Map<String, DataSource> datasourceMap = (Map<String, DataSource>) params.get(ParamsConsts.DATASOURCES);
        DataSource ds = datasourceMap.get(datasource);
        if (ds != null) {
            try {
                conn = ds.getConnection();
                afterNewConnection(datasource,conn);
            } catch (SQLException e) {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }

        String newConnDs = datasource;
        if (isDebug()) {
            logger().logDebug(XProc4jConsts.NAME + " new-connection for datasource:" + newConnDs + " near [" + ContextHolder.traceLocation() + "] ");
        }
        if (conn != null) {
            connectionMap.put(datasource, conn);
        }

        if (conn == null) {
            throw new IllegalStateException(XProc4jConsts.NAME + " missing datasource connection: " + newConnDs + " near [" + ContextHolder.traceLocation() + "] ");
        }

        return conn;
    }

    public void afterNewConnection(String datasource,Connection conn) throws SQLException {
        if(defaultTransactionalConnection.get()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            conn.setHoldability(ResultSet.HOLD_CURSORS_OVER_COMMIT);
        }
    }

    public Function<String, String> getColumnNameMapper(Class<?> resultType) {
        return TypeOf.typeOf(resultType, Map.class) ? (mapTypeColumnNameMapper) : otherTypeColumnNameMapper;
    }

    public void reportSlowSql(long useMillsSeconds, BindSql bql) {
        if (true) {
            SqlExecUseTimeEvent event = new SqlExecUseTimeEvent();
            event.setExecutor(this);
            event.setUseMillsSeconds(useMillsSeconds);
            event.setBql(bql);
            event.setLocation(ContextHolder.traceLocation());
            publishEvent(event);
        }
        if (useMillsSeconds >= slowSqlMinMillsSeconds) {
            logger().logWarn("slow sql, use " + useMillsSeconds + "(ms) : " + bql);
            SlowSqlEvent event = new SlowSqlEvent();
            event.setExecutor(this);
            event.setUseMillsSeconds(useMillsSeconds);
            event.setBql(bql);
            event.setLocation(ContextHolder.traceLocation());
            publishEvent(event);
        }
    }

    @Override
    public List<?> sqlQueryList(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        long bts = SystemClock.currentTimeMillis();
        BindSql execBql = null;
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (isDebug()) {
                logger().logDebug("sql-query-list:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql);
            }
            execBql = bql;
            List<?> list = JdbcResolver.list(conn, bql, resultType, -1, getColumnNameMapper(resultType));
            if (isDebug()) {
                logger().logDebug("sql-query-list:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql + "\nresult: is-empty:" + list.isEmpty());
            }
            return list;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(execBql != null ? (e.getMessage() + "\n" + execBql) : e.getMessage(), e);
            }
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            reportSlowSql(useTs, execBql);
        }
    }

    @Override
    public List<QueryColumn> sqlQueryColumns(String datasource, BindSql bql, Map<String, Object> params) {
        long bts = SystemClock.currentTimeMillis();
        BindSql execBql = null;
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (isDebug()) {
                logger().logDebug("sql-query-columns:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql);
            }
            execBql = bql;
            QueryResult qr = JdbcResolver.query(conn, bql, 1);
            List<QueryColumn> list = qr.getColumns();
            if (isDebug()) {
                logger().logDebug("sql-query-columns:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql + "\nresult: is-empty:" + list.isEmpty());
            }
            return list;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(execBql != null ? (e.getMessage() + "\n" + execBql) : e.getMessage(), e);
            }
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            reportSlowSql(useTs, execBql);
        }
    }

    public DatabaseType getDatabaseType(Connection conn) throws SQLException {
        return DatabaseType.typeOfConnection(conn);
    }

    public DatabaseType getDialectType(Connection conn) throws SQLException {
        return DIALECT_MAPPING.dialectOf(conn);
    }

    @Override
    public Object sqlQueryObject(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        long bts = SystemClock.currentTimeMillis();
        BindSql execBql = null;
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (isDebug()) {
                logger().logDebug("sql-query-object:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql);
            }
            execBql = bql;
            Object obj = JdbcResolver.get(conn, bql, resultType);
            if (isDebug()) {
                logger().logDebug("sql-query-object:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql + "\nresult: " + stringifyWithType(obj));
            }
            return obj;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(execBql != null ? (e.getMessage() + "\n" + execBql) : e.getMessage(), e);
            }
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            reportSlowSql(useTs, execBql);
        }
    }

    public void fillDatabaseDialectType(Map<String, Object> params, Connection conn) throws SQLException {
        DatabaseType databaseType = getDatabaseType(conn);
        visitSet(params, MybatisMapperInflater.DATABASE_TYPE, databaseType);
    }

    @Override
    public Object sqlQueryRow(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType) {
        long bts = SystemClock.currentTimeMillis();
        BindSql execBql = null;
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (isDebug()) {
                logger().logDebug("sql-query-row:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql);
            }
            execBql = bql;
            Object row = JdbcResolver.find(conn, bql, resultType, getColumnNameMapper(resultType));
            if (isDebug()) {
                logger().logDebug("sql-query-row:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql + "\nresult:" + stringifyWithType(row));
            }
            return row;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(execBql != null ? (e.getMessage() + "\n" + execBql) : e.getMessage(), e);
            }
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            reportSlowSql(useTs, execBql);
        }
    }

    @Override
    public int sqlUpdate(String datasource, BindSql bql, Map<String, Object> params) {
        long bts = SystemClock.currentTimeMillis();
        BindSql execBql = null;
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (isDebug()) {
                logger().logDebug("sql-update:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql);
            }
            execBql = bql;
            int num = JdbcResolver.update(conn, bql);
            if (isDebug()) {
                logger().logDebug("sql-update:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + bql + "\nresult:" + num);
            }
            return num;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(execBql != null ? (e.getMessage() + "\n" + execBql) : e.getMessage(), e);
            }
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            reportSlowSql(useTs, execBql);
        }
    }

    @Override
    public BindSql sqlWrapPage(String datasource, BindSql bql, ApiOffsetSize page, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (page != null && (page.getOffset() != null || page.getSize() != null)) {
                IPageWrapper wrapper = PageWrappers.wrapper(conn);
                bql = wrapper.apply(bql, page);
            }
            BindSql pageSql = bql;
            if (isDebug()) {
                logger().logDebug("sql-wrap-page:datasource=" + datasource + ", databaseType=" + params.get(MybatisMapperInflater.DATABASE_TYPE) + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + pageSql);
            }
            return pageSql;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public BindSql sqlWrapCount(String datasource, BindSql bql, Map<String, Object> params) {
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            ICountWrapper wrapper = CountWrappers.wrapper(conn);
            bql = wrapper.apply(bql);
            BindSql pageSql = bql;
            if (isDebug()) {
                logger().logDebug("sql-wrap-count:datasource=" + datasource + ", databaseType=" + params.get(MybatisMapperInflater.DATABASE_TYPE) + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + pageSql);
            }
            return pageSql;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    @Override
    public BindSql sqlScript(String datasource, List<Map.Entry<String, Object>> dialectScriptList, Map<String, Object> params, ApiOffsetSize page) {
        try {
            Connection conn = getConnection(datasource, params);
            Map.Entry<String, BindSql> entry = getDialectSqlScript(dialectScriptList, conn, params);
            fillDatabaseDialectType(params, conn);
            BindSql bql = entry.getValue();
            if (page != null && (page.getOffset() != null || page.getSize() != null)) {
                IPageWrapper wrapper = PageWrappers.wrapper(conn);
                bql = wrapper.apply(bql, page);
            }
            BindSql pageSql = bql;
            if (isDebug()) {
                logger().logDebug("sql-script:datasource=" + datasource + ", dialect=" + entry.getKey() + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + pageSql);
            }
            return pageSql;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(e.getMessage(), e);
            }
        }
    }

    public BindSql resolveSqlScript(String script, Map<String, Object> params) throws Exception {
        MybatisMapperNode mapperNode = MybatisMapperParser.parseScriptNode(script);
        BindSql bql = getMybatisMapperInflater().inflateSqlNode(mapperNode, params, new HashMap<>());
        return bql;
    }

    public MybatisMapperInflater getMybatisMapperInflater() {
        return MybatisMapperInflater.INSTANCE;
    }

    public Map.Entry<String, BindSql> getDialectSqlScript(List<Map.Entry<String, Object>> dialectScriptList,
                                                          Connection conn,
                                                          Map<String, Object> params) throws Exception {
        List<String> databaseNames = detectDatabaseType(conn);
        Map.Entry<String, BindSql> firstScript = null;
        Map.Entry<String, BindSql> nullScript = null;
        for (Map.Entry<String, Object> entry : dialectScriptList) {
            Map.Entry<String, BindSql> val = new AbstractMap.SimpleEntry<>(entry.getKey(), null);
            Object scriptObj = entry.getValue();
            if (scriptObj instanceof BindSql) {
                BindSql bql = (BindSql) scriptObj;
                val.setValue(bql);
            } else {
                String str = String.valueOf(scriptObj);
                BindSql bql = resolveSqlScript(str, params);
                val.setValue(bql);
            }
            if (firstScript == null) {
                firstScript = val;
            }
            String key = entry.getKey();
            if (key == null || key.isEmpty()) {
                if (nullScript == null) {
                    nullScript = val;
                }
            }
            if (key != null) {
                String[] arr = key.split(",");
                for (String item : arr) {
                    for (String databaseName : databaseNames) {
                        if (item.equalsIgnoreCase(databaseName)) {
                            return val;
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
    public List<?> sqlQueryPage(String datasource, BindSql bql, Map<String, Object> params, Class<?> resultType, ApiOffsetSize page) {
        long bts = SystemClock.currentTimeMillis();
        BindSql execBql = null;
        try {
            Connection conn = getConnection(datasource, params);
            fillDatabaseDialectType(params, conn);
            if (page != null && (page.getOffset() != null || page.getSize() != null)) {
                IPageWrapper wrapper = PageWrappers.wrapper(conn);
                bql = wrapper.apply(bql, page);
            }
            BindSql pageBql = bql;
            if (isDebug()) {
                logger().logDebug("sql-query-page:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + pageBql);
            }
            execBql = pageBql;
            List<?> list = JdbcResolver.list(conn, pageBql, resultType, -1, getColumnNameMapper(resultType));
            if (isDebug()) {
                logger().logDebug("sql-query-page:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] " + " \n\tbql:\n" + pageBql + "\nresult: is-empty:" + list.isEmpty());
            }
            return list;
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new ThrowSignalException(execBql != null ? (e.getMessage() + "\n" + execBql) : e.getMessage(), e);
            }
        } finally {
            long ets = SystemClock.currentTimeMillis();
            long useTs = ets - bts;
            reportSlowSql(useTs, execBql);
        }
    }

    @Override
    public void sqlTransBegin(String datasource, int isolation, Map<String, Object> params) {
        if (isDebug()) {
            logger().logDebug("sql-trans-begin:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] ");
        }
        try {
            if (isolation < 0) {
                isolation = Connection.TRANSACTION_READ_COMMITTED;
            }
            Connection conn = getConnection(datasource, params);
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(isolation);
        } catch (SQLException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransCommit(String datasource, Map<String, Object> params, boolean checked) {
        if (isDebug()) {
            logger().logDebug("sql-trans-commit:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] ");
        }
        try {
            Connection conn = getConnection(datasource, params);
            if (checked) {
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
            } else {
                conn.commit();
            }
        } catch (SQLException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransRollback(String datasource, Map<String, Object> params, boolean checked) {
        if (isDebug()) {
            logger().logDebug("sql-trans-rollback:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] ");
        }
        try {
            Connection conn = getConnection(datasource, params);
            if (checked) {
                if (!conn.getAutoCommit()) {
                    conn.rollback();
                }
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }

    @Override
    public void sqlTransNone(String datasource, Map<String, Object> params, boolean checked) {
        if (isDebug()) {
            logger().logDebug("sql-trans-none:datasource=" + datasource + " near [" + ContextHolder.traceLocation() + "] ");
        }
        try {
            Connection conn = getConnection(datasource, params);
            if (checked) {
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            throw new ThrowSignalException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface ExecXmlNodeDelegateTask {
        void exec(XmlNode node, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) throws Throwable;
    }

    @FunctionalInterface
    public interface ExecJavaCallerDelegateTask {
        void exec(JdbcProcedureJavaCaller caller, Map<String, Object> params, boolean beforeNewConnection, boolean afterCloseConnection) throws Throwable;
    }

    @FunctionalInterface
    public interface ExecDelegateTask {
        void exec(Map<String, Object> params, Object... args) throws Throwable;
    }

}
