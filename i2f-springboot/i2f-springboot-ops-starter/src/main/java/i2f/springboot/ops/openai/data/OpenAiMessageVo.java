package i2f.springboot.ops.openai.data;

import i2f.springboot.ops.openai.data.message.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:22
 * @desc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class OpenAiMessageVo {
    protected String type;
    protected OpenAiSystemMessage system;
    protected OpenAiUserMessage user;
    protected OpenAiAssistantMessage assistant;
    protected OpenAiToolMessage tool;
    protected EchoOpenAiToolMessage echoTool;

}
