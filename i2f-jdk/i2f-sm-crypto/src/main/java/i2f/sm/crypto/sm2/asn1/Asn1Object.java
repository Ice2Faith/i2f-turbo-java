package i2f.sm.crypto.sm2.asn1;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/8/14 8:58
 */
@Data
@NoArgsConstructor
public class Asn1Object {
    protected String tlv = null;
    protected String t = "00";
    protected String l = "00";
    protected String v = "";

    /**
     * 获取 der 编码比特流16进制串
     */
    public String getEncodedHex() {
        if (this.tlv == null || this.tlv.isEmpty()) {
            this.v = this.getValue();
            this.l = this.getLength();
            this.tlv = this.t + this.l + this.v;
        }
        return this.tlv;
    }

    public String getLength() {
        int n = this.v.length() / 2; // 字节数
        String nHex = Integer.toString(n, 16);
        if (nHex.length() % 2 == 1) {
            nHex = "0" + nHex; // 补齐到整字节
        }

        if (n < 128) {
            // 短格式，以 0 开头
            return nHex;
        } else {
            // 长格式，以 1 开头
            int head = 128 + nHex.length() / 2; // 1(1位) + 真正的长度占用字节数(7位) + 真正的长度
            return Integer.toString(head, 16) + nHex;
        }
    }

    public String getValue() {
        return "";
    }
}
