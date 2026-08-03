package i2f.extension.antlr4.funvi.impl;

import i2f.extension.antlr4.funvi.grammar.FunviParser;
import i2f.extension.antlr4.funvi.grammar.FunviVisitor;
import i2f.extension.antlr4.funvi.impl.value.ParameterValue;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/8/3 10:36
 * @desc
 */
@FunctionalInterface
public interface FunviBlockHandler {
    Object block(List<ParameterValue> parameterList, FunviParser.BlockBodyContext bodyCtx, Object context, FunviVisitor<Object> visitor);
}
