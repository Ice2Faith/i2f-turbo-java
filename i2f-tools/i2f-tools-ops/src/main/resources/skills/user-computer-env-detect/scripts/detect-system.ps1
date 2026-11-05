# S1 baseline probe: OS / CPU / RAM / disks / GPU / network / charset / elevation
# ASCII-only output and source (console may be a non-UTF8 code page).
#   powershell -ExecutionPolicy Bypass -File detect-system.ps1
param([switch]$Quiet)

$ErrorActionPreference = 'SilentlyContinue'
function L($k, $v) { "{0,-28}: {1}" -f $k, $v }
function H($t) { ""; "=" * 62; "  $t"; "=" * 62 }

try {
    Add-Type -TypeDefinition @'
using System; using System.Runtime.InteropServices;
public class EnvCpProbe {
  [DllImport("kernel32.dll")] public static extern uint GetConsoleOutputCP();
  [DllImport("kernel32.dll")] public static extern uint GetACP();
  [DllImport("kernel32.dll")] public static extern uint GetOEMCP();
  public static string Dump() {
    return string.Format("ConsoleOutputCP={0} ACP={1} OEMCP={2}",
      GetConsoleOutputCP(), GetACP(), GetOEMCP());
  }
}
'@ -ErrorAction Stop
    $cpAvailable = $true
} catch { $cpAvailable = $false }

H "1. OS / HOST"
$os = Get-CimInstance Win32_OperatingSystem
$cs = Get-CimInstance Win32_ComputerSystem
L "Hostname"          $os.CSName
L "Caption"           $os.Caption
L "Version/Build"     "$($os.Version) / $($os.BuildNumber)"
L "Architecture"      $os.OSArchitecture
L "InstallDate"       $os.InstallDate
L "LastBootUpTime"    $os.LastBootUpTime
L "Manufacturer"      $cs.Manufacturer
L "Model"             $cs.Model
L "Domain/Workgroup"  $cs.Domain
$bb = Get-CimInstance Win32_BaseBoard
L "BaseBoard"         "$($bb.Manufacturer) $($bb.Product)"
$bios = Get-CimInstance Win32_BIOS
L "BIOS"              "$($bios.SMBIOSBIOSVersion) ($($bios.ReleaseDate))"

H "2. CPU"
Get-CimInstance Win32_Processor | ForEach-Object {
    L "Name"          $_.Name.Trim()
    L "Cores/Threads" "$($_.NumberOfCores) / $($_.NumberOfLogicalProcessors)"
    L "MaxClockMHz"   $_.MaxClockSpeed
    L "L3CacheKB"     $_.L3CacheSize
    L "VirtFirmware"  $_.VirtualizationFirmwareEnabled
    L "SLAT"          $_.SecondLevelAddressTranslationExtensions
}

H "3. MEMORY"
L "TotalGB"  ([math]::Round($os.TotalVisibleMemorySize / 1MB, 2))
L "FreeGB"   ([math]::Round($os.FreePhysicalMemory / 1MB, 2))
Get-CimInstance Win32_PhysicalMemory | ForEach-Object {
    L "  Stick" ("{0} {1} {2}GB @{3}MHz {4}" -f $_.Manufacturer, $_.PartNumber,
        [math]::Round($_.Capacity / 1GB, 0), $_.Speed, $_.DeviceLocator)
}
Get-CimInstance Win32_PageFileUsage | ForEach-Object {
    L "  PageFile" ("{0} {1}MB peak={2}MB" -f $_.Name, $_.AllocatedBaseSize, $_.PeakUsage)
}

H "4. DISKS (logical)"
Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 } | ForEach-Object {
    $freeGb = [math]::Round($_.FreeSpace / 1GB, 1)
    $flag = if ($freeGb -lt 10) { '  << LOW: DO NOT WRITE' } else { '' }
    L $_.DeviceID ("{0,-6} total={1,7:N1}GB free={2,7:N1}GB label='{3}' fs={4}{5}" -f
        '', ($_.Size / 1GB), $freeGb, $_.VolumeName, $_.FileSystem, $flag)
}

H "5. DISKS (physical)"
Get-PhysicalDisk | ForEach-Object {
    L $_.FriendlyName ("{0,7:N0}GB {1}/{2} health={3}" -f ($_.Size / 1GB), $_.MediaType, $_.BusType, $_.HealthStatus)
}

H "6. GPU"
Get-CimInstance Win32_VideoController | ForEach-Object {
    L "Name"        $_.Name
    L "  PNPDevID"  ($_.PNPDeviceID -replace '\\\\.*', '')
    L "  Driver"    "$($_.DriverVersion) ($($_.DriverDate))"
    L "  AdapterRAM" ("{0:N0} MB" -f ($_.AdapterRAM / 1MB))
    L "  Resolution" ("{0}x{1}@{2}Hz avail={3}" -f $_.CurrentHorizontalResolution,
        $_.CurrentVerticalResolution, $_.CurrentRefreshRate, $_.Availability)
}
Get-PnpDevice -Class Display -ErrorAction SilentlyContinue | ForEach-Object {
    L "  PnP" ("{0} status={1} problem={2}" -f $_.FriendlyName, $_.Status, $_.Problem)
}

H "7. NETWORK"
Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.IPAddress -ne '127.0.0.1' } | ForEach-Object {
    L $_.InterfaceAlias ("{0}/{1}" -f $_.IPAddress, $_.PrefixLength)
}

H "8. CHARSET / LOCALE  (compare all four sources)"
$nls = Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\Nls\CodePage' -ErrorAction SilentlyContinue
L "Registry ACP"      $nls.ACP
L "Registry OEMCP"    $nls.OEMCP
L "Encoding.Default"  ("{0} / CP{1}" -f [System.Text.Encoding]::Default.WebName, [System.Text.Encoding]::Default.CodePage)
L "chcp (cmd)"        (((cmd /c chcp) -replace '.*?(\d+).*', '$1').Trim())
if ($cpAvailable) { L "Win32 API" ([EnvCpProbe]::Dump()) }
L "[Console] Out"     ("{0} / CP{1}" -f [Console]::OutputEncoding.WebName, [Console]::OutputEncoding.CodePage)
L "[Console] In"      ("{0} / CP{1}" -f [Console]::InputEncoding.WebName, [Console]::InputEncoding.CodePage)
L "`$OutputEncoding"  ("{0} / CP{1}" -f $OutputEncoding.WebName, $OutputEncoding.CodePage)
L "CurrentCulture"    [System.Globalization.CultureInfo]::CurrentCulture.Name
L "CurrentUICulture"  [System.Globalization.CultureInfo]::CurrentUICulture.Name
L "WinSystemLocale"   (Get-WinSystemLocale).Name
L "TimeZone"          (Get-TimeZone).Id
if ($nls.ACP -eq '65001') {
    "  -> Global UTF-8 (beta) ENABLED"
} else {
    "  -> Global UTF-8 OFF: text defaults to code page $($nls.ACP); set encoding explicitly"
}

H "9. CONTEXT"
$id = [Security.Principal.WindowsIdentity]::GetCurrent()
$pr = [Security.Principal.WindowsPrincipal]$id
L "Account"        $id.Name
L "IsElevated"     ($pr.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator))
L "PSVersion"      ("{0} {1}" -f $PSVersionTable.PSVersion, $PSVersionTable.PSEdition)
$admins = (net localgroup Administrators 2>$null) -join "`n"
L "InAdminsGroup"  ($admins -match [regex]::Escape(($id.Name -split '\\')[-1]))
$uac = Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System' -ErrorAction SilentlyContinue
L "EnableLUA"      $uac.EnableLUA
L "ConsentPrompt"  $uac.ConsentPromptBehaviorAdmin
if ($uac.ConsentPromptBehaviorAdmin -eq 0) {
    "  -> 0 = elevate WITHOUT prompt: RunAs works silently (security note)"
}
L "LongPathsEnabled" (Get-ItemProperty 'HKLM:\SYSTEM\CurrentControlSet\Control\FileSystem' -Name LongPathsEnabled -EA 0).LongPathsEnabled
L "ExecutionPolicy"  ((Get-ExecutionPolicy -List) | ForEach-Object { "$($_.Scope)=$($_.ExecutionPolicy)" }) -join ' '
""