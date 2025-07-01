package i2f.lambda.inflater;


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
    public static final String LAMBDA_SERIALIZED_METHOD_NAME = "writeReplace";

    private static LruMap<String, Optional<Field>> fastFieldMap = new LruMap<>(8192);

    @FunctionalInterface
    public interface IGetter<R, V> extends Serializable {
        R apply(V v) throws Throwable;
    }

    @FunctionalInterface
    public interface ISetter<T, V> extends Serializable {
        void accept(T obj, V val) throws Throwable;
    }

    @FunctionalInterface
    public interface IBuilder<R, T, V> extends Serializable {
        R apply(T obj, V val) throws Throwable;
    }

    public static <R, V> Field getField(IGetter<R, V> getter) {
        return fastSerializedLambdaFieldNullable(getter);
    }

    public static <T, V> Field getField(ISetter<T, V> setter) {
        return fastSerializedLambdaFieldNullable(setter);
    }

    public static <R, T, V> Field getField(IBuilder<R, T, V> builder) {
        return fastSerializedLambdaFieldNullable(builder);
    }

    public static Field fastSerializedLambdaFieldNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        if (lambda == null) {
            return null;
        }
        String cacheKey = lambda.getImplClass() + "#" + lambda.getImplMethodName();
        Optional<Field> optional = fastFieldMap.get(cacheKey);
        if (optional != null) {
            return optional.orElse(null);
        }
        Field field = parseSerializedLambdaFieldNullable(lambda);
        fastFieldMap.put(cacheKey, Optional.ofNullable(field));
        return field;
    }

    public static Field getSerializedLambdaFieldNullable(Object obj) {
        SerializedLambda lambda = getSerializedLambdaNullable(obj);
        return parseSerializedLambdaFieldNullable(lambda);
    }

    public static Field parseSerializedLambdaFieldNullable(SerializedLambda lambda) {
        if (lambda == null) {
            return null;
        }
        String clazzName = ReflectResolver.path2ClassName(lambda.getImplClass());
        String methodName = lambda.getImplMethodName();
        Class<?> clazz = ReflectResolver.loadClass(clazzName);
        if (clazz == null) {
            return null;
        }
        String fieldName = ReflectResolver.fieldNameByMethodName(methodName);
        Set<Field> set = ReflectResolver.getFields(clazz, (field) -> {
            if (field.getName().equals(fieldName)) {
                return true;
            }
            return false;
        }, true).keySet();
        if (!set.isEmpty()) {
            return set.iterator().next();
        }
        return null;
    }

    private static LruMap<String, Optional<Method>> fastMethodMap = new LruMap<>(8192);

    public static <R, V> Method getMethod(IGetter<R, V> getter) {
        return fastSerializedLambdaMethodNullable(getter);
    }

    public static <T, V> Method getMethod(ISetter<T, V> setter) {
        return fastSerializedLambdaMethodNullable(setter);
    }

    public static <R, T, V> Method getMethod(IBuilder<R, T, V> builder) {
        return fastSerializedLambdaMethodNullable(builder);
    }

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
        String clazzName = ReflectResolver.path2ClassName(lambda.getImplClass());
        String methodName = lambda.getImplMethodName();
        String methodSignature = lambda.getImplMethodSignature();
        Class<?> clazz = ReflectResolver.loadClass(clazzName);
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
        String clazzName = ReflectResolver.path2ClassName(lambda.getImplClass());
        return ReflectResolver.loadClass(clazzName);
    }


    public static SerializedLambda getSerializedLambdaNullable(Object obj) {
        if (!(obj instanceof Serializable)) {
            return null;
        }
        return getSerializedLambdaNullable((Serializable) obj);
    }

    public static SerializedLambda getSerializedLambdaNullable(Serializable fn) {
        try {
            return getSerializedLambda(fn);
        } catch (Exception e) {

        }
        return null;
    }

    public static SerializedLambda getSerializedLambdaNoExcept(Serializable fn) {
        try {
            return getSerializedLambda(fn);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }

    /**
     * 关键在于这个方法
     */
    public static SerializedLambda getSerializedLambda(Serializable fn) throws Exception {
        Method method = fn.getClass().getDeclaredMethod(LAMBDA_SERIALIZED_METHOD_NAME);
        method.setAccessible(true);
        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
        return lambda;
    }
}
