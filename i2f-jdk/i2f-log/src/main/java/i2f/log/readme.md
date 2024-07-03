# log 日志框架设计

## 设计原理

- 使用Slf4j的门面设计思路
- 采用Holder进行静态设置必要组件
- 采用Spi进行加载可拓展组件

## 主要组件

- LoggerFactory 用于产生 ILogger
    - 从Spi中检索第一个可用的 SpiLoggerProvider 来产生 ILogger
    - 如果Spi不可用，则使用 DefaultLogger 产生 ILogger
- ILogger 日志输出的门面接口
    - 由 LoggerFactory 产生（建议使用方式）
- ILogWriter 用于执行日志的写出
    - 对 ILogger 产生的日志进行写出
    - 默认实现为广播写出 DefaultBroadcastLogWriter
- ILogDecider 写出决策器
    - 决定了哪些日志应该进行输出
    - 哪些日志不需要输出
    - 类似：logging.level 配置
    - 默认实现为基于类名的ant匹配控制 DefaultClassNamePattenLogDecider
- LogHolder 执行环境的持有器
    - 决定了使用何种 ILogWriter / ILogDecider
    - 可以进行替换其中的静态变量以进行拓展或者其他功能
- StdoutPlanTextLogWriter 控制台日志写出
    - 默认情况下，配置了控制台的日志输出
- DefaultBroadcastLogWriter 广播写出器
    - 实现将日志写出到多个写出源
    - 以满足多种自定义的需求
    - 例如，推送到日志中心、保存ES，保存数据库，保存文件等
    - 默认加载控制台输出，以及从Spi加载的 SpiLogWriterProvider 提供的写出器
- ILogMsgFormatter 日志消息格式化器
  - 实现对日志消息按照format使用args进行格式化为字符串
  - 默认提供两个实现
  - StringFormatLogMsgFormatter
    - 使用 String.format(format,args) 进行格式化
  - IndexedPattenLogMsgFormatter (默认加载到LogHolder中)
    - 使用 RegexUtil.format(format, args) 进行格式化
- ILogDataFormatter 日志数据格式化器
  - 实现对日志数据的格式化，将LogData格式化（序列化）为字符串
  - 默认实现为 DefaultLogDataFormatter
  - 在其他Log框架中，这也叫做Layout
- LogConfiguration 提供基于LogProperties的配置入口
- LogProperties 提供一些基础的配置
- PropertiesFileLogPropertiesLoader 提供将properties文本转换为LogProperties的实用方法
- IndexedPattenLogMsgFormatter
  - 按照参数索引的方式进行格式化
  - 下面是格式化参数讲解

```shell
/**
     * 按索引格式化字符串
     * 匹配模式：{索引 格式修饰符:格式}
     * 索引如果不指定，则直接使用占位符的索引
     * 格式修饰符，可以用于指定特定的标志位，限定为一个字符，可以不使用
     * 其中格式可以不指定
     * 匹配的格式举例如下：
     * {}
     * {:yyyy-MM}
     * {0}
     * {-1}
     * {2:yyyy-MM-dd HH:mm:ss}
     * {3: \{yyyyMMdd\}-HHmmss.SSS}
     * { 4 :%02d}
     * { 5 t:%02d}
     * {6e:}
     * 特别说明：
     * 索引支持负值，表示从后往前的负索引，上面-1就是案例，-1表示最后一个元素的索引，原因是没有-0的说法
     * 冒号之前的索引，支持前后空白符号，上面的4就是案例
     * 冒号及后面的格式，不会忽略空白符号，上面3就是案例
     * 格式修饰符必须紧跟冒号，否则不会匹配，上面5就是案例
     * 同时，如果只写了冒号，后面的格式为空串，则忽略这个模式，上面6就是案例
     * 在格式中，\{和\}会被恢复转义替换为{和}
     * 其他的\后面的转义不会处理，保留原状
     * 作为格式，
     * 对于时间类型，指定为日期时间的格式，例如上面的2/3
     * 否则，则使用String.format使用的格式，例如上面的4
     * 注意，如果参数类型是数组类型，则不受格式影响
     * 格式修饰符
     * s: 使用String.format进行格式化
     * t: 打印的是元素的class的简单类名class.getSimpleName()
     * T: 打印的是元素的class的全限定类名class.getName()
     * e: 当元素为null时，打印空字符串，而不是"null"
     * c: 当元素为Throwable时，打印包含堆栈
     *
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
      ...
    }
```

## 运作原理

- LoggerFactory 产生针对 location(class) 的 ILogger
    - 优先从Spi中获取 SpiLoggerProvider
    - 否则使用 DefaultLogger
- ILogger 写出到 ILogWriter
    - 根据 LogHolder 的 ILogDecider 判断是否进行输出
    - 写出日志到 LogHolder 的 ILogWriter
    - 默认情况下，使用 DefaultBroadcastLogWriter 写出到注册到其中的所有 ILogWriter

## 添加写出源或者适配目标日志框架

- 在默认使用 DefaultBroadcastLogWriter 时
- 可以借助 Spi 加载 SpiLogWriterProvider 添加写出源
- 也可以直接代码修改 LogHolder 中的变量添加写出源

## 使用示例

- 在下面的例子中
- 展示了添加输出 ILogWriter 到 FILE 中
- 以及一些简单的使用方式

```java
package i2f.log.test;

import i2f.clock.SystemClock;
import i2f.log.ILogger;
import i2f.log.LoggerFactory;
import i2f.log.decide.ILogDecider;
import i2f.log.decide.impl.DefaultClassNamePattenLogDecider;
import i2f.log.enums.LogLevel;
import i2f.log.format.impl.StringFormatLogMsgFormatter;
import i2f.log.holder.LogHolder;
import i2f.log.stdout.StdoutRedirectPrintStream;
import i2f.log.writer.DefaultBroadcastLogWriter;
import i2f.log.writer.ILogWriter;
import i2f.log.writer.impl.LocalFilePlanTextLogWriter;

import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2024/7/1 11:16
 * @desc
 */
public class TestLogger {
  public static ILogger staticLogger = LoggerFactory.getLogger();

  public static void main(String[] args) {
    StdoutRedirectPrintStream.redirectSysoutSyserr();
    ILogWriter writer = LogHolder.GLOBAL_WRITER;
    if (writer instanceof DefaultBroadcastLogWriter) {
      DefaultBroadcastLogWriter broadcastLogWriter = (DefaultBroadcastLogWriter) writer;
      broadcastLogWriter.getAsync().set(false);
      LocalFilePlanTextLogWriter fileWriter = new LocalFilePlanTextLogWriter();
      fileWriter.setFileLimitSize(3 * 1024 * 1024);
      LogHolder.registryWriter("FILE", fileWriter);
    }

    ILogDecider decider = LogHolder.GLOBAL_DECIDER;
    if (decider instanceof DefaultClassNamePattenLogDecider) {
      DefaultClassNamePattenLogDecider pattenLogDecider = (DefaultClassNamePattenLogDecider) decider;
      pattenLogDecider.setRootLevel(LogLevel.ALL);
//            LogHolder.registryDecideLevel("i2f.log.**", LogLevel.WARN);
    }

    // 设置为使用String.format
    LogHolder.GLOBAL_MSG_FORMATTER = new StringFormatLogMsgFormatter();
    staticLogger.info("this is main args: %s", Arrays.toString(args));

    System.out.println("sysout");

    // 设置为使用RegexUtil.format
    LogHolder.GLOBAL_MSG_FORMATTER = LogHolder.DEFAULT_MSG_FORMATTER;
    long bts = SystemClock.currentTimeMillis();
    for (int i = 0; i < 10 * 10000; i++) {
      ILogger logger = LoggerFactory.getLogger();
      logger.info("main scope logger: {}", SystemClock.currentTimeMillis());

      logger.debug("debug level,{}", SystemClock.currentTimeMillis());

      try {
        int a = 1 / 0;
      } catch (Exception e) {
        logger.error(e, "calculate error: { e:}", e.getMessage());
      }
    }
    long ets = SystemClock.currentTimeMillis();
    System.out.println("useTs:" + (ets - bts));
  }
}

```
