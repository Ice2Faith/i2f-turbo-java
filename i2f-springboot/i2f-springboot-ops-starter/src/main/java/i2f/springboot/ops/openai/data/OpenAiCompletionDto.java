package i2f.springboot.ops.openai.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/30 20:38
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiCompletionDto {
    protected String model;
    protected boolean stream;
    protected List<Map<String, Object>> messages;
    protected List<Map<String, Object>> tools;
}
