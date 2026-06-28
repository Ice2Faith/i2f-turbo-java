# 抽象工厂模式（Abstract Factory）技术文档

> **定义：** 提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。  
> **分类：** 创建型模式（Creational Pattern）

---

## 一、核心逻辑

抽象工厂模式的核心思想是**"产品族一致性"**。当系统需要创建一组风格相关、配套使用的对象时，抽象工厂模式通过一个统一的工厂接口来创建这些对象，确保它们来自同一个"家族"，风格协调统一。

### 关键设计理念

1. **产品族概念（Product Family）**  
   产品族是指一组在主题或风格上相互关联的产品。例如：现代风格的椅子+桌子、古典风格的椅子+桌子。每个产品族内的产品在视觉上应该协调一致。

2. **工厂接口定义产品族创建能力**  
   抽象工厂接口声明一组创建方法（如 `createChair()`、`createTable()`），每个方法负责创建产品族中的一个成员。

3. **具体工厂实现产品族的具体风格**  
   每个具体工厂类实现抽象工厂接口，返回特定风格的一整套产品。例如：`ModernFurnitureFactory` 返回现代风格的椅子+桌子，`ClassicFurnitureFactory` 返回古典风格的椅子+桌子。

4. **客户端面向抽象编程**  
   客户端代码仅依赖抽象工厂接口和抽象产品类型，不依赖任何具体实现类。通过切换具体工厂实例，即可切换整个产品族。

### 与工厂方法模式的区别

| 维度 | 工厂方法模式 | 抽象工厂模式 |
|------|------------|------------|
| 创建范围 | 单个产品 | 一族相关产品 |
| 核心关注 | 单个对象的创建 | 产品族的整体一致性 |
| 扩展方式 | 通过继承（子类重写工厂方法） | 通过组合（切换不同的工厂实例） |
| 新增产品族 | 新增工厂子类 | 新增工厂实现 |
| 新增产品种类 | 需修改工厂接口（违反开闭原则） | 需修改工厂接口（违反开闭原则） |

---

## 二、核心组成

抽象工厂模式包含 **4 大核心角色**，在本包中的对应关系如下：

### 1. 抽象工厂（Abstract Factory）

**定义：** 声明一组创建抽象产品的方法。

**本包对应：** [`FurnitureFactory`](./factory/FurnitureFactory.java)

```java
public abstract class FurnitureFactory {
    public abstract Chair createChair();    // 创建产品 A
    public abstract Table createTable();    // 创建产品 B
    public void furnishRoom(String roomName); // 高层模板方法
    public abstract String getFactoryName();
}
```

**职责：**
- 定义创建产品族的接口规范
- 可包含高层业务方法（如 `furnishRoom()`），调用抽象方法创建产品

### 2. 具体工厂（Concrete Factory）

**定义：** 实现抽象工厂接口，创建具体产品族。

**本包对应：** 
- [`ModernFurnitureFactory`](./factory/impl/ModernFurnitureFactory.java)（现代家具工厂）
- [`ClassicFurnitureFactory`](./factory/impl/ClassicFurnitureFactory.java)（古典家具工厂）

```java
public class ModernFurnitureFactory extends FurnitureFactory {
    @Override
    public Chair createChair() {
        return new ModernChair("极简扶手椅", "不锈钢管", true, true);
    }
    
    @Override
    public Table createTable() {
        return new ModernTable("升降办公桌", "钢化玻璃", 1.2, true);
    }
}
```

**职责：**
- 实现工厂方法，返回特定风格的产品实例
- 确保同一工厂创建的产品风格一致

### 3. 抽象产品（Abstract Product）

**定义：** 定义产品族成员的公共接口。

**本包对应：** 
- [`Chair`](./chair/Chair.java)（抽象产品 A：椅子）
- [`Table`](./table/Table.java)（抽象产品 B：桌子）

```java
public abstract class Chair {
    private String name;
    private String material;
    private boolean hasArmrest;
    
    public abstract String sitDown();   // 抽象行为
    public abstract String getStyle();  // 获取风格
}
```

**职责：**
- 声明产品族成员的通用行为接口
- 定义产品族共享的通用属性

### 4. 具体产品（Concrete Product）

**定义：** 实现抽象产品接口，提供具体风格的产品。

**本包对应：** 
- 椅子族：[`ModernChair`](./chair/impl/ModernChair.java)、[`ClassicChair`](./chair/impl/ClassicChair.java)
- 桌子族：[`ModernTable`](./table/impl/ModernTable.java)、[`ClassicTable`](./table/impl/ClassicTable.java)

```java
public class ModernChair extends Chair {
    private boolean swivel;
    
    @Override
    public String sitDown() {
        return String.format("[现代椅·%s] 坐上去感觉简约舒适...", getName());
    }
    
    @Override
    public String getStyle() {
        return "现代简约";
    }
}
```

**职责：**
- 实现抽象产品的行为，提供具体风格的实现
- 包含产品特有的属性（如 `swivel`、`carvingPattern`）

### 模式结构图

```
AbstractFactory（抽象工厂）
  FurnitureFactory
    ├─ createChair(): Chair
    └─ createTable(): Table

ConcreteFactory（具体工厂）
  ├─ ModernFurnitureFactory  → 创建 ModernChair + ModernTable
  └─ ClassicFurnitureFactory → 创建 ClassicChair + ClassicTable

AbstractProduct（抽象产品）
  ├─ Chair（椅子接口）
  └─ Table（桌子接口）

ConcreteProduct（具体产品）
  ├─ ModernChair（现代椅子）    ─┐
  ├─ ClassicChair（古典椅子）   ├─ 椅子族
  ├─ ModernTable（现代桌子）    ─┤
  └─ ClassicTable（古典桌子）   ─┘ 桌子族
```

---

## 三、案例设计解析

### 1. 场景设定

以**"家具工厂"**为业务场景，模拟不同风格的家具工厂生产风格一致的全套家具：
- **现代家具工厂**：生产现代简约风格的椅子（不锈钢管框架、人造革坐垫）+ 桌子（钢化玻璃台面、金属框架）
- **古典家具工厂**：生产古典雕花风格的椅子（实木雕花、祥云纹饰）+ 桌子（红木实木、雕花桌腿）

### 2. 设计意图

**核心问题：** 如何确保同一套家具中的椅子与桌子风格协调一致，避免"现代椅子+古典桌子"的混搭？

**解决方案：** 通过抽象工厂模式，将产品族的创建封装在具体工厂中，客户端只需选择工厂，即可获取风格一致的全套产品。

### 3. 调用链路分析

以 [`Test`](./Test.java) 演示代码为例：

```java
// 第 1 步：客户端面向抽象工厂编程，不依赖具体类型
FurnitureFactory modernFactory = new ModernFurnitureFactory();

// 第 2 步：调用工厂的高层方法，触发产品族创建
modernFactory.furnishRoom("现代客厅");

// 第 3 步：工厂内部调用抽象方法创建配套产品
public void furnishRoom(String roomName) {
    Chair chair = createChair();   // → ModernFurnitureFactory.createChair() → new ModernChair()
    Table table = createTable();   // → ModernFurnitureFactory.createTable() → new ModernTable()
    
    // 第 4 步：使用风格一致的产品族
    System.out.println("椅子风格：" + chair.getStyle());  // 输出："现代简约"
    System.out.println("桌子风格：" + table.getStyle());  // 输出："现代简约"
}
```

### 4. 设计亮点

#### 4.1 产品族一致性保证

通过 `getStyle()` 方法验证同一工厂创建的产品风格一致：

```java
Chair modernChair = modernFactory.createChair();
Table modernTable = modernFactory.createTable();
System.out.println("风格一致？ " + modernChair.getStyle().equals(modernTable.getStyle()));
// 输出：true（均为"现代简约"）
```

#### 4.2 面向抽象编程

客户端代码使用抽象类型声明变量，通过多态切换不同工厂：

```java
FurnitureFactory[] factories = {
    new ModernFurnitureFactory(),
    new ClassicFurnitureFactory()
};

for (FurnitureFactory factory : factories) {
    factory.furnishRoom("客厅");  // 无需 if/else 判断工厂类型
}
```

#### 4.3 开闭原则体现

- **对扩展开放：** 新增"工业风家具"只需新增 `IndustrialFurnitureFactory` + 对应产品类
- **对修改关闭：** 无需修改现有工厂和产品代码

#### 4.4 工厂模板方法

[`FurnitureFactory.furnishRoom()`](./factory/FurnitureFactory.java#L80-L90) 是模板方法，定义产品族使用流程：

```java
public void furnishRoom(String roomName) {
    Chair chair = createChair();  // 延迟到子类实现
    Table table = createTable();  // 延迟到子类实现
    
    System.out.println("房间：" + roomName);
    System.out.println("椅子风格：" + chair.getStyle());
    System.out.println("桌子风格：" + table.getStyle());
}
```

---

## 四、典型应用场景

抽象工厂模式适用于以下场景：

### 1. UI 组件库（跨平台界面框架）

**场景描述：** 开发跨平台 UI 框架，需要为不同操作系统（Windows、macOS、Linux）提供风格一致的控件族（按钮、文本框、滚动条等）。

**抽象工厂应用：**
- `GUIFactory` 抽象工厂定义 `createButton()`、`createTextBox()`、`createScrollbar()`
- `WindowsGUIFactory` 创建 Windows 风格控件（圆角按钮、Aero 滚动条）
- `MacOSGUIFactory` 创建 macOS 风格控件（扁平按钮、细线滚动条）
- 客户端切换工厂即可适配不同平台，确保整套控件风格统一

**典型案例：** Java Swing 的 `LookAndFeel` 机制、Qt 的主题切换

---

### 2. 数据库访问层（多数据源适配）

**场景描述：** 系统需要支持多种数据库（MySQL、PostgreSQL、Oracle），不同数据库的连接对象、语句对象、结果集对象需要配套使用。

**抽象工厂应用：**
- `DatabaseFactory` 抽象工厂定义 `createConnection()`、`createStatement()`、`createResultSet()`
- `MySQLFactory` 创建 MySQL 配套对象（`com.mysql.jdbc.Connection` 等）
- `PostgreSQLFactory` 创建 PostgreSQL 配套对象（`org.postgresql.Connection` 等）
- 确保同一数据库的 Connection、Statement、ResultSet 配套使用，避免混用

**典型案例：** JDBC 的 `DriverManager` 体系、Spring Data 的多数据源 Repository 工厂

---

### 3. 游戏开发（角色装备系统）

**场景描述：** 游戏中不同职业（战士、法师、射手）需要配备风格一致的武器、防具、饰品。

**抽象工厂应用：**
- `EquipmentFactory` 抽象工厂定义 `createWeapon()`、`createArmor()`、`createAccessory()`
- `WarriorFactory` 创建战士装备族（巨剑 + 板甲 + 力量戒指）
- `MageFactory` 创建法师装备族（法杖 + 法袍 + 法力宝石）
- 确保同一职业的角色装备风格一致，避免"战士拿法杖"的搭配冲突

---

### 4. 配置文件解析器（多格式支持）

**场景描述：** 系统需要解析多种配置文件格式（JSON、XML、YAML），每种格式的解析器、验证器、转换器需要配套使用。

**抽象工厂应用：**
- `ConfigParserFactory` 抽象工厂定义 `createParser()`、`createValidator()`、`createConverter()`
- `JsonConfigFactory` 创建 JSON 解析族（JacksonParser + JsonSchemaValidator + JsonToMapConverter）
- `XmlConfigFactory` 创建 XML 解析族（DomParser + XmlSchemaValidator + XmlToMapConverter）
- 确保同一配置格式的解析、验证、转换流程配套使用

---

### 5. 云服务商适配（多云架构）

**场景描述：** 企业采用多云策略，需要统一管理 AWS、Azure、阿里云的资源创建（虚拟机、存储桶、负载均衡器）。

**抽象工厂应用：**
- `CloudProviderFactory` 抽象工厂定义 `createVM()`、`createStorage()`、`createLoadBalancer()`
- `AWSFactory` 创建 AWS 资源族（EC2 + S3 + ELB）
- `AzureFactory` 创建 Azure 资源族（Virtual Machine + Blob Storage + Load Balancer）
- 确保同一云厂商的资源配套使用，API 调用风格一致

---

### 6. 主题皮肤系统（应用界面换肤）

**场景描述：** 桌面应用支持多套主题皮肤（深色主题、浅色主题、高对比度主题），每套主题包含配套的字体、颜色方案、图标集。

**抽象工厂应用：**
- `ThemeFactory` 抽象工厂定义 `createFont()`、`createColorScheme()`、`createIconSet()`
- `DarkThemeFactory` 创建深色主题族（浅字体 + 深色背景 + 亮色图标）
- `LightThemeFactory` 创建浅色主题族（深字体 + 浅色背景 + 暗色图标）
- 切换工厂即可全局换肤，确保字体、颜色、图标风格协调

---

### 7. 支付渠道集成（多渠道支付）

**场景描述：** 电商平台对接多种支付渠道（支付宝、微信支付、银联），每种支付渠道的订单创建、支付请求、回调处理需要配套实现。

**抽象工厂应用：**
- `PaymentFactory` 抽象工厂定义 `createOrder()`、`createPaymentRequest()`、`createCallbackHandler()`
- `AlipayFactory` 创建支付宝支付族（AlipayOrder + AlipayRequest + AlipayCallback）
- `WechatPayFactory` 创建微信支付族（WxPayOrder + WxPayRequest + WxPayCallback）
- 确保同一支付渠道的订单、请求、回调配套使用，协议一致

---

## 五、模式优势与局限

### 优势

1. **保证产品族一致性**  
   同一工厂创建的产品必然风格协调，避免混搭导致的视觉或逻辑冲突。

2. **客户端与具体产品解耦**  
   客户端仅依赖抽象工厂和抽象产品接口，无需知道具体产品类名。

3. **产品族切换简单**  
   更换工厂实例即可切换整套产品族，代码无需修改。

4. **新增产品族遵循开闭原则**  
   扩展新产品族（如新增"工业风家具"）只需新增工厂实现类，无需修改现有代码。

### 局限

1. **新增产品种类困难**  
   如果需要在产品族中增加新产品（如新增"沙发"），必须修改抽象工厂接口，所有具体工厂都需同步修改，违反开闭原则。

2. **类数量膨胀**  
   每个产品族的每个产品都需要一个具体类，n 个产品族 × m 种产品 = n×m 个类。

3. **工厂层次复杂**  
   当产品族种类较多时，工厂类的层次结构会变得复杂，维护成本增加。

---

## 六、总结

抽象工厂模式是**"产品族一致性"**问题的最佳解决方案。它通过工厂接口封装产品族的创建逻辑，确保客户端获取的是一组风格协调的配套产品。

**适用判断标准：**
- ✅ 系统需要创建一组相关或依赖的产品
- ✅ 产品需要按主题/风格/平台分组使用
- ✅ 客户端不应依赖具体产品类
- ✅ 产品族整体切换频繁，但产品族内部稳定

**不适用场景：**
- ❌ 仅需创建单个产品（使用工厂方法模式）
- ❌ 产品种类频繁变化（需频繁修改工厂接口）
- ❌ 产品之间无关联关系（无需保证一致性）

> 抽象工厂模式是创建型模式中"粒度最大"的模式，它不关注单个对象的创建细节，而是关注**整个产品族的协调性与一致性**。
