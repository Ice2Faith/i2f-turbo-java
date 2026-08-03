// Generated from /extension/antlr4/tpl/rule/Tpl.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.tpl.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TplParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TplVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TplParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(TplParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment(TplParser.SegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(TplParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#commonBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonBlock(TplParser.CommonBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(TplParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(TplParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#blockHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockHead(TplParser.BlockHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#blockBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockBody(TplParser.BlockBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(TplParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(TplParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(TplParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(TplParser.TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link TplParser#content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContent(TplParser.ContentContext ctx);
}