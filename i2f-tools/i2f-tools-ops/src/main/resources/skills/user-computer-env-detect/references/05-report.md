# S7 报告模板 & 沉淀为技能

## 一、环境报告模板（复制后填空）

````markdown
# <主机名> 开发环境档案

> 采集时间：<YYYY-MM-DD HH:mm>（<时区>）
> 采集方式：实跑命令 + 注册表 + 现场编译验证（非猜测）
> 标注：✅ 实测可运行　⚠️ 可运行但有坑　❌ 不可用/缺失

## 0. 文件存放约定

- 落盘根：`<盘>:\01-ai-workspace\`（优先 E: → G: → F:）
- 禁止写：`<列出可用空间过小的盘>`
- 只读不写（用户既有目录）：`<列出>`

## 1. 机器概览

| 项 | 值 |
|---|---|
| **平台类型** | Windows / Linux / macOS / Android-Termux / WSL / MSYS |
| **归一化架构** | x64 / x86 / arm64 / arm32（附原始 `uname -m`） |
| 是否为 32 位进程跑在 64 位系统 | |
| Apple Silicon 是否 Rosetta 转译（macOS） | |
| 发行版 / 构建号 | |
| 主机名 / 机型 | |
| OS / 内核版本 | |
| CPU（核/线程/主频） | |
| 内存（总量/条数/频率） | |
| GPU（型号/显存/驱动） | |
| 磁盘（各盘容量与可用） | |
| 字符集（ACP/OEMCP/Culture/时区） | |
| 提权状态 / UAC 模式 | |

## 2. 磁盘与落盘策略

| 盘 | 容量 | 可用 | 介质 | 是否允许写 |
|---|---|---|---|---|

> 结论：<哪个盘可写、哪个盘禁止、是否同一物理盘>

## 3. 字符集与编码

| 项 | 值 |
|---|---|
| ACP / OEMCP | |
| `chcp` 报告 | |
| `GetConsoleOutputCP()` | |
| `Encoding.Default` | |
| UTF-8 Beta 是否开启 | |

> 结论：<是否需要全局显式指定编码；推荐做法>

## 4. 工具链

| 栈 | 版本 | 路径 | 调用方式 | 实测 |
|---|---|---|---|---|
| Java / Maven / Gradle | | | | ✅/⚠️/❌ |
| Python / pip | | | | |
| Go | | | | |
| Node / npm / pnpm | | | | |
| C# / .NET | | | | |
| C/C++ (MSVC) | | | | |
| MinGW / GCC | | | | |

## 5. 常见工具

| 工具 | 版本 | 在 PATH | 备注 |
|---|---|---|---|
| git / git-bash | | | |
| ffmpeg（硬件编码） | | | |
| pandoc（能否出 PDF） | | | |
| LibreOffice | | | |
| ImageMagick | | | |
| Tesseract（语言包） | | | |
| 7-Zip / adb / sqlcmd | | | |

## 6. 图形与计算栈

| 项 | 值 | 状态 |
|---|---|---|
| CUDA toolkit / 驱动 | | |
| `CUDA_VISIBLE_DEVICES` | | |
| CUDA 设备数 / 索引 | | |
| Vulkan（apiVersion/conformance） | | |
| OpenGL（GL_VERSION/RENDERER） | | |
| OpenCV（版本/是否 headless/是否 CUDA） | | |

## 7. 已知问题与短板

按优先级列，每条写：**现象 → 根因 → 对策**。

## 8. 命令速查

按栈给出可直接复制的命令（含 shell 与是否提权）。
````

---

## 二、沉淀为机器专属技能（**流程终点，必须询问**）

### 2.1 询问话术

> 环境体检完成。需要我把它沉淀成一个**机器专属技能**吗？
>
> 好处：下次任何会话我都能直接读到这台机器的真实情况，不用重复探测，也不会踩已知的坑。
> 做法：以 `user-computer-env-detect` 为骨架生成 `<技能名>`，只替换事实部分，装到 `~/.dsh/skills/`。

**用户同意 → 走 2.2；不同意 → 至少把报告写到 `<盘>:\01-ai-workspace\documents\`。**

### 2.2 生成步骤

1. **建目录**：`<盘>:\01-ai-workspace\temp\skill-build\<技能名>\`
2. **复制骨架**：把 `user-computer-env-detect`（或已有的 `user-computer-env`）整包复制
3. **改写 `SKILL.md`**：
   - `name` 用 kebab-case（如 `user-computer-env`、`company-pc-env`）
   - `description` 写清"这台机器是什么 + 什么时候该用"
   - **开头必写适用性自检**：主机名 / 机型 / 关键指纹，不匹配即作废
   - 保留：铁律、版本表、存放约定、协作方式、命令速查
   - 删除：探测方法论章节（那是 `-detect` 技能的职责）
4. **替换 `references/`**：把探测结果写入（`environment-full.md` 放完整档案）
5. **保留 `scripts/`**：自检脚本原样带走（用 `$PSScriptRoot` 定位资源）
6. **安装并校验**（见 2.3）
7. **告知用户最终路径**

### 2.3 安装与校验

```powershell
$sb = [scriptblock]::Create((Get-Content -Raw -LiteralPath "$env:USERPROFILE\.dsh\skills\install-skill\scripts\install-skills.ps1"))
& $sb -SourcePath '<暂存父目录>' -DryRun      # 先预演
& $sb -SourcePath '<暂存父目录>' -Force       # 再落盘（用户级）
```

校验清单：

- [ ] `name` 匹配 `^[a-z0-9]+(?:-[a-z0-9]+)*$`
- [ ] `description` 非空
- [ ] 可选键全是 kebab-case（`whenToUse`、`disable-model-invocation`…），写错 camelCase 会**整条丢弃**
- [ ] UTF-8 **无 BOM**
- [ ] 中文用字节验证，不看终端显示
- [ ] 技能列表里出现该名字（DSH 自动刷新，无需重启）
- [ ] 脚本能从技能目录直接跑通
- [ ] 清理暂存目录

### 2.4 技能内容最小集

一个合格的"机器环境技能"至少要有：

1. **适用性自检**（防误用）
2. **30 秒速览表**（让模型一眼感知）
3. **铁律**（踩过的坑，逐条列）
4. **版本与路径表**（含调用方式）
5. **存放约定**
6. **协作方式**（用户身份对应的档位）
7. **一键自检脚本**
8. **完整档案**（`references/environment-full.md`）

---

## 三、维护约定

- **技能是唯一权威**，不要在工作区留副本（工作区会被切换/移动）
- 环境变化（装新 SDK、清盘、改配置）→ 只改技能
- 技能里每条结论尽量标来源（实测/注册表/推断），便于日后判断是否过期
- 定期或在用户报告异常时，重跑 `check-all.ps1` 对比差异