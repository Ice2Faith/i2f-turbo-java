package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.tree.RuleNode;

/**
 * @author Ice2Faith
 * @date 2026/4/22 10:38
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class ParentFunicValue implements FunicValue {
    protected RuleNode node;
    protected Object parent;
    protected Object key;
    protected Object value;

    @Override
    public Object get() {
        return value;
    }
}
