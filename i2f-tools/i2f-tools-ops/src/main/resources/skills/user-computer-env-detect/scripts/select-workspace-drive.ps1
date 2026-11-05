# Workspace drive selector.
#
# Rule: never assume a drive letter. Rank fixed disks by FREE SPACE, recommend the
# best one, then ASK THE USER before creating anything.
#
#   powershell -ExecutionPolicy Bypass -File select-workspace-drive.ps1
#
# Output is a ranked table plus a one-line recommendation. It never creates folders.
# ASCII-only output and source.

$ErrorActionPreference = 'Continue'
function L($k, $v) { "{0,-18}: {1}" -f $k, $v }

$MIN_FREE_GB     = 10      # below this a drive is not a candidate
$RESERVE_GB      = 5       # keep this much head-room after use

"=" * 62
"  WORKSPACE DRIVE CANDIDATES"
"=" * 62

$sys = $env:SystemDrive
$disks = Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 } | ForEach-Object {
    [PSCustomObject]@{
        Drive     = $_.DeviceID
        Label     = [string]$_.VolumeName
        FreeGB    = [math]::Round($_.FreeSpace / 1GB, 1)
        TotalGB   = [math]::Round($_.Size / 1GB, 1)
        IsSystem  = ($_.DeviceID -eq $sys)
        HasWS     = (Test-Path (Join-Path $_.DeviceID '\01-ai-workspace'))
    }
}

"  {0,-6} {1,-12} {2,9} {3,9} {4,-9} {5}" -f 'DRIVE', 'LABEL', 'FREE_GB', 'TOTAL_GB', 'SYSTEM', 'HAS_01-AI-WORKSPACE'
foreach ($d in ($disks | Sort-Object FreeGB -Descending)) {
    "  {0,-6} {1,-12} {2,9:N1} {3,9:N1} {4,-9} {5}" -f `
        $d.Drive, $(if ($d.Label) { $d.Label } else { '-' }), $d.FreeGB, $d.TotalGB, `
        $(if ($d.IsSystem) { 'YES' } else { '' }), $(if ($d.HasWS) { 'yes' } else { '' })
}

# Candidates: not the system drive, enough free space to be useful.
$cand = $disks | Where-Object { -not $_.IsSystem -and $_.FreeGB -ge $MIN_FREE_GB } |
        Sort-Object FreeGB -Descending

"" 
"=" * 62
"  DECISION"
"=" * 62

if (-not $cand) {
    "  No candidate drive found (need a non-system fixed disk with >= ${MIN_FREE_GB} GB free)."
    "  -> ask the user where to put files; do NOT silently pick C:"
    ""
    exit 2
}

$best = $cand[0]
$existing = $cand | Where-Object { $_.HasWS } | Sort-Object FreeGB -Descending | Select-Object -First 1

L "rule"            "highest free space on a non-system fixed disk"
L "recommended"     "$($best.Drive)  ($($best.FreeGB) GB free)"
if ($existing -and $existing.Drive -ne $best.Drive) {
    L "already in use"  "$($existing.Drive)  ($($existing.FreeGB) GB free) - existing 01-ai-workspace"
    "  NOTE: an existing workspace root was found on a DIFFERENT drive than the rule's pick."
    "        Do not silently move it. Ask the user which one to keep using."
}
L "reserve"         "keep >= ${RESERVE_GB} GB free after writing"
L "target"          "$($best.Drive)\01-ai-workspace\"

"" 
"  ACTION REQUIRED: ask the user to confirm the drive before creating anything."
"  Suggested question (translate to the user's language):"
"    'Drive <X>: has the most free space (<N> GB). Use <X>:\01-ai-workspace for"
"     files I generate, or would you prefer another drive?'"
""
""