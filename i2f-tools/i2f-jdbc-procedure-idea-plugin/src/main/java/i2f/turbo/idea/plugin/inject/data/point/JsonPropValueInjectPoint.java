package i2f.turbo.idea.plugin.inject.data.point;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:06
 * @desc
 */
public class JsonPropValueInjectPoint {
    protected String fileName;
    protected String propName;
    protected String parentType;
    protected List<String> treePropNameList;
    protected boolean treePropNameStrict;
    protected List<String> contextPropNameNames;

    public String getFileName() {
        return fileName;
    }

    public JsonPropValueInjectPoint fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getPropName() {
        return propName;
    }

    public JsonPropValueInjectPoint propName(String propName) {
        this.propName = propName;
        return this;
    }

    public String getParentType() {
        return parentType;
    }

    public JsonPropValueInjectPoint parentType(String parentType) {
        this.parentType = parentType;
        return this;
    }

    public List<String> getTreePropNameList() {
        return treePropNameList;
    }

    public JsonPropValueInjectPoint treePropNameList(List<String> treePropNameList) {
        this.treePropNameList = treePropNameList;
        return this;
    }

    public boolean isTreePropNameStrict() {
        return treePropNameStrict;
    }

    public JsonPropValueInjectPoint treePropNameStrict(boolean treePropNameStrict) {
        this.treePropNameStrict = treePropNameStrict;
        return this;
    }

    public List<String> getContextPropNameNames() {
        return contextPropNameNames;
    }

    public JsonPropValueInjectPoint contextPropNameNames(List<String> contextPropNameNames) {
        this.contextPropNameNames = contextPropNameNames;
        return this;
    }
}
