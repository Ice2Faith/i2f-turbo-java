<p class="lead">标准层 <code>i2f.ai.std.agent.AiAgent</code>（471 行）实现了完整的 <b>Reasoning + Acting</b> 循环。Ops 子系统虽由前端驱动循环节奏，但后端 <code>i2f-ai-std</code> 提供的这套 Agent 引擎是同一套设计哲学的程序化表达，二者共享工具解析（<code>ToolRawHelper</code>）与消息模型。</p>

<div class="diagram-panel">

```svg
assets/diagrams/ch05-react-loop.svg
```


<div class="dg-cap">AiAgent Re-Act 循环 — 直至模型返回 STOP 或触发护栏</div>
</div>

<div class="two-col">
<div>
<div class="panel-title">消息列表演化记号法</div>
<div class="pkg-tree"><span class="cm"># 一轮完整的工具调用对话</span>
[1u] <span class="cm"># 用户发送消息</span>
↓ 注入工具声明
[1u] → LLM 推理
↓ 返回调用契约
[1u, 2ac] <span class="cm"># 2ac = 助手消息 + tool_calls</span>
↓ 执行工具 · 反馈结果
[1u, 2ac, 3t] <span class="cm"># 3t = 工具消息</span>
↓ LLM 继续推理（客户端自动触发）
[1u, 2ac, 3t, 4a] <span class="cm"># 4a = 最终归纳答复</span></div>
<p class="body" style="font-size:13px;margin-top:10px;">两轮 LLM 调用：第一轮仅携带 <code>[1u]</code> 由用户触发；第二轮携带三条消息由客户端自动触发——这正是"客户端即 Agent 运行时"的含义。</p>
</div>
<div>
<div class="panel-title">AiAgentContext 护栏配置</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>配置项</th><th>默认</th><th>说明</th></tr></thead>
<tbody>
<tr><td><code>maxAllToolCallCount</code></td><td>100</td><td>全局工具调用上限</td></tr>
<tr><td><code>maxSingleToolCallCount</code></td><td>10</td><td>单工具调用上限</td></tr>
<tr><td><code>maxSingleToolSameArgumentFailureCount</code></td><td>2</td><td>同参数失败上限（防死磕）</td></tr>
<tr><td><code>maxKeepMessageCount</code></td><td>20</td><td>最大保留消息数</td></tr>
<tr><td><code>compressHistoryCount</code></td><td>16</td><td>触发 LLM 摘要压缩阈值</td></tr>
<tr><td><code>keepFirstUserMessage</code></td><td>true</td><td>截断时保留首条用户消息</td></tr>
<tr><td><code>enableParallelToolCall</code></td><td>true</td><td>并行执行工具契约</td></tr>
</tbody>
</table>
</div>
</div>
</div>

<div class="callout" style="--c:#2b8a3e;">
<div class="co-title">历史压缩策略（compressOrDropHistoryMessage）</div>
<p>消息数达到阈值时，将超量历史消息取出并追加一条 <code>"总结上述对话内容"</code> 的用户消息，单独请求 LLM 生成摘要回填——用一次廉价调用换取上下文瘦身；随后按 <code>maxKeepMessageCount</code> 硬截断，并可选保留首条用户消息以锚定对话主题。前端页面则以"截断会话历史线"分割线可视化这一过程。</p>
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
