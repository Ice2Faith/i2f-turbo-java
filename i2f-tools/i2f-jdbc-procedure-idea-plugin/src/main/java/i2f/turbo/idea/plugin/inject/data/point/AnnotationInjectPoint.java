package i2f.turbo.idea.plugin.inject.data.point;

/**
 * @author Ice2Faith
 * @date 2026/4/16 10:55
 * @desc
 */
public class AnnotationInjectPoint {
    protected String type;
    protected String prop;

    public String getType() {
        return type;
    }

    public AnnotationInjectPoint type(String type) {
        this.type = type;
        return this;
    }

    public String getProp() {
        return prop;
    }

    public AnnotationInjectPoint prop(String prop) {
        this.prop = prop;
        return this;
    }
}
