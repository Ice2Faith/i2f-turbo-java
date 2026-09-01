<p class="lead">Re-Act（Reasoning + Acting）是本框架的<b>核心执行引擎</b>，由 <code>AiAgent</code> 类实现。并非由后端循环驱动，而是<b>前端递归驱动</b>——每次 SSE 流结束后，前端检测到 <code>tool_calls</code> 就自动发起下一轮请求，形成"推理→行动→观察→再推理"的闭环。</p>

<div class="diagram-panel">
<svg viewBox="0 0 780 420" role="img" aria-label="ReAct 循环流程图">
<defs><marker id="r5-ah" markerWidth="10" markerHeight="10" refX="8" refY="5" orient="auto"><path d="M0,0 L10,5 L0,10 Z" fill="#e8590c"/></marker></defs>
<rect class="svg-node svg-node-acc" x="280" y="16" width="220" height="52" rx="4"/><text class="svg-lbl" x="390" y="38" text-anchor="middle">前端发送消息列表</text><text class="svg-lbl-sm" x="390" y="56" text-anchor="middle">role: system/user/assistant/tool</text>
<path class="svg-line-acc" d="M390,68 L390,100" marker-end="url(#r5-ah)"/>
<rect class="svg-node svg-node-teal" x="270" y="104" width="240" height="52" rx="4"/><text class="svg-lbl" x="390" y="126" text-anchor="middle">AiAgent.invoke()</text><text class="svg-lbl-sm" x="390" y="144" text-anchor="middle">注入提示词 → 工具声明 → AiModel.chatStream()</text>
<path class="svg-line-acc" d="M390,156 L390,188" marker-end="url(#r5-ah)"/>
<rect class="svg-node" x="290" y="192" width="200" height="52" rx="4"/><text class="svg-lbl" x="390" y="214" text-anchor="middle">LLM 推理</text><text class="svg-lbl-sm" x="390" y="232" text-anchor="middle">文本回复 / tool_calls 决策</text>
<path class="svg-line" d="M390,244 L390,300" marker-end="url(#r5-ah)"/>
<rect class="svg-node" x="260" y="276" width="260" height="22" rx="11" style="fill:var(--orange-soft);stroke:var(--orange);"/><text class="svg-lbl-sm" x="390" y="291" text-anchor="middle" style="fill:var(--orange);">有 tool_calls？</text>
<path class="svg-line" d="M520,287 L700,287 L700,220 L502,220" marker-end="url(#r5-ah)"/><text class="svg-lbl-sm" x="720" y="260" style="fill:var(--red);">否 → 结束</text>
<path class="svg-line" d="M260,287 L60,287 L60,220 L278,220" marker-end="url(#r5-ah)"/><text class="svg-lbl-sm" x="40" y="260" style="fill:var(--green);">是 → 继续</text>
<rect class="svg-node svg-node-green" x="270" y="316" width="240" height="52" rx="4"/><text class="svg-lbl" x="390" y="338" text-anchor="middle">工具契约并行执行</text><text class="svg-lbl-sm" x="390" y="356" text-anchor="middle">CountDownLatch 并发 · HITL 审批</text>
<path class="svg-line-acc" d="M390,368 L390,400" marker-end="url(#r5-ah)"/>
<text class="svg-lbl-sm" x="390" y="416" text-anchor="middle">结果注入消息列表，前端递归触发下一轮</text>
</svg>
<div class="dg-cap">Re-Act 循环 — 由前端递归驱动，非后端循环</div>
</div>

<div class="panel">
<div class="panel-title">前端递归驱动 vs 后端循环驱动</div>
<p class="body" style="font-size:13.5px;">传统 Agent 框架（如 LangChain AgentExecutor）在服务端内部维护一个 while 循环，直到模型不再输出 tool_calls 才返回最终结果。这种方案的问题在于：</p>
<ul style="list-style:disc;margin-left:20px;font-size:13.5px;color:var(--ink-soft);">
<li><b>长连接风险</b>：多轮工具调用可能需要数分钟，HTTP 连接超时、用户关闭页面等问题难以处理。</li>
<li><b>HITL 无法介入</b>：循环在服务端内部，用户无法在中间步骤审批或干预。</li>
<li><b>无法水平扩展</b>：有状态循环绑定了服务实例，无法做负载均衡。</li>
</ul>
<p class="body" style="font-size:13.5px;margin-top:12px;">本框架的"前端递归"方案：每次 <code>directSendMessage()</code> 调用只做一次 LLM 请求，SSE 流结束后，前端检查 <code>hasToolCalls</code> 标志，若为 true 则自动递归调用 <code>directSendMessage()</code> 发起下一轮。每轮请求相互独立，服务端无状态。</p>
</div>

<div class="callout" style="--c:#e8590c;">
<div class="co-title">工具执行的三层并发模型</div>
<p><b>第1层</b>：前端 <code>AbortController</code> 中断 SSE 流，防止残留输出干扰。</p>
<p><b>第2层</b>：后端 <code>CountDownLatch</code> 实现工具契约并行执行，所有工具调用同时发起，等待全部完成。</p>
<p><b>第3层</b>：单个工具内部可自行实现并发（如 SQL 工具对多个数据库并行查询）。</p>
<p style="margin-top:8px;"><b>错误兜底</b>：工具执行异常不中断 ReAct 循环——异常信息以 <code>tool_call_id</code> 对号入座写回消息列表，模型在下一轮看到"工具执行失败"后可自行决定重试、换方案或放弃。</p>
</div>