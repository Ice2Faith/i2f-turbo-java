package i2f.extension.antlr4.script.funic.lang.value.impl;

import i2f.extension.antlr4.script.funic.lang.value.FunicValue;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.tree.RuleNode;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2026/4/22 10:08
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class NumberFunicValue implements FunicValue {
    protected RuleNode node;
    protected Object value;

    @Override
    public Object get() {
        return value;
    }


    public BigInteger getBigInteger() {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        } else if (value instanceof Long) {
            return BigInteger.valueOf((long) value);
        } else if (value instanceof Integer) {
            return BigInteger.valueOf((int) value);
        } else {
            return new BigInteger(String.valueOf(value));
        }
    }

    public long getLong() {
        if (value instanceof BigInteger) {
            return ((BigInteger) value).intValue();
        } else if (value instanceof Long) {
            return (long) value;
        } else if (value instanceof Integer) {
            return (int) value;
        } else {
            return new BigInteger(String.valueOf(value)).longValue();
        }
    }

    public int getInteger() {
        if (value instanceof BigInteger) {
            return ((BigInteger) value).intValue();
        } else if (value instanceof Long) {
            return (int) (long) value;
        } else if (value instanceof Integer) {
            return (int) value;
        } else {
            return new BigInteger(String.valueOf(value)).intValue();
        }
    }
}
