package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.Tools;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2026/6/1 19:13
 * @desc
 */
@Component
@Tools
public class DatetimeTools {

    @Tool(description = "get current date,time,datetime,week,etc.")
    public String get_current_datetime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return "datetime:" + now.format(formatter) + "\n"
                + "week:" + now.getDayOfWeek();
    }
}
