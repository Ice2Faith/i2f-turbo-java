package i2f.jdbc.procedure.node.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangInvokeNode implements ExecutorNode {
    @Override
    public boolean support(XmlNode node) {
        if (!"element".equals(node.getNodeType())) {
            return false;
        }
        return "lang-invoke".equals(node.getTagName());
    }

    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
        String fullMethodName = node.getTagAttrMap().get("method");
        String targetScript = node.getTagAttrMap().get("target");
        String result = node.getTagAttrMap().get("result");

        List<Map.Entry<Integer, String>> args = new ArrayList<>();
        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith("arg")) {
                continue;
            }
            String idxStr = key.substring("arg".length());
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


        if ("new".equals(methodName)) {
            Class<?> clazz = executor.loadClass(className);
            Constructor<?> constructor = null;
            if (constructor == null) {
                Constructor[] constructors = clazz.getConstructors();
                for (Constructor item : constructors) {
                    if (item.getParameterCount() == args.size()) {
                        constructor = item;
                        break;
                    }

                }
            }
            if (constructor == null) {
                Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
                for (Constructor item : declaredConstructors) {
                    if (item.getParameterCount() == args.size()) {
                        constructor = item;
                        break;
                    }

                }
            }

            if (constructor == null) {
                throw new IllegalArgumentException("not found constructor : " + fullMethodName + " with args count " + args.size());
            }

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] invokeArgs = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = parameterTypes[i];
                Map.Entry<Integer, String> argEntry = args.get(i);
                String argScript = argEntry.getValue();
                Object evalObj = null;
                try {
                    evalObj = executor.attrValue("arg" + argEntry.getKey(), "visit", node, context);
                } catch (Exception e) {
                }
                if (evalObj == null) {
                    evalObj = argScript;
                }
                Object val = ObjectConvertor.tryConvertAsType(evalObj, paramType);
                invokeArgs[i] = val;
            }

            try {
                constructor.setAccessible(true);
                Object res = constructor.newInstance(invokeArgs);

                if (result != null && !result.isEmpty()) {
                    res = executor.resultValue(res, node.getAttrFeatureMap().get("result"), node, context);
                    executor.setParamsObject(context.getParams(), result, res);
                }
            } catch (ReflectiveOperationException e) {
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
            if (clazz == null) {
                throw new IllegalArgumentException("cannot found class by method : " + fullMethodName);
            }
            Method method = null;
            if (method == null) {
                Method[] methods = clazz.getMethods();
                for (Method item : methods) {
                    if (item.getName().equals(methodName)) {
                        if (item.getParameterCount() == args.size()) {
                            method = item;
                            break;
                        }
                    }
                }
            }

            if (method == null) {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method item : declaredMethods) {
                    if (item.getName().equals(methodName)) {
                        if (item.getParameterCount() == args.size()) {
                            method = item;
                            break;
                        }
                    }
                }
            }

            if (method == null) {
                throw new IllegalArgumentException("not found method : " + fullMethodName + " with args count " + args.size());
            }


            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] invokeArgs = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> paramType = parameterTypes[i];
                Map.Entry<Integer, String> argEntry = args.get(i);
                String argScript = argEntry.getValue();
                Object evalObj = null;
                try {
                    evalObj = executor.attrValue("arg" + argEntry.getKey(), "visit", node, context);
                } catch (Exception e) {
                }
                if (evalObj == null) {
                    evalObj = argScript;
                }
                Object val = ObjectConvertor.tryConvertAsType(evalObj, paramType);
                invokeArgs[i] = val;
            }

            try {
                method.setAccessible(true);
                if (!Modifier.isStatic(method.getModifiers())) {
                    if (invokeObject == null) {
                        invokeObject = method.getDeclaringClass().newInstance();
                    }
                }

                Object res = method.invoke(invokeObject, invokeArgs);

                if (result != null && !result.isEmpty()) {
                    res = executor.resultValue(res, node.getAttrFeatureMap().get("result"), node, context);
                    executor.setParamsObject(context.getParams(), result, res);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

    }
}
