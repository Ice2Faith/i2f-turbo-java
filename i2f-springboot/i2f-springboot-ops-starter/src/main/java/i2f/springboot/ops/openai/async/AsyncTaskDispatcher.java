package i2f.springboot.ops.openai.async;

import i2f.springboot.ops.openai.data.OpenAiMeta;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/1 14:22
 * @desc
 */
@Data
@NoArgsConstructor
@Slf4j
@Component
public class AsyncTaskDispatcher implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

    public AsyncTaskItem query(AsyncTaskItem item, OpenAiMeta meta) {
        if (item == null) {
            return null;
        }
        if (item.isFinished()) {
            return item;
        }
        Map<String, AsyncTaskResolver> resolvers = applicationContext.getBeansOfType(AsyncTaskResolver.class);
        for (Map.Entry<String, AsyncTaskResolver> entry : resolvers.entrySet()) {
            AsyncTaskResolver resolver = entry.getValue();
            try {
                if (resolver.support(item,meta)) {
                    return resolver.resolve(item,meta);
                }
            } catch (Throwable e) {
                // ignore
                log.warn("query async task status failure: "+e.getMessage(),e);
            }
        }
        return item;
    }
}
