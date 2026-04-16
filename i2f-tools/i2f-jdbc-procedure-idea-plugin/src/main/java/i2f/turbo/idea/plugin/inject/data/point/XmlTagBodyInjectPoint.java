package i2f.turbo.idea.plugin.inject.data.point;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:06
 * @desc
 */
public class XmlTagBodyInjectPoint {
    protected String fileName;
    protected String tagName;
    protected String rootTagName;
    protected List<String> treeTagNameList;
    protected boolean treeTagNameStrict;
    protected List<String> contextTreeTagNames;
    protected XmlRelationContextJava contextJava;

    public String getFileName() {
        return fileName;
    }

    public XmlTagBodyInjectPoint fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getTagName() {
        return tagName;
    }

    public XmlTagBodyInjectPoint tagName(String tagName) {
        this.tagName = tagName;
        return this;
    }

    public String getRootTagName() {
        return rootTagName;
    }

    public XmlTagBodyInjectPoint rootTagName(String rootTagName) {
        this.rootTagName = rootTagName;
        return this;
    }

    public List<String> getTreeTagNameList() {
        return treeTagNameList;
    }

    public XmlTagBodyInjectPoint treeTagNameList(List<String> treeTagNameList) {
        this.treeTagNameList = treeTagNameList;
        return this;
    }

    public boolean isTreeTagNameStrict() {
        return treeTagNameStrict;
    }

    public XmlTagBodyInjectPoint treeTagNameStrict(boolean treeTagNameStrict) {
        this.treeTagNameStrict = treeTagNameStrict;
        return this;
    }

    public List<String> getContextTreeTagNames() {
        return contextTreeTagNames;
    }

    public XmlTagBodyInjectPoint contextTreeTagNames(List<String> contextTreeTagNames) {
        this.contextTreeTagNames = contextTreeTagNames;
        return this;
    }


    public XmlRelationContextJava getContextJava() {
        return contextJava;
    }

    public XmlTagBodyInjectPoint contextJava(XmlRelationContextJava contextJava) {
        this.contextJava = contextJava;
        return this;
    }
}
