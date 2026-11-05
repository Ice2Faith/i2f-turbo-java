"""
CUDA self-check. Usage: python cuda-check.py

Expected: CUDA_VISIBLE_DEVICES = 0 (or unset), device_count = 1, matmul OK.
ASCII-only output so a GBK/other console code page cannot mangle it.
"""
import os
import sys
import time

print("=" * 62)
print("CUDA self-check")
print("=" * 62)

cvd = os.environ.get("CUDA_VISIBLE_DEVICES", "<unset>")
print("CUDA_VISIBLE_DEVICES : %s" % cvd)
if cvd in ("1", "-1"):
    print("  [WARN] value is %r -> CUDA devices hidden." % cvd)
    print("         This machine has exactly 1 CUDA device (index 0).")
    print("         Process holds an OLD env block: restart it.")

try:
    import torch
except ImportError:
    print("\n[FAIL] torch not installed")
    sys.exit(2)

print("torch                : %s (built for CUDA %s)"
      % (torch.__version__, torch.version.cuda))
print("cuda.is_available()  : %s" % torch.cuda.is_available())
print("cuda.device_count()  : %d" % torch.cuda.device_count())

if not torch.cuda.is_available():
    print("\n[FAIL] CUDA unavailable - no GPU enumerated")
    print("       check: 1) env value is 1 or -1?  2) process restarted?")
    sys.exit(1)

p = torch.cuda.get_device_properties(0)
print("-" * 62)
print("device       : %s" % p.name)
print("capability   : sm_%d%d" % (p.major, p.minor))
print("SMs          : %d  (%d CUDA cores)" % (p.multi_processor_count,
                                              p.multi_processor_count * 128))
print("total VRAM   : %.0f MiB" % (p.total_memory / 1048576))

d = torch.device("cuda:0")
a = torch.randn(4096, 4096, device=d)
b = torch.randn(4096, 4096, device=d)
# preheat: an idle GPU sits in a low-power state; the first run under-reports badly
for _ in range(3):
    _ = a @ b
torch.cuda.synchronize()
t = time.time()
c = a @ b
torch.cuda.synchronize()
dt = time.time() - t
print("-" * 62)
print("matmul 4096  : OK in %.3fs  (%.0f GFLOPS, preheated)"
      % (dt, (2 * 4096 ** 3) / dt / 1e9))
print("allocated    : %.1f MiB" % (torch.cuda.memory_allocated() / 1048576))
print("result device: %s" % c.device)

print("=" * 62)
print("[PASS] CUDA environment OK")