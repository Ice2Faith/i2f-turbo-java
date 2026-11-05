# S1 系统基线探测

## 1. 一段式采集（推荐先跑脚本）

`scripts/detect-system.ps1` 输出：OS / CPU / 内存 / 页面文件 / 磁盘 / 物理盘 / GPU / 网络 / 字符集 / 提权状态。
全部 ASCII 输出，可直接贴进报告。

## 2. 手工命令（按需取用）

### 2.1 OS 与主机

```powershell
$os = Get-CimInstance Win32_OperatingSystem
$os.Caption; $os.Version; $os.BuildNumber; $os.OSArchitecture
$os.InstallDate; $os.LastBootUpTime; $os.CSName
Get-CimInstance Win32_ComputerSystem | Select Manufacturer, Model, TotalPhysicalMemory, Domain
Get-CimInstance Win32_BaseBoard | Select Manufacturer, Product
Get-CimInstance Win32_BIOS | Select Manufacturer, SMBIOSBIOSVersion, ReleaseDate
```

> 机型（`Manufacturer` + `Model`）是天然的"机器指纹"，非常适合写进沉淀技能的适用性自检。

### 2.2 CPU / 虚拟化

```powershell
Get-CimInstance Win32_Processor |
  Select Name, NumberOfCores, NumberOfLogicalProcessors, MaxClockSpeed, L3CacheSize,
         VirtualizationFirmwareEnabled, SecondLevelAddressTranslationExtensions
```

判据：
- `NumberOfCores` 决定并行度建议（≤2 核时别开 `-T 8`、别并发多个子代理）
- `VirtualizationFirmwareEnabled=False` → WSL2/Hyper-V/VMware 64 位客户机可能起不来

### 2.3 内存

```powershell
Get-CimInstance Win32_PhysicalMemory |
  Select Manufacturer, PartNumber, Capacity, Speed, DeviceLocator
Get-CimInstance Win32_PageFileUsage | Select Name, AllocatedBaseSize, PeakUsage
```

判据：条数与频率不一致（如 3200+2400 混插）说明是升级过的机器，实际运行频率取低值。
SODIMM 形态 → 笔记本/迷你主机。

### 2.4 磁盘（最重要的决策依据）

```powershell
Get-CimInstance Win32_LogicalDisk | Where-Object DriveType -eq 3 |
  Select DeviceID, FileSystem, @{n='TotalGB';e={[math]::Round($_.Size/1GB,1)}},
         @{n='FreeGB';e={[math]::Round($_.FreeSpace/1GB,1)}}, VolumeName
Get-PhysicalDisk | Select FriendlyName, MediaType, BusType, HealthStatus, @{n='SizeGB';e={[math]::Round($_.Size/1GB,0)}}
```

判据与结论：

| 观察 | 结论 |
|---|---|
| 某盘可用 < 10 GB | 标记 🔴 **禁止写**，写进报告的硬约束 |
| `MediaType=HDD` | 随机 I/O 慢，别把构建缓存放这里 |
| 多个盘符同一 `FriendlyName` | 同一物理盘！分区不等于独立性能 |
| `HealthStatus != Healthy` | 必须在报告里警示 |

### 2.4b 选盘算法（决定 AI 产物写到哪）

```powershell
$sys = $env:SystemDrive
Get-CimInstance Win32_LogicalDisk | Where-Object { $_.DriveType -eq 3 } |
  Where-Object { $_.DeviceID -ne $sys -and $_.FreeSpace/1GB -ge 10 } |
  Sort-Object FreeSpace -Descending |
  Select-Object DeviceID, VolumeName,
                @{n='FreeGB';e={[math]::Round($_.FreeSpace/1GB,1)}}
```

规则：排除系统盘 → 排除 < 10 GB → 按可用降序 → 第一名作为**推荐盘** → **问用户确认** → 才建 `<盘>:\01-ai-workspace`。

> 不要因为"某盘历史上用过"就沿用；也不要因为"推荐盘最大"就直接建。**先问。**
### 2.5 GPU

```powershell
Get-CimInstance Win32_VideoController |
  Select Name, PNPDeviceID, DriverVersion, DriverDate, AdapterRAM,
         CurrentHorizontalResolution, CurrentVerticalResolution, Availability
Get-PnpDevice -Class Display | Select FriendlyName, Status, Problem
```

判据：
- `PNPDeviceID` 里的 `VEN_10DE` = NVIDIA、`VEN_8086` = Intel、`VEN_1002` = AMD
- `Availability=3` 通常在驱动显示；`8` 表示未驱动桌面（可能是纯计算卡）
- **只有 NVIDIA 能跑 CUDA**，Intel/AMD 核显不是 CUDA 设备

### 2.6 网络

```powershell
Get-NetIPAddress -AddressFamily IPv4 | Where IPAddress -ne '127.0.0.1' |
  Select InterfaceAlias, IPAddress, PrefixLength
Get-NetTCPConnection -State Listen | Where LocalPort -in 3306,5432,1433,6379,8080,11434
```

用途：判断是否公司内网（影响镜像源可用性）、本机是否跑着数据库/服务。

## 3. 字符集与编码（**必测，且要测三处**）

```powershell
# ① 注册表（系统默认，决定文件与 API 的默认编码）
$c = 'HKLM:\SYSTEM\CurrentControlSet\Control\Nls\CodePage'
Get-ItemProperty $c | Select ACP, OEMCP, MACCP

# ② cmd 报告（可能与 ①②③ 不一致）
cmd /c chcp

# ③ 实际控制台输出代码页
Add-Type -TypeDefinition @'
using System; using System.Runtime.InteropServices;
public class CpProbe {
  [DllImport("kernel32.dll")] public static extern uint GetConsoleOutputCP();
  [DllImport("kernel32.dll")] public static extern uint GetACP();
  [DllImport("kernel32.dll")] public static extern uint GetOEMCP();
  public static string Dump(){ return string.Format("ConsoleOutputCP={0} ACP={1} OEMCP={2}",
      GetConsoleOutputCP(), GetACP(), GetOEMCP()); }
}
'@
[CpProbe]::Dump()

# ④ .NET 视角
[System.Text.Encoding]::Default.WebName
[Console]::OutputEncoding.CodePage
[Console]::InputEncoding.CodePage
$OutputEncoding.CodePage          # PS 里默认可能是 us-ascii(20127)！

# ⑤ 区域
[System.Globalization.CultureInfo]::CurrentCulture.Name
[System.Globalization.CultureInfo]::CurrentUICulture.Name
Get-WinSystemLocale; Get-TimeZone
```

**常见值对照**：

| ACP/OEMCP | 字符集 | 典型场景 |
|---|---|---|
| 936 | GBK/GB2312 | 简中 Windows（最常见） |
| 950 | Big5 | 繁中 |
| 932 | Shift-JIS | 日文 |
| 1252 | Windows-1252 | 西欧 |
| 65001 | UTF-8 | 开了 UTF-8 Beta |

**必须写进报告**：ACP、OEMCP、是否开 UTF-8 Beta、`CurrentCulture`、以及"三者是否一致"。
若不一致（很常见），直接给出结论：**该机器存在乱码风险，所有文本落盘必须显式指定编码**。

## 4. 系统信息预查询手法

| 方式 | 输出编码 | 状态 | 建议 |
|---|---|---|---|
| `wmic` | **UTF-16LE 带 BOM** | 微软已标记 deprecated | 只在老系统兜底；管道里会乱码 |
| `systeminfo` | **UTF-8**（字段本地化） | 可用 | 不适合脚本匹配（字段名随语言变） |
| `Get-CimInstance` | .NET 对象 | 现代标准 | ✅ **首选** |
| 注册表 `Uninstall` | — | 已装软件清单 | 软件盘点唯一可靠来源（比 `wmic product` 全） |

常用 CIM 类速查：

```
Win32_OperatingSystem     OS 版本/安装日期/内存
Win32_ComputerSystem      机型/厂商/内存总量/域
Win32_Processor           CPU
Win32_PhysicalMemory      内存条
Win32_LogicalDisk         盘符容量
Win32_DiskDrive           物理盘
Win32_VideoController     GPU
Win32_BaseBoard / Win32_BIOS
Win32_NetworkAdapterConfiguration   网卡/IP
Win32_SystemDriver        驱动
```

已装软件（比 wmic 完整）：

```powershell
$keys = 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\*',
        'HKLM:\SOFTWARE\WOW6432Node\Microsoft\Windows\CurrentVersion\Uninstall\*',
        'HKCU:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\*'
Get-ItemProperty $keys -ErrorAction SilentlyContinue |
  Where-Object { $_.DisplayName -and -not $_.SystemComponent } |
  Select-Object DisplayName, DisplayVersion, InstallLocation -Unique | Sort-Object DisplayName
```