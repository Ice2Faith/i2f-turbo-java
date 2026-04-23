package i2f.extension.antlr4.script.funic.lang.operator;

import i2f.extension.antlr4.script.funic.lang.impl.DefaultFunicVisitor;

/**
 * @author Ice2Faith
 * @date 2026/4/23 9:18
 * @desc
 */
@FunctionalInterface
public interface DoubleOperatorFunction {
    Object apply(Object leftValue, Object rightValue, DefaultFunicVisitor visitor);
}
