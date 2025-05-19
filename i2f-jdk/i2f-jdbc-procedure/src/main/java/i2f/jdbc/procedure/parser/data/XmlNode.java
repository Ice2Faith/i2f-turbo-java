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
    protected NodeType nodeType;
    protected String tagName;
    protected Map<String, String> tagAttrMap;
    protected Map<String, List<String>> attrFeatureMap;
    protected String tagBody;
    protected String textBody;
    protected List<XmlNode> children;
    protected String locationFile;
    protected int locationLineNumber = -1;
    protected String nodeLocation;

    public static enum NodeType {
        ELEMENT,
        TEXT,
        CDATA
    }

    public static String getNodeLocation(XmlNode node) {
        return getNodeLocation(node, null);
    }

    public static String getNodeLocation(XmlNode node,Integer lineNumber) {
        if (node == null) {
            return "";
        }
        String ret = node.getNodeLocation();
        if(ret!=null){
            return ret;
        }
        ret= "" + node.getLocationFile() + ":" + (lineNumber!=null?lineNumber:node.getLocationLineNumber()) + ":" + node.getTagName() + "";
        node.setNodeLocation(ret);
        return ret;
    }
}
