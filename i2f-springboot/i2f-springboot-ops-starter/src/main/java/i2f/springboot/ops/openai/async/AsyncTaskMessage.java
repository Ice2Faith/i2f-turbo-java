package i2f.springboot.ops.openai.async;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/1 14:35
 * @desc
 */
@Data
@NoArgsConstructor
public class AsyncTaskMessage {
    protected List<AsyncTaskItem> list;
    protected String content;
}
