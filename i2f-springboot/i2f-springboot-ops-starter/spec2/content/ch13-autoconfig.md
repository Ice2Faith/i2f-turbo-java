<p class="lead">整个 AI 子系统遵循 SpringBoot Starter 哲学：<b>引入依赖即自动装配</b>，全部能力默认开启，用 <code>@ConditionalOnExpression</code> 挂接配置开关，用 <code>@ConditionalOnMissingBean</code> 让渡自定义实现。</p>

<div class="panel-title">配置开关一览（默认全部为 true）</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>开关</th><th>装配类 / Bean</th><th>控制范围</th></tr></thead>
<tbody>
<tr><td><code>ai.tools.enable</code></td><td><code>SpringContextToolAutoConfiguration</code></td><td>工具体系总开关</td></tr>
<tr><td><code>ai.tools.mcp.app.enable</code></td><td><code>ContextAppMcpToolProvider</code></td><td>应用内 MCP 工具供应商</td></tr>
<tr><td><code>ai.tools.mcp.manager.enable</code></td><td><code>ContextMcpToolGatewayManager</code></td><td>MCP 网关 ToolManager</td></tr>
<tr><td><code>ai.tools.app.manager.enable</code></td><td><code>ContextAppToolManager</code></td><td>应用内 @Tool 扫描 ToolManager</td></tr>
<tr><td><code>ai.tools.mcp-gateway.enable</code></td><td><code>McpProviderTools</code></td><td>MCP 动态工具元工具</td></tr>
<tr><td><code>ai.skills.enable</code></td><td><code>SkillAutoConfiguration</code></td><td>技能系统 + 30 秒热扫描</td></tr>
<tr><td><code>ai.skills.tool.enable</code></td><td><code>SkillsTools</code></td><td>技能三件套工具</td></tr>
<tr><td><code>ai.rags.enable</code></td><td><code>RagAutoConfiguration</code></td><td>RAG 知识库全链路</td></tr>
<tr><td><code>ai.rags.memory.bucket.enable</code></td><td><code>BucketRagEmbeddingStore</code></td><td>记忆桶向量存储（SQLite 桶分区）</td></tr>
<tr><td><code>ai.rags.memory.enable</code></td><td><code>MemoryTools</code></td><td>记忆三件套工具（search / save / delete）</td></tr>
<tr><td><code>ai.tools.session-record.enable</code></td><td><code>SessionRecordTools</code></td><td>循环工程会话记录读写工具</td></tr>
<tr><td><code>ai.tools.groovy.enable</code></td><td><code>GroovyTools</code></td><td>Groovy 脚本执行工具（<b>默认关闭</b>，需 Groovy 依赖）</td></tr>
<tr><td><code>ai.tts.qwen.enable</code></td><td><code>QwenTtsOpsController</code></td><td>千问语音合成代理端点</td></tr>
</tbody>
</table>
</div>

<div class="two-col">
<div>
<div class="panel-title">装配策略</div>
<div class="step-list" style="margin-top:0;">
<div class="step">
<div class="no"></div>
<div class="bd"><h4>条件表达式门控</h4>
<p><code>@ConditionalOnExpression("${ai.xxx.enable:true}")</code> —— 默认开启，配置即关。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>缺失才装配</h4>
<p><code>@ConditionalOnMissingBean</code> —— 用户自实现同类型 Bean 时自动让渡，扩展不冲突。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>双管理器互斥</h4>
<p>MCP 网关与应用扫描两个 <code>ToolManager</code> 互斥装配，避免工具重复注册。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>启动即预热</h4>
<p>技能首轮全量扫描、RAG 后台线程加载文档，均异步完成，不阻塞应用启动。</p></div>
</div>
</div>
</div>
<div>
<div class="panel-title">RAG 可调参数（RagEmbeddingModelProperties）</div>
<div class="spec-table-wrap" style="margin-top:0;">
<table class="spec">
<thead><tr><th>属性</th><th>含义</th></tr></thead>
<tbody>
<tr><td><code>baseUrl / apiKey / model</code></td><td>Embedding 服务接入点</td></tr>
<tr><td><code>dimension</code></td><td>向量维度（SQLite 存储建表依据）</td></tr>
<tr><td><code>docsPath</code></td><td>文档投放目录</td></tr>
<tr><td><code>maxSegmentSizeInChars</code></td><td>切分器最大分段长度</td></tr>
<tr><td><code>maxOverlapRate</code></td><td>分段重叠率</td></tr>
<tr><td><code>docsEmbedBatchSize</code></td><td>入库批量大小</td></tr>
<tr><td><code>enableMarkitdownDocReader</code></td><td>Office 文档读取器开关</td></tr>
<tr><td><code>enableEasyocrDocReader</code></td><td>图片 / PDF-OCR 读取器开关</td></tr>
<tr><td><code>enablePandocDocReader</code></td><td>Pandoc 转换读取器开关</td></tr>
</tbody>
</table>
</div>
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
