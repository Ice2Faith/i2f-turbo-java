# S2 开发工具链探测

## 通用三层法

对每个栈依次尝试，**上一层失败才走下一层**：

1. **L1 命令**：直接跑版本命令（说明已配 PATH）
2. **L2 环境变量**：`JAVA_HOME` / `GOROOT` / `CONDA_PREFIX` / `NODE_HOME` 等
3. **L3 磁盘扫描**：扫常见安装根 + 注册表卸载键（说明"装了但没配 PATH"）

**L3 的常见安装根**（跨盘，深度限制 3~5 层，避免扫爆）：

```
C:\Program Files\*          C:\Program Files (x86)\*
D:\Program Files\*          D:\Programs\*        D:\<工具名>*
E:\<工具名>*                F:\<工具名>*        G:\<工具名>*
%USERPROFILE%\scoop\apps\*  %LOCALAPPDATA%\Programs\*
C:\ProgramData\chocolatey\lib\*
```

判定输出必须包含：**版本 + 绝对路径 + 调用方式**。

---

## Java

```powershell
java -version 2>&1 ; javac -version 2>&1
"JAVA_HOME=$env:JAVA_HOME"
mvn -v 2>&1 ; gradle -v 2>&1
Get-ChildItem 'C:\Program Files\Java','C:\Java','D:\Java','D:\Program Files\Java' -Directory -EA 0
```

必查易漏点：

| 易漏点 | 说明 |
|---|---|
| 多个 JDK 共存 | 报告要列出**所有**版本，并说明当前生效的是哪个 |
| JDK 目录多一层 | 如 `jdk-21\jdk21.0.11_10`，`JAVA_HOME` 要指到内层 |
| `javapath` 影子 | `C:\Program Files\Common Files\Oracle\Java\javapath` 常排在 PATH 前面，可能与 JAVA_HOME 不一致 |
| Maven `platform encoding` | `mvn -v` 会打印平台编码；非 UTF-8 就要提醒 `-Dfile.encoding=UTF-8` |
| Maven 本地仓库位置 | `~/.m2/settings.xml` 的 `localRepository`，可能是符号链接到别的盘 |
| Gradle 缓存 | `GRADLE_USER_HOME`，可能不在 `~/.gradle` |

## Python

```powershell
python -V 2>&1 ; python -m pip -V 2>&1
py -0p                      # 列出所有已注册解释器（Windows）
where python
conda --version 2>&1
Get-ChildItem "$env:LOCALAPPDATA\Programs\Python" -Directory -EA 0
```

必查易漏点：

| 易漏点 | 说明 |
|---|---|
| **坏目录** | 有 `Lib`/`Scripts` 但**没有 `python.exe`** 的残壳，必须在报告里标注勿用 |
| `py -0p` 与 PATH 不一致 | py 启动器只认注册过的版本 |
| 关键包 | `pip list` 过滤 torch/numpy/pandas/opencv/docx/pptx/openpyxl |
| pip 镜像 | `pip config list`；多份 pip.ini 有优先级，以 `pip config list` 为准 |
| 编码默认值 | `python -c "import locale;print(locale.getpreferredencoding(False))"` |
| 是否 conda 环境 | `CONDA_PREFIX`、`conda info --envs` |

## Go

```powershell
go version 2>&1
go env GOROOT GOPATH GOMODCACHE GOPROXY GOOS GOARCH CGO_ENABLED 2>&1
Get-ChildItem 'C:\Go','D:\Go','E:\Go' -Directory -EA 0
```

判据：
- `GOPROXY` 是否国内镜像（决定拉包速度）
- **`CGO_ENABLED=0`** → 依赖 C 的包（sqlite、部分图像库）编不了
- 是否有 64 位 gcc（MinGW-w64 / LLVM），没有则 CGO 实际不可用

## Node.js

```powershell
node -v ; npm -v ; pnpm -v 2>&1 ; yarn -v 2>&1
npm config get registry 2>&1
npm ls -g --depth=0 2>&1
```

**Windows 关键坑**：PowerShell 里 `npm`/`pnpm` 会命中 `.ps1` 包装器，被执行策略拦截（"禁止运行脚本"）。
→ 一律用 `npm.cmd` / `pnpm.cmd`，或 `-ExecutionPolicy Bypass`。

其他：`node-gyp` 需要 Python + MSVC（VS2015+），老 VS 会导致原生模块编译失败。

## C# / .NET

```powershell
dotnet --version 2>&1 ; dotnet --list-sdks 2>&1 ; dotnet --list-runtimes 2>&1
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /? 2>&1 | Select-Object -First 2
Get-ChildItem 'C:\Program Files (x86)\MSBuild' -Directory -EA 0
(Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\NET Framework Setup\NDP\v4\Full' -EA 0).Release
```

判据表：

| 组合 | 能做什么 |
|---|---|
| 有 `dotnet` SDK | 现代 .NET（SDK 风格项目、C# 最新语法） |
| 只有 .NET FW + `csc` | 传统 .NET Framework 4.x；**C# 语法受 csc 版本限制**（4.8 的 csc 仅支持 C# 5） |
| MSBuild 版本低 | 无法通过 `MSBuild` 构建新式项目（需 15+） |

⚠️ 常见误判：装了 VS 不等于装了 C# 编译器/工作负载，也不等于有 `dotnet`。

## C/C++（MSVC / MinGW / LLVM）

```powershell
cl 2>&1 | Select-Object -First 2                 # 通常失败
where.exe cl link lib nmake rc 2>&1
Get-ChildItem 'C:\Program Files\Microsoft Visual Studio','C:\Program Files (x86)\Microsoft Visual Studio',
             'D:\Program Files\Microsoft Visual Studio','D:\Program Files (x86)\Microsoft Visual Studio' `
  -Recurse -Filter cl.exe -Depth 6 -EA 0 | Select -First 5
where.exe vswhere.exe
& "${env:ProgramFiles(x86)}\Microsoft Visual Studio\Installer\vswhere.exe" -all -products * -format json
gcc --version 2>&1 ; g++ --version 2>&1 ; clang --version 2>&1
cmake --version 2>&1 ; make --version 2>&1 ; ninja --version 2>&1
```

必查：

| 项 | 意义 |
|---|---|
| `vcvarsall.bat` / `VsDevCmd.bat` 是否存在 | 决定**怎么激活**编译环境 |
| cl 主版本 | VS2013=18、VS2015=19.0、VS2017=19.1x、VS2019=19.2x、VS2022=19.3x |
| 有无 MFC/ATL 头 | 决定能否编 GUI 老项目 |
| `rc.exe` | 通常不在 VC 目录，而在 Windows SDK 的 `bin\x64` |
| MinGW 位数 | 32 位 GCC 服务不了 amd64 目标 |
| 有无 cmake/ninja | 现代 C++ 项目几乎都需要 |

**必须实测编译一次**（这是唯一可靠的判定）：

```bat
cmd /c "call ""<VS>\VC\vcvarsall.bat"" amd64 && cl /nologo /EHsc a.cpp && a.exe"
```

## Windows SDK / WDK / 调试器

```powershell
Get-ChildItem 'C:\Program Files (x86)\Windows Kits' -Directory -EA 0
Get-ChildItem 'C:\Program Files (x86)\Windows Kits\10\Include' -Directory -EA 0   # 看 SDK 版本号
Test-Path 'C:\Program Files (x86)\Windows Kits\10\Include\<ver>\um\windows.h'      # SDK 是否完整
Test-Path 'C:\Program Files (x86)\Windows Kits\10\Include\<ver>\km\wdm.h'          # WDK 是否完整
where.exe windbg cdb kd ntsd symchk 2>&1
Get-ChildItem 'C:\Program Files (x86)\Debugging Tools for Windows*' -Directory -EA 0
wpr -? 2>&1 | Select-Object -First 2       # Windows Performance Toolkit
```

⚠️ **"注册表里有 WDK"≠"能编驱动"**：必须验证 `km\wdm.h` 真实存在。
同理 `Windows Kits\10` 可能只有空壳 `Include` 目录（装了一半）。

## 判定的最后一步：跑个 hello world

对每个声称"可用"的栈，跑一次最小样例：

| 栈 | 样例 |
|---|---|
| Java | `javac` + `java` 打印版本 |
| Go | `go run` 打印字符串 |
| Python | `print(sys.version)` |
| C# | `csc` 编译后运行 |
| C++ | `cl` 或 `g++` 编译后运行 |
| Node | `node -e "console.log(1)"` |

**只报版本不报"实测能编译运行"，等于没测。**