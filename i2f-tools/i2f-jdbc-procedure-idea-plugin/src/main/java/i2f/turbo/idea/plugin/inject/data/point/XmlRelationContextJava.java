package i2f.turbo.idea.plugin.inject.data.point;

/**
 * @author Ice2Faith
 * @date 2026/4/16 16:17
 * @desc
 */
public class XmlRelationContextJava {
    protected XmlRelationContextJavaXmlAttr javaClass;
    protected XmlRelationContextJavaXmlAttr javaMethod;

    public XmlRelationContextJavaXmlAttr getJavaClass() {
        return javaClass;
    }

    public XmlRelationContextJava javaClass(XmlRelationContextJavaXmlAttr javaClass) {
        this.javaClass = javaClass;
        return this;
    }

    public XmlRelationContextJavaXmlAttr getJavaMethod() {
        return javaMethod;
    }

    public XmlRelationContextJava javaMethod(XmlRelationContextJavaXmlAttr javaMethod) {
        this.javaMethod = javaMethod;
        return this;
    }
}
