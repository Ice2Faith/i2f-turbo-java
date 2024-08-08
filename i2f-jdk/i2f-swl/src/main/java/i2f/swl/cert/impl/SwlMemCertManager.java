package i2f.swl.cert.impl;

import i2f.swl.cert.SwlCertManager;
import i2f.swl.cert.data.SwlCert;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/8/8 21:10
 * @desc
 */
public class SwlMemCertManager implements SwlCertManager {
    public static final String SERVER_SUFFIX = ".server";
    public static final String CLIENT_SUFFIX = ".client";

    private ConcurrentHashMap<String, SwlCert> certMap = new ConcurrentHashMap<>(1024);

    public String getCertName(String certId, boolean server) {
        if (server) {
            return certId + SERVER_SUFFIX;
        }
        return certId + CLIENT_SUFFIX;
    }

    @Override
    public SwlCert load(String certId, boolean server) {
        String certName = getCertName(certId, server);
        SwlCert ret = certMap.get(certName);
        if (ret != null) {
            return ret.copy();
        }
        return null;
    }

    @Override
    public void store(SwlCert cert, boolean server) {
        if (cert == null) {
            return;
        }
        String certId = cert.getCertId();
        String certName = getCertName(certId, server);
        certMap.put(certName, cert.copy());
    }
}
