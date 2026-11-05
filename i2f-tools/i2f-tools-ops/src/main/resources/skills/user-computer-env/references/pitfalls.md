# 已知问题与短板清单

> 按影响优先级排列。带 ✅ 的表示已修复/已确认，带 ⏳ 的表示待办。

## 🔴 高优先级

### ✅ CUDA_VISIBLE_DEVICES 已修复
原值为 `1`，本机只有 1 个 CUDA 设备（索引 0）→ 所有 CUDA 程序看不到 GPU。
2026-11-04 改为 `0`，11-05 重启后复核全线通过（deviceQuery PASS、torch sm_61、matmul ~1.4 TFLOPS、nvcc kernel OK）。
**注意**：改环境变量后必须换新进程链（注销/重登或重启 explorer），子进程继承父进程环境块，不读注册表。

### 🔴 C 盘只剩 7.8 GB
Windows 占 27.5GB、Program Files 14GB、PF(x86) 12.8GB、Python310 6.1GB、ProgramData 4.8GB。
可回收：`C:\Windows\SoftwareDistribution\Download`、`C:\Users\...\$Windows.~WS`（升级残留）、`C:\dbg_symbols`、pip cache、C 盘 4GB pagefile 可移走。
**新装工具一律装 E/F/G。**

## ⚠️ 工具链缺失（按需补）

### ⚠️ 无 .NET SDK（最大短板）
没有 `dotnet` CLI，`C:\Program Files\dotnet` 不存在，全盘无 `dotnet.exe`。
只有 .NET Framework 4.8 + `csc` 4.8（**仅 C# 5**）+ **MSBuild 12 only**。
→ 只能做传统 .NET Framework 4.x；**做不了 .NET Core / 5+ / SDK 风格项目**。
补装：.NET 8 SDK 到 `E:\dotnet`（约 200MB），可同时获得 dotnet CLI + Roslyn + 现代 MSBuild。

### ⚠️ MSVC 只有 VS2013（cl 18.00）
- 语言标准只有**部分 C++11**（无完整 constexpr、无 C++14/17）。
- **无 MFC/ATL 头文件**，无 `rc.exe`（用 SDK 8.1 的）。
- `nvcc` 不支持（需 `-allow-unsupported-compiler`）。
- 无 `cmake` / `make` / `ninja` / `clang` / `vcpkg` / `conan` → **绝大多数现代 C++ 开源项目无法构建**。

### ⚠️ MinGW 是 TDM-GCC 5.1 32 位
`C:\Program Files (x86)\CodeBlocks\MinGW\bin`，**不在 PATH**，32 位 → 与 amd64 目标不匹配，Go CGO 实际编不了。

### ⚠️ WinDbg 6.12（2010, x86）；WDK 残缺
- WinDbg/cdb/kd 在 `C:\Program Files (x86)\Debugging Tools for Windows (x86)`，**不在 PATH**，需自设 `_NT_SYMBOL_PATH`。
- `Windows Kits\10\Include\10.0.14393.0` **残缺**（km 仅 31 文件、um 141 文件，**无 `wdm.h`**）→ **不能编驱动**。
- Windows Kit 10 **无 Debuggers 目录**；8.1 的 `Debuggers\x64` 只有 dbghelp/symsrv。
- 完整可用的是 **SDK 8.1**。
→ 分析 dump 够用；驱动开发需装 WDK 10.0.22621 + VS2022 Build Tools + WinDbg(Store)。

### ⚠️ 未安装 Vulkan SDK
能运行不能开发（无 `glslangValidator`/`spirv-val`）。装到 `E:\VulkanSDK`。

### ⚠️ OpenCV 双发行版冲突 + 无 GUI + 无 CUDA
详见 `graphics.md` §4。

## ⚠️ 环境/脚本层

### ⚠️ 执行策略阻止 PowerShell 跑 .ps1
所有 scope 都是 `Undefined`（PS 5.1 默认 Restricted 行为）。`npm`/`pnpm` 的 `.ps1` 包装器会报"禁止运行脚本"。
解法：用 `npm.cmd`/`pnpm.cmd`；或 `powershell -ExecutionPolicy Bypass -File x.ps1`；或一次性改 `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned`。

### ⚠️ 没有 PowerShell 7
当前是 **Windows PowerShell 5.1.19041.6456**（`C:\Program Files\PowerShell\7` 不存在）。
无 `??`、无三元 `? :`、无 `-Parallel`、`&&`/`||` 不可用（用 `;`）。

### ⚠️ GBK 代码页导致乱码
系统 ACP/OEMCP=936，控制台 CP=65001，工具输出编码各异。详见 `encoding.md`。

### ⚠️ 工具链版本普遍偏旧
`VS2013`、`MinGW GCC 5.1(32位)`、`windbg 6.12(2010)`、`WDK 14393(残缺)`、`ADB 1.0.26`、`SQL Server 2012`、`pip 22.2.1`、`VMware 14`、`Intel iGPU 驱动 2016`。
现代化程度好的：Node 24 / Go 1.23 / Python 3.10+torch2.6 / CUDA 12.4 / FFmpeg 9 / LibreOffice 26 / Pandoc 3.10 / Git 2.52。

### ⚠️ 其他缺失
无 `gh` CLI、无 `docker`、无 `mysql`/`redis` 客户端、无 `conda`、无 WSL（`wsl -l -v` 不可用）。
`C:\Program Files\Python314` 是**坏目录**（无 `python.exe`），别用。
pip 22.2.1 偏旧，建议 `python -m pip install -U pip`。

## ⚠️ 硬件瓶颈
**2 核 4 线程** + 4GB 显存 + 机械盘（E/F/G 同一块 HDD）存仓库。
并行编译 `-j` ≤ 4；多子代理并发、本地大模型推理都要降低预期。GPU 冷启动有降频（详见 `graphics.md` §1）。

## 🔐 安全提醒

`ConsentPromptBehaviorAdmin = 0`（提权不弹窗）意味着**本机任何以当前账户运行的程序都能静默提权到管理员**。
这是便利性取舍；若在意可改为 `5`（默认弹窗）或 `2`。在此之前**别跑来源不明的 exe**。

## 网段与端口

WLAN `10.23.70.251/24`；VMware VMnet1 `192.168.243.1`、VMnet8 `192.168.233.1`。
Maven 内网镜像 `http://10.1.4.161:8085/repository/BI3_release/` —— **离开公司内网会超时**，必要时注释掉该 mirror。
当前仅 8080 有 Java 在监听；11434（Ollama）未运行。