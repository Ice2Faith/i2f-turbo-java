<p class="lead"><code>/ops/open-ai/index.html</code> 是一个<b>单文件胖客户端</b>：Vue2 驱动、ElementUI 组件化、全部静态资源打包在 JAR 内。它不只是聊天界面——会话管理、消息循环、工具审批、功能开关等 <b>Agent 控制面</b>全部运行在浏览器里，后端只是「安全网关 + 执行器 + 中继」。</p>

<div class="panel-title">技术栈矩阵</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>库</th><th>职责</th></tr></thead>
<tbody>
<tr><td><code>Vue 2</code></td><td>响应式视图层，单文件内联组件</td></tr>
<tr><td><code>ElementUI</code></td><td>对话框 / 开关 / 菜单等 UI 组件库</td></tr>
<tr><td><code>markdown-it + highlight.js</code></td><td>答复 Markdown 渲染与代码高亮</td></tr>
<tr><td><code>CodeMirror</code></td><td>JSON / 提示词编辑器</td></tr>
<tr><td><code>KaTeX</code></td><td>数学公式渲染</td></tr>
<tr><td><code>mermaid</code></td><td>流程图 / 时序图渲染</td></tr>
<tr><td><code>Three.js + SES</code></td><td>3D 场景沙箱（ThreeJs 角色产出物的安全执行环境）</td></tr>
<tr><td><code>sm-crypto</code></td><td>前端 SM2 / SM3 / SM4 国密加解密与签名</td></tr>
<tr><td><code>echarts</code></td><td>图表绘制角色的数据可视化</td></tr>
<tr><td><code>panzoom</code></td><td>图像平移缩放查看</td></tr>
</tbody>
</table>
</div>

<div class="panel-title">前端架构</div>
<div class="card-grid">
<div class="card" style="--c:#e8590c;"><span class="idx">1</span><h3>状态驱动</h3><div class="tagline">State-Driven</div><p>全局状态管理会话列表、消息历史、功能开关、供应商连接信息、审批队列。所有 UI 变化均由状态变更驱动。</p></div>
<div class="card" style="--c:#0b7285;"><span class="idx">2</span><h3>SSE 消费</h3><div class="tagline">ReadableStream</div><p>使用 <code>fetch</code> 的 <code>ReadableStream</code> 逐行消费 SSE 流，解析 14 种回显类型，路由到不同渲染逻辑。</p></div>
<div class="card" style="--c:#2b8a3e;"><span class="idx">3</span><h3>打字机效果</h3><div class="tagline">Typewriter</div><p>assistant chunk 事件逐字追加到消息气泡，模拟打字机效果；<code>reasoning_content</code> 独立渲染到可折叠的思考面板。</p></div>
<div class="card" style="--c:#1971c2;"><span class="idx">4</span><h3>HITL 审批弹窗</h3><div class="tagline">Human In The Loop</div><p>检测到 <code>tool_calls</code> 后弹出审批弹窗：逐个/全部/拒绝+原因。获批后自动触发下一轮 ReAct，被拒绝则反馈给模型。</p></div>
<div class="card" style="--c:#e67700;"><span class="idx">5</span><h3>国密加密</h3><div class="tagline">SM2/SM4 in JS</div><p>前端使用 <code>sm-crypto</code> 完成 SM2/SM3/SM4 加密、签名、摘要计算，无需后端代理。</p></div>
<div class="card" style="--c:#c92a2a;"><span class="idx">6</span><h3>资源内嵌</h3><div class="tagline">JAR Embedded</div><p>前端全部资源（HTML/CSS/JS/字体/图片）打包在 JAR 的 <code>META-INF/resources/</code> 下，Spring Boot 自动映射为静态资源，零额外部署。</p></div>
</div>

<div class="two-col">
<div>
<div class="panel-title">角色预设体系（role-config.json）</div>
<p class="body" style="font-size:13.5px;">10 个分类、20 个角色预设，每个角色即一份 <code>roles/**/*.md</code> 系统提示词，菜单点选即切换人格：</p>
<div class="spec-table-wrap" style="margin-top:0;">
<table class="spec">
<thead><tr><th>分类</th><th>角色</th></tr></thead>
<tbody>
<tr><td>独立角色</td><td>默认角色 · 中外语翻译官 · Emoji 表情大师</td></tr>
<tr><td>开发 / 技术</td><td>资深开发工程师 · 实施规划师 · 智能开发工程师</td></tr>
<tr><td>绘图 / 可视化</td><td>Echarts 图表 · Mermaid 图形 · JsCanvas 图像 · HtmlSvg 矢量 · ThreeJs 3D 场景 · Draw.io 流程图</td></tr>
<tr><td>音视频 / 多媒体</td><td>FFMPEG 多媒体处理</td></tr>
<tr><td>文化 / 文学</td><td>易学命理大师 · 文学泰斗</td></tr>
<tr><td>聊天 / 陪聊</td><td>爱莉希雅 · Api 聊天女友</td></tr>
<tr><td>文档 / 办公</td><td>Svg 生成 PPT 演示页面 · Markitdown 文档提取/转换</td></tr>
<tr><td>娱乐 / 放松</td><td>短视频舞蹈规划师</td></tr>
</tbody>
</table>
</div>
</div>
<div>
<div class="panel-title">交互细节 — 为长对话打磨</div>
<div class="step-list" style="margin-top:0;">
<div class="step">
<div class="no"></div>
<div class="bd"><h4>流式光标</h4>
<p>SSE chunk 逐字追加，打字机光标实时跟随。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>思考折叠</h4>
<p><code>reasoning_content</code> 思考过程独立折叠区，可展开查看模型推理。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>工具审批对话框</h4>
<p>tool_calls 触发审批弹窗：批准 / 拒绝 / 编辑参数后放行（HITL）；支持<b>批量全拒 / 批量允许 / 批量填充拒绝原因</b>。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>未 resolved 授权恢复</h4>
<p>每轮发送前检查 <code>toolApprovalList.length &gt; 0 &amp;&amp; !toolApproval.resolved</code>，存在未完成审批则自动重新唤起弹窗，待授权调用不丢失。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>消息记号渲染</h4>
<p>按 <code>[1u]→[2ac]→[3t]→[4a]</code> 角色记号区分渲染用户 / 助手 / 工具消息。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>功能开关面板</h4>
<p>工具 / 技能 / 知识库 / 记忆 / LRU / 事实 / 图片视觉 / 报文回显 / 循环工程等开关即时生效。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>中断输出</h4>
<p><code>AbortController</code> 中断在途 SSE 请求，长输出可随时叫停。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>异步任务状态展示</h4>
<p><code>asyncTasks</code> 列表渲染为带状态标签（等待中/运行中/成功/失败）的任务卡片，支持手动刷新查询状态；任务完成后自动展示结果文件（图片/视频等），支持预览与下载。</p></div>
</div>
</div>
</div>
</div>

<div class="panel-title">Markdown 代码块拓展渲染 — 模型输出即可视化</div>
<p class="body" style="font-size:13.5px;">前端将 6 种语言标记的 fenced code block 渲染为<b>可交互的富媒体组件</b>——绘图角色（Echarts / Mermaid / ThreeJs 等）的产出物由此直接落地为图表、图形与 3D 场景：</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>代码块</th><th>渲染方式</th><th>安全机制</th><th>导出</th></tr></thead>
<tbody>
<tr><td><code>```echarts</code></td><td>ECharts 5 JSON 配置 → 实时图表</td><td>SES 沙箱解析配置</td><td>PNG 图片</td></tr>
<tr><td><code>```mermaid</code></td><td>流程图 / 时序图 / 类图 / 甘特图</td><td>mermaid.render 受控渲染</td><td>SVG 矢量</td></tr>
<tr><td><code>```canvas</code></td><td>Canvas 2D 自由绘图（drawCanvas 函数体）</td><td>SES 沙箱隔离执行</td><td>PNG 图片</td></tr>
<tr><td><code>```svg</code></td><td>SVG 矢量图形直接渲染</td><td>DOMParser 净化，移除 script/iframe/on* 事件</td><td>SVG 矢量</td></tr>
<tr><td><code>```drawio</code></td><td>Draw.io 专业图表（embed.diagrams.net iframe）</td><td>XML 属性自动转义修复</td><td>.drawio 文件</td></tr>
<tr><td><code>```threejs</code></td><td>Three.js 3D 场景（renderThreeJs 函数体）</td><td>SES 沙箱 + THREE 全局注入</td><td>PNG 截图</td></tr>
</tbody>
</table>
</div>
<p class="body" style="font-size:12.5px;">通用特性：代码块头部带语言标签与<b>下载 / 查看 / 复制</b>按钮；流式输出完成后才触发渲染（<code>$responsing</code> 守卫）避免半途渲染；气泡渲染锁防并发冲突；highlight.js 语法高亮兜底 + KaTeX 数学公式；mermaid / svg 支持 Panzoom 缩放平移。</p>

<div class="panel-title">16 大可配置开关 — 细粒度控制 Agent 行为边界</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>开关</th><th>关键配置项</th><th>功能说明</th></tr></thead>
<tbody>
<tr><td><b>定制显示</b></td><td><code>hiddenMessageTypes</code></td><td>按使用习惯控制各类消息（尤其 14 种回显消息）的显示 / 隐藏</td></tr>
<tr><td><b>动态工具</b></td><td><code>enableLruTools</code> · <code>lruToolMaxSize</code></td><td>不全量加载工具，LRU 策略按需披露与淘汰（需后端 MCP 网关支持）</td></tr>
<tr><td><b>工具调用</b></td><td><code>enableTools</code> · <code>autoApprovalToolTags</code></td><td>Function Calling 总开关 + 标签自动审批</td></tr>
<tr><td><b>知识库</b></td><td><code>enableRags</code></td><td>连接 RAG 知识库，模型按需检索知识内容（依赖工具开启）</td></tr>
<tr><td><b>技能调用</b></td><td><code>enableSkills</code></td><td>注入 SKILL.md 技能声明与关联工具（占用 Token，依赖工具开启）</td></tr>
<tr><td><b>语音播报</b></td><td>TextToSpeech API · <code>useQwenTts</code></td><td>自动 TTS 播报助手消息：浏览器原生语音合成，或走千问 TTS 合成（约 1.4 元/万字）</td></tr>
<tr><td><b>图片视觉</b></td><td><code>enableVisionImage</code></td><td>开启后图片压缩为 base64 直送视觉模型推理，不进行 OCR 文本提取；压缩策略按像素尺寸与文件大小双重控制，按像素消耗 Token</td></tr>
<tr><td><b>事实注入</b></td><td><code>enableTruth</code> · <code>store_truth</code></td><td>模型主动存储 / 更新关键事实，防遗忘防幻觉，echo_truth_prompt / echo_truth_content / echo_truth_sync 三态回显</td></tr>
<tr><td><b>附加时间</b></td><td><code>enableUserSendTime</code></td><td>为用户消息附加发送时间，让模型感知时间变化</td></tr>
<tr><td><b>回显请求</b></td><td><code>enableEchoRequestPayload</code></td><td>回显实际发送给 LLM 的报文，右键查看——报文学习与调试</td></tr>
<tr><td><b>输出加密</b></td><td><code>encryptOutput</code></td><td>模型输出 SM4 加密传输（会拖慢输出速度，非必要不开启）</td></tr>
<tr><td><b>系统重排</b></td><td><code>enableMergedSystemMsg</code></td><td>合并所有系统消息为一条并置于消息列表首位，适配要求系统消息唯一且置顶的严格模型</td></tr>
<tr><td><b>记忆系统</b></td><td><code>enableMemories</code> · <code>memoryBucket</code></td><td>用户级跨会话记忆：模型主动存储 / 检索 / 删除记忆，Bucket 桶隔离（依赖工具开启）</td></tr>
<tr><td><b>循环工程</b></td><td><code>enableLoopEngineering</code></td><td>注入五步工作流提示词，SessionRecordTools 读写 request / plan / checklist / agent 会话记录，跨轮断点续作</td></tr>
<tr><td><b>保留首条用户</b></td><td><code>enableKeepFirstUserMessage</code></td><td>截断上下文时保留首条用户消息，锚定对话主题</td></tr>
<tr><td><b>自动总结</b></td><td><code>enableAutoSummary</code></td><td>消息数逼近 <code>maxHistoryCount</code> 时自动发起会话总结（需与截断开关协同），保留人设与主题后重建上下文</td></tr>
</tbody>
</table>
</div>
<p class="body" style="font-size:12.5px;">所有选项均带 Popover 悬浮说明，零学习成本；状态持久化到 <code>localStorage</code>，按需组合启用。</p>

<div class="panel-title">会话管理 — 3440 行单文件 SPA 的状态底座</div>
<div class="step-list" style="margin-top:0;">
<div class="step">
<div class="no"></div>
<div class="bd"><h4>多会话与自动标题</h4>
<p>左侧边栏会话列表，支持新建 / 删除 / 批量删除；会话标题取首条用户消息自动生成。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>每秒自动持久化</h4>
<p>会话数据每 1 秒自动保存到 <code>localStorage</code>，刷新 / 意外关闭不丢历史。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>上下文窗口治理</h4>
<p><code>maxHistoryCount</code> 限制发送轮数（默认 20），超出丢弃早期历史；亦可让 LLM 将历史总结为简要内容压缩 Token；截断时始终保留首条角色消息，前端本地保留完整历史。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>自动会话总结</h4>
<p>开启 <code>enableAutoSummary</code> 后，消息数达到 <code>maxHistoryCount - 1</code> 自动触发 <code>summary-history</code> 总结请求（提示词特意要求保留未完成任务与待办事项）；总结后保留首条 system 人设 / 首条 user / 最后 echo_truth_content / echo_truth_sync + 总结结果重建上下文，<code>needContinue</code> 标志驱动自动发送 "continue" 继续循环——全程无需用户手动干预。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>导入 / 导出 / 打印</h4>
<p>会话历史可导出为 JSON 再导入恢复；集成 print-js 支持打印对话历史。</p></div>
</div>
<div class="step">
<div class="no"></div>
<div class="bd"><h4>性能与交互增强</h4>
<p>vue-virtual-scroller 虚拟滚动优化大量消息渲染；vue-contextmenujs 右键菜单（删除 / 编辑 / 重做 / 复制 / TTS 播报）；<code>Ctrl+Enter</code> 快捷发送。</p></div>
</div>
</div>
