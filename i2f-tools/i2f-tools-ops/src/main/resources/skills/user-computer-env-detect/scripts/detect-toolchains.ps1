# S2 toolchain probe: Java / Python / Go / Node / .NET / MSVC / MinGW / SDK-WDK
# Three-layer strategy: command -> env var -> disk scan. Never aborts on a missing tool.
#
# Hardening lessons baked in:
#   * Never invoke a tool with ZERO arguments: python/java/node without args open a REPL
#     and block forever. Every probe passes an explicit argument list.
#   * Always close the child's stdin and enforce a timeout, so a misbehaving tool
#     cannot hang the whole detection run.
#
# ASCII-only output and source.
#   powershell -ExecutionPolicy Bypass -File detect-toolchains.ps1

$ErrorActionPreference = 'SilentlyContinue'
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false } catch {}
function L($k, $v) { "{0,-22}: {1}" -f $k, $v }
function H($t) { ""; "=" * 62; "  $t"; "=" * 62 }
function Try-Cmd($n) { (Get-Command $n -ErrorAction SilentlyContinue).Source }

# Safe probe: explicit args required, stdin closed, hard timeout.
function Probe {
    param(
        [string]$Exe,
        [string[]]$ArgList,
        [int]$TimeoutSec = 15,
        [int]$MaxLines = 3
    )
    if (-not $Exe) { return '<not found>' }
    if (-not $ArgList -or $ArgList.Count -eq 0) { return '<skipped: no probe args>' }
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
        $p.StandardInput.Close()                    # <- prevents REPL hangs
        if (-not $p.WaitForExit($TimeoutSec * 1000)) {
            try { $p.Kill() } catch {}
            return "<timeout ${TimeoutSec}s>"
        }
        $out = $p.StandardOutput.ReadToEnd() + $p.StandardError.ReadToEnd()
        $lines = $out -split "`r?`n" | Where-Object { $_.Trim() } | Select-Object -First $MaxLines
        if (-not $lines) { return '<empty output>' }
        return ($lines -join ' | ')
    } catch {
        return "<error> $($_.Exception.Message)"
    }
}

H "1. JAVA"
$java = Try-Cmd 'java'
L "java on PATH" $java
L "  version" (Probe $java @('-version'))
L "javac on PATH" (Try-Cmd 'javac')
L "JAVA_HOME" $env:JAVA_HOME
$jdkDirs = @()
foreach ($r in 'C:\Java','D:\Java','E:\Java','C:\Program Files\Java','C:\Program Files (x86)\Java',
               'D:\Program Files\Java','E:\Program Files\Java') {
    if (Test-Path $r) { $jdkDirs += (Get-ChildItem $r -Directory -EA 0 | ForEach-Object { $_.FullName }) }
}
if ($jdkDirs) { "  installed JDKs:"; $jdkDirs | ForEach-Object { "    $_" } }

$mvn = Try-Cmd 'mvn'
L "mvn" $mvn
L "  version" (Probe $mvn @('-v') 40 2)
L "MAVEN_HOME" $env:MAVEN_HOME
$gr = Try-Cmd 'gradle'
L "gradle" $gr
L "  version" (Probe $gr @('-v') 60 2)
L "GRADLE_USER_HOME" $env:GRADLE_USER_HOME
foreach ($p in "$env:USERPROFILE\.m2\settings.xml", "$env:USERPROFILE\.m2\repository") {
    if (Test-Path $p) {
        $item = Get-Item $p -Force
        L "  $p" ("exists LinkType=$($item.LinkType) Target=$($item.Target)")
    }
}

H "2. PYTHON"
$pyExe = Try-Cmd 'python'
L "python" $pyExe
L "  version" (Probe $pyExe @('-V'))
L "pip" (Try-Cmd 'pip')
L "  pip version" (Probe (Try-Cmd 'pip') @('-V'))
$pyL = Try-Cmd 'py'
L "py launcher" $pyL
L "  py -0p" ((Probe $pyL @('-0p') 20 12) -replace '\|', '; ')
L "conda" (Try-Cmd 'conda')
L "CONDA_PREFIX" $env:CONDA_PREFIX
$pyDirs = @()
foreach ($r in 'C:\Python*', 'C:\Program Files\Python*', 'C:\Program Files (x86)\Python*',
               'D:\Python*', 'E:\Python*', "$env:LOCALAPPDATA\Programs\Python\*") {
    $pyDirs += (Get-Item $r -EA 0 | Where-Object { $_.PSIsContainer } | ForEach-Object { $_.FullName })
}
if ($pyDirs) {
    "  python installs (BROKEN = no python.exe, do not use):"
    foreach ($d in ($pyDirs | Select-Object -Unique)) {
        $mark = if (Test-Path (Join-Path $d 'python.exe')) { 'OK    ' } else { 'BROKEN' }
        "    [$mark] $d"
    }
}
$pipIni = Join-Path $env:APPDATA 'pip\pip.ini'
if (Test-Path $pipIni) {
    "  pip.ini mirrors:"
    Select-String -Path $pipIni -Pattern 'index-url|trusted-host|extra-index' -EA 0 |
        Select-Object -First 5 | ForEach-Object { "    " + $_.Line.Trim() }
}

H "3. GO"
$go = Try-Cmd 'go'
L "go" $go
L "  version" (Probe $go @('version'))
L "GOROOT" $env:GOROOT
L "GOPATH" $env:GOPATH
if ($go) {
    $envOut = Probe $go @('env','GOROOT','GOPATH','GOMODCACHE','GOPROXY','GOOS','GOARCH','CGO_ENABLED') 20 10
    "  go env: $envOut"
}
foreach ($p in 'C:\Go', 'D:\Go', 'E:\Go') { if (Test-Path $p) { L "  install dir" $p } }

H "4. NODE.JS"
$node = Try-Cmd 'node'
L "node" $node
L "  version" (Probe $node @('-v'))
L "npm.cmd" ((Probe 'cmd.exe' @('/c','npm.cmd -v')))
L "pnpm.cmd" ((Probe 'cmd.exe' @('/c','pnpm.cmd -v')))
L "yarn" (Probe (Try-Cmd 'yarn') @('-v'))
L "registry" (Probe 'cmd.exe' @('/c','npm.cmd config get registry') 25 1)
"  NOTE: in PowerShell use npm.cmd / pnpm.cmd; the .ps1 shims are blocked by execution policy"
foreach ($p in 'C:\Program Files\nodejs', 'D:\nodejs', 'E:\NodeJs', "$env:LOCALAPPDATA\Programs\nodejs") {
    if (Test-Path $p) { L "  install dir" $p }
}

H "5. .NET / C#"
$dotnet = Try-Cmd 'dotnet'
L "dotnet" $(if($dotnet){$dotnet}else{'<not found>'})
if ($dotnet) { L "  sdks" (Probe $dotnet @('--list-sdks') 30 6) }
else { "  -> no dotnet CLI: only classic .NET Framework projects are possible" }
$csc = 'C:\Windows\Microsoft.NET\Framework64\v4.0.30319\csc.exe'
if (Test-Path $csc) {
    # Do not execute csc: "/?" can block. File version is enough to infer the C# language level.
    $vi = (Get-Item $csc).VersionInfo
    L "csc.exe" "$($vi.FileVersion) (C# language level follows this version)"
}
$fw = (Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\NET Framework Setup\NDP\v4\Full' -EA 0).Release
L ".NET FW Release" $fw
foreach ($mb in @('C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe',
                 'C:\Program Files (x86)\MSBuild\14.0\Bin\MSBuild.exe',
                 'C:\Program Files\Microsoft Visual Studio\2022\*\MSBuild\Current\Bin\MSBuild.exe',
                 'D:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe')) {
    $f = Get-Item $mb -EA 0
    if ($f) { L "  MSBuild" "$($f.FullName) v$($f.VersionInfo.FileVersion)" }
}

H "6. C/C++ (MSVC)"
$vswhere = Join-Path ${env:ProgramFiles(x86)} 'Microsoft Visual Studio\Installer\vswhere.exe'
L "vswhere" (Test-Path $vswhere)
if (Test-Path $vswhere) {
    (Probe $vswhere @('-all','-products','*','-property','installationPath') 25 8) -split ' \| ' |
        ForEach-Object { "  VS: $_" }
    (Probe $vswhere @('-all','-products','*','-property','installationVersion') 25 8) -split ' \| ' |
        ForEach-Object { "    ver: $_" }
}

# cl.exe discovery: vswhere is fast; only fall back to a shallow scan when it is absent.
$clList = @()
if (Test-Path $vswhere) {
    $vsPaths = ((Probe $vswhere @('-all','-products','*','-property','installationPath') 25 8) -split ' \| ')
    foreach ($vp in $vsPaths) {
        $vp = $vp.Trim()
        if ($vp -and (Test-Path $vp)) {
            $clList += (Get-ChildItem $vp -Recurse -Filter 'cl.exe' -Depth 5 -EA 0 | Select-Object -First 3 -ExpandProperty FullName)
        }
    }
}
if (-not $clList) {
    # Directory names vary: "Microsoft Visual Studio 12.0", "... 2022", "BuildTools"...
    # Always glob, never hardcode the folder name.
    $vsRoots = @()
    foreach ($d in (Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 } |
                    ForEach-Object { $_.DeviceID })) {
        $vsRoots += (Get-Item "$d\Program Files*\Microsoft Visual Studio*" -EA 0 |
                     Where-Object { $_.PSIsContainer } | ForEach-Object { $_.FullName })
    }
    foreach ($base in $vsRoots) {
        if (Test-Path $base) {
            $clList += (Get-ChildItem $base -Recurse -Filter 'cl.exe' -Depth 4 -EA 0 |
                        Select-Object -First 3 -ExpandProperty FullName)
        }
    }
}
if ($clList) { "  cl.exe found:"; $clList | Select-Object -Unique | ForEach-Object { "    $_" } }
else { "  cl.exe: NOT FOUND (no C++ toolset)" }
$vcvarsList = @()
foreach ($d in (Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 } | ForEach-Object { $_.DeviceID })) {
    $vcvarsList += (Get-Item "$d\Program Files*\Microsoft Visual Studio*\VC\vcvarsall.bat" -EA 0 |
                    ForEach-Object { $_.FullName })
}
if ($vcvarsList) { $vcvarsList | Select-Object -Unique | ForEach-Object { L "  vcvarsall" $_ } }
else { L "  vcvarsall" '<not found> (cannot activate MSVC environment)' }
L "cl on PATH" (Try-Cmd 'cl')
L "link on PATH" (Try-Cmd 'link')

H "7. MinGW / LLVM / build tools"
$gcc = Try-Cmd 'gcc'
L "gcc" $(if($gcc){$gcc}else{'<not found>'})
if ($gcc) { L "  version" (Probe $gcc @('--version') 20 1) }
L "g++"  $(if(Try-Cmd 'g++'){Try-Cmd 'g++'}else{'<not found>'})
L "clang" (Try-Cmd 'clang')
L "cmake" $(if(Try-Cmd 'cmake'){Try-Cmd 'cmake'}else{'<not found>'})
L "make"  $(if(Try-Cmd 'make'){Try-Cmd 'make'}else{'<not found>'})
L "ninja" (Try-Cmd 'ninja')
foreach ($p in 'C:\MinGW', 'C:\msys64', 'C:\mingw64', 'D:\msys64', 'E:\msys64',
                 'C:\Program Files (x86)\CodeBlocks\MinGW') {
    if (Test-Path $p) { L "  dir" $p }
}
$cbGcc = 'C:\Program Files (x86)\CodeBlocks\MinGW\bin\gcc.exe'
if (Test-Path $cbGcc) { L "  CodeBlocks gcc" (Probe $cbGcc @('--version') 20 1) }

H "8. Windows SDK / WDK / Debuggers"
Get-ChildItem 'C:\Program Files (x86)\Windows Kits' -Directory -EA 0 | ForEach-Object { "  Kits: $($_.Name)" }
Get-ChildItem 'C:\Program Files (x86)\Windows Kits\10\Include' -Directory -EA 0 | ForEach-Object {
    $um = Test-Path (Join-Path $_.FullName 'um\windows.h')
    $km = Test-Path (Join-Path $_.FullName 'km\wdm.h')
    "    ver $($_.Name): SDK(um\windows.h)=$um WDK(km\wdm.h)=$km"
}
if (Test-Path 'C:\Program Files (x86)\Windows Kits\8.1\Include\um\windows.h') { "  SDK 8.1: present" }
foreach ($d in 'C:\Program Files (x86)\Debugging Tools for Windows (x86)',
               'C:\Program Files (x86)\Debugging Tools for Windows (x64)',
               'C:\Program Files\Windows Kits\10\Debuggers\x64') {
    if (Test-Path $d) {
        "  Debuggers dir: $d"
        Get-ChildItem $d -Filter '*.exe' -EA 0 |
            Where-Object { $_.Name -match '^(windbg|cdb|kd|ntsd|symchk)' } |
            ForEach-Object { "    $($_.Name)  $($_.VersionInfo.FileVersion)" }
    }
}
L "wpr (WPT)" (Try-Cmd 'wpr')
L "_NT_SYMBOL_PATH" $env:_NT_SYMBOL_PATH
""