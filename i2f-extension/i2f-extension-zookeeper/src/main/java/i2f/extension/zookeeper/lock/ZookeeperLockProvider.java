package i2f.extension.zookeeper.lock;

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
public class ZookeeperLockProvider {
    private CuratorFramework curator;

    public ZookeeperLockProvider(CuratorFramework curator) {
        this.curator = curator;
    }

    public ZookeeperInterMutexLock getLock(String path) {
        return ZookeeperLockUtil.getZkMutexLock(path, curator);
    }

    public InterProcessMutex getMutex(String path) {
        return ZookeeperLockUtil.getMutexLock(path, curator);
    }
}
