// Generated from D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/calculator/rule/Calculator.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.calculator;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CalculatorParser}.
 */
public interface CalculatorListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link CalculatorParser#eval}.
     *
     * @param ctx the parse tree
     */
    void enterEval(CalculatorParser.EvalContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#eval}.
     *
     * @param ctx the parse tree
     */
    void exitEval(CalculatorParser.EvalContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#number}.
     *
     * @param ctx the parse tree
     */
    void enterNumber(CalculatorParser.NumberContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#number}.
     *
     * @param ctx the parse tree
     */
    void exitNumber(CalculatorParser.NumberContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#expr}.
     *
     * @param ctx the parse tree
     */
    void enterExpr(CalculatorParser.ExprContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#expr}.
     *
     * @param ctx the parse tree
     */
    void exitExpr(CalculatorParser.ExprContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#convertor}.
     *
     * @param ctx the parse tree
     */
    void enterConvertor(CalculatorParser.ConvertorContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#convertor}.
     *
     * @param ctx the parse tree
     */
    void exitConvertor(CalculatorParser.ConvertorContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#bracket}.
     *
     * @param ctx the parse tree
     */
    void enterBracket(CalculatorParser.BracketContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#bracket}.
     *
     * @param ctx the parse tree
     */
    void exitBracket(CalculatorParser.BracketContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#prefixOperator}.
     *
     * @param ctx the parse tree
     */
    void enterPrefixOperator(CalculatorParser.PrefixOperatorContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#prefixOperator}.
     *
     * @param ctx the parse tree
     */
    void exitPrefixOperator(CalculatorParser.PrefixOperatorContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#suffixOperator}.
     *
     * @param ctx the parse tree
     */
    void enterSuffixOperator(CalculatorParser.SuffixOperatorContext ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#suffixOperator}.
     *
     * @param ctx the parse tree
     */
    void exitSuffixOperator(CalculatorParser.SuffixOperatorContext ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#operatorV5}.
     *
     * @param ctx the parse tree
     */
    void enterOperatorV5(CalculatorParser.OperatorV5Context ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#operatorV5}.
     *
     * @param ctx the parse tree
     */
    void exitOperatorV5(CalculatorParser.OperatorV5Context ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#operatorV4}.
     *
     * @param ctx the parse tree
     */
    void enterOperatorV4(CalculatorParser.OperatorV4Context ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#operatorV4}.
     *
     * @param ctx the parse tree
     */
    void exitOperatorV4(CalculatorParser.OperatorV4Context ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#operatorV3}.
     *
     * @param ctx the parse tree
     */
    void enterOperatorV3(CalculatorParser.OperatorV3Context ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#operatorV3}.
     *
     * @param ctx the parse tree
     */
    void exitOperatorV3(CalculatorParser.OperatorV3Context ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#operatorV2}.
     *
     * @param ctx the parse tree
     */
    void enterOperatorV2(CalculatorParser.OperatorV2Context ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#operatorV2}.
     *
     * @param ctx the parse tree
     */
    void exitOperatorV2(CalculatorParser.OperatorV2Context ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#operatorV1}.
     *
     * @param ctx the parse tree
     */
    void enterOperatorV1(CalculatorParser.OperatorV1Context ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#operatorV1}.
     *
     * @param ctx the parse tree
     */
    void exitOperatorV1(CalculatorParser.OperatorV1Context ctx);

    /**
     * Enter a parse tree produced by {@link CalculatorParser#operatorV0}.
     *
     * @param ctx the parse tree
     */
    void enterOperatorV0(CalculatorParser.OperatorV0Context ctx);

    /**
     * Exit a parse tree produced by {@link CalculatorParser#operatorV0}.
     *
     * @param ctx the parse tree
     */
    void exitOperatorV0(CalculatorParser.OperatorV0Context ctx);
}