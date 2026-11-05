# 本机开发环境档案（ICE2FAITH）

> **用途**：让 AI 代理在后续任何会话里"一读即知"本机环境，不必重复探测。
> **读者**：本机用户是一名**程序员**，可直接参与协作（提权、跑脚本、装 GUI 安装器）；协作方式见 **§0.4**。
> **权威性**：本文档的唯一权威副本在本技能目录内（`~/.dsh/skills/user-computer-env/references/`）。
> 不存在任何工作区副本——工作区可能被切换或移动，请勿另存第二份。
> 采集时间：2026-11-04 11:30 起；2026-11-05 补充图形栈 / 协作约定 / 字符集；**2026-11-05 调整存放约定为 `01-ai-workspace` 方案**
> 采集方式：实跑版本命令 + 注册表清单 + 目录探测 + 现场编译探测程序（**非**猜测）
> 标注：✅=已实测可运行　⚠️=可运行但有坑　❌=不可用/缺失

---

## 0. 文件存放约定（**动手前先看这一节**）

> **第一原则**：AI 产出的文件只能进 `<盘>:\01-ai-workspace\`。
> **不要**往用户既有目录里加文件，**不要**新建其它盘根目录，**不要**写 C:/D:。
> 权威版本是本技能目录（`~/.dsh/skills/user-computer-env/`），**不引用任何工作区副本**——工作区可能被切换或移动。

### 0.1 硬约束

| 盘 | 可用 | 定位 | 允许新写？ |
|---|---|---|---|
| **C:** | **7.8 GB** | 系统 + 已装工具链 | 🚫 **绝对禁止**（临时文件也算） |
| **D:** | **23.6 GB** | 已装且不便迁移的工具（VS2013 / Maven repo 4.9G / llama.cpp 17G / Git / LibreOffice / JetBrains） | 🚫 **禁止**（用户明确要求尽量不用 D） |
| **E:** | 58.2 GB | 主工作盘 | ✅ **首选** → `E:\01-ai-workspace\` |
| **G:** | 81.0 GB | 大文件 / 媒体 / 归档 | ✅ 次选 → `G:\01-ai-workspace\` |
| F: | 82.5 GB | 标"软件"，较空 | ⚪ 备选 → `F:\01-ai-workspace\` |

**默认落盘顺序：E: → G: → F:**；三者（E/F/G）是同一块 1TB 机械盘，顺序读写没问题，但**随机 I/O 慢**。

> 💡 `桌面` 和 `文档` 已重定向到 **E 盘**（`E:\MySystemDefaultFiles\Desktop|Documents`，注册表 `User Shell Folders` 已验证），
> 丢桌面不吃 C 盘。但 `Pictures/Videos/Music` **仍在 C 盘**，媒体请走 G:。

### 0.2 标准子结构（每盘一致，已建好）

```
<盘>:\01-ai-workspace\
  projects\     代码工程、构建产物（下按 <项目名>\ 再分层）
  temp\         临时/中间文件，任务结束即删
  backups\      备份、归档快照
  images\       生成的图片（截图、图表、渲染图）
  documents\    生成的文档（docx/pdf/xlsx/pptx/md）
  data\         数据集、模型权重、大文件下载
  logs\         运行日志、构建日志
  scripts\      一次性脚本（长期复用的归技能 scripts/）
```

| 内容类型 | 落点 |
|---|---|
| 代码 / 工程 | `E:\01-ai-workspace\projects\<项目名>\` |
| 构建产物 / 跑数结果 | `01-ai-workspace\projects\<项目名>\output\` |
| 临时文件 | `01-ai-workspace\temp\<任务名>\` |
| 图片 | `01-ai-workspace\images\` |
| 文档交付物 | `01-ai-workspace\documents\` |
| 数据集 / 模型权重 | `01-ai-workspace\data\` |
| 日志 | `01-ai-workspace\logs\` |
| 备份 | `01-ai-workspace\backups\` |

需要新类别时**在 `01-ai-workspace` 下增加**，不要散到别处。

### 0.3 纪律（我会在后续工作中遵守）

1. **只写 `01-ai-workspace`**——禁止写 C:/D:，禁止往用户既有目录（`E:\home`、`E:\data`、`E:\NodeJs`、`G:\05 下载项`、`G:\01 Ice2Feathers`、`G:\01-MyReources` 等）添加任何文件；这些目录**只读**。
2. **一个任务一个目录**：`projects\<项目>\<src|output|logs|scripts>`，不混放。
3. **`temp\` 用完必须清**，残留超过一个任务即违规。
4. **路径避免非 ASCII + 空格**：新目录用小写英文 + 短横线（编译工具链对中文/空格路径敏感）。
5. **不动用户的重定向目录与归档结构**（`E:\MySystemDefaultFiles`、`G:\01 Ice2Feathers` 等）。
6. **历史包袱不再沿用**：此前 AI 在 `D:\ai-agent-projects\dsh-workspace` 留过工具脚本与档案副本，已清理并全部迁入本技能目录，不要回退到那里。

### 0.4 协作约定：我是程序员，需要我出手时直接说

> 用户身份：**程序员 / 开发者**，懂命令行、能读报错、会提权。
> 所以遇到我做不了的事，**不要绕路、不要降级方案、不要假装完成**——直接说清楚要我做什么，我们分工做完。

#### 0.4.1 提权能力（2026-11-05 实测，与直觉不同）

| 事实 | 值 |
|---|---|
| 账户 | `ICE2FAITH\Ice2Faith` |
| 是否在本机 **Administrators** 组 | ✅ **是**（`net localgroup Administrators` 含 Ice2Faith） |
| 当前 DSH 进程是否已提权 | ❌ **否**（`IsElevated=False`，UAC 拆分令牌） |
| `EnableLUA` | `1`（UAC 开启） |
| `ConsentPromptBehaviorAdmin` | **`0` = Elevate without prompting（提权不弹窗）** |
| 实测 `Start-Process -Verb RunAs` | ✅ **静默成功**，子进程 `High Mandatory Level`，无 UAC 弹窗 |

**推论**：我**可以自己完成管理员操作**（起一个提权子进程干活即可），不需要每次都打断你。

**但我仍遵守三条自我约束**：
1. **先说明**：动系统级配置（`HKLM`、服务、驱动、hosts、防火墙、计划任务）之前，先说一句要做什么、影响是什么。
2. **后复核**：做完立刻用只读命令验证，并把结果贴给你看，不让你猜。
3. **破坏性/不可逆的必须先问你**：删数据、改分区、卸软件、改启动项、动注册表关键键、联网改内网配置 —— 一律先确认再动手。

> 🔐 **安全提醒（值得你了解）**：`ConsentPromptBehaviorAdmin=0` 意味着**本机任何以你这个账户运行的程序，都能静默提权到管理员**，不需要你点任何"是"。
> 这是便利性与安全性的取舍，很多企业环境会设成 `5`（默认，弹窗确认）或 `2`。你既然知道自己在做什么，保持现状也合理，但**别在这台机器上跑来源不明的 exe**。

#### 0.4.2 分工边界

| 情形 | 谁做 | 说明 |
|---|---|---|
| 读写文件、跑编译/测试、下载、装用户级包 | **我直接做** | 不问（审批已是 never，本就不会弹） |
| 需要管理员的操作 | **我可做**（提权子进程） | 但按 0.4.1 先说明、后复核 |
| **GUI 操作**：VS Installer 勾组件、驱动向导、Store 应用、系统设置面板 | **交给你** | 我无 GUI 自动化能力 |
| **需要交互登录/凭据**：git 凭据、公司内网 VPN、浏览器 OAuth、DB 账号密码 | **交给你** | 我不持有、也不该持有你的凭据 |
| **物理操作**：插拔设备、BIOS/UEFI、长按电源 | **交给你** | |
| **重启 DSH / 注销 / 重登** | **交给你** | 我重启自己会中断会话 |
| **选择题**：装哪个版本、留哪套 OpenCV、要不要迁移数据 | **交给你定** | 我给方案和权衡，你拍板 |
| 只读核查、日志分析、根因定位 | **我直接做** | 包括复核你执行后的结果 |

#### 0.4.3 我的交接格式（要你动手时）

给你**一个可直接复制的块**，并明确四件事：

1. **用哪个 shell**：`管理员 cmd` / `管理员 PowerShell` / `Git Bash` / 普通 cmd
2. **怎么开**：如 `Win+X → 终端(管理员)`，或 `Start-Process powershell -Verb RunAs -ArgumentList '-NoExit'`
3. **粘贴什么**：单条、无占位符、路径已加引号、可直接回车
4. **期望看到什么**：正常输出长什么样；异常时把哪几行回给我

#### 0.4.4 双向确认（关键）

* 你执行完**不用转述结果**，我也不靠转述下结论 —— 我会自己用只读命令**复核**再把结论告诉你。
* 例：上一轮改 `CUDA_VISIBLE_DEVICES`，你改完后我是去读注册表 + 查进程链 + 跑 `deviceQuery` 确认的，不是听你说"改好了"。
* 若复核发现没生效，我会直接指出卡在哪一层（如 §4.1 的"Explorer 环境块未刷新"），不含糊说"可能没生效"。

#### 0.4.5 待办协作项

| 事项 | 状态 | 谁做 |
|---|---|---|
| `CUDA_VISIBLE_DEVICES` 改 `0` | ✅ 完成（11-04 你改 / 11-05 我复核） | 你（当时需管理员） |
| 清理 C 盘 / 迁移 pagefile | ⏳ 待办 | 我可做，但删东西要先问你 |
| 卸载冲突的两套 OpenCV | ⏳ 待办 | 我可直接做（纯 pip） |
| PowerShell 执行策略改 `RemoteSigned` | ⏳ 可选 | 我可直接做（用户级） |
| 装 .NET 8 SDK / VS2022 Build Tools / Vulkan SDK | ⏳ 按需 | 安装器是 GUI，需你点；或我用静默参数试 |
| 重编 llama.cpp 带 CUDA 后端 | ⏳ 按需 | 我可做（长任务，约 20–40 分钟） |

---

## 1. 机器概览

| 项 | 值 |
|---|---|
| 主机名 | `ICE2FAITH` |
| 机型 | **HASEE W65KJ1_KK1**（神舟笔记本，主板 AMI BIOS 1.05.06 / 2017-04-13） |
| 操作系统 | Windows 10 教育版　10.0.19045.6456 (x64) |
| 安装日期 | 2026-10-02（系统较新） |
| 上次启动 | 2026-10-29 08:25 |
| CPU | Intel **Pentium G4560** @3.50GHz，**2 核 4 线程**，L3 3MB |
| 虚拟化 | VT-x 已启用，SLAT 支持 ✅（可跑 VMware/WSL2） |
| 内存 | **23.9 GB**（Samsung 16GB@3200 + 8GB@2400 混插，SODIMM→笔记本） |
| 内存可用 | ~10.9 GB |
| 页面文件 | `C:\pagefile.sys` 4GB + `D:\pagefile.sys` 8GB |
| 屏幕 | 1920×1080 @60Hz |
| 语言/代码页 | zh-CN，**代码页 936 (GBK)** ⚠️ |
| 长路径支持 | `LongPathsEnabled = 1` ✅ |
| 网络 | WLAN `10.23.70.251/24`；VMware VMnet1 `192.168.243.1`、VMnet8 `192.168.233.1` |

**CPU 是明显瓶颈**：2 核 4 线程。并行编译（`mvn -T`、`gradle --parallel`）、多子代理并发、本地大模型推理都会很吃力；`-j` 建议不超过 4。

### 1.1 磁盘

| 盘 | 容量 | 可用 | 介质 | 内容定位 |
|---|---|---|---|---|
| **C:** | 118.6 GB | **7.8 GB** 🔴 | SAMSUNG PM871b 128GB SSD | 系统 + 部分工具链 |
| **D:** | 119.2 GB | 23.6 GB | SAMSUNG MZNLF128 SSD | 安装目录主战场（Git/Maven/LibreOffice/VS2013/JetBrains/llama.cpp） |
| **E:** | 310.1 GB | 58.2 GB | ST1000LM048 HDD | 标"系统"：gradle、NodeJs、nuget、各类数据 |
| **F:** | 311.0 GB | 82.5 GB | 同上 HDD | 标"软件" |
| **G:** | 310.4 GB | 81.0 GB | 同上 HDD | 标"文档"：下载、docker-images、webdriver |

* 3 块物理盘：2× 128GB SATA SSD + 1× 1TB SATA HDD，全部 `Healthy`。
* **C 盘仅剩 7.8GB，已到危险线**（Windows 占 27.5GB、Program Files 14GB、PF(x86) 12.8GB、Python310 6.1GB、ProgramData 4.8GB）。
* D 盘看似宽裕但只放得下 23.6GB，安装新工具优先 **E/F/G**。
* 注意：**E/F/G 是同一块机械硬盘**，随机 I/O 慢——大仓库（Maven/Gradle/Go 模块）放这里会拖慢构建。

### 1.2 GPU（**两块适配器，但只有 1 个 CUDA 设备**）

| 卡 | PCI 地址 | 分类 | 显存 | 驱动 | 计算能力 |
|---|---|---|---|---|---|
| Intel HD Graphics 610 | `00:02.0` (`VEN_8086&DEV_5902`) | 核显 iGPU | 1GB 共享 | 21.20.16.4550（**2016 年**）⚠️ | ❌ **不是 CUDA 设备** |
| **NVIDIA GeForce GTX 1050** | `01:00.0` (`VEN_10DE&DEV_1C8D`) | 独显 dGPU | **4096 MiB** | 581.57 ✅ | ✅ **CUDA 0 号（唯一）**，sm_61 |

两者状态均 `OK / CM_PROB_NONE`，无禁用、无故障。

**设备索引对照（极易混淆，务必分清）**：

| 视角 | 看到的设备 | 说明 |
|---|---|---|
| PCI 总线 | Intel=00:02.0，NVIDIA=**01**:00.0 | NVIDIA 在"1 号"总线上 |
| **CUDA / NVML / nvidia-smi** | **只有 1 个：索引 0** = GTX 1050 | 只枚举 NVIDIA 卡，按 0 连续编号 |
| Vulkan | 只有 `Vulkan0` = GTX 1050 | Intel 2016 驱动无 Vulkan ICD |
| OpenCL | 仅 Intel **CPU** 运行时（DriverStore 内 `IntelOpenCL64.dll`），无 `igdrcl64.dll` | 核显无 OpenCL |

> ⚠️ **`CUDA_VISIBLE_DEVICES=1` 的由来八成是混淆了 PCI 总线号（NVIDIA 在 01:00.0）**。CUDA 的可见索引是"NVIDIA 设备从 0 顺排"，与 PCI 总线号无关。本机 NVIDIA 只有一个 → 合法索引**只有 0**。

* `nvidia-smi` 报驱动支持 **CUDA 13.0**；本机 Toolkit 是 **12.4**（正常，向下兼容）。
* GTX 1050 = Pascal **compute capability 6.1**：5 个 SM / **640 CUDA Cores**，支持 fp32/fp16，**无** Tensor Core、**无** bf16。编译 kernel 用 `-arch=sm_61`。
* 实测 FP32 性能（预热至 P0，SM 1354MHz）：4096² 矩阵乘 **1.43–1.47 TFLOPS**，占用 200 MiB 显存。
  冷启动首测仅 ~0.49 TFLOPS（P8 / SM 139MHz），属正常降频，**做性能对比前务必预热**。
* 显存仅 4GB：训练基本不可行，只能推理/小模型微调；7B FP16 需 ~14GB，必须量化。
* 显示输出由 **Intel 核显**驱动（1920×1080@60Hz），NVIDIA 是纯计算/渲染卡（WMI `Availability=8`）——对 CUDA 反而是好事：无超时重置风险，4GB 几乎全可用。
* 图形 API 实测（2026-11-05，详见 §3.10）：**Vulkan 1.4.312** / conformance 1.4.1.3；**OpenGL 4.6.0 NVIDIA 581.57**（GLSL 4.60，默认就用独显，非核显）；**OpenCV 5.0.0 headless**（无 GUI、无 CUDA 构建、OpenCL 走 NVD3D11）。

---

## 2. 环境变量要点（PATH 共 66 项，含重复与死链）

| 变量 | 值 | 说明 |
|---|---|---|
| `JAVA_HOME` | `C:\Java\jdk-17` | Maven/Gradle 都读它 |
| `MAVEN_HOME` / `M2_HOME` | `D:\maven\apache-maven-3.9.11` | |
| `GRADLE_HOME` | `E:\gradle\gradle-8.6` | |
| **`GRADLE_USER_HOME`** | **`E:\gradle\repository_uap`** | 缓存不在默认 `~/.gradle`，注意 |
| `GOPATH` | `C:\Users\Ice2Faith\go` | `GOROOT` 未设（自动） |
| `CUDA_PATH` | `C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4` | |
| **`CUDA_VISIBLE_DEVICES`** | **`1`（机器级 HKLM）** | 🔴 **本机只有 1 块 GPU，编号 0 → 导致所有 CUDA 程序看不到 GPU！见 §4.1** |
| `OLLAMA_CUDA` / `OLLAMA_VULKAN` | `1` / `1` | |
| `FFMPEG` | `C:\FFMPEG` | |

**PATH 中的死链（可清理）**：`C:\Java\jdk-17\jre\bin`、`D:\scrcpy-win32-v1.13`。
PATH 里也有大量重复项（Intel WiFi、VS Code、go\bin 等出现 2–3 次），并且用户级 Path 结尾有多余 `;;`。

---

## 3. 各技术栈详情

### 3.1 Java 栈 ✅

| 组件 | 版本 | 路径 |
|---|---|---|
| **JDK 17（当前生效）** | Oracle **17.0.11** LTS | `C:\Java\jdk-17`（`java`/`javac` 来自这里） |
| JDK 8 | Oracle **1.8.0_201** | `C:\Java\jdk1.8.0_201` |
| JRE 8 | 1.8.0_201 | `C:\Java\jre1.8.0_201` |
| JDK 21 | Amazon **Corretto 21.0.11** | ⚠️ **多一层目录**：`C:\Java\jdk-21\jdk21.0.11_10` |
| **Maven** | **3.9.11** | `D:\maven\apache-maven-3.9.11` |
| **Gradle** | **8.6**（PATH 生效） | `E:\gradle\gradle-8.6` |
| 其他 Gradle | 4.7 / 5.4.1 / 8.8 | `E:\gradle\` |

* **Maven 本地仓库 4.9GB 在 `D:\maven\mvn`**，通过符号链接 `C:\Users\Ice2Faith\.m2\repository → D:\maven\mvn` 暴露（`settings.xml` 里也写了 `<localRepository>D:\maven\mvn</localRepository>`，双保险）。别去 `del .m2` 清理，会连到 D 盘真仓库。
* Maven 镜像：内网 `http://10.1.4.161:8085/repository/BI3_release/`（id=`bi3`）+ 阿里云 public。**离开公司内网时 bi3 会拖慢/超时**，必要时 `-s` 指定备用 settings 或临时注释。
* Gradle 仓库：`E:\gradle\repository_uap`（2.78GB）；`~/.gradle` 另有 1.37GB。
* **JDK 切换**：改 `JAVA_HOME` 即可；但 `PATH` 首位的 `C:\Java\jdk-17\bin` 会**抢先生效**——只想临时切，用 `$env:JAVA_HOME=...; $env:PATH="$env:JAVA_HOME\bin;"+$env:PATH`，或直接全路径调 `C:\Java\jdk-21\jdk21.0.11_10\bin\javac.exe`。
* `mvn -v` 显示 `platform encoding: GBK` ⚠️ → 编译含中文/UTF-8 资源易乱码，建议常带 `-Dfile.encoding=UTF-8`。

### 3.2 Golang 栈 ✅

| 项 | 值 |
|---|---|
| 版本 | **go1.23.3 windows/amd64** |
| GOROOT | `C:\Go`（未设环境变量，由二进制自动推断） |
| GOPATH | `C:\Users\Ice2Faith\go` |
| GOMODCACHE | `C:\Users\Ice2Faith\go\pkg\mod`（0.82GB，**在 C 盘**） |
| GOPROXY | `https://mirrors.aliyun.com/goproxy/,direct` |
| CGO_ENABLED | **0** ⚠️ |

* ✅ 实测 `go run` 正常。
* ⚠️ **CGO 默认关闭**：任何依赖 C 的包（sqlite3 的 mattn 版、某些图像库）需要 `CGO_ENABLED=1` + PATH 加入 MinGW，且 MinGW 是 **32 位 TDM-GCC 5.1**，与 amd64 目标不匹配 → **实际几乎无法编译 CGO 项目**。需要时应装 64 位 MinGW-w64/LLVM。
* `GOPATH\bin` 已在 PATH（`C:\Users\Ice2Faith\go\bin`），`go install` 的二进制可直接调用。

### 3.3 C# / .NET 栈 ⚠️（最大的短板）

| 组件 | 状态 |
|---|---|
| `dotnet` CLI / .NET SDK | ❌ **完全没有**（`C:\Program Files\dotnet` 不存在，全盘未见 `dotnet.exe`） |
| .NET Framework 运行时 | ✅ **4.8**（Release=528372） |
| C# 编译器 `csc.exe` | ✅ 4.8.4084，但**仅支持到 C# 5**（无 async/await 后续语法、无 record、无 nullable） |
| MSBuild | ⚠️ **仅 12.0**（VS2013 时代）：`C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe` |
| .NET FW SDK / Targeting Pack | ✅ 4.5 / 4.5.1 / 4.6.2 / 4.7 / 4.8 齐全 |
| NuGet CLI | ❌ 未安装（只有离线包：`E:\nuget\*.nupkg`） |
| Visual Studio | VS **2013 Ultimate** 12.0.21005.13（`D:\Program Files (x86)\Microsoft Visual Studio 12.0`） |

**结论**：只能做**传统 .NET Framework 4.x** 开发（WinForms/WPF/控制台/ASP.NET）。**不能**做 .NET Core / .NET 5+ / 现代 SDK 风格项目。

✅ 实测：`csc.exe` 编译并运行成功（`cs ok 4.0.30319.42000`）。
命令：
```
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /out:app.exe Program.cs
```

> 如需现代化：装 `.NET 8 SDK`（约 200MB，装到 E/F 盘）即可同时获得 `dotnet` CLI + Roslyn + 现代 MSBuild。

### 3.4 C/C++ 栈 ⚠️（双工具链，都需手动激活）

#### (a) MSVC — Visual Studio 2013 ✅ 可用但很旧
| 项 | 值 |
|---|---|
| 编译器 | **cl 18.00.21005.1**（VS2013 Update 5） |
| 根目录 | `D:\Program Files (x86)\Microsoft Visual Studio 12.0` |
| x64 cl | `...\VC\bin\amd64\cl.exe` ✅ |
| 激活脚本 | `...\VC\vcvarsall.bat`、`...\VC\bin\amd64\vcvars64.bat`、`...\Common7\Tools\VsDevCmd.bat` |
| 附带 | `link.exe`、`lib.exe`、`nmake.exe` ✅ |
| 缺失 | MFC / ATL 头文件、`rc.exe`（用 SDK 8.1 的） ⚠️ |

**✅ 实测：`call vcvarsall.bat amd64` + `cl /EHsc` 编译并运行成功（输出 `ok 42`）。**

`cl.exe` **不在 PATH**，每次必须先在 **cmd** 里激活（PowerShell 里 `call` 不好用，用 `cmd /c`）：
```bat
cmd /c "call ""D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"" amd64 && cl /EHsc /O2 a.cpp && a.exe"
```
* 语言标准：VS2013 = **C++11 部分支持**（无 `constexpr` 完整、无可变参数模板完善、无 C++14/17）。写现代 C++ 会踩坑。
* 资源编译：`C:\Program Files (x86)\Windows Kits\8.1\bin\x64\rc.exe`（不在 PATH）。

#### (b) MinGW / TDM-GCC ✅ 但 32 位且很旧
| 项 | 值 |
|---|---|
| 版本 | **gcc/g++ (tdm-1) 5.1.0**（2015 年） |
| 路径 | `C:\Program Files (x86)\CodeBlocks\MinGW\bin`（**不在 PATH**） |
| 工具 | `gcc/g++`、`gdb32.exe`、`mingw32-make.exe`、`windres.exe`、`ar.exe` |
| 架构 | **32 位 (i686)** ⚠️ |
| 标准 | C++14 大部分可用 |

**❌ 缺失**：`cmake`、`make`（只有 `mingw32-make`）、`clang/LLVM`、`MSYS2`、`vcpkg`、`conan`、`ninja`。
→ 意味着**绝大多数现代 C/C++ 开源项目无法直接构建**（它们都假设有 CMake + 64 位工具链）。

调用方式（注意路径含空格+括号，必须引号）：
```powershell
& "C:\Program Files (x86)\CodeBlocks\MinGW\bin\g++.exe" -std=c++14 a.cpp -o a.exe
```

### 3.5 Windows 调试 / 驱动（windbg / WDK）栈 ⚠️

| 组件 | 状态 |
|---|---|
| **WinDbg / cdb / kd / ntsd** | ✅ **6.12.0002.633（2010 年，x86）** — 路径 `C:\Program Files (x86)\Debugging Tools for Windows (x86)`（**不在 PATH**） |
| `symchk.exe` | ✅ 同目录 |
| 现代 WinDbg (Preview/Store 版) | ❌ 未安装 |
| Windows Kits **10 Debuggers** | ❌ 目录不存在 |
| Windows Kits 8.1 Debuggers\x64 | ⚠️ 只有 `dbghelp.dll`/`symsrv.dll`，**无 windbg.exe** |
| **WDK** | ⚠️ 注册表有 "Windows Driver Kit 10.0.14393.0"，但 `C:\Program Files (x86)\Windows Kits\10\Include\10.0.14393.0` **残缺**：km 仅 31 个文件、um 141 个（多为 WinRT idl），**没有 `wdm.h` / `windows.h`**，`Lib\km\x64` 仅 6 个文件 → **实际不能编驱动** |
| Windows SDK **8.1** | ✅ **完整**：`Include\um`(含 windows.h)、`Lib\winv6.3\um\x64`(438 个 lib)、`rc.exe`、`bin\x64` |
| 本地符号缓存 | `C:\dbg_symbols`（含 hal.pdb、ACPI.sys 等）⚠️ 但 **`_NT_SYMBOL_PATH` 为空** |
| WPT (Windows Performance Toolkit) | ✅ 6.3.9600，**已在 PATH**：`wpr.exe`、`wpa.exe`、`xperf.exe`、`WPRUI.exe` |
| 应用验证 | Application Verifier（x86/x64）已装 |

**要点**：
* 分析 dump（用户目录有 `bluescreen-minidump` 项目）用老 windbg 就够了，但**必须用 x86 版**、且要自己设符号路径：
  ```powershell
  $env:_NT_SYMBOL_PATH = "srv*C:\dbg_symbols*https://msdl.microsoft.com/download/symbols"
  & "C:\Program Files (x86)\Debugging Tools for Windows (x86)\windbg.exe" -z C:\path\dump.dmp
  ```
* 命令行式：`cdb.exe -z dump.dmp -c "!analyze -v;q"`（在 cmd 里跑更顺）。
* **驱动开发实际不可行**：WDK 14393 头部缺失，且 WDK 14393 需要 VS2015+，本机只有 VS2013。若要做驱动，需装 WDK 10.0.19041/22621 + VS2019/2022 Build Tools。

### 3.6 Python 栈 ✅（主力是 3.10）

| 项 | 值 |
|---|---|
| **生效版本** | **Python 3.10.6** @ `C:\Python310`（PATH 优先） |
| pip | 22.2.1（**偏旧**，建议 `python -m pip install -U pip`） |
| 其他解释器 | `C:\Program Files (x86)\Python36-32`（3.6 32位）；`C:\Program Files\Python314` **是坏目录（无 python.exe）** ❌ |
| `py` 启动器 | 只识别到 3.10-64 |
| conda / miniconda | ❌ 完全没有 |
| site-packages | 6.14 GB |

**已装关键包**（实测 `pip list`）：

```
torch 2.6.0+cu124         torchvision 0.21.0+cu124    torchaudio 2.6.0+cu124
numpy 2.2.6               pandas 2.3.3                scipy 1.15.3
onnxruntime 1.20.1        opencv-contrib-python 4.10  opencv-python-headless 5.0
fastapi 0.142.4           uvicorn 0.54.0              openai 3.3.1
pymupdf 1.28.0 (+layout, +pymupdf4llm)  pypdf 6.14.2  pypdfium2 5.11
python-docx 1.2.0         python-pptx 1.0.2           openpyxl 3.1.5
lxml 6.1.1                beautifulsoup4 4.15.0       requests 2.34.2
pillow 12.2.0
```
→ 文档处理（docx/pptx/xlsx/pdf）、OCR 后处理、爬虫、Web API、embedding 全部就绪。
❌ 未装：`transformers`、`accelerate`、`scikit-learn`、`matplotlib`、`sentence-transformers`、`pyinstaller`。

**pip 镜像**（`%APPDATA%\pip\pip.ini`）：
* 文件里写的是 `aliyun` + `extra-index https://pypi.ngc.nvidia.com`
* 但 `pip config list` 生效的是 **`https://pypi.tuna.tsinghua.edu.cn/simple`**（优先级更高的配置覆盖了）
* `no-cache-dir = true`（不缓存 wheel，重复安装会重复下载）

### 3.7 CUDA / GPU 计算栈 ✅（2026-11-05 已修复配置错误，全线可用）

| 项 | 值 |
|---|---|
| CUDA Toolkit | **12.4**（nvcc V12.4.99），`C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4` |
| cuDNN | ✅ 头文件存在（`include\cudnn.h`） |
| 驱动 | 581.57，支持 CUDA 13.0 运行时 |
| Samples | ✅ `extras\demo_suite\deviceQuery.exe`、`bandwidthTest.exe`（实测 **PASS**） |
| Nsight | Compute 2024.1、Systems 2023.4.4、VSE 2024.1 |

**实测结果**：
```
deviceQuery: CUDA Driver 13.0 / Runtime 12.4, Capability 6.1, NumDevs=1 → PASS
bandwidthTest → PASS
```
→ **硬件与驱动完全正常**。

> 🔴 **但 `torch.cuda.is_available()` 返回 False** —— 原因见 §4.1。

### 3.8 Node.js / 前端栈 ✅

| 项 | 值 |
|---|---|
| Node.js | **v24.20.0** @ `E:\NodeJs`（`NODE_HOME` 未设） |
| npm | **12.0.2** |
| pnpm | **10.30.3**，store = `D:\.pnpm-store\v10` |
| yarn | 1.22.22（classic） |
| registry | `https://registry.npmmirror.com` ✅ |
| 全局包 | `@anthropic-ai/claude-code@2.1.263`、`@deepseek-ai/dsh@0.2.1-rc.5`、`opencode-ai@1.18.29`、`yo@7.0.1`、`rimraf@6.1.3`、`generator-code@1.12.0` |

⚠️ **两个坑**：
1. **PowerShell 里 `npm`/`pnpm` 会报"禁止运行脚本"**（执行策略 Undefined，PS 解析到 `npm.ps1`）。→ 用 `npm.cmd` / `pnpm.cmd`，或改用 cmd / git-bash。
2. **node-gyp 原生模块无法编译**：需要 VS2015+ / Python，本机只有 VS2013 → `npm i` 遇到需编译的包（sharp、canvas、better-sqlite3）会失败。

### 3.9 工作辅助工具

| 工具 | 版本 | 路径 / PATH | 备注 |
|---|---|---|---|
| **Git** | **2.52.0** | `D:\Program Files\Git\cmd` ✅ 在 PATH | 用户 `Ice2Faith` / `ugex_savelar@163.com` |
| **Git Bash** | GNU bash 5.2.37 (msys) | `D:\Program Files\Git\bin\bash.exe` ✅ | 附 `perl`、`tar`、`unzip` |
| **SVN** | 1.10.6 | `D:\Program Files\TortoiseSVN\bin` ✅ | + TortoiseSVN GUI |
| gh CLI | ❌ 未装 | | |
| **FFmpeg** | **9.0.2**（gyan.dev full build） | `C:\FFMPEG\bin` ✅ 在 PATH | 硬件编码齐全：`h264_nvenc`/`hevc_nvenc`/`av1_nvenc`、`*_qsv`、`*_amf`；含 `--enable-cuda-llvm --enable-cuvid --enable-ffnvcodec`、libx264/265、libsvtav1、libplacebo、whisper |
| **Pandoc** | **3.10** | `C:\Program Files\Pandoc` ✅ | ⚠️ **无 LaTeX**（xelatex/pdflatex/tectonic/typst/wkhtmltopdf 全无）→ `-o x.pdf` 会失败，**PDF 走 LibreOffice** |
| **LibreOffice** | **26.8.1.1** | `D:\Program Files\LibreOffice\program\soffice.com` ✅ | docx/xlsx/pptx↔pdf 转换主力 |
| **ImageMagick** | **7.1.2-27 Q8 x64** | `C:\Program Files\ImageMagick-7.1.2-Q8` ✅ | 命令是 `magick`（不是 `convert`）；OpenCL/OpenMP 可用 |
| **Tesseract OCR** | **5.5.0** | `C:\Program Files\Tesseract-OCR` ✅ | 语言包：`chi_sim`、`chi_tra`、`eng`、`script\HanS/HanT` |
| 7-Zip | 25.01 | `C:\Program Files\7-Zip\7z.exe` ❌ **不在 PATH** | 用全路径 |
| Bandizip | 6.25 | `C:\Program Files\Bandizip` ✅ | 有 `bz.exe` 命令行 |
| ADB | 1.0.26（很旧）⚠️ | `C:\ADB\adb.exe` ✅ | 附 `fastboot.exe` |
| sqlcmd | SQL Server 2012 (11.0) | `C:\Program Files\Microsoft SQL Server\110\Tools\Binn\SQLCMD.EXE` ✅ | + LocalDB 实例 **v11.0**（`sqllocaldb info`） |
| MySQL / Redis / Docker | ❌ 均未装 | | docker 只有 G 盘的 `docker-images` 目录 |
| curl | 8.13.0 | 系统 | |
| OpenSSH | 10.2p1 | `C:\Windows\System32\OpenSSH` ✅ | |
| **Ollama** | 0.32.1 | `~\AppData\Local\Programs\Ollama` ✅ | **当前未运行**（11434 未监听）；模型：`deepseek-r1`、`qwen2.5-coder`、`qwen3-embedding`、`nomic-embed-text-v2-moe`（4.33GB） |
| **llama.cpp** | 自编译 | `D:\llama.cpp\llama`（17.19GB 含 GGUF） | 后端只有 **CPU + Vulkan**（`ggml-vulkan.dll`），**无 CUDA 后端** ⚠️；`startup*.bat` 里用 `llama-server -ngl 999 --port 9090`，`-ngl` 走 Vulkan |
| VS Code | 1.143.1 | `~\AppData\Local\Programs\Microsoft VS Code\bin` ✅ | 有 copilot-shim |
| IntelliJ IDEA | 2024.2.1 + 2021.1 | `D:\Program Files\JetBrains` / `C:\Program Files\JetBrains` | 两个版本 |
| Cherry Studio / Qoder / Tabby / Beyond Compare / Notepad-- / draw.io / PixPin | — | — | |
| VMware Workstation | 14.1.1 | | 较旧，Win10 22H2 客户机建议升级 |
| 安全软件 | **火绒 6.0.12.3** | | ⚠️ 可能拦截脚本/编译产物，构建报"拒绝访问"时先看火绒日志 |

### 3.10 图形 API 栈：Vulkan / OpenGL / OpenCV（2026-11-05 现场实测）

#### (a) Vulkan ✅ 可用，仅 NVIDIA 一张卡

| 项 | 实测值 |
|---|---|
| Loader | `C:\Windows\System32\vulkan-1.dll` **1.4.309.0** |
| Instance 版本 | **1.4.309** |
| 设备 | **1 个**：`GPU0` = NVIDIA GeForce GTX 1050 |
| device apiVersion | **1.4.312** |
| driverVersion / driverID | `581.57.0.0` / `DRIVER_ID_NVIDIA_PROPRIETARY` |
| `driverInfo` | `581.57` |
| deviceType | `PHYSICAL_DEVICE_TYPE_DISCRETE_GPU` |
| vendorID / deviceID | `0x10DE` / `0x1C8D` |
| conformanceVersion | **1.4.1.3**（正式符合性认证） |
| memoryHeaps | 3 个（其中 2 个 `DEVICE_LOCAL`） |
| maxImageDimension2D | 32768 |
| 关键扩展 | `VK_KHR_swapchain`(rev 70)、`VK_KHR_synchronization2`、`VK_EXT_descriptor_indexing`(rev 2) |

* **Intel HD 610 不进 Vulkan 列表**：其 2016 年驱动不提供 Vulkan ICD，全机只有 NVIDIA 的 ICD 注册。
  所以在 Vulkan 里也**只有 `Vulkan0`**，与 CUDA 的"只有索引 0"结论一致。
* ❌ **未安装 Vulkan SDK**（无 `VULKAN_SDK`、无 `C/E/G:\VulkanSDK`、无 `glslangValidator` / `spirv-val`）。
  → 能**跑** Vulkan 程序，但**不能编译 shader / 开发** Vulkan 应用。
  需要时下载 LunarG SDK（装到 `E:\VulkanSDK`，别放 C:）。
* 探测工具：`vulkaninfo.exe`（已在 System32，随 NVIDIA 驱动安装）→ `vulkaninfo --summary`
* 已知消费方：[`D:\llama.cpp\llama`](D:\llama.cpp) 的 `llama-server` 走 Vulkan0（见 §3.9）。

#### (b) OpenGL ✅ 4.6，且默认走**独显**

现场编译探测程序（[`scripts\opengl-probe.cs`](C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\opengl-probe.cs)）创建真实 GL 上下文后 glGetString 实测：

| 项 | 实测值 |
|---|---|
| `GL_VENDOR` | **NVIDIA Corporation** |
| `GL_RENDERER` | **NVIDIA GeForce GTX 1050/PCIe/SSE2** |
| `GL_VERSION` | **4.6.0 NVIDIA 581.57** |
| `GL_SHADING_LANGUAGE_VERSION` | **4.60 NVIDIA** |
| `GL_MAX_TEXTURE_SIZE` | 32768 |
| `GL_MAX_3D_TEXTURE_SIZE` | 16384 |
| `GL_MAX_CUBE_MAP_TEXTURE_SIZE` | 32768 |
| `GL_MAX_RENDERBUFFER_SIZE` | 32768 |
| `GL_MAX_VERTEX_ATTRIBS` | 16 |
| `GL_MAX_TEXTURE_IMAGE_UNITS` | 32 |
| `GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS` | 192 |
| `GL_MAX_VERTEX_UNIFORM_COMPONENTS` | 4096 |
| `GL_MAX_UNIFORM_BLOCK_SIZE` | **65536 B** |
| `GL_MAX_UNIFORM_BUFFER_BINDINGS` | 84 |
| `GL_MAX_VERTEX_UNIFORM_BLOCKS` | 32 |
| `GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS` | 96 |

> 以上均为 glGetString / glGetIntegerv **现场取值**，常量按 Khronos 头文件取；
> 与 NVIDIA Pascal 架构规格一致（65536 / 84 / 4096 均为该架构标准值）。

* **重要且反直觉**：桌面显示由 Intel 核显驱动，但 **OpenGL 上下文默认落到 NVIDIA 独显**（4.6，而非核显）。
  因此 GLSL 可放心用 `#version 460`。
* 双 ICD 并存（均在 DriverStore，不占 System32）：
  * NVIDIA：`nvoglv64.dll`（`nvcvsi.inf_amd64_…`）
  * Intel：`ig9icd64.dll`（`igdlh64.inf_amd64_…`）
* 系统 `opengl32.dll` = 10.0.19041.5794（客户端 DLL，仅转发）。
* 无 `glview` / `wglinfo` / `glewinfo` 等诊断工具，需要时用本仓库的 `scripts\opengl-probe.cs` 重新编译即可。

#### (c) OpenCV ⚠️ 可用，但**是 headless 版、且装了冲突的两套**

| 项 | 实测值 |
|---|---|
| 生效版本 | **cv2 5.0.0**，`C:\Python310\lib\site-packages\cv2` |
| 来源发行版 | `opencv-python-headless 5.0.0.93` |
| **冲突残留** | `opencv-contrib-python 4.10.0.84` **同时装着**（pip list 里两者都在）⚠️ |
| **GUI (highgui)** | ❌ **`cv2.imshow` 直接抛错**（build info: `GUI: NONE`） |
| CUDA | ❌ **`cv2.cuda.getCudaEnabledDeviceCount() = 0`**（该 wheel 未编译 CUDA） |
| **OpenCL** | ✅ **YES (NVD3D11)**，设备 = `NVIDIA GeForce GTX 1050` |
| contrib 模块 | ✅ 有 `xfeatures2d`、`aruco`、`dnn_superres`、`face`、`SIFT_create` |
| Video I/O | FFMPEG YES（预编译二进制）；GStreamer NO |
| 并行框架 | Concurrency；IPP **2026.0.0**；Eigen NO |
| C++ SDK | ❌ 磁盘上无 OpenCV SDK，`opencv_world*.dll` 也不在 System32 |

**三个必须知道的坑**：

1. **不能 `cv2.imshow`** —— 会报 `The function is not implemented. Rebuild the library with ... support`。
   看图请改用 `cv2.imwrite` 落盘再打开，或 matplotlib，或换装 `opencv-python`（非 headless）。
2. **两套 opencv 并存**，`cv2` 目录被后装的 5.0.0 覆盖。任何一次 `pip install opencv-*` 都可能让 import 崩掉。
   清理建议（二选一，别都留）：
   ```powershell
   python -m pip uninstall -y opencv-contrib-python opencv-python-headless
   python -m pip install "opencv-contrib-python==5.0.0.93"   # 要 GUI+contrib
   # 或保持无 GUI 的服务器式环境：pip install "opencv-python-headless==5.0.0.93"
   ```
3. **OpenCV 的 OpenCL 不一定更快**——实测 4000×4000 高斯模糊（两次独立测量）：
   | 路径 | 耗时 |
   |---|---|
   | OpenCL（每轮 numpy→GPU 传输） | **105–118 ms/iter** |
   | CPU | **50–74 ms/iter** |

   即**每次都要把 numpy 数组搬上 GPU 时，比 CPU 还慢约一倍**。要用 OpenCL 必须**全程 UMat 链式**（`cv2.UMat` 进、`cv2.UMat` 出、只在最后 `.get()` 一次），否则纯亏。

**结论**：本机 OpenCV 适合**无界面批处理 / 服务端推理**；要 GPU 加速请走 **PyTorch（CUDA 已修复，§4.1）** 或 **ONNX Runtime**，别指望 OpenCV 的 CUDA 模块。

### 3.11 系统字符集与编码（乱码防治）⚠️ 本机最大的隐形坑

#### (a) 实测基线（2026-11-05）

| 项 | 值 | 含义 |
|---|---|---|
| **系统 ACP（ANSI）** | **936** | 决定文件/API 默认编码 |
| **系统 OEMCP** | **936** | 决定传统控制台默认编码 |
| `Encoding.Default.WebName` | **`gb2312`**（CP936） | .NET Framework 的默认编码 |
| Global UTF-8 beta（`ACP=65001`） | ❌ **未开启** | 若要全局 UTF-8 需手动开（有副作用，见 (e)） |
| `CurrentCulture` / `UICulture` | `zh-CN` / **`en-US`** | ⚠️ 两者不一致 |
| `Get-WinSystemLocale` | `zh-CN` | |
| 时区 | `China Standard Time` | |
| 代码页开关 `chcp`（cmd 报告） | **936** | |
| 控制台实际 `GetConsoleOutputCP()` | **65001** | ⚠️ **与 chcp 报告的 936 不一致！** |

**这组矛盾是乱码的总根源**：系统级默认是 GBK(936)，但命令行会话的输出代码页是 UTF-8(65001)，
而 `cmd.exe` 自己又按 936 报告。三者混用 → 看到"文字变问号/方块/锟斤拷"。

#### (b) 三种乱码根因（对症下药）

| 根因 | 现象 | 证据 | 解法 |
|---|---|---|---|
| ① **默认编码是 GBK** | Python `open()` 写出的文件被 UTF-8 读取器读成乱码 | `open(...,'w').encoding = **cp936**` | 所有读写**显式** `encoding='utf-8'` |
| ② **控制台 CP 与 cmd 报告不一致** | 中文在管道/重定向里变 `U+FFFD` | `chcp`=936 但 `GetConsoleOutputCP`=65001 | 别信终端显示，**写文件+按显式编码读** |
| ③ **各工具输出编码不同** | wmic 中文全成问号，systeminfo 却正常 | 见下表字节证据 | 按工具分别处理 |

**工具输出编码实测（字节级）**：

| 命令 | 实际输出编码 | 字节证据 |
|---|---|---|
| `wmic ...` | **UTF-16LE 带 BOM** | 开头 `FF FE 0D 00 0A 00 43 00 61 00…` |
| `systeminfo` | **UTF-8（无 BOM）** | 开头 `0D 0A 48 6F 73 74 20 4E 61 6D 65 3A`（"Host Name:"） |
| 多数老 CLI（`ipconfig`/`netstat`/`sc` 等） | **GBK** | 中文双字节 `D6 D0 CE C4` 风格 |
| Node.js | UTF-8（恒定） | `中文` → `e4 b8 ad e6 96 87` |
| PowerShell 原生输出 | UTF-16（由宿主转码） | — |

**经典乱码对照**（GBK 被当 UTF-8 读）：

```
UTF-8  "中文" = E4 B8 AD E6 96 87   (6 bytes)
GBK    "中文" = D6 D0 CE C4         (4 bytes)
GBK 的 D6 D0 CE C4 按 UTF-8 解码 -> 4× U+FFFD（替换字符）
UTF-8 的 E4B8AD 按 GBK 解码 -> "涓枃" 这类怪字（"锟斤拷"同源）
```

#### (c) 逐语言正确姿势（本机实测值）

**Python 3.10.6**（`sys.stdout.encoding = gbk`，`locale.getpreferredencoding = cp936`，`utf8_mode = 0`）

```python
# ① 文件读写：永远显式编码（本机 open() 默认是 cp936！）
open(path, "r", encoding="utf-8", newline="")     # csv 必须 newline=""
open(path, "w", encoding="utf-8-sig")             # 需要 Excel 识别中文 → 加 BOM

# ② 源码头：Python 3 默认 UTF-8，无需 PEP 263；
#    仅当文件非 UTF-8（历史遗留 GBK 源码）才需要，且必须是前两行：
# -*- coding: gbk -*-

# ③ 控制台输出（本机 stdout 是 gbk，输出 UTF-8 内容会炸）
sys.stdout.reconfigure(encoding="utf-8")          # 3.7+

# ④ subprocess 取文本
subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")

# ⑤ 环境变量方式（推荐，全局生效）
#    PYTHONUTF8=1        等价 python -X utf8（UTF-8 mode，改 open/stdout 默认）
#    PYTHONIOENCODING=utf-8   只改 stdio
```

> ⚠️ **BOM 双刃剑**：`utf-8-sig` 写出的 BOM(`EF BB BF`) 让 Excel 正常显示中文，
> 但会让 **JSON/TOML/YAML 解析器报错**（`json.load` 直接 `Unexpected UTF-8 BOM`）。写配置/接口数据**绝不要 BOM**。

**Java 17.0.11**（`file.encoding = GBK`，`native.encoding = GBK`，`sun.jnu.encoding = GBK`）

```powershell
java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 App
# 编译（源码含中文）
javac -encoding UTF-8 -d out src\*.java
# Maven 全链路（本机 mvn 显示 platform encoding: GBK）
mvn -Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8 clean package
# Gradle
$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8"
```

* 代码里读写文件用 `Files.readString(p, StandardCharsets.UTF_8)` / `new InputStreamReader(is, StandardCharsets.UTF_8)`，别用无参构造。
* **JDK 18+ 起 `file.encoding` 默认 UTF-8**（JEP 400），但本机是 17，仍需手动指定。
* 控制台若仍乱码，先 `chcp 65001` 再运行。

**Node.js 24**（自身恒 UTF-8，乱码只发生在"显示"层）

```js
fs.readFileSync(p, "utf8"); fs.writeFileSync(p, s, "utf8");
Buffer.from("中文").toString("hex");        // -> e4b8ade69687
fs.writeFileSync(p, "\uFEFF" + s, "utf8");  // 手动加 BOM（Node 不会自动加）
```
> Node 写出的永远是 UTF-8 字节；在 936 控制台里显示乱码 ≠ 文件有问题。

**C# / .NET Framework 4.8**（`Encoding.Default` = gb2312 / CP936）

```csharp
Console.OutputEncoding = new UTF8Encoding(false);   // 控制台
File.ReadAllText(p)                                 // ❌ 用 Encoding.Default(936)
File.ReadAllText(p, new UTF8Encoding(false))        // ✅ 显式
new StreamWriter(p, false, new UTF8Encoding(false)) // 第二个参数 append，第三个决定 BOM
```

**Go 1.23**（源码与字符串恒 UTF-8；Windows 控制台需自己切码页）

```go
// main 里若要打印中文到 Windows 控制台
kernel32 := syscall.NewLazyDLL("kernel32.dll")
kernel32.NewProc("SetConsoleOutputCP").Call(65001)
```

**C/C++ MSVC 18.0（VS2013）**

```bat
:: 源码含中文（UTF-8 保存）必须告诉编译器，否则按 936 解析 -> 乱码/编译错误
cl /utf-8 /EHsc a.cpp
:: 或分开指定
cl /source-charset:utf-8 /execution-charset:utf-8 a.cpp
```
> VS2013 对 `/utf-8` 支持有限，稳妥做法：源码存 **UTF-8 with BOM**，编译器会自动识别。
> MinGW GCC 5.1：`-finput-charset=UTF-8 -fexec-charset=UTF-8`。

**PowerShell 5.1**（注意：`$OutputEncoding` 实测是 **us-ascii/CP20127**）

```powershell
[Console]::OutputEncoding = [Text.Encoding]::UTF8   # 出
[Console]::InputEncoding  = [Text.Encoding]::UTF8   # 入
$OutputEncoding = [Text.Encoding]::UTF8             # 管道给原生程序（默认 ASCII，中文会被吃掉！）

# PS 5.1 的坑：Set-Content -Encoding UTF8 会写 BOM！
Set-Content -Path x.txt -Value $s -Encoding UTF8     # 带 BOM
[IO.File]::WriteAllText($p, $s, (New-Object Text.UTF8Encoding $false))  # 无 BOM
```

**Git**（实测 `core.autocrlf = true`，其余未设）

```powershell
git config --global i18n.commitEncoding utf-8
git config --global i18n.logOutputEncoding utf-8
git config --global core.quotepath false     # 否则中文路径显示为 \346\226\207
git config --global core.autocrlf input      # 现有 true：提交时转 LF，检出转 CRLF
```

**批处理 `.bat/.cmd`**
```bat
@echo off
chcp 65001 >nul      rem 第一件事就切码页，且文件本身要存成 ANSI/GBK 或 UTF-8+BOM
```

#### (d) 我的通用铁律（避免重复踩坑）

1. **不用终端显示判断编码** —— 一律写文件后按显式编码读，或用 hex 打印比对。
2. **任何落盘文本显式指定编码**，默认值在本机是 cp936，几乎总是错的。
3. **跨语言传递优先 ASCII/UTF-8**：脚本输出诊断信息用英文（本档案所有 `scripts\*.py|ps1` 都是 ASCII 输出，就是这个原因）。
4. **BOM 只给"要被人/Excel 打开"的 CSV/TXT**；配置、JSON、源码（Python/JS/Go）不要 BOM。
5. **子进程输出编码不可假设**：wmic=UTF-16LE、systeminfo=UTF-8、老工具=GBK，见 (b) 表。

#### (e) 全局 UTF-8（Beta）要不要开？

开启方式：控制面板 → 区域 → 管理 → 更改系统区域设置 → 勾选"Beta: 使用 Unicode UTF-8 提供全球语言支持"
（等价于把 `HKLM\...\Nls\CodePage\ACP` 改成 `65001`，需重启）。

| 好处 | 风险 |
|---|---|
| Python/Java/.NET 默认编码全变 UTF-8，乱码问题大幅减少 | **一大批老国产软件/游戏/驱动安装器会乱码或崩溃**（它们硬编码 936） |
| 跨平台项目行为一致 | 已装的 GBK 依赖工具链可能异常 |
| | 改回来需要重启，且不保证完全恢复 |

**建议：保持现状（936）+ 在项目里显式指定 UTF-8**。这个更可控，不要动全局开关。

#### (f) 系统信息预查询命令（wmic 及其替代）

`wmic` **本机可用**（`C:\Windows\System32\Wbem\WMIC.exe`），但微软已标记 **deprecated**（实测输出 `WMIC is deprecated.`）。

| 想查 | wmic（旧，输出 UTF-16LE） | 现代替代（推荐） |
|---|---|---|
| OS 版本 | `wmic os get Caption,Version,BuildNumber` | `Get-CimInstance Win32_OperatingSystem` |
| 机型/内存 | `wmic computersystem get Model,TotalPhysicalMemory` | `Get-CimInstance Win32_ComputerSystem` |
| CPU | `wmic cpu get Name,NumberOfCores,MaxClockSpeed` | `Get-CimInstance Win32_Processor` |
| 内存条 | `wmic memorychip get Capacity,Speed,DeviceLocator` | `Get-CimInstance Win32_PhysicalMemory` |
| 磁盘 | `wmic logicaldisk get DeviceID,Size,FreeSpace` | `Get-CimInstance Win32_LogicalDisk` |
| GPU | `wmic path win32_videocontroller get Name` | `Get-CimInstance Win32_VideoController` |
| 主板/BIOS | `wmic baseboard get Product` / `wmic bios get SMBIOSBIOSVersion` | `Get-CimInstance Win32_BaseBoard` / `Win32_BIOS` |
| 已装软件 | `wmic product get Name,Version`（**很慢且不全**） | 注册表 Uninstall 键（见 §3 清单） |
| 驱动 | `wmic sysdriver get Name,State` | `Get-CimInstance Win32_SystemDriver` |

> 本档案 §1 的数据就是 **CIM 查询 + 注册表 + 实跑命令** 三方交叉验证得来的。
> `systeminfo` 也可用（输出 UTF-8），但字段是本地化中文，脚本里 `Select-String` 匹配不稳，**优先用 CIM**。

---

## 4. 已知问题（按优先级）

### ✅ 4.1 `CUDA_VISIBLE_DEVICES` 已修复（2026-11-04 改为 `0`，2026-11-05 重启后全线确认）

**当前状态（2026-11-05 08:31 复核）**：`HKLM\...\Environment` → `CUDA_VISIBLE_DEVICES = REG_SZ "0"`，用户级无副本；**进程内继承值也是 `0`**，全线正常。

> **环境块缓存规律（踩过一次，记住）**：子进程继承父进程的环境块，**改注册表不影响已运行进程**。
> Windows 变量改完要生效，需要「新进程链」：注销/重登（或重启 `explorer.exe`）→ 再启动目标程序。
> 本次即：explorer 重启于 11-05 08:25:43，DSH 重启于 08:29:23，之后所有子进程才拿到 `0`。

* 原故障：该变量曾被设为 `1`，而本机只有 1 块 CUDA 设备（GTX 1050，编号 **0**）→ 变量指向不存在的设备 1，**所有 CUDA 程序看到 0 个 GPU**。
* 四种视角互相印证：`nvidia-smi -L` 仅 GPU 0；NVML `nvmlDeviceGetCount_v2` = 1；`deviceQuery` "Detected 1 CUDA Capable device"；Vulkan 仅 `Vulkan0`。
* 原证据：`torch.cuda.is_available()=False, device_count=0`；`deviceQuery.exe` → FAIL。

**修复后实测（值 `0`，重启后复核）**：

| 检查项 | 结果 |
|---|---|
| 注册表 / 进程继承值 | 均为 `"0"` ✅ |
| 全新进程（计划任务，脱离 DSH 进程树） | 读到 `0` ✅ |
| `deviceQuery.exe` | `Detected 1 CUDA Capable device` / `Device 0: GTX 1050` / `Result = PASS` ✅ |
| torch `scripts\cuda-check.py` | `[PASS]`，exit 0，sm_61 / 5 SM / 4096 MiB ✅ |
| 性能（预热后 3 次） | **1431 / 1460 / 1466 GFLOPS** ≈ 理论值(1354MHz)的 85% ✅ |
| nvcc sm_61 kernel | `kernel OK: c[0]=2.0 ... sm_61` ✅ |
| llama.cpp | `Vulkan0: NVIDIA GeForce GTX 1050 (3619 MiB free)` ✅ |

> ⚡ **性能提示**：GTX 1050 冷启动后的首测会掉到 ~490 GFLOPS（GPU 处于 P8 低功耗，SM 时钟仅 139MHz），
> 跑几轮升到 **P0 / 1354MHz** 后稳定在 **1.4–1.5 TFLOPS**。这是正常现象，不是故障；
> 但**做性能对比实验时必须先预热**，否则结论会被冷启动带偏。

**取值语义实测表**（`torch.cuda.device_count()`）：

| 取值 | 结果 | 判定 |
|---|---|---|
| （未设置 / 空字符串） | **1** | ✅ 推荐，自动用全部卡 |
| `0` | **1** | ✅ 等价推荐 |
| `0,1` | 1 | ✅ 无害（越界索引被静默忽略） |
| `GPU-529c9572-7aed-a6c0-b583-e1ae0f46715e` | **1** | ✅ 按 UUID 绑定，最稳（抗将来设备顺序变化） |
| `1` | **0** | ❌ **当前错误配置** |
| `-1` | 0 | ⚠️ 合法但语义是"禁用 CUDA"（CPU-only 模式） |
| `GPU-0` | 0 | ❌ 语法错误（`GPU-` 前缀必须跟完整 UUID） |

* **修复（需管理员权限）**：
  ```powershell
  # 推荐：直接删除，让 CUDA 自动枚举
  [Environment]::SetEnvironmentVariable('CUDA_VISIBLE_DEVICES', $null, 'Machine')
  # 或显式设为 0
  [Environment]::SetEnvironmentVariable('CUDA_VISIBLE_DEVICES', '0', 'Machine')
  ```
  ```bat
  :: 管理员 cmd 等价写法
  reg delete "HKLM\SYSTEM\CurrentControlSet\Control\Session Manager\Environment" /v CUDA_VISIBLE_DEVICES /f
  setx CUDA_VISIBLE_DEVICES 0 /M
  ```
  改完**必须重开所有终端**；从开始菜单启动的程序需重启 Explorer 或重新登录；**Ollama 必须重启**（`OLLAMA_CUDA=1` 已设，重启后才会走 CUDA）。
* 临时绕过（当前会话，立即生效）：
  ```powershell
  $env:CUDA_VISIBLE_DEVICES = '0'
  ```
* 修复后自检脚本：`C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\cuda-check.py`
  ```
  python scripts\cuda-check.py
  # 期望：device NVIDIA GeForce GTX 1050 / sm_61 / SMs 5 / matmul OK ≈1 TFLOPS
  ```

### 🔴 4.1b nvcc 不支持本机 VS2013（CUDA 12.4 要求 VS2017–2019）

* 直接 `nvcc -arch=sm_61` → `nvcc fatal : nvcc cannot find a supported version of Microsoft Visual Studio. Only the versions between 2017 and 2019 (inclusive) are supported!`
* ✅ **实测绕过可用**：加 `-allow-unsupported-compiler` 后能编译并成功运行（会刷 `warning #541-D`）：
  ```bat
  cmd /c "call ""D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"" amd64 && nvcc -allow-unsupported-compiler -arch=sm_61 -o a.exe a.cu && a.exe"
  ```
  输出：`kernel OK: c[0]=2.0  device=NVIDIA GeForce GTX 1050  sm_61`
* 但这是"侥幸能过"，生产用途建议装 **VS2022 Build Tools**（仅 C++ 生成工具，约 2–3GB，装 E/F 盘）。

### 🔴 4.2 C 盘只剩 7.8 GB
* 影响：临时文件、pip/maven 解压、编译中间产物都可能失败（`No space left`）。
* 可回收：`C:\Windows\SoftwareDistribution\Download`、`$Windows.~WS`（升级残留，通常数 GB）、`C:\dbg_symbols`、pip cache、旧 pagefile（C 盘 4GB 可移走）。
* 新装工具一律装 **E:/F:/G:**，别装 C:。

### ⚠️ 4.3 执行策略阻止 PowerShell 跑 `.ps1` 包装器
```
npm : 无法加载文件 E:\NodeJs\npm.ps1，因为在此系统上禁止运行脚本
```
* 所有 scope 都是 `Undefined`（PS 5.1 默认 Restricted 行为）。
* 三个选择：用 `npm.cmd` / `pnpm.cmd`；或 `cmd /c`；或 `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`（一次性，推荐）。

### ⚠️ 4.4 没有 PowerShell 7
* 所有"pwsh"实际是 **Windows PowerShell 5.1.19041.6456**（`C:\Program Files\PowerShell\7` 不存在）。
* 语法限制：无 `??`、无三元 `? :`、无 `-Parallel`、`&&`/`||` 不可用（需 `;`）、JSON 处理较弱。
* 写脚本时按 PS 5.1 语法来。

### ⚠️ 4.5 GBK 代码页（936）—— 完整对策见 **§3.11**
* 系统 ACP/OEMCP 均为 **936**；Python `open()` 默认 cp936、Java `file.encoding=GBK`、.NET `Encoding.Default=gb2312`。
* 更麻烦的是**三方不一致**：`chcp` 报 936，控制台实际 `GetConsoleOutputCP()`=65001，而各工具输出编码又各不相同（wmic=UTF-16LE、systeminfo=UTF-8、老工具=GBK）。
* 铁律：**任何落盘文本显式 `encoding='utf-8'`；不用终端显示判断编码，改用字节/hex 验证**。
* 逐语言写法（Python/Java/Node/C#/Go/C++/PS/Git/批处理）与 BOM 决策表见 **§3.11**。

### ⚠️ 4.6 工具链版本普遍偏旧
`VS2013 / VS 平台工具集 v120`、`MinGW GCC 5.1(32位)`、`windbg 6.12(2010)`、`WDK 14393(残缺)`、`ADB 1.0.26`、`SQL Server 2012`、`pip 22.2.1`、`VMware 14`、`iGPU 驱动 2016`。
现代化程度高的是：**Node 24 / Go 1.23 / Python 3.10+torch2.6 / CUDA 12.4 / FFmpeg 9 / LibreOffice 26 / Pandoc 3.10 / Git 2.52**。

### ⚠️ 4.7 硬件瓶颈
2 核 4 线程 + 4GB 显存 + 机械盘存仓库。多任务/并行构建/本地推理都要降低预期。

### ⚠️ 4.8 OpenCV 装了两套冲突发行版 + 无 GUI + 无 CUDA
* `opencv-contrib-python 4.10.0.84` 与 `opencv-python-headless 5.0.0.93` **同时存在**，`cv2` 实际是 5.0.0 headless。
* `cv2.imshow` **不可用**（`GUI: NONE`）；`cv2.cuda` **0 设备**（未编译 CUDA）。
* 任何 `pip install opencv-*` 都可能破坏 import → 装之前先 `pip uninstall` 两套再单一重装。
* 详见 §3.10(c)。

### ⚠️ 4.9 未安装 Vulkan SDK / 无 OpenGL 诊断工具
* 能**运行** Vulkan & OpenGL 程序（驱动自带 ICD），但**不能开发**：无 `VULKAN_SDK`、无 `glslangValidator`、无 `spirv-val`、无 `glview/glxinfo` 同类工具。
* 需要 shader 编译时装 LunarG Vulkan SDK 到 `E:\VulkanSDK`（勿装 C:）。
* 临时查 OpenGL 可用本仓库 `scripts\opengl-probe.cs` 现场编译，见 §5.9b。

---

## 5. 命令速查（复制即用）

### 5.1 通用注意
| 场景 | 用哪个 shell |
|---|---|
| 需要 `&&`、批处理、`call vcvarsall`、`cd /d` | **cmd**（`cmd /c "..."`） |
| Unix 工具（`grep/sed/find/tar/make`）、shell 脚本 | **Git Bash**：`& "D:\Program Files\Git\bin\bash.exe" -lc "..."` |
| PowerShell 专属 cmdlet | Windows PowerShell **5.1** |
| 含空格/括号的路径 | **必须加引号**（`C:\Program Files (x86)\...` 特别坑） |

### 5.2 Java
```powershell
java -version; javac -version          # 17.0.11
mvn -v                                 # 3.9.11，repo=C:\...\.m2 -> D:\maven\mvn
mvn -q -Dfile.encoding=UTF-8 clean package -DskipTests
gradle -v                              # 8.6，GRADLE_USER_HOME=E:\gradle\repository_uap
# 切 JDK21（注意嵌套目录）
$env:JAVA_HOME='C:\Java\jdk-21\jdk21.0.11_10'; $env:PATH="$env:JAVA_HOME\bin;$env:PATH"; java -version
```

### 5.3 Go
```powershell
go version                             # go1.23.3
go build ./...; go test ./...
$env:CGO_ENABLED='1'                   # 仅在补装 64 位 MinGW 后才有意义
```

### 5.4 C#（.NET Framework）
```powershell
# 单文件编译
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /out:app.exe Program.cs
# 传统 csproj（MSBuild 12）
& "C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe" MyApp.csproj /p:Configuration=Release
```

### 5.5 C/C++ (MSVC)
```powershell
# 必须一条命令里 call + 编译（cmd 环境，call 与 cl 之间用 &&）
cmd /c "call ""D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"" amd64 && cl /nologo /EHsc /O2 a.cpp && a.exe"
# 交互式：先开 cmd，然后
#   call "D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat" amd64
#   cl /EHsc a.cpp
```

### 5.6 MinGW (GCC 5.1, 32位)
```powershell
& "C:\Program Files (x86)\CodeBlocks\MinGW\bin\g++.exe" -std=c++14 -O2 a.cpp -o a.exe
& "C:\Program Files (x86)\CodeBlocks\MinGW\bin\mingw32-make.exe"
```

### 5.7 WinDbg / 性能分析
```powershell
$env:_NT_SYMBOL_PATH='srv*C:\dbg_symbols*https://msdl.microsoft.com/download/symbols'
& "C:\Program Files (x86)\Debugging Tools for Windows (x86)\windbg.exe" -z C:\dump.dmp
cmd /c "cd /d ""C:\Program Files (x86)\Debugging Tools for Windows (x86)"" && cdb.exe -z C:\dump.dmp -c ""!analyze -v;q"""
wpr -start CPU -filemode; wpr -stop trace.etl    # WPT 已在 PATH
```

### 5.8 Python
```powershell
python -V                              # 3.10.6
python -m pip install <pkg>            # 镜像：清华（生效）
$env:CUDA_VISIBLE_DEVICES='0'; python -c "import torch;print(torch.cuda.is_available())"   # 修复后应 True
```

### 5.9 CUDA
```powershell
nvcc --version                         # 12.4
& "C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4\extras\demo_suite\deviceQuery.exe"
nvidia-smi
nvidia-smi --query-gpu=pstate,clocks.sm,utilization.gpu,memory.used --format=csv   # 看是否降频
nvcc -arch=sm_61 -o k.exe k.cu         # GTX1050 = sm_61；需 -allow-unsupported-compiler(见 §4.1b)
python C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\cuda-check.py   # 一键自检
```

### 5.9b Vulkan / OpenGL / OpenCV

**一条命令全测**（推荐，覆盖 CUDA+Vulkan+OpenGL+OpenCV+GPU 状态）：
```powershell
powershell -ExecutionPolicy Bypass -File C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\check-all.ps1
```

单项调用：
```powershell
# --- Vulkan ---
vulkaninfo --summary                   # 已在 System32；只列出 NVIDIA GPU0
vulkaninfo | Select-String 'apiVersion|deviceName|driverVersion|conformanceVersion'

# --- OpenGL（本仓库自带探测源码，编译到 %TEMP% 不留垃圾） ---
csc /nologo /out:$env:TEMP\opengl-probe.exe C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\opengl-probe.cs
& $env:TEMP\opengl-probe.exe           # GL_VERSION / GL_RENDERER / GLSL / 各上限

# --- OpenCV ---
python C:\Users\Ice2Faith\.dsh\skills\user-computer-env\scripts\opencv-check.py
# 出图只能用 imwrite —— imshow 不可用（GUI: NONE）
# OpenCL 要全程 UMat，否则比 CPU 慢一倍
```

> 这些脚本一律 **ASCII 输出**：本机控制台代码页 936(GBK)，中文脚本在 PS 5.1 下会乱码（连 .ps1 源码都会被按 GBK 误读）。

### 5.10 Node
```powershell
npm.cmd -v; pnpm.cmd -v                # PS 里务必用 .cmd 后缀
pnpm.cmd install; pnpm.cmd build
```

### 5.11 多媒体 / 文档
```powershell
ffmpeg -i in.mp4 -c:v h264_nvenc -b:v 6M -c:a aac out.mp4     # NVENC
ffmpeg -hwaccel cuda -i in.mp4 ...                             # CUVID 解码
magick input.png -resize 800x output.webp
tesseract img.png stdout -l chi_sim+eng
pandoc doc.md -o doc.docx                                      # docx 可以
# PDF：pandoc 直接出 PDF 会失败（无 LaTeX）→ 先 docx/html，再 soffice 转
soffice --headless --convert-to pdf --outdir out doc.docx
7z a out.7z folder\                                            # 7z 不在 PATH，用全路径
```

### 5.12 数据/移动端
```powershell
sqlcmd -S "(localdb)\v11.0" -Q "select @@version"
adb devices; adb install app.apk
```

### 5.13 本地 LLM
```powershell
ollama serve            # 当前未运行；跑起来后 11434
ollama list
cd D:\llama.cpp; .\startup-qwen2.-coder-3b.bat    # llama-server :9090（Vulkan 加速）
```

---

## 6. 建议的补装清单（若后续要做现代开发）

| 需求 | 装什么 | 建议位置 |
|---|---|---|
| .NET 8 开发 | .NET 8 SDK（含 dotnet CLI） | `E:\dotnet` |
| 现代 C/C++ | CMake + Ninja + MinGW-w64 (x86_64) 或 VS2022 Build Tools | `E:\tools\` |
| 驱动开发 | WDK 10.0.22621 + VS2022 Build Tools + WinDbg (Store 版) | C:/E: |
| 现代调试 | WinDbg Preview（Microsoft Store，免费） | — |
| Go CGO | MinGW-w64 x86_64 | 加入 PATH |
| Node 原生模块 | Python 3.10（已有）+ VS2022 Build Tools，或改用预编译包 | — |
| 大模型推理加速 | 重编 llama.cpp 带 CUDA 后端（当前只有 Vulkan） | D:\llama.cpp |
| 版本管理 | gh CLI、nvm-windows（多 Node 版本） | E: |
| **Vulkan / shader 开发** | LunarG Vulkan SDK（含 glslangValidator、spirv-tools） | `E:\VulkanSDK` |
| **OpenCV 修好** | 先卸两套再单一装：`opencv-contrib-python`（要 GUI）或 `-headless`（服务端） | — |
| **OpenCV 的 GPU 加速** | 装 CUDA 版 OpenCV 需自行编译（耗时长）；或直接用 torch / onnxruntime 替代 | — |