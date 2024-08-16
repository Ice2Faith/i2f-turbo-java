package i2f.swl.core.key.impl;

import i2f.cache.base.ICache;
import i2f.cache.impl.container.MapCache;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.lru.LruMap;
import i2f.swl.core.key.SwlKeyManager;
import lombok.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2024/8/8 21:39
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlCacheKeyManager extends AbsSwlCacheKeyManager implements SwlKeyManager {


    protected ICache<String, String> cache = new MapCache<>(new ConcurrentHashMap<>(1024));


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LruMap<String, AsymKeyPair> selfCache = new LruMap<>(1024);
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected LruMap<String, String> otherCache = new LruMap<>(1024);
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
        AsymKeyPair keyPair = selfCache.get(selfAsymSign);
        if (keyPair != null) {
            return keyPair;
        }
        AsymKeyPair ret = null;

        String str = cache.get(cacheKey(getSignName(selfAsymSign, true)));
        if (str != null) {
            ret = deserializeKeyPair(str);
        }

        if (ret != null) {
            selfCache.put(selfAsymSign, ret.copy());
        }
        return ret;
    }

    @Override
    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair keyPair) {
        if (selfAsymSign == null || keyPair == null) {
            return;
        }
        selfCache.put(selfAsymSign, keyPair.copy());
        String str = serializeKeyPair(keyPair);
        cache.set(cacheKey(getSignName(selfAsymSign, true)), str);
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
        String publicKey = otherCache.get(otherAsymSign);
        if (publicKey != null) {
            return publicKey;
        }
        String ret = null;

        String str = cache.get(cacheKey(getSignName(otherAsymSign, false)));
        if (str != null) {
            AsymKeyPair keyPair = deserializeKeyPair(str);
            ret = keyPair.getPublicKey();
        }

        if (ret != null) {
            otherCache.put(otherAsymSign, ret);
        }
        return ret;
    }

    @Override
    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        if (otherAsymSign == null || publicKey == null) {
            return;
        }
        otherCache.put(otherAsymSign, publicKey);
        AsymKeyPair keyPair = new AsymKeyPair(publicKey, null);
        String str = serializeKeyPair(keyPair);
        cache.set(cacheKey(getSignName(otherAsymSign, false)), str);
    }
}
