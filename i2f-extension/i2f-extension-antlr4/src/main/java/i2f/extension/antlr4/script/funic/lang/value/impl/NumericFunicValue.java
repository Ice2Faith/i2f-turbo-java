package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.tree.RuleNode;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2026/4/22 10:08
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class NumericFunicValue implements FunicValue {
    protected RuleNode node;
    protected Object value;

    @Override
    public Object get() {
        return value;
    }

    public BigDecimal getBigDecimal() {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof BigInteger) {
            BigInteger val = (BigInteger) value;
            return new BigDecimal(val);
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((double) value);
        } else if (value instanceof Long) {
            return BigDecimal.valueOf((long) value);
        } else if (value instanceof Float) {
            return BigDecimal.valueOf((float) value);
        } else if (value instanceof Integer) {
            return BigDecimal.valueOf((int) value);
        } else {
            return new BigDecimal(String.valueOf(value));
        }
    }
}
