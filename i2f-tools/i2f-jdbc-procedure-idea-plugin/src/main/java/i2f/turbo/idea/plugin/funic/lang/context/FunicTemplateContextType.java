package i2f.turbo.idea.plugin.funic.lang.context;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import i2f.turbo.idea.plugin.funic.FunicConsts;
import i2f.turbo.idea.plugin.funic.FunicLanguage;
import org.jetbrains.annotations.NotNull;

public class FunicTemplateContextType extends TemplateContextType {

    protected FunicTemplateContextType() {
        super(FunicConsts.LANGUAGE_ID, FunicConsts.LANGUAGE_ID);
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getLanguage().isKindOf(FunicLanguage.INSTANCE);
    }

}
