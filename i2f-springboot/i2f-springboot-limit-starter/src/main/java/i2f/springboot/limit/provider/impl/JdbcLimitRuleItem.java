package i2f.springboot.limit.provider.impl;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/12 15:38
 */
@Data
@NoArgsConstructor
public class JdbcLimitRuleItem {
    protected String appName;
    protected String limitType;
    protected String typeKey;
    protected Integer limitCount=-1;
    protected Long limitTtl=-1L;
    protected Integer limitOrder=Integer.MAX_VALUE;
    protected Integer status;
}
