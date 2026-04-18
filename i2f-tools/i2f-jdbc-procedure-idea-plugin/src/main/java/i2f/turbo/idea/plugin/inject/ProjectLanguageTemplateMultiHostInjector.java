package i2f.turbo.idea.plugin.inject;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.handlers.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/14 9:56
 * @desc
 */
public class ProjectLanguageTemplateMultiHostInjector implements MultiHostInjector {
    public static final Logger log = Logger.getInstance(ProjectLanguageTemplateMultiHostInjector.class);

    protected final List<IProjectInjectHandler<?>> handlers = Arrays.asList(
            new AnnotationInjectHandler(),
            new JsonPropNameInjectHandler(),
            new JsonPropValueInjectHandler(),
            new PropertiesValueInjectHandler(),
            new XmlAttrValueInjectHandler(),
            new XmlTagBodyInjectHandler(),
            new XmlTextSqlParameterInjectHandler(),
            new YamlPropValueInjectHandler()
    );

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
        for (IProjectInjectHandler<?> handler : handlers) {
            handler.inject(registrar, context);
        }
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        List<Class<? extends PsiElement>> ret = new ArrayList<>();
        for (IProjectInjectHandler<?> handler : handlers) {
            ret.add(handler.supportType());
        }
        return ret;
    }


}
