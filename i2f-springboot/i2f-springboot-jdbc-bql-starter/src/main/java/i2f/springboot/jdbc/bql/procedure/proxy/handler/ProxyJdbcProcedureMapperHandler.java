package i2f.springboot.jdbc.bql.procedure.proxy.handler;

import i2f.convert.obj.ObjectConvertor;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.ParamsConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.reflect.ReflectResolver;
import i2f.springboot.jdbc.bql.procedure.proxy.annotations.ProcedureId;
import i2f.springboot.jdbc.bql.procedure.proxy.annotations.ProcedureParam;
import i2f.typeof.TypeOf;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2024/6/6 8:56
 * @desc
 */
public class ProxyJdbcProcedureMapperHandler implements IProxyInvocationHandler {

    private Class<?> proxyClass;

    private JdbcProcedureExecutor executor;


    public ProxyJdbcProcedureMapperHandler(Class<?> proxyClass, JdbcProcedureExecutor executor) {
        this.proxyClass = proxyClass;
        this.executor = executor;
    }

    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof JdkMethod)) {
            throw new IllegalStateException("un-support proxy invokable type=" + invokable.getClass());
        }
        JdkMethod ivkMethod = (JdkMethod) invokable;
        Method method = ivkMethod.getMethod();

        if (method.getName().equals("toString") && method.getParameterCount() == 0) {
            return proxyClass.getName() + ivkObj.getClass().getSimpleName();
        }
        if (method.getName().equals("hashCode") && method.getParameterCount() == 0) {
            return ivkObj.getClass().getName().hashCode();
        }
        String callId = null;
        Map<String, ProcedureMeta> metaMap = executor.getMetaMap();

        if (callId == null) {
            ProcedureId ann = method.getDeclaredAnnotation(ProcedureId.class);
            if (ann != null) {
                String id = ann.value();
                if (id != null && !id.isEmpty()) {
                    ProcedureMeta meta = metaMap.get(id);
                    if (meta != null) {
                        callId = id;
                    }
                    if (callId == null) {
                        for (String procedureId : metaMap.keySet()) {
                            if (callId == null) {
                                if (procedureId.equalsIgnoreCase(id)) {
                                    callId = procedureId;
                                }
                            }
                        }
                    }
                }
            }
        }

        String methodName = method.getName();
        if (callId == null) {
            ProcedureMeta meta = metaMap.get(methodName);
            if (meta != null) {
                callId = methodName;
            }
        }
        if (callId == null) {
            String weakMethodName = methodName.replace("_", "").toLowerCase();
            String weakId = null;
            for (String procedureId : metaMap.keySet()) {
                if (callId == null) {
                    if (procedureId.equalsIgnoreCase(methodName)) {
                        callId = procedureId;
                    }
                }
                if (weakId == null) {
                    if (weakMethodName.equalsIgnoreCase(procedureId.replace("_", ""))) {
                        weakId = procedureId;
                    }
                }
            }
            if (callId == null) {
                callId = weakId;
            }
        }

        if (callId == null) {
            throw new IllegalArgumentException("missing procedure meta for id=" + callId);
        }

        ProcedureMeta meta = metaMap.get(callId);
        if (meta == null) {
            throw new IllegalArgumentException("missing procedure meta for id=" + callId);
        }

        List<String> arguments = meta.getArguments().stream().filter(e -> !Arrays.asList(
                AttrConsts.ID, AttrConsts.REFID,
                AttrConsts.RESULT, ParamsConsts.RETURN
        ).contains(e)).collect(Collectors.toList());

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("rawArgs", args);
        for (int i = 0; i < args.length; i++) {
            params.put("arg" + i, args[i]);
        }

        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            String name = parameter.getName();

            params.put(name, arg);

            if (i < arguments.size()) {
                String argName = arguments.get(i);
                params.put(argName, arg);
            }

            ProcedureParam ann = parameter.getDeclaredAnnotation(ProcedureParam.class);
            if (ann != null) {
                String str = ann.value();
                if (str != null && !str.isEmpty()) {
                    params.put(str, arg);
                }
            }

        }

        Map<String, Object> resMap = executor.call(callId, params);

        Map<String, Object> retMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : resMap.entrySet()) {
            String key = entry.getKey();
            if (ParamsConsts.KEEP_NAME_SET.contains(key)) {
                continue;
            }
            retMap.put(key, entry.getValue());
        }

        Class<?> returnType = method.getReturnType();
        if (void.class.isAssignableFrom(returnType)
                || Void.class.isAssignableFrom(returnType)) {
            return null;
        }

        if (Map.class.isAssignableFrom(returnType)) {
            return retMap;
        }

        if (resMap.containsKey(ParamsConsts.RETURN)) {
            Object retObj = resMap.get(ParamsConsts.RETURN);
            if (TypeOf.isBaseType(returnType)) {
                return ObjectConvertor.tryConvertAsType(retObj, returnType);
            }
            return ReflectResolver.copyVirtualWeak(retObj, ReflectResolver.getInstance(returnType));
        }

        return ReflectResolver.copyVirtualWeak(retMap, ReflectResolver.getInstance(returnType));
    }
}
