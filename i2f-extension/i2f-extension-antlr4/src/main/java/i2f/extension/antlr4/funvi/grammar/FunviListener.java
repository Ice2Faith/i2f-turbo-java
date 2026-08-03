// Generated from /extension/antlr4/funvi/rule/Funvi.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.funvi.grammar;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link FunviParser}.
 */
public interface FunviListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link FunviParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(FunviParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(FunviParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#segment}.
	 * @param ctx the parse tree
	 */
	void enterSegment(FunviParser.SegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#segment}.
	 * @param ctx the parse tree
	 */
	void exitSegment(FunviParser.SegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(FunviParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(FunviParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#commonBlock}.
	 * @param ctx the parse tree
	 */
	void enterCommonBlock(FunviParser.CommonBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#commonBlock}.
	 * @param ctx the parse tree
	 */
	void exitCommonBlock(FunviParser.CommonBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void enterIfBlock(FunviParser.IfBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#ifBlock}.
	 * @param ctx the parse tree
	 */
	void exitIfBlock(FunviParser.IfBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void enterElseBlock(FunviParser.ElseBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#elseBlock}.
	 * @param ctx the parse tree
	 */
	void exitElseBlock(FunviParser.ElseBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#blockHead}.
	 * @param ctx the parse tree
	 */
	void enterBlockHead(FunviParser.BlockHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#blockHead}.
	 * @param ctx the parse tree
	 */
	void exitBlockHead(FunviParser.BlockHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#blockBody}.
	 * @param ctx the parse tree
	 */
	void enterBlockBody(FunviParser.BlockBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#blockBody}.
	 * @param ctx the parse tree
	 */
	void exitBlockBody(FunviParser.BlockBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(FunviParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(FunviParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(FunviParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(FunviParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(FunviParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(FunviParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#text}.
	 * @param ctx the parse tree
	 */
	void enterText(FunviParser.TextContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#text}.
	 * @param ctx the parse tree
	 */
	void exitText(FunviParser.TextContext ctx);
	/**
	 * Enter a parse tree produced by {@link FunviParser#content}.
	 * @param ctx the parse tree
	 */
	void enterContent(FunviParser.ContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link FunviParser#content}.
	 * @param ctx the parse tree
	 */
	void exitContent(FunviParser.ContentContext ctx);
}