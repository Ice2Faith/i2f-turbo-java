# 桥接模式（Bridge Pattern）

> 将抽象部分与它的实现部分分离，使它们可以独立地变化。

## 目录

- [一、核心逻辑](#一核心逻辑)
- [二、核心组成](#二核心组成)
- [三、案例设计](#三案例设计)
- [四、典型应用场景](#四典型应用场景)
- [五、使用建议](#五使用建议)
- [六、参考实现](#六参考实现)

---

## 一、核心逻辑

桥接模式的核心思想是**"抽象与实现分离"**，通过**组合**而非继承的方式，将两个独立变化的维度解耦，使它们可以独立扩展，避免类爆炸问题。

### 1.1 设计原则体现

| 原则 | 说明 |
| --- | --- |
| **优先使用对象组合而非类继承** | 这是 GoF 设计模式的核心原则，桥接模式是此原则的典型体现 |
| **开闭原则（OCP）** | 对扩展开放（新增遥控器或设备），对修改关闭（无需修改现有代码） |
| **单一职责原则（SRP）** | 遥控器专注控制逻辑，设备专注执行逻辑，职责清晰 |
| **依赖倒置原则（DIP）** | 高层模块（遥控器）依赖抽象（Device 接口），而非具体实现 |

### 1.2 核心机制

```
抽象部分（Abstraction）          实现部分（Implementor）
      RemoteControl  ──组合──►       Device
           │                            │
           ├─ togglePower()             ├─ powerOn()
           ├─ volumeUp()                ├─ powerOff()
           ├─ volumeDown()              ├─ volumeUp()
           └─ setDevice() ◄──桥接核心──┘└─ volumeDown()
                                         └─ getStatus()
```

**关键点**：
- **组合关系**：`RemoteControl` 持有 `Device` 接口的引用，而非继承设备类
- **运行时切换**：通过 `setDevice()` 方法，同一遥控器可在运行时动态切换控制的设备
- **独立扩展**：新增遥控器类型或设备类型互不影响

### 1.3 与继承方案对比

| 维度 | 继承方案（类爆炸） | 桥接方案（组合） |
| --- | --- | --- |
| **类数量** | 遥控器类型数 × 设备类型数 | 遥控器类型数 + 设备类型数 |
| **扩展性** | 每新增一个维度，类数量相乘增长 | 每新增一个维度，只需增加对应类 |
| **灵活性** | 编译时确定关系，无法动态切换 | 运行时可动态切换实现 |
| **维护性** | 类关系复杂，修改影响范围大 | 职责单一，修改影响范围小 |

**示例**：
- 2 种遥控器 × 2 种设备 = 继承方案需 4 个类，桥接方案也需 4 个类（但结构清晰）
- 新增 1 种设备 → 继承方案需 2×3=**6** 个类，桥接方案只需 2+3=**5** 个类
- 3 种遥控器 × 4 种设备 → 继承方案需 **12** 个类，桥接方案只需 3+4=**7** 个类

---

## 二、核心组成

### 2.1 角色说明

| 角色 | 类名 | 说明 |
| --- | --- | --- |
| **Implementor（实现接口）** | [`Device`](device/Device.java) | 定义设备操作接口（电源、音量等） |
| **ConcreteImplementor（具体实现）** | [`TV`](device/impl/TV.java) | 电视设备实现，提供频道切换功能 |
| **ConcreteImplementor（具体实现）** | [`Radio`](device/impl/Radio.java) | 收音机设备实现，提供频率调谐功能 |
| **Abstraction（抽象部分）** | [`RemoteControl`](remote/RemoteControl.java) | 遥控器抽象类，组合 Device 接口，定义控制逻辑 |
| **RefinedAbstraction（扩展抽象）** | [`BasicRemote`](remote/impl/BasicRemote.java) | 基础遥控器，提供基本控制功能 |
| **RefinedAbstraction（扩展抽象）** | [`AdvancedRemote`](remote/impl/AdvancedRemote.java) | 高级遥控器，扩展频道切换、频率调谐等功能 |
| **Client（客户端）** | [`Test`](Test.java) | 演示入口，展示桥接模式的核心用法 |

### 2.2 接口设计

#### Implementor 接口（Device）

```java
public interface Device {
    void powerOn();           // 打开电源
    void powerOff();          // 关闭电源
    int volumeUp();           // 增加音量
    int volumeDown();         // 降低音量
    String getStatus();       // 获取状态
    String getDeviceType();   // 获取设备类型
}
```

**设计要点**：
- 定义设备通用操作，不关心具体设备类型
- 所有设备实现此接口，提供各自的具体行为
- 新增设备只需实现此接口，无需修改遥控器代码

#### Abstraction 抽象类（RemoteControl）

```java
public abstract class RemoteControl {
    protected Device device;  // 关键：组合而非继承
    
    public RemoteControl(Device device) {
        this.device = device;
    }
    
    public void togglePower() { ... }
    public void volumeUp() { ... }
    public void volumeDown() { ... }
    public void setDevice(Device newDevice) { ... }  // 运行时切换
    public abstract String getRemoteType();
}
```

**设计要点**：
- 通过 `protected Device device` 持有实现接口引用
- 定义通用控制逻辑，调用 `device` 接口方法
- 提供 `setDevice()` 实现运行时动态切换
- 抽象方法 `getRemoteType()` 留给子类扩展

### 2.3 安全组合特点

桥接模式采用**安全组合**设计：
- 抽象层（`RemoteControl`）只依赖实现接口（`Device`）的通用方法
- 扩展抽象层（`AdvancedRemote`）通过 `instanceof` 判断并向下转型调用设备特有方法
- 客户端面向抽象编程，无需知道具体类型

---

## 三、案例设计

### 3.1 场景选择：智能家居遥控系统

**为什么选择这个场景？**

1. **两个独立变化的维度**：
   - **控制逻辑维度**：基础遥控器、高级遥控器、语音遥控器、手势遥控器...
   - **设备类型维度**：电视、收音机、空调、音响、窗帘...

2. **避免类爆炸**：
   - 继承方案：3 种遥控器 × 4 种设备 = 12 个类
   - 桥接方案：3 种遥控器 + 4 种设备 = 7 个类

3. **运行时灵活性**：
   - 同一个遥控器可以在客厅控制电视，在卧室控制收音机
   - 用户可以根据需要随时切换控制目标

### 3.2 构建树形结构

```
抽象部分（遥控器）                    实现部分（设备）
───────────────────────────        ───────────────────────────
RemoteControl（抽象）                Device（接口）
  ├─ BasicRemote（基础）              ├─ TV（电视）
  │   └─ getRemoteType()                ├─ powerOn()/powerOff()
  │                                     ├─ volumeUp()/volumeDown()
  └─ AdvancedRemote（高级）             └─ getStatus()
      ├─ setChannel()        ──►      TV.setChannel()
      ├─ tuneFrequency()     ──►      Radio.setFrequency()
      └─ mute()
```

### 3.3 统一操作演示

```java
// 客户端代码：面向抽象编程
Device tv = new TV("小米电视 65寸");
RemoteControl remote = new BasicRemote(tv);

// 统一接口，不关心具体设备类型
remote.togglePower();
remote.volumeUp();
remote.volumeDown();

// 运行时动态切换设备
remote.setDevice(new Radio("索尼收音机"));
remote.togglePower();  // 同一遥控器控制不同设备
```

### 3.4 扩展操作演示

```java
// 高级遥控器扩展功能
AdvancedRemote advanced = new AdvancedRemote(new TV("三星 QLED"));

advanced.togglePower();
advanced.setChannel(5);       // 电视特有：频道切换
advanced.mute();              // 通用扩展：静音

advanced.setDevice(new Radio("德生收音机"));
advanced.tuneFrequency(101.7); // 收音机特有：频率调谐
```

### 3.5 模式优势体现

#### 优势 1：分离抽象与实现

```java
// 遥控器层（抽象）只关注控制逻辑
public void togglePower() {
    if (isDevicePoweredOn()) {
        device.powerOff();  // 调用实现层
    } else {
        device.powerOn();   // 调用实现层
    }
}

// 设备层（实现）只关注具体执行
public void powerOn() {
    this.isPowerOn = true;
    System.out.println("📺 电视已开启");  // 电视特有行为
}
```

#### 优势 2：避免类爆炸

| 方案 | 2 遥控器 × 2 设备 | 3 遥控器 × 4 设备 | 5 遥控器 × 6 设备 |
| --- | --- | --- | --- |
| **继承** | 4 个类 | 12 个类 | 30 个类 |
| **桥接** | 4 个类 | 7 个类 | 11 个类 |

#### 优势 3：运行时切换

```java
RemoteControl universal = new BasicRemote(new TV("客厅电视"));
universal.togglePower();

universal.setDevice(new Radio("卧室收音机"));  // 动态切换
universal.togglePower();

universal.setDevice(new TV("书房电视"));       // 再次切换
universal.togglePower();
```

#### 优势 4：遵循开闭原则

```java
// 新增设备类型：只需实现 Device 接口，无需修改遥控器
public class AirConditioner implements Device {
    @Override
    public void powerOn() { ... }
    // ... 其他方法
}

// 新增遥控器类型：只需扩展 RemoteControl，无需修改设备
public class VoiceRemote extends RemoteControl {
    public void voiceCommand(String command) { ... }
}
```

---

## 四、典型应用场景

### 4.1 JDK/JDBC 数据库驱动

**场景描述**：`DriverManager`（抽象）与各数据库 `Driver`（实现）解耦

```java
// 抽象部分：DriverManager 统一管理驱动加载
Connection conn = DriverManager.getConnection(url, user, password);

// 实现部分：不同数据库驱动独立实现
// - MySQL Driver
// - Oracle Driver
// - PostgreSQL Driver
// - SQL Server Driver

// 优势：新增数据库驱动无需修改 DriverManager
```

**桥接体现**：
- 抽象维度：连接管理、事务管理、元数据查询
- 实现维度：MySQL、Oracle、PostgreSQL、SQL Server...
- 独立扩展：新增数据库类型或新增连接管理功能互不影响

### 4.2 Spring 事务管理器

**场景描述**：`PlatformTransactionManager`（抽象）与具体事务管理器（实现）分离

```java
// 抽象接口
public interface PlatformTransactionManager {
    TransactionStatus getTransaction(TransactionDefinition definition);
    void commit(TransactionStatus status);
    void rollback(TransactionStatus status);
}

// 具体实现（独立扩展）
// - DataSourceTransactionManager（JDBC 事务）
// - JpaTransactionManager（JPA 事务）
// - JtaTransactionManager（分布式事务）
// - HibernateTransactionManager（Hibernate 事务）
```

**桥接体现**：
- 抽象维度：事务声明、提交、回滚、传播行为
- 实现维度：JDBC、JPA、JTA、Hibernate、MongoDB...
- 优势：Spring 框架无需关心具体数据库技术，新增数据源只需实现接口

### 4.3 Spring Data 仓储接口

**场景描述**：`Repository`（抽象）与各数据源实现（实现）独立演化

```java
// 抽象接口
public interface CrudRepository<T, ID> {
    <S extends T> S save(S entity);
    Optional<T> findById(ID id);
    Iterable<T> findAll();
    void delete(T entity);
}

// 具体实现（独立扩展）
// - JpaRepository（关系型数据库）
// - MongoRepository（MongoDB）
// - RedisRepository（Redis）
// - ElasticsearchRepository（Elasticsearch）
```

**桥接体现**：
- 抽象维度：CRUD 操作、分页查询、排序、批量操作
- 实现维度：JPA、MongoDB、Redis、Elasticsearch、Cassandra...
- 优势：业务代码依赖 `Repository` 接口，切换数据源无需修改业务逻辑

### 4.4 Slf4j 日志门面

**场景描述**：日志门面（抽象）与具体日志实现（实现）分离

```java
// 抽象门面
Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("Application started");

// 具体实现（可替换）
// - slf4j-logback（Logback 实现）
// - slf4j-log4j12（Log4j 1.x 实现）
// - slf4j-jdk14（JUL 实现）
// - slf4j-simple（简单实现）
```

**桥接体现**：
- 抽象维度：日志级别（TRACE/DEBUG/INFO/WARN/ERROR）、格式化、占位符
- 实现维度：Logback、Log4j、JUL、Log4j2...
- 优势：业务代码依赖 Slf4j API，切换日志框架只需更换依赖，无需修改代码

### 4.5 AWT/Swing 跨平台组件

**场景描述**：`Component`（抽象）与 `Peer`（平台实现）分离

```java
// 抽象组件
JButton button = new JButton("Click Me");

// 平台实现（Peer）
// - WindowsPeer（Windows 平台渲染）
// - MacPeer（macOS 平台渲染）
// - LinuxPeer（Linux 平台渲染）
// - MotifPeer（通用 Unix 渲染）
```

**桥接体现**：
- 抽象维度：按钮、文本框、面板、布局管理器
- 实现维度：Windows、macOS、Linux、Motif...
- 优势：Java 代码跨平台运行，平台特定渲染由 Peer 处理

### 4.6 图形渲染引擎

**场景描述**：渲染器（抽象）与渲染后端（实现）解耦

```java
// 抽象渲染器
public abstract class Renderer {
    protected RenderBackend backend;
    
    public void drawCircle(int x, int y, int radius) {
        backend.drawCircle(x, y, radius);
    }
}

// 渲染后端（实现）
// - OpenGLBackend（OpenGL 渲染）
// - DirectXBackend（DirectX 渲染）
// - VulkanBackend（Vulkan 渲染）
// - MetalBackend（Apple Metal 渲染）
// - SoftwareBackend（软件渲染）
```

**桥接体现**：
- 抽象维度：绘制图形（圆、矩形、线条）、变换、材质、光照
- 实现维度：OpenGL、DirectX、Vulkan、Metal、软件渲染...
- 优势：游戏引擎可跨平台运行，新增渲染后端无需修改引擎逻辑

### 4.7 消息推送系统

**场景描述**：推送服务（抽象）与推送渠道（实现）分离

```java
// 抽象推送服务
public abstract class PushService {
    protected PushChannel channel;
    
    public void sendPush(String title, String content, String target) {
        channel.send(title, content, target);
    }
}

// 推送渠道（实现）
// - APNsChannel（Apple 推送）
// - FCMChannel（Firebase 推送）
// - WeChatChannel（微信推送）
// - SmsChannel（短信推送）
// - EmailChannel（邮件推送）
```

**桥接体现**：
- 抽象维度：消息构建、目标筛选、失败重试、推送统计
- 实现维度：APNs、FCM、微信、短信、邮件、Webhook...
- 优势：业务代码依赖推送服务接口，新增推送渠道无需修改业务逻辑

---

## 五、使用建议

### 5.1 适用条件

✅ **适合使用桥接模式的场景**：
- 一个类存在**两个或多个独立变化的维度**，且都需要扩展
- 不希望使用继承导致**类数量呈乘法增长**（类爆炸）
- 需要在**运行时动态切换**实现
- 希望在抽象层和实现层之间建立**灵活的映射关系**

### 5.2 注意事项

⚠️ **使用桥接模式时需注意**：
- **不要过度设计**：如果只有一个变化维度，使用继承或策略模式即可
- **明确维度边界**：确保抽象层和实现层 truly 独立变化，避免职责混乱
- **接口设计要稳定**：`Implementor` 接口一旦确定，后续修改会影响所有实现类
- **向下转型要谨慎**：扩展抽象调用实现特有功能时，需使用 `instanceof` 检查

### 5.3 与其他模式对比

| 模式 | 核心思想 | 与桥接模式的区别 |
| --- | --- | --- |
| **策略模式** | 封装算法族，运行时替换 | 策略模式只有一个维度（算法），桥接模式有两个维度 |
| **状态模式** | 对象状态改变时行为改变 | 状态模式关注状态流转，桥接模式关注维度解耦 |
| **适配器模式** | 转换接口以兼容 | 适配器用于已有接口不兼容的场景，桥接用于设计阶段解耦 |
| **抽象工厂模式** | 创建对象族 | 抽象工厂关注创建，桥接关注结构组合 |

---

## 六、参考实现

### 6.1 包结构

```
i2f.design.pattern.structural.bridge/
├── device/                          # 实现部分（设备）
│   ├── Device.java                  # 实现接口（Implementor）
│   └── impl/
│       ├── TV.java                  # 具体实现（电视）
│       └── Radio.java               # 具体实现（收音机）
├── remote/                          # 抽象部分（遥控器）
│   ├── RemoteControl.java           # 抽象类（Abstraction）
│   └── impl/
│       ├── BasicRemote.java         # 扩展抽象（基础遥控）
│       └── AdvancedRemote.java      # 扩展抽象（高级遥控）
├── Test.java                        # 演示入口
└── package-info.java                # 包说明文档
```

### 6.2 核心类链接

- [Device 接口](device/Device.java) —— 定义设备操作接口
- [TV 实现](device/impl/TV.java) —— 电视设备（频道切换）
- [Radio 实现](device/impl/Radio.java) —— 收音机设备（频率调谐）
- [RemoteControl 抽象类](remote/RemoteControl.java) —— 遥控器抽象（组合 Device）
- [BasicRemote 扩展](remote/impl/BasicRemote.java) —— 基础遥控器
- [AdvancedRemote 扩展](remote/impl/AdvancedRemote.java) —— 高级遥控器（扩展功能）
- [Test 演示](Test.java) —— 完整演示入口

### 6.3 运行演示

```bash
# 编译运行
javac -cp . i2f/design/pattern/structural/bridge/Test.java
java i2f.design.pattern.structural.bridge.Test
```

**输出示例**：
```
====== 桥接模式（Bridge Pattern）演示 ======
场景：遥控器（Abstraction）通过组合设备接口（Implementor）控制不同设备
      遥控器类型和设备类型可以独立变化，避免类爆炸问题

────── 基础遥控器 + 电视 ──────
遥控器类型：基础遥控器
控制设备：电视[品牌=小米电视 65寸, 频道=1, 音量=20, 电源=关闭]

  ┌─ 遥控器操作：切换电源
  │ 当前状态：设备已关闭 → 执行开启
    📺 [小米电视 65寸] 电视已开启
  └─ 操作完成

  ┌─ 遥控器操作：增加音量
  │ 设备类型：智能电视
  │ 音量调节：→ 25
  └─ 操作完成

...
```

---

> **设计模式是工具而非银弹**，使用时应结合实际业务场景，避免过度设计（Over-engineering）。桥接模式适用于**多维度独立变化**的场景，通过组合替代继承，实现灵活可扩展的架构设计。
