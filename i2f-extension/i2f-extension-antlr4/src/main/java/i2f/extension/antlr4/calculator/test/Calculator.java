package i2f.extension.antlr4.calculator.test;

import i2f.extension.antlr4.calculator.CalculatorLexer;
import i2f.extension.antlr4.calculator.CalculatorParser;
import i2f.extension.antlr4.calculator.CalculatorVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.math.BigDecimal;

/**
 * @author Ice2Faith
 * @date 2025/1/10 22:07
 */
public class Calculator {
    public static BigDecimal eval(String formula) {
        CalculatorParser.EvalContext tree = parse(formula);
        return eval(tree);
    }

    public static BigDecimal eval(CalculatorParser.EvalContext tree) {
        CalculatorVisitor<Object> visitor = new CalculatorVisitorImpl();
        Object ret = visitor.visit(tree);
        return (BigDecimal) ret;
    }

    public static CalculatorParser.EvalContext parse(String formula) {
        CommonTokenStream tokens = parseTokens(formula);
        CalculatorParser parser = new CalculatorParser(tokens);
        CalculatorParser.EvalContext tree = parser.eval();
        return tree;
    }

    public static CommonTokenStream parseTokens(String formula) {
        ANTLRInputStream input = new ANTLRInputStream(formula);
        CalculatorLexer lexer = new CalculatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return tokens;
    }
}
