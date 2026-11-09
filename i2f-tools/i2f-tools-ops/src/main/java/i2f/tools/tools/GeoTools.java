package i2f.tools.tools;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.springboot.ops.openai.controller.HttpServletToolContextUtil;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.net.NetworkUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Ice2Faith
 * @date 2026/11/9 11:43
 * @desc
 */
@McpServerExpose
@ToolIntent(items = @ToolIntentItem(value = "fuzzy_geo", description = "提供大致的用户地理位置信息（不精确）等"))
@ConditionalOnExpression("${ai.tools.fuzzy_geo.enable:true}")
@Component
@Tools(tags = {
        AiTags.READONLY_VALUE
})
public class GeoTools {
    private RestTemplate restTemplate=new RestTemplate();

    @Tool(
            tags = {
                    AiTags.AUTO_VALUE
            },
            description = "get user fuzzy geo location, use user ip"
    )
    public String fuzzy_geo_location() {
        String ip = ToolCallContextHolder.get(HttpServletToolContextUtil.HTTP_CLIENT_IP);
        if(ip==null || ip.isEmpty() || NetworkUtil.isLocalHost(ip) || NetworkUtil.isPrivateIpv4(ip)) {
            return restTemplate.getForObject("http://ip-api.com/json/?lang=zh-CN",String.class);
        }
        return restTemplate.getForObject("http://ip-api.com/json/"+ip+"?lang=zh-CN",String.class);
    }
}
