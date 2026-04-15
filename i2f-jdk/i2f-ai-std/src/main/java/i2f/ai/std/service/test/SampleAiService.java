package i2f.ai.std.service.test;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.service.annotations.*;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;

/**
 * @author Ice2Faith
 * @date 2026/3/30 8:44
 * @desc
 */
@AiService(
        enable = false,
        agent = {
                @AiAgents(clazz = AiAgent.class)
        },
        tools = {
                @AiTools(value = {"datetimeTools"})
        })
public interface SampleAiService {

    @AiAgents(value = "orderAgent")
    @AiSystem("根据要求对订单进行处理")
    @AiTools(value = {"userTools"})
    String orderProcess(@AiUser String question,
                        @AiParam(value = "orderId", description = "订单号") String orderId);

    @Tool(description = "撤销订单")
    default String cancelOrder(@ToolParam(description = "订单号") String orderId) {
        return "撤销成功";
    }
}
