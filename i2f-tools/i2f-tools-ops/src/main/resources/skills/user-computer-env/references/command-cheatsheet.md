# 命令速查（本机实测可用）

> 所有路径按本机实际位置书写。含空格/括号的路径**必须加引号**。
> shell 选择：需要 `&&` / `call` / 批处理 → **cmd**；Unix 工具 → **git-bash**；其余 → **Windows PowerShell 5.1**。

## 通用注意

| 场景 | 正确做法 |
|---|---|
| 在 PowerShell 里调 npm/pnpm | 用 `npm.cmd` / `pnpm.cmd`（`.ps1` 被执行策略拦截） |
| 跑 .ps1 脚本 | `powershell -ExecutionPolicy Bypass -File x.ps1` |
| PowerShell 5.1 语法 | 无 `??`、无三元、无 `&&`、无 `-Parallel`；用 `;` 串联 |
| 含 `(x86)` 的路径 | 双引号包裹，或 `& "..."` 调用 |
| 需要中文输出不乱码 | `chcp 65001`，或写文件再按显式编码读 |

---

## Java

```powershell
java -version; javac -version                 # 17.0.11
mvn -v                                        # 3.9.11, repo=D:\maven\mvn
mvn -q -Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8 clean package -DskipTests
gradle -v                                     # 8.6, GRADLE_USER_HOME=E:\gradle\repository_uap

# 切到 JDK 21（注意多一层目录）
$env:JAVA_HOME='C:\Java\jdk-21\jdk21.0.11_10'
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"
java -version

# 切到 JDK 8
$env:JAVA_HOME='C:\Java\jdk1.8.0_201'; $env:PATH="$env:JAVA_HOME\bin;$env:PATH"

# 运行（控制台 UTF-8）
java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 App
javac -encoding UTF-8 -d out src\*.java
```

## Go

```powershell
go version                                    # go1.23.3
go env GOROOT GOPATH GOMODCACHE GOPROXY
go build ./... ; go test ./...
$env:CGO_ENABLED='1'                          # 注意：需 64 位 MinGW，本机只有 32 位 TDM，实际编不了
```

## Node

```powershell
npm.cmd -v ; pnpm.cmd -v ; yarn -v            # 12.0.2 / 10.30.3 / 1.22.22
pnpm.cmd install ; pnpm.cmd run build
pnpm.cmd store path                           # D:\.pnpm-store\v10
node -e "console.log(process.version)"
```

## Python

```powershell
python -V                                     # 3.10.6
python -m pip -V
python -m pip install <pkg>                   # 镜像：清华 -> 阿里云 -> pypi.ngc.nvidia.com
python -X utf8 script.py                      # UTF-8 模式
$env:PYTHONUTF8='1'                           # 全局 UTF-8 模式
$env:PYTHONIOENCODING='utf-8'                 # 只改 stdio
```

## C#（.NET Framework）

```powershell
# 单文件编译
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe /out:app.exe Program.cs
# 传统 csproj
& "C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe" MyApp.csproj /p:Configuration=Release
```

## C/C++（MSVC 18.0 / VS2013）

```powershell
# 必须同一条命令里 call + 编译
cmd /c "call ""D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"" amd64 && cl /nologo /EHsc /utf-8 /O2 a.cpp && a.exe"

# 交互式：先开 cmd，然后
#   call "D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat" amd64
#   cl /EHsc a.cpp
```
> 资源编译 `rc.exe` 在 `C:\Program Files (x86)\Windows Kits\8.1\bin\x64\rc.exe`（不在 PATH）。
> 无 MFC/ATL 头文件。

## MinGW（GCC 5.1 / 32 位）

```powershell
& "C:\Program Files (x86)\CodeBlocks\MinGW\bin\g++.exe" -std=c++14 -O2 a.cpp -o a.exe
& "C:\Program Files (x86)\CodeBlocks\MinGW\bin\mingw32-make.exe"
```

## CUDA

```powershell
nvcc --version                                # 12.4
nvidia-smi
nvidia-smi --query-gpu=pstate,clocks.sm,utilization.gpu,memory.used --format=csv
& "C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4\extras\demo_suite\deviceQuery.exe"

# 编 kernel（VS2013 超出 CUDA 12.4 支持范围，必须加 -allow-unsupported-compiler）
cmd /c "call ""D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"" amd64 && nvcc -allow-unsupported-compiler -arch=sm_61 -o k.exe k.cu && k.exe"
```

## Vulkan / OpenGL / OpenCV

```powershell
vulkaninfo --summary                          # 只有 NVIDIA GPU0
vulkaninfo | Select-String 'apiVersion|deviceName|driverVersion|conformanceVersion'

# OpenGL 现场探测（源码随技能附带）
csc /nologo /out:$env:TEMP\opengl-probe.exe "<skill-dir>\scripts\opengl-probe.cs"
& $env:TEMP\opengl-probe.exe                  # GL_VERSION / GL_RENDERER / GLSL

python "<skill-dir>\scripts\opencv-check.py"  # OpenCV 能力面
```

## WinDbg / 性能分析

```powershell
$env:_NT_SYMBOL_PATH='srv*C:\dbg_symbols*https://msdl.microsoft.com/download/symbols'
& "C:\Program Files (x86)\Debugging Tools for Windows (x86)\windbg.exe" -z C:\dump.dmp
cmd /c "cd /d ""C:\Program Files (x86)\Debugging Tools for Windows (x86)"" && cdb.exe -z C:\dump.dmp -c ""!analyze -v;q"""
wpr -start CPU -filemode ; wpr -stop trace.etl        # WPT 已在 PATH
```

## 多媒体 / 文档

```powershell
ffmpeg -i in.mp4 -c:v h264_nvenc -b:v 6M -c:a aac out.mp4
ffmpeg -hwaccel cuda -i in.mp4 ...
magick input.png -resize 800x output.webp
tesseract img.png stdout -l chi_sim+eng
pandoc doc.md -o doc.docx                     # docx 可以
# PDF：pandoc 直接出会失败（无 LaTeX）
soffice --headless --convert-to pdf --outdir out doc.docx
"C:\Program Files\7-Zip\7z.exe" a out.7z folder\
```

## 数据 / 移动端

```powershell
sqlcmd -S "(localdb)\v11.0" -Q "select @@version"
sqllocaldb info
adb devices ; adb install app.apk
```

## 本地 LLM

```powershell
ollama serve                                  # 当前未运行；11434
ollama list
cd D:\llama.cpp ; .\startup-qwen2.-coder-3b.bat   # llama-server :9090（Vulkan）
```

## 系统信息查询

```powershell
Get-CimInstance Win32_ComputerSystem          # 机型/内存
Get-CimInstance Win32_OperatingSystem         # OS 版本
Get-CimInstance Win32_Processor               # CPU
Get-CimInstance Win32_PhysicalMemory          # 内存条
Get-CimInstance Win32_VideoController         # GPU
Get-CimInstance Win32_LogicalDisk             # 磁盘
# wmic 已 deprecated，且输出 UTF-16LE；systeminfo 输出 UTF-8 且字段本地化中文
```