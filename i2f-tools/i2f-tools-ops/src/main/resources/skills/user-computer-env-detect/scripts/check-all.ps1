# One-shot environment detection: runs every probe stage and optionally saves a report.
#
#   powershell -ExecutionPolicy Bypass -File check-all.ps1
#   powershell -ExecutionPolicy Bypass -File check-all.ps1 -OutDir "E:\01-ai-workspace\documents\env-detect"
#   powershell -ExecutionPolicy Bypass -File check-all.ps1 -SkipGraphics -SkipPersona   # faster
#
# Design notes (learned the hard way):
#   * Stages run as SEPARATE processes with -ExecutionPolicy Bypass, because invoking a
#     .ps1 in-process fails on machines whose policy is Restricted.
#   * Stage output is redirected to temp FILES instead of a pipeline. Piping a child
#     PowerShell through a pipeline can deadlock on inherited handles, especially when
#     this script is itself hosted inside another script or job.
#   * Every stage has a HARD timeout; a stuck stage is killed and reported, never
#     allowed to hang the whole run.
#
# ASCII-only output and source.
param(
    [string]$OutDir,
    [int]$StageTimeoutSec = 150,
    [switch]$SkipGraphics,
    [switch]$SkipPersona
)

$ErrorActionPreference = 'Continue'
$root = $PSScriptRoot
$stamp = Get-Date -Format 'yyyyMMdd-HHmmss'
$sb = New-Object System.Text.StringBuilder
$work = Join-Path $env:TEMP ("env-detect-" + $stamp)
New-Item -ItemType Directory -Path $work -Force | Out-Null

function Section($title) {
    $line = "=" * 66
    Write-Host ""
    Write-Host $line -ForegroundColor Cyan
    Write-Host "  $title" -ForegroundColor Cyan
    Write-Host $line -ForegroundColor Cyan
    [void]$sb.AppendLine($line)
    [void]$sb.AppendLine("  $title")
    [void]$sb.AppendLine($line)
}

function Emit($lines) {
    foreach ($l in $lines) {
        Write-Host $l
        [void]$sb.AppendLine([string]$l)
    }
}

# Run one stage file in its own process, redirect output to files, enforce a hard timeout.
function Invoke-Stage {
    param([string]$File, [string]$Name)
    $path = Join-Path $root $File
    if (-not (Test-Path $path)) { Emit @("  [SKIP] missing: $File"); return }
    $outFile = Join-Path $work "$Name.out.txt"
    $errFile = Join-Path $work "$Name.err.txt"
    $argList = @('-ExecutionPolicy', 'Bypass', '-NoProfile', '-NonInteractive', '-File', $path)
    $p = $null
    try {
        $p = Start-Process -FilePath 'powershell.exe' -ArgumentList $argList -NoNewWindow -PassThru `
                           -RedirectStandardOutput $outFile -RedirectStandardError $errFile
    } catch {
        Emit @("  [ERROR] cannot start stage ${Name}: $($_.Exception.Message)")
        return
    }
    if (-not $p.WaitForExit($StageTimeoutSec * 1000)) {
        try { $p.Kill() } catch {}
        Emit @("  [TIMEOUT] $Name exceeded ${StageTimeoutSec}s - killed")
    }
    foreach ($f in @($outFile, $errFile)) {
        if (Test-Path $f) {
            # Detectors force UTF-8 on their stdout. Decode UTF-8 first (strict), then
            # fall back to the ANSI code page for legacy/older stage scripts.
            $text = $null
            try {
                $strict = New-Object System.Text.UTF8Encoding($false, $true)
                $text = [System.IO.File]::ReadAllText($f, $strict)
            } catch {
                $text = [System.IO.File]::ReadAllText($f, [System.Text.Encoding]::Default)
            }
            if ($text -and $text.Trim()) { Emit ($text -split "`r?`n" | Where-Object { $_.TrimEnd() }) }
        }
    }
}

Section "CONTEXT"
Emit @(
    "timestamp : $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')",
    "host      : $env:COMPUTERNAME",
    "user      : $env:USERNAME",
    "powershell: $($PSVersionTable.PSVersion) $($PSVersionTable.PSEdition)",
    "policy    : $((Get-ExecutionPolicy).ToString())",
    "script dir: $root",
    "temp dir  : $work"
)

$stages = @(
    @{ Name = 'platform';   Title = '0. PLATFORM & ARCHITECTURE'; File = 'detect-platform.ps1' },
    @{ Name = 'system';     Title = '1. SYSTEM BASELINE';    File = 'detect-system.ps1'     },
    @{ Name = 'toolchains'; Title = '2. TOOLCHAINS';         File = 'detect-toolchains.ps1' },
    @{ Name = 'tools';      Title = '3. COMMON TOOLS';       File = 'detect-tools.ps1'      }
)
if (-not $SkipGraphics) { $stages += @{ Name = 'graphics'; Title = '4. GRAPHICS / COMPUTE'; File = 'detect-graphics.ps1' } }
if (-not $SkipPersona)  { $stages += @{ Name = 'persona';  Title = '5. USER PERSONA';       File = 'detect-persona.ps1'  } }

foreach ($s in $stages) {
    Section $s.Title
    Invoke-Stage -File $s.File -Name $s.Name
}

Section "SUMMARY"
$os  = Get-CimInstance Win32_OperatingSystem
$cim = Get-CimInstance Win32_ComputerSystem
$cpu = Get-CimInstance Win32_Processor | Select-Object -First 1
$nls = Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\Nls\CodePage' -EA 0
$gpu = (Get-CimInstance Win32_VideoController | ForEach-Object { $_.Name }) -join ' + '
$disks = Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 } |
         ForEach-Object { "$($_.DeviceID) $([math]::Round($_.FreeSpace/1GB,1))G free" }
$low = Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 -and $_.FreeSpace/1GB -lt 10 } |
       ForEach-Object { $_.DeviceID }
$ok  = (Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 -and $_.FreeSpace/1GB -gt 10 } |
       ForEach-Object { $_.DeviceID }) -join ','

Emit @(
    "OS      : $($os.Caption) $($os.Version) $($os.OSArchitecture)",
    "Machine : $($cim.Manufacturer) $($cim.Model)",
    "CPU     : $($cpu.Name) ($($cpu.NumberOfCores)C/$($cpu.NumberOfLogicalProcessors)T)",
    "RAM     : $([math]::Round($cim.TotalPhysicalMemory/1GB,1)) GB",
    "GPU     : $gpu",
    "Disks   : $($disks -join ' | ')",
    "LowDisk : $(if ($low) { "$low  << DO NOT WRITE" } else { 'none' })",
    "Charset : ACP=$($nls.ACP) OEMCP=$($nls.OEMCP) -> set explicit encoding everywhere",
    "Writable: $(if ($ok) { $ok } else { 'NONE - free space critical' })",
    "",
    "Next steps:",
    "  1) read references/01..06 for detail on anything surprising",
    "  2) keep probe artifacts under <writable drive>:\01-ai-workspace\temp",
    "  3) ask the user whether to sediment results into a machine-specific skill",
    "     (see references/05-report.md for the template and install steps)"
)

if ($OutDir) {
    if (-not (Test-Path $OutDir)) { New-Item -ItemType Directory -Path $OutDir -Force | Out-Null }
    $file = Join-Path $OutDir "env-detect-$stamp.txt"
    $utf8 = New-Object System.Text.UTF8Encoding($false)
    [System.IO.File]::WriteAllText($file, $sb.ToString(), $utf8)
    Write-Host ""
    Write-Host "report written: $file" -ForegroundColor Green
}

Remove-Item $work -Recurse -Force -ErrorAction SilentlyContinue
""