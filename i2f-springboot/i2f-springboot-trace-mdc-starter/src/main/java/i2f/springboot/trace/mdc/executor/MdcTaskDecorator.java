package i2f.springboot.trace.mdc.executor;

import i2f.trace.mdc.MdcHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.task.TaskDecorator;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 在 ThreadPoolTaskExecutor 中使用，以进行线程池的 MDC 传递
 *
 * @author Ice2Faith
 * @date 2026/4/15 9:08
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.trace.mdc.task-decorator.enable:true}")
@ConditionalOnClass(TaskDecorator.class)
@Component
public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        // 获取主线程的 MDC 上下文快照
        Map<String, String> contextMap = MdcHolder.copyOf();
        return () -> {
            try {
                // 在子线程中设置 MDC 上下文
                if (contextMap != null) {
                    MdcHolder.replaceAs(contextMap);
                }
                runnable.run();
            } finally {
                // 执行完后清理，防止线程复用导致的数据污染
                MdcHolder.clear();
            }
        };
    }
}
