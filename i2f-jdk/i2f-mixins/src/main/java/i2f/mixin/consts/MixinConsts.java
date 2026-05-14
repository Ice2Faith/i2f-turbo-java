package i2f.mixin.consts;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/5/14 8:57
 * @desc
 */
public final class MixinConsts {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static final ThreadLocal<Map<String, Object>> LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static final MathContext MATH_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    public static final BigDecimal NUM_0_5 = new BigDecimal("0.5");

    public static final String[][] ORACLE_REGEX_REPLACE_MAPPING = {
            {"[:alpha:]", "a-zA-Z"},
            {"[:alnum:]", "a-zA-Z0-9"},
            {"[:alphanum:]", "a-zA-Z0-9"},
            {"[:punct:]", "\\!-\\&\\(-/:-@\\[-`\\{-\\~'"},
            {"[:digit:]", "0-9"},
            {"[:lower:]", "a-z"},
            {"[:upper:]", "A-Z"},
            {"[:blank:]", "\\s"},
            {"[:grah:]", "\\S"},

    };

    public static final String[][] CHRONO_UNIT_MAPPING = {
            {"day", "DAYS"},
            {"dd", "DAYS"},
            {"month", "MONTHS"},
            {"mon", "MONTHS"},
            {"mm", "MONTHS"},
            {"year", "YEARS"},
            {"yyyy", "YEARS"},
            {"minute", "MINUTES"},
            {"min", "MINUTES"},
            {"mi", "MINUTES"},
            {"second", "SECONDS"},
            {"sec", "SECONDS"},
            {"ss", "SECONDS"},
            {"hour", "HOURS"},
            {"hh", "HOURS"},
            {"mill", "MILLIS"},
            {"ms", "MILLIS"},
            {"sss", "MILLIS"},
            {"week", "WEEKS"},
            {"ww", "WEEKS"},
            {"micro", "MICROS"},
            {"nano", "NANOS"},
            {"ns", "NANOS"},

    };
}
