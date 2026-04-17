package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.json.psi.*;
import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.JsonPropValueInjectPoint;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJava;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJavaXmlAttr;
import i2f.turbo.idea.plugin.inject.data.point.XmlTagBodyInjectPoint;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.metadata.JavaMetadataResolver;
import i2f.turbo.idea.plugin.inject.metadata.JsonMetadataResolver;
import i2f.turbo.idea.plugin.inject.metadata.XmlMetadataResolver;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:40
 * @desc
 */
public class JsonPropValueInjectHandler extends IProjectInjectHandler<JsonStringLiteral> {
    @Override
    public Class<JsonStringLiteral> supportType() {
        return JsonStringLiteral.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, JsonStringLiteral jsonStringLiteral) {
        PsiElement parent = jsonStringLiteral.getParent();
        if(!(parent instanceof JsonProperty)){
            return;
        }
        JsonProperty jsonProperty = (JsonProperty) parent;
        JsonStringLiteral propNameLiteral=null;
        JsonStringLiteral propValueLiteral=null;
        int index=0;
        @NotNull PsiElement[] children = jsonProperty.getChildren();
        for (@NotNull PsiElement item : children) {
            if(item instanceof JsonStringLiteral){
                index++;
                if(index==1){
                    propNameLiteral=(JsonStringLiteral)item;
                }
                if(index==2){
                    propValueLiteral=(JsonStringLiteral)item;
                }
            }
        }

        if(propValueLiteral!=jsonStringLiteral){
            return;
        }

        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(jsonStringLiteral.getProject(), LanguageInjectItem.TYPE_JSON_PROP_VALUE);

        if (configList == null || configList.isEmpty()) {
            return;
        }

        for (LanguageInjectItem item : configList) {
            List<JsonPropValueInjectPoint> points = item.getPointsOnType(JsonPropValueInjectPoint.class);
            for (JsonPropValueInjectPoint point : points) {
                String fileNamePattern = point.getFileName();
                if (!StringUtils.isEmpty(fileNamePattern)) {
                    PsiFile psiFile = jsonStringLiteral.getContainingFile();
                    VirtualFile virtualFile = psiFile.getVirtualFile();
                    String fileName = virtualFile.getName();
                    if (!SimpleMatcher.INSTANCE.matches(fileName, fileNamePattern)) {
                        continue;
                    }
                }

                String propNamePattern = point.getPropName();
                if (!StringUtils.isEmpty(propNamePattern)) {
                    if(propNameLiteral==null){
                        continue;
                    }
                    String propName = propNameLiteral.getValue();
                    if (!SimpleMatcher.INSTANCE.matches(propName, propNamePattern)) {
                        continue;
                    }
                }

                String parentTypePattern = point.getParentType();
                if (!StringUtils.isEmpty(parentTypePattern)) {
                    String parentType=null;
                    PsiElement jsonParent = jsonProperty.getParent();
                    if(jsonParent instanceof JsonObject){
                        parentType="object";
                    }else if(jsonParent instanceof JsonArray){
                        parentType="array";
                    }
                    if(parentType==null){
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
                    validTreePropNameList(jsonProperty.getParent(), treePropNameList, 0, strict, matched);
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
                JavaMetadataResolver.fillJavaMetadata(item,context,jsonProperty.getProject());

                context.put("prop", JsonMetadataResolver.getPropertyMetadata(jsonProperty));

                List<String> contextPropNames = point.getContextPropNames();
                if (contextPropNames != null && !contextPropNames.isEmpty()) {
                    Map<String, Object> contextPropNamesMap = new HashMap<>();
                    collectTreePropContext(jsonProperty.getParent(), contextPropNames, contextPropNamesMap);
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

                registrar.startInjecting(targetLang)
                        .addPlace(prefix,
                                suffix,
                                (PsiLanguageInjectionHost) jsonStringLiteral,
                                new TextRange(1, jsonStringLiteral.getTextRange().getLength()-1))
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
        if(elem instanceof JsonProperty){
            JsonProperty jsonProperty = (JsonProperty) elem;
            String currentPattern = propNameList.get(currIndex);
            String name = jsonProperty.getName();
            boolean ok = SimpleMatcher.INSTANCE.matches(name, currentPattern);
            if (!ok) {
                if (strict) {
                    matched.set(false);
                    return;
                }
                validTreePropNameList(elem.getParent(), propNameList, currIndex, strict, matched);
            }
            validTreePropNameList(elem.getParent(), propNameList, currIndex + 1, strict, matched);
        }
        validTreePropNameList(elem.getParent(), propNameList, currIndex, strict, matched);
    }


    public static void collectTreePropContext(PsiElement elem, List<String> tagNameList, Map<String, Object> context) {
        if (elem == null) {
            return;
        }
        if(elem instanceof JsonProperty) {
            JsonProperty jsonProperty = (JsonProperty) elem;
            String name = jsonProperty.getName();
            if (!context.containsKey(name)) {
                for (String namePattern : tagNameList) {
                    if (SimpleMatcher.INSTANCE.matches(name, namePattern)) {
                        Map<String, Object> metadata = JsonMetadataResolver.getPropertyMetadata(jsonProperty);
                        context.put(name, metadata);
                        break;
                    }
                }

            }
        }
        collectTreePropContext(elem.getParent(), tagNameList, context);
    }
}
