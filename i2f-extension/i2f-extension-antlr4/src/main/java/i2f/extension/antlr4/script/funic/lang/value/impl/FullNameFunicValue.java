package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.grammar.FunicParser;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/22 11:00
 * @desc
 */
@Data
@NoArgsConstructor
public class FullNameFunicValue implements FunicValue {
    protected FunicParser.FullNameContext node;
    protected String value;

    @Override
    public Object get() {
        return value;
    }

    public String getName() {
        return value;
    }
}
