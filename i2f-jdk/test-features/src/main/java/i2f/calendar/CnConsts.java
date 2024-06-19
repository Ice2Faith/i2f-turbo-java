package i2f.calendar;

/**
 * @author Ice2Faith
 * @date 2024/6/19 10:19
 * @desc
 */
public class CnConsts {
    public static final String NUM_MAPPING = "零一二三四五六七八九十百千万亿";

    public static final String NUM_TRA_MAPPING = "零壹贰叁肆伍陆柒捌玖拾佰仟萬億";

    public static final String SHI_CHEN_MAPPING = "子丑丑寅寅卯卯辰辰巳巳午午未未申申酉酉戌戌亥亥子";

    public static final String WU_XING_MAPPING = "金木水火土";

    public static final String WU_XING_XIANG_SHENG_QUEUE = "木火土金水";

    public static final String WU_XING_XIANG_KE_QUEUE = "木土水火金";

    public static final String WU_YIN_MAPPING = "商角羽徵宫";

    public static final String WU_ZANG_MAPPING = "肺肝肾心脾";

    public static final String WU_SHENG_MAPPING = "哭呼呻笑歌";

    public static final String[] WU_ZANG_BU_TIAO_MAPPING = {"咳嗽", "多言语", "打哈欠、打喷嚏", "伤感", "反酸"};

    public static final String[] WU_ZANG_PA_MAPPING = {"怕烟", "怕堵", "怕干", "怕累", "忌冷"};

    public static final String[] WU_HUA_MAPPING = {"皮、毛", "爪、筋", "发、骨", "脸、舌", "口、唇"};

    public static final String WU_YE_MAPPING = "涕泪唾汗涎";

    public static final String WU_GUAN_MAPPING = "鼻目耳舌唇";

    public static final String WU_TI_MAPPING = "皮筋骨脉肉";

    public static final String WU_BING_MAPPING = "燥风寒暑湿";

    public static final String WU_WEI_MAPPING = "辛酸咸苦甘";

    public static final String WU_KONG_MAPPING = "悲怒恐喜思";

    public static final String WU_SHEN_MAPPING = "魄魂志神情";

    public static final String WU_ZHU_MAPPING = "收生藏長化";

    public static final String[] WU_JI_MAPPING = {"秋", "春", "冬", "夏", "长夏"};

    public static final String[] WU_BIAO_MAPPING = {"大肠", "胆", "膀胱", "小肠", "胃"};

    public static final String WU_SE_MAPPING = "白青黑赤黄";

    public static final String WU_FANG_MAPPING = "西东北南中";

    public static final String TIAN_GAN_MAPPING = "甲乙丙丁戊己庚辛壬癸";

    public static final String DI_ZHI_MAPPING = "子丑寅卯辰巳午未申酉戌亥";

    public static final String SHU_XIANG_MAPPING = "鼠牛虎兔龙蛇马羊猴鸡狗猪";

    public static final String BA_GUA_MAPPING = "乾巽坎艮坤震离兑";

    public static final String BA_GUA_SHI_WU_MAPPING = "天风水山地雷火泽";

    public static final String[] BA_GUA_FU_HAO_MAPPING = {"111", "011", "010", "001", "000", "100", "101", "110"};

    public static final String BA_GUA_FU_MAPPING = "☰☴☵☶☷☳☳☱";

    public static final String[] BA_GUA_YU_YI_MAPPING = {"刚健、创造力、进取心", "温和、轻盈、灵动", "沉静、深度、阴险", "静止、稳定、固执", "柔顺、宽容、耐心", "激动、动力、冲击力", "热情、光明、行动力", "和谐、亲和力"};

    public static final String[] BA_GUA_XIANG_ZHENG_MAPPING = {"积极、有利", "和谐、变通、顺利", "沉静、深度、变化", "稳定、坚固、守旧", "稳定、沉静、包容", "充满冲击、激动", "充满活力、热情", "和谐、亲和、共融"};

    public static void main(String[] args) {
        String str = printWuXingGuanXi();
        System.out.println(str);

        str = printGanZhiGuanXi();
        System.out.println(str);

        str = printBaGuaGuanXi();
        System.out.println(str);

        str = printShiChenGuanXi();
        System.out.println(str);
    }

    public static String printWuXingGuanXi() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t\t五行\n");
        builder.append("\t中国古代山医命相卜，都有五行参与其中\n");
        builder.append("\t在使用时，也是根据对应事物所属的五行，根据五行的相生相克来决定事物的相生相克\n");
        builder.append("\t实际上这是一种以结果为导向，推断原因的总结性知识，实际的支撑原理或许已经淹没在历史长河中\n");

        builder.append("--------------------\n");
        builder.append("五行相生关系：").append("\n");
        builder.append("\t");
        for (int i = 0; i < WU_XING_XIANG_SHENG_QUEUE.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(WU_XING_XIANG_SHENG_QUEUE.charAt(i)).append("生").append(WU_XING_XIANG_SHENG_QUEUE.charAt((i + 1) % WU_XING_XIANG_SHENG_QUEUE.length()));
        }
        builder.append("\n");

        builder.append("五行相克关系：").append("\n");
        builder.append("\t");
        for (int i = 0; i < WU_XING_XIANG_KE_QUEUE.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(WU_XING_XIANG_KE_QUEUE.charAt(i)).append("克").append(WU_XING_XIANG_KE_QUEUE.charAt((i + 1) % WU_XING_XIANG_SHENG_QUEUE.length()));
        }
        builder.append("\n");

        builder.append("--------------------\n");

        for (int i = 0; i < WU_XING_XIANG_SHENG_QUEUE.length(); i++) {
            char wuXing = WU_XING_XIANG_SHENG_QUEUE.charAt(i);
            int idx = WU_XING_MAPPING.indexOf(wuXing);
            char wuXingXiangSheng = WU_XING_XIANG_SHENG_QUEUE.charAt((WU_XING_XIANG_SHENG_QUEUE.indexOf(wuXing) + 1) % WU_XING_XIANG_SHENG_QUEUE.length());
            int idxSheng = WU_XING_MAPPING.indexOf(wuXingXiangSheng);
            char wuXingXiangKe = WU_XING_XIANG_KE_QUEUE.charAt((WU_XING_XIANG_KE_QUEUE.indexOf(wuXing) + 1) % WU_XING_XIANG_KE_QUEUE.length());
            int idxKe = WU_XING_MAPPING.indexOf(wuXingXiangKe);

            builder.append(wuXing).append(":").append("\n");
            builder.append("\t").append("相生：").append(wuXingXiangSheng).append("\n");
            builder.append("\t").append("相克：").append(wuXingXiangKe).append("\n");
            builder.append("\t").append("五音：").append(WU_YIN_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五脏：").append(WU_ZANG_MAPPING.charAt(idx)).append("\n");
            builder.append("\t\t").append("开窍\t：").append(WU_GUAN_MAPPING.charAt(idx)).append("\n");
            builder.append("\t\t").append("华在\t：").append(WU_HUA_MAPPING[idx]).append("\n");
            builder.append("\t\t").append("不调\t：").append(WU_ZANG_BU_TIAO_MAPPING[idx]).append("\n");
            builder.append("\t\t").append("怕\t：").append(WU_ZANG_PA_MAPPING[idx]).append("\n");
            builder.append("\t\t").append("表\t：").append(WU_BIAO_MAPPING[idx]).append("\n");
            builder.append("\t\t").append("声\t：").append(WU_SHENG_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五声：").append(WU_SHENG_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五华：").append(WU_HUA_MAPPING[idx]).append("\n");
            builder.append("\t").append("五液：").append(WU_YE_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五官：").append(WU_GUAN_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五体：").append(WU_TI_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五病：").append(WU_BING_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五味：").append(WU_WEI_MAPPING.charAt(idx)).append("\n");
            builder.append("\t\t").append("喜则伤\t：").append(WU_ZANG_MAPPING.charAt(idxKe)).append("\n");
            builder.append("\t").append("五恐：").append(WU_KONG_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五神：").append(WU_SHEN_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五主：").append(WU_ZHU_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五季：").append(WU_JI_MAPPING[idx]).append("\n");
            builder.append("\t\t").append("养\t：").append(WU_ZANG_MAPPING.charAt(idx)).append("\n");
            builder.append("\t\t").append("食物\t：").append(WU_SE_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五表：").append(WU_BIAO_MAPPING[idx]).append("\n");
            builder.append("\t\t").append("里\t：").append(WU_ZANG_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("五色：").append(WU_SE_MAPPING.charAt(i)).append("\n");
            builder.append("\t").append("五方：").append(WU_FANG_MAPPING.charAt(i)).append("\n");
            builder.append("\t\t")
                    .append("关系：")
                    .append(WU_FANG_MAPPING.charAt(idx)).append("方生").append(WU_BING_MAPPING.charAt(idx))
                    .append("，").append(WU_BING_MAPPING.charAt(idx)).append("生").append(WU_XING_MAPPING.charAt(idx))
                    .append("，").append(WU_XING_MAPPING.charAt(idx)).append("生").append(WU_WEI_MAPPING.charAt(idx))
                    .append("，").append(WU_WEI_MAPPING.charAt(idx)).append("生").append(WU_ZANG_MAPPING.charAt(idx))
                    .append("，").append(WU_ZANG_MAPPING.charAt(idx)).append("生").append(WU_TI_MAPPING.charAt(idx))
                    .append("，").append(WU_TI_MAPPING.charAt(idx)).append("生").append(WU_ZANG_MAPPING.charAt(idxSheng))
                    .append("，").append(WU_ZANG_MAPPING.charAt(idx)).append("主").append(WU_GUAN_MAPPING.charAt(idx))
                    .append("\n");


        }
        return builder.toString();
    }

    public static String printGanZhiGuanXi() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t\t干支\n");
        builder.append("\t实际上这是一种计数方式，常用于时日计时中\n");


        builder.append("--------------------\n");
        builder.append("天干：").append("\n");
        builder.append("\t");
        for (int i = 0; i < TIAN_GAN_MAPPING.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(TIAN_GAN_MAPPING.charAt(i));
        }
        builder.append("\n");

        builder.append("地支：").append("\n");
        builder.append("\t");
        for (int i = 0; i < DI_ZHI_MAPPING.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(DI_ZHI_MAPPING.charAt(i));
        }
        builder.append("\n");
        builder.append("\t");
        for (int i = 0; i < DI_ZHI_MAPPING.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(SHU_XIANG_MAPPING.charAt(i));
        }
        builder.append("\n");

        return builder.toString();

    }

    public static String printBaGuaGuanXi() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t\t八卦\n");
        builder.append("\t实际上是一种总结性的归纳性质的知识，结果导向总结原因的一种知识\n");


        builder.append("--------------------\n");
        builder.append("八卦：").append("\n");
        builder.append("\t");
        for (int i = 0; i < BA_GUA_MAPPING.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(BA_GUA_MAPPING.charAt(i));
        }
        builder.append("\n");

        builder.append("\t");
        for (int i = 0; i < BA_GUA_MAPPING.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(BA_GUA_SHI_WU_MAPPING.charAt(i));
        }
        builder.append("\n");

        builder.append("\t");
        for (int i = 0; i < BA_GUA_MAPPING.length(); i++) {
            if (i > 0) {
                builder.append("，");
            }
            builder.append(BA_GUA_FU_MAPPING.charAt(i));
        }
        builder.append("\n");


        for (int i = 0; i < BA_GUA_MAPPING.length(); i++) {
            char gua = BA_GUA_MAPPING.charAt(i);
            int idx = BA_GUA_MAPPING.indexOf(gua);

            builder.append(gua).append(":").append("\n");
            builder.append("\t").append("代表：").append(BA_GUA_SHI_WU_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("符号：").append(BA_GUA_FU_MAPPING.charAt(idx)).append("\n");
            builder.append("\t").append("寓意：").append(BA_GUA_YU_YI_MAPPING[idx]).append("\n");
            builder.append("\t").append("象征：").append(BA_GUA_XIANG_ZHENG_MAPPING[idx]).append("\n");

        }

        return builder.toString();

    }

    public static String printShiChenGuanXi() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t\t时辰\n");
        builder.append("\t是一种使用12地支表达12个时辰的表达形式，将一天划分为12部分\n");
        builder.append("\t每个部分为2小时，其中23-1点为子时，11-13点为午时类推\n");


        builder.append("--------------------\n");
        for (int i = 0; i < 24; i++) {
            builder.append("\t").append(i).append("\t时:").append(SHI_CHEN_MAPPING.charAt(i)).append("\n");
        }
        return builder.toString();
    }
}
