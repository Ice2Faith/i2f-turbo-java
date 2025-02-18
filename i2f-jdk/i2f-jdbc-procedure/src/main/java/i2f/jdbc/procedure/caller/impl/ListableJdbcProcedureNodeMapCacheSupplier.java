package i2f.jdbc.procedure.caller.impl;

import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2025/2/18 22:35
 * @desc
 */
public class ListableJdbcProcedureNodeMapCacheSupplier extends AbstractJdbcProcedureNodeMapCacheSupplier {
    protected CopyOnWriteArraySet<URL> resourceList = new CopyOnWriteArraySet<>();

    public ListableJdbcProcedureNodeMapCacheSupplier(CopyOnWriteArraySet<URL> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        Map<String, XmlNode> ret = new HashMap<>();
        for (URL resource : resourceList) {
            try (InputStream is = resource.openStream()) {
                XmlNode node = JdbcProcedureParser.parse(is);
                String id = node.getTagAttrMap().get("id");
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
