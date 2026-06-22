package i2f.springboot.ops.openai.data.message;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:34
 * @desc
 */
public interface OpenAiConsts {
    String ROLE = "role";

    String SYSTEM = "system";
    String USER = "user";
    String ASSISTANT = "assistant";
    String TOOL = "tool";

    String TOOL_CALLS = "tool_calls";

    String FUNCTION = "function";

    String ECHO_TOOL = "echo_tool";

    String REQUEST_TOOL = "request_tool";

    String DEFINITION_TOOL = "definition_tool";

    String ECHO_SKILL = "echo_skill";
}
