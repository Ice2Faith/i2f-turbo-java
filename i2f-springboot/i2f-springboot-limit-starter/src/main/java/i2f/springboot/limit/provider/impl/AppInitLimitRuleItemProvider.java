package i2f.springboot.limit.provider.impl;

import i2f.springboot.limit.data.LimitRuleItem;
import i2f.springboot.limit.properties.LimitRuleProperties;
import i2f.springboot.limit.provider.LimitRuleItemProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/12 14:49
 */
@ConditionalOnExpression("${i2f.springboot.rule-provider.app-init.enable:true}")
@Data
@NoArgsConstructor
@Component
public class AppInitLimitRuleItemProvider implements LimitRuleItemProvider {
    @Autowired
    private LimitRuleProperties properties;

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public List<LimitRuleItem> getRules(String appName) {
        List<LimitRuleItem> rules = properties.getRules();
        return rules==null?new ArrayList<>():rules;
    }
}
