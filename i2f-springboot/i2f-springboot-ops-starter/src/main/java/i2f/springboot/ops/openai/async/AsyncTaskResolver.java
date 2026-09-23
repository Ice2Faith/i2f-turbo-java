package i2f.springboot.ops.openai.async;

import i2f.springboot.ops.openai.data.OpenAiMeta;

/**
 * @author Ice2Faith
 * @date 2026/9/1 14:26
 * @desc
 */
public interface AsyncTaskResolver {
    boolean support(AsyncTaskItem item, OpenAiMeta meta) throws Exception;

    AsyncTaskItem resolve(AsyncTaskItem item,OpenAiMeta meta) throws Exception;
}
