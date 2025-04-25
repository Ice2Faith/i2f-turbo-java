package i2f.springboot.jdbc.bql.procedure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ice2Faith
 * @date 2024/4/24 16:34
 * @desc
 */
@Data
@ConfigurationProperties(prefix = "jdbc.xml.procedure")
public class SpringJdbcProcedureProperties {
    public static final String DEFAULT_XML_LOCATIONS = "classpath*:procedure/**/*.xml;classpath*:com/**/procedure/*.xml";
    private boolean enable = true;

    private boolean debug = false;

    private boolean reportOnBoot = true;

    private String xmlLocations = DEFAULT_XML_LOCATIONS;

    private long refreshXmlIntervalSeconds = -1;
}
