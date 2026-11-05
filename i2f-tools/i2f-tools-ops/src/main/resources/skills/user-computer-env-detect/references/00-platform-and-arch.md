# S-1 平台与架构（先定平台，再定命令）

> **为什么必须先做这一步**：同一个问题在不同平台上的命令、路径、编码、提权方式完全不同。
> 用 Windows 的 `Get-CimInstance` 去探测 Linux，或拿 `apt` 去问 macOS，得到的不是"没有"，而是"错误"。
> **先定平台 → 才能选对探测方式。**

---

## 1. 系统类型判定

| 平台 | 主判据 | 辅助判据 |
|---|---|---|
| **Windows** | `$env:OS = "Windows_NT"`；`Win32_OperatingSystem` | 盘符存在（`C:\`） |
| **Linux** | `uname -s` = `Linux` | `/etc/os-release` |
| **macOS** | `uname -s` = `Darwin` | `sw_vers` 有输出 |
| **Android / Termux** | `uname -o` = `Android`，或 `$PREFIX` 含 `com.termux` | `/data/data/com.termux/files/usr` |
| **WSL**（Linux 子系统） | `uname -s` = `Linux` 且 `/proc/version` 含 `microsoft` | `$WSL_DISTRO_NAME`、`/mnt/c` |
| **MSYS / Cygwin（git-bash）** | `uname -s` = `MINGW64_NT-*` / `MSYS_NT-*` / `CYGWIN_*` | `$OSTYPE` 含 msys |
| BSD | `uname -s` = `FreeBSD` / `OpenBSD` / `NetBSD` | — |

判断顺序建议：先看是不是 Windows 的 **msys/cygwin**（此时 `uname` 会说"像 Windows"），再看 WSL，最后才是原生 Linux/macOS。

```sh
uname -s; uname -o; uname -m; uname -r
grep -qi microsoft /proc/version && echo "WSL"
[ -n "$PREFIX" ] && echo "$PREFIX"     # Termux
```

## 2. 架构判定与命名归一化

`uname -m` / `$env:PROCESSOR_ARCHITECTURE` 的取值五花八门，先归一化再决策：

| 原始值 | 归一 | 含义 | 注意 |
|---|---|---|---|
| `x86_64`、`amd64`、`AMD64`、`x64` | **x64** | 64 位 Intel/AMD | 最常见；`amd64` 就是 x64，不是"AMD 专用" |
| `i386`、`i486`、`i586`、`i686`、`x86` | **x86** | 32 位 | 现代工具链常无 32 位构建 |
| `aarch64`、`arm64`、`ARM64` | **arm64** | 64 位 ARM | Apple Silicon、多数 Android 手机、ARM 服务器 |
| `armv7l`、`armv6l`、`armv8l`、`armhf` | **arm32** | 32 位 ARM | 树莓派旧版等 |
| `riscv64`、`ppc64le`、`s390x` | 原样 | 其他 | 记录原值，别硬套 |

**跨平台陷阱**：

* **Apple Silicon 上的 Rosetta**：`uname -m` 在 x86_64 模拟进程里返回 `x86_64`，但真机是 arm64。
  `sysctl -n sysctl.proc_translated` 为 `1` 表示被转译 → 装原生 arm64 才快。
* **Windows 32 位进程跑在 64 位系统**：`$env:PROCESSOR_ARCHITECTURE = "x86"` 但
  `$env:PROCESSOR_ARCHITEW6432 = "AMD64"` → 真实系统是 x64。别误判成 32 位机器。
* **Termux 在 arm64 手机上**：`uname -m` = `aarch64`，但**没有 root**，装不了系统级包。

**归一化后的用途**：选二进制（`x64` / `arm64`）、定编译目标（`-arch=sm_61`、`-march=armv8-a`）、
判断某些工具是否有对应发行版。

## 3. 各平台探测方式差异（**这张表决定用哪套命令**）

| 维度 | Windows | Linux | macOS | Termux |
|---|---|---|---|---|
| Shell | PowerShell 5.1/7、cmd、git-bash | bash/zsh/sh | zsh/bash | bash（sh 兼容） |
| 系统信息 | `Get-CimInstance` / `wmic` / `systeminfo` | `/proc/*`、`lsblk`、`lscpu`、`free -h`、`df -h` | `sw_vers`、`system_profiler`、`sysctl` | `getprop`、`df -h` |
| 包管理 | winget / choco / scoop | apt / dnf / yum / pacman / zypper | brew / port | `pkg` / `apt`（Termux 源） |
| 安装根 | `C:\Program Files*`、`D:\...`、`%LOCALAPPDATA%` | `/usr`、`/usr/local`、`/opt`、`~/.local` | `/opt/homebrew`（ARM）、`/usr/local`（Intel） | `$PREFIX`（`/data/data/com.termux/files/usr`） |
| 提权 | UAC（管理员令牌） | `sudo` | `sudo` | ❌ **通常无 root** |
| 默认编码 | 随代码页（936/932/1252…） | 通常 UTF-8（看 locale） | UTF-8 | UTF-8 |
| 路径分隔 | `\`；盘符 | `/` | `/` | `/` |
| 换行 | CRLF（`core.autocrlf`） | LF | LF | LF |
| 编译器 | MSVC / MinGW / LLVM | gcc / clang / LLVM | clang（Xcode CLT） | clang（部分可用） |
| 构建工具 | MSBuild / nmake / cmake | make / ninja / cmake | make / ninja / cmake | make / cmake |
| CUDA | ✅ NVIDIA | ✅ NVIDIA | ❌ **不支持**（用 Metal/MPS） | ❌ |
| GPU 查询 | `nvidia-smi`、`Win32_VideoController` | `nvidia-smi`、`lspci \| grep -i vga` | `system_profiler SPDisplaysDataType` | 无 |
| OpenGL 查询 | 现场建上下文（`opengl-probe.cs`） | `glxinfo -B`、`eglinfo` | 无 glxinfo，需 Metal 程序 | 通常不可用 |
| 长驻服务 | 服务 / 计划任务 | systemd / cron | launchd | 无（Termux:Boot 除外） |

## 4. Unix 侧工具链探测命令（对应 Windows 的 detect-toolchains.ps1）

```sh
# Java
java -version 2>&1; javac -version 2>&1
echo "JAVA_HOME=$JAVA_HOME"
ls -d /usr/lib/jvm/* /opt/java/* ~/.sdkman/candidates/java/* 2>/dev/null
update-alternatives --list java 2>/dev/null      # Debian/Ubuntu 多版本
/usr/libexec/java_home -V 2>&1                   # macOS 多版本（关键！）
mvn -v 2>&1; gradle -v 2>&1

# Python
python3 -V; python -V 2>/dev/null; pip3 -V
which -a python3 python
ls -d ~/.pyenv/versions/* /usr/bin/python3.* 2>/dev/null
python3 -c "import sys,locale;print(sys.version.split()[0], locale.getpreferredencoding(False))"

# Go
go version; go env GOROOT GOPATH GOMODCACHE GOPROXY GOARCH CGO_ENABLED
ls -d /usr/local/go ~/go /opt/go 2>/dev/null

# Node
node -v; npm -v; pnpm -v 2>/dev/null; yarn -v 2>/dev/null
ls -d ~/.nvm/versions/node/* 2>/dev/null
npm config get registry 2>/dev/null

# C/C++
cc --version 2>&1 | head -1; gcc --version 2>&1 | head -1; clang --version 2>&1 | head -1
cmake --version 2>&1 | head -1; make --version 2>&1 | head -1; ninja --version 2>&1
xcode-select -p 2>/dev/null                      # macOS：Xcode CLT 是否装了

# 其他
git --version; ffmpeg -version 2>&1 | head -1; pandoc --version 2>&1 | head -1
```

**版本管理器优先级**：Unix 上 `which` 经常只找到系统自带版本，真实版本被 sdkman / nvm / pyenv / asdf 管理：
`~/.sdkman/candidates/*`、`~/.nvm/versions/node/*`、`~/.pyenv/versions/*`、`~/.asdf/installs/*`。
**报告里要区分"系统版"与"管理器版"**，否则会出现"用户说装了 JDK21，你只看到 JDK11"。

## 5. 平台专属注意

### Android / Termux
* **无 root**：不能 `sudo`、不能装系统级驱动、不能写 `/system`。
* **无 CUDA / 无桌面 OpenGL 上下文**：GPU 计算基本不可用（个别设备可 Vulkan，但不稳定）。
* 包来自 Termux 源：`pkg install`；`openjdk-17`、`python`、`nodejs`、`golang`、`gcc` 都有对应包。
* 存储：`~/storage/shared` 指向手机共享存储（需 `termux-setup-storage` 授权）。
* 长驻进程会被系统杀；不要假设后台任务能一直跑。
* 架构几乎都是 **arm64**。

### macOS
* **CUDA 不可用**；GPU 计算用 **Metal / MPS**（PyTorch 的 `mps` 后端）。
* Intel Mac 与 Apple Silicon 的 Homebrew 前缀不同：`/usr/local` vs `/opt/homebrew`。
* 编译器来自 Xcode Command Line Tools（`xcode-select -p`），不是独立 gcc。
* 系统完整性保护（SIP）限制改系统目录；优先装用户级。
* `brew install` 装的东西可能不在 PATH（尤其 ARM 的 `/opt/homebrew/bin`）。

### WSL
* **两套世界**：Linux 侧与 Windows 侧。判断当前在哪一侧很重要。
* 访问 Windows 文件：`/mnt/c/...`（性能差，大量小文件 IO 会极慢）。
* 访问 Linux 文件：Windows 侧 `\\wsl$\<distro>\...`。
* 能跑 Linux 原生工具链；GPU 通过 WSL 的 CUDA passthrough（需 Windows 侧驱动 + `nvidia-smi` 在 Linux 侧可见）。
* 编码默认 UTF-8，但**与 Windows 侧交换文件时会碰 CRLF/BOM 问题**。

### Windows（含 git-bash / MSYS）
* `uname` 存在≠Linux：`MINGW64_NT-*` 是 Windows 上的 POSIX 层，**路径仍是 Windows 盘符**。
* git-bash 自带 Unix 工具（`link`、`sort`、`find`、`tar`），**可能遮蔽 MSVC 的同名工具**。
* 多盘符、盘符可用空间差异大 → 落盘前先枚举。

## 6. 探测方式路由（决定接下来跑谁）

```
平台 = ?
├─ Windows（原生）
│    ├─ PowerShell 可用      → detect-platform.ps1 → detect-system.ps1 → detect-toolchains.ps1
│    │                          → detect-tools.ps1 → detect-graphics.ps1 → detect-persona.ps1
│    └─ 只有 cmd / 执行策略限制 → 用 detect-*.ps1 但以 powershell -ExecutionPolicy Bypass -File 调用
├─ WSL / Linux / macOS / Termux
│    └─ detect-platform.sh  → 再按 §4 的命令清单分栈探测
└─ git-bash(MSYS)
     ├─ 查 Windows 侧环境 → 转到 Windows 分支（用 detect-*.ps1）
     └─ 查 Unix 工具     → sh detect-platform.sh 得到 msys 分支结果
```

## 7. 归档到报告里的必填项

* 平台类型（Windows / Linux / macOS / Android-Termux / WSL / MSYS）
* 发行版/版本号（`os-release` / `sw_vers` / build number）
* **归一化架构**（x64 / x86 / arm64 / arm32）+ 原始 `uname -m`
* 是否 32 位进程跑在 64 位系统上
* 可用 shell 与包管理器
* 提权能力（UAC / sudo / root 有无）
* 默认编码/locale
* 若为 WSL：Linux 侧与 Windows 侧是否都可用