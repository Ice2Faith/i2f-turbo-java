# S5 用户身份判定与协作方式

## 为什么重要

同一句话对不同人有完全不同的效果：

- 对**普通人**说"打开终端执行 `Set-ExecutionPolicy`"，他可能直接把系统搞坏；
- 对**程序员**说"你自己点一下安装"是浪费他的时间——他更希望你把命令准备好。

判定身份不是给人贴标签，而是选择**协作档位**。

---

## 一、判定信号（按可信度排序）

### 信号 1：已装软件画像（最强，来自注册表 Uninstall）

```powershell
$keys = 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\*',
        'HKLM:\SOFTWARE\WOW6432Node\Microsoft\Windows\CurrentVersion\Uninstall\*',
        'HKCU:\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\*'
Get-ItemProperty $keys -EA 0 | Where-Object { $_.DisplayName -and -not $_.SystemComponent } |
  Select-Object -ExpandProperty DisplayName -Unique | Sort-Object
```

| 出现这些 | 指向 |
|---|---|
| VS / VS Code / IDEA / PyCharm / JetBrains / Docker / WSL / Git / Postman / SSMS / Navicat / Xshell / MobaXterm | **程序员** |
| Office / WPS / 钉钉 / 企业微信 / 飞书 / Foxmail / 网易邮箱大师 / 百度网盘 | **职场人** |
| 仅浏览器 / 影音 / 游戏 / 输入法 / 安全软件 / 微信 QQ | **普通人** |

> 混合很常见（程序员也装微信）。**取"上限"，但用其它信号交叉验证。**

### 信号 2：开发痕迹（配置文件）

```powershell
Test-Path "$env:USERPROFILE\.gitconfig"                 # 有 → 强烈指向程序员
Test-Path "$env:USERPROFILE\.m2"                        # Maven
Test-Path "$env:USERPROFILE\.gradle"                    # Gradle
Test-Path "$env:USERPROFILE\go"                         # Go
Test-Path "$env:USERPROFILE\.conda"                     # Conda
Test-Path "$env:USERPROFILE\.dsh"                       # 用 AI 编码代理
Test-Path "$env:USERPROFILE\Documents\PowerShell"       # PS profile
$PROFILE; Test-Path $PROFILE                            # 自定义 shell profile = 强信号
```

### 信号 3：PATH 的"手工程度"

```powershell
($env:PATH -split ';' | Where-Object $_).Count          # 数量
$env:PATH -split ';' | Where-Object { $_ -notmatch 'Windows|Program Files' }
```

- PATH 很长、含自建目录（如 `D:\cmd`、`C:\CMDTOOLS`）、含多版本工具 → **程序员**
- PATH 基本是系统默认 → 其它身份

### 信号 4：管理员与提权习惯

```powershell
$id = [Security.Principal.WindowsIdentity]::GetCurrent()
([Security.Principal.WindowsPrincipal]$id).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
net localgroup Administrators
(Get-ItemProperty 'HKLM:\SOFTWARE\Microsoft\Windows\CurrentVersion\Policies\System').ConsentPromptBehaviorAdmin
```

`ConsentPromptBehaviorAdmin` 的含义：

| 值 | 含义 | 对协作的影响 |
|---|---|---|
| `0` | **提权不弹窗** | 你可以静默提权；但要主动说明，且提醒用户"任何程序都能静默提权"的安全风险 |
| `5` | 默认：提示同意 | 每次提权会弹 UAC，需要用户点 |
| `2` | 提示凭据 | 需要输入管理员密码，通常得用户来 |

### 信号 5：文件系统习惯（佐证）

- 盘根是否自建了分类目录（`01-xxx`、`workspace`、`projects`）
- 桌面/文档是否被重定向到其它盘
- 是否有大量版本号命名的工具目录（`jdk-17`、`node-v20`）

---

## 二、三档协作方式

### A. 普通人（Persona: general-user）

**原则：你多做，他少动。**

| 维度 | 做法 |
|---|---|
| 语言 | 不用术语；用"程序""文件夹"，不用"PATH""环境变量" |
| 命令 | **不要让他敲命令**。你代跑，把结果用文字/图片告诉他 |
| 交付 | 直接给可用成果（文件、图片、改好的设置），并说明"在哪里" |
| 改动 | 任何系统改动前解释"会发生什么"，并给恢复办法 |
| 出错 | 不要说堆栈；说"这一步没成功，我换个办法" |

### B. 职场人（Persona: office-worker）

**原则：结果优先，别折腾。**

| 维度 | 做法 |
|---|---|
| 语言 | 结果导向："已经转成 PDF 放在桌面" |
| 命令 | 少量、单步、可复制；不用一次贴一大段 |
| 工具选择 | 优先用**已装好的**（Office/WPS/LibreOffice），不主动推荐装新东西 |
| 打断 | 尽量少问；能在既有工具内解决就别引入新依赖 |
| 交付 | 文件命名清晰、放固定位置（如 `01-ai-workspace\documents\`） |

### C. 程序员（Persona: developer）

**原则：可协作、可讨论、可提权。**

| 维度 | 做法 |
|---|---|
| 语言 | 可以直接用术语；给版本号、路径、报错原文 |
| 命令 | 给**可直接粘贴的完整命令**，标明 shell 与是否需提权，说明预期输出 |
| 分工 | 你可以做管理员操作（先说明），GUI 安装器/登录凭据交给他 |
| 决策 | 给方案 + 权衡，让他拍板（如"装 JDK21 还是 17"） |
| 验证 | 做完自己复核，把证据贴出来，不依赖他转述 |
| 尊重 | 不要替他做**破坏性**决定（删数据、改分区、卸软件） |

---

## 三、判不准时怎么办

1. **默认按"程序员"的技术水平、按"普通人"的谨慎度**处理 —— 能力按高的给，风险按低的控。
2. **用一次小交互验证**：给一条无害命令（如 `git --version`），看他是"直接粘贴"还是"问这是什么"。
3. **显式询问**：一句"你是开发者吗？我可以直接给你命令还是替你执行？"成本极低、收益很高。
4. **允许身份随任务变化**：同一台机器上，做文档转换时按职场人、做环境排查时按程序员。

---

## 四、输出前自检

- [ ] 我给的命令，他能不能直接粘？（shell 对不对、路径有没有空格、要不要管理员）
- [ ] 我有没有把"需要他决策"和"我能直接做"混在一起？
- [ ] 涉及系统改动时，我有没有先说后果？
- [ ] 交付物有没有明确落盘位置？
- [ ] 有没有用到他没装的东西？