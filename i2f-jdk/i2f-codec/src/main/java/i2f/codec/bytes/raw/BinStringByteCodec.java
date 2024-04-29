package i2f.codec.bytes.raw;

import i2f.codec.bytes.IStringByteCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:43
 * @desc
 */
public class BinStringByteCodec implements IStringByteCodec {
    public static BinStringByteCodec INSTANCE = new BinStringByteCodec(null);
    private String separator;

    public BinStringByteCodec() {
    }

    public BinStringByteCodec(String separator) {
        this.separator = separator;
    }

    @Override
    public String encode(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                if (separator != null) {
                    builder.append(separator);
                }
            }
            int bt = data[i] & 0x0ff;
            for (int j = 7; j >= 0; j--) {
                int dt = (bt >>> j) & 0x01;
                builder.append(dt);
            }
        }
        return builder.toString();
    }

    @Override
    public byte[] decode(String enc) {
        List<String> parts = new ArrayList<>();
        if (separator != null) {
            String[] arr = enc.split(separator);
            for (String item : arr) {
                parts.add(item);
            }
        } else {
            int dlen = enc.length();
            for (int i = 0; (i + 8) <= dlen; i += 8) {
                String item = enc.substring(i, i + 8);
                parts.add(item);
            }
        }
        int size = parts.size();
        byte[] ret = new byte[size];
        for (int i = 0; i < size; i++) {
            int num = 0;
            String item = parts.get(i);
            if (item != null) {
                char[] arr = item.toCharArray();
                for (int j = 0; j < arr.length; j++) {
                    if (arr[j] == '1') {
                        num = (num << 1) | 1;
                    } else if (arr[j] == '0') {
                        num = (num << 1) | 0;
                    }
                }
            }
            ret[i] = (byte) (num & 0x0ff);
        }
        return ret;
    }
}
