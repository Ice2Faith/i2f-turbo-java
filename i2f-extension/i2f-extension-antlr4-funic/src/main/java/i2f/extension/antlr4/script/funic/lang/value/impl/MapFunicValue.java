package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import i2f.mutator.BaseMutator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/22 15:49
 * @desc
 */
@Data
@NoArgsConstructor
public class MapFunicValue implements FunicValue, BaseMutator<MapFunicValue> {
    protected RuleNode node;
    protected Map<String, Object> value;

    @Override
    public Object get() {
        return value;
    }

    public Map<String, Object> getMap() {
        return value;
    }
}
