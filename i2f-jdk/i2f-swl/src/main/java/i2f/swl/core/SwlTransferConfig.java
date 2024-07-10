package i2f.swl.core;

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

    private long nonceWindowSeconds = TimeUnit.MINUTES.toSeconds(30);
    private long nonceTimeoutSeconds = TimeUnit.MINUTES.toSeconds(30);

    private boolean enableRefreshSelfKey=true;

    private long selfKeyExpireSeconds = TimeUnit.HOURS.toSeconds(24);
    private int selfKeyMaxCount = 3;
    private long otherKeyExpireSeconds = TimeUnit.HOURS.toSeconds(24);

}
