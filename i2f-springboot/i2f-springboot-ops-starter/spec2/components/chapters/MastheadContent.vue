<template>
    <div>
        <div class="kicker">i2f.springboot.ops.openai · 技术全景文档</div>
        <h1>OpenAI 兼容<br><span class="hl">AI 对话框架</span>设计全景</h1>
        <p class="sub">
            内嵌于运维控制台的<b>胖客户端式</b> AI 对话子系统：以 OpenAI 兼容协议为骨架，
            以 <b>Re-Act 循环</b>为心脏，串联 Function Calling 工具体系、技能系统、RAG 知识库、
            MCP 动态工具与国密全链路安全传输，兼容 OpenAI / DashScope / Ollama / vLLM 等一切兼容服务。
        </p>
        <div class="term-line">
            <span class="prompt">ops&gt;</span>
            <span>POST /ops/open-ai/stream</span>
            <span class="arrow">→</span>
            <span class="sse">text/event-stream</span>
            <span class="caret"></span>
        </div>

        <div class="reveal">
            <DiagramPanel src="assets/diagrams/masthead-loop.svg" caption="核心消息循环 — 一次对话即一轮消息列表的维护与推理" />
        </div>
    </div>
</template>
<script>
    export default {
        name: 'MastheadContent',
        components: {
            DiagramPanel: '../md/DiagramPanel.vue'
        }
    };
</script>
