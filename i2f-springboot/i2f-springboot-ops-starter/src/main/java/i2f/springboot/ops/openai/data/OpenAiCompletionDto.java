package i2f.springboot.ops.openai.data;

import i2f.springboot.ops.openai.data.message.OpenAiMessage;
import i2f.springboot.ops.openai.data.message.OpenAiToolsDefinition;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/30 20:38
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiCompletionDto {
    protected String model;
    protected boolean stream;
    protected List<OpenAiMessage> messages;
    protected List<OpenAiToolsDefinition> tools;
    protected Map<String, Object> stream_options;
}
