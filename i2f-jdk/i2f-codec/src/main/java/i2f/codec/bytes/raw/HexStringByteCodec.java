package i2f.codec.bytes.raw;

import i2f.codec.bytes.IStringByteCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:37
 * @desc
 */
public class HexStringByteCodec implements IStringByteCodec {
    public static HexStringByteCodec INSTANCE = new HexStringByteCodec(null);
    private String separator;

    public HexStringByteCodec() {
    }

    public HexStringByteCodec(String separator) {
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
            builder.append(String.format("%02X", (int) (data[i] & 0x0ff)));
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
            for (int i = 0; (i + 2) <= dlen; i += 2) {
                String item = enc.substring(i, i + 2);
                parts.add(item);
            }
        }
        int size = parts.size();
        byte[] ret = new byte[size];
        for (int i = 0; i < size; i++) {
            try {
                int num = Integer.parseInt(parts.get(i), 16);
                ret[i] = (byte) (num & 0x0ff);
            } catch (Exception e) {
                ret[i] = 0;
            }
        }
        return ret;
    }
}
