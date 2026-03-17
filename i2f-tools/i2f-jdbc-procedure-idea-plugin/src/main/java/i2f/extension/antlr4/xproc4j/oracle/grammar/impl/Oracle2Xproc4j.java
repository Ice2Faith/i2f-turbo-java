package i2f.extension.antlr4.xproc4j.oracle.grammar.impl;

import i2f.extension.antlr4.xproc4j.oracle.grammar.OracleGrammarLexer;
import i2f.extension.antlr4.xproc4j.oracle.grammar.OracleGrammarParser;
import i2f.extension.antlr4.xproc4j.oracle.grammar.OracleGrammarVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * @author Ice2Faith
 * @date 2026/3/17 15:01
 * @desc
 */
public class Oracle2Xproc4j {


    public static String convert(String formula) {
        OracleGrammarParser.ScriptContext tree = parse(formula);
        return convert(tree);
    }

    public static String convert(OracleGrammarParser.ScriptContext tree) {
        OracleGrammarVisitor<String> visitor = new Oracle2Xproc4jOracleGrammarVisitor();
        String ret = visitor.visit(tree);
        return ret;
    }

    public static OracleGrammarParser.ScriptContext parse(String formula) {
        if (formula != null) {
            formula = formula.trim();
        }
        CommonTokenStream tokens = parseTokens(formula);
        OracleGrammarParser parser = new OracleGrammarParser(tokens);
        parser.setErrorHandler(new Oracle2Xproc4jErrorStrategy());
        OracleGrammarParser.ScriptContext tree = parser.script();
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        OracleGrammarLexer lexer = new OracleGrammarLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }
}
