package i2f.ai.std.tool.test;

import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:06
 * @desc
 */
@Tools
//@Component
public class TestToolComponent {
    @Tool(description = "获取当前日期和时间")
    public String sysdate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Tool(description = "根据城市名获取天气情况")
    public String getWeatherByCity(@ToolParam(description = "城市名，例如：北京") String cityName, @ToolParam(description = "查询的日期") LocalDate date) {
        if (cityName.contains("北京")) {
            return "晴转多云，23摄氏度，微风2级";
        }
        throw new RuntimeException("无法获取天气信息");
    }

    @Tool(description = "模拟复杂入参对象")
    public String mock(@ToolParam(description = "城市名，例如：北京") TestSchemaPojo pojo,
                       List<String> citys,
                       Date time) {
        return "";
    }
}
