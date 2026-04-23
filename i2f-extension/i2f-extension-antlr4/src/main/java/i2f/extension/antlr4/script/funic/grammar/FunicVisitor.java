// Generated from /extension/antlr4/script/funic/rule/Funic.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.script.funic.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FunicParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface FunicVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link FunicParser#root}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRoot(FunicParser.RootContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#script}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitScript(FunicParser.ScriptContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#express}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpress(FunicParser.ExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#logicalLinkOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLogicalLinkOperatorPart(FunicParser.LogicalLinkOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#compareOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompareOperatorPart(FunicParser.CompareOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#bitOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBitOperatorPart(FunicParser.BitOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#mathAddSubOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMathAddSubOperatorPart(FunicParser.MathAddSubOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#mathMulDivOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMathMulDivOperatorPart(FunicParser.MathMulDivOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#incrDecrPrefixOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIncrDecrPrefixOperatorPart(FunicParser.IncrDecrPrefixOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#prefixOperatorPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPrefixOperatorPart(FunicParser.PrefixOperatorPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#pipelineFunctionExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPipelineFunctionExpress(FunicParser.PipelineFunctionExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#synchronizedExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSynchronizedExpress(FunicParser.SynchronizedExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#lambdaExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLambdaExpress(FunicParser.LambdaExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#importExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitImportExpress(FunicParser.ImportExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#goRunExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGoRunExpress(FunicParser.GoRunExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#awaitExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAwaitExpress(FunicParser.AwaitExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#functionDeclareExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionDeclareExpress(FunicParser.FunctionDeclareExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#functionDeclareReturn}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionDeclareReturn(FunicParser.FunctionDeclareReturnContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#functionDeclareParameters}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionDeclareParameters(FunicParser.FunctionDeclareParametersContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#functionParameter}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionParameter(FunicParser.FunctionParameterContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#tryCatchFinallyExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTryCatchFinallyExpress(FunicParser.TryCatchFinallyExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#catchBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCatchBlock(FunicParser.CatchBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#throwExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitThrowExpress(FunicParser.ThrowExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#returnExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitReturnExpress(FunicParser.ReturnExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#continueExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitContinueExpress(FunicParser.ContinueExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#breakExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBreakExpress(FunicParser.BreakExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#forRangeExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitForRangeExpress(FunicParser.ForRangeExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#forLoopExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitForLoopExpress(FunicParser.ForLoopExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#foreachExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitForeachExpress(FunicParser.ForeachExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#doWhileExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDoWhileExpress(FunicParser.DoWhileExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#whileExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitWhileExpress(FunicParser.WhileExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#ifElseExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIfElseExpress(FunicParser.IfElseExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#conditionBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConditionBlock(FunicParser.ConditionBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#scriptBlock}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitScriptBlock(FunicParser.ScriptBlockContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#castAsRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCastAsRightPart(FunicParser.CastAsRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#mapValueExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMapValueExpress(FunicParser.MapValueExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#unpackMapExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnpackMapExpress(FunicParser.UnpackMapExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#keyValueExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitKeyValueExpress(FunicParser.KeyValueExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#thirdOperateRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitThirdOperateRightPart(FunicParser.ThirdOperateRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#instanceFieldValueRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInstanceFieldValueRightPart(FunicParser.InstanceFieldValueRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#circleExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCircleExpress(FunicParser.CircleExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#newArrayExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNewArrayExpress(FunicParser.NewArrayExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#newInstanceExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNewInstanceExpress(FunicParser.NewInstanceExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#instanceFunctionCallRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInstanceFunctionCallRightPart(FunicParser.InstanceFunctionCallRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#globalFunctionCall}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitGlobalFunctionCall(FunicParser.GlobalFunctionCallContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#squareQuoteRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSquareQuoteRightPart(FunicParser.SquareQuoteRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#factorPercentRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFactorPercentRightPart(FunicParser.FactorPercentRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#incrDecrAfterRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIncrDecrAfterRightPart(FunicParser.IncrDecrAfterRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#assignRightPart}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAssignRightPart(FunicParser.AssignRightPartContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#staticFieldValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStaticFieldValue(FunicParser.StaticFieldValueContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#staticFunctionCall}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStaticFunctionCall(FunicParser.StaticFunctionCallContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#functionArguments}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionArguments(FunicParser.FunctionArgumentsContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#functionArgument}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionArgument(FunicParser.FunctionArgumentContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#listValueExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitListValueExpress(FunicParser.ListValueExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#unpackListExpress}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnpackListExpress(FunicParser.UnpackListExpressContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#fullName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFullName(FunicParser.FullNameContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#typeClass}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypeClass(FunicParser.TypeClassContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#typeReference}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypeReference(FunicParser.TypeReferenceContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#typeMember}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypeMember(FunicParser.TypeMemberContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#valueSegment}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitValueSegment(FunicParser.ValueSegmentContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#variableValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitVariableValue(FunicParser.VariableValueContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstValue(FunicParser.ConstValueContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constCharSequence}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstCharSequence(FunicParser.ConstCharSequenceContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constString}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstString(FunicParser.ConstStringContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constRenderString}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstRenderString(FunicParser.ConstRenderStringContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constMultiString}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstMultiString(FunicParser.ConstMultiStringContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constNumeric}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstNumeric(FunicParser.ConstNumericContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constNumber}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstNumber(FunicParser.ConstNumberContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constFloat}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstFloat(FunicParser.ConstFloatContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constBoolean}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstBoolean(FunicParser.ConstBooleanContext ctx);

    /**
     * Visit a parse tree produced by {@link FunicParser#constNull}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConstNull(FunicParser.ConstNullContext ctx);
}