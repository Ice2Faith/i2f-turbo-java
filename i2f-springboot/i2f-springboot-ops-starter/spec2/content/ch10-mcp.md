<p class="lead">MCP（Model Context Protocol）是 Anthropic 提出的工具动态发现协议。本框架不全量注入工具声明（节省 Token），而是让模型经"三步发现"自主加载所需工具，并以 LRU 策略淘汰最久未用的工具。</p>

<div class="panel">
<div class="panel-title">MCP 三步发现流程</div>
<div class="diagram-panel">
<svg viewBox="0 0 760 140" role="img" aria-label="MCP 三步发现">
<rect class="svg-node svg-node-acc" x="10" y="20" width="200" height="44" rx="4"/><text class="svg-lbl" x="110" y="42" text-anchor="middle">Step 1: 列举供应商</text><text class="svg-lbl-sm" x="110" y="56" text-anchor="middle">mcp_list_providers</text>
<path class="svg-line" d="M210,42 L260,42" marker-end="url(#mcp-ah)"/>
<rect class="svg-node" x="270" y="20" width="200" height="44" rx="4"/><text class="svg-lbl" x="370" y="42" text-anchor="middle">Step 2: 列举工具</text><text class="svg-lbl-sm" x="370" y="56" text-anchor="middle">mcp_list_tools</text>
<path class="svg-line" d="M470,42 L520,42" marker-end="url(#mcp-ah)"/>
<rect class="svg-node svg-node-green" x="530" y="20" width="200" height="44" rx="4"/><text class="svg-lbl" x="630" y="42" text-anchor="middle">Step 3: 加载工具</text><text class="svg-lbl-sm" x="630" y="56" text-anchor="middle">mcp_load_tool → 注入声明</text>
<rect class="svg-node" x="290" y="84" width="200" height="36" rx="4"/><text class="svg-lbl" x="390" y="106" text-anchor="middle">LRU 淘汰最久未用工具</text>
<defs><marker id="mcp-ah" markerWidth="10" markerHeight="10" refX="8" refY="5" orient="auto"><path d="M0,0 L10,5 L0,10 Z" fill="#46607a"/></marker></defs>
</svg>
<div class="dg-cap">MCP 动态发现 — 不全量注入，按需加载，LRU 淘汰</div>
</div>
</div>

<div class="callout" style="--c:#e67700;">
<div class="co-title">为什么要不全量注入？</div>
<p>MCP 工具可能非常多（数十个甚至上百个），若全部注入工具声明，将占用大量上下文窗口 Token。本框架仅在系统提示词中注入少量"MCP 工具发现"相关的工具声明，模型需要用到某个 MCP 工具时，先调用 <code>mcp_list_providers</code> 了解有哪些 MCP 服务，再调用 <code>mcp_list_tools</code> 查看具体工具，最后调用 <code>mcp_load_tool</code> 加载——此时该工具的声明才会被注入后续对话。</p>
</div>

<div class="panel">
<div class="panel-title">意图推荐：聪慧的 MCP 工具发现</div>
<p class="body" style="font-size:13.5px;">除了模型主动发现，前端还支持<b>意图推荐</b>：基于用户输入进行语义分析，在模型调用 MCP 工具之前，前端就提前向用户推荐可能相关的 MCP 工具，提升交互效率。用户点击推荐卡片即可自动加载对应工具。</p>
</div>