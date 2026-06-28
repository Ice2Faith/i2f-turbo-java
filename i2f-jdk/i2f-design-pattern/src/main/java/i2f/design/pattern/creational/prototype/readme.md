# 原型模式（Prototype Pattern）

> 原型模式是一种创建型设计模式，通过拷贝已有对象来创建新对象，而无需依赖构造函数或知道对象的具体创建细节。

## 一、核心逻辑

原型模式的核心思想是**对象克隆**——用一个已有的对象作为原型，通过复制其状态来创建新的对象。其核心逻辑体现在以下几个方面：

### 1.1 克隆机制

- **避免重复初始化**：当对象的创建过程复杂、耗时或依赖大量外部资源时，通过克隆可以快速复制已有对象的状态，避免重新执行昂贵的初始化逻辑。
- **状态快照**：可以在对象的某个特定时刻创建快照，保留当时的完整状态，用于后续的恢复、对比或分支操作。
- **绕过构造函数**：克隆操作直接复制内存中的对象状态，不经过构造函数，适合那些构造逻辑复杂或带有副作用的对象。

### 1.2 浅拷贝与深拷贝

原型模式中最关键的概念是拷贝深度的选择：

| 拷贝类型 | 基本类型字段 | 引用类型字段 | 性能 | 独立性 |
|---------|------------|------------|------|-------|
| **浅拷贝** | 值复制（独立） | 仅复制引用（共享对象） | 高（JVM native 内存复制） | 低（修改引用对象会相互影响） |
| **深拷贝** | 值复制（独立） | 递归复制整个对象图 | 低（涉及序列化/手动递归） | 高（完全独立互不影响） |

**选择原则：**
- 如果对象仅包含基本类型和不可变对象（如 String、Integer），浅拷贝已足够。
- 如果对象包含可变的引用类型字段，且需要完全独立的副本，必须使用深拷贝。

### 1.3 设计原则体现

- **开闭原则（OCP）**：通过克隆扩展新对象，无需修改现有类的结构。
- **单一职责原则（SRP）**：克隆逻辑封装在原型类内部，职责清晰。
- **依赖倒置原则（DIP）**：客户端依赖原型接口而非具体实现类。

---

## 二、核心组成

原型模式包含以下核心角色：

| 角色 | 说明 | 本包中的实现 |
|-----|------|------------|
| **Prototype（抽象原型）** | 声明克隆方法的接口，通常包含 `clone()` 方法。在 Java 中通常体现为 `Cloneable` 接口。 | `Cloneable` 标记接口 |
| **ConcretePrototype（具体原型）** | 实现克隆方法的具体类，负责复制自身的状态。 | [Sheep](./Sheep.java) |
| **Client（客户端）** | 调用原型对象的克隆方法创建新对象。 | [Test](./Test.java) |

### 2.1 接口设计

在 Java 中，原型模式依赖于两个核心标记接口：

#### Cloneable 接口
- **作用**：JVM 级别的标记接口，声明该类允许被 `Object.clone()` 方法进行内存按位复制。
- **特点**：若不实现此接口直接调用 `clone()`，会抛出 `CloneNotSupportedException`。
- **性能**：native 方法直接操作内存，性能极高。

#### Serializable 接口
- **作用**：序列化标记接口，为基于序列化的深拷贝提供基础。
- **特点**：对象及其所有引用字段都必须实现此接口。
- **用途**：通过序列化 → 反序列化实现递归复制整个对象图。

### 2.2 安全拷贝特点

本包中的 [Sheep](./Sheep.java) 类展示了四种拷贝方式，覆盖了不同场景下的需求：

```java
// 1. JVM 原生浅拷贝（推荐）
@Override
public Sheep clone() {
    return (Sheep) super.clone();
}

// 2. 基于序列化的深拷贝（通用性强）
public Sheep deepClone() {
    // 序列化 → 反序列化，自动处理任意深度对象图
}

// 3. 手动构造浅拷贝（不依赖 Cloneable）
public Sheep shallowCloneDirect() {
    return new Sheep(this.name, this.age, this.color);
}

// 4. 手动递归深拷贝（性能最优）
public Sheep deepCloneDirect() {
    Sheep ret = new Sheep(this.name, this.age, this.color);
    if (this.companion != null) {
        ret.setCompanion(this.companion.deepCloneDirect());
    }
    return ret;
}
```

---

## 三、案例设计

### 3.1 场景选择：克隆羊多莉

本包以"克隆羊多莉（Dolly）"为原型对象，完美契合原型模式的核心思想——**从已有对象复制出新对象**。

**为什么选择这个场景？**
- 克隆羊是现实中"原型复制"的经典案例，易于理解。
- 羊的属性（名称、年龄、毛色）包含基本类型和引用类型，可以清晰演示浅拷贝与深拷贝的差异。
- `companion`（同伴）字段的自引用设计，形成了简单的对象图结构，适合演示递归深拷贝。

### 3.2 原型对象构建

```java
// 构建原型对象
Sheep prototype = new Sheep("Dolly", 3, "白色");
Sheep friend = new Sheep("Bella", 2, "黑色");
prototype.setCompanion(friend);  // 设置同伴引用
```

原型对象 `prototype` 包含：
- 基本类型字段：`name`（String）、`age`（int）、`color`（String）
- 引用类型字段：`companion`（Sheep 类型，指向另一只羊）

### 3.3 浅拷贝演示

```java
// 执行浅拷贝
Sheep shallowClone = prototype.clone();
shallowClone.setName("Dolly-浅拷贝");

// 验证引用共享
System.out.println(prototype.getCompanion() == shallowClone.getCompanion());
// 输出：true（共享同一个 companion 对象）

// 修改克隆体的 companion
shallowClone.getCompanion().setName("Bella-被修改");
// 原型的 companion 也会被修改！
```

**关键观察：**
- 基本类型字段（name、age、color）：原型与克隆体相互独立。
- 引用类型字段（companion）：原型与克隆体**共享同一对象**，修改会相互影响。

### 3.4 深拷贝演示

```java
// 执行深拷贝
Sheep deepClone = prototype.deepClone();
deepClone.setName("Dolly-深拷贝");

// 验证引用独立
System.out.println(prototype.getCompanion() == deepClone.getCompanion());
// 输出：false（完全不同的对象）

// 修改克隆体的 companion
deepClone.getCompanion().setName("Bella-深拷贝独立修改");
// 原型的 companion 不受影响！
```

**关键观察：**
- 所有字段（包括引用类型）都被递归复制，原型与克隆体**完全独立**。
- 修改任一方不会影响另一方，适合需要完全隔离副本的场景。

### 3.5 模式优势

1. **性能优化**：避免重复执行复杂的初始化逻辑（如数据库查询、网络请求、文件读取）。
2. **简化创建**：无需知道对象的具体类型和构造细节，直接通过原型复制。
3. **状态保留**：可以在运行时动态保存对象的某个状态快照。
4. **灵活扩展**：通过选择浅拷贝或深拷贝，平衡性能与独立性的需求。

---

## 四、典型应用场景

### 4.1 对象池与缓存系统

**场景描述**：需要频繁创建结构相似的对象，且初始化成本较高。

**应用示例**：
- **数据库连接池**：预创建连接对象，使用时克隆而非新建。
- **线程池任务对象**：克隆任务模板快速生成新任务。
- **UI 组件缓存**：克隆常用组件（如按钮、表格行）避免重复渲染。
- **Spring Bean 原型作用域**：`@Scope("prototype")` 每次 `getBean()` 返回独立实例。

```java
// 示例：游戏对象池
GameObject prototype = new Enemy("普通怪物", 100, 50);
for (int i = 0; i < 10; i++) {
    Enemy enemy = (Enemy) prototype.clone();
    enemy.setPosition(randomX, randomY);
    gameWorld.add(enemy);
}
```

### 4.2 编辑器与撤销机制

**场景描述**：需要保存对象的历史状态以支持撤销/重做操作。

**应用示例**：
- **图形编辑器**：每次操作前克隆画布状态，撤销时恢复快照。
- **文本编辑器**：克隆文档对象保存历史版本。
- **表单设计器**：克隆组件模板快速生成新表单元素。

```java
// 示例：画布状态管理
List<CanvasState> history = new ArrayList<>();
CanvasState currentState = canvas.getState();
history.add(currentState.deepClone());  // 保存快照

// 撤销操作
CanvasState previousState = history.get(history.size() - 2).deepClone();
canvas.restoreState(previousState);
```

### 4.3 配置与模板系统

**场景描述**：基于配置模板快速生成多个实例，每个实例可独立定制。

**应用示例**：
- **邮件模板**：克隆邮件模板对象，替换收件人和内容后发送。
- **订单模板**：克隆标准订单对象，修改商品列表生成新订单。
- **虚拟机克隆**：基于镜像模板快速创建多个虚拟机实例。

```java
// 示例：邮件发送
EmailTemplate template = new EmailTemplate();
template.setSubject("活动通知");
template.setFrom("noreply@example.com");
template.setHtmlContent("<h1>活动详情...</h1>");

for (User user : users) {
    Email email = template.clone();
    email.setTo(user.getEmail());
    emailService.send(email);
}
```

### 4.4 游戏开发

**场景描述**：游戏中大量相似对象（怪物、道具、粒子效果）的快速生成。

**应用示例**：
- **怪物克隆**：基于基础怪物原型批量生成不同位置的怪物。
- **粒子系统**：克隆粒子原型快速创建大量视觉效果。
- **关卡模板**：克隆关卡配置快速生成不同难度版本。

### 4.5 数据快照与版本控制

**场景描述**：需要在关键操作前后保存数据状态，用于对比、审计或恢复。

**应用示例**：
- **事务快照**：数据库事务开始前克隆数据对象，失败时回滚。
- **版本管理**：每次修改前克隆文档对象保存历史版本。
- **审计日志**：记录操作前后的对象状态快照。

### 4.6 原型注册表（Prototype Registry）

**场景描述**：维护一个原型对象缓存池，客户端通过名称获取原型并克隆。

**应用示例**：
- **形状工厂**：预注册圆形、方形等原型，按需克隆。
- **协议处理器**：预注册不同协议的处理模板，动态克隆使用。
- **Spring BeanFactory**：内部维护单例缓存（类似原型注册表）。

```java
// 示例：原型注册表
class ShapeRegistry {
    private static Map<String, Shape> prototypes = new HashMap<>();
    
    static {
        prototypes.put("circle", new Circle());
        prototypes.put("square", new Square());
        prototypes.put("triangle", new Triangle());
    }
    
    public static Shape getPrototype(String type) {
        return prototypes.get(type).clone();
    }
}
```

### 4.7 分布式系统与序列化传输

**场景描述**：对象需要在网络间传输或持久化，深拷贝确保数据独立性。

**应用示例**：
- **RPC 调用**：序列化参数对象传输到远程服务。
- **消息队列**：深拷贝消息对象确保消费者修改不影响生产者。
- **缓存同步**：克隆缓存对象防止并发修改导致的数据不一致。

---

## 五、使用建议

### 5.1 适用条件

✅ **推荐使用原型模式的场景：**
- 对象创建成本高（初始化耗时长、依赖资源多）。
- 需要保留对象的状态快照。
- 需要避免复杂的构造函数或工厂体系。
- 运行时动态创建结构相似的对象。

❌ **不推荐使用的场景：**
- 对象结构简单，直接使用 `new` 即可。
- 对象包含不可序列化的外部资源（如文件句柄、网络连接）。
- 需要严格控制对象创建过程（构造函数中有重要校验逻辑）。

### 5.2 注意事项

1. **深拷贝 vs 浅拷贝的选择**：
   - 分析对象图中是否存在可变的引用类型字段。
   - 如果存在，必须使用深拷贝或手动处理引用字段。
   
2. **循环引用处理**：
   - 深拷贝时若对象图中存在循环引用，序列化方式可自动处理。
   - 手动递归拷贝需要额外逻辑检测循环引用，防止栈溢出。

3. **性能权衡**：
   - `Object.clone()` 性能最高（native 内存复制）。
   - 序列化深拷贝性能较低（涉及 IO 操作）。
   - 手动深拷贝性能居中（无 IO 开销，但需编写递归逻辑）。

4. **构造函数绕过**：
   - `clone()` 方法不经过构造函数，可能跳过重要的初始化或校验逻辑。
   - 如需触发构造逻辑，使用 `shallowCloneDirect()` 或 `deepCloneDirect()`。

5. **Cloneable 接口的缺陷**：
   - `Cloneable` 是标记接口，不包含 `clone()` 方法声明。
   - `Object.clone()` 是 protected 方法，需重写为 public。
   - 许多设计模式专家建议使用拷贝构造函数或工厂方法替代 `clone()`。

---

## 六、参考实现

### 6.1 包结构

```
i2f.design.pattern.creational.prototype
├── Sheep.java          # 克隆羊原型类（浅拷贝 + 深拷贝）
├── Test.java           # 调用演示
├── package-info.java   # 包级别设计模式说明
└── readme.md           # 本文档
```

### 6.2 核心类

- **[Sheep](./Sheep.java)**：原型类实现，演示四种拷贝方式（JVM 浅拷贝、序列化深拷贝、手动浅拷贝、手动深拷贝）。
- **[Test](./Test.java)**：客户端演示，对比浅拷贝与深拷贝的行为差异。

### 6.3 JDK 与框架中的典型应用

| 框架/库 | 应用场景 | 说明 |
|--------|---------|------|
| **JDK** | `ArrayList.clone()`、`HashMap.clone()` | 集合类的浅拷贝实现 |
| **JDK** | `java.util.Date.clone()` | 时间对象的状态快照 |
| **Spring** | `@Scope("prototype")` Bean | 每次获取返回独立实例 |
| **Spring** | `BeanUtils.copyProperties()` | 浅拷贝属性到新对象 |
| **序列化机制** | `Serializable` 接口 | 基于序列化的深拷贝实现 |

---

## 总结

原型模式通过**对象克隆**提供了一种灵活的对象创建方式，特别适合以下情况：

1. **性能敏感**：避免重复的昂贵初始化操作。
2. **状态管理**：需要保存和恢复对象的某个时刻状态。
3. **简化创建**：绕过复杂的构造函数或工厂体系。
4. **批量生成**：快速创建结构相似但细节不同的对象。

在实际开发中，应根据对象图的复杂度、性能要求和独立性需求，合理选择浅拷贝或深拷贝策略。对于包含复杂引用关系的对象，优先考虑序列化深拷贝或手动递归深拷贝；对于简单对象，JVM 原生浅拷贝已足够高效。

> 💡 **设计原则提醒**：原型模式是工具而非银弹，避免过度设计。当 `new` 关键字足以满足需求时，无需引入克隆机制。
