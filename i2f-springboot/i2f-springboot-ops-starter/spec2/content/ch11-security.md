<p class="lead">安全体系是本框架的<b>护城河</b>。基于国密算法 SM2/SM3/SM4 实现全链路加密传输，支持输入加密与可选输出加密，时间戳防重放攻击。</p>

<div class="diagram-panel">
<svg viewBox="0 0 720 230" role="img" aria-label="国密加密流程">
<rect class="svg-node svg-node-acc" x="20" y="20" width="160" height="50" rx="4"/><text class="svg-lbl" x="100" y="42" text-anchor="middle">前端</text><text class="svg-lbl-sm" x="100" y="58" text-anchor="middle">生成随机 SM4 密钥</text>
<path class="svg-line" d="M180,45 L240,45" marker-end="url(#sec-ah)"/>
<rect class="svg-node" x="250" y="20" width="160" height="50" rx="4"/><text class="svg-lbl" x="330" y="42" text-anchor="middle">SM4 加密</text><text class="svg-lbl-sm" x="330" y="58" text-anchor="middle">加密 JSON 请求体</text>
<path class="svg-line" d="M410,45 L470,45" marker-end="url(#sec-ah)"/>
<rect class="svg-node" x="480" y="20" width="160" height="50" rx="4"/><text class="svg-lbl" x="560" y="42" text-anchor="middle">SM2 封装</text><text class="svg-lbl-sm" x="560" y="58" text-anchor="middle">SM2 公钥加密 SM4 密钥</text>
<rect class="svg-node" x="20" y="90" width="160" height="36" rx="4"/><text class="svg-lbl" x="100" y="110" text-anchor="middle">SM3 摘要 + SM2 签名</text>
<rect class="svg-node" x="480" y="90" width="160" height="36" rx="4"/><text class="svg-lbl" x="560" y="110" text-anchor="middle">时间戳防重放</text>
<rect class="svg-node svg-node-teal" x="250" y="150" width="160" height="50" rx="4"/><text class="svg-lbl" x="330" y="172" text-anchor="middle">后端</text><text class="svg-lbl-sm" x="330" y="188" text-anchor="middle">SM2 解密 → SM4 解密</text>
<path class="svg-line" d="M720,40 L720,40"/>
<defs><marker id="sec-ah" markerWidth="10" markerHeight="10" refX="8" refY="5" orient="auto"><path d="M0,0 L10,5 L0,10 Z" fill="#e8590c"/></marker></defs>
</svg>
<div class="dg-cap">SM2 密钥封装 + SM4 对称加密 + SM3 摘要 + SM2 签名</div>
</div>

<div class="panel">
<div class="panel-title">加密流程细节</div>
<div class="step-list">
<div class="step"><div class="no"></div><div class="bd"><h4>生成随机 SM4 密钥</h4><p>前端每次请求生成一个随机的 128-bit SM4 对称密钥。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>SM4 加密请求体</h4><p>使用 SM4 密钥对 JSON 请求体进行对称加密（CBC 模式）。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>SM2 密钥封装</h4><p>使用后端提供的 SM2 公钥对 SM4 随机密钥进行非对称加密，确保只有持有私钥的后端能解密。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>SM3 摘要 + SM2 签名 + 时间戳</h4><p>对原始请求体计算 SM3 哈希摘要，用 SM2 私钥签名；附加时间戳（默认 5 分钟窗口防重放）。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>后端解密验证</h4><p>SM2 解密 SM4 密钥 → SM4 解密请求体 → 验证签名 → 验证时间戳。全部通过后进入业务逻辑。</p></div></div>
</div>
</div>

<div class="callout" style="--c:#c92a2a;">
<div class="co-title">可选输出加密</div>
<p>SSE 流式输出也支持 SM4 加密：前端在请求中设置 <code>encryptOutput: true</code>，后端将每个 SSE chunk 的 content 部分用同一 SM4 密钥加密后发送，前端在每个 chunk 收到后解密再渲染。此功能默认关闭，因为 SSE 流本身已是 HTTPS 传输，且流式加解密会增加 CPU 开销。</p>
</div>