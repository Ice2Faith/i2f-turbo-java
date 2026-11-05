"""
OpenCV / OpenCL 自检 —— 用法：python env-tools\opencv-check.py

本机实测结论（2026-11-05）：
  cv2 5.0.0 (opencv-python-headless) / GUI: NONE / CUDA 0 devices / OpenCL yes(NVD3D11)
输出纯 ASCII。
"""
import sys
import time

import numpy as np

print("=" * 62)
print("OpenCV self-check")
print("=" * 62)

try:
    import cv2
except ImportError:
    print("[FAIL] cv2 not installed")
    sys.exit(2)

print("cv2.__version__ : %s" % cv2.__version__)
print("cv2.__file__    : %s" % cv2.__file__)

# --- CUDA ---
try:
    n = cv2.cuda.getCudaEnabledDeviceCount()
    print("cv2.cuda devices: %d%s" % (n, "  (no CUDA build)" if n == 0 else ""))
except Exception as e:
    print("cv2.cuda        : not built (%s)" % type(e).__name__)

# --- OpenCL ---
ocl = False
try:
    ocl = cv2.ocl.haveOpenCL()
    print("cv2.ocl OpenCL  : %s" % ocl)
    if ocl:
        cv2.ocl.setUseOpenCL(True)
        print("cv2.ocl useOpenCL: %s" % cv2.ocl.useOpenCL())
        dev = cv2.ocl.Device_getDefault()
        print("cv2.ocl device  : %s" % dev.name())
except Exception as e:
    print("cv2.ocl         : error %s" % e)

# --- GUI / headless ---
try:
    cv2.imshow("t", np.zeros((8, 8, 3), np.uint8))
    cv2.waitKey(1)
    cv2.destroyAllWindows()
    print("highgui imshow  : AVAILABLE")
except Exception:
    print("highgui imshow  : NOT AVAILABLE (headless) -> use cv2.imwrite")

# --- contrib 模块 ---
mods = ["SIFT_create", "xfeatures2d", "aruco", "dnn_superres", "face", "dnn"]
print("contrib/modules : " + " ".join(
    "%s=%s" % (m, hasattr(cv2, m)) for m in mods))

# --- build info 要点 ---
print("-" * 62)
for line in cv2.getBuildInformation().splitlines():
    s = line.strip()
    if any(k in s for k in ("GUI:", "NVIDIA CUDA", "cuDNN", "OpenCL:", "FFMPEG",
                            "GStreamer", "Parallel framework", "Eigen", "Intel IPP")):
        print("  " + s)

# --- 性能对照：OpenCV OpenCL 常因传输反而更慢 ---
print("-" * 62)
a = np.random.rand(4000, 4000).astype(np.float32)
cv2.GaussianBlur(a, (11, 11), 0)
t = time.time()
for _ in range(5):
    cv2.GaussianBlur(a, (11, 11), 0)
cpu = (time.time() - t) / 5 * 1000
print("CPU  GaussianBlur 4000x4000 : %.1f ms/iter" % cpu)

if ocl:
    try:
        u = cv2.UMat(a)
        cv2.GaussianBlur(u, (11, 11), 0)
        t = time.time()
        for _ in range(5):
            cv2.GaussianBlur(u, (11, 11), 0)
        _ = cv2.GaussianBlur(u, (11, 11), 0).get()
        gpu = (time.time() - t) / 5 * 1000
        print("OCL  GaussianBlur 4000x4000 : %.1f ms/iter (numpy->GPU each call)"
              % gpu)
        if gpu > cpu:
            print("  [note] OpenCL slower here: transfer-bound. Chain UMat ops.")
    except Exception as e:
        print("OCL  GaussianBlur : failed %s" % e)

print("=" * 62)
print("[OK] OpenCV usable (headless, no CUDA, OpenCL optional)")