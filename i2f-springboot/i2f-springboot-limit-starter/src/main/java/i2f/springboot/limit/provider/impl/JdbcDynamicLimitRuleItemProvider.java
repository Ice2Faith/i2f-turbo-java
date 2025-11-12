package i2f.springboot.limit.provider.impl;

import i2f.springboot.limit.data.LimitRule;
import i2f.springboot.limit.data.LimitRuleItem;
import i2f.springboot.limit.provider.LimitRuleItemProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2025/11/12 15:33
 */
@ConditionalOnExpression("${i2f.springboot.rule-provider.jdbc-dynamic.enable:false}")
@Data
@NoArgsConstructor
@Component
public class JdbcDynamicLimitRuleItemProvider implements LimitRuleItemProvider {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public List<LimitRuleItem> getRules(String appName) {
        List<LimitRuleItem> ret = Collections.synchronizedList(new ArrayList<>(300));
        Stream<JdbcLimitRuleItem> stream = jdbcTemplate.queryForStream(
                "select * from limit_rule_item where app_name = ? and status=1 order by limit_order asc",
                new SingleColumnRowMapper<>(JdbcLimitRuleItem.class),
                appName);
        stream.forEach(item -> {
            if (item.getLimitType() == null || item.getLimitType().isEmpty()) {
                return;
            }
            if (item.getTypeKey() == null || item.getTypeKey().isEmpty()) {
                return;
            }
            if (item.getLimitCount() == null) {
                return;
            }
            if (item.getLimitTtl() == null) {
                return;
            }
            if (item.getLimitOrder() == null) {
                item.setLimitOrder(Integer.MAX_VALUE);
            }
            LimitRuleItem rule = new LimitRuleItem(
                    item.getLimitType(),
                    item.getTypeKey(),
                    new LimitRule(item.getLimitCount(),
                            item.getLimitTtl(),
                            item.getLimitOrder())
            );
            ret.add(rule);
        });

        return ret;
    }
}
