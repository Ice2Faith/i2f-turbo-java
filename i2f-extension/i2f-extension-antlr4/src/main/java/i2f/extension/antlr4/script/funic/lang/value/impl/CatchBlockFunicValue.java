package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/22 17:02
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class CatchBlockFunicValue implements FunicValue {
    protected String name;
    protected List<Class<?>> types;
    protected FunicParser.ScriptBlockContext scriptCtx;

    @Override
    public Object get() {
        return scriptCtx;
    }
}
