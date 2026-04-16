package i2f.turbo.idea.plugin.inject.data.point;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:06
 * @desc
 */
public class XmlAttrValueInjectPoint {
    protected String fileName;
    protected String attrName;
    protected String tagName;
    protected String rootTagName;
    protected List<String> treeTagNameList;
    protected boolean treeTagNameStrict;
    protected List<String> contextTreeTagNames;
    protected XmlRelationContextJava contextJava;

    public String getFileName() {
        return fileName;
    }

    public XmlAttrValueInjectPoint fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getAttrName() {
        return attrName;
    }

    public XmlAttrValueInjectPoint attrName(String attrName) {
        this.attrName = attrName;
        return this;
    }

    public String getTagName() {
        return tagName;
    }

    public XmlAttrValueInjectPoint tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public String getRootTagName() {
        return rootTagName;
    }

    public XmlAttrValueInjectPoint rootTagName(String rootTagName) {
        this.rootTagName = rootTagName;
        return this;
    }

    public List<String> getTreeTagNameList() {
        return treeTagNameList;
    }

    public XmlAttrValueInjectPoint treeTagNameList(List<String> treeTagNameList) {
        this.treeTagNameList = treeTagNameList;
        return this;
    }

    public boolean isTreeTagNameStrict() {
        return treeTagNameStrict;
    }

    public XmlAttrValueInjectPoint treeTagNameStrict(boolean treeTagNameStrict) {
        this.treeTagNameStrict = treeTagNameStrict;
        return this;
    }

    public List<String> getContextTreeTagNames() {
        return contextTreeTagNames;
    }

    public XmlAttrValueInjectPoint contextTreeTagNames(List<String> contextTreeTagNames) {
        this.contextTreeTagNames = contextTreeTagNames;
        return this;
    }

    public XmlRelationContextJava getContextJava() {
        return contextJava;
    }

    public XmlAttrValueInjectPoint contextJava(XmlRelationContextJava contextJava) {
        this.contextJava = contextJava;
        return this;
    }
}
