package i2f.extension.antlr4.script.funic.lang.operator;

import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;

/**
 * @author Ice2Faith
 * @date 2026/4/23 9:16
 * @desc
 */
@FunctionalInterface
public interface PrefixOperatorFunction {
    Object apply(Object value, DefaultFunicVisitor visitor);
}
