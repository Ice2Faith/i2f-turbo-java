package i2f.jdbc.procedure.parser.data;


import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/1/20 9:27
 */

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
        return null;
    }

    public static String getNodeLocation(XmlNode node, Integer lineNumber) {
        return null;
    }
}
