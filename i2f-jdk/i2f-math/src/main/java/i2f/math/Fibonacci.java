package i2f.math;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2022/6/17 23:53
 * @desc 采用缓存方式，进行斐波那契数列求值
 */
public class Fibonacci {
    private static final BigInteger[] nums = new BigInteger[100];
    private static final BigInteger NEG_ONE = BigInteger.valueOf(-1L);

    static {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = BigInteger.ZERO;
        }
    }

    public static BigInteger get(int i) {
        if (i < 0) {
            return NEG_ONE;
        }
        if (i >= 0 && i < nums.length) {
            if (nums[i].compareTo(BigInteger.ZERO) > 0) {
                return nums[i];
            }
        }
        if (i == 0 || i == 1) {
            nums[i] = BigInteger.ONE;
            return nums[i];
        }
        BigInteger ret = get(i - 1).add(get(i - 2));
        if (i >= 0 && i < nums.length) {
            nums[i] = ret;
        }
        return ret;
    }

}
