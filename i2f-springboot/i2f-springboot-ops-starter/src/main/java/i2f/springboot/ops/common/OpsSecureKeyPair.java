package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/1 22:25
 * @desc
 */
@Data
@NoArgsConstructor
public class OpsSecureKeyPair {
    protected String publicKey;
    protected String privateKey;

    public OpsSecureKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
