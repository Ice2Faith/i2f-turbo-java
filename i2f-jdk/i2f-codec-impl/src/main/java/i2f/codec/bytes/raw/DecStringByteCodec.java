package i2f.codec.bytes.raw;

import i2f.codec.std.bytes.IStringByteCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:44
 * @desc
 */
public class DecStringByteCodec implements IStringByteCodec {
    public static DecStringByteCodec INSTANCE = new DecStringByteCodec(null);
    private String separator;

    public DecStringByteCodec() {
    }

    public DecStringByteCodec(String separator) {
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
            builder.append(String.format("%03d", (int) (data[i] & 0x0ff)));

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
            for (int i = 0; (i + 3) <= dlen; i += 3) {
                String item = enc.substring(i, i + 3);
                parts.add(item);
            }
        }
        int size = parts.size();
        byte[] ret = new byte[size];
        for (int i = 0; i < size; i++) {
            try {
                int num = Integer.parseInt(parts.get(i), 10);
                ret[i] = (byte) (num & 0x0ff);
            } catch (Exception e) {
                ret[i] = 0;
            }
        }
        return ret;
    }
}
