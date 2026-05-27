# 外观模式（Facade Pattern）

## 一、设计模式核心逻辑

外观模式（Facade Pattern）是一种**结构型设计模式**，其核心思想是：

> **为复杂子系统提供一个统一的高层接口，使得子系统更易于使用。**

### 核心逻辑

1. **解耦客户端与子系统**：客户端不需要了解子系统的内部实现细节，只需通过外观类提供的方法即可完成复杂操作。
2. **简化接口**：将多个子系统的复杂调用封装为简单的高层方法，降低使用门槛。
3. **封装复杂性**：外观类内部协调多个子系统的调用顺序和参数配置，对外呈现简洁的接口。
4. **保持灵活性**：外观模式不禁止客户端直接访问子系统，在需要精细控制时仍可绕过外观类。

---

## 二、核心组成

外观模式由以下三个核心角色组成：

### 1. Facade（外观类）
- **职责**：为客户端提供统一的高层接口，内部协调多个子系统的调用。
- **本案例实现**：[`SmartHomeFacade`](./SmartHomeFacade.java)
- **核心方法**：
  - `goHomeMode()` - 回家模式
  - `leaveHomeMode()` - 离家模式
  - `cinemaMode()` - 影院模式
  - `sleepMode()` - 睡眠模式
  - `wakeUpMode()` - 起床模式

### 2. Subsystem Classes（子系统类）
- **职责**：实现子系统的具体功能，处理各自的业务逻辑。子系统之间互不感知。
- **本案例实现**：
  - [`LightSystem`](./subsystem/LightSystem.java) - 灯光子系统（开关、亮度、颜色调节）
  - [`AirConditioningSystem`](./subsystem/AirConditioningSystem.java) - 空调子系统（温度、模式、风速控制）
  - [`AudioSystem`](./subsystem/AudioSystem.java) - 音响子系统（播放、音量、音源切换）
  - [`CurtainSystem`](./subsystem/CurtainSystem.java) - 窗帘子系统（开合、角度调节）

### 3. Client（客户端）
- **职责**：通过外观类访问子系统功能，无需直接操作子系统。
- **本案例实现**：[`Test`](./Test.java)

### 类图关系

```
Client（测试类）
  └─ 调用 Facade（SmartHomeFacade）
       ├─ LightSystem        （灯光子系统）
       ├─ AirConditioningSystem  （空调子系统）
       ├─ AudioSystem        （音响子系统）
       └─ CurtainSystem      （窗帘子系统）
```

---

## 三、案例设计说明

### 案例背景

本案例以**智能家居控制系统**为背景，模拟日常生活中常见的场景化操作。一个完整的智能家居系统包含多个子系统（灯光、空调、音响、窗帘），每个子系统都有自己复杂的控制逻辑。

### 设计思路

#### 1. 子系统独立设计

每个子系统独立实现自身的功能，包含多种操作：

- **灯光系统**：开/关、调节亮度（0-100）、设置颜色、阅读模式、影院模式
- **空调系统**：开/关、设置温度（16-30°C）、切换模式（制冷/制热/除湿）、调节风速、舒适模式、节能模式
- **音响系统**：开/关、播放/暂停/停止、调节音量、切换音源、音乐模式、影院模式
- **窗帘系统**：开/关、设置角度（0-100%）、半开模式、晨起模式

#### 2. 外观类封装场景

`SmartHomeFacade` 将多个子系统的操作组合为**场景化的一键操作**：

**示例：影院模式的实现**
```java
public void cinemaMode() {
    // 1. 灯光调暗（影院模式）
    lightSystem.setCinemaMode();
    lightSystem.turnOn();
    
    // 2. 空调开启（舒适模式）
    airConditioningSystem.setComfortMode();
    airConditioningSystem.turnOn();
    
    // 3. 音响切换至影院模式
    audioSystem.setCinemaMode();
    audioSystem.turnOn();
    audioSystem.play();
    
    // 4. 关闭窗帘（隔绝外部光线）
    curtainSystem.close();
}
```

客户端只需调用 `smartHome.cinemaMode()` 即可完成全部操作，无需记住 8 个方法调用的顺序和参数。

#### 3. 对比演示

案例通过 [`Test`](./Test.java) 类展示了两种方式的对比：

**方式1：直接操作子系统（繁琐）**
```java
light.setCinemaMode();
light.turnOn();
ac.setComfortMode();
ac.turnOn();
audio.setCinemaMode();
audio.turnOn();
audio.play();
curtain.close();
// 需要记住8个方法调用，且顺序不能错！
```

**方式2：使用外观（简洁）**
```java
smartHome.cinemaMode();
// 只需1个方法调用，内部逻辑已封装！
```

#### 4. 灵活性保留

外观模式不限制客户端直接访问子系统。案例演示了通过外观类获取子系统并进行自定义操作：

```java
// 通过外观类获取灯光子系统，进行自定义操作
smartHome.getLightSystem().setBrightness(75);
smartHome.getLightSystem().setColor("紫色");
smartHome.getLightSystem().turnOn();
```

### 模式优势总结

1. **降低耦合**：客户端无需了解子系统内部实现，只与外观类交互
2. **简化接口**：将复杂的子系统操作封装为简单的高层方法
3. **提高可维护性**：子系统变化时，只需修改外观类，不影响客户端
4. **灵活性**：客户端仍可直接访问子系统（如果需要精细控制）
5. **符合迪米特法则**：减少对象之间的直接依赖

---

## 四、典型应用场景

外观模式广泛应用于需要简化复杂系统接口的场景，以下是常见/典型的应用场景：

### 1. 框架/库的统一入口

**典型代表**：
- **Spring**：`JdbcTemplate`、`RestTemplate`、`RedisTemplate` 封装底层复杂的 JDBC/HTTP/Redis 操作
- **Spring Boot**：`SpringApplication.run()` 一行代码启动完整应用，内部封装了环境准备、Bean 创建、自动配置等数十个步骤
- **Slf4j**：`LoggerFactory.getLogger()` 提供统一的日志获取接口，屏蔽不同日志实现（Log4j、Logback）的差异

### 2. 复杂系统的简化访问

**典型代表**：
- **数据库连接池**（Druid、HikariCP）：通过统一的 `DataSource.getConnection()` 获取连接，内部封装了连接创建、复用、监控等复杂逻辑
- **ORM 框架**（MyBatis、Hibernate）：通过 `SqlSession` 或 `EntityManager` 统一操作数据库，隐藏 SQL 执行、结果映射、事务管理等细节
- **HTTP 客户端**（OkHttp、Apache HttpClient）：通过 `OkHttpClient.newCall()` 简化 HTTP 请求发送，内部处理连接池、重试、拦截器等

### 3. 微服务/API 网关

**典型代表**：
- **Spring Cloud Gateway**：网关作为后端多个微服务的统一入口，客户端只需访问网关，由网关路由到具体服务
- **API 聚合层**：将多个微服务的接口聚合为一个统一的 API，客户端一次调用即可获取多系统数据

### 4. 第三方库/SDK 封装

**典型代表**：
- **支付 SDK**：封装支付宝/微信支付的复杂流程（签名、加密、网络请求、回调处理），对外提供 `pay()` 等简单方法
- **消息队列客户端**（Kafka、RabbitMQ）：封装连接管理、序列化、重试机制，提供 `send()`/`receive()` 等简单接口
- **云存储 SDK**（AWS S3、阿里云 OSS）：封装认证、分片上传、断点续传等复杂逻辑

### 5. 遗留系统改造

**典型场景**：
- 旧系统重构时，通过外观类封装旧系统的复杂接口，为新系统提供统一访问入口
- 逐步迁移过程中，外观类作为新旧系统的过渡层，降低迁移风险

### 6. 工具类/帮助类

**典型代表**：
- **Apache Commons Lang**：`StringUtils`、`DateUtils` 等工具类封装常用操作
- **Spring**：`BeanUtils.copyProperties()`、`ReflectionUtils` 等反射工具类
- **自定义工具类**：如文件操作工具类、加密解密工具类等

---

## 五、使用建议

### ✅ 适合使用外观模式的场景
- 子系统复杂，客户端调用繁琐
- 需要为多层系统提供统一入口
- 希望降低客户端与子系统之间的耦合
- 系统需要支持多种使用方式（简单模式 + 高级模式）

### ⚠️ 注意事项
- 外观类不应替代子系统的功能，而是提供额外的高层接口
- 避免外观类过度膨胀，可考虑按业务场景拆分多个外观类
- 子系统仍应保持良好的设计，外观模式不能弥补糟糕的子系统设计
- 不要为了使用模式而使用，简单系统无需强加外观类

---

## 六、相关资源

- **包入口**：[`package-info.java`](./package-info.java)
- **演示入口**：运行 [`Test.main()`](./Test.java#L13) 查看完整演示
