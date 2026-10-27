# 附录 A：参考实现分析（IDEA 插件能力盘点）

> 参考工程：`D:\IDEA_ROOT\DevCenter\i2f-turbo\i2f-turbo-java\i2f-tools\i2f-jdbc-procedure-idea-plugin`
> 框架工程：`D:\IDEA_ROOT\DevCenter\i2f-turbo\i2f-turbo-java\i2f-jdk\i2f-jdbc-procedure`
> 记录日期：2026-10-23

## A.1 插件注册信息（plugin.xml）

| 扩展点 | 实现类 | 作用 | 本次是否迁移 |
|---|---|---|---|
| `psi.referenceContributor` (XML) | `XmlTagReferenceContributor` | XML 标签名 → Java 节点实现类跳转 | 计划迁移（P1） |
| `psi.referenceContributor` (XML) | `XmlIdRefReferenceContributor` | `refid` → `id` 跳转、`id` 反向引用 | 计划迁移（P1） |
| `editorFactoryListener` | `DollarVariablesEditorFactoryListener` | 编辑器级 `$` 变量高亮 | 计划迁移（P0） |
| `fileTemplateGroup` | `XProc4jFileTemplateGroupDescriptorFactory` | 新建 XProc4J 文件模板 | 计划迁移（P1） |
| `multiHostInjector` | `JdbcProcedureXmlLangInjectInjector` | 标签正文/属性值语言注入 | 计划迁移（P0，受 VS Code 能力限制） |
| `completion.contributor` (XML/TinyScript/Funic/SQL) | `JdbcProcedureXmlCompletionContributor` | XML 属性名/值、refid、参数、变量补全 | 计划迁移（P1，仅 XML 部分） |
| `completion.contributor` (any) | `AnyHelpXmlCompletionContributor` | SQL 关键字、变量、函数、SQL 标识符等通用补全 | 计划迁移（P1，仅 XML/注入区域） |
| `defaultLiveTemplatesProvider` | `XProc4jLiveTemplatesProvider` | XProc4j 实时模板组 | 计划迁移（P1，改为 Snippets） |
| `dom.fileMetaData` | `JdbcProcedureDomFileDescription` | 识别根标签 `procedure` | 计划迁移（P0，改为扫描识别） |
| `codeInsight.lineMarkerProvider` | `JdbcProcedureRefidLineMarkerProvider` | refid 行号区导航图标 | 计划迁移（P2，改为 CodeLens/DocumentLink） |
| `multiHostInjector` | `ProjectLanguageTemplateMultiHostInjector` | 工程级语言注入模板（JSONC 配置） | 暂不迁移（复杂、非核心） |
| `xdebugger.breakpointType` / `debugger.positionManagerFactory` | `JdbcProcedure*` / `TinyScript*` / `Funic*` | 断点与调试 | 不迁移 |
| `xml.elementDescriptorProvider` | `XProcOpenAttrDescriptorProvider` | 让特定标签允许任意动态属性，避免 DTD 报错 | 计划以「诊断豁免」方式迁移（P2） |
| `fileType` / `lang.parserDefinition` / `syntaxHighlighterFactory` 等 | TinyScript/Ognl/Funic/Funvi | 自研 DSL 语言支持 | 不迁移（用户明确暂不实现） |
| `actions` | `XProc4jTestAction`、`Oracle2Xproc4jConvertAction`、TinyScript/Funic/Groovy Test | 工具动作 | Oracle 转换/测试动作暂不迁移 |
| `actions` | `JdbcProcedureXmlProcedureJumpSourceFileAction`、`JdbcProcedureFunctionJumpSourceFileAction` | 右键跳转源文件 | 暂不迁移/可选 |

## A.2 XML 语言注入（JdbcProcedureXmlLangInjectInjector）

### A.2.1 注入宿主

- 注入位置：`XmlText`（标签正文）与 `XmlAttribute`（属性值）。
- 注入前先识别目标语言，识别顺序：
  1. 标签的 `_lang` 属性值（支持 `,;|/` 分隔，取第一个可识别项）；
  2. 标签的 `_type` 属性值；
  3. 特殊标签短路：`lang-body`、`lang-catch`、`lang-finally`、`script-segment`、`lang-synchronized`、`lang-async`、`sql-scope` 不注入；
  4. 标签名类型判断：
     - `sql` / `sql-*` / `*-sql`：优先根据 `database` / `databases` 属性找数据库方言语言，否则通用 `SQL`；
     - `java` / `java-*` / `*-java`：Java；
     - `groovy` / `groovy-*` / `*-groovy`：Groovy；
     - `string` / `render`：VTL → FreeMarker → Thymeleaf → Shell 中第一个可用者；
     - `lang-eval-ts` / `lang-eval-tinyscript`：TinyScript（缺失时回退 Scala → Groovy → Shell）；
     - `lang-eval-funic`：Funic（缺失时回退同上）；
     - `lang-eval`：OGNL → Spring EL → EL → JavaScript 中第一个可用者；
  5. 标签名推断（仅在文件扩展名判断通过时）：找标签名整体、首个 `-` 前缀、最后一个 `-` 后缀对应语言；
  6. 属性名特征判断：包含 `body-xml` / `body-text` 的属性，再按 `eval-java` / `eval-js` / `eval-groovy` / `eval-ts` / `eval-tinyscript` / `eval-funic` / `render` / `eval` 选择语言；
  7. 递归到父标签。

### A.2.2 标签正文注入的包装（IDEA 侧）

| 目标 | 包装方式（节选） |
|---|---|
| Java（普通 eval） | 前置大量 `import i2f.*` + `public class MyJavaEval { public Object eval() throws Throwable{`，后置 `} }` |
| `lang-java-import` | 前置 import 段 + `class MyDsl {}` |
| `lang-java-member` | 前置 import 段 + `public class MyJavaProcedure {` |
| `lang-java-body` / `lang-eval-java` | import 段 + 成员段 + `public Object exec(JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable {` |
| Groovy | import 段 + `class MyGroovyProcedure { def exec(...) {` |
| JavaScript | 前置 `JsInject.js`（1546 行，构造 `executor` / `params` 等桩） |
| 其它语言 | 原样注入 |

> 包装内容对语义高亮至关重要（尤其 Java 片段本身不是完整编译单元）。VS Code 的 TextMate 注入无法添加「不可见的前后缀」，因此只能做语法着色，不能获得 Java 语义补全/校验。

### A.2.3 属性值注入规则（摘录）

| 标签 | 属性 | 目标语言 | 包装 |
|---|---|---|---|
| `lang-set` | `radix` | Java | `class MyDsl { public Object v=...;}` |
| `lang-set` | `value`、`value.*` | `_lang`/`_type` 指定语言 | 原样 |
| `lang-fori` | `begin`、`end`、`incr` | Java | 表达式 |
| `lang-invoke` | `method` | Java | import + `exec` 方法体 |
| `lang-catch` / `lang-throw` | `type` | Java | 类型位置 |
| `sql-*` | `result-type` | Java | 类型位置 |
| `lang-async` / `lang-latch-await` / `lang-sleep` | `time-unit` | Java | `TimeUnit.` 枚举位置 |
| 同名布尔类属性 | `delay`、`timeout`、`await`、`read-only`、`params-share`、`limited`、`accept-batch`、`before-truncate` | Java | 表达式 |
| 任意标签 | `propagation` | Java | `Propagation.` 枚举 |
| 任意标签 | `isolation` | Java | `java.sql.Connection.` 常量 |
| 任意标签 | `class`、`type`、`result-type`、`rollback-for`、`no-rollback-for`、`resultType`、`parameterType`、`className`、`packageName`、`return-type`、`returnType`、`javaType`、`typeHandler` | Java | 类型位置 |
| 任意标签 | `package` | Java | `package ...;` |
| 任意标签 | `jdbc-type` / `jdbcType` | Java | `java.sql.JDBCType.` |
| 任意标签 | `*.int` / `*.long` / `*.boolean` / `*.float` / `*.double` | Java | 表达式 |
| 任意标签 | `*.render` | VTL/FreeMarker/Thymeleaf/Shell | 原样 |
| 任意标签 | `test`、`collection`、`*.eval.*`、`*.eval` | OGNL/SpEL/EL/JS | 原样 |
| 任意标签 | `*.eval-java` | Java | import + `exec` |
| 任意标签 | `*.eval-js` | JavaScript | JsInject 前缀 |
| 任意标签 | `*.eval-ts` / `*.eval-tinyscript` | TinyScript | 原样 |
| 任意标签 | `*.eval-funic` | Funic | 原样 |
| 任意标签 | `*.eval-groovy` | Groovy | import + `exec` |
| 任意标签 | `_lang` | Java | 动态枚举（所有已注册语言 ID） |

## A.3 XML 补全（JdbcProcedureXmlCompletionContributor）

| 触发位置 | 补全内容 | 数据来源 |
|---|---|---|
| 属性名以 `.` 结尾 | `FeatureConsts` 全部特性；`type` / `rollback-for` / `no-rollback-for` 额外包含 `cause-*` | 框架常量 |
| `procedure-call` / `function-call` / `script-include` 的属性名 | 根据 `refid` 找到目标 procedure 的参数名、`参数.特性`、整段参数属性模板 | 工程扫描 `ProcedureMeta` |
| `procedure` / `script-segment` 的属性名 | 所有 procedure 的参数集合 | 工程扫描 |
| 无匹配 | `_lang`、`id`、`refid`、`__file`、`__line`、`return`、`result`、`value` | 固定兜底 |
| `refid` 属性值 | 所有 procedure/script-segment 的 id | 工程扫描 |
| `propagation` | `Propagation` 枚举 | 框架枚举 |
| `isolation` | 5 个 `TRANSACTION_*` | JDBC 常量 |
| 布尔属性 | `true` / `false` | 固定 |
| `time-unit` | `TimeUnit` 枚举 | JDK |
| `jdbc-type` / `jdbcType` | `JDBCType` 全部枚举 | JDK |
| `pattern` / `date-format` / `format` | 日期格式模板 | 固定 |
| `method` | 方法名候选 | 固定 |
| `database` / `databases` | mysql、oracle、mariadb、gbase、dm、postgre | 固定 |
| `datasource` / `datasources` | primary、master、main、slave | 固定 |
| `result-type` 等 | int、long、string、boolean、double、float | 固定 |
| 其他属性值 | 当前文件变量集合 | 文件扫描 |
| SQL 参数位置 | 变量集合，可带 `${` / `$!{` / `#{` / `#!{` 前缀 | 文件扫描 |
| 任意位置 | 变量、函数、SQL 标识符、SQL 关键字 | 文件扫描 + 固定关键字 |

### 变量采集规则（CompletionHelper）

- 内置变量：`STATIC_EMBED_VARIABLES`（如上下文/执行器/数据源等固定名）。
- `procedure` / `script-segment` 的属性（除 `return`、`refid`、`id`、`params-share` 外）视为入参。
- 任意属性 `result`、`item`、`result.*` 的值视为产出变量。
- 任意属性值中的 `$...` / `#...` 占位符提取为变量。
- 标签正文中的占位符与 TinyScript 赋值语句提取为变量（VS Code 版可保留占位符部分，TinyScript 赋值部分暂不实现）。
- SQL 标识符单独收集，供 SQL 补全。

## A.4 引用与导航

### A.4.1 refid → id

- 适用范围：根标签为 `procedure` 的 XML 文件；属性名为 `refid`。
- 目标：工程内所有 `procedure` / `script-segment`（含嵌套层级，IDEA 行标记递归深度 3，元数据扫描深度 4）的 `id`。
- 支持 `refid` 的标签：`procedure-call`、`function-call`、`script-include`（行标记范围）；`sql-call`、`include`（DTD 中也存在 refid，但参考插件未显式处理，可作为增强）。
- `id` 反向引用用于 Find Usages。

### A.4.2 标签名 → Java 实现类

- 映射规则：标签名按 `_` / `-` 拆分 → 每段首字母大写 → 追加 `Node`。
  - 例：`sql-query-list` → `SqlQueryListNode`；`lang-eval-java` → `LangEvalJavaNode`。
- 别名表：
  - `lang-eval-ts` / `lang-eval-tinyscript` → `lang-eval-tinyScript`
  - `lang-eval-js` → `lang-eval-javascript`
  - `lang-catch` / `lang-finally` → `lang-try`
  - `lang-otherwise` → `lang-choose`
  - `etl-extra` / `etl-load` / `etl-transform` / `etl-before` / `etl-after` → `sql-etl`
  - `lang-java-import` / `lang-java-member` / `lang-java-body` → `lang-eval-java`
- 目标类在工程源码 + 依赖库中按短类名查找；VS Code 版可先限定工作区源码。

## A.5 `$` 变量高亮（DollarVariablesHighlighter）

- 正则：`([$#](\!)?\{[^\}]*\})|([$][a-zA-Z0-9_]+)`
- 颜色：默认 `DefaultLanguageHighlighterColors.NUMBER`；匹配串去空白后含 `{}` 时使用 `HighlighterColors.BAD_CHARACTER`。
- 限制：文档 > 1,000,000 字符不做扫描；单编辑器最多 2,000 个高亮；文档变化后 1 秒节流。
- 覆盖所有 XML 文本与属性（编辑器级，不区分根标签）。

## A.6 实时模板与文件模板

- 实时模板：`assets/xproc4j/liveTemplates/XProc4j.xml`，约 574 行，模板组 `XProc4j`，覆盖结构标签、Java/Groovy/JS/TinyScript 代码块、SQL 操作、日期/格式化、并发、文件操作、事件等。
- 文件模板：`fileTemplates/XProc4J file.xml.ft`，内容为 `<!DOCTYPE procedure SYSTEM "procedure.dtd">` + `<procedure id="${NAME}">` + 作者/日期注释。
- VS Code 对应：`contributes.snippets` + 自定义「新建 XProc4J 文件」命令（VS Code 无 IDEA 式文件模板组）。

## A.7 框架节点与标签清单

### A.7.1 核心标签（TagConsts，76 个常量）

- 根/片段：`procedure`、`script-segment`、`script-include`
- 日志：`log-debug`、`log-info`、`log-warn`、`log-error`
- 调试/输出：`debugger`、`lang-printf`、`lang-println`
- 字符串/渲染：`lang-string`、`lang-render`、`lang-string-join`
- 赋值/格式化：`lang-set`、`lang-format-date`、`lang-format`
- 调用：`lang-invoke`、`procedure-call`、`function-call`、`java-call`
- Shell/文件：`lang-shell`、`lang-file-exists`、`lang-file-mkdirs`、`lang-file-delete`、`lang-file-list`、`lang-file-tree`、`lang-file-read-text`、`lang-file-write-text`
- Java 代码：`lang-eval-java`、`lang-java-import`、`lang-java-member`、`lang-java-body`
- 脚本：`lang-eval`、`lang-eval-groovy`、`lang-eval-javascript`、`lang-eval-js`、`lang-eval-tinyscript`、`lang-eval-ts`、`lang-eval-funic`
- 流程：`lang-if`、`lang-choose`、`lang-when`、`lang-otherwise`、`lang-while`、`lang-do-while`、`lang-foreach`、`lang-fori`、`lang-break`、`lang-continue`、`lang-try`、`lang-catch`、`lang-finally`、`lang-retry`、`lang-throw`、`lang-return`、`lang-body`
- 并发：`lang-async-all`、`lang-async`、`lang-synchronized`、`lang-lock`、`lang-latch`、`lang-latch-await`、`lang-latch-down`、`lang-sleep`、`lang-thread-pool`、`lang-thread-pool-submit`、`lang-thread-pool-shutdown`、`lang-listener`
- 上下文/事件：`lang-new-params`、`context-convert-method-class`、`context-invoke-method-class`、`context-load-package`、`event-send`、`event-publish`、`lang-ai`
- SQL：`sql-query-list`、`sql-query-object`、`sql-query-row`、`sql-query-columns`、`sql-update`、`sql-dialect`、`sql-scope`、`sql-script`、`sql-cursor`、`sql-etl`、`etl-extra`、`etl-transform`、`etl-load`、`etl-before`、`etl-after`、`sql-trans-begin`、`sql-trans-commit`、`sql-trans-none`、`sql-trans-rollback`、`sql-transactional`、`sql-runner`、`sql-call`

### A.7.2 DTD 中的动态 SQL 标签（MyBatis 风格）

`if`、`where`、`set`、`trim`、`foreach`、`choose`、`when`、`otherwise`、`bind`、`include`、`dialect`、`dialect-choose`、`dialect-when`、`dialect-otherwise`。
这些标签出现在 SQL 正文中，正文文本应继续按 SQL 高亮；`test` / `collection` 等属性按表达式高亮。

### A.7.3 `procedure.dtd`

- 路径：`i2f-jdbc-procedure/src/main/resources/assets/std/procedure.dtd`
- 规模：1267 行，111 个 `<!ELEMENT>`、910 条 `<!ATTLIST>`（93 个标签），71 个标签有多条 ATTLIST。
- 用途：标签/属性结构元数据来源；可生成 VS Code 的补全/语法数据（本期不做诊断）。
- 注意：DTD 存在重复 `ATTLIST` 与 19 条缺类型声明，解析器需容错；详见[附录 B](B-框架标准参考文档分析.md)。

## A.8 与 VS Code 的平台能力差异

| 能力 | IntelliJ IDEA | VS Code | 迁移结论 |
|---|---|---|---|
| 多语言注入 | `MultiHostInjector` 一等 API，可注入任意语言并加前后缀 | 无等价 API；TextMate 注入语法只能按词法模式嵌入，无法加不可见前后缀 | 语法高亮可迁移；语义补全/校验不能直接迁移 |
| 内嵌语言服务 | 注入后使用 IDEA 的 Java/SQL/Groovy 语言服务 | 语言服务按文档语言/文件工作，通常不处理 XML 内嵌片段 | Java/SQL 等「提示」需虚拟文档或自建轻量补全；本期默认只复用语法高亮 |
| XML 校验 | 可挂 DTD，插件可扩展元素描述符 | 无内置校验；可依赖 Red Hat XML 扩展或自建诊断 | 自建诊断（P2），或建议安装 XML 扩展 |
| 行号区图标 | `LineMarkerProvider` | 无行号区图标；用 CodeLens/DocumentLink/装饰器替代 | P2 用 CodeLens/DocumentLink |
| 实时模板 | Live Templates（XML 定义，强上下文） | Snippets（JSON，按语言作用域） | 可迁移为 Snippets；上下文控制较弱 |
| 文件模板 | File Template Group | 无内置等价；用命令 + 模板文件实现 | 可迁移为命令 |
| DOM/PSI | 强类型 XML DOM/PSI | 无内置 XML AST；需第三方解析器或自写扫描器 | 设计阶段选型 |
| 调试器 | 平台调试 API | 需 Debug Adapter Protocol | 不迁移 |
| 工程级索引 | PSI 索引 + 后台线程 | 文件扫描 + 缓存 + watcher | 可迁移，需注意大工程性能 |

## A.9 许可证与资产

- 参考插件代码与模板归用户所有（Ice2Faith），可直接复用/改写。
- 框架 `procedure.dtd`、`procedure.xml`、`procedure.md` 随框架分发，迁移时需确认许可证与署名要求；使用方式见[附录 B](B-框架标准参考文档分析.md)。
- VS Code 内置语法（Java/SQL/JS/JSON/XML/Shell/YAML 等）由 VS Code 提供，扩展通过 `include` 复用，不复制其源码。
- 若要支持 Groovy/OGNL/VTL 等第三方语法，仅声明 `include`，由用户安装对应扩展；扩展本身不打包第三方语法，避免许可证与体积问题。
