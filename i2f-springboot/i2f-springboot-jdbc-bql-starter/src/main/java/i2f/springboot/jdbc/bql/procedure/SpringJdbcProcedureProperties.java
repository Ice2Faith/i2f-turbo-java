package i2f.springboot.jdbc.bql.procedure;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:34
 * @desc
 */
@Data
@ConfigurationProperties(prefix = "xproc4j")
public class SpringJdbcProcedureProperties {
    public static final String DEFAULT_XML_LOCATIONS = "classpath*:procedure/**/*.xml;classpath*:com/**/procedure/*.xml";
    public static final String DEFAULT_WATCHING_DIRECTORIES = "classpath*:procedure/;classpath*:com/";

    protected boolean enable = true;

    protected boolean debug = false;

    protected boolean reportOnBoot = true;

    protected ReportOptions reportOptions = new ReportOptions();

    protected String xmlLocations = DEFAULT_XML_LOCATIONS;

    protected String watchingDirectories = DEFAULT_WATCHING_DIRECTORIES;

    protected long refreshXmlIntervalSeconds = -1;

    protected int maxPreloadCount = 1;

    protected long slowSqlMinMillsSeconds = TimeUnit.SECONDS.toMillis(5);

    protected long slowProcedureMillsSeconds = TimeUnit.SECONDS.toMillis(30);

    protected long slowNodeMillsSeconds = TimeUnit.SECONDS.toMillis(15);

    protected List<String> invokeLogPredicateRegexes = new ArrayList<>();

    protected List<String> mapperPackages = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class ReportOptions{
        // 双单引号('')，可能是错误的转义
        protected boolean checkDoubleSingleQuote=false;
        // 双管道符(||)，可能是错误使用
        protected boolean checkDoublePipe=false;
        // 括号、引号，占位符的花括号等封闭语义符号是否封闭，没封闭可能是错误
        protected boolean checkEnclosedChar=false;
        // 对调用其他过程时，未接受返回值的情况，可能导致返回值丢失
        protected boolean checkCallResult=false;
        // 入参值为空白字符串时，可能是忘记写入参
        protected boolean checkBlankAttribute=false;
        // 是否允许调用子过程时忽略出参，虽然大多数情况下忽略也可以，但是如果既是入参也是出参的时候，如果忽略可能导致缺少入参
        protected boolean checkOutputArgument=false;

    }
}
