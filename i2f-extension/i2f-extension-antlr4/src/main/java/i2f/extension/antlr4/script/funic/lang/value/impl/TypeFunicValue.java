package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.tree.RuleNode;

/**
 * @author Ice2Faith
 * @date 2026/4/22 10:08
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class TypeFunicValue implements FunicValue {
    protected RuleNode node;
    protected Class<?> value;

    @Override
    public Object get() {
        return value;
    }

    public Class<?> getType() {
        return value;
    }
}
