package i2f.lock.impl;

import i2f.lock.ILock;
import i2f.lock.ILockProvider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:21
 */
public class JdkCacheLockProvider implements ILockProvider {
    public static final String NAME = "jvm";

    protected ConcurrentHashMap<String, ILock> cache = new ConcurrentHashMap<>();

    public JdkCacheLockProvider() {
    }

    public JdkCacheLockProvider(ConcurrentHashMap<String, ILock> cache) {
        if (cache != null) {
            this.cache = cache;
        }
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public ILock getLock(String key) {
        return cache.computeIfAbsent(key, k -> new JdkLock());
    }
}
