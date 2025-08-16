package i2f.sm.crypto.sm2;

import i2f.sm.crypto.sm2.asn1.Asn1Inner;
import i2f.sm.crypto.sm2.asn1.DerInteger;
import i2f.sm.crypto.sm2.asn1.DerSequence;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2025/8/14 9:48
 */
public class Asn1 {
    @Data
    @NoArgsConstructor
    public static class Der {
        protected BigInteger r;
        protected BigInteger s;

        public Der(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }
    }

    public static String encodeDer(Der der) {
        return encodeDer(der.getR(), der.getS());
    }

    /**
     * ASN.1 der 编码，针对 sm2 签名
     */
    public static String encodeDer(BigInteger r, BigInteger s) {
        DerInteger derR = new DerInteger(r);
        DerInteger derS = new DerInteger(s);
        DerSequence derSeq = new DerSequence(new ArrayList<>(Arrays.asList(derR, derS)));

        return derSeq.getEncodedHex();
    }

    /**
     * 解析 ASN.1 der，针对 sm2 验签
     */
    public static Der decodeDer(String input) {
        // 结构：
        // input = | tSeq | lSeq | vSeq |
        // vSeq = | tR | lR | vR | tS | lS | vS |
        int start = Asn1Inner.getStartOfV(input, 0);

        int vIndexR = Asn1Inner.getStartOfV(input, start);
        int lR = Asn1Inner.getL(input, start);
        String vR = input.substring(vIndexR, vIndexR + (lR * 2));

        int nextStart = vIndexR + vR.length();
        int vIndexS = Asn1Inner.getStartOfV(input, nextStart);
        int lS = Asn1Inner.getL(input, nextStart);
        String vS = input.substring(vIndexS, vIndexS + (lS * 2));

        BigInteger r = new BigInteger(vR, 16);
        BigInteger s = new BigInteger(vS, 16);

        return new Der(r, s);
    }
}
