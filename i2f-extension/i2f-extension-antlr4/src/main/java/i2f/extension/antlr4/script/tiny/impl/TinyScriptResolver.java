package i2f.extension.antlr4.script.tiny.impl;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2025/2/21 11:31
 */
public interface TinyScriptResolver {
    void debug(boolean enable);

    void debugLog(Supplier<Object> supplier);

    void openDebugger(Object context, String tag, String conditionExpression);

    void setValue(Object context, String name, Object value);

    Object getValue(Object context, String name);

    boolean toBoolean(Object context, Object ret);

    Object resolveDoubleOperator(Object context, Supplier<Object> left, String operator, Supplier<Object> right);

    Object resolveDoubleOperator(Object context, Object left, String operator, Object right);

    Object resolvePrefixOperator(Object context, String prefixOperator, Object value);

    Object resolveSuffixOperator(Object context, Object left, String operator);

    Object resolveFunctionCall(Object context, Object target, boolean isNew, String naming, List<Object> args);

    String renderString(Object context, String text);

    String multilineString(Object context, String text, List<String> features);

    Class<?> loadClass(Object context, String className);


}
