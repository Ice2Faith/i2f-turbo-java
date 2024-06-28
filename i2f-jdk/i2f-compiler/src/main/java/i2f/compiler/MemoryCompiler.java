package i2f.compiler;

import i2f.io.file.FileUtil;
import i2f.reflect.ReflectResolver;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Ice2Faith
 * @date 2024/6/18 19:14
 * @desc
 */
public class MemoryCompiler {

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
    public static Map<Class, File> compileAsClass(String javaSourceCode,
                                                  String javaFileName,
                                                  File outputDir
    ) throws Exception {

        Map<String, File> files = compileAsFile(javaSourceCode, javaFileName, outputDir);

        Map<Class, File> ret = new HashMap<>();

        ClassLoader classLoader = getClassLoader(outputDir);
        for (Map.Entry<String, File> entry : files.entrySet()) {
            String fullClassName = entry.getKey();
            Class<?> clazz = classLoader.loadClass(fullClassName);
            ret.put(clazz, entry.getValue());
        }

        return ret;
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
        File outputDir = new File("./memory-compiler/classes/" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase()));
        outputDir.mkdirs();
        try {
            Map<Class, File> map = compileAsClass(javaSourceCode, javaFileName, outputDir);
            Class<?> clazz = null;
            for (Map.Entry<Class, File> entry : map.entrySet()) {
                Class<?> key = entry.getKey();
                if (key.getName().equals(fullClassName)) {
                    clazz = key;
                    break;
                }
            }
            if (clazz == null) {
                throw new NoClassDefFoundError(fullClassName);
            }
            Method method = ReflectResolver.matchMethod(clazz, methodName, args);
            if (method == null) {
                throw new NoSuchMethodException(fullClassName + "." + methodName + " with args count " + args.length);
            }
            if (Modifier.isStatic(method.getModifiers())) {
                Object ret = ReflectResolver.invokeMethodeDirect(null, method, args);
                return ret;
            }
            Object obj = ReflectResolver.getInstance(clazz);
            Object ret = ReflectResolver.invokeMethodeDirect(obj, method, args);
            return ret;
        } finally {
            FileUtil.delete(outputDir, null);
        }

    }

    /**
     * 作用和上面的类似
     * 不过，javaSourceCode 需要使用 ### 代替类名的位置
     * 并且不能写包名，其他一切正常
     *
     * @param javaSourceCode
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     */
    public static Object compileCallRandomClass(String javaSourceCode, String methodName, Object... args) throws Exception {
        String randomClassName = "RC" + (UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
        String sourceCode = javaSourceCode.replaceAll("###", randomClassName);
        return compileCall(sourceCode, randomClassName, randomClassName, methodName, args);
    }

    /**
     * 执行表达式计算
     * 实际上，是将表达式封装到一个随机类的函数中进行执行
     * 定义如下：
     * public class ### {
     * public Object call(Object root) throws Throwable {
     * ${expression}
     * }
     * }
     * 因此，对expression有一些要求
     * expression具有一个入参，名为root
     * expression必须包含return语句，以满足函数的返回值要求
     * expression中，如果import了其他类，需要用 ### 进行分隔
     * ### 之前的视为imports
     * ### 之后的视为函数体
     * 举例说明：
     * expression=
     * import java.util.Date;
     * ###
     * return root+"/"+new Date();
     *
     * @param expression
     * @param root
     * @return
     * @throws Exception
     */
    public static Object evaluateExpression(String expression, Object root) throws Exception {
        String javaSourceCode = wrapExpressionAsJavaSourceCode(expression, "###");
        return compileCallRandomClass(javaSourceCode, "call", root);
    }

    /**
     * 将表达式包装为java源代码文件
     * 定义如下：
     * public class ### {
     * public Object call(Object root) throws Throwable {
     * ${expression}
     * }
     * }
     * 因此，对expression有一些要求
     * expression具有一个入参，名为root
     * expression必须包含return语句，以满足函数的返回值要求
     * expression中，如果import了其他类，需要用 ### 进行分隔
     * ### 之前的视为imports
     * ### 之后的视为函数体
     * 举例说明：
     * expression=
     * import java.util.Date;
     * ###
     * return root+"/"+new Date();
     *
     * @param expression
     * @param className
     * @return
     */
    public static String wrapExpressionAsJavaSourceCode(String expression, String className) {
        String[] arr = expression.split("###", 2);
        String imports = arr.length > 1 ? arr[0] : "";
        String body = arr.length > 1 ? arr[1] : arr[0];
        if (imports.isEmpty()) {
            imports = DEFAULT_IMPORTS;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(imports).append("\n");
        builder.append("public class ").append(className).append(" {").append("\n");
        builder.append("\t").append("public Object call(Object root) throws Throwable {").append("\n");
        builder.append("\t\t").append(body).append("\n");
        builder.append("\t").append("}").append("\n");
        builder.append("}").append("\n");
        return builder.toString();
    }
}
