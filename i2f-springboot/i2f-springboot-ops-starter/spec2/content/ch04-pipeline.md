<p class="lead">一次完整的 OpenAI 对话，在前端-后端-LLM 三者之间经历 7 个阶段，形成一个端到端的 SSE 流水线。前端维护会话状态，后端仅做安全与转发。</p>

<div class="diagram-panel">
<svg viewBox="0 0 800 480" role="img" aria-label="核心对话流程 SSESequence 图">
<defs><marker id="ch4-ah" markerWidth="10" markerHeight="10" refX="8" refY="5" orient="auto"><path d="M0,0 L10,5 L0,10 Z" fill="#46607a"/></marker></defs>
<rect class="svg-node" x="30" y="10" width="80" height="36" rx="3" style="fill:var(--dark);stroke:var(--dark);"/><text class="svg-lbl-w" x="70" y="33" text-anchor="middle">Browser</text>
<rect class="svg-node svg-node-teal" x="210" y="10" width="100" height="36" rx="3"/><text class="svg-lbl" x="260" y="33" text-anchor="middle">Controller</text>
<rect class="svg-node" x="410" y="10" width="100" height="36" rx="3"/><text class="svg-lbl" x="460" y="33" text-anchor="middle">AiAgent</text>
<rect class="svg-node svg-node-acc" x="610" y="10" width="160" height="36" rx="3"/><text class="svg-lbl" x="690" y="33" text-anchor="middle">LLM (OpenAI兼容)</text>
<path class="svg-line" d="M70,62 L70,440" stroke-dasharray="4 6"/><path class="svg-line" d="M260,62 L260,440" stroke-dasharray="4 6"/><path class="svg-line" d="M460,62 L460,440" stroke-dasharray="4 6"/><path class="svg-line" d="M690,62 L690,440" stroke-dasharray="4 6"/>
<text class="svg-lbl-sm" x="74" y="78">① SM2/SM4 加密</text><path class="svg-line" d="M70,86 L260,86" marker-end="url(#ch4-ah)"/><text class="svg-lbl-sm" x="140" y="102">POST /ops/open-ai/stream</text>
<text class="svg-lbl-sm" x="264" y="126">② 解密·构建 Agent</text><path class="svg-line" d="M260,134 L460,134" marker-end="url(#ch4-ah)"/><text class="svg-lbl-sm" x="340" y="150">AiAgent.invoke(messages)</text>
<text class="svg-lbl-sm" x="464" y="174">③ 注入 Prompt 上下文</text><path class="svg-line" d="M460,182 L690,182" marker-end="url(#ch4-ah)"/><text class="svg-lbl-sm" x="560" y="198">POST /v1/chat/completions (stream)</text>
<text class="svg-lbl-sm" x="680" y="222" text-anchor="end">④ SSE 逐 Token 返回</text><path class="svg-line" d="M690,230 L460,230" marker-end="url(#ch4-ah)"/><text class="svg-lbl-sm" x="560" y="246">delta.content / delta.tool_calls</text>
<text class="svg-lbl-sm" x="464" y="270">⑤ 中继 SSE 到前端</text><path class="svg-line" d="M460,278 L260,278" marker-end="url(#ch4-ah)"/><text class="svg-lbl-sm" x="340" y="294">SseEmitter.send(chunk)</text>
<text class="svg-lbl-sm" x="264" y="318">⑥ 可选输出加密</text><path class="svg-line" d="M260,326 L70,326" marker-end="url(#ch4-ah)"/><text class="svg-lbl-sm" x="140" y="342">SM4 加密（可选）</text>
<text class="svg-lbl-sm" x="74" y="366">⑦ 前端消费·重新渲染</text><path class="svg-line" d="M70,374 L70,374"/><text class="svg-lbl-sm" x="74" y="390">打字机效果·工具审批弹窗·ReAct 循环</text>
<rect class="svg-node" x="30" y="410" width="130" height="36" rx="3"/><text class="svg-lbl" x="95" y="433" text-anchor="middle">SseEmitter 5min</text>
</svg>
<div class="dg-cap">端到端 SSE 流水线 — 后端仅做安全网关 + 中继，不保存会话状态</div>
</div>

<div class="step-list">
<div class="step"><div class="no"></div><div class="bd"><h4>① 加密发送</h4><p>前端生成随机 SM4 密钥，加密 JSON 请求体；SM2 公钥封装 SM4 密钥；SM3 摘要 + SM2 签名；时间戳防重放。POST 到 <code>POST /ops/open-ai/stream</code>。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>② 解密与验证</h4><p><code>OpenAiOpsController</code> 的 <code>doStreamCompletion</code> 方法：SM2 解密 SM4 密钥 → SM4 解密请求体 → 验证签名与时间戳（默认 5 分钟窗口）。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>③ 上下文注入</h4><p>构建 <code>AiAgent</code> 实例，注入系统提示词（角色 + 技能 + 事实 + 时间 + MCP 工具声明），合并用户消息与附件 XML。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>④ 工具检查 + 缓存</h4><p>检查最后一条助手消息是否包含 <code>tool_calls</code>；若所有工具结果已收集完毕，则缓存到 <code>toolMessageCache</code> 供后续复用。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>⑤ 模型调用</h4><p>调用 <code>AiAgent.invoke()</code> → <code>AiModel.chatStream()</code> → <code>HttpOpenAiAiModel</code> 发起 POST 到 LLM 的 <code>/v1/chat/completions</code>，启用 <code>stream: true</code>。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>⑥ SSE 中继</h4><p>后端逐 chunk 接收 LLM 的 SSE 流（<code>delta.content</code>、<code>delta.tool_calls</code>、<code>delta.reasoning_content</code>），包装为 <code>OpsOpenAiConsts</code> 定义的 12 种回显类型，通过 <code>SseEmitter.send()</code> 逐条中继给前端。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>⑦ 前端渲染与循环</h4><p>前端 <code>EventSource</code> 或 <code>fetch</code> 消费 SSE 流：打字机效果逐字渲染 <code>content</code>，独立折叠渲染 <code>reasoning</code>，检测 <code>tool_calls</code> 后弹出审批弹窗，获批后自动触发下一轮 ReAct 循环。</p></div></div>
</div>