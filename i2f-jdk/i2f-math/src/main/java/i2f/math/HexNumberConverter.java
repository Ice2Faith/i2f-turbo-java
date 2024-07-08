package i2f.math;

/**
 * @author Ice2Faith
 * @date 2024/7/8 10:46
 * @desc
 */
public class HexNumberConverter {
    public static final String MAPPING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static boolean isNumberChar(char ch, int base) {
        if (ch >= 'a' && ch <= 'z') {
            ch &= ~32;
        }
        int i = 0;
        while (i < MAPPING.length() && i < base) {
            if (ch == MAPPING.charAt(i)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static int getNumCharValue(char ch, int base) {
        if (ch >= 'a' && ch <= 'z') {
            ch &= ~32;
        }
        int i = 0;
        while (i < MAPPING.length() && i < base) {
            if (ch == MAPPING.charAt(i)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static double hex2number(String str, int base) {
        double ret = 0;
        if (!isNumberChar(str.charAt(0), base)) {
            return 0;
        }
        int i = 0;
        while (i < str.length() && isNumberChar(str.charAt(i), base)) {
            ret = ret * base;
            int cnum = getNumCharValue(str.charAt(i), base);
            ret = ret + cnum;
            i++;
        }
        if (i < str.length() && str.charAt(i) == '.') {
            i++;
            double lbit = 1.0;
            while (i < str.length() && isNumberChar(str.charAt(i), base)) {
                lbit = lbit * base;
                int cnum = getNumCharValue(str.charAt(i), base);
                ret = ret + cnum / lbit;
                i++;
            }
        }
        return ret;
    }

    public static String number2hex(long num, int base) {
        return number2hex(num, base, 0);
    }

    public static String number2hex(int num, int base) {
        return number2hex(num, base, 0);
    }

    public static String number2hex(double num, int base, int scale) {
        StringBuilder hex = new StringBuilder();
        if (base < 2 || base > MAPPING.length() || scale < 0) {
            return hex.toString();
        }


        char[] temp = new char[256];
        int otc = (int) num;
        double flo = num - otc;
        int len = 0;
        while (otc != 0) {
            int pnum = otc % base;
            temp[len++] = MAPPING.charAt(pnum);
            otc = (int) otc / base;
        }
        int tlen = len - 1;
        if (tlen <= 0) {
            hex.append('0');
        }
        while (tlen >= 0) {
            hex.append(temp[tlen]);
            tlen--;
        }
        if (scale > 0) {
            hex.append('.');
            int dotIndex = hex.length();
            int i = 0;
            while (flo != 0.0 && i < scale) {
                int pnum = (int) (flo * base);
                hex.append(MAPPING.charAt(pnum));
                flo = flo * base - pnum;
                i++;
            }
            if (dotIndex == hex.length()) {
                hex.append('0');
            }
        }

        return hex.toString();
    }

    public static void main(String[] args) {
        String str = number2hex(17.357, 12, 20);
        System.out.println(str);
        double value = hex2number(str, 12);
        System.out.println(value);
    }
}
