package i2f.swl.core.worker;

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
}
