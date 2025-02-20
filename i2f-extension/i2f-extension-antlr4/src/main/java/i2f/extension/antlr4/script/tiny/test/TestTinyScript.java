package i2f.extension.antlr4.script.tiny.test;

import i2f.extension.antlr4.script.tiny.TinyScriptLexer;
import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.TinyScriptVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/20 21:03
 * @desc
 */
public class TestTinyScript {
    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        TinyScriptLexer lexer = new TinyScriptLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }

    public static Object script(TinyScriptParser.ScriptContext tree, Map<String, Object> context) {
        TinyScriptVisitor<Object> visitor = new TinyScriptVisitorImpl(context);
        Object ret = visitor.visit(tree);
        return ret;
    }

    public static void main(String[] args) {
        String formula = "tmp=new String(\"@@@\");str=${str}+1;replace(${str},\"\\\\s+\",\" \");String.valueof(1L);org.apache.StringUtils.trimAll(${str},\"\\\\s+\");${str}.length();${str}.repeat(2).length();\n";
        CommonTokenStream tokens = parseTokens(formula);
        TinyScriptParser parser = new TinyScriptParser(tokens);
        TinyScriptParser.ScriptContext tree = parser.script();
        System.out.println(tree.toStringTree(parser));

        Map<String, Object> context = new HashMap<>();
        context.put("str", "1,2,3 4-5-6  7  8  9");
        Object ret = script(tree, context);
        System.out.println(ret);
    }
}
