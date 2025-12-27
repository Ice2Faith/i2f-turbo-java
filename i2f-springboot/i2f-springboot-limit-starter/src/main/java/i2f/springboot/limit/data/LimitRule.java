package i2f.springboot.limit.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/11 16:24
 */
@Data
@NoArgsConstructor
public class LimitRule {
    protected int count = -1;
    protected long ttl = -1;
    protected int order = Integer.MAX_VALUE;

    public LimitRule(int count, long ttl) {
        this.count = count;
        this.ttl = ttl;
    }

    public LimitRule(int count, long ttl, int order) {
        this.count = count;
        this.ttl = ttl;
        this.order = order;
    }
}
