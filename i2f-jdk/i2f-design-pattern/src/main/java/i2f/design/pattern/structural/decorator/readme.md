# 装饰器模式（Decorator Pattern）

> 动态地给一个对象添加一些额外的职责，就增加功能来说，装饰器模式比生成子类更为灵活。

## 一、模式概述

### 1.1 核心逻辑

装饰器模式通过**组合而非继承**的方式，在运行时动态地为对象添加新功能。其核心机制是：

1. **接口一致性**：装饰器与被装饰对象实现相同接口，保证客户端可以透明地使用
2. **功能委派**：装饰器持有组件引用，将请求委派给被装饰对象
3. **行为增强**：装饰器在委派前后添加额外职责，实现功能叠加
4. **嵌套组合**：多个装饰器可以层层嵌套，实现灵活的功能组合

### 1.2 核心组成

| 角色 | 说明 | 本案例对应类 |
|------|------|-------------|
| **抽象组件（Component）** | 定义对象接口，规范被装饰对象和装饰器的共同行为 | `Coffee` |
| **具体组件（Concrete Component）** | 实现抽象组件的基础对象，可以被装饰 | `EspressoCoffee` |
| **抽象装饰器（Decorator）** | 持有组件引用，实现与组件相同的接口，提供默认委派逻辑 | `CoffeeDecorator` |
| **具体装饰器（Concrete Decorator）** | 实现具体的装饰行为，在委派前后添加额外职责 | `MilkDecorator`、`SugarDecorator`、`CreamDecorator` |
| **客户端（Client）** | 面向抽象组件编程，透明地使用被装饰的对象 | `Test` |

## 二、案例设计说明

### 2.1 场景背景

以**咖啡店点单**为业务场景：
- 基础咖啡（浓缩咖啡）具有固定的价格和描述
- 顾客可以根据口味偏好添加各种配料（牛奶、糖、奶油等）
- 每种配料会增加价格和改变描述
- 配料可以任意组合，形成不同的咖啡饮品

### 2.2 设计实现

#### 类层次结构

```
抽象组件              具体组件                 抽象装饰器            具体装饰器
─────────────────   ───────────────────────   ─────────────────   ─────────────────────────────
Coffee               EspressoCoffee           CoffeeDecorator     MilkDecorator（加牛奶 +3元）
                                              CoffeeDecorator     SugarDecorator（加糖 +1.5元）
                                              CoffeeDecorator     CreamDecorator（加奶油 +4元）
```

#### 核心机制

```java
// 1. 抽象组件：定义咖啡接口
public abstract class Coffee {
    public abstract String getDescription();
    public abstract double getCost();
}

// 2. 具体组件：基础浓缩咖啡
public class EspressoCoffee extends Coffee {
    public String getDescription() { return "浓缩咖啡"; }
    public double getCost() { return getPrice(); }
}

// 3. 抽象装饰器：持有咖啡引用，实现委派
public abstract class CoffeeDecorator extends Coffee {
    protected Coffee coffee;
    
    public String getDescription() {
        return coffee.getDescription() + " + " + getName();
    }
    
    public double getCost() {
        return coffee.getCost() + getPrice();
    }
}

// 4. 具体装饰器：添加具体配料
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee, "加牛奶", 3.0);
    }
}
```

#### 使用方式

```java
// 基础咖啡
Coffee espresso = new EspressoCoffee("浓缩咖啡", 12.0);

// 单层装饰：加牛奶
Coffee milkEspresso = new MilkDecorator(espresso);

// 多层装饰：加牛奶 + 糖 + 奶油
Coffee premiumCoffee = new CreamDecorator(
    new SugarDecorator(
        new MilkDecorator(espresso)
    )
);

// 客户端面向抽象编程，统一调用
System.out.println(premiumCoffee.getDescription()); // 浓缩咖啡 + 牛奶 + 糖 + 奶油
System.out.println(premiumCoffee.getCost());        // 20.5元
```

### 2.3 设计优势

1. **遵循开闭原则**：新增配料只需新增装饰器类，无需修改已有代码
2. **动态添加职责**：可以在运行时灵活组合不同的装饰器
3. **避免类爆炸**：N 种配料只需 N 个装饰器类，而非 2^N 个子类
4. **客户端透明**：面向抽象编程，无需依赖具体装饰器类
5. **装饰可嵌套**：实现功能的层层叠加，保持接口一致性

### 2.4 关键验证

案例中通过以下测试验证了装饰器模式的特性：

- ✅ **每次创建新实例**：装饰器每次包装都创建全新对象，不共享状态
- ✅ **装饰顺序对价格无影响**：`牛奶+糖` 与 `糖+牛奶` 价格相同（加法交换律）
- ✅ **装饰顺序对描述有影响**：描述按照装饰顺序拼接，体现装饰层次

## 三、典型应用场景

### 3.1 Java IO 流体系

最经典的装饰器模式应用：

```java
// 基础流
InputStream fileStream = new FileInputStream("data.txt");

// 装饰：添加缓冲功能
InputStream bufferedStream = new BufferedInputStream(fileStream);

// 装饰：添加数据读取功能
DataInputStream dataStream = new DataInputStream(bufferedStream);

// 装饰：添加行读取功能
BufferedReader reader = new BufferedReader(
    new InputStreamReader(fileStream)
);
```

- `BufferedInputStream`：缓冲装饰器
- `DataInputStream`：数据类型转换装饰器
- `LineNumberInputStream`：行号追踪装饰器

### 3.2 Web 请求/响应包装

Servlet API 中的装饰器应用：

```java
// 缓存请求体，支持重复读取
ContentCachingRequestWrapper wrappedRequest = 
    new ContentCachingRequestWrapper(request);

// 记录响应体
ContentCachingResponseWrapper wrappedResponse = 
    new ContentCachingResponseWrapper(response);
```

- `HttpServletRequestWrapper`：请求包装器基类
- `HttpServletResponseWrapper`：响应包装器基类
- 可自定义装饰器实现日志记录、参数校验、权限检查等

### 3.3 集合工具类装饰

Collections 提供的只读/同步装饰器：

```java
List<String> originalList = new ArrayList<>();

// 装饰：不可变列表
List<String> readOnlyList = Collections.unmodifiableList(originalList);

// 装饰：线程安全列表
List<String> syncList = Collections.synchronizedList(originalList);
```

### 3.4 Spring 事务装饰

Spring 对缓存的装饰器实现：

```java
// TransactionAwareCacheDecorator
// 在缓存操作前后添加事务同步逻辑
Cache decoratedCache = new TransactionAwareCacheDecorator(targetCache);
```

### 3.5 权限/日志装饰器

在业务方法调用前后添加横切关注点：

```java
public class LoggingDecorator implements Service {
    private Service target;
    
    public Object execute(Request req) {
        log.info("开始执行: " + req);
        try {
            Object result = target.execute(req);
            log.info("执行成功");
            return result;
        } catch (Exception e) {
            log.error("执行失败", e);
            throw e;
        }
    }
}
```

### 3.6 性能监控装饰

为方法调用添加性能统计：

```java
public class PerformanceDecorator implements Service {
    private Service target;
    
    public Object execute(Request req) {
        long start = System.currentTimeMillis();
        try {
            return target.execute(req);
        } finally {
            long cost = System.currentTimeMillis() - start;
            metrics.record(req.getType(), cost);
        }
    }
}
```

### 3.7 数据转换装饰器

在数据传递过程中进行格式转换：

```java
// 加密装饰器
public class EncryptionDecorator implements DataService {
    public String getData() {
        String raw = target.getData();
        return encrypt(raw);
    }
}

// 压缩装饰器
public class CompressionDecorator implements DataService {
    public String getData() {
        String raw = target.getData();
        return compress(raw);
    }
}
```

### 3.8 GUI 组件装饰

为 UI 组件添加边框、滚动条等装饰：

```java
// Swing 中的装饰器应用
JTextArea textArea = new JTextArea();
JScrollPane scrollPane = new JScrollPane(textArea);  // 添加滚动条
Border border = BorderFactory.createTitledBorder("标题");
textArea.setBorder(border);  // 添加边框
```

## 四、使用注意事项

### 4.1 适用条件

✅ **适合使用装饰器模式的场景**：
- 需要动态、透明地为对象添加职责
- 不能或不想通过继承扩展功能（如 final 类）
- 功能需要灵活组合，可能产生大量子类
- 需要在运行时根据配置动态添加功能

❌ **不适合使用的场景**：
- 对象只需要一次性扩展，且扩展固定
- 装饰器层次过深（超过 5 层），影响可读性和调试
- 需要访问被装饰对象的内部私有成员
- 装饰器之间存在强依赖关系

### 4.2 实现建议

1. **保持接口一致性**：装饰器必须实现与组件相同的接口
2. **简化装饰器接口**：装饰器接口应尽量精简，只保留必要方法
3. **注意装饰顺序**：某些场景下装饰顺序会影响最终结果（如加密后压缩 vs 压缩后加密）
4. **避免过度装饰**：装饰层数过多时应考虑重构
5. **考虑性能开销**：每层装饰都会增加方法调用开销

### 4.3 与相关模式的区别

| 模式 | 核心差异 |
|------|---------|
| **装饰器 vs 代理** | 装饰器关注功能增强，代理关注访问控制 |
| **装饰器 vs 策略** | 装饰器叠加功能，策略替换算法 |
| **装饰器 vs 适配器** | 装饰器接口不变，适配器改变接口 |
| **装饰器 vs 组合** | 装饰器是单对象增强，组合是树形结构 |

## 五、总结

装饰器模式是一种强大的**结构型设计模式**，通过组合而非继承的方式实现功能动态扩展。它完美体现了**开闭原则**（对扩展开放，对修改关闭），在 Java IO 流、Web 框架、集合工具等场景中有广泛应用。

本案例通过咖啡店配料装饰的生动场景，展示了装饰器模式的核心机制和优势。在实际开发中，当面临功能灵活组合、避免类爆炸、运行时动态增强等需求时，装饰器模式是理想的选择。

> **设计模式是工具而非银弹**，使用时应结合实际业务场景，避免过度设计。
