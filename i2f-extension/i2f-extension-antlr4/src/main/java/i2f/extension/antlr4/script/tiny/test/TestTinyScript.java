package i2f.extension.antlr4.script.tiny.test;

import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/20 21:03
 * @desc
 */
public class TestTinyScript {


    public static void main(String[] args) {

        testImpl();

//        testRaw();
    }

    public static void testImpl(){
        String formula=getFormula();

        Map<String, Object> context = new HashMap<>();
        context.put("str", "1,2,3 4-5-6  7  8  9");
        Object ret = TinyScript.script(formula, context);
        System.out.println(ret);
        System.out.println(context);
    }

    public static void testRaw(){
        String formula=getFormula();
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

    public static String getFormula(){
        String formula = "num=1+1.125;num2=${num}+10L;tmp=new String(\"@@@\");str=${str}+1;sadd=${str};svl=String.valueOf(1L);slen=${str}.length();srptlen=${str}.repeat(2).length();\n";
        formula+="complex=[{\n" +
                " username: \"123\",\n" +
                " roles: [\"admin\",\"log\"],\n" +
                " status: true,\n" +
                " age: 12,\n" +
                " image: ${str},\n" +
                " len: String.length(),\n" +
                " token: null\n" +
                "}];\n";
        formula+="streq=${str}==${sadd};\n";
        formula+="strneq=${str}==${tmp};\n";
        formula+="numeeq=${num}>=${slen};\n";
        formula+="\n" +
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
