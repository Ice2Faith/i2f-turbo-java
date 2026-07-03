package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.RuleNode;

/**
 * @author Ice2Faith
 * @date 2026/4/22 10:43
 * @desc
 */
@Data
@NoArgsConstructor
public class ConstStringFunicValue implements FunicValue, BaseMutator<ConstStringFunicValue> {
    protected RuleNode node;
    protected String value;

    @Override
    public Object get() {
        return value;
    }

    public String getText() {
        return value;
    }
}
