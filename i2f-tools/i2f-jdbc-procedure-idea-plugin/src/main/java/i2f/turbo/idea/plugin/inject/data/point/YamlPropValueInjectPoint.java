package i2f.turbo.idea.plugin.inject.data.point;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:06
 * @desc
 */
public class YamlPropValueInjectPoint {
    protected String fileName;
    protected String propName;
    protected String parentType;
    protected List<String> treePropNameList;
    protected boolean treePropNameStrict;
    protected List<String> contextPropNames;


    public String getFileName() {
        return fileName;
    }

    public YamlPropValueInjectPoint fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getPropName() {
        return propName;
    }

    public YamlPropValueInjectPoint propName(String propName) {
        this.propName = propName;
        return this;
    }

    public String getParentType() {
        return parentType;
    }

    public YamlPropValueInjectPoint parentType(String parentType) {
        this.parentType = parentType;
        return this;
    }

    public List<String> getTreePropNameList() {
        return treePropNameList;
    }

    public YamlPropValueInjectPoint treePropNameList(List<String> treePropNameList) {
        this.treePropNameList = treePropNameList;
        return this;
    }

    public boolean isTreePropNameStrict() {
        return treePropNameStrict;
    }

    public YamlPropValueInjectPoint treePropNameStrict(boolean treePropNameStrict) {
        this.treePropNameStrict = treePropNameStrict;
        return this;
    }

    public List<String> getContextPropNames() {
        return contextPropNames;
    }

    public YamlPropValueInjectPoint contextPropNames(List<String> contextPropNames) {
        this.contextPropNames = contextPropNames;
        return this;
    }

}
