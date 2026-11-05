# System character-set / encoding probe.
#
# Usage: powershell -ExecutionPolicy Bypass -File env-tools\encoding-check.ps1
#
# ASCII output only: this script must stay readable regardless of code page.

$ErrorActionPreference = 'Continue'

function Line($k, $v) { "{0,-34}: {1}" -f $k, $v }

Write-Host ("=" * 66) -ForegroundColor Cyan
Write-Host "  SYSTEM CHARACTER SET" -ForegroundColor Cyan
Write-Host ("=" * 66) -ForegroundColor Cyan

# --- code pages ---
$acp = (Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\Nls\CodePage' -Name ACP).ACP
$oemcp = (Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\Nls\CodePage' -Name OEMCP).OEMCP
Line "Registry ACP (ANSI)" $acp
Line "Registry OEMCP" $oemcp
Line "Encoding.Default.CodePage" ([System.Text.Encoding]::Default.CodePage)
Line "Encoding.Default.WebName" ([System.Text.Encoding]::Default.WebName)
Line "Culture.TextInfo.ANSICodePage" ([System.Globalization.CultureInfo]::CurrentCulture.TextInfo.ANSICodePage)
Line "Culture.TextInfo.OEMCodePage" ([System.Globalization.CultureInfo]::CurrentCulture.TextInfo.OEMCodePage)

if ($acp -eq '65001') {
    Write-Host "  -> Global UTF-8 (beta) is ENABLED" -ForegroundColor Green
} else {
    Write-Host "  -> Global UTF-8 (beta) is OFF: ANSI/OEM both $acp/$oemcp (GBK)" -ForegroundColor Yellow
}

# --- locale ---
Write-Host ""
Write-Host ("=" * 66) -ForegroundColor Cyan
Write-Host "  LOCALE / REGION" -ForegroundColor Cyan
Write-Host ("=" * 66) -ForegroundColor Cyan
Line "CurrentCulture" ([System.Globalization.CultureInfo]::CurrentCulture.Name)
Line "CurrentUICulture" ([System.Globalization.CultureInfo]::CurrentUICulture.Name)
try { Line "Get-WinSystemLocale" (Get-WinSystemLocale).Name } catch {}
try { Line "WinUserLanguageList" ((Get-WinUserLanguageList | ForEach-Object { $_.LanguageTag }) -join ',') } catch {}
try { Line "TimeZone" (Get-TimeZone).Id } catch {}

# --- console / powershell ---
Write-Host ""
Write-Host ("=" * 66) -ForegroundColor Cyan
Write-Host "  CONSOLE / POWERSHELL ENCODING" -ForegroundColor Cyan
Write-Host ("=" * 66) -ForegroundColor Cyan
Line "[Console]::OutputEncoding" ("{0} / CP{1}" -f [Console]::OutputEncoding.WebName, [Console]::OutputEncoding.CodePage)
Line "[Console]::InputEncoding" ("{0} / CP{1}" -f [Console]::InputEncoding.WebName, [Console]::InputEncoding.CodePage)
Line '$OutputEncoding' ("{0} / CP{1}" -f $OutputEncoding.WebName, $OutputEncoding.CodePage)
Line "PS version" $PSVersionTable.PSVersion
Line "PS edition" $PSVersionTable.PSEdition
Line "chcp" (& cmd /c chcp) -replace 'Active code page:\s*', ''

# --- proof: read GBK bytes as UTF-8 and vice versa ---
Write-Host ""
Write-Host "  BYTE-LEVEL PROOF (why mojibake happens)" -ForegroundColor Yellow
$s = [char]0x4E2D + [char]0x6587          # CJK
$utf8 = [System.Text.Encoding]::UTF8.GetBytes($s)
$gbk = [System.Text.Encoding]::GetEncoding(936).GetBytes($s)
Line "  utf-8 bytes of U+4E2D U+6587" (($utf8 | ForEach-Object { '{0:X2}' -f $_ }) -join ' ')
Line "  gbk   bytes of U+4E2D U+6587" (($gbk | ForEach-Object { '{0:X2}' -f $_ }) -join ' ')
$asUtf8 = [System.Text.Encoding]::UTF8.GetString($gbk)
Line "  GBK bytes decoded AS UTF-8 -> U+ codes" ((($asUtf8.ToCharArray() | ForEach-Object { '{0:X4}' -f [int]$_ }) -join ' '))
Write-Host "  (that U+ list is the mangled glyphs you see = classic mojibake)"

# --- git ---
Write-Host ""
Write-Host ("=" * 66) -ForegroundColor Cyan
Write-Host "  GIT ENCODING" -ForegroundColor Cyan
Write-Host ("=" * 66) -ForegroundColor Cyan
foreach ($k in @('i18n.commitEncoding', 'i18n.logOutputEncoding', 'core.quotepath', 'core.autocrlf', 'core.safecrlf')) {
    $v = & git config --global --get $k 2>$null
    if (-not $v) { $v = '<unset>' }
    Line ("git $k") $v
}

Write-Host ""
Write-Host ("=" * 66) -ForegroundColor Cyan