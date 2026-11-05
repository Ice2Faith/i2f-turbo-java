---
name: user-computer-env-detect
description: 跨平台（Windows/Linux/macOS/Android-Termux/WSL/MSYS）的电脑环境探测标准化流程。第一步先定**平台类型与架构**（x64/x86/arm64/arm32），再据此选择命令与探测方式；随后覆盖基本硬件（CPU/内存/磁盘/GPU）、字符编码与字符集、开发工具链（Java/Python/Go/Node/C#/MSVC/MinGW/gcc/clang）、常见工具（pandoc/libreoffice/ffmpeg/opencv/opengl）、图形与计算栈（CUDA/Vulkan/Metal）、用户身份判定（普通人/职场人/程序员）与协作方式（提权、脚本运行）。【强制门禁】若本机已存在 user-computer-env 技能且主机指纹匹配，禁止重复探测，直接读该档案；只有三种情况才允许探测：没有档案、指纹不匹配、用户明确要求重新检测。
whenToUse: 仅在本机没有环境档案、或换了机器/换了平台、或用户明确要求重新检测时。若已有 user-computer-env，先读档案而不是探测。
---

# 电脑环境探测方法论

> ## ⛔ 先决门禁（Gate）：有档案就不要探测
>
> **加载本技能不等于要执行探测。** 动手前必须先过这道门。
>
> ```
> ① 本机是否已有 `user-computer-env` 技能？
>    │
>    ├─ 有 ──→ ② 指纹是否匹配？（hostname / 机型）
>    │          ├─ 匹配 ──→ ③ 用户是否明确要求"重新检测"？
>    │          │            ├─ 没要求 ──→ ④ 直接读档案作答，【不探测】，结束
>    │          │            └─ 要求了 ──→ 走完整探测（S1~S7）
>    │          └─ 不匹配 ──→ 换了机器 → 走完整探测
>    └─ 无 ──→ 走完整探测
> ```
>
> **判定方式**（不要凭印象，要查实）：
>
> ```powershell
> Test-Path "$env:USERPROFILE\.dsh\skills\user-computer-env\SKILL.md"   # True = 已有档案
> hostname                                                            # 与档案里的指纹比对
> ```
>
> 指纹比对**大小写不敏感**：本机 `hostname` 返回 `Ice2Faith`，而档案里记的是 `ICE2FAITH`，属同一台机器。
> 更稳的做法是比对 `$env:COMPUTERNAME` + `Win32_ComputerSystem` 的 `Manufacturer/Model`，两者一起看。
>
> **只有这三种情况才允许启动探测：**
> 1. 本机**没有** `user-computer-env`（首次接触这台机器）
> 2. 有档案但**指纹不匹配**（换了电脑 / 换了凭据）
> 3. **用户明确要求**重新检测（"重新体检""再查一遍""环境变了吗"）
>
> **除此之外一律不探测。** 用户问"为什么命令找不到""这个能不能编译""该装哪个版本"时，
> 先去档案里查 —— 十有八九已经有答案。**探测是成本，档案是资产。**
>
> 闭环：`探测(S1~S7) → 询问沉淀(§8.3) → 生成 user-computer-env → 下次直接读` 。
> 只有"环境确实变了"或"用户要求"才重新进入探测，避免每次开工都跑一遍几十秒的体检。

## 0. 本技能是什么 / 不是什么

| | 技能 | 内容 |
|---|---|---|
| ✅ | **`user-computer-env-detect`（本技能）** | **怎么探测**：流程、命令、判据、容错、身份判定、报告与沉淀 |
| ✅ | `user-computer-env` | **某台机器的探测结果**（事实档案，机器相关） |

**两者关系**：本技能是"探测方法"，`user-computer-env` 是"探测产物"。
探测完成后按 §8 询问用户是否沉淀；沉淀出的技能**复制本技能骨架**，只替换事实部分。

> 再次强调：**已有 `user-computer-env` 且指纹匹配时，先读它，别重复探测。**
> 只有发现偏差时才**定向复测那几项**，而不是重跑全流程。

---

## 1. 九条铁律（每条都是踩过的坑）

0. **先定平台与架构，再定命令**（S-1）。同一件事在 Windows/Linux/macOS/Termux 上命令、路径、编码、提权方式完全不同；拿错命令得到的不是"没有"，而是"错误"。
1. **实测优先，禁止臆造**。每条结论必须来自真实命令/注册表/文件字节；猜的标"（推断）"。
2. **不要假设 PATH 里有命令**。开发者常把工具装在非默认盘（如 `D:\maven`、`E:\gradle`），`where`/`which` 找不到就查环境变量 + 扫常见安装根 + 版本管理器目录。
3. **不要假设盘符/挂载点**。Windows 用 `Win32_LogicalDisk` 枚举；Unix 用 `df -h`。先看再决定落哪。
4. **探测脚本必须容错**：任一组件缺失只记"❌ 未安装"并继续，绝不中断整轮体检。
5. **诊断输出用 ASCII**。非英文系统（尤其代码页 936/932/949）下，中文诊断信息自己就会乱码，干扰判断。
6. **不要用终端显示判断编码**，一律用字节/hex 或"写文件后按显式编码读"。
7. **探测默认只读**。不改系统配置、不装东西、不删文件；确需变更先说明并征得同意。
8. **不污染用户目录**。落盘只进 `<盘>:\01-ai-workspace\`（Windows）或 `$HOME/01-ai-workspace`（Unix），不往既有目录塞文件。

---

## 2. 探测流程总览

| 阶段 | 目标 | 参考 | 脚本 |
|---|---|---|---|
| **S-1** | **平台与架构**：Windows/Linux/macOS/Termux/WSL/MSYS + x64/x86/arm64/arm32 | `references/00-platform-and-arch.md` | `detect-platform.ps1`（Win）/ `detect-platform.sh`（Unix） |
| **S0** | 预检：确认是"新机器"还是"已知机器" | — | — |
| **S1** | 系统基线：OS/CPU/内存/磁盘/GPU/字符集 | `references/01-system-baseline.md` | `detect-system.ps1` |
| **S2** | 开发工具链：Java/Python/Go/Node/C#/C++ | `references/02-toolchains.md` | `detect-toolchains.ps1`（Win） |
| **S3** | 常见工具：git/ffmpeg/pandoc/libreoffice/opencv… | `references/03-tools-and-graphics.md` | `detect-tools.ps1`（Win） |
| **S4** | 图形与计算：CUDA/Vulkan/OpenGL/OpenCV | 同上 | `detect-graphics.ps1`（Win） |
| **S5** | 用户身份判定：普通人/职场人/程序员 | `references/04-persona.md` | `detect-persona.ps1`（Win） |
| **S6** | 系统信息预查询手法（wmic/CIM 等） | `references/01-system-baseline.md` | — |
| **S7** | 报告 + 落盘约定 + 是否沉淀为技能 | `references/05-report.md` | — |

> ⚠️ **脚本适用范围**：`detect-system/toolchains/tools/graphics/persona.ps1` 与 `check-all.ps1`
> **只适用于 Windows（PowerShell）**。Linux/macOS/Termux/WSL 请用 `detect-platform.sh` 得到平台信息后，
> 按 `references/00-platform-and-arch.md` §4 的命令清单分栈探测。

### S-1 平台与架构（**新增的强制第一步**）

```powershell
# Windows
powershell -ExecutionPolicy Bypass -File "<技能目录>\scripts\detect-platform.ps1"
```
```sh
# Linux / macOS / Termux / WSL / git-bash
sh "<技能目录>/scripts/detect-platform.sh"
```

**要确定的三件事**：

| 项 | 取值 | 影响什么 |
|---|---|---|
| **OS 类型** | Windows / Linux / macOS / **Android-Termux** / **WSL** / MSYS(git-bash) / BSD | 决定用哪套命令、哪套脚本、哪个包管理器 |
| **归一化架构** | **x64**（=amd64）/ x86 / **arm64**（=aarch64）/ arm32 / 其他 | 决定二进制选择、编译目标、某些工具是否有发行版 |
| **运行环境细节** | 是否 32 位进程跑在 64 位系统；Apple Silicon 是否 Rosetta 转译；是否有 root/sudo | 决定"看起来是 32 位"是否是假象、装原生还是转译包 |

**必记的四个跨平台陷阱**：

1. `amd64` 就是 **x64**，不是"AMD 专用"；同理 `aarch64` 就是 **arm64**。
2. **git-bash 里 `uname` 说 MINGW64_NT** —— 那是 Windows 上的 POSIX 层，路径仍是盘符，**不是 Linux**。
3. **Windows 32 位进程在 64 位系统**：`PROCESSOR_ARCHITECTURE=x86` 但 `PROCESSOR_ARCHITEW6432=AMD64` → 真机是 x64。
4. **macOS 没有 CUDA**（用 Metal/MPS）；**Termux 通常无 root、无 CUDA、无桌面 GL**。

详细判定表、各平台命令对照、Unix 侧工具链清单 → `references/00-platform-and-arch.md`。

**一键跑全量**：

```powershell
powershell -ExecutionPolicy Bypass -File "<skill目录>\scripts\check-all.ps1" -OutDir "<盘>:\01-ai-workspace\documents\env-detect"
```

建议分阶段读参考文档，不要一次把全部命令倒给用户。

---

## 3. S0 预检 = 执行开头那道门禁

> ⚠️ **本节只在该门禁放行后才执行。** 若本机已有 `user-computer-env` 且指纹匹配、用户也没要求重测，
> 请**直接读档案作答并结束**，不要往下读 §4~§6。

```powershell
Test-Path "$env:USERPROFILE\.dsh\skills\user-computer-env\SKILL.md"   # 已有档案？
hostname                                    # 主机名 → 指纹比对
$PSVersionTable                             # 5.1 还是 7？语法不同
whoami / groups | Select-String 'Mandatory' # 是否提权
Get-ExecutionPolicy -List                   # 能否跑 .ps1
```

**判据**：

| 情况 | 做法 |
|---|---|
| 已有档案 + 指纹匹配 + 用户未要求重测 | ⛔ **不探测**，读档案作答 |
| 已有档案 + 指纹匹配 + 用户要求重测 | 走 S1~S7 全流程，结束后**更新档案**（而非新建） |
| 已有档案但指纹**不匹配** | 换了机器 → 走 S1~S7，结束后沉淀为**新技能**（旧档案保留） |
| 完全没有档案 | 走 S1~S7 全流程 |
| 有档案，但用户报告某项异常 | **定向复测那一项**，对比档案、指出偏差，不要全量重跑 |

⚠️ **先确认 shell 与执行策略**：PowerShell 5.1 无 `??`/三元/`&&`/`-Parallel`；执行策略拦 `.ps1` 时用
`powershell -ExecutionPolicy Bypass -File x.ps1`，**不要**为此改系统策略。

💡 **"定向复测"比"全量重跑"更常见**：用户说"命令找不到了"，就只查那一个工具的 PATH/注册表/安装目录，
几秒钟出结论；不必跑 45 秒的完整体检。

---

## 4. S1 系统基线（必须全测）

**采什么**：OS 版本与构建号、架构、安装日期、主机名、机型/主板/BIOS、CPU（型号/核数/主频/虚拟化）、内存（总量/条数与频率）、页面文件、磁盘（容量/可用/介质 SSD-HDD/卷标）、GPU（型号/显存/驱动）、网络（IPv4、是否虚拟机网卡）、**字符集与代码页**。

**关键点**：

1. **磁盘决定后续一切落盘策略**。先算清楚每个盘剩余空间，找出"禁止写"的盘。
2. **区分 SSD 与 HDD**：`Get-PhysicalDisk` 的 `MediaType`；多个盘符可能同属一块物理盘（同一 HDD 上的 E/F/G 随机 I/O 很慢）。
3. **核显 ≠ 计算卡**：GPU 要区分 iGPU（Intel/AMD 核显，通常非 CUDA）与 dGPU。
4. **字符集务必实测量三处**（详见 `references/06-probe-pitfalls.md`）：
   - 注册表 `HKLM\...\Nls\CodePage` 的 `ACP`/`OEMCP`
   - `chcp`（cmd 报的）
   - `GetConsoleOutputCP()`（实际控制台）
   三者可能互相矛盾，这正是乱码根源。

**字符集最小集**（写进报告）：`ACP`、`OEMCP`、`Encoding.Default`、`CurrentCulture`/`UICulture`、时区、是否开启 UTF-8 Beta。

---

## 5. S2 工具链探测（分层，别只看 PATH）

对每个栈按 **三层递进** 探测：

| 层 | 手法 | 例子 |
|---|---|---|
| L1 命令 | 直接跑版本命令 | `java -version`、`go version`、`python -V` |
| L2 环境变量 | 读 `JAVA_HOME`/`GOROOT`/`CONDA_PREFIX` | `"$env:JAVA_HOME"` |
| L3 磁盘扫描 | 扫常见安装根 + 注册表卸载键 | `D:\Java`、`E:\NodeJs`、`HKLM\...\Uninstall\*` |

**L3 的必要性**：用户常"装了但没配环境变量"。此时必须扫常见路径后**明确告知"需要全路径调用"**。

**各栈必查项**：

| 栈 | 必查 | 易漏点 |
|---|---|---|
| Java | `java`/`javac` 版本；`JAVA_HOME`；**安装了哪几个 JDK**；Maven/Gradle | 多个 JDK 共存；JDK 目录可能多一层（`jdk-21\jdk21.0.11_10`）；`platform encoding` 是否 GBK |
| Python | `python -V`；`pip -V`；**`py -0p` 列出全部解释器**；关键包（torch/numpy/pandas/opencv） | 无 `python.exe` 的**坏目录**；`py` 启动器只认一部分；有无 conda |
| Go | `go version`；`go env GOROOT/GOPATH/GOMODCACHE/GOPROXY`；**CGO_ENABLED** | CGO=0 时依赖 C 的包编不了；有无 64 位 gcc |
| Node | `node -v`；`npm/pnpm/yarn -v`；registry 是否国内镜像 | `.ps1` 包装器被执行策略拦截 → 用 `.cmd` |
| C#/.NET | `dotnet --list-sdks`；`csc.exe` 版本；**MSBuild 版本**；.NET FW 版本 | **完全没有 `dotnet`** 时只能做 .NET Framework |
| C/C++ MSVC | `cl.exe` 版本；`vcvarsall.bat` 是否存在；link/lib/nmake；MFC/ATL | `cl` 几乎从不在 PATH；需要 `call vcvarsall` 激活 |
| MinGW/GCC | `gcc --version`；位数（32/64）；`gdb`/`make` | 32 位 GCC 无法服务 amd64 目标 |

**报告里必须给出**：版本 + 绝对路径 + **调用方式**（是否需全路径 / 是否需先激活环境）+ 是否实测通过（最好跑一个 hello world 验证，别只看版本号）。

---

## 6. S3/S4 常见工具与图形栈

**常见工具**：git（含 git-bash、`core.autocrlf`）、ffmpeg（重点看 **硬件编码器** nvenc/qsv/amf）、pandoc（**有无 LaTeX** —— 决定能否直接出 PDF）、LibreOffice（`soffice` 转换）、ImageMagick（命令是 `magick` 还是 `convert`）、Tesseract（语言包）、7-Zip、adb、sqlcmd、curl/ssh、包管理器（choco/scoop/winget）。

**图形与计算**：

| 项 | 判据 | 常见坑 |
|---|---|---|
| CUDA | `nvcc --version`、`nvidia-smi`、`deviceQuery.exe` | **`CUDA_VISIBLE_DEVICES` 设置错误会让 GPU 消失**；驱动版本 ≠ toolkit 版本 |
| Vulkan | `vulkaninfo --summary` | 核显驱动过旧可能无 ICD；有运行时≠有 SDK |
| OpenGL | **必须现场建上下文实测**（`scripts/opengl-probe.cs`） | 桌面由核显驱动时，GL 上下文可能仍落到独显 |
| OpenCV | `cv2.__version__` + `getBuildInformation()` | headless 版**没有 imshow**；双发行版冲突；`cv2.cuda` 常为 0 设备 |

**必做的一件事**：对每个能跑的东西**实际跑一次最小样例**（编译 + 运行），而不是只看 `--version`。

---

## 7. S5 用户身份判定（决定你该怎么说话）

用**行为信号**判断，不要靠猜。三种身份不是标签，而是**协作档位**。

| 信号 | 普通人 | 职场人 | 程序员 |
|---|---|---|---|
| 开发工具 | 无 | 可能有 | IDE、多语言运行时、版本控制、包管理器 |
| 主用软件 | 浏览器/影音/游戏/聊天 | Office/WPS/邮件/网盘/OA | 终端、编辑器、Docker/WSL、数据库客户端 |
| 命令行证据 | 无 profile | 少量 | `~/.gitconfig`、shell profile、自建 bin 目录、自定义 PATH |
| 提权习惯 | 不会 | 会点"是" | 知道 UAC、会自行提权 |
| 对你命令的反应 | 需要你代劳 | 能复制粘贴 | 能读报错、能反馈日志 |

**判定后调整策略**：

| 身份 | 输出方式 | 命令交办 | 风险处理 |
|---|---|---|---|
| **普通人** | 少术语，结论先行，给截图/文件 | **尽量你代跑**，不要让他敲命令 | 任何改动先解释后果，给"一键恢复" |
| **职场人** | 结果导向，少打断，少装东西 | 少量、单步、可复制 | 优先用现成工具完成任务，不折腾环境 |
| **程序员** | 可给命令、可讨论权衡、可贴日志 | 可直接协作：你提权/装 GUI，他跑脚本 | 可改系统配置，但**先说、后做、再复核** |

详见 `references/04-persona.md`（含提权探测与"只在必要时打断"的原则）。

---

## 8. S6/S7 报告、落盘与沉淀

### 8.1 落盘约定（探测产物写哪）

**核心：绝不假设盘符。**

1. **枚举候选**：所有固定盘（`Win32_LogicalDisk` `DriveType=3`），排除系统盘，排除可用 < 10 GB 的盘。
2. **按可用空间降序排序**，取第一名作为**推荐盘**。
3. **★ 必须询问用户**：告诉用户推荐盘及其可用空间，问「用这块，还是换一块？」
   —— 用户可能已有自己的归档盘，或想把 AI 产物和系统盘分开。
4. **用户确认后**才创建 `<盘>:\01-ai-workspace\` 并落盘。
5. **已存在不等于沿用**：若别的盘上已有 `01-ai-workspace`，不要擅自迁移/切换，先问。
6. **有记录就免问**：如果该机器已有档案（如 `user-computer-env`）并写明了固定落盘位置，
   **直接用，不要再问**。询问只发生在**首次探测未知机器**时。
   例：本机 `ICE2FAITH` 已在档案中固定 `E:\01-ai-workspace`（主）+ `G:\01-ai-workspace`（归档），
   走到这一步应**跳过询问**、直接沿用。

推荐盘 ≠ 一定用它。**规则只负责给出候选，决定权在用户。**
但前提是"这台机器还没有既定约定"；一旦有了，就以既有约定为准。

脚本（先跑它，再把结论拿去问用户）：

```powershell
powershell -ExecutionPolicy Bypass -File "<技能目录>\scripts\select-workspace-drive.ps1"
```

输出：各盘可用空间排名 + 推荐盘 + 是否已存在工作区 + 建议向用户提的问题（脚本不改盘、不建目录）。

**标准子结构**：`projects\`（代码与产物）、`temp\`（中间件，用完删）、`backups\`、`images\`、`documents\`（报告）、`data\`、`logs\`、`scripts\`。
禁止写 C:/D:（除非用户明确指定），禁止往用户既有目录里塞文件。

### 8.2 报告结构（照 `references/05-report.md` 模板）

必备 8 块：①机器概览 ②磁盘与落盘策略 ③字符集与编码 ④工具链（含调用方式）⑤常见工具 ⑥图形栈 ⑦已知问题/短板 ⑧命令速查。
**每条结论标注来源**（实测 / 注册表 / 推断）。

### 8.3 最后一步：询问是否沉淀为技能

探测结束后**必须**明确询问用户：

> 环境已体检完毕。要我把它沉淀成一个**机器专属技能**吗？
> 这样下次开工我能直接读到这台机器的真实情况，不用重复探测。
> （我会以 `user-computer-env-detect` 为骨架，只替换事实部分，装到 `~/.dsh/skills/user-computer-env/`）

用户同意后：

1. 复制本技能骨架 → 新目录
2. 把 `references/*` 的事实部分替换为本次探测结果
3. `SKILL.md` 开头写**适用性自检**（主机名/关键指纹），不匹配即作废
4. 用 `install-skill` 流程安装并校验
5. 告知用户**最终技能目录路径**

模板见 `references/05-report.md`。

---

## 9. 探测时的通用坑（先读再动手）

| 坑 | 表现 | 对策 |
|---|---|---|
| 编码错乱 | 中文变 `?`/`锟斤拷`/`U+FFFD` | 诊断输出用 ASCII；验证看字节 |
| `Get-Content` 误读 | 无 BOM 的 UTF-8 被按 ANSI 读成乱码 | 用 `[IO.File]::ReadAllText($p, UTF8NoBOM)` |
| 环境变量缓存 | 改了注册表但程序仍读旧值 | 子进程继承父进程环境块 → 需新进程链 |
| `wmic` 输出 UTF-16LE | 管道里全是替换字符 | 用 CIM cmdlet 或读文件 |
| 工具输出编码不一 | 有的 GBK、有的 UTF-8 | 重定向到文件，按显式编码读 |
| 命令找不到 | `xxx is not recognized` | 查 env var → 扫安装根 → 查注册表卸载键 |
| 提权静默 | `ConsentPromptBehaviorAdmin=0` 时无弹窗 | 可自行提权，但**必须先说明** |
| 核显干扰 | 以为有 2 块 GPU、2 个 CUDA 设备 | 只有 NVIDIA 计 CUDA，索引从 0 顺排 |

细节见 `references/06-probe-pitfalls.md`。