package i2f.extension.antlr4.funvi.lang.resolver;

import i2f.extension.antlr4.funvi.grammar.FunviParser;
import i2f.extension.antlr4.funvi.grammar.FunviVisitor;
import i2f.extension.antlr4.funvi.lang.impl.DefaultFunviVisitor;
import i2f.extension.antlr4.funvi.lang.value.ParameterValue;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2026/7/31 16:16
 * @desc
 */
public interface FunviResolver {
    void debug(boolean enable);

    void debugLog(Supplier<Object> supplier);

    void debugBridge(String fileName, int lineNumber, Supplier<Map<String, Object>> variableMapSupplier);

    void openDebugger(Object context, String tag, String conditionExpression);

    Object concat(Object obj, Object append);

    Object block(String blockName, List<ParameterValue> parameterList, FunviParser.BlockBodyContext bodyCtx, DefaultFunviVisitor visitor);

    Object parameter(String expression, DefaultFunviVisitor visitor);

    Object value(String expression, DefaultFunviVisitor visitor);

    boolean toBoolean(Object val);
}
