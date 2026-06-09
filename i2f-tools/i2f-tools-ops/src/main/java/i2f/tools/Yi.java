package i2f.tools;

import java.util.*;

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
            "比肩", "正印", "正官", "伤官", "正财"
    };

    /**
     * 五行-十神-阴五神
     * ---------------
     * 五行-关系，一一对应
     * 比肩，又叫劫财、败财
     * 偏官，又叫七杀
     * 偏印，又叫倒食、枭神
     */
    public static final String[] WU_XING_SHI_SHEN_YIN = {
            "劫财", "枭神", "七杀", "枭神", "偏财"
    };

    public static final Set<String> SHI_SHEN = Collections.unmodifiableSet(new LinkedHashSet<String>() {
        {
            addAll(Arrays.asList(WU_XING_SHI_SHEN_YANG));
            addAll(Arrays.asList(WU_XING_SHI_SHEN_YIN));
        }
    });



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
     * 藏干-支藏人元五行
     * 地支中藏天干
     * ---------------------------
     * 支藏人元歌诀
     * 子宫癸水在其中，丑癸辛金己土同；
     * 寅宫甲木兼丙戌，卯宫乙木独相逢；
     * 辰宫乙戊三分癸，巳宫庚金丙戊丛；
     * 午宫丁火并己土，未宫乙己丁共宗；
     * 申宫庚金壬水戊，酉宫辛金独丰隆；
     * 戌宫辛金及丁戊，亥宫壬甲是真踪。
     */
    public static final Map<String, String> CANG_GAN_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("子", "癸");
            put("丑", "癸辛己");
            put("寅", "甲丙戊");
            put("卯", "乙");
            put("辰", "乙戊癸");
            put("巳", "庚丙戊");
            put("午", "丁己");
            put("未", "乙己丁");
            put("申", "戊庚壬");
            put("酉", "辛");
            put("戌", "辛丁戊");
            put("亥", "甲壬");
        }
    });

    /**
     * 纳音五行歌决
     * 纳音是以一个干支来算的
     * 因此在四柱八字中，每一个柱都有干支，因此有四个纳音
     * ---------------------------------
     * 甲子乙丑海中金，丙寅丁卯炉中火，
     * 戊辰己巳大林木，庚午辛未路旁土，
     * 壬申癸酉剑锋金，甲戌乙亥山头火，
     * 丙子丁丑涧下水，戊寅己卯城头土，
     * 庚辰辛巳白蜡金，壬午癸未杨柳木，
     * 甲申乙酉泉中水，丙戌丁亥屋上土，
     * 戊子己丑霹雳火，庚寅辛卯松柏木，
     * 壬辰癸巳长流水，甲午乙未沙中金，
     * 丙申丁酉山下火，戊戌己亥平地木，
     * 庚子辛丑壁上土，壬寅癸卯金箔金，
     * 甲辰乙巳覆灯火，丙午丁未天河水，
     * 戊申己酉大驿土，庚戌辛亥钗钏金，
     * 壬子癸丑桑柘木，甲寅乙卯大溪水，
     * 丙辰丁巳沙中土，戊午己未天上火，
     * 庚申辛酉石榴木，壬戌癸亥大海水。
     */
    public static final Map<String, String> NA_YIN_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("甲子", "海中金");
            put("乙丑", "海中金");
            put("丙寅", "炉中火");
            put("丁卯", "炉中火");
            put("戊辰", "大林木");
            put("己巳", "大林木");
            put("庚午", "路旁土");
            put("辛未", "路旁土");
            put("壬申", "剑锋金");
            put("癸酉", "剑锋金");
            put("甲戌", "山头火");
            put("乙亥", "山头火");
            put("丙子", "涧下水");
            put("丁丑", "涧下水");
            put("戊寅", "城头土");
            put("己卯", "城头土");
            put("庚辰", "白蜡金");
            put("辛巳", "白蜡金");
            put("壬午", "杨柳木");
            put("癸未", "杨柳木");
            put("甲申", "泉中水");
            put("乙酉", "泉中水");
            put("丙戌", "屋上土");
            put("丁亥", "屋上土");
            put("戊子", "霹雳火");
            put("己丑", "霹雳火");
            put("庚寅", "松柏木");
            put("辛卯", "松柏木");
            put("壬辰", "长流水");
            put("癸巳", "长流水");
            put("甲午", "沙中金");
            put("乙未", "沙中金");
            put("丙申", "山下火");
            put("丁酉", "山下火");
            put("戊戌", "平地木");
            put("己亥", "平地木");
            put("庚子", "壁上土");
            put("辛丑", "壁上土");
            put("壬寅", "金箔金");
            put("癸卯", "金箔金");
            put("甲辰", "覆灯火");
            put("乙巳", "覆灯火");
            put("丙午", "天河水");
            put("丁未", "天河水");
            put("戊申", "大驿土");
            put("己酉", "大驿土");
            put("庚戌", "钗钏金");
            put("辛亥", "钗钏金");
            put("壬子", "桑柘木");
            put("癸丑", "桑柘木");
            put("甲寅", "大溪水");
            put("乙卯", "大溪水");
            put("丙辰", "沙中土");
            put("丁巳", "沙中土");
            put("戊午", "天上火");
            put("己未", "天上火");
            put("庚申", "石榴木");
            put("辛酉", "石榴木");
            put("壬戌", "大海水");
            put("癸亥", "大海水");
        }
    });

    /**
     * 十二长生
     */
    public static final String[] SHI_ER_ZHANG_SHENG = {"长生", "沐浴", "冠带", "临官", "帝旺", "衰", "病", "死", "墓", "绝", "胎", "养"};

    /**
     * 十二长生-地支对应表
     * -------------------------
     * 此表只记录干支的长生对应的起始地支
     * 其他地支依次排列即可
     * 其中，阳干顺排，阴干逆排
     */
    public static final Map<String, String> SHI_ER_ZHANG_SHENG_ZHI_RULE = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("甲", "亥");
            put("丙", "寅");
            put("戊", "寅");
            put("庚", "巳");
            put("壬", "申");
            put("乙", "午");
            put("丁", "酉");
            put("己", "酉");
            put("辛", "子");
            put("癸", "卯");
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

    /**
     * 根据地支获取对应的藏干
     * @param zhi 地支
     * @return
     */
    public static Set<String> getCangGanByZhi(String zhi) {
        Set<String> ret = new LinkedHashSet<>();
        String str = CANG_GAN_RULE.get(zhi);
        if (str != null && !str.isEmpty()) {
            ret.addAll(Arrays.asList(str.split("")));
        }
        return ret;
    }

    /**
     * 根据干支获取对应的纳音
     *
     * @param ganZhi 干支
     * @return
     */
    public static String getNaYinByGanZhi(String ganZhi) {
        return NA_YIN_RULE.get(ganZhi);
    }

    /**
     * 获取纳音对应的五行
     * 纳音的五行，就是纳音的最后一个字，就是其五行
     *
     * @param naYin 纳音
     * @return
     */
    public static String getWuXingByNaYin(String naYin) {
        return naYin.substring(naYin.length() - 1);
    }

    /**
     * 获取纳音对应的阴阳属性
     * 纳音的阴阳，单纯的某个纳音，难以区分阴阳
     * 因为一个纳音，一般都有两个干支对应，两个干支分别是阴阳的干支
     * 因此，某个纳音的干支既有阴也有阳，或者说没有阴阳区分
     * 但是，因为纳音是由干支推出来的，纳音的阴阳就可以使用推断的干支的阴阳来表示
     * 因为干支都是奇数对奇数，偶数对偶数，干支总数都是偶数，因此干支的阴阳属性必定是一样的
     * 也就是：孤阴不生，独阳不长
     * 从数学排列的角度来说，因为配对规则限制，必定阴阳属性是相同的
     * 因此，只需要看干支的阴阳属性即可
     *
     * @param ganZhi ganZhi
     * @return
     */
    public static String getYinYangByNaYinGanZhi(String ganZhi) {
        String gan = ganZhi.substring(0, 1);
        return getYinYangByGan(gan);
    }

    public static int getStringOffset(String target, String[] arr) {
        int offset = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(target)) {
                offset = i;
                break;
            }
        }
        return offset;
    }

    /**
     * 根据天干获取十二长生的地支
     *
     * @param gan             天干
     * @param shiErZhangSheng 十二长生
     * @return
     */
    public static String getShiErZhangShengZhiByGan(String gan, String shiErZhangSheng) {
        String zhi = SHI_ER_ZHANG_SHENG_ZHI_RULE.get(gan);
        String yinYang = getYinYangByGan(gan);
        int zhiOffset = getStringOffset(zhi, ZHI);
        int shiErOffset = getStringOffset(shiErZhangSheng, SHI_ER_ZHANG_SHENG);
        if ("阳".equals(yinYang)) {
            return ZHI[(zhiOffset + shiErOffset) % 12];
        }
        return ZHI[(zhiOffset - shiErOffset + 12) % 12];
    }

    /**
     * 获取一个天干对应的十二个十二长生对应的地支
     * 返回的键为十二长生，支为地支
     *
     * @param gan 天干
     * @return
     */
    public static Map<String, String> getShiErZhangShengToZhiByGan(String gan) {
        Map<String, String> ret = new LinkedHashMap<>();
        for (String item : SHI_ER_ZHANG_SHENG) {
            ret.put(item, getShiErZhangShengZhiByGan(gan, item));
        }
        return ret;
    }

    /**
     * 获取命宫的干支
     *
     * @param date 生辰八字
     * @return
     */
    public static String getMingGongGanZhi(GanZhiDate date) {
        String monthZhi = date.getMonth().substring(1);
        String hourZhi = date.getHour().substring(1);
        int monthZhiOffset = getStringOffset(monthZhi, ZHI);
        int hourZhiOffset = getStringOffset(hourZhi, ZHI);
        int sumNum = (monthZhiOffset + 1) + (hourZhiOffset + 1);
        if (sumNum == 14) {
            return date.getHour();
        } else if (sumNum < 14) {
            int diff = 14 - sumNum;
            String hourGan = date.getHour().substring(0, 1);
            int hourGanOffset = getStringOffset(hourGan, GAN);
            return GAN[(hourGanOffset + diff) % 12] + ZHI[(hourZhiOffset + diff) % 12];
        } else {
            int diff = 26 - sumNum;
            String hourGan = date.getHour().substring(0, 1);
            int hourGanOffset = getStringOffset(hourGan, GAN);
            return GAN[(hourGanOffset + diff) % 12] + ZHI[(hourZhiOffset + diff) % 12];
        }
    }

}
