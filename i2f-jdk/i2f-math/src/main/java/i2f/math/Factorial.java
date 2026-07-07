package i2f.math;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2026/7/7 9:12
 * @desc
 */
public class Factorial {
    private static final BigInteger[] nums = new BigInteger[100];

    static {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = BigInteger.ZERO;
        }
    }

    public static BigInteger get(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("factorial not support negative number");
        }

        if (n >= 0 && n < nums.length) {
            if (nums[n].compareTo(BigInteger.ZERO) > 0) {
                return nums[n];
            }
        }

        if (n == 0 || n == 1) {
            nums[n] = BigInteger.ONE;
            return nums[n];
        }

        BigInteger ret = BigInteger.valueOf(n).multiply(get(n - 1));
        if (n >= 0 && n < nums.length) {
            nums[n] = ret;
        }
        return ret;
    }
}
