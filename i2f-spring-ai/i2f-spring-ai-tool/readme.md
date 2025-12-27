# spring bean ai 工具自动注册

- SpringBeanMethodToolCallbackProvider 可以自动扫描所有Bean，将带有 @AiTool 的Bean作为 @Tool 的AI工具
- 所以，在 @AiTool 标注的Bean中，应该包含使用 @Tool 标注的 AI 方法
- 建议将 SpringBeanMethodToolCallbackProvider 配置为 @Lazy 的实例化
- 以方便尽可能的将所有 @AiTool 标注的Bean纳入

## 使用方法

### 编写配置类

```java

@Data
@NoArgsConstructor
@Lazy
@Configuration
public class McpAiToolsAutoConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringBeanMethodToolCallbackProvider springBeanMethodToolCallbackProvider() {
        return new SpringBeanMethodToolCallbackProvider(applicationContext);
    }
}
```

- 将 SpringBeanMethodToolCallbackProvider 注册为 Bean
- 同时，使用 @Lazy 标注，尽可能等其他 Bean 都初始化完毕

### 编写工具类

- 然后，就可以编写你自己的工具方法了
- 下面举例说明

```java

@AiTools
@Component
public class DateTools {

    public static final DateTimeFormatter CURRENT_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "'current year:' yyyy\n" +
                    "'current month:' MM\n" +
                    "'current day:' dd\n" +
                    "'current hour(24-hour clock system):' HH\n" +
                    "'current minute:' mm\n" +
                    "'current second:' ss\n" +
                    "'current millisecond:' SSS\n" +
                    "'current week:' EEEE\n" +
                    "'datetime localized zone:' O\n" +
                    "'当前年份:' yyyy\n" +
                    "'当前月份:' MM\n" +
                    "'当前日期、号数:' dd\n" +
                    "'当前小时数(24小时制):' HH\n" +
                    "'当前分钟:' mm\n" +
                    "'当前秒数:' ss\n" +
                    "'当前毫秒数:' SSS\n" +
                    "'当前星期:' EEEE\n" +
                    "'日期时间的时区:' O\n" +
                    "");

    @Tool(description = "get current date in user's timezone,time,year,month,hour,minute,second,millisecond,week,zone,.etc detail information/" +
            "获取当前用户所在时区的（现在、今天）的日期、时间、年份、月份、小时、分钟、秒数、毫秒、星期、时区等详细信息")
    public String getCurrentDateTimeDescription() {
        String text = CURRENT_TIME_FORMATTER.format(LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()));
        return text;
    }
}

```

- 编写一个 Bean 组件，也就是使用 @Component 标注为一个 spring-bean
- 另外，使用 @AiTools 标注是一个 AI 工具组件
- 然后，就可以在其中编写一系列的由 @Tool 标注的方法了

### 将 provider 添加到 ChatClient 中使用即可

- 这里就不再继续讲解了
- spring-ai 的官网已经足够详细的进行说明了

```java
ChatClient.Builder builder = ChatClient.builder(chatModel);
builder.

defaultToolCallbacks(springBeanMethodToolCallbackProvider);
```
  