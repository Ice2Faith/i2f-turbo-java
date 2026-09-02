<p class="lead">RAG（检索增强生成）让 LLM 基于<b>私有文档</b>作答：文档入库时被切分、向量化、持久化到 SQLite；对话时由 <code>rag_search</code> 工具按语义相似度召回片段，作为上下文交给模型。全链路零外部中间件——一个 SQLite 文件即是向量库。</p>

<div class="diagram-panel">

```svg
assets/diagrams/ch09-rag-pipeline.svg
```


<div class="dg-cap">RAG 双管线 — 启动时批量入库，对话时按需检索</div>
</div>

<div class="panel-title">组件清单</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>组件</th><th>实现</th><th>职责</th></tr></thead>
<tbody>
<tr><td>向量模型</td><td><code>HttpOpenAiRagEmbeddingModel</code></td><td>OpenAI 兼容 <code>/embeddings</code> 接口，baseUrl / apiKey / model 可配</td></tr>
<tr><td>向量存储</td><td><code>SqliteRagEmbeddingStore</code></td><td>SQLite 单文件向量库，维度可配，零中间件依赖</td></tr>
<tr><td>调度器</td><td><code>RagWorker</code></td><td>load 入库 / similar 检索的统一入口</td></tr>
<tr><td>切分器</td><td><code>SimpleRecursiveRagTextSplitter</code></td><td>最大分段长度 + 重叠率，递归切分长文</td></tr>
<tr><td>工具</td><td><code>RagTools.rag_search</code></td><td>LLM 主动检索知识库，默认 topN=3，建议小于 10</td></tr>
<tr><td>读取器</td><td><code>ListableRagFileReader</code></td><td>Markitdown（Office）· EasyOCR（图片 / PDF-OCR）· Pandoc（文档转换）按开关组合</td></tr>
</tbody>
</table>
</div>

<div class="callout" style="--c:#0b7285;">
<div class="co-title">文档生命周期 — 加载即归档</div>
<p>入库完成后，<code>loadDocs</code> 将整个 docs 目录移动到 <code>rags_history/history-{yyyyMMddHHmmss}</code> 并重建空目录——<b>同一文档永不被重复向量化</b>，同时保留完整历史可追溯。入库在后台线程异步执行，不阻塞应用启动。</p>
</div>

<div class="panel-title">记忆系统 Memory — 跨会话的用户级长期记忆</div>
<p class="body" style="font-size:13.5px;">RAG 知识库解决的是<b>组织级文档检索</b>，而记忆系统（<code>MemoryTools</code>）解决的是<b>用户级个性化记忆</b>：模型在对话中主动将用户偏好、个人信息、历史结论等保存为向量记忆，后续会话经语义检索召回——让 AI 真正「认识」用户。记忆以 <b>Bucket 桶</b>隔离：默认 <code>public</code> 桶全局共享，请求经 <code>memoryBucket</code> 字段指定用户专属桶，检索时同时覆盖公共桶与用户桶。</p>

<div class="two-col">
<div>
<div class="panel-title">MemoryTools — 记忆三件套</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>工具</th><th>能力</th><th>标签</th></tr></thead>
<tbody>
<tr><td><code>memory_search</code></td><td>按语义相似度检索历史对话记忆（偏好 / 爱好 / 个人信息等），默认 topN=3</td><td><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>memory_save</code></td><td>保存一条记忆内容，规则：简短精确；写入用户桶（无桶则写 public）</td><td><span class="tag t-o">WRITABLE</span></td></tr>
<tr><td><code>memory_delete</code></td><td>按记忆 ID 列表批量删除</td><td><span class="tag t-o">WRITABLE</span></td></tr>
</tbody>
</table>
</div>
<p class="body" style="font-size:12.5px;">记忆存储基于 <code>SqliteBucketRagMemoryStore</code>（带桶分区的 SQLite 向量库），与 RAG 共享同一个 <code>RagEmbeddingModel</code> 向量化服务——由 <code>ai.rags.memory.enable</code> 开关独立控制装配。</p>
</div>
<div>
<div class="panel-title">Bucket 桶隔离模型</div>
<div class="pkg-tree"><span class="cm"># 记忆桶的读写规则</span>
BucketRagEmbeddingStore
├── public 桶 <span class="cm"># 全局共享记忆（默认写入桶）</span>
└── {memoryBucket} 桶 <span class="cm"># 用户专属记忆（请求级指定）</span>
<span class="cm"># memory_search 检索范围</span>
检索桶 = [public] + [memoryBucket]
<span class="cm"># 即：公共记忆 + 用户私有记忆一并召回</span>
<span class="cm"># memory_save 写入规则</span>
写入桶 = memoryBucket ?? public
<span class="cm"># 有用户桶写用户桶，无则写公共桶</span></div>
</div>
</div>

<div class="panel-title">Memory / RAG / Truth — 三级记忆架构与适用场景</div>
<p class="body" style="font-size:13.5px;">框架提供三个层次互补的「记忆」机制，分别对应<b>会话级、用户级、系统级</b>三种信息生命周期——它们共享向量检索底座，但作用域、写入方式与消费方式截然不同：</p>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>维度</th><th>Truth 事实注入</th><th>Memory 记忆系统</th><th>RAG 知识库</th></tr></thead>
<tbody>
<tr><td><b>作用域</b></td><td><span class="tag t-r">会话级</span> 单次聊天会话</td><td><span class="tag t-o">用户级</span> 跨会话持久，按用户桶隔离</td><td><span class="tag t-b">系统级</span> 全局共享，所有用户可见</td></tr>
<tr><td><b>写入者</b></td><td>LLM 主动调用 <code>store_truth</code></td><td>LLM 主动调用 <code>memory_save</code></td><td>管理员投放文档，启动时批量入库</td></tr>
<tr><td><b>存储方式</b></td><td>纯文本 <code>truthContent</code>，前端持有</td><td>向量化 + SQLite 桶存储，服务端持久</td><td>向量化 + SQLite 存储，服务端持久</td></tr>
<tr><td><b>更新策略</b></td><td><b>整体替换</b>（旧事实被新事实覆盖）</td><td><b>增量追加</b>（可逐条删除）</td><td><b>只读</b>（LLM 不可写，仅检索）</td></tr>
<tr><td><b>消费方式</b></td><td>每轮<b>自动注入</b>为系统消息（被动）</td><td>LLM 按需调用 <code>memory_search</code>（主动）</td><td>LLM 按需调用 <code>rag_search</code>（主动）</td></tr>
<tr><td><b>请求开关</b></td><td><code>enableTruth</code></td><td><code>enableMemories</code> · <code>memoryBucket</code></td><td><code>enableRags</code></td></tr>
<tr><td><b>适用场景</b></td><td>当前任务的关键结论 / 数字 / 规则，防遗忘防幻觉</td><td>用户偏好、个人信息、历史结论等跨会话个性化记忆</td><td>组织内部文档、规章制度、产品手册等私有知识检索</td></tr>
<tr><td><b>类比</b></td><td>工作台上的便签纸（随手写、随时换）</td><td>个人笔记本（长期积累、按人隔离）</td><td>公司图书馆（公共藏书、只读借阅）</td></tr>
</tbody>
</table>
</div>
<div class="callout" style="--c:#862e9c;">
<div class="co-title">协作关系 — 三层记忆如何配合</div>
<p>一次典型对话中三层记忆可同时生效：模型从 <b>RAG</b> 检索到公司报销制度（系统级知识），从 <b>Memory</b> 召回「该用户出差偏好高铁二等座」（用户级偏好），再把本轮计算出的报销金额经 <b>Truth</b> 存储（会话级事实）——后续轮次自动注入，不再重复计算。三者由独立开关控制，按需组合：<code>enableRags</code> + <code>enableMemories</code> + <code>enableTruth</code>。</p>
</div>
