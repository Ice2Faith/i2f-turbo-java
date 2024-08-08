package i2f.swl.data;

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
    private String remoteAsymSign;

    private String selfPrivateKey;
    private String selfAsymSign;

    private String timestamp;
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
