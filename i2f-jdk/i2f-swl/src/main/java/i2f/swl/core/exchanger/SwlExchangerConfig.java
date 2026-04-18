package i2f.swl.core.exchanger;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2024/7/10 17:34
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlExchangerConfig {
    protected boolean enableTimestamp = true;
    protected long timestampExpireWindowSeconds = 30;

    protected boolean enableNonce = false;
    protected long nonceTimeoutSeconds = TimeUnit.MINUTES.toSeconds(30);

    protected boolean enableEncrypt = true;

    protected boolean enableDigital = true;
}
