package i2f.swl.cert.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/8/6 22:14
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlCertPair {
    protected SwlCert server;
    protected SwlCert client;

    public SwlCertPair(SwlCert server, SwlCert client) {
        this.server = server;
        this.client = client;
    }
}
