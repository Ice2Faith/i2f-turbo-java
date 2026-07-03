package i2f.ai.rest.openai.model.data;

import i2f.builder.BaseBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiAssistantMessageRespDto implements OpenAiMessage, BaseBuilder<OpenAiAssistantMessageRespDto> {
    protected final String role = OpenAiConsts.ASSISTANT;
    protected String content;
    protected String reasoning_content;
    protected List<OpenAiToolCall> tool_calls;

    public OpenAiAssistantMessageRespDto(String content) {
        this.content = content;
    }

    @Override
    public String role() {
        return role;
    }

    @Override
    public String content() {
        return content;
    }

    public OpenAiAssistantMessage toAssistant() {
        List<OpenAiToolCall> list = null;
        if (tool_calls != null) {
            list = new ArrayList<>(tool_calls);
        }
        return new OpenAiAssistantMessage().toBuilder()
                .set(u -> u::setContent, content)
                .set(u -> u::setTool_calls, list)
                .build();
    }
}
