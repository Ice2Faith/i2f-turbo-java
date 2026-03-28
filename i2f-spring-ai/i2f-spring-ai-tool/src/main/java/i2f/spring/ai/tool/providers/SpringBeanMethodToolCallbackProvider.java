package i2f.spring.ai.tool.providers;

import i2f.ai.std.tool.annotations.Tools;
import i2f.spring.ai.tool.SpringAiToolDefinition;
import i2f.spring.ai.tool.SpringAiToolHelper;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.ApplicationContext;

import java.util.*;
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
            return getCallbacks();
        });
    }

    public ToolCallback[] getCallbacks() {
        Set<String> methodSet = new LinkedHashSet<>();
        List<ToolCallback> list = new ArrayList<>();
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Tools.class);
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object bean = entry.getValue();
            try {
                Map<String, SpringAiToolDefinition> map = SpringAiToolHelper.parseTools(bean);
                for (Map.Entry<String, SpringAiToolDefinition> def : map.entrySet()) {
                    SpringAiToolDefinition val = def.getValue();
                    ToolCallback function = val.getFunction();
                    String toolKey = function.getToolDefinition().name() + "#" + function.getToolDefinition().description();
                    if (!methodSet.contains(toolKey)) {
                        list.add(function);
                        methodSet.add(toolKey);
                    }
                }
                ToolCallback[] callbacks = ToolCallbacks.from(bean);
                if (callbacks != null && callbacks.length > 0) {
                    for (ToolCallback function : callbacks) {
                        String toolKey = function.getToolDefinition().name() + "#" + function.getToolDefinition().description();
                        if (!methodSet.contains(toolKey)) {
                            list.add(function);
                            methodSet.add(toolKey);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list.toArray(new ToolCallback[0]);
    }
}
