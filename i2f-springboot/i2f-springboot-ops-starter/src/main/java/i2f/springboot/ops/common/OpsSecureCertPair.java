package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/2 12:40
 * @desc
 */
@Data
@NoArgsConstructor
public class OpsSecureCertPair {
    protected String serverCert;
    protected String clientCert;
    public OpsSecureCertPair(String serverCert, String clientCert) {
        this.serverCert = serverCert;
        this.clientCert = clientCert;
    }
}
