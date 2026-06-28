package i2f.tools.yi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2026/6/9 15:41
 * @desc
 */
@Data
@NoArgsConstructor
public class BaZi implements Runnable {
    private GanZhiDate baZi;
    private String dayGan;

    private Map<String, String> shiErZhangShengToZhiMap = new LinkedHashMap<>();
    private Map<String, String> zhiToShiErZhangShengMap = new LinkedHashMap<>();

    private Map<String, AtomicInteger> wuXingCountMap = new LinkedHashMap<>();
    private Map<String, AtomicInteger> shiShenCountMap = new LinkedHashMap<>();

    private Map<String, AtomicInteger> cangGanWuXingCountMap = new LinkedHashMap<>();
    private Map<String, AtomicInteger> cangGanShiShenCountMap = new LinkedHashMap<>();

    public static BaZi of(GanZhiDate date) {
        BaZi baZi = new BaZi();
        baZi.setBaZi(date);
        baZi.run();
        return baZi;
    }

    @Override
    public void run() {
        String day = baZi.getDay();
        dayGan = day.substring(0, 1);

        shiErZhangShengToZhiMap = Yi.getShiErZhangShengToZhiByGan(dayGan);
        zhiToShiErZhangShengMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : shiErZhangShengToZhiMap.entrySet()) {
            zhiToShiErZhangShengMap.put(entry.getValue(), entry.getKey());
        }

        wuXingCountMap.clear();
        shiShenCountMap.clear();
        cangGanWuXingCountMap.clear();
        cangGanShiShenCountMap.clear();
    }

    public String getSimpleInfo(Boolean isMale,GanZhiDate liuNian){
        StringBuilder builder = new StringBuilder();
        getBasicSimpleInfo(isMale, builder);
        if(liuNian==null){
            liuNian=GanZhiDate.of(LocalDateTime.now());
        }
        getLiuNianSimpleInfo(liuNian, builder);
        return builder.toString();
    }

    public StringBuilder getBasicSimpleInfo(Boolean isMale, StringBuilder builder) {
        fillGanZhiDateSimpleInfo("八字", baZi, builder);

        if (isMale != null) {
            String dayYinYang = Yi.getYinYangByGan(dayGan);
            if (isMale) {
                builder.append(dayYinYang).append("男").append("\n");
            } else {
                builder.append(dayYinYang).append("女").append("\n");
            }
        }

        builder.append("\n");
        String day = baZi.getDay();
        fillGanZhiHeadSimpleInfo("日柱", day, builder);
        fillDayGanPartSimpleInfo("日元", builder, true);

        String dayZhi = day.substring(1);
        fillZhiPartSimpleInfo("日柱", dayZhi, builder, true, true);


        builder.append("\n");
        String month = baZi.getMonth();
        fillGanZhiSimpleInfo("月柱", month, builder, true, true);


        builder.append("\n");
        String year = baZi.getYear();
        fillGanZhiSimpleInfo("年柱", year, builder, true, true);


        builder.append("\n");
        String hour = baZi.getHour();
        fillGanZhiSimpleInfo("时柱", hour, builder, true, true);

        builder.append("\n");
        String mingGong = Yi.getMingGongGanZhi(baZi);
        fillGanZhiSimpleInfo("命宫", mingGong, builder, false, false);

        builder.append("\n");
        String shenGong = Yi.getShenGongGanZhi(baZi);
        fillGanZhiSimpleInfo("身宫", shenGong, builder, false, false);

        builder.append("\n");
        String taiYuan = Yi.getTaiYuanGanZhi(baZi);
        fillGanZhiSimpleInfo("胎元", taiYuan, builder, false, false);

        // /////////////////////////////////////////////////////////////////////////////////
        List<Map.Entry<String, Integer>> wuXingList = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> entry : wuXingCountMap.entrySet()) {
            wuXingList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().get()));
        }

        wuXingList.sort((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue()));

        builder.append("\n");
        builder.append("五行个数分布：");
        for (Map.Entry<String, Integer> entry : wuXingList) {
            builder.append(" ").append(entry.getKey()).append(": ").append(entry.getValue());
        }
        builder.append("\n");

        List<String> missingWuXingList = new ArrayList<>();
        for (String item : Yi.WU_XING) {
            if (!wuXingCountMap.containsKey(item)) {
                missingWuXingList.add(item);
            }
        }

        builder.append("五行缺失情况：").append(missingWuXingList.isEmpty() ? "无" : String.join("、", missingWuXingList)).append("\n");

        // /////////////////////////////////////////////////////////////////////////////////
        List<Map.Entry<String, Integer>> cangGanWuXingList = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> entry : cangGanWuXingCountMap.entrySet()) {
            cangGanWuXingList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().get()));
        }

        cangGanWuXingList.sort((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue()));

        builder.append("\t仅藏干五行个数分布：");
        for (Map.Entry<String, Integer> entry : cangGanWuXingList) {
            builder.append(" ").append(entry.getKey()).append(": ").append(entry.getValue());
        }
        builder.append("\n");

        // /////////////////////////////////////////////////////////////////////////////////
        List<Map.Entry<String, Integer>> shiShenList = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> entry : shiShenCountMap.entrySet()) {
            shiShenList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().get()));
        }

        shiShenList.sort((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue()));

        builder.append("\n");
        builder.append("十神个数分布：");
        for (Map.Entry<String, Integer> entry : shiShenList) {
            builder.append(" ").append(entry.getKey()).append(": ").append(entry.getValue());
        }
        builder.append("\n");

        List<String> missingShiShenList = new ArrayList<>();
        for (String item : Yi.SHI_SHEN) {
            if (!shiShenCountMap.containsKey(item)) {
                missingShiShenList.add(item);
            }
        }

        builder.append("十神缺失情况：").append(missingShiShenList.isEmpty() ? "无" : String.join("、", missingShiShenList)).append("\n");

        // /////////////////////////////////////////////////////////////////////////////////
        List<Map.Entry<String, Integer>> cangGanShiShenList = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> entry : cangGanShiShenCountMap.entrySet()) {
            cangGanShiShenList.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().get()));
        }

        cangGanShiShenList.sort((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue()));

        builder.append("\t仅藏干十神个数分布：");
        for (Map.Entry<String, Integer> entry : cangGanShiShenList) {
            builder.append(" ").append(entry.getKey()).append(": ").append(entry.getValue());
        }
        builder.append("\n");

        return builder;
    }

    public StringBuilder getLiuNianSimpleInfo(GanZhiDate liuNian, StringBuilder builder) {
        String year = liuNian.getYear();
        if (year != null && !year.isEmpty()) {
            builder.append("\n");
            fillGanZhiSimpleInfo("流年", year, builder, false, false);
        }

        String month = liuNian.getMonth();
        if (month != null && !month.isEmpty()) {
            builder.append("\n");
            fillGanZhiSimpleInfo("流月", month, builder, false, false);
        }

        String day = liuNian.getDay();
        if (day != null && !day.isEmpty()) {
            builder.append("\n");
            fillGanZhiSimpleInfo("流日", day, builder, false, false);
        }

        String hour = liuNian.getHour();
        if (hour != null && !hour.isEmpty()) {
            builder.append("\n");
            fillGanZhiSimpleInfo("流时", hour, builder, false, false);
        }

        return builder;
    }

    protected void fillDayGanPartSimpleInfo(String prefix, StringBuilder builder,
                                            boolean enableCount) {
        String dayGanYinYang = Yi.getYinYangByGanOrZhi(dayGan);
        String dayGanWuXing = Yi.getWuXingByGanOrZhi(dayGan);
        builder.append(prefix).append("：").append(dayGan).append(dayGanYinYang).append(dayGanWuXing).append("\n");
        if (enableCount) {
            wuXingCountMap.computeIfAbsent(dayGanWuXing, k -> new AtomicInteger(0))
                    .addAndGet(1);
        }
    }

    protected void fillGanPartSimpleInfo(String prefix, String gan, StringBuilder builder,
                                         boolean enableCount) {
        String ganYinYang = Yi.getYinYangByGanOrZhi(gan);
        String ganWuXing = Yi.getWuXingByGanOrZhi(gan);
        String ganShiShen = Yi.getShiShenByGanOrZhi(dayGan, gan);
        builder.append(prefix).append("干：").append(gan).append(ganYinYang).append(ganWuXing)
                .append("，十神：").append(ganShiShen)
                .append("\n");
        if (enableCount) {
            wuXingCountMap.computeIfAbsent(ganWuXing, k -> new AtomicInteger(0))
                    .addAndGet(1);
            shiShenCountMap.computeIfAbsent(ganShiShen, k -> new AtomicInteger(0))
                    .addAndGet(1);
        }
    }

    protected void fillZhiPartSimpleInfo(String prefix, String zhi, StringBuilder builder,
                                         boolean enableCount, boolean enableCangGanCount) {
        String zhiYinYang = Yi.getYinYangByGanOrZhi(zhi);
        String zhiWuXing = Yi.getWuXingByGanOrZhi(zhi);
        String zhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, zhi);
        builder.append(prefix).append("支：").append(zhi).append(zhiYinYang).append(zhiWuXing)
                .append("，十神：").append(zhiShiShen)
                .append("，十二长生：").append(zhiToShiErZhangShengMap.get(zhi))
                .append("\n");
        if (enableCount) {
            wuXingCountMap.computeIfAbsent(zhiWuXing, k -> new AtomicInteger(0))
                    .addAndGet(1);
            shiShenCountMap.computeIfAbsent(zhiShiShen, k -> new AtomicInteger(0))
                    .addAndGet(1);
        }
        Set<String> zhihiCangGan = Yi.getCangGanByZhi(zhi);
        if (!zhihiCangGan.isEmpty()) {
            for (String item : zhihiCangGan) {
                String itemYinYang = Yi.getYinYangByGanOrZhi(item);
                String itemWuXing = Yi.getWuXingByGanOrZhi(item);
                String itemShiShen = Yi.getShiShenByGanOrZhi(dayGan, item);
                builder.append("\t藏干：").append(item).append(itemYinYang).append(itemWuXing).append("，十神：").append(itemShiShen).append("\n");
                if (enableCangGanCount) {
                    cangGanWuXingCountMap.computeIfAbsent(itemWuXing, k -> new AtomicInteger(0))
                            .addAndGet(1);
                    cangGanShiShenCountMap.computeIfAbsent(itemShiShen, k -> new AtomicInteger(0))
                            .addAndGet(1);
                }
            }
        }
    }

    protected void fillGanZhiHeadSimpleInfo(String prefix, String ganZhi, StringBuilder builder) {
        builder.append(prefix).append("：").append(ganZhi).append("，纳音：").append(Yi.getNaYinByGanZhi(ganZhi)).append("\n");
    }

    protected void fillGanZhiSimpleInfo(String prefix, String ganZhi, StringBuilder builder,
                                        boolean enableCount, boolean enableCangGanCount) {
        fillGanZhiHeadSimpleInfo(prefix, ganZhi, builder);

        String[] arr = ganZhi.split("");
        String gan = arr[0];
        fillGanPartSimpleInfo(prefix, gan, builder, enableCount);

        String zhi = arr[1];
        fillZhiPartSimpleInfo(prefix, zhi, builder, enableCount, enableCangGanCount);
    }

    protected void fillGanZhiDateSimpleInfo(String prefix, GanZhiDate date, StringBuilder builder) {
        builder.append(prefix).append("：").append(date.getYear())
                .append(" ").append(date.getMonth())
                .append(" ").append(date.getDay())
                .append(" ").append(date.getHour())
                .append("\n");
    }

}
