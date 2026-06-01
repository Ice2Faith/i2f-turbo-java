package i2f.springboot.ops.openai.tool.impl;

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

    @Tool(description = "get current date,time,datetime,utc timestamp,week, etc.")
    public String get_current_datetime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return "datetime:" + now.format(formatter) + "\n"
                + "week:" + now.getDayOfWeek()
                + "utc timestamp(s):" + now.toEpochSecond(ZoneOffset.UTC);
    }

    @Tool(description = "determine if a given year is a leap year")
    public boolean is_leap_year(@ToolParam(description = "the year to be checked, for example: 2016 or 1998") int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
