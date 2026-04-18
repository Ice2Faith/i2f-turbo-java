package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.XmlAttrValueInjectPoint;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJava;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJavaXmlAttr;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.metadata.JavaMetadataResolver;
import i2f.turbo.idea.plugin.inject.metadata.XmlMetadataResolver;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;

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
public class XmlAttrValueInjectHandler extends IProjectInjectHandler<XmlAttributeValue> {
    @Override
    public Class<XmlAttributeValue> supportType() {
        return XmlAttributeValue.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, XmlAttributeValue attributeValue) {
        XmlAttribute attribute = (XmlAttribute) attributeValue.getParent();

        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(attributeValue.getProject(), LanguageInjectItem.TYPE_XML_ATTR_VALUE);

        if (configList == null || configList.isEmpty()) {
            return;
        }

        XmlTag tag = attribute.getParent();

        for (LanguageInjectItem item : configList) {
            List<XmlAttrValueInjectPoint> points = item.getPointsOnType(XmlAttrValueInjectPoint.class);
            for (XmlAttrValueInjectPoint point : points) {
                String fileNamePattern = point.getFileName();
                if (!StringUtils.isEmpty(fileNamePattern)) {
                    XmlFile xmlFile = (XmlFile) tag.getContainingFile();
                    VirtualFile virtualFile = xmlFile.getVirtualFile();
                    String fileName = virtualFile.getName();
                    if (!SimpleMatcher.INSTANCE.matches(fileName, fileNamePattern)) {
                        continue;
                    }
                }

                String attrNamePattern = point.getAttrName();
                if (!StringUtils.isEmpty(attrNamePattern)) {
                    String attrName = attribute.getName();
                    if (!SimpleMatcher.INSTANCE.matches(attrName, attrNamePattern)) {
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
                    validTreeTagNameList(tag.getParentTag(), treeTagNameList, 0, strict, matched);
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
                JavaMetadataResolver.fillJavaMetadata(item, context, tag.getProject());

                context.put("tag", XmlMetadataResolver.getTagMetadata(tag));

                List<String> contextTreeTagNames = point.getContextTreeTagNames();
                if (contextTreeTagNames != null && !contextTreeTagNames.isEmpty()) {
                    Map<String, Object> contextTreeTags = new HashMap<>();
                    collectTreeTagContext(tag.getParentTag(), contextTreeTagNames, contextTreeTags);
                    context.put("contextTreeTags", contextTreeTags);
                }

                XmlRelationContextJava contextJava = point.getContextJava();
                if (contextJava != null) {
                    Map<String, Object> contextJavaMap = new HashMap<>();
                    context.put("contextJava", contextJavaMap);

                    XmlRelationContextJavaXmlAttr javaClass = contextJava.getJavaClass();
                    AtomicReference<PsiClass> classRef = new AtomicReference<>();
                    matchJavaClass(tag, javaClass, classRef);

                    if (classRef.get() != null) {
                        Map<String, Object> metadata = JavaMetadataResolver.getClassMetadata(classRef.get());
                        contextJavaMap.put("javaClass", metadata);
                    }

                    XmlRelationContextJavaXmlAttr javaMethod = contextJava.getJavaMethod();
                    AtomicReference<PsiMethod> methodRef = new AtomicReference<>();
                    matchJavaMethod(tag, javaMethod, methodRef, classRef.get());

                    if (methodRef.get() != null) {
                        Map<String, Object> metadata = JavaMetadataResolver.getMethodMetadata(methodRef.get());
                        contextJavaMap.put("javaMethod", metadata);
                    }
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
                                (PsiLanguageInjectionHost) attributeValue,
                                new TextRange(0, attributeValue.getTextRange().getLength()))
                        .doneInjecting();
            }
        }
    }

    public static void matchJavaMethod(XmlTag tag, XmlRelationContextJavaXmlAttr relation, AtomicReference<PsiMethod> methodRef, PsiClass psiClass) {
        if (tag == null) {
            return;
        }
        if (relation == null) {
            return;
        }
        String tagNamePattern = relation.getTagName();
        if (StringUtils.isEmpty(tagNamePattern)) {
            return;
        }
        String attrNamePattern = relation.getAttrName();
        if (StringUtils.isEmpty(attrNamePattern)) {
            return;
        }
        String tagName = tag.getName();
        if (SimpleMatcher.INSTANCE.matches(tagName, tagNamePattern)) {
            XmlAttribute[] attributes = tag.getAttributes();
            if (attributes != null) {
                for (XmlAttribute attribute : attributes) {
                    String attrName = attribute.getName();
                    if (SimpleMatcher.INSTANCE.matches(attrName, attrNamePattern)) {
                        String value = attribute.getValue();
                        if (!StringUtils.isEmpty(value)) {
                            String methodName = value;
                            int idx = value.lastIndexOf(".");
                            if (idx >= 0) {
                                String className = value.substring(0, idx);
                                methodName = value.substring(idx + 1);
                                psiClass = JavaMetadataResolver.findClassByName(tag.getProject(), className);
                            }
                            if (psiClass != null) {
                                PsiMethod[] allMethods = psiClass.getAllMethods();
                                for (PsiMethod method : allMethods) {
                                    if (method.getName().equals(methodName)) {
                                        methodRef.set(method);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        matchJavaMethod(tag.getParentTag(), relation, methodRef, psiClass);
    }

    public static void matchJavaClass(XmlTag tag, XmlRelationContextJavaXmlAttr relation, AtomicReference<PsiClass> classRef) {
        if (tag == null) {
            return;
        }
        if (relation == null) {
            return;
        }
        String tagNamePattern = relation.getTagName();
        if (StringUtils.isEmpty(tagNamePattern)) {
            return;
        }
        String attrNamePattern = relation.getAttrName();
        if (StringUtils.isEmpty(attrNamePattern)) {
            return;
        }
        String tagName = tag.getName();
        if (SimpleMatcher.INSTANCE.matches(tagName, tagNamePattern)) {
            XmlAttribute[] attributes = tag.getAttributes();
            if (attributes != null) {
                for (XmlAttribute attribute : attributes) {
                    String attrName = attribute.getName();
                    if (SimpleMatcher.INSTANCE.matches(attrName, attrNamePattern)) {
                        String value = attribute.getValue();
                        if (!StringUtils.isEmpty(value)) {
                            PsiClass clazz = JavaMetadataResolver.findClassByName(tag.getProject(), value);
                            if (clazz != null) {
                                classRef.set(clazz);
                                return;
                            }
                        }
                    }
                }
            }
        }
        matchJavaClass(tag.getParentTag(), relation, classRef);
    }


    public static void collectTreeTagContext(XmlTag tag, List<String> tagNameList, Map<String, Object> context) {
        if (tag == null) {
            return;
        }
        String name = tag.getName();
        if (!context.containsKey(name)) {
            for (String namePattern : tagNameList) {
                if (SimpleMatcher.INSTANCE.matches(name, namePattern)) {
                    Map<String, Object> metadata = XmlMetadataResolver.getTagMetadata(tag);
                    context.put(name, metadata);
                    break;
                }
            }

        }
        collectTreeTagContext(tag.getParentTag(), tagNameList, context);
    }


    public static void validTreeTagNameList(XmlTag tag, List<String> tagNameList, int currIndex, boolean strict, AtomicBoolean matched) {
        if (tag == null) {
            // 没有更多父节点，但是还未匹配完，那就是不匹配
            if (currIndex < tagNameList.size()) {
                matched.set(false);
            } else {
                matched.set(true);
            }
            return;
        }
        String currentPattern = tagNameList.get(currIndex);
        String name = tag.getName();
        boolean ok = SimpleMatcher.INSTANCE.matches(name, currentPattern);
        if (!ok) {
            if (strict) {
                matched.set(false);
                return;
            }
            validTreeTagNameList(tag.getParentTag(), tagNameList, currIndex, strict, matched);
            return;
        }
        validTreeTagNameList(tag.getParentTag(), tagNameList, currIndex + 1, strict, matched);
    }
}
