package i2f.tools.yi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/8 17:43
 * @desc
 */
@Data
@NoArgsConstructor
public class GanZhiDate {

    private static final String BASE_DATE = "1970-02-06 00:00:00";
    private static final String[] BASE_GAN_ZHI_DATE = {
            "庚戌", "戊寅", "丁巳"
    };
    public static final int[][] BASE_GAN_ZHI_OFFSET = {
            {6, 10},
            {4, 2},
            {3, 5}
    };
    public static final DateTimeFormatter BASE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final LocalDateTime BASE_DATETIME = LocalDateTime.parse(BASE_DATE, BASE_FORMATTER);

    /**
     * 24节气歌
     * 春雨惊春清谷天，
     * 夏满芒夏暑相连。
     * 秋处露秋寒霜降，
     * 冬雪雪冬小大寒。
     */

    /**
     * 寿星公式，世纪常数，20世纪，1901-2000
     */
    public static final Map<String, Double> CENTURY_20_JIE_QI_C_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, Double>() {
        {
            put("小寒", 6.11);
            put("大寒", 20.84);
            put("立春", 4.6295);
            put("雨水", 19.459);
            put("惊蛰", 6.3826);
            put("春分", 21.415);
            put("清明", 5.59);
            put("谷雨", 20.888);
            put("立夏", 6.318);
            put("小满", 21.86);
            put("芒种", 6.50);
            put("夏至", 22.20);
            put("小暑", 7.928);
            put("大暑", 24.00);
            put("立秋", 8.44);
            put("处暑", 24.02);
            put("白露", 8.60);
            put("秋分", 23.93);
            put("寒露", 9.14);
            put("霜降", 24.43);
            put("立冬", 8.218);
            put("小雪", 23.08);
            put("大雪", 7.90);
            put("冬至", 22.60);
        }
    });

    /**
     * 寿星公式，世纪常数，21世纪，2001-2100
     */
    public static final Map<String, Double> CENTURY_21_JIE_QI_C_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, Double>() {
        {
            put("小寒", 5.4055); // 节，表节点变化，是干支纪年法的界定月边界，交节也就是换月份
            put("大寒", 20.12); // 气，表气候变化
            put("立春", 3.87);
            put("雨水", 18.73);
            put("惊蛰", 5.63);
            put("春分", 20.646);
            put("清明", 4.81);
            put("谷雨", 20.10);
            put("立夏", 5.52);
            put("小满", 21.04);
            put("芒种", 5.678);
            put("夏至", 21.37);
            put("小暑", 7.108);
            put("大暑", 22.83);
            put("立秋", 7.50);
            put("处暑", 23.13);
            put("白露", 7.646);
            put("秋分", 23.042);
            put("寒露", 8.318);
            put("霜降", 23.438);
            put("立冬", 7.438);
            put("小雪", 22.36);
            put("大雪", 7.18);
            put("冬至", 21.94); // 公历12月，子月，大雪到小寒为子月，冬至在中间，子月中气
        }
    });

    /**
     * 节气对应公历月份
     */
    public static final Map<String, Integer> JIE_QI_MONTH_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, Integer>() {
        {
            put("小寒", 1);
            put("大寒", 1);
            put("立春", 2);
            put("雨水", 2);
            put("惊蛰", 3);
            put("春分", 3);
            put("清明", 4);
            put("谷雨", 4);
            put("立夏", 5);
            put("小满", 5);
            put("芒种", 6);
            put("夏至", 6);
            put("小暑", 7);
            put("大暑", 7);
            put("立秋", 8);
            put("处暑", 8);
            put("白露", 9);
            put("秋分", 9);
            put("寒露", 10);
            put("霜降", 10);
            put("立冬", 11);
            put("小雪", 11);
            put("大雪", 12);
            put("冬至", 12);
        }
    });

    /**
     * 节气对应地支关系
     */
    public static final Map<String, String> JIE_QI_ZHI_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, String>() {
        {
            put("小寒", "丑");
            put("大寒", "丑");
            put("立春", "寅");
            put("雨水", "寅");
            put("惊蛰", "卯");
            put("春分", "卯");
            put("清明", "辰");
            put("谷雨", "辰");
            put("立夏", "巳");
            put("小满", "巳");
            put("芒种", "午");
            put("夏至", "午");
            put("小暑", "未");
            put("大暑", "未");
            put("立秋", "申");
            put("处暑", "申");
            put("白露", "酉");
            put("秋分", "酉");
            put("寒露", "戌");
            put("霜降", "戌");
            put("立冬", "亥");
            put("小雪", "亥");
            put("大雪", "子");// [大雪,小寒)，为子，左闭右开区间
            put("冬至", "子");
        }
    });


    /**
     * 获取世纪的寿星公式常量C映射
     *
     * @param century 世纪，20世纪就是20世纪
     * @return
     */
    public static Map<String, Double> getCenturyJieQiMap(int century) {
        if (century == 20) {
            return new LinkedHashMap<>(CENTURY_20_JIE_QI_C_MAP);
        } else if (century == 21) {
            return new LinkedHashMap<>(CENTURY_21_JIE_QI_C_MAP);
        }
        throw new IllegalArgumentException("un-support get " + century + " century jie-qi map");
    }

    /**
     * 根据年份获取寿星公式常量C映射
     *
     * @param year
     * @return
     */
    public static Map<String, Double> getCenturyJieQiMapByYear(int year) {
        if (year >= 1901 && year <= 2000) {
            return getCenturyJieQiMap(20);
        } else if (year >= 2001 && year <= 2100) {
            return getCenturyJieQiMap(21);
        }
        throw new IllegalArgumentException("un-support get century jie-qi map by year: " + year);
    }

    /**
     * 是否是闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 获取某年某个节气的日期
     *
     * @param year
     * @param jieQi
     * @return
     */
    public static LocalDate getJieQiDate(int year, String jieQi) {
        Map<String, Double> jieQiMap = getCenturyJieQiMapByYear(year);
        int Y = year % 100;
        double C = jieQiMap.get(jieQi);
        double D = 0.2422;

        int L = Y / 4;
        if (isLeapYear(year)) {
            L = (Y - 1) / 4;
        }
        int day = (int) ((Y * D + C) - L);

        Integer month = JIE_QI_MONTH_MAP.get(jieQi);

        return LocalDate.of(year, month, day);
    }

    /**
     * 获取某年所有节气的日期
     *
     * @param year
     * @return
     */
    public static Map<String, LocalDate> getJieQiDateMap(int year) {
        Map<String, LocalDate> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : JIE_QI_MONTH_MAP.entrySet()) {
            String jieQi = entry.getKey();
            Integer month = entry.getValue();
            LocalDate date = getJieQiDate(year, jieQi);
            ret.put(jieQi, date);
        }
        return ret;
    }

    /**
     * 获取日期所属的节气
     *
     * @param date
     * @return
     */
    public static String getJieQiOfDate(LocalDate date) {
        String jieQi = null;
        Map<String, LocalDate> jieQiDateMap = getJieQiDateMap(date.getYear());
        Map<String, LocalDate> basicMap = jieQiDateMap;
        Map.Entry<String, LocalDate> last = null;
        for (Map.Entry<String, LocalDate> entry : jieQiDateMap.entrySet()) {
            if (last == null) {
                // do nothing
            } else {
                if (date.compareTo(last.getValue()) >= 0 && date.compareTo(entry.getValue()) < 0) {
                    jieQi = last.getKey();
                    break;
                }
            }

            last = entry;
        }
        if (jieQi == null) {
            // 查找前一年的区间
            Map<String, LocalDate> preMap = getJieQiDateMap(date.getYear() - 1);
            jieQiDateMap = new LinkedHashMap<>();
            int count = 0;
            for (Map.Entry<String, LocalDate> entry : preMap.entrySet()) {
                count++;
                if (count > 12) {
                    jieQiDateMap.put(entry.getKey(), entry.getValue());
                }
            }
            count = 0;
            for (Map.Entry<String, LocalDate> entry : basicMap.entrySet()) {
                count++;
                jieQiDateMap.put(entry.getKey(), entry.getValue());
                if (count >= 12) {
                    break;
                }
            }
            last = null;
            for (Map.Entry<String, LocalDate> entry : jieQiDateMap.entrySet()) {
                if (last == null) {
                    // do nothing
                } else {
                    if (date.compareTo(last.getValue()) >= 0 && date.compareTo(entry.getValue()) < 0) {
                        jieQi = last.getKey();
                        break;
                    }
                }

                last = entry;
            }
        }
        if (jieQi == null) {
            // 查找后一年的区间
            Map<String, LocalDate> nextMap = getJieQiDateMap(date.getYear() + 1);
            jieQiDateMap = new LinkedHashMap<>();
            int count = 0;
            for (Map.Entry<String, LocalDate> entry : basicMap.entrySet()) {
                count++;
                if (count > 12) {
                    jieQiDateMap.put(entry.getKey(), entry.getValue());
                }
            }
            count = 0;
            for (Map.Entry<String, LocalDate> entry : nextMap.entrySet()) {
                count++;
                jieQiDateMap.put(entry.getKey(), entry.getValue());
                if (count >= 12) {
                    break;
                }
            }
            last = null;
            for (Map.Entry<String, LocalDate> entry : jieQiDateMap.entrySet()) {
                if (last == null) {
                    // do nothing
                } else {
                    if (date.compareTo(last.getValue()) >= 0 && date.compareTo(entry.getValue()) < 0) {
                        jieQi = last.getKey();
                        break;
                    }
                }

                last = entry;
            }
        }
        return jieQi;
    }

    /**
     * 获取某个日期的所在的地支
     *
     * @param date
     * @return
     */
    public static String getZhiOfMonth(LocalDate date) {
        String jieQi = getJieQiOfDate(date);

        String zhi = null;
        for (Map.Entry<String, String> entry : JIE_QI_ZHI_MAP.entrySet()) {
            if (entry.getKey().equals(jieQi)) {
                zhi = entry.getValue();
                break;
            }
        }

        return zhi;
    }

    protected String year;
    protected String month;
    protected String day;
    protected String hour;

    public static GanZhiDate of(String year, String month, String day, String hour) {
        GanZhiDate ret = new GanZhiDate();
        ret.setYear(year);
        ret.setMonth(month);
        ret.setDay(day);
        ret.setHour(hour);
        return ret;
    }

    public static GanZhiDate of(LocalDate date) {
        return of(LocalDateTime.of(date, LocalTime.of(0, 0, 0)));
    }

    public static GanZhiDate of(LocalDateTime datetime) {
        GanZhiDate ret = new GanZhiDate();

        long diffDays = BASE_DATETIME.until(datetime, ChronoUnit.DAYS);
        int dayOffset = (int) (((diffDays % 60) + 60) % 60);


        int dayGanOffset = dayOffset % 10;
        int dayZhiOffset = dayOffset % 12;

        dayGanOffset = (dayGanOffset + BASE_GAN_ZHI_OFFSET[2][0]) % 10;
        dayZhiOffset = (dayZhiOffset + BASE_GAN_ZHI_OFFSET[2][1]) % 12;
        ret.day = Yi.GAN[dayGanOffset] + "" + Yi.ZHI[dayZhiOffset];

        int hourGanOffset = 0;
        String dayGan = Yi.GAN[dayGanOffset];

        for (Map.Entry<String, String> entry : Yi.WU_SHU_DUN_HOUR_OF_DAY_RULE.entrySet()) {
            if (dayGan.equals(entry.getKey())) {
                String hourGan = entry.getValue();
                for (int i = 0; i < Yi.GAN.length; i++) {
                    if (hourGan.equals(Yi.GAN[i])) {
                        hourGanOffset = i;
                        break;
                    }
                }
                break;
            }
        }

        int dateHour = datetime.getHour(); // 0-23
        hourGanOffset = (hourGanOffset + dateHour / 2) % 10;
        int hourZhiOffset = (0 + dateHour / 2) % 12;
        ret.hour = Yi.GAN[hourGanOffset] + "" + Yi.ZHI[hourZhiOffset];


        String monthZhi = getZhiOfMonth(datetime.toLocalDate());

        int monthZhiOffset = 0;
        for (int i = 0; i < Yi.ZHI.length; i++) {
            if (monthZhi.equals(Yi.ZHI[i])) {
                monthZhiOffset = i;
                break;
            }
        }

        // 以年中计算相差年份，这样这年大体就是这样了，后面只需要处理还没立春的情况，修正年份即可
        LocalDateTime yearTime = datetime.withDayOfMonth(1).withMonth(6);
        long diffYear = BASE_DATETIME.until(yearTime, ChronoUnit.YEARS);
        int yearOffset = (int) (((diffYear % 60) + 60) % 60);

        int yearGanOffset = yearOffset % 10;
        int yearZhiOffset = yearOffset % 12;

        yearGanOffset = (yearGanOffset + BASE_GAN_ZHI_OFFSET[0][0]) % 10;
        yearZhiOffset = (yearZhiOffset + BASE_GAN_ZHI_OFFSET[0][1]) % 12;

        // 月份修正年干支，前半年如果出现子丑月份，那必定属于上一年
        if (Arrays.asList("丑", "子").contains(monthZhi) && datetime.getMonthValue() < 6) {
            yearGanOffset = (yearGanOffset - 1 + 10) % 10;
            yearZhiOffset = (yearZhiOffset - 1 + 12) % 12;
        }

        ret.year = Yi.GAN[yearGanOffset] + "" + Yi.ZHI[yearZhiOffset];

        int monthGanOffset = 0;
        String yearGan = Yi.GAN[yearGanOffset];

        for (Map.Entry<String, String> entry : Yi.WU_HU_DUN_MONTH_OF_YEAR_RULE.entrySet()) {
            if (yearGan.equals(entry.getKey())) {
                String monthGan = entry.getValue();
                for (int i = 0; i < Yi.GAN.length; i++) {
                    if (monthGan.equals(Yi.GAN[i])) {
                        monthGanOffset = i;
                        break;
                    }
                }
                break;
            }
        }

        monthZhiOffset = (((monthZhiOffset - 2) % 12) + 12) % 12;
        monthGanOffset = (monthGanOffset + monthZhiOffset) % 10;

        ret.month = Yi.GAN[monthGanOffset] + monthZhi;


        return ret;
    }
}
