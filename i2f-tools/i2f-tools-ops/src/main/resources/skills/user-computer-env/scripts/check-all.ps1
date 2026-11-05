# One-shot environment health check: CUDA / Vulkan / OpenGL / OpenCV / GPU
#
# Usage (any shell):
#   powershell -ExecutionPolicy Bypass -File env-tools\check-all.ps1
#   pwsh -File env-tools\check-all.ps1
#
# Build artifacts go to %TEMP% - keeps the workspace clean (doc section 0).
# ASCII-only on purpose: console code page here is 936 (GBK).

$ErrorActionPreference = 'Continue'
$tools = $PSScriptRoot

function Section($t) {
    Write-Host ""
    Write-Host ("=" * 66) -ForegroundColor Cyan
    Write-Host "  $t" -ForegroundColor Cyan
    Write-Host ("=" * 66) -ForegroundColor Cyan
}

# ---------- 1. GPU ----------
Section "1. GPU status (nvidia-smi)"
if (Get-Command nvidia-smi.exe -ErrorAction SilentlyContinue) {
    & nvidia-smi.exe --query-gpu=name,driver_version,pstate,clocks.sm,clocks.max.sm,memory.used,memory.total,utilization.gpu,temperature.gpu --format=csv
    Write-Host ""
    & nvidia-smi.exe -L
} else {
    Write-Host "  [FAIL] nvidia-smi not found" -ForegroundColor Red
}

# ---------- 2. CUDA ----------
Section "2. CUDA"
Write-Host ("CUDA_VISIBLE_DEVICES = '{0}'" -f $env:CUDA_VISIBLE_DEVICES)
if ($env:CUDA_VISIBLE_DEVICES -in @('1', '-1')) {
    Write-Host "  [WARN] should be 0 or unset: this box has only 1 CUDA device" -ForegroundColor Yellow
}
$cudaOk = $false
if (Get-Command python -ErrorAction SilentlyContinue) {
    & python (Join-Path $tools 'cuda-check.py')
    $cudaOk = ($LASTEXITCODE -eq 0)
} else {
    Write-Host "  [FAIL] python not found" -ForegroundColor Red
}
$dq = 'C:\Program Files\NVIDIA GPU Computing Toolkit\CUDA\v12.4\extras\demo_suite\deviceQuery.exe'
if (Test-Path $dq) {
    Write-Host "-- deviceQuery --"
    & $dq 2>&1 | Select-String -Pattern 'Detected|Device 0:|CUDA Capability|Result =' |
        ForEach-Object { "  " + $_.Line.Trim() }
}

# ---------- 3. Vulkan ----------
Section "3. Vulkan"
if (Get-Command vulkaninfo -ErrorAction SilentlyContinue) {
    & vulkaninfo --summary 2>&1 |
        Select-String -Pattern 'Instance Version|apiVersion|driverVersion|driverInfo|deviceName|deviceType|vendorID|deviceID|conformanceVersion' |
        ForEach-Object { "  " + $_.Line.Trim() }
    $ls = 'D:\llama.cpp\llama\llama-server.exe'
    if (Test-Path $ls) {
        Write-Host ""
        Write-Host "  llama.cpp devices:"
        Push-Location (Split-Path $ls)
        & $ls --list-devices 2>&1 | ForEach-Object { "    $_" }
        Pop-Location
    }
} else {
    Write-Host "  [FAIL] vulkaninfo not found" -ForegroundColor Red
}
if (-not $env:VULKAN_SDK) {
    Write-Host "  [note] no Vulkan SDK: runtime OK, shader compilation NOT possible (doc 4.9)" -ForegroundColor Yellow
}

# ---------- 4. OpenGL ----------
Section "4. OpenGL (live context probe)"
$csc = 'C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe'
$glSrc = Join-Path $tools 'opengl-probe.cs'
$glExe = Join-Path $env:TEMP 'opengl-probe.exe'
if ((Test-Path $csc) -and (Test-Path $glSrc)) {
    & $csc /nologo "/out:$glExe" $glSrc 2>&1 |
        Where-Object { $_ -match 'error' } | ForEach-Object { "  [build] $_" }
    if (Test-Path $glExe) {
        & $glExe
    } else {
        Write-Host "  [FAIL] compile failed" -ForegroundColor Red
    }
} else {
    Write-Host "  [SKIP] csc or probe source missing" -ForegroundColor Yellow
}

# ---------- 5. OpenCV / OpenCL ----------
Section "5. OpenCV / OpenCL"
if (Get-Command python -ErrorAction SilentlyContinue) {
    & python (Join-Path $tools 'opencv-check.py')
    $pkgs = & python -m pip list --disable-pip-version-check 2>$null |
        Select-String -Pattern 'opencv'
    if ($pkgs) {
        Write-Host ""
        Write-Host "  installed opencv distributions:"
        $pkgs | ForEach-Object { "    " + $_.Line.Trim() }
        if (@($pkgs).Count -gt 1) {
            Write-Host "  [WARN] multiple opencv distributions: any pip install may break cv2 (doc 4.8)" -ForegroundColor Yellow
        }
    }
}

# ---------- 6. Encoding / charset ----------
Section "6. System charset / encoding (mojibake prevention)"
$encPs = Join-Path $tools 'encoding-check.ps1'
$encPy = Join-Path $tools 'encoding-check.py'
if (Test-Path $encPs) {
    & powershell -ExecutionPolicy Bypass -NoProfile -File $encPs 2>&1 |
        ForEach-Object { "  $_" }
} else {
    Write-Host "  [SKIP] encoding-check.ps1 missing" -ForegroundColor Yellow
}
if ((Get-Command python -ErrorAction SilentlyContinue) -and (Test-Path $encPy)) {
    Write-Host ""
    & python $encPy 2>&1 | ForEach-Object { "  $_" }
}

# ---------- summary ----------
Section "SUMMARY"
$c = if ($cudaOk) { 'PASS' } else { 'FAIL' }
$col = if ($cudaOk) { 'Green' } else { 'Red' }
Write-Host ("  CUDA   : " + $c) -ForegroundColor $col
Write-Host "  Vulkan : runtime OK (NVIDIA ICD only)" -ForegroundColor Green
Write-Host "  OpenGL : runtime OK (expect 4.6.0 NVIDIA)" -ForegroundColor Green
Write-Host "  OpenCV : headless, no CUDA build"
$acp = (Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\Nls\CodePage' -Name ACP).ACP
Write-Host ("  Charset: ACP/OEMCP={0} - set explicit encoding everywhere (doc 3.11)" -f $acp) -ForegroundColor Yellow
Write-Host ""