package i2f.extension.antlr4.script.tiny.impl;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/2/21 11:31
 */
public interface TinyScriptResolver {

    void setValue(Object context, String name, Object value);

    Object getValue(Object context, String name);

    Object resolveDoubleOperator(Object left, String operator, Object right);

    Object resolvePrefixOperator(String prefixOperator, Object value);

    Object resolveFunctionCall(Object target, boolean isNew, String naming, List<Object> args);

    String renderString(String text, Object context);

    String multilineString(String text, List<String> features, Object context);
}
