package i2f.limit.limiter;

import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.cache.std.expire.IExpireCache;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2022/5/6 17:42
 * @desc 做大次数限制器
 * 在触发了限制达到最大次数之后，将会限制，直到等待时间过后
 * 使用场景：
 * 多次登录失败后，一段时间内限制登录
 */
@Data
@NoArgsConstructor
public class MaxCountWaitTimeExpireKeyedLimiter implements IKeyedLimiter {
    // 达到多少次限流
    protected int maxCount;
    // 记录次数最大值，在maxCount和maxRecordsCount中取最大值
    protected int maxRecordsCount;
    // 等待时间
    protected int waitTime;
    // 等待时间单位
    protected TimeUnit waitTimeUint;

    protected IExpireCache<String, Object> cache = new ObjectExpireCacheWrapper<>(new MapCache<>());

    public IExpireCache<String, Object> getCache() {
        return cache;
    }

    public synchronized Integer getKeyCount(String key) {
        Integer ret = null;
        Object ocnt = getCache().get(key);
        if (ocnt != null) {
            try {
                String scnt = ocnt + "";
                ret = Integer.parseInt(scnt);
            } catch (Exception e) {

            }
        }
        return ret;
    }

    public synchronized void setKeyCount(String key, int cnt) {
        getCache().set(key, cnt + "", waitTime, waitTimeUint);
    }

    @Override
    public boolean hasLimit(String key, Object... args) {
        synchronized (this) {
            Integer cnt = getKeyCount(key);
            if (cnt != null) {
                if (cnt >= maxCount) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void limit(String key, Object... args) {
        synchronized (this) {
            Integer cnt = getKeyCount(key);
            if (cnt == null) {
                cnt = 0;
            }
            int max = Math.max(maxCount, maxRecordsCount);
            if (cnt < max) {
                cnt++;
            }
            setKeyCount(key, cnt);
        }
    }

    @Override
    public void unlimited(String key, Object... args) {
        synchronized (this) {
            getCache().remove(key);
        }
    }
}
