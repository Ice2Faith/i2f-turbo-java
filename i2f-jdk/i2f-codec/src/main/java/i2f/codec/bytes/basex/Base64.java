package i2f.codec.bytes.basex;

/**
 * @author Ice2Faith
 * @date 2024/6/13 16:59
 * @desc
 */
public class Base64 {
    public static final String BASE64_BASE_MAPPING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789"
            + "+/";
    public static final String BASE64_URL_MAPPING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789"
            + "-_";

    public static byte[] encodeRaw(byte[] data) {
        int srcGroupLen = 3;
        int dstGroupLen = 4;
        int moreLen = data.length % srcGroupLen;
        int srcPadLen = (moreLen == 0 ? 0 : srcGroupLen - moreLen);
        int dstPadLen = 0;
        if (srcPadLen == 1) {
            dstPadLen = 1;
        } else if (srcPadLen == 2) {
            dstPadLen = 2;
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
            dst[j + 0] = (byte) ((src[i + 0] >>> 2) & 0x03f);
            dst[j + 1] = (byte) (((src[i + 0] & 0x03) << 4) | ((src[i + 1] >>> 4) & 0x0f));
            dst[j + 2] = (byte) (((src[i + 1] & 0x0f) << 2) | ((src[i + 2] >>> 6) & 0x03));
            dst[j + 3] = (byte) (src[i + 2] & 0x03f);
        }

        for (int i = 0; i < dstPadLen; i++) {
            dst[dst.length - 1 - i] = BaseX.PAD_BYTE;
        }

        return dst;
    }

    public static byte[] decodeRaw(byte[] data) {
        int srcGroupLen = 4;
        int dstGroupLen = 3;
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
        } else if (dstPadLen == 2) {
            dstPadLen = 2;
        }

        for (int i = 0, j = 0; i < srcLen; i += srcGroupLen, j += dstGroupLen) {
            dst[j + 0] = (byte) (((src[i + 0] & 0x03f) << 2) | ((src[i + 1] >>> 4) & 0x03));
            dst[j + 1] = (byte) (((src[i + 1] & 0x0f) << 4) | ((src[i + 2] >>> 2) & 0x0f));
            dst[j + 2] = (byte) (((src[i + 2] & 0x03) << 6) | (src[i + 3] & 0x03f));
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
        return encode(data, BASE64_BASE_MAPPING);
    }

    public static byte[] decode(String str) {
        return decode(str, BASE64_BASE_MAPPING);
    }

    public static String encodeUrl(byte[] data) {
        return encode(data, BASE64_URL_MAPPING);
    }

    public static byte[] decodeUrl(String str) {
        return decode(str, BASE64_URL_MAPPING);
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
