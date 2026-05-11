package i2f.extension.antlr4.script.funic.lang.resolver.impl;

import i2f.extension.antlr4.script.funic.lang.exception.throwable.FunicRejectException;
import i2f.extension.antlr4.script.funic.lang.resolver.FunicResolver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2026/5/11 10:00
 * @desc 安全的 FunicResolver 代理，使用 JDK 动态代理实现。
 * 作为安全的脚本执行控制器，防止恶意脚本执行。
 * <p>
 * 拦截范围涵盖：
 * <ul>
 *   <li>进程执行：Runtime.exec、ProcessBuilder.start</li>
 *   <li>本地库加载（JNI）：System.load、Runtime.loadLibrary</li>
 *   <li>JVM 终止：Runtime.halt、System.exit</li>
 *   <li>不安全内存操作：sun.misc.Unsafe、jdk.internal.misc.Unsafe</li>
 *   <li>类加载器滥用：ClassLoader、URLClassLoader</li>
 *   <li>反射滥用：Field/Method/Constructor 的 setAccessible 等</li>
 *   <li>JMX 管理：javax.management.*</li>
 *   <li>RMI 远程调用：java.rmi.*</li>
 *   <li>JNDI 注入：javax.naming.*</li>
 *   <li>脚本引擎嵌套：javax.script.*、Groovy、Jython 等</li>
 * </ul>
 * <p>
 * 支持通过 {@link #addDangerousClassName}、{@link #addDangerousPackage}、
 * {@link #addDangerousMethod}、{@link #addGlobalDangerousMethod} 扩展黑名单，
 * 通过 {@link #addWhitelistClassName} 添加白名单例外。
 */
public class SafeFunicResolverProxy implements InvocationHandler {

    // -------------------------------------------------------------------------
    // 静态默认黑名单（只读，在类加载时初始化）
    // -------------------------------------------------------------------------

    /**
     * 危险类全限定名黑名单（精确匹配）
     */
    protected static final Set<String> DEFAULT_DANGEROUS_CLASS_NAMES;

    /**
     * 危险包名前缀黑名单（前缀匹配，含子包）
     */
    protected static final Set<String> DEFAULT_DANGEROUS_PACKAGES;

    /**
     * 针对特定类的危险方法名黑名单；key 为类全限定名，value 为该类禁用的方法名集合
     */
    protected static final Map<String, Set<String>> DEFAULT_CLASS_DANGEROUS_METHODS;

    /**
     * 全局危险方法名黑名单：任意类上出现这些方法名均被拒绝
     */
    protected static final Set<String> DEFAULT_GLOBAL_DANGEROUS_METHODS;

    static {
        // ---------- 危险类 ----------
        Set<String> cls = new HashSet<>();

        // 进程执行
        cls.add("java.lang.Runtime");
        cls.add("java.lang.ProcessBuilder");
        cls.add("java.lang.Process");
        cls.add("java.lang.ProcessHandle");
        cls.add("java.lang.ProcessHandle$Info");

        // 不安全内存操作
        cls.add("sun.misc.Unsafe");
        cls.add("jdk.internal.misc.Unsafe");
        cls.add("jdk.internal.access.SharedSecrets");
        cls.add("jdk.internal.reflect.Reflection");

        // 类加载器
        cls.add("java.lang.ClassLoader");
        cls.add("java.net.URLClassLoader");
        cls.add("java.security.SecureClassLoader");
        cls.add("sun.reflect.misc.MethodUtil");

        // 系统与安全管理
        cls.add("java.lang.System");
        cls.add("java.lang.SecurityManager");
        cls.add("java.lang.Shutdown");
        cls.add("java.lang.Runtime");

        // 反射 API 直接操作
        cls.add("java.lang.reflect.Field");
        cls.add("java.lang.reflect.Method");
        cls.add("java.lang.reflect.Constructor");
        cls.add("java.lang.reflect.AccessibleObject");
        cls.add("java.lang.reflect.Proxy");

        // 方法句柄与动态代理
        cls.add("java.lang.invoke.MethodHandles");
        cls.add("java.lang.invoke.MethodHandles$Lookup");
        cls.add("java.lang.invoke.MethodHandle");
        cls.add("java.lang.invoke.VarHandle");

        // 序列化（反序列化 RCE 风险）
        cls.add("java.io.ObjectInputStream");
        cls.add("java.io.ObjectOutputStream");
        cls.add("java.io.ObjectStreamClass");

        // 编译器动态编译
        cls.add("javax.tools.JavaCompiler");
        cls.add("javax.tools.ToolProvider");
        cls.add("com.sun.tools.javac.Main");

        // 线程组与线程内省
        cls.add("java.lang.ThreadGroup");

        // 网络连接（主动建立出站/入站 TCP/UDP 连接）
        cls.add("java.net.Socket");
        cls.add("java.net.ServerSocket");
        cls.add("java.net.DatagramSocket");
        cls.add("java.net.MulticastSocket");
        cls.add("java.net.DatagramChannel");
        cls.add("java.nio.channels.SocketChannel");
        cls.add("java.nio.channels.ServerSocketChannel");

        // 文件写操作（防止恶意写磁盘）
        cls.add("java.io.FileOutputStream");
        cls.add("java.io.FileWriter");
        cls.add("java.io.RandomAccessFile");
        cls.add("java.io.PrintWriter");

        // NIO 文件通道（可绕过流接口直接读写文件）
        cls.add("java.nio.channels.FileChannel");
        cls.add("java.nio.MappedByteBuffer");

        // GUI 系统控制（截屏、模拟鼠标键盘）
        cls.add("java.awt.Robot");
        cls.add("java.awt.Desktop");

        // Java Agent 字节码插桩句柄
        cls.add("java.lang.instrument.Instrumentation");
        cls.add("sun.instrument.InstrumentationImpl");

        // 数据库驱动管理（防止建立任意数据库连接）
        cls.add("java.sql.DriverManager");

        // 系统托盘 / 剪贴板（信息泄露）
        cls.add("java.awt.SystemTray");
        cls.add("java.awt.datatransfer.Clipboard");
        cls.add("java.awt.datatransfer.StringSelection");

        DEFAULT_DANGEROUS_CLASS_NAMES = Collections.unmodifiableSet(cls);

        // ---------- 危险包前缀 ----------
        Set<String> pkgs = new HashSet<>();

        // JMX
        pkgs.add("javax.management");
        pkgs.add("com.sun.jmx");
        pkgs.add("com.sun.management");

        // RMI 远程调用
        pkgs.add("java.rmi");
        pkgs.add("sun.rmi");

        // JNDI 注入（Log4Shell 类攻击向量）
        pkgs.add("javax.naming");
        pkgs.add("com.sun.jndi");

        // JDI Java 调试接口
        pkgs.add("com.sun.jdi");
        pkgs.add("com.sun.tools.jdi");

        // JDK 内部包
        pkgs.add("sun.misc");
        pkgs.add("sun.reflect");
        pkgs.add("sun.security");
        pkgs.add("sun.nio.ch");
        pkgs.add("jdk.internal");
        pkgs.add("com.sun.security");

        // 安全策略
        pkgs.add("java.security");
        pkgs.add("javax.security");

        // 嵌套脚本引擎
        pkgs.add("javax.script");
        pkgs.add("groovy");
        pkgs.add("org.codehaus.groovy");
        pkgs.add("bsh");
        pkgs.add("org.python");
        pkgs.add("org.jruby");
        pkgs.add("clojure");

        // 字节码工程（ASM / Javassist / ByteBuddy / CGLIB）
        pkgs.add("net.bytebuddy");
        pkgs.add("javassist");
        pkgs.add("org.objectweb.asm");
        pkgs.add("net.sf.cglib");
        pkgs.add("org.springframework.cglib");

        // JVM 管理 MXBean（内存、线程、类加载等监控与操控）
        pkgs.add("java.lang.management");
        pkgs.add("sun.management");

        // Java Agent 字节码插桩
        pkgs.add("java.lang.instrument");

        // NIO 文件系统内部实现（绕过权限检查）
        pkgs.add("sun.nio.fs");

        // AWT/Swing GUI（截屏、键鼠模拟、系统剪贴板）
        pkgs.add("java.awt");
        pkgs.add("javax.swing");
        pkgs.add("sun.awt");
        pkgs.add("com.sun.awt");

        // 工具类（javac / javatools）
        pkgs.add("javax.tools");
        pkgs.add("com.sun.tools");

        DEFAULT_DANGEROUS_PACKAGES = Collections.unmodifiableSet(pkgs);

        // ---------- 特定类危险方法 ----------
        Map<String, Set<String>> methods = new HashMap<>();

        methods.put("java.lang.System", new HashSet<>(Arrays.asList(
                "exit",                    // JVM 退出
                "halt",                    // 强制终止
                "gc",                      // 触发GC（DoS 风险）
                "runFinalization",
                "load",                    // JNI 库加载
                "loadLibrary",             // JNI 库加载
                "setSecurityManager",      // 安全策略篡改
                "setProperty",             // 系统属性篡改
                "clearProperty",
                "setProperties",
                "setIn", "setOut", "setErr"
        )));

        methods.put("java.lang.Class", new HashSet<>(Arrays.asList(
                "forName",                 // 动态类加载
                "newInstance",             // 已废弃但危险
                "getClassLoader",          // 暴露类加载器
                "getDeclaredField",        // 反射字段（setAccessible 绕过）
                "getDeclaredMethod",
                "getDeclaredConstructor",
                "getDeclaredFields",
                "getDeclaredMethods",
                "getDeclaredConstructors"
        )));

        // java.io.File 文件系统危险操作（删除、创建、重命名、修改权限）
        methods.put("java.io.File", new HashSet<>(Arrays.asList(
                "delete",           // 删除文件/目录
                "deleteOnExit",     // JVM 退出时删除
                "mkdir",            // 创建目录
                "mkdirs",           // 递归创建目录
                "renameTo",         // 移动/重命名
                "createNewFile",    // 创建新文件
                "createTempFile",   // 创建临时文件
                "setExecutable",    // 修改执行权限
                "setReadable",      // 修改读权限
                "setWritable",      // 修改写权限
                "setLastModified",  // 篡改时间戳
                "setReadOnly"       // 修改只读属性
        )));

        // java.net.URL 发起网络请求
        methods.put("java.net.URL", new HashSet<>(Arrays.asList(
                "openConnection",   // 建立网络连接
                "openStream",       // 打开输入流（HTTP/FTP 请求）
                "getContent"        // 获取远程内容
        )));

        // java.net.URI 跳转/解析
        methods.put("java.net.URI", new HashSet<>(Arrays.asList(
                "toURL"             // 转为 URL（再 openConnection）
        )));

        // java.lang.Thread 额外危险操作
        methods.put("java.lang.Thread", new HashSet<>(Arrays.asList(
                "stop",             // 已废弃但危险
                "destroy",
                "suspend",
                "resume",
                "setContextClassLoader"  // 替换线程类加载器（可加载任意类）
        )));

        DEFAULT_CLASS_DANGEROUS_METHODS = Collections.unmodifiableMap(methods);

        // ---------- 全局危险方法 ----------
        Set<String> globalMethods = new HashSet<>(Arrays.asList(
                "exec",            // 执行外部命令（Runtime.exec 等）
                "load",            // JNI 本地库加载
                "loadLibrary",     // JNI 本地库加载
                "loadLibrary0",    // JDK 内部加载
                "halt",            // JVM 强制终止
                "deleteOnExit"     // 注册 JVM 退出时删除文件（任意类调用均拦截）
        ));

        DEFAULT_GLOBAL_DANGEROUS_METHODS = Collections.unmodifiableSet(globalMethods);
    }

    // -------------------------------------------------------------------------
    // 实例字段
    // -------------------------------------------------------------------------

    /**
     * 被代理的实际 FunicResolver
     */
    protected final FunicResolver delegate;

    /**
     * 危险类名集合（精确匹配）。
     * 构造时自动载入 {@link #DEFAULT_DANGEROUS_CLASS_NAMES} 作为初始值，
     * 可通过 {@link #addDangerousClassName} / {@link #removeDangerousClassName} 动态调整。
     */
    protected final Set<String> extraDangerousClassNames = new CopyOnWriteArraySet<>();

    /**
     * 危险包名集合（前缀匹配，含子包）。
     * 构造时自动载入 {@link #DEFAULT_DANGEROUS_PACKAGES} 作为初始值，
     * 可通过 {@link #addDangerousPackage} / {@link #removeDangerousPackage} 动态调整。
     */
    protected final Set<String> extraDangerousPackages = new CopyOnWriteArraySet<>();

    /**
     * 特定类危险方法集合；key 为类全限定名，value 为该类禁用的方法名集合。
     * 构造时自动载入 {@link #DEFAULT_CLASS_DANGEROUS_METHODS} 作为初始值，
     * 可通过 {@link #addDangerousMethod} / {@link #removeDangerousMethod} 动态调整。
     */
    protected final Map<String, Set<String>> extraClassDangerousMethods = new ConcurrentHashMap<>();

    /**
     * 全局危险方法名集合：任意类上出现这些方法名均被拒绝。
     * 构造时自动载入 {@link #DEFAULT_GLOBAL_DANGEROUS_METHODS} 作为初始值，
     * 可通过 {@link #addGlobalDangerousMethod} / {@link #removeGlobalDangerousMethod} 动态调整。
     */
    protected final Set<String> extraGlobalDangerousMethods = new CopyOnWriteArraySet<>();

    /**
     * 类名白名单：白名单中的类不受黑名单限制（谨慎使用）。
     * 可通过 {@link #addWhitelistClassName} / {@link #removeWhitelistClassName} 动态调整。
     */
    protected final Set<String> whitelistClassNames = new CopyOnWriteArraySet<>();

    // -------------------------------------------------------------------------
    // 构造器与静态工厂
    // -------------------------------------------------------------------------

    public SafeFunicResolverProxy(FunicResolver delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate FunicResolver must not be null");
        }
        this.delegate = delegate;
        // 将静态默认黑名单作为初始值载入实例可变集合，使后续可动态增删
        loadDefaults();
    }

    /**
     * 使用默认安全配置，为指定 resolver 创建 JDK 动态代理。
     *
     * @param delegate 被代理的 resolver（通常是 {@link DefaultFunicResolver} 实例）
     * @return 安全代理包装的 FunicResolver
     */
    public static FunicResolver create(FunicResolver delegate) {
        return buildProxy(new SafeFunicResolverProxy(delegate));
    }

    /**
     * 使用自定义 {@link SafeFunicResolverProxy}（已设置好额外规则）创建代理。
     *
     * <pre>{@code
     * FunicResolver safe = SafeFunicResolverProxy
     *     .builder(new DefaultFunicResolver())
     *     .addDangerousClassName("com.example.Evil")
     *     .addWhitelistClassName("java.lang.System")  // 谨慎
     *     .build();
     * }</pre>
     *
     * @param handler 已配置的 handler
     * @return 安全代理
     */
    public static FunicResolver create(SafeFunicResolverProxy handler) {
        return buildProxy(handler);
    }

    /**
     * 便捷的 Builder 入口，返回当前 handler 本身（支持链式调用后调用 {@link #build()}）。
     */
    public static SafeFunicResolverProxy builder(FunicResolver delegate) {
        return new SafeFunicResolverProxy(delegate);
    }

    /**
     * 基于当前 handler 构建代理（链式调用结尾）。
     */
    public FunicResolver build() {
        return buildProxy(this);
    }

    protected static FunicResolver buildProxy(SafeFunicResolverProxy handler) {
        return (FunicResolver) Proxy.newProxyInstance(
                FunicResolver.class.getClassLoader(),
                new Class[]{FunicResolver.class},
                handler
        );
    }

    // -------------------------------------------------------------------------
    // InvocationHandler 核心
    // -------------------------------------------------------------------------

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 对 Object 上的基础方法直接透传，避免影响正常使用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(delegate, args);
        }

        // 安全检查
        doSecurityCheck(method.getName(), args);

        // 透传到被代理的真实实现
        try {
            return method.invoke(delegate, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw (cause != null) ? cause : e;
        }
    }

    /**
     * 根据方法名与参数列表执行对应的安全检查。
     * 子类可覆盖此方法以定制检查逻辑。
     */
    protected void doSecurityCheck(String methodName, Object[] args) {
        switch (methodName) {

            // 类查找：检查类名字符串
            case "findClass":
                if (args != null && args.length >= 1 && args[0] instanceof String) {
                    checkClassNameSafe((String) args[0]);
                }
                break;

            // 静态方法调用：检查目标类 + 方法名
            case "invokeStaticMethod":
                if (args != null && args.length >= 2) {
                    Class<?> type = args[0] instanceof Class ? (Class<?>) args[0] : null;
                    String mName = args[1] instanceof String ? (String) args[1] : null;
                    if (type != null) {
                        checkClassSafe(type);
                    }
                    checkMethodSafe(type, mName);
                }
                break;

            // 实例方法调用：检查目标对象类型 + 方法名
            case "invokeInstanceMethod":
                if (args != null && args.length >= 2) {
                    Object target = args[0];
                    String mName = args[1] instanceof String ? (String) args[1] : null;
                    if (target != null) {
                        Class<?> targetClass = (target instanceof Class)
                                ? (Class<?>) target
                                : target.getClass();
                        checkClassSafe(targetClass);
                        checkMethodSafe(targetClass, mName);
                    }
                }
                break;

            // 全局方法调用：检查方法名是否为全局危险方法
            case "invokeGlobalMethod":
                if (args != null && args.length >= 1 && args[0] instanceof String) {
                    String mName = (String) args[0];
                    checkGlobalMethodSafe(mName);
                }
                break;

            // 新建实例：检查目标类
            case "newInstance":
                if (args != null && args.length >= 1 && args[0] instanceof Class) {
                    checkClassSafe((Class<?>) args[0]);
                }
                break;

            // 新建数组：检查元素类型
            case "newArray":
                if (args != null && args.length >= 1 && args[0] instanceof Class) {
                    checkClassSafe((Class<?>) args[0]);
                }
                break;

            // 获取/设置静态字段或枚举：检查类
            case "getStaticFieldOrEnum":
            case "setStaticField":
                if (args != null && args.length >= 1 && args[0] instanceof Class) {
                    checkClassSafe((Class<?>) args[0]);
                }
                break;

            // 获取/设置实例字段：检查目标对象类型
            case "getFieldValue":
            case "setFieldValue":
            case "getSquareFieldValue":
            case "setSquareFieldValue":
                if (args != null && args.length >= 1 && args[0] != null) {
                    Object target = args[0];
                    Class<?> targetClass = (target instanceof Class)
                            ? (Class<?>) target
                            : target.getClass();
                    checkClassSafe(targetClass);
                }
                break;

            // 导入包：检查包名是否安全
            case "onPreRegistryContextImportPackage":
                if (args != null && args.length >= 1 && args[0] instanceof String) {
                    checkPackageNameSafe((String) args[0]);
                }
                break;

            default:
                break;
        }
    }

    // -------------------------------------------------------------------------
    // 安全检查辅助方法
    // -------------------------------------------------------------------------

    /**
     * 检查类名字符串是否安全（支持数组格式、JVM 内部格式）。
     *
     * @param className 待检查的类名
     * @throws FunicRejectException 类名属于危险范围时抛出
     */
    public void checkClassNameSafe(String className) {
        if (className == null || className.isEmpty()) {
            return;
        }
        String normalized = normalizeClassName(className);

        // 白名单优先
        if (isInWhitelist(normalized)) {
            return;
        }

        // 精确类名黑名单
        if (extraDangerousClassNames.contains(normalized)) {
            rejectClass(className);
        }

        // 包名前缀黑名单
        checkPackageNameSafe(normalized);
    }

    /**
     * 检查 Class 对象是否安全。
     *
     * @param clazz 待检查的 Class
     * @throws FunicRejectException 类属于危险范围时抛出
     */
    public void checkClassSafe(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        // 处理数组：取元素类型
        Class<?> target = clazz;
        while (target.isArray()) {
            target = target.getComponentType();
        }
        // 基本类型直接放行
        if (target.isPrimitive()) {
            return;
        }
        checkClassNameSafe(target.getName());
    }

    /**
     * 检查包名是否安全（可传入包名或完整类名）。
     *
     * @param packageOrClassName 包名或类全限定名
     * @throws FunicRejectException 包属于危险范围时抛出
     */
    public void checkPackageNameSafe(String packageOrClassName) {
        if (packageOrClassName == null || packageOrClassName.isEmpty()) {
            return;
        }
        String normalized = packageOrClassName.replace('/', '.').trim();

        for (String dangerousPkg : extraDangerousPackages) {
            if (matchesPackage(normalized, dangerousPkg)) {
                rejectPackage(packageOrClassName);
            }
        }
    }

    /**
     * 检查方法调用是否安全（全局危险方法名 + 特定类危险方法）。
     *
     * @param type       调用方法所在类（可为 null）
     * @param methodName 方法名
     * @throws FunicRejectException 方法属于危险范围时抛出
     */
    public void checkMethodSafe(Class<?> type, String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            return;
        }
        // 全局危险方法名
        checkGlobalMethodSafe(methodName);

        // 特定类的危险方法（含父类检查）
        if (type != null) {
            checkClassSpecificMethod(type.getName(), methodName);
            Class<?> superClass = type.getSuperclass();
            while (superClass != null && superClass != Object.class) {
                checkClassSpecificMethod(superClass.getName(), methodName);
                superClass = superClass.getSuperclass();
            }
        }
    }

    /**
     * 检查全局危险方法名。
     */
    public void checkGlobalMethodSafe(String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            return;
        }
        if (extraGlobalDangerousMethods.contains(methodName)) {
            rejectMethod(null, methodName);
        }
    }

    /**
     * 判断类名是否在白名单中。
     */
    public boolean isInWhitelist(String className) {
        return whitelistClassNames.contains(className);
    }

    // -------------------------------------------------------------------------
    // 私有工具方法
    // -------------------------------------------------------------------------

    protected void checkClassSpecificMethod(String className, String methodName) {
        Set<String> blocked = extraClassDangerousMethods.get(className);
        if (blocked != null && blocked.contains(methodName)) {
            rejectMethod(className, methodName);
        }
    }

    /**
     * 标准化类名：处理数组格式（[Ljava.lang.String;）和 JVM 内部格式（L...;）。
     */
    protected String normalizeClassName(String className) {
        String s = className.replace('/', '.').trim();
        // 剥离数组维度
        while (s.startsWith("[")) {
            s = s.substring(1);
        }
        // JVM 内部对象类型格式 Ljava.lang.String;
        if (s.startsWith("L") && s.endsWith(";")) {
            s = s.substring(1, s.length() - 1);
        }
        // 剥离 [] 数组后缀
        while (s.endsWith("[]")) {
            s = s.substring(0, s.length() - 2);
        }
        return s;
    }

    /**
     * 判断 name（类名或包名）是否匹配 packagePrefix（含子包）
     */
    protected boolean matchesPackage(String name, String packagePrefix) {
        return name.equals(packagePrefix) || name.startsWith(packagePrefix + ".");
    }

    protected void rejectClass(String className) {
        throw new FunicRejectException(
                "Security violation: access to dangerous class is denied: [" + className + "]");
    }

    protected void rejectPackage(String packageName) {
        throw new FunicRejectException(
                "Security violation: access to dangerous package is denied: [" + packageName + "]");
    }

    protected void rejectMethod(String className, String methodName) {
        if (className != null) {
            throw new FunicRejectException(
                    "Security violation: calling dangerous method [" + className + "." + methodName + "] is denied");
        }
        throw new FunicRejectException(
                "Security violation: calling dangerous method [" + methodName + "] is denied");
    }

    // -------------------------------------------------------------------------
    // 链式配置方法
    // -------------------------------------------------------------------------

    /**
     * 追加额外的危险类名（精确匹配全限定名）。
     */
    public SafeFunicResolverProxy addDangerousClassName(String className) {
        if (className != null && !className.isEmpty()) {
            extraDangerousClassNames.add(className);
        }
        return this;
    }

    /**
     * 移除危险类名（可移除默认值或自定义值）。
     */
    public SafeFunicResolverProxy removeDangerousClassName(String className) {
        if (className != null) {
            extraDangerousClassNames.remove(className);
        }
        return this;
    }

    /**
     * 追加额外的危险包名（前缀匹配，含子包）。
     */
    public SafeFunicResolverProxy addDangerousPackage(String packageName) {
        if (packageName != null && !packageName.isEmpty()) {
            extraDangerousPackages.add(packageName);
        }
        return this;
    }

    /**
     * 移除危险包名（可移除默认值或自定义值）。
     */
    public SafeFunicResolverProxy removeDangerousPackage(String packageName) {
        if (packageName != null) {
            extraDangerousPackages.remove(packageName);
        }
        return this;
    }

    /**
     * 追加特定类的危险方法名。
     *
     * @param className  类全限定名
     * @param methodName 方法名
     */
    public SafeFunicResolverProxy addDangerousMethod(String className, String methodName) {
        if (className != null && methodName != null) {
            extraClassDangerousMethods
                    .computeIfAbsent(className, k -> new CopyOnWriteArraySet<>())
                    .add(methodName);
        }
        return this;
    }

    /**
     * 移除特定类的危险方法名（可移除默认值或自定义值）。
     */
    public SafeFunicResolverProxy removeDangerousMethod(String className, String methodName) {
        if (className != null && methodName != null) {
            Set<String> methods = extraClassDangerousMethods.get(className);
            if (methods != null) {
                methods.remove(methodName);
            }
        }
        return this;
    }

    /**
     * 追加全局危险方法名（任意类上调用均被拒绝）。
     */
    public SafeFunicResolverProxy addGlobalDangerousMethod(String methodName) {
        if (methodName != null && !methodName.isEmpty()) {
            extraGlobalDangerousMethods.add(methodName);
        }
        return this;
    }

    /**
     * 移除全局危险方法名（可移除默认值或自定义值）。
     */
    public SafeFunicResolverProxy removeGlobalDangerousMethod(String methodName) {
        if (methodName != null) {
            extraGlobalDangerousMethods.remove(methodName);
        }
        return this;
    }

    /**
     * 将指定类名加入白名单，使其不受黑名单限制（谨慎使用）。
     */
    public SafeFunicResolverProxy addWhitelistClassName(String className) {
        if (className != null && !className.isEmpty()) {
            whitelistClassNames.add(className);
        }
        return this;
    }

    /**
     * 从白名单中移除指定类名。
     */
    public SafeFunicResolverProxy removeWhitelistClassName(String className) {
        if (className != null) {
            whitelistClassNames.remove(className);
        }
        return this;
    }

    /**
     * 将所有静态默认黑名单载入到当前实例的可变集合中。
     * <p>构造器会自动调用一次；也可在 {@link #clearAll()} 之后手动调用以恢复默认值。</p>
     */
    public SafeFunicResolverProxy loadDefaults() {
        extraDangerousClassNames.addAll(DEFAULT_DANGEROUS_CLASS_NAMES);
        extraDangerousPackages.addAll(DEFAULT_DANGEROUS_PACKAGES);
        for (Map.Entry<String, Set<String>> entry : DEFAULT_CLASS_DANGEROUS_METHODS.entrySet()) {
            extraClassDangerousMethods
                    .computeIfAbsent(entry.getKey(), k -> new CopyOnWriteArraySet<>())
                    .addAll(entry.getValue());
        }
        extraGlobalDangerousMethods.addAll(DEFAULT_GLOBAL_DANGEROUS_METHODS);
        return this;
    }

    /**
     * 清空当前实例的所有黑名单规则（包括默认值，白名单不受影响）。
     * <p>谨慎使用：清空后将不拦截任何操作，需配合 {@link #loadDefaults()} 或手动 add 使用。</p>
     */
    public SafeFunicResolverProxy clearAll() {
        extraDangerousClassNames.clear();
        extraDangerousPackages.clear();
        extraClassDangerousMethods.clear();
        extraGlobalDangerousMethods.clear();
        return this;
    }

    /**
     * 清空所有规则后重新载入默认黑名单，等价于 {@code clearAll().loadDefaults()}。
     */
    public SafeFunicResolverProxy resetToDefaults() {
        return clearAll().loadDefaults();
    }

    // -------------------------------------------------------------------------
    // Getter
    // -------------------------------------------------------------------------

    public FunicResolver getDelegate() {
        return delegate;
    }

    public Set<String> getExtraDangerousClassNames() {
        return Collections.unmodifiableSet(extraDangerousClassNames);
    }

    public Set<String> getExtraDangerousPackages() {
        return Collections.unmodifiableSet(extraDangerousPackages);
    }

    public Set<String> getExtraGlobalDangerousMethods() {
        return Collections.unmodifiableSet(extraGlobalDangerousMethods);
    }

    public Set<String> getWhitelistClassNames() {
        return Collections.unmodifiableSet(whitelistClassNames);
    }
}
