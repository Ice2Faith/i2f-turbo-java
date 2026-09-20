# i2f-turbo-java

> 面向 JDK / Spring / SpringBoot / SpringCloud 的 Java 基础能力增强与扩展库，提供反射、JDBC、类型系统、AI、扩展组件等模块化能力。

## i2f-jdk

> 仅依赖 JDK8 的基础能力模块集合，提供反射、类型系统、集合、IO、加密、JDBC、SPI、JVM Agent、AI 标准抽象、网络通信、脚本引擎、日志门面等通用工具与契约层。

### i2f-jdk-all

> i2f-jdk 全子模块的 Maven 聚合分发包，将约 91 个 i2f-jdk 子模块经 `maven-assembly-plugin` 打包为单体 fat-jar，是「一次依赖、全套引入」的一站式构建入口。

- 详细文档：[i2f-jdk-all](./i2f-jdk/i2f-jdk-all/readme.md)

### i2f-spi-annotations

> SPI 注解定义模块，提供 `@Spi` 标记注解，声明服务实现类及其提供的接口，配合构建期插件生成 `META-INF/services/` 描述文件。

- 详细文档：[i2f-spi-annotations](./i2f-jdk/i2f-spi-annotations/readme.md)

### i2f-spi

> SPI 运行期支撑模块，基于 `java.util.ServiceLoader` 封装统一的服务加载、组件/工厂契约接口与描述文件编程式生成工具。

- 详细文档：[i2f-spi](./i2f-jdk/i2f-spi/readme.md)

### i2f-agent

> 基于 `java.lang.instrument` 的 Java Agent 模块，支持静态 `premain` 与动态 `agentmain` 挂载，提供 Instrumentation 装配、按 Ant 模式匹配类的字节码转换骨架与交互式 attach 工具。

- 详细文档：[i2f-agent](./i2f-jdk/i2f-agent/readme.md)

### i2f-ai-std

> AI 能力标准抽象（SPI）模块，模型无关地定义大模型对话、工具（function-calling）、RAG、技能、MCP 网关、记忆与 Re-Act Agent 引擎，并提供声明式 `@AiService` 动态代理。

- 详细文档：[i2f-ai-std](./i2f-jdk/i2f-ai-std/readme.md)

### i2f-network

> 基于 JDK 标准网络 API 的通用网络模块，封装 HTTP/REST 客户端、BIO/NIO 双栈 TCP/UDP、RMI 远程调用、本机网卡与出口 IP 探测及并发端口扫描。

- 详细文档：[i2f-network](./i2f-jdk/i2f-network/readme.md)

### i2f-ai-rest-openai

> `i2f-ai-std` 抽象契约的 OpenAI 兼容 HTTP 实现，基于 `i2f-network` 对接对话/Embedding/Rerank/Models 端点（支持 SSE 流式），并提供自研 HMAC 签名的 Simple MCP 跨进程工具网关（客户端 + 服务端）。

- 详细文档：[i2f-ai-rest-openai](./i2f-jdk/i2f-ai-rest-openai/readme.md)

### i2f-algo

> 通用算法模块，提供基于二分（策略驱动）的边界近似求交点算法（数值区间/圆形/多边形）与基于动态规划的最短编辑距离（Levenshtein）算法。

- 详细文档：[i2f-algo](./i2f-jdk/i2f-algo/readme.md)

### i2f-annotations-core

> i2f 注解核心库，零依赖收录 67 个纯语义的标记/描述型注解（base/check/doc/env/except/format/naming/resources/value/version 十大类），统一 RUNTIME 保留、自描述，供反射与文档/代码生成消费。

- 详细文档：[i2f-annotations-core](./i2f-jdk/i2f-annotations-core/readme.md)

### i2f-annotations-ext

> i2f 注解扩展库，在 core 之上声明缓存/重试降级/锁/事务/调度/异步编排/字典翻译/文本整形等横切行为语义（34 注解 + IDict 枚举），供 i2f 运行期切面/代理引擎解释执行。

- 详细文档：[i2f-annotations-ext](./i2f-jdk/i2f-annotations-ext/readme.md)

### i2f-annotations-db

> 数据库映射注解库，以注解声明「Bean ↔ 关系表 DDL」映射（表/列/类型、主键自增、外键/软引用、唯一/索引/约束、忽略控制，17 注解 + IDict 枚举），供元数据读取与反向工程生成器反射消费。

- 详细文档：[i2f-annotations-db](./i2f-jdk/i2f-annotations-db/readme.md)

### i2f-annotations-api

> API 描述注解库，以协议无关的注解声明接口语义（模块/系统/标签/操作/请求方式/协议，6 注解），为 API 文档生成与接口治理提供结构化描述，由 Spring MVC 元数据解析等反射消费。

- 详细文档：[i2f-annotations-api](./i2f-jdk/i2f-annotations-api/readme.md)

### i2f-annotations

> i2f 注解体系的聚合（门面）模块，零源码，仅通过 POM 将 core/ext/db/api 四支汇总为单一坐标，供使用方「一次依赖、全套注解」引入，版本由父 POM 统一托管。

- 详细文档：[i2f-annotations](./i2f-jdk/i2f-annotations/readme.md)

### i2f-bytes

> 字节序编解码工具模块，单一静态门面 `ByteUtil` 以 48 个方法完成「7 种基本类型 × Big/Little 端序 × 新建数组/就地写入」双向互转，另支持 1–8 字节任意宽度定长编解码；零 Maven 依赖。

- 详细文档：[i2f-bytes](./i2f-jdk/i2f-bytes/readme.md)

### i2f-atomic

> 原子变量补充模块，零依赖自研 JDK 缺失的 `AtomicDouble`/`AtomicDoubleArray`，把 double 经 raw-long-bits 编码复用 `AtomicLongFieldUpdater`/`AtomicLongArray` 做无锁 CAS，语义对齐 `java.util.concurrent.atomic` 家族。

- 详细文档：[i2f-atomic](./i2f-jdk/i2f-atomic/readme.md)

### i2f-array

> 数组工具模块，单一静态门面 `ArrayUtil` 提供 458 个方法，覆盖判定/读写/构造/集合互转/映射过滤/合并/二维扁平/填充/拷贝/反转/比较/相等，对象与 8 种基本类型数组全覆盖。

- 详细文档：[i2f-array](./i2f-jdk/i2f-array/readme.md)

### i2f-authentication

> 认证标准契约模块，框架无关地定义密码编码/登录密码解码/用户与 RBAC 主体模型，并给出加盐 + 多轮迭代、密文自描述参数的 `MessageDigestPasswordEncoder` 实现；被 Spring Security、Shiro、SpringBoot starter 实现与继承。

- 详细文档：[i2f-authentication](./i2f-jdk/i2f-authentication/readme.md)

### i2f-bindsql

> 参数化 SQL 流式构建器，以 `BindSql` 将「SQL 文本 + 绑定参数」一体化链式拼装，内建 MyBatis 风格动态 SQL（when/choose/foreach/连接词裁剪）与关键字 DSL，并提供字面量回填/美化/去注释的正则文本处理；为分页、JDBC、BQL 等持久化能力的地基。

- 详细文档：[i2f-bindsql](./i2f-jdk/i2f-bindsql/readme.md)

### i2f-bindsql-page

> 分页/计数 SQL 方言适配器，以 `IPageWrapper`/`ICountWrapper` 双扩展点把 `BindSql` 改写为各库分页与 `count(1)` 语句；内置 9 方言 + 20+ 小库重定向归并，经 ThreadLocal→SPI Provider→方言映射→内置分发四级解析链选择。

- 详细文档：[i2f-bindsql-page](./i2f-jdk/i2f-bindsql-page/readme.md)

### i2f-bindsql-stringify

> 参数化 SQL 文本化模块，以 `WrappedBindSqlStringifier` 把 `BindSql` 的裸 `?` 按目标方言回填为 SQL 字面量（字符串转义、MySQL `STR_TO_DATE`/Oracle `TO_DATE`），还原成完整可执行/可展示语句，供日志打印与纯 Statement 脚本执行器使用。

- 详细文档：[i2f-bindsql-stringify](./i2f-jdk/i2f-bindsql-stringify/readme.md)

### i2f-database-type

> 数据库类型/方言识别层（持久化族的类型地基），`DatabaseType` 枚举收录 36 库经「URL 嗅探 + 连接探测 + SPI」三路径识别，`DatabaseDialectMapping` 做小库→主流方言归并；纯 JDK 零三方依赖。

- 详细文档：[i2f-database-type](./i2f-jdk/i2f-database-type/readme.md)

### i2f-bql

> 类型安全绑定查询语言（BQL），在 `BindSql` 之上提供全量 SQL 关键字 DSL、方言自适应标识符装饰与动态 SQL；以 core→map→lambda→bean 四级继承链支持「字符串/Map/方法引用/实体 Bean」递进式列寻址，产物仍是 `BindSql`。

- 详细文档：[i2f-bql](./i2f-jdk/i2f-bql/readme.md)

### i2f-lambda-core

> 方法引用解析内核，以 `extends Serializable` 的 `IGetter`/`ISetter`/`IBuilder` 承接 getter/setter 型方法引用，经 `writeReplace`→`SerializedLambda` 反查引用的类/方法/字段，反射定位 `Class`/`Method`/`Field`；是 bql/mutator/lambda 的类型安全底座。

- 详细文档：[i2f-lambda-core](./i2f-jdk/i2f-lambda-core/readme.md)

### i2f-lambda

> 方法引用解析通用层，在 `i2f-lambda-core` 的 getter/setter 命名约定之上补上「任意方法引用」的还原：用 `SerializedLambda` 的方法名 + JVM 描述符经 `i2f-reflect` 精确匹配真实 `Method`。

- 详细文档：[i2f-lambda](./i2f-jdk/i2f-lambda/readme.md)

### i2f-functional

> 可序列化函数式接口地基（零依赖），以 `IFunctional extends Serializable` 为顶点按「返回域 × 元数 × 是否抛异常」枚举 327 个接口，是上层 lambda 反解方法引用的前提。

- 详细文档：[i2f-functional](./i2f-jdk/i2f-functional/readme.md)

### i2f-functional-lambda

> 「函数式接口 × 方法引用解析」粘合层：把 `ExFunctionalDelegator` 重载汇聚前门与 `LambdaInflater` 方法引用反解器对接，产出 `Lambdas.FIELD/METHOD/LAMBDA` 三个无状态转换器单例，一行即得 `Field`/`Method`/`SerializedLambda`。

- 详细文档：[i2f-functional-lambda](./i2f-jdk/i2f-functional-lambda/readme.md)

### i2f-browser-std

> 浏览器搜索引擎抓取的标准契约层，零引擎依赖地沉淀统一出参模型 `SearchContext`/`SearchResult`、爬行阶段枚举、运行期可变屏蔽常量与 URL 归一工具；是 Playwright 与 Selenium 两套抓取实现的共同上游契约。

- 详细文档：[i2f-browser-std](./i2f-jdk/i2f-browser-std/readme.md)

### i2f-mutator

> 流式对象修改器，以 `Mutator<T>` 门面用方法引用链式修改任意对象（set/with 四维矩阵 + apply/when/map 控制流），并借方法引用反射提供 `field*` 族「只给 getter 即读改写」的特色能力。

- 详细文档：[i2f-mutator](./i2f-jdk/i2f-mutator/readme.md)

### i2f-otpauth

> 一次性口令（OTP）认证模块，纯 JDK 实现 RFC 4226/6238 的「HMAC → 动态截断 → 数字码」内核与 `otpauth://` 密钥 URI 装配，抽象骨架下沉 HOTP/TOTP 与 Microsoft/Steam 两级厂商兼容；运行期零三方依赖。

- 详细文档：[i2f-otpauth](./i2f-jdk/i2f-otpauth/readme.md)

### i2f-tuple-std

> 元组标准契约层，全模块仅 `Tuple` 接口：以 size/get/set/toList 四抽象刻画「下标化异质多值容器」，并用 default 派生判空/转数组/可遍历，是 `Tuple0…Tuple20` 实现族的向上转型锚点。

- 详细文档：[i2f-tuple-std](./i2f-jdk/i2f-tuple-std/readme.md)

### i2f-tuple-impl

> 元组强类型实现家族，以「一档 arity 一类」提供 `Tuple0`…`Tuple20`（@Data POJO）+ 变长 `TupleVars`，由 `Tuples.of/ofVars` 工厂按实参数分派产出；`Tuple2` 兼任 `Map.Entry`。

- 详细文档：[i2f-tuple-impl](./i2f-jdk/i2f-tuple-impl/readme.md)

### i2f-typeof

> 类型系统地基：自动装箱感知的类型谓词门面 `TypeOf`（原始/包装/父子三态统一判定）+ Guava 风格超类型令牌 `TypeToken`/`TypeNode`（匿名子类反泛型擦除、还原任意深度嵌套泛型），是 reflect/convert/container-builder 等的共同判据。

- 详细文档：[i2f-typeof](./i2f-jdk/i2f-typeof/readme.md)

### i2f-uid-impl

> UID 生成器系列的默认实现家族：32 位秒级雪花 `SnowflakeIntUid`、64 位毫秒级雪花 `SnowflakeLongUid`（MAC 哈希推导 workerId 零配置自适应）与 UUID 压缩 `UuidRandomStringUid`，全量落地上游 `i2f-uid-std` 契约。

- 详细文档：[i2f-uid-impl](./i2f-jdk/i2f-uid-impl/readme.md)

### i2f-uid-std

> UID 生成器系列的标准契约层：三层契约树 `IStringUid`/`IIntUid`/`ILongUid`（均函数式接口 + 定宽 hex 默认方法），零依赖。

- 详细文档：[i2f-uid-std](./i2f-jdk/i2f-uid-std/readme.md)

### i2f-unsafe

> 底层内存窥探工具：静态类 `UnsafeHacker` 反射撬取 `sun.misc.Unsafe`，提供对象物理地址读取与对象深链字节大小估算（Nashorn 缺失时优雅降级），零 Maven 依赖。

- 详细文档：[i2f-unsafe](./i2f-jdk/i2f-unsafe/readme.md)

### i2f-verifycode

> 「看图答题」式图形验证码「生成 + 校验」框架：1 接口 + 5 抽象基类按「交互形态 × 维度」定型 8 种实现（艺术字/算式/极坐标加法/标尺与矩阵点击等），答案统一百分比且缩放无关，模块无状态。

- 详细文档：[i2f-verifycode](./i2f-jdk/i2f-verifycode/readme.md)

### i2f-workflow

> 「任务依赖编排 + DAG 并发调度」双引擎流程编排模块：旧引擎 `WorkFlow` 流式声明节点图轮询触发，新引擎 `rag.DagScheduler` 拓扑分层调度 + 条件边 + 人工节点断点暂停/恢复。

- 详细文档：[i2f-workflow](./i2f-jdk/i2f-workflow/readme.md)

### i2f-xml

> 「XML 解析双引擎 + 行号定位工具箱 + Maven POM 解析器」：DOM 解析行号位置注入（「文件:行号」级错误定位）+ StAX 流式轻量树 + 父 POM 依赖版本推导，双工厂 XXE 三禁闭。

- 详细文档：[i2f-xml](./i2f-jdk/i2f-xml/readme.md)

### i2f-match-std

> 字符串匹配器标准契约层：`IMatcher` 布尔契约 + `IPriorMatcher` 打分契约（matchRate 负值不匹配），沉淀打分公式与浮点阈值，解决「一个串命中多模式」时按精确度择优排序。

- 详细文档：[i2f-match-std](./i2f-jdk/i2f-match-std/readme.md)

### i2f-match

> 字符串匹配器实现层 + 正则工具箱：Simple/Ant/Regex 三实现共享打分公式，`StringMatcher` 门面按精确度降序择优；另一半 `regex` 子包是被约 20 个模块共用的全仓正则底座。

- 详细文档：[i2f-match](./i2f-jdk/i2f-match/readme.md)

### i2f-lru-map

> 「内存缓存 / 自调节容器 / 弱引用复用 / RAII 作用域」工具族：LruMap 家族（容量淘汰/时间片/自调节/并发缓存）、过期记忆化、弱引用复用与 ScopeValue 配对释放；`LruMap` 是全仓反射/编译缓存的事实标准。

- 详细文档：[i2f-lru-map](./i2f-jdk/i2f-lru-map/readme.md)

### i2f-lru-cache

> 将 LRU 淘汰策略接入缓存接口的适配桥接模块：`LruMapCache` 继承 `MapCache` 并以注入的 `LruMap` 替换底层 Map，使缓存自带 LRU 容量淘汰，可与过期装饰器组合出「LRU + TTL」双淘汰。

- 详细文档：[i2f-lru-cache](./i2f-jdk/i2f-lru-cache/readme.md)

### i2f-math

> 数学计算工具箱：`MathUtil` 静态门面（三角/插值/排列组合/精度截断）、双栈公式计算器 `FormulaCalculator`（60+ 运算符）、`Matrix` 矩阵运算、`Segment` 区间抽象与大数数列。

- 详细文档：[i2f-math](./i2f-jdk/i2f-math/readme.md)

### i2f-mixins

> 混入式函数编程工具箱：以 JDK 接口默认方法实现 Java「多重继承」式函数编程，18 个 `*Mixins` 接口聚合为 `AllMixins`，覆盖 String/Math/Object/Date/Regex 等九大领域数百函数，`MixinProxyFactory` 动态代理免匿名对象。

- 详细文档：[i2f-mixins](./i2f-jdk/i2f-mixins/readme.md)

### i2f-native-core

> 原生（JNI）指针工具箱：`Ptr` 指针值类型安全包装 + 三种分配来源标记指针（malloc/new/new[]）+ `NativeUtil` 从 classpath 释放并加载原生库，附系统 JNI 开发教程。

- 详细文档：[i2f-native-core](./i2f-jdk/i2f-native-core/readme.md)

### i2f-native-windows

> Windows（JNI）原生 API 门面：`NativesWindows` 约 250+ native 方法声明 + `WinApi` 类型安全门面，13 大子域覆盖窗口管理/GDI 绘图/输入模拟/进程线程/注册表/服务/COM/Shell/控制台等。

- 详细文档：[i2f-native-windows](./i2f-jdk/i2f-native-windows/readme.md)

### i2f-native-windows-easyx

> EasyX 图形库（JNI）Java 门面：一行 `initGraph` 即创建图形窗口，11 大功能域覆盖绘图原语/文字输出/图像操作/绘图状态/批量绘制/鼠标输入/颜色转换/工作图像等。

- 详细文档：[i2f-native-windows-easyx](./i2f-jdk/i2f-native-windows-easyx/readme.md)

### i2f-number-idcard

> 中国大陆身份证号码解析/校验工具：18 位公民身份号码五段切分（区划/日期/顺序/性别/校验位）+ 加权模 11 校验码判定 + 内置行政区划字典（GB 11643-1999），失败以原因说明而不抛异常。

- 详细文档：[i2f-number-idcard](./i2f-jdk/i2f-number-idcard/readme.md)

### i2f-cache-std

> 缓存标准契约层（6 个纯接口零实现）：根接口 `ICache` 沿「容器枚举 / 过期 / 持久分布式」三条正交轴继承扩展，为本地/Redis/Zookeeper/Hazelcast 等多形态缓存提供统一契约。

- 详细文档：[i2f-cache-std](./i2f-jdk/i2f-cache-std/readme.md)

### i2f-cache

> 缓存本地实现层：`MapCache` 把可注入 Map 适配为容器缓存，`ExpireCacheWrapper` 以装饰器 + ExpireData 信封把任意 ICache 升级为 TTL 过期缓存，二者正交组合即得「本地 + 可过期」默认装配。

- 详细文档：[i2f-cache](./i2f-jdk/i2f-cache/readme.md)

### i2f-check

> 「校验/断言」工具箱（pom 零依赖）：可累积、可换值、可快速失败的流式校验链 `Checker<T>` + 约 90 个「为方法引用而生」的静态布尔谓词 `Predicates`。

- 详细文档：[i2f-check](./i2f-jdk/i2f-check/readme.md)

### i2f-check-filter

> 存在性判定 / 大数据去重过滤器族：内存级 `ICheckFilter`/`BloomFilter`（位数组多哈希概率判重）+ 大数据级 `IRepeatFilter`（布隆近似去重 / 哈希分组 + 临时文件精确去重）。

- 详细文档：[i2f-check-filter](./i2f-jdk/i2f-check-filter/readme.md)

### i2f-code

> 码值/密钥生成器（单静态门面 `CodeUtil`）：`makeUUID` 去连字符大写压缩 + `makeCheckCode` 基于 SecureRandom 的 62 字符均匀采样（避免模偏差），被 SWL 加密器用于生成 AES 密钥。

- 详细文档：[i2f-code](./i2f-jdk/i2f-code/readme.md)

### i2f-codec-std

> 编解码标准契约层：根接口 `ICodec` 以 encode/decode 双动词定义「类型 E ↔ D 双向转换」，沿数据形态衍生 5 个子接口（字节/字符串/集合打包/流式），配套统一异常。

- 详细文档：[i2f-codec-std](./i2f-jdk/i2f-codec-std/readme.md)

### i2f-codec-impl

> 编解码实现层（23 实现类覆盖 5 组接口）：Base64/Base32/Hex 等字节编解码、URL/HTML/Unicode 转义、集合 ID 打包、Gzip/Deflate/Zip 压缩，`CodecUtil` 门面 25+ 便捷方法一行完成常见操作。

- 详细文档：[i2f-codec-impl](./i2f-jdk/i2f-codec-impl/readme.md)

### i2f-color

> RGBA / HSL 色彩空间表示与转换模型：`Rgba` 四通道 + 位掩码枚举 + 灰度/色差/平滑插值，`Hsl` 标准双向转换算法，二者零拷贝互转，含 10 色预定义常量。

- 详细文档：[i2f-color](./i2f-jdk/i2f-color/readme.md)

### i2f-comparator

> 通用比较器工具箱：`NullableComparator` null 哨兵协议 + `DefaultComparator` 六级兜底级联（Comparable→类型兼容→BigDecimal→字典序→try→hashCode）+ 数组/反序/Bean 属性比较器与方法引用友好门面。

- 详细文档：[i2f-comparator](./i2f-jdk/i2f-comparator/readme.md)

### i2f-compiler

> 内存 Java 编译器 / 表达式求值器：基于 JDK `JavaCompiler` API 的编译→类加载→自动清理三级能力，以 `###import/###class/###method` 标签 DSL 包装表达式反射求值；需完整 JDK。

- 详细文档：[i2f-compiler](./i2f-jdk/i2f-compiler/readme.md)

### i2f-compress-std

> 压缩标准契约层：多文件归档级 `ICompressor` + 单流级 `ISingleCompressor` 双契约，`AbsCompressor` 沉淀文件遍历/流绑定/解包落地的公共骨架。

- 详细文档：[i2f-compress-std](./i2f-jdk/i2f-compress-std/readme.md)

### i2f-compress-impl

> 压缩 JDK 内置实现层（零三方运行期依赖）：归档级 Zip/Jar + 单流级 Gzip/Deflater/Zip 共 5 实现，全量落地 compress-std 双契约。

- 详细文档：[i2f-compress-impl](./i2f-jdk/i2f-compress-impl/readme.md)

### i2f-console-color

> ANSI 控制台彩色输出工具：3 枚举覆盖标准 SGR 参数（18 前景/18 背景/5 样式）+ `ConsoleOutput` 门面自动编排转义序列（合并与自动 RESET）+ 三态开关自动检测（IDEA/终端/Windows）。

- 详细文档：[i2f-console-color](./i2f-jdk/i2f-console-color/readme.md)

### i2f-container

> 集合容器增强工具族：`CollectionUtil` 约 300 方法全覆盖收集场景 + `RingQueue` 有界阻塞队列 + LRU 族 + 并发 Set + sync 同步包装族（12 适配器 + 模板/代理骨架）+ readonly 只读包装族。

- 详细文档：[i2f-container](./i2f-jdk/i2f-container/readme.md)

### i2f-container-builder

> 流式集合/Map/对象构建器族：`ObjectBuilder`（方法引用改 POJO）/`CollectionBuilder`/`ListBuilder`/`MapBuilder` + `Builders` 约 50 静态工厂，以 fluent 模式补齐 JDK 集合框架缺失的链式构造。

- 详细文档：[i2f-container-builder](./i2f-jdk/i2f-container-builder/readme.md)

### i2f-context-std

> IoC 容器标准契约层（4 个纯接口）：`IContext` 按类型查询 + `INamingContext` 按名查询 + 两个可写变体，构成「查询维度 × 读写性」2×2 正交矩阵。

- 详细文档：[i2f-context-std](./i2f-jdk/i2f-context-std/readme.md)

### i2f-context-impl

> IoC 容器纯 JDK 内存实现层：`ListableContext`（类型仓储，精确匹配优先、兼容匹配兜底）与 `ListableNamingContext`（名→Bean 双结构 + 读写锁），被 AI 栈与存储过程引擎消费。

- 详细文档：[i2f-context-impl](./i2f-jdk/i2f-context-impl/readme.md)

### i2f-convert

> 通用类型转换工具箱：以 `ObjectConvertor.tryConvertAsType` 覆盖 20+ 类型域的万能转换（源→目标双向匹配 + BigDecimal/Instant 归一化中转 + 字符串字面量解析 + 反射四级策略），另含列表⇄树双向转换子包。

- 详细文档：[i2f-convert](./i2f-jdk/i2f-convert/readme.md)

### i2f-crypto-std

> 加密/解密/摘要/签名标准契约层：5 接口（内建 Base64/Hex 默认方法）+ 5 个 `Bytes*Key` 字节适配密钥模型 + `AsymKeyPair` 密钥对流式序列化。

- 详细文档：[i2f-crypto-std](./i2f-jdk/i2f-crypto-std/readme.md)

### i2f-crypto-impl

> 加密/解密/摘要/签名 JDK 内置实现层：对称 8 算法 + 非对称 2 算法 + 签名 12 变体 + 摘要/HMAC/校验和三族，最底层 `Encryptor` 封装全部 JDK 安全 API 实例化，算法常量枚举消除字面量散落。

- 详细文档：[i2f-crypto-impl](./i2f-jdk/i2f-crypto-impl/readme.md)

### i2f-data-processor

> 数据读/写抽象处理器，基于 ILifeCycle 生命周期约束的逐条读写最小契约，提供 IDataReader/IDataWriter 双接口 + 2 个文本行 JDK IO 实现，被 i2f-check-filter 的大数据去重族消费。

- 详细文档：[i2f-data-processor](./i2f-jdk/i2f-data-processor/readme.md)

### i2f-datetime

> Java 日期时间一站式工具箱，零三方依赖以 Dates 静态门面 + fluent 实例 API 双模式提供格式解析、日期算术、边界计算、类型互转及时令判定；Calendars 内建中国农历生肖与干支纪年推算；被 i2f-number-idcard 消费。

- 详细文档：[i2f-datetime](./i2f-jdk/i2f-datetime/readme.md)

### i2f-design-pattern

> 设计模式教学演示模块：189 源文件系统化落地 5 大类 31 种设计模式（GoF 23 + 并发 4 + 架构 4），每模式以「场景实现 + Test 演示 + readme」四件套呈现，附总纲文档梳理定义与 JDK/Spring 典型案例。

- 详细文档：[i2f-design-pattern](./i2f-jdk/i2f-design-pattern/readme.md)

### i2f-detegate

> 「通用委托执行骨架」三件套（零状态静态门面）：`BatchDelegator` 分页批量拉取、`CacheDelegator` 读穿缓存、`FallbackDelegator` 重试 + 几何退避 + 降级容错。

- 详细文档：[i2f-detegate](./i2f-jdk/i2f-detegate/readme.md)

### i2f-dict

> 注解驱动的字典码值双向转换器：`@DictDecode`/`@DictEncode` 声明跨字段或就地映射 + `@Dict` 内联字典项（含 null 兜底），`DictResolver` 沿提供者链取字典强转写回。

- 详细文档：[i2f-dict](./i2f-jdk/i2f-dict/readme.md)

### i2f-enums

> 字典枚举契约 `IDict`（code/text/key/remark 四方法）与两个开箱即用的三值基础枚举 `Bool`/`YesNo`（未定义/真-是/假-否），零依赖。

- 详细文档：[i2f-enums](./i2f-jdk/i2f-enums/readme.md)

### i2f-environment-std

> 环境配置标准契约层：`IEnvironment` 只读契约（getProperty + 4 组类型安全 default getter）与 `IWritableEnvironment` 写入契约（setProperty + 4 setter）。

- 详细文档：[i2f-environment-std](./i2f-jdk/i2f-environment-std/readme.md)

### i2f-event

> 轻量级异步事件发布/订阅调度框架：单守护线程 + 无界队列 + 工作窃取线程池的异步批处理分发（批量攒批、订阅过滤、超期丢弃），支持空闲睡眠与线程属性定制。

- 详细文档：[i2f-event](./i2f-jdk/i2f-event/readme.md)

### i2f-environment-impl

> 环境配置契约的默认实现层：`SystemAdditionalEnvironment` 采集 15 项 JVM 运行时/编译/OS 信息快照并支持写覆盖，`ListableDelegateEnvironment` 提供多环境委托链（首个非空胜出）。

- 详细文档：[i2f-environment-impl](./i2f-jdk/i2f-environment-impl/readme.md)

### i2f-exception

> 语义化运行时异常词汇表：16 个直接继承 RuntimeException 的扁平异常类（长度/报文/类型/并发/数据访问/反射/远程/服务等场景），统一五构造器模板，零依赖。

- 详细文档：[i2f-exception](./i2f-jdk/i2f-exception/readme.md)

### i2f-features

> 能力标记接口集：cluster/concurrent/store/sync 四子包共 11 个空标记接口（分布式的/并发的/内存的/异步的等能力标签），供实现类打标 + `instanceof` 嗅探。

- 详细文档：[i2f-features](./i2f-jdk/i2f-features/readme.md)

### i2f-firewall

> 注入攻击防护防火墙：CRLF/Host/路径穿越/反序列化类名/SQL 注入/XSS/XXE 共 7 类断言式检测器（失败抛异常），检测引擎支持嵌套编码变体组合与 Decode 路线的地毯式匹配。

- 详细文档：[i2f-firewall](./i2f-jdk/i2f-firewall/readme.md)

### i2f-form

> 「Swing 模态对话框组件库 + 统一预览系统」：消息/确认/输入/单选/多选/倒计时 6 类模态窗口（CountDownLatch 同步阻塞）+ 现代扁平皮肤 + 图片/媒体/文本/网页 15 种预览实现（JavaFX 桥接）。

- 详细文档：[i2f-form](./i2f-jdk/i2f-form/readme.md)

### i2f-form-url-encoded

> 「form-urlencoded 编解码器 + URI 结构化解析器」：任意对象/嵌套 Map/集合与 form 串双向转换（点号路径扁平化协议），`UriMeta` 解析 8 类 URI 方言（http/jdbc/file/git@/Oracle 等）。

- 详细文档：[i2f-form-url-encoded](./i2f-jdk/i2f-form-url-encoded/readme.md)

### i2f-geo

> 地理坐标转换与米度概算工具：WGS84/GCJ-02/BD-09/WebMercator 等五大坐标系互转（含火星/百度偏移与反解）+ 米度概算，零依赖。

- 详细文档：[i2f-geo](./i2f-jdk/i2f-geo/readme.md)

### i2f-graphics-2d

> 二维图形学基础库（纯 JDK + AWT 零引擎依赖）：原语/形状层 + 方向旋转反射运算 + 贝塞尔采样 + 齐次变换 + 多边形工具 5 件套 + 投影/变换接口族 + Swing 画布与函数绘图。

- 详细文档：[i2f-graphics-2d](./i2f-jdk/i2f-graphics-2d/readme.md)

### i2f-graphics-3d

> 三维图形学基础库（纯 JDK + AWT/Swing）：3D 原语与齐次变换 + 10 种投影（三视图/正交/透视）+ 23 种变换 + Phong 光照材质 + 背面剔除 + 参数形状 13 个 + 三角化 + 交互画布。

- 详细文档：[i2f-graphics-3d](./i2f-jdk/i2f-graphics-3d/readme.md)

### i2f-graphics

> 二维/三维图形学能力聚合门面模块 —— 零源码纯聚合，将 `i2f-graphics-2d` 与 `i2f-graphics-3d` 经 `maven-assembly-plugin` 打包为单一 fat-jar，被 `i2f-jdk-all` 全仓聚合引入。

- 详细文档：[i2f-graphics](./i2f-jdk/i2f-graphics/readme.md)

### i2f-hash

> 非加密哈希算法工具箱：泛型 `IHashProvider<T>` 接口与序列化桥接基类 + 12 种经典非加密哈希算法（AP/BKDR/Boost/FNV/DJB/JS/PJW/SDBM 等），被 check-filter 的布隆与分组去重消费。

- 详细文档：[i2f-hash](./i2f-jdk/i2f-hash/readme.md)

### i2f-http-proxy

> 声明式 REST 客户端框架：`@RestClient` 接口 + JDK 动态代理 + 11 种自研注解构建声明式 HTTP 客户端，内置环境变量替换与可插拔 Processor/序列化/请求定制/响应抽取扩展点。

- 详细文档：[i2f-http-proxy](./i2f-jdk/i2f-http-proxy/readme.md)

### i2f-i18n

> 国际化消息工具集：`I18n` 静态门面 + SPI 三优先级初始化 + `InheritableThreadLocal` 线程语言上下文 + 自动文件扫描（properties/XML 双格式，XML 支持 ref 引用与 keep/only 格式控制）。

- 详细文档：[i2f-i18n](./i2f-jdk/i2f-i18n/readme.md)

### i2f-image-impl

> 图像滤镜与处理实现层：`ImageCompressor` 智能压缩 + LSB 隐写 + 28 种图像级滤镜 + 20 种像素级 RGBA 滤镜，被 `i2f-springboot-ops-starter` 的 OpenAiOpsController 消费。

- 详细文档：[i2f-image-impl](./i2f-jdk/i2f-image-impl/readme.md)

### i2f-image-std

> 图像处理标准接口与工具集：`ImageUtil` 图片 I/O + `FontUtil` 字体加载注册 + 三层过滤器接口体系（IImageFilter/PixelFilter/RgbaFilter）及双向桥接适配器。

- 详细文档：[i2f-image-std](./i2f-jdk/i2f-image-std/readme.md)

### i2f-invokable

> JDK 反射调用抽象层：`IInvokable` 最小调用契约 + `IMethod` 元数据扩展 + 4 种 JDK 反射包装（Method/Constructor/Executable/InstanceStatic）+ 装饰器与调用快照，被 antlr4 脚本引擎与 aspectj 消费。

- 详细文档：[i2f-invokable](./i2f-jdk/i2f-invokable/readme.md)

### i2f-io-stream

> Java 字节流操作工具集（纯 JDK）：`StreamUtil` 全能静态工具（拷贝/广播/范围/本地化）+ 校验和体系 + XOR 加密流体系 + 黑洞/白洞/懒加载/临时文件等特殊流；被全仓 25+ 模块消费。

- 详细文档：[i2f-io-stream](./i2f-jdk/i2f-io-stream/readme.md)

### i2f-io-file

> 全能文件操作工具集：`FileUtil` 全能静态工具（读写/拷贝/移动/删除/合并/分割/路径规约/树遍历/CSV 解析）+ `FileType` 魔数判定（76 种）+ MIME 映射（约 420 条）+ 文件种类分类器 + 应用级回收站；被全仓 22+ 模块消费。

- 详细文档：[i2f-io-file](./i2f-jdk/i2f-io-file/readme.md)

### i2f-io-filesystem

> 统一文件系统抽象层：`IFile`/`IFileSystem` 双契约 + `AbsFile`/`AbsFileSystem` 抽象基类（组合能力模板，实现方仅需约 10 个原语）+ 路径安全工具 + JDK 本地实现；被 6 个 `i2f-extension-filesystem-*` 模块继承消费。

- 详细文档：[i2f-io-filesystem](./i2f-jdk/i2f-io-filesystem/readme.md)

### i2f-iterator

> 迭代器三向适配与增强工具集：三门面实现 Iterator ↔ Iterable ↔ Enumeration 互转（覆盖 Stream/8 种原生数组/任意对象数组/Reader 行流）+ 5 装饰器（map/filter/peek/透传/懒加载）+ 资源生命周期模板 + 17 个数组迭代器。

- 详细文档：[i2f-iterator](./i2f-jdk/i2f-iterator/readme.md)

### i2f-javacode-graph

> Java 代码结构图生成模块：三步管线（反射解析类/字段/方法/构造器/注解节点树 → 关系图拍平 → ECharts graph 数据输出）产出可预览的类结构关系图。

- 详细文档：[i2f-javacode-graph](./i2f-jdk/i2f-javacode-graph/readme.md)

### i2f-jdbc-data

> JDBC 生态的数据契约地基（纯数据载体零执行逻辑）：连接元信息 `JdbcMeta` + 查询结果模型 `QueryResult`/`QueryColumn` + 类型化参数协议 `TypedArgument` + 存储过程命名输出参数四组共享模型。

- 详细文档：[i2f-jdbc-data](./i2f-jdk/i2f-jdbc-data/readme.md)

### i2f-jdbc-impl

> JDBC 体系的核心执行层：超级门面 `JdbcResolver`（连接/事务/查询家族/批处理/存储过程/游标/类型绑定/结果解析管线）+ 三组 SPI 注册表 + `JdbcTemplate` 会话委托模板与脚本执行器。

- 详细文档：[i2f-jdbc-impl](./i2f-jdk/i2f-jdbc-impl/readme.md)

### i2f-jdbc-std

> JDBC 体系的最小契约层 / 依赖倒置地基（3 接口 50 行）：连接获取上下文 `JdbcInvokeContextProvider` 生命周期抽象 + `SQLFunction`/`SQLBiFunction` SQL 感知函数式接口。

- 详细文档：[i2f-jdbc-std](./i2f-jdk/i2f-jdbc-std/readme.md)

### i2f-jdbc-bql

> BQL 的运行期落地层 / JDBC 执行门面：`BqlTemplate extends JdbcTemplate` 把 BQL 流式构建器物化后直接执行，含 Bean CRUD 快捷方法与继承的查询直通通道。

- 详细文档：[i2f-jdbc-bql](./i2f-jdk/i2f-jdbc-bql/readme.md)

### i2f-jdbc-proxy

> Mapper 接口的 JDBC 动态代理层（MyBatis Mapper 模式的独立实现）：参数名解析 + 会话委托 + 按返回类型分派 `JdbcResolver` + 五级 SQL 来源管线（BaseMapper 内建桥/缓存/@SqlScript/脚本渲染）。

- 详细文档：[i2f-jdbc-proxy](./i2f-jdk/i2f-jdbc-proxy/readme.md)

### i2f-jdbc-proxy-xml

> MyBatis 风格的 XML 动态 SQL 引擎（Mapper XML 独立重实现）：`MybatisMapperParser` 节点树解析 + 解释器（15 种脚本标签/foreach 多形态/占位符/组件与 SPI 扩展），默认内存编译表达式且 OGNL 变体可换。

- 详细文档：[i2f-jdbc-proxy-xml](./i2f-jdk/i2f-jdbc-proxy-xml/readme.md)

### i2f-jdbc-procedure

> XML 存储过程 / 工作流引擎（XProc4J）——「去数据库存储过程」的核心业务引擎：XML 解析 → 元信息 → 执行器（77 内置节点三通道注册 + 事务/连接缓存/慢监控）+ Java/XML 过程统一注册。

- 详细文档：[i2f-jdbc-procedure](./i2f-jdk/i2f-jdbc-procedure/readme.md)

### i2f-database

> 数据库能力聚合门面模块，零源码纯聚合，将 `i2f-database-type`（方言类型识别）与 `i2f-database-metadata-impl`（元数据多方言实现）经 `maven-assembly-plugin` 打包为单一 fat-jar。

- 详细文档：[i2f-database](./i2f-jdk/i2f-database/readme.md)

### i2f-database-metadata-bean

> Java Bean ↔ 数据库元数据映射层：从 @Table/@Column/@Primary 等注解标注的 POJO 反射解析为 `TableMeta` 数据模型，并支持反向从 `TableMeta` 生成带注解的 Java Bean 源码。

- 详细文档：[i2f-database-metadata-bean](./i2f-jdk/i2f-database-metadata-bean/readme.md)

### i2f-database-metadata-data

> 数据库元数据数据模型与类型系统层：`TableMeta`/`ColumnMeta`/`IndexMeta`/`IndexColumnMeta` 四级 POJO + `StdType` 标准化类型枚举（34 常量）+ 方言类型映射接口。

- 详细文档：[i2f-database-metadata-data](./i2f-jdk/i2f-database-metadata-data/readme.md)

### i2f-database-metadata-std

> 数据库元数据标准契约层：定义 `DatabaseMetadataProvider`（JDBC 元数据读取器）与 `DatabaseReverseEngineer`（DDL 反向生成器）双接口，被多方言实现与逆向生成器消费。

- 详细文档：[i2f-database-metadata-std](./i2f-jdk/i2f-database-metadata-std/readme.md)

### i2f-database-metadata-impl

> 数据库元数据多方言实现层：以模板方法 + 委拖代理双架构提供 9 种方言的 JDBC 元数据读取器（MySQL/Oracle/DM/Gbase/H2/PostgreSQL/SQLite3/SQL Server/通用）+ 5 方言 DDL 反向生成器。

- 详细文档：[i2f-database-metadata-impl](./i2f-jdk/i2f-database-metadata-impl/readme.md)

### i2f-database-dialect

> SQL 字面量方言安全转换器：以模板方法 + 9 方言实现将 Java 对象（日期/数值/字符串/布尔）转换为各数据库方言兼容的 SQL 字面量字符串，被 `i2f-bindsql-stringify` 作为底层文本化引擎消费。

- 详细文档：[i2f-database-dialect](./i2f-jdk/i2f-database-dialect/readme.md)

### i2f-launcher

> 轻量级应用启动器 / 外置 classpath 加载器：`Main-Class` 指向 `ExtApplicationLauncher`，运行期读 `Ext-Main-Class`/`Ext-Path` 经 child-first classloader 加载外置目录与 jar 后反射调用真实入口，适配插件化与动态 SPI 加载。

- 详细文档：[i2f-launcher](./i2f-jdk/i2f-launcher/readme.md)

### i2f-log-std

> 日志标准门面层：统一大门面 `ILogger`（6 级别 × 多种入参形态数百 default 方法收敛到 2 个抽象 write）+ `LoggerFactory` 多路径解析可插拔 `LoggerProvider`（内置彩色 `StdioLogger` 兜底）。

- 详细文档：[i2f-log-std](./i2f-jdk/i2f-log-std/readme.md)

### i2f-log

> `i2f-log-std` 门面的默认完整实现层：全局/线程双路由组件注册表 `LogHolder` + ANSI 彩色格式化 + 多目标异步广播写出（控制台/本地滚动文件/JDBC 表）+ `System.out` 收编 + log.properties 一键装配。

- 详细文档：[i2f-log](./i2f-jdk/i2f-log/readme.md)

### i2f-jvm

> JVM 运行环境探测工具（单类 `JvmUtil` 约 100 行）：进程 PID/启动参数直通与 debug/-javaagent/verify 三大启动参数特征检测，无锁惰性缓存；被 9 模块消费（调试桥门控 14 处）。

- 详细文档：[i2f-jvm](./i2f-jdk/i2f-jvm/readme.md)

### i2f-lifecycle

> 统一生命周期契约模块（1 接口 + 1 异常约 50 行）：`ILifeCycle` 三动词 create/destroy/close（close 桥接 destroy 兼容 try-with-resources）+ `LifeCycleException` 失败信号。

- 详细文档：[i2f-lifecycle](./i2f-jdk/i2f-lifecycle/readme.md)

### i2f-limit

> 按 key 限流器模块：新线单动词 `Limiter.require` + 令牌桶 `TokenBucketLimiter`（按名分桶 + 守护线程补充/清理），旧线三动词 `IKeyedLimiter`（失败锁定 + 令牌补充两实现）。

- 详细文档：[i2f-limit](./i2f-jdk/i2f-limit/readme.md)

### i2f-lock

> 锁抽象契约模块（框架无关）：`ILock`/`INotify`/`INotifyLock`/`IReadWriteLock`/`ILockProvider` 五接口 + JDK 三实现线（ReentrantLock / 读写锁 / 按 key 懒建锁提供者）。

- 详细文档：[i2f-lock](./i2f-jdk/i2f-lock/readme.md)

### i2f-os

> 操作系统工具模块：`OsUtil` 跨平台命令执行四形态 + Windows PowerShell 封装 + `PerfUtil` 按平台分发 CPU/内存/磁盘采集（Linux top/free/df，Windows WMIC 映射 14 类 DTO）。

- 详细文档：[i2f-os](./i2f-jdk/i2f-os/readme.md)

### i2f-packet

> 二进制流封包协议模块（纯 JDK 零依赖）：`EE EE` 引导 + `EF` 转义的流式帧协议（多 head/多 body/tail 校验），`PacketProtocol` 映射 HTTP 风格头，512KB 阈值自适应内存/临时文件。

- 详细文档：[i2f-packet](./i2f-jdk/i2f-packet/readme.md)

### i2f-page

> 分页数据模型模块：`ApiOffsetSize`（偏移/大小/排他结束下标）+ `ApiPage`（零基页索引与一基页号转换）+ `Page<T>` 结果承载，构成 JDBC/MyBatis/ES/脚本统一分页契约。

- 详细文档：[i2f-page](./i2f-jdk/i2f-page/readme.md)

### i2f-pool

> 对象池与分段并发原语模块（纯 JDK）：队列缓存对象池 `ObjectPool` + 分段对象提供者 / 分段锁 / `String.intern` 分段同步族。

- 详细文档：[i2f-pool](./i2f-jdk/i2f-pool/readme.md)

### i2f-properties

> Properties 配置装载模块：`PropertiesUtil` 单门面提供「点分键树化 + 键风格归一 + Visitor 前缀定位 + RichConverter 强转」的「properties → 强类型 Bean」装载管线。

- 详细文档：[i2f-properties](./i2f-jdk/i2f-properties/readme.md)

### i2f-proxy-std

> 代理标准契约层：`IProxyHandler` 五阶段钩子（before 短路/after 改写/except 替换）+ 函数式 `IProxyInvocationHandler` + default 方法 MethodHandles 兼容，JDK/CGLIB/AspectJ 统一代理契约。

- 详细文档：[i2f-proxy-std](./i2f-jdk/i2f-proxy-std/readme.md)

### i2f-proxy

> JDK 动态代理实现模块：`JdkProxyUtil` 六重载门面（三契约 × 实例/接口双形态）+ normal/interfaces 双适配器 + 双 Provider + 五阶段解包骨架。

- 详细文档：[i2f-proxy](./i2f-jdk/i2f-proxy/readme.md)

### i2f-proxy-handlers

> 注解驱动的代理处理器模块：`@Lock`/`@Retry`/`@Validate` 三大横切语义落地为可直接挂动态代理的 `IProxyInvocationHandler`（含 15 校验注解族标签化递归校验）。

- 详细文档：[i2f-proxy-handlers](./i2f-jdk/i2f-proxy-handlers/readme.md)

### i2f-reference

> 值的三态引用包装（单类 183 行、零依赖）：VALUE/NOP/FINISH 三态显式区分「有值/值为 null/跳过/终止」四义，被 iterator/container 直接依赖并传递至 9 模块消费。

- 详细文档：[i2f-reference](./i2f-jdk/i2f-reference/readme.md)

### i2f-reflect

> 全仓反射能力中央底座：`ReflectResolver` 单类 3414 行七大能力族（类加载/字段方法发现/注解解析/调用匹配/值读写/Bean 复制/虚拟字段）+ RichConverter 泛型强转 + 点分路由 + 签名还原 + vistor 表达式引擎。

- 详细文档：[i2f-reflect](./i2f-jdk/i2f-reflect/readme.md)

### i2f-resources

> 「资源定位与类路径扫描」工具箱：`ResourceUtil` 六态位置协议解析 + `ResourcesLoader` 全类路径扫描引擎（目录递归/jar 流/嵌套 jar 解包/manifest 补全/前缀排除/最短收缩）。

- 详细文档：[i2f-resources](./i2f-jdk/i2f-resources/readme.md)

### i2f-resp

> 统一 API 响应契约模块：`ApiResp<T>`（code/msg/data + 惰性 kvs 扩展 + 链式与静态工厂）+ `ApiCode` 七码常量，被 spring-starter 默认装配为全局响应包装/异常转换/404 转换器。

- 详细文档：[i2f-resp](./i2f-jdk/i2f-resp/readme.md)

### i2f-robot

> AWT 桌面自动化工具（单类 `RobotUtil` 约 165 行、纯 JDK）：多屏枚举与主屏截屏/存图/取色 + 单键与组合键模拟 + 左中右键点击与插值拖拽。

- 详细文档：[i2f-robot](./i2f-jdk/i2f-robot/readme.md)

### i2f-rowset

> 表格行集流式读写契约：`IRowSet`（Iterator + Closeable）与 CSV 引号状态机读写（类型自动推断 + 长数字防科学计数），JSON 数组/JSONL 抽象写出注入 JSON 库。

- 详细文档：[i2f-rowset](./i2f-jdk/i2f-rowset/readme.md)

### i2f-script

> JSR-223 脚本引擎统一门面：`ScriptProvider` 聚合 ScriptEngine/Invocable/Compilable 三接口（compile/invoke 前置特性探测守卫）；唯一消费方 xproc4j 的 JavaScript 求值节点。

- 详细文档：[i2f-script](./i2f-jdk/i2f-script/readme.md)

### i2f-search

> 极简前缀索引树（Trie）双形态：`PrefixSearchTree<T,D>` 泛型键序列版 + `StringSearchTree<D>` String 特化版，基于 `ConcurrentSkipListMap` 的 add/find/prefix/collect/printTree。

- 详细文档：[i2f-search](./i2f-jdk/i2f-search/readme.md)

### i2f-serialize-impl

> 序列化契约的官方实现层：零第三方自研 JSON 引擎 `Json2`（17 处实例化为全仓默认引擎）+ JDK 原生字节序列化 + XML 只写生成 + 自描述文本格式。

- 详细文档：[i2f-serialize-impl](./i2f-jdk/i2f-serialize-impl/readme.md)

### i2f-serialize-std

> 序列化标准契约层：`ISerializer` 双动词统一「对象↔字节/文本」双通道九接口 + 类型化反序列化三级重载 + Base64 便捷 + 双向适配器；被 26 模块 77 文件消费。

- 详细文档：[i2f-serialize-std](./i2f-jdk/i2f-serialize-std/readme.md)

### i2f-sm-crypto

> 纯 Java 国密算法实现库：SM2（加解密/签名验签，C1C3C2 与 DER）/ SM3（杂凑与 HMAC）/ SM4（ECB/CBC）全自研三层结构，零密码学三方依赖；被 SWL 国密适配与 ops-starter 消费。

- 详细文档：[i2f-sm-crypto](./i2f-jdk/i2f-sm-crypto/readme.md)

### i2f-sm-crypto-swl

> SWL 安全传输协议族的纯 Java 国密适配层：以适配器模式把 i2f-sm-crypto 包装为 SWL 三大契约 + 3 个 Supplier 工厂，成为 SWL「JDK 默认 / 国密 / BouncyCastle」三套可互换实现中的国密一极。

- 详细文档：[i2f-sm-crypto-swl](./i2f-jdk/i2f-sm-crypto-swl/readme.md)

### i2f-std-const

> 全仓统一的「运行时目录约定」常量词汇表：以 `runtime` 为组合根派生 persist（跨次保留资产）与 tmp（可清理临时产物）两大类目录，被 14 模块 17 文件消费。

- 详细文档：[i2f-std-const](./i2f-jdk/i2f-std-const/readme.md)

### i2f-streaming

> 函数式流式处理框架（JDK8 从零实现的类 Stream 惰性管道、零依赖）：`Streaming<E>` 接口百余方法 + 唯一实现类，含四态引用协议、迭代器工具箱、并行模型、rich 上下文注入与元素窗口。

- 详细文档：[i2f-streaming](./i2f-jdk/i2f-streaming/readme.md)

### i2f-swl

> SWL（Secure Wire Layer）安全网络层的默认实现层与协议引擎：无状态 `SwlExchanger` 收发流水线（时间戳窗口→nonce 防重放→摘要→数字签名→双加密）+ `SwlTransfer` 会话 + 证书链 + JDK 四件密码学实现。

- 详细文档：[i2f-swl](./i2f-jdk/i2f-swl/readme.md)

### i2f-swl-std

> SWL 安全传输协议族的纯契约层 / 密码学 SPI 标准：4 能力接口（非对称/对称/摘要/混淆）+ 3 个 Supplier 类型别名 + 36 常量错误码枚举，为三套密码学实现提供依赖倒置契约。

- 详细文档：[i2f-swl-std](./i2f-jdk/i2f-swl-std/readme.md)

### i2f-template-render

> 基于正则表达式的轻量级文本模板渲染引擎（纯 JDK）：「控制表达式」两阶段渲染管线 + 11 种动作（for/fori/if/tpl/cmd 等）+ 上下文变量（item/root/ctx）+ 路由取值与 @方法转换链。

- 详细文档：[i2f-template-render](./i2f-jdk/i2f-template-render/readme.md)

### i2f-text

> 通用文本/字符串工具库（零三方依赖）：`StringUtils` 判空/清洗/命名风格转换 + `Appender` 泛型链式拼装 DSL + `CnNumber` 中文数字读法 + `Escapes` 转义 + RLE 压缩 + 星号脱敏。

- 详细文档：[i2f-text](./i2f-jdk/i2f-text/readme.md)

### i2f-thread

> 纯 JDK 并发编程工具箱：并行求值 `Asyncs`、跨线程消息等待、异步串行队列、CPU 反馈动态线程池、DAG 编排、命名线程工厂等一站式基础设施。

- 详细文档：[i2f-thread](./i2f-jdk/i2f-thread/readme.md)

### i2f-trace

> 调用点感知基础设施：基于线程栈回溯的 `ThreadTrace`（栈切片定位调用方 + 调用点类名/方法/文件/行号反射还原），被日志体系 3 模块消费。

- 详细文档：[i2f-trace](./i2f-jdk/i2f-trace/readme.md)

### i2f-trace-mdc

> MDC（Mapped Diagnostic Context）上下文容器与跨线程传播基础设施：`MdcHolder` 三级选择的存储门面 + 标准 traceId 键 + 完整线程传播装饰器族（Runnable/Callable/Executor/ThreadFactory）。

- 详细文档：[i2f-trace-mdc](./i2f-jdk/i2f-trace-mdc/readme.md)

### i2f-translate

> 翻译器系列的契约层 + 基础转换器：`ITranslator` 统一接口（extends ILifeCycle）+ 全角/半角双向映射表 + 声调→ASCII 表三组无状态转换器。

- 详细文档：[i2f-translate](./i2f-jdk/i2f-translate/readme.md)

### i2f-translate-en2zh

> 英文标识符→中文注释的「行业词典优先」翻译器：正则逐词替换 + 标识符启发式还原（缩写规范化/蛇形分段/后缀剥离）+ 内置 SQLite FTS3 词典（首用自动释放到 runtime/persist）。

- 详细文档：[i2f-translate-en2zh](./i2f-jdk/i2f-translate-en2zh/readme.md)

### i2f-translate-zh2pinyin

> 中文→拼音 / 简繁双向转换的零配置翻译器：逐字查表 + 声调双模式（保留声调符或转纯 ASCII）+ 内置 SQLite FTS3 词典（首用自动释放）+ 容量 LRU 缓存。

- 详细文档：[i2f-translate-zh2pinyin](./i2f-jdk/i2f-translate-zh2pinyin/readme.md)

## i2f-jdk-ext

> 基于 javax.servlet 规范的 Web 层扩展模块集合，提供安全过滤器矩阵、Web 防火墙、失败锁定守卫与 Servlet 上下文 / 文件 / 包装器基础设施。

### i2f-jdk-ext-web

> Servlet 层 Web 增强套件：过滤器矩阵（Security/Trace/耗时统计）+ Web 防火墙（字符 × 编码变体交叉断言）+ 失败锁定守卫 + Servlet 上下文/文件 Range 下载/包装器（body 可重复读）一站式横切能力层。

- 详细文档：[i2f-jdk-ext-web](./i2f-jdk-ext/i2f-jdk-ext-web/readme.md)

### i2f-jdk-ext-swl

> SWL 安全传输协议的 Servlet 过滤器接入层：请求侧「四头取值 + URL 防篡改校验 + 解密重建」，响应侧「全量缓冲 + 加密回写」，对业务透明的「请求解密 + 响应加密」适配底座。

- 详细文档：[i2f-jdk-ext-swl](./i2f-jdk-ext/i2f-jdk-ext-swl/readme.md)

### i2f-jdk-ext-all

> i2f-jdk-ext 子模块的 Maven 聚合分发包，将 `i2f-jdk-ext-swl` 与 `i2f-jdk-ext-web` 经 `maven-assembly-plugin` 打包为单体 fat-jar 的构建入口。

- 详细文档：[i2f-jdk-ext-all](./i2f-jdk-ext/i2f-jdk-ext-all/readme.md)

## i2f-spring

> Spring 生态集成模块集合，封装 Spring 核心、MVC、安全、Redis、Web 等能力的增强与元数据解析。

### i2f-spring-all

> i2f-spring 子模块的 Maven 聚合分发包（仅 1 个 pom.xml 共 64 行、无 Java 源码、无测试无资源无 SPI）：以 7 枚 compile 依赖聚合本组全部功能模块——authentication/core/mvc-metadata/redis/security/swl/web——依赖均不写版本（由根 DM 以 ${i2f.version} 锁定），并继承根 pom pluginManagement 的 maven-assembly-plugin（jar-with-dependencies 描述符、appendAssemblyId=false）产出可脱离父 POM 直接 -cp 部署的 fat-jar，自身 <build> 仅覆盖 addMavenDescriptor=true。与 i2f-jdk-ext-all/i2f-extension-all/i2f-jdk-all 同构的组级聚合分发件；子模块 Spring/servlet 多为 provided+optional 故容器依赖不随门面外泄。仓库内无任何源码级或 POM 级消费方，仅面向外部项目/手工部署作统一依赖入口。登记 spring-pom:17（<modules> 首个）/根DM1310-1314，bash 四目录 jar 全含 jdk17。核心静态缺陷：pom:15-18 保留被注释的自依赖块（误启用即成循环依赖构建失败）、门面硬编码全量 7 依赖无法按需裁剪、同时充当 thin 门面与 fat-jar 易致重复类双份定义、无 DM/exclusions 治理 fat-jar 合并可能同名资源覆盖、新增子模块漏登记进本件时构建静默缺件无校验。

- 详细文档：[i2f-spring-all](./i2f-spring/i2f-spring-all/readme.md)

### i2f-spring-authentication

> Spring Security / Shiro 认证结果的统一出口控制器（全模块仅 1 主源 SecurityForwardController 约 35 行、无测试无 SPI）：一个映射 /forward/response 的 @RestController，接收安全过滤器链内经 ServletContextUtil.forward(req,resp,FORWARD_PATH,ApiResp…) 转发的登录成功/失败/登出/鉴权异常结果，读 forward-data 属性、为 ApiResp 则原样透出、否则包 ApiResp.success，data 为空时回落 forward-exception 转 ApiResp.error(“internal error!”)，把跑在 DispatcherServlet 之外的认证 Handler 结果纳入统一 ApiResp JSON 序列化管线。路径与属性契约全部源自 i2f-jdk-ext-web.ServletContextUtil（经 i2f-spring-web 传递依赖）；真实消费方为 i2f-springboot-security-starter 与 shiro-starter 的两套认证 Handler。瑕疵：forward-exception 分支因消费方恒传 data 而几乎不可达（死代码）、ex.printStackTrace 绕过日志、@RequestMapping 未限定 HTTP method、lombok 声明冗余、零测试。

- 详细文档：[i2f-spring-authentication](./i2f-spring/i2f-spring-authentication/readme.md)

### i2f-spring-core

> Spring Framework 基础能力到 i2f-jdk 契约的桥接基座（22 主源约 1400 行、零单元测试、横跨 core/enviroment/event/matcher/param/resource/scanner/spel/tx/cglib 子包）：把 spring-core/context/tx（provided+optional 且版本走父 DM ${spring.version} 非硬编码）收敛为四类 i2f 契约实现——SpringContext→IWritableNamingContext、SpringEnvironment→IEnvironment、CglibProxyProvider→IProxyProvider、SpringAntPathMatcher→IPriorMatcher——并附 SpringUtil（Aware+CountDownLatch 容器门面）、EnvironmentUtil（属性前缀/分组提取）、EventManager/Event（应用事件）、TransactionUtil（手动事务 + ScheduledExecutor 超时自动提交）、SpelExpressionResolver、PackageScanner/ClasspathScanner/ResourceResolver/MatcherUtil/ParamNameResolver 等工具。i2f-spring 组依赖面最广的基座件，被 ai/ops/security/shiro/spring/swl/xproc4j/gateway 等 10+ starter 消费，核心装配方 SpringCoreAutoConfiguration 以 @Import 注入六类 Bean（i2f.spring.core.enable 默认 true）。bash 四目录 jar 齐全含 jdk17。高危静态缺陷：Event 构造函数 source=source 自赋值致 getSource 恒 null、EventManager.publish 无条件 context.publishEvent 双发布且 publisher-only 构造 NPE、TransactionUtil 超时定时器提前 commit 与任务自身二次提交冲突且 pool 不 shutdown、SpringUtil.getResource 绕过闩锁读裸字段、CglibProxyProvider.proxy 把 obj 强转 Class、DEFAULT_ENHANCER/ClasspathScanner.provider 共享 static 并发不安全、getGroupMapConfigs 无点 key substring 越界、数值 getter 空 catch 吞异常、包名 enviroment 拼写错误已固化、TestPackageScanner main 演示类混入 src/main。

- 详细文档：[i2f-spring-core](./i2f-spring/i2f-spring-core/readme.md)

### i2f-spring-mvc-metadata

> 基于反射解析 Spring MVC Controller 的 API 元数据，提取 URL、HTTP 方法、参数、返回值与 Swagger 注释，为 API 文档生成提供结构化数据。

- 详细文档：[i2f-spring-mvc-metadata](./i2f-spring/i2f-spring-mvc-metadata/readme.md)

### i2f-spring-redis

> IRedisClient 契约（i2f-extension-redis-api）的 Spring Data Redis 适配器（全模块仅 1 主源 SpringRedisClient 约 192 行、无测试无 SPI）：持有 RedisTemplate<String,Object> + 可选 prefix，wrapKey 统一收口键命名空间，把 21 个 String/List/Hash/TTL 契约方法一比一映射到 opsForValue/opsForList/opsForHash；spring-data-redis:2.3.5.RELEASE provided 但版本硬编码未走根 DM（与同组 spring-core 规范不一致）。真实消费方 i2f-springboot-redis-starter.RedisCacheConfiguration @ConditionalOnMissingBean 注册为 IRedisClient Bean，security/shiro starter 亦依赖。核心静态缺陷：hashGetAll 建空 ret 却从 map.put 回写源集从不填 ret → 恒返回空 map 且可能 CME；listSet 对 key 二次 wrapKey 读旧值取 prefix+prefix+key 恒 null；flushDb 取 RedisConnection 从不 close 泄漏且清空整库无视 prefix；del 先 get 后 delete 非原子、String.valueOf 对 Object 模板值类型不安全、keys 走阻塞 KEYS、lombok/i2f-cache 声明冗余。登记 spring-pom:21/根DM1330-1334/spring-all:33，bash 四目录 jar 全含 jdk17。

- 详细文档：[i2f-spring-redis](./i2f-spring/i2f-spring-redis/readme.md)

### i2f-spring-security

> Spring Security 认证上下文与密码编码的极薄工具模块（全模块仅 3 主源约 92 行、无测试无 SPI 无资源）：SecurityUtil 把 SecurityContextHolder 的取上下文/取认证/取 principal/取权限四步收敛为静态门面，SecurityCryptoUtil 提供默认 BCryptPasswordEncoder 单例与 bCryptEncode/bCryptMatch 便捷法及可传任意 PasswordEncoder 的泛化 encode/match，SpringPasswordEncoder 把 Spring PasswordEncoder 适配到 i2f-authentication 的 IPasswordEncoder 契约（覆写 matches 走 Spring 原生 matches 以规避接口默认 re-encode+equals 对随机盐恒 false 的陷阱）。spring-security-core/crypto 均 provided 且版本走父 i2f-spring pom 的 ${spring.security.version} DM 非硬编码（但未叠加 optional，与同组 spring-core 的 provided+optional 不一致）。真实消费方 i2f-springboot-security-starter 的 AuthenticationTokenFilter 调 SecurityUtil.getAuthentication()。核心静态缺陷：getPrincipal/getAuthorities 对未认证请求的 getAuthentication()==null 无兜底致 NPE 级联、getPrincipal 泛型 (T) 未检查强转、bCryptPasswordEncoder 为 public static 非 final 可被外部重赋值、工具类无私有构造、BC 静态实例与无参构造双路径冗余、零测试。登记 spring-pom:22/根DM1335-1339/spring-all:37，bash 四目录 jar 全含 jdk17。

- 详细文档：[i2f-spring-security](./i2f-spring/i2f-spring-security/readme.md)

### i2f-spring-swl

> SWL（Secure Web Layer）透明加解密的 Spring MVC 切面件（全模块仅 3 主源约 183 行、无测试无 SPI 无资源）：两个 @ControllerAdvice——SwlDecryptionRequestBodyAdvice extends RequestBodyAdviceAdapter（入站解密）与 SwlEncryptionResponseBodyAdvice implements ResponseBodyAdvice（出站加密），均 @Order(-1)，把 i2f-swl 的 SwlExchanger（RSA 加密随机对称密钥+AES 加密数据+SHA-256 签名+RSA 数字签名+时间戳/nonce 防重放）接到 MVC 消息读写管线，按控制器方法 @SwlCtrl(in/out) 逐方法开关，统一以 SwlData{header,parts,attaches,context} 信封收发、入站只取 parts[0]、String 返回型特判回串。spring 五件套+servlet-api 全 provided+optional 且版本走父 i2f-spring DM ${spring.version} 非硬编码（规范同 spring-core/web）。全模块无任何源码级消费方：i2f-springboot-swl-starter 用的是另一套 AOP/Filter 实现（i2f.springboot.swl.spring 包）并不依赖本模块，本件走 @ControllerAdvice 组件扫描路线需宿主显式扫 i2f.spring.swl.advice 才生效。核心静态缺陷：两 Advice 字段与 getServerCert 逐字重复且各 new 全套协作者无 @Autowired（SwlExchanger 各开关恒默认）、@Data 加在 Advice 上 toString 可能牵出证书状态、SwlAdviceConfig 各持独立实例改一处不影响另一处且无外部化、loadServer 返 null 后 cert.getCertId NPE、beforeBodyRead 反序列化无空/类型校验且 getParts().get(0) 空列表越界多 part 丢失、RequestContextHolder 强转 ServletRequestAttributes 非 servlet 线程 NPE、@SwlCtrl 类级不生效、i2f-jdk-ext-web 冗余依赖、bash 仅 3/4 目录有 jar 缺 backup-jdk8。

- 详细文档：[i2f-spring-swl](./i2f-spring/i2f-spring-swl/readme.md)

### i2f-spring-web

> Spring Web MVC / WebFlux 能力工具箱 + i2f-network REST 契约的 RestTemplate 落地（13 主源约 1250 行、无测试无 SPI，横跨 mvc/mapping/file/proxy/rest/webflux/wrapper 七子包）：SpringMvcUtil（RequestContextHolder + 下沉 i2f-jdk-ext-web.ServletContextUtil 的取 req/resp/session/cookie/forward/redirect/respJson 静态门面）与 WebfluxContextUtil（响应式 ServerHttpRequest 取 token/cookie/ip）双栈覆盖，MappingUtil 为 RequestMappingHandlerMapping 建 path→HandlerMethod 索引供网关/运维反查 Controller 方法，HttpFileUtil 桥接 ServletFileUtil 断点续传/流式下载，HttpProxyHandler+HttpProxyFilter 是前缀映射 + 属性打标记防重入的轻量反向代理，SpringWebRestClient/SpringWebHttpProcessor/SpringWebAutoHttpRequestBodyHandler 把 i2f-network 的 IRestClient/IHttpProcessor 抽象契约落地到 RestTemplate.exchange/execute，三个 *MultipartFile（临时文件/byte[]/File）适配 Spring MultipartFile。spring-core/context/web/webmvc/webflux + javax.servlet-api 全 provided+optional 且版本走父 i2f-spring pom DM ${spring.version} 非硬编码（与同组 spring-core 一致的规范做法）。真实消费方 i2f-springboot-http-proxy-starter（@ConfigurationProperties("i2f.http.proxy") 装配代理）与 ai-mcp-client（new SpringWebRestClient 作 MCP REST 通道）。核心静态缺陷：SpringMvcUtil.include 误调 forward、FileMultipartFile.isEmpty 返回 file.exists() 语义反转、HttpProxyHandler.proxy 泄漏 ClientHttpResponse 且原样转发含 Host 的全量 header、静态 proxy 与实例 pathMapping 割裂且用废弃 HttpMethod.resolve、三 wrapper copy 缓冲 4086(应4096) 且 while len>0、MappingUtil fastMapping 对模板路径不命中恒回落全扫、@Data 破坏适配器封装、SpringWebRestClient params 一律拼 URL query、WebfluxContextUtil.getHeaders 缺 header new ArrayList(null) NPE、多处 printStackTrace、零测试。登记 spring-pom:24/spring-all:45/authentication 依赖本模块/根DM1345-1349，bash 四目录 jar 全含 jdk17。

- 详细文档：[i2f-spring-web](./i2f-spring/i2f-spring-web/readme.md)

## i2f-extension

> 可选扩展能力集合，按需集成第三方库与增强组件（7z 压缩、AI、文档、数据库反向工程、文件系统、序列化、ANTLR4 语言引擎、Java Agent 字节码增强观测等）。

### i2f-extension-7zip

> 基于 SevenZipJBinding 的 7z 归档压缩器实现：`SevenZCompressor` 落地 `i2f-compress-std` 的 `ICompressor` 契约，复用 `AbsCompressor` 骨架把目录树打包为 `.7z`（可选密码、头加密）或解包回目录。

- 详细文档：[i2f-extension-7zip](./i2f-extension/i2f-extension-7zip/readme.md)

### i2f-extension-ai-dashscope

> 阿里云百炼（DashScope）官方 SDK 的 `i2f-ai-std` 契约实现：对话（qwen-plus）/文本向量化/重排序三组契约 + function-calling 自循环 + `@Tool` 注解桥接；SDK 以 provided + optional 引入。

- 详细文档：[i2f-extension-ai-dashscope](./i2f-extension/i2f-extension-ai-dashscope/readme.md)

### i2f-extension-ai-langchain4j8

> 面向 Java 8 的 langchain4j 0.31.0 版 `i2f-ai-std` 契约实现：对话/模型/RAG 三件套复用 langchain4j 原生类型对接任意 OpenAI 兼容端点（默认百炼 qwen-plus）+ function-calling 自循环；provided + optional。

- 详细文档：[i2f-extension-ai-langchain4j8](./i2f-extension/i2f-extension-ai-langchain4j8/readme.md)

### i2f-extension-ai-openai

> 官方 OpenAI Java SDK 4.28.0 的 `i2f-ai-std` 契约实现：对话/模型/向量化三组契约对接 OpenAI 与任意兼容端点 + function-calling 自循环 + `@Tool` 注解桥接；provided + optional。

- 详细文档：[i2f-extension-ai-openai](./i2f-extension/i2f-extension-ai-openai/readme.md)

### i2f-extension-ai-rag-sqlite

> sqlite-vec 原生扩展版的 RAG 存储层实现：单库 `SqliteRagEmbeddingStore` + 分桶记忆 `BucketRagEmbeddingStore` 双契约落地本地向量库（vec0 虚拟表 + KNN 检索），原生扩展自动释放。

- 详细文档：[i2f-extension-ai-rag-sqlite](./i2f-extension/i2f-extension-ai-rag-sqlite/readme.md)

### i2f-extension-all

> i2f-extension 子模块的 Maven 聚合分发包（仅 1 个 pom.xml 共 359 行、无 Java 源码、无测试无资源无 SPI）：以 81 枚 compile 依赖「一处声明、整组引入」聚合本组除自身外全部功能模块（7zip/ai-*/antlr4-*/compress/groovy/jackson/mongodb/mybatis/netty/redis-*/velocity*/zookeeper 等，含末位追加的 freemarker/xproc4j，无漏件），依赖均不写版本（由根 DM 以 ${i2f.version} 锁定），并全继承根 pom pluginManagement 的 maven-assembly-plugin（jar-with-dependencies、appendAssemblyId=false）产 fat-jar——本模块 <build> 连 addMavenDescriptor 覆盖都没有，比 i2f-spring-all 更彻底全继承。是全仓库聚合规模最大的组级门面，与 i2f-jdk-all/i2f-jdk-ext-all/i2f-spring-all 同构。子模块对第三方普遍 provided（多未 optional）故容器/三方库不随门面 Maven 传递，代价是 fat-jar 非自足可运行包。仓库内无任何源码级或 POM 级消费方，仅面向外部/手工部署。核心静态缺陷：pom.xml:39-42 残留被注释的自依赖块（误启用即循环依赖构建失败）、门面硬编码全量 81 依赖无法按需裁剪、thin 门面与 fat-jar 双身份易致重复类、无 DM/exclusions 治理 81 模块合并时同名 META-INF/services 后者胜（本组多 SPI 尤险）、新增子模块漏登记时静默缺件无校验。

- 详细文档：[i2f-extension-all](./i2f-extension/i2f-extension-all/readme.md)

### i2f-extension-antlr4

> ANTLR4 语言族（calculator + tinyscript + funic）的 Maven 聚合分发模块，将三个子模块及传递依赖打包为单体 fat-jar，是 xproc4j 系的统一依赖入口。

- 详细文档：[i2f-extension-antlr4](./i2f-extension/i2f-extension-antlr4/readme.md)

### i2f-extension-antlr4-calculator

> 基于 ANTLR4 的计算器表达式解析与求值模块：设计为 `Calculator.eval(String)` → BigDecimal 一步式求值（算术 + 多参数函数调用）；主源码与文法文件缺失，当前为不可构建的占位模块。

- 详细文档：[i2f-extension-antlr4-calculator](./i2f-extension/i2f-extension-antlr4-calculator/readme.md)

### i2f-extension-antlr4-funic

> 基于 ANTLR4 的「脚本引擎 + 模板引擎」双语言模块（Funic + Funvi）：静态门面 + 语法树缓存 + 可扩展 Resolver + 值对象体系 + 沙箱/代理双安全方案；被 xproc4j 与 IDEA 插件消费。

- 详细文档：[i2f-extension-antlr4-funic](./i2f-extension/i2f-extension-antlr4-funic/readme.md)

### i2f-extension-antlr4-tinyscript

> 基于 ANTLR4 的嵌入式迷你脚本语言引擎（TinyScript）：`TinyScript.script()` 静态门面 + 语法树缓存 + Resolver 扩展点 + 内建方法注册表；被 xproc4j 与 IDEA 插件消费。

- 详细文档：[i2f-extension-antlr4-tinyscript](./i2f-extension/i2f-extension-antlr4-tinyscript/readme.md)

### i2f-extension-aspectj

> AspectJ 连接点与 i2f-proxy 统一代理模型之间的桥接层：把 `@Around` 切面的 `ProceedingJoinPoint` 经 JDK 动态代理包装为编程式执行点（可读方法/改写参数/短路返回）；aspectjweaver 以 provided 引入。

- 详细文档：[i2f-extension-aspectj](./i2f-extension/i2f-extension-aspectj/readme.md)

### i2f-extension-asr-vosk

> 基于 Vosk 的本地离线语音识别门面：`AsrVoskProvider` 把「模型获取→释放到 runtime/persist→初始化→WAV 解码→分帧识别」收敛为 4 个静态方法；vosk/jna 以 provided 引入。

- 详细文档：[i2f-extension-asr-vosk](./i2f-extension/i2f-extension-asr-vosk/readme.md)

### i2f-extension-browser-playwright

> 基于 Microsoft Playwright 的浏览器自动化搜索抓取扩展：驱动工厂/四层持有者/网络屏蔽与 DOM 清洗 + 五大搜索引擎同构实现（阶段队列爬行 + 配额截断）；playwright 以 provided + optional 引入。

- 详细文档：[i2f-extension-browser-playwright](./i2f-extension/i2f-extension-browser-playwright/readme.md)

### i2f-extension-browser-selenium

> 基于 Selenium WebDriver 的浏览器自动化搜索抓取扩展：Edge/Chrome 双引擎驱动工厂（内置 driver 释放 + 版本不匹配 CDN 下载）+ CDP 网络拦截/DOM 清洗 + 五大搜索引擎同构搜索类。

- 详细文档：[i2f-extension-browser-selenium](./i2f-extension/i2f-extension-browser-selenium/readme.md)

### i2f-extension-canal

> 基于 Alibaba Canal 的 MySQL Binlog 订阅消费模板：`CanalClient` 模板方法封装「连接→订阅→轮询→解析→分发」骨架，子类只需重写 insert/delete/update 三个钩子；canal 依赖 provided + optional。

- 详细文档：[i2f-extension-canal](./i2f-extension/i2f-extension-canal/readme.md)

### i2f-extension-cglib

> 基于 CGLIB 字节码增强的 `IProxyProvider` 代理实现：`CglibUtil` 六重载门面 + Adapter 桥接 `IProxyInvocationHandler`；cglib 以 provided + optional 引入，与 JDK 动态代理/AspectJ 共享统一契约。

- 详细文档：[i2f-extension-cglib](./i2f-extension/i2f-extension-cglib/readme.md)

### i2f-extension-compress

> 基于 Apache Commons Compress 的归档压缩适配层：ZIP/JAR/TAR/CPIO/7Z 五种格式的 `ICompressor` 桥接实现（继承 `AbsCompressor` 骨架免费获得组合能力）。

- 详细文档：[i2f-extension-compress](./i2f-extension/i2f-extension-compress/readme.md)

### i2f-extension-cron

> 基于 cron-utils 的嵌入式单机 CRON 任务调度器：`CronUtil` 三方言解析与执行时间计算 + `CronExecutor` 三池分离调度（扫描/触发/执行）；cron-utils 以 provided + optional 引入。

- 详细文档：[i2f-extension-cron](./i2f-extension/i2f-extension-cron/readme.md)

### i2f-extension-document

> 基于 Aspose 与 PDFBox 生态的办公文档格式转换与处理套件：Word/Excel 20+ 格式互转 + Velocity 模板渲染导出 + documents4j COM 转换（仅 Windows）+ PDFBox 文本提取与逐页 PNG 渲染。

- 详细文档：[i2f-extension-document](./i2f-extension/i2f-extension-document/readme.md)

### i2f-extension-easyexcel

> 基于 Alibaba EasyExcel 的 Excel 导入/导出工具套件：导入导出双门面 + 循环分页引擎（自动分 sheet）+ 7 种数据提供器策略 + `@ExcelCellStyle` 注解驱动样式 + 转换器注册中心。

- 详细文档：[i2f-extension-easyexcel](./i2f-extension/i2f-extension-easyexcel/readme.md)

### i2f-extension-elasticsearch

> 基于 ES RestHighLevelClient 7.6.2 的索引/文档/查询操作封装套件：`EsManager` 三层门面（客户端/索引/CRUD/批量/搜索）+ `EsBeanManager` 注解映射 + SQL-like 查询构建器 + Spring Data 桥接。

- 详细文档：[i2f-extension-elasticsearch](./i2f-extension/i2f-extension-elasticsearch/readme.md)

### i2f-extension-email

> 基于 JavaMail 的邮件发送工具门面：`directSend` 一键发信与分步装配 + 流式 Builder 配置（预设 20+ 邮箱服务商 SMTP 常量）+ SSL/认证/多收件人 + 纯文本/HTML + 多附件。

- 详细文档：[i2f-extension-email](./i2f-extension/i2f-extension-email/readme.md)

### i2f-extension-fastexcel

> 基于 cn.idev.excel（fastexcel 1.2.0）的 Excel 导入/导出工具套件：与 i2f-extension-easyexcel 同构架构、底层引擎切换为 fastexcel（分页引擎/提供器策略/注解样式/转换器注册中心）。

- 详细文档：[i2f-extension-fastexcel](./i2f-extension/i2f-extension-fastexcel/readme.md)

### i2f-extension-fastjson

> 基于 fastjson1 兼容包（`com.alibaba:fastjson:2.0.26`，实由 fastjson2 引擎驱动）的 `IJsonSerializer` 契约适配器：三路类型化反序列化分派 + `INSTANCE` 单例；与 fastjson2 模块为可替换双实现。

- 详细文档：[i2f-extension-fastjson](./i2f-extension/i2f-extension-fastjson/readme.md)

### i2f-extension-fastjson2

> 基于原生 fastjson2（2.0.34）的 `IJsonSerializer` 契约适配器：三路类型化反序列化分派 + `INSTANCE` 单例；与 fastjson1 兼容包模块为可替换双实现，面向新项目优先推荐。

- 详细文档：[i2f-extension-fastjson2](./i2f-extension/i2f-extension-fastjson2/readme.md)

### i2f-extension-filesystem-ftp

> 基于 Apache Commons Net FTPClient 的 `IFileSystem` 契约适配器：把 FTP 协议装配为统一文件系统契约（11 操作原语 + 继承 40+ 组合能力），默认每操作新建连接规避流传输应答残留。

- 详细文档：[i2f-extension-filesystem-ftp](./i2f-extension/i2f-extension-filesystem-ftp/readme.md)

### i2f-extension-filesystem-hdfs

> 基于 Apache Hadoop FileSystem 客户端（hadoop-client 3.2.1）的 `IFileSystem` 契约适配器：薄封装直通 HDFS（三态元信息/三路流/递归 mkdir），注意 Hadoop 全局静态缓存按 scheme+authority+user 跨实例共享。

- 详细文档：[i2f-extension-filesystem-hdfs](./i2f-extension/i2f-extension-filesystem-hdfs/readme.md)

### i2f-extension-filesystem-minio

> 基于 MinIO Java SDK 的 `IFileSystem` 契约适配器：MinIO/S3 对象存储装配为统一契约（桶/键两级拆分 + `.ignore` 占位模拟目录）；写通道需用 `getOutputStream` 规避 `store()` 已知缺陷。

- 详细文档：[i2f-extension-filesystem-minio](./i2f-extension/i2f-extension-filesystem-minio/readme.md)

### i2f-extension-filesystem-oss-aliyun

> 基于阿里云 OSS Java SDK 的 `IFileSystem` 契约适配器：桶/键两级拆分 + 同桶 `moveTo` 走服务端 rename；写通道全可用（未知长度流自动 chunked 传输）。

- 详细文档：[i2f-extension-filesystem-oss-aliyun](./i2f-extension/i2f-extension-filesystem-oss-aliyun/readme.md)

### i2f-extension-filesystem-oss-aws-s3

> 基于 AWS SDK v2 S3 的 `IFileSystem` 契约适配器：桶/键两级拆分 + 服务端 copyObject，签名全程 AWS4-HMAC-SHA256；`store()` 已知失败，写通道需用 `getOutputStream`。

- 详细文档：[i2f-extension-filesystem-oss-aws-s3](./i2f-extension/i2f-extension-filesystem-oss-aws-s3/readme.md)

### i2f-extension-filesystem-sftp

> 基于 JSch 的 `IFileSystem` 契约适配器：SFTP 装配为统一契约（14 覆写方法 + 继承 40+ 组合能力），`ProxySftpFileSystem` 经 SSH 端口转发实现「跳板机 → 目标机」两跳接入。

- 详细文档：[i2f-extension-filesystem-sftp](./i2f-extension/i2f-extension-filesystem-sftp/readme.md)

### i2f-extension-ftp

> 独立 FTP 操作工具类 `FtpUtil`（Apache Commons Net `FTPClient`，provided）：链式封装登录/递归建目录/上传/下载/删除/列举，单连接会话 + 完整操作式应答消费；`IFtpMeta`/`FtpMeta` 配置契约被 `i2f-extension-sftp` 继承复用。注意 `login()` 正负应答判断反转（登录成功反而抛异常），使用前需先修复。

- 详细文档：[i2f-extension-ftp](./i2f-extension/i2f-extension-ftp/readme.md)

### i2f-extension-gif

> 极简 GIF 动图编解码工具（`animated-gif-lib`，provided）：`Gif`/`GifFrame` 帧模型 + `GifUtil.save`/`load` 静态门面，帧序列经等比缩放居中合成统一画布（`Rgba` 颜色键透明底）后编码为 GIF，或把 GIF 解码回帧序列（含帧延迟与循环次数）。

- 详细文档：[i2f-extension-gif](./i2f-extension/i2f-extension-gif/readme.md)

### i2f-extension-groovy

> Groovy 脚本执行工具（Apache Groovy 4，provided）：`GroovyScript.eval`（LruMap 缓存脚本 Class）与 `evalScript`（即时解析）双轨求值，`delegating` 子包经 Groovy MOP 把脚本方法调用路由到 Java 类/对象/方法 provider；被 xproc4j 脚本节点与 ops-starter 在线求值消费。

- 详细文档：[i2f-extension-groovy](./i2f-extension/i2f-extension-groovy/readme.md)

### i2f-extension-gson

> 基于 Google Gson（2.10.1，provided）的 `IJsonSerializer` 契约适配器：`GsonJsonSerializer` 把 `toJson`/`fromJson` 装配为契约实现，支持 `Class`/`Type`/`TypeToken` 类型化反序列化与 `bean2Map`/`deserializeAsMap`；可经注入构造器携带定制 `Gson`，与 fastjson/fastjson2/jackson 为可替换多实现。

- 详细文档：[i2f-extension-gson](./i2f-extension/i2f-extension-gson/readme.md)

### i2f-extension-hazelcast

> Hazelcast 分布式缓存适配器（Hazelcast 5.3，provided）：`HazelcastCache` 把分布式并发 `IMap` 装配为 `IExpireContainerCache` 契约实现——泛型 K/V 直存（原生 Java 序列化、零编码器）+ TTL 原子写入，构造器兼容 client/member 双形态；`HazelcastUtil` 提供智能客户端快速创建。与 redis-cache、zookeeper 同属 cache 契约远程实现族。

- 详细文档：[i2f-extension-hazelcast](./i2f-extension/i2f-extension-hazelcast/readme.md)

### i2f-extension-hdfs

> HDFS 操作与 Hadoop 上下文工具（hadoop-client 3.2.1，provided）：`HdfsUtil` 以 `IHdfsMeta` 四要素契约双检锁懒加载 `FileSystem`，链式封装建目录/上传/下载/删除/列举；`HadoopContext` 提供 `Configuration` 定制钩子与 `JobBuilder` 链式构建 MapReduce 作业。与 `i2f-extension-filesystem-hdfs` 构成同协议双轨。

- 详细文档：[i2f-extension-hdfs](./i2f-extension/i2f-extension-hdfs/readme.md)

### i2f-extension-httpclient

> Apache HttpClient 4.x 的 `IHttpProcessor` 传输层绑定（httpclient/httpmime provided）：`HttpClientHttpProcessor` 按 Content-Type 分派 JSON/XML/Form/Multipart 请求体处理器（Bean 经反射展开），逐请求装配超时与重定向；`HttpClientUtil` 提供全局静态单例切换点。与 JDK 原生、okhttp、spring-web 实现可互换。

- 详细文档：[i2f-extension-httpclient](./i2f-extension/i2f-extension-httpclient/readme.md)

### i2f-extension-image-metadata

> 图像元数据读取工具（metadata-extractor 2.18.0，provided）：`ImageFileMetadata` 单静态门面把图片文件解析为 `Metadata`（Directory × Tag 树形），支持 JPEG/PNG/GIF/TIFF/WebP 等格式的 EXIF、GPS、XMP、尺寸等元数据读取；需 XMP 支持时还应引入传递依赖 xmpcore。

- 详细文档：[i2f-extension-image-metadata](./i2f-extension/i2f-extension-image-metadata/readme.md)

### i2f-extension-jackson

> Jackson 多格式序列化适配器与扩展件集合（jackson 2.13.5，provided）：`AbsJacksonSerializer` 基座派生 8 个格式适配器（JSON/XML/YAML/CBOR/CSV/Protobuf/Smile + 类型保留 JSON），datetime 六处理器、`@Sensible` 注解式数据脱敏（双注册表 + 字典翻译扩展）、`@Long2String` 精度保护；有专属 SpringBoot 脱敏 starter，全仓消费最广的序列化适配模块。

- 详细文档：[i2f-extension-jackson](./i2f-extension/i2f-extension-jackson/readme.md)

### i2f-extension-javassist

> Javassist 字节码工具（javassist 3.28.0-GA，provided）：`JavassistUtil` 两个静态原语——`isAssignableFrom`（CtClass 递归超类/接口链的全限定名类型判断）与 `getAllMethods`（含父类/接口的全量方法枚举，Map 保留声明类）；是 `i2f-extension-agent-javassist` 六个 transformer 的字节码工具底座。

- 详细文档：[i2f-extension-javassist](./i2f-extension/i2f-extension-javassist/readme.md)

### i2f-extension-jce-bc

> BouncyCastle 加密增强适配器（bcprov 1.74，provided）：`Bc*` 四件套继承 `i2f-crypto-impl` 基类、把 provider 切换为 `BC`，配套 8 个 BC 版算法枚举，解锁国密 SM4、SHA3/SHAKE、Tiger 等算法；`BcSm2Encryptor` 走 BC 轻量 API 实现 SM2 加密签名一体。是 `i2f-extension-swl` 与 `i2f-tools-encrypt` 的 BC 引擎底座。

- 详细文档：[i2f-extension-jce-bc](./i2f-extension/i2f-extension-jce-bc/readme.md)

### i2f-extension-jce-sm-antherd

> antherd 国密适配器（sm-crypto 0.3.2.1，provided）：Nashorn 执行内嵌 JS 实现 SM2/SM3/SM4，`Sm2Encryptor`/`Sm4Encryptor`/`Sm3Digester` 等四件套适配 `i2f-crypto-std` 契约，密钥密文统一 hex；JDK15+ 需另补 nashorn-core。`i2f-extension-swl` 与 `i2f-tools-encrypt` 的国密引擎。

- 详细文档：[i2f-extension-jce-sm-antherd](./i2f-extension/i2f-extension-jce-sm-antherd/readme.md)

### i2f-extension-jdbc-procedure-datax

> `i2f-jdbc-procedure` 的 DataX 节点扩展（零三方 Maven 依赖）：`DataxExecNode` 经 SPI 注册为 `<datax-exec>` 标签，把内联/变量 JSON 作业写入临时文件后以外部进程调用 `datax.py` 做异构数据批量 ETL，可选捕获标准输出回写上下文、`await` 控阻塞；与姊妹 `jdbc-procedure-flink`（JVM 内流批计算）构成数据工程双轨。

- 详细文档：[i2f-extension-jdbc-procedure-datax](./i2f-extension/i2f-extension-jdbc-procedure-datax/readme.md)

### i2f-extension-jdbc-procedure-flink

> `i2f-jdbc-procedure` 的 Flink 节点扩展：8 个 `flink-*` 标签经 SPI 注册，把「建环境→执行/查询 FlinkSQL→转 Changelog 流→打印→提交作业」拆为可编排节点，Flink 对象经上下文变量流转；`org.apache.flink:*` 全 `provided`。与姊妹 `jdbc-procedure-datax`（外部进程批量同步）构成 JVM 内流批计算双轨。

- 详细文档：[i2f-extension-jdbc-procedure-flink](./i2f-extension/i2f-extension-jdbc-procedure-flink/readme.md)

### i2f-extension-reverse-engineer-generator

> 数据库反向工程与代码生成模块，基于 Velocity 模板从表结构或 Spring MVC 元数据生成分层代码、DDL、设计文档与 ER 图。

- 详细文档：[i2f-extension-reverse-engineer-generator](./i2f-extension/i2f-extension-reverse-engineer-generator/readme.md)

### i2f-extension-freemarker

> 基于 FreeMarker 2.3.34（provided）的模板渲染与代码生成工具：三源渲染（字符串/文件系统/类路径）+ 整目录批量生成（`#filename` 动态输出名）+ 模板内 `_vm` 工具对象（混入 500+ 函数 + Stringifier SPI）。

- 详细文档：[i2f-extension-freemarker](./i2f-extension/i2f-extension-freemarker/readme.md)

### i2f-extension-agent-javassist

> 基于 Java Instrumentation Agent + Javassist 的运行期字节码增强观测套件：`-javaagent`/动态附加无侵入打点，覆盖 JDBC/文件/RMI/异常/退出/进程等事件监听 + Spring 上下文捕获 + traceId 编织 + 活体 REPL。

- 详细文档：[i2f-extension-agent-javassist](./i2f-extension/i2f-extension-agent-javassist/readme.md)

### i2f-extension-log-slf4j

> `i2f-log` 的 SLF4J 输出桥接：实现 `LogWriterProvider` SPI，探测到 `org.slf4j.Logger` 即挂载 `LogSlf4jLogWriter`，把 i2f 的 `LogData` 转投 SLF4J 后端，并在 `loaded()` 摘除 STDOUT 完成接管；`slf4j-api`/`i2f-log` 全 `provided`。与反向 `slf4j-log`（SLF4J→i2f）方向相反，不可共存否则成环。

- 详细文档：[i2f-extension-log-slf4j](./i2f-extension/i2f-extension-log-slf4j/readme.md)

### i2f-extension-minio

> MinIO SDK（`io.minio:minio:7.1.0`，provided）的零内部依赖薄封装：`MinioMeta` 承载 url/ak/sk 连接配置，`MinioUtil` 包装 `MinioClient` 提供桶/对象上传下载/前缀/预签名 URL 便捷方法并将受检异常降级为 `IOException`。作 `filesystem-minio` 适配层与 oss-minio starter 的连接底座（实际仅复用构造器与 `getClient`）。

- 详细文档：[i2f-extension-minio](./i2f-extension/i2f-extension-minio/readme.md)

### i2f-extension-mongodb

> MongoDB 旧版统一驱动（`mongo-java-driver:3.10.2`，provided）的 insert-only 薄门面：`MongoDbMeta` 承载 host/port/source/账号连接配置，`MongoDbUtil` 有状态持有 client/库/集合，链式「选库→选集合→插入(Document/Map/Bean/批量)」，Bean 经内部依赖 `i2f-reflect` 摊平为文档。仓库内无消费方；固定 PLAIN 认证、单点、无 close 为主要隐患。

- 详细文档：[i2f-extension-mongodb](./i2f-extension/i2f-extension-mongodb/readme.md)

### i2f-extension-mybatis

> MyBatis 桥接扩展层（MyBatis 经 `mybatis-spring-boot-starter:3.0.3` 以 provided 引入）：以「`@Intercepts` 空壳拦截器 + 适配器 + `IProxyInvocationHandler`」范式实现三条 ThreadLocal 驱动的功能线——Executor 物理分页（count 与分页 SQL 改写委托 i2f-bindsql-page 按方言执行）、SQL 执行记录（参数合并为可读 SQL，支持 Oracle TO_DATE 字面量）、结果集列元数据捕获（代理 Statement 抓取 ResultSetMetaData）；另含脱离 SqlSessionFactory 的动态 script 直连执行器、String↔LOB TypeHandler 三件与 OGNL 求值辅助，唯一源码消费方为 i2f-springboot-mybatis-starter。

- 详细文档：[i2f-extension-mybatis](./i2f-extension/i2f-extension-mybatis/readme.md)

### i2f-extension-netty

> Netty 桥接扩展层（netty-all:4.1.65.Final 以 provided 引入）：三条独立能力线——注解驱动 HTTP 服务器（@NettyController 扫描路由 + URL/表单/JSON 三源参数绑定）、自定义 TCP 二进制协议栈（16 字节头 0xface1024 协议，拆包编解码 + 心跳/ECHO/BROADCAST flag 语义 + ISocketChannelHandler 扩展钩子）、同步风格 TCP-RPC（JDK 动态代理 + seqId→DefaultPromise + JSON 传输 + 服务端按类型查 bean 反射调用）；另含流式分片重组 NettyMessageCodec（未默认装配）与字符串 echo 工具，仓库内无源码级消费方（仅 i2f-extension-all 聚合）。

- 详细文档：[i2f-extension-netty](./i2f-extension/i2f-extension-netty/readme.md)

### i2f-extension-ocr-tesseract

> Tesseract OCR 桥接扩展（tess4j:4.5.1 以 provided 引入）：单类 OcrTesseractProvider 全静态 API，recognize 三入口（InputStream/BufferedImage/File）识别；训练数据采用自备引导约定——datapath 固定 ./runtime/persist/tesseract/ocr，首次调用目录不存在则 mkdirs 后抛引导异常提示从 tesseract-ocr/tessdata 下载 *.traineddata；未实现任何 OCR 契约接口，仓库内无源码级消费方（仅 i2f-extension-all 聚合）。

- 详细文档：[i2f-extension-ocr-tesseract](./i2f-extension/i2f-extension-ocr-tesseract/readme.md)

### i2f-extension-ognl

> OGNL 表达式桥接扩展（ognl:3.4.11 以 provided+optional 引入）：2 源文件极小模块——OgnlUtil 提供 LruMap(4096) 表达式 AST 树缓存 + evaluateExpression 求值（root 双通道：setRoot + #$root 变量），DefaultMemberAccess 提供默认全放行（private/protected/package）的 MemberAccess 单例；是 i2f 数据访问 DSL 的表达式引擎底座，消费方含 i2f-extension-xproc4j（存储过程脚本求值/预载/语法报告）、i2f-jdbc-proxy-xml（mapper XML OGNL inflate）与 i2f-springboot-jdbc-bql-starter（@ConditionalOnClass 条件装配退避），与 i2f-extension-mybatis 内嵌 shaded ognl 包为同源分叉。

- 详细文档：[i2f-extension-ognl](./i2f-extension/i2f-extension-ognl/readme.md)

### i2f-extension-okhttp

> OkHttp HTTP 客户端桥接扩展（okhttp:4.9.3 以 provided 引入）：i2f-network 的 IHttpProcessor 统一 HTTP 契约在 OkHttp 上的适配实现——OkHttpHttpProcessor 按 Content-Type 分派 6 个 RequestBodyHandler（表单/JSON/XML/Multipart/RawBytes/RawInputStream，序列化器与 OkHttpClient 均可注入），响应以 HttpResponse + OkHttpCloser 双模释放；仓库内无源码级消费方（仅 i2f-extension-all 聚合），与 i2f-network 的 JDK 版实现平行可互换。

- 详细文档：[i2f-extension-okhttp](./i2f-extension/i2f-extension-okhttp/readme.md)

### i2f-extension-opencv

> 原生 OpenCV 4.3.0 桥接扩展（system scope 指向 resources/lib/opencv-430.jar）：静态块自引导（init 释放 DLL 提示 + loadNative System.load ./runtime/persist/opencv 下的 opencv_java430_x64/x86.dll，DLL 需用户自备）+ 全静态门面——detectFrontFace/detectFullBody/detectEye/detectMultiScale（CascadeClassifier + opencv-data 的 Haar 级联，GUI 预览与标注保存）与 findContours（Sobel+二值化流水线）；核心静态缺陷：system 依赖不传递不入 fat jar 且 opencv-430.jar 成为不可加载的 jar-in-jar（独立运行产物 org.opencv 缺失）、DLL 释放链空转且 null 流 is.close() NPE、DLL 引导目录判定被 opencv-data cascade 释放连带短路；仓库内无源码级消费方（仅 i2f-extension-all 聚合）。

- 详细文档：[i2f-extension-opencv](./i2f-extension/i2f-extension-opencv/readme.md)

### i2f-extension-opencv-data

> OpenCV 训练数据资源模块：单类 OpenCvDataFileProvider（26 行）+ 35 个 OpenCV 官方级联分类器 XML（约 25MB，Haar CPU/CUDA + HOG + LBP），无 OpenCV 运行时依赖；唯一职责是把 classpath lib/data/** 幂等释放到 ./runtime/persist/opencv/data 并返回 File（FileUtil.getClasspathExtraFile 契约，存在即复用），供 CascadeClassifier 加载；是 i2f-extension-opencv 与 i2f-extension-opencv-javacv 的共享数据底座，ROOT_PATH 常量兼作 OpenCvProvider 的 DLL 释放目录。核心静态缺陷：资源缺失静默黑洞（null 流静默返回不存在的 File）、首次释放非原子残缺永久化、cascade 释放短路 OpenCvProvider DLL 引导（exists 判定被连带目录欺骗）。

- 详细文档：[i2f-extension-opencv-data](./i2f-extension/i2f-extension-opencv-data/readme.md)

### i2f-extension-opencv-javacv

> JavaCV 桥接人脸识别扩展（javacv/javacv-platform:1.5.9 双 provided、裁剪 11 项无关 platform 组件留 OpenCV 线，绑定 OpenCV 4.7.0）：单主类 OpenCvFaceRecognizer 封装「目录约定训练 + LBPH 识别」门面——子目录名即标签、文件即样本、non- 前缀即负样本；模型三件套持久化（*.xml 模型 + *.label.txt 标签 + *.properties 预处理参数）；预处理链 imread→灰度→detectMultiScale 取第一个脸 ROI→resize 256×256；置信度判定为应用层后置过滤（confidenceLimit<=40 且 >-0.5，LBPH setThreshold 未用）。级联 XML 来自 i2f-extension-opencv-data；唯一源码级消费方 i2f-tools-face-recognizer（可执行工具壳，把本模块测试类原样复制进自身 main 源集）。核心静态缺陷：标签文件名双分支单复数不一致、test() 未守卫 -1 标签越界、无脸静默整图参与识别、重复 train 覆盖模型但标签累积错位、构造即 IO 的字段初始化器。

- 详细文档：[i2f-extension-opencv-javacv](./i2f-extension/i2f-extension-opencv-javacv/readme.md)

### i2f-extension-oss-aliyun

> 阿里云 OSS 对象存储桥接扩展（aliyun-sdk-oss:3.17.4 以 provided+optional 引入，附带 jaxb/activation java9+ 补偿三件）：2 类轻量适配——AliyunOssMeta 配置元数据（url/region/AK/SK/signVersion，无效签名版本静默回落默认 V4）+ AliyunOssUtil 全实例门面（桶管理、上传三形态/下载/元信息、prefix 0 字节对象模拟目录、presigned URL，SDK 异常统一包装 IOException）；与 i2f-extension-oss-aws-s3 平行同构无共享契约；唯一源码级消费方 i2f-extension-filesystem-oss-aliyun（AbsFileSystem 适配层，构造即 getClient）。核心静态缺陷：getClient 构建的 ClientBuilderConfiguration 从未传入 builder 链（javap 证实 fluent API 有 clientConfiguration(...) 而代码未调用）——签名版本配置整体架空走 SDK 默认签名，缺陷经构造注入传播到 filesystem 适配层；prefixExists 语义错位（精确 key 而非前缀）、upload 的 -1 哨兵泄漏到 setContentLength。

- 详细文档：[i2f-extension-oss-aliyun](./i2f-extension/i2f-extension-oss-aliyun/readme.md)

### i2f-extension-oss-aws-s3

> AWS S3 对象存储桥接扩展（AWS SDK v2 s3/kms/s3control 以模块内硬编码 bom:2.17.100 配 provided+optional 引入，kms/s3control 源码零引用冗余）：与 i2f-extension-oss-aliyun 平行同构的 2 类轻量适配——AwsS3OssMeta 配置元数据（url/AK/SK/region 纯 POJO）+ AwsS3OssUtil 实例门面（S3Client+S3Presigner 双客户端、桶管理、上传三形态/下载/元信息、prefix 0 字节对象模拟目录、marker 分页列举、presigned URL，SDK 异常统一包装 IOException）；唯一源码级消费方 i2f-extension-filesystem-oss-aws-s3（仅静态复用 getClient，其余操作自行手写并复制了本模块三类缺陷模式）。核心静态缺陷（javap 铁证）：bucketExists 误用 getBucketPolicyStatus 判存在（未配 BPA 的桶恒 false → create 必重复建桶）、presigner 丢 endpointOverride（Builder 有该方法未调，第三方 S3 预签名 URL 指向 amazonaws.com）、fromInputStream(is,-1L) 哨兵无负数校验产出 Content-Length: -1、urlOf 999 年时长超 SigV4 上限 7 天、getObjectInfo 流不关闭连接泄漏。

- 详细文档：[i2f-extension-oss-aws-s3](./i2f-extension/i2f-extension-oss-aws-s3/readme.md)

### i2f-extension-qrcode

> 二维码桥接扩展（zxing core/javase:3.4.1 双 provided 引入，模块内硬编码版本）：仓库最小扩展模块之一（2 文件约 180 行）——QrCodeWorker 链式封装「zxing 生成 + 2D 贴 Logo + zxing 解码」（QR_CODE + ErrorCorrectionLevel.H 约三成面积容错为 Logo 预留、圆角描边、logo 空/缺失静默降级纯码），QrCodeUtil 全静态门面（生成 4 重载 / 解析 2 重载，输出硬编码 JPG）；解码走 LuminanceSource→HybridBinarizer→MultiFormatReader 标准链。仓库内无源码级消费方（仅 i2f-extension-all 聚合）。核心静态缺陷：Logo 缩放逻辑自我矛盾（clamp 判断成死代码 + getScaledInstance 强制非等比拉伸正方形失真）、getScaledInstance 第三参 TYPE_INT_RGB magic number 值恰等于 SCALE_DEFAULT 语义侥幸、ImageIO.read 返 null 未判致 NPE、logoSize/codeSize 无比例校验超容错面积码不可扫无提示、JPG 有损压缩输出伤扫描率、setCharset(null) Hashtable NPE。

- 详细文档：[i2f-extension-qrcode](./i2f-extension/i2f-extension-qrcode/readme.md)

### i2f-extension-quartz

> Quartz 调度桥接扩展（quartz:2.3.2 以 provided 引入，模块内硬编码版本）：双层结构——QuartzUtil 全静态门面（Scheduler 获取、JobDetail/Simple 间隔与 Cron Trigger 构建、schedule/reschedule/pause/resume/runOnce/delete 生命周期）+ 注解驱动线（@QuartzSchedule 方法注解 → QuartzScanner classpath 扫描 → QuartzJobMeta 双轨元数据 → 统一注册为固定 Job 类 QuartzAnnotationJob，meta 经 JobDataMap "meta" 传递，执行时反射回调，static 直调、非 static 优先 invokeObj 否则每次 newInstance）；唯一源码级消费方 i2f-springboot-quartz-starter（容器刷新事件触发扫描、getBeansOfType 单命中注入 bean 作 invokeObj）。核心静态缺陷：热更新路径仅 reschedule Trigger 而丢弃新 JobDetail（注解方法绑定变更不生效）、QuartzJobMeta 含 Method/Class 字段 JDBC JobStore 下不可序列化、重载方法按名解析错位、带参注解方法注册期无校验运行时炸。

- 详细文档：[i2f-extension-quartz](./i2f-extension/i2f-extension-quartz/readme.md)

### i2f-extension-redis-api

> Redis 统一客户端契约模块（纯 SPI，零 Redis 客户端依赖，pom 仅 lombok 且源码零使用冗余）：单接口 IRedisClient（72 行 23 方法，Spring Data Redis 风格签名，string/list/hash 三组 + 过期管理）；是 i2f Redis 体系的 SPI 中枢——双实现 i2f-extension-jedis 的 JedisRedisClient（JedisPool 自管）与 i2f-spring-redis 的 SpringRedisClient（RedisTemplate 门面），双消费 i2f-extension-redis-cache 的 RedisCache（桥接 i2f-cache-std 三契约）与 i2f-springboot-redis-starter（@ConditionalOnMissingBean 可替换装配）。核心静态缺陷：接口层零语义文档（23 方法仅 2 个 javadoc，SETNX 语义/负数超时哨兵/del 返回旧值/push 方向全缺位）已实际引致双实现 6 方法行为分歧——listPush Jedis 左推 vs Spring 右推顺序相反、setUnique Jedis setnx+expire 两步（分布式锁死锁缺陷）vs Spring 单条原子、hashSet/listSet 返回值各异（Spring 版 listSet 还双重包 prefix 读错键、hashGetAll 向遍历 map put 恒返空）、RedisCache.clean 直接 flushDb 清空整库、Spring 版 flushDb 裸取连接泄漏。

- 详细文档：[i2f-extension-redis-api](./i2f-extension/i2f-extension-redis-api/readme.md)

### i2f-extension-redis-cache

> Redis 缓存适配扩展（单类 RedisCache 92 行，依赖 redis-api + i2f-cache 传递的 cache-std + lombok 冗余）：把 IRedisClient string 型 KV 向上适配为 i2f-cache-std 三契约——IExpireContainerCache（容器+过期组合）、IPersistCache、IDistributedCache（空标记接口）；核心机制 prefix 键空间隔离（wrapKey）+ 可注入编解码器（Function 对，装配方注入 Jackson2JsonRedisSerializer）；是 i2f Redis 体系「缓存语义层」——starter RedisCacheConfiguration 自动装配 + security/shiro starter 两个 TokenHolder 窄化为 IExpireCache 存 30 分钟 token 会话。核心静态缺陷：clean() 直接 flushDb 清空整个 DB（prefix 隔离失效，数据灾难级）；keys() 返回带 prefix 存储键致 default forEach/remove 组合二次包装删错键静默失效；KEYS 阻塞命令；(int)unit.toSeconds 溢出压精度、-1 哨兵靠巧合命中；encoder/decoder null 语义双层缺位（starter encoder 无 null 判空 set(null) 必炸、new String UTF-8 往返损毁二进制序列化器数据）。

- 详细文档：[i2f-extension-redis-cache](./i2f-extension/i2f-extension-redis-cache/readme.md)

### i2f-extension-sftp

> SFTP/SSH 桥接扩展（jsch:0.1.55 以 provided 引入，模块内硬编码版本且上游已停更）：三条能力线——basic 线 SftpUtil 单会话 ChannelSftp 门面（登录/递归建目录/上传下载/删除/列举 + ChannelShell/ChannelExec 直取 + 异步 exec）、proxy 线 ProxySftpUtil 经代理机 setPortForwardingL 两级会话的跳板 SFTP、tunnel 线 SshTunnelUtil 通用 SSH 本地端口正向隧道（daemon keepalive 重连 + shutdown hook 清理，可给 MySQL/Redis 等 TCP 服务套跳板）；配置契约继承 i2f-extension-ftp 的 FtpMeta（Commons Net provided 不传递）；源码级消费方为 i2f-springboot-ops-starter（ssh 文件管理/exec 控制台）与 i2f-springboot-ssh-tunnel-starter（环境准备期自动建隧道），i2f-extension-filesystem-sftp 为平行独立实现不消费本模块。核心静态缺陷：login 硬编码 PreferredAuthentications=password 使纯私钥认证必然失效、recursiveDelete 对普通文件双重路径拼接致 SSH_FX_NO_SUCH_FILE 被吞而静默无操作、exec 只读 stdout 不消费 stderr 撑满窗口即挂死且超时后半挂线程泄漏、无连接超时无限阻塞、SshTunnelUtil shutdown hook 与 keepalive 锁竞争可挂死 JVM 退出、starter 创建的隧道实例从未加入 SshTunnelManager.servers。

- 详细文档：[i2f-extension-sftp](./i2f-extension/i2f-extension-sftp/readme.md)

### i2f-extension-slf4j

> SLF4J 桥接增强扩展（slf4j-api:1.7.36 以 provided 引入，版本根 POM 属性供给）：4 源文件三件工具——PerfLogger lambda 延迟求值门面（isXxxEnabled 预检查，5 级 × Supplier/异常/1~3 参/varargs 60+ 重载）、Slf4jPrintStream 控制台重定向（System.out/err 全方法代理进 slf4j，幂等重定向 + 可选堆栈定位 logger 名 + THREAD_CONSUMER 钩子）、Slf4jUtil 栈扫描调用点感知 logger；另含 Slf4jMdcManager——i2f-trace-mdc 的 MdcManager SPI 实现（META-INF/services 注册，MdcHolder 按名字优先自动选中），是 trace-mdc 全链路 MDC 传播的运行时底座；源码级消费方 i2f-springboot-spring-starter（启动期重定向）与 i2f-springboot-trace-mdc-starter（装配），另有 maven-project/tools-ops 两份包名分叉副本。核心静态缺陷：重定向先于日志系统初始化时 ConsoleAppender 捕获包装流可能无限递归（logbackEnv 防线探测的是 Spring Boot 专有属性不可靠）、write(byte[]) 数据黑洞/日志丢失、useTrace 每条日志 getStackTrace + 行级唯一 logger 名致 LoggerFactory 注册表无限增长、PerfLogger 异常/值版重载仅边界不同致异常堆栈静默丢失风险、MdcManager 契约零 javadoc 且 copyOf 空上下文返 null。

- 详细文档：[i2f-extension-slf4j](./i2f-extension/i2f-extension-slf4j/readme.md)

### i2f-extension-slf4j-log

> SLF4J 绑定器扩展（slf4j-api:${slf4j.version}=1.7.36 以 provided+optional 引入，DM 重复声明）：SLF4J 1.x StaticLoggerBinder 绑定协议接到 i2f-log 的反向桥（与 i2f-extension-slf4j 方向相反）——org.slf4j.impl 包 5 个绑定器标准类（StaticLoggerBinder/StaticMarkerBinder/StaticMDCBinder + SimpleMarkerFactory + SimpleMdcAdapter，Marker/MDC 为 logback 同名类手写简化复制）+ i2f 包 2 个适配器（Slf4jLogLoggerAdapter 375 行全覆写 SLF4J Logger 转发 ILogger、FactoryAdapter LruMap(1024) 缓存）；落地链 LoggerProvider SPI（系统属性→log.properties→ServiceLoader）→ StdioLogger 兜底（ANSI 彩色 System.out/err）；仓库内无源码级消费方（仅 i2f-extension-all 聚合，聚合连带存在多绑定竞争风险）。核心静态缺陷：SLF4J {} 占位符语义在 i2f-log 侧完全失效（LogUtil.formatMsg 走 printf % 语义，{} 原样 + 参数 [0](Type)val 尾巴追加；消息偶发 % 被 String.format 误解析）、异常混入 varargs 丢堆栈、SLF4J 2.x 断代（REQUESTED_API_VERSION=1.7.30 与 pom 1.7.36 双源漂移）、冷启动竞态固化 StdioLogger（i2f-log-std 侧）。

- 详细文档：[i2f-extension-slf4j-log](./i2f-extension/i2f-extension-slf4j-log/readme.md)

### i2f-extension-sqlparser

> SQL 解析桥接扩展（JSqlParser:4.9 以 provided+optional 引入，版本模块内硬编码）：仓库最小扩展模块之一（单类单方法约 53 行），`SqlParserUtil.wrapAsCountSql(sql)` 把查询 SQL 改写为 count(1) 计数 SQL——快路径用 JSqlParser 将 PlainSelect 查询列替换为 `count(1) cnt` 并剥离 order by（保持单层、效率高），解析失败（如 MyBatis `#{}` 占位符）或非 PlainSelect 则降级为子查询包装 `select count(1) cnt from (sql) tmp_count`；解析器关闭 complexParsing/unsupportedStatements/errorRecovery 三关，支持 `?` 不支持 `#{}`。零 i2f 内部依赖、仓库内无源码级消费方（仅 extension-all 聚合；ops-starter 引用的是 JSqlParser 自身 CCJSqlParserUtil，同名易混）。核心静态缺陷：`catch(ParseException){}` 静默吞异常且仅捕获该型致 NoClassDefFoundError 穿透降级承诺失效、非查询语句被误包装为非法 count SQL、替换列丢弃 DISTINCT 且保留 GROUP BY 致计数语义错误、复杂查询普遍降级使快路径名不副实、以 Column 承载 `count(1)` 属脆弱用法。

- 详细文档：[i2f-extension-sqlparser](./i2f-extension/i2f-extension-sqlparser/readme.md)

### i2f-extension-swl

> SWL 安全传输协议密码学引擎扩展（BouncyCastle:1.74 + sm-crypto:0.3.2.1 双 provided 硬编码版本）：为 i2f-swl-std 三大 SPI 接口提供 BC 六件套（RSA2048/AES256/SHA512/SM2/SM3/SM4 + 6 Supplier）与 Antherd 国密三件套（SM2/SM3/SM4 + 3 Supplier）共 18 主源文件约 900 行，含 9 个对等 Supplier 工厂供 SwlTransfer ObjectPool 池化；BC 路线以 Base64（国际算法）或 Hex（国密算法）编码密钥密文、统一 SwlException+SwlCode 包装异常，Antherd 路线直接委托 String-in-String-out 透传；内部依赖 i2f-swl-std（契约）+ i2f-code（AES 密钥字符）+ i2f-extension-jce-bc + i2f-extension-jce-sm-antherd（底层引擎）。下游 springboot/gateway 两个 swl-starter 以 POM compile 引入运行时 Spring 配置切换引擎，仓库内无其他源码级 import。核心静态缺陷：BC 对称加密器 6 处方法 catch 全用 ASYMMETRIC_ENCRYPT_EXCEPTION(1100) 而非 SYMMETRIC_* 码（复制粘贴）、AES-256 generateKey 走 CodeUtil.makeCheckCode(32) 有效熵仅约 190bit 不及 256 名义且 getKey 用 UTF8.encode 任意字节转 String 丢失密钥、Antherd 三件套 digest/verify/encrypt/decrypt 无 try-catch 破坏统一异常契约、SwlBcSm4SymmetricEncryptor 用 JDK 默认 Provider 的 SymmetricEncryptor 而非 BcSymmetricEncryptor 致 SM4 算法可能找不到、4 个测试引用已废弃 API 无法编译。

- 详细文档：[i2f-extension-swl](./i2f-extension/i2f-extension-swl/readme.md)

### i2f-extension-tokenlization-ansj

> 中文分词扩展（ansj_seg:5.1.6 单三方 provided+optional 硬编码版本，零 i2f 内部依赖）：仓库四个并列分词桥接模块（ansj/hanlp/jcseg/jieba）之一，单类 AnsjTokenlizer 约 24 行、两静态方法——tokenlize(text) 委托 ToAnalysis.parse().getTerms() 透传带词性的 Term 列表，tokenlizeSplit(text) 取 Term::getName 展平为词串。桥接面收窄到 ToAnalysis 简分词，未暴露词性标注/自定义词典等其它 Ansj 分析器；与兄弟模块方法同名但返回类型互异（各自引擎原生 Term/IWord/SegToken），无公共接口、引擎不可运行时切换。仅 extension-all 聚合，仓库内无源码级消费方，测试为 main 方法跑地址分词样例。核心静态缺陷：tokenlization/Tokenlizer 系统性拼写错误已固化进坐标包名、throws Exception 过宽（底层不抛受检异常）、null 入参直穿 NPE 无防护、split 结果不过滤标点停用词、lombok 零使用冗余依赖、版本未纳根 DM、无 JUnit 断言测试。

- 详细文档：[i2f-extension-tokenlization-ansj](./i2f-extension/i2f-extension-tokenlization-ansj/readme.md)

### i2f-extension-tokenlization-hanlp

> 中文分词扩展（hanlp:portable-1.8.2 单三方 provided+optional 硬编码版本，零 i2f 内部依赖）：四个并列分词桥接模块（ansj/hanlp/jcseg/jieba）中功能最全者，单类 HanlpTokenlizer 约 59 行，以类内 Mode 枚举（STANDARD/NLP/SPEED/NOTIONAL/INDEX/TRA_CN）+ if-else 链把 HanLP 六套静态分词器收拢到 tokenlize/tokenlizeSplit 两组重载（无 Mode 参默认 STANDARD，带 Mode 参分派对应器，末尾兜底降级 STANDARD），Split 取 Term::word 展平；透传 HanLP 原生 Term 不做适配。仅 extension-all 聚合，仓库内无源码级消费方，测试为 main 方法仅跑 STANDARD。核心静态缺陷：tokenlization/Tokenlizer 全组系统性拼写错误、throws Exception 过宽（底层不抛受检异常）、null/未匹配 Mode 静默降级 STANDARD 掩盖选择失效、if-else 链无穷尽检查、NOTIONAL 等 Mode 在 portable 版可能缺数据运行期异常、无 null 防护、split 不过滤标点停用词、lombok 零使用冗余、版本未纳根 DM、无 JUnit 且 6 Mode 仅测 1。

- 详细文档：[i2f-extension-tokenlization-hanlp](./i2f-extension/i2f-extension-tokenlization-hanlp/readme.md)

### i2f-extension-tokenlization-jcseg

> 中文分词扩展（jcseg-core:2.5.0 单三方 provided+optional 硬编码版本，零 i2f 内部依赖）：四个并列分词桥接模块（ansj/hanlp/jcseg/jieba）之一，单类 JcsegTokenlizer 约 47 行，以两个 public static 共享字段 config(JcsegTaskConfig)+dic(ADictionary 类加载时建默认词典) 复用词典，tokenlize(mode,text) 每次 createJcseg 新建 ISegment + while(next()) 手动收集 IWord 列表，默认 COMPLEX_MODE（检索型含复合词），Split 取 getValue 展平。仅 extension-all 聚合，仓库内无源码级消费方，测试为 main 方法仅跑默认模式。核心静态缺陷：config/dic 是 public static 非 final 可变共享字段（破坏封装且 ISegment 非线程安全，并发读改存数据竞争）、每次新建 ISegment 对象抖动无池化、throws Exception 过宽（底层仅 JcsegException）、text.length() 当初始容量且 null 直穿 NPE、COMPLEX_MODE 输出复合冗余词不过滤、tokenlization/Tokenlizer 全组拼写错误、lombok 零使用冗余、版本未纳根 DM、int mode 常量与 hanlp 枚举风格不一、无 JUnit。

- 详细文档：[i2f-extension-tokenlization-jcseg](./i2f-extension/i2f-extension-tokenlization-jcseg/readme.md)

### i2f-extension-tokenlization-jieba

> 中文分词扩展（jieba-analysis:1.0.2 单三方 provided+optional 硬编码版本，零 i2f 内部依赖）：四个并列分词桥接模块（ansj/hanlp/jcseg/jieba）中最后一个，单类 JiebaTokenlizer 约 34 行三静态方法——tokenlize(text) 默认以 SegMode.SEARCH 委托 tokenlize(mode,text) 调 process 返回带词性偏移的 SegToken，tokenlizeSplit(text) 另走 sentenceProcess 按句切分返回展平词串。仅 extension-all 普通 compile 聚合，仓库内无源码级消费方，测试为 main 方法跑地址分词样例。核心静态缺陷：getSegmenter() 每次 new JiebaSegmenter 触发全量词典重复加载（四兄弟中最严重性能缺陷，jcseg 至少 static dic 复用）、tokenlize/tokenlizeSplit 两路径算法不对称（process vs sentenceProcess）却命名对称易误导、throws Exception 过宽（底层不抛受检异常）、无 null 防护直穿 NPE、SEARCH 模式输出冗余细粒度子词、与兄弟无公共接口引擎不可运行时切换且返回类型互异（SegToken/Term/IWord）、SegToken 原生类型泄漏、tokenlization/Tokenlizer 全组系统性拼写错误、版本未纳根 DM、lombok 零使用冗余、无 JUnit 断言。

- 详细文档：[i2f-extension-tokenlization-jieba](./i2f-extension/i2f-extension-tokenlization-jieba/readme.md)

### i2f-extension-tts-espeak

> 文本转语音（TTS）扩展（不同于分词四兄弟封装 Maven 三方库的范式，本模块走原生二进制内嵌+类加载自解压+外部进程调用路线）：将 espeak 命令行引擎（windows/espeak.zip 含 espeak.exe+espeak-data）作为 classpath 资源打进 jar，TtsEspeakProvider static 块触发 init() 以 AtomicBoolean getAndSet+目录 exists() 双幂等将 zip 解压至 {RUNTIME_PERSIST_DIR}/espeak/windows，再由 text2speech(String[,wavFile]) 写临时 txt 后 Runtime.exec 调 espeak.exe --path -b 1 -v zh -f（可选 -w 产 wav 再 move）合成。仅支持 Windows、中文语音 -v zh 硬编码、引擎不走 Maven 而是随 jar 打包的二进制。内部仅依赖 i2f-std-const（运行时目录常量）+ i2f-io-file（文件工具，并借其传递获得 i2f-io-stream 的 StreamUtil）。仅 extension-all 聚合、无源码级消费方、未出现在 bash 四目录分发 jar 中（推测因含大体积原生二进制被脚本排除）。核心静态缺陷：windows/espeak.zip 不在仓库源码树（缺失时 getResourceAsStream 返回 null、ZipInputStream 抛 NPE 被 static 块吞后 initialed 已置 true 致后续静默失败）、maven-jar-plugin 仅 include windows/**/* 可能排除 .class、initialed getAndSet 一旦置位异常不重试且 exists() 无法修复半成品目录、Runtime.exec(String) 单串含空格路径分词隐患、waitFor 不校验 exitValue 不消费 stdout/stderr 输出缓冲满可阻塞、临时 wav 无 finally 兜底、平台/语音硬编码不可配置、直接播放强依赖音频设备、StreamUtil 依赖传递未声明、throws Exception 过宽、lombok 零使用、无 JUnit。

- 详细文档：[i2f-extension-tts-espeak](./i2f-extension/i2f-extension-tts-espeak/readme.md)

### i2f-extension-tts-jacob

> 文本转语音（TTS）扩展（与 tts-espeak 外置引擎命令行路线并列，本模块走 JACOB Java-COM Bridge + Windows SAPI Sapi.SpVoice 进程内 COM 桥接路线）：把 JACOB 桥接 jar（lib/jacob.jar，system scope 且随资源打包）与 jacob-1.21-x64/x86.dll 两份本地库内置进 jar，TtsJacobProvider static 块触发 init() 无条件写 LibraryLoader.JACOB_DLL_PATH 系统属性后以 AtomicBoolean getAndSet+目录 exists() 双幂等把首选位数 DLL 解压至 {RUNTIME_PERSIST_DIR}/jacob，再由 text2speech(String) 实例化 Sapi.SpVoice setProperty Volume80/Rate2 后 Dispatch.call Speak 本地播放、text2speech(String,File) 再接 SpFileStream+SpAudioFormat(Type22) 重定向 AudioOutputStream 落盘 wav。仅 Windows、参数全硬编码、双位数 DLL 择一。内部仅依赖 i2f-std-const（目录常量）+ i2f-io-file（FileUtil.save 落 DLL，借其传递 StreamUtil）。仅 extension-all 聚合，仓库内无源码级消费方，未纳入 bash 分发 jar。核心静态缺陷：com.jacob 用 system scope 不随 Maven 传递、内嵌 lib/jacob.jar 作嵌套资源又不会自动上运行时 classpath 极易 NoClassDefFoundError、同一 jacob.jar 既是 systemPath 编译目标又打进 jar 双重身份、getResourceAsStream 返 null 时 FileUtil.save 静默返回后 is.close() 立即 NPE 且无 try-with-resources、initialed 一旦置位异常不重试且 exists() 无法修复半成品/损坏 DLL 目录、setProperty 早于存在性判断、text2speech(File) 复用同一 ax 变量指向三 ActiveXComponent 释放归属易混且无 try-finally 泄漏 COM 句柄、Volume/Rate/Type22/Variant3 魔法数不可配、throws Exception 过宽（实为 ComFailException RuntimeException）、System.out 代替日志、OFFICIAL_URL 死常量、测试文案从 espeak 复制未改且非 JUnit、${pom.basedir} 已废弃。

- 详细文档：[i2f-extension-tts-jacob](./i2f-extension/i2f-extension-tts-jacob/readme.md)

### i2f-extension-velocity

> Velocity 模板渲染与代码生成扩展（velocity-engine-core:2.3 单三方 provided 硬编码版本未走根 DM）：与 i2f-extension-freemarker 为同一设计在两种引擎上的同构兄弟，但显著更重——除共有的 VelocityGenerator 三源渲染门面（字符串/文件/classpath，字符串走 StringResourceLoader+UUID 键+finally 回收、config 空复用 DEFAULT_STRING_ENGINE 双检锁单例）+ batchRender 整目录镜像生成（#filename 动态输出名）+ _vm 工具对象（GeneratorTool implements AllMixins）+ Stringifier 责任链 SPI 外，独有 7 自定义指令：#trim（前后缀剥离+条件附加，变长参 0~4）、#sqlWhere/#sqlSet（trim 的 SQL 硬编码特例）、#richFor（Iterable/Iterator/Enumeration/Map/Array 五型迭代+$index+嵌套堆栈备份还原）、#fori（initState=begin<end 支持正反向）、#replaceAll（正则整体替换）、#script（body 交 VelocityScriptProvider 三级查找 THREAD_PROVIDERS→静态→ServiceLoader 求值）。12 主源文件约 1400 行。内部依赖 i2f-io-stream/typeof/os/serialize-impl/mixins/text 六模块，且仓库内有真实源码级消费方（document 渲染 Word XML/reverse-engineer-generator/xproc4j/velocity-bindsql 均 compile），是扩展组少见的被广泛复用模板底座，bash 四目录 jar 均在册。核心静态缺陷：测试 GeneratorTool.readFile 以静态调 AllMixins default 实例方法不编译且路径 i2f-velocity 陈旧失配、ObjectConvertor 属 i2f-convert 未声明靠传递、i2f-text 声明却无直接 import 疑似冗余、sqlWhere/sqlSet 与 trim 逻辑逐行拷贝未抽取、前/后缀各只剥首个命中 break 易误用、文件渲染与带 config 渲染每次新建引擎无缓存致批量 loadDirective 开销放大、GeneratorTool.fori 用 i!=end 保留死循环（与已修正的 #fori 指令语义不一致）、cmd 吞异常不校验 exitValue 不消费 stdout/stderr、#script THREAD_PROVIDERS 无写入入口近死路径、#replaceAll 正则强转+ReDoS、OUTPUT_ENCODING 注释未启用、#script+cmd 构成 RCE 安全面。

- 详细文档：[i2f-extension-velocity](./i2f-extension/i2f-extension-velocity/readme.md)

### i2f-extension-velocity-bindsql

> Velocity 模板 + BindSql 桥接扩展（velocity-engine-core:2.3 单三方 provided 硬编码版本未走根 DM，与 velocity 兄弟重复声明）：把 i2f-extension-velocity 模板渲染与 i2f-bindsql 参数化模型对接，实现 MyBatis 风格 XML Mapper → 参数化 BindSql（? 占位 + args）动态 SQL 引擎。核心 VelocitySqlGenerator.renderSql 向模板前缀注入 #macro(sql $value)$__sql.wrap($value)#end、把 ValueWrapper 以 __sql 键塞入 params，模板 #sql($val) 调 wrap 登记 ${n}=值并回吐 ${n} 字面量，渲染后 RegexUtil.replace(\$\{\d+\}) 逐个改写为 ? 按序收集 args 产 new BindSql；VelocityResourceSqlTemplateResolver 解析 <mapper class> 下 query/update/call/sql 节点按 class.method 或全限定 method 归建 ConcurrentHashMap 缓存支持 refreshResources 热重载，类型映射 query→QUERY/update→UPDATE/call→CALL/sql→UNSET。3 主源约 239 行，内部依赖 velocity(底座+指令)/bindsql/match/xml/io-stream。仓库内真实 Spring Boot 消费方：jdbc-bql-starter 以 @ConditionalOnClass 条件装配 VelocityProxyRenderSqlProvider 接入 JDK 动态代理 Mapper，bash 四目录 jar 均在册。核心静态缺陷：renderSql 就地 params.put(__sql) 改入参违反洁癖契约循环复用污染、BindSql.Type 渲染后丢失(resolver 解析类型被 new BindSql 默认 UNSET 吞掉)、占位正则 \$\{\d+\} 过宽误匹配正文 ${数字} 字面量致 SQL 错位、两测试路径写 i2f-velocity-bindsql/i2f.velocity 陈旧目录且 readFile 静态调 default 方法不编译、SqlProxy.executeSql conn=null 直调 NPE 骨架、@Data 暴露 templateMap setter 破坏封装且 clear+putAll 非原子有空窗、sql 标签 demo 注释称自动探测类型实为恒 UNSET、表名列名裸插值 ${table}/$column 无白名单构成注入面、#sql 宏名与 ${n} 占位属脆弱隐式契约。

- 详细文档：[i2f-extension-velocity-bindsql](./i2f-extension/i2f-extension-velocity-bindsql/readme.md)

### i2f-extension-verifycode

> Kaptcha 2.3.2（com.github.penggle，provided 硬编码版本未走根 DM）图形验证码极薄封装（2 主源约 167 行、无测试）：VerifyCodeUtil 单门面收敛两套 DefaultKaptcha 配置——getKaptchaBean 字符型（黑字 160×60 字长 4 ShadowGimpy）与 getKaptchaBeanMath 算术型（蓝字 字长 6 NoNoise），genVerifyCode(VerifyCodeType) 按枚举分派产 VerifyCodeData（VRFCD_+UUID 一次性 key / img / showText / code / imgBase64），MATH 分支以 lastIndexOf('@') 切分算式与答案，图片 ImageIO.write('jpg') 后经 i2f-codec-impl 的 Base64UrlStringByteCodec 转裸 base64url；VerifyCodeData @Data 附 getCacheEntry(key→code)/getWebShowEntry(key→imgBase64) 双视图对齐服务端存答案与前端取图闭环。仓库内无源码级消费方（仅 extension-all 聚合 + bash 四目录 jar 分发对外），与平行模块 i2f-jdk/i2f-verifycode（自绘看图答题型）互不依赖。核心静态缺陷：getKaptchaBeanMath 的 KAPTCHA_TEXTPRODUCER_IMPL 硬编码 com.ruoyi.framework.config.KaptchaTextCreator（RuoYi 脚手架外部类本仓不存在，MATH 模式必然实例化失败整体不可用）、@ 分隔契约脆弱（默认生产者无 @ 则 substring(0,-1) 越界）、genVerifyCode 无 else/null 兜底 img=null 致 ImageIO NPE、枚举 type 字段 private 无 getter 从不读取属死代码、每次调用 new DefaultKaptcha 无缓存且尺寸颜色字体全硬编码、只生成不校验比对全交调用方、imgBase64 裸编码无 data URI 前缀命名易误导、固定有损 JPEG 不利文本可读、kaptcha provided+版本写死脱离 DM、零测试零日志。

- 详细文档：[i2f-extension-verifycode](./i2f-extension/i2f-extension-verifycode/readme.md)

### i2f-extension-xproc4j

> XProc4J 多语言脚本执行扩展（18 主源约 2900 行 + 2 测试，包名直接落 i2f.jdbc.procedure.* 与 JDK 底座共享命名空间）：DefaultJdbcProcedureExecutor extends BasicJdbcProcedureExecutor，向 i2f-jdbc-procedure 引擎补齐 Java/Groovy/JavaScript(Nashorn)/Funic/TinyScript/OGNL 六语言的 lang:eval-* 节点与 EVAL_* 特性通道，innerTest/Eval/Visit 走 OGNL、innerRender 走 Velocity+#script 回调，FunicJdbcProcedureExecutor 以 Funic 取代 OGNL 作默认语言；LangEvalJavaNode 用 MemoryCompiler 内存编译并 LruMap(2048) 缓存 RC<hash> 类，Procedure{Funic,TinyScript}Resolver 把脚本内 FUN_/SP_ 函数调用回落为嵌套 executor.exec 并经反射暴露 env/bean/sql_query_* 内建函数。第三方 groovy/nashorn-core/ognl/velocity/antlr4 + 三只 DB 驱动全 provided+optional 且版本硬编码未走根 DM。有真实深度消费方 i2f-springboot-xproc4j-starter（按 isEnableFunic 选 executor 并条件装配 ScriptPreload/GrammarReporter 两监听器）。核心静态缺陷：LangEvalJavaNode 的 LANG_JAVA_BODY 误置 bodyNode=node、MetaDependencyResolver SQL 名 substring 越界裁剪、双检锁 inflater 未二次判空、runScript 五分支空 catch 吞异常、JS 三处一致跳过静态检查、TestDefaultProcedureExecutor 实建 Basic 底座致六节点零覆盖、bash 仅 jdk8 无 jdk17 jar。
- 详细文档：[i2f-extension-xproc4j](./i2f-extension/i2f-extension-xproc4j/readme.md)

### i2f-extension-zip4j

> Zip4J 2.9.1（net.lingala，provided 硬编码版本未走根 DM）的 ICompressor 契约适配器（1 主源 ZipZip4jCompressor 约 90 行 + 1 测试、无 SPI）：继承 AbsCompressor 骨架只补 compressBindData（写：new ZipFile→逐条 addStream，path=directory+'/'+fileName 剥前导 /，size>=0 时 setEntrySize）与 release(三参)（读：extractAll 全量落盘）两叶子方法；密码非空时构造 AES + KEY_STRENGTH_128 加密参数、读取按 isEncrypted() 自动 setPassword，是本压缩契约族唯一开箱提供 AES-128 加密的实现，与 commons-compress 的 i2f-extension-compress（多格式无强加密）互补。仓库内无源码级消费方（仅 extension-all 聚合 + bash 四目录 jar 分发）。核心静态缺陷：release(三参) 完全忽略 BiConsumer 违背 AbsCompressor 逐条目回调契约（父类 release(二参) 写盘 consumer 成死代码）、加密包未传密码时 password.toCharArray() NPE、is.close/zipFile.close 不在 finally 异常泄露、new ZipFile 对已存在文件追加不覆盖致重复条目、三参构造器只 setPassword 不 setEncryptFiles 与单参构造行为割裂、目录条目 inputStream==null 直接 continue 丢失空目录结构、仅支持 zip 读无格式探测、版本写死脱离 DM、lombok 依赖冗余。

- 详细文档：[i2f-extension-zip4j](./i2f-extension/i2f-extension-zip4j/readme.md)

### i2f-extension-zookeeper

> Apache ZooKeeper 3.6.3 + Curator 4.0.0（三者 provided 硬编码未走根 DM）的分布式协调适配套件（17 主源约 1200 行、无测试无 SPI）：以 ZookeeperManager（312 行）为连接与 CRUD 核心（CountDownLatch 阻塞建连、Expired 递归 reload 重连、逐级 mkdirs、KV/TTL/addWatch 持久递归监听/multi 事务），向外派生三大 i2f 契约实现——ZookeeperCache 实现 IExpireContainerCache/IPersistCache/IDistributedCache（/cache 前缀）、ZookeeperLockProvider+ZookeeperInterMutexLock 适配 ILockProvider/ILock（Curator InterProcessMutex）、AbsClusterProvider/ZookeeperClusterProvider 以临时节点+递归 watch 做 |domainId|%count==myid 一致性取模任务分片。厚适配模块（自带连接/重连/监听续挂/分片算法），有真实消费方 i2f-springboot-zookeeper-starter 装配五类 Bean。核心静态缺陷：最严重——ZookeeperManager.serializer 无 setter/无初始化全仓无赋值恒 null，凡经 obj2ZkData/zkData2Obj 的 set/get/expire/带数据mkdirs/集群注册mkdirs(path,true) 必然 NPE（数据读写通道整体不可用）；另 ZookeeperCache.exists 误传 key 应传 path 漏拼前缀、expire 用 toSeconds 而 set 用 toMillis TTL 差千倍、clean 误删 /cache 自身触发 NotEmpty、AbsClusterProvider.GUID 为 static 同进程多实例互覆、myid 取未排序 index 而 count 取排序列表致分片漂移、getExpire 恒 null、isMy 先 count 后 myid 非原子、重连无退避、PERSISTENT_WITH_TTL 依赖服务端 3.6+ 配置。

- 详细文档：[i2f-extension-zookeeper](./i2f-extension/i2f-extension-zookeeper/readme.md)

## i2f-springboot

> SpringBoot 生态的开箱即用 Starter 集合，以条件装配（`@ConditionalOnClass` / `@ConditionalOnExpression` / `@ConditionalOnMissingBean`）为核心，将 i2f 的 AI 工具链、数据访问、安全认证、缓存、分布式任务、运维控制台等能力自动配置为可插拔的 Spring Boot 组件。

### i2f-springboot-activity-starter

> Activiti 7 工作流 Starter：条件自动装配 `ProcessEngine` 与五大运行时服务（Repository/Runtime/History/Management/Task），数据源支持独立 JDBC 或复用容器二选一，`ActivityManager` 封装部署/启动/待办/完成/历史/挂起激活/组任务拾取归还转派全流程 API，另附默认关闭的 REST 演示接口与请假流程 BPMN 样例。

- 详细文档：[i2f-springboot-activity-starter](./i2f-springboot/i2f-springboot-activity-starter/readme.md)

### i2f-springboot-ai-mcp-server

> MCP 服务端 Starter：将 Spring 容器中的 `@Tool`/`@Tools` 工具以 HMAC-SHA256 签名认证的 Simple MCP 协议（`/mcp/tool/list`、`/mcp/tool/call`）对外暴露，内置 Spring Web MVC（共享宿主 Web 端口）与 Netty（独立端口）双传输模式的自动装配，支持 nonce 防重放与请求级上下文透传。

- 详细文档：[i2f-springboot-ai-mcp-server](./i2f-springboot/i2f-springboot-ai-mcp-server/readme.md)

### i2f-springboot-ai-mcp-client

> MCP 客户端 Starter：按 `instances` 配置将远程 MCP Server 注册为本地 `McpToolProvider` Bean，内置三套客户端实现——自研 Simple MCP 私协议（HMAC-SHA256 签名认证）、自研标准 JSON-RPC Streamable HTTP（`Mcp-Session-Id` 会话管理）与 solon-ai-mcp SDK（STDIO/SSE/STREAMABLE 等多种通道），供 AI 工具网关聚合为「实例名.工具名」形式的动态工具。

- 详细文档：[i2f-springboot-ai-mcp-client](./i2f-springboot/i2f-springboot-ai-mcp-client/readme.md)

### i2f-springboot-ai-starter

> AI 能力 Starter：两级条件自动装配注册 `AiModel`（REST OpenAI / DashScope 二选一）、`IJsonSerializer`、`AiAgent`，并以 `BeanDefinitionRegistryPostProcessor` 扫描 `@AiService` 接口、用 `FactoryBean` + JDK 动态代理实例化为「方法即提示词、参数即上下文、返回值即结构化输出」的声明式 AI 服务，对齐 LangChain4j `@AiService` 范式。

- 详细文档：[i2f-springboot-ai-starter](./i2f-springboot/i2f-springboot-ai-starter/readme.md)

### i2f-springboot-auth-starter

> RBAC 声明式权限校验 Starter：`@CheckPermissions` 注解承载 SpEL 表达式，`CheckPermissionAspect` 以 `@Before` 拦截并配合 `CheckPermissionContextProvider` 注入的 `user/auth/jp/args/pN` 上下文求值，非 `true` 抛 `PermissionDenyException`；`IRabcLoginUser` 承载身份、`RbacCheckPermissionHelper` 提供 hasAll/AnyRoles/Perms 判定，不绑定任何认证框架、零内部依赖。

- 详细文档：[i2f-springboot-auth-starter](./i2f-springboot/i2f-springboot-auth-starter/readme.md)

### i2f-springboot-dubbo-starter

> Dubbo 集成 Starter：单个空壳 `@Configuration` 类 `DubboAutoConfiguration` 承载 `@EnableDubbo`，由 `@ConditionalOnExpression("${i2f.springboot.config.dubbo.enable:true}")` 一键开关，把「启用 Alibaba Dubbo 注解驱动」收敛为带开关的薄封装 Starter；零内部依赖，上游为 EOL 的 `com.alibaba.boot:dubbo-spring-boot-starter:0.2.0`（`com.alibaba.dubbo` 旧命名空间），Dubbo 运行时 `provided` 需宿主自备。

- 详细文档：[i2f-springboot-dubbo-starter](./i2f-springboot/i2f-springboot-dubbo-starter/readme.md)

### i2f-springboot-dynamic-datasource-starter

> 多数据源动态路由 Starter：基于 `AbstractRoutingDataSource`，以 `@DataSource` 注解 + AOP 环绕切面将目标数据源写入 `ThreadLocal` 并路由；支持 `multiply.{id}.*` 批量声明、`group` 分组与 `ring`/`random` 组内负载均衡，缺失目标按 `strict` 抛 `DataSourceNotFoundException` 或回落 `primary`；内置 Druid/Hikari 两类 `DataSourceInitializer`。零内部依赖，`test-springboot` 为仓库内真实消费方。

- 详细文档：[i2f-springboot-dynamic-datasource-starter](./i2f-springboot/i2f-springboot-dynamic-datasource-starter/readme.md)

### i2f-springboot-encrypt-property-starter

> 配置属性透明解密 Starter：`PropertiesDecryptAdapter`（`BeanFactoryPostProcessor`）在启动期把 Environment 的每个 `PropertySource` 包装为 `DecryptPropertySourceWrapper`，读值时按前缀自动解密——`aes.` 走 AES/ECB（`i2f-crypto-impl`）、`bs64.` 走 Base64（`i2f-codec-impl`），让敏感配置密文落盘、`@Value`/`getProperty` 透明还原。策略由 `IPropertyDecryptor` 契约可插拔；但两解密器 `@Bean` 同名 `propertyDecryptor` 且默认同开、AES key 默认 null 为高危瑕疵。

- 详细文档：[i2f-springboot-encrypt-property-starter](./i2f-springboot/i2f-springboot-encrypt-property-starter/readme.md)

### i2f-springboot-http-proxy-starter

> HTTP 反向代理 Starter：`HttpProxyAutoConfiguration` 以 `@ConfigurationProperties("i2f.http.proxy")` 绑定「prefix→target」映射列表，`@ConditionalOnExpression` 默认开启，把 `i2f-spring-web` 的 `HttpProxyHandler`/`HttpProxyFilter` 注册为 `order=-1`、拦截 `/*` 的前置过滤器，命中前缀即透明转发（透传 method/header/body/query）。唯一内部依赖 `i2f-spring-web`；但 `mappings` 默认 null 无判空、未配即启动 NPE，且未登记进元数据为瑕疵。

- 详细文档：[i2f-springboot-http-proxy-starter](./i2f-springboot/i2f-springboot-http-proxy-starter/readme.md)

### i2f-springboot-jackson-sensible-starter

> Jackson 脱敏处理器桥接 Starter：单类 `JacksonSensibleAutoConfiguration`（`ApplicationContextAware` + `ApplicationListener<ContextRefreshedEvent>`）在容器启动/刷新时把全部 `ISensibleHandler` Bean 收集进上游 `i2f-extension-jackson` 的静态注册表 `SensibleHandlersHolder.GLOBAL_HANDLERS`，让自定义脱敏「声明为 Spring Bean 即被全局感知」，`@ConditionalOnExpression` 默认开启。唯一内部依赖 `i2f-extension-jackson`；但 `clear()`+`addAll()` 非原子有空窗、`@ConfigurationProperties` 无对应字段、异常静默吞为瑕疵。

- 详细文档：[i2f-springboot-jackson-sensible-starter](./i2f-springboot/i2f-springboot-jackson-sensible-starter/readme.md)

### i2f-springboot-jdbc-bql-starter

> BQL 与 JdbcProxy 双引擎 Spring Boot 自动装配 Starter：`JdbcBqlAutoConfiguration` 装配 `BqlTemplate` 程序化查询，`JdbcProxyAutoConfiguration` 以 `BeanDefinitionRegistryPostProcessor` 扫描 Mapper 接口注册动态代理 Bean，支持 MyBatis XML → Velocity 模板 → 简单注解三级 SQL 渲染降级链；`@ConditionalOnClass` 让 Velocity/OGNL 可选引入自动适配，两套引擎各由独立开关 `i2f.jdbc.bql.enable`/`i2f.jdbc.proxy.enable` 控制启停。

- 详细文档：[i2f-springboot-jdbc-bql-starter](./i2f-springboot/i2f-springboot-jdbc-bql-starter/readme.md)

### i2f-springboot-kafka-starter

> Kafka 消息队列 Spring Boot 自动装配 Starter，分两层：`KafkaAutoConfiguration` 产出原生 `AdminClient`/`KafkaProducer`/`KafkaConsumer`，`SpringKafkaConfiguration` 产出 `KafkaTemplate`/`KafkaListenerContainerFactory` 等 spring-kafka 抽象 Bean（含事务变体与消费错误日志处理器），参数统一由 `KafkaConfigProperties` 绑定构建；两层各由 `kafka.enable`/`spring-kafka.enable` 独立开关，`client-id`/`group-id` 缺省回退 `spring.application.name`；零内部依赖，kafka-clients/spring-kafka 为 provided；但事务工厂强转复用 `@Primary` 单例致非事务模板被污染、原生 `KafkaConsumer` 单例线程不安全为瑕疵。

- 详细文档：[i2f-springboot-kafka-starter](./i2f-springboot/i2f-springboot-kafka-starter/readme.md)

### i2f-springboot-limit-starter

> 请求限流 Spring Boot 自动装配 Starter：`LimitManager` 为核心枢纽，启动守护线程每 5s 从可插拔的 `LimitRuleItemProvider`（配置/JDBC/Redis 三种来源）刷新规则到内存，基于 Redis `INCR`+`EXPIRE` 做分布式固定窗口计数；`LimitFilter`（过滤器）与 `LimitAop`（`@Limited` 注解）两条入口按 global/ip/path/api/ant-path/user 六维判定超限抛 `LimitException`，各组件由独立 `@ConditionalOnExpression` 开关控制；瑕疵含 `scanRule()` 返回空 Map 致 `applyRule` 成死逻辑、扫描线程与 Redis 游标读取静默吞异常、`Cursor` 未关闭、限流后端故障时 fail-open 放行、规则写入 Redis 后不再更新。

- 详细文档：[i2f-springboot-limit-starter](./i2f-springboot/i2f-springboot-limit-starter/readme.md)

### i2f-springboot-maven-project

> Spring Boot 应用**脚手架/模板工程**（非发布型 Starter，已在父 pom `<modules>` 注释排除、不参与 reactor 聚合）：`BaseBootApplication` 统一启动并一次性打印含 JVM/MBean/SPI/网络/内存画像的诊断 Banner，`Slf4jPrintStream`+`PerfLogger` 将 `System.out/err` 重定向为 SLF4J 延迟求值日志；构建层以 `maven-jar-plugin` 产出配置外置瘦 jar（`Class-Path` 清单）+ `maven-assembly-plugin` 产出全量/增量 `tar.gz`，部署层提供 `jarctrl.sh`（进程/JVM/JMX/Agent）、`deploy.sh`（OTA+备份清理）、`startup.bat`（JDK9+ 模块自适应）；瑕疵含 `startup` 的 web 类型判断反向、Banner 编译 MBean 判空变量写错可 NPE、反射主类脆弱、静态可变全局态、演示用 system 依赖缺失 jar。

- 详细文档：[i2f-springboot-maven-project](./i2f-springboot/i2f-springboot-maven-project/readme.md)

### i2f-springboot-mybatis-starter

> MyBatis / MyBatis-Plus Spring Boot 自动装配 Starter，在三方 starter（均 provided、零版本绑定）之上叠加 i2f 增强：`MybatisAutoConfiguration` 以 `@MapperScan` 扫描 `com.**.mapper`/`com.**.dao` 并按开关经 `ConfigurationCustomizer` 注册分页拦截器，`MybatisDynamicAutoConfiguration` 产出 SQL 记录/结果集元数据两个拦截器 Bean，`SpringMybatisDynamicScriptExecutor` 借 `DataSourceUtils` 对 MyBatis `<script>` XML 片段直接 find/query/update（反射探测 `MybatisProperties`/`MybatisPlusProperties` 复用全局 Configuration，并持 `CountDownLatch` 静态单例）；三能力各由独立 `@ConditionalOnExpression` 开关控制。瑕疵含结果集拦截器配置的 handler 被无参构造丢弃、执行器以普通服务类冒充自动配置类、静态单例未初始化时 `getInstance()` 永久阻塞、反射探测空 catch 吞异常、`@MapperScan` 包范围硬编码、`@EnableScheduling` 冗余、元数据 hints 模板残留与键登记不全。

- 详细文档：[i2f-springboot-mybatis-starter](./i2f-springboot/i2f-springboot-mybatis-starter/readme.md)

### i2f-springboot-nginx-rtmp-auth-server-starter

> Nginx-RTMP 推/拉流鉴权回调服务端 Spring Boot 自动装配 Starter：`NginxRtmpAuthController` 暴露 `POST /api/rtmp/auth` 整体承接 nginx 回调的 `call/addr/name/tcUrl…` 表单参数与 `token`，委托函数式接口 `NginxRtmpAuthTokenValidator` 判定后以 HTTP 状态码（2xx 放行/500 拒绝）回应 nginx；默认 `DefaultNginxRtmpAuthTokenValidator` 按固定 `access-token` 比对，可注册自定义校验器覆盖。零内部依赖、spring-boot-starter-web 为 provided+optional。瑕疵含默认校验器空 token 直接放行（fail-open 安全隐患）、无 `@ConditionalOnMissingBean` 致覆盖时多候选注入失败、控制器与 `@Component` 冒充自动配置类登记、缺 Web 环境条件保护、响应声明 JSON 实为 text/plain。

- 详细文档：[i2f-springboot-nginx-rtmp-auth-server-starter](./i2f-springboot/i2f-springboot-nginx-rtmp-auth-server-starter/readme.md)

### i2f-springboot-ops-starter

> 一体化在线运维/开发/AI 控制台 Spring Boot 全量装载 Starter（组内体量最大：174 源文件 + 内嵌 Vue2 SPA，直接 compile 引入约 22 个 i2f 内部模块，处于依赖栈顶端聚合枢纽）：把应用自省（system/env/beans/deadlock/运行期日志级别 + `/ops/app/eval` Groovy 任意求值）、多数据源 SQL 控制台（`DatasourceProvider`+三 `DatasourceCollector`，query/update/runner/import/export）、SSH/SFTP 文件与远程 shell、Redis/ES/MinIO/AWS-S3/主机文件/xproc4j/xxl-job、OpenAI 对话 + 约 25 个 `@Tool` AI 工具 + Sqlite RAG + Groovy 技能 + TTS、DashScope 文生图/生视频/3D 全套能力，统一挂 `i2f.springboot.ops.base-url`（默认 `/ops`），全部入出站经 `OpsSecureTransfer` 的 SM2 密钥封装+SM4 加密+SM3 摘要+SM2 签名国密信封，`/ops/menus` 遍历 `IOpsProvider` 汇总导航。瑕疵含【高危】约 80 个 `@Controller`/`@Component`（含各 Tools/Collector）被直接登记进 `EnableAutoConfiguration`（spring.factories+AutoConfiguration.imports 双份一致）却无一为 `@Configuration`/`@AutoConfiguration`（误用装配机制、被扫则双注册，同 trace-mdc/websocket 族但规模空前）、【高危】`OpsSecureHelper.generateCertPair` 中 `clientPair = serverPair;` 覆盖刚生成的客户端密钥对致 server/client「证书」完全相同且均为含私钥完整密钥对 Base64(JSON)（拿 clientCert 即持服务端私钥可伪造）、【高危】`/ops/app/eval` Groovy RCE 端点与 datasource/ssh/host/redis/es 等 `ops.*.enable` 默认 true 而 AI `command/groovy/nodejs` 工具默认 false（同为执行能力姿态不一致）、【高危】`HostIdHelper.canAcceptHostId` 空 hostId 直接放行（实质只作集群定向非鉴权）、`RagAutoConfiguration.memoryTools` 缺 `@Bean` 致 MemoryTools 永不生成、`DefaultDatasourceProvider.refresh` 无数据源时 `put("primary",null)` NPE 被空 catch 吞+`detectPrimaryDatasource` 重复 isEmpty 死分支、`SkillAutoConfiguration` static `ScheduledExecutorService` 从不 shutdown+裸 `new Thread`、`AppEvalOpsController.eval` 每请求裸线程超时不中断致失控脚本泄漏、`OpsSecureAutoConfiguration` 无 cert 启动即 `System.out.println` 打印且每次换密钥+`test()` 自测死代码、SSH cmd/download 路径穿越+表名原样拼接标识符注入、元数据仅登记 `secure.cert` 一个属性数十真实开关零登记 `hints` 是 jsp/accesslog 死条目。**位于依赖栈顶端、被多上游文档标注为消费方，非孤岛**。

- 详细文档：[i2f-springboot-ops-starter](./i2f-springboot/i2f-springboot-ops-starter/readme.md)

### i2f-springboot-oss-minio-starter

> MinIO 对象存储 Spring Boot 自动装配 Starter（薄装配层，仅 2 个类）：`MinioProperties` 继承 `MinioMeta` 绑定 `i2f.minio.*`（url/accessKey/secretKey 三元连接参数），`MinioAutoConfiguration` 据此产出 `MinioUtil`（桶/对象/前缀/预签名 URL 原生 API 封装）与 `MinioFileSystem`（把对象存储适配为 `i2f-io-filesystem` 的 `IFileSystem`）两个共享同一 `MinioClient` 的 Bean，整体由 `i2f.minio.enable`（默认 true）开关控制；实现全部下沉 `i2f-extension-minio` / `i2f-extension-filesystem-minio`，minio 客户端 provided+optional。瑕疵含 `@Import(MinioClient.class)` 可疑且无消费、缺 `@ConditionalOnClass` 类存在保护、自动配置类未标 `@Configuration`、`@Data`+未用 `dateFormat` 遗留字段、两 Bean 日志文案复制粘贴相同、`enable` 未纳入属性类、元数据 hints 模板残留、无 region/桶/超时扩展参数。

- 详细文档：[i2f-springboot-oss-minio-starter](./i2f-springboot/i2f-springboot-oss-minio-starter/readme.md)

### i2f-springboot-quartz-starter

> Quartz 定时任务 Spring Boot 自动装配 Starter：`QuartzAutoConfiguration` 装配 `SchedulerFactoryBean`/`Scheduler`，`SpringJobFactory`（继承 `AdaptableJobFactory`）令 Quartz 反射创建的 Job 实例接受 Spring 注入，`QuartzScannerConfig` 在 `ContextRefreshedEvent` 时扫描 `base-packages` 下 `@QuartzSchedule` 方法并注册/更新 Interval/Cron 触发器（幂等走 `updateTrigger`）；调度核心下沉内部依赖 `i2f-extension-quartz`，附带标准 `QRTZ_*` 建表 SQL 支持持久化/集群；quartz/spring-boot-starter-quartz/c3p0 全 provided+optional，调度器与扫描器各由独立 `@ConditionalOnExpression` 开关。瑕疵含配置探测 `if (rs != null)` 恒真从不校验资源存在（降级链失效）、`scheduler()` 直接取 `getScheduler()` 存在初始化时机风险、与 Boot 自带 Quartz 自动配置冲突且无 `@ConditionalOnMissingBean`、缺 `@ConditionalOnClass` 保护、`SpringJobFactory` 以 `@Component` 混入自动配置登记、非唯一 Bean 目标类裸实例化丢失注入、`ContextRefreshedEvent` 重复扫描、元数据 `scaner` 拼写错误且主配置项未登记。

- 详细文档：[i2f-springboot-quartz-starter](./i2f-springboot/i2f-springboot-quartz-starter/readme.md)

### i2f-springboot-rabbitmq-starter

> RabbitMQ Spring Boot 自动装配 Starter（薄装配层，仅 4 类、零内部依赖）：`RabbitMqAutoConfiguration`（`@ConfigurationProperties` 绑 `i2f.springboot.config.rabbit.*`）在 `enable`（默认 true）开关下以 `@ConditionalOnMissingBean` 产出全局 `RabbitTemplate`，按 `mandatory` 设强制回退并按 `log-confirm-callback`/`log-return-callback` 可选挂载两个仅打印日志的 `ConfirmCallback`/`ReturnCallback` 观测桩；`RabbitMqManager` 提供 `send(exchange, routing, msg)`→`convertAndSend` 极简门面；连接/序列化全委托 provided+optional 的 `spring-boot-starter-amqp`。瑕疵含 `@ConditionalOnMissingBean(RabbitTemplate.class)` 致 Boot 自带模板先产出使本模块 mandatory+回调常态静默空转、`enable=false` 无法关停恒定注册且强依赖模板的 `RabbitMqManager`、`RabbitMqManager` 以 `@Component` 冒充自动配置类登记、回调触发依赖 Boot 侧 `publisher-confirm-type`/`publisher-returns` 却不被校验联动、回调一律 info 级无失败区分补救、`@Data` 用于配置类、元数据未登记 `enable` 且 hints 为 JSP/Tomcat 模板残留。

- 详细文档：[i2f-springboot-rabbitmq-starter](./i2f-springboot/i2f-springboot-rabbitmq-starter/readme.md)

### i2f-springboot-redis-starter

> Redis Spring Boot 自动装配 Starter，4 个自动配置类分层装配：`RedisAutoConfiguration` 产出带 Jackson 默认类型的 `Jackson2JsonRedisSerializer`（+`@EnableRedisRepositories`），`RedisTemplateAutoConfiguration` 组装 String-key/JSON-value 的 `RedisTemplate<String,Object>`，`RedisCacheConfiguration` 将二者桥接为 i2f 统一分布式缓存 `IRedisClient`（`SpringRedisClient`）+`RedisCache`，`LettuceRedisHeartbeatConfiguration`（`@ConditionalOnClass`）后台线程周期 `validateConnection` 规避 Lettuce 15 分钟 TCP 重传假死；缓存/客户端下沉内部依赖 `i2f-extension-redis-cache`/`i2f-spring-redis`，data-redis/starter-json 为 provided；四者各由独立 `@ConditionalOnExpression` 开关。瑕疵含心跳 `run()` 无 try/catch 而 `scheduleAtFixedRate` 遇未捕获异常即取消后续执行致保活永久停摆、开关强依赖链不可独立关停、`redisStringKeyTemplate` 缺 `@ConditionalOnMissingBean` 与 Boot 模板叠加多候选、4 类均无 `@Configuration` 走轻量模式、`enableDefaultTyping` 已废弃且反序列化隐患、`@Data` 误用、元数据 hints 模板残留。

- 详细文档：[i2f-springboot-redis-starter](./i2f-springboot/i2f-springboot-redis-starter/readme.md)

### i2f-springboot-redisson-starter

> Redisson 分布式锁/原子自增 Spring Boot 自动装配 Starter：`RedissonAutoConfiguration` 复用 Boot `RedisProperties`（单点/集群/哨兵三模式）构建 `RedissonClient`，并 `@Import` 装配 `RedissonLockProvider`（i2f `ILockProvider` 抽象 + 裸 `RLock`/`RReadWriteLock`）、`RedissonAtomic`（`RAtomicLong` 自增 ID）、`RedissonLockAop`（切面解析 `@RedisLock`/`@RedisReadLock`/`@RedisWriteLock` 按优先级自动加解锁）；锁接口下沉内部依赖 `i2f-lock`，redisson-spring-boot-starter 为 provided。瑕疵含 `kidx > 0` 使文档示例的 `keyIdx=0` 首参分区锁永远失效（功能级 bug）、`redissonClient` 缺 `@ConditionalOnMissingBean` 与 redisson 自带客户端多候选冲突、自动配置类无 `@Configuration` 且缺 `@ConditionalOnClass`、三模式全不落空静默产无效 Config、仅支持 `redis://` 不支持 TLS、前缀拼写 `redission`、元数据默认值/描述错误与 hints 模板残留、AOP 三分支复制粘贴。

- 详细文档：[i2f-springboot-redisson-starter](./i2f-springboot/i2f-springboot-redisson-starter/readme.md)

### i2f-springboot-security-starter

> Spring Security 无状态 token 认证/授权 Spring Boot 自动装配 Starter：`SecurityAutoConfiguration`（继承 `WebSecurityConfigurerAdapter`）以一堆 `@Value` 布尔开关与白名单驱动 STATELESS 安全链，`JsonSupportUsernamePasswordAuthenticationFilter` 按 Content-Type 分流 JSON/表单双登录，`AbstractTokenHolder`（进程内 `DefaultTokenHolder` / 可选 `RedisTokenHolder`）以 UUID token 缓存 `UserDetails` 并支持单点登录互踢，`LoginGuarder`+`LoginLockBeforeLoginChecker` 做账号/IP 失败锁定，成功/失败/登出/异常经 `ServletContextUtil.forward` 转发 `/forward/response` 统一 `ApiResp` 输出；登录/登出/令牌/异常入口/用户详情均 `@ConditionalOnMissingBean` 可覆盖，配 `ISecurityConfigListener`/`BeforeLoginChecker`/`LoginPasswordDecoder` 三扩展点；security/web 均 provided+optional。瑕疵含 spring.factories 注册了不存在的 `i2f.springboot.security.impl.SecurityForwardController`（真类在 `i2f.spring.authentication.forward`）可致启动 ClassNotFound、大量 `@Component` 又进自动配置登记双重身份致 `@ConditionalOnMissingBean` 求值不确定、`SecurityAutoConfiguration` 职责过载叠加 `@Data`/`@ControllerAdvice`、默认用户每次加载重编码且 admin/admin 弱口令、`e.printStackTrace()` 绕过日志、绝大多数配置项未登记元数据且 hints 为 JSP/Tomcat 残留、token/用户名 INFO 日志泄露、`DisableSecurityConfiguration` 关总开关仍不排除本模块 `@Component` 默认 Bean。

- 详细文档：[i2f-springboot-security-starter](./i2f-springboot/i2f-springboot-security-starter/readme.md)

### i2f-springboot-shiro-starter

> Apache Shiro 无状态 token 认证/授权 Spring Boot 自动装配 Starter（23 类，手写全套装配、不用官方 shiro-starter）：`ShiroAutoConfiguration`（`@Configuration`+`@ConfigurationProperties("i2f.springboot.config.shiro")`）产出 `SecurityManager`/`SessionControlWebSubjectFactory`/`HashedCredentialsMatcher`(MD5/256)/`ShiroFilterFactoryBean`/`DelegatingFilterProxy`/`FilterRegistrationBean`，`ShiroCoreFilter`（全局 filter）拦截 `/login`（JSON/表单双登录）`/logout` 并对带 token 请求以 `CustomerAuthToken` 自动登录续期，`AbstractShiroTokenHolder`（进程内 `DefaultShiroTokenHolder` MapCache / 可选 `RedisShiroTokenHolder` RedisCache）以 UUID token 缓存 `IShiroUser` 支持单点互踢，多 Realm 按优先级编排、`@ConditionalOnMissingBean` 默认 Realm/handler 可覆盖、`filters.*`+反射动态注册额外过滤器、`ShiroExceptionHandler` 兜底；shiro-spring provided+optional，依赖 9 个 i2f 内部模块。瑕疵含 `RedisShiroTokenHolder` 无 `@Component`/未进 spring.factories 实为死代码、`AuthorizationAttributeSourceAdvisor` 缺 `DefaultAdvisorAutoProxyCreator` 致注解授权静默失效、缺 `@ConditionalOnClass`、默认 admin/admin 弱口令且以用户名作盐、JSON 登录 `printStackTrace` 吞异常后以字符串 "null" 续登、常量 `shiroCoreFiler` 拼写错、`removeSingleToken` 删错目标且无调用、`i2f-spring-redis`/`IHttpRequestHandler` 闲置、元数据仅登记 `enable`。

- 详细文档：[i2f-springboot-shiro-starter](./i2f-springboot/i2f-springboot-shiro-starter/readme.md)

### i2f-springboot-spring-starter

> Spring 基础 Starter（i2f-springboot 组的地基装配件）：以 14 个各带独立 `@ConditionalOnExpression`/`@ConditionalOnClass` 开关的自动配置类，一次性把 `i2f-spring`/`i2f-resp`/`i2f-extension-jackson`/`i2f-jdk-ext-web` 能力接入 Boot 应用——`@Import` 注册 `SpringUtil`/`EnvironmentUtil`/`EventManager`/`SpringContext`/`SpringEnvironment`/`TransactionUtil` 并回填 `SpringContextHolder`（三把 `CountDownLatch` 阻塞式静态持有器），装配异步/调度线程池、CORS 过滤器、全局异常→`ApiResp` 转换、全局响应体 `ApiResp` 包装（含 404 截获、`@StandardResp(false)` 逐点关闭）、Jackson「Long→String + 日期时间格式化」转换器、链路 `TraceFilter` 与约 80 项可配的 Web `SecurityFilter`，并附 `BaseBootApplication`/`WarBootApplication` 启动入口与富诊断 Banner；直接/传递引入 7+ 内部模块，compile 面最广。瑕疵含【高危】`startup` 对任意非 null webType 强制 `NONE`（逻辑反转禁用 Web）、元数据 `i2f.spring.{response,trace}.enable.enable` 键与实际 `...enable` 开关错位（IDE 补全永不生效）、async/schedule 把 `CallerRunsPolicy` 配错映射为 `DiscardOldestPolicy`、两类无 `@Configuration` 走 lite 模式致 `getAsyncExecutor`/`configureTasks` 直调 `@Bean` 方法再 new 一份脱离生命周期管理的重复线程池、`SecurityFilterAutoConfiguration` 约 80 属性零元数据登记且命名空间 `i2f.security.filter` 异类、`@Data`+类/方法级重复 `@ConditionalOnClass` 滥用、`SecurityFilter`/`TraceFilter` 靠 `i2f-spring-web` 传递未直声明、两个 `HandlerExceptionResolver` 无 `@Order`、零测试无 `<description>`。

- 详细文档：[i2f-springboot-spring-starter](./i2f-springboot/i2f-springboot-spring-starter/readme.md)

### i2f-springboot-ssh-tunnel-starter

> SSH 隧道自动建立 Starter（5 类、唯一内部依赖 `i2f-extension-sftp`）：以「两阶段装配」把上游 `SshTunnelUtil` 接入 Boot 生命周期——阶段 A `TunnelSetupConfiguration`（实现 `ApplicationListener<ApplicationEnvironmentPreparedEvent>`，经 `spring.factories` + `ApplicationListener.imports` 双登记）抢在容器/数据源创建之前用 `Binder` 绑定 `i2f.springboot.ssh.tunnel.servers[]`，逐台跳板机 `new SshTunnelUtil(...).createTunnel(localPort,remoteHost,remotePort).setup()` 建立本地端口正向隧道（jsch `setPortForwardingL` + daemon keepalive 重连 + shutdown hook 清理），令内网 MySQL/Redis 可按 `localhost` 直连；阶段 B `TunnelAutoConfiguration`（`@Configuration`+`@ConditionalOnExpression(enable)`）把 `TunnelHolder` 静态持有器里的 `TunnelProperties`/`SshTunnelManager` 暴露为可注入 Bean。瑕疵含【高危】`sshTunnelManager()` 创建的 `SshTunnelUtil` 从不 `manager.servers.add(ret)` 致管理器 Bean 恒空、隧道不受管只能靠各自 shutdown hook、`@ConditionalOnExpression` 对 `spring.factories` 反射实例化的监听器不生效故 `enable=false` 仍会建隧道、`@Component`+`ApplicationListener` 双重身份且扫描版收不到 EnvironmentPrepared 事件、建隧道失败被 `log.info` 吞掉且照常 setup、`TunnelProperties` 的 `@ConfigurationProperties` 与 `Binder` 手工绑定双路径并存注解名不副实、`@Configuration`/`@Component` 类违反规范加 `@Data`、元数据仅登记 `enable`（描述占位 "all."）且 `hints` 段是别处拷来的 jsp/accesslog 死条目、`com.jcraft:jsch:0.1.55` 硬编码 provided 与根 DM 的 mwiede fork 不一致、`SshProperties` 仅 host/port/username/password 无法配 knownHost/私钥且上游硬编码 `StrictHostKeyChecking=no`。

- 详细文档：[i2f-springboot-ssh-tunnel-starter](./i2f-springboot/i2f-springboot-ssh-tunnel-starter/readme.md)

### i2f-springboot-swagger2-starter

> Swagger2（Springfox 2.9.2）接口文档自动装配 Starter（5 类、无任何 i2f 内部 compile 依赖）：以 `Swagger2AutoConfiguration`（`@ConditionalOnExpression(i2f.swagger2.enable)`+`@EnableConfigurationProperties`+`@Import`）为入口，产 `ApiInfo`（`@ConditionalOnMissingBean`可覆盖）与 `groupName=all` 全量 `Docket`，`@Import` 的 `Swagger2RestfulConfiguration` 按注解维度静态产 `20-rest-all`/`30-web`/`40-get`/`50-post`/`60-put`/`70-delete` 六个 `Docket`，`DynamicSwaggerApisConfiguration`（`@Configuration`+`InitializingBean`）根据 `i2f.swagger2.apis.dynamic.group.*` 在 `afterPropertiesSet` 中用 `BeanDefinitionBuilder`+`DocketFactoryBean` 动态注册任意多个分组 `Docket`；每组独立开关默认全开，Boot/spring-web/springfox 全 provided。瑕疵含【高危】全 Starter 无 `@EnableSwagger2`（springfox 2.9.2 无 Boot 自动配置）故不自标则 Docket 无人消费、无 swagger-ui.html，“自动配置”名不副实、sample 承诺的 base-package/ant-path 逗号多值因 springfox `basePackage`/`ant` 只收单值而实际不生效、`Swagger2AutoConfiguration`/`Swagger2RestfulConfiguration` 缺 `@Configuration` 走 lite 模式、`@ConfigurationProperties(i2f.swagger2.apis)` 加在无字段类上纯属噪声、总开关 `i2f.swagger2.enable` 与 `apis.dynamic.enable` 零元数据登记且 `hints` 是别处拷来的 jsp/accesslog 死条目、`@Data` 滥用于自动配置类、`ApiInfo` 的 Contact 恒为 `new Contact(null,null,null)`、Guava `Predicates` 靠传递未直声明且 springfox 2.9.2 陈旧与 Boot 2.6+ 兼容风险、动态注册中为日志额外 `getBean` 触发 prototype 实例化、默认分组过密重叠、日志 `registrt`/`registry` 拼写错。

- 详细文档：[i2f-springboot-swagger2-starter](./i2f-springboot/i2f-springboot-swagger2-starter/readme.md)

### i2f-springboot-swl-starter

> SWL（Secure Web Layer）安全传输协议 Spring Boot 透明加解密 Starter（9 类）：`SwlSpringAutoConfiguration`（`@ConditionalOnExpression(i2f.swl.enable)`+`@EnableConfigurationProperties`）按可插拔算法类装配 `SwlTransfer`（非对称/对称/摘要/混淆四套 Supplier，默认 RSA+AES+SHA256+Base64，`getBeanByTypeOrNewInstance` 优先取 Bean 否则反射 new）并以 `FilterRegistrationBean`（order -10、`/*`）注册继承自 `i2f-jdk-ext-swl` `SwlWebFilter` 的入站解密·出站加密过滤器，`SwlSpringAop` 环绕所有 `@*Mapping` 把过滤器暂存于 request attribute 的解密异常延迟重抛（绕开 Filter 抛异常不被 `@ControllerAdvice` 捕获）并逐方法标 `@SwlCtrl`，`SwlSpringController` 暴露 `/swl/swapKey` 密钥协商握手，`SwlExceptionHandler` 兜底，`SwlMissingBeanConfiguration` 无外部缓存时兜底进程内 `IExpireCache`。与 `i2f-spring-swl`（`@ControllerAdvice` 版）为两套并行互不依赖实现，消费方 `test-swl-starter`。瑕疵含【高危】`i2f.swl.filter.enable` 是死开关（`SwlSpringWebFilter` 未登记 spring.factories/无 @Component、仅 `new` 出来，`@Conditional` 永不对手动对象求值，总开关一开过滤器恒在）、【高危】`i2f.swl.web.enable` 是死开关（`@ConditionalOnExpression` 标在 `@PostMapping` handler 方法上对 MVC 无效）、四套供应商 catch 全错用 `SYMMETRIC_EXCEPTION` 码、`swlTransfer()` 含空 try-catch 死代码、`SwlExceptionHandler` 用 `printStackTrace` 且硬编码 status:500/`message`=枚举 name 掩盖错误类别、`SwlSpringAop` `@Autowired(required=false)` request 却无条件用、实际生效的 `i2f.swl.filter.order`/`url-pattern` 未登记元数据、`hints` 含 jsp/accesslog 拷贝死条目、`@Data` 滥用于配置类/Filter、兜底进程内缓存使集群防重放失效且关 missing 无外部缓存则启动失败、上游 `SwlWebFilter` 出站 `responseText="$."+responseBody`（byte[] 拼成数组地址）疑似缺陷被默认全开 out 直接触发。

- 详细文档：[i2f-springboot-swl-starter](./i2f-springboot/i2f-springboot-swl-starter/readme.md)

### i2f-springboot-totp-starter

> TOTP/HOTP 动态口令（Google/Microsoft Authenticator、Steam Guard 兼容）Spring Boot 即装配 Starter（5 类、唯一内部依赖 `i2f-otpauth`）：`HmacOtpAutoConfiguration`（`@ConditionalOnExpression(i2f.springboot.totp.enable)`，无 `@Bean` 方法）经 `@Import` 拉入门面 `HmacOtpAccountAuthenticator`（`generateRandomKey`/`getAuthenticator(account)`/`verify`/`generate`/`makeUrl` 产 `otpauth://` 扫码地址，`InitializingBean` 启动期强校验 provider）与默认工厂 `DefaultTotpAuthenticatorFactory`（按 `type` 分派 totp/microsoft/steam），业务方实现 SPI `HmacOtpAccountKeyProvider`（按账号返 Base32 密钥）即可注入使用，另一 SPI `HmacOtpAuthenticatorFactory` 可覆盖分派。瑕疵含【高危】`algorithm`/`digits` 仅在 `type` 非 totp/microsoft/steam 的兜底分支才 `setXxx`，而默认 `type=totp` 提前 return，故对全部文档化取值二者均为死配置（`makeUrl` 里 URL 参数也因此恒为默认 SHA1/6）、元数据把工厂开关登记成 `i2f.springboot.totp.factory`（Boolean）而代码实为 `...factory.enable` 键错位、`HmacOtpAutoConfiguration` 无 `@Configuration` 走 lite 模式且 `@ConfigurationProperties` 加在无字段空类、`@ConditionalOnMissingBean` 用在 `@Import` 普通类上顺序不可靠有注入歧义风险、上游 `OtpAuthenticator.verify` 只比当前窗口无 ±1 时间偏移容错、`@Autowired(required=false)` provider 却在 `afterPropertiesSet` 硬抛异常语义矛盾、`@Data`/`@NoArgsConstructor` 滥用、`generateSecretKey` 固定 16 字节对 SHA256 偏短、`hints` 含 jsp/accesslog 拷贝死条目、全仓库无任何下游消费方与 test 样例属未验证生态孤岛。

- 详细文档：[i2f-springboot-totp-starter](./i2f-springboot/i2f-springboot-totp-starter/readme.md)

### i2f-springboot-trace-mdc-starter

> 全链路 TraceId/MDC 上下文传播 Spring Boot 装配 Starter（11 源文件 = 10 组件类 + `@MdcTrace` 注解，内部依赖 `i2f-trace-mdc`+`i2f-extension-slf4j` 的 `Slf4jMdcManager` SPI）：一个 Starter 打通 Servlet（`MdcWebFilter` order -990）/Reactive（`MdcGatewayFilter`）两类入站从多别名头解析或生成 traceId 写 MDC 五键、Feign（`MdcFeignInterceptor`）/RestTemplate（`MdcHttpClientInterceptor`+`MdcRestTemplateInterceptorProcessor` BPP 自动注入每个 RestTemplate）两类出站注入 trace 头、`MdcTaskDecorator` 线程池复制 MDC、三切面环绕 `@Scheduled`/`@XxlJob`/`@PowerJobHandler` 与 `@MdcTrace` 生成新 traceId，各带独立 `@ConditionalOnExpression`（默认全开）+`@ConditionalOnClass`，Web/AOP/Feign/Gateway/xxl-job/powerjob 全 provided+optional。瑕疵含【高危】10 组件类被直接登记为自动配置类却无一为 `@Configuration`/`@AutoConfiguration`（误用装配机制）、全缺 `@ConditionalOnMissingBean` 致宿主自定义 `TaskDecorator` 时 `getIfAvailable` 抛 `NoUniqueBeanDefinitionException` 启动失败、【高危】`MdcGatewayFilter` put 五键只 remove 两键且 ThreadLocal MDC 与 Reactor 事件循环线程错配致跨请求污染（对照 servlet 版成对清理正确）、`@Component`+auto-config 双身份被扫则重复注册、BPP 构造器注入普通组件反模式且 `@Data` 暴露 setInterceptor、`@AutoConfigureAfter` 对 BPP 实例化时序语义不成立、10 真实开关零元数据登记 `hints` 是 jsp/accesslog 拷贝死条目、`MdcTaskDecorator` 不覆盖 i2f-springboot-spring-starter 自建异步/调度池致跨模块断层、切面 finally remove 与入口 filter 同名键冲突提前清空 MDC、三切面重复 `@EnableAspectJAutoProxy`、入口不回写响应 trace 头、`getIp` 的 `getAddress()` NPE 与 `printStackTrace`、全仓库无下游消费方与 test 样例属生态孤岛。

- 详细文档：[i2f-springboot-trace-mdc-starter](./i2f-springboot/i2f-springboot-trace-mdc-starter/readme.md)

### i2f-springboot-websocket-starter

> WebSocket 轻量装配 Starter（5 类、唯一内部依赖 `i2f-reflect`、`spring-boot-starter-websocket` provided）：同时接入**两套并行独立**的 WebSocket 技术栈——JSR-356 `@ServerEndpoint` 风格（`BasicWebsocketEndpointHandler` 给出 onOpen/Close/Message/Error + 在线计数 + `ConcurrentHashMap` 会话表 + 广播/回显模板，`DefaultWebsocketEndpointHandler` 继承并 `@ServerEndpoint("${...path:/default/broadcast}")`+`@Component` 暴露默认广播端点，配 `ServerEndpointExporter` 让容器扫描）与 Spring WebSocket 风格（`WebSocketAutoConfiguration` `@EnableWebSocket`+`implements WebSocketConfigurer`+`@ConditionalOnExpression(i2f.springboot.websocket.enable)`+`@ConfigurationProperties`，按 `registry.*` Map 逐条 `addHandler`，`use-context-bean` 决定取 Bean 还是 `ReflectResolver` 反射 new，`impl` 包 `AbstractWebSocketHandler`/`AbstractWebsocketHandshakeInterceptor` 给可实例化基类）。瑕疵含【高危】`@ServerEndpoint` 端点用**实例字段**存 `clients`/`onlineCount` 而容器每连接 new 一实例致广播与在线数整体失效、【高危】`DefaultWebsocketEndpointHandler` 组件被登记进 `EnableAutoConfiguration` 却非 `@Configuration`（误用自动配置机制）、【高危】`default-endpoint.enable=false` 关不掉端点（`ServerEndpointExporter` 独立扫 classpath `@ServerEndpoint` 与 Spring 条件化无关）、`registerWebSocketHandlers` 只空 catch `IllegalAccessException` 漏 `ReflectResolver.getInstance` 抛的 `IllegalArgumentException` 与 `loadClass` 返 null 的 NPE、`interceptor==null` 仍 `addInterceptors(null)`、`@Data` 滥用于 `@Configuration`、sample 指向全仓库不存在的 `@EnableWebsocketConfig` 注解、`@ServerEndpoint` 的 `${...}` 占位符 Spring 不解析、war 部署下 `ServerEndpointExporter` 与外部容器 `WsSci` 冲突、两个 Abstract 命名类实为具体类且以 `user_<时间戳>` 为 key 同毫秒相撞、跨域全开无鉴权、registry 项零元数据 + `hints` 拷 jsp/accesslog 死条目、无测试无下游消费方属生态孤岛。

- 详细文档：[i2f-springboot-websocket-starter](./i2f-springboot/i2f-springboot-websocket-starter/readme.md)

### i2f-springboot-xproc4j-starter

> XProc4J「去数据库存储过程」引擎 Spring Boot 全量装配 Starter（23 源文件、直接依赖 6 个 i2f compile 模块 `i2f-extension-xproc4j`/`-antlr4`/`-ognl`/`i2f-spring-core`/`-ai-dashscope`/`-ai-openai`，velocity/ognl/antlr4/dynamic-datasource/redisson/dashscope-sdk/spring-web 全 provided+optional，组内依赖面最广装配最重）：`SpringContextJdbcProcedureExecutorAutoConfiguration`（`@ConditionalOnExpression(xproc4j.enable)`+`@EnableConfigurationProperties`+`@Import(JdbcProcedureHelper)`，无 `@Configuration` 走 lite）以约 15 个各带独立 `@ConditionalOnExpression`+`@ConditionalOnMissingBean` 的 `@Bean` 装配 `INamingContext`/`IEnvironment`/`XProc4jEventHandler`/两套 `DataSourceProvider`（baomidou DynamicRouting + Spring AbstractRouting）/XML 扫描+目录 watch 热更新/Java caller/registry/`JdbcProcedureContext`/`JdbcProcedureExecutor`（`enable-funic` 选 Funic/Default）/慢SQL阈值/日志器/语法报告器/预加载与调用日志监听器；`SpringExtensionJdbcProcedureAutoConfiguration`（`@Configuration`+`InitializingBean`）把 `spring_bean`/`spring_env`/`redis_*`/`log_*`/`to_json`/`http_get` 静态方法注册进上游 `ContextHolder` 全局函数表；`SpringJdbcProcedureProxyMapperAutoConfiguration`（`BeanDefinitionRegistryPostProcessor`，`xproc4j.proxy.enable`）扫 `@ProcedureMapper` 接口经 `FactoryBean`+JDK 代理注册为方法名匹配过程 id 的声明式 Mapper；`JdbcProcedureHelper` 供 `call/invoke` 静态入口；另附 DashScope/OpenAI 各二 Provider 条件装配。瑕疵含【高危】`afterPropertiesSet` 在 applicationContext 赋值前先注册 `SpringContextFunctions` 且重复注册、`SpringEnvironmentFunctions`/`SpringSlf4jFunctions` 永不注册致 `spring_env`/全部 `log_*` 脚本函数静默不可用（复制粘贴错类）、【高危】`OpenAiAiAutoConfiguration` 错用 `@ConditionalOnClass(dashscope Generation)` 且调 `DashScopeAi.getPossibleApiKey` 致纯 openai 环境不装配、【高危】`mapperPackages` 默认空 List 非 null 使 BDRPP 的 `if(!=null)` 恒真、`else` 兜底扫描模式永不执行、不配 `mapper-packages` 则 `@ProcedureMapper` 全不生效、两核心配置类无 `@Configuration` 走 lite+`@Data` 滥用、BDRPP 冒充 auto-config、开关键错别字 `spring-routring-datasource`（关不掉）、上游 context/registry 关下游 executor 开则必填参数 `NoSuchBean` 启动失败、大量 enable/funic/mapper/report-options/dashscope/openai 键零元数据登记 `hints` 是 jsp/accesslog 死条目、xml-locations 等描述照抄 whether enable、`JdbcProcedureHelper` `CountDownLatch.await` 未就绪前调用永久阻塞吞 InterruptedException、`run()` 裸线程吞异常、代理 Handler 漏处理 equals 且报错 id 恒 null、AI Provider 缺 `@ConditionalOnMissingBean`、IO 异常空 catch、ForkJoinPool 不 shutdown、裸 `xproc4j.*`/`dashscope.ai.*`/`openai.ai.*` 异类命名空间、源码目录内嵌 readme 提及不存在的 `@JdbcProcedure`。**有真实消费方 `i2f-tools-ops`（compile 未注释）**，非孤岛。

- 详细文档：[i2f-springboot-xproc4j-starter](./i2f-springboot/i2f-springboot-xproc4j-starter/readme.md)

### i2f-springboot-xxl-job-starter

> xxl-job 执行器（`xxl-job-core:2.4.1`）Spring Boot 装配 Starter（2 类、无任何 i2f 内部 compile 依赖）：把官方 `XxlJobConfig` 手动装配样例改造为自动配置——`XxlJobAutoConfiguration`（`@ConditionalOnExpression(${xxl.job.enable:true})`+`@ConditionalOnClass(XxlJobSpringExecutor,XxlJob)`+`@EnableConfigurationProperties`）产 `@Bean XxlJobSpringExecutor`，`XxlJobProperties`（`@ConfigurationProperties(xxl.job)`，嵌套 `AdminProperties{addresses}`/`ExecutorProperties{appname,address,ip,port,logPath,logRetentionDays}`）承载全量配置，注册/内嵌 server/`@XxlJob` 扫描/调度回调全外包给 xxl-core 自管。Boot 与 `xxl-job-core` 均 provided。瑕疵含自动配置类无 `@Configuration` 走 lite 模式（同 spring/totp/swagger2 家族）、`xxl.job.enable` 零元数据登记且非 `XxlJobProperties` 字段（仅被 `@ConditionalOnExpression` 读，宜改 `@ConditionalOnProperty`）、`xxlJobExecutor` 对八项 setter 无条件套用把未配字段的 Java 默认（port 0/地址 null）灌进执行器覆盖 xxl-core 自身默认且 `getAdmin()` 未配时 `.getAddresses()` 直接 NPE、元数据 `defaultValue` 仅供 IDE 提示不参与绑定与实际生效值脱节、缺 `@ConditionalOnMissingBean` 宿主自定义执行器会双注册冲突、`@Data`+`@NoArgsConstructor` 滥用于配置类、`xxl-job-core` provided 但未标 optional、sample/元数据 log-path 默认误拼 `xxj-job`、末尾 InetUtils 自动取 IP 注释是无实现死指引、唯一疑似消费方 `test-xxl-job` 已将本 Starter 依赖注释改直依 xxl-core 但残留全量 `xxl.job.*` 配置（本 Starter 元路径未经运行验证属孤岛）。

- 详细文档：[i2f-springboot-xxl-job-starter](./i2f-springboot/i2f-springboot-xxl-job-starter/readme.md)

### i2f-springboot-zookeeper-starter

> ZooKeeper 分布式协调 Spring Boot 装配 Starter（2 类、内部 compile 依赖 `i2f-lock`+`i2f-extension-zookeeper`）：`ZookeeperAutoConfiguration`（`@ConditionalOnExpression(${i2f.zookeeper.enable:true})`+`@EnableConfigurationProperties`，无 `@Configuration` 走 lite 模式）一把注册 `ZookeeperManager`（原生 ZK 连接/CRUD/watch）、`ZookeeperCache`（`/cache` 前缀分布式过期缓存）、`ZookeeperClusterProvider`（按 `spring.application.name` 自动拼 `/apps/{name}/cluster` 临时节点 + 递归 watch 一致性取模分片）、`@ConditionalOnClass` 条件化的 `CuratorFramework`（`ZookeeperLockUtil.getClient` 指数退避）与 `ZookeeperLockProvider`（`InterProcessMutex` 适配 `ILockProvider`）五个 Bean；`ZookeeperProperties extends ZookeeperConfig`（`@ConfigurationProperties(i2f.zookeeper)` 仅 `connectString`/`sessionTimeout` 两字段）。zookeeper/curator 全 provided。瑕疵含【高危】`clusterProvider()` → `init()` → `mkdirs(path,true)` → `obj2ZkData` → `serializer`（上游恒 null）→ NPE 只要 ZK 可达应用启动必失败、【高危】`sessionTimeout` 默认 -1 传给 Curator `sessionTimeoutMs(-1)` 非法、【高危】ZK 不可达时 `CountDownLatch.await(Integer.MAX_VALUE)` 近乎无限阻塞无快速失败、元数据 group 名从 swagger2 拷贝未改为 `i2f.swagger2.apis`、`i2f.zookeeper..session-timeout` 双点号拼写错、`hints` 含 jsp/accesslog 死条目、`@Data` 暴露 `setEnvironment`/`setZkConfig`、全 5 Bean 无 `@ConditionalOnMissingBean`、`spring-boot-starter-aop` 死依赖、无下游消费方属生态孤岛。

- 详细文档：[i2f-springboot-zookeeper-starter](./i2f-springboot/i2f-springboot-zookeeper-starter/readme.md)

## i2f-springcloud

> 面向 Spring Cloud 微服务生态的装配 Starter 集合，覆盖注册发现/配置中心/网关/负载均衡/熔断/链路追踪/监控等能力的自动装载，把「引依赖即生效」的即插即用风格从 SpringBoot 组延伸到 SpringCloud 全家桶。

### i2f-springcloud-actuator-admin-starter

> Spring Boot Admin 监控台自动装载 Starter（1 源文件 + 3 资源、零 i2f 内部 compile 依赖）：把 codecentric `spring-boot-admin-starter-server` 经一个空的 `@Configuration`+`@EnableAdminServer` 类改造为放 classpath 即自动拉起图形化监控中心，`@ConditionalOnExpression(${i2f.springcloud.actuator-server.enable:true})` 单布尔开关默认开启，双通道登记（`spring.factories`+`AutoConfiguration.imports`），配合同组 `i2f-springcloud-actuator-starter` 一监控系统一暴露端点。瑕疵含【高危】仅有布尔门无 `@ConditionalOnClass(EnableAdminServer)`，admin-server 依赖为 provided 非 optional 不具传递性，缺类时默认开仍装载致 `NoClassDefFoundError` 启动失败而非优雅退避、【版本错配】pin `spring-boot-admin-starter-server:2.2.3`（Boot 2.2.x 时代）与根 pom `spring-boot.version=2.7.18` 非兼容组合且版本硬编码未纳 DM、`@ConfigurationProperties` 加在无字段空类上属空挂（`enable` 实由 SpEL 直读非绑定字段）、`@Data`/`@NoArgsConstructor` 对空类纯装饰、封装价值单薄且 `@EnableAdminServer` 无 `@ConditionalOnMissingBean` 退避、元数据 `hints` 是 jsp/accesslog 跨模块拷贝死条目且仅登记 `enable` 一项、双通道登记过时冗余、全仓库无任何模块真实 compile 依赖本 Starter 亦无 test 样例属生态孤岛。

- 详细文档：[i2f-springcloud-actuator-admin-starter](./i2f-springcloud/i2f-springcloud-actuator-admin-starter/readme.md)

### i2f-springcloud-actuator-starter

> Spring Boot Admin 客户端接入 + Actuator 端点暴露自动装载 Starter（`actuator-admin-starter` 的客户端对偶，1 源文件 + 4 资源、零 i2f 内部 compile 依赖）：设计意图是用单布尔开关 `i2f.springcloud.actuator-client.enable`（默认 true）控制本服务向 Admin Server 注册并暴露监控端点，双通道登记（`spring.factories`+`AutoConfiguration.imports`）。瑕疵含【高危·空转】唯一类 `ActuatorClientAutoConfiguration` 是完全空的 `@Configuration`（无 `@Bean`/`@Import`/`@Enable*`），装载它对容器零影响，真正的注册由 codecentric 自带 `SpringBootAdminClientAutoConfiguration` 读取 `spring.boot.admin.client.url` 独立驱动、【高危·开关名不副实】`enable:false` 亦不能阻止注册（因被门控的动作根本不存在，较 admin-starter 至少还有 `@EnableAdminServer` 更空）、【高危·provided 非 optional 不传递】`spring-boot-starter-actuator`+`spring-boot-admin-starter-client` 均 provided 非 optional 不随本 Starter 传递，使用方须自行补齐两依赖方能使本 Starter 意义成立、一旦补齐本 Starter 即彻底多余、【版本错配】硬编码 admin-client `2.2.3`（Boot 2.2.x 时代）与根 pom Boot `2.7.18` 非兼容且未纳 DM、`@ConfigurationProperties`/`@Data`/`@Slf4j` 加在空类上纯装饰空挂、元数据 `hints` 是 jsp/accesslog 跨模块拷贝死条目且仅登记 `enable` 一项、双通道登记过时冗余、全仓库无任何模块 compile/test 依赖本 Starter 属生态孤岛。

- 详细文档：[i2f-springcloud-actuator-starter](./i2f-springcloud/i2f-springcloud-actuator-starter/readme.md)

### i2f-springcloud-alibaba-nacos-starter

> 阿里 Nacos（服务注册发现 + 配置中心）即插即用装配 Starter（本组注册中心子域核心接入件，1 源文件 + 6 资源、零 i2f 内部 compile 依赖）：将 `spring-cloud-starter-alibaba-nacos-discovery`/`-config` 与 `spring-cloud-starter-loadbalancer` 作为 provided 依赖引入，经带 `@EnableDiscoveryClient` 的 `@Configuration` 类 `NacosAutoConfiguration` + 单布尔开关 `i2f.springcloud.nacos.enable`（默认 true）门控自动装载，附三份翔实 sample bootstrap-nacos.yaml 作接入范本。版本由根 pom `spring-cloud-alibaba-dependencies:2021.0.5.0` BOM 统一治理（优于同组硬编码 actuator 件）。瑕疵含【中危·开关约束有限】`@ConditionalOnExpression` 只门控本类，`enable:false` 无法阻止 nacos 自身自动配置的注册与配置拉取、【冗余】Spring Cloud 2021 已默认自动注册，`@EnableDiscoveryClient` 已无实际作用且与本模块 sample 注释自相矛盾、`spring-cloud-starter-loadbalancer` provided 但未标 optional 且无代码引用、pom 注释建议排除 ribbon 但 `<exclusions>` 整段被注释未生效、`@ConfigurationProperties` 空挂在无字段类上、`InitializingBean` 仅一行日志、nacos 依赖 provided+optional 不传递需使用方自行补齐、元数据 hints 是 jsp/accesslog 跨模块拷贝死条目仅登记 enable、双通道登记过时冗余、全仓库无任何模块 compile/test 依赖本 Starter 属生态孤岛。

- 详细文档：[i2f-springcloud-alibaba-nacos-starter](./i2f-springcloud/i2f-springcloud-alibaba-nacos-starter/readme.md)

### i2f-springcloud-alibaba-seata-starter

> Apache Seata 分布式事务即插即用装配 Starter（本组分布式事务子域唯一接入件，1 源文件 + 5 资源、零 i2f 内部 compile 依赖）：将 `spring-cloud-starter-alibaba-seata` 作 provided+optional 依赖引入，经 `@Configuration` 类 `SeataAutoConfiguration` + 布尔开关门控自动装载，附 sample bootstrap-seata.yaml（事务分组/registry/config 走 nacos），版本由根 BOM `2021.0.5.0` 治理。瑕疵含【高危·开关键拼写分裂】代码 `@ConditionalOnExpression`/`@ConfigurationProperties` 均用错拼 `i2f.springcloud.seate`（seate），而元数据与 sample 登记的是正确 `i2f.springcloud.seata`，全仓 grep 证实代码从不读 seata、文档从不读 seate，致用户按文档设 `seata.enable=false` 完全无效且真实键不可发现、【空壳】类无 `@Enable*`/`@Bean`/`@Import` 仅 InitializingBean 一行日志，真正的 Seata 能力由 seata 自身自动配置驱动、`@ConfigurationProperties` 空挂且前缀错拼、provided+optional 不传递需使用方自行补齐、sample 多处错拼（文件名 `application-seate.properties`、yaml `SETA_GROUP` 疑为 SEATA_GROUP）、元数据 hints 是 jsp/accesslog 拷贝死条目仅登记 enable、双通道登记过时冗余、全仓库无任何模块 compile/test 依赖本 Starter 属生态孤岛（封装质量弱于 nacos-starter）。

- 详细文档：[i2f-springcloud-alibaba-seata-starter](./i2f-springcloud/i2f-springcloud-alibaba-seata-starter/readme.md)

### i2f-springcloud-alibaba-sentinel-starter

> 阿里 Sentinel（流控/熔断降级/热点参数/系统保护/授权）即插即用装配 Starter（本组「流量治理」子域核心接入件，2 源文件 + 5 资源，**本组首个含真实功能逻辑且有 i2f 内部 compile 依赖的模块**）：将 `spring-cloud-starter-alibaba-sentinel`/`sentinel-datasource-nacos`/`spring-cloud-alibaba-sentinel-gateway` 作 provided+optional 引入（版本由根 BOM `2021.0.5.0` 治理无硬编码错配），除标准空壳 `SentinelAutoConfiguration`（仅一行日志、`i2f.springcloud.sentinel.enable` 键拼写正确）外，核心提供 `DefaultSentinelBlockExceptionHandler`——implements Sentinel `BlockExceptionHandler` 并 compile 依赖本组首个内部件 `i2f-resp`，把 Flow/Degrade/ParamFlow/System/Authority 五类 BlockException 统一转为 `ApiResp.error(中文提示)` JSON，独立开关 `sentinel.global-exception-handler.enable`。瑕疵含【中危·HTTP 状态码语义错误】被限流仍恒 `setStatus(200)` 仅靠 body code 表达失败、网关/监控无法从状态码识别、【性能】每请求 `new ObjectMapper()`、【反模式】已 @Slf4j 仍 `e.printStackTrace()`、【高危·缺 @ConditionalOnClass】Handler 直接 implements provided+optional 的 BlockExceptionHandler 且 sentinel/boot-web 不传递，缺类时默认开即 NoClassDefFoundError 无优雅退避、【开关门控有限】`sentinel.enable:false` 只关空壳类不能停 Sentinel 自身自动配置、【元数据登记不全】更关键的 `global-exception-handler.enable` 未登记且 hints 是 jsp/accesslog 拷贝死条目、sentinel-datasource-nacos 与 gateway 两依赖引入但零代码引用、双通道登记过时、全仓无 compile/test 消费方属生态孤岛。

- 详细文档：[i2f-springcloud-alibaba-sentinel-starter](./i2f-springcloud/i2f-springcloud-alibaba-sentinel-starter/readme.md)

### i2f-springcloud-config-client-starter

> Spring Cloud Config 客户端装配 Starter（本组「配置中心」子域客户端端，3 源文件 + 5 资源、零 i2f 内部 compile 依赖，**本组功能最完整、封装深度最高且唯一带 `@ConditionalOnClass` 兜底的接入件**）：除布尔开关 `i2f.springcloud.config-client.enable`（默认 true）门控的标准薄壳 `ConfigClientAutoConfiguration`（带过时 `@EnableDiscoveryClient`）外，额外实现了 `EnvironmentPullBaseWithVersionCompareIntervalRefresher`——一个基于 Git commit 版本轮询、无需消息总线即可让 Config 客户端热更新配置的自定义刷新器（默认 `refresh.pull.enable:false` opt-in，周期 GET `{uri}/{name}/{profile}` 取 version 比对，变化则 `ConfigDataContextRefresher.refresh()`），配套真实字段 `EnvironmentPullBaseWithVersionProperties`（init/interval-delay-seconds 默认 30）。版本由根 pom `spring-cloud.version=2021.0.8` BOM 治理无硬编码。瑕疵含【高危·必需 @Autowired 但只门控类存在】`@ConditionalOnClass(ConfigDataContextRefresher.class)` 只保证类不保证 bean，`pull.enable=true` 而环境无该 bean 时 NoSuchBeanDefinitionException 启动失败、【中危】`new RestTemplate()` 无超时使单线程调度器可被卡死的 Config Server 永久阻塞、`ScheduledExecutorService` 无 shutdown 线程泄漏且裸 `new Thread` 跑 initConfig 冗余、【日志 Bug】L111 profiles 文案误打 configNameSet 且全类字符串拼接日志、initConfig/refreshTask 逻辑逐行重复、`@Component` 登记为自动配置双通道有双重注册风险、元数据仅登记 enable 而三个真实功能键全未登记、sample 用传统 bootstrap 与 2021 `spring.config.import` 脱节、spring-cloud-starter-config provided 未标 optional 不传递、全仓无 compile/test 消费方属生态孤岛。

- 详细文档：[i2f-springcloud-config-client-starter](./i2f-springcloud/i2f-springcloud-config-client-starter/readme.md)

### i2f-springcloud-config-server-starter

> Spring Cloud Config 服务端装配 Starter（本组「配置中心」子域服务端端，`config-client-starter` 的服务端对偶，2 源文件 + 11 资源含 9 份 native 示范配置、零 i2f 内部 compile 依赖）：`ConfigServerAutoConfiguration` 以 `@EnableConfigServer` + 布尔开关 `i2f.springcloud.config-server.enable`（默认 true）自动拉起 Config Server；核心功能 `EnvironmentControllerNativeVersionResponseAdvice`（`@RestControllerAdvice implements ResponseBodyAdvice<Environment>`，独立开关 `native.version.enable` 默认 true）专为 native 本地文件模式（官方 version 恒 null）合成 SHA-256 版本号，补齐 `config-client-starter` 版本轮询刷新器所缺的 version，实现免消息总线热更新闭环。版本由根 pom `spring-cloud-dependencies:2021.0.8` BOM 治理无硬编码。瑕疵含【高危·缺 @ConditionalOnClass】`@EnableConfigServer` 而 config-server 为 provided 未标 optional 不传递，缺类时默认开即 NoClassDefFoundError 崩溃非优雅退避（同 actuator-admin 族）、【高危·version 哈希实际忽略内容】Advice 用 `resourceLoader.getResource(PropertySource.getName())` 回读，但 name 是资源 description（classpath 型形如 `Class path resource [...]`）DefaultResourceLoader 无法解析→catch 仅 digest nop→classpath native 源 version 只由名称列表决定，改值不改名则哈希不变、客户端轮询永不 refresh，恰击穿设计目的且自带 sample 正是 classpath:/config、【中危】`@RestControllerAdvice` 被登记为 EnableAutoConfiguration 若组件扫描命中则双重注册、每响应全量重算哈希+磁盘IO、【低】@ConfigurationProperties 空挂无字段、关键键 native.version.enable 未登记元数据且 hints 是 jsp/accesslog 死条目、provided 未标 optional、双通道登记冗余、全仓无消费方属生态孤岛。

- 详细文档：[i2f-springcloud-config-server-starter](./i2f-springcloud/i2f-springcloud-config-server-starter/readme.md)

### i2f-springcloud-consul-starter

> HashiCorp Consul（服务注册发现 + 分布式配置中心）即插即用装配 Starter（本组「注册中心/配置中心」子域，`alibaba-nacos-starter` 的 Consul 技术栈对偶，1 源文件 + 5 资源、零 i2f 内部 compile 依赖）：将 `spring-cloud-starter-consul-discovery`/`-config` 作 provided 依赖引入（版本由根 pom `spring-cloud-dependencies:2021.0.8` BOM 治理无硬编码错配），经带 `@EnableDiscoveryClient` 的 `@Configuration` 类 `ConsulAutoConfiguration` + 单布尔开关 `i2f.springcloud.consul.enable`（默认 true）门控自动装载，附翔实 sample bootstrap-consul.yaml（host/port/service-name/heartbeat/config format）与 consul.md 二进制安装启动指引。瑕疵含【高危·缺 @ConditionalOnClass + provided 非 optional 不传递】consul-discovery/config 及带来的 spring-cloud-commons（含 @EnableDiscoveryClient 所在包）均不传递，本类登记进 EnableAutoConfiguration 仅有布尔门无类兜底，使用方未补齐时 enable 默认 true 装载即 NoClassDefFoundError 启动失败非优雅退避（同 actuator-admin/config-server 族）、【开关名不副实】`consul.enable:false` 只关本空类不能阻止 consul 自身自动配置的注册与拉取、`@ConfigurationProperties` 空挂无字段、`@EnableDiscoveryClient` 在 2021 已冗余、sample 依赖传统 bootstrap 机制与 2021 config.import 脱节、元数据仅登记 enable 且 hints 是 jsp/accesslog 拷贝死条目、双通道登记过时冗余、lombok 空引、全仓无 compile/test 消费方属生态孤岛。

- 详细文档：[i2f-springcloud-consul-starter](./i2f-springcloud/i2f-springcloud-consul-starter/readme.md)

### i2f-springcloud-discovery-server-starter

> 自研轻量级服务注册中心「服务端」装配 Starter（本组「服务发现」子域服务端，配套 `i2f-springcloud-discovery-starter` 客户端；5 源文件 + 4 资源、零 i2f 内部 compile 依赖）：与同组 nacos/consul/eureka 薄封装根本不同，本模块不依赖任何现成注册中心框架，改用 Redis 作实例存储后端 + 一组 HTTP POST 接口 + SHA-256 签名校验，从零实现最小可用注册/发现服务端（`POST /api/api/registry` 上报 `{uid,sign,serviceId,port}` 以 `http://{remoteAddr}:{port}` 写带 TTL 的 `{prefix}:services:{serviceId}:{URLEncode(url)}` 键、`POST /api/api/services` 拉全量实例表），三级独立布尔开关 `discovery.server.enable`/`api.enable`/`redis-manager.enable` 键拼写全部正确并登记元数据，`RedisServiceInstanceManager` 带 `@ConditionalOnClass(RedisTemplate)` 兜底。瑕疵含【高危·默认密钥极弱且被 sample 固化】`DiscoveryServerProperties.secretKey` 默认硬编码 `"123456"` 且 sample 原样写死，未覆盖则整套签名鉴权形同虚设可被投毒/侦察、【高危·条件不一致致启动崩溃】`RedisServiceInstanceManager` 可 `@ConditionalOnClass`+`redis-manager.enable` 优雅退避，但 `DiscoverServerController` 仅受 `api.enable`（默认 true）门控且 `@Autowired`(required) 注入 `IServiceInstanceManager`，Redis 缺失/关管理器时 `NoSuchBeanDefinitionException` 启动失败（应加 `@ConditionalOnBean` 或 required=false）、【中危】`redisTemplate.keys()` 用 Redis `KEYS` O(N) 阻塞主线程应改 `SCAN`、`makeSign` 载荷仅 uid 无 timestamp/nonce 可无限重放、`getRemoteAddr()` 在网关/代理后取到代理 IP 且 host(远端)与 port(自报)来源割裂、`secret-key`/`keepalive-seconds`/`redis-manager.prefix` 三关键属性完全未登记元数据、Controller/Manager 既 `@Import` 又带 stereotype 有双重注册风险、键解析 `split(":",2)` 隐含 serviceId 不含冒号假设、`spring-cloud-starter` provided 未 optional 却零 import 是死依赖、hints 是 jsp/accesslog 拷贝死条目且 `redis-manager.enable` 描述误写 "api"、全仓无真实消费方属生态孤岛。

- 详细文档：[i2f-springcloud-discovery-server-starter](./i2f-springcloud/i2f-springcloud-discovery-server-starter/readme.md)

### i2f-springcloud-discovery-starter

> 自研轻量级服务发现「客户端」装配 Starter（本组「服务发现」子域客户端，`i2f-springcloud-discovery-server-starter` 的客户端对偶；6 源文件 + 6 资源、零 i2f 内部 compile 依赖）：与同组 nacos/consul/eureka 薄封装根本不同，不依赖任何现成注册中心框架，而是自研实现 Spring Cloud `DiscoveryClient`，提供两种并存来源——配置式 `DiscoveryClientProvider`（读 `i2f.springcloud.discovery.instances` 静态表 + 自动纳入本应用自身，`@Configuration(proxyBeanMethods=false)`+`@AutoConfigureBefore` 抢先官方类+`WebServerInitializedEvent` 纠端口，写法相对规范）与远程注册式 `RemoteDiscoveryClientProvider`（`@PostConstruct` 起双线程 `RestTemplate` 每 10s 心跳上报 / 每 30s 拉全量，`sign=SHA-256(secretKey#uid)` 与服务端一致）。瑕疵含【高危·远程式默认开】`registry.enable:true` 使任何引本件的应用后台线程持续向 `localhost:9999` POST、无 server 时刷屏错误日志且线程池从不 shutdown；【高危·开箱 404】客户端 `base-url+/api/registry` 与服务端实际双重前缀 `/api/api/registry` 不对齐，这对官方 CP 默认打不通须手改 registry-path；【高危·弱密钥】`secretKey` 默认硬编码 `123456`；另含 `@PostConstruct` 早于 Web 启动且无端口事件纠正（随机端口注册错误）、`RestTemplate` 无超时挂死调度线程、`@Component` 登记为自动配置双通道双重注册、两 `DiscoveryClient` bean 语义重叠、`@ConfigurationProperties` 空挂无 enable 字段、registry 8 真实键零登记元数据、spring-web/jackson 硬编码版本绕开 DM、`spring-cloud-starter` provided 未 optional 缺 InetUtils 无优雅退避、`setUri` 无端口 port=-1、全仓无消费方属生态孤岛。

- 详细文档：[i2f-springcloud-discovery-starter](./i2f-springcloud/i2f-springcloud-discovery-starter/readme.md)

### i2f-springcloud-gateway-starter

> Spring Cloud Gateway 增强装配 Starter（本组「服务网关」子域核心，**本组功能最完整、非薄封装的网关增强件**：9 源文件 + 6 资源、零 i2f 内部 compile 依赖、版本由根 BOM `spring-cloud-dependencies:2021.0.8` 治理）：`GatewayAutoConfiguration`（`i2f.springcloud.gateway.enable:true`）经 `@Import` 一把拉入代码式 CORS（`GatewayCorsConfig` `@Bean CorsWebFilter`）、自定义断言 `RequestAttrRoutePredicateFactory`（`RequestAttr=param,regexp`）、自定义过滤器 `RequestAttrGatewayFilterFactory`、请求日志全局过滤器 `RequestLogGlobalFilter`（注入 trace-id + `TimeStatistic` 按 path/IP 统计）、可选重复参数裁剪 `RequestQueryRepeatFilter`（默认 `query-repeat.enable:false` opt-in），另有抽象 token 鉴权模板 `AbsAuthTokenFilter`，各子件独立开关分层控制，附 457 行翔实 sample 教学文档。瑕疵含【高危·缺 @ConditionalOnClass + provided 非 optional】gateway 核心不传递且装配仅布尔门无类兜底，使用方未补齐时默认开装载 `@Import` 的 implements gateway 类型件即 NoClassDefFoundError 崩溃（同 actuator-admin/config-server/consul 族）、【高危·RequestAttrGatewayFilterFactory NPE+402 不断链】value==null 设 402+setComplete 后不 return 继续 `value.matches(regex)` NPE 且末尾仍 chain.filter 放行，注释承诺的 402 拦截不成立、【高危·默认 CORS 不安全】allowCredentials=true + allowOrigins=* 转 allowedOriginPatterns=* 反射任意 Origin 带凭证、【中危】RequestLogGlobalFilter 统计 Map 按 path/IP 无界增长泄漏 + builder 请求头重复打印两遍、【中危】元数据 show-querys/show-statistic/enable-repeat-form 默认值与代码相反且 repeat-props 三功能键零登记、GatewayAutoConfiguration/GatewayCorsConfig 无 @Configuration 走 lite、AbsAuthTokenFilter 抽象类未注册（默认零鉴权）+ subPath(*2) 魔法数 + getOrder 与日志 filter 同为 -1、RequestQueryRepeatProperties @Configuration+@EnableConfigurationProperties 双注册、enableAccessLog 用 System.setProperty 污染全局 JVM、repeat filter 全量缓冲 body OOM 风险、双通道登记冗余 + hints jsp/accesslog 死条目。**有真实消费方 同组示例 test-gateway-swl**，非孤岛。

- 详细文档：[i2f-springcloud-gateway-starter](./i2f-springcloud/i2f-springcloud-gateway-starter/readme.md)

### i2f-springcloud-gateway-swl-starter

> Spring Cloud Gateway「SWL 安全传输（加解密）网关装配 Starter」（本组「服务网关」子域，Servlet 侧 `i2f-springboot-swl-starter` 的**响应式对偶**；7 源文件 + 3 资源、**含 5 个 i2f 内部 compile 依赖** i2f-spring-core/i2f-swl/i2f-sm-crypto-swl/i2f-jdk-ext-swl/i2f-extension-swl，本组少见）：把 `i2f.web.swl.filter.SwlWebConfig`/`SwlTransfer` 加解密协议搬到网关边缘——`SwlGatewayFilter`（`GlobalFilter` order -100，单文件 667 行核心）请求侧缓冲 body 解密重建 `ServerHttpRequestDecorator`、响应侧 `buffer()` 全量加密包装 `ServerHttpResponseDecorator`，`SwlGatewayApiFilter`（order -999）拦 `POST /swl/swapKey` 完成公钥握手，`SwlMissingBeanConfiguration` `@ConditionalOnMissingBean(IExpireCache)` 兜底进程内缓存，四独立开关 gateway/filter/api/missing 分层条件化，算法供应商经 asym/symm/digest/obfuscate-algo-class 四属性可插拔。瑕疵含【高危·无 Content-Type 请求解密 NPE】`mediaType=getContentType()` 可 null 但 `ctrl.isIn()` 分支 `mediaType.getCharset()` 无判空、默认 in=true 未配 url-patterns 时几乎所有请求进入即崩、【高危·swapKey 吞异常后 NPE/越界】握手反序列化包空 catch 失败后 `reqHandleShake.getAttaches().get(0)` NPE/IOOBE、【高危·缺 @ConditionalOnClass+provided 非 optional】gateway 不传递且仅布尔门无类兜底装载即 NoClassDefFoundError 崩溃（同 gateway-starter/actuator-admin/config-server/consul 族）、【高危·clientIp 可伪造且作会话隔离键】getIp 优先取可伪造 x-forwarded-for 用作 `transfer.receive(clientIp,...)` 会话/nonce 隔离键且 unknown 时回落本机 IP、【中危】握手路由硬编码 `http://localhost:80` 与 api filter 强耦合、api-route.enable/api-swap-key-path/enable-url-path-check/cert-id-name/url-path-name 等大量功能键零登记元数据、命名国密+provided sm-crypto/bcprov 但默认装配实为国际算法 RSA/AES/SHA256/Base64、URL 路径校验 swlu 头缺失 decode NPE、4 自动配置类全无 @Configuration 走 lite 且两个 implements GlobalFilter 的 filter 类直接登记当自动配置类、@Import 空挂+@ConfigurationProperties 空挂、多处空 catch 吞异常+响应全量缓冲 OOM（无响应侧白名单）+ISwlExceptionAdvideConverter 死接口、双通道登记冗余+jsp/accesslog 死 hints+sm-crypto/bcprov 硬编码版本绕开 BOM。**有真实消费方 同组示例 test-gateway-swl**，非孤岛。

- 详细文档：[i2f-springcloud-gateway-swl-starter](./i2f-springcloud/i2f-springcloud-gateway-swl-starter/readme.md)

### i2f-springcloud-loadbalancer-starter

> Spring Cloud LoadBalancer 客户端负载均衡装配 Starter（本组「客户端负载均衡」子域，`i2f-springcloud-netflix-ribbon-starter` 旧 Ribbon 的新一代对偶；**仅 1 个 27 行源文件 + 4 资源、零 i2f 内部 compile 依赖**，本组最典型**纯空壳转发件**）：`LoadBalancerAutoConfiguration` 除 `afterPropertiesSet` 打一行日志外**不做任何定制增强**——无 `@Bean`、无自定义 `ReactorLoadBalancer`、无 Ribbon 排斥逻辑，负载均衡能力全靠使用方自行引官方 `spring-cloud-starter-loadbalancer`（本件 provided 引入）。瑕疵含【纯空壳零增强】`@ConfigurationProperties` 前缀挂在零字段类上、唯一属性 enable 由 `@ConditionalOnExpression` SpEL 直读非绑定，引与不引行为一致、【provided 非 optional 不传递·名不副实】仅引本 loadbalancer-starter 并不真正获得 LoadBalancer 能力须再自引官方 starter、【sample 开关键拼写错误】`application-loadbalancer.properties` 写 `i2f.springcloud.config.loadbalancer.enable` 多一段 `.config.` 与实际键 `i2f.springcloud.loadbalancer.enable` 不符照抄即开关永不生效、Ribbon 排斥须手动（pom 注释/sample 均要求 `spring.cloud.loadbalancer.ribbon.enabled=false` 或排除 ribbon，本件不代处理）、双通道登记冗余 + jsp/accesslog 死 hints。相对规范处：自动配置类带了 `@Configuration`（不同于 gateway 族 lite）。**全仓零消费方属纯孤岛**。

- 详细文档：[i2f-springcloud-loadbalancer-starter](./i2f-springcloud/i2f-springcloud-loadbalancer-starter/readme.md)

### i2f-springcloud-netflix-eureka-client-starter

> Netflix Eureka 服务注册**客户端**装配 Starter（本组「服务注册与发现」子域，与 `netflix-eureka-server-starter` 为 client/server 对；**仅 1 个 25 行源文件 + 5 资源、零 i2f 内部 compile 依赖**，纯空壳件但比 loadbalancer-starter 多一个实质功能）：`EurekaClientAutoConfiguration` 类体零字段，唯一行为是携带 `@EnableEurekaClient` 注解——作为官方同名自动配置的**布尔门代理**，通过 `i2f.springcloud.eureka-client.enable:true` 控制是否触发 Eureka 客户端注册。瑕疵含【provided 非 optional + 无 @ConditionalOnClass → 缺 classpath 即 NoClassDefFoundError 启动崩溃（高危，同 gateway/actuator-admin/config-server/consul 族）】、@ConfigurationProperties 挂零字段类（与 loadbalancer 同病）、布尔门语义与官方 `eureka.client.enabled` 重叠属多余抽象层、sample `instance_id` 下划线非 canonical kebab-case、双通道登记冗余 + jsp/accesslog 死 hints + configuration-processor 形同虚设。**全仓零消费方属纯孤岛**。

- 详细文档：[i2f-springcloud-netflix-eureka-client-starter](./i2f-springcloud/i2f-springcloud-netflix-eureka-client-starter/readme.md)

### i2f-springcloud-netflix-eureka-server-starter

> Netflix Eureka 注册中心**服务端**装配 Starter（本组「服务注册与发现」子域，与 `netflix-eureka-client-starter` 为**完全镜像对称的 client/server 对**；同作者同日同结构，仅 `@EnableEurekaClient` → `@EnableEurekaServer`；**仅 1 个 25 行源文件 + 4 资源、零 i2f 内部 compile 依赖**，纯空壳布尔门代理）：`EurekaServerAutoConfiguration` 类体零字段，唯一行为是携带 `@EnableEurekaServer` 注解触发官方 Eureka Server 自动配置（管理面板/注册表/驱逐线程）。瑕疵含【provided 非 optional + 无 @ConditionalOnClass → 缺 classpath 即 NoClassDefFoundError（高危，同 eureka-client/gateway/actuator-admin 族）】、@ConfigurationProperties 挂零字段类、布尔门可被 Main 类 `@EnableEurekaServer` 静默绕过（开关假象）、sample L13 分隔符混用冒号与等号、双通道登记冗余 + jsp/accesslog 死 hints + configuration-processor 形同虚设。**全仓零消费方属纯孤岛**。sample 含完整独立部署推荐配置（关自注册+关自保存+驱逐 5s）有参考价值。

- 详细文档：[i2f-springcloud-netflix-eureka-server-starter](./i2f-springcloud/i2f-springcloud-netflix-eureka-server-starter/readme.md)

### i2f-springcloud-netflix-hystrix-starter

> Netflix Hystrix 熔断降级装配 Starter（本组「熔断降级」子域，`alibaba-sentinel-starter` 的旧 Netflix 对偶、已 EOL；**仅 1 个 29 行源文件 + 4 资源、零 i2f 内部 compile 依赖**，本组唯一「版本时间胶囊」——模块级自引 `spring-boot:2.3.7` / `spring-cloud:Hoxton.SR12` BOM 覆盖根 2.7.18/2021.0.8，与项目主版本不兼容）：`HystrixAutoConfiguration` 类体零字段，`@EnableHystrix` 触发断路器 + `InitializingBean` 打一行日志。瑕疵含【模块级 BOM 覆盖根版本治理·架构级危险】、【引用已被 Spring Cloud 2020.0+ 移除的组件】、【provided 非 optional + 无 @ConditionalOnClass → NCDNF 崩溃族】、@ConfigurationProperties 挂零字段类、`spring.version` 死属性、sample `feign.hystrix.enabled` 为 Hoxton 废弃键、双通道冗余 + 死 hints。**全仓零消费方属纯孤岛**。应迁移至 Sentinel/Resilience4j。

- 详细文档：[i2f-springcloud-netflix-hystrix-starter](./i2f-springcloud/i2f-springcloud-netflix-hystrix-starter/readme.md)

### i2f-springcloud-netflix-openfeign-starter

> Spring Cloud OpenFeign 声明式 HTTP 客户端装配 Starter（本组「服务调用」子域，Netflix 三件套中唯一仍被 Spring Cloud 2021.0.8 积极维护的组件；**仅 1 个 29 行源文件 + 4 资源、零 i2f 内部 compile 依赖**，本组功能最重的“Enable...”注解代理）：`FeignAutoConfiguration` 类体零字段，`@EnableFeignClients` 触发 `FeignClientsRegistrar` 扫描应用包下 `@FeignClient` 接口并注册 HTTP 代理 Bean。模块名/包名中 "netflix" 为 Hoxton 历史分组遗留误导（OpenFeign 非 Netflix 组件）。瑕疵含【provided 非 optional + 无 @ConditionalOnClass → NCDNF 崩溃族】、默认开启全局 Feign 扫描（违反显式优于隐式原则）、sample 含大量 Hoxton 废弃键（feign.hystrix.enabled/hystrix.command/ribbon）、@ConfigurationProperties 挂零字段类、双通道冗余 + 死 hints。**全仓零消费方属纯孤岛**。

- 详细文档：[i2f-springcloud-netflix-openfeign-starter](./i2f-springcloud/i2f-springcloud-netflix-openfeign-starter/readme.md)

### i2f-springcloud-netflix-ribbon-starter

> Netflix Ribbon 客户端负载均衡装配 Starter（本组「客户端负载均衡」子域，`loadbalancer-starter` 的旧 Ribbon 对偶、已 EOL；**仅 1 个 27 行源文件 + 4 资源、零 i2f 内部 compile 依赖**，本组最纯粹空壳件——类体零字段、零 @Enable 注解、零 @Bean，仅 `InitializingBean` 打一行日志，引与不引行为完全一致）：Ribbon 无 `@EnableRibbon` 注解，激活靠 classpath 存在 + `@LoadBalanced`，本件无法通过注解启用 Ribbon，退化为纯日志标记。与 `netflix-hystrix-starter` 同为「Hoxton 版本时间胶囊」（模块级自引 Boot 2.3.7/Cloud Hoxton.SR12 BOM 覆盖根 2.7.18/2021.0.8）。相对规范处：provided 正确标了 optional（无 NCDNF 崩溃风险）。瑕疵含【BOM 覆盖架构级】、纯空壳零功能、开关名不副实（enable=false 不能关 Ribbon）、sample L28 `${client-name}` 未注释占位符、spring.version 死属性、sample 引用已弃用 Ribbon 超时键。**全仓零消费方属纯孤岛**。应迁移至 LoadBalancer。

- 详细文档：[i2f-springcloud-netflix-ribbon-starter](./i2f-springcloud/i2f-springcloud-netflix-ribbon-starter/readme.md)

### i2f-springcloud-netflix-zuul-starter

> Netflix Zuul 1.x 边缘网关装配 Starter（本组「服务网关」子域的 Servlet 阻塞式分支，与响应式 `gateway-starter` 互为新/旧网关对偶、已 EOL；**2 个 Java 源文件（`ZuulAutoConfiguration` 25 行 + 真实过滤器 `ZuulResponseCharsetFilter` 50 行）+ 4 资源、零 i2f 内部 compile 依赖**，本组 Netflix 薄壳件中少见的带实际增强逻辑件）：`ZuulAutoConfiguration` 类体零字段、`@EnableZuulProxy` 布尔门代理；`ZuulResponseCharsetFilter`（`extends ZuulFilter` pre/0）在响应写出前 `setCharacterEncoding(charset)` 规避中文乱码。与 `netflix-hystrix`/`netflix-ribbon` 同为「Hoxton 版本时间胶囊」（模块级自引 Boot 2.3.7/Cloud Hoxton.SR12 BOM 覆盖根 2.7.18/2021.0.8）。瑕疵含【BOM 覆盖架构级】、【provided 非 optional + 无 @ConditionalOnClass → NCDNF 崩溃族】、ZuulFilter 被误登记为自动配置类（应用 @Bean 暴露）、`@Component`+双通道登记重叠、元数据仅登记 enable 而 response-charset-filter.enable/charset 两功能键未登记、hints 拷贝死条目、@ConfigurationProperties 挂零字段类、spring.version 死属性、run() 恒 null/shouldFilter 恒 true 无差别设编码。**全仓零消费方属纯孤岛**。应迁移至 Spring Cloud Gateway。

- 详细文档：[i2f-springcloud-netflix-zuul-starter](./i2f-springcloud/i2f-springcloud-netflix-zuul-starter/readme.md)

### i2f-springcloud-refresh-starter

> Spring Cloud Context 动态刷新装配 Starter（本组「配置外部化与动态刷新」子域，与 `config-client-starter` 互补——后者负责取新配置、本件负责怎么触发刷新；**3 源文件 60+60+64 行、本组功能最完整的自研件、唯一依赖 i2f 自研 `i2f-otpauth` 的 Spring Cloud 件**）：在官方 `ContextRefresher`/`RefreshScope` 之上叠加「定时自动刷新」`AutoRefreshConfiguration`（默认 5min）+ 「TOTP 二次口令鉴权的 REST 手动刷新端点」`RefreshController`（`POST /refresh/trigger`，默认关）。`RefreshAutoConfiguration` 经 `@Import` 装配三能力、`@EventListener` 监听 `EnvironmentChangeEvent`。无 Hoxton BOM 覆盖（不同于 netflix 三件）。瑕疵含【高危·线程池泄漏】`afterPropertiesSet` 先建池后判 `delayTime<=0` 且 return 时不 shutdown、【高危·鉴权旁路】`totpKey==null` 时端点完全无鉴权可被滥用、【元数据 vs 代码默认值背离】`api-refresh.enable` 元数据 true/代码 false、`Base32.decode(encode(x))` 编解码空转、Base32 靠传递依赖 i2f-codec 未直接声明、入口缺 @Configuration lite、@ConditionalOnBean 顺序敏感。**全仓零消费方属纯孤岛**。

- 详细文档：[i2f-springcloud-refresh-starter](./i2f-springcloud/i2f-springcloud-refresh-starter/readme.md)

### i2f-springcloud-sleuth-starter

> Spring Cloud Sleuth 分布式链路跟踪装配 Starter（本组「链路跟踪」子域，与 `zipkin-starter` 为观测性配对——sleuth 生成 traceId/注 MDC、zipkin 上报 span；**仅 1 个 27 行零字段源文件 + 4 资源、零 i2f 内部 compile 依赖**，本组典型**纯空壳转发件**，与 loadbalancer/eureka-client/server/ribbon 同族）：`SleuthAutoConfiguration` 类体零字段，`afterPropertiesSet()` 仅打一行 `SleuthConfig config done.`，无任何 `@Bean`/自定义 Sampler/属性绑定，链路跟踪能力全靠使用方自引官方 `spring-cloud-starter-sleuth`。相对规范处：自动配置类带了 `@Configuration`（不同于 gateway lite 族，但无 @Bean 故无意义）。瑕疵含【纯空壳零增强】@ConfigurationProperties 挂零字段类、enable 由 SpEL 直读非绑定、【provided 非 optional 不传递·名不副实】、【无 @ConditionalOnClass → NCDNF 崩溃族】、【开关语义误导】enable=false 关不掉跟踪（真正须 spring.sleuth.enabled=false）、**【sample 高危误导】写废弃键 spring.sleuth.sampler.rate/percentage（3.1.x 正确为 .probability 0.0-1.0）两值均不生效且互相冲突**、@Data/无参构造冗余、双通道登记冗余、元数据 hints 死条目拷贝。**全仓零消费方属纯孤岛**。

- 详细文档：[i2f-springcloud-sleuth-starter](./i2f-springcloud/i2f-springcloud-sleuth-starter/readme.md)

### i2f-springcloud-zipkin-starter

> Zipkin 链路跟踪数据上报装配 Starter（本组「链路跟踪/可观测性」子域、**本组最后一个建档模块**，与 `sleuth-starter` 为上报端/生成端配对且源码逐行对称（同作者同日 2022/5/28 19:45）；**仅 1 个 27 行零字段源文件 + 4 资源、零 i2f 内部 compile 依赖**，本组典型**纯空壳转发件**）：`ZipkinAutoConfiguration` 类体零字段，`afterPropertiesSet()` 仅打一行 `ZipkinConfig config done.`，无任何 `@Bean`/自定义 Reporter/Sender/属性绑定。pom 代引 `spring-cloud-sleuth-zipkin`（provided 未 optional）——它是 Sleuth 3.1.x 中承载 Zipkin 自动配置、绑定 `spring.zipkin.*` 的正式组件（官方已弃用 spring-cloud-starter-zipkin），但须 Sleuth tracing 核心在位才产出可上报 span，本件不引核心。相对规范处：自动配置类带了 `@Configuration`（不同于 gateway lite 族，但无 @Bean 故无意义）。瑕疵含【纯空壳零增强】@ConfigurationProperties 挂零字段类、enable 由 SpEL 直读非绑定、【本件对上报链路零贡献·名带 starter 实为占位壳】、【provided 非 optional 不传递】、【无 @ConditionalOnClass → NCDNF 崩溃族】、【开关语义误导】enable=false 关不掉上报（真正须 spring.zipkin.enabled=false）、【sample 依赖未引入的 Sleuth 核心】键拼写正确但单独引无 span 来源、@Data/无参构造冗余、双通道登记冗余、元数据 hints 死条目拷贝。**全仓零消费方属纯孤岛**。

> 至此 `i2f-springcloud` 组 22 个真实模块（跳过 `test-gateway-swl`）已全部建档并登记索引。

- 详细文档：[i2f-springcloud-zipkin-starter](./i2f-springcloud/i2f-springcloud-zipkin-starter/readme.md)

## i2f-tools

> 开发/构建辅助工具集合，提供与工程构建、代码生成集成的独立能力。

### i2f-jdbc-procedure-idea-plugin

> IntelliJ IDEA 平台桌面插件「XProc4J 智能开发插件」（`i2f-tools` 组第 2 个建档模块，全仓**唯一 Gradle + `org.jetbrains.intellij` 构建件、唯一 JetBrains 平台件**，与运行期 Starter / 构建期 Maven 插件性质根本不同；`<id>` `i2f.turbo.jdbc-procedure-plugin`、`<name>` XProc4J、`version` 1.0、JDK17、sinceBuild 232/untilBuild 253.*；**规模 601 个 Java 源文件**，本组最重）：为 XProc4J（XML 存储过程）配置与 4 门自定义语言（TinyScript `.tis`/Ognl `.ognl`/Funic `.fic`/Funvi `.fvi`）提供完整 IDE 能力——标签名→Java 类/refid→定义引用跳转、`$变量` 高亮、SQL/Java/Groovy/JS/Velocity 等多语言注入、补全、DOM 描述、行标记导航、断点调试（xdebugger breakpointType + positionManagerFactory）、Live Templates、Oracle→XProc4J/TinyScript/OGNL 语法转换（ANTLR4）。功能真实完整、非空壳。瑕疵集中在**工程治理与可复现性**：【完全游离 Maven reactor，Gradle 无 parent、未列入 i2f-tools modules、version 1.0 脱离 1.0-jdk8】、【硬编码本机 IDEA localPath `C:\Program Files\...\2024.1`、下载式 version/type 被注释，换机即构建失败·高危】、【仓库硬编码阿里云、mavenCentral 注释】、【`lib/` 目录缺失且未被 gitignore 排除 → flatDir 声明的 `i2f-extension-xproc4j:1.0-jdk8` 内部依赖干净克隆不可解析，buildPlugin 失败】、【自带 plugin-intro.md 版本矛盾：称 IDEA>=2021.1/示例 jar `...-2021.1-211.jar` 但 sinceBuild=232、产物实为 `jdbc-procedure-plugin-1.0.jar`】、【intro 依赖清单漂移漏列 yaml/intelliLang】、【目录/rootProject.name/`<name>`/`<id>` 四处命名不一致】、【plugin.xml `<depends>xml` 与 gradle 注释掉 xml 供给不对齐】、【ANTLR/Grammar-Kit 生成码手工提交进 src 无再生任务】、【静态块起常驻 15s 轮询守护线程】、【Test*Debugger 挂 main 源集】。与运行期 `i2f-extension-xproc4j` 为「框架/IDE 前端」配对，自成产品不被 compile 消费。

- 详细文档：[i2f-jdbc-procedure-idea-plugin](./i2f-tools/i2f-jdbc-procedure-idea-plugin/readme.md)

### i2f-maven-plugin

> i2f 构建期 Maven 插件（`i2f-tools` 组首个建档模块，全仓**唯一 `packaging=maven-plugin`** 件，与 springboot/springcloud 组的运行期 Starter 性根本不同）：提供单目标 `i2f:spi`（`SpiComponentScanMojo` 341 行），绑定 `process-classes` 阶段，基于 ASM（`SKIP_CODE|DEBUG|FRAMES` 零类加载）扫描 `target/classes` 下标 `@Spi` 的 class，为 `value` 声明的接口自动生成并合并 `META-INF/services/` 描述文件。与 `i2f-spi-annotations`（注解契约）/`i2f-spi`（`ServiceLoader`）构成「声明→构建期生成→运行期加载」SPI 闭环，本件为中枢。功能完整无 NPE 硬伤，瑕疵集中在工程治理层：【游离 reactor、无 `<parent>` 且未列入 `i2f-tools` 的 modules、`version=1.0` 脱离 1.0-jdk8 约定、需单独 install、**全仓零消费方**】、【自带 readme 「项目结构」谎称 `Spi.java` 在本模块内（实际在 i2f-spi-annotations）、与自身依赖说明矛盾】、【`maven-project:2.2.1` Maven2 死依赖源码零引用】、【`i2f.version` 死属性】、【`requiresDependencyResolution=COMPILE` 多余开销】、【合并静默丢弃注释/空行、删除类后旧条目残留（幂等仅对新增成立）】。

- 详细文档：[i2f-maven-plugin](./i2f-tools/i2f-maven-plugin/readme.md)
