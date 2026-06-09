package i2f.tools;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/9 14:10
 * @desc 《易》常量
 */
public class Yi {
    /**
     * 阴阳
     * ----------------------------
     * 大多数都是奇数为阳，偶数为阴
     * 对应到编程里面，因为下标从0开始，因此就是偶数为阳，奇数为阴
     */
    public static final String[] YIN_YANG = "阳阴".split("");

    /**
     * 五行
     * ---------------------------
     * 常说的是金木水火土
     * 但是，木火土金水这种顺序
     * 从前往后，顺相生，前一个生后一个，比如木生火，火生土
     * 隔相克，中间隔着一个，前一个克后一个，比如木克土，火克金
     * 画成一个圆环就更加形象了
     */
    public static final String[] WU_XING = "木火土金水".split("");

    /**
     * 五行-相生
     */
    public static final Map<String, String> WU_XING_SHENG_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("木", "火");
            put("火", "土");
            put("土", "金");
            put("金", "水");
            put("水", "木");
        }
    });

    /**
     * 五行-相克
     */
    public static final Map<String, String> WU_XING_KE_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("木", "土");
            put("火", "金");
            put("土", "水");
            put("金", "木");
            put("水", "火");
        }
    });

    /**
     * 五行-关系
     */
    public static final String[] WU_XING_RELATION = {
            "同我", "生我", "克我", "我生", "我克"
    };

    /**
     * 五行-十神-阳五神
     * ---------------
     * 五行-关系，一一对应
     */
    public static final String[] WU_XING_SHI_SHEN_YANG = {
            "比肩", "正印", "正官", "食神", "正财"
    };

    /**
     * 五行-十神-阴五神
     * ---------------
     * 五行-关系，一一对应
     */
    public static final String[] WU_XING_SHI_SHEN_YIN = {
            "劫财", "偏印", "七杀", "枭神", "偏财"
    };

    /**
     * 五方
     * ---------------------------
     * 常说的是，东南西北中
     * 但是，东南中西北这种顺序
     * 刚好和木火土金水吻合，也与五行一一对应
     */
    public static final String[] WU_FANG = "东南中西北".split("");

    /**
     * 五色
     * ---------------------------
     * 也与五行一一对应
     */
    public static final String[] WU_SE = "青赤黄白黑".split("");

    /**
     * 五神兽
     * -------------------------
     * 也与五行一一对应
     * 中间的有两种说法，一种是黄龙，一种是轩辕
     * 这里使用黄龙，与五色对应
     */
    public static final String[] WU_SHOU = {"青龙", "朱雀", "黄龙", "白虎", "玄武"};

    /**
     * 天干
     */
    public static final String[] GAN = "甲乙丙丁戊己庚辛壬癸".split("");

    /**
     * 天干-五行
     */
    public static final Map<String, String> GAN_WU_XING_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("甲", "木");
            put("乙", "木");
            put("丙", "火");
            put("丁", "火");
            put("戊", "土");
            put("己", "土");
            put("庚", "金");
            put("辛", "金");
            put("壬", "水");
            put("癸", "水");
        }
    });

    /**
     * 天干-阴阳
     */
    public static final Map<String, String> GAN_YIN_YANG_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("甲", "阳");
            put("乙", "阴");
            put("丙", "阳");
            put("丁", "阴");
            put("戊", "阳");
            put("己", "阴");
            put("庚", "阳");
            put("辛", "阴");
            put("壬", "阳");
            put("癸", "阴");
        }
    });

    /**
     * 地支
     */
    public static final String[] ZHI = "子丑寅卯辰巳午未申酉戌亥".split("");

    /**
     * 地支-五行
     */
    public static final Map<String, String> ZHI_WU_XING_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("寅", "木");
            put("卯", "木");
            put("巳", "火");
            put("午", "火");
            put("申", "金");
            put("酉", "金");
            put("亥", "水");
            put("子", "水");
            put("辰", "土");
            put("未", "土");
            put("戌", "土");
            put("丑", "土");
        }
    });

    /**
     * 地支-五行
     */
    public static final Map<String, String> ZHI_YIN_YANG_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("子", "阳");
            put("丑", "阴");
            put("寅", "阳");
            put("卯", "阴");
            put("辰", "阳");
            put("巳", "阴");
            put("午", "阳");
            put("未", "阴");
            put("申", "阳");
            put("酉", "阴");
            put("戌", "阳");
            put("亥", "阴");
        }
    });

    /**
     * 干支-五行
     */
    public static final Map<String, String> GAN_ZHI_WU_XING_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            putAll(GAN_WU_XING_RULE);
            putAll(ZHI_WU_XING_RULE);
        }
    });

    /**
     * 干支-阴阳
     */
    public static final Map<String, String> GAN_ZHI_YIN_YANG_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            putAll(GAN_YIN_YANG_RULE);
            putAll(ZHI_YIN_YANG_RULE);
        }
    });

    /**
     * 五虎遁，年上起月口诀
     * 正月建寅，寅为虎，所以叫五虎遁，因此正月就是寅月，只需要推导出天干，然后依次顺推即可
     * ------------------------------------------------
     * 甲己之年丙作首，
     * 乙庚之岁戊为头，
     * 丙辛必定寻庚起，
     * 丁壬壬位顺行流，
     * 若问戊癸何方发，
     * 甲寅之上好追求。
     */
    public static final Map<String, String> WU_HU_DUN_MONTH_OF_YEAR_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("甲", "丙");
            put("己", "丙");
            put("乙", "戊");
            put("庚", "戊");
            put("丙", "庚");
            put("辛", "庚");
            put("丁", "壬");
            put("壬", "壬");
            put("戊", "甲");
            put("癸", "甲");
        }
    });

    /**
     * 五鼠遁，日上起时口诀
     * 子时作为一天的起止，子为鼠，所以叫五鼠遁
     * ---------------------------------
     * 甲己还加甲，
     * 乙庚丙作初，
     * 丙辛从戊起，
     * 丁壬庚子居，
     * 戊癸何方发，
     * 壬子是真途。
     */
    public static final Map<String, String> WU_SHU_DUN_HOUR_OF_DAY_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("甲", "甲");
            put("己", "甲");
            put("乙", "丙");
            put("庚", "丙");
            put("丙", "戊");
            put("辛", "戊");
            put("丁", "庚");
            put("壬", "庚");
            put("戊", "壬");
            put("癸", "壬");
        }
    });

    /**
     * 节，名称
     */
    public static final String[] JIE_NAMES = {
            "小寒",
            "立春",
            "惊蛰",
            "清明",
            "立夏",
            "芒种",
            "小暑",
            "立秋",
            "白露",
            "寒露",
            "立冬",
            "大雪"
    };

    /**
     * 气，名称
     */
    public static final String[] QI_NAMES = {
            "大寒",
            "雨水",
            "春分",
            "谷雨",
            "小满",
            "夏至",
            "大暑",
            "处暑",
            "秋分",
            "霜降",
            "小雪",
            "冬至"
    };

    /**
     * 获取五行关系
     *
     * @param meWuXing    我的五行
     * @param otherWuXing 他的五行
     * @return
     */
    public static String getWuXingRelation(String meWuXing, String otherWuXing) {
        if (meWuXing == null || otherWuXing == null) {
            return null;
        }
        if (meWuXing.equals(otherWuXing)) {
            return "同我";
        }
        if (otherWuXing.equals(WU_XING_SHENG_RULE.get(meWuXing))) {
            return "我生";
        }
        if (otherWuXing.equals(WU_XING_KE_RULE.get(meWuXing))) {
            return "我克";
        }
        if (meWuXing.equals(WU_XING_SHENG_RULE.get(otherWuXing))) {
            return "生我";
        }
        if (meWuXing.equals(WU_XING_KE_RULE.get(otherWuXing))) {
            return "克我";
        }
        return null;
    }

    /**
     * 根据天干获取五行
     *
     * @param gan 天干
     * @return
     */
    public static String getWuXingByGan(String gan) {
        return GAN_WU_XING_RULE.get(gan);
    }

    /**
     * 根据地支获取五行
     *
     * @param zhi 地支
     * @return
     */
    public static String getWuXingByZhi(String zhi) {
        return ZHI_WU_XING_RULE.get(zhi);
    }

    /**
     * 根据天干或地支获取五行
     *
     * @param gz 天干/地支
     * @return
     */
    public static String getWuXingByGanOrZhi(String gz) {
        return GAN_ZHI_WU_XING_RULE.get(gz);
    }

    /**
     * 根据天干获取阴阳
     *
     * @param gan 天干
     * @return
     */
    public static String getYinYangByGan(String gan) {
        return GAN_YIN_YANG_RULE.get(gan);
    }

    /**
     * 根据地支获取阴阳
     *
     * @param zhi 地支
     * @return
     */
    public static String getYinYangByZhi(String zhi) {
        return ZHI_YIN_YANG_RULE.get(zhi);
    }

    /**
     * 根据天干或地支获取阴阳
     *
     * @param gz 天干/地支
     * @return
     */
    public static String getYinYangByGanOrZhi(String gz) {
        return GAN_ZHI_YIN_YANG_RULE.get(gz);
    }

    /**
     * 根据五行关系，以及他的阴阳属性，获取十神
     *
     * @param wuXingRelation 五行关系
     * @param otherYinYang   他的阴阳属性
     * @return
     */
    public static String getShiShenByRelationAndYinYang(String wuXingRelation, String otherYinYang) {
        int idx = -1;
        for (int i = 0; i < WU_XING_RELATION.length; i++) {
            if (WU_XING_RELATION[i].equals(wuXingRelation)) {
                idx = i;
                break;
            }
        }
        if ("阳".equals(otherYinYang)) {
            return WU_XING_SHI_SHEN_YANG[idx];
        }
        return WU_XING_SHI_SHEN_YIN[idx];
    }

    /**
     * 根据双方的干支获取十神
     *
     * @param meGanOrZhi    我的干或支
     * @param otherGanOrZhi 他的干或支
     * @return
     */
    public static String getShiShenByGanOrZhi(String meGanOrZhi, String otherGanOrZhi) {
        String meWuXing = getWuXingByGanOrZhi(meGanOrZhi);
        String otherWuXing = getWuXingByGanOrZhi(otherGanOrZhi);
        String wuXingRelation = getWuXingRelation(meWuXing, otherWuXing);
        String otherYinYang = getYinYangByGanOrZhi(otherGanOrZhi);
        return getShiShenByRelationAndYinYang(wuXingRelation, otherYinYang);
    }
}
