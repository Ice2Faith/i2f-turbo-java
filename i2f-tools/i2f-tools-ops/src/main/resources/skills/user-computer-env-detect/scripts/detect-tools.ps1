# S3 common tools probe: git / ffmpeg / pandoc / LibreOffice / ImageMagick / OCR / archives / db / net
# ASCII-only output and source.
#   powershell -ExecutionPolicy Bypass -File detect-tools.ps1

$ErrorActionPreference = 'SilentlyContinue'
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false } catch {}
function L($k, $v) { "{0,-24}: {1}" -f $k, $v }
function H($t) { ""; "=" * 62; "  $t"; "=" * 62 }
function Try-Cmd($n) { (Get-Command $n -ErrorAction SilentlyContinue).Source }
function First($lines) { ($lines 2>&1 | Select-Object -First 1) -join '' }

H "1. PRESENCE MATRIX (on PATH or not)"
"  {0,-14} {1,-8} {2}" -f 'TOOL', 'ON PATH', 'RESOLVED PATH'
foreach ($t in 'git','ffmpeg','ffprobe','pandoc','soffice','magick','convert','tesseract',
               '7z','bz','docker','adb','fastboot','sqlcmd','sqllocaldb','curl','ssh','gh',
               'wget','unzip','tar','python','node','java','go','cargo','dotnet','code','idea64') {
    $src = Try-Cmd $t
    "  {0,-14} {1,-8} {2}" -f $t, $(if ($src) { 'YES' } else { 'no' }), $(if ($src) { $src } else { '' })
}

H "2. GIT"
$git = Try-Cmd 'git'
if ($git) {
    L "version"  ((& git --version 2>&1) -join '')
    L "user.name"  ((& git config --global --get user.name 2>&1) -join '')
    L "user.email" ((& git config --global --get user.email 2>&1) -join '')
    L "core.autocrlf" ((& git config --global --get core.autocrlf 2>&1) -join '')
    L "core.quotepath" ((& git config --global --get core.quotepath 2>&1) -join '')
    L "i18n.commitEncoding" ((& git config --global --get i18n.commitEncoding 2>&1) -join '')
    $bash = @('C:\Program Files\Git\bin\bash.exe','C:\Program Files (x86)\Git\bin\bash.exe',
              'D:\Program Files\Git\bin\bash.exe') | Where-Object { Test-Path $_ }
    L "git-bash" ($(if ($bash) { $bash[0] } else { 'not found' }))
    if ($bash) { L "  bash version" ((& $bash[0] --version 2>&1) -join '') }
} else { L "git" 'NOT INSTALLED' }
L "svn" (Try-Cmd 'svn')

H "3. FFMPEG (hardware acceleration matters)"
$ff = Try-Cmd 'ffmpeg'
if ($ff) {
    L "version" (First (& ffmpeg -version))
    "  hardware encoders:"
    (& ffmpeg -hide_banner -encoders 2>&1 | Select-String 'nvenc|qsv|amf|vaapi|videotoolbox|v4l2m2m') |
        Select-Object -First 12 | ForEach-Object { "    " + $_.Line.Trim() }
    "  hwaccels:"
    (& ffmpeg -hide_banner -hwaccels 2>&1 | Select-Object -Skip 1) | Select-Object -First 10 |
        ForEach-Object { "    $_" }
} else { L "ffmpeg" 'NOT INSTALLED' }

H "4. PANDOC / LaTeX / LibreOffice"
$pd = Try-Cmd 'pandoc'
if ($pd) {
    L "pandoc" (First (& pandoc --version))
    $latex = @()
    foreach ($e in 'xelatex','pdflatex','lualatex','tectonic','typst','wkhtmltopdf','weasyprint') {
        $s = Try-Cmd $e
        if ($s) { $latex += "$e=$s" }
    }
    L "PDF engines" ($(if ($latex) { $latex -join '; ' } else { 'NONE -> pandoc cannot emit PDF directly' }))
    "  -> workaround if none: pandoc -o out.docx, then soffice --convert-to pdf"
}
$so = Try-Cmd 'soffice'
if (-not $so) {
    foreach ($p in 'C:\Program Files\LibreOffice\program\soffice.com',
                   'D:\Program Files\LibreOffice\program\soffice.com',
                   'E:\Program Files\LibreOffice\program\soffice.com') { if (Test-Path $p) { $so = $p } }
}
if ($so) { L "soffice" $so; L "  version" (First (& $so --version)) } else { L "soffice" 'NOT INSTALLED' }

H "5. IMAGE / OCR"
$mg = Try-Cmd 'magick'
if ($mg) {
    L "magick" (First (& magick -version))
    $policy = 'C:\Program Files\ImageMagick-7.1.2-Q8\policy.xml'
    Get-ChildItem 'C:\Program Files\ImageMagick*\policy.xml' -EA 0 | ForEach-Object {
        "  policy: $($_.FullName)"
        Select-String -Path $_.FullName -Pattern 'rights="none"|pattern="PDF|pattern="PS' -EA 0 |
            Select-Object -First 4 | ForEach-Object { "    " + $_.Line.Trim() }
    }
} else {
    $cv = Try-Cmd 'convert'
    if ($cv) { L "convert (IM6)" (First (& convert -version)) } else { L "ImageMagick" 'NOT INSTALLED' }
}
$ts = Try-Cmd 'tesseract'
if ($ts) {
    L "tesseract" (First (& tesseract --version))
    L "  languages" ((& tesseract --list-langs 2>&1 | Select-Object -Skip 1) -join ' ')
} else { L "tesseract" 'NOT INSTALLED' }

H "6. ARCHIVE / MOBILE / DB / NET"
L "7z"   (Try-Cmd '7z')
L "bz"   (Try-Cmd 'bz')
L "adb"  (Try-Cmd 'adb')
if (Try-Cmd 'adb') { L "  adb version" (First (& adb version)) }
L "sqlcmd" (Try-Cmd 'sqlcmd')
L "sqllocaldb" (Try-Cmd 'sqllocaldb')
if (Try-Cmd 'sqllocaldb') { "  instances:"; (& sqllocaldb info 2>&1) | ForEach-Object { "    $_" } }
L "curl" (First (& curl --version))
L "ssh"  (First (& ssh -V))
L "docker" (Try-Cmd 'docker')
L "gh"   (Try-Cmd 'gh')

H "7. PACKAGE MANAGERS"
foreach ($pm in 'choco','scoop','winget','vcpkg','conan') {
    $s = Try-Cmd $pm
    L $pm $(if ($s) { $s } else { 'not found' })
}
""