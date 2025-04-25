package i2f.jdbc.procedure.provider.types.xml.impl;

import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.event.ProcedureMetaProviderNotifyEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:35
 * @desc
 */
@Data
@NoArgsConstructor
public class StaticJdbcProcedureXmlNodeMetaCacheProvider extends AbstractJdbcProcedureXmlNodeMetaCacheProvider {
    protected ConcurrentHashMap<String, XmlNode> staticMap = new ConcurrentHashMap<>();
    protected volatile XProc4jEventHandler eventHandler = new XProc4jEventHandler();

    public StaticJdbcProcedureXmlNodeMetaCacheProvider(Map<String, XmlNode> map) {
        if (map == null) {
            return;
        }
        putAll(map);
    }

    public void notifyChange() {
        ProcedureMetaProviderNotifyEvent event = new ProcedureMetaProviderNotifyEvent();
        event.setProvider(this);
        if (eventHandler != null) {
            eventHandler.publish(event);
        }
    }

    public void putAll(Map<String, XmlNode> map) {
        for (Map.Entry<String, XmlNode> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            staticMap.put(entry.getKey(), entry.getValue());
        }
        notifyChange();
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        return staticMap;
    }
}
