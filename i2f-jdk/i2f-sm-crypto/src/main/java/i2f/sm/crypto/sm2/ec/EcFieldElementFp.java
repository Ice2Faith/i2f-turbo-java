package i2f.sm.crypto.sm2.ec;

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

    protected BigInteger x;
    protected BigInteger q;

    public EcFieldElementFp(BigInteger q, BigInteger x) {
        this.x = x;
        this.q = q;
        // TODO if (x.compareTo(q) >= 0) error
    }


    /**
     * 判断相等
     */
    public boolean equals(EcFieldElementFp other) {
        if (other == this){
            return true;
        }
        return (this.q.equals(other.q) && this.x.equals(other.x));
    }

    /**
     * 返回具体数值
     */
    public BigInteger toBigInteger() {
        return this.x;
    }

    /**
     * 取反
     */
    public EcFieldElementFp negate() {
        return new EcFieldElementFp(this.q, this.x.negate().mod(this.q));
    }

    /**
     * 相加
     */
    public EcFieldElementFp add(EcFieldElementFp b) {
        return new EcFieldElementFp(this.q, this.x.add(b.toBigInteger()).mod(this.q));
    }

    /**
     * 相减
     */
    public EcFieldElementFp subtract(EcFieldElementFp b) {
        return new EcFieldElementFp(this.q, this.x.subtract(b.toBigInteger()).mod(this.q));
    }

    /**
     * 相乘
     */
    public EcFieldElementFp multiply(EcFieldElementFp b) {
        return new EcFieldElementFp(this.q, this.x.multiply(b.toBigInteger()).mod(this.q));
    }

    /**
     * 相除
     */
    public EcFieldElementFp divide(EcFieldElementFp b) {
        return new EcFieldElementFp(this.q, this.x.multiply(b.toBigInteger().modInverse(this.q)).mod(this.q));
    }

    /**
     * 平方
     */
    public EcFieldElementFp square() {
        return new EcFieldElementFp(this.q, this.x.pow(2).mod(this.q));
    }
}
