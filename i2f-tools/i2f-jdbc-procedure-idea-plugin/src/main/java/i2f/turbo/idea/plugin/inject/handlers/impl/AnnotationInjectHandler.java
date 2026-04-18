package i2f.turbo.idea.plugin.inject.handlers.impl;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import i2f.match.impl.AntMatcher;
import i2f.turbo.idea.plugin.inject.config.ProjectInjectConfig;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectPlace;
import i2f.turbo.idea.plugin.inject.data.point.AnnotationInjectPoint;
import i2f.turbo.idea.plugin.inject.handlers.IProjectInjectHandler;
import i2f.turbo.idea.plugin.inject.metadata.JavaMetadataResolver;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/4/16 11:23
 * @desc
 */
public class AnnotationInjectHandler extends IProjectInjectHandler<PsiAnnotationParameterList> {
    @Override
    public Class<PsiAnnotationParameterList> supportType() {
        return PsiAnnotationParameterList.class;
    }

    @Override
    public void doInjectInner(MultiHostRegistrar registrar, PsiAnnotationParameterList annotationParameterList) {

        PsiElement parentElem = annotationParameterList.getParent();
        if (!(parentElem instanceof PsiAnnotation)) {
            return;
        }

        PsiAnnotation psiAnnotation = (PsiAnnotation) parentElem;

        PsiElement annotationElement = null;

        PsiElement modifierList = psiAnnotation.getParent();
        if (modifierList instanceof PsiModifierList) {
            annotationElement = modifierList.getParent();
        }

        PsiClass annotationType = psiAnnotation.resolveAnnotationType();
        if (annotationType == null) {
            return;
        }

        Project project = psiAnnotation.getProject();
        List<LanguageInjectItem> injectConfig = ProjectInjectConfig.getProjectInjectConfigForType(project, LanguageInjectItem.TYPE_ANNOTATION);
        if (injectConfig == null || injectConfig.isEmpty()) {
            return;
        }

        String annotationFullName = annotationType.getQualifiedName();

        PsiNameValuePair[] attributes = annotationParameterList.getAttributes();

        for (PsiNameValuePair attribute : attributes) {
            String name = attribute.getAttributeName();
            PsiAnnotationMemberValue attributeValue = attribute.getValue();
            if (attributeValue == null) {
                continue;
            }

            List<PsiLiteralExpression> ranges = new ArrayList<>();
            StringBuilder builder = new StringBuilder();
            if (attributeValue instanceof PsiLiteralExpression) {
                PsiLiteralExpression literalExpression = (PsiLiteralExpression) attributeValue;
//                Object value = literalExpression.getValue();
//                builder.append(value);
//                String value = attribute.getLiteralValue();
                ranges.add(literalExpression);
            } else if (attributeValue instanceof PsiPolyadicExpression) {
                PsiPolyadicExpression polyadicExpression = (PsiPolyadicExpression) attributeValue;
                PsiExpression[] operands = polyadicExpression.getOperands();
                for (PsiExpression operand : operands) {
                    if (operand instanceof PsiLiteralExpression) {
                        PsiLiteralExpression literalExpression = (PsiLiteralExpression) operand;
//                        Object value = literalExpression.getValue();
//                        builder.append(value);
                        ranges.add(literalExpression);
                    }
                }
            }

            if (ranges.isEmpty()) {
                continue;
            }

            String value = builder.toString();

            for (LanguageInjectItem item : injectConfig) {
                List<AnnotationInjectPoint> points = item.getPointsOnType(AnnotationInjectPoint.class);
                for (AnnotationInjectPoint point : points) {
                    if (!AntMatcher.PKG.matches(annotationFullName, point.getType())) {
                        continue;
                    }
                    if (!name.equals(point.getProp())) {
                        continue;
                    }


                    LanguageInjectPlace inject = item.getInject();
                    if (inject == null) {
                        continue;
                    }
                    if (StringUtils.isEmpty(inject.getLanguage())) {
                        continue;
                    }

                    Language targetLang = ProjectInjectConfig.matchLanguage(inject.getLanguage());
                    String prefix = inject.getPrefixTemplate();
                    String suffix = inject.getSuffixTemplate();

                    if (targetLang == null) {
                        continue;
                    }

                    Map<String, Object> context = new HashMap<>();
                    JavaMetadataResolver.fillJavaMetadata(item, context, annotationElement.getProject());

                    if (annotationElement instanceof PsiMethod) {
                        PsiMethod method = (PsiMethod) annotationElement;

                        Map<String, Object> metadata = JavaMetadataResolver.getMethodMetadata(method);
                        context.put("method", metadata);

                        PsiClass declareClass = method.getContainingClass();
                        if (declareClass != null) {
                            Map<String, Object> declareClassMetadata = JavaMetadataResolver.getClassMetadata(declareClass);
                            context.put("declareClass", declareClassMetadata);
                        }
                    } else if (annotationElement instanceof PsiField) {
                        PsiField field = (PsiField) annotationElement;

                        Map<String, Object> metadata = JavaMetadataResolver.getFieldMetadata(field);
                        context.put("field", metadata);

                        PsiClass declareClass = field.getContainingClass();
                        if (declareClass != null) {
                            Map<String, Object> declareClassMetadata = JavaMetadataResolver.getClassMetadata(declareClass);
                            context.put("declareClass", declareClassMetadata);
                        }
                    } else if (annotationElement instanceof PsiClass) {
                        PsiClass clazz = (PsiClass) annotationElement;

                        Map<String, Object> metadata = JavaMetadataResolver.getClassMetadata(clazz);
                        context.put("class", metadata);
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

                    for (PsiLiteralExpression range : ranges) {
                        registrar.startInjecting(targetLang)
                                .addPlace(prefix,
                                        suffix,
                                        (PsiLanguageInjectionHost) range,
                                        new TextRange(1, range.getTextRange().getLength() - 1))
                                .doneInjecting();
                    }
                }

            }

        }

    }

}
