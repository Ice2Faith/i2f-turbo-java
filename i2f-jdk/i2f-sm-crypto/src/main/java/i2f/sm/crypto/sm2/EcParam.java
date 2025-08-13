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
    protected BigInteger g;
    protected BigInteger n;
}
