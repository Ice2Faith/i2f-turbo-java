# 框架标准参考文档分析（补充）

> 分析日期：2026-10-23
> 来源目录：`D:\IDEA_ROOT\DevCenter\i2f-turbo\i2f-turbo-java\i2f-jdk\i2f-jdbc-procedure\src\main\resources\assets\std`
> 用户说明：`procedure.xml`、`procedure.dtd` 为节点定义文件，`procedure.md` 为向导文件，其他文件仅供参考。

## 1. 文档清单与定位

| 文件 | 规模 | 定位 | 在本项目中的用途 |
|---|---|---|---|
| `procedure.xml` | 2,133 行 / 92 KB | XML 节点的可运行示例集合，几乎每个节点都带注释与示例 | Snippets 内容、fixture 基线、补全/语法覆盖核对、标签说明来源 |
| `procedure.dtd` | 1,267 行 / 63 KB | 节点与属性的结构约束（供编辑器补全参考，运行期会被删除） | 构建期提取标签/属性/子节点关系，生成元数据 |
| `procedure.md` | 4,028 行 / 145 KB | 以 Oracle 存储过程迁移为主线的语法向导 | feature 语义、内建变量、内建函数、扩展节点方式、文档撰写参考 |
| `node-definition.md` | 4,047 行 / 121 KB | XML 节点定义白皮书，按标签逐个说明 | 次要参考：补充标签语义；本期不据此实现悬停文档 |
| `framework.md`、`design.md`、`quick-start.md`、`convert-guide.md` 等 | — | 框架总体/设计/入门/转换资料 | 仅在语义存疑时查阅，不作为实现依据 |

## 2. procedure.xml 分析

### 2.1 结构

- 根节点为 `<procedure id="">`，文件头声明 `<!DOCTYPE procedure SYSTEM "procedure.dtd">`。
- 文件开头用大段注释说明：框架目标、技术实现（OGNL/Velocity/Groovy/TinyScript/Funic）、属性修饰符（feature）清单与执行语义。
- 正文按节点类别组织，每个节点前通常有一段注释，说明默认取值方式、属性含义与示例。
- 内容同时是「参考手册」与「可执行示例」，但并非机器可读的 schema。

### 2.2 覆盖度（脚本统计）

- 共出现约 510 个标签实例、106 种不同标签。
- DTD 共 111 种标签，`procedure.xml` 覆盖其中 106 种。
- 未出现在示例中的 5 种：`bind`、`dialect`、`dialect-choose`、`dialect-otherwise`、`dialect-when`。
- 使用频率较高的节点：`script-segment`（71）、`lang-set`（42）、`sql-update`（30）、`lang-if`（17）、`lang-body`（15）、`lang-return`（11）、`lang-catch`（10）、`etl-transform`（10）。

### 2.3 对实现的价值

1. **Snippets 校验源**：IDEA Live Templates 只有 90 个，且部分为组合模板；可用 `procedure.xml` 补齐未覆盖标签的示例并校验生成结构。
2. **fixture 基线**：将 `procedure.xml` 拆分为按类别的 fixture（日志、语言控制、SQL、ETL、事件、上下文等），用于语法/注入/补全回归。
3. **标签说明来源**：注释中包含默认 feature（如 `value` 默认 `string`、`e` 默认 `visit`）与典型用法，可用于补全项的 `documentation`（本期可选，不承诺悬停）。
4. **动态属性核对**：示例中出现大量未在 DTD 声明的属性名（参数名、`OUT_*.out`、`result.body-text.trim.render` 等），可用于识别「开放属性」标签与属性 feature 组合。

### 2.4 使用注意

- 它是示例文件，不是完整节点清单；不能替代 DTD。
- 含少量占位值（如 `id=""`、`name="v_name"`）与演示性属性（`aaa`、`bbb`），不能直接作为生产模板。
- 不包含 `dialect-*` 动态 SQL 节点，相关语法需以 DTD 与框架源码为准。

## 3. procedure.dtd 分析

### 3.1 结构统计（脚本统计）

- 111 个 `<!ELEMENT>` 声明，无重复 ELEMENT。
- 910 个 `<!ATTLIST>` 声明，分布在 93 个标签上；71 个标签存在多条 ATTLIST 声明（最多 193 条）。
- 18 个标签没有 ATTLIST：`choose`、`dialect-choose`、`dialect-otherwise`、`etl-after`、`etl-before`、`lang-body`、`lang-break`、`lang-choose`、`lang-continue`、`lang-finally`、`lang-java-body`、`lang-java-import`、`lang-java-member`、`lang-otherwise`、`lang-try`、`otherwise`、`set`、`where`。
- 标签分类：`lang-*` 59、`sql-*` 17、`etl-*` 5、`log-*` 4、`context-*` 3、`dialect-*` 3、`event-*` 2、`script-*` 2，其余 16 个为根节点、调用节点与 MyBatis 风格动态 SQL 节点。

### 3.2 已知不严谨之处

- 存在缺少类型的属性声明，例如：
  - `<!ATTLIST lang-set value.double #IMPLIED>`
  - `<!ATTLIST lang-return value.float #IMPLIED>`
  - `<!ATTLIST lang-shell result trim #IMPLIED>`
  
  统计到 19 条异常声明：18 条缺少 `CDATA` 等类型标记（`lang-set` 12 条、`lang-return` 6 条），另有 1 条类型非法（`<!ATTLIST lang-shell result trim #IMPLIED>`，`trim` 不是合法 DTD 类型）。解析器应按 `CDATA` 容错处理，并逐条记录到报告。
- 部分属性以 feature 后缀形式重复声明（`value.int`、`value.render`、`result.trim` 等），不能简单当作独立属性名。
- 18 个标签无属性声明，但其中部分标签在示例中带有动态属性或仅使用子节点。
- 因此：**DTD 只能作为构建期元数据源，不能作为 VS Code 运行期校验依据，也不能直接交给严格 XML 解析器。**

### 3.3 与 TagConsts 的差异

- `TagConsts` 共 97 个标签；DTD 多出 14 个标签，全部为 MyBatis 风格动态 SQL/方言节点：
  `bind`、`choose`、`dialect`、`dialect-choose`、`dialect-otherwise`、`dialect-when`、`foreach`、`if`、`include`、`otherwise`、`set`、`trim`、`when`、`where`。
- 这些标签不会出现在 `TagConsts` 中，但属于 XProc4J XML 的合法节点，语法高亮、补全与结构识别必须覆盖。
- `TagConsts` 未出现 DTD 之外的标签，说明常量类是 DTD 的子集。

### 3.4 属性建模建议

元数据应按以下模型归一化，而不是照搬 DTD：

- **基础属性**：去掉最后一个或多个 feature 后缀后的属性名，如 `value.string.long.int` → `value`，features = `[string, long, int]`。
- **声明类型**：DTD 中声明的 CDATA/枚举；未声明时默认字符串。
- **feature 白名单**：来自 `FeatureConsts` 与 `procedure.xml` 注释，用于补全时追加 `.int`、`.render`、`.eval-*` 等候选。
- **开放属性标签**：除 IDEA 插件已有的白名单（`procedure`、`script-segment`、`procedure-call`、`function-call`、`script-include`、`lang-new-params`、`lang-println`、`lang-printf`、`lang-thread-pool-submit`、`event-send`、`event-publish`）外，需对照 `procedure.xml` 核对 `sql-call`、`log-info`、`debugger`、`lang-if`、`lang-when`、`lang-string`、`lang-set` 等标签的动态属性场景；设计阶段给出最终白名单与理由。
- **属性值枚举**：`propagation`、`isolation`、`time-unit`、`jdbc-type`、`result-type`、`database`、`method` 等，继续沿用参考插件补全表。

## 4. procedure.md 分析

- 主线是「Oracle 存储过程/函数 → XProc4J XML」的转换对照，不是完整的节点白皮书。
- 与本项目直接相关的章节：
  - §5.2 修饰符 feature：属性修饰符的完整语义与组合顺序。
  - §5.3~§5.19：procedure/function/变量/赋值/条件/循环/调用/SQL/异常等转换写法。
  - §7 内建变量：`trace.location`、`trace.line`、`trace.errmsg`、`trace.node` 等。
  - §8 内建函数：TinyScript 相关，本期不实现其补全。
  - §10.3 拓展 XML 节点：自定义节点的 DTD 声明与处理逻辑，说明 DTD 不是封闭集合。
  - §10.5 IDEA 插件支持：可对照参考插件的行为说明。
- 使用注意：包含大量 TinyScript/Funic/OGNL/Velocity 示例，其中自研 DSL 部分本期不实现；不要据此扩大范围。

## 5. 对需求与实现的影响

1. **元数据来源确定**：以 `procedure.dtd` 为主结构源，`procedure.xml` 为示例/核对源，`procedure.md` 为语义补充；`TagConsts` 等常量用于交叉校验。
2. **DTD 解析器必须容错**：构建期使用自定义 token 扫描器，不依赖严格 DTD 解析；对缺失类型、重复 ATTLIST、feature 后缀做归一化；输出差异报告与人工覆盖表。
3. **动态 SQL 节点必须纳入**：14 个 DTD-only 标签（`if`/`where`/`set`/`trim`/`foreach`/`choose`/`when`/`otherwise`/`bind`/`include`/`dialect*`）需要高亮、补全与嵌套 SQL 注入支持。
4. **Snippets/fixture 以 `procedure.xml` 校验**：除 90 个 IDEA 模板外，补齐 106 种标签的覆盖核对；fixture 按节点类别拆分。
5. **开放属性白名单需复核**：IDEA 插件白名单与 `procedure.xml` 的实际用法存在差异，设计阶段确定最终列表，避免补全遗漏或过度开放。
6. **不引入运行期 DTD 校验**：与 D-008（不提供诊断）一致；DTD 仅用于构建期生成元数据与文档。

## 6. 后续动作（概要设计阶段）

- 编写 `tools/extract-metadata` 的输入/输出规范：输入 DTD + procedure.xml + 常量类，输出 `tags.json`、`attributes.json`、`features.json`、差异报告。
- 确定标签分类与父子关系表（含动态 SQL 节点）。
- 以 `procedure.xml` 为蓝本拆分 fixture，并标注每个标签应命中的 TextMate scope。
- 从 `procedure.xml` 注释与 `node-definition.md` 摘取标签简介，作为补全项 `documentation` 的可选数据源（本期不实现悬停）。

## 7. 关联文档

- [参考实现分析](A-参考实现分析.md)
- [需求分析](../01-需求分析.md)
- [工作进度](../00-工作进度.md)
