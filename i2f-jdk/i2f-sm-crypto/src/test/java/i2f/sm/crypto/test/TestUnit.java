package i2f.sm.crypto.test;

import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.sm.crypto.sm2.Utils;

/**
 * @author Ice2Faith
 * @date 2025/8/15 10:17
 */
public class TestUnit {
    public static void main(String[] args) {
        byte[] arr = Utils.hexToArray(Utils.utf8ToHex("hello你好"));
        System.out.println(HexStringByteCodec.INSTANCE.encode(arr));

    }
}
