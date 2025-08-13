package i2f.sm.crypto.sm2;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2025/8/13 21:23
 * @desc
 */
@Data
@NoArgsConstructor
public class EcPointFp {
    protected EcCurveFp curve;
    protected EcFieldElementFp x;
    protected EcFieldElementFp y;
    protected BigInteger z;
    protected BigInteger zinv;

    public EcPointFp(EcCurveFp curve, EcFieldElementFp x, EcFieldElementFp y, BigInteger z) {
        this.curve = curve;
        this.x = x;
        this.y = y;
        this.z = z == null ? BigInteger.ONE : z;
        this.zinv = null;
        // TODO: compression flag
    }
}
