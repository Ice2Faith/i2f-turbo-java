# 状态模式（State Pattern）

> **定义**：允许一个对象在其内部状态改变时改变它的行为，对象看起来似乎修改了它的类。  
> **分类**：行为型模式（Behavioral Pattern）

---

## 一、核心逻辑

状态模式的核心思想是**"对象的行为由其内部状态决定，状态改变时行为随之改变"**。它将状态相关的行为封装到独立的状态类中，上下文对象（Context）将所有状态相关的请求委托给当前状态对象处理。

### 1.1 核心机制

- **状态委托**：Context 不直接处理状态相关的逻辑，而是将请求委托给当前的 State 对象
- **状态转换**：每个具体状态类负责定义在该状态下允许的操作，并在操作完成后决定是否切换到新状态
- **行为多态**：同一操作在不同状态下产生不同的行为（例如：`pay()` 在待支付状态执行成功，在已支付状态则拒绝）

### 1.2 设计原则体现

| 原则 | 体现方式 |
|------|---------|
| **开闭原则（OCP）** | 新增状态只需新增状态类，无需修改已有代码 |
| **单一职责原则（SRP）** | 每个状态类只负责定义该状态下的行为规则和转换逻辑 |
| **依赖倒置原则（DIP）** | Context 依赖抽象的 State 接口，而非具体状态实现 |
| **里氏替换原则（LSP）** | 所有具体状态类可替换 State 接口，不影响 Context 运行 |

### 1.3 与传统 if/else 方式对比

```
传统方式（if/else）                          状态模式（State Pattern）
───────────────────────────────────────     ───────────────────────────────────────
所有状态逻辑集中在一个类中                    每个状态独立成类，职责清晰
新增状态需修改大量条件判断（违反开闭原则）     新增状态只需新增一个状态类（符合开闭原则）
代码臃肿，难以维护                            代码分散到各状态类，易于扩展
```

---

## 二、核心组成

状态模式包含以下 3 大核心角色：

| 角色 | 接口/类 | 职责说明 | 本案例对应 |
|------|---------|---------|-----------|
| **State（抽象状态）** | [OrderState](./state/OrderState.java) | 定义一个接口，封装与 Context 特定状态相关的行为。声明所有状态相关的方法（如 `pay()`、`ship()`、`cancel()` 等） | 定义订单状态下允许的操作接口 |
| **ConcreteState（具体状态）** | [PendingState](./state/impl/PendingState.java)<br>[PaidState](./state/impl/PaidState.java)<br>[ShippingState](./state/impl/ShippingState.java)<br>[CompletedState](./state/impl/CompletedState.java)<br>[CancelledState](./state/impl/CancelledState.java) | 实现 State 接口，定义该状态下的具体行为逻辑和状态转换规则。每个状态类明确：① 允许哪些操作 ② 操作后转换到哪个状态 ③ 拒绝哪些操作并给出提示 | 5 个具体订单状态类，各自实现状态行为 |
| **Context（上下文）** | [Order](./order/Order.java) | 维护一个 State 实例表示当前状态。将所有状态相关的请求委托给当前 State 对象处理。提供 `setState()` 方法供状态对象切换状态 | 订单对象，持有当前状态并委托状态对象处理请求 |

### 模式结构图

```
OrderState（抽象状态接口）
  ├─ pay(Order): void                  ← 支付操作
  ├─ ship(Order): void                 ← 发货操作
  ├─ confirmReceive(Order): void       ← 确认收货操作
  ├─ cancel(Order): void               ← 取消订单操作
  └─ getStateName(): String            ← 获取状态名称

ConcreteState（具体状态实现）
  ├─ PendingState    → 允许支付、取消
  ├─ PaidState       → 允许发货、取消（退款）
  ├─ ShippingState   → 允许确认收货
  ├─ CompletedState  → 终态，不允许任何操作
  └─ CancelledState  → 终态，不允许任何操作

Context（上下文）
  └─ Order（订单）
      ├─ 持有 OrderState currentState
      ├─ pay() → currentState.pay(this)
      ├─ ship() → currentState.ship(this)
      ├─ confirmReceive() → currentState.confirmReceive(this)
      └─ cancel() → currentState.cancel(this)
```

---

## 三、案例设计解析

### 3.1 业务场景

以**"电商订单状态流转"**为例——订单在不同状态（待支付、已支付、配送中、已完成、已取消）下，允许执行的操作各不相同：

- **待支付**：允许支付、取消
- **已支付**：允许发货、取消（退款）
- **配送中**：只允许确认收货
- **已完成**：终态，不允许任何操作
- **已取消**：终态，不允许任何操作

### 3.2 状态流转规则

```
待支付 ──支付──→ 已支付 ──发货──→ 配送中 ──确认收货──→ 已完成
  │                │
  └────取消────────┘
```

### 3.3 代码执行流程

以正常订单流程（待支付 → 已支付 → 配送中 → 已完成）为例：

```java
// 1. 创建订单，初始状态为 PendingState
Order order = new Order("ORD-001", "iPhone 16 Pro", 8999.00);
// → currentState = new PendingState()

// 2. 调用支付操作
order.pay();
// → 委托：currentState.pay(this)
// → PendingState.pay() 执行：
//     ✓ 打印"支付成功"
//     ✓ 调用 order.setState(new PaidState()) 切换到已支付状态

// 3. 调用发货操作
order.ship();
// → 委托：currentState.ship(this)
// → PaidState.ship() 执行：
//     ✓ 打印"发货成功"
//     ✓ 调用 order.setState(new ShippingState()) 切换到配送中状态

// 4. 调用确认收货操作
order.confirmReceive();
// → 委托：currentState.confirmReceive(this)
// → ShippingState.confirmReceive() 执行：
//     ✓ 打印"确认收货成功"
//     ✓ 调用 order.setState(new CompletedState()) 切换到已完成状态

// 5. 尝试在已完成状态下执行操作（均被拒绝）
order.pay();
// → 委托：currentState.pay(this)
// → CompletedState.pay() 执行：
//     ✗ 打印"操作失败：订单已完成，无法再次支付"
```

### 3.4 设计模式应用要点

#### （1）委托机制

[Order](./order/Order.java#L91-L118) 类中的所有状态相关操作都采用**委托模式**：

```java
public void pay() {
    currentState.pay(this);  // 委托给当前状态对象
}
```

客户端无需编写任何 if/else 判断，Order 自动将请求转发给正确的状态对象处理。

#### （2）状态转换控制

状态转换逻辑封装在各个具体状态类中，例如 [PendingState](./state/impl/PendingState.java#L29-L33)：

```java
@Override
public void pay(Order order) {
    System.out.println("  ✓ 支付成功！订单金额 ¥" + order.getAmount() + " 已支付");
    order.setState(new PaidState());  // 状态转换：待支付 → 已支付
}
```

状态对象拥有完整的控制权：
- 决定操作是否合法
- 执行操作后的业务逻辑
- 决定是否切换到新状态，以及切换到哪个状态

#### （3）终态设计

[CompletedState](./state/impl/CompletedState.java) 和 [CancelledState](./state/impl/CancelledState.java) 作为**终态（Terminal State）**，所有操作均被拒绝并给出友好提示：

```java
@Override
public void pay(Order order) {
    System.out.println("  ✗ 操作失败：订单已完成，无法再次支付");
    System.out.println("  提示：当前状态为" + order.getStateName());
}
```

#### （4）客户端透明性

[Test](./Test.java) 演示中，客户端代码极其简洁：

```java
Order order = new Order("ORD-001", "iPhone 16 Pro", 8999.00);
order.pay();           // 自动判断是否允许支付
order.ship();          // 自动判断是否允许发货
order.confirmReceive();// 自动判断是否允许确认收货
order.cancel();        // 自动判断是否允许取消
```

客户端无需关心当前处于哪个状态、该状态允许哪些操作，全部由状态模式自动处理。

---

## 四、典型应用场景

状态模式适用于**"对象的行为依赖于其内部状态，且状态数量较多、转换规则复杂"**的场景。以下是 6 大类典型应用：

### 4.1 工作流引擎

- **场景**：审批流程、工单流转、合同审批等
- **状态**：草稿 → 待审批 → 审批中 → 已通过/已驳回
- **优势**：每个审批节点的行为和流转规则独立封装，新增审批节点无需修改已有代码
- **案例**：Activiti、Flowable、Camunda 等工作流引擎

### 4.2 订单/交易状态管理

- **场景**：电商订单、支付交易、物流跟踪
- **状态**：待支付 → 已支付 → 配送中 → 已完成/已取消/退款中
- **优势**：避免大量 if/else 判断状态合法性，状态转换规则显式化
- **案例**：本案例的电商订单系统

### 4.3 游戏开发中的角色状态

- **场景**：游戏角色的状态切换
- **状态**：站立 → 行走 → 跑步 → 跳跃 → 攻击 → 受伤 → 死亡
- **优势**：每个状态独立定义可执行的动作和状态转换，易于扩展新状态
- **案例**：游戏引擎中的状态机（FSM）

### 4.4 网络连接/会话状态

- **场景**：TCP 连接、WebSocket、用户会话管理
- **状态**：CLOSED → CONNECTING → CONNECTED → DISCONNECTING → CLOSED
- **优势**：不同连接状态下允许的操作不同（如未连接时不能发送数据）
- **案例**：`java.net.Socket` 内部状态管理、Netty 的 Channel 状态

### 4.5 UI 组件状态

- **场景**：按钮、表单、对话框的交互状态
- **状态**：正常 → 悬停 → 按下 → 禁用 → 加载中
- **优势**：不同状态下组件的渲染和行为不同，状态模式使 UI 逻辑清晰
- **案例**：Android View 状态管理、前端组件库的状态机

### 4.6 设备/系统生命周期

- **场景**：虚拟机、容器、线程的生命周期管理
- **状态**：NEW → RUNNABLE → BLOCKED → WAITING → TIMED_WAITING → TERMINATED
- **优势**：生命周期各阶段的行为和转换规则独立，便于管理和监控
- **案例**：`Thread.State` 枚举、Spring `AbstractApplicationContext` 生命周期（active/closed）

---

## 五、优势与注意事项

### 5.1 优势

1. **遵循开闭原则**：新增状态只需新增状态类，无需修改已有代码
2. **遵循单一职责**：每个状态类只负责定义该状态下的行为规则
3. **消除大量 if/else**：状态转换逻辑分散到各状态类，代码清晰易维护
4. **状态转换显式化**：每个状态类明确定义可转换的下一个状态
5. **客户端面向抽象编程**：Context 无需知道具体状态类型，委托即可
6. **易于扩展**：新增状态（如"退款中"）不影响现有状态逻辑

### 5.2 注意事项

1. **状态类数量膨胀**：如果状态过多（如 >10 个），会导致类数量激增。可考虑结合状态表或枚举优化
2. **状态转换逻辑分散**：状态转换规则分散在各个状态类中，难以全局查看完整流转图。建议配合状态机图或文档说明
3. **共享状态实例**：如果状态对象无内部状态（仅行为），可使用单例或享元模式共享状态实例，减少对象创建开销
4. **与策略模式的区别**：
   - **策略模式**：客户端主动选择算法/策略（如选择排序算法）
   - **状态模式**：状态转换由状态对象或 Context 自动控制（如订单状态自动流转）

---

## 六、框架中的典型应用

| 框架 | 应用场景 | 说明 |
|------|---------|------|
| **JDK** | `java.net.Socket` | 连接状态（CLOSED/BOUND/CONNECTED）管理 |
| **JDK** | `java.util.Iterator` | hasNext/next 状态切换 |
| **Spring** | `AbstractApplicationContext` | 生命周期状态（active/closed） |
| **Spring Batch** | `JobExecution` | 作业状态流转（STARTING → STARTED → COMPLETED/FAILED） |
| **Spring StateMachine** | 通用状态机框架 | 提供声明式状态机定义和转换规则 |

---

> **总结**：状态模式通过将状态相关的行为封装到独立的状态类中，消除了复杂的条件判断，使代码更符合开闭原则和单一职责原则。适用于状态数量较多、转换规则复杂的场景，是管理工作流、订单流转、生命周期等状态机的利器。
