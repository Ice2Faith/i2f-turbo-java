package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/22 11:20
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class ListKeyPairFunicValue implements FunicValue {
    protected RuleNode node;
    protected List<Map.Entry<String, Object>> value;

    @Override
    public Object get() {
        return value;
    }

    public List<Map.Entry<String, Object>> getList() {
        return value;
    }
}
