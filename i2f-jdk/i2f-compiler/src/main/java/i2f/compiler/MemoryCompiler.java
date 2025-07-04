package i2f.compiler;

import i2f.io.file.FileUtil;
import i2f.lru.LruMap;
import i2f.reflect.ReflectResolver;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2024/6/18 19:14
 * @desc
 */
public class MemoryCompiler {

    public static final String SCRIPT_TAG_PREFIX = "###";
    public static final String IMPORT_SPLIT_TAG = SCRIPT_TAG_PREFIX + "import";
    public static final String CLASS_NAME_TAG = SCRIPT_TAG_PREFIX + "class";
    public static final String METHOD_SPLIT_TAG = SCRIPT_TAG_PREFIX + "method";

    public static final String DEFAULT_IMPORTS = "\n" +
            "import java.lang.*;\n" +
            "import java.lang.reflect.*;\n" +
            "import java.io.*;\n" +
            "import java.time.*;\n" +
            "import java.math.*;\n" +
            "import java.util.*;\n" +
            "import java.util.concurrent.*;\n" +
            "import java.util.concurrent.atomic.*;\n" +
            "import java.text.*;\n" +
            "import java.net.*;\n" +
            "import java.security.*;\n" +
            "import java.util.stream.*;\n" +
            "import java.util.function.*;\n";

    /**
     * 将java文件编译为class文件
     *
     * @param javaSourceCode java 源代码文件，对应一个.java文件所支持的内容
     * @param javaFileName   java 源代码文件的拟定文件名，由于编译器强制文件名需要与public的类名一致，所以这里就写类名就行，注意，不是全限定类名
     * @param outputDir      输出路径，所有的编译结果class文件都会将此路径作为根路径展开
     * @return
     */
    public static Map<String, File> compileAsFile(String javaSourceCode, String javaFileName, File outputDir) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        if (javaFileName.endsWith(".java")) {
            javaFileName = javaFileName.substring(0, javaFileName.length() - ".java".length());
        }
        int idx = javaFileName.lastIndexOf(".");
        if (idx >= 0) {
            javaFileName = javaFileName.substring(idx + 1);
        }
        JavaFileObject javaFileObject = new SimpleJavaFileObject(URI.create("string:///" + javaFileName + ".java"), JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                return javaSourceCode;
            }
        };

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObject);

        ConcurrentHashMap<String, File> outputFilesMap = new ConcurrentHashMap<>();

        fileManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
                if (kind == JavaFileObject.Kind.CLASS) {
                    String fileName = className.replaceAll("\\.", "/");
                    File classFile = new File(outputDir, fileName + ".class");
                    outputFilesMap.put(className, classFile);
                    classFile.getParentFile().mkdirs();
                    return new SimpleJavaFileObject(classFile.toURI(), kind) {
                        @Override
                        public OutputStream openOutputStream() throws IOException {
                            return new FileOutputStream(classFile);
                        }
                    };
                } else {
                    return super.getJavaFileForOutput(location, className, kind, sibling);
                }
            }
        };

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(bos);
        JavaCompiler.CompilationTask task = compiler.getTask(writer, fileManager, null, null, null, compilationUnits);
        boolean success = task.call();
        try {
            writer.flush();
            writer.close();
        } catch (Exception e) {

        }
        String outText = new String(bos.toByteArray());
        if (!success) {
            throw new RuntimeException("compile error : " + outText);
        }
        return outputFilesMap;
    }

    /**
     * 将指定路径作为classpath的根路径，这样可以任意加载路径下的类
     *
     * @param outputDir
     * @return
     * @throws Exception
     */
    public static ClassLoader getClassLoader(File outputDir) throws Exception {
        URLClassLoader classLoader = new URLClassLoader(new URL[]{outputDir.toURI().toURL()});
        return classLoader;
    }

    public static final Pattern RETURN_PATTERN = Pattern.compile("\\s*return\\s+");
    private static final LruMap<String, Set<Class<?>>> CACHE_COMPILE_CLASS = new LruMap<>(2048);
    private static final LruMap<String, Class<?>> CACHE_FIND_COMPILE_CLASS = new LruMap<>(2048);
    private static final LruMap<String, String> CACHE_RANDOM_CLASS_NAME = new LruMap<>(2048);
    private static final LruMap<String, String> CACHE_WRAP_EXPRESSION_AS_JAVA_SOURCE_CODE = new LruMap<>(2048);

    /**
     * 直接得到源文件编译之后的class
     * 参数和作用同上
     *
     * @param javaSourceCode
     * @param javaFileName
     * @param outputDir
     * @return
     * @throws Exception
     */
    public static Map<Class<?>, File> compileAsClass(String javaSourceCode,
                                                     String javaFileName,
                                                     File outputDir
    ) throws Exception {

        Map<String, File> files = compileAsFile(javaSourceCode, javaFileName, outputDir);

        Map<Class<?>, File> ret = new HashMap<>();

        ClassLoader classLoader = getClassLoader(outputDir);
        for (Map.Entry<String, File> entry : files.entrySet()) {
            String fullClassName = entry.getKey();
            Class<?> clazz = classLoader.loadClass(fullClassName);
            ret.put(clazz, entry.getValue());
        }

        return ret;
    }

    public static Set<Class<?>> compileClass(String javaSourceCode, String javaFileName) throws Exception {
        String key = javaFileName + "##" + javaSourceCode;
        Set<Class<?>> set = CACHE_COMPILE_CLASS.get(key);
        if (set != null) {
            return new LinkedHashSet<>(set);
        }
        synchronized (CACHE_COMPILE_CLASS) {
            Set<Class<?>> ret = compileClass0(javaSourceCode, javaFileName);
            if (ret != null) {
                CACHE_COMPILE_CLASS.put(key, new LinkedHashSet<>(ret));
            }
            return ret;
        }
    }

    public static Set<Class<?>> compileClass0(String javaSourceCode, String javaFileName) throws Exception {
        File outputDir = new File("./memory-compiler/classes/" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase()));
        outputDir.mkdirs();
        try {
            Map<Class<?>, File> map = compileAsClass(javaSourceCode, javaFileName, outputDir);
            return new LinkedHashSet<>(map.keySet());
        } finally {
            FileUtil.delete(outputDir, null);
        }
    }

    public static Class<?> findCompileClass(String javaSourceCode, String javaFileName, String fullClassName) throws Exception {
        if (javaSourceCode != null) {
            javaSourceCode = javaSourceCode.trim();
        }
        String key = fullClassName + "##" + javaFileName + "##" + javaSourceCode;
        Class<?> set = CACHE_FIND_COMPILE_CLASS.get(key);
        if (set != null) {
            return set;
        }
        synchronized (CACHE_FIND_COMPILE_CLASS) {
            Class<?> ret = findCompileClass0(javaSourceCode, javaFileName, fullClassName);
            if (ret != null) {
                CACHE_FIND_COMPILE_CLASS.put(key, ret);
            }
            return ret;
        }
    }

    public static Class<?> findCompileClass0(String javaSourceCode, String javaFileName, String fullClassName) throws Exception {
        Set<Class<?>> set = compileClass(javaSourceCode, javaFileName);
        Class<?> clazz = null;
        for (Class<?> key : set) {
            if (key.getName().equals(fullClassName)) {
                clazz = key;
                break;
            }
        }
        if (clazz == null) {
            throw new NoClassDefFoundError(fullClassName);
        }
        return clazz;
    }

    /**
     * 编译并进行调用指定类的指定方法
     * 将使用一个随机路径生成所有class文件
     * 加载所有class文件
     * 并执行指定的方法
     *
     * @param javaSourceCode
     * @param javaFileName
     * @param fullClassName  全限定类名，需要执行的类的类名
     * @param methodName     类中的方法，静态方法则直接执行，非静态方法则使用无参构造实例化对象后执行
     * @param args           调用函数的参数，如果重载函数时，一定要参数类型匹配
     * @return
     * @throws Exception
     */
    public static Object compileCall(String javaSourceCode, String javaFileName, String fullClassName, String methodName, Object... args) throws Exception {
        Class<?> clazz = findCompileClass(javaSourceCode, javaFileName, fullClassName);
        List<Object> callArgs = Arrays.asList(args);
        Method method = ReflectResolver.matchExecMethod(clazz, methodName, callArgs);
        if (method == null) {
            throw new NoSuchMethodException(fullClassName + "." + methodName + " with args count " + args.length);
        }
        Object ret = ReflectResolver.execMethod(null, method, callArgs);
        return ret;
    }

    public static Object evaluateExpression(String expression, Object root) throws Exception {
        return evaluateExpression(expression, root, null, null);
    }

    public static Object evaluateExpression(String expression, Object root,
                                            String additionalImports) throws Exception {
        return evaluateExpression(expression, root, additionalImports, null);
    }

    public static String getCachedRandomClassName(String javaSourceCode) throws Exception {
        String key = javaSourceCode;
        String set = CACHE_RANDOM_CLASS_NAME.get(key);
        if (set != null) {
            return set;
        }
        synchronized (CACHE_RANDOM_CLASS_NAME) {
            String ret = "RC" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
            if (ret != null) {
                CACHE_RANDOM_CLASS_NAME.put(key, ret);
            }
            return ret;
        }
    }

    public static String wrapExpressionAsJavaSourceCode(String expression, String className) {
        return wrapExpressionAsJavaSourceCode(expression, className, null, null);
    }

    public static String wrapExpressionAsJavaSourceCode(String expression, String className,
                                                        String additionalImports) {
        return wrapExpressionAsJavaSourceCode(expression, className, additionalImports, null);
    }

    /**
     * 作用和上面的类似
     * 不过，javaSourceCode 需要使用 ###class 代替类名的位置
     * 并且不能写包名，其他一切正常
     *
     * @param javaSourceCode
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public static Object compileCallRandomClass(String javaSourceCode, String methodName, Object... args) throws Exception {
        String randomClassName = getCachedRandomClassName(javaSourceCode);
        String sourceCode = javaSourceCode.replaceAll(CLASS_NAME_TAG, randomClassName);
        return compileCall(sourceCode, randomClassName, randomClassName, methodName, args);
    }

    ;

    /**
     * 执行表达式计算
     * 关于表达式 expression 的包装规则，请查看 {@link  #wrapExpressionAsJavaSourceCode(String expression, String classNameString additionalImports, String additionalMethods)} 的注释
     *
     * @param expression
     * @param root
     * @return
     * @throws Exception
     */
    public static Object evaluateExpression(String expression, Object root,
                                            String additionalImports,
                                            String additionalMethods) throws Exception {
        String javaSourceCode = wrapExpressionAsJavaSourceCode(expression, CLASS_NAME_TAG, additionalImports, additionalMethods);
        return compileCallRandomClass(javaSourceCode, "_call", root);
    }

    public static String wrapExpressionAsJavaSourceCode(String expression, String className,
                                                        String additionalImports,
                                                        String additionalMethods) {
        String key = className + "##" + expression + "##" + additionalImports + "##" + additionalMethods;
        String set = CACHE_WRAP_EXPRESSION_AS_JAVA_SOURCE_CODE.get(key);
        if (set != null) {
            return set;
        }
        synchronized (CACHE_WRAP_EXPRESSION_AS_JAVA_SOURCE_CODE) {
            String ret = wrapExpressionAsJavaSourceCode0(expression, className, additionalImports, additionalMethods);
            if (ret != null) {
                CACHE_WRAP_EXPRESSION_AS_JAVA_SOURCE_CODE.put(key, ret);
            }
            return ret;
        }
    }

    /**
     * 将表达式包装为java源代码文件
     * 定义如下：
     * <p>
     * <hr/>
     * <pre>
     * <b><i>
     * ${expression.import}
     * ${additionalImports}
     *
     * public class ###class {
     *     public Object _call(Object root) throws Throwable {
     *         ${expression.expr}
     *     }
     *
     *     ${expression.method}
     *
     *     ${additionalMethods}
     * }
     * </i></b>
     * </pre>
     * <p>
     * <hr/>
     * 因此，对expression有一些要求
     * expression具有一个入参，名为 $root
     * expression必须包含return语句，以满足函数的返回值要求
     * <p>
     * <hr/>
     * <h6>expression 组成部分</h6>
     * <pre>
     * [import ###import] [method ###method] expr
     * 分层3部分
     *     分别是，由 ###import 分隔的 import 导入语句部分
     *     由 ###method 分隔的定义方法部分
     *     以及最后的 expr 表达式部分
     * 优先截取 ###import 之前的部分作为 import 语句部分
     * 再对剩下的部分，截取 ###method 之前的部分作为 method 语句部分
     * 最后剩下的部分，就是 expr 语句部分
     * </pre>
     * <p>
     * <hr/>
     * <h6>举例说明：</h6>
     * <pre>
     * expression=```java
     * <b><i>
     * import java.util.Date;
     *
     * ###import
     *
     * public Date currentDate(){
     *     return new Date();
     * }
     *
     * ###method
     *
     * return $root+"/"+currentDate();
     * </i></b>
     * ```
     * </pre>
     * <p>
     * <hr/>
     * 在这个例子中
     * 演示了完整的三部分内容
     * <p>
     * <hr/>
     * <h6>import 部分为 ：</h6>
     * <pre>
     * ```java
     * <b><i>
     * import java.util.Date;
     * </i></b>
     * ```
     * </pre>
     * <p>
     * <hr/>
     * <h6>method 部分为 ：</h6>
     * <pre>
     * ```java
     * <b><i>
     * public Date currentDate(){
     *     return new Date();
     * }
     * </i></b>
     * ```
     * </pre>
     * <p>
     * <hr/>
     * <h6>expr 部分为 ：</h6>
     * <pre>
     * ```java
     * <b><i>
     * return $root+"/"+currentDate();
     * </i></b>
     * ```
     * </pre>
     * <p>
     * <hr/>
     * 同时，因为import和method部分可以缺省
     * 所以，下面的语句也是可以的
     * <p>
     * <hr/>
     * <h6>有import语句，无method语句</h6>
     * <pre>
     * expression=```java
     * <b><i>
     * import java.util.Date;
     *
     * ###import
     *
     * return $root+"/"+new Date();
     * </i></b>
     * ```
     * </pre>
     * <p>
     * <hr/>
     * <h6>有import语句，无method语句</h6>
     * <pre>
     * expression=```java
     * <b><i>
     * public long currentTimeMillis(){
     *     return System.currentTimeMillis();
     * }
     *
     * ###method
     *
     * return $root+"/"+currentTimeMillis();
     * </i></b>
     * ```
     * </pre>
     * <p>
     * <hr/>
     * <h6>无import语句，无method语句</h6>
     * <pre>
     * expression=```java
     * <b><i>
     * return $root+"/"+System.currentTimeMillis();
     * </i></b>
     * ```
     * </pre>
     *
     * @param expression
     * @param className
     * @param additionalImports
     * @param additionalMethods
     * @return
     */
    public static String wrapExpressionAsJavaSourceCode0(String expression, String className,
                                                         String additionalImports,
                                                         String additionalMethods) {
        String[] arr = expression.split(IMPORT_SPLIT_TAG, 2);
        String imports = arr.length > 1 ? arr[0] : "";
        String body = arr.length > 1 ? arr[1] : arr[0];

        String methods = "";
        arr = body.split(METHOD_SPLIT_TAG, 2);
        if (arr.length > 1) {
            methods = arr[0];
            body = arr[1];
        }
        if (!body.contains("return")) {
            // return segment not exists
            if (!RETURN_PATTERN.matcher(body).find()) {
                String[] lines = body.split("\n");
                int lastCodeLineIdx = lines.length - 1;
                for (int i = lastCodeLineIdx; i >= 0; i--) {
                    String str = lines[i];
                    if (str.isEmpty()) {
                        continue;
                    }
                    str = str.trim();
                    if (str.isEmpty()) {
                        continue;
                    }
                    if ("{".equals(str)) {
                        continue;
                    }
                    if ("}".equals(str)) {
                        continue;
                    }
                    lastCodeLineIdx = i;
                    break;
                }
                String line = lines[lastCodeLineIdx];
                int idx = line.lastIndexOf(";");
                if (idx >= 0) {
                    line = line.substring(0, idx + 1) + " return " + line.substring(idx + 1);
                } else {
                    line = " return " + line;
                }
                if (!line.endsWith(";")) {
                    line = line + ";";
                }
                lines[lastCodeLineIdx] = line;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < lines.length; i++) {
                    builder.append(lines[i]).append("\n");
                }
                body = builder.toString();
            }
        }

        StringBuilder builder = new StringBuilder();

        builder.append(DEFAULT_IMPORTS).append("\n");
        builder.append(imports).append("\n");
        if (additionalImports != null) {
            builder.append(additionalImports).append("\n");
        }

        builder.append("public class ").append(className).append(" {").append("\n");

        builder.append("\t").append(methods).append("\n");
        if (additionalMethods != null) {
            builder.append("\t").append(additionalMethods).append("\n");
        }

        builder.append("\t").append("public Object _call(Object $root) throws Throwable {").append("\n");
        builder.append("\t\t").append(body).append("\n");
        builder.append("\t").append("}").append("\n");

        builder.append("}").append("\n");
        return builder.toString();
    }
}
