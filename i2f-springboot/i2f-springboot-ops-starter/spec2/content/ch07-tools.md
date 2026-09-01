<p class="lead">工具体系是 Agent 的<b>四肢</b>。18 个内置工具类通过 <code>@Tool</code> 注解声明式注册，自动生成 JSON Schema 注入对话上下文，经 HITL 审批后并行执行。</p>

<div class="panel">
<div class="panel-title">@Tool 注解 — 声明式工具定义</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>属性</th><th>说明</th></tr></thead>
<tbody>
<tr><td><code>name</code></td><td>工具名称（LLM 看到的函数名），如 <code>mouse_click</code></td></tr>
<tr><td><code>description</code></td><td>工具描述（LLM 决定何时调用的依据）</td></tr>
<tr><td><code>params</code></td><td>参数 Schema 数组，每个参数定义 name/type/description/required/enum</td></tr>
<tr><td><code>tags</code></td><td>语义标签数组，如 <code>{"system", "dangerous"}</code>，用于自动审批策略</td></tr>
</tbody>
</table>
</div>
</div>

<div class="panel">
<div class="panel-title">17 个内置工具类一览</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>工具类</th><th>工具名</th><th>功能</th><th class="tc">标签</th></tr></thead>
<tbody>
<tr><td><code>ComputerTools</code></td><td><code>mouse_click</code> / <code>keyboard_type</code> / <code>screen_screenshot</code></td><td>桌面自动化</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>DateTimeTools</code></td><td><code>get_current_time</code></td><td>获取当前时间</td><td><span class="tag t-b">system</span></td></tr>
<tr><td><code>FileSystemTools</code></td><td><code>file_read</code> / <code>file_write</code> / <code>file_list</code> / <code>file_delete</code></td><td>文件系统操作</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>HttpClientTools</code></td><td><code>http_request</code></td><td>HTTP 请求</td><td><span class="tag t-o">network</span></td></tr>
<tr><td><code>BrowserTools</code></td><td><code>browser_navigate</code> / <code>browser_screenshot</code></td><td>浏览器自动化</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>DatabaseTools</code></td><td><code>sql_query</code> / <code>sql_execute</code></td><td>数据库操作</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>ShellTools</code></td><td><code>shell_execute</code></td><td>Shell 命令执行</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>GroovyTools</code></td><td><code>groovy_run_script</code></td><td>Groovy 脚本执行（条件装配）</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>MathTools</code></td><td><code>math_calculate</code></td><td>数学计算</td><td><span class="tag t-b">system</span></td></tr>
<tr><td><code>NetworkTools</code></td><td><code>ping</code> / <code>nslookup</code></td><td>网络诊断</td><td><span class="tag t-o">network</span></td></tr>
<tr><td><code>ProcessTools</code></td><td><code>process_list</code> / <code>process_kill</code></td><td>进程管理</td><td><span class="tag t-r">dangerous</span></td></tr>
<tr><td><code>SkillTools</code></td><td><code>skill_list</code> / <code>skill_execute</code></td><td>技能系统操作</td><td><span class="tag t-g">skill</span></td></tr>
<tr><td><code>RagTools</code></td><td><code>rag_search</code> / <code>rag_import</code></td><td>知识库操作</td><td><span class="tag t-g">rag</span></td></tr>
<tr><td><code>McpTools</code></td><td><code>mcp_list_providers</code> / <code>mcp_list_tools</code> / <code>mcp_load_tool</code></td><td>MCP 工具发现</td><td><span class="tag t-t">mcp</span></td></tr>
<tr><td><code>LoopEngineeringTools</code></td><td><code>loop_engineering_prompt</code></td><td>循环工程五步工作流提示词</td><td><span class="tag t-b">system</span></td></tr>
<tr><td><code>SessionRecordTools</code></td><td><code>session_record_read</code> / <code>session_record_write</code></td><td>循环工程会话记录</td><td><span class="tag t-b">system</span></td></tr>
<tr><td><code>AsyncTaskTools</code></td><td><code>async_task_status</code></td><td>异步任务状态查询</td><td><span class="tag t-b">system</span></td></tr>
</tbody>
</table>
</div>
</div>

<div class="callout" style="--c:#c92a2a;">
<div class="co-title">HITL 工具审批：autoApprovalToolTags 多维标签策略</div>
<p>前端支持 <code>autoApprovalToolTags</code> 配置，是一个字符串数组，如 <code>["system"]</code>。当工具调用涉及的 <b>所有</b> 标签均包含在此配置中时，自动批准跳过人工审批；否则弹出审批弹窗，用户可逐个/全部允许/拒绝，拒绝时可附带原因反馈给模型。</p>
<p>标签是多维度的：一个工具可同时拥有 <code>["system", "dangerous"]</code> 标签。若 <code>autoApprovalToolTags</code> 仅包含 <code>["system"]</code>，则此类工具仍需人工审批，因为 <code>dangerous</code> 标签未在白名单中。</p>
</div>