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

    void openDebugger(String tag,Object context,String conditionExpression);

    void setValue(Object context, String name, Object value);

    Object getValue(Object context, String name);

    boolean toBoolean(Object ret);

    Object resolveDoubleOperator(Supplier<Object> left, String operator, Supplier<Object> right);

    Object resolveDoubleOperator(Object left, String operator, Object right);

    Object resolvePrefixOperator(String prefixOperator, Object value);

    Object resolveFunctionCall(Object target, boolean isNew, String naming, List<Object> args);

    String renderString(String text, Object context);

    String multilineString(String text, List<String> features, Object context);

    Class<?> loadClass(String className);
}
