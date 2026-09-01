<p class="lead">除了核心的 ReAct 循环和工具体系，框架还提供多项高级特性，让 AI 对话长出真正的"记忆"和"手脚"。</p>

<div class="card-grid">
<div class="card" style="--c:#e8590c;"><span class="idx">A</span><h3>角色系统 (Role)</h3><div class="tagline">20 个预设人格</div><p>20 个预设角色快速切换 Agent 人格：运维专家、代码助手、SQL 专家、安全顾问、架构师等。角色定义包含 system 提示词、自动审批标签、建议模型等。</p></div>
<div class="card" style="--c:#0b7285;"><span class="idx">B</span><h3>多模态输入</h3><div class="tagline">图片 · 文件 · 附件</div><p>支持图片上传（base64 编码注入 content 数组）、文件上传（通过附件 XML 标签追加到用户消息末尾）、粘贴图片。</p></div>
<div class="card" style="--c:#2b8a3e;"><span class="idx">C</span><h3>循环工程五步法</h3><div class="tagline">Loop Engineering</div><p>内置 <code>LoopEngineeringTools</code> 提供五步工作流提示词：需求分析 → 方案设计 → 实现执行 → 结果验证 → 记录归档。模型可自主调用获取工作流指引。</p></div>
<div class="card" style="--c:#1971c2;"><span class="idx">D</span><h3>异步任务调度</h3><div class="tagline">Async Task</div><p><code>AsyncTaskDispatcher</code> 支持工具以异步方式执行（如长时间 SQL 查询），前端通过 <code>async_task_status</code> 轮询任务状态，避免阻塞 SSE 连接。</p></div>
<div class="card" style="--c:#e67700;"><span class="idx">E</span><h3>语音合成 (TTS)</h3><div class="tagline">Qwen TTS</div><p><code>QwenAudioTtsWebSocket</code> 通过 WebSocket 连接千问语音合成服务，将 AI 回复转为语音输出，前端音频播放器实时播放。</p></div>
<div class="card" style="--c:#c92a2a;"><span class="idx">F</span><h3>A2A 嵌套调用</h3><div class="tagline">Agent as Tool</div><p>支持将一个 Agent 作为另一个 Agent 的工具：被嵌套 Agent 拥有独立上下文，外层 Agent 通过 <code>agent_call</code> 工具调用子 Agent，结果返回后继续推理。</p></div>
</div>

<div class="panel">
<div class="panel-title">提示词注入策略</div>
<p class="body" style="font-size:13.5px;">系统提示词并非固定不变，而是<b>动态组装</b>：</p>
<ul style="list-style:disc;margin-left:20px;font-size:13.5px;color:var(--ink-soft);">
<li><b>角色提示词</b>：从 20 个预设中选择，定义 Agent 人设与行为边界。</li>
<li><b>技能提示词</b>：已激活技能的系统提示词片段，按需追加。</li>
<li><b>事实注入</b>：Truth 记忆中的当前事实，如 <code>Current Facts: 用户名=张三, 项目=demo</code>。</li>
<li><b>时间感知</b>：当前日期时间，帮助模型理解时间上下文。</li>
<li><b>30% 概率重注入</b>：每轮对话有 30% 概率重新注入完整提示词，防止长对话中模型遗忘关键指令。</li>
<li><b>附件 XML</b>：用户上传的文件以 <code>&lt;attachment&gt;...&lt;/attachment&gt;</code> XML 标签追加到用户消息末尾。</li>
</ul>
</div>