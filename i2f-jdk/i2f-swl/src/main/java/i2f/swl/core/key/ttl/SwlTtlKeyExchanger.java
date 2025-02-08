package i2f.swl.core.key.ttl;


import i2f.crypto.std.encrypt.asymmetric.key.AsymKeyPair;
import i2f.swl.core.key.SwlKeyExchanger;
import i2f.swl.core.key.ttl.impl.SwlCacheTtlKeyManager;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/8/8 22:09
 * @desc
 */
@Data
public class SwlTtlKeyExchanger extends SwlKeyExchanger {

    protected long selfKeyExpireSeconds = TimeUnit.HOURS.toSeconds(24);
    protected int selfKeyMaxCount = 3;
    protected long otherKeyExpireSeconds = TimeUnit.HOURS.toSeconds(24);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient AtomicBoolean refreshing = new AtomicBoolean(false);
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private transient ScheduledExecutorService schedulePool = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "swl-refreshing");
            thread.setDaemon(true);
            return thread;
        }
    });

    public SwlTtlKeyExchanger() {
        this.keyManager = new SwlCacheTtlKeyManager();
    }

    public SwlTtlKeyExchanger(SwlTtlKeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public void initialRefreshingThread() {
        if (refreshing.getAndSet(true)) {
            return;
        }
        long timeout = selfKeyExpireSeconds - 10;
        if (timeout <= 0) {
            timeout = selfKeyExpireSeconds;
        }
        schedulePool.scheduleAtFixedRate(() -> {
            resetSelfKeyPair();
        }, 0, timeout, TimeUnit.SECONDS);
    }

    public SwlTtlKeyManager getTtlKeyManager() {
        return (SwlTtlKeyManager) keyManager;
    }

    @Override
    public void setSelfKeyPair(String selfAsymSign, AsymKeyPair selfKeyPair) {
        SwlTtlKeyManager manager = getTtlKeyManager();
        if (manager.preferSetAndTtl()) {
            manager.setSelfKeyPairWithTtl(selfAsymSign, selfKeyPair, selfKeyMaxCount * selfKeyExpireSeconds);
        } else {
            manager.setSelfKeyPair(selfAsymSign, selfKeyPair);
            manager.setSelfTtl(selfAsymSign, selfKeyMaxCount * selfKeyExpireSeconds);
        }
        manager.setDefaultSelfAsymSign(selfAsymSign);
    }

    @Override
    public void setOtherPublicKey(String otherAsymSign, String publicKey) {
        SwlTtlKeyManager manager = getTtlKeyManager();
        if (manager.preferSetAndTtl()) {
            manager.setOtherPublicKeyWithTtl(otherAsymSign, publicKey, otherKeyExpireSeconds);
        } else {
            manager.setOtherPublicKey(otherAsymSign, publicKey);
            manager.setOtherTtl(otherAsymSign, otherKeyExpireSeconds);
        }
        manager.setDefaultOtherAsymSign(otherAsymSign);
    }

    public void refreshOtherPublicKeyExpire(String otherAsymSign) {
        SwlTtlKeyManager manager = getTtlKeyManager();
        manager.setOtherTtl(otherAsymSign, otherKeyExpireSeconds);
    }
}
