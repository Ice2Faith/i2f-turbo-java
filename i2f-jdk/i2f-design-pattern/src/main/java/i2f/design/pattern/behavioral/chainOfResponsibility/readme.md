# 责任链模式（Chain of Responsibility）

> 责任链模式是一种行为型设计模式，使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递请求，直到有一个对象处理它为止。

---

## 一、核心逻辑

### 1.1 请求传递机制

责任链模式的核心在于**请求的自动传递**：

- 每个处理者持有对下一个处理者的引用（`nextHandler`）
- 当请求到达某个处理者时，该处理者决定是否处理
- 如果能处理，则处理请求并返回结果
- 如果不能处理，则自动将请求传递给下一个处理者（`handleNext()`）
- 传递过程对请求发送者完全透明

### 1.2 解耦发送者与接收者

- 请求发送者只需要将请求提交给链的第一个处理者
- 发送者无需知道具体哪个处理者会处理请求
- 发送者无需知道责任链的内部结构和传递逻辑

### 1.3 动态链构建

- 责任链可以在运行时动态组装和调整
- 通过 `setNext()` 方法灵活链接不同的处理者
- 支持链式调用，便于构建复杂的责任链

### 1.4 开闭原则体现

- **对扩展开放**：新增处理者只需新增类并插入链中
- **对修改关闭**：无需修改现有处理者代码
- 每个处理者独立演进，互不影响

---

## 二、核心组成

### 2.1 Handler（抽象处理者）

**类名**：`ApprovalHandler`

**职责**：
- 定义处理请求的接口（`handle()` 抽象方法）
- 持有对下一个处理者的引用（`nextHandler` 字段）
- 提供设置下一个处理者的方法（`setNext()`）
- 提供传递给下一个处理者的方法（`handleNext()`）

**关键代码**：
```java
public abstract class ApprovalHandler {
    private ApprovalHandler nextHandler;
    
    public ApprovalHandler setNext(ApprovalHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }
    
    public abstract boolean handle(ApprovalRequest request);
    
    protected boolean handleNext(ApprovalRequest request) {
        if (nextHandler != null) {
            return nextHandler.handle(request);
        }
        return false;
    }
}
```

### 2.2 ConcreteHandler（具体处理者）

**实现类**：
- `GroupLeader`（组长）：处理 ≤ 3 天的请假
- `DepartmentManager`（部门经理）：处理 ≤ 7 天的请假
- `GeneralManager`（总经理）：处理 ≤ 30 天的请假

**职责**：
- 继承 `ApprovalHandler` 抽象类
- 实现 `handle()` 方法，根据自身权限决定是否处理请求
- 如果超出权限，调用 `handleNext()` 将请求传递给下一个处理者

**关键代码示例**（以 GroupLeader 为例）：
```java
public class GroupLeader extends ApprovalHandler {
    private static final int MAX_LEAVE_DAYS = 3;
    
    @Override
    public boolean handle(ApprovalRequest request) {
        if (request.getLeaveDays() <= MAX_LEAVE_DAYS) {
            // 处理请求
            return true;
        }
        // 传递给下一个处理者
        return handleNext(request);
    }
}
```

### 2.3 Request（请求对象）

**类名**：`ApprovalRequest`

**职责**：
- 封装需要在责任链中传递的请求数据
- 包含申请人姓名、请假天数、请假事由等信息
- 作为处理者之间传递的上下文对象

**关键代码**：
```java
@Data
public class ApprovalRequest {
    private String applicantName;
    private int leaveDays;
    private String reason;
}
```

---

## 三、案例设计解析

### 3.1 业务场景

本案例以**员工请假审批流程**为业务场景：

- 员工提交请假申请后，请求需要按照组织架构层级逐级审批
- 不同层级的管理者有不同的审批权限
- 请假天数决定了需要哪一级别的管理者审批

### 3.2 责任链构建

```java
// 创建处理者
ApprovalHandler groupLeader = new GroupLeader();
ApprovalHandler deptManager = new DepartmentManager();
ApprovalHandler generalManager = new GeneralManager();

// 构建责任链：组长 → 部门经理 → 总经理
groupLeader.setNext(deptManager).setNext(generalManager);
```

**责任链结构**：
```
员工提交请求
    ↓
GroupLeader（≤ 3天）
    ↓（超过3天）
DepartmentManager（≤ 7天）
    ↓（超过7天）
GeneralManager（≤ 30天）
    ↓（超过30天）
请求未被处理
```

### 3.3 请求处理流程

**测试场景 1：请假 2 天**
```
提交请求 → 组长处理（2 ≤ 3，审批通过）✅
```

**测试场景 2：请假 5 天**
```
提交请求 → 组长判断（5 > 3，超出权限）→ 传递给部门经理
         → 部门经理处理（5 ≤ 7，审批通过）✅
```

**测试场景 3：请假 15 天**
```
提交请求 → 组长判断（15 > 3，超出权限）→ 传递给部门经理
         → 部门经理判断（15 > 7，超出权限）→ 传递给总经理
         → 总经理处理（15 ≤ 30，审批通过）✅
```

**测试场景 4：请假 45 天**
```
提交请求 → 组长判断（45 > 3，超出权限）→ 传递给部门经理
         → 部门经理判断（45 > 7，超出权限）→ 传递给总经理
         → 总经理判断（45 > 30，超出最大权限）→ 请求未被处理 ❌
```

### 3.4 动态链组装验证

案例还展示了责任链的**动态调整能力**：

```java
// 临时调整审批流程：跳过部门经理，组长直接上报总经理
ApprovalHandler shortChainLeader = new GroupLeader();
ApprovalHandler shortChainGeneral = new GeneralManager();
shortChainLeader.setNext(shortChainGeneral);
```

这证明了责任链模式可以在运行时灵活重组，无需修改处理者本身。

### 3.5 设计模式优势体现

| 优势 | 说明 | 代码体现 |
|------|------|----------|
| **降低耦合度** | 请求发送者无需知道哪个处理者会处理 | `Test` 类只需调用 `groupLeader.handle(request)` |
| **增强灵活性** | 可动态调整责任链的顺序和组成 | 通过 `setNext()` 动态构建不同链 |
| **符合开闭原则** | 新增处理者无需修改已有代码 | 可新增 `ProjectManager` 插入链中 |
| **符合单一职责** | 每个处理者只关注自己权限范围 | 每个 `Handler` 只判断自己的 `MAX_LEAVE_DAYS` |
| **简化对象交互** | 对象只需持有下一个处理者的引用 | `ApprovalHandler` 只需维护 `nextHandler` |

---

## 四、典型应用场景

### 4.1 过滤器链（Filter Chain）

**场景**：Web 请求需要经过多个过滤器（认证、日志、权限、限流等）

**应用**：
- Servlet Filter 与 FilterChain
- Spring Security 的 SecurityFilterChain
- Spring Cloud Gateway 的 GlobalFilter 链
- 网关中的请求预处理流程

**价值**：每个过滤器专注单一职责，可按需组合，便于扩展新过滤器

---

### 4.2 拦截器链（Interceptor Chain）

**场景**：在方法执行前后进行拦截处理（日志、性能监控、事务管理等）

**应用**：
- Spring MVC 的 HandlerInterceptor 链
- MyBatis 的 Interceptor 插件链
- AOP 中的 Advisor 链

**价值**：拦截逻辑独立于业务逻辑，可动态配置拦截顺序

---

### 4.3 审批工作流（Approval Workflow）

**场景**：业务流程需要多级审批（报销、采购、合同、人事变动等）

**应用**：
- 财务报销审批流程
- 采购订单审批流程
- 人事入职/离职审批
- 合同签署审批

**价值**：审批层级可配置，支持动态调整审批路径，符合组织架构变化

---

### 4.4 异常处理链（Exception Handling Chain）

**场景**：不同类型的异常由不同的处理器处理

**应用**：
- Spring MVC 的 HandlerExceptionResolver 链
- 全局异常处理器的优先级匹配
- 业务异常的分级处理

**价值**：异常处理逻辑解耦，可按异常类型动态路由到合适的处理器

---

### 4.5 日志记录链（Logging Chain）

**场景**：日志按级别或类型传递到不同的记录器

**应用**：
- JDK 的 Logger 按层级向父 Handler 传递
- 多级日志系统（控制台、文件、远程日志服务器）
- 审计日志的多级记录

**价值**：日志记录策略可组合，支持灵活的日志路由

---

### 4.6 数据校验链（Validation Chain）

**场景**：数据需要经过多重校验规则（格式校验、业务规则校验、权限校验等）

**应用**：
- 表单数据的多级校验
- API 请求参数的链式验证
- 数据导入时的规则校验

**价值**：校验规则独立可复用，可按业务需求动态组合校验链

---

### 4.7 事件处理链（Event Handling Chain）

**场景**：事件需要多个处理器依次处理

**应用**：
- 消息中间件的消息处理管道
- 事件驱动架构中的事件传播
- 用户操作的多级响应

**价值**：事件处理逻辑解耦，支持事件的多级消费和转换

---

## 五、总结

责任链模式通过**将请求的发送者与接收者解耦**，使多个处理者都有机会处理请求。该模式特别适合处理**多级审批、过滤器链、拦截器链**等场景，具有**高灵活性、易扩展、符合开闭原则**等优势。

在使用时应注意：
- 责任链不宜过长，否则影响性能
- 需要保证至少有一个处理者能处理请求，或做好链尾处理
- 调试时可能较难追踪请求的传递路径，需做好日志记录

---

**相关文档**：[设计模式总览](../../design-pattern.md)
