<p class="lead">框架的扩展性设计遵循"添加 → 注解 → 注入"三步法，接入一个新工具只需极少的代码量。</p>

<div class="panel">
<div class="panel-title">三步接入一个新工具</div>
<div class="step-list">
<div class="step"><div class="no"></div><div class="bd"><h4>创建工具类</h4><p>在 <code>tool/impl/</code> 包下创建新类，使用 <code>@Tool</code> 注解标注方法：</p>
<pre class="hljs"><code>@Tool(name = "weather_query", description = "查询指定城市的天气",
      params = {@ToolParam(name = "city", type = "string", description = "城市名称", required = true)},
      tags = {"network", "utility"})
public String weatherQuery(String city) {
    return WeatherService.query(city);
}</code></pre></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>注册为 Spring Bean</h4><p>在工具类上添加 <code>@Component</code> 注解，或在 <code>SpringContextToolAutoConfiguration</code> 中显式声明 Bean。框架会自动扫描所有带 <code>@Tool</code> 注解的 Bean。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>注入 ToolManager</h4><p>无需手动操作——<code>SpringContextToolAutoConfiguration</code> 在应用启动时自动收集所有 <code>@Tool</code> 方法，注册到 <code>ToolManager</code>，后续对话中自动注入工具声明。</p></div></div>
</div>
</div>

<div class="panel">
<div class="panel-title">扩展点总览</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>扩展点</th><th>接口/注解</th><th>说明</th></tr></thead>
<tbody>
<tr><td>自定义工具</td><td><code>@Tool</code></td><td>声明式工具定义，自动生成 JSON Schema</td></tr>
<tr><td>AI 模型实现</td><td><code>AiModel</code></td><td>实现此接口即可接入新的 LLM 平台</td></tr>
<tr><td>工具管理器</td><td><code>ToolManager</code></td><td>自定义工具发现与注册逻辑</td></tr>
<tr><td>RAG Worker</td><td><code>RagWorker</code></td><td>自定义文档读取器或向量存储</td></tr>
<tr><td>MCP Provider</td><td><code>McpToolProvider</code></td><td>接入新的 MCP 工具供应商</td></tr>
<tr><td>技能扫描器</td><td><code>SkillsHelper</code></td><td>自定义技能发现与激活逻辑</td></tr>
<tr><td>SSE 回显类型</td><td><code>OpsOpenAiConsts</code></td><td>新增自定义回显类型</td></tr>
</tbody>
</table>
</div>
</div>

<div class="callout" style="--c:#e8590c;">
<div class="co-title">从 i2f-ai-std 到 i2f-springboot-ops-starter — 完整的工具链</div>
<p>本框架的 AI 能力并非孤立存在，而是依托 i2f-turbo-java 整体工具链：网络层 <code>i2f-network</code>、国密层 <code>i2f-sm-crypto</code>、反射层 <code>i2f-reflect</code>、代理层 <code>i2f-proxy</code>、序列化层 <code>i2f-serialize-std</code>——这些基础模块共同构成了一个完整的、可独立运行的 AI Agent 运行时。</p>
<p>如果你需要在自己的项目中使用这些能力，推荐直接从 <code>i2f-ai-std</code> 开始，按需组合各模块，而非依赖整个 Starter。</p>
</div>