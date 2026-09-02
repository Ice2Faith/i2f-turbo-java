<p class="lead">如果 LLM 是 CPU，工具就是它的 I/O 设备。框架用<b>声明式注解</b>把普通 Java 方法变成 LLM 能理解的工具能力：一个 <code>@Tool</code> 即完成「反射扫描 → JSON Schema 生成 → OpenAI <code>tools</code> 字段上报」的全链路注册，模型凭描述文本自主决定何时调用、传什么参数。</p>

<div class="diagram-panel">

```svg
assets/diagrams/ch07-tool-flow.svg
```


<div class="dg-cap">工具注册与调用全链路 — 注册一次，每轮对话按需调用</div>
</div>

<div class="two-col">
<div>
<div class="panel-title">注解即契约 — 一个方法即一个工具</div>

```java
@Tools(tags = {AiTags.RAG_VALUE})
public class RagTools {

    @Tool(tags = {AiTags.READONLY_VALUE},
        description = "获取与文本内容具有相关性的知识库文档资料")
    public List<RagSearchResultItem> rag_search(
        @ToolParam(description = "检索文本内容") String text,
        @ToolParam(description = "最多返回的条数，默认为 3") Integer topN) {
        return worker.similar(text, topN == null ? 3 : topN);
    }
}
```
<p class="body" style="font-size:13px;">方法名即工具名，<code>@ToolParam</code> 描述即参数说明——LLM 读到的 <code>tools</code> 字段完全由注解反射生成，<b>零手写 Schema</b>。</p>
</div>
<div>
<div class="panel-title">ToolManager — 工具池统一抽象</div>

```java
public interface ToolManager {
    // 列举全部工具定义 → 注入 tools 字段
    List<ToolDefinition> getTools();

    // 判定本管理器能否处理该调用请求
    boolean support(ToolBaseCallRequest request);

    // 执行工具调用，结果回填 ToolMessage
    Object callTool(ToolBaseCallRequest request) throws Throwable;
}
```
<p class="body" style="font-size:13px;">实现双轨：<code>ContextAppToolManager</code> 扫描容器内 @Tool Bean；<code>ContextMcpToolGatewayManager</code> 经 MCP 网关聚合外部工具，二者经 <code>@ConditionalOnMissingBean</code> 互斥装配。</p>
</div>
</div>

<div class="panel-title">ToolManager 工具管理体系 — 三层架构全景</div>
<p class="body" style="font-size:13.5px;">工具并非散落在 Spring 容器中各自为战。框架通过三层架构将它们组织为统一的工具管理体系，由 <code>SpringContextToolAutoConfiguration</code> 在启动时自动装配：</p>
<div class="diagram-panel">

```svg
assets/diagrams/ch07-tool-manager.svg
```


<div class="dg-cap">ToolManager 三层架构 — 工具从定义到执行的完整数据流</div>
</div>

<div class="panel-title">SpringContextToolAutoConfiguration 装配的四大 Bean</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>Bean</th><th>类型</th><th>开关属性</th><th>说明</th></tr></thead>
<tbody>
<tr><td><code>appMcpToolProvider</code></td><td><code>ContextAppMcpToolProvider</code></td><td><code>ai.tools.mcp.app.enable</code></td><td>将 Spring 容器中所有 @Tool/@Tools Bean 包装为名为 app_context 的工具提供者</td></tr>
<tr><td><code>mcpToolGatewayManager</code></td><td><code>ContextMcpToolGatewayManager</code></td><td><code>ai.tools.mcp.manager.enable</code></td><td>聚合所有 McpToolProvider Bean（本地 + 远程），实现统一工具路由</td></tr>
<tr><td><code>appToolManager</code></td><td><code>ContextAppToolManager</code></td><td><code>ai.tools.app.manager.enable</code></td><td>直接管理 @Tool/@Tools Bean，不经过 MCP 聚合层，与网关互斥</td></tr>
<tr><td><code>mcpProviderTools</code></td><td><code>McpProviderTools</code></td><td><code>ai.tools.mcp-gateway.enable</code></td><td>提供 3 个动态工具发现与加载的元工具</td></tr>
</tbody>
</table>
</div>

<div class="panel-title">Java 类型 → JSON Schema 映射</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>Java 类型</th><th>Schema 类型</th><th>细节</th></tr></thead>
<tbody>
<tr><td><code>String / char</code></td><td><span class="tag t-t">string</span></td><td>直接映射</td></tr>
<tr><td><code>Integer / Long / int …</code></td><td><span class="tag t-t">integer</span></td><td>整型族统一</td></tr>
<tr><td><code>Double / Float / BigDecimal</code></td><td><span class="tag t-t">number</span></td><td>浮点族统一</td></tr>
<tr><td><code>Boolean</code></td><td><span class="tag t-t">boolean</span></td><td>直接映射</td></tr>
<tr><td><code>List / Set / 数组</code></td><td><span class="tag t-t">array</span></td><td><code>items</code> 递归推导泛型</td></tr>
<tr><td><code>Map / POJO</code></td><td><span class="tag t-t">object</span></td><td><code>properties</code> 递归展开字段</td></tr>
<tr><td><code>Enum</code></td><td><span class="tag t-t">string + enum</span></td><td>枚举值列为候选约束</td></tr>
</tbody>
</table>
</div>

<div class="panel-title">内置工具清单 — ops 层 18 类 + 条件装配 5 类</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>工具类</th><th>代表工具</th><th>能力</th><th>关键标签</th></tr></thead>
<tbody>
<tr><td><code>DatabaseQueryTools</code></td><td><code>sql_query_datasource</code></td><td>SQL 数据查询，双重安全校验</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>DatabaseMetadataTools</code></td><td>元数据查询</td><td>表结构 / 索引 / 字段信息</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>CommandTools</code></td><td>shell 执行</td><td>本地命令行执行</td><td><span class="tag t-r">EXECUTABLE</span><span class="tag t-o">HUMAN</span></td></tr>
<tr><td><code>LocalFileTools</code></td><td>文件读写</td><td>本地文件列举 / 读取 / 写入</td><td><span class="tag t-o">WRITABLE</span></td></tr>
<tr><td><code>TmpFileTools</code></td><td>临时文件</td><td>上传 / 下载临时文件管理</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>TruthStoreTools</code></td><td><code>store_truth</code></td><td>存储关键事实，注入后续对话</td><td><span class="tag t-o">WRITABLE</span></td></tr>
<tr><td><code>McpProviderTools</code></td><td>元工具 ×3</td><td>MCP 供应商发现与动态装载</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>AgentTools</code> (a2a)</td><td><code>safe_sql_detect</code></td><td>Agent 调 Agent 的 SQL 安全检测</td><td><span class="tag t-b">A2A</span></td></tr>
<tr><td><code>CodecTools</code></td><td>编解码</td><td>Base64 / URL / Unicode 转换</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>JceTools</code></td><td>加密哈希</td><td>摘要 / 加解密计算</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>DatetimeTools</code></td><td>时间转换</td><td>时间戳 / 日期格式互转</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>LunarTools</code></td><td>农历查询</td><td>农历 / 节气 / 干支</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>UidTools</code></td><td>唯一 ID</td><td>雪花等分布式 ID 生成</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>RandomTools</code></td><td>随机采样</td><td>随机数 / 抽样</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>WebDownloadTools</code></td><td>网络下载</td><td>下载网络资源到本地</td><td><span class="tag t-b">PUBLIC_NET</span></td></tr>
<tr><td><code>FileToolUtils</code></td><td>文件工具</td><td>文件处理辅助能力</td><td>—</td></tr>
<tr><td><code>SkillsTools</code> ※</td><td>技能三件套</td><td>技能文档 / 资源 / 脚本执行</td><td><span class="tag t-t">SKILL</span></td></tr>
<tr><td><code>RagTools</code> ※</td><td><code>rag_search</code></td><td>知识库相似度检索</td><td><span class="tag t-t">RAG</span></td></tr>
<tr><td><code>MemoryTools</code> ※</td><td><code>memory_search / save / delete</code></td><td>用户级记忆检索 / 保存 / 删除</td><td><span class="tag t-t">RAG</span></td></tr>
<tr><td><code>SessionRecordTools</code> ※</td><td><code>session_record_read / session_record_update</code></td><td>循环工程会话记录读写（request / plan / checklist / agent）</td><td><span class="tag t-g">AUTO</span></td></tr>
<tr><td><code>GroovyTools</code> ※</td><td><code>groovy_run_script</code></td><td>Groovy 脚本动态执行（GroovyShell）</td><td><span class="tag t-r">EXECUTABLE</span><span class="tag t-o">HUMAN</span><span class="tag t-t">SCRIPT</span></td></tr>
</tbody>
</table>
</div>
<p class="body" style="font-size:12.5px;">※ 条件装配：随 <code>ai.skills.enable</code> / <code>ai.rags.enable</code> / <code>ai.rags.memory.enable</code> / <code>ai.tools.session-record.enable</code> / <code>ai.tools.groovy.enable</code> 开关注入，详见第 13 章。</p>

<div class="panel-title">AiTags — 工具的「危险品标签」体系</div>
<p class="body" style="font-size:13.5px;">每个工具可携带多维标签，供前端审批策略与后端过滤决策使用：</p>
<div class="tag-cloud">
<span class="tc"><b>行为</b>：READONLY · WRITABLE · EXECUTABLE · COMMAND</span>
<span class="tc"><b>审批</b>：AUTO · SANDBOX · HUMAN</span>
<span class="tc"><b>网络</b>：PUBLIC_NET · PRIVATE_NET · INTRANET_ONLY</span>
<span class="tc"><b>敏感</b>：SENSIBLE · AUTH · SECRET</span>
<span class="tc"><b>成本</b>：HIGH_COST · SLOW_EXEC · RATE_LIMITED</span>
<span class="tc"><b>领域</b>：SKILL · RAG</span>
</div>

<div class="panel-title">ToolCallContextHolder — 请求级上下文线程传递</div>
<p class="body" style="font-size:13.5px;">工具执行时需要访问请求级数据（如 <code>OpenAiOperateDto</code>、用户信息、会话配置等）。<code>ToolCallContextHolder</code> 基于 <code>InheritableThreadLocal&lt;Map&lt;String, Object&gt;&gt;</code> 实现，提供线程安全的请求上下文存取，并在 MCP 跨服务调用时<b>自动序列化传递</b>——远程工具与本地工具使用完全相同的上下文读取方式。</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>方法</th><th>说明</th><th>典型场景</th></tr></thead>
<tbody>
<tr><td><code>put(key, value)</code></td><td>写入上下文键值</td><td>OpenAiOpsController 将 <code>req</code> 放入上下文</td></tr>
<tr><td><code>get(key)</code></td><td>读取上下文值</td><td>工具方法中读取请求对象：<code>ToolCallContextHolder.get("req")</code></td></tr>
<tr><td><code>copyOf()</code></td><td>获取上下文快照</td><td>MCP Client 序列化上下文随 HTTP 发送</td></tr>
<tr><td><code>replaceAs(map)</code></td><td>整体替换上下文</td><td>MCP Server 反序列化后恢复上下文</td></tr>
<tr><td><code>clear()</code></td><td>清空上下文</td><td>工具执行完成后清理，防止泄漏</td></tr>
</tbody>
</table>
</div>
<div class="callout" style="--c:#0b7285;">
<div class="co-title">InheritableThreadLocal — 并行工具执行的基石</div>
<p>工具契约在线程池中并行执行时，<code>InheritableThreadLocal</code> 自动将父线程（Controller 线程）的上下文传递给子线程（工具执行线程），无需显式传递——这是 CountDownLatch 并行工具执行模式能够正常工作的底层保障。MCP 跨服务传递的完整链路详见第 10 章。</p>
</div>

<div class="panel-title">AsyncTaskResolver — 异步任务工具扩展模式</div>
<p class="body" style="font-size:13.5px;">对于文生图、视频生成等<b>长耗时操作</b>，工具无法在单次 HTTP 请求内完成。框架提供了 <code>AsyncTaskResolver</code> 接口，让工具将耗时操作建模为<b>异步任务</b>：工具方法立即返回 <code>AsyncTaskMessage</code>（含任务 ID 与初始状态），Controller 自动将其收集并通过 <code>echo_async_tasks</code> 事件推送前端；前端可随时经 <code>POST /ops/open-ai/async/task/query</code> 轮询任务状态，<code>AsyncTaskDispatcher</code> 从 Spring 容器中查找匹配的 <code>AsyncTaskResolver</code> 实现进行状态查询。</p>

```java
public interface AsyncTaskResolver {
    // 判定是否支持处理该任务类型
    boolean support(AsyncTaskItem item, OpenAiMeta meta) throws Exception;

    // 查询任务状态并更新 item 的 status / result / error
    AsyncTaskItem resolve(AsyncTaskItem item, OpenAiMeta meta) throws Exception;
}

public class AsyncTaskItem {
    protected String description; // 任务描述
    protected String type; // 任务类型（用于 Resolver 匹配）
    protected String taskId; // 任务 ID
    protected String status; // pending / running / success / failure
    protected String resultType; // image / image_list / video / file_list
    protected Object result; // 结果数据
    protected String error; // 错误信息
}
```

<div class="callout" style="--c:#0b7285;">
<div class="co-title">Dispatcher 自动路由 — 零配置扩展</div>
<p><code>AsyncTaskDispatcher</code> 实现 <code>ApplicationContextAware</code>，启动时自动发现容器中所有 <code>AsyncTaskResolver</code> Bean。每次轮询时按 <code>item.type</code> 匹配——新增一种异步任务类型只需实现 <code>AsyncTaskResolver</code> 接口并注册为 Spring Bean，无需修改任何调度代码。</p>
</div>

<div class="two-col">
<div>
<div class="panel-title">标签自动审批 — 决策流程</div>
<div class="pkg-tree"><span class="cm"># 模型返回 tool_calls 后的前端审批决策</span>
查找工具定义的 tags
├── tags 包含 "auto" 且用户勾选了 "auto"
│   └── → 自动放行执行
├── tags 全部被 autoApprovalToolTags 覆盖
│   └── → 自动放行执行
└── 未被覆盖
    └── → 弹出审批弹窗（HITL 人工确认）</div>
</div>
<div>
<div class="panel-title">配置来源与效果</div>
<div class="callout" style="--c:#0b7285;margin-top:0;">
<div class="co-title">/ops/open-ai/tool/tags — 标签集合接口</div>
<p>前端经 <code>POST /ops/open-ai/tool/tags</code> 拉取全部已注册工具的标签并集，渲染为审批配置面板；用户勾选信任标签（如 <code>auto</code> / <code>readonly</code>）持久化到 <code>autoApprovalToolTags</code>。命中标签的工具调用<b>无需手动确认直接执行</b>——在安全与效率之间按需调节。</p>
</div>
</div>
</div>

<div class="callout" style="--c:#e8590c;">
<div class="co-title">HITL 鲁棒性 — 未 resolved 授权恢复检查</div>
<p>审批弹窗是异步中断点：用户可能在弹窗打开时切走会话或直接关闭弹窗，导致 <code>toolApprovalList</code> 存在待审批契约却无法回传决定。前端在每轮发送前检查 <code>toolApprovalList.length &gt; 0 &amp;&amp; !toolApproval.resolved</code>——存在未完成审批则强制重新唤起弹窗（支持<b>批量全拒 / 批量允许 / 批量填充拒绝原因</b>）；审批决定经 <code>continueExecuteWithToolApproval()</code> 提交后置 <code>resolved=true</code> 并继续推理，保证待授权工具调用不会被静默丢弃。</p>
</div>

<div class="callout" style="--c:#c92a2a;">
<div class="co-title">SQL 双重校验 — 给最危险的工具上双保险</div>
<p><code>sql_query_datasource</code> 直连数据源，框架用 <code>OpsSqlValidators</code> 做两道拦截：<b>JSqlParser AST 解析</b>为主——将 SQL 解析成语法树后按语句类型白名单放行；解析失败（方言差异等）时回退到 <b>OpsSimpleRegexSqlValidator 正则校验</b>。DROP / TRUNCATE / DELETE 等危险语句在到达数据库之前即被阻断。</p>
</div>
