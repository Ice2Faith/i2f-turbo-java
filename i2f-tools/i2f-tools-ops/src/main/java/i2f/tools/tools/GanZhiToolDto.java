package i2f.tools.tools;

import i2f.ai.std.tool.annotations.ToolParam;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/10 15:23
 * @desc
 */
@Data
@NoArgsConstructor
public class GanZhiToolDto {
    @ToolParam(description = "年柱，例如：甲午 或者 丙寅")
    protected String year;
    @ToolParam(description = "月柱，例如：乙卯 或者 癸未")
    protected String month;
    @ToolParam(description = "日柱，例如：丁未 或者 甲辰")
    protected String day;
    @ToolParam(description = "时柱，例如：乙丑 或者 庚寅")
    protected String hour;
}
