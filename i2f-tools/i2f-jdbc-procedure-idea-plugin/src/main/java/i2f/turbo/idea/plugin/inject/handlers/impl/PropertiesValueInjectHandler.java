package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.json.psi.JsonArray;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.properties.psi.Property;
import com.intellij.lang.properties.psi.impl.PropertyValueImpl;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.JsonPropValueInjectPoint;
import i2f.turbo.idea.plugin.inject.data.point.PropertiesValueInjectPoint;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.metadata.JavaMetadataResolver;
import i2f.turbo.idea.plugin.inject.metadata.JsonMetadataResolver;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/4/17 15:39
 * @desc
 */
public class PropertiesValueInjectHandler extends IProjectInjectHandler<PropertyValueImpl> {
    @Override
    public Class<PropertyValueImpl> supportType() {
        return PropertyValueImpl.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, PropertyValueImpl propertyValue) {
        PsiElement parent = propertyValue.getParent();
        if (!(parent instanceof Property)) {
            return;
        }
        Property property = (Property) parent;

        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(propertyValue.getProject(), LanguageInjectItem.TYPE_PROPERTIES_VALUE);

        if (configList == null || configList.isEmpty()) {
            return;
        }

        for (LanguageInjectItem item : configList) {
            List<PropertiesValueInjectPoint> points = item.getPointsOnType(PropertiesValueInjectPoint.class);
            for (PropertiesValueInjectPoint point : points) {
                String fileNamePattern = point.getFileName();
                if (!StringUtils.isEmpty(fileNamePattern)) {
                    PsiFile psiFile = property.getContainingFile();
                    VirtualFile virtualFile = psiFile.getVirtualFile();
                    String fileName = virtualFile.getName();
                    if (!SimpleMatcher.INSTANCE.matches(fileName, fileNamePattern)) {
                        continue;
                    }
                }

                String propNamePattern = point.getPropName();
                if (!StringUtils.isEmpty(propNamePattern)) {
                    String propName = property.getKey();
                    if (!SimpleMatcher.INSTANCE.matches(propName, propNamePattern)) {
                        continue;
                    }
                }

                LanguageInjectPlace inject = item.getInject();

                Language targetLang = ProjectInjectConfig.matchLanguage(inject.getLanguage());
                String prefix = inject.getPrefixTemplate();
                String suffix = inject.getSuffixTemplate();

                if (targetLang == null) {
                    continue;
                }

                Map<String, Object> context = new HashMap<>();
                JavaMetadataResolver.fillJavaMetadata(item, context, property.getProject());

                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(JavaMetadataResolver.class.getClassLoader());
                    if (!StringUtils.isEmpty(prefix)) {
                        prefix = VelocityGenerator.render(prefix, context);
                    }
                    if (!StringUtils.isEmpty(suffix)) {
                        suffix = VelocityGenerator.render(suffix, context);
                    }
                } finally {
                    Thread.currentThread().setContextClassLoader(oldClassLoader);
                }
                if (prefix == null) {
                    prefix = "";
                }
                if (suffix == null) {
                    suffix = "";
                }

                registrar.startInjecting(targetLang)
                        .addPlace(prefix,
                                suffix,
                                (PsiLanguageInjectionHost) propertyValue,
                                new TextRange(0, propertyValue.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

    }

}
