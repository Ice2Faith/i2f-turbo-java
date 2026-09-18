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

### i2f-spring-mvc-metadata

> 基于反射解析 Spring MVC Controller 的 API 元数据，提取 URL、HTTP 方法、参数、返回值与 Swagger 注释，为 API 文档生成提供结构化数据。

- 详细文档：[i2f-spring-mvc-metadata](./i2f-spring/i2f-spring-mvc-metadata/readme.md)

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

### i2f-springboot-ai-mcp-server

> MCP 服务端 Starter：将 Spring 容器中的 `@Tool`/`@Tools` 工具以 HMAC-SHA256 签名认证的 Simple MCP 协议（`/mcp/tool/list`、`/mcp/tool/call`）对外暴露，内置 Spring Web MVC（共享宿主 Web 端口）与 Netty（独立端口）双传输模式的自动装配，支持 nonce 防重放与请求级上下文透传。

- 详细文档：[i2f-springboot-ai-mcp-server](./i2f-springboot/i2f-springboot-ai-mcp-server/readme.md)

### i2f-springboot-ai-mcp-client

> MCP 客户端 Starter：按 `instances` 配置将远程 MCP Server 注册为本地 `McpToolProvider` Bean，内置三套客户端实现——自研 Simple MCP 私协议（HMAC-SHA256 签名认证）、自研标准 JSON-RPC Streamable HTTP（`Mcp-Session-Id` 会话管理）与 solon-ai-mcp SDK（STDIO/SSE/STREAMABLE 等多种通道），供 AI 工具网关聚合为「实例名.工具名」形式的动态工具。

- 详细文档：[i2f-springboot-ai-mcp-client](./i2f-springboot/i2f-springboot-ai-mcp-client/readme.md)

## i2f-tools

> 开发/构建辅助工具集合，提供与工程构建、代码生成集成的独立能力。

### i2f-maven-plugin

> i2f 构建期 Maven 插件，提供 `i2f:spi` 目标，基于 ASM 扫描 `@Spi` 注解，自动生成并合并 `META-INF/services/` 服务描述文件。

- 详细文档：[i2f-maven-plugin](./i2f-tools/i2f-maven-plugin/readme.md)
