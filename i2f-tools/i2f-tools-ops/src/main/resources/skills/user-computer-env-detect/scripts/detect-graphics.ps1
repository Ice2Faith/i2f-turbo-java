# S4 graphics/compute probe: CUDA / Vulkan / OpenGL / OpenCV / local inference
# ASCII-only output and source. Build artifacts go to %TEMP% only.
#   powershell -ExecutionPolicy Bypass -File detect-graphics.ps1

$ErrorActionPreference = 'SilentlyContinue'
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false } catch {}
function L($k, $v) { "{0,-24}: {1}" -f $k, $v }
function H($t) { ""; "=" * 62; "  $t"; "=" * 62 }
function Try-Cmd($n) { (Get-Command $n -ErrorAction SilentlyContinue).Source }

# Timed probe: some GPU tools can block (driver init, license prompts, spinning server).
function Probe {
    param([string]$Exe, [string[]]$ArgList, [int]$TimeoutSec = 25, [int]$MaxLines = 40)
    if (-not $Exe) { return @('<not found>') }
    if (-not $ArgList -or $ArgList.Count -eq 0) { return @('<skipped: no probe args>') }
    try {
        $psi = New-Object System.Diagnostics.ProcessStartInfo
        $psi.FileName = $Exe
        $psi.Arguments = ($ArgList | ForEach-Object { if ($_ -match '\s') { '"' + $_ + '"' } else { $_ } }) -join ' '
        $psi.RedirectStandardInput = $true
        $psi.RedirectStandardOutput = $true
        $psi.RedirectStandardError = $true
        $psi.UseShellExecute = $false
        $psi.CreateNoWindow = $true
        $psi.StandardOutputEncoding = [System.Text.Encoding]::Default
        $psi.StandardErrorEncoding = [System.Text.Encoding]::Default
        $p = [System.Diagnostics.Process]::Start($psi)
        $p.StandardInput.Close()
        if (-not $p.WaitForExit($TimeoutSec * 1000)) {
            try { $p.Kill() } catch {}
            return @("<timeout ${TimeoutSec}s>")
        }
        $out = $p.StandardOutput.ReadToEnd() + $p.StandardError.ReadToEnd()
        return @($out -split "`r?`n" | Where-Object { $_.Trim() } | Select-Object -First $MaxLines)
    } catch { return @("<error> $($_.Exception.Message)") }
}

H "1. NVIDIA / CUDA DEVICES"
$smi = Try-Cmd 'nvidia-smi'
L "nvidia-smi" $smi
if ($smi) {
    (Probe "nvidia-smi" @("-L") 25 10) | ForEach-Object { "  $_" }
    "  device table:"
    (Probe "nvidia-smi" @("--query-gpu=index,name,uuid,pci.bus_id,driver_version,memory.total,compute_cap,pstate","--format=csv") 25 10) |
        ForEach-Object { "    $_" }
} else {
    "  no nvidia-smi -> no NVIDIA driver, or driver installed without tools"
}

H "2. CUDA TOOLKIT"
L "CUDA_PATH" $env:CUDA_PATH
foreach ($base in 'C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA',
                  'D:\Program Files\NVIDIA GPU Computing Toolkit\CUDA',
                  'C:\CUDA', 'D:\CUDA') {
    if (Test-Path $base) {
        Get-ChildItem $base -Directory -EA 0 | ForEach-Object { "  toolkit: $($_.FullName)" }
    }
}
$nvcc = Try-Cmd 'nvcc'
L "nvcc" $nvcc
if ($nvcc) { (& nvcc --version 2>&1 | Select-Object -Last 2) | ForEach-Object { "    $_" } }
$cudnn = @()
foreach ($p in "$env:CUDA_PATH\include\cudnn.h", "$env:CUDA_PATH\include\cudnn_v8.h") {
    if (Test-Path $p) { $cudnn += $p }
}
L "cuDNN header" ($(if ($cudnn) { $cudnn -join '; ' } else { 'not found' }))

H "3. CUDA VISIBILITY (a very common trap)"
$cvd = $env:CUDA_VISIBLE_DEVICES
L "CUDA_VISIBLE_DEVICES(proc)" $(if ($cvd -ne $null) { "'$cvd'" } else { '<unset>' })
$reg = (Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\Session Manager\Environment' -EA 0).CUDA_VISIBLE_DEVICES
L "  machine(registry)" $(if ($reg -ne $null) { "'$reg'" } else { '<unset>' })
$usr = (Get-ItemProperty 'HKCU:\Environment' -EA 0).CUDA_VISIBLE_DEVICES
L "  user(registry)" $(if ($usr -ne $null) { "'$usr'" } else { '<unset>' })
if ($cvd -eq '-1') { "  -> '-1' means CUDA DISABLED on purpose (CPU-only mode)" }
if ($cvd -match '^\d+$') { "  -> numeric value points at a device INDEX, not a PCI bus number" }

H "4. deviceQuery (authoritative)"
$dq = @(
    "$env:CUDA_PATH\extras\demo_suite\deviceQuery.exe",
    'C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4\extras\demo_suite\deviceQuery.exe'
) | Where-Object { $_ -and (Test-Path $_) } | Select-Object -First 1
if ($dq) {
    (& $dq 2>&1) | Select-String 'Detected|Device \d:|CUDA Capability|Result =' |
        ForEach-Object { "  " + $_.Line.Trim() }
} else { "  deviceQuery.exe not found (samples may not be installed)" }

H "5. VULKAN"
$vk = Try-Cmd 'vulkaninfo'
L "vulkaninfo" $vk
L "VULKAN_SDK" $env:VULKAN_SDK
L "vulkan-1.dll" (Test-Path 'C:\Windows\System32\vulkan-1.dll')
if ($vk) {
    (Probe "vulkaninfo" @("--summary") 40 60) |
        Select-String 'Instance Version|apiVersion|driverVersion|driverInfo|deviceName|deviceType|vendorID|deviceID|conformanceVersion' |
        ForEach-Object { "  " + $_.Line.Trim() }
}
foreach ($t in 'glslangValidator','spirv-val','spirv-cross') {
    L "  $t" (Try-Cmd $t)
}
if (-not $env:VULKAN_SDK) { "  -> no SDK: runtime OK, shader compilation NOT possible" }

H "6. OPENGL (live context probe - do not infer from drivers)"
$csc = 'C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe'
$probeSrc = Join-Path $PSScriptRoot 'opengl-probe.cs'
$probeExe = Join-Path $env:TEMP 'opengl-probe.exe'
if ((Test-Path $csc) -and (Test-Path $probeSrc)) {
    if (-not (Test-Path $probeExe)) {
        & $csc /nologo "/out:$probeExe" $probeSrc 2>&1 | Where-Object { $_ -match 'error' } |
            ForEach-Object { "  [build] $_" }
    }
    if (Test-Path $probeExe) { (& $probeExe 2>&1) | ForEach-Object { "  $_" } }
    else { "  [probe build failed]" }
} else {
    "  [skip] csc or opengl-probe.cs missing"
    "  manual check: any GL_RENDERER mentioning 'GDI Generic' means no real GPU driver"
}

H "7. OPENCV (python)"
$py = Try-Cmd 'python'
if ($py) {
    & python -c "import cv2,sys;print('version', cv2.__version__);print('file', cv2.__file__)" 2>&1 |
        Select-Object -First 3 | ForEach-Object { "  $_" }
    & python -c "import cv2;print('cuda_devices', cv2.cuda.getCudaEnabledDeviceCount())" 2>&1 |
        Select-Object -First 2 | ForEach-Object { "  $_" }
    "  build info:"
    & python -c "import cv2;[print('   ',l.strip()) for l in cv2.getBuildInformation().splitlines() if any(k in l for k in ['GUI:','OpenCL','NVIDIA CUDA','cuDNN','FFMPEG','Parallel','IPP','Eigen'])]" 2>&1 |
        Select-Object -First 12 | ForEach-Object { "  $_" }
    "  installed opencv distributions:"
    & python -m pip list --disable-pip-version-check 2>&1 | Select-String -Pattern 'opencv' |
        ForEach-Object { "    " + $_.Line.Trim() }
} else { "  python not found, skip OpenCV" }

H "8. TORCH / INFERENCE BACKENDS"
if ($py) {
    & python -c "import torch;print('torch', torch.__version__, 'cuda_build', torch.version.cuda, 'available', torch.cuda.is_available(), 'count', torch.cuda.device_count())" 2>&1 |
        Select-Object -First 3 | ForEach-Object { "  $_" }
    & python -c "import onnxruntime as o;print('onnxruntime', o.__version__, o.get_available_providers())" 2>&1 |
        Select-Object -First 2 | ForEach-Object { "  $_" }
}
L "ollama" (Try-Cmd 'ollama')
if (Try-Cmd 'ollama') { (& ollama list 2>&1 | Select-Object -First 8) | ForEach-Object { "  $_" } }
L "port 11434 listening" ((Get-NetTCPConnection -LocalPort 11434 -State Listen -EA 0) -ne $null)
""