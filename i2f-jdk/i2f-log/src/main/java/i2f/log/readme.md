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
import i2f.log.holder.LogHolder;
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

        staticLogger.info("this is main args: %s", Arrays.toString(args));

        long bts = SystemClock.currentTimeMillis();
        for (int i = 0; i < 10 * 10000; i++) {
            ILogger logger = LoggerFactory.getLogger();
            logger.info("main scope logger: %d", SystemClock.currentTimeMillis());

            logger.debug("debug level,%d", SystemClock.currentTimeMillis());

            try {
                int a = 1 / 0;
            } catch (Exception e) {
                logger.error(e, "calculate error: %s", e.getMessage());
            }
        }
        long ets = SystemClock.currentTimeMillis();
        System.out.println("useTs:" + (ets - bts));
    }
}

```
