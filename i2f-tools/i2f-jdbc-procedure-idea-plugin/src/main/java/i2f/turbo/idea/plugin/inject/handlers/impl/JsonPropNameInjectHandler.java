package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.json.psi.JsonProperty;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:40
 * @desc
 */
public class JsonPropNameInjectHandler extends IProjectInjectHandler<JsonStringLiteral> {
    @Override
    public Class<JsonStringLiteral> supportType() {
        return JsonStringLiteral.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, JsonStringLiteral context) {
        PsiElement parent = context.getParent();
        if(!(parent instanceof JsonProperty)){
            return;
        }
        JsonProperty jsonProperty = (JsonProperty) parent;
        int index=0;
        @NotNull PsiElement[] children = jsonProperty.getChildren();
        for (@NotNull PsiElement item : children) {
            if(item instanceof JsonStringLiteral){
                index++;
                if(item==context){
                    break;
                }
            }
        }
        // index=1 属性名,index=2 属性值
        if(index!=1){
            return;
        }


    }
}
