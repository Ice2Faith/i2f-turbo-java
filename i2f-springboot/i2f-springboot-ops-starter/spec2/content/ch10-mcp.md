<p class="lead">工具不是越多越好——上百个工具定义全量上报会<b>吞噬 token、分散模型注意力</b>。MCP 动态工具方案的回答是：只上报 3 个「元工具」，让 LLM 像查目录一样<b>先发现、再装载、按需使用</b>，并用 LRU 控制会话内工具总量。</p>

<div class="diagram-panel">

```svg
assets/diagrams/ch10-mcp-discovery.svg
```


<div class="dg-cap">三步发现 + LRU 淘汰 — 工具装载的「目录-翻页」模型</div>
</div>

<div class="two-col">
<div>
<div class="panel-title">注入的元提示词（McpProviderTools.SYSTEM_PROMPT）</div>

```text
当现有工具无法满足任务需求时：
1. 调用 tools_provider_list 列举可用的 MCP 工具供应商
2. 调用 list_tools_from_providers 按供应商名查看其工具清单
3. 调用 load_tools_by_names 将所需工具装载到当前会话
装载后的工具将参与后续推理；会话内工具数量受 LRU 策略约束，
超容量时最久未使用的工具将被淘汰。
```

</div>
<div>
<div class="panel-title">设计要点</div>
<div class="callout" style="--c:#e8590c;margin-top:0;">
<div class="co-title">常驻工具不可被淘汰</div>
<p>MCP 元工具自身、<code>TmpFileTools</code>、<code>TruthStoreTools</code> 等基础设施类工具被标记为<b>常驻</b>——LRU 淘汰只作用于动态装载的业务工具，保证「查目录的能力」永远在线。</p>
</div>
<div class="callout" style="--c:#0b7285;">
<div class="co-title">前后端双向同步</div>
<p>每次装载 / 淘汰后，后端经 <code>echo_lru_tools</code> 回显当前存活工具名列表与容量（<code>lruToolNames</code> / <code>lruToolMaxSize</code>），前端据此渲染工具面板——用户随时可见「模型手里现在有哪些工具」。</p>
</div>
</div>
</div>

<div class="panel-title">意图识别辅助工具推荐 — 先猜意图，再给工具</div>
<p class="body" style="font-size:13.5px;">动态工具虽能按需装载，但 LLM 仍需从零开始「翻目录」——对上百个工具提供商的场景，三步发现本身就需要消耗额外的推理轮次。意图识别方案在 LRU 动态工具的基础上引入<b>前置过滤</b>：利用 <code>@ToolIntent</code> 注解为每个工具标注意图标签，再由 LLM 根据用户问题<b>先推断意图</b>，将推断出的意图标签对应的工具<b>优先插入 LRU 队列头部</b>，让 LLM 在后续推理中<b>更大概率直接命中所需工具</b>。</p>

<div class="diagram-panel" style="margin-top:14px;">

```svg
assets/diagrams/ch10-intent.svg
```


<div class="dg-cap">意图识别五步 — 前置过滤 + LRU 队列重排，让 LLM 更大概率命中所需工具</div>
</div>

<div class="two-col">
<div>
<div class="panel-title">@ToolIntent 注解 — 工具意图标注</div>

```java
public @interface ToolIntent {
    // 方式一：枚举常量
    ToolIntents[] value() default {};
    // 方式二：自定义键值对
    ToolIntentItem[] items() default {};
    // 方式三：实现 IToolIntent 接口的类
    Class<? extends IToolIntent>[] from() default {};
}

// 使用示例
@ToolIntent(items = @ToolIntentItem(
    value = "sql_safe",
    description = "SQL语句安全性校验"
))
public String safe_sql_detect(...) { ... }
```

</div>
<div>
<div class="panel-title">设计要点</div>
<div class="callout" style="--c:#e8590c;margin-top:0;">
<div class="co-title">前置于 LLM 推理</div>
<p>意图识别发生在 LLM 正式推理<b>之前</b>，利用 <code>AgentTools.intent_recognize()</code> 调用 LLM 做一次轻量级的意图分类（非流式），将识别出的意图标签对应的工具<b>插入 LRU 队列头部</b>——后续 LLM 正常推理时，看到的就是已按意图排好序的工具列表。</p>
</div>
<div class="callout" style="--c:#0b7285;">
<div class="co-title">未标注工具的降级策略</div>
<p>未标注 <code>@ToolIntent</code> 的工具不会丢失：以工具名本身作为意图标签，确保所有工具都参与意图识别。</p>
</div>
<div class="callout" style="--c:#2b8a3e;">
<div class="co-title">Token 成本可控</div>
<p>意图识别是一次额外的非流式 LLM 调用，会增加少量 Token 消耗；但相比将上百个工具定义全量注入请求，意图识别 + LRU 的组合方案在工具数量多时<b>综合 Token 消耗更低</b>。</p>
</div>
</div>
</div>

<div class="panel-title">MCP 独立模块 — 可脱离 ops-starter 独立部署</div>
<p class="body" style="font-size:13.5px;">除了 ops-starter 内嵌的 MCP 动态工具网关，项目还提供了两个独立的 Spring Boot Starter 模块，用于构建<b>分布式 MCP 工具服务网络</b>。它们不依赖 ops-starter，可独立部署在任意微服务中：</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>模块</th><th>artifactId</th><th>角色</th><th>核心类</th></tr></thead>
<tbody>
<tr><td><b>MCP Server</b></td><td><code>i2f-springboot-ai-mcp-server</code></td><td>将本地 @Tool 工具以 HTTP 接口暴露</td><td><code>HttpSimpleMcpServer</code> · <code>SpringAiMcpServerAutoConfiguration</code></td></tr>
<tr><td><b>MCP Client</b></td><td><code>i2f-springboot-ai-mcp-client</code></td><td>注册远程 MCP 工具提供者为 Spring Bean</td><td><code>HttpSimpleMcpClientToolProvider</code> · <code>SpringAiMcpClientAutoConfiguration</code></td></tr>
</tbody>
</table>
</div>
<div class="spec-table-wrap" style="margin-top:14px;">
<table class="spec">
<thead><tr><th colspan="2">MCP 协议定义（HttpSimpleMcpConstants）</th></tr></thead>
<tbody>
<tr><td>认证方式</td><td>HMAC-SHA256 签名：<code>Base64(HmacSHA256(appId#timestamp#nonce, appKey))</code></td></tr>
<tr><td>请求头</td><td><code>X-App-Id</code> · <code>X-App-Date</code> · <code>X-App-Nonce</code> · <code>X-App-Sign</code></td></tr>
<tr><td>工具列表接口</td><td><code>GET /mcp/tool/list</code> → 返回工具元信息</td></tr>
<tr><td>工具调用接口</td><td><code>POST /mcp/tool/call</code> → 转发执行结果</td></tr>
</tbody>
</table>
</div>

<div class="panel-title">MCP Server — 两种传输模式</div>
<p class="body" style="font-size:13.5px;">Server 端提供两种 HTTP 传输实现，通过自动配置按 Classpath 条件激活：</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>模式</th><th>自动配置类</th><th>条件</th><th>端口</th><th>适用场景</th></tr></thead>
<tbody>
<tr><td><b>Spring Web MVC</b></td><td><code>SpringAiSpringWebMcpServerAutoConfiguration</code></td><td>Classpath 含 <code>RestController</code></td><td>共享 Web 端口</td><td>与 Web 应用共存</td></tr>
<tr><td><b>Netty</b></td><td><code>SpringAiNettyMcpServerAutoConfiguration</code></td><td>Classpath 含 <code>ServerBootstrap</code></td><td>独立端口 (默认 23745)</td><td>独立 AI 工具服务</td></tr>
</tbody>
</table>
</div>
<div class="spec-table-wrap" style="margin-top:14px;">
<table class="spec">
<thead><tr><th colspan="2">Server 配置属性（前缀：i2f.springboot.ai.mcp.server）</th></tr></thead>
<tbody>
<tr><td><code>simple-server.app-list</code></td><td>授权应用列表（appId / appKey 对，用于 HMAC 签名验证）</td></tr>
<tr><td><code>simple-server.expire-window-minutes</code></td><td>过期时间窗口，默认 30 分钟</td></tr>
<tr><td><code>netty.port</code></td><td>Netty 监听端口，默认 23745</td></tr>
<tr><td><code>netty.max-content-length</code></td><td>HTTP 内容最大长度，默认 65536 字节</td></tr>
</tbody>
</table>
</div>

<div class="panel-title">MCP Client — 动态 Bean 注册 + 本地缓存</div>
<p class="body" style="font-size:13.5px;">Client 端通过 <code>BeanDefinitionRegistryPostProcessor</code> 在启动时动态注册 <code>McpToolProvider</code> Bean：每个配置实例对应一个 <code>SimpleMcpClientMcpToolProviderFactoryBean</code>，Bean 名称为 <code>{name}_McpToolProvider</code>。首次调用时经 HTTP 拉取远程工具列表，本地缓存 15 秒 TTL，<code>CopyOnWriteArrayList</code> + <code>ReentrantLock</code> 保证并发安全。</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th colspan="2">Client 配置属性（前缀：i2f.springboot.ai.mcp.client.simple.instances）</th></tr></thead>
<tbody>
<tr><td><code>name</code> (必填)</td><td>工具提供者名称，同时用作 Bean 名称和工具名前缀</td></tr>
<tr><td><code>base-url</code></td><td>远程 MCP Server 的 Base URL</td></tr>
<tr><td><code>app-id / app-key</code></td><td>HMAC 签名认证凭据</td></tr>
<tr><td><code>description</code></td><td>工具提供者描述信息</td></tr>
</tbody>
</table>
</div>

<div class="panel-title">请求级上下文透明传递 — 跨 MCP 边界无感知</div>
<p class="body" style="font-size:13.5px;">MCP 协议的核心设计目标之一是让远程工具调用<b>与本地工具调用完全一致</b>。借助 <code>ToolCallContextHolder</code>（详见第 07 章），请求上下文（如 <code>req</code> 对象）在 MCP Client 端自动序列化，随 HTTP 请求传递到 Server 端后自动恢复——远程工具可以通过 <code>ToolCallContextHolder.get("req")</code> 读取与本地工具完全相同的请求数据。</p>
<div class="diagram-panel" style="margin-top:14px;">

```svg
assets/diagrams/ch10-context.svg
```


<div class="dg-cap">MCP 上下文传递 — Client 端序列化 → HTTP 传输 → Server 端恢复，对工具开发者完全透明</div>
</div>
<div class="spec-table-wrap" style="margin-top:14px;">
<table class="spec">
<thead><tr><th>阶段</th><th>位置</th><th>操作</th></tr></thead>
<tbody>
<tr><td><b>写入</b></td><td><code>OpenAiOpsController.stream()</code></td><td><code>ToolCallContextHolder.put("req", req)</code></td></tr>
<tr><td><b>快照 &amp; 序列化</b></td><td><code>HttpSimpleMcpClientToolProvider.callTool()</code></td><td><code>copyOf()</code> → JSON → <code>payloadDto.context</code></td></tr>
<tr><td><b>签名保护</b></td><td><code>HttpSimpleMcpClientToolProvider.applyHeader()</code></td><td>context 参与 HMAC-SHA256 签名，防篡改</td></tr>
<tr><td><b>反序列化 &amp; 恢复</b></td><td><code>SpringHttpSimpleMcpController</code> / <code>HttpSimpleMcpInBoundHandler</code></td><td>JSON → Map → <code>replaceAs(ctx)</code></td></tr>
<tr><td><b>清理</b></td><td>Server 端 Controller / Handler</td><td><code>finally { clear() }</code> 防止 ThreadLocal 泄漏</td></tr>
</tbody>
</table>
</div>
<div class="callout" style="--c:#0b7285;margin-top:14px;">
<div class="co-title">两种传输模式一致处理</div>
<p>无论是 Spring Web MVC（<code>SpringHttpSimpleMcpController</code>）还是 Netty（<code>HttpSimpleMcpInBoundHandler</code>），Server 端对上下文的恢复与清理逻辑完全一致——<code>replaceAs</code> → <code>callTool</code> → <code>clear</code>，工具实现者无需关心底层传输方式。</p>
</div>
<div class="callout" style="--c:#2b8a3e;margin-top:10px;">
<div class="co-title">对工具开发者透明</div>
<p>编写远程 MCP 工具时，与本地工具使用完全相同的上下文读取方式：<code>OpenAiOperateDto req = ToolCallContextHolder.get("req");</code> —— 无需关心上下文是通过 HTTP 传递的还是本地线程共享的。</p>
</div>

<div class="panel-title">场景：分布式 MCP 工具联邦</div>
<p class="body" style="font-size:13.5px;">多个微服务各自提供专业工具，通过 MCP 协议组成联邦工具网络。AI 主控服务作为 MCP Client，透明调用分布在文件服务、数据库服务上的远程工具，对 AI 模型而言与本地工具无异：</p>
<div class="diagram-panel" style="margin-top:14px;">

```svg
assets/diagrams/ch10-federation.svg
```


<div class="dg-cap">分布式 MCP 联邦 — AI 模型统一调用本地与远程工具</div>
</div>

<div class="two-col" style="margin-top:28px;">
<div>
<div class="panel-title">MCP Server 端配置</div>

```yaml
i2f:
  springboot:
    ai:
      mcp:
        server:
          enable: true
          simple-server:
            app-list:
              - app-id: "ai-master"
                app-key: "secret-key-1"
          netty:
            enable: false  # 使用 Spring Web MVC 模式

ai:
  tools:
    enable: true
    file:
      enable: true
      root-path: "/data/ai-files"
```

</div>
<div>
<div class="panel-title">MCP Client 端配置（AI 主控服务）</div>

```yaml
i2f:
  springboot:
    ai:
      mcp:
        client:
          enable: true
          simple:
            instances:
              - name: "file_svr"
                base-url: "http://file-service:8080"
                app-id: "ai-master"
                app-key: "secret-key-1"
                description: "文件操作工具服务"
              - name: "db_svr"
                base-url: "http://db-service:8080"
                app-id: "ai-master"
                app-key: "secret-key-2"
                description: "数据库查询工具服务"

ai:
  tools:
    enable: true
    mcp-gateway:
      enable: true  # 启用 MCP 动态工具网关
```

</div>
</div>

<div class="panel-title">MCP 工具 Tags 分类体系</div>
<p class="body" style="font-size:13.5px;">每个 <code>@Tool</code> 可携带多维标签，用于前端审批策略与后端过滤决策。以下为 MCP 场景中的关键标签（完整标签见第 07 章）：</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>Tag 常量</th><th>含义</th><th>MCP 典型场景</th></tr></thead>
<tbody>
<tr><td><code>AUTO_VALUE</code></td><td>自动加载的常用工具</td><td><code>get_current_datetime</code> — AI 随时调用无需审批</td></tr>
<tr><td><code>READONLY_VALUE</code></td><td>只读操作</td><td><code>file_svr.read_file</code> — 远程文件只读</td></tr>
<tr><td><code>WRITABLE_VALUE</code></td><td>写入操作（需谨慎）</td><td><code>file_svr.write_file</code> — 远程文件写入</td></tr>
<tr><td><code>EXECUTABLE_VALUE</code></td><td>可执行操作（高风险）</td><td><code>app_context.run_command_line</code></td></tr>
<tr><td><code>HUMAN_VALUE</code></td><td>需人工确认</td><td>跨网络命令执行，必须 HITL 审批</td></tr>
<tr><td><code>MCP_VALUE</code></td><td>MCP 元工具</td><td><code>McpProviderTools</code> 的三个发现工具</td></tr>
</tbody>
</table>
</div>
