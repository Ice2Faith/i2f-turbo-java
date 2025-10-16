package i2f.jdbc.procedure.node.impl.tinyscript;

import i2f.extension.antlr4.script.tiny.impl.DefaultTinyScriptResolver;
import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.jdk.JdkInstanceStaticMethod;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;
import i2f.reference.Reference;
import i2f.reflect.ReflectResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/10/5 14:54
 * @desc
 */
@Data
@NoArgsConstructor
public class ProcedureTinyScriptResolver extends DefaultTinyScriptResolver {
    protected JdbcProcedureExecutor executor;
    protected XmlNode node;

    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Method>> executorMethods = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Method>> execContextMethods = new ConcurrentHashMap<>();

    public static CopyOnWriteArrayList<Method> getExecutorMethods(String naming) {
        if (executorMethods.isEmpty()) {
            synchronized (executorMethods) {
                Method[] list = ExecutorMethodProvider.class.getMethods();
                for (Method item : list) {
                    if (Object.class.equals(item.getDeclaringClass())) {
                        continue;
                    }
                    executorMethods.computeIfAbsent(item.getName(), (k) -> new CopyOnWriteArrayList<>())
                            .add(item);
                }
            }
        }
        return executorMethods.get(naming);
    }

    public static CopyOnWriteArrayList<Method> getExecContextMethods(String naming) {
        if (execContextMethods.isEmpty()) {
            synchronized (execContextMethods) {
                Method[] list = ExecContextMethodProvider.class.getMethods();
                for (Method item : list) {
                    if (Object.class.equals(item.getDeclaringClass())) {
                        continue;
                    }
                    execContextMethods.computeIfAbsent(item.getName(), (k) -> new CopyOnWriteArrayList<>())
                            .add(item);
                }
            }
        }
        return execContextMethods.get(naming);
    }

    public ProcedureTinyScriptResolver(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }

    public ProcedureTinyScriptResolver(JdbcProcedureExecutor executor, XmlNode node) {
        this.executor = executor;
        this.node = node;
    }

    @Override
    public void debugLog(Supplier<Object> supplier) {
//            if(executor.isDebug()) {
//                executor.logger().logDebug(() -> "tiny-script:" + supplier.get() + " , at " + ContextHolder.TRACE_LOCATION.get() + ":" + ContextHolder.TRACE_LINE.get());
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
    public Object getFunctionCallContext(Object context, Object target, boolean isNew, String naming, List<Object> argList) {
        ProcedureFunctionCallContext ret = new ProcedureFunctionCallContext();
        ret.setResolver(this);
        ret.setContext(context);
        ret.setTarget(target);
        ret.setNew(isNew);
        ret.setNaming(naming);
        ret.setArgList(argList);
        ret.setExecutor(executor);
        ret.setNode(node);
        return ret;
    }

    @Override
    public Reference<Object> beforeFunctionCall(Object context, Object target, boolean isNew, String naming, List<Object> argList) {
        try {
            ProcedureMeta meta = executor.getMeta(naming, (Map<String, Object>) context);
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
        } finally {
            if (node != null) {
                AbstractExecutorNode.updateTraceInfo(node, (Map<String, Object>) context, executor);
            }
        }
    }


    @Override
    public IMethod findMethod(Object context, String naming, List<Object> args) {
        CopyOnWriteArrayList<IMethod> namingMethods = ContextHolder.INVOKE_METHOD_MAP.get(naming);
        if (namingMethods != null && !namingMethods.isEmpty()) {
            IMethod method = ReflectResolver.matchExecMethod(namingMethods, args);
            if (method != null) {
                return method;
            }
        }

        CopyOnWriteArrayList<Method> methods = getExecutorMethods(naming);
        if (methods != null && !methods.isEmpty()) {
            Method method = ReflectResolver.matchExecutable(methods, args);
            if (method != null) {
                return new JdkInstanceStaticMethod(new ExecutorMethodProvider(executor), method);
            }
        }

        methods = getExecContextMethods(naming);
        if (methods != null && !methods.isEmpty()) {
            Method method = ReflectResolver.matchExecutable(methods, args);
            if (method != null) {
                return new JdkInstanceStaticMethod(new ExecContextMethodProvider(executor, context), method);
            }
        }

        return super.findMethod(context, naming, args);
    }

    @Override
    public String renderString(Object context, String text) {
        return executor.render(text, (Map<String, Object>) context);
    }
}
