package i2f.sm.crypto.sm2;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/8/13 21:40
 * @desc
 */
@Data
@NoArgsConstructor
public class KeyPair {
    protected String publicKey;
    protected String privateKey;

    public KeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
