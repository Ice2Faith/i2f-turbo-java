package i2f.jdbc.procedure.provider.types.xml.impl;

import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.context.CacheObjectRefresherSupplier;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.types.xml.JdbcProcedureXmlNodeMetaProvider;
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
public abstract class AbstractJdbcProcedureXmlNodeMetaCacheProvider
        extends CacheObjectRefresherSupplier<Map<String, XmlNode>, ConcurrentHashMap<String, XmlNode>>
        implements JdbcProcedureXmlNodeMetaProvider {

    protected volatile Thread refreshThread = null;
    protected AtomicBoolean refreshing = new AtomicBoolean(false);

    public AbstractJdbcProcedureXmlNodeMetaCacheProvider() {
        super(new ConcurrentHashMap<>(), XProc4jConsts.NAME + "-node-map-refresher");
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
