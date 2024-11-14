package i2f.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * 7891,2345,6789,1234.56789
 * 一万,两千三百四十五,亿,六千几百八十九,万,一千二百三十四，又，五角六分七厘八毫九
 *
 * @author Ice2Faith
 * @date 2024/10/28 10:09
 */
public class CnNumber {
    /**
     * 固定0-9
     */
    public static final String[] CN_SIM_NUMBERS = {
            "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九"
    };
    public static final String[] CN_TRA_NUMBERS = {
            "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"
    };
    /**
     * 固定，中国人读数，每4位一组地读
     */
    public static final String[] CN_SIM_PARTS = {
            "", "十", "百", "千"
    };
    public static final String[] CN_TRA_PARTS = {
            "", "拾", "佰", "仟"
    };
    /**
     * 每4位一组对应的大单位
     * 第0位视为基础单位
     */
    public static final String[] CN_SIM_UNITS = {
            "", "万", "亿", "万亿", "兆", "京", "垓", "杼", "穰", "沟", "涧"
    };
    public static final String[] CN_TRA_UNITS = {
            "", "萬", "億", "萬億", "兆", "京", "垓", "杼", "穰", "沟", "涧"
    };
    /**
     * 每一个小数位
     * 第0位视为小数点
     */
    public static final String[] CN_SIM_METERS = {
            "点", "分", "厘", "毫"
    };
    public static final String[] CN_SIM_MONEYS = {
            "元", "角", "分", "厘", "毫"
    };
    public static final String[] CN_TRA_MONEYS = {
            "圆", "角", "分", "厘", "毫"
    };

    public static void main(String[] args) {
        // 七千七百七十七杼八千垓九千九百〇九京一千〇一兆〇一万亿四千〇五亿六千〇七万〇八十九元一角二分三厘四毫〇〇五六七八九一二三四五六七八九
        BigDecimal num = new BigDecimal("7777_8000_9909_1001_0001_4005_6007_0089.1234_0056789123456789".replaceAll("_", ""));
        int percision = 20;
        String str = number2CnSimMoney(num, percision);
        System.out.println(str);

        str = number2CnTraMoney(num, percision);
        System.out.println(str);

        str = number2CnSim(num, percision);
        System.out.println(str);

        str = number2CnTra(num, percision);
        System.out.println(str);

        for (int i = 0; i < 10; i++) {
            System.out.println("===================");
            double val = (int) (Math.random() * 10000000) + Math.random();
            System.out.println(val);
            System.out.println(number2CnSimMoney(BigDecimal.valueOf(val), 4));
            System.out.println(number2CnTraMoney(BigDecimal.valueOf(val), 4));
            System.out.println(number2CnSim(BigDecimal.valueOf(val), 4));
            System.out.println(number2CnTra(BigDecimal.valueOf(val), 4));
        }
    }

    public static String number2CnSim(BigDecimal num, int precision) {
        return number2Cn0(num, precision, CN_SIM_NUMBERS, CN_SIM_PARTS, CN_SIM_UNITS, CN_SIM_METERS);
    }

    public static String number2CnTra(BigDecimal num, int precision) {
        return number2Cn0(num, precision, CN_TRA_NUMBERS, CN_TRA_PARTS, CN_TRA_UNITS, CN_SIM_METERS);
    }

    public static String number2CnSimMoney(BigDecimal num, int precision) {
        String str = number2Cn0(num, precision, CN_SIM_NUMBERS, CN_SIM_PARTS, CN_SIM_UNITS, CN_SIM_MONEYS);
        if (!str.contains(CN_SIM_MONEYS[0])) {
            str += CN_SIM_MONEYS[0];
        }
        return str;
    }

    public static String number2CnTraMoney(BigDecimal num, int precision) {
        String str = number2Cn0(num, precision, CN_TRA_NUMBERS, CN_TRA_PARTS, CN_TRA_UNITS, CN_TRA_MONEYS);
        if (!str.contains(CN_TRA_MONEYS[0])) {
            str += CN_TRA_MONEYS[0];
        }
        return str;
    }

    public static final BigInteger NUM_10_POW_4 = new BigInteger("10000");

    public static String number2Cn0(BigDecimal num, int precision, String[] cnNumbers, String[] cnParts, String[] cnUnits, String[] cnFacUnits) {
        MathContext context = new MathContext(Math.max(precision, 20), RoundingMode.HALF_UP);

        boolean neg = num.compareTo(BigDecimal.ZERO) < 0;
        num = num.abs();
        BigInteger srcDec = num.toBigInteger();
        BigDecimal srcFac = num.subtract(new BigDecimal(srcDec), context);

        BigInteger dec = srcDec;
        BigDecimal fac = srcFac;

        Deque<Map.Entry<String, String>> deque = new ArrayDeque<>();
        int unitIdx = 0;

        while (dec.compareTo(BigInteger.ZERO) > 0) {
            int cur = (dec.mod(NUM_10_POW_4)).intValue();

            for (int i = 0; i < 4; i++) {
                int a = cur % 10;

                deque.addLast(new AbstractMap.SimpleEntry<>(cnNumbers[a] + cnParts[i], cnUnits[unitIdx]));

                cur /= 10;
            }

            unitIdx++;

            dec = dec.divide(NUM_10_POW_4);
        }


        StringBuilder builder = new StringBuilder();

        if (neg) {
            builder.append("负");
        }

        if (srcDec.compareTo(BigInteger.ZERO) == 0) {
            builder.append(cnNumbers[0]);
        }

        String lastUnit = null;
        boolean lastZero = false;
        boolean isFirst = true;
        while (!deque.isEmpty()) {
            Map.Entry<String, String> entry = deque.removeLast();
            String key = entry.getKey();

            if (key.startsWith(cnNumbers[0])) {
                lastZero = true;
                if (!entry.getValue().equals(lastUnit)) {
                    if (lastUnit != null) {
                        builder.append(lastUnit);
                    }
                    lastUnit = entry.getValue();
                }
                continue;
            }
            String value = entry.getValue();
            if (value.equals(lastUnit)) {
                if (lastZero) {
                    if (!isFirst) {
                        builder.append(cnNumbers[0]);
                    }
                }
                builder.append(key);
            } else {
                if (lastUnit != null) {
                    builder.append(lastUnit);
                }
                builder.append(key);
            }

            isFirst = false;

            lastUnit = entry.getValue();
            lastZero = key.startsWith(cnNumbers[0]);
        }


        BigDecimal digital = BigDecimal.valueOf(Math.pow(0.1, precision));
        if (srcFac.compareTo(digital) > 0) {
            builder.append(cnFacUnits[0]);
        }

        int percIdx = 1;
        int tailZeroCnt = 0;
        while (fac.compareTo(digital) > 0 && precision > 0) {
            fac = fac.multiply(BigDecimal.TEN, context);

            int a = fac.intValue();
            if (a == 0) {
                tailZeroCnt++;
            } else {
                for (int i = 0; i < tailZeroCnt; i++) {
                    deque.addLast(new AbstractMap.SimpleEntry<>(cnNumbers[0], cnFacUnits.length > percIdx - i ? cnFacUnits[percIdx - i] : ""));
                }
                deque.addLast(new AbstractMap.SimpleEntry<>(cnNumbers[a], cnFacUnits.length > percIdx ? cnFacUnits[percIdx] : ""));
                tailZeroCnt = 0;
            }


            fac = fac.subtract(BigDecimal.valueOf(fac.intValue()), context);

            precision--;
            percIdx++;
        }

        percIdx = 1;
        lastZero = false;
        tailZeroCnt = 0;
        while (!deque.isEmpty()) {
            Map.Entry<String, String> entry = deque.removeFirst();
            String key = entry.getKey();

            if (percIdx >= cnFacUnits.length) {
                for (int i = 0; i < tailZeroCnt; i++) {
                    builder.append(cnNumbers[0]);
                }
                tailZeroCnt = 0;
                builder.append(key);
                percIdx++;
                continue;
            }

            if (key.startsWith(cnNumbers[0])) {
                lastZero = true;
                percIdx++;
                tailZeroCnt++;
                continue;
            }
            if (lastZero) {
                builder.append(cnNumbers[0]);
            }
            builder.append(key);
            builder.append(entry.getValue());
            tailZeroCnt = 0;

            lastZero = key.startsWith(cnNumbers[0]);
            percIdx++;
        }


        return builder.toString();
    }
}
