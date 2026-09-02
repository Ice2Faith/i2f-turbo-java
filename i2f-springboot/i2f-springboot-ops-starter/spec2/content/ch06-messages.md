<p class="lead">消息是框架的第一公民。标准层定义 4 种角色消息，Ops 层通过 <code>OpenAiMessageVo</code> 多态载体与 <code>OpsOpenAiConsts</code> 扩展出 14 种<b>回显消息</b>——每个类型独占一个字段，让前端不仅能看到对话，还能看到"系统为模型做了什么"（提示词注入 / 工具调用 / 事实变迁）。</p>

<div class="two-col">
<div>
<div class="panel-title">OpenAI 标准四角色</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>角色</th><th>常量</th><th>语义</th></tr></thead>
<tbody>
<tr><td><span class="tag t-b">user</span></td><td><code>OpenAiConsts.USER</code></td><td>用户提示词，一般由用户发送</td></tr>
<tr><td><span class="tag t-g">system</span></td><td><code>OpenAiConsts.SYSTEM</code></td><td>系统提示词，限定角色 / 职责 / 推理约束</td></tr>
<tr><td><span class="tag t-o">assistant</span></td><td><code>OpenAiConsts.ASSISTANT</code></td><td>模型答复；特例：携带 <code>tool_calls</code> 契约</td></tr>
<tr><td><span class="tag t-t">tool</span></td><td><code>OpenAiConsts.TOOL</code></td><td>工具执行结果，凭 <code>tool_call_id</code> 关联契约</td></tr>
</tbody>
</table>
</div>
<p class="body" style="font-size:13px;">前端可按角色定制显示/隐藏；<code>assistant</code> 消息额外支持 <code>reasoning_content</code> 思考过程折叠渲染。</p>
</div>
<div>
<div class="panel-title">Ops 扩展回显类型（OpsOpenAiConsts）</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>类型常量</th><th>载荷</th><th>用途</th></tr></thead>
<tbody>
<tr><td><code>definition_tool</code></td><td>工具定义 DTO</td><td>告知前端本轮注入了哪些工具（名称/描述/参数/标签/绑定类方法）</td></tr>
<tr><td><code>echo_tool</code></td><td>EchoOpenAiToolMessage</td><td>回显工具执行结果与调用参数</td></tr>
<tr><td><code>request_tool</code></td><td>RequestOpenAiToolMessage</td><td>回显工具调用请求</td></tr>
<tr><td><code>echo_skill</code></td><td>技能系统提示词</td><td>回显注入的技能声明提示词（SKILL.md 聚合）</td></tr>
<tr><td><code>echo_dynamic_tool</code></td><td>MCP 动态工具提示词</td><td>回显注入的 MCP 三步发现指引（列举供应商 → 列举工具 → 装载工具）</td></tr>
<tr><td><code>echo_loop_engineering</code></td><td>循环工程提示词</td><td>回显注入的五步工程化工作流提示词（进度恢复 → 需求 → 方案 → 待办 → 实施）</td></tr>
<tr><td><code>echo_truth_prompt</code></td><td>事实使用方式提示词</td><td>回显注入的 TruthStoreTools 事实读写指引（如何用 <code>store_truth</code>）</td></tr>
<tr><td><code>echo_truth_content</code></td><td>事实内容系统消息</td><td>回显注入的「# 关键事实」内容（置于 messages[0]）</td></tr>
<tr><td><code>echo_truth_sync</code></td><td>事实同步系统消息</td><td>工具执行后回显最新事实——<code>store_truth</code> 可能改写事实，前端据此刷新下一轮 <code>truthContent</code></td></tr>
<tr><td><code>echo_lru_tools</code></td><td>工具名列表</td><td>同步 LRU 淘汰后的存活工具集</td></tr>
<tr><td><code>echo_tool_intent_recommend</code></td><td>意图识别推理结果</td><td>回显工具意图识别的完整推理过程（prompt / rawResult / finalResult），前端可见、可追溯</td></tr>
<tr><td><code>echo_request_payload</code></td><td>完整补全报文</td><td>回显实际发给 LLM 的请求报文（学习/调试）</td></tr>
<tr><td><code>echo_session_records_map</code></td><td>循环工程会话记录 Map</td><td>流结束前回显 request / plan / checklist / agent 四类记录，前端持久化接力</td></tr>
<tr><td><code>echo_async_tasks</code></td><td>异步任务列表</td><td>回显工具产生的异步任务（如文生图），前端渲染状态标签并提供刷新查询</td></tr>
</tbody>
</table>
</div>
</div>
</div>

<div class="panel-title">OpenAiMessageVo.java — 多态消息载体（type + 每类型独立字段）</div>

```java
public class OpenAiMessageVo {
    protected String type; // 消息类型（决定读取哪个字段）

    // ---- 标准四角色 ----
    protected OpenAiSystemMessage system;
    protected OpenAiUserMessage user;
    protected OpenAiAssistantMessage assistant;
    protected OpenAiToolMessage tool;

    // ---- 工具回显 ----
    protected EchoOpenAiToolMessage echo_tool; // 工具执行结果
    protected RequestOpenAiToolMessage request_tool; // 工具调用请求

    // ---- 系统提示词注入回显（OpenAiSystemMessage 载荷）----
    protected OpenAiSystemMessage echo_skill; // 技能声明
    protected OpenAiSystemMessage echo_dynamic_tool; // MCP 动态工具指引
    protected OpenAiSystemMessage echo_loop_engineering; // 循环工程工作流

    // ---- 事实注入三态回显（Truth 三元组）----
    protected OpenAiSystemMessage echo_truth_prompt; // 事实使用方式
    protected OpenAiSystemMessage echo_truth_content; // 事实内容
    protected OpenAiSystemMessage echo_truth_sync; // 事实同步（工具执行后）

    // ---- 工具意图推荐 ----
    protected OpenAiSystemMessage echo_tool_intent_recommend; // 意图识别推理过程

    protected List<TmpFileTools.UploadTmpFileMetadata> attachFiles; // 附件

    // ---- 异步任务 ----
    protected OpenAiSystemMessage echo_async_tasks; // 异步任务回显
    protected List<AsyncTaskItem> asyncTasks; // 异步任务列表
}
```

<div class="callout" style="--c:#862e9c;">
<div class="co-title">Truth 三元组 — echo_truth 的一分为三</div>
<p>原单一 <code>echo_truth</code> 细化为三个独立事件，对应事实生命周期的三个时刻：请求开始注入「# 关键事实」内容时发 <code>echo_truth_content</code>；注入事实使用方式指引时发 <code>echo_truth_prompt</code>；工具执行完毕（模型可能刚调用 <code>store_truth</code> 改写了事实）再以 <code>echo_truth_sync</code> 回显最新事实。前端倒序查找 <code>echo_truth_content</code> / <code>echo_truth_sync</code> 取最新值作为下一轮 <code>truthContent</code>——事实的「注入 → 使用 → 同步」全程可见、可追溯。</p>
</div>

<div class="panel-title">OpenAiOperateDto.java — 一次请求即一份"功能开关清单"</div>

```java
public class OpenAiOperateDto {
    protected OpenAiMeta meta; // baseUrl · apiKey
    protected OpenAiCompletionVo completion; // model · messages · stream
    protected List<OpenAiToolApprovalDto> toolApprovalList; // HITL 审批决定

    protected boolean encryptOutput; // 输出是否 SM4 加密
    protected boolean enableTools; // Function Calling 开关
    protected boolean enableSkills; // 技能系统开关
    protected boolean enableRags; // 知识库开关
    protected boolean enableLruTools; // MCP 动态工具开关
    protected boolean enableToolRecommendByIntentRecognize; // 意图识别辅助工具推荐（依赖 enableLruTools）
    protected boolean enableEchoRequestPayload; // 请求报文回显
    protected boolean enableMergedSystemMsg; // 系统重排：合并系统消息至首条
    protected boolean enableTruth; // 事实注入开关
    protected boolean enableMemories; // 记忆系统开关
    protected String memoryBucket; // 记忆桶（用户级隔离）

    protected List<String> lruToolNames; // LRU 存活工具
    protected Integer lruToolMaxSize; // LRU 容量
    protected List<ToolDefinition> loadedTools; // 前端已加载工具定义（回传续用）
    protected String truthContent; // 关键事实内容

    protected String md5; // 附件 MD5 校验
    protected String fileUrl; // 附件访问凭据
    protected boolean parsedText; // 下载解析后的纯文本
    protected boolean enableVisionImage; // 图片视觉输入开关
    protected String ttsContent; // 语音合成文本（千问 TTS）

    protected boolean enableLoopEngineering; // 循环工程开关
    protected Map<String, String> sessionRecordsMap; // 循环工程会话记录（四类）

    protected List<AsyncTaskItem> asyncTasks; // 异步任务状态列表（轮询回传）
}
```
