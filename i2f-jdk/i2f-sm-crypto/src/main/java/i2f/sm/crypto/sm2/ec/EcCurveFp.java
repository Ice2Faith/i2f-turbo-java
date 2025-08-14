package i2f.sm.crypto.sm2.ec;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * 椭圆曲线 y^2 = x^3 + ax + b
 *
 * @author Ice2Faith
 * @date 2025/8/13 18:45
 */
@Data
@NoArgsConstructor
public class EcCurveFp {
    protected BigInteger q;
    protected EcFieldElementFp a;
    protected EcFieldElementFp b;
    protected EcPointFp infinity;
    public EcCurveFp(BigInteger q, BigInteger a, BigInteger b) {
        this.q = q;
        this.a = fromBigInteger(a);
        this.b = fromBigInteger(b);
        this.infinity = new EcPointFp(this, null, null, null); // 无穷远点
    }

    /**
     * 判断两个椭圆曲线是否相等
     */
    public boolean equals(EcCurveFp other) {
        if (other == this){
            return true;
        }
        return (this.q.equals(other.q) && this.a.equals(other.a) && this.b.equals(other.b));
    }

    /**
     * 生成椭圆曲线域元素
     */
    public EcFieldElementFp fromBigInteger(BigInteger x) {
        return new EcFieldElementFp(this.q, x);
    }

    /**
     * 解析 16 进制串为椭圆曲线点
     */
    public EcPointFp decodePointHex(String s) {
        int bt = Integer.parseInt(s.substring(0, 2), 16);
        switch (bt) {
            // 第一个字节
            case 0:
                return this.infinity;
            case 2:
            case 3:
                // 压缩
                EcFieldElementFp x = this.fromBigInteger(new BigInteger(s.substring(2), 16));
                // 对 p ≡ 3 (mod4)，即存在正整数 u，使得 p = 4u + 3
                // 计算 y = (√ (x^3 + ax + b) % p)^(u + 1) modp
                EcFieldElementFp y = this.fromBigInteger(x.multiply(x.square()).add(
                                x.multiply(this.a)
                        ).add(this.b).toBigInteger()
                        .modPow(
                                this.q.divide(new BigInteger("4")).add(BigInteger.ONE), this.q
                        ));
                // 算出结果 2 进制最后 1 位不等于第 1 个字节减 2 则取反
                if (!y.toBigInteger().mod(new BigInteger("2")).equals(new BigInteger(s.substring(0, 2), 16).subtract(new BigInteger("2")))) {
                    y = y.negate();
                }
                return new EcPointFp(this, x, y, null);
            case 4:
            case 6:
            case 7:
                int len = (s.length() - 2) / 2;
                String xHex = s.substring(2, 2 + len);
                String yHex = s.substring(len + 2, (len + 2) + len);

                return new EcPointFp(this, this.fromBigInteger(new BigInteger(xHex, 16)),
                        this.fromBigInteger(new BigInteger(yHex, 16)), null);
            default:
                // 不支持
                return null;
        }
    }
}
