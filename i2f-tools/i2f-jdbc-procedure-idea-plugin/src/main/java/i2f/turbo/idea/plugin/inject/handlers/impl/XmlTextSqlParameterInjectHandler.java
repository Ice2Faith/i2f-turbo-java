package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.sql.psi.SqlParameter;
import i2f.match.impl.SimpleMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJava;
import i2f.turbo.idea.plugin.inject.data.point.XmlRelationContextJavaXmlAttr;
import i2f.turbo.idea.plugin.inject.data.point.XmlTagBodyInjectPoint;
import i2f.turbo.idea.plugin.inject.data.point.XmlTextSqlParameterInjectPoint;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.metadata.JavaMetadataResolver;
import i2f.turbo.idea.plugin.inject.metadata.XmlMetadataResolver;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2026/4/17 14:14
 * @desc
 */
public class XmlTextSqlParameterInjectHandler extends IProjectInjectHandler<SqlParameter> {
    @Override
    public Class<SqlParameter> supportType() {
        return SqlParameter.class;
    }

    @Override
    protected void doInjectInner(MultiHostRegistrar registrar, SqlParameter sqlParameter) {

        XmlTag tag = CompletionHelper.getParentElement(sqlParameter, XmlTag.class);
        if (tag == null) {
            return;
        }

        List<LanguageInjectItem> configList = ProjectInjectConfig.getProjectInjectConfigForType(tag.getProject(), LanguageInjectItem.TYPE_TEXT_SQL_PARAMETER);

        if (configList == null || configList.isEmpty()) {
            return;
        }

        for (LanguageInjectItem item : configList) {
            List<XmlTextSqlParameterInjectPoint> points = item.getPointsOnType(XmlTextSqlParameterInjectPoint.class);
            for (XmlTextSqlParameterInjectPoint point : points) {
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
                JavaMetadataResolver.fillJavaMetadata(item, context, sqlParameter.getProject());

                context.put("tag", XmlMetadataResolver.getTagMetadata(tag));

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
                        Map<String, Object> metadata = JavaMetadataResolver.getClassMetadata(classRef.get());
                        contextJavaMap.put("javaClass", metadata);
                    }

                    XmlRelationContextJavaXmlAttr javaMethod = contextJava.getJavaMethod();
                    AtomicReference<PsiMethod> methodRef = new AtomicReference<>();
                    XmlAttrValueInjectHandler.matchJavaMethod(tag, javaMethod, methodRef, classRef.get());

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

                String text = sqlParameter.getText();
                int beginIndex = 0;
                int endIndex = sqlParameter.getTextRange().getLength();
                int idx = text.indexOf("{");
                if (idx >= 0) {
                    beginIndex = idx + 1;
                }
                idx = text.lastIndexOf("}");
                if (idx >= 0) {
                    endIndex = endIndex - (text.substring(idx).length());
                }
                registrar.startInjecting(targetLang)
                        .addPlace(prefix,
                                suffix,
                                (PsiLanguageInjectionHost) sqlParameter,
                                new TextRange(beginIndex, endIndex))
                        .doneInjecting();
                return;
            }
        }


    }

}
