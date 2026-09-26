package i2f.turbo.idea.plugin.funvi.lang.live.templates;

import com.intellij.codeInsight.template.LiveTemplateContext;
import com.intellij.codeInsight.template.LiveTemplateContextProvider;
import com.intellij.codeInsight.template.TemplateContextType;
import i2f.turbo.idea.plugin.funvi.FunviConsts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ice2Faith
 * @date 2026/5/12 21:22
 * @desc
 */
public class FunviLiveTemplateContextProvider implements LiveTemplateContextProvider {
    @Override
    public @NotNull Collection<LiveTemplateContext> createContexts() {
        return Collections.singletonList(new FunviLiveTemplateContext());
    }

    public static class FunviLiveTemplateContext implements LiveTemplateContext {

        @Override
        public @NotNull String getContextId() {
            return FunviConsts.LANGUAGE_ID;
        }

        @Override
        public @Nullable String getBaseContextId() {
            return null;
        }

        @Override
        public @NotNull TemplateContextType getTemplateContextType() {
            return new FunviTemplateContextType();
        }
    }
}
