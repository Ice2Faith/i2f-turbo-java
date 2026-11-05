# 字符集与编码（本机乱码防治）

## 实测基线（2026-11-05）

| 项 | 值 |
|---|---|
| 系统 ACP（ANSI） | **936** |
| 系统 OEMCP | **936** |
| .NET `Encoding.Default` | **gb2312 (936)** |
| Global UTF-8 beta（ACP=65001） | ❌ 未开启 |
| `CurrentCulture` / `UICulture` | `zh-CN` / **`en-US`**（不一致） |
| 时区 | `China Standard Time` |
| `chcp`（cmd 报告） | **936** |
| 控制台实际 `GetConsoleOutputCP()` | **65001** ⚠️ 与 chcp 矛盾 |

**乱码总根源**：系统默认 GBK(936)，但会话输出代码页是 UTF-8(65001)，而 `cmd.exe` 又按 936 报告。

## 三种根因

| 根因 | 现象 | 证据 | 解法 |
|---|---|---|---|
| ① 默认编码 GBK | 文件被 UTF-8 读取器读成乱码 | Python `open()` = **cp936** | 显式 `encoding='utf-8'` |
| ② 控制台 CP 不一致 | 中文在管道/重定向变 `U+FFFD` | `chcp`=936 但 `GetConsoleOutputCP`=65001 | 别信终端，写文件+显式读 |
| ③ 各工具输出编码不同 | wmic 全问号、systeminfo 正常 | 见下表 | 按工具分别处理 |

## 工具输出编码

| 命令 | 编码 | 字节证据 |
|---|---|---|
| `wmic` | **UTF-16LE + BOM** | `FF FE 0D 00 0A 00 43 00…` |
| `systeminfo` | **UTF-8 无 BOM** | `0D 0A 48 6F 73 74 20 4E 61 6D 65 3A` |
| 老 CLI（ipconfig/netstat/sc） | **GBK** | `D6 D0 CE C4` 风格 |
| Node.js | UTF-8 恒定 | `中文` → `e4 b8 ad e6 96 87` |
| PowerShell 原生 | UTF-16（宿主转码） | — |

## 经典对照

```
UTF-8 "中文" = E4 B8 AD E6 96 87   (6 bytes)
GBK   "中文" = D6 D0 CE C4         (4 bytes)
GBK 按 UTF-8 解 → U+FFFD × N（替换字符）
UTF-8 按 GBK 解 → "涓枃" 类怪字（"锟斤拷"同源）
```

## 逐语言

### Python 3.10.6（stdout=**gbk**，getpreferredencoding=**cp936**，utf8_mode=**0**）

```python
open(path, "r", encoding="utf-8", newline="")   # csv 必须 newline=""
open(path, "w", encoding="utf-8-sig")           # 要给 Excel 看才加 BOM
sys.stdout.reconfigure(encoding="utf-8")        # 3.7+
subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")
# 源码：Python 3 默认 UTF-8，无需 PEP 263；仅历史 GBK 源码才用
# -*- coding: gbk -*-
# 环境变量：PYTHONUTF8=1 / PYTHONIOENCODING=utf-8
```

⚠️ `utf-8-sig` 的 BOM 会让 `json.load` 报 `Unexpected UTF-8 BOM`。配置/接口数据**绝不要 BOM**。

### Java 17.0.11（file.encoding=**GBK**，native.encoding=**GBK**，sun.jnu.encoding=**GBK**）

```powershell
java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 App
javac -encoding UTF-8 -d out src\*.java
mvn -Dfile.encoding=UTF-8 -Dproject.build.sourceEncoding=UTF-8 clean package
$env:JAVA_TOOL_OPTIONS = "-Dfile.encoding=UTF-8"
```
代码里用 `Files.readString(p, StandardCharsets.UTF_8)`。**JDK18+ 起默认 UTF-8（JEP 400），但本机是 17，仍需手动指定。**

### Node 24（自身恒 UTF-8）

```js
fs.readFileSync(p, "utf8"); fs.writeFileSync(p, s, "utf8");
fs.writeFileSync(p, "\uFEFF" + s, "utf8");   // 手动加 BOM，Node 不自动加
```
Node 写出的永远是 UTF-8；在 936 控制台显示乱码 ≠ 文件有问题。

### C# / .NET FW 4.8（Encoding.Default = gb2312）

```csharp
Console.OutputEncoding = new UTF8Encoding(false);
File.ReadAllText(p, new UTF8Encoding(false));            // 别用无参（走 936）
new StreamWriter(p, false, new UTF8Encoding(false));     // 第三参决定 BOM
```

### Go 1.23

```go
kernel32 := syscall.NewLazyDLL("kernel32.dll")
kernel32.NewProc("SetConsoleOutputCP").Call(65001)
```

### MSVC 18.0 / VS2013

```bat
cl /utf-8 /EHsc a.cpp
cl /source-charset:utf-8 /execution-charset:utf-8 a.cpp
```
VS2013 对 `/utf-8` 支持有限 → **源码存 UTF-8 with BOM** 最稳。
MinGW GCC 5.1：`-finput-charset=UTF-8 -fexec-charset=UTF-8`。

### PowerShell 5.1（`$OutputEncoding` = **us-ascii / CP20127**）

```powershell
[Console]::OutputEncoding = [Text.Encoding]::UTF8
[Console]::InputEncoding  = [Text.Encoding]::UTF8
$OutputEncoding = [Text.Encoding]::UTF8      # 否则管道给原生程序时中文静默丢失

Set-Content x.txt -Value $s -Encoding UTF8                                   # PS5.1 会带 BOM
[IO.File]::WriteAllText($p, $s, (New-Object Text.UTF8Encoding $false))       # 无 BOM
```

### Git（core.autocrlf=true，其余未设）

```powershell
git config --global i18n.commitEncoding utf-8
git config --global i18n.logOutputEncoding utf-8
git config --global core.quotepath false     # 否则中文路径显示 \346\226\207
git config --global core.autocrlf input
```

### 批处理

```bat
@echo off
chcp 65001 >nul
```

## BOM 决策表

| 目标 | BOM |
|---|---|
| CSV 给 Excel 打开 | ✅ 要（`utf-8-sig`） |
| `.ps1` 含中文（PS 5.1 读） | ✅ 要，否则按 ANSI 误读 |
| MSVC 源码含中文 | ✅ 建议（替代 `/utf-8`） |
| JSON / TOML / YAML / 配置文件 | ❌ 绝不要 |
| Python / JS / Go 源码 | ❌ 不需要 |
| 日志、数据交换 | ❌ 不要 |

## 铁律

1. 不用终端显示判断编码 —— 写文件后按显式编码读，或 hex 比对。
2. 任何落盘文本显式指定编码（默认 cp936 几乎总是错的）。
3. 脚本输出的诊断信息用 **ASCII**（技能内所有脚本都如此）。
4. 子进程输出编码不可假设（wmic=UTF-16LE、systeminfo=UTF-8、老工具=GBK）。

## 全局 UTF-8（Beta）要不要开？

开启 = `HKLM\...\Nls\CodePage\ACP` 改 `65001` + 重启。
**建议：保持 936**。开启后大量硬编码 936 的老国产软件/游戏/驱动安装器会乱码或崩溃，回滚需重启且不保证完全恢复。项目里显式指定 UTF-8 更可控。