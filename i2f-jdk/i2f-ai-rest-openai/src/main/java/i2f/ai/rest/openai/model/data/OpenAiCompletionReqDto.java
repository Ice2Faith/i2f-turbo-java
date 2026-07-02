package i2f.ai.rest.openai.model.data;

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
public class OpenAiCompletionReqDto {
    protected String model;
    protected Boolean stream;
    protected List<OpenAiMessage> messages;
    protected List<OpenAiToolsDefinition> tools;
    protected Map<String, Object> stream_options;
}
