package i2f.sm.crypto.sm2.asn1;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2025/8/14 9:03
 */
public class DerInteger extends Asn1Object{
    public DerInteger(BigInteger bigint){
        super();

        this.t="02";// 整型标签说明
        if (bigint!=null){
            this.v = Asn1Inner.bigintToValue(bigint);
        }
    }

    @Override
    public String getValue() {
        return this.v;
    }
}
