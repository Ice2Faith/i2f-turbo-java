package i2f.extension.antlr4.tpl.impl;

import i2f.extension.antlr4.tpl.grammar.TplParser;
import i2f.extension.antlr4.tpl.grammar.TplVisitor;
import i2f.extension.antlr4.tpl.impl.value.ParameterValue;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/7/31 16:16
 * @desc
 */
public interface TplResolver {
    void debug(boolean enable);

    void debugLog(Supplier<Object> supplier);

    void debugBridge(String fileName, int lineNumber, Supplier<Map<String, Object>> variableMapSupplier);

    void openDebugger(Object context, String tag, String conditionExpression);

    Object concat(Object obj, Object append);

    Object block(String blockName, List<ParameterValue> parameterList, TplParser.BlockBodyContext bodyCtx, Object context, TplVisitor<Object> visitor);

    Object parameter(String expression, Object context, TplVisitor<Object> visitor);

    Object value(String expression, Object context, TplVisitor<Object> visitor);

    boolean toBoolean(Object val);
}
