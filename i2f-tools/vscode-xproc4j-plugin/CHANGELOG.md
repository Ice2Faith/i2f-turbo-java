# Changelog

## 0.1.0 - 2026-10-23

首个可安装版本，对齐 IntelliJ IDEA 插件标准版能力。

### 新增

- XProc4J XML 标签、属性与 feature 后缀高亮，支持 procedure 根节点识别。
- SQL / Java / JavaScript / JSON / YAML / Shell / CSS / Markdown / TypeScript 正文注入；`lang-string`、`lang-render` 支持 `_lang` 动态切换。
- 标签、属性、feature、枚举值、数据源/数据库、refid、变量与 SQL 关键字补全。
- 42 个内置变量与当前文件变量补全；`${}`、`$!{}`、`#{}`、`#!{}` 与短变量形式。
- 跨文件 refid 定义跳转、引用查找，支持未保存文档覆盖层与工作区索引。
- SQL 正文可选浅黄色、`lang-eval*` 正文可选浅绿色整行背景（`xproc4j.blockBackground.enabled`，默认开启），用于识别嵌入块类型与边界。
- XML 属性值（不含引号）可选更浅的绿色背景（`xproc4j.attributeValueBackground.enabled`，默认开启）。
- `lang-eval*` 正文中的 Java、JavaScript 与 SQL99 关键字可选 dodgerblue 高亮（`xproc4j.evalKeywordHighlight.enabled`，默认开启），用装饰强制前景色，不依赖主题 token 配色。
- `lang-eval*` 正文中的数值字面量（整数/小数）显示蓝色、字符串字面量（单/双引号，含引号）显示绿色（`xproc4j.evalLiteralHighlight.enabled`，默认开启），颜色随 Light/Dark 主题选明度，与浅绿整行背景、dodgerblue 关键字保持区分；字符串内部的关键字与数字不再二次着色。
- 变量高亮默认覆盖 `xml`、`yaml`、`yml` 与 `java`：XML 保持 procedure 根节点与已知标签/属性门控，其余后缀按原始文本扫描 `${x}`、`$!{x}`、`#{x}`、`#!{x}` 与 `$x`；后缀列表由 `xproc4j.variableHighlight.extensions` 配置，空数组关闭。
- 文档大纲、面包屑与折叠范围。
- 90 个由 IDEA Live Template 转换的代码片段，保持 `<tag` 前缀。
- 「新建 Procedure 文件」命令，包含 id 校验、覆盖确认与模板渲染。
- 中文配置项、输出通道、索引状态栏与重建命令。

### 已知限制

- 不提供 XML/DTD 校验与诊断。
- 不支持 TinyScript、Funic、Funvi、OGNL 的语法与补全。
- 不支持标签到 Java 类的跳转。
- 第三方语言注入取决于本机是否安装对应语法扩展。
- 多行开始标签的正文注入可能降级，变量仍由语义 token 着色。
- 非 XML 文件的变量高亮为纯文本扫描，字符串与注释中的变量也会被着色。
- `lang-eval*` 关键字/数值/字符串按正则语义扫描，注释中的匹配也会着色；未闭合的字符串引号会跨行吞到下一个同型引号。

### 许可

- 本项目使用 GPL-3.0-only；上游 i2f-turbo-java 署名与 vendored 文件清单见 NOTICE。
