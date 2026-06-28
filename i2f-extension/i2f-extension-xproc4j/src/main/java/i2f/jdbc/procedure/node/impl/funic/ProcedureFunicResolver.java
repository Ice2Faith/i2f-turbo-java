package i2f.jdbc.procedure.node.impl.funic;

import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.context.FunicFunctionCallContext;
import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;
import i2f.extension.antlr4.script.funic.lang.resolver.impl.DefaultFunicResolver;
import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.jdk.JdkInstanceStaticMethod;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.debugger.JdbcProcedureDebugBridgeReporter;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.impl.tinyscript.ExecutorMethodProvider;
import i2f.jdbc.procedure.node.impl.tinyscript.ProcedureTinyScriptResolver;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.reference.Reference;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2026/5/13 20:27
 * @desc
 */
public class ProcedureFunicResolver extends DefaultFunicResolver {
    protected JdbcProcedureExecutor executor;
    protected XmlNode node;

    public ProcedureFunicResolver(JdbcProcedureExecutor executor) {
        this.executor = executor;
    }

    public ProcedureFunicResolver(JdbcProcedureExecutor executor, XmlNode node) {
        this.executor = executor;
        this.node = node;
    }

    @Override
    public void debugLog(Supplier<Object> supplier) {
//            if(executor.isDebug()) {
//                executor.logger().logDebug(() -> "funic:" + supplier.get() + " , at " + ContextHolder.TRACE_LOCATION.get() + ":" + ContextHolder.TRACE_LINE.get());
//            }
    }

    @Override
    public void debugBridge(String fileName, int lineNumber, Supplier<Map<String, Object>> variableMapSupplier) {
        JdbcProcedureDebugBridgeReporter.proxy(fileName, lineNumber, variableMapSupplier);
    }

    @Override
    public void onDebugger(String label, Object value, FunicParser.DebuggerExpressContext ctx, DefaultFunicVisitor visitor) {
        executor.openDebugger("funic:" + label, visitor.getContext(), ctx.getText());
    }

    @Override
    public Class<?> findClass(String className, DefaultFunicVisitor visitor) {
        Class<?> clazz = executor.loadClass(className);
        if (clazz != null) {
            return clazz;
        }
        clazz = super.findClass(className, visitor);
        return clazz;
    }

    @Override
    public Reference<?> beforeInvokeInstanceMethod(Object target, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        return super.beforeInvokeInstanceMethod(target, methodName, args, visitor);
    }

    @Override
    public Reference<?> beforeInvokeGlobalMethod(String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        ProcedureMeta meta = executor.getMeta(methodName, (Map<String, Object>) visitor.getContext());
        if (meta == null) {
            return super.beforeInvokeGlobalMethod(methodName, args, visitor);
        }

        Map<String, Object> callParams = executor.newParams((Map<String, Object>) visitor.getContext());

        Map<String, Object> argsMap = castArgumentListAsNamingMap(args, visitor);
        if (argsMap.isEmpty()) {
            List<String> arguments = meta.getArguments().stream().filter(e -> !Arrays.asList(
                    AttrConsts.ID, AttrConsts.REFID,
                    AttrConsts.RESULT, ParamsConsts.RETURN
            ).contains(e)).collect(Collectors.toList());
            for (int i = 0; i < args.size(); i++) {
                if (i >= arguments.size()) {
                    break;
                }
                String name = arguments.get(i);
                Map.Entry<String, Object> entry = args.get(i);
                argsMap.put(name, entry.getValue());
            }
        }

        callParams.putAll(argsMap);

        Map<String, Object> ret = executor.exec(methodName, callParams, false, false);
        if (ret.containsKey(ParamsConsts.RETURN)) {
            Object val = ret.get(ParamsConsts.RETURN);
            if (val instanceof Reference) {
                return (Reference<Object>) val;
            }
            return Reference.of(val);
        }
        return Reference.of(ret);


    }

    public Map<String, Object> castArgumentListAsNamingMap(List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        Map<String, Object> ret = new HashMap<>();
        for (Map.Entry<String, Object> entry : args) {
            if (entry.getKey() == null) {
                continue;
            }
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    @Override
    public List<IMethod> searchGlobalMethod(String methodName, DefaultFunicVisitor visitor) {
        List<IMethod> ret = new ArrayList<>();

        CopyOnWriteArrayList<IMethod> namingMethods = ContextHolder.INVOKE_METHOD_MAP.get(methodName);
        if (namingMethods != null) {
            ret.addAll(namingMethods);
        }

        CopyOnWriteArrayList<Method> methods = ProcedureTinyScriptResolver.getExecutorMethods(methodName);
        if (methods != null && !methods.isEmpty()) {
            for (Method method : methods) {
                IMethod item = new JdkInstanceStaticMethod(new ExecutorMethodProvider(executor), method);
                ret.add(item);
            }
        }

        methods = ProcedureTinyScriptResolver.getExecContextMethods(methodName);
        if (methods != null && !methods.isEmpty()) {
            for (Method method : methods) {
                IMethod item = new JdkInstanceStaticMethod(new ExecutorMethodProvider(executor), method);
                ret.add(item);
            }
        }

        List<IMethod> defaultList = super.searchGlobalMethod(methodName, visitor);
        ret.addAll(defaultList);
        return ret;
    }


    @Override
    public Reference<?> beforeInvokeStaticMethod(Class<?> type, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        return super.beforeInvokeStaticMethod(type, methodName, args, visitor);
    }

    @Override
    public String renderString(String text, DefaultFunicVisitor visitor) {
        return executor.render(text, (Map<String, Object>) visitor.getContext());
    }

    @Override
    public Object getCallContextOfInvokeInstanceMethod(Object target, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        return ProcedureFunicFunctionCallContext.builder()
                .visitor(visitor)
                .type(FunicFunctionCallContext.Type.INSTANCE_METHOD)
                .invokeTarget(target)
                .methodName(methodName)
                .argsList(args)
                .executor(executor)
                .node(node)
                .build();
    }

    @Override
    public Object getCallContextOfInvokeGlobalMethod(String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        return ProcedureFunicFunctionCallContext.builder()
                .visitor(visitor)
                .type(FunicFunctionCallContext.Type.GLOBAL_METHOD)
                .methodName(methodName)
                .argsList(args)
                .executor(executor)
                .node(node)
                .build();
    }

    @Override
    public Object getCallContextOfNewInstance(Class<?> clazz, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        return ProcedureFunicFunctionCallContext.builder()
                .visitor(visitor)
                .type(FunicFunctionCallContext.Type.NEW_INSTANCE)
                .callClass(clazz)
                .argsList(args)
                .executor(executor)
                .node(node)
                .build();
    }

    @Override
    public Object getCallContextOfInvokeStaticMethod(Class<?> type, String methodName, List<Map.Entry<String, Object>> args, DefaultFunicVisitor visitor) {
        return ProcedureFunicFunctionCallContext.builder()
                .visitor(visitor)
                .type(FunicFunctionCallContext.Type.STATIC_METHOD)
                .callClass(type)
                .methodName(methodName)
                .argsList(args)
                .executor(executor)
                .node(node)
                .build();
    }

}
