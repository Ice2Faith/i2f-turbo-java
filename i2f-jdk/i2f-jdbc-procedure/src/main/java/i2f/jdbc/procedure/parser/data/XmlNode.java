package i2f.jdbc.procedure.parser.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 9:27
 */
@Data
@NoArgsConstructor
public class XmlNode {
    protected String nodeType;
    protected String tagName;
    protected Map<String, String> tagAttrMap;
    protected Map<String, List<String>> attrFeatureMap;
    protected String tagBody;
    protected String textBody;
    protected List<XmlNode> children;
}
