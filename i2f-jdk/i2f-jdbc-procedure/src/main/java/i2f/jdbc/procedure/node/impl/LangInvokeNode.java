package i2f.jdbc.procedure.node.impl;

import i2f.convert.obj.ObjectConvertor;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.context.ContextHolder;
import i2f.jdbc.procedure.context.ExecuteContext;
import i2f.jdbc.procedure.executor.JdbcProcedureExecutor;
import i2f.jdbc.procedure.node.ExecutorNode;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/1/20 14:07
 */
public class LangInvokeNode implements ExecutorNode {
    public static final String TAG_NAME = "lang-invoke";

    @Override
    public boolean support(XmlNode node) {
        if (!XmlNode.NODE_ELEMENT.equals(node.getNodeType())) {
            return false;
        }
        return TAG_NAME.equals(node.getTagName());
    }


    @Override
    public void exec(XmlNode node, ExecuteContext context, JdbcProcedureExecutor executor) {
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
            Constructor<?> constructor = null;
            Constructor<?> constructorArgs = null;
            if (constructor == null || constructorArgs == null) {
                Constructor[] constructors = clazz.getConstructors();
                for (Constructor item : constructors) {
                    if (item.getParameterCount() == args.size()) {
                        if (constructor == null) {
                            constructor = item;
                        }
                    }
                    if (args.size() > item.getParameterCount()) {
                        if (item.getParameterCount() > 0) {
                            if (item.getParameterTypes()[item.getParameterCount() - 1].isArray()) {
                                if (constructorArgs == null) {
                                    constructorArgs = item;
                                }
                            }
                        }
                    }
                }
            }
            if (constructor == null || constructorArgs == null) {
                Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
                for (Constructor item : declaredConstructors) {
                    if (item.getParameterCount() == args.size()) {
                        if (constructor == null) {
                        constructor = item;
                        }
                    }
                    if (args.size() > item.getParameterCount()) {
                        if (item.getParameterCount() > 0) {
                            if (item.getParameterTypes()[item.getParameterCount() - 1].isArray()) {
                                if (constructorArgs == null) {
                                    constructorArgs = item;
                                }
                            }
                        }
                    }
                }
            }

            if (constructor == null) {
                constructor = constructorArgs;
            }

            if (constructor == null) {
                throw new IllegalArgumentException("not found constructor : " + fullMethodName + " with args count " + args.size());
            }

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] invokeArgs = new Object[parameterTypes.length];

            for (int i = 0; i < args.size(); i++) {
                Class<?> paramType = null;
                if (i < invokeArgs.length - 1) {
                    paramType = parameterTypes[i];
                } else {
                    if (parameterTypes[parameterTypes.length - 1].isArray()) {
                        paramType = parameterTypes[parameterTypes.length - 1].getComponentType();
                    } else {
                        paramType = parameterTypes[parameterTypes.length - 1];
                    }
                }
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
                Object val = ObjectConvertor.tryConvertAsType(evalObj, paramType);
                if (i < parameterTypes.length - 1) {
                    invokeArgs[i] = val;
                } else {
                    if (parameterTypes[parameterTypes.length - 1].isArray()) {
                        if (invokeArgs[parameterTypes.length - 1] == null) {
                            invokeArgs[parameterTypes.length - 1] = Array.newInstance(paramType, args.size() - parameterTypes.length + 1);
                        }
                        Array.set(invokeArgs[parameterTypes.length - 1], i - parameterTypes.length + 1, val);
                    } else {
                        invokeArgs[i] = val;
                    }
                }
            }


            try {
                constructor.setAccessible(true);
                Object res = constructor.newInstance(invokeArgs);

                if (result != null && !result.isEmpty()) {
                    res = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
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
            Method method = null;
            Method methodArgs = null;
            if(targetScript==null || targetScript.isEmpty()){
                method= ContextHolder.INVOKE_METHOD_MAP.get(methodName);
                if(clazz==null){
                    clazz=method.getDeclaringClass();
                }
            }

            if (clazz == null) {
                throw new IllegalArgumentException("cannot found class by method : " + fullMethodName);
            }

            if (method == null || methodArgs == null) {
                Method[] methods = clazz.getMethods();
                for (Method item : methods) {
                    if (item.getName().equals(methodName)) {
                        if (item.getParameterCount() == args.size()) {
                            if (method == null) {
                                method = item;
                            }
                        }
                        if (args.size() > item.getParameterCount()) {
                            if (item.getParameterCount() > 0) {
                                if (item.getParameterTypes()[item.getParameterCount() - 1].isArray()) {
                                    if (methodArgs == null) {
                                        methodArgs = item;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (method == null || methodArgs == null) {
                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method item : declaredMethods) {
                    if (item.getName().equals(methodName)) {
                        if (item.getParameterCount() == args.size()) {
                            if (method == null) {
                                method = item;
                            }
                        }
                        if (args.size() > item.getParameterCount()) {
                            if (item.getParameterCount() > 0) {
                                if (item.getParameterTypes()[item.getParameterCount() - 1].isArray()) {
                                    if (methodArgs == null) {
                                        methodArgs = item;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (method == null) {
                method = methodArgs;
            }

            if (method == null) {
                throw new IllegalArgumentException("not found method : " + fullMethodName + " with args count " + args.size());
            }


            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] invokeArgs = new Object[parameterTypes.length];

            for (int i = 0; i < args.size(); i++) {
                Class<?> paramType = null;
                if (i < invokeArgs.length - 1) {
                    paramType = parameterTypes[i];
                } else {
                    if (parameterTypes[parameterTypes.length - 1].isArray()) {
                        paramType = parameterTypes[parameterTypes.length - 1].getComponentType();
                    } else {
                        paramType = parameterTypes[parameterTypes.length - 1];
                    }
                }
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
                Object val = ObjectConvertor.tryConvertAsType(evalObj, paramType);
                if (i < parameterTypes.length - 1) {
                    invokeArgs[i] = val;
                } else {
                    if (parameterTypes[parameterTypes.length - 1].isArray()) {
                        if (invokeArgs[parameterTypes.length - 1] == null) {
                            invokeArgs[parameterTypes.length - 1] = Array.newInstance(paramType, args.size() - parameterTypes.length + 1);
                        }
                        Array.set(invokeArgs[parameterTypes.length - 1], i - parameterTypes.length + 1, val);
                    } else {
                        invokeArgs[i] = val;
                    }
                }
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
                    res = executor.resultValue(res, node.getAttrFeatureMap().get(AttrConsts.RESULT), node, context);
                    executor.setParamsObject(context.getParams(), result, res);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }

    }
}
