package i2f.springboot.ops.openai.data.message;

import i2f.ai.rest.openai.model.data.OpenAiConsts;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:34
 * @desc
 */
public interface OpsOpenAiConsts extends OpenAiConsts {

    String ECHO_TOOL = "echo_tool";

    String REQUEST_TOOL = "request_tool";

    String DEFINITION_TOOL = "definition_tool";

    String ECHO_SKILL = "echo_skill";
}
