package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.tree.RuleNode;

import java.math.BigDecimal;

/**
 * @author Ice2Faith
 * @date 2026/4/22 10:08
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class FloatFunicValue implements FunicValue {
    protected RuleNode node;
    protected Object value;

    @Override
    public Object get() {
        return value;
    }

    public BigDecimal getBigDecimal() {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((double) value);
        } else if (value instanceof Float) {
            return BigDecimal.valueOf((float) value);
        } else {
            return new BigDecimal(String.valueOf(value));
        }
    }

    public double getDouble() {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else if (value instanceof Double) {
            return (double) value;
        } else if (value instanceof Float) {
            return (float) value;
        } else {
            return new BigDecimal(String.valueOf(value)).doubleValue();
        }
    }

    public double getFloat() {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).floatValue();
        } else if (value instanceof Double) {
            return (double) value;
        } else if (value instanceof Float) {
            return (float) value;
        } else {
            return new BigDecimal(String.valueOf(value)).floatValue();
        }
    }
}
