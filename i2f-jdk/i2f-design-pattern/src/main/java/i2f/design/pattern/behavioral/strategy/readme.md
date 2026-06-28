# 策略模式（Strategy Pattern）

> **定义**：定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换。本模式使得算法可独立于使用它的客户而变化。
> 
> **模式分类**：行为型模式（Behavioral Pattern）

---

## 一、核心逻辑

策略模式的核心思想是**将可变的行为（算法）抽象为独立策略，使算法与使用算法的客户端解耦**。

### 关键机制

1. **算法封装**：每种算法独立封装在具体策略类中，遵循单一职责原则
2. **面向接口编程**：上下文（Context）持有抽象策略接口引用，不依赖具体实现
3. **运行时切换**：在程序运行期间可以动态替换策略，无需修改上下文代码
4. **消除条件分支**：用多态替代 if-else/switch 分支判断，符合开闭原则

### 核心优势

- **可扩展性**：新增策略只需新增类，无需修改已有代码（开闭原则）
- **可维护性**：每个策略独立封装，算法修改不影响其他策略
- **可测试性**：策略可独立进行单元测试
- **灵活性**：运行时根据业务场景动态选择或切换策略

---

## 二、核心组成

策略模式由三个核心角色组成：

### 1. 抽象策略（Strategy）

定义所有具体策略的公共接口，声明算法方法。

**本包实现**：[PaymentStrategy](./payment/PaymentStrategy.java)

```java
public interface PaymentStrategy {
    String pay(double amount);           // 执行支付
    String getPaymentName();             // 获取支付方式名称
    double calculateFee(double amount);  // 计算手续费（不同策略算法不同）
}
```

### 2. 具体策略（Concrete Strategy）

实现抽象策略接口，封装具体的算法实现。

**本包实现**：
- [AliPay](./payment/impl/AliPay.java) - 支付宝支付（手续费 0.6%）
- [WeChatPay](./payment/impl/WeChatPay.java) - 微信支付（手续费 0.6%）
- [CreditCardPay](./payment/impl/CreditCardPay.java) - 信用卡支付（手续费 2.5%，支持分期）

每个具体策略独立封装了自己的支付流程和手续费计算逻辑，互不影响。

### 3. 上下文（Context）

持有策略接口引用，通过该引用调用具体策略的算法。上下文不关心具体策略的实现细节。

**本包实现**：[OrderContext](./order/OrderContext.java)

```java
public class OrderContext {
    private PaymentStrategy paymentStrategy;  // 策略引用（可动态切换）
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;      // 运行时动态切换策略
    }
    
    public String checkout(double amount) {
        // 委托给具体策略执行支付
        return paymentStrategy.pay(amount);
    }
}
```

---

## 三、案例设计说明

### 场景背景

以**电商支付系统**为场景：用户在订单结算时可以选择不同的支付方式（支付宝、微信、信用卡），每种支付方式的支付流程、手续费计算、第三方接口调用等算法各不相同。

### 设计思路

#### 传统 if-else 写法（反面案例）

```java
public class OrderService {
    public String checkout(String orderNo, double amount, String paymentType) {
        if ("alipay".equals(paymentType)) {
            // 支付宝支付逻辑（手续费计算、调用支付宝接口...）
        } else if ("wechat".equals(paymentType)) {
            // 微信支付逻辑（手续费计算、调用微信接口...）
        } else if ("credit".equals(paymentType)) {
            // 信用卡支付逻辑（手续费计算、调用银行网关...）
        }
        // 新增支付方式？→ 必须修改此方法（违反开闭原则）
    }
}
```

**问题**：
- 违反开闭原则：新增支付方式必须修改已有代码
- 违反单一职责：一个方法承担多种支付逻辑
- 代码冗长：随着支付方式增多，if-else 分支膨胀
- 难以测试：无法单独测试某种支付方式

#### 策略模式写法（本包实现）

```java
// 1. 创建订单上下文
OrderContext order = new OrderContext("ORDER-20260521-001");

// 2. 设置支付策略（运行时动态选择）
order.setPaymentStrategy(new AliPay());

// 3. 执行结算（上下文委托给具体策略）
order.checkout(1000.00);

// 4. 运行时可动态切换策略
order.setPaymentStrategy(new WeChatPay());  // 用户改变主意
order.checkout(1000.00);
```

**优势**：
- ✅ 符合开闭原则：新增支付方式只需新增策略类，无需修改已有代码
- ✅ 符合单一职责：每个策略类只负责一种支付算法
- ✅ 避免条件分支：用多态替代 if-else，代码更简洁
- ✅ 算法独立演化：各支付策略互不影响，可独立测试和维护
- ✅ 运行时切换：同一订单可在运行时更换支付方式

### 运行演示

执行 [Test.java](./Test.java) 可看到：

1. **不同订单使用不同支付策略**：订单1用支付宝、订单2用微信、订单3用信用卡
2. **同一订单动态切换策略**：订单4先用支付宝支付，后改为微信支付
3. **面向抽象编程**：通过 `PaymentStrategy[]` 数组批量处理不同策略的订单

### 类图结构

```
┌──────────────────────────────────────────────┐
│          PaymentStrategy（抽象策略）            │
│  ┌─────────────────────────────────────┐     │
│  │ + pay(amount: double): String       │     │
│  │ + getPaymentName(): String          │     │
│  │ + calculateFee(amount: double): double│   │
│  └─────────────────────────────────────┘     │
└──────────────────────────────────────────────┘
                    ▲
                    │ implements
        ┌───────────┼───────────┐
        │           │           │
┌───────┴──────┐ ┌──┴────────┐ ┌┴──────────────┐
│   AliPay     │ │WeChatPay  │ │CreditCardPay  │
│ (支付宝支付)  │ │(微信支付)  │ │ (信用卡支付)   │
│ 手续费 0.6%  │ │手续费 0.6% │ │ 手续费 2.5%    │
└──────────────┘ └───────────┘ └───────────────┘

┌──────────────────────────────────────────────┐
│          OrderContext（上下文）                │
│  ┌─────────────────────────────────────┐     │
│  │ - paymentStrategy: PaymentStrategy  │─────┼── 持有策略引用
│  │ - orderNo: String                   │     │
│  ├─────────────────────────────────────┤     │
│  │ + setPaymentStrategy(strategy)      │     │
│  │ + checkout(amount: double): String  │─────┼── 委托策略执行
│  └─────────────────────────────────────┘     │
└──────────────────────────────────────────────┘
```

---

## 四、常见应用场景

策略模式在实际开发中应用广泛，以下是典型场景：

### 1. 排序算法选择

**场景**：根据数据规模和特征选择不同的排序策略（快速排序、归并排序、冒泡排序等）。

```java
// JDK 案例：java.util.Comparator 作为排序策略接口
List<User> users = getUsers();
users.sort(Comparator.comparing(User::getAge));        // 按年龄排序
users.sort(Comparator.comparing(User::getName));       // 按姓名排序
```

### 2. 线程池拒绝策略

**场景**：当线程池队列满时，采用不同的拒绝策略（丢弃、抛出异常、调用者运行等）。

```java
// JDK 案例：ThreadPoolExecutor 的 RejectedExecutionHandler
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    10, 20, 60, TimeUnit.SECONDS, 
    new ArrayBlockingQueue<>(100),
    new ThreadPoolExecutor.AbortPolicy()      // 策略1：抛出异常
    // new ThreadPoolExecutor.CallerRunsPolicy()  // 策略2：调用者运行
    // new ThreadPoolExecutor.DiscardPolicy()     // 策略3：静默丢弃
);
```

### 3. 加密算法切换

**场景**：系统支持多种加密算法（BCrypt、Argon2、SHA256 等），根据安全级别动态选择。

```java
// Spring Security 案例：PasswordEncoder 作为加密策略
PasswordEncoder encoder = new BCryptPasswordEncoder();  // BCrypt 策略
String hash = encoder.encode(password);

// 升级为更安全的 Argon2
PasswordEncoder encoder = new Argon2PasswordEncoder();  // Argon2 策略
```

### 4. 缓存策略

**场景**：根据业务场景选择不同的缓存策略（Redis、本地缓存、多级缓存等）。

```java
// Spring Cache 案例：CacheManager 作为缓存策略
CacheManager redisCacheManager = new RedisCacheManager(...);
CacheManager caffeineCacheManager = new CaffeineCacheManager(...);
```

### 5. 路由/负载均衡策略

**场景**：微服务调用时选择不同的负载均衡策略（轮询、随机、权重、一致性哈希等）。

```java
// Spring Cloud LoadBalancer 案例
@LoadBalancerClient(name = "user-service", configuration = LoadBalancerConfig.class)
// 配置不同负载均衡策略：RoundRobin、Random、WeightedResponseTime
```

### 6. 数据导出格式

**场景**：报表导出支持多种格式（Excel、CSV、PDF、JSON），每种格式使用不同的导出策略。

```java
// 本系统可应用：异步导出模块
ExportStrategy excelStrategy = new ExcelExportStrategy();
ExportStrategy csvStrategy = new CsvExportStrategy();
context.setExportStrategy(excelStrategy);
context.export(data);
```

### 7. 压缩算法

**场景**：文件压缩支持多种算法（GZIP、ZIP、BZIP2、LZ4），根据压缩率和速度要求选择。

```java
CompressionStrategy gzip = new GzipCompression();
CompressionStrategy zip = new ZipCompression();
context.setCompressionStrategy(gzip);
context.compress(file);
```

### 8. 消息序列化/反序列化

**场景**：消息队列中数据的序列化方式（JSON、Protobuf、Avro、Hessian）。

```java
SerializationStrategy json = new JsonSerialization();
SerializationStrategy protobuf = new ProtobufSerialization();
context.setSerializationStrategy(json);
byte[] bytes = context.serialize(message);
```

---

## 五、与工厂方法模式的对比

| 维度 | 工厂方法模式（Factory Method） | 策略模式（Strategy） |
|------|-------------------------------|---------------------|
| **关注点** | 对象的创建过程 | 算法/行为的封装与替换 |
| **解决的问题** | 如何创建对象 | 如何选择算法 |
| **核心机制** | 由子类决定创建哪种产品 | 由上下文在运行时选择使用哪种策略 |
| **模式类型** | 创建型模式 | 行为型模式 |
| **典型案例** | `Calendar.getInstance()` | `Comparator` 排序策略 |

**两者结合使用**：工厂方法可用于创建策略对象，策略模式负责算法替换。

---

## 六、注意事项

### 适用条件

- ✅ 系统有多个类，它们之间的区别仅在于行为不同
- ✅ 需要在运行时动态选择算法或行为
- ✅ 避免代码中出现大量的条件分支判断
- ✅ 算法需要独立于使用它的客户端演化

### 不适用的场景

- ❌ 系统中只有少量固定算法，且不会扩展（过度设计）
- ❌ 算法之间有复杂的依赖关系，无法独立封装
- ❌ 客户端必须知道所有具体策略类（违背封装原则）

### 最佳实践

1. **配合工厂模式**：使用工厂方法或简单工厂创建策略实例，避免客户端直接依赖具体策略类
2. **策略注册表**：结合 Map 或 Spring 容器管理策略实例，通过策略名称/类型动态获取
3. **默认策略**：在上下文中设置默认策略，避免未设置策略时的空指针异常
4. **策略组合**：复杂场景可将多个策略组合使用（如责任链 + 策略模式）

---

## 七、总结

策略模式通过**将算法封装为独立的策略类**，实现了算法与客户端的解耦，是消除 if-else 分支、提升代码可扩展性的利器。

**核心价值**：
- 🎯 **开闭原则**：对扩展开放（新增策略），对修改关闭（无需改动已有代码）
- 🎯 **单一职责**：每个策略类只负责一种算法实现
- 🎯 **运行时灵活**：上下文可在运行时动态切换策略
- 🎯 **独立演化**：算法之间互不影响，可独立测试和维护

**记住**：当你的代码中出现超过 3 个 if-else 分支，且每个分支执行不同的算法逻辑时，就应该考虑使用策略模式重构。
