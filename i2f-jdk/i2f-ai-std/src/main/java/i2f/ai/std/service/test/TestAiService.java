package i2f.ai.std.service.test;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.service.proxy.AiServiceDynamicProxyHandler;
import i2f.ai.std.service.proxy.AiServices;
import i2f.context.impl.ListableNamingContext;
import i2f.context.std.INamingContext;


/**
 * @author Ice2Faith
 * @date 2026/3/30 10:48
 * @desc
 */
public class TestAiService {
    public static void main(String[] args) {
        ListableNamingContext ctx = new ListableNamingContext();
        ctx.addBean("aiAgent", new AiAgent());
        INamingContext context = ctx;

        AiServiceDynamicProxyHandler handler = new AiServiceDynamicProxyHandler(context);
        SampleAiService service = AiServices.create(SampleAiService.class, handler);

        String ret = service.orderProcess("撤销该订单", "1001");
        System.out.println(ret);
    }
}
