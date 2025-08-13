package i2f.sm.crypto.sm2;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * 椭圆曲线域元素
 *
 * @author Ice2Faith
 * @date 2025/8/13 21:20
 * @desc
 */
@Data
@NoArgsConstructor
public class EcFieldElementFp {
    public static final BigInteger TWO = new BigInteger("2");
    public static final BigInteger THREE = new BigInteger("3");

    protected BigInteger x;
    protected BigInteger q;

    public EcFieldElementFp(BigInteger q, BigInteger x) {
        this.x = x;
        this.q = q;
        // TODO if (x.compareTo(q) >= 0) error
    }
}
