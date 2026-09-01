<p class="lead">Spring Boot 自动装配是"一个依赖即一个工作台"理念的基石。引入 <code>i2f-springboot-ops-starter</code> 后，Spring Boot 自动配置机制将整个 OpenAI 子系统注入应用。</p>

<div class="panel">
<div class="panel-title">自动装配组件矩阵</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>配置类</th><th>职责</th><th>条件</th></tr></thead>
<tbody>
<tr><td><code>OpenAiOpsWebAutoConfiguration</code></td><td>注册 <code>OpenAiOpsController</code> 和 <code>QwenTtsOpsController</code></td><td><code>@ConditionalOnWebApplication</code></td></tr>
<tr><td><code>SpringContextToolAutoConfiguration</code></td><td>扫描 <code>@Tool</code> 注解，注册所有工具 Bean</td><td>始终装配</td></tr>
<tr><td><code>SkillAutoConfiguration</code></td><td>启动 30 秒热扫描调度器</td><td><code>i2f.ops.openai.skill.enabled=true</code></td></tr>
<tr><td><code>RagAutoConfiguration</code></td><td>初始化 Embedding 服务 + SQLite 向量存储</td><td><code>i2f.ops.openai.rag.enabled=true</code></td></tr>
<tr><td><code>GroovyTools</code> (条件 Bean)</td><td>注册 Groovy 脚本执行工具</td><td><code>@ConditionalOnClass(GroovyShell.class)</code></td></tr>
<tr><td><code>SQL 校验器</code> (条件 Bean)</td><td>注册 SQL AST 安全校验器</td><td><code>@ConditionalOnClass(JSqlParser.class)</code></td></tr>
</tbody>
</table>
</div>
</div>

<div class="panel">
<div class="panel-title">关键配置属性</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>属性</th><th>默认值</th><th>说明</th></tr></thead>
<tbody>
<tr><td><code>i2f.ops.openai.base-url</code></td><td>—</td><td>LLM 服务 Base URL</td></tr>
<tr><td><code>i2f.ops.openai.api-key</code></td><td>—</td><td>LLM 服务 API Key</td></tr>
<tr><td><code>i2f.ops.openai.model</code></td><td>—</td><td>默认模型名</td></tr>
<tr><td><code>i2f.ops.openai.skill.enabled</code></td><td>true</td><td>是否启用技能系统</td></tr>
<tr><td><code>i2f.ops.openai.skill.base-path</code></td><td>skills/</td><td>技能目录路径</td></tr>
<tr><td><code>i2f.ops.openai.rag.enabled</code></td><td>true</td><td>是否启用 RAG</td></tr>
<tr><td><code>i2f.ops.openai.rag.embedding-model</code></td><td>—</td><td>Embedding 模型名</td></tr>
<tr><td><code>i2f.ops.openai.crypto.enabled</code></td><td>true</td><td>是否启用国密加密</td></tr>
<tr><td><code>i2f.ops.openai.crypto.timestamp-window</code></td><td>300</td><td>时间戳防重放窗口（秒）</td></tr>
</tbody>
</table>
</div>
</div>

<div class="callout" style="--c:#2b8a3e;">
<div class="co-title">provided + optional 依赖策略</div>
<p>大量第三方依赖以 <code>&lt;scope&gt;provided&lt;/scope&gt;</code> 或 <code>&lt;optional&gt;true&lt;/optional&gt;</code> 声明。宿主应用引入 Starter 后，不会强制传递这些依赖——功能随 classpath 存在与否自动激活或静默降级。例如：</p>
<ul style="list-style:disc;margin-left:20px;font-size:13.5px;color:var(--ink-soft);">
<li>classpath 中有 <code>groovy-all</code> → Groovy 脚本执行工具自动激活</li>
<li>classpath 中有 <code>jsqlparser</code> → SQL AST 安全校验自动激活</li>
<li>classpath 中无则静默跳过，不影响其他功能</li>
</ul>
</div>