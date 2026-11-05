---
name: user-computer-env
description: 本机（ICE2FAITH，Windows 10 19045 / 2C4T / 24GB / GTX1050）的完整开发环境档案与硬性操作约束。涵盖 JDK 17/8/21、Maven、Gradle、Go 1.23、Node 24、Python 3.10(torch cu124)、C#(.NET FW 4.8 无 dotnet SDK)、MSVC(VS2013)、MinGW、WinDbg/WDK、CUDA 12.4、Vulkan、OpenGL 4.6、OpenCV，以及 936 代码页乱码防治、E/G 盘文件存放约定、提权与协作方式。当需要运行/编译/安装/调试任何东西、选择语言或工具版本、处理中文乱码、决定文件写到哪个盘、或遇到"命令找不到""版本不符""编码异常"时，先加载本技能，不要重新探测。
whenToUse: 在本机开工前先看一眼；遇到命令不存在、版本不对、中文乱码、路径/磁盘选择、权限、GPU 或 CUDA 疑问时务必加载。
---

# 本机环境速查（ICE2FAITH）

> ## ✅ 我是"存款"，不是"流水"——存在即免探测
>
> **只要本技能还在、且 `hostname` 仍是 `ICE2FAITH`，就不要再跑 `user-computer-env-detect`。**
> 用户问"能不能编译""为什么命令找不到""装哪个版本""中文为什么乱码"——**先在这里查**，
> 本文件几乎已经写了答案。探测要花几十秒，查档案只要几秒。
>
> 只有两种情况需要动：
> | 情况 | 做法 |
> |---|---|
> | **用户明确要求**重新检测 / 环境确实变了（装了新 SDK、清了盘、换了驱动） | 跑 detect → 用结果**更新本技能**（不是新建） |
> | 本技能记录与实测**冲突**（例如档案说 `dotnet` 不存在，但用户说刚装了） | **只定向复测那一项**，然后改对应段落 |
>
> 闭环：`detect → 沉淀成本技能 → 下次直接读` 。**避免重复探测是这套机制存在的意义。**

> **适用性自检**：`hostname` / `$env:COMPUTERNAME` 应为 `ICE2FAITH`（**大小写不敏感**，`hostname` 实际返回 `Ice2Faith`），
> 机型应为 `HASEE W65KJ1_KK1`。若两者都对不上，说明换了机器 → 本技能全部结论作废，需重新探测并沉淀为新技能。
> **采集方式**：全部为实跑命令 / 注册表 / 现场编译验证（2026-11-04 ~ 11-05），无臆测。
> **权威来源就是本技能目录** `C:\Users\Ice2Faith\.dsh\skills\user-computer-env\`，深度资料见 `references/environment-full.md`。
> **不要引用任何工作区副本**：用户可能随时切换或移动工作区，本技能是唯一可靠来源；发现环境变化就改本技能，别再维护第二份。

---

## 1. 30 秒速览

| 维度 | 事实 |
|---|---|
| OS | Windows 10 教育版 **19045.6456** x64，代码页 **936(GBK)**，长路径已开 |
| 机型 | HASEE **W65KJ1_KK1** 笔记本，AMI BIOS 2017 |
| CPU / 内存 | **Pentium G4560 2核4线程**（弱，并行别超过 `-j 4`）/ **23.9 GB** |
| GPU | **GTX 1050 4GB sm_61**（唯一 CUDA 设备，索引 **0**）+ Intel HD610（仅显示输出，非 CUDA） |
| 磁盘 | **C: 仅剩 7.8G**（禁止写）/ D: 23.6G（默认不放）/ **E: 58G、G: 81G（首选）** / F: 82G。E、F、G 是同一块机械盘 |
| 桌面/文档 | 已重定向到 **E:**（`E:\MySystemDefaultFiles\`），丢桌面不吃 C 盘 |
| **AI 落盘根** | **`E:\01-ai-workspace\`**（次选 `G:\01-ai-workspace\`）—— 见 §6 |

---

## 2. 铁律（每条都是踩过的坑）

1. **落盘只进 `<盘>:\01-ai-workspace\`**（E: 优先，次 G:/F:）。**绝不写 C: 和 D:**，**绝不往用户已有目录塞文件**（详见 §6）。
2. **文本落盘必须显式 `encoding='utf-8'`** —— 本机 Python `open()` 默认 **cp936**、Java `file.encoding=GBK`、.NET `Encoding.Default=gb2312`。
3. **不要用终端显示判断编码**，改看字节/hex；脚本自身输出用 **ASCII**，否则诊断信息自己就乱码。
4. **`dotnet` 不存在** —— 无法做 .NET Core/5+/SDK 风格项目，只能 .NET Framework 4.x + `csc`/MSBuild 12。
5. **`cl.exe` 不在 PATH** —— 必须 `cmd /c "call "…\vcvarsall.bat" amd64 && cl …"` 一条命令内激活。
6. **PowerShell 里 `npm`/`pnpm` 会因执行策略失败** —— 用 `npm.cmd` / `pnpm.cmd`，或 `-ExecutionPolicy Bypass`。
7. **没有 PowerShell 7** —— 当前 shell 是 **Windows PowerShell 5.1**：无 `??`、无三元、无 `&&`、无 `-Parallel`。
8. **`gcc`/`cmake`/`make`/`clang`/`7z` 都不在 PATH** —— 用全路径调用。
9. **CUDA_VISIBLE_DEVICES 必须是 `0` 或未设** —— 设成 `1` 会让 GPU 彻底消失（本机只有索引 0）；`-1` 是"禁用 CUDA"。
10. **pandoc 出不了 PDF**（无 LaTeX）→ 走 `pandoc → docx → soffice --convert-to pdf`。
11. **`cv2.imshow` 不可用**（OpenCV 是 headless 版），只能 `cv2.imwrite`。
12. **改环境变量后必须换新进程链**（注销/重登或重启 explorer），子进程继承父进程环境块，不读注册表。

---

## 3. 按任务路由

| 你要做 | 看这里 |
|---|---|
| 复制粘贴命令（Java/Go/Node/Python/C#/C++/CUDA/音视频/文档） | `references/command-cheatsheet.md` |
| 中文乱码、编码、BOM、`chcp`、各语言编码参数 | `references/encoding.md` |
| CUDA / Vulkan / OpenGL / OpenCV / GPU 性能 | `references/graphics.md` |
| 已知问题清单、版本短板、缺失组件 | `references/pitfalls.md` |
| 完整档案（硬件、注册表、版本全部细节） | `references/environment-full.md` |
| **一键体检**（GPU/CUDA/Vulkan/OpenGL/OpenCV/编码） | `scripts\check-all.ps1` |
| 单项自检 | `scripts\cuda-check.py`、`scripts\opencv-check.py`、`scripts\encoding-check.py`、`scripts\encoding-check.ps1`、`scripts\opengl-probe.cs` |

---

## 4. 技术栈版本（一句话版）

| 栈 | 版本 / 位置 | 状态 |
|---|---|---|
| **Java** | **JDK 17.0.11**（`C:\Java\jdk-17`，`JAVA_HOME` 指向它）；另有 1.8.0_201、Corretto **21**（`C:\Java\jdk-21\jdk21.0.11_10`，多一层目录） | ✅ |
| **Maven** | **3.9.11**，`D:\maven\apache-maven-3.9.11`；本地仓库实为 **`D:\maven\mvn`**（`~/.m2/repository` 是符号链接） | ✅ |
| **Gradle** | **8.6**（PATH），`E:\gradle\` 另有 4.7/5.4.1/8.8；`GRADLE_USER_HOME=E:\gradle\repository_uap` | ✅ |
| **Go** | **1.23.3**，`C:\Go`，`GOPATH=~\go`，GOPROXY 阿里云；**CGO_ENABLED=0** | ✅ |
| **Node** | **24.20.0**，`E:\NodeJs`；npm 12.0.2；pnpm 10.30.3（store `D:\.pnpm-store`）；yarn 1.22.22 | ✅ |
| **Python** | **3.10.6**，`C:\Python310`；另有 3.6-32 位；`C:\Program Files\Python314` **无 python.exe（坏目录）**；**无 conda** | ⚠️ |
| **C#/.NET** | .NET FW **4.8** + `csc` 4.8.4084（**仅 C# 5**）+ **MSBuild 12 only**；**无 dotnet SDK** | ⚠️ 短板 |
| **MSVC** | **cl 18.00（VS2013）**，`D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC`；有 `vcvarsall.bat`/`link`/`nmake`，**无 MFC/ATL/rc** | ✅ 旧 |
| **MinGW** | TDM-GCC **5.1.0 32 位**，`C:\Program Files (x86)\CodeBlocks\MinGW\bin` | ⚠️ |
| **调试** | WinDbg **6.12(2010, x86)**，`C:\Program Files (x86)\Debugging Tools for Windows (x86)`；WDK 14393 **残缺不可用**；SDK **8.1 完整**；WPT 6.3 在 PATH | ⚠️ |
| **CUDA** | **12.4**（nvcc V12.4.99）+ cuDNN；驱动 581.57 | ✅ |
| **Vulkan** | 设备 **1.4.312**，conformance 1.4.1.3，仅 NVIDIA；**无 Vulkan SDK** | ⚠️ |
| **OpenGL** | **4.6.0 NVIDIA 581.57**（GLSL 4.60），默认走独显 | ✅ |
| **OpenCV** | **5.0.0 headless**（`opencv-python-headless`）+ 残留 `opencv-contrib-python 4.10`；**无 GUI、无 CUDA 构建**，OpenCL 可用 | ⚠️ |

---

## 5. 工具与 shell

| 工具 | 命令 / 路径 |
|---|---|
| **Git** | 2.52.0，`D:\Program Files\Git`（`core.autocrlf=true`）；**git-bash** `D:\Program Files\Git\bin\bash.exe` |
| FFmpeg / ffprobe | **9.0.2** full build，`C:\FFMPEG\bin`（`h264_nvenc`/`hevc_nvenc`/`av1_nvenc`、QSV、AMF 全有） |
| Pandoc | **3.10**（无 LaTeX，出不了 PDF） |
| LibreOffice | **26.8.1**，`soffice` 在 PATH |
| ImageMagick | **7.1.2-27**，命令是 **`magick`**（不是 `convert`） |
| Tesseract | **5.5.0**，语言包 `chi_sim`/`chi_tra`/`eng`/`HanS` |
| 7-Zip | `C:\Program Files\7-Zip\7z.exe`（**不在 PATH**） |
| ADB | 1.0.26（旧），`C:\ADB`，附 `fastboot` |
| SQL | `sqlcmd`（SQL 2012）+ **LocalDB `(localdb)\v11.0`** |
| Ollama | 0.32.1，**当前未运行**；模型 deepseek-r1 / qwen2.5-coder / qwen3-embedding / nomic-embed-text-v2-moe |
| llama.cpp | `D:\llama.cpp`（17GB），**仅 CPU + Vulkan 后端**，无 CUDA 后端 |
| shell | **Windows PowerShell 5.1**、cmd、git-bash（**无 pwsh 7 / WSL / Docker**） |
| 安全软件 | **火绒 6.0.12.3** —— 构建报"拒绝访问"先查它的日志 |

---

## 6. 文件存放约定（**硬性**）

### 6.1 唯一落盘入口：`<盘>:\01-ai-workspace\`

**AI 产生的任何文件，都只能放在目标盘根目录的 `01-ai-workspace\` 下。**
绝对不要往用户已有的目录里塞文件（不要碰 `E:\home`、`E:\data`、`G:\05 下载项`、`G:\01 Ice2Feathers`、桌面、文档等），
也不要新建其它盘根目录。这样用户能一眼找到 AI 的产出，并自行按他的习惯归档。

**本机位置已固定 —— 不要再询问用户选哪个盘：**

```
E:\01-ai-workspace\        <- 主工作区（首选）
G:\01-ai-workspace\        <- 归档 / 备用（大文件、媒体、备份）
```

| 场景 | 用哪个 |
|---|---|
| 代码、构建产物、报告、图片、临时文件 | **`E:\01-ai-workspace\`** |
| 大文件、模型权重、媒体、备份快照 | **`G:\01-ai-workspace\`** |
| 单次产物大小不确定 | 先写 E:，超过约 2 GB 再转 G: |

* 两处骨架都已建好，直接落子目录即可，**不需要每次做磁盘对比再挑**。
* **C:、D: 一律禁止**（C: 仅剩 7 GB；D: 已被既有工具占用，用户明确要求尽量不用）。
* `user-computer-env-detect` 里「按剩余空间优选出推荐盘并询问用户」的流程，**只用于探测未知机器**，不适用于本机。

### 6.2 标准子结构（所有盘一致）

```
<盘>:\01-ai-workspace\
  projects\     代码工程、产物（下按 <项目名>\ 分层）
  temp\         临时/中间文件，任务结束即删
  backups\      备份、归档快照
  images\       生成的图片（截图、图表、渲染图）
  documents\    生成的文档（docx/pdf/xlsx/pptx/md）
  data\         数据集、模型权重、大文件下载
  logs\         运行日志、构建日志
  scripts\      一次性脚本（长期复用的脚本归技能 scripts/）
```

需要新类别时**在 `01-ai-workspace` 下增加**，不要散落到别处。

### 6.3 纪律

1. **禁止**写 `C:\`（含临时文件）；**禁止**写 `D:\`（除用户明确要求）。
2. 一个任务一个目录：`01-ai-workspace\projects\<项目>\<输入|输出|日志|脚本>`，不混放。
3. `temp\` 用完必须清；残留超过一个任务的属于违规。
4. 目录名用**小写英文 + 短横线**，避免中文/空格/`(x86)` 类路径（编译工具链易踩坑）。
5. 注意 `E:`/`F:`/`G:` 是**同一块机械盘**，随机 I/O 慢，别当 SSD 用。
6. 买东西似的"顺手放一下"是禁止的：先问「该落在 01-ai-workspace 哪个子目录」。

> 历史包袱：`E:\home`、`E:\data`、`G:\05 下载项` 等是用户自己既有的分类，**只读不写**。
> 此前 AI 在 `D:\ai-agent-projects\dsh-workspace` 留过工具脚本与档案副本，已清理并全部迁入本技能，不要回退到那里。

---

## 7. 协作方式（用户是程序员）

* **提权可用**：账户在 Administrators 组，`ConsentPromptBehaviorAdmin=0`（不弹窗）→ `Start-Process -Verb RunAs` **静默提权成功**（实测 `High Mandatory Level`）。
* 但动系统级配置（`HKLM`、服务、驱动、hosts、防火墙）前**先说一句**；破坏性操作**先问**；做完**用只读命令复核**并把结果贴出来。
* **只能交给用户**：GUI 安装器、交互登录/凭据、物理操作、重启 DSH/注销、以及"选哪个方案"的决策。
* 🔐 该 UAC 配置意味着本机任何程序都能静默提权 —— 别跑来源不明的 exe。

---

## 8. 一键体检

```powershell
powershell -ExecutionPolicy Bypass -File "C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\check-all.ps1"
```

覆盖 GPU 状态 / CUDA / Vulkan / OpenGL / OpenCV+OpenCL / 系统字符集，结尾给 SUMMARY。
脚本用 `$PSScriptRoot` 定位资源，技能目录整体搬迁也能跑。
**产物一律编译到 `%TEMP%`，不落在技能目录里。**