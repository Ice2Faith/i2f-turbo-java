package i2f.springboot.jdbc.bql.procedure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    protected String xmlLocations = DEFAULT_XML_LOCATIONS;

    protected String watchingDirectories = DEFAULT_WATCHING_DIRECTORIES;

    protected long refreshXmlIntervalSeconds = -1;

    protected int maxPreloadCount = 1;

    protected long slowSqlMinMillsSeconds = TimeUnit.SECONDS.toMillis(5);

    protected long slowProcedureMillsSeconds = TimeUnit.SECONDS.toMillis(30);

    protected long slowNodeMillsSeconds = TimeUnit.SECONDS.toMillis(15);

    protected List<String> invokeLogPredicateRegexes = new ArrayList<>();

    protected List<String> mapperPackages = new ArrayList<>();
}
