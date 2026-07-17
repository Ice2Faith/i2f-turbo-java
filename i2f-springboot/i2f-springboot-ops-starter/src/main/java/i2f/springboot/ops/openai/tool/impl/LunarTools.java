package i2f.springboot.ops.openai.tool.impl;

import com.nlf.calendar.EightChar;
import com.nlf.calendar.Lunar;
import com.nlf.calendar.Solar;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/8 20:08
 * @desc
 */
@ConditionalOnClass(Lunar.class)
@ConditionalOnExpression("${ai.tools.lunar.enable:true}")
@Component
@Tools(tags = {
        AiTags.DATETIME_VALUE
})
public class LunarTools {

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "获取指定公历日期的农历、四柱八字、纳音、五行等信息"
    )
    public Map<String, Object> extra_date_lunar_metadata(@ToolParam(value = "date", description = "the date, use java date pattern 'yyyy-MM-dd HH:mm', for example: '2016-01-01 00:00' or '1998-11-23 14:54'")
                                                         String date) throws Exception {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = fmt.parse(date);
        Solar solar = Solar.fromDate(d);
        Lunar lunar = solar.getLunar();
        return getLunarMetadata(lunar);
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "获取指定农历日期的四柱八字、纳音、五行等信息"
    )
    public Map<String, Object> extra_lunar_metadata(@ToolParam(value = "year", description = "the year, for example: 1996 or 2004")
                                                    int year,
                                                    @ToolParam(value = "month", description = "the month, for example: 1 or 12")
                                                    int month,
                                                    @ToolParam(value = "day", description = "the day, for example: 1 or 21")
                                                    int day,
                                                    @ToolParam(value = "hour", description = "the hour, for example: 0 or 23")
                                                    int hour) throws Exception {
        return getLunarMetadata(new Lunar(year, month, day, hour, 0, 0));
    }

    public Map<String, Object> getLunarMetadata(Lunar lunar) {
        EightChar eightChar = lunar.getEightChar();
        Map<String, Object> map = new HashMap<>();
        map.put("农历", lunar.toFullString());

        map.put("年柱", eightChar.getYear());
        map.put("月柱", eightChar.getMonth());
        map.put("日柱", eightChar.getDay());
        map.put("时柱", eightChar.getTime());
        map.put("命宫", eightChar.getMingGong());
        map.put("身宫", eightChar.getShenGong());

        map.put("年柱纳音", eightChar.getYearNaYin());
        map.put("月柱纳音", eightChar.getMonthNaYin());
        map.put("日柱纳音", eightChar.getDayNaYin());
        map.put("时柱纳音", eightChar.getTimeNaYin());
        map.put("命宫纳音", eightChar.getMingGongNaYin());
        map.put("身宫纳音", eightChar.getShenGongNaYin());

        map.put("年柱五行", eightChar.getYearWuXing());
        map.put("月柱五行", eightChar.getMonthWuXing());
        map.put("日柱五行", eightChar.getDayWuXing());
        map.put("时柱五行", eightChar.getDayWuXing());
        return map;
    }
}
