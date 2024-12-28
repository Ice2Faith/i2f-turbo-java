package i2f.swl.core;

import i2f.cache.expire.IExpireCache;
import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.clock.SystemClock;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.consts.SwlCode;
import i2f.swl.data.SwlContext;
import i2f.swl.data.SwlData;
import i2f.swl.data.SwlHeader;
import i2f.swl.exception.SwlException;
import i2f.swl.impl.SwlBase64Obfuscator;
import i2f.swl.impl.SwlSha256MessageDigester;
import i2f.swl.impl.supplier.SwlAesSymmetricEncryptorSupplier;
import i2f.swl.impl.supplier.SwlRsaAsymmetricEncryptorSupplier;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.ISwlObfuscator;
import i2f.swl.std.ISwlSymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlTransferBackup {
    public static final String SELF_KEY_PAIR_CURRENT_KEY = "swl:key:self:current";
    public static final String SELF_KEY_PAIR_HISTORY_KEY_PREFIX = "swl:key:self:history:";
    public static final String OTHER_KEY_PUBLIC_KEY_PREFIX = "swl:key:other:keys:";
    public static final String OTHER_KEY_PUBLIC_DEFAULT = "swl:key:other:default";
    public static final String NONCE_PREFIX = "swl:nonce:";
    public static final String KEYPAIR_SEPARATOR = "\n==========\n";

    private Supplier<ISwlAsymmetricEncryptor> asymmetricEncryptorSupplier = new SwlRsaAsymmetricEncryptorSupplier();
    private Supplier<ISwlSymmetricEncryptor> symmetricEncryptorSupplier = new SwlAesSymmetricEncryptorSupplier();
    private ISwlMessageDigester messageDigester = new SwlSha256MessageDigester();
    private ISwlObfuscator obfuscator = new SwlBase64Obfuscator();

    private IExpireCache<String, String> cache = new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    private SwlTransferConfig config = new SwlTransferConfig();
    private SecureRandom random = new SecureRandom();

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

    public boolean containsNonce(String nonce) {
        String key = NONCE_PREFIX + nonce;
        return cache.exists(cacheKey(key));
    }

    public void setNonce(String nonce, long timeoutSeconds) {
        String key = NONCE_PREFIX + nonce;
        cache.set(cacheKey(key), String.valueOf(SystemClock.currentTimeMillis()), timeoutSeconds, TimeUnit.SECONDS);
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

    public String obfuscateEncode(String data) {
        if (data == null) {
            return null;
        }
        if (obfuscator == null) {
            return data;
        }
        return obfuscator.encode(data);
    }

    public String obfuscateDecode(String data) {
        if (data == null) {
            return null;
        }
        if (obfuscator == null) {
            return data;
        }
        return obfuscator.decode(data);
    }

    public SwlData send(String remotePublicKey, List<String> parts) {
        return send(remotePublicKey, parts, null);
    }

    public SwlData send(String remotePublicKey, List<String> parts, List<String> attaches) {
        SwlData ret = new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setAttaches(new ArrayList<>());
        ret.setHeader(new SwlHeader());
        ret.setContext(new SwlContext());

        ret.getContext().setRemotePublicKey(remotePublicKey);

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();

        AsymKeyPair selfKeyPair = getSelfKeyPair();
        ret.getContext().setSelfPrivateKey(selfKeyPair.getPrivateKey());

        String localAsymSign = messageDigester.digest(selfKeyPair.getPublicKey());
        ret.getHeader().setLocalAsymSign(localAsymSign);
        ret.getContext().setSelfAsymSign(localAsymSign);

        String remoteAsymSign = messageDigester.digest(remotePublicKey);
        ret.getHeader().setRemoteAsymSign(remoteAsymSign);
        ret.getContext().setRemoteAsymSign(remoteAsymSign);

        long timestamp = SystemClock.currentTimeMillis() / 1000;
        ret.getHeader().setTimestamp(String.valueOf(timestamp));
        ret.getContext().setTimestamp(String.valueOf(timestamp));

        long seq = random.nextInt(0x7fff);
        String nonce = Long.toString(timestamp, 16) + "-" + Long.toString(seq, 16);
        ret.getHeader().setNonce(nonce);
        ret.getContext().setNonce(nonce);

        String key = symmetricEncryptor.generateKey();
        ret.getContext().setKey(key);

        asymmetricEncryptor.setPublicKey(remotePublicKey);
        String randomKey = asymmetricEncryptor.encrypt(key);
        ret.getHeader().setRandomKey(randomKey);
        ret.getContext().setRandomKey(randomKey);

        symmetricEncryptor.setKey(key);
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            String data = null;
            if (part != null) {
                data = symmetricEncryptor.encrypt(part);
            }
            if (data != null) {
                builder.append(data);
            }
            ret.getParts().add(data);
        }
        if (attaches != null) {
            for (String attach : attaches) {
                if (attach != null) {
                    builder.append(attach);
                }
                ret.getAttaches().add(attach);
            }
        }

        String data = builder.toString();
        ret.getContext().setData(data);

        String sign = messageDigester.digest(data + randomKey + timestamp + nonce + localAsymSign + remoteAsymSign);
        ret.getHeader().setSign(sign);
        ret.getContext().setSign(sign);

        asymmetricEncryptor.setPrivateKey(selfKeyPair.getPrivateKey());
        String digital = asymmetricEncryptor.sign(sign);
        ret.getHeader().setDigital(digital);
        ret.getContext().setDigital(digital);

        ret.getHeader().setLocalAsymSign(obfuscateEncode(ret.getHeader().getLocalAsymSign()));
        ret.getHeader().setRemoteAsymSign(obfuscateEncode(ret.getHeader().getRemoteAsymSign()));
        ret.getHeader().setRandomKey(obfuscateEncode(ret.getHeader().getRandomKey()));
        ret.getHeader().setNonce(obfuscateEncode(ret.getHeader().getNonce()));
        ret.getHeader().setSign(obfuscateEncode(ret.getHeader().getSign()));
        ret.getHeader().setDigital(obfuscateEncode(ret.getHeader().getDigital()));
        return ret;
    }

    public SwlData receive(String clientId, SwlData request) {
        SwlData ret = new SwlData();
        ret.setParts(new ArrayList<>());
        ret.setAttaches(new ArrayList<>());
        ret.setHeader(SwlHeader.copy(request.getHeader(), new SwlHeader()));
        ret.setContext(new SwlContext());

        ret.getHeader().setLocalAsymSign(obfuscateDecode(ret.getHeader().getLocalAsymSign()));
        ret.getHeader().setRemoteAsymSign(obfuscateDecode(ret.getHeader().getRemoteAsymSign()));
        ret.getHeader().setRandomKey(obfuscateDecode(ret.getHeader().getRandomKey()));
        ret.getHeader().setNonce(obfuscateDecode(ret.getHeader().getNonce()));
        ret.getHeader().setSign(obfuscateDecode(ret.getHeader().getSign()));
        ret.getHeader().setDigital(obfuscateDecode(ret.getHeader().getDigital()));

        String str = ret.getHeader().getLocalAsymSign();
        ret.getHeader().setLocalAsymSign(ret.getHeader().getRemoteAsymSign());
        ret.getHeader().setRemoteAsymSign(str);

        ret.getContext().setClientId(clientId);

        long currentTimestamp = SystemClock.currentTimeMillis() / 1000;
        ret.getContext().setCurrentTimestamp(String.valueOf(currentTimestamp));

        String timestamp = ret.getHeader().getTimestamp();
        long ts = Long.parseLong(timestamp);
        ret.getContext().setTimestamp(timestamp);

        String nonce = ret.getHeader().getNonce();
        ret.getContext().setNonce(nonce);
        if (nonce == null || nonce.isEmpty()) {
            throw new SwlException(SwlCode.NONCE_MISSING_EXCEPTION.code(), "nonce cannot is empty!");
        }


        long window = config.getTimestampExpireWindowSeconds();
        ret.getContext().setWindow(String.valueOf(window));

        if (Math.abs(currentTimestamp - ts) > window) {
            throw new SwlException(SwlCode.NONCE_TIMESTAMP_EXCEED_EXCEPTION.code(), "timestamp is exceed allow window seconds!");
        }

        String nonceKey = nonce;
        if (clientId != null && !clientId.isEmpty()) {
            nonceKey = clientId + "-" + nonce;
        }
        ret.getContext().setNonceKey(nonceKey);
        if (containsNonce(nonceKey)) {
            throw new SwlException(SwlCode.NONCE_ALREADY_EXISTS_EXCEPTION.code(), "nonce key already exists!");
        }

        setNonce(nonceKey, config.getNonceTimeoutSeconds());

        String sign = ret.getHeader().getSign();
        ret.getContext().setSign(sign);
        if (sign == null || sign.isEmpty()) {
            throw new SwlException(SwlCode.SIGN_MISSING_EXCEPTION.code(), "sign cannot be empty!");
        }

        StringBuilder builder = new StringBuilder();
        List<String> parts = request.getParts();
        if (parts != null) {
            for (String part : parts) {
                if (part != null) {
                    builder.append(part);
                }
            }
        }

        List<String> attaches = request.getAttaches();
        if (attaches != null) {
            for (String attach : attaches) {
                if (attach != null) {
                    builder.append(attach);
                }
            }
        }

        String data = builder.toString();
        ret.getContext().setData(data);

        String randomKey = ret.getHeader().getRandomKey();
        ret.getContext().setRandomKey(randomKey);
        if (randomKey == null || randomKey.isEmpty()) {
            throw new SwlException(SwlCode.RANDOM_KEY_MISSING_EXCEPTION.code(), "random key cannot be empty!");
        }

        String remoteAsymSign = ret.getHeader().getRemoteAsymSign();
        ret.getContext().setRemoteAsymSign(remoteAsymSign);
        if (remoteAsymSign == null || remoteAsymSign.isEmpty()) {
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "remote asym sign cannot be empty!");
        }

        String localAsymSign = ret.getHeader().getLocalAsymSign();
        ret.getContext().setSelfAsymSign(localAsymSign);
        if (localAsymSign == null || localAsymSign.isEmpty()) {
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "local asym sign cannot be empty!");
        }

        boolean signOk = messageDigester.verify(sign, data + randomKey + timestamp + nonce + remoteAsymSign + localAsymSign);
        ret.getContext().setSignOk(signOk);
        if (!signOk) {
            throw new SwlException(SwlCode.SIGN_VERIFY_FAILURE_EXCEPTION.code(), "verify sign failure!");
        }

        String digital = ret.getHeader().getDigital();
        ret.getContext().setDigital(digital);
        if (digital == null || digital.isEmpty()) {
            throw new SwlException(SwlCode.DIGITAL_MISSING_EXCEPTION.code(), "digital cannot be empty!");
        }

        String remotePublicKey = getOtherPublicKey(remoteAsymSign);
        ret.getContext().setRemotePublicKey(remotePublicKey);
        if (remotePublicKey == null || remotePublicKey.isEmpty()) {
            throw new SwlException(SwlCode.CLIENT_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "remote key not found!");
        }

        refreshOtherPublicKeyExpire(remoteAsymSign);

        ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorSupplier.get();
        asymmetricEncryptor.setPublicKey(remotePublicKey);

        boolean digitalOk = asymmetricEncryptor.verify(digital, sign);
        ret.getContext().setDigitalOk(digitalOk);
        if (!digitalOk) {
            throw new SwlException(SwlCode.DIGITAL_VERIFY_FAILURE_EXCEPTION.code(), "verify digital failure!");
        }

        String localPrivateKey = getSelfPrivateKey(localAsymSign);
        ret.getContext().setSelfPrivateKey(localPrivateKey);
        if (localPrivateKey == null || localPrivateKey.isEmpty()) {
            throw new SwlException(SwlCode.SERVER_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "server key not found!");
        }

        asymmetricEncryptor.setPrivateKey(localPrivateKey);
        String key = asymmetricEncryptor.decrypt(randomKey);
        ret.getContext().setKey(key);
        if (key == null || key.isEmpty()) {
            throw new SwlException(SwlCode.RANDOM_KEY_INVALID_EXCEPTION.code(), "random key is invalid!");
        }

        ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorSupplier.get();
        symmetricEncryptor.setKey(key);

        if (parts != null) {
            for (String part : parts) {
                String item = null;
                if (part != null) {
                    item = symmetricEncryptor.decrypt(part);
                }
                ret.getParts().add(item);
            }
        }

        return ret;
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
