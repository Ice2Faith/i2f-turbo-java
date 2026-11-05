# 探测时的通用坑与对策

> 这一节全部来自真实翻车经历。每一条都曾导致"看起来是环境问题，其实是探测方法问题"。

---

## 1. 编码类

### 1.1 诊断输出自己先乱码

非英文 Windows（代码页 936/932/949）下，中文诊断信息经管道/重定向常被错误解码，你会看到 `?`、`锟斤拷`、`U+FFFD`。

**对策**：所有探测脚本**只输出 ASCII**。必须展示中文时，先写文件再用显式编码读。

### 1.2 `Get-Content` 把 UTF-8 读成乱码

无 BOM 的 UTF-8 文件，PowerShell 5.1 的 `Get-Content` 默认按 ANSI（如 GBK）解码 → 中文全乱。

```powershell
# ❌ 可能乱码
Get-Content .\SKILL.md
# ✅ 正确
[System.IO.File]::ReadAllText($p, (New-Object System.Text.UTF8Encoding($false)))
```

**判定技巧**：如果"乱码"看起来是有规律的汉字（如"鏂囦欢"），那是 UTF-8 被当 GBK 读；如果是 `?`/方块，那是字符集映射丢字。

### 1.3 三方代码页可能互相矛盾

真实案例：`chcp` 报 936，但 `GetConsoleOutputCP()` 是 65001。谁都不算错——它们看的是不同层。

**对策**：三个都测，报告里并列写出；结论以"注册表 ACP"为**文件默认编码**依据，以"控制台 CP"为**终端显示**依据。

### 1.4 各工具输出编码不同

| 工具 | 编码 |
|---|---|
| `wmic` | **UTF-16LE + BOM** |
| `systeminfo` | **UTF-8 无 BOM** |
| 多数老 CLI | GBK/本地 ANSI |
| Node.js | UTF-8 恒定 |

**对策**：不要假设。要么重定向到文件后按多种编码试读，要么改用 CIM cmdlet（返回对象，无编码问题）。

### 1.5 用"终端显示"判断文件是否损坏 = 不可靠

模特例：文件内容完全正确，但终端显示是乱码。**验证编码只看字节**：

```powershell
$b = [System.IO.File]::ReadAllBytes($p)
($b | Select-Object -First 8 | ForEach-Object { '{0:X2}' -f $_ }) -join ' '
```

---

## 2. Shell 与语法类

### 2.1 PowerShell 版本差异

| 特性 | PS 5.1 | PS 7 |
|---|---|---|
| `??` / `?:` | ❌ | ✅ |
| `&&` / `\|\|` | ❌ | ✅ |
| `-Parallel` | ❌ | ✅ |
| `Get-Content -Encoding Byte` | ✅ | ❌（改 `-AsByteStream`） |

**对策**：先 `$PSVersionTable`，按 5.1 语法写（最保守）。

### 2.2 中文引号会打断解析

PS 字符串里出现中文引号 `"…"` 时，常被误认为字符串边界，报 `字符串缺少终止符`。

**对策**：字符串内只用 ASCII 引号；中文内容里的 `"` 用 `「」` 或直接去掉。

### 2.3 `$var:` 被当驱动器限定符

```powershell
"$PSDefaultParameterValues:"   # ❌ InvalidVariableReferenceWithDrive
"${PSDefaultParameterValues}:" # ✅
```

### 2.4 `%VAR%>` 在 cmd 里是重定向

```bat
echo X=%VAR%> file.txt      ❌ 变量值与重定向抢解析
echo X=%VAR% > file.txt     ✅ 加空格
```

### 2.5 执行策略拦截 `.ps1`

`npm`/`pnpm` 在 PS 里命中 `.ps1` 包装器会报"禁止运行脚本"。

**对策**：用 `.cmd` 版本，或 `powershell -ExecutionPolicy Bypass -File x.ps1`；**不要**为此改系统策略。

### 2.6 `2>&1` 的语义不同

cmd 与 PS 的重定向行为不同；把 `2>&1` 拼进复杂管道容易把错误信息吞掉或重复。
**对策**：先独立跑一次看原始输出，再写管道。

---

## 3. 环境变量类

### 3.1 改了注册表 ≠ 生效

子进程**继承父进程的环境块**，不读注册表。

**表现**：用户改了系统变量，你测出来还是旧值，双方都以为是"没生效"。

**对策**：

1. 读注册表确认"持久化值"已改
2. 检查当前进程的值（可能是旧的）
3. 判断进程链：新终端是否由**刷新过环境**的父进程启动
4. 必要时注销/重登或重启 `explorer.exe`
5. 用**真正脱离当前进程树**的方式验证（如计划任务、`Start-Process` 新会话）

### 3.2 用户级 vs 机器级

同名变量可能同时存在用户级与机器级，**用户级优先**。两处都要查。

### 3.3 PATH 里可能有死链

PATH 中不存在的目录不报错，只是被忽略。列表里 `[exists=False]` 的项值得在报告里提一句（清理建议）。

---

## 4. 提权类

### 4.1 "不是管理员"≠"不能提权"

即使当前进程 `IsElevated=False`，只要账户在 Administrators 组，且 `ConsentPromptBehaviorAdmin=0`（不弹窗），
`Start-Process -Verb RunAs` 可以**静默拿到管理员**。

**对策**：先测能力，别急着说"我做不到"；但要做系统改动前**先说明**。

### 4.2 UAC 模式的副作用

`ConsentPromptBehaviorAdmin=0` 意味着本机任何程序都能静默提权 —— 这是**安全风险**，值得主动告知用户。

### 4.3 提权进程的输出要落文件

提权子进程的 stdout 不一定能回传。
**对策**：让它把结果重定向到临时文件，父进程再读。

---

## 5. 探测方法类

### 5.1 全盘递归会跑很久

在机械盘上 `Get-ChildItem -Recurse` 统计体积可能几十秒到几分钟，还会被当成长任务。

**对策**：
- 目录体积用**浅层**统计（`-Depth 2`）或直接跳过
- 找特定文件用 `-Filter` + `-Depth` 限制
- 优先查注册表/已知路径，而不是全盘扫描
- 命令超时就**取消**改轻量方案，不要硬等

### 5.2 `where` 找不到 ≠ 没装

开发者常把工具装在非 PATH 目录（`D:\maven`、`E:\NodeJs`）。

**对策**：走 L1→L2→L3 三层；L3 扫常见安装根 + 注册表 Uninstall。

### 5.3 注册表里"装了"≠"能用"

例：WDK 在注册表有条目，但 `Include\...\km\wdm.h` 根本不存在（装了一半）。
例：`Python314` 目录存在但**没有 `python.exe`**。

**对策**：每个关键组件都要验证"标志性文件"存在，并跑一次最小样例。

### 5.4 只看 `--version` 会误判

`--version` 只证明"二进制在"，不证明"能编译运行"。
**对策**：每栈跑一次 hello world（编译 + 执行 + 看输出）。

### 5.5 索引混淆（GPU/CUDA 高发）

- PCI 总线号 ≠ CUDA 设备索引
- CUDA 只给 **NVIDIA** 卡从 0 顺排编号
- 核显存在不代表有第 2 个 CUDA 设备
**对策**：报告里显式给出"物理卡数"vs"CUDA 设备数"vs"合法索引集合"。

### 5.6 冷启动降频

新启动的 GPU 常处于低功耗态，首测性能可能只有真实值的 1/3。
**对策**：预热 3 轮以上再计时；报告里注明"已预热"。

---

## 6. 落盘类

### 6.1 别往用户目录塞文件

用户有自己的归档习惯。**只写** `<盘>:\01-ai-workspace\` 下的约定子目录。

### 6.2 别写小容量盘

先算可用空间。可用 < 10GB 的盘标"禁止写"，写进报告硬约束。

### 6.3 临时文件必须清

`temp\` 用完删；编译产物打到 `%TEMP%`，不要留在技能或工作区目录。

### 6.4 路径避免中文与空格

`Program Files (x86)`、中文目录名会让命令行工具链出错。
**对策**：新目录用小写英文 + 短横线；调用时一律加引号。

---

## 7. 汇报类

### 7.1 区分"实测"与"推断"

报告里每条结论标注来源。推断的要写明"（推断）"，否则后人会当成事实。

### 7.2 别只给版本，要给"怎么用"

用户真正需要的是：**这条命令怎么调、有什么限制、没有它时用什么替代**。

### 7.3 报告要能独立阅读

不要写成"刚才那个命令的输出如下"——要把关键数据抄进报告，让它脱离对话也能看懂。
---

## 8. 子进程与脚本执行类（写探测脚本必踩）

### 8.1 无参调用解释器会永久阻塞

`python` / `node` / `irb` / `psql` 等在**没有参数**时会进入交互式 REPL，一直等 stdin。

**翻车现场**：探测脚本里写 `& $exe $args`，因参数绑定异常导致 `$args` 为空 → `python` 进 REPL → 整个探测挂死，CPU 0%、磁盘 0%，看起来像"卡住了"。

**三重防护**（缺一不可）：

```powershell
# 1) 参数名不要用 $args（它是自动变量，命名易出诡异问题）
function Probe { param([string]$Exe, [string[]]$ArgList, [int]$TimeoutSec = 15)
    # 2) 明确拒绝无参调用
    if (-not $ArgList -or $ArgList.Count -eq 0) { return '<skipped: no probe args>' }
    $psi = New-Object System.Diagnostics.ProcessStartInfo
    $psi.FileName = $Exe
    $psi.Arguments = ($ArgList -join ' ')
    $psi.RedirectStandardInput = $true      # 关键
    $psi.UseShellExecute = $false
    ...
    $p = [System.Diagnostics.Process]::Start($psi)
    $p.StandardInput.Close()                # 3) 关掉 stdin，REPL 立即退出
    if (-not $p.WaitForExit($TimeoutSec * 1000)) { $p.Kill(); return '<timeout>' }
}
```

实测验证：不关 stdin 时 `python` 4 秒内不退出；关掉后立即退出。

### 8.2 某些命令行工具会在 `/?` 上挂住

`csc.exe /?` 在重定向 stdin 的场景下可能不退出。

**对策**：能读文件版本就别执行它 —— `(Get-Item $exe).VersionInfo.FileVersion` 足够判断编译器年代。

### 8.3 进程内调用 .ps1 会被执行策略拦

当 `Get-ExecutionPolicy` 为 `Restricted`（`Undefined` 的默认后果）时，`& '.\x.ps1'` 直接报
`无法加载文件 ... 因为在此系统上禁止运行脚本`。

**对策**：始终用子进程 + 绕过策略：

```powershell
powershell -ExecutionPolicy Bypass -NoProfile -File x.ps1
```

### 8.4 用管道捕获子 PowerShell 可能死锁

`& powershell -File x.ps1 2>&1 | Select-String ...` 在本脚本又被别的宿主（job/计划任务）包裹时，
可能因句柄继承而永久等待。

**对策**：**重定向到文件**再读，并加硬超时：

```powershell
$p = Start-Process powershell.exe -ArgumentList @('-ExecutionPolicy','Bypass','-NoProfile','-File',$script) `
        -NoNewWindow -PassThru -RedirectStandardOutput $out -RedirectStandardError $err
if (-not $p.WaitForExit(150000)) { $p.Kill() }
$text = [System.IO.File]::ReadAllText($out, [System.Text.Encoding]::Default)
```

### 8.5 清理进程时把自己杀了

用 `CommandLine -match 'detect-'` 过滤进程时，**当前命令行本身也含该字符串** → 自杀，表现为命令 exit -1 且无输出。

**对策**：永远排除 `$_.ProcessId -ne $PID`。

### 8.6 目录名不能硬编码

`Microsoft Visual Studio` 实际可能是 `Microsoft Visual Studio 12.0` / `... 2022` / `BuildTools`。
写死目录名会漏检（本例中一度漏掉了真正的 MSVC）。

**对策**：用通配符 `Program Files*\Microsoft Visual Studio*`，并跨所有盘符扫描。

### 8.7 同名命令被别的工具链遮蔽

`link.exe` 在 Git for Windows 里也有（Unix `link` 工具），会在 PATH 中遮蔽 MSVC 的 `link.exe`。
同理 `sort`、`find`、`tar`、`sort.exe` 等。

**对策**：报告里除"命令是否存在"外，还要记**解析到的完整路径**；MSVC 工具链应在 `vcvarsall` 激活后的会话中使用。

---

## 9. 探测脚本设计清单（自检）

- [ ] 每个外部命令都有**显式参数**
- [ ] 每个外部命令都**关闭 stdin**
- [ ] 每个外部命令都有**超时**
- [ ] 任一组件缺失只记 `<not found>`，不中断
- [ ] 脚本自身源码是 **ASCII**（非英文系统下 PS 5.1 会按 ANSI 误读 UTF-8 源文件）
- [ ] 输出是 ASCII
- [ ] 不假设盘符、不硬编码目录名，用通配符 + 跨盘扫描
- [ ] 不写 C:/D:/用户既有目录，只写 `<盘>:\01-ai-workspace\`
- [ ] 编译/中间产物打到 `%TEMP%`，结束清理
- [ ] 有 `-OutDir` 之类参数可把结果落盘成报告
### 8.8 子进程写 UTF-8、父进程按 ANSI 读 -> 假乱码

多阶段探测里，子脚本输出被重定向到文件后由父脚本读取。若子脚本 stdout 是 UTF-8（或 UTF-8 控制台），
而父脚本用 `[Text.Encoding]::Default`（936/1252）去读，中文会变成 `64 浣?` 这类假乱码 —— 数据没错，读取方式错了。

**对策（两端都要管）**：

```powershell
# 子脚本（写侧）：固定 stdout 为 UTF-8
try { [Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false } catch {}

# 父脚本（读侧）：UTF-8 严格优先，失败再退回 ANSI
$text = $null
try {
    $strict = New-Object System.Text.UTF8Encoding($false, $true)   # throwOnInvalidBytes
    $text = [System.IO.File]::ReadAllText($f, $strict)
} catch {
    $text = [System.IO.File]::ReadAllText($f, [System.Text.Encoding]::Default)
}
```

**验证乱码的正则要写对**：`锛?` 里的 `?` 会把它变成可选，结果匹配所有行（自己造出 399 行"乱码"）。
应写成明确的字符类：`[\uFFFD]|锟斤拷|[鎬浣鏂]`。

### 8.9 跨平台脚本的换行符

给 Unix 用的 `.sh` 必须是 **LF**，CRLF 会让 `sh` 报 `bad interpreter` / 命令名带 `\r`。
Windows 上生成后要显式转换并校验（统计 `0D 0A` 出现次数应为 0）。
`git config core.autocrlf` 为 `true` 时，检出 `.sh` 也可能被转成 CRLF —— 必要时加 `.gitattributes`。