package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJava;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJavaXmlAttr;
import i2f.turbo.idea.plugin.inject.data.point.XmlTagBodyInjectPoint;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
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
public class XmlTagBodyInjectHandler extends IProjectInjectHandler<XmlTag> {
    @Override
    public Class<XmlTag> supportType() {
        return XmlTag.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, XmlTag tag) {
        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(tag.getProject(), LanguageInjectItem.TYPE_XML_TAG_BODY);

        if (configList == null || configList.isEmpty()) {
            return;
        }


        for (LanguageInjectItem item : configList) {
            List<XmlTagBodyInjectPoint> points = item.getPointsOnType(XmlTagBodyInjectPoint.class);
            for (XmlTagBodyInjectPoint point : points) {
                String fileNamePattern = point.getFileName();
                if (!StringUtils.isEmpty(fileNamePattern)) {
                    XmlFile xmlFile = (XmlFile) tag.getContainingFile();
                    VirtualFile virtualFile = xmlFile.getVirtualFile();
                    String fileName = virtualFile.getName();
                    if (!SimpleMatcher.INSTANCE.matches(fileName, fileNamePattern)) {
                        continue;
                    }
                }

                String tagNamePattern = point.getTagName();
                if (!StringUtils.isEmpty(tagNamePattern)) {
                    String tagName = tag.getName();
                    if (!SimpleMatcher.INSTANCE.matches(tagName, tagNamePattern)) {
                        continue;
                    }
                }

                String rootTagNamePattern = point.getRootTagName();
                if (!StringUtils.isEmpty(rootTagNamePattern)) {
                    XmlFile xmlFile = (XmlFile) tag.getContainingFile();
                    XmlTag rootTag = xmlFile.getRootTag();
                    if (rootTag == null) {
                        continue;
                    }
                    String tagName = rootTag.getName();
                    if (!SimpleMatcher.INSTANCE.matches(tagName, rootTagNamePattern)) {
                        continue;
                    }
                }

                List<String> treeTagNameList = point.getTreeTagNameList();
                boolean strict = point.isTreeTagNameStrict();
                if (treeTagNameList != null && !treeTagNameList.isEmpty()) {
                    AtomicBoolean matched = new AtomicBoolean(true);
                    XmlAttrValueInjectHandler.validTreeTagNameList(tag.getParentTag(), treeTagNameList, 0, strict, matched);
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
                context.put("tag", XmlAttrValueInjectHandler.getTagMetadata(tag));

                List<String> contextTreeTagNames = point.getContextTreeTagNames();
                if (contextTreeTagNames != null && !contextTreeTagNames.isEmpty()) {
                    Map<String, Object> contextTreeTags = new HashMap<>();
                    XmlAttrValueInjectHandler.collectTreeTagContext(tag.getParentTag(), contextTreeTagNames, contextTreeTags);
                    context.put("contextTreeTags", contextTreeTags);
                }

                XmlRelationContextJava contextJava = point.getContextJava();
                if (contextJava != null) {
                    Map<String, Object> contextJavaMap = new HashMap<>();
                    context.put("contextJava", contextJavaMap);

                    XmlRelationContextJavaXmlAttr javaClass = contextJava.getJavaClass();
                    AtomicReference<PsiClass> classRef = new AtomicReference<>();
                    XmlAttrValueInjectHandler.matchJavaClass(tag, javaClass, classRef);

                    if (classRef.get() != null) {
                        Map<String, Object> metadata = AnnotationInjectHandler.getClassMetadata(classRef.get());
                        contextJavaMap.put("javaClass", metadata);
                    }

                    XmlRelationContextJavaXmlAttr javaMethod = contextJava.getJavaMethod();
                    AtomicReference<PsiMethod> methodRef = new AtomicReference<>();
                    XmlAttrValueInjectHandler.matchJavaMethod(tag, javaMethod, methodRef, classRef.get());

                    if (methodRef.get() != null) {
                        Map<String, Object> metadata = AnnotationInjectHandler.getMethodMetadata(methodRef.get());
                        contextJavaMap.put("javaMethod", metadata);
                    }
                }


                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(AnnotationInjectHandler.class.getClassLoader());
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

                tagChildrenApplyInject(registrar, tag, targetLang, prefix, suffix);
            }
        }
    }

    public static void tagChildrenApplyInject(MultiHostRegistrar registrar, XmlTag tag, Language targetLang, String prefix, String suffix) {
        if (tag == null) {
            return;
        }
        @NotNull PsiElement[] children = tag.getChildren();
        if (children == null || children.length == 0) {
            return;
        }
        for (@NotNull PsiElement item : children) {
            if (item instanceof XmlTag) {
                XmlTag xmlTag = (XmlTag) item;
                tagChildrenApplyInject(registrar, xmlTag, targetLang, prefix, suffix);
            } else if (item instanceof XmlText) {
                XmlText xmlText = (XmlText) item;
                registrar.startInjecting(targetLang)
                        .addPlace(prefix,
                                suffix,
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            }
        }
    }

}
