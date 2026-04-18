package i2f.turbo.idea.plugin.inject.metadata;

import com.intellij.json.psi.JsonArray;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.psi.PsiElement;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/17 14:55
 * @desc
 */
public class JsonMetadataResolver {
    public static Map<String,Object> getPropertyMetadata(JsonProperty jsonProperty) {
        Map<String,Object> ret=new HashMap<>();
        ret.put("name",jsonProperty.getName());
        String parentType=null;
        PsiElement parent = jsonProperty.getParent();
        if(parent instanceof JsonObject){
            parentType="object";
        }else if(parent instanceof JsonArray){
            parentType="array";
        }
        ret.put("parentType",parentType);
        PsiElement parentElem=jsonProperty.getParent();
        while(!(parentElem instanceof JsonProperty)){
            parentElem=parentElem.getParent();
            if (parentElem == null) {
                break;
            }
        }
        String parentName=null;
        if(parentElem instanceof JsonProperty){
            JsonProperty property = (JsonProperty) parentElem;
            parentName= property.getName();
        }
        ret.put("parentName",parentName);
        return ret;
    }
}
