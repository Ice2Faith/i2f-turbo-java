# 工厂方法模式（Factory Method Pattern）

> **定义**：定义一个用于创建对象的接口，让子类决定实例化哪一个类，使一个类的实例化延迟到其子类。  
> **分类**：创建型模式（Creational Pattern）  
> **核心思想**：将对象的创建与使用解耦，通过子类化来扩展创建逻辑，而非修改现有代码。

---

## 一、核心逻辑

工厂方法模式的核心逻辑可以概括为以下三点：

### 1. 延迟实例化到子类

父类（抽象创建者）定义一个创建对象的接口（工厂方法），但**不实现**具体的创建逻辑，而是将实现延迟到子类。这使得父类可以专注于业务流程，而将"创建什么"的决定权交给子类。

### 2. 面向抽象编程

客户端代码仅依赖抽象产品接口，不关心具体产品的类型。通过多态机制，同一个业务方法可以处理不同类型的产品，实现**创建与使用的完全解耦**。

### 3. 遵循开闭原则

- **对扩展开放**：新增产品类型时，只需新增对应的具体工厂子类，无需修改已有代码。
- **对修改关闭**：现有工厂和产品的代码保持稳定，不会因新增需求而被修改。

---

## 二、核心组成

工厂方法模式由四个核心角色组成：

| 角色 | 英文名称 | 职责 | 本包对应类 |
|------|---------|------|-----------|
| **抽象产品** | Abstract Product | 定义产品的公共接口和通用属性 | [`Transport`](./transport/Transport.java) |
| **具体产品** | Concrete Product | 实现抽象产品接口，提供具体产品行为 | [`Truck`](./transport/impl/Truck.java)、[`Ship`](./transport/impl/Ship.java) |
| **抽象创建者** | Abstract Creator | 声明工厂方法，定义业务逻辑框架 | [`Logistics`](./logistics/Logistics.java) |
| **具体创建者** | Concrete Creator | 重写工厂方法，返回具体产品实例 | [`RoadLogistics`](./logistics/impl/RoadLogistics.java)、[`SeaLogistics`](./logistics/impl/SeaLogistics.java) |

### 类层次结构

```
抽象产品                  具体产品
─────────────────        ─────────────────────────────────
Transport                Truck（卡车 —— 公路运输）
                         Ship（轮船 —— 海洋运输）

抽象创建者                具体创建者
─────────────────        ─────────────────────────────────
Logistics                RoadLogistics → 创建 Truck
                         SeaLogistics  → 创建 Ship
```

---

## 三、案例设计解析

### 场景背景

本包以**物流运输系统**为演示场景：物流公司需要根据不同的运输方式（公路、海运）创建对应的运输工具（卡车、轮船），并完成配送任务。

### 设计实现

#### 1. 抽象产品层：Transport

[`Transport`](./transport/Transport.java) 定义了所有运输工具的公共属性（名称、载重量、速度）和抽象方法（`deliver` 配送、`getType` 获取类型），是客户端操作的产品接口。

```java
@Data
@NoArgsConstructor
public abstract class Transport {
    private String name;        // 运输工具名称
    private double capacity;    // 最大载重量（吨）
    private double speed;       // 最大速度（km/h）
    
    public abstract String deliver(String destination);  // 配送任务
    public abstract String getType();                    // 获取类型
}
```

#### 2. 具体产品层：Truck / Ship

[`Truck`](./transport/impl/Truck.java) 和 [`Ship`](./transport/impl/Ship.java) 分别实现公路卡车和远洋轮船的具体行为：

- **Truck**：新增 `plateNumber`（车牌号）属性，实现公路配送逻辑
- **Ship**：新增 `vesselCode`（船舶编号）属性，实现海运配送逻辑

```java
// Truck 实现示例
@Override
public String deliver(String destination) {
    return String.format("[卡车·%s] 通过公路运输 %.1f 吨货物，以 %.0f km/h 的速度驶向【%s】",
            getName(), getCapacity(), getSpeed(), destination);
}
```

#### 3. 抽象创建者层：Logistics

[`Logistics`](./logistics/Logistics.java) 是整个模式的核心，定义了两个关键方法：

- **`createTransport()`**：工厂方法（抽象），由子类决定创建哪种运输工具
- **`planDelivery(destination)`**：业务方法，调用工厂方法获取产品并执行配送

```java
public abstract class Logistics {
    // 工厂方法 —— 子类决定创建什么
    public abstract Transport createTransport();
    
    // 业务方法 —— 使用工厂方法创建的产品
    public void planDelivery(String destination) {
        Transport transport = createTransport();  // 面向抽象编程
        System.out.println("物流公司：" + getCompanyName());
        System.out.println("运输工具：" + transport);
        System.out.println("配送执行：" + transport.deliver(destination));
    }
}
```

#### 4. 具体创建者层：RoadLogistics / SeaLogistics

[`RoadLogistics`](./logistics/impl/RoadLogistics.java) 和 [`SeaLogistics`](./logistics/impl/SeaLogistics.java) 重写工厂方法，分别创建卡车和轮船：

```java
// RoadLogistics 实现
@Override
public Transport createTransport() {
    return new Truck("重型卡车-A1", 20.0, 80.0, "京A·88888");
}

// SeaLogistics 实现
@Override
public Transport createTransport() {
    return new Ship("远洋巨轮-东方号", 5000.0, 30.0, "COSCO-2026");
}
```

#### 5. 客户端调用：Test

[`Test`](./Test.java) 演示了工厂方法模式的三大使用场景：

```java
// ① 直接使用具体工厂
Logistics roadLogistics = new RoadLogistics();
roadLogistics.planDelivery("北京市朝阳区");

// ② 面向抽象编程 —— 客户端无需知道具体工厂类型
Logistics[] companies = {new RoadLogistics(), new SeaLogistics()};
for (Logistics company : companies) {
    company.planDelivery("目的地");
}

// ③ 验证每次创建新实例（非单例）
Transport t1 = roadLogistics.createTransport();
Transport t2 = roadLogistics.createTransport();
System.out.println("t1 == t2 ? " + (t1 == t2));  // false
```

### 设计模式优势

1. **遵循开闭原则**：新增"航空物流"只需新增 `AirLogistics` 和 `Airplane`，无需修改现有代码
2. **遵循单一职责**：每个具体工厂只负责创建一种产品，职责清晰
3. **降低耦合度**：客户端仅依赖 `Logistics` 和 `Transport` 抽象，不依赖具体实现类
4. **灵活扩展**：工厂方法每次调用创建全新实例，支持对象复用策略自定义

---

## 四、典型应用场景

工厂方法模式在以下场景中广泛使用：

### 1. 框架设计与插件系统

**场景**：框架提供抽象接口，由插件开发者实现具体产品。

**案例**：
- **Spring Framework**：`FactoryBean` 接口允许开发者自定义 Bean 的创建逻辑
- **MyBatis**：`SqlSessionFactory#openSession()` 工厂方法创建 `SqlSession`，支持不同配置
- **JUnit**：`TestRunner` 工厂方法创建不同类型的测试执行器

### 2. 多数据源/多协议适配

**场景**：系统需要支持多种数据源或协议，但客户端面向统一接口编程。

**案例**：
- **JDBC**：`DriverManager.getConnection()` 根据 URL 创建不同数据库连接
- **Spring Data**：`RepositoryFactory` 为 JPA/MongoDB/Redis 创建对应的 Repository 实例
- **日志框架**：Slf4j 的 `LoggerFactory.getLogger()` 根据配置创建不同日志实现

### 3. UI 组件创建

**场景**：跨平台 UI 框架需要根据操作系统创建不同的原生组件。

**案例**：
- **Java AWT/Swing**：`Toolkit` 工厂方法创建 Button、TextField 等平台相关组件
- **跨平台应用**：根据 Windows/macOS/Linux 创建对应的原生窗口组件

### 4. 文档/文件导出

**场景**：系统需要导出多种格式（PDF/Excel/Word/CSV），但导出流程统一。

**案例**：
```java
// 抽象创建者
abstract class DocumentExporter {
    public abstract Document createDocument();
    
    public void export(String data) {
        Document doc = createDocument();
        doc.render(data);
        doc.save();
    }
}

// 具体创建者
class PdfExporter extends DocumentExporter {
    public Document createDocument() { return new PdfDocument(); }
}

class ExcelExporter extends DocumentExporter {
    public Document createDocument() { return new ExcelDocument(); }
}
```

### 5. 支付网关集成

**场景**：电商平台支持多种支付方式（支付宝/微信/银联/PayPal），支付流程相同但实现不同。

**案例**：
```java
abstract class PaymentGateway {
    public abstract PaymentChannel createChannel();
    
    public PaymentResult pay(Order order) {
        PaymentChannel channel = createChannel();
        return channel.process(order);
    }
}

class AlipayGateway extends PaymentGateway {
    public PaymentChannel createChannel() { return new AlipayChannel(); }
}
```

### 6. 游戏开发中的角色/道具创建

**场景**：游戏中不同关卡需要创建不同的敌人或道具，但游戏引擎的处理逻辑统一。

**案例**：
- **关卡工厂**：`LevelFactory` 工厂方法创建不同难度的敌人
- **道具系统**：`ItemFactory` 根据职业创建不同的装备/药水

### 7. 微服务中的客户端代理创建

**场景**：微服务架构中，根据服务名称创建对应的 RPC/REST 客户端代理。

**案例**：
- **Spring Cloud OpenFeign**：`@FeignClient` 接口通过工厂方法动态代理生成远程调用客户端
- **Dubbo**：`ReferenceBean` 工厂方法创建远程服务代理

---

## 五、与简单工厂的对比

| 对比维度 | 简单工厂（Static Factory） | 工厂方法（Factory Method） |
|---------|--------------------------|--------------------------|
| **结构** | 一个工厂类包含所有创建逻辑 | 每个具体工厂只负责一种产品 |
| **扩展性** | 新增产品需修改工厂类（违反开闭原则） | 新增产品只需新增工厂子类（符合开闭原则） |
| **职责** | 工厂类职责过重，违反单一职责 | 职责分散到各子类，符合单一职责 |
| **复杂度** | 代码简单，适合少量产品 | 类数量较多，适合复杂系统 |
| **典型实现** | `if/switch` 判断类型 | 子类重写工厂方法 |

---

## 六、使用建议

1. **优先使用工厂方法而非简单工厂**：当产品类型可能扩展时，工厂方法的开闭原则优势明显
2. **避免过度设计**：如果只有 1-2 种产品且不太可能扩展，简单工厂更合适
3. **结合配置文件**：可通过反射 + 配置文件实现动态工厂，进一步提升灵活性
4. **与 Spring 集成**：在 Spring 中，可将具体工厂声明为 Bean，通过依赖注入获取
5. **注意对象生命周期**：工厂方法默认每次创建新实例，如需复用需自行实现缓存池

---

## 七、总结

工厂方法模式是创建型模式中最常用的模式之一，它通过**将对象创建延迟到子类**，实现了创建与使用的解耦。本包以物流运输为场景，清晰展示了四个核心角色的协作关系：

- **Transport**（抽象产品）定义运输工具接口
- **Truck/Ship**（具体产品）实现具体运输行为
- **Logistics**（抽象创建者）声明工厂方法和业务流程
- **RoadLogistics/SeaLogistics**（具体创建者）决定创建哪种运输工具

掌握工厂方法模式，可以帮助我们在框架设计、插件系统、多数据源适配等场景中编写出**高内聚、低耦合、易扩展**的代码。
