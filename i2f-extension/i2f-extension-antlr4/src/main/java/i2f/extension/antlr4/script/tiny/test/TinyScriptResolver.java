package i2f.extension.antlr4.script.tiny.test;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/21 11:31
 */
public interface TinyScriptResolver {

    void setValue(Map<String,Object> context, String name, Object value);


    Object getValue(Map<String,Object> context, String name);

    Object resolveDoubleOperator(Object left, String operator, Object right);

    Object resolvePrefixOperator(String prefixOperator, Object value);

    Object resolveFunctionCall(Object target, boolean isNew, String naming, List<Object> args);


}
