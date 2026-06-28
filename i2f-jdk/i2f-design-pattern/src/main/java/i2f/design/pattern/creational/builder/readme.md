# 建造者模式（Builder Pattern）

## 一、核心逻辑

建造者模式的核心思想是**将复杂对象的构建过程与其表示分离**，使得同样的构建过程可以创建不同的表示。该模式通过分步骤构建对象，将创建逻辑封装在独立的建造者类中，客户端无需了解对象的内部构建细节。

### 核心价值

1. **构建与表示分离**：构建流程由指挥者定义，具体配置由建造者决定
2. **流程复用**：同一套构建流程可用于创建不同配置的产品
3. **细粒度控制**：支持分步骤构建复杂对象，每步可独立配置
4. **链式调用**：建造者方法返回自身，支持流式 API，提升可读性

---

## 二、核心组成

建造者模式包含四个核心角色：

### 1. Product（产品）
- **本例实现**：[Computer](./Computer.java)
- **职责**：被构建的复杂对象，包含多个部件属性（CPU、内存、硬盘、显卡等）
- **特点**：具有复杂的内部结构，需要多个步骤才能完整构建

### 2. Builder（抽象建造者）
- **本例实现**：[Builder](./Builder.java) 接口
- **职责**：定义构建产品各个部件的抽象方法
- **本例方法**：
  - `buildCPU(String cpu)` - 安装 CPU
  - `buildMemory(int memory)` - 安装内存
  - `buildStorage(String storage)` - 安装硬盘
  - `buildGPU(String gpu)` - 安装显卡
  - `buildMonitor(double monitorSize)` - 安装显示器
  - `buildSSD(boolean hasSSD)` - 安装固态硬盘
  - `getComputer()` - 获取构建完成的产品

### 3. ConcreteBuilder（具体建造者）
- **本例实现**：
  - [OfficeComputerBuilder](./impl/OfficeComputerBuilder.java) - 办公电脑建造者
  - [GamingComputerBuilder](./impl/GamingComputerBuilder.java) - 游戏电脑建造者
- **职责**：实现 Builder 接口，提供具体的部件配置逻辑
- **特点**：维护一个产品实例，逐步组装各部件，最后返回完整产品

### 4. Director（指挥者）
- **本例实现**：[Director](./Director.java)
- **职责**：定义构建流程的顺序，调用建造者的构建方法
- **本例流程**：
  - `construct(Builder builder)` - 标准构建流程
  - `constructOfficeComputer(Builder builder)` - 办公电脑专用流程
  - `constructGamingComputer(Builder builder)` - 游戏电脑专用流程

---

## 三、案例设计说明

### 场景背景
以"电脑组装流水线"为场景，展示建造者模式如何分离构建过程与产品表示。

### 设计实现

#### 1. 产品定义
`Computer` 类代表被构建的复杂对象，包含 6 个部件属性：
- CPU 型号
- 内存大小
- 硬盘类型与容量
- 显卡型号
- 显示器尺寸
- 是否包含 SSD

#### 2. 构建步骤抽象
`Builder` 接口定义了 6 个构建步骤，每个步骤返回 `Builder` 自身以支持链式调用：
```java
Builder buildCPU(String cpu);
Builder buildMemory(int memory);
Builder buildStorage(String storage);
Builder buildGPU(String gpu);
Builder buildMonitor(double monitorSize);
Builder buildSSD(boolean hasSSD);
Computer getComputer();
```

#### 3. 具体建造者实现
- **OfficeComputerBuilder**：专注于构建办公电脑，配置偏向实用与性价比
- **GamingComputerBuilder**：专注于构建游戏电脑，配置偏向高性能

两者都维护一个 `Computer` 实例，在构建过程中逐步设置各部件属性。

#### 4. 指挥者定义构建流程
`Director` 定义了三种构建流程：
- **标准流程**（`construct`）：通用组装步骤
- **办公电脑流程**（`constructOfficeComputer`）：低配办公配置
  - Intel Core i3-12100 / 8GB / 500GB HDD + 128GB SSD / 集成显卡 / 21.5寸
- **游戏电脑流程**（`constructGamingComputer`）：高配游戏配置
  - AMD Ryzen 9 7950X / 64GB / 2TB NVMe SSD / RTX 4090 / 27寸

#### 5. 客户端使用方式
通过 [Test](./Test.java) 演示了多种使用场景：

**方式一：使用指挥者 + 专用方法**
```java
Director director = new Director();
OfficeComputerBuilder officeBuilder = new OfficeComputerBuilder();
Computer officeComputer = director.constructOfficeComputer(officeBuilder);
```

**方式二：面向抽象编程**
```java
Builder[] builders = {new OfficeComputerBuilder(), new GamingComputerBuilder()};
for (Builder builder : builders) {
    Computer computer = director.construct(builder);
}
```

**方式三：链式调用（不使用指挥者）**
```java
Computer customComputer = new OfficeComputerBuilder()
    .buildCPU("Intel Core i7-13700K")
    .buildMemory(32)
    .buildStorage("1TB NVMe SSD")
    .buildGPU("NVIDIA RTX 4070")
    .buildMonitor(27.0)
    .buildSSD(true)
    .getComputer();
```

### 模式优势体现

1. **同样的构建流程，不同的产品表示**
   - 使用 `director.construct()` 方法，传入不同的建造者
   - 流程相同（6 个步骤），但产出的电脑配置完全不同

2. **构建过程封装**
   - 客户端无需知道 Computer 有哪些属性、如何设置
   - 只需选择合适的建造者，调用指挥者方法即可

3. **灵活扩展**
   - 新增其他类型电脑（如设计电脑、服务器）只需新增 ConcreteBuilder
   - 无需修改 Director 或其他已有类，符合开闭原则

4. **与工厂方法的区别**
   - 工厂方法：一次调用创建完整对象，关注"创建哪种类型"
   - 建造者模式：分步骤构建复杂对象，关注"如何构建内部配置"

---

## 四、常见适用场景

### 1. 复杂对象构建（多参数、多步骤）
**场景特征**：对象具有多个可选参数或需要多个步骤初始化

**典型案例**：
- JDK：`java.lang.StringBuilder`、`ProcessBuilder`、`Stream.Builder`
- Spring：`UriComponentsBuilder`（链式构造 URL）、`BeanDefinitionBuilder`
- Spring Boot：`SpringApplicationBuilder`（链式配置启动应用）
- Spring Security：`HttpSecurity` 链式 DSL（`.authorizeRequests().and().formLogin()...`）
- OkHttp：`Request.Builder`、`OkHttpClient.Builder`

### 2. 配置对象/选项对象构建
**场景特征**：需要创建包含大量配置项的对象，不同场景配置不同

**典型案例**：
- Lombok：`@Builder` 注解自动生成建造者代码
- Guava：`CacheBuilder`（构建缓存配置）、`ImmutableList.Builder`
- Spring WebFlux：`WebClient.Builder`（响应式 HTTP 客户端配置）
- MyBatis：`SqlSessionFactoryBuilder`（逐步配置数据源、事务、插件等）

### 3. 产品族/多版本构建
**场景特征**：同一类产品有多个版本或变体，构建流程相同但配置不同

**典型案例**：
- 文档导出：PDF 导出器、Word 导出器、Excel 导出器（同样导出流程，不同格式配置）
- 消息构建：邮件消息构建器、短信消息构建器、推送消息构建器
- 报表生成：日报建造者、月报建造者、年报建造者
- 本系统：不同配置的电脑（办公电脑、游戏电脑、设计电脑）

### 4. 不可变对象构建
**场景特征**：对象创建后不可修改，需要通过建造者逐步设置属性

**典型案例**：
- Guava：`ImmutableList`、`ImmutableMap` 等不可变集合
- Java 9+：`List.of()`、`Map.of()` 的建造者替代方案
- 配置类对象：一旦创建不再修改的配置对象

### 5. 链式 API 设计
**场景特征**：希望提供流畅的流式 API，提升代码可读性

**典型案例**：
- Spring MVC：`MockMvcRequestBuilders.get("/api").param("key", "value")...`
- JPA Criteria API：链式构建查询条件
- Testcontainers：`DockerContainer.withImage().withPortBindings().withEnv()...`
- 本系统：Builder 接口所有方法返回自身，支持流畅链式调用

### 6. 与模板方法结合
**场景特征**：构建流程固定，但某些步骤需要子类扩展

**典型组合**：
- Director 定义固定构建流程（类似模板方法）
- Builder 接口定义步骤，ConcreteBuilder 实现具体逻辑
- Spring Batch：`JobBuilder` 链式配置任务步骤，每步可自定义实现

### 7. 构造参数过多的替代方案
**场景特征**：构造函数参数超过 4-5 个，可读性差，容易传错顺序

**优势对比**：
```java
// ❌ 传统方式：参数多、顺序易错、可读性差
new Computer("i7-13700K", 32, "1TB SSD", "RTX 4070", 27.0, true);

// ✅ 建造者模式：参数命名清晰、顺序无关、可读性强
new ComputerBuilder()
    .cpu("i7-13700K")
    .memory(32)
    .storage("1TB SSD")
    .gpu("RTX 4070")
    .monitor(27.0)
    .ssd(true)
    .build();
```

---

## 五、总结

建造者模式通过**分离构建过程与产品表示**，解决了复杂对象创建时的灵活性与可维护性问题。本例以电脑组装为场景，清晰展示了：

- **核心机制**：Builder（抽象建造者）→ ConcreteBuilder（具体建造者）→ Director（指挥者）→ Product（产品）
- **关键优势**：同样的构建流程可创建不同配置的产品，支持链式调用，客户端无需了解构建细节
- **适用边界**：对象构建步骤多、配置复杂、需要多种变体时优先考虑；简单对象直接使用构造函数或工厂方法即可

> **设计原则体现**：开闭原则（新增建造者无需修改现有代码）、单一职责原则（构建逻辑与产品表示分离）、依赖倒置原则（客户端依赖 Builder 抽象而非具体实现）