// Generated from D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/i2f-tools/i2f-jdbc-procedure-idea-plugin/src/main/java/i2f/extension/antlr4/xproc4j/oracle/grammar/rule/OracleGrammar.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.xproc4j.oracle.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link OracleGrammarParser}.
 */
public interface OracleGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#convert}.
	 * @param ctx the parse tree
	 */
	void enterConvert(OracleGrammarParser.ConvertContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#convert}.
	 * @param ctx the parse tree
	 */
	void exitConvert(OracleGrammarParser.ConvertContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#script}.
	 * @param ctx the parse tree
	 */
	void enterScript(OracleGrammarParser.ScriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#script}.
	 * @param ctx the parse tree
	 */
	void exitScript(OracleGrammarParser.ScriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#segment}.
	 * @param ctx the parse tree
	 */
	void enterSegment(OracleGrammarParser.SegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#segment}.
	 * @param ctx the parse tree
	 */
	void exitSegment(OracleGrammarParser.SegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#returnSegment}.
	 * @param ctx the parse tree
	 */
	void enterReturnSegment(OracleGrammarParser.ReturnSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#returnSegment}.
	 * @param ctx the parse tree
	 */
	void exitReturnSegment(OracleGrammarParser.ReturnSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#declareProcedureSegment}.
	 * @param ctx the parse tree
	 */
	void enterDeclareProcedureSegment(OracleGrammarParser.DeclareProcedureSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#declareProcedureSegment}.
	 * @param ctx the parse tree
	 */
	void exitDeclareProcedureSegment(OracleGrammarParser.DeclareProcedureSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#argumentDeclareListSegment}.
	 * @param ctx the parse tree
	 */
	void enterArgumentDeclareListSegment(OracleGrammarParser.ArgumentDeclareListSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#argumentDeclareListSegment}.
	 * @param ctx the parse tree
	 */
	void exitArgumentDeclareListSegment(OracleGrammarParser.ArgumentDeclareListSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#argumentDeclareSegment}.
	 * @param ctx the parse tree
	 */
	void enterArgumentDeclareSegment(OracleGrammarParser.ArgumentDeclareSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#argumentDeclareSegment}.
	 * @param ctx the parse tree
	 */
	void exitArgumentDeclareSegment(OracleGrammarParser.ArgumentDeclareSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#declareVariableSegment}.
	 * @param ctx the parse tree
	 */
	void enterDeclareVariableSegment(OracleGrammarParser.DeclareVariableSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#declareVariableSegment}.
	 * @param ctx the parse tree
	 */
	void exitDeclareVariableSegment(OracleGrammarParser.DeclareVariableSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#sqlDataType}.
	 * @param ctx the parse tree
	 */
	void enterSqlDataType(OracleGrammarParser.SqlDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#sqlDataType}.
	 * @param ctx the parse tree
	 */
	void exitSqlDataType(OracleGrammarParser.SqlDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionSegment(OracleGrammarParser.ConditionSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionSegment(OracleGrammarParser.ConditionSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionInSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionInSegment(OracleGrammarParser.ConditionInSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionInSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionInSegment(OracleGrammarParser.ConditionInSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionNotInSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionNotInSegment(OracleGrammarParser.ConditionNotInSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionNotInSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionNotInSegment(OracleGrammarParser.ConditionNotInSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#variableListSegment}.
	 * @param ctx the parse tree
	 */
	void enterVariableListSegment(OracleGrammarParser.VariableListSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#variableListSegment}.
	 * @param ctx the parse tree
	 */
	void exitVariableListSegment(OracleGrammarParser.VariableListSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionBetweenSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionBetweenSegment(OracleGrammarParser.ConditionBetweenSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionBetweenSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionBetweenSegment(OracleGrammarParser.ConditionBetweenSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionNotLikeSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionNotLikeSegment(OracleGrammarParser.ConditionNotLikeSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionNotLikeSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionNotLikeSegment(OracleGrammarParser.ConditionNotLikeSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionIsNotNullSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionIsNotNullSegment(OracleGrammarParser.ConditionIsNotNullSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionIsNotNullSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionIsNotNullSegment(OracleGrammarParser.ConditionIsNotNullSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionIsNullSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionIsNullSegment(OracleGrammarParser.ConditionIsNullSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionIsNullSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionIsNullSegment(OracleGrammarParser.ConditionIsNullSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#conditionCompositeSegment}.
	 * @param ctx the parse tree
	 */
	void enterConditionCompositeSegment(OracleGrammarParser.ConditionCompositeSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#conditionCompositeSegment}.
	 * @param ctx the parse tree
	 */
	void exitConditionCompositeSegment(OracleGrammarParser.ConditionCompositeSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#ifElseSegment}.
	 * @param ctx the parse tree
	 */
	void enterIfElseSegment(OracleGrammarParser.IfElseSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#ifElseSegment}.
	 * @param ctx the parse tree
	 */
	void exitIfElseSegment(OracleGrammarParser.IfElseSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#variableSegment}.
	 * @param ctx the parse tree
	 */
	void enterVariableSegment(OracleGrammarParser.VariableSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#variableSegment}.
	 * @param ctx the parse tree
	 */
	void exitVariableSegment(OracleGrammarParser.VariableSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#functionSegment}.
	 * @param ctx the parse tree
	 */
	void enterFunctionSegment(OracleGrammarParser.FunctionSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#functionSegment}.
	 * @param ctx the parse tree
	 */
	void exitFunctionSegment(OracleGrammarParser.FunctionSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#argumentListSegment}.
	 * @param ctx the parse tree
	 */
	void enterArgumentListSegment(OracleGrammarParser.ArgumentListSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#argumentListSegment}.
	 * @param ctx the parse tree
	 */
	void exitArgumentListSegment(OracleGrammarParser.ArgumentListSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#commitSegment}.
	 * @param ctx the parse tree
	 */
	void enterCommitSegment(OracleGrammarParser.CommitSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#commitSegment}.
	 * @param ctx the parse tree
	 */
	void exitCommitSegment(OracleGrammarParser.CommitSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#rollbackSegment}.
	 * @param ctx the parse tree
	 */
	void enterRollbackSegment(OracleGrammarParser.RollbackSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#rollbackSegment}.
	 * @param ctx the parse tree
	 */
	void exitRollbackSegment(OracleGrammarParser.RollbackSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#executeImmediadeVariableSegment}.
	 * @param ctx the parse tree
	 */
	void enterExecuteImmediadeVariableSegment(OracleGrammarParser.ExecuteImmediadeVariableSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#executeImmediadeVariableSegment}.
	 * @param ctx the parse tree
	 */
	void exitExecuteImmediadeVariableSegment(OracleGrammarParser.ExecuteImmediadeVariableSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#assignSegment}.
	 * @param ctx the parse tree
	 */
	void enterAssignSegment(OracleGrammarParser.AssignSegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#assignSegment}.
	 * @param ctx the parse tree
	 */
	void exitAssignSegment(OracleGrammarParser.AssignSegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#sqlNull}.
	 * @param ctx the parse tree
	 */
	void enterSqlNull(OracleGrammarParser.SqlNullContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#sqlNull}.
	 * @param ctx the parse tree
	 */
	void exitSqlNull(OracleGrammarParser.SqlNullContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#sqlIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSqlIdentifier(OracleGrammarParser.SqlIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#sqlIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSqlIdentifier(OracleGrammarParser.SqlIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#sqlNumber}.
	 * @param ctx the parse tree
	 */
	void enterSqlNumber(OracleGrammarParser.SqlNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#sqlNumber}.
	 * @param ctx the parse tree
	 */
	void exitSqlNumber(OracleGrammarParser.SqlNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link OracleGrammarParser#sqlString}.
	 * @param ctx the parse tree
	 */
	void enterSqlString(OracleGrammarParser.SqlStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link OracleGrammarParser#sqlString}.
	 * @param ctx the parse tree
	 */
	void exitSqlString(OracleGrammarParser.SqlStringContext ctx);
}