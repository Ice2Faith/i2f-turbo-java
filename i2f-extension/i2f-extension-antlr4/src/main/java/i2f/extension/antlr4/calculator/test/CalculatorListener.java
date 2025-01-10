package i2f.extension.antlr4.calculator.test;

import i2f.extension.antlr4.calculator.CalculatorBaseListener;
import i2f.extension.antlr4.calculator.CalculatorParser;

/**
 * @author Ice2Faith
 * @date 2025/1/10 11:20
 */
public class CalculatorListener extends CalculatorBaseListener {
    @Override
    public void enterNumber(CalculatorParser.NumberContext ctx) {

        System.out.println("ok");
    }
}
