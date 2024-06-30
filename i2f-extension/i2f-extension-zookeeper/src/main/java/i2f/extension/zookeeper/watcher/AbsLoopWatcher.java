package i2f.extension.zookeeper.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AddWatchMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author Ice2Faith
 * @date 2021/9/22
 */
@Slf4j
public abstract class AbsLoopWatcher implements Watcher {

    @Override
    public final void process(WatchedEvent event) {
        boolean loop = onProcess(event);
        if (loop) {
            try {
                getZooKeeper().addWatch(getPath(), this, AddWatchMode.PERSISTENT_RECURSIVE);
            } catch (Exception e) {
                log.warn("watcher {} next loop set failure.", getPath());
            }
        }
    }

    public abstract boolean onProcess(WatchedEvent event);

    public abstract ZooKeeper getZooKeeper();

    public abstract String getPath();
}
