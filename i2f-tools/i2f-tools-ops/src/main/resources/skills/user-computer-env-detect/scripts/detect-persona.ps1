# S5 persona probe: infer general-user / office-worker / developer from BEHAVIOUR signals.
# Never relies on a single hint; prints evidence and a suggestion, not a verdict.
# ASCII-only output and source.
#   powershell -ExecutionPolicy Bypass -File detect-persona.ps1

$ErrorActionPreference = 'SilentlyContinue'
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false } catch {}
function L($k, $v) { "{0,-26}: {1}" -f $k, $v }
function H($t) { ""; "=" * 62; "  $t"; "=" * 62 }

$devScore = 0
$officeScore = 0
$devHits = @()
$officeHits = @()

H "1. INSTALLED SOFTWARE PROFILE"
$keys = 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\*',
        'HKLM:\SOFTWARE\WOW6432Node\Microsoft\Windows\CurrentVersion\Uninstall\*',
        'HKCU:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\*'
$apps = @(Get-ItemProperty $keys -EA 0 | Where-Object { $_.DisplayName -and -not $_.SystemComponent } |
          Select-Object -ExpandProperty DisplayName -Unique)
L "app count" $apps.Count

$devPattern    = 'Visual Studio|VS Code|IntelliJ|PyCharm|WebStorm|GoLand|CLion|Rider|JetBrains|Eclipse|' +
                 'Docker|WSL|Git|Subversion|TortoiseSVN|Node\.js|Python 3|JDK|Java.*Development|Go Programming|' +
                 'Postman|Navicat|SSMS|SQL Server Management|Xshell|MobaXterm|PuTTY|Maven|Gradle|' +
                 'CMake|MinGW|LLVM|Anaconda|Miniconda|Cygwin|MSYS2|WinSCP|DBeaver|Redis|MongoDB'
# Chinese app names are written as regex \uXXXX escapes so this file stays pure ASCII
$officePattern = 'Microsoft Office|Office 365|WPS|LibreOffice|\u9489\u9489|DingTalk|\u4F01\u4E1A\u5FAE\u4FE1|WeCom|\u98DE\u4E66|Feishu|Lark|' +
                 'Foxmail|Outlook|\u7F51\u6613\u90AE\u7BB1\u5927\u5E08|MailMaster|\u767E\u5EA6\u7F51\u76D8|BaiduNetdisk|\u6709\u9053|\u5370\u8C61\u7B14\u8BB0|Evernote|Notion|' +
                 'Acrobat|PDF|Adobe|Zoom|\u817E\u8BAF\u4F1A\u8BAE|Tencent Meeting'

foreach ($a in $apps) {
    if ($a -match $devPattern)    { $devScore++;    if ($devHits.Count   -lt 12) { $devHits    += $a } }
    if ($a -match $officePattern) { $officeScore++; if ($officeHits.Count -lt 12) { $officeHits += $a } }
}
"  developer-ish apps:";    if ($devHits)    { $devHits    | ForEach-Object { "    $_" } } else { "    (none)" }
"  office-ish apps:";       if ($officeHits) { $officeHits | ForEach-Object { "    $_" } } else { "    (none)" }

H "2. DEVELOPER TRACES (config files)"
$traces = @(
    "$env:USERPROFILE\.gitconfig",
    "$env:USERPROFILE\.m2",
    "$env:USERPROFILE\.gradle",
    "$env:USERPROFILE\.npmrc",
    "$env:USERPROFILE\.condarc",
    "$env:USERPROFILE\.cargo",
    "$env:USERPROFILE\go",
    "$env:USERPROFILE\.dsh",
    "$env:USERPROFILE\.vscode",
    "$env:USERPROFILE\.idea*",
    "$env:USERPROFILE\.ssh\id_*"
)
$traceHit = 0
foreach ($t in $traces) {
    $hit = @(Get-Item $t -EA 0)
    if ($hit.Count -gt 0) { $traceHit++; L "  found" $t }
}
L "trace count" $traceHit
if ($traceHit -ge 3) { $devScore += 3 }

H "3. SHELL CUSTOMISATION"
$prof = $PROFILE
L "PROFILE path" $prof
L "PROFILE exists" (Test-Path $prof)
if (Test-Path $prof) { $devScore += 2 }
L "PSModulePath entries" (($env:PSModulePath -split ';').Count)
$pathCount = ($env:PATH -split ';' | Where-Object { $_ }).Count
L "PATH entries" $pathCount
$customPath = $env:PATH -split ';' | Where-Object { $_ -and $_ -notmatch '(?i)Windows|Program Files|System32|AppData\\Local\\Microsoft' }
L "custom PATH dirs" $customPath.Count
if ($customPath) { $customPath | Select-Object -First 8 | ForEach-Object { "    $_" } }
if ($pathCount -gt 30) { $devScore += 1 }
if ($customPath.Count -ge 3) { $devScore += 2 }

H "4. ELEVATION HABITS"
$id = [Security.Principal.WindowsIdentity]::GetCurrent()
$pr = [Security.Principal.WindowsPrincipal]$id
L "account" $id.Name
L "isElevated(now)" ($pr.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator))
$admins = (net localgroup Administrators 2>$null) -join "`n"
$inAdmin = $admins -match [regex]::Escape(($id.Name -split '\\')[-1])
L "in Administrators" $inAdmin
$uac = Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System' -EA 0
L "ConsentPromptBehaviorAdmin" $uac.ConsentPromptBehaviorAdmin
switch ($uac.ConsentPromptBehaviorAdmin) {
    0 { "  -> 0: elevation is SILENT. Any program can gain admin. Tell the user." }
    5 { "  -> 5: default, UAC prompt on elevation (user must click Yes)" }
    2 { "  -> 2: credential prompt required (user must type admin password)" }
}
L "Developer Mode" ((Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\AppModelUnlock' -EA 0).AllowDevelopmentWithoutDevLicense)

H "5. FILESYSTEM HABITS (weak signal)"
$writable = Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 -and $_.FreeSpace/1GB -gt 10 }
foreach ($d in $writable) {
    $root = Get-ChildItem "$($d.DeviceID)\" -Directory -EA 0 |
            Where-Object { $_.Name -match '^(01-|workspace|projects|dev|code|work|projects)' }
    if ($root) {
        "  $($d.DeviceID) organised roots:"
        $root | ForEach-Object { "    $($_.Name)" }
    }
}
L "Desktop redirected" ([Environment]::GetFolderPath('Desktop'))
L "Documents redirected" ([Environment]::GetFolderPath('MyDocuments'))

H "6. RECOMMENDATION (evidence-based, not a verdict)"
"  developer hits = $devScore   office hits = $officeScore"
$persona = if ($devScore -ge 5) { 'DEVELOPER' }
           elseif ($officeScore -ge 4) { 'OFFICE-WORKER' }
           elseif ($devScore -ge 2) { 'LIKELY-DEVELOPER (weak)' }
           else { 'GENERAL-USER' }
L "suggested persona" $persona

switch -Wildcard ($persona) {
    'DEVELOPER*' {
        "  -> give copy-paste commands; state shell + elevation; you may do admin work"
        "  -> put decisions (versions, tradeoffs) to the user; never destructive actions silently"
    }
    'OFFICE-WORKER' {
        "  -> result-first; prefer already-installed tools; avoid installing new things"
        "  -> few, single-step commands; minimise interruptions"
    }
    default {
        "  -> do it for them; avoid command lines and jargon; deliver files, not instructions"
        "  -> explain consequences before any change; provide a rollback path"
    }
}
""
"  NOTE: if unsure, assume DEVELOPER capability + GENERAL-USER risk tolerance,"
"        and confirm with one cheap question instead of guessing."
""