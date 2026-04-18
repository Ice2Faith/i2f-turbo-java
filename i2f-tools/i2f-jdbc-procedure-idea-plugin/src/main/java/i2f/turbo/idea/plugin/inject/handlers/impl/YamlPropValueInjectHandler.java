package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.YamlPropValueInjectPoint;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.metadata.JavaMetadataResolver;
import i2f.turbo.idea.plugin.inject.metadata.YamlMetadataResolver;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;
import org.jetbrains.yaml.psi.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2026/4/18 16:42
 * @desc
 */
public class YamlPropValueInjectHandler extends IProjectInjectHandler<YAMLScalar> {
    protected static Logger log = Logger.getInstance(YamlPropValueInjectHandler.class);

    @Override
    public Class<YAMLScalar> supportType() {
        return YAMLScalar.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, YAMLScalar yamlScalar) {
        PsiElement parent = yamlScalar.getParent();
        log.warn("yaml-prop-value: parent " + (parent == null ? null : parent.getText()));
        if (!(parent instanceof YAMLKeyValue)) {
            return;
        }
        YAMLKeyValue yamlKeyValue = (YAMLKeyValue) parent;

        YAMLValue value = yamlKeyValue.getValue();

        log.warn("yaml-prop-value: value " + (value == null ? null : value.getText()));
        if (value != yamlScalar) {
            return;
        }

        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(yamlScalar.getProject(), LanguageInjectItem.TYPE_YAML_PROP_VALUE);
        log.warn("yaml-prop-value: configList " + (configList == null ? null : configList.size()));
        if (configList == null || configList.isEmpty()) {
            return;
        }

        for (LanguageInjectItem item : configList) {
            List<YamlPropValueInjectPoint> points = item.getPointsOnType(YamlPropValueInjectPoint.class);
            for (YamlPropValueInjectPoint point : points) {
                String fileNamePattern = point.getFileName();
                if (!StringUtils.isEmpty(fileNamePattern)) {
                    PsiFile psiFile = yamlScalar.getContainingFile();
                    VirtualFile virtualFile = psiFile.getVirtualFile();
                    String fileName = virtualFile.getName();
                    log.warn("yaml-prop-value: fileName " + (fileName));
                    if (!SimpleMatcher.INSTANCE.matches(fileName, fileNamePattern)) {
                        continue;
                    }
                }

                String propNamePattern = point.getPropName();
                if (!StringUtils.isEmpty(propNamePattern)) {
                    String propName = yamlKeyValue.getKeyText();
                    log.warn("yaml-prop-value: propName " + (propName));
                    if (!SimpleMatcher.INSTANCE.matches(propName, propNamePattern)) {
                        continue;
                    }
                }

                String parentTypePattern = point.getParentType();
                if (!StringUtils.isEmpty(parentTypePattern)) {
                    String parentType = null;
                    PsiElement yamlParent = yamlKeyValue.getParent();
                    if (yamlParent instanceof YAMLMapping) {
                        parentType = "object";
                    } else if (yamlParent instanceof YAMLSequence) {
                        parentType = "array";
                    }
                    if (parentType == null) {
                        continue;
                    }
                    if (!SimpleMatcher.INSTANCE.matches(parentType, parentTypePattern)) {
                        continue;
                    }
                }

                List<String> treePropNameList = point.getTreePropNameList();
                boolean strict = point.isTreePropNameStrict();
                if (treePropNameList != null && !treePropNameList.isEmpty()) {
                    AtomicBoolean matched = new AtomicBoolean(true);
                    validTreePropNameList(yamlKeyValue.getParent(), treePropNameList, 0, strict, matched);
                    if (!matched.get()) {
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
                JavaMetadataResolver.fillJavaMetadata(item, context, yamlKeyValue.getProject());

                context.put("prop", YamlMetadataResolver.getPropertyMetadata(yamlKeyValue));

                List<String> contextPropNames = point.getContextPropNames();
                if (contextPropNames != null && !contextPropNames.isEmpty()) {
                    Map<String, Object> contextPropNamesMap = new HashMap<>();
                    collectTreePropContext(yamlKeyValue.getParent(), contextPropNames, contextPropNamesMap);
                    context.put("contextPropNames", contextPropNamesMap);
                }

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

                log.warn("yaml-prop-value: prefix " + (prefix));
                int offsetIndex = 0;
                int endIndex = yamlScalar.getTextRange().getLength();
                if (yamlScalar instanceof YAMLQuotedText) {
                    offsetIndex = 1;
                    endIndex = endIndex - 1;
                }

                registrar.startInjecting(targetLang)
                        .addPlace(prefix,
                                suffix,
                                (PsiLanguageInjectionHost) yamlScalar,
                                new TextRange(offsetIndex, endIndex))
                        .doneInjecting();
                return;
            }
        }
    }


    public static void validTreePropNameList(PsiElement elem, List<String> propNameList, int currIndex, boolean strict, AtomicBoolean matched) {
        if (elem == null) {
            // 没有更多父节点，但是还未匹配完，那就是不匹配
            if (currIndex < propNameList.size()) {
                matched.set(false);
            } else {
                matched.set(true);
            }
            return;
        }
        if (elem instanceof YAMLKeyValue) {
            YAMLKeyValue yamlKeyValue = (YAMLKeyValue) elem;
            String currentPattern = propNameList.get(currIndex);
            String name = yamlKeyValue.getKeyText();
            boolean ok = SimpleMatcher.INSTANCE.matches(name, currentPattern);
            if (!ok) {
                if (strict) {
                    matched.set(false);
                    return;
                }
                validTreePropNameList(elem.getParent(), propNameList, currIndex, strict, matched);
                return;
            }
            validTreePropNameList(elem.getParent(), propNameList, currIndex + 1, strict, matched);
            return;
        }
        validTreePropNameList(elem.getParent(), propNameList, currIndex, strict, matched);
    }


    public static void collectTreePropContext(PsiElement elem, List<String> tagNameList, Map<String, Object> context) {
        if (elem == null) {
            return;
        }
        if (elem instanceof YAMLKeyValue) {
            YAMLKeyValue yamlKeyValue = (YAMLKeyValue) elem;
            String name = yamlKeyValue.getName();
            if (!context.containsKey(name)) {
                for (String namePattern : tagNameList) {
                    if (SimpleMatcher.INSTANCE.matches(name, namePattern)) {
                        Map<String, Object> metadata = YamlMetadataResolver.getPropertyMetadata(yamlKeyValue);
                        context.put(name, metadata);
                        break;
                    }
                }

            }
        }
        collectTreePropContext(elem.getParent(), tagNameList, context);
    }
}
