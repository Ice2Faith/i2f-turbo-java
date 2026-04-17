package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.json.psi.JsonArray;
import com.intellij.json.psi.JsonObject;
import com.intellij.json.psi.JsonProperty;
import com.intellij.json.psi.JsonStringLiteral;
import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlTag;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.JsonPropNameInjectPoint;
import i2f.turbo.idea.plugin.inject.data.point.JsonPropValueInjectPoint;
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
 * @date 2026/4/16 11:40
 * @desc
 */
public class JsonPropNameInjectHandler extends IProjectInjectHandler<JsonStringLiteral> {
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

        if(propNameLiteral!=jsonStringLiteral){
            return;
        }

        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(jsonStringLiteral.getProject(), LanguageInjectItem.TYPE_JSON_PROP_NAME);

        if (configList == null || configList.isEmpty()) {
            return;
        }

        for (LanguageInjectItem item : configList) {
            List<JsonPropNameInjectPoint> points = item.getPointsOnType(JsonPropNameInjectPoint.class);
            for (JsonPropNameInjectPoint point : points) {
                String fileNamePattern = point.getFileName();
                if (!StringUtils.isEmpty(fileNamePattern)) {
                    PsiFile psiFile = jsonStringLiteral.getContainingFile();
                    VirtualFile virtualFile = psiFile.getVirtualFile();
                    String fileName = virtualFile.getName();
                    if (!SimpleMatcher.INSTANCE.matches(fileName, fileNamePattern)) {
                        continue;
                    }
                }

                String parentNamePattern = point.getParentName();
                if (!StringUtils.isEmpty(parentNamePattern)) {
                    if(propNameLiteral==null){
                        continue;
                    }
                    PsiElement parentElem=jsonProperty.getParent();
                    while(!(parentElem instanceof JsonProperty)){
                        parentElem=parentElem.getParent();
                    }
                    if(parentElem==null){
                        continue;
                    }
                    if(!(parentElem instanceof JsonProperty)){
                        continue;
                    }
                    JsonProperty parentProperty=(JsonProperty)parentElem;
                    String propName = parentProperty.getName();
                    if (!SimpleMatcher.INSTANCE.matches(propName, parentNamePattern)) {
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
                    JsonPropValueInjectHandler.validTreePropNameList(jsonProperty.getParent(), treePropNameList, 0, strict, matched);
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
                    JsonPropValueInjectHandler.collectTreePropContext(jsonProperty.getParent(), contextPropNames, contextPropNamesMap);
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
}
