package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.tools.yi.BaZi;
import i2f.tools.yi.GanZhiDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2026/6/10 15:18
 * @desc
 */
@ConditionalOnExpression("${ai.tools.ba-zi.enable:true}")
@Component
@Tools
public class BaZiTools {
    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "获取生辰八字的基本排盘信息，五行、十神、命宫、身宫、胎元等"
    )
    public String get_ba_zi_simple_info(
            @ToolParam(value="baZi",description = "生辰八字，每个字段都必须要有值")
            GanZhiToolDto baZi,
            @ToolParam(value = "isMale",description = "男或女, 男为 true 否则（女） false")
            boolean isMale,
            @ToolParam(value="liuNian",description = "流年信息，可以为 null 表示使用当前时间，不是每个字段都需要值，有值的字段会返回对应的流年/流月/流日/流时信息")
            GanZhiToolDto liuNian){
        BaZi ret = BaZi.of(GanZhiDate.of(baZi.getYear(), baZi.getMonth(), baZi.getDay(), baZi.getHour()));

        GanZhiDate liuNianDate=null;
        if(liuNian!=null){
            liuNianDate=GanZhiDate.of(liuNian.getYear(), liuNian.getMonth(), liuNian.getDay(), liuNian.getHour());
        }
        return ret.getSimpleInfo(isMale,liuNianDate);
    }
}
