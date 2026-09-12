package i2f.extension.antlr4.funvi.lang.handler;

import i2f.extension.antlr4.funvi.grammar.FunviParser;
import i2f.extension.antlr4.funvi.lang.impl.DefaultFunviVisitor;
import i2f.extension.antlr4.funvi.lang.value.ParameterValue;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/8/3 10:36
 * @desc
 */
@FunctionalInterface
public interface FunviBlockHandler {
    Object block(List<ParameterValue> parameterList, FunviParser.BlockBodyContext bodyCtx, DefaultFunviVisitor visitor);
}
