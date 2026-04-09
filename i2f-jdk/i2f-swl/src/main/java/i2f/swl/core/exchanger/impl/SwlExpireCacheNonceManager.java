package i2f.swl.core.exchanger.impl;

import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.cache.std.expire.IExpireCache;
import i2f.swl.core.exchanger.SwlNonceManager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2026/4/9 10:39
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlExpireCacheNonceManager implements SwlNonceManager {
    public static final String NONCE_PREFIX_KEY = "swl:nonce:";

    private IExpireCache<String, String> cache = new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    private String cacheKeyPrefix;

    public String cacheKey(String key) {
        String cacheKeyPrefix = this.cacheKeyPrefix;
        if (cacheKeyPrefix == null || cacheKeyPrefix.isEmpty()) {
            return key;
        }
        return cacheKeyPrefix + ":" + key;
    }

    public String nonceKey(String nonce) {
        return NONCE_PREFIX_KEY + nonce;
    }

    @Override
    public boolean contains(String nonce) {
        return cache.exists(cacheKey(nonceKey(nonce)));
    }

    @Override
    public void set(String nonce, long timeoutSeconds) {
        cache.set(cacheKey(nonceKey(nonce)),nonce,timeoutSeconds, TimeUnit.SECONDS);
    }
}
