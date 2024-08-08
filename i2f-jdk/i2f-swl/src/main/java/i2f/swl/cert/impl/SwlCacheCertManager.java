package i2f.swl.cert.impl;

import i2f.cache.base.ICache;
import i2f.cache.impl.container.MapCache;
import i2f.lru.LruMap;
import i2f.swl.cert.SwlCertManager;
import i2f.swl.cert.SwlCertUtil;
import i2f.swl.cert.data.SwlCert;
import lombok.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/8/8 21:28
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlCacheCertManager implements SwlCertManager {
    public static final String DEFAULT_KEY_PREFIX = "swl:cert:";
    public static final String SERVER_SUFFIX = ".server";
    public static final String CLIENT_SUFFIX = ".client";

    protected ICache<String, String> cache = new MapCache<>(new ConcurrentHashMap<>());
    protected String cachePrefix = DEFAULT_KEY_PREFIX;


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient LruMap<String, SwlCert> cacheCert = new LruMap<>(1024);


    public String getCertName(String certId, boolean server) {
        if (server) {
            return certId + SERVER_SUFFIX;
        }
        return certId + CLIENT_SUFFIX;
    }

    public String cacheKey(String key) {
        if (cachePrefix == null || cachePrefix.isEmpty()) {
            return key;
        }
        return cachePrefix + ":" + key;
    }

    @Override
    public SwlCert load(String certId, boolean server) {
        String certName = getCertName(certId, server);
        SwlCert cert = cacheCert.get(certName);
        if (cert != null) {
            return cert.copy();
        }
        String str = cache.get(cacheKey(certName));
        SwlCert ret = SwlCertUtil.deserialize(str);
        if (ret != null) {
            cacheCert.put(certName, ret.copy());
        }
        return ret;
    }

    @Override
    public void store(SwlCert cert, boolean server) {
        if (cert == null) {
            return;
        }
        String certId = cert.getCertId();
        String certName = getCertName(certId, server);
        cacheCert.put(certName, cert.copy());
        String str = SwlCertUtil.serialize(cert);
        cache.set(cacheKey(certName), str);
    }
}
