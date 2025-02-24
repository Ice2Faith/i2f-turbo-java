// Generated from C:/home/dev/java/dev-center/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/script/tiny/rule/TinyScript.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.script.tiny;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TinyScriptParser}.
 */
public interface TinyScriptListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#script}.
	 * @param ctx the parse tree
	 */
	void enterScript(TinyScriptParser.ScriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#script}.
	 * @param ctx the parse tree
	 */
	void exitScript(TinyScriptParser.ScriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#express}.
	 * @param ctx the parse tree
	 */
	void enterExpress(TinyScriptParser.ExpressContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#express}.
	 * @param ctx the parse tree
	 */
	void exitExpress(TinyScriptParser.ExpressContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#ifSegment}.
	 * @param ctx the parse tree
	 */
	void enterIfSegment(TinyScriptParser.IfSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#ifSegment}.
	 * @param ctx the parse tree
	 */
	void exitIfSegment(TinyScriptParser.IfSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#conditionBlock}.
	 * @param ctx the parse tree
	 */
	void enterConditionBlock(TinyScriptParser.ConditionBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#conditionBlock}.
	 * @param ctx the parse tree
	 */
	void exitConditionBlock(TinyScriptParser.ConditionBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#scriptBlock}.
	 * @param ctx the parse tree
	 */
	void enterScriptBlock(TinyScriptParser.ScriptBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#scriptBlock}.
	 * @param ctx the parse tree
	 */
	void exitScriptBlock(TinyScriptParser.ScriptBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#equalValue}.
	 * @param ctx the parse tree
	 */
	void enterEqualValue(TinyScriptParser.EqualValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#equalValue}.
	 * @param ctx the parse tree
	 */
	void exitEqualValue(TinyScriptParser.EqualValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#newInstance}.
	 * @param ctx the parse tree
	 */
	void enterNewInstance(TinyScriptParser.NewInstanceContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#newInstance}.
	 * @param ctx the parse tree
	 */
	void exitNewInstance(TinyScriptParser.NewInstanceContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#invokeFunction}.
	 * @param ctx the parse tree
	 */
	void enterInvokeFunction(TinyScriptParser.InvokeFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#invokeFunction}.
	 * @param ctx the parse tree
	 */
	void exitInvokeFunction(TinyScriptParser.InvokeFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(TinyScriptParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(TinyScriptParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#refCall}.
	 * @param ctx the parse tree
	 */
	void enterRefCall(TinyScriptParser.RefCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#refCall}.
	 * @param ctx the parse tree
	 */
	void exitRefCall(TinyScriptParser.RefCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(TinyScriptParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(TinyScriptParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(TinyScriptParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(TinyScriptParser.ArgumentContext ctx);
	/**
     * Enter a parse tree produced by {@link TinyScriptParser#argumentValue}.
     * @param ctx the parse tree
     */
    void enterArgumentValue(TinyScriptParser.ArgumentValueContext ctx);

    /**
     * Exit a parse tree produced by {@link TinyScriptParser#argumentValue}.
     *
     * @param ctx the parse tree
     */
    void exitArgumentValue(TinyScriptParser.ArgumentValueContext ctx);

    /**
	 * Enter a parse tree produced by {@link TinyScriptParser#constValue}.
	 * @param ctx the parse tree
	 */
	void enterConstValue(TinyScriptParser.ConstValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constValue}.
	 * @param ctx the parse tree
	 */
	void exitConstValue(TinyScriptParser.ConstValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#refValue}.
	 * @param ctx the parse tree
	 */
	void enterRefValue(TinyScriptParser.RefValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#refValue}.
	 * @param ctx the parse tree
	 */
	void exitRefValue(TinyScriptParser.RefValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#constBool}.
	 * @param ctx the parse tree
	 */
	void enterConstBool(TinyScriptParser.ConstBoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constBool}.
	 * @param ctx the parse tree
	 */
	void exitConstBool(TinyScriptParser.ConstBoolContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#constNull}.
	 * @param ctx the parse tree
	 */
	void enterConstNull(TinyScriptParser.ConstNullContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constNull}.
	 * @param ctx the parse tree
	 */
	void exitConstNull(TinyScriptParser.ConstNullContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#constString}.
	 * @param ctx the parse tree
	 */
	void enterConstString(TinyScriptParser.ConstStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constString}.
	 * @param ctx the parse tree
	 */
	void exitConstString(TinyScriptParser.ConstStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#constMultilineString}.
	 * @param ctx the parse tree
	 */
	void enterConstMultilineString(TinyScriptParser.ConstMultilineStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constMultilineString}.
	 * @param ctx the parse tree
	 */
	void exitConstMultilineString(TinyScriptParser.ConstMultilineStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#constRenderString}.
	 * @param ctx the parse tree
	 */
	void enterConstRenderString(TinyScriptParser.ConstRenderStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constRenderString}.
	 * @param ctx the parse tree
	 */
	void exitConstRenderString(TinyScriptParser.ConstRenderStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#decNumber}.
	 * @param ctx the parse tree
	 */
	void enterDecNumber(TinyScriptParser.DecNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#decNumber}.
	 * @param ctx the parse tree
	 */
	void exitDecNumber(TinyScriptParser.DecNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#hexNumber}.
	 * @param ctx the parse tree
	 */
	void enterHexNumber(TinyScriptParser.HexNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#hexNumber}.
	 * @param ctx the parse tree
	 */
	void exitHexNumber(TinyScriptParser.HexNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#otcNumber}.
	 * @param ctx the parse tree
	 */
	void enterOtcNumber(TinyScriptParser.OtcNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#otcNumber}.
	 * @param ctx the parse tree
	 */
	void exitOtcNumber(TinyScriptParser.OtcNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#binNumber}.
	 * @param ctx the parse tree
	 */
	void enterBinNumber(TinyScriptParser.BinNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#binNumber}.
	 * @param ctx the parse tree
	 */
	void exitBinNumber(TinyScriptParser.BinNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void enterJsonValue(TinyScriptParser.JsonValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void exitJsonValue(TinyScriptParser.JsonValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#jsonMapValue}.
	 * @param ctx the parse tree
	 */
	void enterJsonMapValue(TinyScriptParser.JsonMapValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#jsonMapValue}.
	 * @param ctx the parse tree
	 */
	void exitJsonMapValue(TinyScriptParser.JsonMapValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#jsonPairs}.
	 * @param ctx the parse tree
	 */
	void enterJsonPairs(TinyScriptParser.JsonPairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#jsonPairs}.
	 * @param ctx the parse tree
	 */
	void exitJsonPairs(TinyScriptParser.JsonPairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#jsonPair}.
	 * @param ctx the parse tree
	 */
	void enterJsonPair(TinyScriptParser.JsonPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#jsonPair}.
	 * @param ctx the parse tree
	 */
	void exitJsonPair(TinyScriptParser.JsonPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#jsonArrayValue}.
	 * @param ctx the parse tree
	 */
	void enterJsonArrayValue(TinyScriptParser.JsonArrayValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#jsonArrayValue}.
	 * @param ctx the parse tree
	 */
	void exitJsonArrayValue(TinyScriptParser.JsonArrayValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#jsonItemList}.
	 * @param ctx the parse tree
	 */
	void enterJsonItemList(TinyScriptParser.JsonItemListContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#jsonItemList}.
	 * @param ctx the parse tree
	 */
	void exitJsonItemList(TinyScriptParser.JsonItemListContext ctx);
}