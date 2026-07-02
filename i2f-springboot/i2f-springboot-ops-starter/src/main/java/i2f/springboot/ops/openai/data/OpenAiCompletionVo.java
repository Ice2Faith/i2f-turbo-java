package i2f.springboot.ops.openai.data;

import i2f.ai.rest.openai.model.data.OpenAiToolsDefinition;
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
public class OpenAiCompletionVo {
    protected String model;
    protected boolean stream;
    protected List<OpenAiMessageVo> messages;
    protected List<OpenAiToolsDefinition> tools;
    protected Map<String, Object> stream_options;
}
