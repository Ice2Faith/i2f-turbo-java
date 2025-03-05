package i2f.xml.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/3/5 11:16
 */
@Data
@NoArgsConstructor
public class Xml {
    public static enum Type {
        DOCUMENT,
        ELEMENT,
        ATTRIBUTE,
        COMMENT,
        CDATA,
        TEXT
    }

    protected Type type;
    protected String name;
    protected String value;
    protected List<Xml> attributes;
    protected List<Xml> children;

    protected transient Node dom;
    protected transient String innerXml;

    protected transient String locationName;
    protected transient int locationLineNumber = -1;
    protected transient int locationColumnNumber = -1;

}
