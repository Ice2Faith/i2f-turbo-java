package i2f.swl.core;

import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.cache.std.expire.IExpireCache;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.exchanger.SwlExchanger;
import i2f.swl.data.SwlData;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
@Data

@NoArgsConstructor
public class SwlTransferRefactor extends SwlExchanger {
    public static final String SELF_KEY_PAIR_CURRENT_KEY = "swl:key:self:current";
    public static final String SELF_KEY_PAIR_HISTORY_KEY_PREFIX = "swl:key:self:history:";
    public static final String OTHER_KEY_PUBLIC_KEY_PREFIX = "swl:key:other:keys:";
    public static final String OTHER_KEY_PUBLIC_DEFAULT = "swl:key:other:default";
    public static final String KEYPAIR_SEPARATOR = "\n==========\n";

    private IExpireCache<String, String> cache = new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    private SwlTransferConfig config = new SwlTransferConfig();

    private AtomicBoolean refreshing = new AtomicBoolean(false);
    private ScheduledExecutorService schedulePool = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "swl-refreshing");
            thread.setDaemon(true);
            return thread;
        }
    });

    public void initialRefreshingThread() {
        if (refreshing.getAndSet(true)) {
            return;
        }
        long timeout = config.getSelfKeyExpireSeconds() - 10;
        if (timeout <= 0) {
            timeout = config.getSelfKeyExpireSeconds();
        }
        schedulePool.scheduleAtFixedRate(() -> {
            resetSelfKeyPair();
        }, 0, timeout, TimeUnit.SECONDS);
    }

    public String cacheKey(String key) {
        String cacheKeyPrefix = config.getCacheKeyPrefix();
        if (cacheKeyPrefix == null || cacheKeyPrefix.isEmpty()) {
            return key;
        }
        return cacheKeyPrefix + ":" + key;
    }

    public AsymKeyPair getSelfKeyPair() {
        String obj = cache.get(cacheKey(SELF_KEY_PAIR_CURRENT_KEY));
        if (obj == null) {
            return resetSelfKeyPair();
        }
        String key = SELF_KEY_PAIR_HISTORY_KEY_PREFIX + obj;
        obj = cache.get(cacheKey(key));
        if (obj == null) {
            return resetSelfKeyPair();
        }

        String[] arr = obj.split(KEYPAIR_SEPARATOR, 2);
        AsymKeyPair ret = new AsymKeyPair();
        ret.setPublicKey(obfuscateDecode(arr[0]));
        ret.setPrivateKey(obfuscateDecode(arr[1]));
        return ret;
    }

    public String getSelfPublicKey() {
        AsymKeyPair keyPair = getSelfKeyPair();
        return keyPair.getPublicKey();
    }

    public String calcKeySign(String publicKey) {
        return messageDigester.digest(publicKey);
    }

    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair selfKeyPair) {
        String key = SELF_KEY_PAIR_HISTORY_KEY_PREFIX + selfAsymSign;
        AsymKeyPair keyPair = new AsymKeyPair(
                obfuscateEncode(selfKeyPair.getPublicKey()),
                obfuscateEncode(selfKeyPair.getPrivateKey())
        );
        String text = keyPair.getPublicKey() + KEYPAIR_SEPARATOR + keyPair.getPrivateKey();
        cache.set(cacheKey(key), text, config.getSelfKeyMaxCount() * config.getSelfKeyExpireSeconds(), TimeUnit.SECONDS);
        cache.set(cacheKey(SELF_KEY_PAIR_CURRENT_KEY), selfAsymSign);
    }

    public String getSelfPrivateKey(String selfAsymSign) {
        String key = SELF_KEY_PAIR_HISTORY_KEY_PREFIX + selfAsymSign;
        String obj = cache.get(cacheKey(key));
        if (obj == null) {
            return null;
        }
        String[] arr = obj.split(KEYPAIR_SEPARATOR, 2);
        return obfuscateDecode(arr[1]);
    }

    public String getOtherPublicKey(String otherAsymSign) {
        String key = OTHER_KEY_PUBLIC_KEY_PREFIX + otherAsymSign;
        String obj = cache.get(cacheKey(key));
        if (obj == null) {
            return null;
        }
        return obfuscateDecode(obj);
    }

    public String getOtherPublicKeyDefault() {
        String obj = cache.get(cacheKey(OTHER_KEY_PUBLIC_DEFAULT));
        if (obj == null) {
            return null;
        }
        String otherAsymSign = obj;
        return getOtherPublicKey(otherAsymSign);
    }

    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        String key = OTHER_KEY_PUBLIC_KEY_PREFIX + otherAsymSign;
        cache.set(cacheKey(key), obfuscateEncode(publicKey), config.getOtherKeyExpireSeconds(), TimeUnit.SECONDS);
        cache.set(cacheKey(OTHER_KEY_PUBLIC_DEFAULT), otherAsymSign);
    }

    public void refreshOtherPublicKeyExpire(String otherAsymSign) {
        String key = OTHER_KEY_PUBLIC_KEY_PREFIX + otherAsymSign;
        cache.expire(cacheKey(key), config.getOtherKeyExpireSeconds(), TimeUnit.SECONDS);
    }

    public void acceptOtherPublicKey(String otherPublicKey) {
        String otherAsymSign = messageDigester.digest(otherPublicKey);
        setOtherPublicKey(otherAsymSign, otherPublicKey);
    }

    public String getSelfSwapKey() {
        String selfPublicKey = getSelfPublicKey();
        return obfuscateEncode(selfPublicKey);
    }

    public void acceptOtherSwapKey(String otherSwapKey) {
        String otherPublicKey = obfuscateDecode(otherSwapKey);
        acceptOtherPublicKey(otherPublicKey);
    }

    public AsymKeyPair resetSelfKeyPair() {
        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        AsymKeyPair asymKeyPair = asymmetricEncryptor.generateKeyPair();
        String selfAsymSign = messageDigester.digest(asymKeyPair.getPublicKey());
        setSelfKeyPair(selfAsymSign, asymKeyPair);
        return asymKeyPair;
    }

    public SwlData send(String remotePublicKey, List<String> parts) {
        return send(remotePublicKey, parts, null);
    }

    public SwlData send(String remotePublicKey, List<String> parts, List<String> attaches) {
        AsymKeyPair keyPair = getSelfKeyPair();
        return sendByRaw(remotePublicKey, calcKeySign(remotePublicKey),
                keyPair.getPrivateKey(), calcKeySign(keyPair.getPublicKey()),
                parts, attaches);
    }

    public SwlData receive(String clientId, SwlData request) {
        String selfAsymSign = request.getHeader().getRemoteAsymSign();
        String otherAsymSign = request.getHeader().getLocalAsymSign();
        String otherPublicKey = getOtherPublicKey(otherAsymSign);
        String selfPrivateKey = getSelfPrivateKey(selfAsymSign);
        return receiveByRaw(clientId, request, otherPublicKey, selfPrivateKey);
    }

    public SwlData sendDefault(List<String> parts, List<String> attaches) {
        String otherPublicKey = getOtherPublicKeyDefault();
        return send(otherPublicKey, parts, attaches);
    }

    public SwlData sendDefault(List<String> parts) {
        String otherPublicKey = getOtherPublicKeyDefault();
        return send(otherPublicKey, parts, null);
    }

    public SwlData response(String remoteAsymSign, List<String> parts, List<String> attaches) {
        String otherPublicKey = getOtherPublicKey(remoteAsymSign);
        return send(otherPublicKey, parts, attaches);
    }

    public SwlData response(String remoteAsymSign, List<String> parts) {
        String otherPublicKey = getOtherPublicKey(remoteAsymSign);
        return send(otherPublicKey, parts, null);
    }
}
