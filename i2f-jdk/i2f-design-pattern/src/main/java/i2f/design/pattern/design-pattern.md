# 设计模式（Design Patterns）

> 设计模式是软件开发过程中针对常见问题总结出来的、可复用的解决方案。GoF（Gang of Four）在《Design Patterns: Elements of Reusable Object-Oriented Software》一书中将 23 种经典设计模式分为三大类：**创建型（Creational）**、**结构型（Structural）** 和 **行为型（Behavioral）**。后续社区还补充了 **并发型（Concurrency）** 和 **架构型（Architectural）** 等扩展模式。

## 目录

- [一、创建型模式（Creational Patterns）](#一创建型模式creational-patterns)
  - [1.1 单例模式（Singleton）](#11-单例模式singleton)
  - [1.2 工厂方法模式（Factory Method）](#12-工厂方法模式factory-method)
  - [1.3 抽象工厂模式（Abstract Factory）](#13-抽象工厂模式abstract-factory)
  - [1.4 建造者模式（Builder）](#14-建造者模式builder)
  - [1.5 原型模式（Prototype）](#15-原型模式prototype)
- [二、结构型模式（Structural Patterns）](#二结构型模式structural-patterns)
  - [2.1 适配器模式（Adapter）](#21-适配器模式adapter)
  - [2.2 桥接模式（Bridge）](#22-桥接模式bridge)
  - [2.3 组合模式（Composite）](#23-组合模式composite)
  - [2.4 装饰器模式（Decorator）](#24-装饰器模式decorator)
  - [2.5 外观模式（Facade）](#25-外观模式facade)
  - [2.6 享元模式（Flyweight）](#26-享元模式flyweight)
  - [2.7 代理模式（Proxy）](#27-代理模式proxy)
- [三、行为型模式（Behavioral Patterns）](#三行为型模式behavioral-patterns)
  - [3.1 责任链模式（Chain of Responsibility）](#31-责任链模式chain-of-responsibility)
  - [3.2 命令模式（Command）](#32-命令模式command)
  - [3.3 解释器模式（Interpreter）](#33-解释器模式interpreter)
  - [3.4 迭代器模式（Iterator）](#34-迭代器模式iterator)
  - [3.5 中介者模式（Mediator）](#35-中介者模式mediator)
  - [3.6 备忘录模式（Memento）](#36-备忘录模式memento)
  - [3.7 观察者模式（Observer）](#37-观察者模式observer)
  - [3.8 状态模式（State）](#38-状态模式state)
  - [3.9 策略模式（Strategy）](#39-策略模式strategy)
  - [3.10 模板方法模式（Template Method）](#310-模板方法模式template-method)
  - [3.11 访问者模式（Visitor）](#311-访问者模式visitor)
- [四、并发型模式（Concurrency Patterns）](#四并发型模式concurrency-patterns)
  - [4.1 生产者-消费者模式（Producer-Consumer）](#41-生产者-消费者模式producer-consumer)
  - [4.2 读写锁模式（Read-Write Lock）](#42-读写锁模式read-write-lock)
  - [4.3 Future / Promise 模式](#43-future--promise-模式)
  - [4.4 线程池模式（Thread Pool）](#44-线程池模式thread-pool)
- [五、架构型模式（Architectural Patterns）](#五架构型模式architectural-patterns)
  - [5.1 MVC 模式](#51-mvc-模式)
  - [5.2 MVVM 模式](#52-mvvm-模式)
  - [5.3 DAO 模式](#53-dao-模式)
  - [5.4 IoC / DI 模式](#54-ioc--di-模式)

---

## 一、创建型模式（Creational Patterns）

> 创建型模式关注对象的**创建过程**，将对象的创建与使用解耦，让系统在不指定具体类的情况下创建对象。

### 1.1 单例模式（Singleton）

- **定义**：保证一个类仅有一个实例，并提供一个访问它的全局访问点。
- **分类**：创建型
- **适用场景**：
  - 需要全局唯一的资源管理对象（如配置、日志、连接池）。
  - 创建对象耗费资源较多，希望复用同一实例。
  - 需要全局唯一访问入口。
- **实现案例**：
  - [readme.md](./creational/singleton/readme.md)
- **典型案例**：
  - JDK：`java.lang.Runtime#getRuntime()`、`java.awt.Desktop#getDesktop()`、`java.util.logging.LogManager#getLogManager()`
  - Spring：`BeanFactory` 默认 scope=singleton 的 Bean；`DefaultSingletonBeanRegistry#singletonObjects` 维护单例缓存
  - Spring Boot：`@Configuration` + `@Bean` 默认注册为单例；`@ConditionalOnMissingBean` 防止重复实例化
  - Hibernate：`SessionFactory` 单例

### 1.2 工厂方法模式（Factory Method）

- **定义**：定义一个用于创建对象的接口，让子类决定实例化哪一个类，使一个类的实例化延迟到其子类。
- **分类**：创建型
- **适用场景**：
  - 创建对象时不希望暴露具体实现类。
  - 一个类希望由其子类来决定创建哪种对象。
  - 系统需要根据不同条件创建不同对象。
- **实现案例**：
  - [readme.md](./creational/factoryMethod/readme.md)
- **典型案例**：
  - JDK：`java.util.Calendar#getInstance()`、`java.text.NumberFormat#getInstance()`、`java.util.concurrent.Executors`（`newFixedThreadPool`/`newCachedThreadPool` 等工厂方法）
  - Spring：`BeanFactory#getBean()`、`FactoryBean` 接口（如 `SqlSessionFactoryBean`）、`ProxyFactory#getProxy()`
  - Spring Boot：`@Bean` 注解方法即工厂方法；`SpringApplication#run()` 工厂式创建 `ApplicationContext`
  - Spring Data：`RepositoryFactorySupport` 创建不同数据源的 Repository 实例
  - MyBatis：`SqlSessionFactory#openSession()`

### 1.3 抽象工厂模式（Abstract Factory）

- **定义**：提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。
- **分类**：创建型
- **适用场景**：
  - 系统需要独立于产品的创建、组合和表示。
  - 系统需要一组相关产品对象进行组合使用。
  - 强调一系列相关产品对象的设计以便联合使用。
- **实现案例**：
  - [readme.md](./creational/abstractFactory/readme.md)
- **典型案例**：
  - JDK：`javax.xml.parsers.DocumentBuilderFactory`、`javax.xml.transform.TransformerFactory`、`javax.xml.xpath.XPathFactory`
  - Spring：`ApplicationContext` 体系（`ClassPathXmlApplicationContext`、`AnnotationConfigApplicationContext`）；`AbstractBeanFactory` 统一管理对象族创建
  - Spring Data：`JpaRepositoryFactory`、`MongoRepositoryFactory`（针对不同数据源的仓储工厂族，统一接口各自实现）
  - MyBatis：`Configuration` 中针对不同数据库厂商的多种工厂对象

### 1.4 建造者模式（Builder）

- **定义**：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
- **分类**：创建型
- **适用场景**：
  - 创建对象需要很多步骤，且步骤顺序固定但参数可变。
  - 构造函数参数过多，希望通过链式调用提升可读性。
  - 同一构建过程可生成不同的产品表示。
- **实现案例**：
  - [readme.md](./creational/builder/readme.md)
- **典型案例**：
  - JDK：`java.lang.StringBuilder`、`java.nio.ByteBuffer`、`Stream.Builder`、`ProcessBuilder`
  - Spring：`UriComponentsBuilder`（链式构造 URL）、`BeanDefinitionBuilder`（链式构造 BeanDefinition）、`MockMvcRequestBuilders`（测试请求构造）
  - Spring Boot：`SpringApplicationBuilder`（链式配置启动应用、设置 parent context）
  - Spring Security：`HttpSecurity` 链式 DSL（`http.authorizeRequests().and().formLogin()...`）
  - Spring WebFlux：`WebClient.Builder` 响应式 HTTP 客户端链式构造
  - Guava：`ImmutableList.Builder`、`CacheBuilder`
  - Lombok：`@Builder` 注解
  - OkHttp：`Request.Builder`、`OkHttpClient.Builder`

### 1.5 原型模式（Prototype）

- **定义**：用原型实例指定创建对象的种类，并通过拷贝这些原型创建新的对象。
- **分类**：创建型
- **适用场景**：
  - 创建对象成本较高（如初始化耗时长、依赖资源多），希望通过拷贝快速创建。
  - 需要避免使用复杂的构造函数体系。
  - 需要保留对象在某一时刻的状态。
- **实现案例**：
  - [readme.md](./creational/prototype/readme.md)
- **典型案例**：
  - JDK：`java.lang.Object#clone()`、`Cloneable` 接口；`ArrayList#clone()`、`HashMap#clone()`、`Calendar#clone()`
  - Spring：`scope=prototype` 的 Bean（每次 `getBean` 均返回全新实例）；`BeanUtils.copyProperties()` 浅拷贝属性
  - Spring Data：`AbstractEntityInformation` 对实体元信息的复制
  - 序列化机制（基于 `Serializable` 实现深拷贝）

---

## 二、结构型模式（Structural Patterns）

> 结构型模式关注**类与对象的组合**，描述如何将类或对象结合在一起形成更大的结构。

### 2.1 适配器模式（Adapter）

- **定义**：将一个类的接口转换成客户希望的另一个接口，使得原本由于接口不兼容而不能一起工作的类可以协同工作。
- **分类**：结构型
- **适用场景**：
  - 想使用一个已经存在的类，但其接口不符合需求。
  - 需要将多个不相关的类整合到统一的接口下。
  - 旧系统改造或第三方库的兼容。
- **实现案例**：
  - [readme.md](./structural/adapter/readme.md)
- **典型案例**：
  - JDK：`java.util.Arrays#asList()`、`java.io.InputStreamReader`、`java.io.OutputStreamWriter`；`java.awt.event.WindowAdapter`（适配器抽象类，只实现需要的回调方法）
  - Spring MVC：`HandlerAdapter` 接口（`RequestMappingHandlerAdapter`、`SimpleControllerHandlerAdapter` 将不同类型 Handler 适配为统一调用）
  - Spring：`WebMvcConfigurer` 将 MVC 配置接口适配为可选覆盖；`AdvisorAdapter` 将 Advisor 适配为 `MethodInterceptor`
  - Spring Data：`QuerydslPredicateArgumentResolver` 将请求参数适配为 Querydsl Predicate
  - Slf4j：各种日志实现的桥接器（slf4j-log4j12、slf4j-jdk14）

### 2.2 桥接模式（Bridge）

- **定义**：将抽象部分与它的实现部分分离，使它们可以独立地变化。
- **分类**：结构型
- **适用场景**：
  - 一个类存在两个或多个独立变化的维度，且这两个维度都需要扩展。
  - 不希望使用继承导致类爆炸。
  - 抽象与实现需要在运行时动态切换。
- **实现案例**：
  - [readme.md](./structural/bridge/readme.md)
- **典型案例**：
  - JDK/JDBC：`DriverManager`（抽象）与各数据库 `Driver`（实现）之间的解耦
  - Spring：`PlatformTransactionManager`（抽象）与 `DataSourceTransactionManager`、`JpaTransactionManager`、`JtaTransactionManager`（实现）相互独立扩展
  - Spring Data：`Repository`（抽象接口）与 `JpaRepository`、`MongoRepository`、`RedisRepository`（具体实现）独立演化
  - Slf4j：日志门面（抽象）与具体日志实现（log4j、logback）的分离
  - AWT 中的 `Component`（抽象）与 `Peer`（平台实现）

### 2.3 组合模式（Composite）

- **定义**：将对象组合成树形结构以表示"部分-整体"的层次结构，使用户对单个对象和组合对象的使用具有一致性。
- **分类**：结构型
- **适用场景**：
  - 想表示对象的部分-整体层次结构（树形结构）。
  - 希望客户端忽略组合对象与单个对象的差异。
  - 文件系统、菜单、组织架构等树形结构。
- **实现案例**：
  - [readme.md](./structural/composite/readme.md)
- **典型案例**：
  - JDK：`java.awt.Container#add(Component)`、Swing 中的 `JComponent`
  - Spring：`CompositeCacheManager`（组合多个 `CacheManager`）、`CompositePropertySource`（组合多个属性源）
  - Spring Security：`ProviderManager` 组合多个 `AuthenticationProvider`；`CompositeFilter` 组合多个过滤器
  - Spring MVC：`CompositeHandlerExceptionResolver` 组合多个异常处理器
  - DOM 树：`org.w3c.dom.Node`
  - MyBatis：`SqlNode`（IfSqlNode、TrimSqlNode 等）

### 2.4 装饰器模式（Decorator）

- **定义**：动态地给一个对象添加一些额外的职责，就增加功能来说，装饰器模式比生成子类更为灵活。
- **分类**：结构型
- **适用场景**：
  - 需要在不影响其他对象的情况下，以动态、透明的方式给单个对象添加职责。
  - 不能采用继承的方式扩展功能（如 final 类，或继承会导致类爆炸）。
  - 希望功能可叠加组合。
- **实现案例**：
  - [readme.md](./structural/decorator/readme.md)
- **典型案例**：
  - JDK：`java.io.BufferedInputStream`、`java.io.DataInputStream` 等 IO 流体系；`Collections.synchronizedList()`、`Collections.unmodifiableList()` 装饰 List
  - Spring：`TransactionAwareCacheDecorator`；`ContentCachingRequestWrapper`（缓存请求体便于重复读取）；`BeanDefinitionDecorator` 装饰 BeanDefinition
  - Spring Cloud：`RetryableFeignBlockingLoadBalancerClient` 装饰 Feign 客户端增加重试能力
  - Servlet：`HttpServletRequestWrapper`、`HttpServletResponseWrapper`

### 2.5 外观模式（Facade）

- **定义**：为子系统中的一组接口提供一个一致的高层接口，使得子系统更加容易使用。
- **分类**：结构型
- **适用场景**：
  - 为一个复杂子系统提供一个简单入口。
  - 客户端与多个子系统之间存在较大依赖，希望解耦。
  - 分层设计中，作为每一层的入口。
- **实现案例**：
  - [readme.md](./structural/facade/readme.md)
- **典型案例**：
  - JDK：`javax.faces.context.FacesContext`（JSF 统一门面入口）
  - Spring：`JdbcTemplate`、`RedisTemplate`、`RestTemplate`；`ApplicationContext`（整个 IoC 容器的统一入口门面）；`TransactionTemplate` 封装事务操作
  - Spring Boot：`SpringApplication` 封装启动流程；各 `AutoConfiguration` 封装复杂依赖配置细节
  - Spring Data：`CrudRepository`、`JpaRepository` 封装底层 ORM/数据库操作
  - Slf4j：`LoggerFactory` 作为日志门面
  - Tomcat：`Request`、`Response` 对底层 Connector 的封装

### 2.6 享元模式（Flyweight）

- **定义**：运用共享技术有效地支持大量细粒度的对象，通过共享内部状态减少内存占用。
- **分类**：结构型
- **适用场景**：
  - 系统中存在大量相似对象。
  - 对象的大部分状态可以外部化（即可以共享）。
  - 需要缓冲池场景。
- **实现案例**：
  - [readme.md](./structural/flyweight/readme.md)
- **典型案例**：
  - JDK：`Integer.valueOf()`（-128~127）、`Long.valueOf()`、`Short.valueOf()`、`Byte.valueOf()`、`Character.valueOf()`（0~127）、`Boolean.valueOf()`；`String` 常量池（`intern()`）
  - Spring：`DefaultSingletonBeanRegistry#singletonObjects`（Bean 单例缓存，共享同一实例）；`SimpleTypeConverter` 缓存类型转换器
  - Spring MVC：`DispatcherServlet` 对 `HandlerMapping`、`HandlerAdapter`、`ViewResolver` 的缓存复用
  - 数据库连接池（Druid、HikariCP）
  - 线程池：`ThreadPoolExecutor`

### 2.7 代理模式（Proxy）

- **定义**：为其他对象提供一种代理以控制对这个对象的访问。
- **分类**：结构型
- **适用场景**：
  - 需要对目标对象进行访问控制（远程代理、保护代理）。
  - 需要在访问前后增加额外操作（日志、缓存、事务）。
  - 需要延迟加载（虚代理）。
- **实现案例**：
  - [readme.md](./structural/proxy/readme.md)
- **典型案例**：
  - JDK：`java.lang.reflect.Proxy`（JDK 动态代理）；`Collections#synchronizedList()`（保护代理）；`Collections#unmodifiableList()`（只读代理）
  - Spring AOP：`@Transactional`（事务代理）、`@Cacheable`（缓存代理）、`@Async`（异步代理）均基于 JDK 动态代理或 CGLIB 实现
  - Spring Data：`Repository` 接口由 `JdkDynamicAopProxy` 或 `SimpleJpaRepository` 动态代理生成
  - Spring Cloud OpenFeign：`@FeignClient` 接口通过 `FeignClientFactoryBean` 动态代理生成远程调用实现
  - MyBatis：`MapperProxy` 代理 Mapper 接口；`CglibProxyFactory` 实现延迟加载代理
  - Dubbo：`StubProxyFactoryWrapper` 远程服务代理

---

## 三、行为型模式（Behavioral Patterns）

> 行为型模式关注**对象之间的通信和职责分配**，描述类或对象怎样交互和怎样分配职责。

### 3.1 责任链模式（Chain of Responsibility）

- **定义**：使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系，将这些对象连成一条链，并沿着这条链传递请求，直到有一个对象处理它为止。
- **分类**：行为型
- **适用场景**：
  - 多个对象可以处理同一请求，但具体由哪个对象处理在运行时决定。
  - 希望在不明确指定接收者的情况下，向多个对象提交请求。
  - 处理流程可动态组合。
- **实现案例**：
  - [readme.md](./behavioral/chainOfResponsibility/readme.md)
- **典型案例**：
  - JDK：`java.util.logging.Logger`（日志按层级向父 Handler 传递）
  - Servlet：`Filter` 与 `FilterChain`
  - Spring：`HandlerInterceptor` 拦截器链；`BeanPostProcessor` 链（Bean 初始化前后依次处理）；`BeanFactoryPostProcessor` 链；Spring Security `SecurityFilterChain` 过滤器链
  - Spring Cloud Gateway：`GlobalFilter` 与 `GatewayFilter` 链（网关请求依次经过各过滤器）
  - Netty：`ChannelPipeline` 与 `ChannelHandler`
  - MyBatis：`Interceptor` 插件链

### 3.2 命令模式（Command）

- **定义**：将一个请求封装为一个对象，从而使你可用不同的请求对客户进行参数化，对请求排队或记录请求日志，以及支持可撤销的操作。
- **分类**：行为型
- **适用场景**：
  - 需要将请求发送者和接收者解耦。
  - 需要支持撤销/重做、事务、日志、队列。
  - 需要将操作组合成宏命令。
- **实现案例**：
  - [readme.md](./behavioral/command/readme.md)
- **典型案例**：
  - JDK：`java.lang.Runnable`、`java.util.concurrent.Callable`、`javax.swing.Action`（封装 UI 操作命令）
  - Spring：`JdbcTemplate` 中的 `StatementCallback`；`TaskExecutor#execute(Runnable)` 将任务封装为命令提交
  - Spring Batch：`Tasklet` 将批处理步骤封装为可执行命令；`Step` 作为可排队执行的命令单元
  - JUnit：`Test` 接口

### 3.3 解释器模式（Interpreter）

- **定义**：给定一个语言，定义它的文法的一种表示，并定义一个解释器，这个解释器使用该表示来解释语言中的句子。
- **分类**：行为型
- **适用场景**：
  - 有一个简单的语言需要解释执行。
  - 文法相对简单且文法变化不频繁。
  - 表达式求值、规则引擎、SQL 解析等。
- **实现案例**：
  - [readme.md](./behavioral/interpreter/readme.md)
- **典型案例**：
  - JDK：`java.util.regex.Pattern`（正则表达式解释执行）、`java.text.MessageFormat`、`java.text.SimpleDateFormat`（日期格式表达式解释）
  - Spring：SpEL（`ExpressionParser` 解释 `#{...}` 表达式）；`@Value("${...}")` 属性占位符解析；`Spring Security` 的 `MethodSecurityExpressionHandler` 解释权限表达式
  - Spring Data：`@Query` 注解中 JPQL/HQL/SpEL 表达式解析执行
  - Drools 规则引擎、MVEL 表达式

### 3.4 迭代器模式（Iterator）

- **定义**：提供一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部表示。
- **分类**：行为型
- **适用场景**：
  - 需要访问聚合对象内容而不暴露内部表示。
  - 需要为聚合对象提供多种遍历方式。
  - 提供统一的遍历接口。
- **实现案例**：
  - [readme.md](./behavioral/iterator/readme.md)
- **典型案例**：
  - JDK：`java.util.Iterator`、`java.util.Enumeration`、`Iterable` 接口；`java.util.Scanner`（按行/按 token 迭代输入流）；`java.nio.file.DirectoryStream`（迭代目录条目）
  - Spring Data：`Page<T>` 与 `Slice<T>` 分页迭代；`ScrollPosition` 游标式翻页（Spring Data 3.x）
  - JDBC：`ResultSet`
  - MyBatis：`Cursor` 游标查询（流式迭代大数据集）

### 3.5 中介者模式（Mediator）

- **定义**：用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而使其耦合松散。
- **分类**：行为型
- **适用场景**：
  - 一组对象以复杂的方式进行通信，导致依赖关系混乱。
  - 一个对象引用很多其他对象并直接通信，难以复用。
  - GUI 中多个组件相互协作。
- **实现案例**：
  - [readme.md](./behavioral/mediator/readme.md)
- **典型案例**：
  - JDK：`java.util.Timer`、`java.util.concurrent.ExecutorService`（作为任务提交与执行的中介）
  - Spring：`ApplicationContext` 作为 Bean 之间的中介；`ApplicationEventPublisher` 解耦事件发布者与订阅者
  - Spring Integration：`MessageChannel`（生产者与消费者通过 Channel 通信，无需直接引用对方）
  - Spring Cloud：`LoadBalancer` 作为调用方与服务实例之间的中介
  - JMS / 消息中间件：Broker 作为生产者与消费者的中介

### 3.6 备忘录模式（Memento）

- **定义**：在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，以便以后将该对象恢复到原先保存的状态。
- **分类**：行为型
- **适用场景**：
  - 需要保存对象的历史状态，支持撤销操作。
  - 需要事务回滚。
  - 直接暴露内部状态会破坏封装。
- **实现案例**：
  - [readme.md](./behavioral/memento/readme.md)
- **典型案例**：
  - JDK：`java.io.Serializable` 序列化保存对象状态；`java.util.Date#clone()`、`java.util.Calendar#clone()` 快照当前时间状态
  - Spring：`BindingResult` 保存数据绑定前后的状态快照；`SavedRequest`（Spring Security）保存请求状态待认证后恢复
  - Spring Webflow：状态保存与恢复
  - 数据库事务回滚机制（undo log 思想类似）

### 3.7 观察者模式（Observer）

- **定义**：定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。
- **分类**：行为型
- **适用场景**：
  - 一个对象的改变需要同时改变其它对象，但不知道具体多少对象有待改变。
  - 事件驱动的系统、消息发布订阅。
  - 解耦发布者和订阅者。
- **实现案例**：
  - [readme.md](./behavioral/observer/readme.md)
- **典型案例**：
  - JDK：`java.util.Observer`/`Observable`（已废弃）、`java.beans.PropertyChangeListener`、`javax.servlet.ServletContextListener`
  - Spring：`ApplicationListener` 与 `ApplicationEvent`；`@EventListener` 注解（方法级订阅）；`SmartApplicationListener`（有序监听）；`TransactionSynchronization`（事务提交/回滚回调）
  - Spring Boot：生命周期事件（`ApplicationStartingEvent`、`ApplicationReadyEvent`、`ContextRefreshedEvent`）
  - Spring Cloud：`HeartbeatEvent`（注册中心服务实例变化通知）
  - Guava：`EventBus`
  - Reactor / RxJava：响应式编程

### 3.8 状态模式（State）

- **定义**：允许一个对象在其内部状态改变时改变它的行为，对象看起来似乎修改了它的类。
- **分类**：行为型
- **适用场景**：
  - 一个对象的行为取决于它的状态，并且必须在运行时根据状态改变它的行为。
  - 代码中包含大量与对象状态有关的条件语句（if/switch）。
  - 工作流引擎、订单状态流转。
- **实现案例**：
  - [readme.md](./behavioral/state/readme.md)
- **典型案例**：
  - JDK：`java.util.Iterator`（hasNext 与 next 状态切换）；`java.net.Socket`（CLOSED、BOUND、CONNECTED 状态）
  - Spring：`AbstractApplicationContext` 生命周期状态（active/closed）；`DefaultLifecycleProcessor` 管理 Bean 的 start/stop 状态
  - Spring Batch：`JobExecution` 状态流转（STARTING → STARTED → COMPLETED / FAILED）；`StepExecution` 状态机
  - Spring StateMachine：通用状态机框架
  - 线程的 `Thread.State`（NEW、RUNNABLE、BLOCKED、WAITING、TIMED_WAITING、TERMINATED）

### 3.9 策略模式（Strategy）

- **定义**：定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换，本模式使得算法可独立于使用它的客户而变化。
- **分类**：行为型
- **适用场景**：
  - 一个系统有许多类，它们之间的区别仅在于行为。
  - 需要在运行时动态选择算法。
  - 避免多重 if/else 条件判断。
- **实现案例**：
  - [readme.md](./behavioral/strategy/readme.md)
- **典型案例**：
  - JDK：`java.util.Comparator`（可替换的排序策略）、`ThreadPoolExecutor` 的拒绝策略（`AbortPolicy`/`CallerRunsPolicy` 等 `RejectedExecutionHandler`）
  - Spring：`Resource` 接口不同实现（`ClassPathResource`、`FileSystemResource`）；`TaskScheduler` 不同调度策略；`CacheManager` 不同缓存策略
  - Spring MVC：`HandlerMapping`（`RequestMappingHandlerMapping`/`BeanNameUrlHandlerMapping` 可替换）；`ViewResolver` 不同视图解析策略
  - Spring Security：`PasswordEncoder`（`BCryptPasswordEncoder`/`Argon2PasswordEncoder` 可替换）；`AccessDecisionManager` 访问决策策略
  - Spring AOP：`AopProxy` 的 JDK 动态代理与 CGLIB 两种策略

### 3.10 模板方法模式（Template Method）

- **定义**：定义一个操作中的算法的骨架，而将一些步骤延迟到子类中，使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤。
- **分类**：行为型
- **适用场景**：
  - 多个类有相似算法但具体步骤实现不同。
  - 控制子类扩展点（钩子方法）。
  - 复用公共代码、消除重复。
- **实现案例**：
  - [readme.md](./behavioral/templateMethod/readme.md)
- **典型案例**：
  - JDK：`java.io.InputStream#read(byte[])`（定义骨架，子类实现单字节 `read()`）；`java.util.AbstractList`、`java.util.AbstractMap`、`java.util.AbstractQueue`
  - Spring：`JdbcTemplate`、`RestTemplate`、`AbstractApplicationContext#refresh()`（固定刷新骨架，各步骤留给子类扩展）；`AbstractController` 封装 HTTP 处理骨架
  - Spring Boot：`SpringBootServletInitializer#configure()` 定义 WAR 部署骨架
  - Spring Batch：`AbstractStep`（固定 execute 骨架）；`AbstractItemCountingItemStreamItemReader`（固定读取骨架）
  - Spring Data：`SimpleJpaRepository` 封装 JPA 操作模板（CRUD 骨架实现）
  - Servlet：`HttpServlet#service()` 调度到 `doGet`/`doPost`

### 3.11 访问者模式（Visitor）

- **定义**：表示一个作用于某对象结构中的各元素的操作，它使你可以在不改变各元素的类的前提下定义作用于这些元素的新操作。
- **分类**：行为型
- **适用场景**：
  - 对象结构中包含多种类型的对象，希望对这些对象施加一些不同的、不相关的操作。
  - 对象结构很少改变，但常常需要增加新的操作。
  - 编译器 AST 处理、语法分析。
- **实现案例**：
  - [readme.md](./behavioral/visitor/readme.md)
- **典型案例**：
  - JDK：`java.nio.file.FileVisitor`（`SimpleFileVisitor` 遍历文件树）、`java.lang.model.element.ElementVisitor`（注解处理器 APT）
  - Spring：`BeanDefinitionVisitor`（访问并替换 BeanDefinition 中的占位符字符串）；`ReflectionUtils` 通过回调访问类的方法/字段
  - ASM 字节码框架：`ClassVisitor`、`MethodVisitor`、`FieldVisitor`
  - 编译器：JavaC AST 访问者

---

## 四、并发型模式（Concurrency Patterns）

> 并发型模式关注**多线程编程中的同步、协作与资源共享**问题，是 GoF 之外常见的扩展模式。

### 4.1 生产者-消费者模式（Producer-Consumer）

- **定义**：通过一个共享缓冲区解耦生产者与消费者，生产者向缓冲区放入数据，消费者从缓冲区取出数据。
- **分类**：并发型
- **适用场景**：
  - 生产与消费速率不一致，需要削峰填谷。
  - 异步处理、解耦上下游。
  - 消息队列、任务队列。
- **实现案例**：
  - [package-info.java](./concurrency/producerConsumer/package-info.java)
- **典型案例**：
  - JDK：`BlockingQueue`（`ArrayBlockingQueue`、`LinkedBlockingQueue`、`DelayQueue`、`SynchronousQueue`）
  - Spring：`ApplicationEventMulticaster`（支持异步模式，生产者发布事件，消费者异步处理）
  - Spring Integration：`QueueChannel`（生产者-消费者消息通道）
  - Disruptor：高性能无锁环形队列
  - Kafka、RabbitMQ 消息中间件

### 4.2 读写锁模式（Read-Write Lock）

- **定义**：对共享资源的访问进行读写分离，允许多个读操作并发执行，但写操作必须独占。
- **分类**：并发型
- **适用场景**：
  - 读多写少的共享资源访问。
  - 缓存系统、配置中心。
- **典型案例**：
  - JDK：`java.util.concurrent.locks.ReentrantReadWriteLock`、`StampedLock`；`CopyOnWriteArrayList`/`CopyOnWriteArraySet`（写时复制，读无锁）
  - Spring：`DefaultSingletonBeanRegistry` 中使用 `synchronized` + `ConcurrentHashMap` 组合的读写分离控制
  - Caffeine、Guava Cache 内部读写控制

### 4.3 Future / Promise 模式

- **定义**：用一个占位对象代表异步计算的结果，调用方可以在未来的某个时间点获取该结果。
- **分类**：并发型
- **适用场景**：
  - 需要异步执行任务并在未来获取结果。
  - 需要将多个异步操作组合编排。
- **实现案例**：
  - [readme.md](./concurrency/futurePromise/readme.md)
- **典型案例**：
  - JDK：`java.util.concurrent.Future`、`FutureTask`、`ScheduledFuture`；`CompletableFuture`（支持链式编排、`thenApply`/`thenCompose`）
  - Spring：`@Async` 方法返回 `CompletableFuture`；`AsyncResult` 包装异步结果
  - Spring WebFlux：`Mono<T>`（单值异步）、`Flux<T>`（多值异步流）—— 响应式 Future
  - Netty：`ChannelFuture`、`Promise`
  - Guava：`ListenableFuture`

### 4.4 线程池模式（Thread Pool）

- **定义**：预先创建一组线程并复用，避免频繁创建销毁线程带来的开销。
- **分类**：并发型
- **适用场景**：
  - 高并发场景下需要大量短任务执行。
  - 控制并发线程数量，保护系统资源。
- **典型案例**：
  - JDK：`ThreadPoolExecutor`、`ForkJoinPool`、`ScheduledThreadPoolExecutor`；`Executors` 工厂方法创建各类线程池
  - Spring：`ThreadPoolTaskExecutor`（Spring 封装的线程池，支持 `@Async`）；`ThreadPoolTaskScheduler`（定时任务线程池）
  - Spring Boot：`TaskExecutionAutoConfiguration`（自动配置应用线程池）；`TaskSchedulingAutoConfiguration`（自动配置调度线程池）
  - Tomcat：`Executor` 工作线程池
  - Dubbo：业务线程池（`fixed`/`cached`/`limited` 多种策略）

---

## 五、架构型模式（Architectural Patterns）

> 架构型模式关注**整体软件结构**的组织方式，是更高粒度的设计指导。

### 5.1 MVC 模式

- **定义**：将应用程序划分为 Model（模型）、View（视图）、Controller（控制器）三层，实现关注点分离。
- **分类**：架构型
- **适用场景**：
  - Web 应用、桌面 GUI 程序。
  - 需要在 UI、业务逻辑与数据之间解耦。
- **典型案例**：
  - Spring MVC：`DispatcherServlet`（前端控制器）统一接收请求，委托 `HandlerMapping`（M）/`ViewResolver`（V）/`HandlerAdapter`（C）三层协作
  - Spring Boot：`WebMvcAutoConfiguration` 自动装配 Spring MVC 三层组件
  - Struts2：`ActionServlet` + `Action` + JSP
  - Swing / JavaFX 中的视图与模型分离

### 5.2 MVVM 模式

- **定义**：在 MVC 基础上引入 ViewModel，通过数据双向绑定将 View 与 Model 解耦。
- **分类**：架构型
- **适用场景**：
  - 前端富客户端应用、桌面客户端。
  - 需要数据双向绑定、UI 自动响应数据变化。
- **典型案例**：
  - JavaFX 中的属性绑定（Property Binding）
  - 前端 Vue、Angular（虽非 Java，但概念一致）

### 5.3 DAO 模式

- **定义**：将数据访问逻辑封装到数据访问对象（Data Access Object）中，与业务逻辑解耦。
- **分类**：架构型
- **适用场景**：
  - 屏蔽底层数据库实现差异。
  - 系统需要支持多数据源切换。
- **典型案例**：
  - Spring：`JdbcDaoSupport`（提供 `JdbcTemplate` 注入的 DAO 基类）
  - Spring Data：`CrudRepository`、`JpaRepository`、`MongoRepository`、`RedisRepository`（统一屏蔽底层存储差异）
  - MyBatis：Mapper 接口（`@Mapper`）
  - Hibernate：`HibernateDaoSupport` DAO 模板基类

### 5.4 IoC / DI 模式

- **定义**：控制反转（Inversion of Control）将对象依赖关系的创建与管理交给容器，依赖注入（Dependency Injection）是其主要实现方式。
- **分类**：架构型
- **适用场景**：
  - 大型应用中希望解耦对象之间的依赖关系。
  - 提升可测试性和可维护性。
- **典型案例**：
  - Spring：`@Autowired`（字段/构造器/Setter 注入）、`@Resource`、`@Value`；`ApplicationContext` 作为 IoC 容器
  - Spring Boot：`@SpringBootApplication` = `@EnableAutoConfiguration` + `@ComponentScan`，自动发现并注入 Bean
  - Spring Cloud：`@EnableDiscoveryClient`（服务发现注入）；`@LoadBalanced` 修饰 `RestTemplate` 自动注入负载均衡能力
  - Google Guice：`@Inject` 注解
  - Java EE CDI（Contexts and Dependency Injection）

---

## 附：设计模式六大原则（SOLID + 1）

| 原则 | 英文 | 核心思想 |
| --- | --- | --- |
| 单一职责原则 | Single Responsibility Principle (SRP) | 一个类只承担一种职责 |
| 开闭原则 | Open-Closed Principle (OCP) | 对扩展开放，对修改关闭 |
| 里氏替换原则 | Liskov Substitution Principle (LSP) | 子类必须能替换父类 |
| 接口隔离原则 | Interface Segregation Principle (ISP) | 使用多个专门接口优于一个总接口 |
| 依赖倒置原则 | Dependency Inversion Principle (DIP) | 高层模块不依赖低层模块，二者依赖抽象 |
| 迪米特法则 | Law of Demeter (LoD) | 一个对象应当对其他对象有最少了解 |

> 设计模式是工具而非银弹，使用时应结合实际业务场景，避免过度设计（Over-engineering）。
