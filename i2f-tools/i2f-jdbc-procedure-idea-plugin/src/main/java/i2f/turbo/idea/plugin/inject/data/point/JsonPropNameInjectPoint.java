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
    protected List<String> treePropNameList;
    protected boolean treePropNameStrict;
    protected List<String> contextPropNameNames;

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

    public List<String> getContextPropNameNames() {
        return contextPropNameNames;
    }

    public JsonPropNameInjectPoint contextPropNameNames(List<String> contextPropNameNames) {
        this.contextPropNameNames = contextPropNameNames;
        return this;
    }
}
