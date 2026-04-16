package i2f.turbo.idea.plugin.inject;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import i2f.turbo.idea.plugin.inject.handlers.impl.AnnotationInjectHandler;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.handlers.impl.XmlAttrValueInjectHandler;
import i2f.turbo.idea.plugin.inject.handlers.impl.XmlTagBodyInjectHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/4/14 9:56
 * @desc
 */
public class ProjectLanguageTemplateMultiHostInjector implements MultiHostInjector {
    public static final Logger log = Logger.getInstance(ProjectLanguageTemplateMultiHostInjector.class);

    protected final List<IProjectInjectHandler<?>> handlers=Arrays.asList(
            new AnnotationInjectHandler(),
            new XmlAttrValueInjectHandler(),
            new XmlTagBodyInjectHandler()
    );

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
        for (IProjectInjectHandler<?> handler : handlers) {
            handler.inject(registrar,context);
        }
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        List<Class<? extends PsiElement>> ret=new ArrayList<>();
        for (IProjectInjectHandler<?> handler : handlers) {
            ret.add(handler.supportType());
        }
        return ret;
    }


}
