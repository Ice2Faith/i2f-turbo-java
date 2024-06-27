package i2f.springcloud.gateway.filters.global.repeat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/17 9:00
 * @desc
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "i2f.springcloud.gateway.filters.query-repeat")
public class RequestQueryRepeatProperties {

    private boolean enableRepeatJson = false;

    private boolean enableRepeatForm = true;

    private boolean enableRepeatXml = false;

    private int prefixCount = 1;

    private int order = 10;

    private List<String> repeatProps;

    private List<String> repeatListPathPattens;

    private List<String> repeatListPathExcludePattens;

}

