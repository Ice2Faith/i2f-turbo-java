package i2f.codec.bytes.basex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

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


    public static void groupConvert(InputStream is, OutputStream os, int srcGroupLen, Function<byte[], byte[]> converter) throws IOException {
        byte[] srcData = new byte[srcGroupLen];
        int srcLen = 0;
        int bt = 0;
        while ((bt = is.read()) >= 0) {
            srcData[srcLen] = (byte) bt;
            srcLen++;
            if (srcLen == srcData.length) {
                byte[] dstData = converter.apply(srcData);
                os.write(dstData);
                os.flush();
                srcLen = 0;
            }
        }
        if (srcLen > 0) {
            byte[] leftData = new byte[srcLen];
            for (int i = 0; i < leftData.length; i++) {
                leftData[i] = srcData[i];
            }
            byte[] dstData = converter.apply(leftData);
            os.write(dstData);
            os.flush();
        }
    }
}
