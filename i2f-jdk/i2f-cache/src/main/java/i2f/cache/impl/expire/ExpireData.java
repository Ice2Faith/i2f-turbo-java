package i2f.cache.impl.expire;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/6/26 10:57
 * @desc
 */
@Data
public class ExpireData<V> {
    protected V data;
    protected long expireTs = -1;

    public ExpireData(V data) {
        this.data = data;
    }

    public ExpireData(V data, long expireTs) {
        this.data = data;
        this.expireTs = expireTs;
    }
}
