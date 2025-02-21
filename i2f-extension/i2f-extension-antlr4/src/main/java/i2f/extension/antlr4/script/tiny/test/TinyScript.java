package i2f.extension.antlr4.script.tiny.test;

import i2f.extension.antlr4.script.tiny.TinyScriptLexer;
import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.TinyScriptVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/10 22:07
 */
public class TinyScript {
    public static Object script(String formula,Map<String,Object> context) {
        TinyScriptParser.ScriptContext tree = parse(formula);
        return script(tree,context);
    }

    public static Object script(TinyScriptParser.ScriptContext tree, Map<String, Object> context) {
        TinyScriptVisitor<Object> visitor = new TinyScriptVisitorImpl(context);
        Object ret = visitor.visit(tree);
        return ret;
    }

    public static TinyScriptParser.ScriptContext parse(String formula) {
        CommonTokenStream tokens = parseTokens(formula);
        TinyScriptParser parser = new TinyScriptParser(tokens);
        TinyScriptParser.ScriptContext tree = parser.script();
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        TinyScriptLexer lexer = new TinyScriptLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }

}
