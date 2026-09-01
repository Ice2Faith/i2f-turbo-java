<p class="lead">前端是<b>浏览器里的 Agent 状态机</b>——一个约 3440 行的单文件 SPA，维护会话列表、消息历史、工具审批与功能开关，将 Agent 状态完全搬到浏览器一侧。</p>

<div class="panel">
<div class="panel-title">前端架构</div>
<div class="card-grid">
<div class="card" style="--c:#e8590c;"><span class="idx">1</span><h3>状态驱动</h3><div class="tagline">State-Driven</div><p>全局状态管理会话列表、消息历史、功能开关、供应商连接信息、审批队列。所有 UI 变化均由状态变更驱动。</p></div>
<div class="card" style="--c:#0b7285;"><span class="idx">2</span><h3>SSE 消费</h3><div class="tagline">ReadableStream</div><p>使用 <code>fetch</code> 的 <code>ReadableStream</code> 逐行消费 SSE 流，解析 12 种回显类型，路由到不同渲染逻辑。</p></div>
<div class="card" style="--c:#2b8a3e;"><span class="idx">3</span><h3>打字机效果</h3><div class="tagline">Typewriter</div><p><code>reply_message_delta</code> 事件逐字追加到消息气泡，模拟打字机效果；<code>reasoning_content</code> 独立渲染到可折叠的思考面板。</p></div>
<div class="card" style="--c:#1971c2;"><span class="idx">4</span><h3>HITL 审批弹窗</h3><div class="tagline">Human In The Loop</div><p>检测到 <code>tool_calls</code> 后弹出审批弹窗：逐个/全部/拒绝+原因。获批后自动触发下一轮 ReAct，被拒绝则反馈给模型。</p></div>
<div class="card" style="--c:#e67700;"><span class="idx">5</span><h3>国密加密</h3><div class="tagline">SM2/SM4 in JS</div><p>前端使用纯 JavaScript 实现 SM2/SM3/SM4 算法，无需后端代理即可完成加密、签名、摘要计算。</p></div>
<div class="card" style="--c:#c92a2a;"><span class="idx">6</span><h3>资源内嵌</h3><div class="tagline">JAR Embedded</div><p>前端全部资源（HTML/CSS/JS/字体/图片）打包在 JAR 的 <code>META-INF/resources/</code> 下，Spring Boot 自动映射为静态资源，零额外部署。</p></div>
</div>
</div>

<div class="panel">
<div class="panel-title">前端技术栈</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>技术</th><th>用途</th></tr></thead>
<tbody>
<tr><td>原生 JavaScript (ES6+)</td><td>无框架 SP A，3440 行单文件</td></tr>
<tr><td>CSS3 + CSS Variables</td><td>主题系统、暗色模式支持</td></tr>
<tr><td>Fetch API + ReadableStream</td><td>SSE 流式消费</td></tr>
<tr><td>SM2/SM3/SM4 (纯 JS 实现)</td><td>国密加密、签名、摘要</td></tr>
<tr><td>AbortController</td><td>中断 SSE 流</td></tr>
<tr><td>localStorage</td><td>会话持久化、供应商配置</td></tr>
</tbody>
</table>
</div>
</div>