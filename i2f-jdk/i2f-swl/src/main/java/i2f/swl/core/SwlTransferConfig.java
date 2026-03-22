package i2f.swl.core;

import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
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
public class SwlTransferConfig {
    private String cacheKeyPrefix;

    private long timestampExpireWindowSeconds = TimeUnit.MINUTES.toSeconds(30);
    private long nonceTimeoutSeconds = TimeUnit.MINUTES.toSeconds(30);

    private boolean enableRefreshSelfKey = true;

    private long channelExpireSeconds = TimeUnit.MINUTES.toSeconds(30);

    private AsymKeyPair swapKeyPair;

}
