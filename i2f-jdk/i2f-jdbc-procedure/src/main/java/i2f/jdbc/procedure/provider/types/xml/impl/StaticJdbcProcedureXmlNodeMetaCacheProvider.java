package i2f.jdbc.procedure.provider.types.xml.impl;

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
public class StaticJdbcProcedureXmlNodeMetaCacheProvider extends AbstractJdbcProcedureXmlNodeMetaCacheProvider {
    protected ConcurrentHashMap<String, XmlNode> staticMap = new ConcurrentHashMap<>();

    public StaticJdbcProcedureXmlNodeMetaCacheProvider(Map<String, XmlNode> staticMap) {
        if (staticMap == null) {
            return;
        }
        putAll(staticMap);
    }

    public void putAll(Map<String,XmlNode> staticMap){
        for (Map.Entry<String, XmlNode> entry : staticMap.entrySet()) {
            if(entry.getKey()==null || entry.getValue()==null){
                continue;
            }
            staticMap.put(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        return staticMap;
    }
}
