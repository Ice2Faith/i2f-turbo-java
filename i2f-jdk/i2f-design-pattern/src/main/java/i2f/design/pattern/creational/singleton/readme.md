# 单例模式（Singleton Pattern）详细文档

## 一、核心逻辑

单例模式是创建型设计模式中最基础且应用最广的模式之一，其核心逻辑围绕**全局唯一性**展开：

### 1.1 唯一性保证（Uniqueness）
- **私有化构造方法**：将构造方法声明为 `private`，剥夺外部通过 `new` 关键字创建实例的权限
- **内部掌控实例化权**：由类自身负责创建和维护唯一实例，外部只能通过统一入口获取
- **全局访问点**：提供静态方法 `getInstance()` 作为全局唯一的访问入口

### 1.2 线程安全性（Thread Safety）
- **饿汉式**：利用 JVM 类加载机制（`<clinit>` 类初始化锁）天然保证线程安全，无需显式同步
- **懒汉式**：通过 `volatile` + 双重检查锁（DCL, Double-Checked Locking）实现延迟加载与线程安全的平衡
  - `volatile` 防止对象创建三步（分配内存 → 初始化 → 赋值引用）发生重排序
  - 双重检查避免不必要的锁竞争，保证高并发读性能

### 1.3 延迟加载 vs 提前初始化（Lazy vs Eager）
- **饿汉式**：类加载时立即创建实例，适合实例轻量或确定会被使用的场景
- **懒汉式**：首次调用 `getInstance()` 时才创建，适合实例重量或不一定使用的场景

---

## 二、核心组成

单例模式由以下三个核心角色组成：

| 角色 | 说明 | 本包示例 |
| --- | --- | --- |
| **唯一实例持有者** | 静态字段持有全局唯一的实例引用 | `private static Earth instance` |
| **私有构造方法** | 阻止外部实例化，将创建权收归类自身 | `private Earth()` |
| **全局访问点** | 静态方法提供统一访问入口 | `public static Earth getInstance()` |

### 2.1 饿汉式核心组成

```java
public class Earth {
    // 1. 静态字段持有实例（类加载时立即初始化）
    private static Earth instance = initialInstance();
    
    // 2. 私有构造方法
    private Earth() {}
    
    // 3. 初始化方法（职责分离）
    private static Earth initialInstance() {
        Earth ret = new Earth();
        // 其他初始化逻辑
        return ret;
    }
    
    // 4. 全局访问点（无锁高性能）
    public static Earth getInstance() {
        return instance;
    }
}
```

**特点**：
- 无需 `volatile`：JVM 类加载机制通过 `<clinit>` 保证原子性与可见性
- 无锁设计：并发性能等同于普通字段读取
- 内存占用早：类加载即分配，适合轻量对象

### 2.2 懒汉式核心组成（DCL 双重检查锁）

```java
public class Earth {
    // 1. volatile 防止重排序
    private static volatile Earth instance;
    
    // 2. 私有构造方法
    private Earth() {}
    
    // 3. 初始化方法
    private static Earth initialInstance() {
        Earth ret = new Earth();
        return ret;
    }
    
    // 4. 双重检查锁全局访问点
    public static Earth getInstance() {
        if (instance != null) {  // 第一次检查：避免无谓锁竞争
            return instance;
        }
        synchronized (Earth.class) {
            if (instance != null) {  // 第二次检查：防止竞态条件
                return instance;
            }
            instance = initialInstance();
        }
        return instance;
    }
}
```

**特点**：
- 必须使用 `volatile`：防止 JVM 重排序导致其他线程拿到半初始化对象
- 延迟加载：首次访问才创建，节省启动资源
- 高性能：实例创建后，绝大多数调用走无锁路径

---

## 三、案例设计

### 3.1 案例选择：Earth（地球）

本包使用 `Earth`（地球）作为单例模式的典型案例，理由如下：

1. **天然唯一性**：地球在宇宙中只有一个实例，符合单例模式的语义直觉
2. **全局访问需求**：任何地方需要"地球"时，应该获取的是同一个实例
3. **资源消耗考量**：地球包含大量状态（大气、海洋、生物圈等），实例化成本高，适合单例复用
4. **线程安全场景**：多线程环境下访问地球状态，必须保证数据一致性

### 3.2 案例实现分析

#### 饿汉式 Earth（`eager/Earth.java`）

**设计意图**：地球实例在类加载时立即创建，适用于系统启动即需要地球资源的场景。

**实现要点**：
- 利用 JVM 类加载机制天然线程安全，无需任何同步控制
- 通过 `initialInstance()` 方法分离创建与初始化逻辑，便于扩展
- `getInstance()` 方法零开销，性能最优

**适用场景**：
- 地球资源（大气模型、地磁数据）在系统启动时就已确定
- 频繁访问地球状态，提前初始化可避免首次调用延迟

#### 懒汉式 Earth（`lazy/Earth.java`）

**设计意图**：地球实例在首次访问时延迟创建，适用于系统启动时不确定是否需要地球资源的场景。

**实现要点**：
- `volatile` 关键字禁止对象创建三步的重排序
- 双重检查锁平衡了延迟加载与并发性能
- 外层无锁检查覆盖 99% 的高频读路径

**适用场景**：
- 地球资源初始化耗时（如加载卫星数据、建立大气模型）
- 系统运行期间可能不会访问地球资源，延迟加载节省启动开销

### 3.3 测试用例

```java
// eager/Test.java 或 lazy/Test.java
public class Test {
    public static void main(String[] args) {
        Earth item = Earth.getInstance();
        System.out.println(item);
    }
}
```

**验证目标**：
- 多次调用 `getInstance()` 返回同一实例引用
- 多线程并发调用保证唯一性
- 实例创建后状态一致

---

## 四、典型应用场景

单例模式在工业级框架和实际业务中无处不在，以下是七大典型应用场景：

### 4.1 配置管理器（Configuration Manager）
- **场景**：系统全局配置（如数据库连接串、缓存地址、日志级别）
- **理由**：配置数据全局唯一，避免重复读取配置文件
- **示例**：
  - Spring Boot `@ConfigurationProperties` 绑定的配置类
  - `java.util.ResourceBundle` 资源束缓存
  - 自定义 `AppConfig` 单例集中管理所有配置项

### 4.2 连接池/线程池（Connection Pool / Thread Pool）
- **场景**：数据库连接池、Redis 连接池、HTTP 连接池、线程池
- **理由**：连接资源昂贵，需集中管理、复用共享
- **示例**：
  - HikariCP `HikariDataSource`（Spring 中默认单例 Bean）
  - `java.util.concurrent.ThreadPoolExecutor`（通过 Spring 管理为单例）
  - Jedis `JedisPool`、Lettuce `StatefulRedisConnection`

### 4.3 日志管理器（Logger Manager）
- **场景**：全局日志记录器
- **理由**：日志输出需要统一格式、统一目的地，避免并发写冲突
- **示例**：
  - `java.util.logging.LogManager#getLogManager()`
  - SLF4J `LoggerFactory` 缓存的 Logger 实例
  - Log4j2 `LogManager` 全局日志上下文

### 4.4 缓存管理器（Cache Manager）
- **场景**：本地缓存、分布式缓存客户端
- **理由**：缓存数据全局共享，避免重复创建缓存实例
- **示例**：
  - Spring `CacheManager`（EhCache、Caffeine 默认单例）
  - Caffeine `CacheBuilder.build()` 缓存实例
  - Redis 客户端 `RedisTemplate`（Spring 中单例）

### 4.5 事务管理器（Transaction Manager）
- **场景**：数据库事务管理、分布式事务协调
- **理由**：事务状态需全局唯一，避免事务上下文混乱
- **示例**：
  - Spring `PlatformTransactionManager`（如 `DataSourceTransactionManager`）
  - JTA `TransactionManager`
  - `TransactionSynchronizationManager` 事务同步管理器

### 4.6 ID 生成器（ID Generator）
- **场景**：全局唯一 ID 生成（如雪花算法、UUID、数据库序列）
- **理由**：ID 生成需要全局状态（如工作节点 ID、序列号），单例保证不重复
- **示例**：
  - 雪花算法 `SnowflakeIdGenerator`
  - 数据库序列 `SequenceGenerator`
  - 分布式 ID 服务客户端（如美团 Leaf、滴滴 TinyID）

### 4.7 应用上下文/容器（Application Context / IoC Container）
- **场景**：Spring `ApplicationContext`、Servlet `ServletContext`
- **理由**：容器管理所有 Bean，必须全局唯一以保证依赖注入一致性
- **示例**：
  - Spring `ApplicationContext`（应用上下文单例）
  - `DefaultSingletonBeanRegistry#singletonObjects` 单例缓存
  - `ServletContext`（每个 Web 应用一个实例）

---

## 五、使用建议与注意事项

### 5.1 何时使用
- 需要全局唯一访问点
- 对象创建成本高（资源密集、初始化耗时）
- 需要集中管理共享资源（连接池、缓存、配置）
- 对象无状态或状态全局共享

### 5.2 何时避免
- 对象状态频繁变化且需要隔离（如用户会话、请求上下文）
- 单元测试困难（单例导致全局状态污染）
- 过度设计（简单工具类用静态方法即可）

### 5.3 线程安全要点
- **饿汉式**：天然线程安全，无需额外控制
- **懒汉式**：必须使用 `volatile` + DCL，缺一不可
- **静态内部类**：利用 JVM 类加载机制延迟加载且线程安全（推荐）
- **枚举单例**：`enum` 天然防反射破坏，绝对单例（最安全）

### 5.4 破坏单例的陷阱
- **反射攻击**：通过 `Constructor.setAccessible(true)` 调用私有构造方法
- **序列化破坏**：实现 `Serializable` 时需重写 `readResolve()` 方法
- **克隆破坏**：实现 `Cloneable` 时需重写 `clone()` 方法抛出异常
- **类加载器**：不同类加载器加载同一类会产生不同实例

---

## 六、参考实现

### 包结构

```
i2f.design.pattern.creational.singleton
├── eager/
│   ├── Earth.java        # 饿汉式实现
│   └── Test.java         # 测试用例
├── lazy/
│   ├── Earth.java        # 懒汉式实现（DCL）
│   └── Test.java         # 测试用例
├── package-info.java     # 包说明
└── readme.md             # 本文档
```

### 核心类

- [饿汉式 Earth](./eager/Earth.java)：类加载即初始化，无锁高性能
- [懒汉式 Earth](./lazy/Earth.java)：延迟加载 + 双重检查锁
- [包说明](./package-info.java)：模式定义与分类

---

> **总结**：单例模式通过私有化构造方法、静态实例持有者和全局访问点三要素，保证类的实例全局唯一。饿汉式与懒汉式各有优劣，选择时需权衡初始化时机、线程安全与性能需求。在配置管理、连接池、日志管理等场景中，单例模式是不可或缺的设计工具。
