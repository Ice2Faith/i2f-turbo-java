// Generated from C:/home/dev/java/dev-center/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/script/tiny/rule/TinyScript.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.script.tiny;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TinyScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface TinyScriptVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link TinyScriptParser#script}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitScript(TinyScriptParser.ScriptContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#express}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpress(TinyScriptParser.ExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#equalValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEqualValue(TinyScriptParser.EqualValueContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#newInstance}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNewInstance(TinyScriptParser.NewInstanceContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#invokeFunction}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInvokeFunction(TinyScriptParser.InvokeFunctionContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#functionCall}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionCall(TinyScriptParser.FunctionCallContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#refCall}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRefCall(TinyScriptParser.RefCallContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#argumentList}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitArgumentList(TinyScriptParser.ArgumentListContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#argument}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitArgument(TinyScriptParser.ArgumentContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#constValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstValue(TinyScriptParser.ConstValueContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#refValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRefValue(TinyScriptParser.RefValueContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#constBool}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstBool(TinyScriptParser.ConstBoolContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#constString}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstString(TinyScriptParser.ConstStringContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#decNumber}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDecNumber(TinyScriptParser.DecNumberContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#hexNumber}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitHexNumber(TinyScriptParser.HexNumberContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#otcNumber}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOtcNumber(TinyScriptParser.OtcNumberContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#binNumber}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBinNumber(TinyScriptParser.BinNumberContext ctx);
}