package i2f.extension.antlr4.calculator.test;

import i2f.extension.antlr4.calculator.CalculatorLexer;
import i2f.extension.antlr4.calculator.CalculatorParser;
import i2f.extension.antlr4.calculator.CalculatorVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * @author Ice2Faith
 * @date 2025/1/10 10:10
 */
public class TestCalculator {
    public static void main(String[] args) {
        String sql = "avg(4+3,5,6)";
        ANTLRInputStream input = new ANTLRInputStream(sql);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CalculatorParser parser = new CalculatorParser(tokens);
        CalculatorParser.EvalContext tree = parser.eval();
        System.out.println(tree.toStringTree(parser));

        CalculatorVisitor visitor = new CalculatorVisitorImpl();
        Object ret = visitor.visit(tree);
        System.out.println(ret);
    }
}
