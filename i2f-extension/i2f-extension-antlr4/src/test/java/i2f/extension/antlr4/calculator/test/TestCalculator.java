package i2f.extension.antlr4.calculator.test;

import i2f.extension.antlr4.calculator.CalculatorParser;
import i2f.extension.antlr4.calculator.impl.Calculator;
import org.antlr.v4.runtime.CommonTokenStream;

import java.math.BigDecimal;

/**
 * @author Ice2Faith
 * @date 2025/1/10 10:10
 */
public class TestCalculator {
    public static void main(String[] args) {
        testImpl();

//        testRaw();
    }

    public static void testImpl(){
        String formula = "avg(4+3,5,6)";
        BigDecimal ret = Calculator.eval(formula);
        System.out.println(ret);
    }

    public static void testRaw(){
        String formula = "avg(4+3,5,6)";
        CommonTokenStream tokens = Calculator.parseTokens(formula);
        CalculatorParser parser = new CalculatorParser(tokens);
        CalculatorParser.EvalContext tree = parser.eval();
        System.out.println(tree.toStringTree(parser));

        Object ret = Calculator.eval(tree);
        System.out.println(ret);
    }
}
