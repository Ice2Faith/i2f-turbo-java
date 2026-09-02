<p class="lead">技能（Skill）是<b>按需取用的提示词 + 脚本资产包</b>：与把所有领域知识塞进系统提示词不同，技能只在 LLM 判断「需要」时才被读取——省 token、降干扰、可热插拔。一个 <code>SKILL.md</code> 文件即一个技能。</p>

<div class="two-col">
<div>
<div class="panel-title">技能目录结构</div>

```text
skills/
├── search_website/
│   ├── SKILL.md   # 技能声明：YAML 头 + Markdown 正文
│   └── script/
│       └── search.py  # 附带脚本，可被 run_skill_script 执行
└── data_report/
    ├── SKILL.md
    └── resources/
        └── template.md  # 静态资源，可被 get_skill_resource 读取
```

<div class="panel-title">SKILL.md 格式</div>

```yaml
---
name: search_website
description: 调用搜索引擎检索实时网页信息
tags: [search, web]
version: 1.0.0
---

# 网站搜索技能

## 何时使用
当用户询问实时信息、新闻、天气……

## 使用步骤
1. 调用 run_skill_script，脚本路径 script/search.py
2. 解析返回的 JSON 结果并归纳回答
```
</div>
<div>
<div class="panel-title">SkillsTools — 技能三件套</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>工具</th><th>能力</th><th>标签</th></tr></thead>
<tbody>
<tr><td><code>get_skill_document</code></td><td>读取技能 SKILL.md 全文</td><td><span class="tag t-g">AUTO</span><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>get_skill_resource</code></td><td>读取技能内资源 / 脚本内容</td><td><span class="tag t-g">AUTO</span><span class="tag t-g">READONLY</span></td></tr>
<tr><td><code>run_skill_script</code></td><td>执行技能内命令行脚本</td><td><span class="tag t-r">EXECUTABLE</span><span class="tag t-o">HUMAN</span><span class="tag t-r">COMMAND</span></td></tr>
</tbody>
</table>
</div>
<div class="panel-title">脚本解释器自动识别</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>扩展名</th><th>解释器</th></tr></thead>
<tbody>
<tr><td><code>.py</code></td><td><code>python</code></td></tr>
<tr><td><code>.pl</code></td><td><code>perl</code></td></tr>
<tr><td><code>.js</code></td><td><code>node</code></td></tr>
<tr><td>其他（Windows）</td><td><code>cmd /c</code></td></tr>
<tr><td>其他（*nix）</td><td><code>sh</code></td></tr>
</tbody>
</table>
</div>
<p class="body" style="font-size:12.5px;">脚本执行带 <b>3 分钟超时</b>（OsUtil.execCmd），工作目录为脚本所在目录；<code>run_skill_script</code> 携带 HUMAN 标签，调用前须人工审批。</p>
</div>
</div>

<div class="callout" style="--c:#2b8a3e;">
<div class="co-title">30 秒热扫描 — 技能即文件，改文件即生效</div>
<p><code>SkillAutoConfiguration</code> 实现 <code>ApplicationRunner</code>：启动即全量扫描 <code>SkillsHelper.scanFileSystemSkills()</code>，随后以 <b>30 秒</b>为周期 <code>scheduleWithFixedDelay</code> 增量刷新到 <code>skillDefinitionMap</code>。新增技能无需重启应用——把目录放进 <code>skills/</code>，半分钟内 LLM 即可发现并使用。</p>
</div>

<div class="callout" style="--c:#0b7285;">
<div class="co-title">技能 vs 工具 vs MCP</div>
<p><b>工具</b>：原子能力单元，如 <code>sql_query_datasource</code>、<code>store_truth</code>。由 <code>@Tool</code> 注解定义，应用启动时注册。</p>
<p><b>技能</b>：领域知识包 = 提示词 + 工具 + 资源。由 <code>SKILL.md</code> 定义，30 秒热扫描，按需激活。</p>
<p><b>MCP 工具</b>：外部服务提供的动态能力，由 MCP 协议发现并加载，LRU 策略淘汰。详见第 10 章。</p>
</div>
