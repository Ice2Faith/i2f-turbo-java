package i2f.reflect;


import i2f.convert.obj.ObjectConvertor;
import i2f.lru.LruMap;
import i2f.reflect.virtual.VirtualField;
import i2f.reflect.virtual.VirtualGetterField;
import i2f.reflect.virtual.VirtualSetterField;
import i2f.reflect.virtual.impl.FieldGetterSetterField;
import i2f.reflect.virtual.impl.MethodGetterField;
import i2f.reflect.virtual.impl.MethodGetterSetterField;
import i2f.reflect.virtual.impl.MethodSetterField;
import i2f.typeof.TypeOf;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/2/28 16:03
 * @desc
 */
public class ReflectResolver {
    public static AtomicBoolean ENABLE_CACHE = new AtomicBoolean(true);

    public static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ReflectResolver.class.getClassLoader();
        }
        return loader;
    }

    public static String getClasspathName(String name) {
        if (name.startsWith("classpath*:")) {
            name = name.substring("classpath*:".length());
        }
        if (name.startsWith("classpath:")) {
            name = name.substring("classpath:".length());
        }
        if (name.startsWith("!")) {
            name = name.substring(1);
        }
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }

    public static Class<?> loadClassWithJdk(String className) {
        Class<?> ret = loadClass(className);

        for (String item : jdkPackages) {
            ret = loadClass(item + className);
            if (ret != null) {
                break;
            }
        }
        return ret;
    }

    public static Class<?> loadClass(String className) {
        if (className == null) {
            return null;
        }
        Class<?> clazz = null;
        if (clazz == null) {
            try {
                ClassLoader classLoader = getClassLoader();
                clazz = classLoader.loadClass(className);
            } catch (Throwable e) {
            }
        }
        if (clazz == null) {
            try {
                clazz = Class.forName(className);
            } catch (Throwable e) {
            }
        }
        return clazz;
    }

    public static String path2ClassName(String path) {
        if (path == null) {
            return null;
        }
        String classpathName = getClasspathName(path);
        if (classpathName.endsWith(".class")) {
            classpathName = classpathName.substring(0, classpathName.length() - ".class".length());
        }
        return classpathName.replaceAll("\\/|\\\\", ".");
    }

    public static String class2Path(Class<?> clazz) {
        return className2PathFileName(clazz.getName());
    }

    public static String className2PathFileName(String className) {
        if (className == null) {
            return null;
        }
        return className.replaceAll("\\.", "/") + ".class";
    }

    /**
     * 包名或类名转路径
     *
     * @param className
     * @return
     */
    public static String className2Path(String className) {
        return className.replaceAll("\\.", File.separator);
    }

    /**
     * 包名或类名转URL的路径，也就是/分隔
     *
     * @param className
     * @return
     */
    public static String className2UrlPath(String className) {
        return className.replaceAll("\\.", "/");
    }

    /**
     * 获取上下文类加载器
     *
     * @return
     */
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    private static LruMap<String, Field> CACHE_GET_FIELD = new LruMap<>(8192);

    public static Field getField(Class<?> clazz, String fieldName) {
        String key = clazz + "##" + fieldName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_FIELD : null), key, (k) -> {
            return getField0(clazz, fieldName);
        }, v -> v);
    }

    public static Field getField0(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);
            if (field != null) {
                return field;
            }
        } catch (Exception e) {

        }
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (field != null) {
                return field;
            }
        } catch (Exception e) {

        }

        Map<Field, Class<?>> fields = getFields(clazz, (field) -> {
            return field.getName().equals(fieldName);
        }, true);
        if (fields.isEmpty()) {
            return null;
        }
        return fields.keySet().iterator().next();
    }

    public static <T extends Annotation> Map<Field, Set<T>> getFieldsWithAnnotation(Class<?> clazz, Class<T> annotationClass) {
        Map<Field, Set<T>> ret = new LinkedHashMap<>();
        Map<Field, Class<?>> fields = getFields(clazz);
        for (Field field : fields.keySet()) {
            Set<T> annotations = getAnnotations(field, annotationClass, false);
            ret.put(field, annotations);
        }
        return ret;
    }

    private static LruMap<Class<?>, Map<Field, Class<?>>> CACHE_GET_FIELDS = new LruMap<>(8192);

    public static Map<Field, Class<?>> getFields(Class<?> clazz) {
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_FIELDS : null), clazz, (k) -> getFields(k, null, null, false), LinkedHashMap::new);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Field> fieldFilter
    ) {
        return getFields(clazz, fieldFilter, false);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Field> fieldFilter,
                                                 boolean matchedOne
    ) {
        return getFields(clazz, null, fieldFilter, matchedOne);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Class<?>> superFilter,
                                                 Predicate<Field> fieldFilter
    ) {
        return getFields(clazz, superFilter, fieldFilter, false);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Class<?>> superFilter,
                                                 Predicate<Field> fieldFilter,
                                                 boolean matchedOne
    ) {
        Map<Field, Class<?>> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        Set<Field> arr = findField(clazz, fieldFilter, matchedOne);
        for (Field item : arr) {
            ret.put(item, clazz);
            if (matchedOne) {
                return ret;
            }
        }
        if (Object.class.equals(clazz)) {
            return ret;
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superFilter == null || superFilter.test(superclass)) {
            Map<Field, Class<?>> next = getFields(superclass, superFilter, fieldFilter, matchedOne);
            ret.putAll(next);
        }

        return ret;
    }

    private static LruMap<String, Method> CACHE_GET_METHOD = new LruMap<>(8192);

    public static Method getMethod(Class<?> clazz, String methodName) {
        String key = clazz + "#" + methodName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_METHOD : null), key, (k) -> {
            return getMethod0(clazz, methodName);
        }, v -> v);
    }

    public static Method getMethod0(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            if (method != null) {
                return method;
            }
        } catch (Exception e) {

        }
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            if (method != null) {
                return method;
            }
        } catch (Exception e) {

        }
        Map<Method, Class<?>> methods = getMethods(clazz, (method) -> {
            return method.getName().equals(methodName);
        }, true);
        if (methods.isEmpty()) {
            return null;
        }
        return methods.keySet().iterator().next();
    }

    private static LruMap<Class<?>, Map<Method, Class<?>>> CACHE_GET_METHODS = new LruMap<>(8192);

    public static Map<Method, Class<?>> getMethods(Class<?> clazz) {
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_METHODS : null), clazz, (k) -> {
            return getMethods(k, null, null, false);
        }, LinkedHashMap::new);
    }

    public static Map<Method, Class<?>> getMethods(Class<?> clazz,
                                                   Predicate<Method> methodFilter
    ) {
        return getMethods(clazz, methodFilter, false);
    }

    public static Map<Method, Class<?>> getMethods(Class<?> clazz,
                                                   Predicate<Method> methodFilter,
                                                   boolean matchedOne
    ) {
        return getMethods(clazz, null, methodFilter, matchedOne);
    }

    public static Map<Method, Class<?>> getMethods(Class<?> clazz,
                                                   Predicate<Class<?>> superFilter,
                                                   Predicate<Method> methodFilter
    ) {
        return getMethods(clazz, superFilter, methodFilter, false);
    }

    private static LruMap<String, Map<String, VirtualField>> CACHE_GET_VIRTUAL_FIELDS = new LruMap<>(8192);

    public static Set<Method> findMethod(Class<?> clazz, Predicate<Method> filter) {
        return findMethod(clazz, filter, false);
    }

    public static Set<Method> findMethod(Class<?> clazz, Predicate<Method> filter, boolean matchedOne) {
        Set<Method> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        mergeArray(ret, filter, matchedOne, clazz.getDeclaredMethods(), clazz.getMethods());
        return ret;
    }

    public static <T> Set<Constructor<T>> getConstructors(Class<?> clazz) {
        return getConstructors(clazz, null);
    }

    public static <T> Set<Constructor<T>> getConstructors(Class<?> clazz, Predicate<Constructor<T>> constructorFilter) {
        Set<Constructor<T>> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        if (declaredConstructors != null) {
            for (Constructor<?> item : declaredConstructors) {
                Constructor<T> elem = (Constructor<T>) item;
                if (constructorFilter == null || constructorFilter.test(elem)) {
                    ret.add(elem);
                }
            }
        }
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors != null) {
            for (Constructor<?> item : constructors) {
                Constructor<T> elem = (Constructor<T>) item;
                if (constructorFilter == null || constructorFilter.test(elem)) {
                    ret.add(elem);
                }
            }
        }
        return ret;
    }

    public static Set<Class<?>> getSuperClasses(Class<?> clazz) {
        return getSuperClasses(clazz, true, null);
    }

    public static Set<Class<?>> getSuperClasses(Class<?> clazz, boolean withInterfaces) {
        return getSuperClasses(clazz, withInterfaces, null);
    }

    public static Set<Class<?>> getSuperClasses(Class<?> clazz, boolean withInterfaces, Predicate<Class<?>> superFilter) {
        Set<Class<?>> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        ret.add(clazz);
        if (Object.class.equals(clazz)) {
            return ret;
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superFilter == null || superFilter.test(superclass)) {
            Set<Class<?>> next = getSuperClasses(superclass, withInterfaces, superFilter);
            ret.addAll(next);
        }

        if (withInterfaces) {
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces != null) {
                for (Class<?> anInterface : interfaces) {
                    if (superFilter == null || superFilter.test(anInterface)) {
                        Set<Class<?>> next = getSuperClasses(anInterface, withInterfaces, superFilter);
                        ret.addAll(next);
                    }
                }
            }
        }

        return ret;
    }

    public static <T> T getInstance(Class<T> clazz, Object... args) throws IllegalArgumentException, IllegalAccessException {
        int argsLen = args.length;
        List<Class<?>> types = new ArrayList<>();
        for (Object arg : args) {
            if (arg == null) {
                types.add(null);
                continue;
            }
            types.add(arg.getClass());
        }
        Set<Constructor<T>> constructors = getConstructors(clazz, (constructor) -> {
            if (!Modifier.isPublic(constructor.getModifiers())) {
                return false;
            }
            if (constructor.getParameterCount() != argsLen) {
                return false;
            }
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < argsLen; i++) {
                Class<?> paramType = parameterTypes[i];
                Class<?> argType = types.get(i);
                if (argType == null || paramType == null) {
                    continue;
                }
                if (!TypeOf.typeOf(argType, paramType)) {
                    return false;
                }
            }
            return true;
        });
        if (constructors.isEmpty()) {
            throw new IllegalAccessException("constructor not found in class [" + clazz + "] with parameter count equals " + argsLen + " or parameter types matched.");
        }
        Constructor<T> next = constructors.iterator().next();
        T ret = null;
        boolean success = false;
        Throwable ex = null;
        try {
            ret = next.newInstance(args);
            success = true;
        } catch (Throwable e) {
            ex = e;
        }
        if (!success) {
            if (ex instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) ex;
            } else if (ex instanceof IllegalAccessException) {
                throw (IllegalAccessException) ex;
            } else {
                throw new IllegalArgumentException(ex.getMessage() + "\ncause by: " + ex.getClass().getName(), ex);
            }
        }
        return ret;
    }

    public static Set<Field> findField(Class<?> clazz, Predicate<Field> filter) {
        return findField(clazz, filter, false);
    }

    public static Set<Field> findField(Class<?> clazz, Predicate<Field> filter, boolean matchedOne) {
        Set<Field> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        mergeArray(ret, filter, matchedOne, clazz.getDeclaredFields(), clazz.getFields());
        return ret;
    }

    public static String[] getParameterNames(Method method) {
        return getParameterNames(method, null);
    }

    public static String[] getParameterNames(Method method, BiFunction<Parameter, Method, String> nameProvider) {
        Parameter[] parameters = method.getParameters();
        String[] ret = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            ret[i] = parameters[i].getName();
            if (nameProvider != null) {
                String name = nameProvider.apply(parameters[i], method);
                if (name != null && !name.isEmpty()) {
                    ret[i] = name;
                }
            }
        }
        return ret;
    }

    public static Object invokeMethod(Object ivkObj, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException {
        return invokeMethod(ivkObj, ivkObj.getClass(), methodName, args);
    }

    public static Object invokeSingletonMethod(Object ivkObj, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException {
        return invokeSingletonMethod(ivkObj, ivkObj.getClass(), methodName, args);
    }

    public static Object invokeStaticMethod(Method method, Object... args) throws IllegalArgumentException, IllegalAccessException {
        return invokeSingletonMethod(null, method.getDeclaringClass(), method.getName(), args);
    }

    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException {
        return invokeMethod(null, clazz, methodName, args);
    }

    public static Object invokeStaticSingletonMethod(Class<?> clazz, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException {
        return invokeSingletonMethod(null, clazz, methodName, args);
    }

    public static Object invokeSingletonMethod(Object ivkObj, Class<?> clazz, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException {
        Method method = getMethod(clazz, methodName);
        if (method == null) {
            throw new IllegalAccessException("method [" + methodName + "] not found in class [" + clazz + "].");
        }
        Object ret = null;
        boolean success = false;
        Throwable ex = null;
        try {
            ret = method.invoke(ivkObj, args);
            success = true;
        } catch (Throwable e) {
            ex = e;
        }
        if (!success) {
            if (ex instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) ex;
            } else if (ex instanceof IllegalAccessException) {
                throw (IllegalAccessException) ex;
            } else {
                throw new IllegalArgumentException(ex.getMessage() + "\ncause by: " + ex.getClass().getName(), ex);
            }
        }
        return ret;
    }

    public static Method matchMethodByParamTypes(Class<?> clazz, String methodName, Class<?>[] types) {
        int argsLen = types.length;
        Map<Method, Class<?>> methods = getMethods(clazz, (method) -> {
            if (!method.getName().equals(methodName)) {
                return false;
            }
            if (method.getParameterCount() != argsLen) {
                return false;
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < argsLen; i++) {
                Class<?> paramType = parameterTypes[i];
                Class<?> argType = types[i];
                if (argType == null || paramType == null) {
                    continue;
                }
                if (!TypeOf.typeOf(argType, paramType)) {
                    return false;
                }
            }
            return true;
        }, true);
        if (methods.isEmpty()) {
            return null;
        }
        Method next = methods.keySet().iterator().next();
        return next;
    }

    public static Method matchMethod(Class<?> clazz, String methodName, Object... args) {
        int argsLen = args.length;
        Class<?>[] types = new Class[argsLen];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                types[i] = null;
                continue;
            }
            types[i] = arg.getClass();
        }
        return matchMethodByParamTypes(clazz, methodName, types);
    }

    public static Object invokeMethod(Object ivkObj, Class<?> clazz, String methodName, Object... args) throws IllegalArgumentException, IllegalAccessException {
        Method method = matchMethod(clazz, methodName, args);
        if (method == null) {
            throw new IllegalAccessException("method [" + methodName + "] not found in class [" + clazz + "] with parameter count equals " + args.length + " or parameter types matched.");
        }
        return invokeMethodeDirect(ivkObj, method, args);
    }

    public static Object invokeMethodeDirect(Object ivkObj, Method method, Object... args) throws IllegalAccessException {
        Object ret = null;
        boolean success = false;
        Throwable ex = null;
        try {
            method.setAccessible(true);
            ret = method.invoke(ivkObj, args);
            success = true;
        } catch (Throwable e) {
            ex = e;
        }
        if (!success) {
            if (ex instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) ex;
            } else if (ex instanceof IllegalAccessException) {
                throw (IllegalAccessException) ex;
            } else {
                throw new IllegalArgumentException(ex.getMessage() + "\ncause by: " + ex.getClass().getName(), ex);
            }
        }
        return ret;
    }

    public static Object valueGetStatic(Class<?> clazz, String fieldName) throws IllegalArgumentException, IllegalAccessException {
        return valueGet(null, clazz, fieldName);
    }

    public static Object valueGet(Object ivkObj, String fieldName) throws IllegalArgumentException, IllegalAccessException {
        return valueGet(ivkObj, ivkObj.getClass(), fieldName);
    }

    public static Object valueGet(Object ivkObj, Class<?> clazz, String fieldName) throws IllegalArgumentException, IllegalAccessException {
        Field field = getField(clazz, fieldName);
        if (field == null) {
            throw new IllegalAccessException("field [" + fieldName + "] not found in class [" + clazz + "]");
        }
        return valueGet(ivkObj, field);
    }

    public static Object valueGetStatic(Field field) throws IllegalArgumentException, IllegalAccessException {
        return valueGet(null, field);
    }

    public static Object valueGet(Object ivkObj, Field field) throws IllegalArgumentException, IllegalAccessException {
        Method getter = getGetter(field);
        boolean success = false;
        Object ret = null;
        Throwable ex = null;
        if (!success) {
            try {
                if (getter != null) {
                    boolean ok = true;
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (ivkObj == null) {
                            if (!Modifier.isStatic(getter.getModifiers())) {
                                ok = false;
                            }
                        }
                    }
                    if (ok) {
                        ret = getter.invoke(ivkObj);
                        success = true;
                    }
                }
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (!success) {
            try {
                field.setAccessible(true);
                ret = field.get(ivkObj);
                success = true;
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (!success) {
            if (ex instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) ex;
            } else if (ex instanceof IllegalAccessException) {
                throw (IllegalAccessException) ex;
            } else {
                throw new IllegalArgumentException(ex.getMessage() + "\ncause by: " + ex.getClass().getName(), ex);
            }
        }
        return ret;
    }

    public static Object valueSetStatic(Class<?> clazz, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException {
        return valueSet(null, clazz, fieldName, value);
    }

    public static Object valueSet(Object ivkObj, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException {
        return valueSet(ivkObj, ivkObj.getClass(), fieldName, value);
    }

    public static Object valueSet(Object ivkObj, Class<?> clazz, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException {
        Field field = getField(clazz, fieldName);
        if (field == null) {
            throw new IllegalAccessException("field [" + fieldName + "] not found in class [" + clazz + "]");
        }
        return valueSet(ivkObj, field, value);
    }

    public static Object valueSetStatic(Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
        return valueSet(null, field, value);
    }

    public static Object valueSet(Object ivkObj, Field field, Object value) throws IllegalArgumentException, IllegalAccessException {
        Method setter = getSetter(field);
        boolean success = false;
        Object ret = null;
        Throwable ex = null;
        if (!success) {
            try {
                if (setter != null) {
                    boolean ok = true;
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (ivkObj == null) {
                            if (!Modifier.isStatic(setter.getModifiers())) {
                                ok = false;
                            }
                        }
                    }
                    if (ok) {
                        value = ObjectConvertor.tryConvertAsType(value, setter.getParameterTypes()[0]);
                        ret = setter.invoke(ivkObj, value);
                        success = true;
                    }
                }
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (!success) {
            try {
                field.setAccessible(true);
                ret = field.get(ivkObj);
                value = ObjectConvertor.tryConvertAsType(value, field.getType());
                field.set(ivkObj, value);
                success = true;
            } catch (Throwable e) {
                ex = e;
            }
        }
        if (!success) {
            if (ex instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) ex;
            } else if (ex instanceof IllegalAccessException) {
                throw (IllegalAccessException) ex;
            } else {
                throw new IllegalArgumentException(ex.getMessage() + "\ncause by: " + ex.getClass().getName(), ex);
            }
        }
        return ret;
    }

    public static <T> T copyWeak(Object src, T dst) {
        return copy(src, dst, null, null, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName, ReflectResolver::weakName);
    }

    public static <T> T copyWeak(Object src, T dst, Predicate<Field> fieldFilter, Predicate<String> mapFieldFilter) {
        return copy(src, dst, fieldFilter, mapFieldFilter, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName, ReflectResolver::weakName);
    }

    public static <T> T copy(Object src, T dst) {
        return copy(src, dst, null, null, null, null, null);
    }

    public static <T> T copy(Object src, T dst, Predicate<Field> fieldFilter, Predicate<String> mapFieldFilter) {
        return copy(src, dst, fieldFilter, mapFieldFilter, null, null, null);
    }

    public static <T> T copy(Object src, T dst, Predicate<Field> fieldFilter, Predicate<String> mapFieldFilter,
                             Function<Field, String> srcFieldNameMapper,
                             Function<Field, String> dstFieldNameMapper,
                             Function<String, String> srcMapNameMapper) {
        if (src == null || dst == null) {
            return dst;
        }
        if (src.getClass().isArray() && dst instanceof Collection) {
            return (T) arrObj2col(src, (Collection) dst);
        } else if (src instanceof Iterator && dst instanceof Collection) {
            return (T) col2col((Iterator) src, (Collection) dst);
        } else if (src instanceof Iterable && dst instanceof Collection) {
            return (T) col2col((Iterable) src, (Collection) dst);
        } else if (src instanceof Enumeration && dst instanceof Collection) {
            return (T) col2col((Enumeration) src, (Collection) dst);
        } else if (src instanceof Map && dst instanceof Map) {
            map2map((Map) src, (Map) dst, mapFieldFilter == null ? null : (key) -> {
                String mKey = null;
                if (key != null) {
                    if (key instanceof String) {
                        mKey = (String) key;
                    } else {
                        mKey = String.valueOf(key);
                    }
                }
                return mapFieldFilter.test(mKey);
            }, srcMapNameMapper);
        } else if (src instanceof Map) {
            map2bean((Map<String, Object>) src, dst, mapFieldFilter, srcMapNameMapper, dstFieldNameMapper);
        } else if (dst instanceof Map) {
            bean2map(src, (Map<String, Object>) dst, fieldFilter, srcFieldNameMapper);
        } else {
            beanCopy(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper);
        }
        return dst;
    }

    public static <E, C extends Collection<E>> C arrObj2col(Object arr, C col) {
        return arrObj2col(arr, col, null);
    }

    public static <E, C extends Collection<E>> C arrObj2col(Object arr, C col, Predicate<E> filter) {
        if (arr == null || col == null) {
            return col;
        }
        if (!arr.getClass().isArray()) {
            return col;
        }
        int len = Array.getLength(arr);
        for (int i = 0; i < len; i++) {
            E elem = (E) Array.get(arr, i);
            if (filter == null || filter.test(elem)) {
                col.add(elem);
            }
        }
        return col;
    }

    public static <E, C extends Collection<E>> C arr2col(E[] arr, C col) {
        return arr2col(arr, col, null);
    }

    public static <E, C extends Collection<E>> C arr2col(E[] arr, C col, Predicate<E> filter) {
        if (arr == null || col == null) {
            return col;
        }
        for (int i = 0; i < arr.length; i++) {
            E elem = arr[i];
            if (filter == null || filter.test(elem)) {
                col.add(elem);
            }
        }
        return col;
    }

    public static <E, C extends Collection<E>> C col2col(Iterable<E> iterable, C col) {
        return col2col(iterable, col, null);
    }

    public static <E, C extends Collection<E>> C col2col(Iterable<E> iterable, C col, Predicate<E> filter) {
        if (iterable == null || col == null) {
            return col;
        }
        return col2col(iterable.iterator(), col, filter);
    }

    public static <E, C extends Collection<E>> C col2col(Iterator<E> iterator, C col) {
        return col2col(iterator, col, null);
    }

    public static <E, C extends Collection<E>> C col2col(Iterator<E> iterator, C col, Predicate<E> filter) {
        if (iterator == null || col == null) {
            return col;
        }
        while (iterator.hasNext()) {
            E elem = iterator.next();
            if (filter == null || filter.test(elem)) {
                col.add(elem);
            }
        }
        return col;
    }

    public static <E, C extends Collection<E>> C col2col(Enumeration<E> enumeration, C col) {
        return col2col(enumeration, col, null);
    }

    public static <E, C extends Collection<E>> C col2col(Enumeration<E> enumeration, C col, Predicate<E> filter) {
        if (enumeration == null || col == null) {
            return col;
        }
        while (enumeration.hasMoreElements()) {
            E elem = enumeration.nextElement();
            if (filter == null || filter.test(elem)) {
                col.add(elem);
            }
        }
        return col;
    }

    public static <K, T extends Map> T map2map(Map<K, ?> src, T dst) {
        return map2map(src, dst, null, null);
    }

    public static <K, T extends Map> T map2map(Map<K, ?> src, T dst, Predicate<K> mapFieldFilter) {
        return map2map(src, dst, mapFieldFilter, null);
    }

    public static <K, T extends Map> T map2map(Map<K, ?> src, T dst, Predicate<K> mapFieldFilter,
                                               Function<String, String> srcMapNameMapper) {
        if (src == null || dst == null) {
            return dst;
        }
        for (Map.Entry<K, ?> entry : src.entrySet()) {
            K name = entry.getKey();
            boolean isTarget = false;
            if (mapFieldFilter == null || mapFieldFilter.test(name)) {
                isTarget = true;
            }
            if (name instanceof String) {
                name = (K) srcMapNameMapper.apply((String) name);
            }
            try {
                dst.put(name, entry.getValue());
            } catch (Throwable e) {

            }
        }
        return dst;
    }

    public static <T> T map2beanWeak(Map<String, Object> src, T dst) {
        return map2bean(src, dst, null, ReflectResolver::weakName, ReflectResolver::weakFieldName);
    }

    public static <T> T map2beanWeak(Map<String, Object> src, T dst, Predicate<String> fieldFilter) {
        return map2bean(src, dst, fieldFilter, ReflectResolver::weakName, ReflectResolver::weakFieldName);
    }

    public static <T> T map2bean(Map<String, Object> src, T dst) {
        return map2bean(src, dst, null, null, null);
    }

    public static <T> T map2bean(Map<String, Object> src, T dst, Predicate<String> fieldFilter) {
        return map2bean(src, dst, fieldFilter, null, null);
    }

    public static <T> T map2bean(Map<String, Object> src, T dst, Predicate<String> fieldFilter,
                                 Function<String, String> srcMapNameMapper,
                                 Function<Field, String> dstFieldNameMapper) {
        if (src == null || dst == null) {
            return dst;
        }
        Map<Field, Class<?>> dstFields = getFields(dst.getClass());
        Map<String, Field> dstNameMap = new HashMap<>();
        for (Field item : dstFields.keySet()) {
            String name = item.getName();
            if (dstFieldNameMapper != null) {
                String str = dstFieldNameMapper.apply(item);
                if (str != null) {
                    name = str;
                }
            }
            dstNameMap.put(name, item);
        }
        for (Map.Entry<String, Object> entry : src.entrySet()) {
            String name = entry.getKey();
            boolean isTarget = false;
            if (fieldFilter == null || fieldFilter.test(name)) {
                isTarget = true;
            }
            if (srcMapNameMapper != null) {
                String str = srcMapNameMapper.apply(name);
                if (str != null) {
                    name = str;
                }
            }
            Field dstField = dstNameMap.get(name);
            if (dstField == null) {
                isTarget = false;
            }
            if (!isTarget) {
                continue;
            }
            try {
                Object val = entry.getValue();
                val = ObjectConvertor.tryConvertAsType(val, dstField.getType());
                valueSet(dst, dstField, val);
            } catch (Throwable e) {

            }
        }
        return dst;
    }

    public static <M extends Map<String, Object>> M bean2map(Object src, M dst) {
        return bean2map(src, dst, null, null);
    }

    public static <M extends Map<String, Object>> M bean2map(Object src, M dst, Predicate<Field> fieldFilter) {
        return bean2map(src, dst, fieldFilter, null);
    }

    public static <M extends Map<String, Object>> M bean2map(Object src, M dst, Predicate<Field> fieldFilter,
                                                             Function<Field, String> srcFieldNameMapper) {
        if (src == null || dst == null) {
            return dst;
        }

        Map<Field, Class<?>> srcFields = getFields(src.getClass());

        for (Field srcField : srcFields.keySet()) {
            boolean isTarget = false;
            if (fieldFilter == null || fieldFilter.test(srcField)) {
                isTarget = true;
            }
            String name = srcField.getName();
            if (srcFieldNameMapper != null) {
                String str = srcFieldNameMapper.apply(srcField);
                if (str != null) {
                    name = str;
                }
            }
            if (!isTarget) {
                continue;
            }

            try {
                Object val = valueGet(src, srcField);
                dst.put(name, val);
            } catch (Throwable e) {

            }
        }

        return dst;
    }

    public static String weakName(String name) {
        if (name == null) {
            return null;
        }
        return name.replaceAll("[-_'\"`.]", "").toLowerCase();
    }

    public static String weakFieldName(Field field) {
        if (field == null) {
            return null;
        }
        return field.getName().replaceAll("[-_]", "").toLowerCase();
    }


    public static <T> T beanCopyWeak(Object src, T dst) {
        return beanCopy(src, dst, null, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName);
    }

    public static <T> T beanCopyWeak(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanCopy(src, dst, fieldFilter, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName);
    }

    public static <T> T beanCopy(Object src, T dst) {
        return beanCopy(src, dst, null, null, null, null, null, null);
    }

    public static <T> T beanCopy(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanCopy(src, dst, fieldFilter, null, null, null, null, null);
    }

    /**
     * bean属性的复制
     * 无论值src中的值是什么全部复制覆盖dst中的字段
     *
     * @param src
     * @param dst
     * @param fieldFilter
     * @param srcFieldNameMapper
     * @param dstFieldNameMapper
     * @param <T>
     * @return
     */
    public static <T> T beanCopy(Object src, T dst, Predicate<Field> fieldFilter,
                                 Function<Field, String> srcFieldNameMapper,
                                 Function<Field, String> dstFieldNameMapper) {
        return beanCopy(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper, null, null, null);
    }

    public static <T> T beanAssignWeak(Object src, T dst) {
        return beanAssign(src, dst, null, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName);
    }

    public static <T> T beanAssignWeak(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanAssign(src, dst, fieldFilter, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName);
    }

    public static <T> T beanAssign(Object src, T dst) {
        return beanAssign(src, dst, null, null, null);
    }

    public static <T> T beanAssign(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanAssign(src, dst, fieldFilter, null, null);
    }

    /**
     * bean的覆盖
     * 使用src中的所有非空属性覆盖dst中的属性
     * 当dst中的属性也非空时，也会被src覆盖
     *
     * @param src
     * @param dst
     * @param fieldFilter
     * @param srcFieldNameMapper
     * @param dstFieldNameMapper
     * @param <T>
     * @return
     */
    public static <T> T beanAssign(Object src, T dst, Predicate<Field> fieldFilter,
                                   Function<Field, String> srcFieldNameMapper,
                                   Function<Field, String> dstFieldNameMapper) {
        return beanCopy(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper, null, v -> {
            if (v == null) {
                return false;
            }
            if (v instanceof String) {
                if (((String) v).isEmpty()) {
                    return false;
                }
            }
            return true;
        }, null);
    }

    public static <T> T beanMergeWeak(Object src, T dst) {
        return beanMerge(src, dst, null, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName);
    }

    public static <T> T beanMergeWeak(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanMerge(src, dst, fieldFilter, ReflectResolver::weakFieldName, ReflectResolver::weakFieldName);
    }

    public static <T> T beanMerge(Object src, T dst) {
        return beanMerge(src, dst, null, null, null);
    }

    public static <T> T beanMerge(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanMerge(src, dst, fieldFilter, null, null);
    }

    /**
     * bean属性的合并
     * 保留dst中的非空属性，并且使用src中的非空属性补充到dst中
     * 到dst中和src中同时存在相同字段的非空时，则保留dst中的值
     *
     * @param src
     * @param dst
     * @param fieldFilter
     * @param srcFieldNameMapper
     * @param dstFieldNameMapper
     * @param <T>
     * @return
     */
    public static <T> T beanMerge(Object src, T dst, Predicate<Field> fieldFilter,
                                  Function<Field, String> srcFieldNameMapper,
                                  Function<Field, String> dstFieldNameMapper) {
        return beanCopy(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper, null, v -> {
            if (v == null) {
                return false;
            }
            if (v instanceof String) {
                if (((String) v).isEmpty()) {
                    return false;
                }
            }
            return true;
        }, v -> {
            if (v == null) {
                return true;
            }
            if (v instanceof String) {
                if (((String) v).isEmpty()) {
                    return true;
                }
            }
            return false;
        });
    }

    public static <T> T beanCopy(Object src, T dst, Predicate<Field> fieldFilter,
                                 Function<Field, String> srcFieldNameMapper,
                                 Function<Field, String> dstFieldNameMapper,
                                 Function<Object, Object> valueMapper,
                                 Predicate<Object> newValueCopyFilter,
                                 Predicate<Object> oldValueCoverFilter) {
        return beanCopy0(src, dst, fieldFilter,
                srcFieldNameMapper == null ? null : (field) -> {
                    String name = srcFieldNameMapper.apply(field);
                    if (name == null) {
                        return Collections.emptyList();
                    }
                    return Collections.singletonList(name);
                }, dstFieldNameMapper == null ? null : (field) -> {
                    String name = dstFieldNameMapper.apply(field);
                    if (name == null) {
                        return Collections.emptyList();
                    }
                    return Collections.singletonList(name);
                }, valueMapper,
                newValueCopyFilter,
                oldValueCoverFilter);
    }

    public static <T> T beanCopy0(Object src, T dst) {
        return beanCopy0(src, dst, null,
                null, null,
                null, null, null);
    }

    public static <T> T beanCopy0(Object src, T dst, Predicate<Field> fieldFilter) {
        return beanCopy0(src, dst, fieldFilter,
                null, null,
                null, null, null);
    }

    public static <T> T beanCopy0(Object src, T dst, Predicate<Field> fieldFilter,
                                  Function<Field, List<String>> srcFieldNameMapper,
                                  Function<Field, List<String>> dstFieldNameMapper) {
        return beanCopy0(src, dst, fieldFilter,
                srcFieldNameMapper, dstFieldNameMapper,
                null, null, null);
    }

    /**
     * bean的字段复制
     *
     * @param src
     * @param dst
     * @param fieldFilter         指定src中的哪些字段将会被用于copy，允许null则全字段
     * @param srcFieldNameMapper  指定src中的每个字段可以映射为哪些字段名以和dst的字段进行匹配，也就是说允许一个字段有多个别名，允许null则默认字段名
     * @param dstFieldNameMapper  指定dst中的每个字段可以映射为哪些字段名以和src的字段进行匹配，也就是说允许一个字段有多个别名，允许null则默认字段名
     * @param valueMapper         指定从src中取出的字段值进行怎样的转换操作到dst中，允许null则改规则不生效
     * @param newValueCopyFilter  指定哪些情况下的src的值需要调用dst进行值的设定，这就允许过滤某些src的值不设置到dst中，比如null值不进行设置，允许null则改规则不生效
     * @param oldValueCoverFilter 指定当dst中要被设置时先检测是否需要进行覆盖，这就可以排除当dst中的值已经满足需求时不要进行覆盖，比如dst中的值已经非null时保留dst原始值而不进行修改，允许null则改规则不生效
     * @param <T>
     * @return dst
     */
    public static <T> T beanCopy0(Object src, T dst, Predicate<Field> fieldFilter,
                                  Function<Field, List<String>> srcFieldNameMapper,
                                  Function<Field, List<String>> dstFieldNameMapper,
                                  Function<Object, Object> valueMapper,
                                  Predicate<Object> newValueCopyFilter,
                                  Predicate<Object> oldValueCoverFilter) {
        if (src == null || dst == null) {
            return dst;
        }

        Map<Field, Class<?>> srcFields = getFields(src.getClass());
        if (srcFields.isEmpty()) {
            return dst;
        }

        Map<Field, Class<?>> dstFields = getFields(dst.getClass());
        if (dstFields.isEmpty()) {
            return dst;
        }

        Map<String, Field> dstNameMap = new HashMap<>();
        for (Field item : dstFields.keySet()) {
            if (dstFieldNameMapper != null) {
                List<String> list = dstFieldNameMapper.apply(item);
                for (String name : list) {
                    if (name != null) {
                        dstNameMap.put(name, item);
                    }
                }

            }
            dstNameMap.put(item.getName(), item);
        }

        HashSet<String> list = new HashSet<>();
        for (Field srcField : srcFields.keySet()) {
            boolean isTarget = false;
            if (fieldFilter == null || fieldFilter.test(srcField)) {
                isTarget = true;
            }
            if (!isTarget) {
                continue;
            }
            list.clear();
            if (srcFieldNameMapper != null) {
                List<String> names = srcFieldNameMapper.apply(srcField);
                for (String name : names) {
                    if (name != null) {
                        list.add(name);
                    }
                }
            }
            list.add(srcField.getName());

            for (String name : list) {
                Field dstField = dstNameMap.get(name);
                if (dstField == null) {
                    continue;
                }

                try {
                    if (oldValueCoverFilter != null) {
                        Object old = valueGet(dst, dstField);
                        if (oldValueCoverFilter != null) {
                            if (!oldValueCoverFilter.test(old)) {
                                continue;
                            }
                        }
                    }
                    Object val = valueGet(src, srcField);
                    if (valueMapper != null) {
                        val = valueMapper.apply(val);
                    }
                    if (newValueCopyFilter != null && !newValueCopyFilter.test(val)) {
                        continue;
                    }
                    val = ObjectConvertor.tryConvertAsType(val, dstField.getType());
                    valueSet(dst, dstField, val);
                } catch (Throwable e) {

                }
            }
        }

        return dst;
    }

    private static final String[] jdkPackages = {
            "java.lang.",
            "java.util.",
            "java.util.concurrent.",
            "java.util.concurrent.atomic.",
            "java.util.concurrent.locks",
            "java.time.",
            "java.time.format.",
            "java.io.",
            "java.math.",
            "java.lang.reflect.",
            "java.util.function.",
            "java.util.regex.",
            "java.util.stream.",
            "java.net.",
            "java.nio.",
            "java.nio.file.",
            "java.nio.charset.",
            "java.security.",
            "java.sql.",
            "java.text.",
            "javax.crypto.",
            "javax.crypto.interfaces.",
            "javax.crypto.spec.",
    };


    private static LruMap<Field, Method[]> CACHE_GET_GETTER_AND_SETTER = new LruMap<>(8192);

    public static Method[] getGetterAndSetter(Field field) {
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_GETTER_AND_SETTER : null), field, (k) -> {
            Method[] ret = new Method[2];
            ret[0] = getGetter(k);
            ret[1] = getSetter(k);
            return ret;
        }, (v) -> {
            Method[] ret = new Method[2];
            ret[0] = v[0];
            ret[1] = v[1];
            return ret;
        });
    }

    private static LruMap<String, Method[]> CACHE_GET_GETTER_AND_SETTER_BY_NAME = new LruMap<>(8192);

    public static Method[] getGetterAndSetter(Class<?> clazz, String fieldName) {
        String key = clazz + "##" + fieldName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_GETTER_AND_SETTER_BY_NAME : null), key, (k) -> {
            Method[] ret = new Method[2];
            ret[0] = getGetter(clazz, fieldName);
            ret[1] = getSetter(clazz, fieldName);
            return ret;
        }, (v) -> {
            Method[] ret = new Method[2];
            ret[0] = v[0];
            ret[1] = v[1];
            return ret;
        });
    }

    private static LruMap<Field, Method> CACHE_GET_GETTER = new LruMap<>(8192);

    public static Method getGetter(Field field) {
        return cacheDelegate(CACHE_GET_GETTER, field, (k) -> {
            Class<?> declaringClass = k.getDeclaringClass();
            String name = k.getName();
            return getGetter(declaringClass, name);
        }, v -> v);
    }

    private static LruMap<String, Method> CACHE_GET_GETTER_BY_NAME = new LruMap<>(8192);

    public static Method getGetter(Class<?> clazz, String fieldName) {
        String key = clazz + "##" + fieldName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_GETTER_BY_NAME : null), key, (k) -> {
            return getGetter0(clazz, fieldName);
        }, v -> v);
    }

    public static Method getGetter0(Class<?> clazz, String fieldName) {
        AtomicReference<Method> ret = new AtomicReference<>();
        iterGetterNames(fieldName, (name) -> {
            try {
                Method method = clazz.getMethod(name);
                if (method != null && isGetter(method)) {
                    ret.set(method);
                    return true;
                }
            } catch (Exception e) {

            }
            try {
                Method method = clazz.getDeclaredMethod(name);
                if (method != null && isGetter(method)) {
                    ret.set(method);
                    return true;
                }
            } catch (Exception e) {

            }
            return false;
        });
        if (ret.get() != null) {
            return ret.get();
        }
        List<Method> getters = getGetters0(clazz, fieldName, true);
        if (getters.isEmpty()) {
            return null;
        }
        return getters.get(0);
    }

    private static LruMap<Field, List<Method>> CACHE_GET_GETTERS = new LruMap<>(8192);

    public static List<Method> getGetters(Field field) {
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_GETTERS : null), field, (k) -> {
            Class<?> clazz = k.getDeclaringClass();
            String name = k.getName();
            return getGetters(clazz, name);
        }, ArrayList::new);
    }

    private static LruMap<String, List<Method>> CACHE_GET_GETTERS_BY_NAME = new LruMap<>(8192);

    public static List<Method> getGetters(Class<?> clazz, String fieldName) {
        String key = clazz + "##" + fieldName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_GETTERS_BY_NAME : null), key, (k) -> {
            return getGetters0(clazz, fieldName, false);
        }, ArrayList::new);
    }

    public static List<Method> getGetters0(Class<?> clazz, String fieldName, boolean matchedOne) {
        Set<String> methodNames = getGetterNames(fieldName);
        Map<Method, Class<?>> methods = getMethods(clazz, (method) -> {
            if (!methodNames.contains(method.getName())) {
                return false;
            }
            return isGetter(method);
        }, matchedOne);
        return new ArrayList<>(methods.keySet());
    }

    public static boolean isGetter(Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return false;
        }
        if (Modifier.isStatic(modifiers)) {
            return false;
        }
        if (method.getParameterCount() != 0) {
            return false;
        }
        Class<?> returnType = method.getReturnType();
        if (returnType == null) {
            return false;
        }
        if (isVoid(returnType)) {
            return false;
        }
        return true;
    }

    private static LruMap<Field, Method> CACHE_GET_SETTER = new LruMap<>(8192);

    public static Method getSetter(Field field) {
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_SETTER : null), field, (k) -> {
            Class<?> declaringClass = k.getDeclaringClass();
            String name = k.getName();
            return getSetter(declaringClass, name);
        }, v -> v);
    }

    private static LruMap<String, Method> CACHE_GET_SETTER_BY_NAME = new LruMap<>(8192);

    public static Method getSetter(Class<?> clazz, String fieldName) {
        String key = clazz + "##" + fieldName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_SETTER_BY_NAME : null), key, (k) -> {
            List<Method> setters = getSetters0(clazz, fieldName, true);
            if (setters.isEmpty()) {
                return null;
            }
            return setters.get(0);
        }, v -> v);
    }

    private static LruMap<Field, List<Method>> CACHE_GET_SETTERS = new LruMap<>(8192);

    public static List<Method> getSetters(Field field) {
        return cacheDelegate(CACHE_GET_SETTERS, field, (k) -> {
            Class<?> clazz = k.getDeclaringClass();
            String name = k.getName();
            return getSetters(clazz, name);
        }, ArrayList::new);
    }

    private static LruMap<String, List<Method>> CACHE_GET_SETTERS_BY_NAME = new LruMap<>(8192);

    public static List<Method> getSetters(Class<?> clazz, String fieldName) {
        String key = clazz + "##" + fieldName;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_SETTERS_BY_NAME : null), key, (k) -> {
            return getSetters0(clazz, fieldName, false);
        }, ArrayList::new);
    }

    public static List<Method> getSetters0(Class<?> clazz, String fieldName, boolean matchedOne) {
        Set<String> methodNames = getSetterNames(fieldName);
        Map<Method, Class<?>> methods = getMethods(clazz, (method) -> {
            if (!methodNames.contains(method.getName())) {
                return false;
            }
            return isSetter(method);
        }, matchedOne);
        return new ArrayList<>(methods.keySet());
    }

    public static boolean isSetter(Method method) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return false;
        }
        if (Modifier.isStatic(modifiers)) {
            return false;
        }
        if (method.getParameterCount() != 1) {
            return false;
        }
        return true;
    }

    public static <K, V> V cacheDelegate(Map<K, V> cache, K key, Function<K, V> provider, Function<V, V> cacheCopier) {
        return cacheDelegate(cache, key,
                Map::get,
                Map::put,
                provider, cacheCopier);
    }

    @FunctionalInterface
    public interface SetConsumer<C, K, V> {
        void accept(C c, K k, V v);
    }

    public static <C, K, V> V cacheDelegate(C cache, K key,
                                            BiFunction<C, K, V> cacheSupplier,
                                            SetConsumer<C, K, V> cacheConsumer,
                                            Function<K, V> provider, Function<V, V> cacheCopier) {
        if (cache == null || key == null) {
            return provider.apply(key);
        }
        synchronized (cache) {
            V ret = cacheSupplier.apply(cache, key);
            if (ret != null) {
                return cacheCopier.apply(ret);
            }

            ret = provider.apply(key);
            if (ret != null) {
                cacheConsumer.accept(cache, key, cacheCopier.apply(ret));
            }
            return ret;
        }
    }

    public static boolean isVoid(Class<?> clazz) {
        return TypeOf.typeOf(clazz, Void.class) || TypeOf.typeOf(clazz, void.class);
    }

    public static boolean isArray(Class<?> clazz) {
        return clazz.isArray();
    }

    public static boolean isArray(Object obj) {
        if (obj instanceof Class) {
            return ((Class<?>) obj).isArray();
        }
        Class<?> clazz = obj.getClass();
        return clazz.isArray();
    }

    public static int arrayLength(Object obj) {
        return Array.getLength(obj);
    }

    public static Object arrayGet(Object arr, int index) {
        return Array.get(arr, index);
    }

    public static void arraySet(Object arr, int index, Object value) {
        Array.set(arr, index, value);
    }

    public static boolean iterGetterNames(String fieldName, Predicate<String> stopper) {
        String suffix = firstUpper(fieldName);
        if (stopper.test("get" + suffix)) {
            return true;
        }
        if (stopper.test("is" + suffix)) {
            return true;
        }
        if (stopper.test("has" + suffix)) {
            return true;
        }
        if (stopper.test("enable" + suffix)) {
            return true;
        }
        if (stopper.test(fieldName)) {
            return true;
        }
        if (stopper.test(suffix)) {
            return true;
        }
        return false;
    }

    public static Set<String> getGetterNames(String fieldName) {
        Set<String> ret = new LinkedHashSet<>();
        iterGetterNames(fieldName, (name) -> {
            ret.add(name);
            return false;
        });
        return ret;
    }

    public static boolean iterSetterNames(String fieldName, Predicate<String> stopper) {
        String suffix = firstUpper(fieldName);
        if (stopper.test("set" + suffix)) {
            return true;
        }
        if (stopper.test("with" + suffix)) {
            return true;
        }
        if (stopper.test("enable" + suffix)) {
            return true;
        }
        if (stopper.test("build" + suffix)) {
            return true;
        }
        if (stopper.test(fieldName)) {
            return true;
        }
        if (fieldName.startsWith("is")) {
            if (stopper.test("set" + firstUpper(fieldName.substring("is".length())))) {
                return true;
            }
        }
        if (stopper.test(suffix)) {
            return true;
        }
        return false;
    }

    public static Set<String> getSetterNames(String fieldName) {
        Set<String> ret = new LinkedHashSet<>();
        iterSetterNames(fieldName, (name) -> {
            ret.add(name);
            return false;
        });
        return ret;
    }

    public static String fieldNameByMethodName(String methodName) {
        String[] prefixes = {"get", "set", "is", "has", "enable", "with", "build"};
        for (String prefix : prefixes) {
            if (methodName.startsWith(prefix)) {
                methodName = methodName.substring(prefix.length());
            }
        }
        if (methodName.isEmpty()) {
            return methodName;
        }
        return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
    }

    public static String firstUpper(String name) {
        if (name == null) {
            return null;
        }
        if (name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String firstLower(String name) {
        if (name == null) {
            return null;
        }
        if (name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static <T, C extends Collection<T>> C mergeArray(C ret, Predicate<T> filter, T[] arr1, T[] arr2) {
        return mergeArray(ret, filter, false, arr1, arr2);
    }

    public static <T, C extends Collection<T>> C mergeArray(C ret, Predicate<T> filter, boolean matchedOne, T[] arr1, T[] arr2) {
        if (arr1 != null) {
            for (T item : arr1) {
                if (filter == null || filter.test(item)) {
                    ret.add(item);
                    if (matchedOne) {
                        return ret;
                    }
                }
            }
        }
        if (arr2 != null) {
            for (T item : arr2) {
                if (filter == null || filter.test(item)) {
                    ret.add(item);
                    if (matchedOne) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    public static <T> T getAnnotationValue(AnnotatedElement elem, Class<? extends Annotation> clazz, String fieldName) {
        Annotation ann = getAnnotation(elem, clazz);
        if (ann == null) {
            return null;
        }
        try {
            Object value = invokeMethod(ann, fieldName);
            return (T) value;
        } catch (Exception e) {

        }
        return null;
    }

    public static Map<String, Object> getAnnotationValues(AnnotatedElement elem, Class<? extends Annotation> clazz, List<String> fieldNames) {
        Map<String, Object> ret = new LinkedHashMap<>();
        Annotation ann = getAnnotation(elem, clazz);
        if (ann == null) {
            return ret;
        }
        for (String fieldName : fieldNames) {
            try {
                Object value = invokeMethod(ann, fieldName);
                ret.put(fieldName, value);
            } catch (Exception e) {

            }
        }

        return ret;
    }

    private static LruMap<String, Map<String, Object>> CACHE_GET_ANNOTATION_ALL_VALUES = new LruMap<>(2048);

    public static Map<String, Object> getAnnotationAllValues(AnnotatedElement elem, Class<? extends Annotation> clazz) {
        String key = elem + "#" + clazz;
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_ANNOTATION_ALL_VALUES : null), key, (k) -> {
            Map<String, Object> ret = new LinkedHashMap<>();
            Annotation ann = getAnnotation(elem, clazz);
            if (ann == null) {
                return ret;
            }
            Class<? extends Annotation> annotationType = ann.annotationType();
            Map<Method, Class<?>> methods = getMethods(annotationType);
            for (Map.Entry<Method, Class<?>> entry : methods.entrySet()) {
                Method method = entry.getKey();
                if (method.getParameterCount() > 0) {
                    continue;
                }

                String name = method.getName();
                if (Arrays.asList("toString", "equals", "hashCode", "annotationType").contains(name)) {
                    continue;
                }
                try {
                    Object value = invokeMethod(ann, name);
                    ret.put(name, value);
                } catch (Exception e) {

                }
            }
            return ret;
        }, LinkedHashMap::new);
    }

    private static LruMap<String, Annotation> CACHE_GET_MEMBER_ANNOTATION = new LruMap<>(8192);

    public static <T extends Annotation> T getMemberAnnotation(Member member, Class<T> clazz) {
        if (!(member instanceof AnnotatedElement)) {
            return null;
        }
        AnnotatedElement elem = (AnnotatedElement) member;
        String key = member + "#" + clazz;
        return (T) cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_MEMBER_ANNOTATION : null), key, (k) -> {
            T ann = getAnnotation(elem, clazz);
            if (ann == null) {
                ann = getAnnotation(member.getDeclaringClass(), clazz);
            }
            return ann;
        }, e -> e);
    }

    private static LruMap<String, Annotation> CACHE_GET_ANNOTATION = new LruMap<>(8192);

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        String key = elem + "#" + clazz;
        return (T) cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_ANNOTATION : null), key, (k) -> {
            Set<T> anns = getAnnotations(elem, clazz, true);
            if (!anns.isEmpty()) {
                return anns.iterator().next();
            }
            return null;
        }, e -> e);
    }

    private static LruMap<String, Map<Annotation, List<Annotation>>> CACHE_GET_ASSIGN_ANNOTATIONS = new LruMap<>(8192);

    public static <T extends Annotation> Map<T, List<Annotation>> getAssignAnnotations(AnnotatedElement elem, Class<T> clazz) {
        String key = elem + "#" + clazz;
        return (Map<T, List<Annotation>>) cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_ASSIGN_ANNOTATIONS : null), key, (k) -> {
            Map ret = getAssignAnnotationsNext(elem, clazz, null, new LinkedList<>());
            return ret;
        }, e -> {
            Map<Annotation, List<Annotation>> ret = new LinkedHashMap<>();
            for (Map.Entry<Annotation, List<Annotation>> entry : e.entrySet()) {
                ret.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            return ret;
        });

    }

    public static <T extends Annotation> Map<T, List<Annotation>> getAssignAnnotationsNext(AnnotatedElement elem, Class<T> clazz, Class<? extends Annotation> repeatableClass) {
        return getAssignAnnotationsNext(elem, clazz, repeatableClass, new LinkedList<>());
    }

    public static <T extends Annotation> Map<T, List<Annotation>> getAssignAnnotationsNext(AnnotatedElement elem, Class<T> clazz, Class<? extends Annotation> repeatableClass, LinkedList<Annotation> stack) {
        Map<T, List<Annotation>> ret = new LinkedHashMap<>();
        if (repeatableClass == null) {
            Repeatable repeatable = findAnnotation(clazz, Repeatable.class);
            if (repeatable != null) {
                repeatableClass = repeatable.value();
            }
        }
        Set<Annotation> anns = findAllAnnotation(elem, (ann) -> ann != null, false);
        for (Annotation ann : anns) {
            stack.addLast(ann);
            try {
                Class<? extends Annotation> type = ann.annotationType();
                if (TypeOf.typeOf(type, clazz)) {
                    ret.put((T) ann, new LinkedList<>(stack));
                }
                if (repeatableClass != null) {
                    if (TypeOf.typeOf(type, repeatableClass)) {
                        Map<Method, Class<?>> map = getMethods(type, (e) -> e.getName().equals("value"), true);
                        if (!map.isEmpty()) {
                            Method method = map.entrySet().iterator().next().getKey();
                            try {
                                method.setAccessible(true);
                                Object arr = method.invoke(ann);
                                if (arr != null) {
                                    if (arr.getClass().isArray()) {
                                        int len = Array.getLength(arr);
                                        for (int i = 0; i < len; i++) {
                                            Object item = Array.get(arr, i);
                                            if (item != null) {
                                                if (TypeOf.typeOf(item.getClass(), clazz)) {
                                                    ret.put((T) item, new LinkedList<>(stack));
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
                if (TypeOf.typeOf(type, Documented.class)
                        || TypeOf.typeOf(type, Inherited.class)
                        || TypeOf.typeOf(type, Native.class)
                        || TypeOf.typeOf(type, Repeatable.class)
                        || TypeOf.typeOf(type, Retention.class)
                        || TypeOf.typeOf(type, Target.class)) {
                    continue;
                }
                Map<T, List<Annotation>> next = getAssignAnnotationsNext(type, clazz, repeatableClass, stack);
                ret.putAll(next);
            } finally {
                stack.removeLast();
            }
        }
        return ret;
    }

    private static LruMap<String, Set<Annotation>> CACHE_GET_ANNOTATIONS = new LruMap<>(8192);

    public static <T extends Annotation> Set<T> getAnnotations(AnnotatedElement elem, Class<T> clazz) {
        String key = elem + "#" + clazz;
        return (Set<T>) cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_ANNOTATIONS : null), key, (k) -> {
            Set set = getAnnotations(elem, clazz, false);
            return set;
        }, e -> {
            return new HashSet<>(e);
        });
    }

    public static <T extends Annotation> Set<T> getAnnotations(AnnotatedElement elem, Class<T> clazz, boolean matchedOne) {
        Set<T> ret = new LinkedHashSet<>();
        T ann = findAnnotation(elem, clazz);
        if (ann != null) {
            ret.add(ann);
            if (matchedOne) {
                return ret;
            }
        }
        mergeArray(ret, null, matchedOne, elem.getDeclaredAnnotationsByType(clazz), elem.getAnnotationsByType(clazz));
        return ret;
    }

    public static <T extends Annotation> T findAnnotation(AnnotatedElement elem, Class<T> annClazz) {
        T ann = elem.getDeclaredAnnotation(annClazz);
        if (ann == null) {
            ann = elem.getAnnotation(annClazz);
        }
        return ann;
    }

    public static Set<Annotation> findAllAnnotation(AnnotatedElement elem, Predicate<Annotation> filter) {
        return findAllAnnotation(elem, filter, false);
    }

    public static Set<Annotation> findAllAnnotation(AnnotatedElement elem, Predicate<Annotation> filter, boolean matchedOne) {
        Set<Annotation> ret = new LinkedHashSet<>();
        if (elem == null) {
            return ret;
        }
        mergeArray(ret, filter, matchedOne, elem.getDeclaredAnnotations(), elem.getAnnotations());
        return ret;
    }
    private static LruMap<String, Map<VirtualField, Class<?>>> CACHE_GET_VIRTUAL_FIELDS0 = new LruMap<>(8192);
    private static LruMap<String, Map<String, VirtualGetterField>> CACHE_GET_VIRTUAL_GETTER_FIELDS = new LruMap<>(8192);
    private static LruMap<String, Map<String, VirtualSetterField>> CACHE_GET_VIRTUAL_SETTER_FIELDS = new LruMap<>(8192);

    public static Map<Method, Class<?>> getMethods(Class<?> clazz,
                                                   Predicate<Class<?>> superFilter,
                                                   Predicate<Method> methodFilter,
                                                   boolean matchedOne
    ) {
        Map<Method, Class<?>> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        Set<Method> arr = findMethod(clazz, methodFilter, matchedOne);
        for (Method item : arr) {
            ret.put(item, clazz);
            if (matchedOne) {
                return ret;
            }
        }
        if (Object.class.equals(clazz)) {
            return ret;
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superFilter == null || superFilter.test(superclass)) {
            Map<Method, Class<?>> next = getMethods(superclass, superFilter, methodFilter, matchedOne);
            ret.putAll(next);
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (Class<?> superInterface : interfaces) {
                if (superFilter == null || superFilter.test(superInterface)) {
                    Map<Method, Class<?>> next = getMethods(superclass, superFilter, methodFilter, matchedOne);
                    ret.putAll(next);
                }
            }
        }
        return ret;
    }

    public static Map<String, VirtualField> getVirtualFields(Class<?> clazz) {
        String key = clazz + "##";
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_VIRTUAL_FIELDS : null), key, (k) -> {
            return getVirtualFields(clazz, null, null, null, false);
        }, v -> {
            return new LinkedHashMap<>(v);
        });
    }

    public static Map<String, VirtualField> getVirtualFields(Class<?> clazz,
                                                             Predicate<Class<?>> superFilter
    ) {
        return getVirtualFields(clazz, superFilter, null, null, false);
    }

    public static Map<String, VirtualField> getVirtualFields(Class<?> clazz,
                                                             Predicate<Class<?>> superFilter,
                                                             Predicate<Field> fieldFilter,
                                                             Predicate<Method> methodFilter
    ) {
        return getVirtualFields(clazz, superFilter, fieldFilter, methodFilter, false);
    }

    public static Map<VirtualField, Class<?>> getVirtualFields0(Class<?> clazz) {
        String key = clazz + "##";
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_VIRTUAL_FIELDS0 : null), key, (k) -> {
            return getVirtualFields0(clazz, null, null, null, false);
        }, v -> {
            return new LinkedHashMap<>(v);
        });
    }

    public static Map<VirtualField, Class<?>> getVirtualFields0(Class<?> clazz,
                                                                Predicate<Class<?>> superFilter
    ) {
        return getVirtualFields0(clazz, superFilter, null, null, false);
    }

    public static Map<VirtualField, Class<?>> getVirtualFields0(Class<?> clazz,
                                                                Predicate<Class<?>> superFilter,
                                                                Predicate<Field> fieldFilter,
                                                                Predicate<Method> methodFilter
    ) {
        return getVirtualFields0(clazz, superFilter, fieldFilter, methodFilter, false);
    }

    /**
     * 获取虚拟字段
     * 优先返回具备完整读写权限的类型
     *
     * @param clazz
     * @param superFilter
     * @param fieldFilter
     * @param methodFilter
     * @param matchedOne
     * @return
     */
    public static Map<String, VirtualField> getVirtualFields(Class<?> clazz,
                                                             Predicate<Class<?>> superFilter,
                                                             Predicate<Field> fieldFilter,
                                                             Predicate<Method> methodFilter,
                                                             boolean matchedOne
    ) {
        Map<VirtualField, Class<?>> map = getVirtualFields0(clazz, superFilter, fieldFilter, methodFilter, matchedOne);
        Map<String, List<VirtualField>> aggMap = new LinkedHashMap<>();
        for (Map.Entry<VirtualField, Class<?>> entry : map.entrySet()) {
            VirtualField key = entry.getKey();
            String name = key.name();
            if (!aggMap.containsKey(name)) {
                aggMap.put(name, new ArrayList<>());
            }
            aggMap.get(name).add(key);
        }

        Map<String, VirtualField> ret = new LinkedHashMap<>();
        for (Map.Entry<String, List<VirtualField>> entry : aggMap.entrySet()) {
            List<VirtualField> list = entry.getValue();
            VirtualField rwField = null;
            for (VirtualField item : list) {
                if (item.readable() && item.writeable()) {
                    rwField = item;
                    break;
                }
            }
            if (rwField != null) {
                ret.put(entry.getKey(), rwField);
            } else {
                ret.put(entry.getKey(), list.get(0));
            }
        }
        return ret;
    }

    public static Map<String, VirtualGetterField> getVirtualGetterFields(Class<?> clazz) {
        String key = clazz + "##";
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_VIRTUAL_GETTER_FIELDS : null), key, (k) -> {
            return getVirtualGetterFields0(clazz);
        }, v -> {
            return new LinkedHashMap<>(v);
        });
    }

    public static Map<String, VirtualSetterField> getVirtualSetterFields(Class<?> clazz) {
        String key = clazz + "##";
        return cacheDelegate((ENABLE_CACHE.get() ? CACHE_GET_VIRTUAL_SETTER_FIELDS : null), key, (k) -> {
            return getVirtualSetterFields0(clazz);
        }, v -> {
            return new LinkedHashMap<>(v);
        });
    }

    public static Map<String, VirtualGetterField> getVirtualGetterFields0(Class<?> clazz) {
        Map<VirtualField, Class<?>> map = getVirtualFields0(clazz);
        Map<String, VirtualGetterField> ret = new LinkedHashMap<>();
        for (Map.Entry<VirtualField, Class<?>> entry : map.entrySet()) {
            VirtualField key = entry.getKey();
            if (key instanceof VirtualGetterField) {
                String name = key.name();
                if (!ret.containsKey(name)) {
                    ret.put(name, (VirtualGetterField) key);
                }
            }
        }
        return ret;
    }

    public static Map<String, VirtualSetterField> getVirtualSetterFields0(Class<?> clazz) {
        Map<VirtualField, Class<?>> map = getVirtualFields0(clazz);
        Map<String, VirtualSetterField> ret = new LinkedHashMap<>();
        for (Map.Entry<VirtualField, Class<?>> entry : map.entrySet()) {
            VirtualField key = entry.getKey();
            if (key instanceof VirtualSetterField) {
                String name = key.name();
                if (!ret.containsKey(name)) {
                    ret.put(name, (VirtualSetterField) key);
                }
            }
        }
        return ret;
    }

    /**
     * 虚拟字段
     * 使用getter/setter方法的名称，映射为一个字段名，这是虚拟字段，也就是类中可能没有对应的属性
     * 同时包含类中定义的field字段，这是实际字段，也就是类中定义的属性
     *
     * @param clazz
     * @param superFilter
     * @param fieldFilter
     * @param methodFilter
     * @param matchedOne
     * @return
     */
    public static Map<VirtualField, Class<?>> getVirtualFields0(Class<?> clazz,
                                                                Predicate<Class<?>> superFilter,
                                                                Predicate<Field> fieldFilter,
                                                                Predicate<Method> methodFilter,
                                                                boolean matchedOne
    ) {
        Map<VirtualField, Class<?>> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        Set<Method> methods = findMethod(clazz, (m) -> {
            if (!isGetter(m) && !isSetter(m)) {
                return false;
            }
            return methodFilter == null || methodFilter.test(m);
        }, false);
        Map<String, List<Method>> methodFieldsMap = new LinkedHashMap<>();
        for (Method item : methods) {
            if (isGetter(item)) {
                MethodGetterField vir = new MethodGetterField(item);
                String name = vir.name();
                if (!methodFieldsMap.containsKey(name)) {
                    methodFieldsMap.put(name, new ArrayList<>());
                }
                methodFieldsMap.get(name).add(item);
            }
            if (isSetter(item)) {
                MethodSetterField vir = new MethodSetterField(item);
                String name = vir.name();
                if (!methodFieldsMap.containsKey(name)) {
                    methodFieldsMap.put(name, new ArrayList<>());
                }
                methodFieldsMap.get(name).add(item);
            }
        }
        for (Map.Entry<String, List<Method>> entry : methodFieldsMap.entrySet()) {
            List<Method> list = entry.getValue();
            Method getter = null;
            Method setter = null;
            for (Method method : list) {
                if (isGetter(method)) {
                    getter = method;
                }
                if (isSetter(method)) {
                    setter = method;
                }
                if (getter != null && setter != null) {
                    break;
                }
            }
            if (getter != null && setter != null) {
                ret.put(new MethodGetterSetterField(getter, setter), clazz);
            } else {
                if (getter != null) {
                    ret.put(new MethodGetterField(getter), clazz);
                }
                if (setter != null) {
                    ret.put(new MethodSetterField(setter), clazz);
                }
            }
            if (matchedOne) {
                return ret;
            }
        }
        Set<Field> fields = findField(clazz, fieldFilter, matchedOne);
        for (Field item : fields) {
            ret.put(new FieldGetterSetterField(item), clazz);
            if (matchedOne) {
                return ret;
            }
        }
        if (Object.class.equals(clazz)) {
            return ret;
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superFilter == null || superFilter.test(superclass)) {
            Map<VirtualField, Class<?>> next = getVirtualFields0(superclass, superFilter, fieldFilter, methodFilter, matchedOne);
            ret.putAll(next);
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (Class<?> superInterface : interfaces) {
                if (superFilter == null || superFilter.test(superInterface)) {
                    Map<VirtualField, Class<?>> next = getVirtualFields0(superclass, superFilter, fieldFilter, methodFilter, matchedOne);
                    ret.putAll(next);
                }
            }
        }
        return ret;
    }

    public static String weakVirtualFieldName(VirtualField field) {
        if (field == null) {
            return null;
        }
        return field.name().replaceAll("[-_]", "").toLowerCase();
    }


    public static <T> T beanCopyVirtualWeak(Object src, T dst) {
        return beanCopyVirtual(src, dst, null, ReflectResolver::weakVirtualFieldName, ReflectResolver::weakVirtualFieldName);
    }

    public static <T> T beanCopyVirtualWeak(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanCopyVirtual(src, dst, fieldFilter, ReflectResolver::weakVirtualFieldName, ReflectResolver::weakVirtualFieldName);
    }

    public static <T> T beanCopyVirtual(Object src, T dst) {
        return beanCopyVirtual(src, dst, null, null, null, null, null, null);
    }

    public static <T> T beanCopyVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanCopyVirtual(src, dst, fieldFilter, null, null, null, null, null);
    }

    /**
     * bean虚拟属性的复制
     * 无论值src中的值是什么全部复制覆盖dst中的字段
     *
     * @param src
     * @param dst
     * @param fieldFilter
     * @param srcFieldNameMapper
     * @param dstFieldNameMapper
     * @param <T>
     * @return
     */
    public static <T> T beanCopyVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter,
                                        Function<VirtualField, String> srcFieldNameMapper,
                                        Function<VirtualField, String> dstFieldNameMapper) {
        return beanCopyVirtual(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper, null, null, null);
    }

    public static <T> T beanAssignVirtualWeak(Object src, T dst) {
        return beanAssignVirtual(src, dst, null, ReflectResolver::weakVirtualFieldName, ReflectResolver::weakVirtualFieldName);
    }

    public static <T> T beanAssignVirtualWeak(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanAssignVirtual(src, dst, fieldFilter, ReflectResolver::weakVirtualFieldName, ReflectResolver::weakVirtualFieldName);
    }

    public static <T> T beanAssignVirtual(Object src, T dst) {
        return beanAssignVirtual(src, dst, null, null, null);
    }

    public static <T> T beanAssignVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanAssignVirtual(src, dst, fieldFilter, null, null);
    }

    /**
     * bean虚拟字段的覆盖
     * 使用src中的所有非空属性覆盖dst中的属性
     * 当dst中的属性也非空时，也会被src覆盖
     *
     * @param src
     * @param dst
     * @param fieldFilter
     * @param srcFieldNameMapper
     * @param dstFieldNameMapper
     * @param <T>
     * @return
     */
    public static <T> T beanAssignVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter,
                                          Function<VirtualField, String> srcFieldNameMapper,
                                          Function<VirtualField, String> dstFieldNameMapper) {
        return beanCopyVirtual(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper, null, v -> {
            if (v == null) {
                return false;
            }
            if (v instanceof String) {
                if (((String) v).isEmpty()) {
                    return false;
                }
            }
            return true;
        }, null);
    }

    public static <T> T beanMergeVirtualWeak(Object src, T dst) {
        return beanMergeVirtual(src, dst, null, ReflectResolver::weakVirtualFieldName, ReflectResolver::weakVirtualFieldName);
    }

    public static <T> T beanMergeVirtualWeak(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanMergeVirtual(src, dst, fieldFilter, ReflectResolver::weakVirtualFieldName, ReflectResolver::weakVirtualFieldName);
    }

    public static <T> T beanMergeVirtual(Object src, T dst) {
        return beanMergeVirtual(src, dst, null, null, null);
    }

    public static <T> T beanMergeVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanMergeVirtual(src, dst, fieldFilter, null, null);
    }

    /**
     * bean虚拟属性的合并
     * 保留dst中的非空属性，并且使用src中的非空属性补充到dst中
     * 到dst中和src中同时存在相同字段的非空时，则保留dst中的值
     *
     * @param src
     * @param dst
     * @param fieldFilter
     * @param srcFieldNameMapper
     * @param dstFieldNameMapper
     * @param <T>
     * @return
     */
    public static <T> T beanMergeVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter,
                                         Function<VirtualField, String> srcFieldNameMapper,
                                         Function<VirtualField, String> dstFieldNameMapper) {
        return beanCopyVirtual(src, dst, fieldFilter, srcFieldNameMapper, dstFieldNameMapper, null, v -> {
            if (v == null) {
                return false;
            }
            if (v instanceof String) {
                if (((String) v).isEmpty()) {
                    return false;
                }
            }
            return true;
        }, v -> {
            if (v == null) {
                return true;
            }
            if (v instanceof String) {
                if (((String) v).isEmpty()) {
                    return true;
                }
            }
            return false;
        });
    }

    public static <T> T beanCopyVirtual(Object src, T dst, Predicate<VirtualField> fieldFilter,
                                        Function<VirtualField, String> srcFieldNameMapper,
                                        Function<VirtualField, String> dstFieldNameMapper,
                                        Function<Object, Object> valueMapper,
                                        Predicate<Object> newValueCopyFilter,
                                        Predicate<Object> oldValueCoverFilter) {
        return beanCopyVirtual0(src, dst, fieldFilter,
                srcFieldNameMapper == null ? null : (field) -> {
                    String name = srcFieldNameMapper.apply(field);
                    if (name == null) {
                        return Collections.emptyList();
                    }
                    return Collections.singletonList(name);
                }, dstFieldNameMapper == null ? null : (field) -> {
                    String name = dstFieldNameMapper.apply(field);
                    if (name == null) {
                        return Collections.emptyList();
                    }
                    return Collections.singletonList(name);
                }, valueMapper,
                newValueCopyFilter,
                oldValueCoverFilter);
    }

    public static <T> T beanCopyVirtual0(Object src, T dst) {
        return beanCopyVirtual0(src, dst, null,
                null, null,
                null, null, null);
    }

    public static <T> T beanCopyVirtual0(Object src, T dst, Predicate<VirtualField> fieldFilter) {
        return beanCopyVirtual0(src, dst, fieldFilter,
                null, null,
                null, null, null);
    }

    public static <T> T beanCopyVirtual0(Object src, T dst, Predicate<VirtualField> fieldFilter,
                                         Function<VirtualField, List<String>> srcFieldNameMapper,
                                         Function<VirtualField, List<String>> dstFieldNameMapper) {
        return beanCopyVirtual0(src, dst, fieldFilter,
                srcFieldNameMapper, dstFieldNameMapper,
                null, null, null);
    }

    /**
     * bean的虚拟字段复制
     *
     * @param src
     * @param dst
     * @param fieldFilter         指定src中的哪些字段将会被用于copy，允许null则全字段
     * @param srcFieldNameMapper  指定src中的每个字段可以映射为哪些字段名以和dst的字段进行匹配，也就是说允许一个字段有多个别名，允许null则默认字段名
     * @param dstFieldNameMapper  指定dst中的每个字段可以映射为哪些字段名以和src的字段进行匹配，也就是说允许一个字段有多个别名，允许null则默认字段名
     * @param valueMapper         指定从src中取出的字段值进行怎样的转换操作到dst中，允许null则改规则不生效
     * @param newValueCopyFilter  指定哪些情况下的src的值需要调用dst进行值的设定，这就允许过滤某些src的值不设置到dst中，比如null值不进行设置，允许null则改规则不生效
     * @param oldValueCoverFilter 指定当dst中要被设置时先检测是否需要进行覆盖，这就可以排除当dst中的值已经满足需求时不要进行覆盖，比如dst中的值已经非null时保留dst原始值而不进行修改，允许null则改规则不生效
     * @param <T>
     * @return dst
     */
    public static <T> T beanCopyVirtual0(Object src, T dst, Predicate<VirtualField> fieldFilter,
                                         Function<VirtualField, List<String>> srcFieldNameMapper,
                                         Function<VirtualField, List<String>> dstFieldNameMapper,
                                         Function<Object, Object> valueMapper,
                                         Predicate<Object> newValueCopyFilter,
                                         Predicate<Object> oldValueCoverFilter) {
        if (src == null || dst == null) {
            return dst;
        }

        Map<String, VirtualGetterField> srcFields = getVirtualGetterFields(src.getClass());
        if (srcFields.isEmpty()) {
            return dst;
        }

        Map<String, VirtualSetterField> dstFields = getVirtualSetterFields(dst.getClass());
        if (dstFields.isEmpty()) {
            return dst;
        }

        Map<String, VirtualSetterField> dstNameMap = new HashMap<>();
        for (VirtualSetterField item : dstFields.values()) {
            if (dstFieldNameMapper != null) {
                List<String> list = dstFieldNameMapper.apply(item);
                for (String name : list) {
                    if (name != null) {
                        dstNameMap.put(name, item);
                    }
                }

            }
            dstNameMap.put(item.name(), item);
        }

        Map<String, VirtualGetterField> dstGetterFields = new LinkedHashMap<>();
        Map<String, VirtualGetterField> dstGetterNameMap = new HashMap<>();
        if (oldValueCoverFilter != null) {
            dstGetterFields = getVirtualGetterFields(dst.getClass());
            for (VirtualGetterField item : dstGetterFields.values()) {
                if (dstFieldNameMapper != null) {
                    List<String> list = dstFieldNameMapper.apply(item);
                    for (String name : list) {
                        if (name != null) {
                            dstGetterNameMap.put(name, item);
                        }
                    }

                }
                dstGetterNameMap.put(item.name(), item);
            }
        }

        HashSet<String> list = new HashSet<>();
        for (VirtualGetterField srcField : srcFields.values()) {
            boolean isTarget = false;
            if (fieldFilter == null || fieldFilter.test(srcField)) {
                isTarget = true;
            }
            if (!isTarget) {
                continue;
            }
            list.clear();
            if (srcFieldNameMapper != null) {
                List<String> names = srcFieldNameMapper.apply(srcField);
                for (String name : names) {
                    if (name != null) {
                        list.add(name);
                    }
                }
            }
            list.add(srcField.name());

            for (String name : list) {
                VirtualSetterField dstField = dstNameMap.get(name);
                if (dstField == null) {
                    continue;
                }

                try {
                    if (oldValueCoverFilter != null) {
                        VirtualGetterField dstGetterField = dstGetterNameMap.get(name);
                        if (dstGetterField != null) {
                            Object old = dstGetterField.get(dst);
                            if (oldValueCoverFilter != null) {
                                if (!oldValueCoverFilter.test(old)) {
                                    continue;
                                }
                            }
                        }
                    }
                    Object val = srcField.get(src);
                    if (valueMapper != null) {
                        val = valueMapper.apply(val);
                    }
                    if (newValueCopyFilter != null && !newValueCopyFilter.test(val)) {
                        continue;
                    }
                    val = ObjectConvertor.tryConvertAsType(val, dstField.type());
                    dstField.set(dst, val);
                } catch (Throwable e) {

                }
            }
        }

        return dst;
    }
}


