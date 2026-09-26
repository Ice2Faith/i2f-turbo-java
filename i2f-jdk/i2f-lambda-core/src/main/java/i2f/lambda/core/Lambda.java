package i2f.lambda.core;

import i2f.lambda.core.func.IBuilder;
import i2f.lambda.core.func.IGetter;
import i2f.lambda.core.func.ISetter;
import i2f.lru.LruMap;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2026/7/3 15:56
 * @desc 本类基于 java 的 lambda 表达式处理类方法引用/实例方法引用，解析获取其引用的字段/方法/类
 * 本类主要针对getter/setter类型的方法处理
 * 不针对其他类型的方法引用处理
 * 保持功能最小化
 * 因为，getter/setter是最常用的方式
 * ------
 * 解析lambda, 前提是对应的lambda的functional-interface实现了serializable可序列化接口
 * 否则lambda不可解析
 */
public class Lambda {

    public static <R, V> Field getField(IGetter<R, V> getter) {
        return ofField(getter);
    }

    public static <T, V> Field getField(ISetter<T, V> setter) {
        return ofField(setter);
    }

    public static <R, T, V> Field getField(IBuilder<R, T, V> builder) {
        return ofField(builder);
    }

    public static <R, V> Method getGetter(IGetter<R, V> getter) {
        return ofGetter(getter);
    }

    public static <T, V> Method getSetter(ISetter<T, V> setter) {
        return ofSetter(setter);
    }

    public static <R, T, V> Method getSetter(IBuilder<R, T, V> builder) {
        return ofSetter(builder);
    }

    // //////////////////////////////////////////////////////
    public static Class<?> ofClass(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofClass(lambda);
    }

    public static Class<?> ofClass(SerializedLambda lambda) {
        String className = ofClassName(lambda);
        return findClass(className);
    }

    public static Method ofGetter(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofGetter(lambda);
    }

    public static Method ofGetter(SerializedLambda lambda) {
        Class<?> clazz = ofClass(lambda);
        String fieldName = ofFieldName(lambda);
        return getGetter(clazz, fieldName);
    }

    public static Method ofSetter(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofSetter(lambda);
    }

    public static Method ofSetter(SerializedLambda lambda) {
        Class<?> clazz = ofClass(lambda);
        String fieldName = ofFieldName(lambda);
        return getSetter(clazz, fieldName);
    }

    public static Field ofField(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofField(lambda);
    }

    public static Field ofField(SerializedLambda lambda) {
        Class<?> clazz = ofClass(lambda);
        String fieldName = ofFieldName(lambda);
        return getField(clazz, fieldName);
    }

    public static Method ofGetter(Class<?> bindClass, Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofGetter(bindClass, lambda);
    }

    public static Method ofGetter(Class<?> bindClass, SerializedLambda lambda) {
        String fieldName = ofFieldName(lambda);
        return getGetter(bindClass, fieldName);
    }

    public static Method ofSetter(Class<?> bindClass, Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofSetter(bindClass, lambda);
    }

    public static Method ofSetter(Class<?> bindClass, SerializedLambda lambda) {
        String fieldName = ofFieldName(lambda);
        return getSetter(bindClass, fieldName);
    }

    public static Field ofField(Class<?> bindClass, Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofField(bindClass, lambda);
    }

    public static Field ofField(Class<?> bindClass, SerializedLambda lambda) {
        String fieldName = ofFieldName(lambda);
        return getField(bindClass, fieldName);
    }

    // /////////////////////////////////////////////////////////////////////////
    public static String ofClassName(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofClassName(lambda);
    }

    public static String ofClassName(SerializedLambda lambda) {
        String implClassName = lambda.getImplClass();
        return extractClassName(implClassName);
    }

    public static String ofMethodName(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofMethodName(lambda);
    }

    public static String ofMethodName(SerializedLambda lambda) {
        return lambda.getImplMethodName();
    }

    public static String ofFieldName(Serializable serializable) {
        SerializedLambda lambda = getSerializedLambdaNullable(serializable);
        if (lambda == null) {
            return null;
        }
        return ofFieldName(lambda);
    }

    public static String ofFieldName(SerializedLambda lambda) {
        String getterMethodName = lambda.getImplMethodName();
        return extractFieldName(getterMethodName);
    }

    // /////////////////////////////////////////////////////////////////////////
    protected static LruMap<String, String> CACHE_EXTRACT_CLASS_NAME = new LruMap<>(2048);

    public static String extractClassName(String implClassName) {
        String ret = CACHE_EXTRACT_CLASS_NAME.get(implClassName);
        if (ret != null) {
            return ret;
        }
        String className = implClassName;
        if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - ".class".length());
        }
        className = className.replace("\\", "/");
        className = className.replace("/", ".");
        if (className.endsWith(".")) {
            className = className.substring(0, className.length() - 1);
        }
        if (className.startsWith(".")) {
            className = className.substring(1);
        }
        CACHE_EXTRACT_CLASS_NAME.put(implClassName, className);
        return className;
    }

    protected static LruMap<String, String> CACHE_EXTRACT_FIELD_NAME = new LruMap<>(2048);

    public static String extractFieldName(String methodName) {
        String ret = CACHE_EXTRACT_FIELD_NAME.get(methodName);
        if (ret != null) {
            return ret;
        }
        String fieldName = methodName;
        String[] getterPrefixes = {"get", "set", "is", "has", "enable", "with", "build"};
        for (String prefix : getterPrefixes) {
            if (fieldName.startsWith(prefix)) {
                fieldName = fieldName.substring(prefix.length());
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                break;
            }
        }
        CACHE_EXTRACT_FIELD_NAME.put(methodName, fieldName);
        return fieldName;
    }
    // /////////////////////////////////////////////////////////////////////////

    protected static LruMap<String, Optional<Class<?>>> CACHE_CLASS = new LruMap<>(2048);

    public static Class<?> findClass(String className) {
        Optional<Class<?>> ret = CACHE_CLASS.get(className);
        if (ret != null) {
            return ret.orElse(null);
        }
        Class<?> clazz = null;
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
            } catch (Exception e) {

            }
        }
        if (clazz == null) {
            try {
                clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            } catch (Exception e) {

            }
        }
        CACHE_CLASS.put(className, Optional.ofNullable(clazz));
        return clazz;
    }

    // /////////////////////////////////////////////////////////////////////////
    protected static LruMap<String, Optional<Method>> CACHE_SETTER = new LruMap<>(4096);

    public static Method getSetter(Class<?> clazz, String fieldName) {
        String cacheKey = clazz + "#" + fieldName;
        Optional<Method> ret = CACHE_SETTER.get(cacheKey);
        if (ret != null) {
            return ret.orElse(null);
        }
        String pascalFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String[] setterPrefixes = {"set", "with", "enable", "with", "build"};
        Method[] methods = clazz.getMethods();
        Method setter = null;
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                continue;
            }
            if (method.getName().equals(fieldName)) {
                setter = method;
                break;
            }
            for (String prefix : setterPrefixes) {
                String searchName = prefix + pascalFieldName;
                if (method.getName().equals(searchName)) {
                    setter = method;
                    break;
                }
            }
        }
        CACHE_SETTER.put(cacheKey, Optional.ofNullable(setter));
        return setter;
    }

    protected static LruMap<String, Optional<Method>> CACHE_GETTER = new LruMap<>(4096);

    public static Method getGetter(Class<?> clazz, String fieldName) {
        String cacheKey = clazz + "#" + fieldName;
        Optional<Method> ret = CACHE_GETTER.get(cacheKey);
        if (ret != null) {
            return ret.orElse(null);
        }
        String pascalFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String[] setterPrefixes = {"get", "is", "has", "enable"};
        Method[] methods = clazz.getMethods();
        Method getter = null;
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getParameterCount() != 0) {
                continue;
            }
            if (Void.class.equals(method.getReturnType())) {
                continue;
            }
            if (method.getName().equals(fieldName)) {
                getter = method;
                break;
            }
            for (String prefix : setterPrefixes) {
                String searchName = prefix + pascalFieldName;
                if (method.getName().equals(searchName)) {
                    getter = method;
                    break;
                }
            }
        }
        CACHE_GETTER.put(cacheKey, Optional.ofNullable(getter));
        return getter;
    }

    // /////////////////////////////////////////////////////////////////////////
    protected static LruMap<String, Optional<Field>> CACHE_FIELD = new LruMap<>(4096);

    public static Field getField(Class<?> clazz, String fieldName) {
        String cacheKey = clazz + "#" + fieldName;
        Optional<Field> ret = CACHE_FIELD.get(cacheKey);
        if (ret != null) {
            return ret.orElse(null);
        }
        Field f = findField(clazz, field -> {
            return field.getName().equals(fieldName);
        });
        CACHE_FIELD.put(cacheKey, Optional.ofNullable(f));
        return f;
    }

    public static Field findField(Class<?> clazz, Predicate<Field> filter) {
        AtomicReference<Field> ref = new AtomicReference<>();
        walkField(clazz, field -> {
            if (filter.test(field)) {
                ref.set(field);
                return false;
            }
            return true;
        });
        return ref.get();
    }

    public static void walkField(Class<?> clazz, Predicate<Field> continuer) {
        if (clazz == null) {
            return;
        }
        try {
            Field[] fields = clazz.getFields();
            if (fields != null) {
                for (Field item : fields) {
                    boolean ok = continuer.test(item);
                    if (!ok) {
                        return;
                    }
                }
            }
        } catch (Exception e) {

        }
        try {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null) {
                for (Field item : fields) {
                    boolean ok = continuer.test(item);
                    if (!ok) {
                        return;
                    }
                }
            }
        } catch (Exception e) {

        }
        if (Object.class.equals(clazz)) {
            return;
        }
        walkField(clazz.getSuperclass(), continuer);
    }

    // /////////////////////////////////////////////////////////////////////////
    public static final String LAMBDA_SERIALIZED_METHOD_NAME = "writeReplace";

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
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(true);
        }
        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
        return lambda;
    }
}
