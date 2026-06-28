package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/22 18:40
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class PipelineFunctionFunicValue implements FunicValue {
    public static enum Type {
        INSTANCE, GLOBAL, STATIC
    }

    protected Class<?> clazz;
    protected Type type;
    protected String name;
    protected List<Map.Entry<String, Object>> args;

    @Override
    public Object get() {
        return args;
    }
}
