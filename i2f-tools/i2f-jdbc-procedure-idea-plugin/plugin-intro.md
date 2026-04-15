# Jdbc-Procedure IDEA 插件介绍

## 插件概述

**插件名称**: Jdbc-Procedure  
**唯一标识**: `i2f.turbo.jdbc-procedure-plugin`  
**版本**: 1.0  
**作者**: Ice2Faith  
**邮箱**: ugex_savelar@163.com  
**仓库地址**: https://github.com/Ice2Faith

### 插件简介

Jdbc-Procedure 是专为 **jdbc-procedure 框架** 开发的 IntelliJ IDEA 插件，为 XProc4J（XML Procedure for Java）配置文件提供智能开发支持。该插件适用于 JetBrains 系列 IDE，包括 IDEA、PyCharm、DataGrip 等。

### 最低版本要求

- IDEA 版本 >= 2021.1
- 其他 JetBrains IDE 的构建时间应晚于 2021年3月

---

## 主要功能

### 1. TinyScript 语言支持

插件为 TinyScript 提供完整的语言支持，TinyScript 是一种基于 ANTLR4 的迷你脚本语言，主要用于在 procedure 配置文件中进行嵌套函数调用和多行语句执行。

**TinyScript 文件类型**:
- 扩展名: `.tis`
- 语言 ID: `TinyScript`

**TinyScript 核心特性**:

| 特性类别 | 支持内容 |
|---------|---------|
| **基础数据类型** | int, long, float, double, null, boolean, string, class 类型字面量 |
| **进制数表示** | 十进制、十六进制(0x)、八进制(0t)、二进制(0b) |
| **字符串类型** | 普通字符串、单引号字符串、模板字符串(R"")、多行字符串(```) |
| **取值语法** | `${变量路径}`、`$!{变量路径}`（null时返回空字符串） |
| **赋值语法** | `=`、`+=`、`-=`、`*=`、`/=`、`%=`、`?=`（空赋值）、`.=`（非空赋值） |
| **解包语法** | `#{key1:value1,key2:value2}=表达式` |
| **控制语句** | if/elif/else、foreach、for、while、do-while、try-catch-finally、throw |
| **函数定义** | `func 函数名(参数列表){ 函数体 };` |
| **管道函数** | `表达式 |> 函数名() |> ::方法名()` |
| **运算符** | 算术、比较、逻辑、类型转换(as/cast/is/typeof)、成员访问 |

**TinyScript IDE 功能**:

- 语法高亮（关键词、数字、字符串、注释等）
- 注释支持（单行 `//` 和多行 `/* */`）
- 括号匹配高亮
- 代码配色配置页面

---

### 2. XML 语言增强

插件为 XProc4J 的 XML 配置文件提供丰富的语言增强功能。

#### 2.1 语言注入 (Language Injection)

插件支持将以下语言动态注入到 XML 特定标签的内容中:

**SQL 系列**:

| 数据库 | 语言 ID |
|-------|--------|
| MySQL | `MySQL` |
| Oracle | `Oracle` |
| PostgreSQL | `PostgreSQL` |
| MariaDB | `MariaDB` |
| DB2 | `DB2_IS` |
| ClickHouse | `ClickHouse` |
| Hive | `HiveQL` |
| Spark SQL | `SparkSQL` |
| Cassandra | `CassandraQL` |
| MongoDB | `MongoDB-JSON` |
| 通用 SQL | `SQL` |

**其他语言**:
- Java: 通过 `lang-eval-java`、`lang-java-body`、`lang-java-import`、`lang-java-member` 等标签注入
- Groovy: 通过 `lang-eval-groovy` 标签注入
- JavaScript: 通过 `lang-eval-js` 标签注入
- VTL/Velocity: 通过 `lang-string`(lang="vtl")、`lang-render` 标签注入
- FreeMarker: 注入 `InjectedFreeMarker`
- Thymeleaf: 注入 `ThymeleafExpressions`
- JSON: 注入 `JSON`
- 正则表达式: 注入 `RegExp`
- TypeScript: 注入 `TypeScript`
- Shell/Bash: 注入 `Shell Script`
- YAML: 注入 `yaml`
- Vue: 注入 `Vue`
- Markdown: 注入 `Markdown`
- CSS/SASS: 注入 `CSS`/`SASS`
- ANTLR4: 注入 `ANTLRv4`
- Redis: 注入 `Redis`
- Cron: 注入 `CronExp`
- SpEL: 注入 `SpEL`

**自动语言检测**: 插件根据标签名和 `_lang` 属性自动检测注入语言，例如：
- 标签名包含 `sql-` → SQL
- 标签名包含 `java-` → Java
- `_lang="vtl"` → Velocity

**Java 注入增强**: 对于 Java 相关标签，插件自动添加框架相关导入：
```java
import i2f.jdbc.procedure.*;      // JDBC Procedure 框架
import i2f.bindsql.*;             // SQL 绑定
import i2f.script.*;              // 脚本支持
import i2f.extension.antlr4.script.tiny.*;  // TinyScript
import i2f.extension.groovy.*;     // Groovy
import i2f.extension.velocity.*;  // Velocity
import i2f.extension.ognl.*;      // OGNL
// 以及 java.util、java.time、java.math 等标准库
```

#### 2.2 代码补全 (Completion)

**XML 属性补全**:

| 属性类型 | 补全内容 |
|---------|---------|
| `refid` | procedure 文件中定义的所有 procedure/script-segment 的 id |
| `propagation` | REQUIRED, SUPPORTS, MANDATORY, REQUIRES_NEW, NOT_SUPPORTED, NEVER, NESTED |
| `isolation` | TRANSACTION_NONE, READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE |
| `time-unit` | NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS |
| `jdbc-type` | 所有 JDBCType 枚举值（VARCHAR, INTEGER, TIMESTAMP 等） |
| `database` | mysql, oracle, mariadb, gbase, dm, postgre |
| `datasource` | primary, master, main, slave |
| `pattern` | 日期格式模板（yyyy-MM-dd HH:mm:ss.SSS 等） |
| `result-type` | int, long, string, boolean, double, float |
| `boolean 属性` | true, false |
| `method` | replace, contains, startsWith, indexOf, substring, Date.new, String.valueOf 等 |

**变量补全**: 在 SQL 表达式中，输入 `$` 或 `${}` 时，自动补全当前文件中已定义的变量。

**参数补全**: 在 `procedure-call`、`function-call` 标签的 refid 属性补全后，自动列出目标 procedure 的所有参数。

#### 2.3 引用跳转 (Reference Navigation)

| 功能 | 说明 |
|-----|------|
| **refid 跳转** | 在 `procedure-call`、`function-call`、`script-include` 标签中，点击 refid 属性的值可跳转到对应 id 的定义位置 |
| **标签名跳转** | 点击 XML 标签名可跳转到对应的 Java 类实现 |
| **行标记** | 在行号区域显示 refid 引用目标图标，点击可导航 |

**标签别名映射**:
```
lang-eval-ts / lang-eval-tinyscript → lang-eval-tinyScript
lang-eval-js → lang-eval-javascript
lang-catch / lang-finally → lang-try
lang-otherwise → lang-choose
etl-extra/etl-load/etl-transform/etl-before/etl-after → sql-etl
```

---

### 3. $变量高亮

插件对 XML 文件中的 `$变量` 语法提供实时高亮显示：

- **普通变量**: `${变量名}`
- **非空变量**: `$!{变量名}`（变量为 null 时返回空字符串而非 null）
- **Hash 变量**: `#{变量名}`、`#!{变量名}`
- **短变量**: `$变量名`

高亮颜色使用数字颜色样式，空变量（包含 `{}`）使用错误高亮颜色标记。

---

### 4. 实时模板 (Live Templates)

插件提供丰富的实时模板，按 `XProc4j` 模板组分类:

**结构标签模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<procedure` | procedure 标签骨架 |
| `<procedure-call` | procedure-call 调用标签 |
| `<script-include` | script-include 引用标签 |
| `<script-segment` | script-segment 片段定义 |

**Java 代码模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<lang-eval-java` | Java 代码块（带方法签名） |
| `<lang-eval-groovy` | Groovy 代码块 |
| `<lang-eval-tinyscript` | TinyScript 代码块 |
| `<lang-eval-js` | JavaScript 代码块 |
| `<lang-java-import` | import 语句块 |
| `<lang-java-member` | 成员变量块 |
| `<lang-java-body` | 方法体块 |
| `<lang-if` | if 条件块 |
| `<lang-foreach` | foreach 循环块 |
| `<lang-try` | try-catch-finally 块 |
| `<lang-catch` | catch 块 |
| `<lang-finally` | finally 块 |

**SQL 操作模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<sql-query-list` | SQL 查询列表 |
| `<sql-query-row` | 单行查询 |
| `<sql-query-object` | 对象查询 |
| `<sql-update` | 更新语句 |
| `<sql-cursor` | 游标遍历 |
| `<sql-scope` | 数据源作用域 |
| `<sql-transactional` | 事务包装 |
| `<sql-etl` | ETL 操作 |

**日期/格式化模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<lang-format-date` | 日期格式化 |
| `<lang-format` | 字符串格式化 |
| `<lang-printf` | 打印格式化 |

**并发控制模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<lang-async` | 异步执行 |
| `<lang-async-all` | 全部异步 |
| `<lang-latch` | 门闩创建 |
| `<lang-latch-await` | 门闩等待 |
| `<lang-latch-down` | 门闩减数 |
| `<lang-synchronized` | 同步块 |

**文件操作模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<lang-file-read-text` | 读取文本文件 |
| `<lang-file-write-text` | 写入文本文件 |
| `<lang-file-list` | 列出文件 |
| `<lang-file-tree` | 文件树 |
| `<lang-file-exists` | 检查文件存在 |
| `<lang-file-mkdirs` | 创建目录 |
| `<lang-file-delete` | 删除文件 |

**其他模板**:

| 模板缩写 | 生成内容 |
|---------|---------|
| `<lang-sleep` | 延迟 |
| `<lang-shell` | Shell 脚本 |
| `<lang-return` | 返回语句 |
| `<lang-throw` | 抛出异常 |
| `<lang-string` | 字符串模板 |
| `<lang-render` | 模板渲染 |
| `<event-publish` | 事件发布 |
| `<event-send` | 事件发送 |

---

### 5. Oracle 语法转换器

插件提供 Oracle 存储过程语法到 XProc4J 的转换功能。

**访问方式**: 右键菜单 → `Oracle 2 XProc4J`

**转换选项**:
- **XProc4J**: 转换为 XProc4J XML 格式
- **TinyScript**: 转换为 TinyScript 表达式
- **Ognl**: 转换为 OGNL 表达式

转换器基于 ANTLR4 解析 Oracle 语法，能够识别 PL/SQL 中的变量声明、赋值、函数调用等结构。

---

### 6. 文件模板

插件注册了 XProc4J 文件模板，创建新文件时可选择 `XProc4J file` 模板。

---

## XML 配置示例

一个典型的 XProc4J 配置文件结构：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<procedure id="user-procedure">
    
    <!-- 上下文加载 -->
    <context-load-package package="com.example" class="UserContext"/>
    <context-invoke-method-class class="UserHelper"/>
    
    <!-- 参数定义 -->
    <lang-set result="userId" value="params.userId"/>
    <lang-set result="userName" value="params.userName"/>
    
    <!-- SQL 查询 -->
    <sql-query-list result="userList">
        <sql-dialect databases="mysql,mariadb">
            SELECT * FROM sys_user 
            WHERE status = 1
            <if test="${userId}">
                AND id = ${userId}
            </if>
        </sql-dialect>
    </sql-query-list>
    
    <!-- Java 代码块 -->
    <lang-eval-java result="processResult">
        String name = (String) params.get("userName");
        return "Hello, " + name;
    </lang-eval-java>
    
    <!-- 条件判断 -->
    <lang-choose>
        <lang-when test="processResult">
            <lang-println tag="success"/>
        </lang-when>
        <lang-otherwise>
            <lang-println tag="failed"/>
        </lang-otherwise>
    </lang-choose>
    
</procedure>
```

---

## 安装与使用

### 安装步骤

1. 打开 IDEA 菜单栏：`File` → `Settings` → `Plugins`
2. 点击设置图标 → `Install Plugin from Disk...`
3. 选择插件 JAR 文件：`jdbc-procedure-plugin-2021.1-211.jar`
4. 点击 `OK` 并重启 IDEA

### 验证安装

1. 在 `Settings` → `Plugins` → `Installed` 中搜索 `jdbc`
2. 如能看到 `Jdbc-Procedure` 插件，说明安装成功
3. 打开任意 `procedure.xml` 文件，检查：
   - SQL 标签中的关键字是否变色
   - 输入 `$` 是否有变量补全提示
   - 标签名是否有导航标记

---

## 注意事项

1. **功能完整性**: 插件提供的提示和补全功能可能不完全，实际运行结果以框架执行为准
2. **错误提示**: 插件标记的错误有时可能是误报，请以实际运行结果为准
3. **版本兼容性**: 建议使用 IDEA 2021.1 及以上版本以获得最佳体验

---

## 技术架构

### 依赖模块

插件依赖以下 IntelliJ 平台模块：
- `com.intellij.modules.platform`
- `com.intellij.java`
- `com.intellij.database`
- `com.intellij.modules.xml`
- `com.intellij.velocity`

### 核心组件

| 组件类 | 功能 |
|-------|------|
| `JdbcProcedureXmlCompletionContributor` | XML 代码补全贡献者 |
| `JdbcProcedureXmlLangInjectInjector` | 多语言注入器 |
| `JdbcProcedureDomFileDescription` | DOM 文件描述 |
| `JdbcProcedureRefidLineMarkerProvider` | 行标记提供者 |
| `XmlIdRefReferenceContributor` | refid 引用贡献者 |
| `XmlTagReferenceContributor` | 标签引用贡献者 |
| `DollarVariablesHighlighter` | $变量高亮器 |
| `TinyScriptFileType` | TinyScript 文件类型 |
| `TinyScriptParserDefinition` | TinyScript 语法解析器定义 |
| `TinyScriptSyntaxHighlighter` | TinyScript 语法高亮器 |
| `Oracle2Xproc4jConvertAction` | Oracle 转换动作 |

---

**文档版本：** 1.0  
**最后更新：** 2026-04-02  
**维护者：** Ice2Faith