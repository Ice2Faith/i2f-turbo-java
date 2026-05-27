# 适配器模式（Adapter Pattern）

> 适配器模式是一种**结构型设计模式**，它允许接口不兼容的类能够协同工作。通过创建一个适配器类，将一个类的接口转换为客户端期望的另一个接口，从而解决接口不匹配的问题。

---

## 一、核心逻辑

适配器模式的核心逻辑可以概括为以下三点：

### 1. **接口转换（Interface Conversion）**
将不兼容的接口转换为兼容的接口，使得原本无法一起工作的类可以协同工作。客户端只需要面向目标接口编程，无需关心底层实现细节。

### 2. **委托转发（Delegation）**
适配器内部持有适配者（Adaptee）的实例，当客户端调用目标接口时，适配器将请求转发给适配者的特定方法，实现"翻译"功能。

### 3. **透明性（Transparency）**
对客户端而言，适配器的存在是透明的。客户端通过统一的目标接口调用，无需知道背后是否存在适配器，也无需知道具体使用的是哪个适配者。

---

## 二、核心组成

适配器模式包含以下四个核心角色：

| 角色 | 说明 | 本案例对应类 |
|------|------|--------------|
| **Target（目标接口）** | 定义客户端期望使用的标准接口 | `MediaPlayer` |
| **Adapter（适配器）** | 实现 Target 接口，内部持有 Adaptee 实例，负责接口转换 | `MediaAdapter` |
| **Adaptee（适配者）** | 已存在的、但接口不兼容的类 | `AdvancedMediaPlayer` 及其实现 `VlcPlayer`、`Mp4Player` |
| **Client（客户端）** | 使用 Target 接口的对象 | `AudioPlayer`（同时也作为 Concrete Target） |

### 类图结构

```
┌──────────────────────┐
│   <<interface>>      │
│   MediaPlayer        │  ◄──── Target（目标接口）
│   ─────────────────  │
│   + play(type, file) │
└──────────┬───────────┘
           │ implements
           │
┌──────────▼───────────┐         ┌──────────────────────┐
│   AudioPlayer        │         │   <<interface>>      │
│   (Concrete Target)  │         │   AdvancedMediaPlayer│  ◄──── Adaptee Interface
│   ─────────────────  │         │   ─────────────────  │
│   + play(type, file) │         │   + playVlc(file)    │
└──────────┬───────────┘         │   + playMp4(file)    │
           │                     └──────────┬───────────┘
           │ 创建并使用                     │ implements
           ▼                     ┌──────────┴───────────┐
┌──────────────────────┐         │   VlcPlayer          │
│   MediaAdapter       │         │   Mp4Player          │
│   (Adapter)          │         │   (Concrete Adaptee) │
│   ─────────────────  │         └──────────────────────┘
│   - advancedPlayer   │
│   + play(type, file) │
└──────────────────────┘
```

### 关键设计特点

1. **对象适配器（Object Adapter）**：本案例采用对象适配器模式，通过**组合**（而非继承）持有 Adaptee 实例，符合"组合优于继承"的设计原则。
2. **运行时动态适配**：在 `MediaAdapter` 构造函数中根据 `audioType` 动态创建对应的播放器实例。
3. **接口隔离**：`AdvancedMediaPlayer` 定义了 `playVlc()` 和 `playMp4()` 两个方法，但每个具体播放器只实现自己支持的格式，体现了接口隔离原则。

---

## 三、案例设计

### 场景选择：媒体播放器系统

#### 为什么选择这个场景？

1. **贴近生活**：媒体播放器是日常熟悉的概念，易于理解
2. **接口不兼容典型**：不同格式播放器有各自的 API（如 `playVlc()` vs `playMp4()`）
3. **扩展示范清晰**：从只支持 MP3 扩展到支持 VLC、MP4，展示适配器的价值

#### 案例实现流程

**步骤 1：定义目标接口**

```java
public interface MediaPlayer {
    void play(String audioType, String fileName);
}
```

客户端期望的统一播放接口。

**步骤 2：实现基础播放器（Concrete Target）**

```java
public class AudioPlayer implements MediaPlayer {
    private static final String SUPPORTED_FORMAT = "mp3";
    
    @Override
    public void play(String audioType, String fileName) {
        // 1. 原生支持的格式，直接播放
        if (SUPPORTED_FORMAT.equalsIgnoreCase(audioType)) {
            System.out.println("  [AudioPlayer] 正在播放音频: " + fileName);
            return;
        }
        
        // 2. 不支持的格式，通过适配器转换
        MediaAdapter mediaAdapter = new MediaAdapter(audioType);
        if (mediaAdapter != null) {
            System.out.println("  [AudioPlayer] 检测到不支持的格式，使用适配器转换...");
            mediaAdapter.play(audioType, fileName);
        }
    }
}
```

`AudioPlayer` 本身只支持 MP3，但通过引入适配器，能够无缝扩展支持其他格式。

**步骤 3：定义适配者接口及实现**

```java
public interface AdvancedMediaPlayer {
    void playVlc(String fileName);
    void playMp4(String fileName);
}

public class VlcPlayer implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("  [VlcPlayer] 正在播放 VLC 视频: " + fileName);
    }
    
    @Override
    public void playMp4(String fileName) {
        System.out.println("  [VlcPlayer] 不支持 MP4 格式");
    }
}

public class Mp4Player implements AdvancedMediaPlayer {
    @Override
    public void playVlc(String fileName) {
        System.out.println("  [Mp4Player] 不支持 VLC 格式");
    }
    
    @Override
    public void playMp4(String fileName) {
        System.out.println("  [Mp4Player] 正在播放 MP4 视频: " + fileName);
    }
}
```

这些是已有的、接口不兼容的高级播放器。

**步骤 4：创建适配器**

```java
public class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedMusicPlayer;
    
    public MediaAdapter(String audioType) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            this.advancedMusicPlayer = new VlcPlayer();
        } else if ("mp4".equalsIgnoreCase(audioType)) {
            this.advancedMusicPlayer = new Mp4Player();
        }
    }
    
    @Override
    public void play(String audioType, String fileName) {
        if ("vlc".equalsIgnoreCase(audioType)) {
            advancedMusicPlayer.playVlc(fileName);
        } else if ("mp4".equalsIgnoreCase(audioType)) {
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}
```

适配器是"翻译官"：
- 实现 `MediaPlayer` 接口（面向客户端）
- 内部持有 `AdvancedMediaPlayer` 实例（面向适配者）
- 将 `play()` 调用转换为 `playVlc()` 或 `playMp4()` 调用

**步骤 5：客户端使用**

```java
public class Test {
    public static void main(String[] args) {
        MediaPlayer audioPlayer = new AudioPlayer();
        
        // 客户端统一使用 MediaPlayer 接口
        audioPlayer.play("mp3", "平凡之路.mp3");   // 直接支持
        audioPlayer.play("vlc", "复仇者联盟4.vlc"); // 通过适配器
        audioPlayer.play("mp4", "星际穿越.mp4");    // 通过适配器
        audioPlayer.play("avi", "测试视频.avi");    // 不支持，优雅降级
    }
}
```

客户端无需知道 `VlcPlayer`、`Mp4Player` 的存在，也无需知道 `MediaAdapter` 的存在，只需面向 `MediaPlayer` 接口编程。

#### 模式优势体现

1. **开闭原则**：新增播放器（如 `AviPlayer`）只需新增适配者类和适配器映射，无需修改已有代码
2. **单一职责**：每个播放器只负责自己擅长的格式
3. **依赖倒置**：客户端依赖 `MediaPlayer` 抽象，而非具体实现
4. **接口转换集中管理**：所有适配逻辑封装在 `MediaAdapter` 中，便于维护

---

## 四、典型应用场景

### 1. **第三方库/SDK 集成**

**场景**：引入第三方库，但其接口与现有系统不兼容。

**示例**：
- 接入支付宝、微信支付 SDK，各自接口不同，通过适配器统一为 `PaymentService` 接口
- 接入多个短信服务商（阿里云、腾讯云、华为云），通过适配器统一为 `SmsService` 接口

```java
public interface PaymentService {
    boolean pay(Order order);
}

public class AlipayAdapter implements PaymentService {
    private AlipayClient alipayClient; // 第三方 SDK
    
    @Override
    public boolean pay(Order order) {
        // 将统一支付请求转换为支付宝 SDK 调用
        return alipayClient.execute(new AlipayTradePagePayRequest());
    }
}
```

### 2. **遗留系统改造**

**场景**：老系统接口陈旧，但无法直接修改，需要通过适配器封装为新接口。

**示例**：
- 老系统的 `UserDAO` 使用 JDBC 直接操作数据库，新系统期望使用 MyBatis 的 `UserMapper`
- 创建适配器将新接口调用转换为老系统方法调用

### 3. **日志框架统一门面**

**场景**：系统中存在多种日志实现（log4j、logback、JUL），通过适配器统一为 Slf4j 接口。

**示例**：Slf4j 的各种桥接器（`slf4j-log4j12`、`slf4j-logback`）

```java
// Slf4j 门面接口
Logger logger = LoggerFactory.getLogger(MyClass.class);
logger.info("日志内容"); // 底层自动适配到具体日志框架
```

### 4. **数据格式转换**

**场景**：不同系统使用不同的数据格式，需要适配器进行转换。

**示例**：
- XML ↔ JSON 转换器
- 不同数据库方言适配器（MySQL、PostgreSQL、Oracle）
- Spring MVC 的 `HttpMessageConverter`（将 HTTP 请求体转换为 Java 对象）

### 5. **Spring MVC 的 HandlerAdapter**

**场景**：Spring MVC 支持多种 Handler（`@Controller`、`HttpRequestHandler`、`SimpleController`），每种 Handler 调用方式不同。

**示例**：通过 `HandlerAdapter` 将不同类型的 Handler 适配为统一的 `handle()` 方法调用。

```java
// DispatcherServlet 内部逻辑
HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
ha.handle(request, response, mappedHandler.getHandler()); // 统一调用
```

### 6. **JDBC 驱动适配**

**场景**：Java 应用统一使用 `java.sql.Connection`、`java.sql.Statement` 接口，但各数据库驱动实现不同。

**示例**：`DriverManager` 加载不同数据库驱动（MySQL、Oracle、PostgreSQL），通过 JDBC 标准接口屏蔽底层差异。

```java
// 应用代码面向 JDBC 标准接口
Connection conn = DriverManager.getConnection("jdbc:mysql://...");
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM users");

// 底层自动适配到 MySQL/Oracle/PostgreSQL 驱动
```

### 7. **Servlet Filter 包装器**

**场景**：需要在原有 `HttpServletRequest` 基础上增加功能（如读取请求体多次），通过 `HttpServletRequestWrapper` 适配。

**示例**：Spring 的 `ContentCachingRequestWrapper` 缓存请求体，解决请求体只能读取一次的问题。

```java
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedBody;
    
    public CachedBodyHttpServletRequest(HttpServletRequest request) {
        super(request);
        // 缓存请求体
        this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
    }
    
    @Override
    public ServletInputStream getInputStream() {
        // 返回缓存的请求体（可多次读取）
        return new CachedBodyServletInputStream(this.cachedBody);
    }
}
```

---

## 五、使用建议

### 适用条件

✅ **适合使用适配器模式的场景**：
- 需要使用已有类，但其接口不符合需求
- 需要整合多个不相关的类到统一接口下
- 旧系统改造或第三方库集成
- 希望在运行时动态切换实现

### 注意事项

⚠️ **使用时的注意事项**：
1. **避免过度使用**：如果是全新设计，应优先考虑统一接口设计，而非事后适配
2. **适配器职责单一**：适配器只负责接口转换，不应包含业务逻辑
3. **性能开销**：适配器增加了调用层级，对性能敏感场景需权衡
4. **类适配器 vs 对象适配器**：
   - **类适配器**（通过多重继承）：Java 不支持多继承，不推荐
   - **对象适配器**（通过组合）：推荐方式，更灵活

### 与相关模式的区别

| 模式 | 目的 | 关键区别 |
|------|------|----------|
| **适配器模式** | 接口转换，让不兼容的接口协同工作 | 改变接口 |
| **装饰器模式** | 动态增加功能，不改变接口 | 增强接口 |
| **代理模式** | 控制对象访问，不改变接口 | 控制访问 |
| **外观模式** | 简化复杂子系统的接口 | 提供高层接口 |

---

## 六、参考实现

### 包结构

```
i2f.design.pattern.structural.adapter
├── player/                          # Target 角色
│   ├── MediaPlayer.java            # 目标接口
│   └── impl/
│       └── AudioPlayer.java        # 具体目标实现
├── adapter/                         # Adapter 角色
│   └── MediaAdapter.java           # 媒体适配器
├── advanced/                        # Adaptee 角色
│   ├── AdvancedMediaPlayer.java    # 适配者接口
│   └── impl/
│       ├── VlcPlayer.java          # VLC 播放器
│       └── Mp4Player.java          # MP4 播放器
├── Test.java                        # 演示入口
└── package-info.java               # 包说明
```

### 核心类链接

- [MediaPlayer（目标接口）](player/MediaPlayer.java)
- [MediaAdapter（适配器）](adapter/MediaAdapter.java)
- [AudioPlayer（具体目标）](player/impl/AudioPlayer.java)
- [AdvancedMediaPlayer（适配者接口）](advanced/AdvancedMediaPlayer.java)
- [VlcPlayer（VLC 播放器）](advanced/impl/VlcPlayer.java)
- [Mp4Player（MP4 播放器）](advanced/impl/Mp4Player.java)
- [Test（演示入口）](Test.java)

---

> **总结**：适配器模式是系统整合和接口兼容的利器，它通过"翻译"让不兼容的接口协同工作，遵循开闭原则和依赖倒置原则，是软件开发中最常用的结构型模式之一。
