// Generated from /extension/antlr4/script/tiny/rule/TinyScript.g4 by ANTLR 4.13.2

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
     * Enter a parse tree produced by {@link TinyScriptParser#segments}.
     * @param ctx the parse tree
     */
    void enterSegments(TinyScriptParser.SegmentsContext ctx);

    /**
     * Exit a parse tree produced by {@link TinyScriptParser#segments}.
     *
     * @param ctx the parse tree
     */
    void exitSegments(TinyScriptParser.SegmentsContext ctx);

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
	 * Enter a parse tree produced by {@link TinyScriptParser#negtiveSegment}.
	 * @param ctx the parse tree
	 */
	void enterNegtiveSegment(TinyScriptParser.NegtiveSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#negtiveSegment}.
	 * @param ctx the parse tree
	 */
	void exitNegtiveSegment(TinyScriptParser.NegtiveSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#debuggerSegment}.
	 * @param ctx the parse tree
	 */
	void enterDebuggerSegment(TinyScriptParser.DebuggerSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#debuggerSegment}.
	 * @param ctx the parse tree
	 */
	void exitDebuggerSegment(TinyScriptParser.DebuggerSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#trySegment}.
	 * @param ctx the parse tree
	 */
	void enterTrySegment(TinyScriptParser.TrySegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#trySegment}.
	 * @param ctx the parse tree
	 */
	void exitTrySegment(TinyScriptParser.TrySegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#throwSegment}.
	 * @param ctx the parse tree
	 */
	void enterThrowSegment(TinyScriptParser.ThrowSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#throwSegment}.
	 * @param ctx the parse tree
	 */
	void exitThrowSegment(TinyScriptParser.ThrowSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#tryBodyBlock}.
	 * @param ctx the parse tree
	 */
	void enterTryBodyBlock(TinyScriptParser.TryBodyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#tryBodyBlock}.
	 * @param ctx the parse tree
	 */
	void exitTryBodyBlock(TinyScriptParser.TryBodyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#catchBodyBlock}.
	 * @param ctx the parse tree
	 */
	void enterCatchBodyBlock(TinyScriptParser.CatchBodyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#catchBodyBlock}.
	 * @param ctx the parse tree
	 */
	void exitCatchBodyBlock(TinyScriptParser.CatchBodyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#finallyBodyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBodyBlock(TinyScriptParser.FinallyBodyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#finallyBodyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBodyBlock(TinyScriptParser.FinallyBodyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#classNameBlock}.
	 * @param ctx the parse tree
	 */
	void enterClassNameBlock(TinyScriptParser.ClassNameBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#classNameBlock}.
	 * @param ctx the parse tree
	 */
	void exitClassNameBlock(TinyScriptParser.ClassNameBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#parenSegment}.
	 * @param ctx the parse tree
	 */
	void enterParenSegment(TinyScriptParser.ParenSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#parenSegment}.
	 * @param ctx the parse tree
	 */
	void exitParenSegment(TinyScriptParser.ParenSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#controlSegment}.
	 * @param ctx the parse tree
	 */
	void enterControlSegment(TinyScriptParser.ControlSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#controlSegment}.
	 * @param ctx the parse tree
	 */
	void exitControlSegment(TinyScriptParser.ControlSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#whileSegment}.
	 * @param ctx the parse tree
	 */
	void enterWhileSegment(TinyScriptParser.WhileSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#whileSegment}.
	 * @param ctx the parse tree
	 */
	void exitWhileSegment(TinyScriptParser.WhileSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#forSegment}.
	 * @param ctx the parse tree
	 */
	void enterForSegment(TinyScriptParser.ForSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#forSegment}.
	 * @param ctx the parse tree
	 */
	void exitForSegment(TinyScriptParser.ForSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#foreachSegment}.
	 * @param ctx the parse tree
	 */
	void enterForeachSegment(TinyScriptParser.ForeachSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#foreachSegment}.
	 * @param ctx the parse tree
	 */
	void exitForeachSegment(TinyScriptParser.ForeachSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#namingBlock}.
	 * @param ctx the parse tree
	 */
	void enterNamingBlock(TinyScriptParser.NamingBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#namingBlock}.
	 * @param ctx the parse tree
	 */
	void exitNamingBlock(TinyScriptParser.NamingBlockContext ctx);
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
	 * Enter a parse tree produced by {@link TinyScriptParser#extractExpress}.
	 * @param ctx the parse tree
	 */
	void enterExtractExpress(TinyScriptParser.ExtractExpressContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#extractExpress}.
	 * @param ctx the parse tree
	 */
	void exitExtractExpress(TinyScriptParser.ExtractExpressContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#extractPairs}.
	 * @param ctx the parse tree
	 */
	void enterExtractPairs(TinyScriptParser.ExtractPairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#extractPairs}.
	 * @param ctx the parse tree
	 */
	void exitExtractPairs(TinyScriptParser.ExtractPairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#extractPair}.
	 * @param ctx the parse tree
	 */
	void enterExtractPair(TinyScriptParser.ExtractPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#extractPair}.
	 * @param ctx the parse tree
	 */
	void exitExtractPair(TinyScriptParser.ExtractPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link TinyScriptParser#staticEnumValue}.
	 * @param ctx the parse tree
	 */
	void enterStaticEnumValue(TinyScriptParser.StaticEnumValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#staticEnumValue}.
	 * @param ctx the parse tree
	 */
	void exitStaticEnumValue(TinyScriptParser.StaticEnumValueContext ctx);
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
	 * Enter a parse tree produced by {@link TinyScriptParser#constClass}.
	 * @param ctx the parse tree
	 */
	void enterConstClass(TinyScriptParser.ConstClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link TinyScriptParser#constClass}.
	 * @param ctx the parse tree
	 */
	void exitConstClass(TinyScriptParser.ConstClassContext ctx);
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