<p class="lead">整个 AI 工具链遵循 i2f-turbo-java 一以贯之的"简练、独立、单一、易用"哲学，并在 AI 领域落地为四条核心原则。</p>

<div class="card-grid">
<div class="card" style="--c:#e8590c;"><span class="idx">壹</span><h3>平台无关化</h3><div class="tagline">Platform Agnostic</div><p>标准层 <code>i2f-ai-std</code> 不依赖任何 AI 平台 SDK（OpenAI SDK / LangChain4j 等），仅依赖项目内部基础模块。上层可自由切换 4 种 <code>AiModel</code> 实现：自研零依赖 HTTP 实现、OpenAI 官方 SDK、阿里 DashScope、LangChain4j。</p></div>
<div class="card" style="--c:#0b7285;"><span class="idx">贰</span><h3>依赖无关化</h3><div class="tagline">Dependency Free</div><p>协议实现层 <code>i2f-ai-rest-openai</code> 使用项目自有 HTTP 客户端 <code>i2f-network</code>，零第三方 AI/HTTP 依赖；Ops 层则复用 Spring 的 <code>RestTemplate</code> 作为 SSE 传输底座。</p></div>
<div class="card" style="--c:#2b8a3e;"><span class="idx">叁</span><h3>接口-实现分离</h3><div class="tagline">std / impl Split</div><p><code>i2f-ai-std</code> 定义全部接口与标准（AiModel / ToolManager / RagWorker / McpToolProvider），实现散布于各 extension 模块。依赖单向流动，杜绝循环依赖。</p></div>
<div class="card" style="--c:#e67700;"><span class="idx">肆</span><h3>胖客户端 + HITL</h3><div class="tagline">Fat Client · Human In The Loop</div><p>需要用户参与（工具授权、危险操作把关）的场景选择<b>胖客户端</b>：状态维护在浏览器，服务端无状态、可水平扩展。全自动化场景（Dify / ComfyUI 式）才需要胖服务器——本框架明确选择了前者。</p></div>
</div>

<div class="callout" style="--c:#e8590c;">
<div class="co-title">为什么是"消息列表即一切"？</div>
<p>多轮对话中，模型会重新读取整个对话列表——这就是模型"记忆"的最初形式。框架源码用 <code>[1u] → [2ac] → [3t] → [4a]</code> 记号法描述一轮 ReAct：<b>1u</b> 第一条用户消息；<b>2ac</b> 第二条助手消息（c = 携带 tool_calls 契约）；<b>3t</b> 第三条工具消息；<b>4a</b> 最终归纳答复。第一轮调用仅携带 <code>[1u]</code>（用户触发），第二轮携带 <code>[1u, 2ac, 3t]</code>（客户端自动触发）——客户端必须维护复杂状态，这正是胖客户端存在的理由。</p>
</div>

<div class="panel-title">AI 工程范式全景 — 15 种范式 · 6 层架构</div>
<p class="body" style="font-size:13.5px;">本模块以一个 OpenAI 兼容的胖客户端对话系统为载体，完整实践了当前 AI Agent 工程的主流范式。这些范式并非孤立存在，而是<b>层层嵌套、相互协作</b>，共同构成功能完备的 Agent 系统：</p>
<div class="diagram-panel" style="margin-top:14px;">
<svg viewBox="0 0 940 386" role="img" aria-label="AI 工程范式六层架构图">
<rect class="svg-node" x="20" y="16" width="900" height="52" rx="3" style="stroke:#e67700;"/><rect x="20" y="16" width="8" height="52" fill="#e67700"/><text class="svg-lbl" x="44" y="38" style="fill:#e67700;">引导层</text><text class="svg-lbl-sm" x="44" y="56">Prompt Engineering · Multi-Agent</text><text class="svg-lbl-sm" x="330" y="38">20 角色预设切换人格 · 技能/MCP/事实动态注入</text><text class="svg-lbl-sm" x="330" y="56">30% 概率重注入防遗忘 · 时间感知 · 附件 XML 标签追加</text>
<rect class="svg-node" x="20" y="78" width="900" height="52" rx="3" style="stroke:#1971c2;"/><rect x="20" y="78" width="8" height="52" fill="#1971c2"/><text class="svg-lbl" x="44" y="100" style="fill:#1971c2;">执行层</text><text class="svg-lbl-sm" x="44" y="118">ReAct · Loop Engineering</text><text class="svg-lbl-sm" x="330" y="100">推理-行动循环 · 前端递归驱动 directSendMessage</text><text class="svg-lbl-sm" x="330" y="118">CountDownLatch 并发执行 · AbortController 中断 · 错误兜底不中断循环</text>
<rect class="svg-node" x="20" y="140" width="900" height="52" rx="3" style="stroke:#c92a2a;"/><rect x="20" y="140" width="8" height="52" fill="#c92a2a"/><text class="svg-lbl" x="44" y="162" style="fill:#c92a2a;">控制层</text><text class="svg-lbl-sm" x="44" y="180">HITL · Tool Tags &amp; Auto-Approval</text><text class="svg-lbl-sm" x="330" y="162">工具审批弹窗 · 逐个/全部允许/拒绝 + 拒绝原因反馈</text><text class="svg-lbl-sm" x="330" y="180">多维语义标签 · autoApprovalToolTags 标签自动审批</text>
<rect class="svg-node" x="20" y="202" width="900" height="52" rx="3" style="stroke:#2b8a3e;"/><rect x="20" y="202" width="8" height="52" fill="#2b8a3e"/><text class="svg-lbl" x="44" y="224" style="fill:#2b8a3e;">能力层</text><text class="svg-lbl-sm" x="44" y="242">MCP · RAG · Skills · A2A</text><text class="svg-lbl-sm" x="330" y="224">MCP 动态发现 + LRU 淘汰 · SQLite 向量检索 · SKILL.md 文档技能</text><text class="svg-lbl-sm" x="330" y="242">A2A Agent 嵌套调用（LLM 作为工具 · 独立上下文）</text>
<rect class="svg-node" x="20" y="264" width="900" height="52" rx="3" style="stroke:#862e9c;"/><rect x="20" y="264" width="8" height="52" fill="#862e9c"/><text class="svg-lbl" x="44" y="286" style="fill:#862e9c;">记忆层</text><text class="svg-lbl-sm" x="44" y="304">Truth · Memory · Context Window</text><text class="svg-lbl-sm" x="330" y="286">Truth 会话级事实（整体替换）· Memory 用户级向量记忆（Bucket 桶隔离）</text><text class="svg-lbl-sm" x="330" y="304">maxHistoryCount 截断 · 角色消息始终保留 · LRU 控制工具声明占用</text>
<rect class="svg-node" x="20" y="326" width="900" height="52" rx="3" style="stroke:#e8590c;"/><rect x="20" y="326" width="8" height="52" fill="#e8590c"/><text class="svg-lbl" x="44" y="348" style="fill:#e8590c;">基础层</text><text class="svg-lbl-sm" x="44" y="366">Harness Eng. · Fat Client · SSE Streaming</text><text class="svg-lbl-sm" x="330" y="348">3440 行单文件 SPA 状态驱动 · 后端单轮无状态可水平扩展</text><text class="svg-lbl-sm" x="330" y="366">SSE 逐 Token 流式输出 · SM2/SM4 国密加密传输</text>
</svg>
<div class="dg-cap">范式六层架构 — 执行/控制/能力/记忆/基础/引导层层嵌套协作</div>
</div>