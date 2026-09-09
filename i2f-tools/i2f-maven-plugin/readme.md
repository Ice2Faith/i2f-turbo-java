# i2f-maven-plugin

一个轻量级 Maven 插件集合，提供编译期自动化增强能力。当前已支持 SPI 服务描述文件自动生成，后续将持续扩展更多构建期功能。

## 功能概览

| Goal | 阶段 | 说明 |
|---|---|---|
| [`i2f:spi`](#goal-spi) | `process-classes` | 扫描 `@Spi` 注解，自动生成 `META-INF/services/` 文件 |

## 快速开始

### 1. 引入插件

在 `pom.xml` 的 `<build>` 中配置插件：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>i2f.turbo</groupId>
            <artifactId>i2f-maven-plugin</artifactId>
            <version>1.0</version>
            <executions>
                <execution>
                    <goals>
                        <!-- 按需启用所需的 goal -->
                        <goal>spi</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### 2. 构建

```bash
mvn compile
```

各 goal 默认绑定到合适的生命周期阶段，执行 `mvn compile` 即可自动触发。

---

## Goal: `spi`

在编译阶段自动扫描项目中的 `@Spi` 注解，生成 Java SPI 标准服务描述文件（`META-INF/services/`），告别手动维护。

### 特性

- **零配置** — 默认绑定 `process-classes` 阶段，编译后自动执行
- **注解驱动** — 使用 `@Spi` 注解声明 SPI 实现类，支持单接口和多接口
- **智能合并** — 自动合并已存在的手动创建的 services 文件，不会覆盖已有条目
- **字节码分析** — 基于 ASM 直接读取 `.class` 文件元数据，无需加载类到 JVM，速度快且无类加载依赖
- **去重处理** — 自动去除重复的实现类注册条目

### 添加注解依赖

在你的项目中引入 `@Spi` 注解：

```xml
<dependency>
    <groupId>i2f.turbo</groupId>
    <artifactId>i2f-spi-annotations</artifactId>
    <version>1.0</version>
</dependency>
```

> 如果你不想引入额外依赖，也可以自行定义注解，通过插件配置 `spiAnnotation` 参数指定你的注解全限定名（详见[配置参数](#配置参数)）。

### 使用注解

在实现类上添加 `@Spi` 注解，通过 `value` 指定该实现类所服务的 SPI 接口：

```java
// 单接口
@Spi(PaymentService.class)
public class AlipayServiceImpl implements PaymentService {
    // ...
}

// 多接口
@Spi({PaymentService.class, NotificationService.class})
public class WechatPayServiceImpl implements PaymentService, NotificationService {
    // ...
}
```

### 生成结果

以上述代码为例，插件会自动生成以下文件：

```
target/classes/
└── META-INF/
    └── services/
        ├── com.example.PaymentService
        └── com.example.NotificationService
```

**`com.example.PaymentService`** 文件内容：

```
com.example.AlipayServiceImpl
com.example.WechatPayServiceImpl
```

**`com.example.NotificationService`** 文件内容：

```
com.example.WechatPayServiceImpl
```

这些文件完全符合 [Java SPI 规范](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)，可直接通过 `ServiceLoader` 加载：

```java
ServiceLoader<PaymentService> loader = ServiceLoader.load(PaymentService.class);
for (PaymentService service : loader) {
    // 使用 service
}
```

### 配置参数

| 参数 | 类型 | 默认值 | 说明 |
|---|---|---|---|
| `spiAnnotation` | `String` | `i2f.spi.annotations.Spi` | 要扫描的 SPI 注解全限定名，可通过系统属性 `-Dspi.annotation=xxx` 覆盖 |
| `classesDirectory` | `File` | `${project.build.outputDirectory}` | 编译输出目录（只读，通常无需修改） |

#### 使用自定义注解

如果你不想依赖内置的 `@Spi` 注解，可以自定义一个结构相同的注解（必须包含 `Class<?>[] value()` 属性），然后在插件中指定：

```xml
<plugin>
    <groupId>i2f.turbo</groupId>
    <artifactId>i2f-maven-plugin</artifactId>
    <version>1.0</version>
    <configuration>
        <spiAnnotation>com.mycompany.annotations.MySpi</spiAnnotation>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>spi</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 智能合并机制

当 `META-INF/services/` 下已存在同名文件时（例如开发者手动创建的），插件不会直接覆盖，而是：

1. 读取已有文件中的所有条目（忽略空行和 `#` 开头的注释行）
2. 追加扫描发现的新实现类
3. 使用 `LinkedHashSet` 去重并保持顺序
4. 写回合并后的完整内容

这意味着你可以**混合使用**手动注册和注解自动注册，两者互不冲突。

### 工作原理

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────────────┐
│  mvn compile │ ──▶ │ process-classes   │ ──▶ │  i2f:spi goal       │
│  (编译源码)   │     │ (生命周期阶段)     │     │  (本插件执行)        │
└─────────────┘     └──────────────────┘     └─────────────────────┘
                                                      │
                                                      ▼
                                            ┌──────────────────┐
                                            │ 遍历 target/classes│
                                            │ 下所有 .class 文件  │
                                            └──────────────────┘
                                                      │
                                                      ▼
                                            ┌──────────────────┐
                                            │ ASM 字节码分析      │
                                            │ 读取 @Spi 注解信息  │
                                            └──────────────────┘
                                                      │
                                                      ▼
                                            ┌──────────────────┐
                                            │ 生成/合并           │
                                            │ META-INF/services/ │
                                            │ 服务描述文件         │
                                            └──────────────────┘
```

核心流程：

1. **文件遍历** — 递归扫描编译输出目录下所有 `.class` 文件
2. **字节码解析** — 使用 ASM 库读取每个 class 文件的注解元数据（跳过方法体、调试信息和栈帧，仅解析注解）
3. **接口提取** — 从 `@Spi` 注解的 `value` 属性中提取 SPI 接口名，支持单值和数组两种形式
4. **映射构建** — 建立 `SPI 接口 → [实现类列表]` 的映射关系
5. **文件生成** — 为每个 SPI 接口生成 `META-INF/services/{接口全限定名}` 文件，并与已有内容合并

---

## 本地构建

```bash
git clone <repository-url>
cd i2f-maven-plugin
mvn clean install
```

### 环境要求

- **JDK**: 8+
- **Maven**: 3.6+

### 技术栈

| 组件 | 版本 | 说明 |
|---|---|---|
| Maven Plugin API | 3.6.3 | Maven 插件开发基础 API |
| Maven Plugin Annotations | 3.6.0 | 插件注解支持（`@Mojo`, `@Parameter` 等） |
| ASM | 9.6 | 字节码分析与注解读取 |

## 项目结构

```
i2f-maven-plugin/
├── pom.xml
└── src/main/java/i2f/
    ├── maven/plugin/spi/
    │   └── SpiComponentScanMojo.java    # spi goal 实现
    └── spi/annotations/
        └── Spi.java                      # @Spi 注解定义
```