package i2f.bytes;

/**
 * @author Ice2Faith
 * @date 2024/6/13 20:52
 * @desc
 */
public class ByteUtil {
    public static final int LONG_BYTE_COUNT = 8;
    public static final int INT_BYTE_COUNT = 4;
    public static final int SHORT_BYTE_COUNT = 2;
    public static final int CHAR_BYTE_COUNT = 2;
    public static final int BOOLEAN_BYTE_COUNT = 1;
    public static final int FLOAT_BYTE_COUNT = 4;
    public static final int DOUBLE_BYTE_COUNT = 8;

    public static byte[] toBigEndian(double value) {
        return toBigEndian(Double.doubleToLongBits(value), DOUBLE_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(double value) {
        return toLittleEndian(Double.doubleToLongBits(value), DOUBLE_BYTE_COUNT);
    }

    public static byte[] toBigEndian(float value) {
        return toBigEndian(Float.floatToIntBits(value), FLOAT_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(float value) {
        return toLittleEndian(Float.floatToIntBits(value), FLOAT_BYTE_COUNT);
    }

    public static byte[] toBigEndian(boolean value) {
        return toBigEndian(value ? 1 : 0, BOOLEAN_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(boolean value) {
        return toLittleEndian(value ? 1 : 0, BOOLEAN_BYTE_COUNT);
    }

    public static byte[] toBigEndian(char value) {
        return toBigEndian(value, CHAR_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(char value) {
        return toLittleEndian(value, CHAR_BYTE_COUNT);
    }

    public static byte[] toBigEndian(short value) {
        return toBigEndian(value, SHORT_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(short value) {
        return toLittleEndian(value, SHORT_BYTE_COUNT);
    }

    public static byte[] toBigEndian(int value) {
        return toBigEndian(value, INT_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(int value) {
        return toLittleEndian(value, INT_BYTE_COUNT);
    }

    public static byte[] toBigEndian(long value) {
        return toBigEndian(value, LONG_BYTE_COUNT);
    }

    public static byte[] toLittleEndian(long value) {
        return toLittleEndian(value, LONG_BYTE_COUNT);
    }


    public static byte[] toBigEndian(long value, int byteCount) {
        byte[] ret = new byte[byteCount];
        toBigEndian(value, byteCount, ret, 0);
        return ret;
    }

    public static byte[] toLittleEndian(long value, int byteCount) {
        byte[] ret = new byte[byteCount];
        toLittleEndian(value, byteCount, ret, 0);
        return ret;
    }

    public static void toBigEndian(double value, byte[] arr, int offset) {
        toBigEndian(Double.doubleToLongBits(value), DOUBLE_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(double value, byte[] arr, int offset) {
        toLittleEndian(Double.doubleToLongBits(value), DOUBLE_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(float value, byte[] arr, int offset) {
        toBigEndian(Float.floatToIntBits(value), FLOAT_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(float value, byte[] arr, int offset) {
        toLittleEndian(Float.floatToIntBits(value), FLOAT_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(boolean value, byte[] arr, int offset) {
        toBigEndian(value ? 1 : 0, BOOLEAN_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(boolean value, byte[] arr, int offset) {
        toLittleEndian(value ? 1 : 0, BOOLEAN_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(char value, byte[] arr, int offset) {
        toBigEndian(value, CHAR_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(char value, byte[] arr, int offset) {
        toLittleEndian(value, CHAR_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(short value, byte[] arr, int offset) {
        toBigEndian(value, SHORT_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(short value, byte[] arr, int offset) {
        toLittleEndian(value, SHORT_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(int value, byte[] arr, int offset) {
        toBigEndian(value, INT_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(int value, byte[] arr, int offset) {
        toLittleEndian(value, INT_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(long value, byte[] arr, int offset) {
        toBigEndian(value, LONG_BYTE_COUNT, arr, offset);
    }

    public static void toLittleEndian(long value, byte[] arr, int offset) {
        toLittleEndian(value, LONG_BYTE_COUNT, arr, offset);
    }

    public static void toBigEndian(long value, int byteCount, byte[] arr, int offset) {
        // long to big-endian
        for (int i = 0; i < byteCount; i++) {
            arr[offset + i] = (byte) (value >>> (8 * (byteCount - 1 - i)) & 0x0ff);
        }
    }

    public static void toLittleEndian(long value, int byteCount, byte[] arr, int offset) {
        // long to little-endian
        for (int i = 0; i < byteCount; i++) {
            arr[offset + i] = (byte) (value >>> (8 * (i)) & 0x0ff);
        }
    }

    public static long ofBigEndian(byte[] data, int offset, int byteCount) {
        long ret = 0;
        for (int i = 0; i < byteCount; i++) {
            ret <<= 8;
            ret |= data[offset + i];
        }
        return ret;
    }

    public static long ofLittleEndian(byte[] data, int offset, int byteCount) {
        long ret = 0;
        for (int i = 0; i < byteCount; i++) {
            ret <<= 8;
            ret |= data[offset + (byteCount - 1 - i)];
        }
        return ret;
    }

    public static long ofBigEndianLong(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, LONG_BYTE_COUNT);
        return ret;
    }

    public static int ofBigEndianInt(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, INT_BYTE_COUNT);
        return (int) ret;
    }

    public static short ofBigEndianShort(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, SHORT_BYTE_COUNT);
        return (short) ret;
    }

    public static char ofBigEndianChar(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, CHAR_BYTE_COUNT);
        return (char) ret;
    }

    public static boolean ofBigEndianBoolean(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, BOOLEAN_BYTE_COUNT);
        return ret != 0;
    }

    public static float ofBigEndianFloat(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, FLOAT_BYTE_COUNT);
        return Float.intBitsToFloat((int) ret);
    }

    public static double ofBigEndianDouble(byte[] data, int offset) {
        long ret = ofBigEndian(data, offset, DOUBLE_BYTE_COUNT);
        return Double.longBitsToDouble(ret);
    }

    public static long ofLittleEndianLong(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, LONG_BYTE_COUNT);
        return ret;
    }

    public static int ofLittleEndianInt(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, INT_BYTE_COUNT);
        return (int) ret;
    }

    public static short ofLittleEndianShort(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, SHORT_BYTE_COUNT);
        return (short) ret;
    }

    public static char ofLittleEndianChar(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, CHAR_BYTE_COUNT);
        return (char) ret;
    }

    public static boolean ofLittleEndianBoolean(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, BOOLEAN_BYTE_COUNT);
        return ret != 0;
    }

    public static float ofLittleEndianFloat(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, FLOAT_BYTE_COUNT);
        return Float.intBitsToFloat((int) ret);
    }

    public static double ofLittleEndianDouble(byte[] data, int offset) {
        long ret = ofLittleEndian(data, offset, BOOLEAN_BYTE_COUNT);
        return Double.longBitsToDouble(ret);
    }
}
