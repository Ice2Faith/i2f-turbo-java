package i2f.codec.bytes.basex;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/6/13 20:41
 * @desc
 */
public class BaseX {
    public static final byte PAD_BYTE = 0x7f;

    public static String encodeMappingAsString(byte[] arr, String mapping) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            byte bt = arr[i];
            if (bt == PAD_BYTE) {
                builder.append("=");
            } else {
                char ch = mapping.charAt(bt);
                builder.append(ch);
            }
        }
        return builder.toString();
    }


    public static byte[] decodeMappingAsBytes(String str, String mapping) {
        int strLen = str.length();
        int mappingLen = mapping.length();
        byte[] data = new byte[strLen];
        Map<Character, Byte> map = new LinkedHashMap<>();
        for (int i = 0; i < mappingLen; i++) {
            map.put(mapping.charAt(i), (byte) i);
        }
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            Byte bt = map.get(ch);
            if (bt == null) {
                data[i] = PAD_BYTE;
            } else {
                data[i] = bt;
            }
        }
        return data;
    }
}
