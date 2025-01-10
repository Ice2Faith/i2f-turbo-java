package i2f.extension.antlr4.calculator.test;

import i2f.extension.antlr4.calculator.CalculatorLexer;
import i2f.extension.antlr4.calculator.CalculatorParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author Ice2Faith
 * @date 2025/1/10 10:10
 */
public class TestCalculator {
    public static void main(String[] args) {
        String sql = "1+1>(2+3==4)";
        ANTLRInputStream input = new ANTLRInputStream(sql);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        CalculatorParser.EvalContext tree = parser.eval();
        System.out.println(tree.toStringTree(parser));

        ParseTreeListener listener = new CalculatorListener();
        ParseTreeWalker.DEFAULT.walk(listener, tree);
    }
}
