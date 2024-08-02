package i2f.compiler.test;

import i2f.compiler.MemoryCompiler;

/**
 * @author Ice2Faith
 * @date 2024/6/18 21:26
 * @desc
 */
public class TestCompiler {
    public static void main(String[] args) throws Exception {
        String code = "\n" +
                "import java.util.Date;\n" +
                "\n" +
                "public class ###class{\n" +
                "\n" +
                "    public Object call(Object arg){\n" +
                "        System.out.println(\"test:\"+arg);\n" +
                "        return new Date();\n" +
                "    }\n" +
                "}";
        Object ret = MemoryCompiler.compileCallRandomClass(code, "call", "hello");
        System.out.println(ret);

        String expression = "" +
                "return root+\"/\"+new Date();";
        String javaSourceCode = MemoryCompiler.wrapExpressionAsJavaSourceCode(expression, "TestExpression");
        System.out.println(javaSourceCode);

        long ts = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Object hello = MemoryCompiler.evaluateExpression(expression, "hello");
            System.out.println(hello);
        }
        long uts = System.currentTimeMillis() - ts;
        System.out.println(uts);
    }
}
