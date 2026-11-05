# 图形与 GPU 计算栈（CUDA / Vulkan / OpenGL / OpenCV）

## 0. 两张 GPU，但只有一个 CUDA 设备

| 卡 | PCI 地址 | 分类 | 显存 | 驱动 | 计算 |
|---|---|---|---|---|---|
| Intel HD Graphics 610 | `00:02.0` (`VEN_8086&DEV_5902`) | 核显 | 1GB 共享 | 21.20.16.4550（2016，极旧） | ❌ 非 CUDA |
| **NVIDIA GeForce GTX 1050** | `01:00.0` (`VEN_10DE&DEV_1C8D`) | 独显 | **4096 MiB** | **581.57** | ✅ **CUDA 索引 0（唯一）**，sm_61 |

**索引对照（易混淆）**

| 视角 | 看到 | 说明 |
|---|---|---|
| PCI 总线 | Intel=00:02.0，NVIDIA=**01**:00.0 | NVIDIA 在"1 号"总线 |
| CUDA / NVML / nvidia-smi | **只有索引 0** | 只枚举 NVIDIA，从 0 顺排 |
| Vulkan | 只有 `Vulkan0` | Intel 2016 驱动无 Vulkan ICD |
| OpenCL | 仅 Intel CPU 运行时 | 核显无 `igdrcl64.dll` |

> ⚠️ `CUDA_VISIBLE_DEVICES=1` 的由来就是混淆了 PCI 总线号。本机合法值只有 **`0`**。

## 1. CUDA ✅

| 项 | 值 |
|---|---|
| Toolkit | **12.4**（nvcc V12.4.99），`C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4` |
| cuDNN | ✅ `include\cudnn.h` |
| 驱动 | 581.57（支持 CUDA 13.0 运行时，向下兼容 12.4） |
| Samples | `extras\demo_suite\deviceQuery.exe` / `bandwidthTest.exe` |
| Nsight | Compute 2024.1、Systems 2023.4.4 |

**设备规格**：sm_61 / **5 SM / 640 CUDA Cores** / 4096 MiB / 无 Tensor Core / 无 bf16。

### `CUDA_VISIBLE_DEVICES` 取值语义（实测 `torch.cuda.device_count()`）

| 取值 | 结果 | 判定 |
|---|---|---|
| 未设置 / 空 | **1** | ✅ 推荐 |
| `0` | **1** | ✅ 推荐（当前值） |
| `0,1` | 1 | ✅ 无害（越界静默忽略） |
| `GPU-529c9572-7aed-a6c0-b583-e1ae0f46715e` | 1 | ✅ UUID 绑定最稳 |
| `1` | **0** | ❌ 本机唯一错误配置 |
| `-1` | 0 | ⚠️ 合法，语义是"禁用 CUDA" |
| `GPU-0` | 0 | ❌ 语法错（须完整 UUID） |

### 性能（GTX 1050，4096² 矩阵乘 FP32）

| 状态 | SM 时钟 | 算力 |
|---|---|---|
| 冷启动首测（P8） | 139 MHz | ~490 GFLOPS |
| **预热后（P0）** | **1354 MHz** | **1431–1466 GFLOPS**（≈理论值 85%） |

档位上限 1911 MHz。**做性能对比必须先热身 3 轮以上**，否则结论会被冷启动带偏一半。

### nvcc 的坑

CUDA 12.4 只支持 VS2017–2019，本机只有 VS2013：

```
nvcc fatal : nvcc cannot find a supported version of Microsoft Visual Studio.
Only the versions between 2017 and 2019 (inclusive) are supported!
```

✅ 加 `-allow-unsupported-compiler` **实测可编译可运行**（会刷 `warning #541-D`）：

```powershell
cmd /c "call ""D:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"" amd64 && nvcc -allow-unsupported-compiler -arch=sm_61 -o k.exe k.cu && k.exe"
```

→ 输出 `kernel OK: c[0]=2.0  device=NVIDIA GeForce GTX 1050  sm_61`

**编译目标**：`-arch=sm_61`（或 `-gencode arch=compute_61,code=sm_61`）。**不要**用 sm_50/52/75。
生产用途建议补装 **VS2022 Build Tools**（仅 C++ 生成工具）。

### PyTorch

`torch 2.6.0+cu124`（+torchvision 0.21.0 / torchaudio 2.6.0），与 toolkit 12.4 匹配，cu124 wheel 覆盖 sm_50–90。

```python
torch.cuda.is_available()      # 期望 True
torch.cuda.device_count()      # 期望 1
```

显存仅 4GB：训练基本不可行，只能推理/小模型微调；7B FP16 需 ~14GB，必须量化。

## 2. Vulkan ✅ 运行可用

| 项 | 实测值 |
|---|---|
| Loader | `C:\Windows\System32\vulkan-1.dll` **1.4.309.0** |
| Instance | **1.4.309** |
| device apiVersion | **1.4.312** |
| driverVersion / driverID | `581.57.0.0` / `DRIVER_ID_NVIDIA_PROPRIETARY` |
| deviceType | `DISCRETE_GPU` |
| vendorID / deviceID | `0x10DE` / `0x1C8D` |
| **conformanceVersion** | **1.4.1.3**（正式符合性认证） |
| memoryHeaps | 3（2 个 DEVICE_LOCAL） |
| maxImageDimension2D | 32768 |
| 关键扩展 | `VK_KHR_swapchain`(rev70)、`VK_KHR_synchronization2`、`VK_EXT_descriptor_indexing`(rev2) |

* **Intel HD610 不进列表**（2016 驱动无 Vulkan ICD）→ 全机只有 `Vulkan0`。
* ❌ **未安装 Vulkan SDK**：无 `VULKAN_SDK`、无 `glslangValidator`/`spirv-val`。
  → 能**运行**不能**开发**。需要时装 LunarG SDK 到 `E:\VulkanSDK`。
* 探测：`vulkaninfo --summary`（已在 System32）。
* 消费方：`D:\llama.cpp\llama\llama-server` 走 Vulkan0。

## 3. OpenGL ✅ 4.6，默认走独显

现场编译探测（`scripts\opengl-probe.cs`）真建 GL 上下文后 `glGetString` 实测：

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

* **反直觉但有利**：桌面显示由 Intel 核显驱动，但 **OpenGL 上下文默认落到 NVIDIA**。GLSL 可放心 `#version 460`。
* 双 ICD 并存（均在 DriverStore）：NVIDIA `nvoglv64.dll`（`nvcvsi.inf`）、Intel `ig9icd64.dll`（`igdlh64.inf`）。
* 系统 `opengl32.dll` = 10.0.19041.5794（仅转发）。
* 无 `glview`/`wglinfo`/`glewinfo`，需要时编译 `scripts\opengl-probe.cs`。

## 4. OpenCV ⚠️ headless + 双发行版冲突

| 项 | 实测值 |
|---|---|
| 生效版本 | **cv2 5.0.0**，`C:\Python310\lib\site-packages\cv2` |
| 来源 | `opencv-python-headless 5.0.0.93` |
| **冲突残留** | `opencv-contrib-python 4.10.0.84` 同时装着 ⚠️ |
| **GUI** | ❌ **`cv2.imshow` 抛错**（`GUI: NONE`） |
| CUDA | ❌ `cv2.cuda.getCudaEnabledDeviceCount() = 0`（未编译 CUDA） |
| **OpenCL** | ✅ `YES (NVD3D11)`，设备 = GTX 1050 |
| contrib 模块 | ✅ `xfeatures2d`、`aruco`、`dnn_superres`、`face`、`SIFT_create` |
| Video I/O | FFMPEG YES；GStreamer NO |
| 其他 | Parallel=Concurrency；IPP 2026.0.0；Eigen NO |
| C++ SDK | ❌ 无；`opencv_world*.dll` 不在 System32 |

### 三个坑

1. **不能 `cv2.imshow`** → 用 `cv2.imwrite` 落盘，或 matplotlib，或换非 headless 包。
2. **两套发行版打架** → 任何 `pip install opencv-*` 都可能让 import 崩。清理：
   ```powershell
   python -m pip uninstall -y opencv-contrib-python opencv-python-headless
   python -m pip install "opencv-contrib-python==5.0.0.93"   # 要 GUI+contrib
   # 或 opencv-python-headless==5.0.0.93 保持无 GUI
   ```
3. **OpenCL 不一定更快** —— 4000×4000 高斯模糊：

   | 路径 | 耗时 |
   |---|---|
   | OpenCL（每轮 numpy→GPU 传输） | **105–118 ms/iter** |
   | CPU | **50–74 ms/iter** |

   每次搬 numpy 上 GPU 反而慢一倍。要用必须**全程 UMat 链式**，只在最后 `.get()` 一次。

**结论**：OpenCV 适合无界面批处理/服务端推理；GPU 加速请走 **PyTorch** 或 **ONNX Runtime**。

## 5. llama.cpp

`D:\llama.cpp\llama`（17.19GB 含 GGUF）。后端只有 **CPU + Vulkan**（`ggml-vulkan.dll`），**无 CUDA 后端**。

```
Vulkan0: NVIDIA GeForce GTX 1050 (4221 MiB, 3619 MiB free)
```

`startup*.bat` 用 `llama-server -m xxx.gguf -ngl 999 -c 4096 --flash-attn on --port 9090`。
`-ngl` 实际走 Vulkan。要 CUDA 加速需重编（约 20–40 分钟）。