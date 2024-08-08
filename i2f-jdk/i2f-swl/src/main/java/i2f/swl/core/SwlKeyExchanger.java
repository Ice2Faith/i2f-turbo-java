package i2f.swl.core;

import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.exchanger.SwlExchanger;
import i2f.swl.core.impl.SwlLocalFileKeyManager;
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
public class SwlKeyExchanger extends SwlExchanger {
    public static final String SELF_KEY_PAIR_CURRENT_KEY = "swl:key:self:current";
    public static final String SELF_KEY_PAIR_HISTORY_KEY_PREFIX = "swl:key:self:history:";
    public static final String OTHER_KEY_PUBLIC_KEY_PREFIX = "swl:key:other:keys:";
    public static final String OTHER_KEY_PUBLIC_DEFAULT = "swl:key:other:default";
    public static final String NONCE_PREFIX = "swl:nonce:";
    public static final String KEYPAIR_SEPARATOR = "\n==========\n";

    private SwlKeyManager keyManager = new SwlLocalFileKeyManager();

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
        return keyManager.getSelfKeyPair();
    }

    public String getSelfPublicKey() {
        AsymKeyPair keyPair = getSelfKeyPair();
        return keyPair.getPublicKey();
    }

    public String calcKeySign(String publicKey) {
        return messageDigester.digest(publicKey);
    }

    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair selfKeyPair) {
        keyManager.setSelfKeyPair(selfAsymSign, selfKeyPair);

        // TODO key expire
//        cache.set(cacheKey(key), text, config.getSelfKeyMaxCount() * config.getSelfKeyExpireSeconds(), TimeUnit.SECONDS);
    }

    public String getSelfPrivateKey(String selfAsymSign) {
        AsymKeyPair keyPair = keyManager.getSelfKeyPair(selfAsymSign);
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPrivateKey();
    }

    public String getOtherPublicKey(String otherAsymSign) {
        AsymKeyPair keyPair = keyManager.getOtherKeyPair(otherAsymSign);
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPublicKey();
    }

    public String getOtherPublicKeyDefault() {
        AsymKeyPair keyPair = keyManager.getOtherKeyPair();
        if (keyPair == null) {
            return null;
        }
        return keyPair.getPublicKey();
    }

    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        keyManager.setOtherKeyPair(otherAsymSign, new AsymKeyPair(publicKey, null));
        keyManager.setDefaultOtherAsymSign(otherAsymSign);

        // TODO key expire
//        cache.set(cacheKey(key), obfuscateEncode(publicKey), config.getOtherKeyExpireSeconds(), TimeUnit.SECONDS);

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
        return send(remotePublicKey, calcKeySign(remotePublicKey),
                keyPair.getPrivateKey(), calcKeySign(keyPair.getPublicKey()),
                parts, attaches);
    }

    public SwlData receive(String clientId, SwlData request) {
        String selfAsymSign = request.getHeader().getRemoteAsymSign();
        String otherAsymSign = request.getHeader().getLocalAsymSign();
        String otherPublicKey = getOtherPublicKey(otherAsymSign);
        String selfPrivateKey = getSelfPrivateKey(selfAsymSign);
        return receive(clientId, request, otherPublicKey, selfPrivateKey);
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
