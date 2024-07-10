package i2f.swl.data;

import i2f.jce.std.encrypt.asymmetric.key.AsymKeyPair;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 14:11
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlContext {
    private String remotePublicKey;
    private AsymKeyPair selfKeyPair;
    private String localAsymSign;
    private String remoteAsymSign;
    private String timestamp;
    private String seq;
    private String nonce;
    private String key;
    private String randomKey;
    private String data;
    private String sign;
    private String digital;

    private String clientId;
    private String currentTimestamp;
    private String window;
    private String nonceKey;
    private boolean signOk;
    private boolean digitalOk;
}
