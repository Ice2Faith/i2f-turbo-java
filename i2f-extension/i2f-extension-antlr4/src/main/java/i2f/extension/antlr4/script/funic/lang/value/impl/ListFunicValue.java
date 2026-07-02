package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.builder.BaseBuilder;
import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/22 11:09
 * @desc
 */
@Data
@NoArgsConstructor
public class ListFunicValue implements FunicValue, BaseBuilder<ListFunicValue> {
    protected RuleNode node;
    protected List<Object> value;

    @Override
    public Object get() {
        return value;
    }

    public List<Object> getList() {
        return value;
    }
}
