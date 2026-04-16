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
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import i2f.turbo.idea.plugin.inject.velocity.VelocityGenerator;

import java.util.*;

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
//        log.warn("project-language-inject: inject 3"+(parentElem==null?parentElem:parentElem.getClass()));
        if (!(parentElem instanceof PsiAnnotation)) {
            return;
        }

        PsiAnnotation psiAnnotation = (PsiAnnotation) parentElem;

        PsiElement annotationElement = null;

        PsiElement modifierList = psiAnnotation.getParent();
//        log.warn("project-language-inject: inject 4"+(modifierList==null?modifierList:modifierList.getClass()));
        if (modifierList instanceof PsiModifierList) {
            annotationElement = modifierList.getParent();
        }

        PsiClass annotationType = psiAnnotation.resolveAnnotationType();
//        log.warn("project-language-inject: inject 5"+(annotationType==null?annotationType:annotationType.getClass()));
        if (annotationType == null) {
            return;
        }

        Project project = psiAnnotation.getProject();
        List<LanguageInjectItem> injectConfig = ProjectInjectConfig.getProjectInjectConfigForType(project, LanguageInjectItem.TYPE_ANNOTATION);
//        log.warn("project-language-inject: inject 6"+(injectConfig==null?injectConfig:injectConfig.size()));
        if (injectConfig == null || injectConfig.isEmpty()) {
            return;
        }

        String annotationFullName = annotationType.getQualifiedName();

        PsiNameValuePair[] attributes = annotationParameterList.getAttributes();

        for (PsiNameValuePair attribute : attributes) {
            String name = attribute.getAttributeName();
//            log.warn("project-language-inject: inject l1 "+(name==null?name:name));
            PsiAnnotationMemberValue attributeValue = attribute.getValue();
//            log.warn("project-language-inject: inject l2 "+(attributeValue==null?attributeValue:attributeValue.getClass()));
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

            Map<String, Object> context = new HashMap<>();

            if (annotationElement instanceof PsiMethod) {
                PsiMethod method = (PsiMethod) annotationElement;

                Map<String, Object> metadata = getMethodMetadata(method);
                context.put("method", metadata);

                PsiClass declareClass = method.getContainingClass();
                if (declareClass != null) {
                    Map<String, Object> declareClassMetadata = getClassMetadata(declareClass);
                    context.put("declareClass", declareClassMetadata);
                }
            } else if (annotationElement instanceof PsiField) {
                PsiField field = (PsiField) annotationElement;

                Map<String, Object> metadata = getFieldMetadata(field);
                context.put("field", metadata);

                PsiClass declareClass = field.getContainingClass();
                if (declareClass != null) {
                    Map<String, Object> declareClassMetadata = getClassMetadata(declareClass);
                    context.put("declareClass", declareClassMetadata);
                }
            } else if (annotationElement instanceof PsiClass) {
                PsiClass clazz = (PsiClass) annotationElement;

                Map<String, Object> metadata = getClassMetadata(clazz);
                context.put("class", metadata);
            }

            String value = builder.toString();
//            log.warn("project-language-inject: inject l3 "+(value==null?value:value));

            LanguageInjectItem matchItem = null;
            for (LanguageInjectItem item : injectConfig) {
                List<AnnotationInjectPoint> points = item.getPointsOnType(AnnotationInjectPoint.class);
                for (AnnotationInjectPoint point : points) {
                    if (AntMatcher.PKG.matches(annotationFullName, point.getType())
                            && name.equals(point.getProp())
                    ) {
                        matchItem = item;
                        break;
                    }
                }

                if (matchItem != null) {
                    break;
                }
            }

//            log.warn("project-language-inject: inject l4 "+(matchItem==null?matchItem:matchItem));
            if (matchItem == null) {
                continue;
            }

            LanguageInjectPlace inject = matchItem.getInject();
//            log.warn("project-language-inject: inject l5 "+(inject==null?inject:inject));
            if (inject == null) {
                continue;
            }
            if (StringUtils.isEmpty(inject.getLanguage())) {
                continue;
            }

            Language targetLang = ProjectInjectConfig.matchLanguage(inject.getLanguage());
            String prefix = inject.getPrefixTemplate();
            String suffix = inject.getSuffixTemplate();

//            log.warn("project-language-inject: inject l6 "+(targetLang==null?targetLang:targetLang.getID()));
            if (targetLang == null) {
                continue;
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

    public static Map<String, Object> getClassMetadata(PsiClass clazz) {
        Map<String, Object> ret = new HashMap<>();
        String fullTypeName = clazz.getQualifiedName();
        String displayTypeName = clazz.getName();

        ret.put("fullType", fullTypeName);
        ret.put("simpleType", displayTypeName);

        return ret;
    }

    public static Map<String, Object> getFieldMetadata(PsiField field) {
        Map<String, Object> ret = new HashMap<>();
        PsiType fieldType = field.getType();
        String fieldFullTypeName = fieldType.getCanonicalText();
        String fieldDisplayTypeName = fieldType.getPresentableText();

        ret.put("name", field.getName());
        ret.put("fullType", fieldFullTypeName);
        ret.put("simpleType", fieldDisplayTypeName);


        return ret;
    }

    public static Map<String, Object> getMethodMetadata(PsiMethod method) {
        Map<String, Object> ret = new HashMap<>();
        ret.put("name", method.getName());

        List<Map<String, Object>> parametersCtx = new ArrayList<>();
        ret.put("parameters", parametersCtx);

        if (method.hasParameters()) {
            PsiParameterList parameterList = method.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                PsiParameter parameter = parameters[i];
                String parameterName = parameter.getName();
                String parameterFullTypeName = parameter.getType().getCanonicalText();
                String parameterDisplayTypeName = parameter.getType().getPresentableText();
                Map<String, Object> parameterMap = new LinkedHashMap<>();
                parameterMap.put("name", parameterName);
                parameterMap.put("fullType", parameterFullTypeName);
                parameterMap.put("simpleType", parameterDisplayTypeName);
                parameterMap.put("index", i);
                parametersCtx.add(parameterMap);
            }
        }

        PsiType returnType = method.getReturnType();
        if (returnType != null) {
            String returnFullTypeName = returnType.getCanonicalText();
            String returnDisplayTypeName = returnType.getPresentableText();

            Map<String, Object> returnMap = new LinkedHashMap<>();
            returnMap.put("fullType", returnFullTypeName);
            returnMap.put("simpleType", returnDisplayTypeName);

            ret.put("return", returnMap);
        }

        return ret;
    }
}
