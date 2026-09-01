<p class="lead">技能系统是 Agent 的<b>领域知识包</b>。每个技能就是一个包含 <code>SKILL.md</code> 的目录，由 <code>SkillAutoConfiguration</code> 每 30 秒热扫描，按需将技能提示词和技能工具注入对话上下文。</p>

<div class="panel">
<div class="panel-title">SKILL.md 格式</div>
<div class="spec-table-wrap">
<table class="spec">
<thead><tr><th>字段</th><th>说明</th></tr></thead>
<tbody>
<tr><td><code>name</code></td><td>技能名称</td></tr>
<tr><td><code>description</code></td><td>技能描述（LLM 决定何时激活）</td></tr>
<tr><td><code>prompt</code></td><td>技能提示词，注入 system 消息</td></tr>
<tr><td><code>tools</code></td><td>技能专用工具列表（脚本/资源）</td></tr>
<tr><td><code>resources</code></td><td>技能附带资源文件列表</td></tr>
</tbody>
</table>
</div>
</div>

<div class="panel">
<div class="panel-title">技能生命周期</div>
<div class="step-list">
<div class="step"><div class="no"></div><div class="bd"><h4>扫描发现</h4><p><code>SkillAutoConfiguration</code> 每 30 秒扫描 <code>skills/</code> 目录，发现含有 <code>SKILL.md</code> 的子目录即注册为一个技能。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>懒加载激活</h4><p>LLM 在对话中提及技能相关领域时，模型通过 <code>skill_list</code> 工具查看可用技能，通过 <code>skill_execute</code> 激活具体技能。激活后技能提示词注入 system 消息。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>脚本执行与资源读取</h4><p>技能可包含可执行脚本（Shell/Python/Groovy），通过 <code>skill_execute</code> 工具触发；技能资源文件通过 <code>skill_list</code> 工具返回路径，供其他工具读取。</p></div></div>
<div class="step"><div class="no"></div><div class="bd"><h4>热更新</h4><p>修改 <code>SKILL.md</code> 或添加/删除技能目录后，最多 30 秒自动生效，无需重启应用。</p></div></div>
</div>
</div>

<div class="callout" style="--c:#2b8a3e;">
<div class="co-title">技能 vs 工具 vs MCP</div>
<p><b>工具</b>：原子能力单元，如 <code>sql_query</code>、<code>file_read</code>。由 <code>@Tool</code> 注解定义，应用启动时注册。</p>
<p><b>技能</b>：领域知识包 = 提示词 + 工具 + 资源。由 <code>SKILL.md</code> 定义，30 秒热扫描，按需激活。</p>
<p><b>MCP 工具</b>：外部服务提供的动态能力，由 MCP 协议发现并加载，LRU 策略淘汰。详见第 10 章。</p>
</div>