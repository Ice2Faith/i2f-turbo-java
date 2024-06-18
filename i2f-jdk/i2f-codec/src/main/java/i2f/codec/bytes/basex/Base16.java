package i2f.codec.bytes.basex;

/**
 * @author Ice2Faith
 * @date 2024/6/13 16:59
 * @desc
 */
public class Base16 {
    public static final String BASE16_BASE_MAPPING = "0123456789"
            + "ABCDEF";

    public static byte[] encodeRaw(byte[] data) {
        int srcGroupLen = 1;
        int dstGroupLen = 2;
        int moreLen = data.length % srcGroupLen;
        int srcPadLen = (moreLen == 0 ? 0 : srcGroupLen - moreLen);
        int dstPadLen = 0;
        if (srcPadLen == 1) {
            dstPadLen = 1;
        }
        int srcLen = data.length + srcPadLen;
        int dstLen = srcLen / srcGroupLen * dstGroupLen;
        byte[] src = new byte[srcLen];
        byte[] dst = new byte[dstLen];

        for (int i = 0; i < src.length; i++) {
            src[i] = 0;
            if (i < data.length) {
                src[i] = data[i];
            }
        }

        for (int i = 0, j = 0; i < srcLen; i += srcGroupLen, j += dstGroupLen) {
            dst[j + 0] = (byte) ((src[i + 0] >>> 4) & 0x0f);
            dst[j + 1] = (byte) ((src[i + 0] & 0x0f));
        }

        for (int i = 0; i < dstPadLen; i++) {
            dst[dst.length - 1 - i] = BaseX.PAD_BYTE;
        }

        return dst;
    }

    public static byte[] decodeRaw(byte[] data) {
        int srcGroupLen = 2;
        int dstGroupLen = 1;
        int moreLen = data.length % srcGroupLen;
        int srcPadLen = (moreLen == 0 ? 0 : srcGroupLen - moreLen);
        int dstPadLen = 0;
        int srcLen = data.length + srcPadLen;
        int dstLen = srcLen / srcGroupLen * dstGroupLen;
        byte[] src = new byte[srcLen];
        byte[] dst = new byte[dstLen];

        for (int i = 0; i < src.length; i++) {
            src[i] = 0;
            if (i < data.length) {
                if (data[i] == BaseX.PAD_BYTE) {
                    dstPadLen++;
                }
                src[i] = data[i];
            } else {
                dstPadLen++;
            }
        }

        if (dstPadLen == 1) {
            dstPadLen = 1;
        }

        for (int i = 0, j = 0; i < srcLen; i += srcGroupLen, j += dstGroupLen) {
            dst[j + 0] = (byte) (((src[i + 0] & 0x0f) << 4) | (src[i + 1] & 0x0f));
        }

        if (dstPadLen == 0) {
            return dst;
        }

        byte[] ret = new byte[dstLen - dstPadLen];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = dst[i];
        }

        return ret;
    }

    public static String encode(byte[] data, String mapping) {
        byte[] arr = encodeRaw(data);
        return BaseX.encodeMappingAsString(arr, mapping);
    }

    public static byte[] decode(String str, String mapping) {
        byte[] data = BaseX.decodeMappingAsBytes(str, mapping);
        return decodeRaw(data);
    }

    public static String encode(byte[] data) {
        return encode(data, BASE16_BASE_MAPPING);
    }

    public static byte[] decode(String str) {
        return decode(str, BASE16_BASE_MAPPING);
    }


    public static void main(String[] args) {
        String raw = "";
        for (int i = 0; i < 10; i++) {
            raw += (char) ('0' + i);
            System.out.println("-------------");
            System.out.println(raw);
            String b64 = encode(raw.getBytes());
            System.out.println(b64);
            b64 = b64.replaceAll("=", "");
            String dec = new String(decode(b64));
            System.out.println(dec);
            System.out.println(raw.equals(dec));
        }
    }

}
