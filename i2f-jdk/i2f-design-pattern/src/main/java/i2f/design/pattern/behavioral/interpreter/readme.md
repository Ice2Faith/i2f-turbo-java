# 解释器模式（Interpreter Pattern）

> 解释器模式是一种行为型设计模式，它定义了语言的文法表示，并通过构建解释器来解释执行该语言中的句子。

---

## 一、核心逻辑

### 1.1 模式本质

解释器模式的核心在于**将语言的文法规则映射为对象结构**，通过递归解释实现语言执行：

- **文法对象化**：将语言中的每个文法规则（终结符和非终结符）映射为一个独立的类
- **抽象语法树（AST）**：通过组合表达式对象构建语法树，树的每个节点都是一个表达式对象
- **递归解释**：从根节点开始，递归调用每个节点的 `interpret()` 方法，自底向上计算结果
- **上下文隔离**：使用 `Context` 对象存储全局状态（如变量绑定），与表达式对象解耦

### 1.2 关键机制

#### 递归解释机制
```
根表达式.interpret(context)
  ├── 左子表达式.interpret(context)
  │     ├── 递归直到终结符表达式
  │     └── 返回基础值
  ├── 右子表达式.interpret(context)
  │     ├── 递归直到终结符表达式
  │     └── 返回基础值
  └── 合并左右结果并返回
```

#### 终结符 vs 非终结符
- **终结符表达式**：文法的叶子节点，直接返回值（如数字、变量）
- **非终结符表达式**：包含子表达式，递归解释后运算（如加减乘除）

### 1.3 设计原则体现

- **开闭原则（OCP）**：新增运算符只需新增表达式类，无需修改现有代码
- **单一职责原则（SRP）**：每个表达式类只负责一种运算的解释
- **组合模式（Composite）**：表达式对象形成树形结构，统一接口递归处理

---

## 二、核心组成

### 2.1 角色映射

| 角色 | 类名 | 职责说明 |
|------|------|----------|
| **抽象表达式（Abstract Expression）** | `Expression` | 定义统一的解释接口 `interpret(Context)` |
| **终结符表达式（Terminal Expression）** | `NumberExpression` | 实现文法中的终结符（数字/变量），直接返回值 |
| **非终结符表达式（Non-terminal Expression）** | `AddExpression`<br>`SubtractExpression`<br>`MultiplyExpression`<br>`DivideExpression` | 实现文法中的运算规则，递归解释子表达式后运算 |
| **上下文（Context）** | `Context` | 存储全局信息（变量绑定、配置等） |
| **客户端（Client）** | `Test` | 构建抽象语法树并调用解释器 |

### 2.2 类结构图

```
                    ┌─────────────────┐
                    │   Expression    │ ◄── 抽象表达式接口
                    │  +interpret()   │
                    └────────┬────────┘
                             │ implements
              ┌──────────────┴──────────────┐
              │                             │
    ┌─────────▼─────────┐      ┌────────────▼────────────┐
    │ NumberExpression  │      │   Non-terminal Expressions│  ◄── 非终结符表达式
    │ (终结符表达式)      │      │   (非终结符表达式)         │
    │                   │      │                           │
    │ -value: double    │      │ -left: Expression         │
    │ -variableName     │      │ -right: Expression        │
    └───────────────────┘      └───────────────────────────┘
                                        │
                              ┌─────────┼─────────┐
                              │         │         │
                        ┌─────▼──┐ ┌───▼────┐ ┌──▼──────┐
                        │  Add   │ │  Sub   │ │  Mul    │ ...
                        └────────┘ └────────┘ └─────────┘

                    ┌─────────────────┐
                    │    Context      │ ◄── 上下文环境
                    │ -variables: Map │
                    │ +setVariable()  │
                    │ +getVariable()  │
                    └─────────────────┘
```

### 2.3 文法规则

本包实现的数学表达式文法：

```
Expression ::= NumberExpression
             | AddExpression
             | SubtractExpression
             | MultiplyExpression
             | DivideExpression

AddExpression      ::= Expression '+' Expression
SubtractExpression ::= Expression '-' Expression
MultiplyExpression ::= Expression '*' Expression
DivideExpression   ::= Expression '/' Expression

NumberExpression   ::= 字面量数字 | 变量名
```

---

## 三、案例设计解析

### 3.1 业务场景

**数学表达式计算器**：支持四则运算和变量绑定的表达式求值系统。

### 3.2 案例设计流程

#### 步骤 1：定义抽象表达式接口

```java
public interface Expression {
    double interpret(Context context);
}
```

所有表达式都实现此接口，确保统一的解释入口。

#### 步骤 2：实现终结符表达式

```java
// 字面量模式：直接存储数值
NumberExpression num = new NumberExpression(3.14);

// 变量模式：存储变量名，解释时从 Context 查找
NumberExpression var = new NumberExpression("price");
```

终结符表达式是递归的终止条件，直接返回数值。

#### 步骤 3：实现非终结符表达式

以 `AddExpression` 为例：

```java
public class AddExpression implements Expression {
    private Expression left;
    private Expression right;
    
    @Override
    public double interpret(Context context) {
        // 递归解释左右子表达式，然后相加
        return left.interpret(context) + right.interpret(context);
    }
}
```

非终结符表达式通过递归调用子表达式的 `interpret()` 方法实现运算。

#### 步骤 4：构建抽象语法树（AST）

**示例表达式**：`(10 - 3) * 2`

```java
Expression expr = new MultiplyExpression(
    new SubtractExpression(
        new NumberExpression(10),
        new NumberExpression(3)
    ),
    new NumberExpression(2)
);
```

**对应的 AST 结构**：

```
        MultiplyExpression
           /         \
  SubtractExpression  NumberExpression(2)
      /      \
Number(10)  Number(3)
```

#### 步骤 5：递归解释执行

调用 `expr.interpret(context)` 时的执行流程：

```
1. MultiplyExpression.interpret()
   ├── 调用 left.interpret() → SubtractExpression
   │   ├── 调用 left.interpret() → NumberExpression(10) 返回 10
   │   ├── 调用 right.interpret() → NumberExpression(3) 返回 3
   │   └── 计算 10 - 3 = 7
   ├── 调用 right.interpret() → NumberExpression(2) 返回 2
   └── 计算 7 * 2 = 14
```

### 3.3 案例特性

#### 特性 1：变量绑定支持

```java
Context context = new Context();
context.setVariable("price", 99.5);
context.setVariable("quantity", 4);

Expression expr = new MultiplyExpression(
    new NumberExpression("price"),
    new NumberExpression("quantity")
);
// 结果：398.0
```

变量表达式在解释时从 `Context` 中动态查找值，实现表达式与数据的解耦。

#### 特性 2：复杂表达式嵌套

```java
// (a + b) * (c - d) / e
Expression expr = new DivideExpression(
    new MultiplyExpression(
        new AddExpression(new NumberExpression("a"), new NumberExpression("b")),
        new SubtractExpression(new NumberExpression("c"), new NumberExpression("d"))
    ),
    new NumberExpression("e")
);
```

通过对象组合实现任意复杂度的表达式嵌套。

#### 特性 3：异常处理

- **除零检查**：`DivideExpression` 在解释前验证除数不为零
- **未定义变量**：`NumberExpression` 在变量未定义时抛出异常

#### 特性 4：实际业务应用

**商品折扣计算场景**：

```java
// 原价 × 数量 × 折扣率 = 最终价格
Expression priceExpr = new MultiplyExpression(
    new MultiplyExpression(
        new NumberExpression("originalPrice"),
        new NumberExpression("quantity")
    ),
    new NumberExpression("discountRate")
);

Context shoppingContext = new Context();
shoppingContext.setVariable("originalPrice", 299.0);
shoppingContext.setVariable("quantity", 3);
shoppingContext.setVariable("discountRate", 0.8); // 8折
// 结果：¥717.60
```

### 3.4 模式优势总结

1. **易于扩展新规则**：新增运算符（如幂运算、取模）只需新增一个表达式类
2. **实现简单**：每个表达式类职责单一，代码清晰易懂
3. **天然支持递归**：非终结符表达式通过递归调用子表达式实现复杂逻辑
4. **易于构建 AST**：表达式对象组合自然形成树形结构

### 3.5 模式局限性

1. **文法复杂时类爆炸**：每个文法规则都需要一个类，复杂语言会导致类数量激增
2. **性能问题**：递归解释效率较低，复杂表达式需考虑缓存或编译优化
3. **适用场景有限**：仅适合简单文法，复杂语言应使用专业解析器（如 ANTLR、JavaCC）

---

## 四、典型应用场景

### 4.1 表达式求值引擎

- **数学公式计算**：科学计算器、Excel 公式引擎、财务报表公式
- **业务规则表达式**：促销规则引擎（`price * quantity > 1000 ? discount(0.9) : discount(1.0)`）
- **动态条件判断**：可配置的过滤条件解析（如 SQL WHERE 子句解析）

**业务价值**：将硬编码的计算逻辑转为可配置的表达式，提升系统灵活性。

### 4.2 编译器与语言解析

- **编译器前端**：词法分析 → 语法分析 → 构建 AST → 解释执行/编译
- **脚本语言解释器**：自定义 DSL（领域特定语言）的解释执行
- **SQL 解析器**：将 SQL 语句解析为 AST，优化后执行

**典型案例**：
- JDK：`java.util.regex.Pattern`（正则表达式解释执行）
- Spring：SpEL（`ExpressionParser` 解释 `#{...}` 表达式）
- ASM 字节码框架：`ClassVisitor`、`MethodVisitor` 访问者模式配合解释器

### 4.3 规则引擎

- **业务规则管理**：将业务规则建模为表达式，支持动态配置和热更新
- **权限规则解析**：RBAC/ABAC 权限表达式的解释执行
- **工作流条件分支**：流程节点流转条件的动态解析

**业务价值**：业务规则与代码解耦，非技术人员可通过配置表达式调整规则。

### 4.4 查询语言解析

- **GraphQL 查询解析**：将 GraphQL 查询语句解释为执行计划
- **Elasticsearch DSL**：解析查询 DSL 构建搜索请求
- **自定义查询语言**：为特定业务场景设计查询语言（如日志查询、数据筛选）

### 4.5 配置文件解析

- **表达式型配置**：配置文件中嵌入表达式（如 `${price * 0.8}`），运行时解释求值
- **模板引擎**：模板语言中的变量替换和表达式计算
- **Spring 属性占位符**：`@Value("${...}")` 中的占位符解析

### 4.6 数学与科学计算

- **符号计算**：数学表达式的符号推导、化简、求导
- **单位换算**：带单位的表达式解释（如 `1.5 km + 500 m`）
- **金融计算**：复利公式、期权定价模型的表达式解释

### 4.7 验证规则解析

- **表单验证规则**：可配置的验证表达式（如 `age >= 18 && age <= 65`）
- **数据质量检查**：数据清洗规则的表达式化
- **合同条款验证**：合同条件的自动校验

---

## 五、相关模式对比

| 模式 | 区别点 | 关联 |
|------|--------|------|
| **组合模式** | 解释器模式通常使用组合模式构建 AST | AST 的树形结构本质是组合模式 |
| **访问者模式** | 访问者模式用于在不改变类的前提下增加新操作 | 可用于在 AST 上执行新操作（如代码生成、优化） |
| **迭代器模式** | 迭代器用于遍历集合 | 可用于遍历 AST 节点 |
| **模板方法模式** | 模板方法定义算法骨架 | 表达式解释流程可抽象为模板方法 |

---

## 六、总结

解释器模式通过**文法对象化**和**递归解释**机制，为简单语言提供了优雅的实现方案。它特别适合以下场景：

✅ 文法简单且稳定  
✅ 需要动态解析和执行表达式  
✅ 希望将规则与代码解耦  
✅ 需要支持用户自定义逻辑  

但在面对复杂语言时，应考虑使用专业的解析器生成工具（如 ANTLR、JavaCC）以避免类爆炸和性能问题。
