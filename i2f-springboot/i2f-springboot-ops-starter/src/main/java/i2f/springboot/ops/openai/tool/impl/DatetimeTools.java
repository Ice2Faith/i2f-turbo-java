package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:13
 * @desc
 */
@Component
@Tools
public class DatetimeTools {

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "get current date,time,datetime,utc timestamp,week, etc."
    )
    public String get_current_datetime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return "datetime:" + now.format(formatter) + "\n"
                + "week:" + now.getDayOfWeek() + "\n"
                + "utc timestamp(s):" + now.toEpochSecond(ZoneOffset.UTC);
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "determine if a given year is a leap year"
    )
    public boolean is_leap_year(@ToolParam(description = "the year to be checked, for example: 2016 or 1998") int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "calculate a given year total days in year"
    )
    public int get_year_total_days(@ToolParam(description = "the year to be calculate, for example: 2016 or 1998") int year) {
        return is_leap_year(year) ? 366 : 365;
    }

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "calculate a given month total days in month"
    )
    public int get_month_total_days(@ToolParam(description = "the year of the month, for example: 2016 or 1998") int year,
                                    @ToolParam(description = "the month to be calculate, value range in [1,12], for example: 1 or 12") int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                return is_leap_year(year) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        throw new IllegalArgumentException("month only accept in range [1,12], but accept :" + month);
    }


}
