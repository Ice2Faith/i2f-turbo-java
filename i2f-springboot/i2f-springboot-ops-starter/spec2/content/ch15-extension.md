<p class="lead">框架的扩展面刻意保持极小：加工具只需一个注解类，加技能只需一个 Markdown 文件，加知识只需把文档丢进目录。</p>

<div class="two-col">
<div>
<div class="panel-title">示例：新增一个天气工具</div>

```java
@Component
@Tools(tags = {AiTags.READONLY_VALUE})
public class WeatherTools {

    @Tool(description = "查询指定城市的实时天气")
    public String query_weather(
        @ToolParam(description = "城市名称，例如：北京") String city) {
        return weatherService.query(city);
    }
}
```

<div class="step-list" style="margin-top:0;">
<div class="step">
<div class="no"></div>
<div class="bd"><h4>编写 Bean</h4>
<p>如上：Spring 组件 + <code>@Tools</code> / <code>@Tool</code> / <code>@ToolParam</code> 注解。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>确认开关</h4>
<p><code>ai.tools.enable</code> 默认开启，<code>ContextAppToolManager</code> 启动时自动扫描收录。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>前端验证</h4>
<p>开启 <code>enableEchoRequestPayload</code>，在回显报文中确认工具定义已注入 <code>tools</code> 字段。</p></div>
</div>
</div>
</div>
<div>
<div class="panel-title">扩展面速查</div>
<div class="spec-table-wrap" style="margin-top:0;">
<table class="spec">
<thead><tr><th>想做什么</th><th>怎么做</th></tr></thead>
<tbody>
<tr><td>新增工具</td><td>编写 <code>@Tool</code> 注解的 Spring Bean</td></tr>
<tr><td>新增异步任务工具</td><td>实现 <code>AsyncTaskResolver</code> + <code>@Tool</code> 返回 <code>AsyncTaskMessage</code>，注册为 Spring Bean</td></tr>
<tr><td>新增技能</td><td>在 <code>skills/{name}/</code> 放置 <code>SKILL.md</code>，30 秒内生效</td></tr>
<tr><td>新增知识</td><td>文档投放到 <code>docsPath</code> 目录，重启后自动向量化</td></tr>
<tr><td>新增角色</td><td>编写 <code>roles/**/*.md</code> 并在 <code>role-config.json</code> 登记</td></tr>
<tr><td>自定义 ToolManager</td><td>自实现 Bean，<code>@ConditionalOnMissingBean</code> 自动让渡</td></tr>
<tr><td>接入 MCP 供应商</td><td>实现 <code>AbstractMcpToolGatewayManager</code> 生态的供应商</td></tr>
<tr><td>控制台菜单</td><td>经 <code>IOpsProvider</code> 注册菜单项挂载入口</td></tr>
</tbody>
</table>
</div>
<div class="callout" style="--c:#e67700;margin-top:16px;">
<div class="co-title">描述即接口 — 写给 LLM 的文档</div>
<p><code>description</code> 是 LLM 决定「要不要调、怎么调」的唯一依据。写工具描述要像写 API 文档：<b>能力边界、参数格式、返回示例、使用时机</b>，一样都不能少；危险操作务必打上 <code>HUMAN</code> 标签，把最终决定权留给人。</p>
</div>
</div>
</div>

<div class="panel-title">MCP 专属扩展 — 自定义传输层与客户端</div>
<div class="two-col" style="margin-top:0;">
<div>
<div class="step-list" style="margin-top:0;">
<div class="step">
<div class="no"></div>
<div class="bd"><h4>自定义 MCP Server 传输层</h4>
<p>参考 Netty 模式和 Spring Web MVC 模式的实现：创建新 AutoConfiguration 类，使用 <code>@ConditionalOnClass</code> 条件装配；实现请求处理器将 HTTP 请求转换为 <code>HttpSimpleMcpRequest</code>；调用 <code>HttpSimpleMcpServer.getTools()</code> 和 <code>callTool()</code> 完成业务逻辑；在 <code>META-INF/spring.factories</code> 中注册。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>自定义 MCP Client 传输</h4>
<p>客户端 HTTP 通信基于 <code>IRestClient</code> 接口，默认使用 <code>SpringWebRestClient</code>（基于 RestTemplate）。可按需替换为 <code>OkHttpRestClient</code> 或其他实现，通过 <code>HttpSimpleMcpClientToolProvider.setRestClient()</code> 注入。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>接入远程 MCP 供应商</h4>
<p>在 <code>application.yml</code> 中配置 <code>i2f.springboot.ai.mcp.client.simple.instances</code> 列表，每个实例指定 name / base-url / app-id / app-key，启动时自动注册为 <code>McpToolProvider</code> Bean，被 <code>ContextMcpToolGatewayManager</code> 聚合。</p></div>
</div>
</div>
</div>
<div>
<div class="callout" style="--c:#0b7285;margin-top:0;">
<div class="co-title">Server 端自动注册清单</div>
<p>三个自动配置类注册在 <code>META-INF/spring.factories</code>：<br>
<code>SpringAiMcpServerAutoConfiguration</code>（主配置）<br>
<code>SpringAiNettyMcpServerAutoConfiguration</code>（Netty 模式）<br>
<code>SpringAiSpringWebMcpServerAutoConfiguration</code>（Web MVC 模式）</p>
</div>
<div class="callout" style="--c:#e8590c;">
<div class="co-title">Client 端自动注册清单</div>
<p><code>SpringAiMcpClientAutoConfiguration</code> 实现 <code>BeanDefinitionRegistryPostProcessor</code>，按配置动态注册 <code>McpToolProvider</code> Bean 到 Spring 容器。</p>
</div>
<div class="callout" style="--c:#2b8a3e;">
<div class="co-title">双模式互斥与配置总览</div>
<p>所有配置开关默认启用，按需关闭。MCP Server 的 Spring Web MVC 与 Netty 模式可并存（独立端口）；Client 端每个实例独立注册；ToolManager 单机模式与网关模式互斥装配。详见第 13 章自动装配配置表。</p>
</div>
</div>
</div>

<div class="panel-title">AsyncTask 专属扩展 — 三步接入异步任务工具</div>
<div class="two-col" style="margin-top:0;">
<div>
<div class="step-list" style="margin-top:0;">
<div class="step">
<div class="no"></div>
<div class="bd"><h4>编写工具方法，返回 AsyncTaskMessage</h4>
<p>在 <code>@Tool</code> 注解方法中调用远程 API 创建异步任务，将返回的 taskId 封装为 <code>AsyncTaskItem</code>（设置 status=PENDING、type 为唯一标识），然后包装为 <code>AsyncTaskMessage</code> 返回。Controller 自动检测 <code>AsyncTaskMessage</code> 返回值并通过 <code>echo_async_tasks</code> 推送前端。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>实现 AsyncTaskResolver 接口</h4>
<p>实现 <code>support()</code> 方法按 <code>item.type</code> 匹配任务类型；实现 <code>resolve()</code> 方法调用远程 API 查询任务状态，更新 <code>item.status</code>、<code>item.result</code> 等字段。完成后将结果文件下载到临时文件目录，返回 <code>TmpFileTools.UploadTmpFileMetadata</code> 列表作为结果。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>注册为 Spring Bean</h4>
<p>将实现类标注 <code>@Component</code>，<code>AsyncTaskDispatcher</code> 自动发现并纳入调度。无需额外配置，前端轮询时自动路由到正确的 Resolver。</p></div>
</div>
</div>
</div>
<div>

```java
@Component
@Tools
public class ImageGenTools implements AsyncTaskResolver {
    public static final String TASK_TYPE = "image_gen";

    @Tool(description = "文生图，异步返回结果")
    public AsyncTaskMessage text_to_image(String content) {
        String taskId = remoteApi.createTask(content); // 提交远程任务
        AsyncTaskItem item = new AsyncTaskItem();
        item.setStatus(AsyncTaskItem.Status.PENDING);
        item.setType(TASK_TYPE);
        item.setTaskId(taskId);
        return new AsyncTaskMessage(item);
    }

    @Override
    public boolean support(AsyncTaskItem item, OpenAiMeta meta) {
        return TASK_TYPE.equals(item.getType());
    }

    @Override
    public AsyncTaskItem resolve(AsyncTaskItem item, OpenAiMeta meta) {
        Map<String, Object> resp = remoteApi.queryTask(item.getTaskId());
        if ("SUCCEEDED".equals(resp.get("status"))) {
            item.setStatus(AsyncTaskItem.Status.SUCCESS);
            item.setResult(downloadResultFiles(resp));
        }
        return item;
    }
}
```

</div>
</div>

<div class="two-col">
<div>
<div class="panel-title">启动要求与运行目录</div>
<div class="pkg-tree"><span class="cm"># 启动要求：JDK 1.8+ · Spring Boot 2.x · 网络连通 LLM API</span>
<span class="cm"># （可选）Embedding 模型服务（RAG）· 数据库连接（数据库工具）</span>
${base-dir}/
├── <span class="pkg">tmp-files</span>/ <span class="cm"># 临时文件存储（keep-days 15 天自动清理）</span>
├── <span class="pkg">rags-docs</span>/ <span class="cm"># RAG 文档投放目录</span>
│   └── <span class="pkg">rags_history</span>/ <span class="cm"># 已处理文档归档（自动创建）</span>
└── <span class="pkg">skills</span>/ <span class="cm"># 技能目录（30s 热扫描）</span>
└── {技能名称}/
└── SKILL.md <span class="cm"># 技能定义文件</span></div>
</div>
<div>
<div class="panel-title">监控指标与部署注意</div>
<div class="spec-table-wrap" style="margin-top:0;">
<table class="spec">
<thead><tr><th>指标</th><th>说明</th></tr></thead>
<tbody>
<tr><td>SSE 流式响应时间</td><td>LLM 首 token 延迟与整体响应时间</td></tr>
<tr><td>Token 消耗</td><td>每次对话的 prompt / completion / total 用量</td></tr>
<tr><td>工具调用成功率</td><td>工具执行成功 / 失败统计</td></tr>
<tr><td>工具调用频次</td><td>各工具调用频率分布</td></tr>
<tr><td>HITL 授权率</td><td>用户允许 / 拒绝工具调用的比例</td></tr>
</tbody>
</table>
</div>
<div class="callout" style="--c:#c92a2a;margin-top:14px;">
<div class="co-title">部署红线</div>
<p>命令行工具默认禁用（<code>ai.tools.command.enable=false</code>）需手动开启；Groovy 脚本工具默认禁用（<code>ai.tools.groovy.enable=false</code>）需手动开启；SQL 执行前经安全校验；文件路径规范化防穿越；加密输出拖慢模型输出速度、非必要不开启；工具调用需模型支持 Function-Calling（Qwen / DeepSeek / GPT 等）；技能依赖工具系统，需同时开启 <code>ai.tools.enable</code>；循环工程会话记录（<code>sessionRecordsMap</code>）由前端持久化到 localStorage 跨轮接力，服务端不落盘、无隐私残留；千问 TTS 按调用量计费（约 1.4 元/万字），依赖临时文件上传能力（<code>ai.tools.tmp-file.enable</code>）。</p>
</div>
</div>
</div>

<div class="callout" style="--c:#e8590c;">
<div class="co-title">从 i2f-ai-std 到 i2f-springboot-ops-starter — 完整的工具链</div>
<p>本框架的 AI 能力并非孤立存在，而是依托 i2f-turbo-java 整体工具链：网络层 <code>i2f-network</code>、国密层 <code>i2f-sm-crypto</code>、反射层 <code>i2f-reflect</code>、代理层 <code>i2f-proxy</code>、序列化层 <code>i2f-serialize-std</code>——这些基础模块共同构成了一个完整的、可独立运行的 AI Agent 运行时。</p>
<p>如果你需要在自己的项目中使用这些能力，推荐直接从 <code>i2f-ai-std</code> 开始，按需组合各模块，而非依赖整个 Starter。</p>
</div>
