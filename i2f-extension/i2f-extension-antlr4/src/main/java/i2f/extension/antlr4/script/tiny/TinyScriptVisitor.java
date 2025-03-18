// Generated from C:/home/dev/java/dev-center/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/script/tiny/rule/TinyScript.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.script.tiny;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TinyScriptParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TinyScriptVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#script}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScript(TinyScriptParser.ScriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#express}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpress(TinyScriptParser.ExpressContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#negtiveSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegtiveSegment(TinyScriptParser.NegtiveSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#debuggerSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDebuggerSegment(TinyScriptParser.DebuggerSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#trySegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrySegment(TinyScriptParser.TrySegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#throwSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowSegment(TinyScriptParser.ThrowSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#tryBodyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryBodyBlock(TinyScriptParser.TryBodyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#catchBodyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchBodyBlock(TinyScriptParser.CatchBodyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#finallyBodyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinallyBodyBlock(TinyScriptParser.FinallyBodyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#classNameBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassNameBlock(TinyScriptParser.ClassNameBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#parenSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenSegment(TinyScriptParser.ParenSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#prefixOperatorSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrefixOperatorSegment(TinyScriptParser.PrefixOperatorSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#controlSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitControlSegment(TinyScriptParser.ControlSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#whileSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileSegment(TinyScriptParser.WhileSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#forSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForSegment(TinyScriptParser.ForSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#foreachSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachSegment(TinyScriptParser.ForeachSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#namingBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamingBlock(TinyScriptParser.NamingBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#ifSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfSegment(TinyScriptParser.IfSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#conditionBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionBlock(TinyScriptParser.ConditionBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#scriptBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScriptBlock(TinyScriptParser.ScriptBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#equalValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualValue(TinyScriptParser.EqualValueContext ctx);
	/**
     * Visit a parse tree produced by {@link TinyScriptParser#extractExpress}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExtractExpress(TinyScriptParser.ExtractExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#extractPairs}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExtractPairs(TinyScriptParser.ExtractPairsContext ctx);

    /**
     * Visit a parse tree produced by {@link TinyScriptParser#extractPair}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExtractPair(TinyScriptParser.ExtractPairContext ctx);

    /**
	 * Visit a parse tree produced by {@link TinyScriptParser#newInstance}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewInstance(TinyScriptParser.NewInstanceContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#invokeFunction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInvokeFunction(TinyScriptParser.InvokeFunctionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(TinyScriptParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#refCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefCall(TinyScriptParser.RefCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(TinyScriptParser.ArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(TinyScriptParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#argumentValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentValue(TinyScriptParser.ArgumentValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstValue(TinyScriptParser.ConstValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#refValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefValue(TinyScriptParser.RefValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constBool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstBool(TinyScriptParser.ConstBoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constNull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstNull(TinyScriptParser.ConstNullContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constClass}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstClass(TinyScriptParser.ConstClassContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstString(TinyScriptParser.ConstStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constMultilineString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstMultilineString(TinyScriptParser.ConstMultilineStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#constRenderString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstRenderString(TinyScriptParser.ConstRenderStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#decNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecNumber(TinyScriptParser.DecNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#hexNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHexNumber(TinyScriptParser.HexNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#otcNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOtcNumber(TinyScriptParser.OtcNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#binNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinNumber(TinyScriptParser.BinNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#jsonValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonValue(TinyScriptParser.JsonValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#jsonMapValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonMapValue(TinyScriptParser.JsonMapValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#jsonPairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonPairs(TinyScriptParser.JsonPairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#jsonPair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonPair(TinyScriptParser.JsonPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#jsonArrayValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonArrayValue(TinyScriptParser.JsonArrayValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TinyScriptParser#jsonItemList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonItemList(TinyScriptParser.JsonItemListContext ctx);
}