package i2f.turbo.idea.plugin.inject.data.point;

/**
 * @author Ice2Faith
 * @date 2026/4/16 16:18
 * @desc
 */
public class XmlRelationContextJavaXmlAttr {
    protected String tagName;
    protected String attrName;

    public String getTagName() {
        return tagName;
    }

    public XmlRelationContextJavaXmlAttr tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public String getAttrName() {
        return attrName;
    }

    public XmlRelationContextJavaXmlAttr attrName(String attrName) {
        this.attrName = attrName;
        return this;
    }
}
