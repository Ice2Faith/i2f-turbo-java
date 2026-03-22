// Generated from D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/i2f-tools/i2f-jdbc-procedure-idea-plugin/src/main/java/i2f/extension/antlr4/xproc4j/oracle/grammar/rule/OracleGrammar.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.xproc4j.oracle.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link OracleGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface OracleGrammarVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#convert}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConvert(OracleGrammarParser.ConvertContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#script}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScript(OracleGrammarParser.ScriptContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment(OracleGrammarParser.SegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#returnSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnSegment(OracleGrammarParser.ReturnSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#declareProcedureSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareProcedureSegment(OracleGrammarParser.DeclareProcedureSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#argumentDeclareListSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentDeclareListSegment(OracleGrammarParser.ArgumentDeclareListSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#argumentDeclareSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentDeclareSegment(OracleGrammarParser.ArgumentDeclareSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#declareVariableSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclareVariableSegment(OracleGrammarParser.DeclareVariableSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#sqlDataType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlDataType(OracleGrammarParser.SqlDataTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionSegment(OracleGrammarParser.ConditionSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionInSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionInSegment(OracleGrammarParser.ConditionInSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionNotInSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionNotInSegment(OracleGrammarParser.ConditionNotInSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#variableListSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableListSegment(OracleGrammarParser.VariableListSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionBetweenSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionBetweenSegment(OracleGrammarParser.ConditionBetweenSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionNotLikeSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionNotLikeSegment(OracleGrammarParser.ConditionNotLikeSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionIsNotNullSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionIsNotNullSegment(OracleGrammarParser.ConditionIsNotNullSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionIsNullSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionIsNullSegment(OracleGrammarParser.ConditionIsNullSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#conditionCompositeSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionCompositeSegment(OracleGrammarParser.ConditionCompositeSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#ifElseSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElseSegment(OracleGrammarParser.IfElseSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#variableSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableSegment(OracleGrammarParser.VariableSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#functionSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionSegment(OracleGrammarParser.FunctionSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#argumentListSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentListSegment(OracleGrammarParser.ArgumentListSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#commitSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommitSegment(OracleGrammarParser.CommitSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#rollbackSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRollbackSegment(OracleGrammarParser.RollbackSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#executeImmediadeVariableSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExecuteImmediadeVariableSegment(OracleGrammarParser.ExecuteImmediadeVariableSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#assignSegment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignSegment(OracleGrammarParser.AssignSegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#sqlNull}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlNull(OracleGrammarParser.SqlNullContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#sqlIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlIdentifier(OracleGrammarParser.SqlIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#sqlNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlNumber(OracleGrammarParser.SqlNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link OracleGrammarParser#sqlString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSqlString(OracleGrammarParser.SqlStringContext ctx);
}