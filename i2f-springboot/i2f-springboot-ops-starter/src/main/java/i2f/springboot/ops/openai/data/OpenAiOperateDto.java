package i2f.springboot.ops.openai.data;

import i2f.ai.std.tool.definition.ToolDefinition;
import i2f.springboot.ops.openai.async.AsyncTaskItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/30 20:28
 * @desc
 */
@Data
@NoArgsConstructor
public class OpenAiOperateDto {
    protected OpenAiMeta meta;
    protected OpenAiCompletionVo completion;
    protected List<OpenAiToolApprovalDto> toolApprovalList;
    // 插话
    protected OpenAiMessageVo interject;
    // 本轮请求的动作类型，用于区分轮次语义（例如 summary-history 会话总结轮独占一轮，不插入插话）
    protected String action;
    protected boolean encryptOutput = false;
    protected boolean enableTools = false;
    protected boolean enableSkills = false;
    protected boolean enableRags = false;
    protected boolean enableLruTools = false;
    protected boolean enableEchoRequestPayload = false;
    protected boolean enableMergedSystemMsg = false;
    protected boolean enableFullAccess=false;
    protected boolean enableAgentsMarkdown=false;
    protected boolean enableRecycleToolMessage=false;
    protected Integer recycleToolLastKeepCount =10;
    protected Integer recycleToolMessageMinLength=512;
    protected List<String> lruToolNames;
    protected Integer lruToolMaxSize;
    protected boolean enableToolRecommendByIntentRecognize;
    protected List<ToolDefinition> loadedTools;

    protected String md5;
    protected String fileUrl;
    protected boolean parsedText;
    protected boolean enableVisionImage;

    protected boolean enableTruth;
    protected String truthContent;

    protected boolean enableMemories = false;
    protected String memoryBucket;

    protected String ttsContent;

    protected boolean enableLoopEngineering = false;
    protected Map<String, String> sessionRecordsMap = new HashMap<>();
    protected Map<String, String> recycleMap = new HashMap<>();

    protected List<AsyncTaskItem> asyncTasks;

    protected List<OpenAiWebjsToolResult> webjsToolResults;
}
