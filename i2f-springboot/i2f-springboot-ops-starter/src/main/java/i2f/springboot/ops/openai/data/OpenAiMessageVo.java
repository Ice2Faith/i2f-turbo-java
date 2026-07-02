package i2f.springboot.ops.openai.data;

import i2f.ai.rest.openai.model.data.OpenAiAssistantMessage;
import i2f.ai.rest.openai.model.data.OpenAiSystemMessage;
import i2f.ai.rest.openai.model.data.OpenAiToolMessage;
import i2f.ai.rest.openai.model.data.OpenAiUserMessage;
import i2f.springboot.ops.openai.data.message.EchoOpenAiToolMessage;
import i2f.springboot.ops.openai.data.message.RequestOpenAiToolMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiMessageVo {
    protected String type;
    protected OpenAiSystemMessage system;
    protected OpenAiUserMessage user;
    protected OpenAiAssistantMessage assistant;
    protected OpenAiToolMessage tool;
    protected EchoOpenAiToolMessage echo_tool;
    protected RequestOpenAiToolMessage request_tool;
    protected OpenAiSystemMessage echo_skill;
}
