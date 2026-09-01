<p class="lead">RAG（检索增强生成）是 Agent 的<b>外部记忆</b>。框架提供从文档导入到向量检索的完整流水线，并实现三级记忆体系：Truth（会话事实）、Memory（用户向量记忆）、Context Window（上下文窗口管理）。</p>

<div class="panel">
<div class="panel-title">RAG 流水线</div>
<div class="diagram-panel">
<svg viewBox="0 0 760 160" role="img" aria-label="RAG 流水线">
<rect class="svg-node svg-node-teal" x="10" y="20" width="100" height="44" rx="4"/><text class="svg-lbl" x="60" y="42" text-anchor="middle">文档</text><text class="svg-lbl-sm" x="60" y="56" text-anchor="middle">PDF/DOCX/MD/TXT</text>
<path class="svg-line" d="M110,42 L160,42" marker-end="url(#rag-ah)"/>
<rect class="svg-node" x="165" y="20" width="120" height="44" rx="4"/><text class="svg-lbl" x="225" y="42" text-anchor="middle">读取器</text><text class="svg-lbl-sm" x="225" y="56" text-anchor="middle">Markitdown/OCR/Pandoc</text>
<path class="svg-line" d="M285,42 L335,42" marker-end="url(#rag-ah)"/>
<rect class="svg-node svg-node-acc" x="340" y="20" width="120" height="44" rx="4"/><text class="svg-lbl" x="400" y="42" text-anchor="middle">分割器</text><text class="svg-lbl-sm" x="400" y="56" text-anchor="middle">递归文本分割</text>
<path class="svg-line" d="M460,42 L510,42" marker-end="url(#rag-ah)"/>
<rect class="svg-node svg-node-green" x="515" y="20" width="120" height="44" rx="4"/><text class="svg-lbl" x="575" y="42" text-anchor="middle">Embedding</text><text class="svg-lbl-sm" x="575" y="56" text-anchor="middle">向量化</text>
<path class="svg-line" d="M635,42 L685,42" marker-end="url(#rag-ah)"/>
<rect class="svg-node" x="650" y="20" width="100" height="44" rx="4"/><text class="svg-lbl" x="700" y="42" text-anchor="middle">SQLite</text><text class="svg-lbl-sm" x="700" y="56" text-anchor="middle">向量存储</text>
<rect class="svg-node svg-node-teal" x="280" y="90" width="200" height="44" rx="4"/><text class="svg-lbl" x="380" y="112" text-anchor="middle">检索</text><text class="svg-lbl-sm" x="380" y="126" text-anchor="middle">余弦相似度 Top-K</text>
<path class="svg-line" d="M380,74 L380,90" marker-end="url(#rag-ah)"/>
<defs><marker id="rag-ah" markerWidth="10" markerHeight="10" refX="8" refY="5" orient="auto"><path d="M0,0 L10,5 L0,10 Z" fill="#46607a"/></marker></defs>
</svg>
<div class="dg-cap">文档 → 读取 → 分割 → 向量化 → 存储 → 检索</div>
</div>
</div>

<div class="panel">
<div class="panel-title">三级记忆体系</div>
<div class="card-grid">
<div class="card" style="--c:#e8590c;"><span class="idx">T</span><h3>Truth</h3><div class="tagline">会话级事实</div><p>会话范围内的事实记忆，如"用户名叫张三"、"当前项目是 demo"。整体替换模式，每次更新覆盖全部事实。系统提示词中注入 <code>Current Facts: ...</code>。</p></div>
<div class="card" style="--c:#1971c2;"><span class="idx">M</span><h3>Memory</h3><div class="tagline">用户级向量记忆</div><p>跨会话的用户记忆，通过 Embedding 向量化存储，检索时按余弦相似度召回。Bucket 桶隔离不同用户/领域的记忆空间。</p></div>
<div class="card" style="--c:#0b7285;"><span class="idx">C</span><h3>Context Window</h3><div class="tagline">上下文窗口管理</div><p><code>maxHistoryCount</code> 截断历史消息；角色消息（system）始终保留在最前；LRU 策略控制工具声明占用的 Token 数量。</p></div>
</div>
</div>