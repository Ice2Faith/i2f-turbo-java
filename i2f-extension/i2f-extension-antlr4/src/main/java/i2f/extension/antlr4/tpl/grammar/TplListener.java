// Generated from /extension/antlr4/tpl/rule/Tpl.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.tpl.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TplParser}.
 */
public interface TplListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TplParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(TplParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(TplParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#segment}.
	 * @param ctx the parse tree
	 */
	void enterSegment(TplParser.SegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#segment}.
	 * @param ctx the parse tree
	 */
	void exitSegment(TplParser.SegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(TplParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(TplParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#commonBlock}.
	 * @param ctx the parse tree
	 */
	void enterCommonBlock(TplParser.CommonBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#commonBlock}.
	 * @param ctx the parse tree
	 */
	void exitCommonBlock(TplParser.CommonBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void enterIfBlock(TplParser.IfBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void exitIfBlock(TplParser.IfBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void enterElseBlock(TplParser.ElseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void exitElseBlock(TplParser.ElseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#blockHead}.
	 * @param ctx the parse tree
	 */
	void enterBlockHead(TplParser.BlockHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#blockHead}.
	 * @param ctx the parse tree
	 */
	void exitBlockHead(TplParser.BlockHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#blockBody}.
	 * @param ctx the parse tree
	 */
	void enterBlockBody(TplParser.BlockBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#blockBody}.
	 * @param ctx the parse tree
	 */
	void exitBlockBody(TplParser.BlockBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(TplParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(TplParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(TplParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(TplParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(TplParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(TplParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#text}.
	 * @param ctx the parse tree
	 */
	void enterText(TplParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#text}.
	 * @param ctx the parse tree
	 */
	void exitText(TplParser.TextContext ctx);
	/**
	 * Enter a parse tree produced by {@link TplParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(TplParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link TplParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(TplParser.ContentContext ctx);
}