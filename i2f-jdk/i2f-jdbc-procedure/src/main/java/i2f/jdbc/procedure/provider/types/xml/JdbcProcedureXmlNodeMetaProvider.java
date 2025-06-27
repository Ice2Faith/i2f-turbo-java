package i2f.jdbc.procedure.provider.types.xml;

import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.JdbcProcedureMetaProvider;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:06
 * @desc
 */
@FunctionalInterface
public interface JdbcProcedureXmlNodeMetaProvider extends JdbcProcedureMetaProvider {
    Map<String, XmlNode> getNodeMap();

    @Override
    default Map<String, ProcedureMeta> getMetaMap() {
        Map<String, XmlNode> nodeMap = getNodeMap();
        return parseMetaMap(nodeMap);
    }

    default Map<String,ProcedureMeta> parseMetaMap(Map<String, XmlNode> nodeMap){
        Map<String, ProcedureMeta> metaMap = new LinkedHashMap<>();
        if (nodeMap != null) {
            for (Map.Entry<String, XmlNode> entry : nodeMap.entrySet()) {
                String key = entry.getKey();
                XmlNode value = entry.getValue();
                ProcedureMeta meta = ProcedureMeta.ofMeta(key, value);
                if (meta != null) {
                    metaMap.put(meta.getName(), meta);
                }
            }
        }
        return metaMap;
    }

    default XmlNode getNode(String procedureId) {
        return getNodeMap().get(procedureId);
    }
}
