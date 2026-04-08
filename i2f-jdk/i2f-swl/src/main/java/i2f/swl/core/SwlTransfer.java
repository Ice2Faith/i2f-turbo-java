package i2f.swl.core;

import i2f.cache.impl.container.MapCache;
import i2f.cache.impl.expire.ObjectExpireCacheWrapper;
import i2f.cache.std.expire.IExpireCache;
import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.cert.SwlCertUtil;
import i2f.swl.cert.data.SwlCert;
import i2f.swl.consts.SwlCode;
import i2f.swl.core.exchanger.SwlExchanger;
import i2f.swl.data.SwlData;
import i2f.swl.exception.SwlException;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:22
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlTransfer extends SwlExchanger {

    public static final String CERT_PREFIX_KEY = "swl:key:cert:";
    public static final String OTHER_KEY_PUBLIC_DEFAULT = "swl:key:other:default";

    private IExpireCache<String, String> cache = new ObjectExpireCacheWrapper<>(new MapCache<>(new ConcurrentHashMap<>()));
    private SwlTransferConfig config = new SwlTransferConfig();


    public String cacheKey(String key) {
        String cacheKeyPrefix = config.getCacheKeyPrefix();
        if (cacheKeyPrefix == null || cacheKeyPrefix.isEmpty()) {
            return key;
        }
        return cacheKeyPrefix + ":" + key;
    }

    public String certKey(String certId) {
        return CERT_PREFIX_KEY + certId;
    }


    public SwlCert getCert(String certId) {
        String obj = cache.get(cacheKey(certKey(certId)));
        if (obj == null) {
            throw new SwlException(SwlCode.CERT_ID_MISSING_EXCEPTION.code(), "cert id missing or not built channel");
        }
        SwlCert cert = SwlCertUtil.deserialize(obj);
        cert.setCertId(certId);
        return cert;
    }

    public AsymKeyPair getSelfKeyPair(String certId) {
        SwlCert cert = getCert(certId);
        return new AsymKeyPair(cert.getPublicKey(), cert.getPrivateKey());
    }

    public String getSelfPublicKey(String certId) {
        AsymKeyPair keyPair = getSelfKeyPair(certId);
        return keyPair.getPublicKey();
    }

    public String getSelfPrivateKey(String certId) {
        AsymKeyPair keyPair = getSelfKeyPair(certId);
        return keyPair.getPrivateKey();
    }

    public String getOtherPublicKey(String certId) {
        SwlCert cert = getCert(certId);
        return cert.getRemotePublicKey();
    }

    public void buildCert(String certId, AsymKeyPair selfKeyPair, String otherPublicKey) {
        SwlCert cert = new SwlCert(certId, selfKeyPair.getPublicKey(), selfKeyPair.getPrivateKey(), otherPublicKey);
        String text = SwlCertUtil.serialize(cert);
        String key = certKey(certId);
        cache.set(cacheKey(key), text, config.getCertExpireSeconds(), TimeUnit.SECONDS);
        cache.set(cacheKey(OTHER_KEY_PUBLIC_DEFAULT),certId);
    }

    public SwlCert removeCert(String certId) {
        String key = certKey(certId);
        String text = cache.get(cacheKey(key));
        if (text != null) {
            return SwlCertUtil.deserialize(text);
        }
        cache.remove(cacheKey(key));
        return null;
    }

    public void resetCertExpire(String certId) {
        String key = certKey(certId);
        cache.expire(cacheKey(key), config.getCertExpireSeconds(), TimeUnit.SECONDS);
    }

    public String acceptOtherPublicKey(String otherPublicKey) {
        String certId = UUID.randomUUID().toString().replace("-", "");
        return acceptOtherPublicKeyWithId(certId, otherPublicKey);
    }

    public String acceptOtherPublicKeyWithId(String certId, String otherPublicKey) {
        ISwlAsymmetricEncryptor asymmetricEncryptor = requireAsymmetricEncryptor();
        AsymKeyPair selfKeyPair = asymmetricEncryptor.generateKeyPair();
        return acceptOtherPublicKeyRaw(certId, selfKeyPair, otherPublicKey);
    }

    public String acceptOtherPublicKeyRaw(String certId, AsymKeyPair selfKeyPair, String otherPublicKey) {
        buildCert(certId, selfKeyPair, otherPublicKey);
        return certId;
    }

    public AsymKeyPair getSelfSwapKey() {
        AsymKeyPair swapKeyPair = config.getSwapKeyPair();
        return swapKeyPair;
    }

    public String acceptOtherSwapKey(String otherSwapKey) {
        String otherPublicKey = obfuscateDecode(otherSwapKey);
        return acceptOtherPublicKey(otherPublicKey);
    }

    public SwlData send(String certId, List<String> parts) {
        return send(certId, parts, null);
    }

    public SwlData send(String certId, List<String> parts, List<String> attaches) {
        SwlCert cert = getCert(certId);
        return sendByRaw(certId,
                cert.getRemotePublicKey(),
                cert.getPrivateKey(),
                parts, attaches);
    }

    public SwlData receive(String clientId, SwlData request) {
        String certId = request.getHeader().getCertId();
        SwlCert cert = getCert(certId);
        return receiveByRaw(clientId, request, cert.getRemotePublicKey(), cert.getPrivateKey());
    }

    public String getOtherCertIdDefault(){
        return cache.get(cacheKey(OTHER_KEY_PUBLIC_DEFAULT));
    }

    public SwlData sendDefault(List<String> parts, List<String> attaches) {
        String certId = getOtherCertIdDefault();
        return send(certId, parts, attaches);
    }

    public SwlData response(String certId, List<String> parts, List<String> attaches) {
        return send(certId, parts, attaches);
    }

    public SwlData response(String certId, List<String> parts) {
        return send(certId, parts, null);
    }
}
