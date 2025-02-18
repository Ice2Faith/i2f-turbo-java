package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:35
 * @desc
 */
@NoArgsConstructor
public class StaticJdbcProcedureNodeMapCacheSupplier extends AbstractJdbcProcedureNodeMapCacheSupplier {
    protected Map<String, XmlNode> staticMap = new ConcurrentHashMap<>();

    public StaticJdbcProcedureNodeMapCacheSupplier(Map<String, XmlNode> staticMap) {
        if (staticMap == null) {
            return;
        }
        this.staticMap = staticMap;
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        return staticMap;
    }
}
