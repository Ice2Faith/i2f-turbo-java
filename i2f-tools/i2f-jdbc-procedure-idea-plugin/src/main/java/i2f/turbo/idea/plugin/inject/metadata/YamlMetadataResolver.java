package i2f.turbo.idea.plugin.inject.metadata;

import com.intellij.json.psi.JsonArray;
import com.intellij.json.psi.JsonObject;
import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/17 14:55
 * @desc
 */
public class YamlMetadataResolver {
    public static Map<String, Object> getPropertyMetadata(YAMLKeyValue yamlKeyValue) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", yamlKeyValue.getKeyText());
        String parentType = null;
        PsiElement parent = yamlKeyValue.getParent();
        if (parent instanceof JsonObject) {
            parentType = "object";
        } else if (parent instanceof JsonArray) {
            parentType = "array";
        }
        ret.put("parentType", parentType);
        PsiElement parentElem = yamlKeyValue.getParent();
        while (!(parentElem instanceof YAMLKeyValue)) {
            parentElem = parentElem.getParent();
            if (parentElem == null) {
                break;
            }
        }
        String parentName = null;
        if (parentElem instanceof YAMLKeyValue) {
            YAMLKeyValue property = (YAMLKeyValue) parentElem;
            parentName = property.getKeyText();
        }
        ret.put("parentName", parentName);
        return ret;
    }
}
