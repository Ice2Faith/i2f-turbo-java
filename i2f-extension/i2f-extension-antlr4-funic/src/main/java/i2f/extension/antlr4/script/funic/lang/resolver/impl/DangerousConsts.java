package i2f.extension.antlr4.script.funic.lang.resolver.impl;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/7/6 17:43
 * @desc
 */
public class DangerousConsts {
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

        methods.put("java.lang.System", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
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
        ))));

        methods.put("java.lang.Class", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                "forName",                 // 动态类加载
                "newInstance",             // 已废弃但危险
                "getClassLoader",          // 暴露类加载器
                "getDeclaredField",        // 反射字段（setAccessible 绕过）
                "getDeclaredMethod",
                "getDeclaredConstructor",
                "getDeclaredFields",
                "getDeclaredMethods",
                "getDeclaredConstructors"
        ))));

        // java.io.File 文件系统危险操作（删除、创建、重命名、修改权限）
        methods.put("java.io.File", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
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
        ))));

        // java.net.URL 发起网络请求
        methods.put("java.net.URL", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                "openConnection",   // 建立网络连接
                "openStream",       // 打开输入流（HTTP/FTP 请求）
                "getContent"        // 获取远程内容
        ))));

        // java.net.URI 跳转/解析
        methods.put("java.net.URI", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                "toURL"             // 转为 URL（再 openConnection）
        ))));

        // java.lang.Thread 额外危险操作
        methods.put("java.lang.Thread", Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                "stop",             // 已废弃但危险
                "destroy",
                "suspend",
                "resume",
                "setContextClassLoader"  // 替换线程类加载器（可加载任意类）
        ))));

        DEFAULT_CLASS_DANGEROUS_METHODS = Collections.unmodifiableMap(methods);

        // ---------- 全局危险方法 ----------
        Set<String> globalMethods = new HashSet<>(Arrays.asList(
                "exec",            // 执行外部命令（Runtime.exec 等）
                "load",            // JNI 本地库加载
                "loadLibrary",     // JNI 本地库加载
                "loadLibrary0",    // JDK 内部加载
                "halt",            // JVM 强制终止
                "deleteOnExit",     // 注册 JVM 退出时删除文件（任意类调用均拦截）

                "eval"

        ));

        DEFAULT_GLOBAL_DANGEROUS_METHODS = Collections.unmodifiableSet(globalMethods);
    }
}
