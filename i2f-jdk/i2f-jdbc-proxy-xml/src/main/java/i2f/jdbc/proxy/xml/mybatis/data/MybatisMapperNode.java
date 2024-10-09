package i2f.jdbc.proxy.xml.mybatis.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/10/9 17:00
 */
@Data
@NoArgsConstructor
public class MybatisMapperNode {
    protected String namespace;
    protected String id;
    protected String unqId;

    protected boolean xmlType;
    protected short nodeType;
    protected String tagName;
    protected String content;
    protected Map<String, String> attributes;
    protected List<MybatisMapperNode> children;

    public String getXmlContent() {
        return getXmlContent(true);
    }

    public String getInnerContent() {
        return getXmlContent(false);
    }

    public String getTextContent() {
        return getXmlContent(false, true, false);
    }

    public String getXmlContent(boolean full) {
        return getXmlContent(full, false);
    }

    public String getXmlContent(boolean full, boolean less) {
        return getXmlContent(full, less, true);
    }

    public String getXmlContent(boolean full, boolean less, boolean recursiveFull) {
        if (xmlType) {
            StringBuilder builder = new StringBuilder();
            if (full) {
                builder.append("<").append(tagName);
                if (attributes != null && !attributes.isEmpty()) {
                    for (Map.Entry<String, String> entry : attributes.entrySet()) {
                        builder.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
                    }
                }
            }
            if (children == null || children.isEmpty()) {
                if (full) {
                    builder.append("/>");
                }
                return builder.toString();
            }
            if (full) {
                builder.append(">");
            }
            for (MybatisMapperNode child : children) {
                String next = child.getXmlContent(recursiveFull, less, recursiveFull);
                builder.append(next);
            }
            if (full) {
                builder.append("</").append(tagName).append(">");
            }
            return builder.toString();
        } else {
            if (Node.COMMENT_NODE == nodeType) {
                if (less) {
                    return "";
                }
                return "<!--" + content + "-->";
            }
            if (Node.CDATA_SECTION_NODE == nodeType) {
                if (less) {
                    return content;
                }
                return "<![CDATA[" + content + "]]>";
            }

            return content;

        }
    }
}
