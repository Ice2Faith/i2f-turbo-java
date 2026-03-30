package i2f.ai.std.service.proxy;

import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.INamingContext;

import java.lang.reflect.Proxy;

/**
 * @author Ice2Faith
 * @date 2026/3/30 14:20
 * @desc
 */
public class AiServices {
    public static <T> T create(Class<T> type, INamingContext context) {
        return create(type, new AiServiceDynamicProxyHandler(context));
    }

    public static <T> T create(Class<T> type, INamingContext context, JsonSchemaAnnotationResolver resolver) {
        return create(type, new AiServiceDynamicProxyHandler(context, resolver));
    }

    public static <T> T create(Class<T> type, AiServiceDynamicProxyHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }
}
