package i2f.sm.crypto.sm2;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2025/8/13 18:45
 */
@Data
@NoArgsConstructor
public class EcParam {
    protected EcCurveFp curve;
    protected EcPointFp g;
    protected BigInteger n;

    public EcParam(EcCurveFp curve, EcPointFp g, BigInteger n) {
        this.curve = curve;
        this.g = g;
        this.n = n;
    }
}
