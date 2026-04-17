package i2f.turbo.idea.plugin.inject.data.point;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:06
 * @desc
 */
public class JsonPropNameInjectPoint {
    protected String fileName;
    protected String parentName;
    protected String parentType;
    protected List<String> treePropNameList;
    protected boolean treePropNameStrict;
    protected List<String> contextPropNames;


    public String getFileName() {
        return fileName;
    }

    public JsonPropNameInjectPoint fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getParentName() {
        return parentName;
    }

    public JsonPropNameInjectPoint parentName(String parentName) {
        this.parentName = parentName;
        return this;
    }

    public String getParentType() {
        return parentType;
    }

    public JsonPropNameInjectPoint parentType(String parentType) {
        this.parentType = parentType;
        return this;
    }

    public List<String> getTreePropNameList() {
        return treePropNameList;
    }

    public JsonPropNameInjectPoint treePropNameList(List<String> treePropNameList) {
        this.treePropNameList = treePropNameList;
        return this;
    }

    public boolean isTreePropNameStrict() {
        return treePropNameStrict;
    }

    public JsonPropNameInjectPoint treePropNameStrict(boolean treePropNameStrict) {
        this.treePropNameStrict = treePropNameStrict;
        return this;
    }

    public List<String> getContextPropNames() {
        return contextPropNames;
    }

    public JsonPropNameInjectPoint contextPropNames(List<String> contextPropNames) {
        this.contextPropNames = contextPropNames;
        return this;
    }
}
