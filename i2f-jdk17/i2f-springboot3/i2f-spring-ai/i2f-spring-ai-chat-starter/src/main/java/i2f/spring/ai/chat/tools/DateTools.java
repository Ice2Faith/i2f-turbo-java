package i2f.spring.ai.chat.tools;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2025/4/26 19:22
 * @desc
 */
public class DateTools {
    public static final DateTimeFormatter CURRENT_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "'current year:' yyyy\n" +
                    "'current month:' MM\n" +
                    "'current day:' dd\n" +
                    "'current hour(24-hour clock system):' HH\n" +
                    "'current minute:' mm\n" +
                    "'current second:' ss\n" +
                    "'current millisecond:' SSS\n" +
                    "'current week:' EEEE\n" +
                    "'datetime localized zone:' O\n" +
                    "'当前年份:' yyyy\n" +
                    "'当前月份:' MM\n" +
                    "'当前日期、号数:' dd\n" +
                    "'当前小时数(24小时制):' HH\n" +
                    "'当前分钟:' mm\n" +
                    "'当前秒数:' ss\n" +
                    "'当前毫秒数:' SSS\n" +
                    "'当前星期:' EEEE\n" +
                    "'日期时间的时区:' O\n" +
                    "");

    @Tool(description = "get current date,time,year,month,hour,minute,second,millisecond,week,zone,.etc detail information/" +
            "获取当前（现在、今天）的日期、时间、年份、月份、小时、分钟、秒数、毫秒、星期、时区等详细信息")
    public String getCurrentDateTimeDescription() {
        return CURRENT_TIME_FORMATTER.format(LocalDateTime.now());
    }

}
