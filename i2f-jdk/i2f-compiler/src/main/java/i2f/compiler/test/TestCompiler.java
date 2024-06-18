package i2f.compiler.test;

import i2f.compiler.MemoryCompiler;

/**
 * @author Ice2Faith
 * @date 2024/6/18 21:26
 * @desc
 */
public class TestCompiler {
    public static void main(String[] args) throws Exception {
        String code="\n" +
                "import java.util.Date;\n" +
                "\n" +
                "public class ###{\n" +
                "\n" +
                "    public Object call(Object arg){\n" +
                "        System.out.println(\"test:\"+arg);\n" +
                "        return new Date();\n" +
                "    }\n" +
                "}";
        Object ret = MemoryCompiler.compileCallRandomClass(code, "call", "hello");
        System.out.println(ret);
    }
}
