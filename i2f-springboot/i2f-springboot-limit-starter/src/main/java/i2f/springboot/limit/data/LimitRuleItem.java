package i2f.springboot.limit.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/12 14:19
 */
@Data
@NoArgsConstructor
public class LimitRuleItem {
    protected String limitType;
    protected String typeKey;
    protected LimitRule rule;

    public LimitRuleItem(String limitType, String typeKey, LimitRule rule) {
        this.limitType = limitType;
        this.typeKey = typeKey;
        this.rule = rule;
    }
}
