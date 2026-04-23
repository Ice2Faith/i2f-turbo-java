// Generated from /extension/antlr4/script/funic/rule/Funic.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.script.funic.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FunicParser}.
 */
public interface FunicListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link FunicParser#root}.
     *
     * @param ctx the parse tree
     */
    void enterRoot(FunicParser.RootContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#root}.
     *
     * @param ctx the parse tree
     */
    void exitRoot(FunicParser.RootContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#script}.
     *
     * @param ctx the parse tree
     */
    void enterScript(FunicParser.ScriptContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#script}.
     *
     * @param ctx the parse tree
     */
    void exitScript(FunicParser.ScriptContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#express}.
     *
     * @param ctx the parse tree
     */
    void enterExpress(FunicParser.ExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#express}.
     *
     * @param ctx the parse tree
     */
    void exitExpress(FunicParser.ExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#logicalLinkOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterLogicalLinkOperatorPart(FunicParser.LogicalLinkOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#logicalLinkOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitLogicalLinkOperatorPart(FunicParser.LogicalLinkOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#compareOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterCompareOperatorPart(FunicParser.CompareOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#compareOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitCompareOperatorPart(FunicParser.CompareOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#bitOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterBitOperatorPart(FunicParser.BitOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#bitOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitBitOperatorPart(FunicParser.BitOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#mathAddSubOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterMathAddSubOperatorPart(FunicParser.MathAddSubOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#mathAddSubOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitMathAddSubOperatorPart(FunicParser.MathAddSubOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#mathMulDivOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterMathMulDivOperatorPart(FunicParser.MathMulDivOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#mathMulDivOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitMathMulDivOperatorPart(FunicParser.MathMulDivOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#incrDecrPrefixOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterIncrDecrPrefixOperatorPart(FunicParser.IncrDecrPrefixOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#incrDecrPrefixOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitIncrDecrPrefixOperatorPart(FunicParser.IncrDecrPrefixOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#prefixOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void enterPrefixOperatorPart(FunicParser.PrefixOperatorPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#prefixOperatorPart}.
     *
     * @param ctx the parse tree
     */
    void exitPrefixOperatorPart(FunicParser.PrefixOperatorPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#pipelineFunctionExpress}.
     *
     * @param ctx the parse tree
     */
    void enterPipelineFunctionExpress(FunicParser.PipelineFunctionExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#pipelineFunctionExpress}.
     *
     * @param ctx the parse tree
     */
    void exitPipelineFunctionExpress(FunicParser.PipelineFunctionExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#synchronizedExpress}.
     *
     * @param ctx the parse tree
     */
    void enterSynchronizedExpress(FunicParser.SynchronizedExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#synchronizedExpress}.
     *
     * @param ctx the parse tree
     */
    void exitSynchronizedExpress(FunicParser.SynchronizedExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#lambdaExpress}.
     *
     * @param ctx the parse tree
     */
    void enterLambdaExpress(FunicParser.LambdaExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#lambdaExpress}.
     *
     * @param ctx the parse tree
     */
    void exitLambdaExpress(FunicParser.LambdaExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#importExpress}.
     *
     * @param ctx the parse tree
     */
    void enterImportExpress(FunicParser.ImportExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#importExpress}.
     *
     * @param ctx the parse tree
     */
    void exitImportExpress(FunicParser.ImportExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#goRunExpress}.
     *
     * @param ctx the parse tree
     */
    void enterGoRunExpress(FunicParser.GoRunExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#goRunExpress}.
     *
     * @param ctx the parse tree
     */
    void exitGoRunExpress(FunicParser.GoRunExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#awaitExpress}.
     *
     * @param ctx the parse tree
     */
    void enterAwaitExpress(FunicParser.AwaitExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#awaitExpress}.
     *
     * @param ctx the parse tree
     */
    void exitAwaitExpress(FunicParser.AwaitExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#functionDeclareExpress}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionDeclareExpress(FunicParser.FunctionDeclareExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#functionDeclareExpress}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionDeclareExpress(FunicParser.FunctionDeclareExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#functionDeclareReturn}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionDeclareReturn(FunicParser.FunctionDeclareReturnContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#functionDeclareReturn}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionDeclareReturn(FunicParser.FunctionDeclareReturnContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#functionDeclareParameters}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionDeclareParameters(FunicParser.FunctionDeclareParametersContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#functionDeclareParameters}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionDeclareParameters(FunicParser.FunctionDeclareParametersContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#functionParameter}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionParameter(FunicParser.FunctionParameterContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#functionParameter}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionParameter(FunicParser.FunctionParameterContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#tryCatchFinallyExpress}.
     *
     * @param ctx the parse tree
     */
    void enterTryCatchFinallyExpress(FunicParser.TryCatchFinallyExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#tryCatchFinallyExpress}.
     *
     * @param ctx the parse tree
     */
    void exitTryCatchFinallyExpress(FunicParser.TryCatchFinallyExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#catchBlock}.
     *
     * @param ctx the parse tree
     */
    void enterCatchBlock(FunicParser.CatchBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#catchBlock}.
     *
     * @param ctx the parse tree
     */
    void exitCatchBlock(FunicParser.CatchBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#throwExpress}.
     *
     * @param ctx the parse tree
     */
    void enterThrowExpress(FunicParser.ThrowExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#throwExpress}.
     *
     * @param ctx the parse tree
     */
    void exitThrowExpress(FunicParser.ThrowExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#returnExpress}.
     *
     * @param ctx the parse tree
     */
    void enterReturnExpress(FunicParser.ReturnExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#returnExpress}.
     *
     * @param ctx the parse tree
     */
    void exitReturnExpress(FunicParser.ReturnExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#continueExpress}.
     *
     * @param ctx the parse tree
     */
    void enterContinueExpress(FunicParser.ContinueExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#continueExpress}.
     *
     * @param ctx the parse tree
     */
    void exitContinueExpress(FunicParser.ContinueExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#breakExpress}.
     *
     * @param ctx the parse tree
     */
    void enterBreakExpress(FunicParser.BreakExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#breakExpress}.
     *
     * @param ctx the parse tree
     */
    void exitBreakExpress(FunicParser.BreakExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#forRangeExpress}.
     *
     * @param ctx the parse tree
     */
    void enterForRangeExpress(FunicParser.ForRangeExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#forRangeExpress}.
     *
     * @param ctx the parse tree
     */
    void exitForRangeExpress(FunicParser.ForRangeExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#forLoopExpress}.
     *
     * @param ctx the parse tree
     */
    void enterForLoopExpress(FunicParser.ForLoopExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#forLoopExpress}.
     *
     * @param ctx the parse tree
     */
    void exitForLoopExpress(FunicParser.ForLoopExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#foreachExpress}.
     *
     * @param ctx the parse tree
     */
    void enterForeachExpress(FunicParser.ForeachExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#foreachExpress}.
     *
     * @param ctx the parse tree
     */
    void exitForeachExpress(FunicParser.ForeachExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#doWhileExpress}.
     *
     * @param ctx the parse tree
     */
    void enterDoWhileExpress(FunicParser.DoWhileExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#doWhileExpress}.
     *
     * @param ctx the parse tree
     */
    void exitDoWhileExpress(FunicParser.DoWhileExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#whileExpress}.
     *
     * @param ctx the parse tree
     */
    void enterWhileExpress(FunicParser.WhileExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#whileExpress}.
     *
     * @param ctx the parse tree
     */
    void exitWhileExpress(FunicParser.WhileExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#ifElseExpress}.
     *
     * @param ctx the parse tree
     */
    void enterIfElseExpress(FunicParser.IfElseExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#ifElseExpress}.
     *
     * @param ctx the parse tree
     */
    void exitIfElseExpress(FunicParser.IfElseExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#conditionBlock}.
     *
     * @param ctx the parse tree
     */
    void enterConditionBlock(FunicParser.ConditionBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#conditionBlock}.
     *
     * @param ctx the parse tree
     */
    void exitConditionBlock(FunicParser.ConditionBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#scriptBlock}.
     *
     * @param ctx the parse tree
     */
    void enterScriptBlock(FunicParser.ScriptBlockContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#scriptBlock}.
     *
     * @param ctx the parse tree
     */
    void exitScriptBlock(FunicParser.ScriptBlockContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#castAsRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterCastAsRightPart(FunicParser.CastAsRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#castAsRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitCastAsRightPart(FunicParser.CastAsRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#mapValueExpress}.
     *
     * @param ctx the parse tree
     */
    void enterMapValueExpress(FunicParser.MapValueExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#mapValueExpress}.
     *
     * @param ctx the parse tree
     */
    void exitMapValueExpress(FunicParser.MapValueExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#unpackMapExpress}.
     *
     * @param ctx the parse tree
     */
    void enterUnpackMapExpress(FunicParser.UnpackMapExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#unpackMapExpress}.
     *
     * @param ctx the parse tree
     */
    void exitUnpackMapExpress(FunicParser.UnpackMapExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#keyValueExpress}.
     *
     * @param ctx the parse tree
     */
    void enterKeyValueExpress(FunicParser.KeyValueExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#keyValueExpress}.
     *
     * @param ctx the parse tree
     */
    void exitKeyValueExpress(FunicParser.KeyValueExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#thirdOperateRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterThirdOperateRightPart(FunicParser.ThirdOperateRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#thirdOperateRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitThirdOperateRightPart(FunicParser.ThirdOperateRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#instanceFieldValueRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterInstanceFieldValueRightPart(FunicParser.InstanceFieldValueRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#instanceFieldValueRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitInstanceFieldValueRightPart(FunicParser.InstanceFieldValueRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#circleExpress}.
     *
     * @param ctx the parse tree
     */
    void enterCircleExpress(FunicParser.CircleExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#circleExpress}.
     *
     * @param ctx the parse tree
     */
    void exitCircleExpress(FunicParser.CircleExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#newArrayExpress}.
     *
     * @param ctx the parse tree
     */
    void enterNewArrayExpress(FunicParser.NewArrayExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#newArrayExpress}.
     *
     * @param ctx the parse tree
     */
    void exitNewArrayExpress(FunicParser.NewArrayExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#newInstanceExpress}.
     *
     * @param ctx the parse tree
     */
    void enterNewInstanceExpress(FunicParser.NewInstanceExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#newInstanceExpress}.
     *
     * @param ctx the parse tree
     */
    void exitNewInstanceExpress(FunicParser.NewInstanceExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#instanceFunctionCallRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterInstanceFunctionCallRightPart(FunicParser.InstanceFunctionCallRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#instanceFunctionCallRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitInstanceFunctionCallRightPart(FunicParser.InstanceFunctionCallRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#globalFunctionCall}.
     *
     * @param ctx the parse tree
     */
    void enterGlobalFunctionCall(FunicParser.GlobalFunctionCallContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#globalFunctionCall}.
     *
     * @param ctx the parse tree
     */
    void exitGlobalFunctionCall(FunicParser.GlobalFunctionCallContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#squareQuoteRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterSquareQuoteRightPart(FunicParser.SquareQuoteRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#squareQuoteRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitSquareQuoteRightPart(FunicParser.SquareQuoteRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#factorPercentRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterFactorPercentRightPart(FunicParser.FactorPercentRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#factorPercentRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitFactorPercentRightPart(FunicParser.FactorPercentRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#incrDecrAfterRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterIncrDecrAfterRightPart(FunicParser.IncrDecrAfterRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#incrDecrAfterRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitIncrDecrAfterRightPart(FunicParser.IncrDecrAfterRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#assignRightPart}.
     *
     * @param ctx the parse tree
     */
    void enterAssignRightPart(FunicParser.AssignRightPartContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#assignRightPart}.
     *
     * @param ctx the parse tree
     */
    void exitAssignRightPart(FunicParser.AssignRightPartContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#staticFieldValue}.
     *
     * @param ctx the parse tree
     */
    void enterStaticFieldValue(FunicParser.StaticFieldValueContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#staticFieldValue}.
     *
     * @param ctx the parse tree
     */
    void exitStaticFieldValue(FunicParser.StaticFieldValueContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#staticFunctionCall}.
     *
     * @param ctx the parse tree
     */
    void enterStaticFunctionCall(FunicParser.StaticFunctionCallContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#staticFunctionCall}.
     *
     * @param ctx the parse tree
     */
    void exitStaticFunctionCall(FunicParser.StaticFunctionCallContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#functionArguments}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionArguments(FunicParser.FunctionArgumentsContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#functionArguments}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionArguments(FunicParser.FunctionArgumentsContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#functionArgument}.
     *
     * @param ctx the parse tree
     */
    void enterFunctionArgument(FunicParser.FunctionArgumentContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#functionArgument}.
     *
     * @param ctx the parse tree
     */
    void exitFunctionArgument(FunicParser.FunctionArgumentContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#listValueExpress}.
     *
     * @param ctx the parse tree
     */
    void enterListValueExpress(FunicParser.ListValueExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#listValueExpress}.
     *
     * @param ctx the parse tree
     */
    void exitListValueExpress(FunicParser.ListValueExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#unpackListExpress}.
     *
     * @param ctx the parse tree
     */
    void enterUnpackListExpress(FunicParser.UnpackListExpressContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#unpackListExpress}.
     *
     * @param ctx the parse tree
     */
    void exitUnpackListExpress(FunicParser.UnpackListExpressContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#fullName}.
     *
     * @param ctx the parse tree
     */
    void enterFullName(FunicParser.FullNameContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#fullName}.
     *
     * @param ctx the parse tree
     */
    void exitFullName(FunicParser.FullNameContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#typeClass}.
     *
     * @param ctx the parse tree
     */
    void enterTypeClass(FunicParser.TypeClassContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#typeClass}.
     *
     * @param ctx the parse tree
     */
    void exitTypeClass(FunicParser.TypeClassContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#typeReference}.
     *
     * @param ctx the parse tree
     */
    void enterTypeReference(FunicParser.TypeReferenceContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#typeReference}.
     *
     * @param ctx the parse tree
     */
    void exitTypeReference(FunicParser.TypeReferenceContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#typeMember}.
     *
     * @param ctx the parse tree
     */
    void enterTypeMember(FunicParser.TypeMemberContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#typeMember}.
     *
     * @param ctx the parse tree
     */
    void exitTypeMember(FunicParser.TypeMemberContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#valueSegment}.
     *
     * @param ctx the parse tree
     */
    void enterValueSegment(FunicParser.ValueSegmentContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#valueSegment}.
     *
     * @param ctx the parse tree
     */
    void exitValueSegment(FunicParser.ValueSegmentContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#variableValue}.
     *
     * @param ctx the parse tree
     */
    void enterVariableValue(FunicParser.VariableValueContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#variableValue}.
     *
     * @param ctx the parse tree
     */
    void exitVariableValue(FunicParser.VariableValueContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constValue}.
     *
     * @param ctx the parse tree
     */
    void enterConstValue(FunicParser.ConstValueContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constValue}.
     *
     * @param ctx the parse tree
     */
    void exitConstValue(FunicParser.ConstValueContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constCharSequence}.
     *
     * @param ctx the parse tree
     */
    void enterConstCharSequence(FunicParser.ConstCharSequenceContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constCharSequence}.
     *
     * @param ctx the parse tree
     */
    void exitConstCharSequence(FunicParser.ConstCharSequenceContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constString}.
     *
     * @param ctx the parse tree
     */
    void enterConstString(FunicParser.ConstStringContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constString}.
     *
     * @param ctx the parse tree
     */
    void exitConstString(FunicParser.ConstStringContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constRenderString}.
     *
     * @param ctx the parse tree
     */
    void enterConstRenderString(FunicParser.ConstRenderStringContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constRenderString}.
     *
     * @param ctx the parse tree
     */
    void exitConstRenderString(FunicParser.ConstRenderStringContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constMultiString}.
     *
     * @param ctx the parse tree
     */
    void enterConstMultiString(FunicParser.ConstMultiStringContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constMultiString}.
     *
     * @param ctx the parse tree
     */
    void exitConstMultiString(FunicParser.ConstMultiStringContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constNumeric}.
     *
     * @param ctx the parse tree
     */
    void enterConstNumeric(FunicParser.ConstNumericContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constNumeric}.
     *
     * @param ctx the parse tree
     */
    void exitConstNumeric(FunicParser.ConstNumericContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constNumber}.
     *
     * @param ctx the parse tree
     */
    void enterConstNumber(FunicParser.ConstNumberContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constNumber}.
     *
     * @param ctx the parse tree
     */
    void exitConstNumber(FunicParser.ConstNumberContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constFloat}.
     *
     * @param ctx the parse tree
     */
    void enterConstFloat(FunicParser.ConstFloatContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constFloat}.
     *
     * @param ctx the parse tree
     */
    void exitConstFloat(FunicParser.ConstFloatContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constBoolean}.
     *
     * @param ctx the parse tree
     */
    void enterConstBoolean(FunicParser.ConstBooleanContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constBoolean}.
     *
     * @param ctx the parse tree
     */
    void exitConstBoolean(FunicParser.ConstBooleanContext ctx);

    /**
     * Enter a parse tree produced by {@link FunicParser#constNull}.
     *
     * @param ctx the parse tree
     */
    void enterConstNull(FunicParser.ConstNullContext ctx);

    /**
     * Exit a parse tree produced by {@link FunicParser#constNull}.
     *
     * @param ctx the parse tree
     */
    void exitConstNull(FunicParser.ConstNullContext ctx);
}