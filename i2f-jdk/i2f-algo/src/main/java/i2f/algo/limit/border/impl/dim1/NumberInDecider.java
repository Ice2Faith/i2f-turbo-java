package i2f.algo.limit.border.impl.dim1;


import i2f.algo.limit.border.BorderInDecider;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:08
 * @desc
 */
public class NumberInDecider implements BorderInDecider<Double, NumberRange> {
    @Override
    public boolean isInRange(Double elem, NumberRange range) {
        return elem >= range.begin && elem <= range.end;
    }
}
