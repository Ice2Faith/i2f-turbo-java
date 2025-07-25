package i2f.spring.ai.mcp.sse.tools;

import i2f.spring.ai.tool.annotations.AiTools;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2025/6/6 20:19
 * @desc
 */
@AiTools
@Component
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

    @Tool(description = "get current date in user's timezone,time,year,month,hour,minute,second,millisecond,week,zone,.etc detail information/" +
            "获取当前用户所在时区的（现在、今天）的日期、时间、年份、月份、小时、分钟、秒数、毫秒、星期、时区等详细信息")
    public String getCurrentDateTimeDescription() {
        String text = CURRENT_TIME_FORMATTER.format(LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()));
        return text;
    }
}
