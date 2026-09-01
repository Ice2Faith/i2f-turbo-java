<p class="lead">消息模型是整个框架的<b>血液</b>。前端通过 <code>OpenAiMessageVo</code> 多态载体传输消息，后端通过 <code>OpsOpenAiConsts</code> 定义 12 种回显类型，SSE 流中的每一帧都带有明确的类型标签。</p>

<div class="panel">
<div class="panel-title">OpenAiMessageVo — 多态消息载体</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>字段</th><th>类型</th><th>说明</th></tr></thead>
<tbody>
<tr><td><code>role</code></td><td>String</td><td>system / user / assistant / tool</td></tr>
<tr><td><code>content</code></td><td>String | Object</td><td>文本内容或多模态数组</td></tr>
<tr><td><code>tool_calls</code></td><td>List&lt;ToolCallVo&gt;</td><td>助手请求的工具调用列表</td></tr>
<tr><td><code>tool_call_id</code></td><td>String</td><td>工具消息关联的工具调用 ID</td></tr>
<tr><td><code>name</code></td><td>String</td><td>工具名称/可选参与者名称</td></tr>
<tr><td><code>reasoning_content</code></td><td>String</td><td>DeepSeek-R1 / o1 等模型的思考过程</td></tr>
<tr><td><code>prefix</code></td><td>Boolean</td><td>推理模型 prefix 模式（assistant 角色）</td></tr>
</tbody>
</table>
</div>
</div>

<div class="panel">
<div class="panel-title">OpsOpenAiConsts — 12 种 SSE 回显类型</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>常量</th><th>值</th><th>含义</th></tr></thead>
<tbody>
<tr><td><code>REPLY_MESSAGE</code></td><td>reply_message</td><td>普通回复消息</td></tr>
<tr><td><code>REPLY_MESSAGE_ID</code></td><td>reply_message_id</td><td>回复消息 ID</td></tr>
<tr><td><code>REPLY_MESSAGE_DELTA</code></td><td>reply_message_delta</td><td>回复消息增量（流式文本）</td></tr>
<tr><td><code>REPLY_MESSAGE_FINISH</code></td><td>reply_message_finish</td><td>回复消息完成</td></tr>
<tr><td><code>REPLY_MESSAGE_REASONING</code></td><td>reply_message_reasoning</td><td>思考过程增量</td></tr>
<tr><td><code>REPLY_MESSAGE_REASONING_FINISH</code></td><td>reply_message_reasoning_finish</td><td>思考过程完成</td></tr>
<tr><td><code>REPLY_MESSAGE_TOOL_CALLS</code></td><td>reply_message_tool_calls</td><td>工具调用请求</td></tr>
<tr><td><code>REPLY_MESSAGE_TOOL_CALL_FINISH</code></td><td>reply_message_tool_call_finish</td><td>工具调用完成</td></tr>
<tr><td><code>REPLY_ERROR</code></td><td>reply_error</td><td>错误回显</td></tr>
<tr><td><code>REPLY_SYSTEM</code></td><td>reply_system</td><td>系统消息</td></tr>
<tr><td><code>REPLY_RECORD</code></td><td>reply_record</td><td>会话记录回显</td></tr>
<tr><td><code>REPLY_ASYNC_TASK</code></td><td>reply_async_task</td><td>异步任务状态</td></tr>
</tbody>
</table>
</div>
</div>

<div class="callout" style="--c:#1971c2;">
<div class="co-title">前端如何消费 SSE 帧</div>
<p>前端通过 <code>fetch</code> 的 <code>ReadableStream</code> 逐行读取 SSE 流，每行解析为 <code>data: { "type": "reply_message_delta", "content": "..." }</code>。根据 <code>type</code> 字段路由到不同的渲染逻辑：<code>reply_message_delta</code> 追加到打字机效果、<code>reply_message_reasoning</code> 追加到折叠的思考面板、<code>reply_message_tool_calls</code> 弹出 HITL 审批弹窗。</p>
</div>