package i2f.turbo.idea.plugin.funic.lang.live.templates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2025/3/26 23:35
 * @desc
 */
public class FunicLiveTemplatesProvider implements DefaultLiveTemplatesProvider {
    @Override
    public String[] getDefaultLiveTemplateFiles() {
        return new String[]{"assets/funic/liveTemplates/Funic.xml"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}
