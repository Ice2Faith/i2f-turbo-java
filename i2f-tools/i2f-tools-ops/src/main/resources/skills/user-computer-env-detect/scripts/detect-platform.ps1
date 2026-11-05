# S-1 platform + architecture probe (native Windows PowerShell).
# For Linux / macOS / Termux / WSL / git-bash use detect-platform.sh instead.
#
#   powershell -ExecutionPolicy Bypass -File detect-platform.ps1
#
# ASCII-only output and source.

$ErrorActionPreference = 'Continue'
function L($k, $v) { "{0,-26}: {1}" -f $k, $v }

"=" * 62
"  S-1 PLATFORM & ARCHITECTURE (Windows host)"
"=" * 62

# ---------- OS ----------
$os = Get-CimInstance Win32_OperatingSystem
L "OS family"        "windows"
L "Caption"          $os.Caption
L "Version"          $os.Version
L "BuildNumber"      $os.BuildNumber
L "OSArchitecture"   $os.OSArchitecture
L "CSName"           $os.CSName
L "Manufacturer"     (Get-CimInstance Win32_ComputerSystem).Manufacturer
L "Model"            (Get-CimInstance Win32_ComputerSystem).Model

# Windows 11 reports 10.0.2xxxx; distinguish it from Windows 10 (1904x).
$build = [int]$os.BuildNumber
$winVer = if ($build -ge 22000) { "Windows 11 (build $build)" } else { "Windows 10 (build $build)" }
L "Effective version" $winVer

# ---------- architecture ----------
$proc = Get-CimInstance Win32_Processor | Select-Object -First 1
L "PROCESSOR_ARCHITECTURE" $env:PROCESSOR_ARCHITECTURE
L "PROCESSOR_ARCHITEW6432" $env:PROCESSOR_ARCHITEW6432
L "Processor id"           ($proc.Name.Trim())
L "AddressWidth"           "$($proc.AddressWidth) bit"
L "DataWidth"              "$($proc.DataWidth) bit"

$arch = switch ($env:PROCESSOR_ARCHITECTURE) {
    'AMD64' { 'x64' }
    'x86'   { if ($env:PROCESSOR_ARCHITEW6432 -eq 'AMD64') { 'x64 (32-bit process on x64)' } else { 'x86' } }
    'ARM64' { 'arm64' }
    'ARM'   { 'arm32' }
    default { $env:PROCESSOR_ARCHITECTURE }
}
L "architecture" $arch
if ($arch -like 'x64*')     { "  -> 64-bit: use x64/amd64 binaries (CUDA, JDK, Node all x64)" }
elseif ($arch -eq 'x86')    { "  -> 32-bit ONLY: many modern toolchains have no 32-bit build" }
elseif ($arch -like 'arm*') { "  -> ARM Windows: check per-tool ARM64 support (many x64 apps run emulated)" }

# Is the current process running under WOW64 / emulation?
L "Process is 64-bit" ([Environment]::Is64BitProcess)
L "OS is 64-bit"      ([Environment]::Is64BitOperatingSystem)

# ---------- shell / host ----------
L "PowerShell version" ("{0} {1}" -f $PSVersionTable.PSVersion, $PSVersionTable.PSEdition)
L "pwsh (PS7) present" [bool](Get-Command pwsh -ErrorAction SilentlyContinue)
L "cmd.exe"            (Get-Command cmd.exe -ErrorAction SilentlyContinue).Source
L "git-bash"           (@('C:\Program Files\Git\bin\bash.exe','D:\Program Files\Git\bin\bash.exe') |
                        Where-Object { Test-Path $_ } | Select-Object -First 1)
L "WSL present"        [bool](Get-Command wsl.exe -ErrorAction SilentlyContinue)
L "ExecutionPolicy"    ((Get-ExecutionPolicy).ToString())
L "Console code page"  ((cmd /c chcp) -replace '[^0-9]', '')

# ---------- package managers ----------
$pms = @()
foreach ($p in 'winget','choco','scoop') {
    if (Get-Command $p -ErrorAction SilentlyContinue) { $pms += $p }
}
L "package managers" ($(if ($pms) { $pms -join ', ' } else { '<none>' }))

# ---------- virtualization / container ----------
$hv = Get-CimInstance Win32_ComputerSystem
L "HypervisorPresent" $hv.HypervisorPresent
L "docker" (Get-Command docker -ErrorAction SilentlyContinue).Source

"=" * 62
"  NEXT: pick the Windows branch in references/00-platform-and-arch.md"
"        then run detect-system.ps1 / detect-toolchains.ps1 / detect-tools.ps1 / detect-graphics.ps1"
"=" * 62
""