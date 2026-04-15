package i2f.turbo.idea.plugin.inject.data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/14 11:10
 * @desc
 */
public class LanguageInjectItem {
    public static final String TYPE_ANNOTATION="annotation";

    protected String type;
    protected List<String> points;
    protected LanguageInjectPlace inject;

    public String getType() {
        return type;
    }

    public LanguageInjectItem type(String type) {
        this.type = type;
        return this;
    }

    public List<String> getPoints() {
        return points;
    }

    public LanguageInjectItem points(List<String> points) {
        this.points = points;
        return this;
    }

    public LanguageInjectPlace getInject() {
        return inject;
    }

    public LanguageInjectItem inject(LanguageInjectPlace inject) {
        this.inject = inject;
        return this;
    }
}
