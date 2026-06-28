package i2f.turbo.idea.plugin.funic.lang.live.templates;

import com.intellij.codeInsight.template.LiveTemplateContext;
import com.intellij.codeInsight.template.LiveTemplateContextProvider;
import com.intellij.codeInsight.template.TemplateContextType;
import i2f.turbo.idea.plugin.funic.FunicConsts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ice2Faith
 * @date 2026/5/12 21:22
 * @desc
 */
public class FunicLiveTemplateContextProvider implements LiveTemplateContextProvider {
    @Override
    public @NotNull Collection<LiveTemplateContext> createContexts() {
        return Collections.singletonList(new FunicLiveTemplateContext());
    }

    public static class FunicLiveTemplateContext implements LiveTemplateContext {

        @Override
        public @NotNull String getContextId() {
            return FunicConsts.LANGUAGE_ID;
        }

        @Override
        public @Nullable String getBaseContextId() {
            return null;
        }

        @Override
        public @NotNull TemplateContextType getTemplateContextType() {
            return new FunicTemplateContextType();
        }
    }
}
