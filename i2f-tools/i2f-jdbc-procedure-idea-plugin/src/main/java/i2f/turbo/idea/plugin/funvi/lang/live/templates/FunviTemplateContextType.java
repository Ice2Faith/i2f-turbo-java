package i2f.turbo.idea.plugin.funvi.lang.live.templates;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import i2f.turbo.idea.plugin.funvi.FunviConsts;
import i2f.turbo.idea.plugin.funvi.FunviLanguage;
import org.jetbrains.annotations.NotNull;

public class FunviTemplateContextType extends TemplateContextType {

    protected FunviTemplateContextType() {
        super(FunviConsts.LANGUAGE_ID, FunviConsts.LANGUAGE_ID);
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getLanguage().isKindOf(FunviLanguage.INSTANCE);
    }

}
