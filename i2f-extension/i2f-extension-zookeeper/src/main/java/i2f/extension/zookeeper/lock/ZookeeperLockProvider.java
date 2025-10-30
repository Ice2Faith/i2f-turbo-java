package i2f.extension.zookeeper.lock;

import i2f.lock.ILock;
import i2f.lock.ILockProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @author Ice2Faith
 * @date 2024/6/30 18:02
 * @desc
 */
@Data
@NoArgsConstructor
public class ZookeeperLockProvider implements ILockProvider {
    public static final String NAME = "zookeeper";

    protected String keyPrefix = "/lock/";

    private CuratorFramework curator;

    public ZookeeperLockProvider(CuratorFramework curator) {
        this.curator = curator;
    }

    @Override
    public String name() {
        return NAME;
    }

    public String getLockKey(String key) {
        String prefix = keyPrefix;
        if (prefix == null || prefix.isEmpty()) {
            if (!key.startsWith("/")) {
                return "/" + key;
            }
            return key;
        }
        if (!prefix.startsWith("/")) {
            prefix = "/" + prefix;
        }
        if (prefix.endsWith("/")) {
            if (key.startsWith("/")) {
                return prefix + key.substring(1);
            }
            return prefix + key;
        }
        if (key.startsWith("/")) {
            return prefix + key;
        }
        return prefix + "/" + key;
    }

    @Override
    public ILock getLock(String key) {
        return getZkLock(key);
    }

    public ZookeeperInterMutexLock getZkLock(String path) {
        return ZookeeperLockUtil.getZkMutexLock(getLockKey(path), curator);
    }

    public InterProcessMutex getMutex(String path) {
        return ZookeeperLockUtil.getMutexLock(getLockKey(path), curator);
    }
}
