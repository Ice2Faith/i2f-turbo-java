# S3 常见工具 & S4 图形计算栈

## 一、常见工具探测清单

对每项都记录：**版本 + 路径 + 是否在 PATH + 调用注意事项**。

```powershell
# 版本清点
foreach($t in 'git','ffmpeg','ffprobe','pandoc','soffice','magick','convert','tesseract',
              '7z','docker','adb','sqlcmd','curl','ssh','gh','wget','unzip'){
  $c = Get-Command $t -ErrorAction SilentlyContinue
  if($c){ "OK   $t -> $($c.Source)" } else { "MISS $t" }
}
```

### 1.1 版本控制

```powershell
git --version
git config --global --get user.name ; git config --global --get user.email
git config --global --get core.autocrlf   ; git config --global --get core.quotepath
Test-Path 'C:\Program Files\Git\bin\bash.exe'      # git-bash 是否存在
svn --version --quiet 2>&1
```

要点：
- **git-bash 是很多 Unix 工具的唯一来源**（`tail`/`grep`/`sed`/`tar`），值得单独记
- `core.autocrlf=true`（Windows 默认）→ 跨平台协作要提醒
- `core.quotepath=true` 会让中文路径显示成 `\346\226\207`

### 1.2 媒体（ffmpeg）

```powershell
ffmpeg -version 2>&1 | Select-Object -First 1
ffmpeg -hide_banner -encoders 2>&1 | Select-String 'nvenc|qsv|amf|vaapi|videotoolbox'
ffmpeg -hide_banner -hwaccels 2>&1
nvidia-smi --query-gpu=name,encoder.stats.sessionCount --format=csv 2>&1
```

判据：有无硬件编码器决定转码方案（`h264_nvenc` 比 `libx264` 快数倍且省 CPU）。
`--enable-cuda-llvm`/`--enable-cuvid` 表示支持 GPU 解码链路。

### 1.3 文档（pandoc / LibreOffice / OCR）

```powershell
pandoc --version 2>&1 | Select-Object -First 2
pandoc --list-output-formats 2>&1 | Select-String 'pdf'
foreach($e in 'xelatex','pdflatex','tectonic','wkhtmltopdf','typst'){ "$e : $((Get-Command $e -EA 0).Source)" }
soffice --version 2>&1
tesseract --version 2>&1 | Select-Object -First 1
tesseract --list-langs 2>&1
```

**关键判据：pandoc 能否直接出 PDF，取决于有没有 LaTeX 引擎。**
没有 → 必须走 `pandoc → docx/html → soffice --convert-to pdf` 的间接路线。这条一定要写进报告。

### 1.4 图像（ImageMagick）

```powershell
magick -version 2>&1 | Select-Object -First 1
convert -version 2>&1 | Select-Object -First 1    # ImageMagick 6 才是 convert
Test-Path 'C:\Program Files\ImageMagick*\policy.xml'
```

要点：IM7 命令是 `magick`；**policy.xml 可能禁用 PDF/PS**（安全策略），要读一下有没有被注释掉的 module policy。

### 1.5 包管理器 / 其他

```powershell
choco --version 2>&1 ; scoop --version 2>&1 ; winget --version 2>&1
docker --version 2>&1
adb version 2>&1
sqlcmd -? 2>&1 | Select-Object -First 3
```

---

## 二、图形与计算栈

### 2.1 CUDA

```powershell
nvidia-smi
nvidia-smi -L                                                    # 设备列表与 UUID
nvidia-smi --query-gpu=index,name,uuid,driver_version,memory.total,compute_cap,pstate,clocks.sm --format=csv
nvcc --version
"CUDA_PATH=$env:CUDA_PATH"
Get-ChildItem 'C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA' -Directory -EA 0
Test-Path "$env:CUDA_PATH\include\cudnn.h"                       # cuDNN
& "$env:CUDA_PATH\extras\demo_suite\deviceQuery.exe"             # 权威验证
& "$env:CUDA_PATH\extras\demo_suite\bandwidthTest.exe"
```

**必查的三件事**：

| 检查 | 为什么 |
|---|---|
| `CUDA_VISIBLE_DEVICES` 的值 | 设错（如只有 1 卡却设 `1`）会让**所有** CUDA 程序看不到 GPU；`-1` = 主动禁用 |
| 设备索引与实际卡数 | CUDA 只给 **NVIDIA** 卡从 0 顺排编号，与 PCI 总线号无关 |
| 驱动版本 vs toolkit 版本 | 驱动要 ≥ toolkit 要求；驱动支持更高版本是正常的（向下兼容） |

**验证"真的能用"**（版本号不够）：

```powershell
# Python
python -c "import torch;print(torch.cuda.is_available(), torch.cuda.device_count())"
# C，无需框架
& "$env:CUDA_PATH\extras\demo_suite\deviceQuery.exe" | Select-String 'Result ='
```

**性能基线要预热**：新启动的 GPU 常处于低功耗态（如 P8 / 139MHz），首测可能只有真实性能的 1/3。
必须预热 3 轮以上再计时，否则会得出错误结论。

**编译注意**：`nvcc` 对宿主编译器版本有白名单（如 CUDA 12.x 只认 VS2017–2019）。
版本不匹配会 `nvcc fatal : nvcc cannot find a supported version of Microsoft Visual Studio`。
可试 `-allow-unsupported-compiler` 硬过，但要标注"非官方支持"。

### 2.2 Vulkan

```powershell
where.exe vulkaninfo 2>&1
vulkaninfo --summary 2>&1
"VULKAN_SDK=$env:VULKAN_SDK"                    # 有 SDK 才能编译 shader
Get-ItemProperty 'HKLM:\SOFTWARE\Khronos\Vulkan\Drivers' -EA 0
Get-ChildItem 'C:\Windows\System32' -Filter '*-vk*.json' -EA 0
foreach($t in 'glslangValidator','spirv-val','spirv-cross'){ "$t : $((Get-Command $t -EA 0).Source)" }
```

判据：
- `vulkaninfo` 能列出**几个** `GPU0/GPU1`？核显驱动过旧可能**没有 ICD**，只有独显可见
- `apiVersion` / `conformanceVersion` 反映驱动能力
- **能运行 ≠ 能开发**：没有 SDK 就没有 `glslangValidator`，写不了 shader

### 2.3 OpenGL（必须实测，不能只看注册表）

`scripts/opengl-probe.cs` 会创建隐藏窗口 + 真实 GL 上下文，读取 `glGetString`：

```powershell
csc /nologo /out:"$env:TEMP\opengl-probe.exe" "<skill目录>\scripts\opengl-probe.cs"
& "$env:TEMP\opengl-probe.exe"
```

输出 `GL_VENDOR` / `GL_RENDERER` / `GL_VERSION` / `GL_SHADING_LANGUAGE_VERSION` + 各上限。

要点：
- **桌面由核显驱动时，GL 上下文仍可能落到独显**（NVIDIA ICD 优先）→ 不能凭"显示输出在哪块卡"推断 GL 版本
- ICD 一般在 `DriverStore\FileRepository\...` 下（`nvoglv64.dll` / `ig9icd64.dll`），不在 System32
- 系统 `opengl32.dll` 只是转发层，其版本号与 GL 版本无关
- 拿不到 `GL_VERSION` 而报 Microsoft GDI Generic 1.1 → 说明**没装上真正的显卡驱动**

### 2.4 OpenCV

```powershell
python -c "import cv2;print(cv2.__version__, cv2.__file__)" 2>&1
python -c "import cv2;print(cv2.getBuildInformation())" 2>&1 | Select-String 'GUI:|OpenCL|CUDA|cuDNN|FFMPEG|Parallel|IPP'
python -c "import cv2;print(cv2.cuda.getCudaEnabledDeviceCount())" 2>&1
python -m pip list --disable-pip-version-check 2>&1 | Select-String opencv
```

四个高频坑：

| 坑 | 判定 | 影响 |
|---|---|---|
| **headless 版** | build info `GUI: NONE`；`imshow` 抛 `not implemented` | 不能弹窗看图，只能 `imwrite` |
| **多发行版共存** | `pip list` 里同时有 `opencv-python`/`opencv-contrib-python`/`-headless` | `cv2` 目录被覆盖，任何 `pip install` 都可能弄坏 import |
| **无 CUDA 构建** | `cv2.cuda.getCudaEnabledDeviceCount()==0` | 想要 GPU 只能自行编译或改用 PyTorch/ORT |
| **OpenCL 反而更慢** | 每轮 `numpy→UMat` 传输 | 必须全程 UMat 链式才有收益 |

### 2.5 本地推理（如存在）

```powershell
ollama --version 2>&1 ; ollama list 2>&1
Get-NetTCPConnection -LocalPort 11434 -State Listen -EA 0
Get-ChildItem '<模型目录>' -Filter '*.gguf' -Recurse -Depth 2 -EA 0
```

要点：确认 **后端**（CPU / CUDA / Vulkan / Metal），以及 `-ngl` 实际走哪个后端；
有 CUDA 卡但程序用 CPU 后端是常见"装了没用上"的情况。

---

## 三、报告时必须给出的"可执行结论"

不要只列版本，要给：

1. **这东西怎么调**（是否需全路径 / 是否需先激活环境 / 是否需要特定 shell）
2. **它能不能真的跑**（实测样例结果）
3. **有什么限制**（headless、无 CUDA、无 LaTeX、只支持 32 位……）
4. **替代方案**（没有 A 时用什么完成同一任务）