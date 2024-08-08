package i2f.swl.cert;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/8/6 21:44
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlCert {
    protected String certId;
    protected String publicKey;
    protected String privateKey;
    protected String remotePublicKey;

    public SwlCert(String certId, String publicKey, String privateKey, String remotePublicKey) {
        this.certId = certId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.remotePublicKey = remotePublicKey;
    }

    public static SwlCert copy(SwlCert cert) {
        SwlCert ret = new SwlCert();
        ret.setCertId(cert.getCertId());
        ret.setPublicKey(cert.getPublicKey());
        ret.setPrivateKey(cert.getPrivateKey());
        ret.setRemotePublicKey(cert.getRemotePublicKey());
        return ret;
    }

    public SwlCert copy() {
        return copy(this);
    }
}
