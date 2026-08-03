package i2f.extension.antlr4.tpl.impl;

import i2f.extension.antlr4.tpl.grammar.TplParser;
import i2f.extension.antlr4.tpl.grammar.TplVisitor;
import i2f.extension.antlr4.tpl.impl.value.ParameterValue;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/8/3 10:36
 * @desc
 */
@FunctionalInterface
public interface TplBlockHandler {
    Object block(List<ParameterValue> parameterList, TplParser.BlockBodyContext bodyCtx, Object context, TplVisitor<Object> visitor);
}
