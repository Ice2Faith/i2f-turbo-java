# XProc4J for VS Code

将 `i2f-turbo-java` 中 `i2f-jdbc-procedure-idea-plugin` 的 IntelliJ IDEA 插件能力迁移到 VS Code：
为根节点为 `<procedure>` 的 XProc4J XML 提供语法高亮、语言注入、智能补全、refid 导航、大纲/折叠与代码片段。

- 扩展标识：`ice2faith.xproc4j`
- 最低 VS Code：`1.90.0`
- 当前版本：`0.1.0`
- 许可证：GPL-3.0-only

## 功能

| 能力 | 说明 |
|---|---|
| 语法高亮 | 识别 111 个 XProc4J 标签、89 个属性基础名与 58 个 feature 后缀；普通 XML 不受影响。 |
| 语言注入 | SQL、Java、JavaScript、JSON、YAML、Shell、CSS、Markdown、TypeScript 等内置语法直接复用；`_lang` 可切换 `lang-string` / `lang-render` 正文。 |
| 嵌入块背景 | SQL 正文显示浅黄色、`lang-eval*` 正文显示浅绿色整行背景，起止标签行包含在内；可在设置中关闭。 |
| 属性值背景 | XML 属性值（`a="b"` 中的 `b`，不含引号）显示更浅的绿色背景；可单独关闭。 |
| 关键字高亮 | `lang-eval*` 正文中的 Java、JavaScript 与 SQL99 关键字显示为 dodgerblue（`#1E90FF`），便于区分语句结构；可单独关闭。 |
| 字面量高亮 | `lang-eval*` 正文中的数值字面量显示为蓝色、字符串字面量显示为绿色（颜色随 Light/Dark 主题变化，与块背景保持区分）；可单独关闭。 |
| 变量高亮 | `${name}`、`$!{name}`、`#{name}`、`#!{name}` 与 `$name`；XML 默认要求 procedure 根节点，yaml/yml/java 默认对原始文本高亮，后缀列表可配置。 |
| 补全 | 标签名、属性名、`属性.feature`、枚举值、数据源/数据库、refid、内置变量、SQL 关键字。 |
| 大纲与折叠 | 按 procedure / script-segment / 语言标签生成文档符号与折叠区域。 |
| refid 导航 | 跨文件定义跳转、引用查找；打开文件实时覆盖磁盘索引。 |
| 代码片段 | 由上游 90 个 IDEA Live Template 转换，保持 `<tag` 前缀。 |
| 新建文件 | 在资源管理器目录右键或命令面板创建带 `id`、作者与日期的 procedure 骨架。 |

变量高亮与补全支持 `${name}`、`$!{name}`、`#{name}`、`#!{name}` 与短变量 `$name`。
未安装对应语法扩展时，Groovy、VTL、FreeMarker、Thymeleaf、SpEL 等正文会降级为普通文本，不影响 XML 结构高亮。

## 安装

从 VSIX 安装：

```bash
code --install-extension dist/vscode-xproc4j-0.1.0.vsix
```

或在 VS Code 中执行「Extensions: Install from VSIX...」并选择该文件。

扩展只在 XML 根元素为 `procedure` 时启用语义补全、导航、大纲与语义 token；打开 `pom.xml`、Spring、MyBatis Mapper 等普通 XML 不会受到这些能力干扰。原生代码片段的作用域是 `xml`，因此在任意 XML 的 `<` 建议列表中都可能出现；不会自动插入，可在 VS Code 的片段设置中禁用。

## 使用

### 补全

在 procedure 文档中输入：

```xml
<procedure id="demo">
    <sql-update datasource="main" result="rows">
        select * from sys_user where id = ${id}
    </sql-update>
    <procedure-call refid="otherProcedure"/>
</procedure>
```

- `<` 后输入标签前缀，获得标签候选；未知标签只提供兜底属性。
- 标签内输入空格或属性前缀，获得属性与 feature 候选；已出现的属性会自动排除。
- 属性值内获得枚举、数据源、refid 等候选；`databases="mysql,ora"` 只替换当前段。
- SQL 正文内获得 SQL 关键字与变量候选。
- 代码片段前缀形如 `<sql-update`，选中后按 Tab 展开。

### 导航

- 在 `refid="..."` 的值上执行「转到定义」，跳转到目标 procedure 或 script-segment。
- 在 `id="..."` 上执行「查找所有引用」，列出工作区内所有 refid 调用。
- 大纲视图与面包屑展示 procedure、script-segment 与主要语言节点。

### 新建 Procedure 文件

在资源管理器中对目标文件夹右键选择「XProc4J: 新建 Procedure 文件」，或从命令面板执行。
需要输入 procedure id；文件已存在时会二次确认覆盖。命令会生成：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE procedure SYSTEM "procedure.dtd">

<procedure id="your-id">
    <!--

    @author ...
    @date ...
    @time ...
    -->

</procedure>
```

### 索引

工程索引用于跨文件 refid 补全与导航，默认在工作区打开后按需构建：

- 默认扫描 `**/*.xml`，排除 `node_modules`、`.git`、`target`、`build`、`out`、`dist`、`logs`。
- 只索引根节点为 procedure 的文件；跳过二进制文件与超过大小上限的文件。
- 打开文档的未保存内容以覆盖层优先，关闭后恢复磁盘内容。
- 可执行「XProc4J: 重建工程索引」手动刷新，进度与统计显示在通知和输出通道。

## 配置

| 设置 | 默认值 | 说明 |
|---|---|---|
| `xproc4j.enable` | `true` | 启用语义功能；TextMate 注入语法不受此开关控制。 |
| `xproc4j.index.enabled` | `true` | 启用工作区索引。 |
| `xproc4j.projectScan.include` | `["**/*.xml"]` | 参与索引的文件匹配。 |
| `xproc4j.projectScan.exclude` | 常见构建目录 | 索引排除项。 |
| `xproc4j.projectScan.maxFiles` | `20000` | 索引文件数量上限（1~100000）。 |
| `xproc4j.completion.enabled` | `true` | 启用补全。 |
| `xproc4j.completion.sqlKeywords` | `true` | SQL 区域补全关键字。 |
| `xproc4j.completion.builtinVariables` | `true` | 补全 42 个框架内置变量。 |
| `xproc4j.refid.enabled` | `true` | refid 定义跳转、引用查找与 refid 语义着色。 |
| `xproc4j.symbols.enabled` | `true` | 大纲与面包屑。 |
| `xproc4j.folding.enabled` | `true` | 折叠范围。 |
| `xproc4j.blockBackground.enabled` | `true` | SQL 浅黄色、`lang-eval*` 浅绿色的整行背景。 |
| `xproc4j.attributeValueBackground.enabled` | `true` | XML 属性值的更浅绿色背景（不含引号）。 |
| `xproc4j.evalKeywordHighlight.enabled` | `true` | `lang-eval*` 正文关键字的 dodgerblue 前景色。 |
| `xproc4j.evalLiteralHighlight.enabled` | `true` | `lang-eval*` 正文数值蓝色、字符串绿色的前景色（Light `#1565C0`/`#2E7D32`，Dark `#90CAF9`/`#81C784`）。 |
| `xproc4j.variableHighlight.extensions` | `["xml","yaml","yml","java"]` | 启用变量高亮的文件后缀；支持 `xml`、`.xml`、`*.xml` 写法，空数组关闭。 |
| `xproc4j.maxDocumentSizeKB` | `2048` | 超过大小的文档跳过语义分析；语法高亮仍生效。 |
| `xproc4j.trace.server` | `off` | 预留日志级别；本扩展不使用 LSP。 |

## 命令

| 命令 | 说明 |
|---|---|
| `XProc4J: 新建 Procedure 文件` | 在选定目录创建 procedure XML。 |
| `XProc4J: 重建工程索引` | 重建跨文件索引。 |
| `XProc4J: 显示输出` | 打开 XProc4J 输出通道。 |

## 已知限制

- 不提供 XML 校验、DTD 校验或错误诊断。
- 不实现 TinyScript、Funic、Funvi、OGNL 自研 DSL 的语法/补全；这些正文只保留变量高亮。
- 不支持标签到 Java 类的跳转。
- 只有 XML 语言 ID 与 `procedure` 根节点会启用语义补全、导航、大纲与语义 token；其他根节点不受这些能力影响。
- 原生代码片段的作用域为 `xml`，在 POM/Spring/MyBatis 等普通 XML 的 `<` 建议中也可能出现；可由 VS Code 片段设置关闭，不会自动插入。
- 第三方语言语法（Groovy、VTL、FreeMarker、Thymeleaf、SpEL 等）是否生效取决于本机已安装的扩展及其 scope 名。
- 开始标签跨多行书写时，TextMate 注入可能降级；正文变量仍由语义 token 兜底。
- 补全从光标向前最多回溯 4096 个字符；超长开始标签可能无法定位补全上下文。
- 虚拟工作区（virtual workspace）中 `workspace.findFiles` 可能不可用，跨文件索引能力受限。
- 非 XML 文件的变量高亮为纯文本扫描，字符串与注释中的变量也会着色；可通过 `xproc4j.variableHighlight.extensions` 限定后缀或设为空数组关闭。
- `lang-eval*` 关键字/数值/字符串高亮按正则语义扫描，注释中的关键字与字面量也会着色；字符串内部的关键字与数字不会二次着色。字符串按 `'[^']*'` / `"[^"]*"` 匹配，未闭合引号会跨行吞到下一个同型引号。
- `xproc4j.trace.server` 仅为兼容预留，不产生 LSP 日志。

## 隐私

扩展只读取工作区内的 XML 文件用于本地索引，不联网、不执行文档内容。
输出通道只记录数量、耗时、错误类型等运行信息，不记录文档正文、属性值、变量值、SQL 文本或凭据。

## 开发

环境要求：Node.js 20+、npm、VS Code 1.90+。

```bash
npm install
npm run gen          # 生成元数据、TextMate 语法、片段与模板模块
npm run check        # typecheck + lint + 单元测试 + 语法测试 + 生成物校验
npm run build        # esbuild 打包 dist/extension.js
npm run test:integration  # 启动本机 VS Code Extension Host 跑集成测试
npm run package      # 生成 + 检查 + 构建 + 打包 dist/vscode-xproc4j-0.1.0.vsix
npm run source:check # 校验 tools/sources 的 SHA-256 清单
```

`tools/sources/` 中的上游文件发生变化时，先确认版本，再执行 `npm run source:update` 更新清单；`gen:metadata` 会自动校验，不一致即失败。

目录分层：

```
src/domain/          纯领域逻辑（扫描、补全、索引、符号、模板），零 vscode 依赖
src/adapters/        VS Code Provider、命令、文档门控
src/infrastructure/  索引服务、glob、文件监听等宿主能力
src/core/            激活装配、配置、日志、状态栏、系统信息
src/generated/       构建期生成的元数据与模板（勿手改）
tools/               元数据/语法/片段生成器与 vendored 上游源文件
syntaxes/            生成的 TextMate 注入语法
snippets/            生成的 VS Code 代码片段
test/                unit / grammar / integration / fixtures
docs/                需求、概要/详细设计、测试与维护文档
```

修改元数据或语法后必须重新执行 `npm run gen`；`npm run gen:check` 会在不写文件的前提下校验生成物是否最新。
vendored 上游文件与生成物署名见仓库根目录的 `NOTICE` 文件。

## 许可证

本项目以 GPL-3.0-only 分发，完整文本见仓库根目录的 `LICENSE` 文件。
上游 `i2f-turbo-java` 的署名、vendored 文件清单与第三方测试语法说明见 `NOTICE` 文件。
