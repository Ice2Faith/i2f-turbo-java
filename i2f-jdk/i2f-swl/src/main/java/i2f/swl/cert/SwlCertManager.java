package i2f.swl.cert;

import i2f.swl.cert.data.SwlCert;
import i2f.swl.cert.data.SwlCertPair;

/**
 * @author Ice2Faith
 * @date 2024/8/8 15:37
 * @desc
 */
public interface SwlCertManager {
    SwlCert load(String certId, boolean server);

    void store(SwlCert cert, boolean server);

    default SwlCert loadServer(String certId) {
        return load(certId, true);
    }

    default SwlCert loadClient(String certId) {
        return load(certId, false);
    }

    default void storeServer(SwlCert cert) {
        store(cert, true);
    }

    default void storeClient(SwlCert cert) {
        store(cert, false);
    }

    default SwlCertPair loadPair(String certId) {
        SwlCert server = load(certId, true);
        SwlCert client = load(certId, false);
        return new SwlCertPair(server, client);
    }

    default void storePair(SwlCertPair pair) {
        store(pair.getServer(), true);
        store(pair.getClient(), false);
    }
}
