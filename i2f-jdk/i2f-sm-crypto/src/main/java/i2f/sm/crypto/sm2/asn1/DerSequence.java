package i2f.sm.crypto.sm2.asn1;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/8/14 9:05
 */
public class DerSequence extends Asn1Object{
    protected List<Asn1Object> asn1Array;
    public DerSequence(List<Asn1Object> asn1Array) {
        super();

        this.t = "30"; // 序列标签说明
        this.asn1Array = asn1Array;
    }

    @Override
    public String getValue() {
        this.v = this.asn1Array.stream().map(asn1Object -> asn1Object.getEncodedHex()).collect(Collectors.joining(""));
        return this.v;
    }
}
