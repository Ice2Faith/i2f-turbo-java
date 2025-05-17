package i2f.jdbc.procedure.node.impl;

import i2f.bindsql.BindSql;
import i2f.extension.antlr4.script.tiny.impl.DefaultTinyScriptResolver;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.extension.antlr4.script.tiny.impl.TinyScriptResolver;
import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.jdk.JdkInstanceStaticMethod;
import i2f.jdbc.procedure.consts.*;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reportor.GrammarReporter;
import i2f.jdbc.procedure.script.EvalScriptProvider;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.lru.LruList;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangEvalTinyScriptNode extends AbstractExecutorNode implements EvalScriptProvider {
    public static final String TAG_NAME = TagConsts.LANG_EVAL_TINYSCRIPT;
    public static final String ALIAS_TAG_NAME = TagConsts.LANG_EVAL_TS;

    public static void main(String[] args) {
        /*language=tinyscript*/
        String script = "${a}+${b}";
        Map<String, Object> context = new HashMap<>();
        context.put("a", 1);
        context.put("b", 2.5);
        Object obj = evalTinyScript(script, context, null);
        System.out.println(obj);
    }

    protected static final WeakHashMap<JdbcProcedureExecutor, ProcedureTinyScriptResolver> resolverMap = new WeakHashMap<>();

    public static Object evalTinyScript(String script, Object context, JdbcProcedureExecutor executor) {

        Object obj = null;

        try {
            TinyScriptResolver resolver = null;
            if (executor != null) {
                synchronized (resolverMap) {
                    resolver = resolverMap.get(executor);
                    if (resolver == null) {
                        ProcedureTinyScriptResolver val = new ProcedureTinyScriptResolver(executor);
                        resolverMap.put(executor, val);
                        resolver = val;
                    }
                }
            }
            obj = TinyScript.script(script, context, resolver);
        } catch (Exception e) {
            if (e instanceof SignalException) {
                throw (SignalException) e;
            } else {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return obj;
    }

    @Override
    public boolean support(XmlNode node) {
        if (XmlNode.NodeType.ELEMENT != node.getNodeType()) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName())
                || ALIAS_TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String script = node.getTextBody();
        if (script != null && !script.isEmpty()) {
            GrammarReporter.reportExprFeatureGrammar(script, FeatureConsts.EVAL_TINYSCRIPT, node, "element body ", warnPoster);
        }
    }

    @Override
    public void execInner(XmlNode node, Map<String, Object> context, JdbcProcedureExecutor executor) {
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);
        String script = node.getTextBody();
        Object obj = evalTinyScript(script, context, executor);

        if (result != null) {
            obj = executor.resultValue(obj, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
            executor.visitSet(context, result, obj);
        }

    }

    @Override
    public boolean support(String lang) {
        return Arrays.asList(LangConsts.TINYSCRIPT, LangConsts.TS).contains(lang);
    }

    @Override
    public Object eval(String script, Map<String, Object> params, JdbcProcedureExecutor executor) {
        Object obj = evalTinyScript(script, params, executor);
        return obj;
    }

    public static class ProcedureTinyScriptResolver extends DefaultTinyScriptResolver {
        protected JdbcProcedureExecutor executor;

        private final ExecutorMethodProvider methodProvider = new ExecutorMethodProvider();
        private final ConcurrentHashMap<String, LruList<IMethod>> executorMethods = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<String,LruList<Method>> execContextMethods = new ConcurrentHashMap<>();

        public class ExecutorMethodProvider {
            public Class<?> load_class(String className) {
                return executor.loadClass(className);
            }

            public boolean is_deug() {
                return executor.isDebug();
            }

            public String env(String property) {
                return executor.env(property);
            }

            public String env(String property, String defaultValue) {
                return executor.env(property, defaultValue);
            }

            public Integer env_int(String property) {
                return executor.envAs(property, Integer.class, null);
            }

            public Integer env_int(String property, Integer defaultValue) {
                return executor.envAs(property, Integer.class, defaultValue);
            }

            public Long env_long(String property) {
                return executor.envAs(property, Long.class, null);
            }

            public Long env_long(String property, Long defaultValue) {
                return executor.envAs(property, Long.class, defaultValue);
            }

            public Double env_double(String property) {
                return executor.envAs(property, Double.class, null);
            }

            public Double env_double(String property, Double defaultValue) {
                return executor.envAs(property, Double.class, defaultValue);
            }

            public Boolean env_boolean(String property) {
                return executor.envAs(property, Boolean.class, null);
            }

            public Boolean env_boolean(String property, Boolean defaultValue) {
                return executor.envAs(property, Boolean.class, defaultValue);
            }

            public Object get_bean(String name) {
                return executor.getBean(name);
            }

            public Object get_bean(Class<?> type) {
                return executor.getBean(type);
            }

            public ProcedureMeta get_meta(String procedureId) {
                return executor.getMeta(procedureId);
            }

            public String trace_location() {
                return executor.traceLocation();
            }

            public String trace_file() {
                return ContextHolder.TRACE_LOCATION.get();
            }

            public int trace_line() {
                return ContextHolder.TRACE_LINE.get();
            }

            public XmlNode trace_node() {
                return ContextHolder.TRACE_NODE.get();
            }

            public Throwable trace_error() {
                return ContextHolder.TRACE_ERROR.get();
            }

            public String trace_errmsg() {
                return ContextHolder.TRACE_ERRMSG.get();
            }

            public String tracking_comment() {
                return tracking_comment(false);
            }

            public String tracking_comment(boolean force) {
                if (!force) {
                    if (!executor.isDebug()) {
                        return "";
                    }
                }
                return AbstractExecutorNode.getTrackingComment(ContextHolder.TRACE_NODE.get());
            }

            public void log_debug(Object obj) {
                executor.logDebug(obj);
            }

            public void log_info(Object obj) {
                executor.logInfo(obj);
            }

            public void log_warn(Object obj) {
                executor.logWarn(obj);
            }

            public void log_error(Object obj) {
                executor.logError(obj);
            }
        }

        public class ExecContextMethodProvider {
            public JdbcProcedureExecutor executor;
            public Object context;

            public ExecContextMethodProvider(JdbcProcedureExecutor executor, Object context) {
                this.executor = executor;
                this.context = context;
            }

            public Connection get_connection(String datasource) {
                return executor.getConnection(datasource, (Map<String, Object>) context);
            }

            public Object eval(String script) {
                return executor.eval(script, context);
            }

            public Object eval_script(String lang, String script) {
                return executor.evalScript(lang, script, (Map<String, Object>) context);
            }

            public String render(String script) {
                return executor.render(script, context);
            }

            public Object visit(String expression) {
                return executor.visit(expression, context);
            }

            public void visit_set(String expression, Object value) {
                executor.visitSet((Map<String, Object>) context, expression, value);
            }

            public void visit_delete(String expression) {
                executor.visitDelete((Map<String, Object>) context, expression);
            }

            public boolean test(String expression) {
                return executor.test(expression, context);
            }

            public void sql_trans_begin(String datasource) {
                executor.sqlTransBegin(datasource, Connection.TRANSACTION_READ_COMMITTED, (Map<String, Object>) context);
            }

            public void sql_trans_commit(String datasource) {
                executor.sqlTransCommit(datasource, (Map<String, Object>) context);
            }

            public void sql_trans_rollback(String datasource) {
                executor.sqlTransRollback(datasource, (Map<String, Object>) context);
            }

            public void sql_trans_none(String datasource) {
                executor.sqlTransNone(datasource, (Map<String, Object>) context);
            }

            public Object sql_query_object(String datasource, String sql, Object... args) {
                return executor.sqlQueryObject(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null);
            }

            public Object sql_query_row(String datasource, String sql, Object... args) {
                return executor.sqlQueryRow(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null);
            }

            public Object sql_query_list(String datasource, String sql, Object... args) {
                return executor.sqlQueryList(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context, null);
            }

            public Object sql_update(String datasource, String sql, Object... args) {
                return executor.sqlUpdate(datasource, new BindSql(sql, new ArrayList<>(Arrays.asList(args))), (Map<String, Object>) context);
            }
        }

        public List<IMethod> getExecutorMethods(String naming) {
            if (executorMethods.isEmpty()) {
                synchronized (executorMethods) {
                    Method[] list = ExecutorMethodProvider.class.getMethods();
                    for (Method item : list) {
                        executorMethods.computeIfAbsent(item.getName(),(k)->new LruList<>())
                                .add(new JdkInstanceStaticMethod(methodProvider, item));
                    }
                }
            }
            return executorMethods.get(naming);
        }

        public List<Method> getExecContextMethods(String naming) {
            if (execContextMethods.isEmpty()) {
                synchronized (execContextMethods) {
                    Method[] list = ExecContextMethodProvider.class.getMethods();
                    for (Method item : list) {
                        execContextMethods.computeIfAbsent(item.getName(),(k)->new LruList<>())
                                .add(item);
                    }
                }
            }
            return execContextMethods.get(naming);
        }

        public ProcedureTinyScriptResolver(JdbcProcedureExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void debugLog(Supplier<Object> supplier) {
//            if(executor.isDebug()) {
//                executor.logDebug(() -> "tiny-script:" + supplier.get() + " , at " + ContextHolder.TRACE_LOCATION.get() + ":" + ContextHolder.TRACE_LINE.get());
//            }
        }

        @Override
        public void openDebugger(Object context, String tag, String conditionExpression) {
            executor.openDebugger("tiny-script:" + tag, context, conditionExpression);
        }

        @Override
        public void setValue(Object context, String name, Object value) {
            executor.visitSet((Map<String, Object>) context, name, value);
        }

        @Override
        public Object getValue(Object context, String name) {
            return executor.visit(name, (Map<String, Object>) context);
        }

        @Override
        public Class<?> loadClass(Object context, String className) {
            return executor.loadClass(className);
        }

        @Override
        public Reference<Object> beforeFunctionCall(Object context, Object target, boolean isNew, String naming, List<Object> argList) {
            try {
                ProcedureMeta meta = executor.getMeta(naming);
                if (meta == null) {
                    return Reference.nop();
                }

                Map<String, Object> callParams = executor.newParams((Map<String, Object>) context);

                Map<String, Object> argsMap = castArgumentListAsNamingMap(context, argList);
                if (argsMap.isEmpty()) {
                    List<String> arguments = meta.getArguments().stream().filter(e -> !Arrays.asList(
                            AttrConsts.ID, AttrConsts.REFID,
                            AttrConsts.RESULT, ParamsConsts.RETURN
                    ).contains(e)).collect(Collectors.toList());
                    for (int i = 0; i < argList.size(); i++) {
                        if (i >= arguments.size()) {
                            break;
                        }
                        String name = arguments.get(i);
                        Object val = argList.get(i);
                        argsMap.put(name, val);
                    }
                }

                callParams.putAll(argsMap);

                Map<String, Object> ret = executor.exec(naming, callParams, false, false);
                if (ret.containsKey(ParamsConsts.RETURN)) {
                    Object val = ret.get(ParamsConsts.RETURN);
                    if (val instanceof Reference) {
                        return (Reference<Object>) val;
                    }
                    return Reference.of(val);
                }
                return Reference.of(ret);
            } catch (NotFoundSignalException e) {
                return Reference.nop();
            }
        }


        @Override
        public IMethod findMethod(Object context, String naming, List<Object> args) {
            List<IMethod> list = ContextHolder.INVOKE_METHOD_MAP.get(naming);
            if (list != null && !list.isEmpty()) {
                IMethod method = ReflectResolver.matchExecMethod(list, args);
                if (method != null) {
                    return method;
                }
            }

            list = getExecutorMethods(naming);
            if (list != null && !list.isEmpty()) {
                IMethod method = ReflectResolver.matchExecMethod(list, args);
                if (method != null) {
                    return method;
                }
            }

            list = getExecContextMethods(naming).stream()
                    .map(e -> new JdkInstanceStaticMethod(new ExecContextMethodProvider(executor, context), e))
                    .collect(Collectors.toList());
            if (list != null && !list.isEmpty()) {
                IMethod method = ReflectResolver.matchExecMethod(list, args);
                if (method != null) {
                    return method;
                }
            }

            return super.findMethod(context, naming, args);
        }

        @Override
        public String renderString(Object context, String text) {
            return executor.render(text, (Map<String, Object>) context);
        }
    }


}
