# 访问者模式（Visitor Pattern）

> 表示一个作用于某对象结构中的各元素的操作，在不改变各元素的类的前提下定义作用于这些元素的新操作。

---

## 一、核心逻辑

访问者模式的核心思想是：**将数据结构与作用于数据上的操作解耦**。

### 1.1 双分派机制（Double Dispatch）

访问者模式通过**双分派**实现运行时多态，这是其最核心的技术机制：

```
第一次分派：element.accept(visitor)
  → 根据 element 的具体类型（StaffEmployee/ManagerEmployee/ExecutiveEmployee）
    决定调用哪个 accept 实现

第二次分派：visitor.visit(this)
  → 根据 visitor 的具体类型（SalaryCalculatorVisitor/AnnualReportVisitor）
    决定调用哪个 visit 方法

结果：在运行时同时确定了 element 和 visitor 的具体类型，实现真正的多态
```

### 1.2 开闭原则实现

- **对扩展开放**：新增操作只需新增访问者类，无需修改元素类
- **对修改关闭**：元素类保持稳定，不受新操作影响

### 1.3 状态累积能力

访问者可以在遍历对象结构的过程中累积状态（如累计总薪资、统计总数等），这是访问者模式的独特优势。

---

## 二、核心组成

访问者模式包含 **5 大核心角色**：

### 2.1 抽象访问者（Visitor）

**接口**：[EmployeeVisitor](./visitor/EmployeeVisitor.java)

**职责**：声明针对每种具体元素类型的 visit 方法（方法重载）

```java
public interface EmployeeVisitor {
    void visit(StaffEmployee employee);      // 处理普通员工
    void visit(ManagerEmployee employee);    // 处理经理
    void visit(ExecutiveEmployee employee);  // 处理高管
}
```

### 2.2 具体访问者（ConcreteVisitor）

**实现类**：
- [SalaryCalculatorVisitor](./visitor/impl/SalaryCalculatorVisitor.java) - 薪资计算器
- [AnnualReportVisitor](./visitor/impl/AnnualReportVisitor.java) - 年度报告生成器

**职责**：实现每种元素类型的访问逻辑，可以累积状态

### 2.3 抽象元素（Element）

**抽象类**：[Employee](./emplyee/Employee.java)

**职责**：声明 `accept(EmployeeVisitor visitor)` 方法，作为访问者模式的入口点

```java
public abstract class Employee {
    protected String name;
    protected String position;
    protected double baseSalary;
    
    public abstract void accept(EmployeeVisitor visitor);
}
```

### 2.4 具体元素（ConcreteElement）

**实现类**：
- [StaffEmployee](./emplyee/impl/StaffEmployee.java) - 普通员工（薪资 = 基本工资 + 加班费）
- [ManagerEmployee](./emplyee/impl/ManagerEmployee.java) - 经理（薪资 = 基本工资 + 团队奖金 + 管理津贴）
- [ExecutiveEmployee](./emplyee/impl/ExecutiveEmployee.java) - 高管（薪资 = 基本工资 + 年度分红 + 股票期权）

**职责**：实现 `accept` 方法，调用 `visitor.visit(this)` 完成双分派

```java
@Override
public void accept(EmployeeVisitor visitor) {
    visitor.visit(this);  // 双分派核心：this 的编译时类型确定 visit 方法
}
```

### 2.5 对象结构（ObjectStructure）

**实现**：`List<Employee>`（员工列表）

**职责**：存储元素集合，提供遍历能力，使访问者可以访问所有元素

---

## 三、案例设计解析

### 3.1 业务场景

**公司人事系统**：公司有不同类型的员工（普通员工、经理、高管），每种员工的薪资结构、绩效评估方式不同。需要对员工执行多种操作：
- 计算薪资
- 生成年度报告
- 计算个人所得税（扩展场景）

### 3.2 设计实现

#### 步骤 1：创建对象结构

```java
List<Employee> employees = new ArrayList<>();
employees.add(new StaffEmployee("张三", 8000, 20, 50));
employees.add(new ManagerEmployee("王经理", 15000, 8, 5000, 3000));
employees.add(new ExecutiveEmployee("赵总", 30000, 2.0, 50000, 1000000));
```

#### 步骤 2：使用薪资计算器访问者

```java
SalaryCalculatorVisitor salaryVisitor = new SalaryCalculatorVisitor();
for (Employee emp : employees) {
    emp.accept(salaryVisitor);  // 双分派
}
System.out.printf("全员薪资总计：¥%.2f%n", salaryVisitor.getTotalSalary());
```

**执行流程**：
1. `StaffEmployee.accept(salaryVisitor)` → 调用 `salaryVisitor.visit(this)`（this 类型为 StaffEmployee）
2. 精确匹配到 `SalaryCalculatorVisitor.visit(StaffEmployee)` 方法
3. 计算：基本工资 8000 + 加班费（20h × 50/h = 1000）= 9000
4. 累计到 totalSalary

#### 步骤 3：使用年度报告生成器访问者

```java
AnnualReportVisitor reportVisitor = new AnnualReportVisitor();
for (Employee emp : employees) {
    emp.accept(reportVisitor);  // 同一个对象结构，不同的访问者
}
```

**设计亮点**：同一个员工列表，使用不同的访问者执行完全不同的操作，体现了"**对象结构稳定、操作频繁变化**"的场景适配。

#### 步骤 4：验证开闭原则

```java
// 新增税务计算访问者，完全不需要修改任何员工类
EmployeeVisitor taxVisitor = new EmployeeVisitor() {
    @Override
    public void visit(StaffEmployee employee) {
        double totalPay = employee.getBaseSalary() + employee.calculateOvertimePay();
        double tax = totalPay * 0.1;
        System.out.printf("  [普通员工] %s - 个人所得税：¥%.2f%n", employee.getName(), tax);
    }
    // ... 其他 visit 方法
};
```

**开闭原则体现**：新增操作（税务计算）只需新增访问者，零侵入现有元素类。

### 3.3 设计优势

1. **遵循开闭原则**：新增操作只需新增访问者类，无需修改已有元素类
2. **遵循单一职责**：每个访问者只负责一种操作，逻辑集中易维护
3. **双分派机制**：在运行时同时确定元素和访问者的具体类型，实现真正多态
4. **状态累积**：访问者可以累积状态（如 SalaryCalculatorVisitor 累计总薪资）
5. **操作可组合**：可以对同一对象结构依次应用多个访问者

### 3.4 设计局限

1. **增加新元素类困难**：需要修改所有访问者接口和实现
2. **破坏封装**：访问者需要访问元素的内部状态
3. **适用场景受限**：仅适用于元素类型固定、操作类型多变的场景

---

## 四、典型应用场景

### 4.1 编译器 AST 处理

**场景**：编译器需要遍历抽象语法树（AST），执行词法分析、语法分析、代码生成、优化等操作。

**应用**：
- 词法分析访问者：遍历 AST 节点，提取 token
- 语法检查访问者：验证类型、作用域、语义
- 代码生成访问者：将 AST 转换为目标代码
- 优化访问者：常量折叠、死代码消除

**优势**：AST 结构稳定，但编译过程需要多种操作，访问者模式可以清晰分离各阶段逻辑。

---

### 4.2 财务报表系统

**场景**：企业有不同账户类型（活期、定期、贷款、信用卡），需要执行多种财务操作。

**应用**：
- 利息计算访问者：根据账户类型计算利息
- 风险评估访问者：评估账户风险等级
- 税务计算访问者：计算不同账户的税费
- 月度报表访问者：生成账户月度报表

**优势**：账户类型稳定，但财务规则经常变化（税率调整、利率调整），访问者模式便于维护。

---

### 4.3 XML/JSON 文档处理

**场景**：解析 XML/JSON 文档树，执行验证、转换、提取等操作。

**应用**：
- 验证访问者：验证文档是否符合 XSD/JSON Schema
- 转换访问者：将 XML 转换为 JSON 或其他格式
- 提取访问者：提取特定节点的数据
- 统计访问者：统计文档结构信息

**典型案例**：JDK 的 `javax.xml.parsers.DocumentBuilderFactory`、`BeanDefinitionVisitor`（Spring 中替换占位符）

---

### 4.4 文件系统遍历

**场景**：遍历文件树，执行搜索、统计、备份等操作。

**应用**：
- 搜索访问者：查找特定类型/名称的文件
- 统计访问者：统计文件大小、类型分布
- 备份访问者：复制文件到备份目录
- 清理访问者：删除临时文件

**典型案例**：JDK 的 `java.nio.file.FileVisitor` 接口、`SimpleFileVisitor` 适配器类

---

### 4.5 测试框架中的用例执行

**场景**：测试框架需要遍历测试用例集合，执行不同类型的测试。

**应用**：
- 单元测试访问者：执行单元测试用例
- 性能测试访问者：执行性能测试并收集指标
- 覆盖率访问者：统计代码覆盖率
- 报告访问者：生成测试报告

**优势**：测试用例结构稳定，但测试类型和报告格式经常变化。

---

### 4.6 图形渲染引擎

**场景**：场景图（Scene Graph）包含多种图形元素（三角形、圆形、光线、相机），需要执行多种操作。

**应用**：
- 渲染访问者：遍历场景图，调用 OpenGL/DirectX 渲染
- 碰撞检测访问者：检测图形元素之间的碰撞
- 序列化访问者：将场景图保存到文件
- 优化访问者：合并网格、剔除不可见元素

**优势**：场景图元素类型固定，但渲染策略、优化算法经常迭代。

---

### 4.7 数据库查询优化器

**场景**：SQL 解析后生成查询计划树，需要执行优化、评估、执行等操作。

**应用**：
- 优化访问者：应用索引选择、连接顺序优化
- 代价评估访问者：计算查询执行代价
- 执行访问者：将查询计划转换为实际执行
- 缓存访问者：检查查询结果是否在缓存中

**典型案例**：MyBatis 的 `SqlNode` 体系、Hibernate 的 HQL 解析器

---

## 五、总结

访问者模式是 **"对象结构稳定、操作频繁变化"** 场景的最佳实践。通过双分派机制，它在不修改元素类的前提下实现了操作的可扩展性，是开闭原则的经典体现。

**适用条件**：
✅ 对象结构包含多种类型的元素  
✅ 需要对这些元素执行多种不同的操作  
✅ 元素类型很少改变，但操作经常增加  
✅ 希望将相关操作集中到一个类中  

**避免使用**：
❌ 元素类型经常变化  
❌ 访问者需要频繁访问元素的私有状态（破坏封装）  
❌ 对象结构简单，操作也简单（过度设计）

---

> 设计模式是工具而非银弹，使用时应结合实际业务场景，避免过度设计（Over-engineering）。
