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

    String ECHO_LRU_TOOLS = "echo_lru_tools";

    String ECHO_REQUEST_PAYLOAD = "echo_request_payload";

    String ECHO_DYNAMIC_TOOL = "echo_dynamic_tool";

    String ECHO_LOOP_ENGINEERING = "echo_loop_engineering";

    String ECHO_TRUTH_PROMPT = "echo_truth_prompt";

    String ECHO_TRUTH_CONTENT = "echo_truth_content";

    String ECHO_TRUTH_SYNC = "echo_truth_sync";

    String ECHO_SESSION_RECORDS_MAP = "echo_session_records_map";

    String ECHO_TOOL_INTENT_RECOMMEND="echo_tool_intent_recommend";

    String ECHO_PROGRESS = "echo_progress";
}
