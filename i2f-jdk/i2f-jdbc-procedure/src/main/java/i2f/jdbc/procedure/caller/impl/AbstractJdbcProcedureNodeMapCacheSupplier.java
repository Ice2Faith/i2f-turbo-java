package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.caller.JdbcProcedureNodeMapSupplier;
import i2f.jdbc.procedure.context.CacheObjectRefresherSupplier;
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
public abstract class AbstractJdbcProcedureNodeMapCacheSupplier
        extends CacheObjectRefresherSupplier<Map<String, XmlNode>, ConcurrentHashMap<String, XmlNode>>
        implements JdbcProcedureNodeMapSupplier {

    protected volatile Thread refreshThread = null;
    protected AtomicBoolean refreshing = new AtomicBoolean(false);

    public AbstractJdbcProcedureNodeMapCacheSupplier() {
        super(new ConcurrentHashMap<>(), "xproc4j-node-map-refresher");
    }

    @Override
    public Map<String, XmlNode> getNodeMap() {
        return get();
    }

    @Override
    public boolean isMissingCache() {
        return cache == null || cache.isEmpty();
    }

    @Override
    public Map<String, XmlNode> wrapGet(ConcurrentHashMap<String, XmlNode> ret) {
        return new HashMap<>(ret);
    }

    @Override
    public void refresh() {
        Map<String, XmlNode> map = parseResources();
        cache.putAll(map);
    }

    public abstract Map<String, XmlNode> parseResources();


}
