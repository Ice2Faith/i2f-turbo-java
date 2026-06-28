package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * @author Ice2Faith
 * @date 2026/4/22 9:35
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class TerminalFunicValue implements FunicValue {
    protected TerminalNode node;
    protected Token symbol;
    protected String text;
    protected Object value;

    @Override
    public Object get() {
        return value;
    }
}
