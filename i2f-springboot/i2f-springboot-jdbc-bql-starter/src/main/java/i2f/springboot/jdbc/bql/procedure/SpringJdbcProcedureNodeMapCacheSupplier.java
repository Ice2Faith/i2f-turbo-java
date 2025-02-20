package i2f.springboot.jdbc.bql.procedure;

import i2f.jdbc.procedure.caller.impl.AbstractJdbcProcedureNodeMapCacheSupplier;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Ice2Faith
 * @date 2025/2/8 10:15
 */
@Data
@NoArgsConstructor
@Slf4j
public class SpringJdbcProcedureNodeMapCacheSupplier extends AbstractJdbcProcedureNodeMapCacheSupplier {

    protected final CopyOnWriteArraySet<String> xmlLocations = new CopyOnWriteArraySet<>();
    protected final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    public SpringJdbcProcedureNodeMapCacheSupplier(List<String> xmlLocations) {
        if (xmlLocations == null) {
            return;
        }
        this.xmlLocations.addAll(xmlLocations);
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        Map<String, XmlNode> ret = new HashMap<>();
        if (xmlLocations == null || xmlLocations.isEmpty()) {
            return ret;
        }
        Set<Resource> resources = new HashSet<>();
        for (String location : xmlLocations) {
            if (location == null || location.isEmpty()) {
                continue;
            }
            try {
                Resource[] arr = resourcePatternResolver.getResources(location);
                if (arr != null) {
                    resources.addAll(Arrays.asList(arr));
                }
            } catch (IOException e) {

            }
        }
        for (Resource resource : resources) {
            try (InputStream is = resource.getInputStream()) {
                XmlNode node = JdbcProcedureParser.parse(is);
                String id = node.getTagAttrMap().get("id");
                if (id == null) {
                    String filename = resource.getFilename();
                    if (filename != null) {
                        id = filename;
                        int idx = filename.lastIndexOf(".");
                        if (idx >= 0) {
                            id = filename.substring(0, idx);
                        }
                    }
                }
                if (id != null) {
                    ret.put(id, node);

                    Map<String, XmlNode> next = new HashMap<>();
                    JdbcProcedureParser.resolveEmbedIdNode(node, next);
                    for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                        ret.put(entry.getKey(), entry.getValue());
                        if(!entry.getKey().equals(id)){
                            String childId = id + "." + entry.getKey();
                            ret.put(childId, entry.getValue());
                        }
                    }
                }
            } catch (Exception e) {
                log.warn(resource.getFilename()+" parse error: "+e.getMessage(), e);
            }
        }
        return ret;
    }


}
