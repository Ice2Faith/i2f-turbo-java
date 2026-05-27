# 观察者模式（Observer Pattern）

> 观察者模式定义对象间的一种**一对多的依赖关系**，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。该模式又称**发布-订阅模式（Publish-Subscribe）**，是行为型设计模式中最常用的模式之一。

---

## 一、核心逻辑

### 1.1 发布-订阅机制

观察者模式的核心是**发布-订阅机制**：
- **目标对象（Subject）** 维护一个观察者列表，充当"发布者"角色
- **观察者（Observer）** 注册到目标对象，充当"订阅者"角色
- 当目标状态变化时，主动**推送（Push）** 通知给所有已注册的观察者
- 观察者接收通知后，从目标对象**拉取（Pull）** 最新状态并执行更新

### 1.2 松耦合设计

该模式实现了**目标与观察者之间的松耦合**：
- 目标对象只需知道观察者实现了 `Observer` 接口，无需了解具体类型
- 观察者之间互不知晓，独立接收通知并执行各自逻辑
- 新增或删除观察者无需修改目标对象代码，符合**开闭原则（OCP）**
- 目标与观察者都依赖抽象接口，符合**依赖倒置原则（DIP）**

### 1.3 动态订阅管理

观察者模式支持**运行时动态管理订阅关系**：
- 可以随时添加新观察者（`attach`）
- 可以随时移除观察者（`detach`）
- 通知机制自动适配当前订阅列表，无需重启系统

### 1.4 广播通信

一次状态更新触发**广播式通知**：
- 目标对象遍历观察者列表，依次调用 `update()` 方法
- 所有观察者同时接收到相同的事件通知
- 各观察者根据自身职责决定如何处理通知

---

## 二、核心组成

观察者模式包含 **4 大核心角色**，在本实现中对应如下：

### 2.1 Subject（抽象目标）

**对应类：** [`Subject.java`](./subject/Subject.java)

**职责：**
- 定义观察者管理接口（注册、移除、通知）
- 维护观察者列表的抽象契约
- 不依赖任何具体观察者实现

**核心方法：**
```java
void attach(Observer observer);      // 注册观察者
void detach(Observer observer);      // 移除观察者
void notifyObservers();              // 通知所有观察者
```

### 2.2 ConcreteSubject（具体目标）

**对应类：** [`WeatherStation.java`](./subject/WeatherStation.java)

**职责：**
- 实现 `Subject` 接口，维护具体的观察者列表（`List<Observer>`）
- 管理自身业务状态（温度、湿度、气压）
- 状态变化时自动触发 `notifyObservers()` 推送通知

**核心逻辑：**
```java
public void setMeasurements(double temperature, double humidity, double pressure) {
    this.temperature = temperature;
    this.humidity = humidity;
    this.pressure = pressure;
    // 数据更新后，自动通知所有观察者
    notifyObservers();
}
```

### 2.3 Observer（抽象观察者）

**对应类：** [`Observer.java`](./observer/Observer.java)

**职责：**
- 定义统一的更新接口 `update(Subject subject)`
- 通过 `Subject` 参数获取目标最新状态
- 所有具体观察者必须实现此接口

**核心方法：**
```java
void update(Subject subject);  // 接收通知并更新
```

### 2.4 ConcreteObserver（具体观察者）

**对应类：**
- [`PhoneDisplay.java`](./observer/impl/PhoneDisplay.java) - 手机显示屏
- [`ComputerDisplay.java`](./observer/impl/ComputerDisplay.java) - 电脑显示屏
- [`StatisticsDisplay.java`](./observer/impl/StatisticsDisplay.java) - 统计显示屏

**职责：**
- 实现 `Observer` 接口，订阅目标对象的状态变化
- 在 `update()` 方法中从目标获取最新数据
- 根据自身职责执行不同的更新逻辑（显示格式、统计计算等）

**各观察者差异：**
| 观察者 | 关注数据 | 更新逻辑 | 显示特点 |
|--------|---------|---------|---------|
| PhoneDisplay | 温度、湿度 | 简洁显示 | 适合手机小屏幕 |
| ComputerDisplay | 温度、湿度、气压 | 完整显示 | 利用大屏展示全部信息 |
| StatisticsDisplay | 温度 + 历史记录 | 统计极值 | 维护最高/最低温度、更新次数 |

---

## 三、案例设计解析

### 3.1 业务场景

本案例实现了一个**气象站监测系统**：
- **气象站（WeatherStation）** 作为被观察者，持续监测温度、湿度、气压等气象数据
- **多个显示设备** 作为观察者订阅气象站数据：
  - 📱 手机显示屏：简洁显示核心数据
  - 💻 电脑显示屏：详细显示完整数据
  - 📊 统计显示屏：记录历史极值并统计

### 3.2 模式使用流程

通过 [`Test.java`](./Test.java) 演示了观察者模式的完整使用流程：

#### 步骤 1：创建目标与观察者
```java
// 创建气象站（被观察者）
WeatherStation weatherStation = new WeatherStation(25.0, 60.0, 1013.0);

// 创建显示设备（观察者）
PhoneDisplay phoneDisplay = new PhoneDisplay();
ComputerDisplay computerDisplay = new ComputerDisplay();
StatisticsDisplay statisticsDisplay = new StatisticsDisplay();
```

#### 步骤 2：注册观察者（订阅）
```java
weatherStation.attach(phoneDisplay);
weatherStation.attach(computerDisplay);
weatherStation.attach(statisticsDisplay);
```

#### 步骤 3：状态更新触发自动通知
```java
// 气象站数据更新，自动通知所有观察者
weatherStation.setMeasurements(28.5, 65.0, 1012.5);
```

**内部执行流程：**
```
setMeasurements() 
  → 更新温度、湿度、气压
  → 调用 notifyObservers()
    → 遍历 observers 列表
      → phoneDisplay.update(this)      → 显示手机格式
      → computerDisplay.update(this)   → 显示电脑格式
      → statisticsDisplay.update(this) → 更新统计并显示
```

#### 步骤 4：动态管理订阅关系
```java
// 取消订阅
weatherStation.detach(phoneDisplay);

// 后续更新不再通知手机显示屏
weatherStation.setMeasurements(26.0, 62.0, 1013.5);
```

#### 步骤 5：面向抽象编程
```java
// 通过 Observer 接口统一管理不同类型观察者
Observer[] displays = {new PhoneDisplay(), new ComputerDisplay(), new StatisticsDisplay()};
for (Observer display : displays) {
    newStation.attach(display);
}
```

### 3.3 设计模式优势体现

本案例充分展示了观察者模式的核心优势：

1. **开闭原则（OCP）**：新增显示设备（如平板显示屏）只需实现 `Observer` 接口，无需修改 `WeatherStation` 代码
2. **松耦合**：气象站不依赖任何具体显示设备，只依赖 `Observer` 抽象接口
3. **广播通信**：一次数据更新，自动通知所有订阅者
4. **动态订阅**：运行时可随时添加或移除观察者
5. **依赖倒置（DIP）**：高层模块（气象站）和低层模块（显示设备）都依赖抽象

### 3.4 与 Spring 事件机制对比

本实现与 Spring 框架的事件机制高度对应：

| 本示例 | Spring 事件机制 | 说明 |
|--------|----------------|------|
| `Subject` | `ApplicationEventPublisher` | 事件发布器 |
| `Observer` | `ApplicationListener` | 事件监听器 |
| `WeatherStation` | 事件发布者（Service 层） | 实际发布事件的业务组件 |
| `notifyObservers()` | `publishEvent(event)` | 发布事件触发通知 |
| `update(subject)` | `onApplicationEvent(event)` | 处理事件回调 |

---

## 四、典型应用场景

观察者模式在实际开发中应用广泛，以下是最常见的典型场景：

### 4.1 事件驱动系统

**场景描述：** GUI 界面、Web 应用中的事件处理机制

**典型案例：**
- **Swing/JavaFX**：按钮点击、窗口关闭等事件监听
  ```java
  button.addActionListener(e -> System.out.println("按钮被点击"));
  ```
- **Servlet**：`ServletContextListener`、`HttpSessionListener` 监听 Web 应用生命周期
- **Spring Boot**：生命周期事件监听（`ApplicationStartingEvent`、`ApplicationReadyEvent`）

**业务价值：** 将事件源与事件处理逻辑解耦，支持灵活扩展新的事件处理器

### 4.2 消息发布/订阅系统

**场景描述：** 消息队列、事件总线、实时数据推送

**典型案例：**
- **Spring ApplicationEvent**：组件间异步通信
  ```java
  @EventListener
  public void handleOrderCreated(OrderCreatedEvent event) {
      // 发送通知、更新库存、记录日志等
  }
  ```
- **Guava EventBus**：轻量级事件总线
- **Kafka/RabbitMQ**：分布式消息中间件（生产者-消费者模式的扩展）
- **WebSocket 推送**：实时通知、聊天室、股票行情推送

**业务价值：** 实现系统组件间的松耦合通信，支持一对多消息分发

### 4.3 数据同步与缓存更新

**场景描述：** 数据库变更通知、缓存失效、配置热更新

**典型案例：**
- **缓存失效通知**：数据库记录更新后，通知缓存组件清除对应缓存
- **配置中心**：配置变更时，通知所有服务实例重新加载配置（如 Nacos、Apollo）
- **Redis Pub/Sub**：发布订阅模式实现跨节点数据同步

**业务价值：** 保证数据一致性，避免轮询带来的性能损耗

### 4.4 响应式编程

**场景描述：** 数据流处理、异步事件流、Reactive Streams

**典型案例：**
- **Reactor（Spring WebFlux）**：`Mono<T>`、`Flux<T>` 响应式流
  ```java
  flux.subscribe(
      data -> System.out.println("收到数据: " + data),
      error -> System.err.println("发生错误: " + error),
      () -> System.out.println("数据流完成")
  );
  ```
- **RxJava**：观察者模式的响应式扩展
- **Java 9+ Flow API**：标准的响应式流规范

**业务价值：** 处理异步数据流，支持背压（Backpressure）和流式计算

### 4.5 日志与监控系统

**场景描述：** 日志收集、指标监控、告警通知

**典型案例：**
- **日志框架**：Logback、Log4j2 的 Appender 机制（日志事件通知多个输出目标）
- **监控告警**：系统指标超阈值时，通知邮件、短信、钉钉等多个告警渠道
- **Spring Boot Actuator**：健康检查、指标采集的观察者机制

**业务价值：** 统一事件源，灵活扩展多种监控/告警方式

### 4.6 工作流与状态机

**场景描述：** 订单状态流转、审批流程、业务状态变更通知

**典型案例：**
- **订单状态变更**：订单从"待支付"→"已支付"→"已发货"时，通知库存、物流、财务等模块
- **Spring StateMachine**：状态机框架中的状态转换监听
- **审批流程**：审批节点状态变化时，通知申请人、下一个审批人

**业务价值：** 状态变更与后续动作解耦，易于新增状态处理逻辑

### 4.7 数据绑定与 MVC 架构

**场景描述：** 视图与模型的数据同步、MVVM 双向绑定

**典型案例：**
- **JavaFX Property Binding**：UI 控件与数据模型的双向绑定
- **前端框架**：Vue、Angular、React 的响应式数据绑定
- **MVC 模式**：Model 数据变化时自动更新 View

**业务价值：** 视图与数据模型解耦，数据变化自动反映到 UI

---

## 五、模式变体

### 5.1 Push 模式 vs Pull 模式

- **Push 模式**：目标对象将详细数据作为参数传递给观察者（通知中包含完整数据）
- **Pull 模式**：目标对象只发送简单通知，观察者主动从目标拉取所需数据（本示例采用）

**本示例采用 Pull 模式：**
```java
// 目标只传递自身引用
observer.update(this);

// 观察者按需获取数据
WeatherStation station = (WeatherStation) subject;
this.temperature = station.getTemperature();
```

### 5.2 同步通知 vs 异步通知

- **同步通知**：目标对象依次调用观察者的 `update()` 方法（本示例）
- **异步通知**：通过线程池或事件总线异步分发通知（如 Spring 的 `@Async` + `@EventListener`）

---

## 六、注意事项

### 6.1 性能考虑

- 观察者数量过多时，同步通知可能导致性能瓶颈
- 观察者的 `update()` 方法应避免耗时操作，必要时采用异步通知
- 注意观察者的执行顺序（可引入优先级机制）

### 6.2 循环依赖风险

- 避免观察者更新时再次触发目标状态变化，导致无限循环
- 注意观察者之间的间接依赖关系

### 6.3 内存泄漏

- 观察者不再使用时必须调用 `detach()` 移除，否则会导致内存泄漏
- 在 Spring 中注意 `@EventListener` 的生命周期管理

### 6.4 错误隔离

- 某个观察者的异常不应影响其他观察者的通知
- 建议在 `notifyObservers()` 中添加异常捕获：
  ```java
  for (Observer observer : observers) {
      try {
          observer.update(this);
      } catch (Exception e) {
          log.error("观察者更新失败", e);
      }
  }
  ```

---

## 七、总结

观察者模式通过**发布-订阅机制**实现了对象间的**松耦合通信**，是事件驱动架构的基石。本案例通过气象站系统清晰展示了：

1. **核心机制**：目标维护观察者列表，状态变化时自动广播通知
2. **设计优势**：开闭原则、依赖倒置、动态订阅、广播通信
3. **应用广泛**：从 GUI 事件到分布式消息系统，从缓存更新到响应式编程

在现代 Java 开发中，观察者模式的理念已深度融入 Spring 事件机制、响应式编程、消息中间件等基础设施，是每位开发者必须掌握的核心设计模式。

> **设计原则体现**：开闭原则（OCP）、依赖倒置原则（DIP）、迪米特法则（LoD）
