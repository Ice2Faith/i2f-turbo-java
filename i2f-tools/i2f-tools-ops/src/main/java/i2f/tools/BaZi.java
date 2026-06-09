package i2f.tools;

import com.GanZhiDate;
import com.Yi;
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
    public static void main(String[] args) {
        BaZi baZi = of(GanZhiDate.of(LocalDateTime.now()));
        System.out.println(baZi);
    }

    public static BaZi of(GanZhiDate date) {
        StringBuilder builder = new StringBuilder();
        builder.append("八字：").append(date.getYear())
                .append(" ").append(date.getMonth())
                .append(" ").append(date.getDay())
                .append(" ").append(date.getHour())
                .append("\n");

        Map<String, AtomicInteger> wuXingCount = new HashMap<>();

        String day = date.getDay();
        String[] arr = day.split("");
        String dayGan = arr[0];
        String dayGanYinYang = Yi.getYinYangByGanOrZhi(dayGan);
        String dayGanWuXing = Yi.getWuXingByGanOrZhi(dayGan);
        builder.append("日干：").append(dayGan).append(dayGanYinYang).append(dayGanWuXing).append("\n");
        wuXingCount.computeIfAbsent(dayGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String dayZhi = arr[1];
        String dayZhiYinYang = Yi.getYinYangByGanOrZhi(dayZhi);
        String dayZhiWuXing = Yi.getWuXingByGanOrZhi(dayZhi);
        String dayZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, dayZhi);
        builder.append("日支：").append(dayZhi).append(dayZhiYinYang).append(dayZhiWuXing).append("，十神：").append(dayZhiShiShen).append("\n");
        wuXingCount.computeIfAbsent(dayZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String month = date.getMonth();
        arr = month.split("");
        String monthGan = arr[0];
        String monthGanYinYang = Yi.getYinYangByGanOrZhi(monthGan);
        String monthGanWuXing = Yi.getWuXingByGanOrZhi(monthGan);
        String monthGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, monthGan);
        builder.append("月干：").append(monthGan).append(monthGanYinYang).append(monthGanWuXing).append("，十神：").append(monthGanShiShen).append("\n");
        wuXingCount.computeIfAbsent(monthGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String monthZhi = arr[1];
        String monthZhiYinYang = Yi.getYinYangByGanOrZhi(monthZhi);
        String monthZhiWuXing = Yi.getWuXingByGanOrZhi(monthZhi);
        String monthZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, monthZhi);
        builder.append("月干：").append(monthZhi).append(monthZhiYinYang).append(monthZhiWuXing).append("，十神：").append(monthZhiShiShen).append("\n");
        wuXingCount.computeIfAbsent(monthZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String year = date.getYear();
        arr = year.split("");
        String yearGan = arr[0];
        String yearGanYinYang = Yi.getYinYangByGanOrZhi(yearGan);
        String yearGanWuXing = Yi.getWuXingByGanOrZhi(yearGan);
        String yearGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, yearGan);
        builder.append("年干：").append(yearGan).append(yearGanYinYang).append(yearGanWuXing).append("，十神：").append(yearGanShiShen).append("\n");
        wuXingCount.computeIfAbsent(yearGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String yearZhi = arr[1];
        String yearZhiYinYang = Yi.getYinYangByGanOrZhi(yearZhi);
        String yearZhiWuXing = Yi.getWuXingByGanOrZhi(yearZhi);
        String yearZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, yearZhi);
        builder.append("年支：").append(yearZhi).append(yearZhiYinYang).append(yearZhiWuXing).append("，十神：").append(yearZhiShiShen).append("\n");
        wuXingCount.computeIfAbsent(yearZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String hour = date.getHour();
        arr = hour.split("");
        String hourGan = arr[0];
        String hourGanYinYang = Yi.getYinYangByGanOrZhi(hourGan);
        String hourGanWuXing = Yi.getWuXingByGanOrZhi(hourGan);
        String hourGanShiShen = Yi.getShiShenByGanOrZhi(dayGan, hourGan);
        builder.append("时干：").append(hourGan).append(hourGanYinYang).append(hourGanWuXing).append("，十神：").append(hourGanShiShen).append("\n");
        wuXingCount.computeIfAbsent(hourGanWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        String hourZhi = arr[1];
        String hourZhiYinYang = Yi.getYinYangByGanOrZhi(hourZhi);
        String hourZhiWuXing = Yi.getWuXingByGanOrZhi(hourZhi);
        String hourZhiShiShen = Yi.getShiShenByGanOrZhi(dayGan, hourZhi);
        builder.append("时支：").append(hourZhi).append(hourZhiYinYang).append(hourZhiWuXing).append("，十神：").append(hourZhiShiShen).append("\n");
        wuXingCount.computeIfAbsent(hourZhiWuXing, k -> new AtomicInteger(0))
                .addAndGet(1);

        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> entry : wuXingCount.entrySet()) {
            list.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().get()));
        }

        list.sort((v1, v2) -> Integer.compare(v2.getValue(), v1.getValue()));

        builder.append("五行个数分布：");
        for (Map.Entry<String, Integer> entry : list) {
            builder.append(" ").append(entry.getKey()).append(": ").append(entry.getValue());
        }
        builder.append("\n");

        List<String> missingWuXingList = new ArrayList<>();
        for (String item : Yi.WU_XING) {
            if (!wuXingCount.containsKey(item)) {
                missingWuXingList.add(item);
            }
        }

        builder.append("五行缺失情况：").append(missingWuXingList.isEmpty() ? "无" : String.join("、", missingWuXingList)).append("\n");

        System.out.println(builder.toString());
        return new BaZi();
    }
}
