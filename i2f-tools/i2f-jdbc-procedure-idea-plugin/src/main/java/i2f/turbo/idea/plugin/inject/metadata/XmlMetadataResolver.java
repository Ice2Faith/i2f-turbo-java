package i2f.turbo.idea.plugin.inject.metadata;

import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/17 14:04
 * @desc
 */
public class XmlMetadataResolver {

    public static Map<String, Object> getTagMetadata(XmlTag tag) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", tag.getName());
        List<Map<String, Object>> attributes = new ArrayList<>();
        ret.put("attributes", attributes);
        XmlAttribute[] arr = tag.getAttributes();
        if (arr != null && arr.length > 0) {
            for (XmlAttribute item : arr) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", item.getName());
                map.put("value", item.getValue());
                attributes.add(map);
            }
        }
        return ret;
    }
}
