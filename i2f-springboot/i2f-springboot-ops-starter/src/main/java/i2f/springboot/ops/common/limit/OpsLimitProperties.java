package i2f.springboot.ops.common.limit;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/11/21 17:14
 * @desc
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "i2f.springboot.ops.limited")
public class OpsLimitProperties {
    public static final String KEY_PREFIX = "ops:limited:";
    public static final String KEY_ALL = KEY_PREFIX + "all";
    public static final String KEY_IP = KEY_PREFIX + "ip:";
    public static final String KEY_ANT_PATH = KEY_PREFIX + "ant-path:";

    protected String filterUrlPattern;
    protected LimitRule allRule = new LimitRule();
    protected EveryIpLimitRule everyIpRule = new EveryIpLimitRule();
    protected List<PathLimitRule> pathRules = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class PathLimitRule {
        protected String antPath;
        protected LimitRule rule = new LimitRule();
    }

    @Data
    @NoArgsConstructor
    public static class EveryIpLimitRule {
        protected int lruMaxSize = 8192;
        protected LimitRule rule = new LimitRule();
    }

    @Data
    @NoArgsConstructor
    public static class LimitRule {
        protected boolean enable = true;
        protected double ratePerSecond = 100;
        protected double capacity = 300;
    }

}
