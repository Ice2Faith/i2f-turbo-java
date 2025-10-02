package i2f.jdbc.procedure.parser.data;

import i2f.reference.Reference;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * XML 节点的属性常量优化使用
     * 这两个字段由执行器自动维护管理
     * 一旦进入了优化器模块， attrConstValueMap 保证始终有值 Optional
     * 但是 Optional 内部的 Reference 如果有值 Reference.isValue()，则表示进行了常量优化，否则 !Reference.isValue()没有常量优化
     * 如果进行了常量优化，则 attrConstFeatureMap 中保存的就是不能被常量优化的 features 修饰符
     */
    protected transient Map<String, Optional<Reference<Object>>> attrConstValueMap = new ConcurrentHashMap<>();
    protected transient Map<String, List<String>> attrConstFeatureMap = new ConcurrentHashMap<>();

    public static enum NodeType {
        ELEMENT,
        TEXT,
        CDATA
    }

    public static String getNodeLocation(XmlNode node) {
        if (node == null) {
            return "";
        }
        String ret = node.getNodeLocation();
        if (ret != null) {
            return ret;
        }
        ret = "" + node.getLocationFile() + ":" + node.getLocationLineNumber() + ":" + node.getTagName() + "";
        node.setNodeLocation(ret);
        return ret;
    }

    public static String getNodeLocation(XmlNode node, Integer lineNumber) {
        if (lineNumber == null || lineNumber < 0) {
            return getNodeLocation(node);
        }
        if (node == null) {
            return "";
        }
        String ret = "" + node.getLocationFile() + ":" + lineNumber + ":" + node.getTagName() + "";
        return ret;
    }
}
