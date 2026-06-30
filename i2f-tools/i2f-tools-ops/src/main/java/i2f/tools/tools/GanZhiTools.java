package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.tools.yi.GanZhiDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2026/6/10 15:42
 * @desc
 */
@ConditionalOnExpression("${ai.tools.gan-zhi.enable:true}")
@Component
@Tools
public class GanZhiTools {
    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "根据公历时间获取对应的生辰八字（节气四柱）"
    )
    public String get_gan_zhi_date(
            @ToolParam(value = "date", description = "公历时间，格式使用java标准的 yyyy-MM-dd HH:mm 格式，例如：2001-2-16 13:54")
            String date) {
        LocalDateTime ldate = LocalDateTime.parse(date, FMT);
        GanZhiDate ret = GanZhiDate.of(ldate);
        return "年柱：" + ret.getYear() + "\n"
                + "月柱：" + ret.getMonth() + "\n"
                + "日柱：" + ret.getDay() + "\n"
                + "时柱：" + ret.getHour() + "\n";
    }
}
