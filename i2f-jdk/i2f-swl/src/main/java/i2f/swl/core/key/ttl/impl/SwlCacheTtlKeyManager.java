package i2f.swl.core.key.ttl.impl;

import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.clock.SystemClock;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.lru.LruMap;
import i2f.swl.core.key.impl.AbsSwlCacheKeyManager;
import i2f.swl.core.key.ttl.SwlTtlKeyManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2024/8/8 22:35
 * @desc
 */
public class SwlCacheTtlKeyManager extends AbsSwlCacheKeyManager implements SwlTtlKeyManager {


    protected IExpireCache<String, String> cache = new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LruMap<String, Map.Entry<AsymKeyPair, Long>> selfCache = new LruMap<>(1024);
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LruMap<String, Map.Entry<String, Long>> otherCache = new LruMap<>(1024);
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected AtomicReference<String> selfDefaultCache = new AtomicReference<>();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected AtomicReference<String> otherDefaultCache = new AtomicReference<>();

    @Override
    public AsymKeyPair getDefaultSelfKeyPair() {
        try {
            String selfAsymSign = getDefaultSelfAsymSign();
            return getSelfKeyPair(selfAsymSign);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public String getDefaultSelfAsymSign() {
        String str = selfDefaultCache.get();
        if (str != null) {
            return str;
        }
        String selfAsymSign = cache.get(cacheKey(DEFAULT_SELF_NAME));
        if (selfAsymSign != null) {
            selfDefaultCache.set(selfAsymSign);
        }
        return selfAsymSign;

    }

    @Override
    public void setDefaultSelfAsymSign(String selfAsymSign) {
        selfDefaultCache.set(selfAsymSign);
        if (selfAsymSign == null) {
            cache.remove(cacheKey(DEFAULT_SELF_NAME));
            return;
        }
        cache.set(cacheKey(DEFAULT_SELF_NAME), selfAsymSign);

    }

    @Override
    public AsymKeyPair getSelfKeyPair(String selfAsymSign) {
        if (selfAsymSign == null) {
            return null;
        }
        Map.Entry<AsymKeyPair, Long> entry = selfCache.get(selfAsymSign);
        if (entry != null) {
            if (SystemClock.currentTimeMillis() > entry.getValue() && entry.getValue() >= 0) {
                selfCache.remove(selfAsymSign);
                return null;
            }
            return entry.getKey().copy();
        }
        AsymKeyPair ret = null;

        String key = cacheKey(getSignName(selfAsymSign, true));
        String str = cache.get(key);
        long expireTs = -1;
        if (str != null) {
            String[] arr = str.split("=", 2);
            if (arr.length == 2) {
                expireTs = Long.parseLong(arr[0]);
                str = arr[1];
                ret = deserializeKeyPair(str);
            }
        }

        if (ret != null) {
            selfCache.put(selfAsymSign, new AbstractMap.SimpleEntry<>(ret.copy(), expireTs));
        }
        return ret;
    }

    @Override
    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair keyPair) {
        if (selfAsymSign == null || keyPair == null) {
            return;
        }
        selfCache.put(selfAsymSign, new AbstractMap.SimpleEntry<>(keyPair.copy(), -1L));
        String str = serializeKeyPair(keyPair);
        cache.set(cacheKey(getSignName(selfAsymSign, true)), -1 + "=" + str);
    }

    @Override
    public String getDefaultOtherPublicKey() {
        try {
            String otherAsymSign = getDefaultOtherAsymSign();
            return getOtherPublicKey(otherAsymSign);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public String getDefaultOtherAsymSign() {
        String str = otherDefaultCache.get();
        if (str != null) {
            return str;
        }

        String otherAsymSign = cache.get(cacheKey(DEFAULT_OTHER_NAME));
        if (otherAsymSign != null) {
            otherDefaultCache.set(otherAsymSign);
        }
        return otherAsymSign;

    }

    @Override
    public void setDefaultOtherAsymSign(String otherAsymSign) {
        otherDefaultCache.set(otherAsymSign);
        if (otherAsymSign == null) {
            cache.remove(cacheKey(DEFAULT_OTHER_NAME));
            return;
        }
        cache.set(cacheKey(DEFAULT_OTHER_NAME), otherAsymSign);

    }

    @Override
    public String getOtherPublicKey(String otherAsymSign) {
        if (otherAsymSign == null) {
            return null;
        }
        Map.Entry<String, Long> entry = otherCache.get(otherAsymSign);
        if (entry != null) {
            if (SystemClock.currentTimeMillis() > entry.getValue() && entry.getValue() >= 0) {
                otherCache.remove(otherAsymSign);
                return null;
            }
            return entry.getKey();
        }
        String ret = null;

        String str = cache.get(cacheKey(getSignName(otherAsymSign, false)));
        long expireTs = -1;
        if (str != null) {
            String[] arr = str.split("=", 2);
            if (arr.length == 2) {
                expireTs = Long.parseLong(arr[0]);
                str = arr[1];
                AsymKeyPair keyPair = deserializeKeyPair(str);
                ret = keyPair.getPublicKey();
            }
        }

        if (ret != null) {
            otherCache.put(otherAsymSign, new AbstractMap.SimpleEntry<>(str, expireTs));
        }
        return ret;
    }

    @Override
    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        if (otherAsymSign == null || publicKey == null) {
            return;
        }
        otherCache.put(otherAsymSign, new AbstractMap.SimpleEntry<>(publicKey, -1L));
        AsymKeyPair keyPair = new AsymKeyPair(publicKey, null);
        String str = serializeKeyPair(keyPair);
        cache.set(cacheKey(getSignName(otherAsymSign, false)), -1 + "=" + str);
    }

    @Override
    public boolean preferSetAndTtl() {
        return cache.preferSetAndTtl();
    }

    @Override
    public void setSelfKeyPairWithTtl(String selfAsymSign, AsymKeyPair keyPair, long ttlSeconds) {
        if (selfAsymSign == null || keyPair == null) {
            return;
        }
        long expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
        selfCache.put(selfAsymSign, new AbstractMap.SimpleEntry<>(keyPair.copy(), expireTs));
        String str = serializeKeyPair(keyPair);
        cache.set(cacheKey(getSignName(selfAsymSign, true)), expireTs + "=" + str, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setOtherPublicKeyWithTtl(String otherAsymSign, String publicKey, long ttlSeconds) {
        if (otherAsymSign == null || publicKey == null) {
            return;
        }
        long expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
        otherCache.put(otherAsymSign, new AbstractMap.SimpleEntry<>(publicKey, expireTs));
        AsymKeyPair keyPair = new AsymKeyPair(publicKey, null);
        String str = serializeKeyPair(keyPair);
        cache.set(cacheKey(getSignName(otherAsymSign, false)), expireTs + "=" + str, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setSelfTtl(String selfAsymSign, long ttlSeconds) {
        String key = cacheKey(getSignName(selfAsymSign, true));
        Map.Entry<AsymKeyPair, Long> entry = selfCache.get(selfAsymSign);
        long expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
        if (entry != null) {
            entry.setValue(expireTs);
        }
        String str = cache.get(key);
        if (str == null) {
            return;
        }
        String[] arr = str.split("=");
        if (arr.length != 2) {
            return;
        }
        cache.set(key, expireTs + "=" + arr[1], ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void setOtherTtl(String otherAsymSign, long ttlSeconds) {
        String key = cacheKey(getSignName(otherAsymSign, false));
        Map.Entry<String, Long> entry = otherCache.get(otherAsymSign);
        long expireTs = SystemClock.currentTimeMillis() + ttlSeconds * 1000;
        if (entry != null) {
            entry.setValue(expireTs);
        }
        String str = cache.get(key);
        if (str == null) {
            return;
        }
        String[] arr = str.split("=");
        if (arr.length != 2) {
            return;
        }
        cache.set(key, expireTs + "=" + arr[1], ttlSeconds, TimeUnit.SECONDS);
    }
}
