package i2f.jdbc.procedure.provider.types.xml.impl;

import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:35
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableJdbcProcedureXmlNodeMetaCacheProvider extends AbstractJdbcProcedureXmlNodeMetaCacheProvider {
    protected final CopyOnWriteArraySet<URL> resourceList = new CopyOnWriteArraySet<>();
    protected volatile XProc4jEventHandler eventHandler = new DefaultXProc4jEventHandler();

    public ListableJdbcProcedureXmlNodeMetaCacheProvider(Collection<URL> resourceList) {
        if (resourceList != null) {
            this.resourceList.addAll(resourceList);
        }
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        Map<String, XmlNode> ret = new HashMap<>();
        for (URL resource : resourceList) {
            try {
                XmlNode node = JdbcProcedureParser.parse(resource);
                String id = node.getTagAttrMap().get(AttrConsts.ID);
                if (id != null) {
                    ret.put(id, node);

                    Map<String, XmlNode> next = new HashMap<>();
                    JdbcProcedureParser.resolveEmbedIdNode(node, next);
                    for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                        ret.put(entry.getKey(), entry.getValue());
                        if (!entry.getKey().equals(id)) {
                            String childId = id + "." + entry.getKey();
                            ret.put(childId, entry.getValue());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
