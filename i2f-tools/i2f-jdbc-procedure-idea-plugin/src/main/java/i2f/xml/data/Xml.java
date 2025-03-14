package i2f.xml.data;


import org.w3c.dom.Node;

import java.util.List;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2025/3/5 11:16
 */
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Xml> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Xml> attributes) {
        this.attributes = attributes;
    }

    public List<Xml> getChildren() {
        return children;
    }

    public void setChildren(List<Xml> children) {
        this.children = children;
    }

    public Node getDom() {
        return dom;
    }

    public void setDom(Node dom) {
        this.dom = dom;
    }

    public String getInnerXml() {
        return innerXml;
    }

    public void setInnerXml(String innerXml) {
        this.innerXml = innerXml;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getLocationLineNumber() {
        return locationLineNumber;
    }

    public void setLocationLineNumber(int locationLineNumber) {
        this.locationLineNumber = locationLineNumber;
    }

    public int getLocationColumnNumber() {
        return locationColumnNumber;
    }

    public void setLocationColumnNumber(int locationColumnNumber) {
        this.locationColumnNumber = locationColumnNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        ;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ;
        Xml xml = (Xml) o;
        return locationLineNumber == xml.locationLineNumber
                && locationColumnNumber == xml.locationColumnNumber
                && type == xml.type
                && Objects.equals(name, xml.name)
                && Objects.equals(value, xml.value)
                && Objects.equals(attributes, xml.attributes)
                && Objects.equals(children, xml.children)
                && Objects.equals(dom, xml.dom)
                && Objects.equals(innerXml, xml.innerXml)
                && Objects.equals(locationName, xml.locationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, value, attributes, children, dom, innerXml, locationName, locationLineNumber, locationColumnNumber);
    }

    @Override
    public String toString() {
        return "Xml{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", attributes=" + attributes +
                ", children=" + children +
                ", dom=" + dom +
                ", innerXml='" + innerXml + '\'' +
                ", locationName='" + locationName + '\'' +
                ", locationLineNumber=" + locationLineNumber +
                ", locationColumnNumber=" + locationColumnNumber +
                '}';
    }
}
