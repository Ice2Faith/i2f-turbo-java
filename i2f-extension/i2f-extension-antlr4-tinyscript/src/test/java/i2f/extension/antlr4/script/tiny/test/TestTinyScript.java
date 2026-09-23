package i2f.extension.antlr4.script.tiny.test;

import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/20 21:03
 * @desc
 */
public class TestTinyScript {

    public static JDBCType type = JDBCType.INTEGER;

    public static class LoginUser {
        public User user = new User();

        public User user() {
            return this.user;
        }
    }

    public static class User {
        public String name = " zhang ";

        public String name() {
            return this.name;
        }
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String substr(String str, int index, int len) {
        if (str == null) {
            return str;
        }
        return str.substring(index, index + len);
    }

    public static void registryMethods() {

        TinyScript.registryBuiltMethodByStaticMethod(TestTinyScript.class, (method) -> {
            return method.getName().startsWith("regex");
        });

        TinyScript.registryBuiltMethodByStaticMethod(TestTinyScript.class);

    }

    public static void main(String[] args) throws Exception {

        registryMethods();

        // ///////////////////////////////

        testPipeline();

//        testFileScript();

//        testEnumConst();

//        testImpl();

//        testRaw();
    }

    public static void testPipeline() {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("a", new LoginUser());
        //substr(trim(a.user().name()),0,2)
        Object ret = TinyScript.script("${a} \n" +
                "|> ::user() \n" +
                "|> ::name() \n" +
                "|> trim() \n" +
                "|> substr(0, 2);", ctx);
        System.out.println("ret:" + ret);
    }

    public static void testFileScript() throws Exception {
        File file = new File("i2f-extension/i2f-extension-antlr4/src/test/java/i2f/extension/antlr4/script/tiny/test/test.ts.txt");
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }
        reader.close();

        String script = builder.toString();
        Map<String, Object> params = new HashMap<>();
        params.put("IN_FORMUAL_CONTEN", "a.username, a.age, a.username");

        Object ret = TinyScript.script(script, params);
        System.out.println("ret:" + ret);
    }

    public static void testEnumConst() {
        Map<String, Object> ctx = new HashMap<>();
        String className = TestTinyScript.class.getName();
        Object ret = TinyScript.script("jdbcType1=@java.sql.JDBCType.VARCHAR;" +
                "jdbcType2=java.sql.JDBCType@VARCHAR;" +
                "type1=@java.sql.Types.VARCHAR;" +
                "type2=java.sql.Types@VARCHAR;" +
                "static1=@" + className + ".type;" +
                "static2=" + className + "@type;" +
                "@" + className + ".type=java.sql.JDBCType@VARCHAR;" +
                "static3=@" + className + ".type;" +
                "" + className + "@type=java.sql.JDBCType@BIGINT;" +
                "static4=@" + className + ".type;" +
                "", ctx);
        System.out.println("ret:" + ret);
    }

    public static boolean regexLike(String str, String regex) {
        if (str == regex) {
            return true;
        }
        if (str == null || regex == null) {
            return false;
        }
        return str.matches(regex);
    }

    public static void testImpl() {
        String formula = getFormula();

        Map<String, Object> context = new HashMap<>();
        context.put("str", "1,2,3 4-5-6  7  8  9");
        Object ret = TinyScript.script(formula, context);
        System.out.println(ret);
        System.out.println(context);
    }

    public static void testRaw() {
        String formula = getFormula();
        CommonTokenStream tokens = TinyScript.parseTokens(formula);
        TinyScriptParser parser = new TinyScriptParser(tokens);
        TinyScriptParser.ScriptContext tree = parser.script();
        System.out.println(tree.toStringTree(parser));

        Map<String, Object> context = new HashMap<>();
        context.put("str", "1,2,3 4-5-6  7  8  9");
        Object ret = TinyScript.script(tree, context);
        System.out.println(ret);
        System.out.println(context);
    }

    public static String getFormula() {
        String formula = "num=1+1.125;num2=${num}+10L;tmp=new String(\"@@@\");str=${str}+1;sadd=${str};svl=String.valueOf(1L);slen=${str}.length();srptlen=${str}.repeat(2).length();\n";
        formula += "complex=[{\n" +
                " username: \"123\",\n" +
                " roles: [\"admin\",\"log\"],\n" +
                " status: true,\n" +
                " age: 12,\n" +
                " image: ${str},\n" +
                " len: String.length(),\n" +
                " token: null\n" +
                "}];\n";
        formula += "streq=${str}==${sadd};\n";
        formula += "strneq=${str}==${tmp};\n";
        formula += "numeeq=${num}>=${slen};\n";
        formula += "\n" +
                "if(${num}>4){\n" +
                "    ok=3;\n" +
                "}else if(${num}>3){\n" +
                "    ok=2;\n" +
                "}else{\n" +
                "    ok=1;\n" +
                "};\n";
        return formula;
    }
}
