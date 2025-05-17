package i2f.jdbc.procedure.context;

import i2f.invokable.method.IMethod;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.lru.LruList;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/2/17 16:24
 */
public class ContextHolder {
    // 用于静态直接根据方法名在这个集合类中查找同名的方法，使用于LangInvokeNode中，方法需要为public的，不限制是否为static的方法
    public static final ConcurrentHashMap<String, List<IMethod>> INVOKE_METHOD_MAP = new ConcurrentHashMap<>();

    // 用于静态直接根据方法名在这个集合类中查找同名的方法，使用于BasicJdbcProcedureExecutor的Feature中，方法需要为public static的，且一个入参，具有返回值
    public static final ConcurrentHashMap<String, IMethod> CONVERT_METHOD_MAP = new ConcurrentHashMap<>();

    // 使用于BasicJdbcProcedureExecutor的Feature中
    public static final ConcurrentHashMap<String, Function> CONVERT_FUNC_MAP = new ConcurrentHashMap<>();

    // 使用于JdbcProcedureExecutor的loadClass方法中，用于简写的类名，允许查找哪些包，值例如：java.util.
    public static final CopyOnWriteArraySet<String> LOAD_PACKAGE_SET = new CopyOnWriteArraySet<>();

    public static final ThreadLocal<String> TRACE_LOCATION = new ThreadLocal<>();
    public static final ThreadLocal<Integer> TRACE_LINE = new ThreadLocal<>();
    public static final ThreadLocal<String> TRACE_ERRMSG = new ThreadLocal<>();
    public static final ThreadLocal<Throwable> TRACE_ERROR = new ThreadLocal<>();
    public static final ThreadLocal<XmlNode> TRACE_NODE = new ThreadLocal<>();

    static {
        registryAllInvokeMethods(ContextFunctions.class);

        registryAllConvertMethods(
                String.class,
                Integer.class,
                Double.class,
                Boolean.class
        );

        CONVERT_FUNC_MAP.put("upperCase", (Function<String, String>) String::toUpperCase);
        CONVERT_FUNC_MAP.put("lowerCase", (Function<String, String>) String::toLowerCase);

        registryLoadPackages(ContextHolder.class);
    }


    public static void registryAllInvokeMethods(Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return;
        }
        for (Class<?> clazz : classes) {
            if (clazz == null) {
                return;
            }
            Method[] methods = clazz.getMethods();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            registryAllInvokeMethods(methods);
            registryAllInvokeMethods(declaredMethods);
        }
    }

    public static void registryAllInvokeMethods(Method... methods) {
        if (methods == null || methods.length == 0) {
            return;
        }
        for (Method method : methods) {
            if (method == null) {
                continue;
            }
            int mod = method.getModifiers();
            if (!Modifier.isPublic(mod)) {
                continue;
            }
            if (Modifier.isAbstract(mod)) {
                continue;
            }
            String name = method.getName();
            List<IMethod> list = INVOKE_METHOD_MAP.computeIfAbsent(name, (key) -> new LruList<>());
            list.add(new JdkMethod(method));
        }
    }

    public static void registryAllConvertMethods(Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return;
        }
        for (Class<?> clazz : classes) {
            if (clazz == null) {
                return;
            }
            Method[] methods = clazz.getMethods();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            registryAllConvertMethods(methods);
            registryAllConvertMethods(declaredMethods);
        }
    }

    public static void registryAllConvertMethods(Method... methods) {
        if (methods == null || methods.length == 0) {
            return;
        }
        for (Method method : methods) {
            if (method == null) {
                continue;
            }
            int mod = method.getModifiers();
            if (!Modifier.isPublic(mod)) {
                continue;
            }
            if (Modifier.isAbstract(mod)) {
                continue;
            }
            String name = method.getName();

            if (!Modifier.isStatic(mod)) {
                continue;
            }
            Class<?> returnType = method.getReturnType();
            if (Void.class.equals(returnType)) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                continue;
            }
            CONVERT_METHOD_MAP.put(name, new JdkMethod(method));
        }
    }

    public static void registryLoadPackages(Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return;
        }
        for (Class<?> clazz : classes) {
            if (clazz == null) {
                return;
            }
            String name = clazz.getName();
            int idx = name.lastIndexOf(".");
            if (idx < 0) {
                continue;
            }
            String packageName = name.substring(0, idx + 1);
            LOAD_PACKAGE_SET.add(packageName);
        }
    }
}
