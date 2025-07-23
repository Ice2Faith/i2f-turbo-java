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
    private CuratorFramework curator;

    public ZookeeperLockProvider(CuratorFramework curator) {
        this.curator = curator;
    }

    @Override
    public ILock getLock(String key) {
        return getZkLock(key);
    }

    public ZookeeperInterMutexLock getZkLock(String path) {
        return ZookeeperLockUtil.getZkMutexLock(path, curator);
    }

    public InterProcessMutex getMutex(String path) {
        return ZookeeperLockUtil.getMutexLock(path, curator);
    }
}
