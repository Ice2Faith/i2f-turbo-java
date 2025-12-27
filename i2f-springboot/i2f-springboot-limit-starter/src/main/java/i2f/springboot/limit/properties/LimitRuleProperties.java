package i2f.springboot.limit.properties;

import i2f.springboot.limit.core.LimitConsts;
import i2f.springboot.limit.data.LimitRuleItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/11 17:07
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.limit")
public class LimitRuleProperties {
    protected String appName = LimitConsts.DEFAULT_APP_NAME;
    protected FilterProperties filter = new FilterProperties();
    protected List<LimitRuleItem> rules = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class FilterProperties {
        int order = 0;
        String pattern = "/*";
    }
}
