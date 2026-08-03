# Funvi(funic view) 模版引擎技术手册

## 设计理念

- 设计一个轻巧、简洁的模版引擎
- 用于传统模板引擎渲染，或者用于实现 BindSql 的绑定变量 SQL 语句构建

## 基础依赖

- antlr4-runtime
- funic 脚本引擎（同样使用antlr4构建的轻量级函数式脚本语言，全称 functional logic）

---

## 模板语法

### 取值表达式

用于在模板中嵌入动态值，格式为 `${...}` 或 `#{...}`：

| 语法 | 说明 | 示例 |
|---|---|---|
| `${expression}` | 对 expression 求值，结果转为字符串拼接到输出 | `${username}` → 输出用户名 |
| `#{expression}` | 默认实现同 `${}`；在 BindSql 模式下转为 SQL 绑定参数 `?` | `#{age}` → 生成 `?` + 绑定值 |
| `$!{expression}` | 同 `${}`，但当结果为 null 时输出空字符串 `""` 而非 "null" | `$!{nickname}` → null 时输出空 |
| `#!{expression}` | 同 `#{}`，但当结果为 null 时输出空字符串 `""` | `#!{phone}` → null 时输出空 |

expression 中是 **Funic 脚本表达式**，支持：
- 变量访问：`${username}`、`${user.name}`
- 方法调用：`${username.toCharArray()}`
- 三元运算：`${status ? "正常" : "禁用"}`
- 字面量：`"字符串"`、`'字符串'`、`123`、`true`、`false`、`null`

> 在 `{...}` 中若需要包含 `}` 字符，使用 `\}` 转义。

### 块语法

块以 `#` 开头，`()` 包裹参数，`##` 结束：

```
#blockName(参数1, 参数2, ...)
    块体内容（可选）...
##
```

**匿名参数**：直接写表达式，按位置传递：
```funvi
#foreach(item, ${userList})
    ${item.name}
##
```

**命名参数**：以 `name:expression` 形式指定，不依赖位置：
```funvi
#trim(prefix=${" where "}, prefixOverrides=${"and|or"})
    and name = #{name}
##
```

**无参块**：括号可为空或省略参数：
```funvi
#break()
#sharp()
```

**无体块**：某些块不需要块体，直接以 `()` 结束（如 `#break()`、`#bind(...)`、`#sharp()`）。

### if 多分支

```
#if(条件1)
    分支1...
#else(条件2)
    分支2...
#else()
    默认分支...
##
```

- 按顺序对每个分支的条件求值，命中第一个为 true 的分支后忽略后续
- `#else()`（空条件）始终为 true，等价于 default 分支
  - 默认分支也必须带有 `()` 空括号
- `#else` 分支可有零个或多个
- 条件判断规则：`null` → false；空字符串/空集合/空Map → false；`Boolean.FALSE` → false；其余 → true

### 普通文本

除了取值表达式和块语法之外的内容都作为普通文本原样输出。独立的 `#`、`$`、`(`、`)` 等字符在未构成合法语法结构时，也会被当作普通文本处理。

### 转义字符

表达式字符串中支持以下转义：

| 转义序列 | 输出 |
|---|---|
| `\\r` | 回车 |
| `\\n` | 换行 |
| `\\t` | Tab |
| `\\b` | 退格 |
| `\\'` | `'` |
| `\\"` | `"` |
| `\\$` | `$` |
| `\\#` | `#` |
| `\\{` | `{` |
| `\\}` | `}` |
| `\\\\` | `\` |

---

## API 使用

### 入口方法

所有操作通过 `Funvi` 类的静态方法完成：

```java
// 方式1：渲染模板字符串（使用默认 DefaultFunviResolver）
Object result = Funvi.render("你好 ${name}", contextMap);

// 方式2：指定 Resolver 渲染
FunviResolver resolver = new BindSqlFunviResolver();
Object result = Funvi.render(sqlTemplate, params, resolver);

// 方式3：从文件加载模板渲染
Object result = Funvi.render(new File("template.fvi"), contextMap);

// 方式4：解析与渲染分离（复用 AST，适合高频调用同一模板）
FunviParser.RootContext ast = Funvi.parse(template);
Object result1 = Funvi.render(ast, ctx1, resolver);
Object result2 = Funvi.render(ast, ctx2, resolver);
```

> `Funvi.parse()` 内部有 LRU 缓存（容量 4096），相同模板字符串只解析一次。

### 上下文数据

上下文可以是 `Map` 或任意 POJO 对象，支持层级属性路径访问（如 `user.dept.name`）：

```java
Map<String, Object> context = new HashMap<>();
context.put("username", "zhang");
context.put("age", 25);

// 或使用 POJO
User user = new User();
user.setName("zhang");

Object result = Funvi.render("用户: ${name}", user);
```

---

## DefaultFunviResolver — 内置块指令

`DefaultFunviResolver` 是默认的模板渲染器，直接使用 `Funvi.render(...)` 不传 resolver 时即为该实现。它内置了以下块指令。

### 1. `#if` / `#else` — 条件分支

- 条件表达式部分，支持变量名 `a.b.c` 形式
- 或者使用 `${}` 来包含一个 funic 表达式，支持复杂的表达式行为
- 例如：`${age >= 18}`
- 因此，当不确定的时候，都可以使用 `${}` 来包含一个 funic 表达式

```funvi
#if(${age >= 18})
    已成年
#else(${age > 0})
    未成年
#else()
    年龄无效
##
```

### 2. `#foreach(itemName, collection)` — 集合遍历

- itemName 表示的是迭代变量名
- collection 表示被迭代的变量（集合、数组等），可以使用 funic 复杂表达式

```funvi
#foreach(user, ${userList})
    姓名: ${user.name}, 年龄: ${user.age}
##
```

- 参数1：迭代变量名（默认 `"item"`）
- 参数2：集合对象，支持 `List`/`Set`（Iterable）、`Iterator`、数组
- 遍历前保存同名变量，遍历后恢复，不污染外部作用域
- 支持 `#break()` 退出、`#continue()` 跳过当前项

### 3. `#for(begin, cond, incr)` — for 循环

- 三个参数，一般都使用 funic 表达式进行运算

```funvi
#for(${i=0}, ${i < 10}, ${i = i + 1})
    第 ${i} 次
##
```

- 参数1：初始表达式（循环前求值一次）
- 参数2：循环条件（每次迭代前求值）
- 参数3：递增表达式（每次迭代后求值）
- 支持 `#break()`、`#continue()`

### 4. `#while(cond)` — while 循环

- cond 表示循环条件，一般使用 funic 表达式进行运算

```funvi
#while(${index < list.size()})
    ${list.get(index)}
    #bind(index, ${index + 1})
##
```

- 参数1：循环条件
- 支持 `#break()`、`#continue()`

### 5. `#bind(name, value)` / `#set(name, value)` — 变量赋值

- name 表示要设置的变量名
- value 表示怎么得到值，可以是简单值，也可是是一个复杂的 funic 表达式
- `#set(...)` 是 `#bind(...)` 的一个别名

```funvi
#bind(counter, 0)
#set(total, ${price * quantity})
当前计数: ${counter}, 总价: ${total}
```

- `#bind` 和 `#set` 等价，在上下文中写入变量
- 无块体（不能包含 `##` 体）

### 6. `#trim(prefix, suffix, prefixOverrides, suffixOverrides)` — 修剪块

- 因为参数都是可选参数，且通常只使用其中几个参数
- 因此，设计上，只允许使用具名参数方式进行调用
- 即：`参数名: 传递值` 格式
- prefix/suffix 表示内部的内容如果包含有效内容，则添加对应的前缀后缀
- prefixOverrides/suffixOverrides 表示如果内部内容以正则开始或结束，则去除这个开头或结尾

```funvi
select * from user
#trim(prefix: ${" where "}, prefixOverrides: ${"and|or"})
    #if(username)
        and username = #{username}
    ##
    #if(${age != null})
        and age = #{age}
    ##
##
```

- 全部使用**命名参数**，均为可选：
  - `prefix`：前缀，修剪后内容非空时添加
  - `suffix`：后缀，修剪后内容非空时添加
  - `prefixOverrides`：要去除的前导匹配正则（大小写不敏感）
  - `suffixOverrides`：要去除的末尾匹配正则（大小写不敏感）

> 执行顺序：先去除前导 → 去除末尾 → 若内容非空则添加前后缀。

### 7. `#break()` — 跳出循环

```funvi
#foreach(item, ${list})
    #if(${item == null})
        #break()
    ##
##
```

- 无块体，仅在 `#foreach` 循环内有效

### 8. `#continue()` — 继续下一次迭代

```funvi
#foreach(item, ${list})
    #if(${item == null})
        #continue()
    ##
    ${item}
##
```

- 无块体，仅在 `#foreach` 循环内有效

### 9. `#sharp(count)` — 输出 # 字符

```funvi
#sharp()   → #
#sharp(3)  → ###
```

- 无块体，参数为输出个数，默认 1

### 10. `#dollar(count)` — 输出 $ 字符

```funvi
#dollar()  → $
#dollar(2) → $$
```

- 无块体，参数为输出个数，默认 1

---

## BindSqlFunviResolver — 动态 SQL 构建

`BindSqlFunviResolver` 继承自 `DefaultFunviResolver`，专为**动态 SQL + 绑定变量**场景设计。在 Default 全部能力基础上，额外提供以下 SQL 专用特性。

### `$` vs `#` 的核心区别

这是 BindSql 模式与 Default 模式最关键的差异：

| 取值语法 | DefaultFunviResolver | BindSqlFunviResolver |
|---|---|---|
| `${expr}` | 表达式结果转字符串拼接 | 同左，直接拼入 SQL（**无注入保护**） |
| `#{expr}` | 表达式结果转字符串拼接 | 生成 `?` 占位符 + 绑定参数值（**防注入**） |

```funvi
-- 假设 username = "zhang", age = 25
select * from user where name = ${username} and age = #{age}

-- Default 输出（纯字符串拼接）:
select * from user where name = zhang and age = 25

-- BindSql 输出（安全的参数化查询）:
-- SQL:  select * from user where name = zhang and age = ?
-- 参数: [25]
```

> **最佳实践**：用户输入的值使用 `#{}`（绑定参数防注入），固定的安全值使用 `${}`（直接拼接）。

### 新增 `#where()` 块

自动处理 WHERE 子句前缀，去除块内首条条件的 `and`/`or`：

```funvi
select * from user
#where()
    and username = #{username}
    or dept_id = #{deptId}
    #if(${age != null})
        and age > #{age}
    ##
##
```

- 自动去除块内容前导的 `and` 或 `or`（大小写不敏感）
- 若修剪后内容非空 → 添加 ` where ` 前缀返回
- 若内容为空（所有条件均不满足）→ 返回 null，整体 where 子句被排除

### 覆盖 `#set()` 块 — SQL SET 子句

BindSql 模式下 `#set` 的行为变为 SQL 的 SET 子句构建（替代了 Default 中的变量赋值功能）：

```funvi
update user
#set()
    #if(${newName != null})
        username = #{newName},
    ##
    #if(${newAge != null})
        age = #{newAge},
    ##
##
where id = #{id}
```

- 自动去除块内容首尾的逗号
- 若修剪后内容非空 → 添加 ` set ` 前缀返回

> 注意：BindSql 中 `#set` 是 SQL SET 构建块；如需变量赋值请使用 `#bind`。

### 使用方式

```java
// 准备参数
Map<String, Object> params = new HashMap<>();
params.put("username", "zhang");
params.put("age", 25);
params.put("deptId", null);  // 该条件将被自动排除

// 定义 SQL 模板
String sqlFunvi = """
    select * from user
    #where()
        and username = #{username}
        #if(${age != null})
            and age > #{age}
        ##
        #if(${deptId != null})
            and dept_id = #{deptId}
        ##
    ##
    """;

// 使用 BindSqlFunviResolver 渲染
BindSqlFunviResolver resolver = new BindSqlFunviResolver();
Object result = Funvi.render(sqlFunvi, params, resolver);

// result 为 BindSql 对象：
//   SQL:   select * from user where username = ? and age > ?
//   参数:  ["zhang", 25]
```

---

## 典型使用示例

### 示例 1：字符串模板渲染

```java
Map<String, Object> map = new HashMap<>();
map.put("username", "zhang");
map.put("age", 12);
map.put("status", true);

String text = "用户: ${username}, 年龄: ${age}, 状态: ${status ? \"正常\" : \"禁用\"}";
Object result = Funvi.render(text, map);
// 输出: "用户: zhang, 年龄: 12, 状态: 正常"
```

### 示例 2：条件与循环

```funvi
#if(username)
    用户存在：${username}
    #foreach(ch, ${username.toCharArray()})
        字符: ${ch}
    ##
    #if(${age > 10})
        年龄大于10
    #else()
        年龄不大于10
    ##
#else()
    未登录
##
```

### 示例 3：动态 SQL 完整案例

```java
Map<String, Object> params = new HashMap<>();
params.put("username", "zhang");
params.put("age", 25);
params.put("deptId", "D001");
params.put("newAge", 30);

String sqlFunvi = """
    select * from user
    #where()
        and username = #{username}
        #if(${age != null})
            and age > #{age}
        ##
        #if(${deptId != null})
            and dept_id = #{deptId}
        ##
    ##
    #if(${newAge != null})
        #set()
            age = #{newAge},
        ##
    ##
    """;

BindSqlFunviResolver resolver = new BindSqlFunviResolver();
Object result = Funvi.render(sqlFunvi, params, resolver);
// SQL:  select * from user where username = ? and age > ? and dept_id = ?
//               set age = ?
// 参数: ["zhang", 25, "D001", 30]
```

### 示例 4：空值安全与转义

```funvi
昵称: $!{nickname}        ← nickname 为 null 时输出空字符串，而非 "null"
转义测试: \$ 和 \# 不会被解析为取值表达式
```
