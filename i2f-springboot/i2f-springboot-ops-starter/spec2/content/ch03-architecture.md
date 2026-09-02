<p class="lead">OpenAI 子系统横跨项目的四个层次：标准定义在 <code>i2f-jdk</code>，协议实现在 <code>i2f-ai-rest-openai</code>，Spring 适配在 <code>i2f-spring</code>，应用落地在 <code>i2f-springboot-ops-starter</code>。依赖严格自下而上。</p>

<div class="diagram-panel">

```svg
assets/diagrams/ch03-architecture.svg
```


<div class="dg-cap">四层依赖拓扑 — 依赖单向流动，上层可替换、下层零感知</div>
</div>

<div class="two-col">
<div>
<div class="panel-title">子系统包结构（45 个 Java 文件）</div>
<div class="pkg-tree"><span class="dir">i2f.springboot.ops.openai</span>
├── <span class="pkg">controller</span>/
│ ├── OpenAiOpsController <span class="cm"># 7 个 REST 端点 · SSE 编排核心</span>
│ └── QwenTtsOpsController <span class="cm"># 千问语音合成代理</span>
├── <span class="pkg">data</span>/
│   ├── OpenAiOperateDto <span class="cm"># 操作请求：功能开关集合</span>
│   ├── OpenAiCompletionVo / Dto <span class="cm"># 补全请求 VO/DTO</span>
│   ├── OpenAiMessageVo <span class="cm"># 多态消息载体</span>
│   ├── OpenAiMeta <span class="cm"># baseUrl · apiKey</span>
│   ├── OpenAiToolApprovalDto <span class="cm"># 工具审批决定</span>
│   └── <span class="pkg">message</span>/ OpsOpenAiConsts <span class="cm"># 12 种回显类型常量</span>
├── <span class="pkg">tool</span>/
│   ├── SpringContextToolAutoConfiguration
│ ├── <span class="pkg">impl</span>/ 17 个工具类 <span class="cm"># @Tool 注解驱动</span>
│ │ ├── GroovyTools <span class="cm"># groovy_run_script（条件装配）</span>
│ │ ├── LoopEngineeringTools <span class="cm"># 循环工程五步工作流提示词</span>
│ │ └── SessionRecordTools <span class="cm"># 循环工程会话记录读写</span>
│   └── <span class="pkg">impl.sql</span>/ SQL 校验器 <span class="cm"># AST + 正则双校验</span>
├── <span class="pkg">skill</span>/ SkillAutoConfiguration <span class="cm"># 30s 热扫描</span>
├── <span class="pkg">rag</span>/ RagAutoConfiguration <span class="cm"># 向量化 + SQLite 存储</span>
├── <span class="pkg">async</span>/ AsyncTaskDispatcher / AsyncTaskResolver <span class="cm"># 异步任务调度与状态轮询</span>
└── <span class="pkg">tts</span>/ QwenAudioTtsWebSocket <span class="cm"># 千问语音合成 WebSocket 客户端</span></div>
</div>
<div>
<div class="panel-title">关键依赖（pom.xml 摘录）</div>
<div class="spec-table-wrap" style="margin-top:0;">
<table class="spec"><thead><tr><th>依赖</th><th>职责</th></tr></thead><tbody>
<tr><td><code>i2f-ai-std</code></td><td>Agent / 工具 / 技能 / RAG / MCP 标准定义</td></tr>
<tr><td><code>i2f-ai-rest-openai</code></td><td>OpenAI 兼容协议 REST 实现</td></tr>
<tr><td><code>i2f-extension-ai-rag-sqlite</code></td><td>SQLite 向量存储</td></tr>
<tr><td><code>i2f-sm-crypto</code></td><td>SM2/SM3/SM4 国密算法</td></tr>
<tr><td><code>i2f-spring-core / i2f-spring-web</code></td><td>SpringContext · RestTemplate 桥接</td></tr>
<tr><td><code>i2f-jdbc-impl / i2f-rowset</code></td><td>数据库工具的 SQL 执行底座</td></tr>
<tr><td><code>i2f-extension-document</code></td><td>PDF 转图片（OCR 链路）</td></tr>
<tr><td><code>jsqlparser 4.9</code> <span class="tag t-t">provided</span></td><td>SQL AST 安全校验（可选）</td></tr>
</tbody></table>
</div>
<p class="body" style="font-size:13px;">大量第三方依赖以 <code>provided + optional</code> 声明——引入 Starter 不会污染宿主应用的依赖树，功能随 classpath 按需激活。</p>
</div>
</div>