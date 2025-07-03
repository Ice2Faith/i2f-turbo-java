# Logger 标准定义

- 此除主要是定义标准的 Logger 以及 LoggerFactory
- 同时有一个简单的 StdioLogger 用于默认实现
- 涉及使用门面设计模式

## 用法

```java
import i2f.log.std.Logger;
import i2f.log.std.LoggerFactory;

public class Test {
    public static ILogger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        logger.info("main...");
    }
}



```

## 实现标准

- 实现 ILogger 或者继承 AbsLogger

```java
package com.xxx;

import i2f.log.std.data.LogData;
import i2f.log.std.enums.LogLevel;
import i2f.log.std.logger.AbsLogger;

public class MyLogger extends AbsLogger {
    protected String location;
    protected LogLevel controlLevel = LogLevel.INFO;

    public MyLogger(String location) {
        this.location = location;
    }

    @Override
    public void writeLogData(LogData data) {
        // ... 在此处实现你的日志逻辑，比如打印到控制台，保存到数据库等
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public boolean enableLevel(LogLevel level) {
        // ... 在此处，你可以根据 location 的完全限定类名 和 Level 结合
        // 用于实现控制哪些包或者那些类的日志启用的功能
        return level.level() < controlLevel.level();
    }
}
```

- 实现 LoggerProvider 用于产生 ILogger

```java
package com.xxx;

import i2f.log.std.ILogger;
import i2f.log.std.provider.LoggerProvider;

/**
 * @author Ice2Faith
 * @date 2025/7/3 17:10
 */
public class MyLoggerProvider implements LoggerProvider {

    public static final String NAME = "MyProvider";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ILogger getLogger(String location) {
        return new MyLogger(location);
    }
}
```

- 将自己的 LoggerProvider 实现类添加到 SPI文件中

- SPI 文件

```shell
META-INF/services/i2f.log.std.provider.LoggerProvider
```

- 项目文件结构
- 请注意，文件名就是全限定类名，没有多余的东西

```shell
java
resources
  META-INF
    services
      i2f.log.std.provider.LoggerProvider
```

- SPI 文件内容
- 内部每一行都是一个SPI类实现

```shell
com.xxx.MyLoggerProvider
```

