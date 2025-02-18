package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
public abstract class AbstractJdbcProcedureNodeMapCacheSupplier implements JdbcProcedureNodeMapSupplier {

    protected final ConcurrentHashMap<String, XmlNode> nodeMap = new ConcurrentHashMap<>();

    protected volatile Thread refreshThread = null;
    protected AtomicBoolean refreshing = new AtomicBoolean(false);


    @Override
    public Map<String, XmlNode> getNodeMap() {
        if (nodeMap.isEmpty()) {
            refreshNodeMap();
        }
        return new HashMap<>(nodeMap);
    }

    public void startRefreshThread(long intervalSeconds) {
        if (intervalSeconds < 0) {
            refreshing.set(false);
            if (refreshThread != null) {
                refreshThread.interrupt();
            }
            refreshThread = null;
        }
        refreshing.set(true);
        Thread thread = new Thread(() -> {
            while (refreshing.get()) {
                try {
                    refreshNodeMap();
                } catch (Exception e) {
                }
                try {
                    Thread.sleep(intervalSeconds * 1000);
                } catch (Exception e) {
                }
            }
        });
        thread.setName("procedure-xml-refresher");
        thread.setDaemon(true);
        thread.start();
        refreshThread = thread;
    }

    public void refreshNodeMap() {
        Map<String, XmlNode> map = parseResources();
        nodeMap.putAll(map);
    }

    public abstract Map<String, XmlNode> parseResources();


}
