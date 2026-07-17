package i2f.springboot.ops.openai.data;

import i2f.ai.rest.openai.model.data.OpenAiAssistantMessage;
import i2f.ai.rest.openai.model.data.OpenAiSystemMessage;
import i2f.ai.rest.openai.model.data.OpenAiToolMessage;
import i2f.ai.rest.openai.model.data.OpenAiUserMessage;
import i2f.mutator.BaseMutator;
import i2f.springboot.ops.openai.data.message.EchoOpenAiToolMessage;
import i2f.springboot.ops.openai.data.message.RequestOpenAiToolMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiMessageVo implements BaseMutator<OpenAiMessageVo> {
    protected String type;
    protected OpenAiSystemMessage system;
    protected OpenAiUserMessage user;
    protected OpenAiAssistantMessage assistant;
    protected OpenAiToolMessage tool;
    protected EchoOpenAiToolMessage echo_tool;
    protected RequestOpenAiToolMessage request_tool;
    protected OpenAiSystemMessage echo_skill;
    protected OpenAiSystemMessage echo_truth;
    protected List<Map<String, Object>> attachFiles;
}
