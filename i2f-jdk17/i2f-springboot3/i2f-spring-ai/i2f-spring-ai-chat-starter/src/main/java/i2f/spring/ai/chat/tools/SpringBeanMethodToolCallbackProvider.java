package i2f.spring.ai.chat.tools;

import i2f.spring.ai.chat.annotations.EnabledTools;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/6/3 20:36
 * @desc
 */
public class SpringBeanMethodToolCallbackProvider implements ToolCallbackProvider {
    protected ApplicationContext applicationContext;
    private final transient AtomicReference<ToolCallback[]> toolCallbacks = new AtomicReference<>();

    public SpringBeanMethodToolCallbackProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public ToolCallback[] getToolCallbacks() {
        return toolCallbacks.updateAndGet(e -> {
            if (e != null) {
                return e;
            }
            return getToolCallbacks();
        });
    }

    public ToolCallback[] getCallbacks() {
        List<ToolCallback> list = new ArrayList<>();
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(EnabledTools.class);
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object bean = entry.getValue();
            try {
                ToolCallback[] callbacks = ToolCallbacks.from(bean);
                if (callbacks != null && callbacks.length > 0) {
                    list.addAll(Arrays.asList(callbacks));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list.toArray(new ToolCallback[0]);
    }
}
