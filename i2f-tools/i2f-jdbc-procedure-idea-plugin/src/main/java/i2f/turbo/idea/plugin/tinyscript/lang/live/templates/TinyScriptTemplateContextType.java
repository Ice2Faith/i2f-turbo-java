package i2f.turbo.idea.plugin.tinyscript.lang.live.templates;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptConsts;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptLanguage;
import org.jetbrains.annotations.NotNull;

public class TinyScriptTemplateContextType extends TemplateContextType {

    protected TinyScriptTemplateContextType() {
        super(TinyScriptConsts.LANGUAGE_ID, TinyScriptConsts.LANGUAGE_ID);
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getLanguage().isKindOf(TinyScriptLanguage.INSTANCE);
    }

}
