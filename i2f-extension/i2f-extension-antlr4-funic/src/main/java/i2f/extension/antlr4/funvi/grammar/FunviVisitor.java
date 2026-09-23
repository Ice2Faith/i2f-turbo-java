// Generated from /extension/antlr4/funvi/rule/Funvi.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.funvi.grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FunviParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FunviVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FunviParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(FunviParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#segment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSegment(FunviParser.SegmentContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(FunviParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#commonBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCommonBlock(FunviParser.CommonBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#ifBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBlock(FunviParser.IfBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#elseBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseBlock(FunviParser.ElseBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#blockHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockHead(FunviParser.BlockHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#blockBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockBody(FunviParser.BlockBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(FunviParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(FunviParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(FunviParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#text}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitText(FunviParser.TextContext ctx);
	/**
	 * Visit a parse tree produced by {@link FunviParser#content}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContent(FunviParser.ContentContext ctx);
}