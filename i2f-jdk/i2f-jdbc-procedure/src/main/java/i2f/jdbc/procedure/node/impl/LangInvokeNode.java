package i2f.jdbc.procedure.node.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.basic.AbstractExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.reflect.ReflectResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangInvokeNode extends AbstractExecutorNode {
    public static final String TAG_NAME = "lang-invoke";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }

    @Override
    public void reportGrammar(XmlNode node, Consumer<String> warnPoster) {
        String method = node.getTagAttrMap().get(AttrConsts.METHOD);
        if(method==null || method.isEmpty()){
            warnPoster.accept(TAG_NAME+" missing attribute "+AttrConsts.METHOD);
        }
    }

    @Override
    public void execInner(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String fullMethodName = node.getTagAttrMap().get(AttrConsts.METHOD);
        String targetScript = node.getTagAttrMap().get(AttrConsts.TARGET);
        String result = node.getTagAttrMap().get(AttrConsts.RESULT);

        List<Map.Entry<Integer, String>> args = new ArrayList<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(AttrConsts.ARG)) {
                continue;
            }
            String idxStr = key.substring(AttrConsts.ARG.length());
            int idx = Integer.parseInt(idxStr);
            args.add(new AbstractMap.SimpleEntry<>(idx, entry.getValue()));
        }

        args.sort((o1, o2) -> Integer.compare(o1.getKey(), o2.getKey()));

        int idx = fullMethodName.lastIndexOf(".");
        String className = "";
        String methodName = fullMethodName;
        if (idx >= 0) {
            className = fullMethodName.substring(0, idx);
            methodName = fullMethodName.substring(idx + 1);
        }

        List<Object> callArgs = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            Map.Entry<Integer, String> argEntry = args.get(i);
            String argScript = argEntry.getValue();
            Object evalObj = null;
            try {
                evalObj = executor.attrValue(AttrConsts.ARG + argEntry.getKey(), FeatureConsts.VISIT, node, context);
            } catch (Exception e) {
            }
            if (evalObj == null) {
                evalObj = argScript;
            }
            callArgs.add(evalObj);
        }


        if ("new".equals(methodName)) {
            if (Map.class.getSimpleName().equals(className)) {
                className = HashMap.class.getName();
            } else if (List.class.getSimpleName().equals(className)) {
                className = ArrayList.class.getName();
            } else if (Collection.class.getSimpleName().equals(className)) {
                className = ArrayList.class.getName();
            } else if (Set.class.getSimpleName().equals(className)) {
                className = HashSet.class.getName();
            }
            Class<?> clazz = executor.loadClass(className);
            Constructor<?> constructor = ReflectResolver.matchExecConstructor(clazz,callArgs);

            if (constructor == null) {
                throw new IllegalArgumentException("not found constructor : " + fullMethodName + " with args count " + args.size());
            }

            try {
                Object res = ReflectResolver.execConstructor(constructor,callArgs);

                if (result != null && !result.isEmpty()) {
                    res = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                    executor.setParamsObject(context.getParams(), result, res);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        } else {
            Class<?> clazz = null;
            Object invokeObject = null;
            if (targetScript != null && !targetScript.isEmpty()) {
                invokeObject = executor.visit(targetScript, context.getParams());
            }
            if (invokeObject != null) {
                clazz = invokeObject.getClass();
            }
            if (className != null && !className.isEmpty()) {
                clazz = executor.loadClass(className);
            }
            Method method = null;
            if (targetScript == null || targetScript.isEmpty()) {
                List<Method> list= ContextHolder.INVOKE_METHOD_MAP.get(methodName);
                if(list!=null && !list.isEmpty()) {
                    method = ReflectResolver.matchExecutable(list, callArgs);
                    if (clazz == null) {
                        clazz = method.getDeclaringClass();
                    }
                }
            }

            if (clazz == null) {
                throw new IllegalArgumentException("cannot found class by method : " + fullMethodName);
            }

            if(method==null){
                method= ReflectResolver.matchExecMethod(clazz,methodName,callArgs);
            }

            if (method == null) {
                throw new IllegalArgumentException("not found method : " + fullMethodName + " with args count " + args.size());
            }

            try {

                Object res = ReflectResolver.execMethod(invokeObject,method,callArgs);

                if (result != null && !result.isEmpty()) {
                    res = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                    executor.setParamsObject(context.getParams(), result, res);
                }
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

    }

}
