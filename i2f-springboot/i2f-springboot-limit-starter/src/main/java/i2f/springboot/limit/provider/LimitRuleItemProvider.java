package i2f.springboot.limit.provider;

import i2f.springboot.limit.data.LimitRuleItem;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/12 14:21
 */
public interface LimitRuleItemProvider {
    boolean isDynamic();

    List<LimitRuleItem> getRules(String appName);
}
