package i2f.turbo.idea.plugin.inject.data.point;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:06
 * @desc
 */
public class PropertiesValueInjectPoint {
    protected String fileName;
    protected String propName;

    public String getFileName() {
        return fileName;
    }

    public PropertiesValueInjectPoint fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getPropName() {
        return propName;
    }

    public PropertiesValueInjectPoint propName(String propName) {
        this.propName = propName;
        return this;
    }
}
