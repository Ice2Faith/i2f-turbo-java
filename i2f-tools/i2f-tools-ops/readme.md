# 运行环境

- 软件运行需要 java8+ 环境支持

# 程序启动

- 双击 startup.bat 启动即可
- 启动完成后会自动打开浏览器页面
- 如果没有自动打开浏览器
- 可以手动访问

```
http://localhost:8080/ops/
```

# 目录介绍

- lib
    - 依赖包目录
- lib-unload
    - 启动后根据java版本不同，部分依赖将会被移动到此目录
- logs
    - 运行日志
- plugins
    - 插件目录，动态加载jar包，可以放置自己的 starter 插件
    - 也可以放置自己的 classpath 资源以覆盖默认的资源
- resources
    - 配置目录
- runtime
    - 运行时临时目录
- skills
    - AI的技能目录
    - 可以参考现有的模式，编写自己的技能
    - 或者下载网络上的技能，添加到其中

# 拓展OpenAI的角色设定

- 角色设定配置文件路径

```
plugins/static/ops/open-ai/role-config.json
```

- 按照格式对应添加自己的角色即可
- 然后，按照现有格式，编写自己的角色设定 md 文件
- 保存即可

# 拓展OpenAI的技能

- 技能存放路径

```
skills
```

- 目录下的每一个目录就是一个技能
- 必须存在一个技能文档 SKILL.md/skill.md/index.md/readme.md
- 可以自己编写技能，也可以在网上下载技能放到此路径

# 主要配置

- i2f.springboot.ssh.tunnel.enable
    - 是否启用隧道
- i2f.springboot.ops.secure.cert
    - ops控制台的通信证书
    - 如果不填，系统启动时会自动生成一对，注意查看运行日志
- i2f.springboot.ops.datasource.datasourceMap
    - ops控制台的数据源配置
    - 可以在这里配置允许连接的数据源
- xproc4j.xml-locations
    - xproc4j 框架扫描XML文件的路径
- xproc4j.watching-directories
    - xproc4j 框架监听XML文件变化进行自动加载的路径
- ai.tools.file.root-path
    - AI的文件操作工具允许访问的根目录
    - 除此目录外，其他目录都不能访问

# 拓展OpenAI的工具

- 基于插件系统
- 可以将自己编写的 starter 添加到插件目录
- 这样就能够自动加载 starter 启动
- 一次，可以通过这种方式，拓展自己的AI工具
- 操作步骤
- 引入依赖包

```
lib/i2f-springboot-ops-starter-1.0-jdk8.jar
```

- 编写自己的工具类，并注册为 bean

```java
package i2f.tools.tools;

import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.tools.yi.GanZhiDate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ice2Faith
 * @date 2026/6/10 15:42
 * @desc
 */
@ConditionalOnExpression("${i2f.tools.gan-zhi.enable:true}") // 可选，条件配置
@Component // 注册为bean
@Tools // 标记为工具类
public class GanZhiTools {
    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Tool( // 标记为工具函数
            tags = {
                    AiTags.AUTO_VALUE // 标记是否允许自动调用，非Auto的都需要授权弹窗
            },
            description = "根据公历时间获取对应的生辰八字（节气四柱）" // 标记工具的功能及作用
    )
    public String get_gan_zhi_date(
            @ToolParam(value = "date", description = "公历时间，格式使用java标准的 yyyy-MM-dd HH:mm 格式，例如：2001-2-16 13:54") // 标记工具需要的参数名以及描述
            String date) {
        LocalDateTime ldate = LocalDateTime.parse(date, FMT);
        GanZhiDate ret = GanZhiDate.of(ldate);
        return "年柱：" + ret.getYear() + "\n"
                + "月柱：" + ret.getMonth() + "\n"
                + "日柱：" + ret.getDay() + "\n"
                + "时柱：" + ret.getHour() + "\n";
    }
}
```

- 将类添加到starter的配置文件中

```
src/main/resources/META-INF/spring.factories
```

- 添加自己的内容

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  i2f.tools.tools.GanZhiTools
```

- 建议同时添加到兼容的配置文件中

```
src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

- 添加自己的内容

```
i2f.tools.tools.GanZhiTools
```