package i2f.tools;

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
public class BaZi {
    private GanZhiDate date;

    public static void main(String[] args) {
        BaZi baZi = of(GanZhiDate.of(LocalDateTime.now()));
        System.out.println(baZi.getSimpleInfo());
    }

    public static BaZi of(GanZhiDate date) {
        BaZi baZi = new BaZi();
        baZi.setDate(date);
        return baZi;
    }

    public String getSimpleInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("八字：").append(date.getYear())
                .append(" ").append(date.getMonth())
                .append(" ").append(date.getDay())
                .append(" ").append(date.getHour())
                .append("\n");

        Map<String, AtomicInteger> wuXingCountMap = new HashMap<>();
        Map<String, AtomicInteger> shiShenCountMap = new HashMap<>();

        Map<String, AtomicInteger> cangGanWuXingCountMap = new HashMap<>();
        Map<String, AtomicInteger> cangGanShiShenCountMap = new HashMap<>();

        String day = date.getDay();
        String dayGan = day.substring(0, 1);

        Map<String, String> shiErZhangShengToZhiMap = Yi.getShiErZhangShengToZhiByGan(dayGan);
        Map<String, String> zhiToShiErZhangShengMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : shiErZhangShengToZhiMap.entrySet()) {
            zhiToShiErZhangShengMap.put(entry.getValue(), entry.getKey());
        }

        builder.append("\n");
        String mingGong = Yi.getMingGongGanZhi(date);
        builder.append("命宫：").append(mingGong).append("，纳音：").append(Yi.getNaYinByGanZhi(mingGong)).append("\n");
        String[] arr = mingGong.split("");
        String mingGongGan = arr[0];
        String mingGongGanYinYang = Yi.getYinYangByGanOrZhi(mingGongGan);
        String mingGongGanWuXing = Yi.getWuXingByGanOrZhi(mingGongGan);
        String mingGongGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, mingGongGan);
        builder.append("命宫干：").append(mingGongGan).append(mingGongGanYinYang).append(mingGongGanWuXing)
                .append("，十神：").append(mingGongGanShiShen)
                .append("\n");

        String mingGongZhi = arr[1];
        String mingGongZhiYinYang = Yi.getYinYangByGanOrZhi(mingGongZhi);
        String mingGongZhiWuXing = Yi.getWuXingByGanOrZhi(mingGongZhi);
        String mingGongZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, mingGongZhi);
        builder.append("命宫支：").append(mingGongZhi).append(mingGongZhiYinYang).append(mingGongZhiWuXing)
                .append("，十神：").append(mingGongZhiShiShen)
                .append("，十二长生：").append(zhiToShiErZhangShengMap.get(mingGongZhi))
                .append("\n");
        wuXingCountMap.computeIfAbsent(mingGongZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(mingGongZhiShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);
        Set<String> mingGongZhiCangGan = Yi.getCangGanByZhi(mingGongZhi);
        if (!mingGongZhiCangGan.isEmpty()) {
            for (String item : mingGongZhiCangGan) {
                String itemYinYang = Yi.getYinYangByGanOrZhi(item);
                String itemWuXing = Yi.getWuXingByGanOrZhi(item);
                String itemShiShen = Yi.getShiShenByGanOrZhi(dayGan, item);
                builder.append("\t藏干：").append(item).append(itemYinYang).append(itemWuXing).append("，十神：").append(itemShiShen).append("\n");
            }
        }


        builder.append("\n");
        builder.append("日柱：").append(day).append("，纳音：").append(Yi.getNaYinByGanZhi(day)).append("\n");
        arr = day.split("");
        String dayGanYinYang = Yi.getYinYangByGanOrZhi(dayGan);
        String dayGanWuXing = Yi.getWuXingByGanOrZhi(dayGan);
        builder.append("日干：").append(dayGan).append(dayGanYinYang).append(dayGanWuXing).append("\n");
        wuXingCountMap.computeIfAbsent(dayGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);


        String dayZhi = arr[1];
        String dayZhiYinYang = Yi.getYinYangByGanOrZhi(dayZhi);
        String dayZhiWuXing = Yi.getWuXingByGanOrZhi(dayZhi);
        String dayZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, dayZhi);
        builder.append("日支：").append(dayZhi).append(dayZhiYinYang).append(dayZhiWuXing)
                .append("，十神：").append(dayZhiShiShen)
                .append("，十二长生：").append(zhiToShiErZhangShengMap.get(dayZhi))
                .append("\n");
        wuXingCountMap.computeIfAbsent(dayZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(dayZhiShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);
        Set<String> dayZhiCangGan = Yi.getCangGanByZhi(dayZhi);
        if (!dayZhiCangGan.isEmpty()) {
            for (String item : dayZhiCangGan) {
                String itemYinYang = Yi.getYinYangByGanOrZhi(item);
                String itemWuXing = Yi.getWuXingByGanOrZhi(item);
                String itemShiShen = Yi.getShiShenByGanOrZhi(dayGan, item);
                builder.append("\t藏干：").append(item).append(itemYinYang).append(itemWuXing).append("，十神：").append(itemShiShen).append("\n");
                cangGanWuXingCountMap.computeIfAbsent(itemWuXing, k -> new AtomicInteger(0))
                        .addAndGet(1);
                cangGanShiShenCountMap.computeIfAbsent(itemShiShen, k -> new AtomicInteger(0))
                        .addAndGet(1);
            }
        }

        builder.append("\n");
        String month = date.getMonth();
        builder.append("月柱：").append(month).append("，纳音：").append(Yi.getNaYinByGanZhi(month)).append("\n");
        arr = month.split("");
        String monthGan = arr[0];
        String monthGanYinYang = Yi.getYinYangByGanOrZhi(monthGan);
        String monthGanWuXing = Yi.getWuXingByGanOrZhi(monthGan);
        String monthGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, monthGan);
        builder.append("月干：").append(monthGan).append(monthGanYinYang).append(monthGanWuXing)
                .append("，十神：").append(monthGanShiShen)
                .append("\n");
        wuXingCountMap.computeIfAbsent(monthGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(monthGanShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);

        String monthZhi = arr[1];
        String monthZhiYinYang = Yi.getYinYangByGanOrZhi(monthZhi);
        String monthZhiWuXing = Yi.getWuXingByGanOrZhi(monthZhi);
        String monthZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, monthZhi);
        builder.append("月支：").append(monthZhi).append(monthZhiYinYang).append(monthZhiWuXing)
                .append("，十神：").append(monthZhiShiShen)
                .append("，十二长生：").append(zhiToShiErZhangShengMap.get(monthZhi))
                .append("\n");
        wuXingCountMap.computeIfAbsent(monthZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(monthZhiShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);
        Set<String> monthZhiCangGan = Yi.getCangGanByZhi(monthZhi);
        if (!monthZhiCangGan.isEmpty()) {
            for (String item : monthZhiCangGan) {
                String itemYinYang = Yi.getYinYangByGanOrZhi(item);
                String itemWuXing = Yi.getWuXingByGanOrZhi(item);
                String itemShiShen = Yi.getShiShenByGanOrZhi(dayGan, item);
                builder.append("\t藏干：").append(item).append(itemYinYang).append(itemWuXing).append("，十神：").append(itemShiShen).append("\n");
                cangGanWuXingCountMap.computeIfAbsent(itemWuXing, k -> new AtomicInteger(0))
                        .addAndGet(1);
                cangGanShiShenCountMap.computeIfAbsent(itemShiShen, k -> new AtomicInteger(0))
                        .addAndGet(1);
            }
        }


        builder.append("\n");
        String year = date.getYear();
        builder.append("年柱：").append(year).append("，纳音：").append(Yi.getNaYinByGanZhi(year)).append("\n");
        arr = year.split("");
        String yearGan = arr[0];
        String yearGanYinYang = Yi.getYinYangByGanOrZhi(yearGan);
        String yearGanWuXing = Yi.getWuXingByGanOrZhi(yearGan);
        String yearGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, yearGan);
        builder.append("年干：").append(yearGan).append(yearGanYinYang).append(yearGanWuXing)
                .append("，十神：").append(yearGanShiShen)
                .append("\n");
        wuXingCountMap.computeIfAbsent(yearGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(yearGanShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);

        String yearZhi = arr[1];
        String yearZhiYinYang = Yi.getYinYangByGanOrZhi(yearZhi);
        String yearZhiWuXing = Yi.getWuXingByGanOrZhi(yearZhi);
        String yearZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, yearZhi);
        builder.append("年支：").append(yearZhi).append(yearZhiYinYang).append(yearZhiWuXing)
                .append("，十神：").append(yearZhiShiShen)
                .append("，十二长生：").append(zhiToShiErZhangShengMap.get(yearZhi))
                .append("\n");
        wuXingCountMap.computeIfAbsent(yearZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(yearZhiShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);
        Set<String> yearZhiCangGan = Yi.getCangGanByZhi(yearZhi);
        if (!yearZhiCangGan.isEmpty()) {
            for (String item : yearZhiCangGan) {
                String itemYinYang = Yi.getYinYangByGanOrZhi(item);
                String itemWuXing = Yi.getWuXingByGanOrZhi(item);
                String itemShiShen = Yi.getShiShenByGanOrZhi(dayGan, item);
                builder.append("\t藏干：").append(item).append(itemYinYang).append(itemWuXing).append("，十神：").append(itemShiShen).append("\n");
                cangGanWuXingCountMap.computeIfAbsent(itemWuXing, k -> new AtomicInteger(0))
                        .addAndGet(1);
                cangGanShiShenCountMap.computeIfAbsent(itemShiShen, k -> new AtomicInteger(0))
                        .addAndGet(1);
            }
        }


        builder.append("\n");
        String hour = date.getHour();
        builder.append("时柱：").append(hour).append("，纳音：").append(Yi.getNaYinByGanZhi(hour)).append("\n");
        arr = hour.split("");
        String hourGan = arr[0];
        String hourGanYinYang = Yi.getYinYangByGanOrZhi(hourGan);
        String hourGanWuXing = Yi.getWuXingByGanOrZhi(hourGan);
        String hourGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, hourGan);
        builder.append("时干：").append(hourGan).append(hourGanYinYang).append(hourGanWuXing)
                .append("，十神：").append(hourGanShiShen)
                .append("\n");
        wuXingCountMap.computeIfAbsent(hourGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(hourGanShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);

        String hourZhi = arr[1];
        String hourZhiYinYang = Yi.getYinYangByGanOrZhi(hourZhi);
        String hourZhiWuXing = Yi.getWuXingByGanOrZhi(hourZhi);
        String hourZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, hourZhi);
        builder.append("时支：").append(hourZhi).append(hourZhiYinYang).append(hourZhiWuXing)
                .append("，十神：").append(hourZhiShiShen)
                .append("，十二长生：").append(zhiToShiErZhangShengMap.get(hourZhi))
                .append("\n");
        wuXingCountMap.computeIfAbsent(hourZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);
        shiShenCountMap.computeIfAbsent(hourZhiShiShen, k -> new AtomicInteger(0))
                .addAndGet(1);
        Set<String> hourZhiCangGan = Yi.getCangGanByZhi(hourZhi);
        if (!hourZhiCangGan.isEmpty()) {
            for (String item : hourZhiCangGan) {
                String itemYinYang = Yi.getYinYangByGanOrZhi(item);
                String itemWuXing = Yi.getWuXingByGanOrZhi(item);
                String itemShiShen = Yi.getShiShenByGanOrZhi(dayGan, item);
                builder.append("\t藏干：").append(item).append(itemYinYang).append(itemWuXing).append("，十神：").append(itemShiShen).append("\n");
                cangGanWuXingCountMap.computeIfAbsent(itemWuXing, k -> new AtomicInteger(0))
                        .addAndGet(1);
                cangGanShiShenCountMap.computeIfAbsent(itemShiShen, k -> new AtomicInteger(0))
                        .addAndGet(1);
            }
        }


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

        return builder.toString();
    }
}
