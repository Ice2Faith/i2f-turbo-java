package i2f.turbo.idea.plugin.inject.data;

import i2f.turbo.idea.plugin.inject.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/14 11:10
 * @desc
 */
public class LanguageInjectItem {
    public static final String TYPE_ANNOTATION="annotation";
    public static final String TYPE_XML_ATTR_VALUE="xml-attr-value";
    public static final String TYPE_XML_TAG_BODY="xml-tag-body";
    public static final String TYPE_TEXT_SQL_PARAMETER="xml-text-sql-parameter";
    public static final String TYPE_JSON_PROP_VALUE="json-prop-value";
    public static final String TYPE_JSON_PROP_NAME="json-prop-name";
    public static final String TYPE_PROPERTIES_VALUE="properties-value";

    protected String type;
    protected List<Map<String,Object>> points;
    protected LanguageInjectJavaMetadata javaMetadata;
    protected LanguageInjectPlace inject;

    public <T> List<T> getPointsOnType(Class<T> type){
        return JsonUtils.copyList(points,type);
    }

    public String getType() {
        return type;
    }

    public LanguageInjectItem type(String type) {
        this.type = type;
        return this;
    }

    public List<Map<String,Object>> getPoints() {
        return points;
    }

    public LanguageInjectItem points(List<Map<String,Object>> points) {
        this.points = points;
        return this;
    }

    public LanguageInjectJavaMetadata getJavaMetadata() {
        return javaMetadata;
    }

    public LanguageInjectItem javaMetadata(LanguageInjectJavaMetadata javaMetadata) {
        this.javaMetadata = javaMetadata;
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
