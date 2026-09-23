package i2f.lambda.inflater;


import i2f.lambda.core.Lambda;
import i2f.lru.LruMap;
import i2f.reflect.ReflectResolver;
import i2f.reflect.ReflectSignature;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/3/29 16:38
 * @desc 解析lambda, 前提是对应的lambda的functional-interface实现了serializable可序列化接口
 * 否则lambda不可解析
 */
public class LambdaInflater {

    public static Field fastSerializedLambdaFieldNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if (lambda == null) {
            return null;
        }
        return Lambda.ofField(lambda);
    }

    public static Field getSerializedLambdaFieldNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        return parseSerializedLambdaFieldNullable(lambda);
    }

    public static Field parseSerializedLambdaFieldNullable(SerializedLambda lambda) {
        return Lambda.ofField(lambda);
    }

    private static LruMap<String, Optional<Method>> fastMethodMap = new LruMap<>(8192);

    public static Method fastSerializedLambdaMethodNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if (lambda == null) {
            return null;
        }
        String cacheKey = lambda.getImplClass() + "#" + lambda.getImplMethodName();
        Optional<Method> optional = fastMethodMap.get(cacheKey);
        if (optional != null) {
            return optional.orElse(null);
        }
        Method method = parseSerializedLambdaMethodNullable(lambda);
        fastMethodMap.put(cacheKey, Optional.ofNullable(method));
        return method;
    }

    public static Method getSerializedLambdaMethodNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        return parseSerializedLambdaMethodNullable(lambda);
    }

    public static Method parseSerializedLambdaMethodNullable(SerializedLambda lambda) {
        if (lambda == null) {
            return null;
        }
        String methodName = lambda.getImplMethodName();
        String methodSignature = lambda.getImplMethodSignature();
        Class<?> clazz = Lambda.ofClass(lambda);
        if (clazz == null) {
            return null;
        }
        Set<Method> set = ReflectResolver.getMethods(clazz, (method) -> {
            if (method.getName().equals(methodName)) {
                String sign = ReflectSignature.sign(method);
                if (sign.equals(methodSignature)) {
                    return true;
                }
            }
            return false;
        }, true).keySet();
        if (!set.isEmpty()) {
            return set.iterator().next();
        }
        return null;
    }

    public static Class<?> getSerializedLambdaClassNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if (lambda == null) {
            return null;
        }
        return Lambda.ofClass(lambda);
    }


    public static SerializedLambda getSerializedLambdaNullable(Object obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        return Lambda.getSerializedLambdaNullable((Serializable) obj);
    }
}
