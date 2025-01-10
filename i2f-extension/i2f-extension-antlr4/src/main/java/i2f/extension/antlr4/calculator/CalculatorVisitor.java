// Generated from D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/calculator/rule/Calculator.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.calculator;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CalculatorParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface CalculatorVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link CalculatorParser#eval}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEval(CalculatorParser.EvalContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#number}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumber(CalculatorParser.NumberContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#expr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpr(CalculatorParser.ExprContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#convertor}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConvertor(CalculatorParser.ConvertorContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#bracket}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBracket(CalculatorParser.BracketContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#prefixOperator}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPrefixOperator(CalculatorParser.PrefixOperatorContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#suffixOperator}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSuffixOperator(CalculatorParser.SuffixOperatorContext ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#operatorV5}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOperatorV5(CalculatorParser.OperatorV5Context ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#operatorV4}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOperatorV4(CalculatorParser.OperatorV4Context ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#operatorV3}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOperatorV3(CalculatorParser.OperatorV3Context ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#operatorV2}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOperatorV2(CalculatorParser.OperatorV2Context ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#operatorV1}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOperatorV1(CalculatorParser.OperatorV1Context ctx);

    /**
     * Visit a parse tree produced by {@link CalculatorParser#operatorV0}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOperatorV0(CalculatorParser.OperatorV0Context ctx);
}