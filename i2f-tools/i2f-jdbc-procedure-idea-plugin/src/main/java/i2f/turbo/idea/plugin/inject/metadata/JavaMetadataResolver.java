package i2f.turbo.idea.plugin.inject.metadata;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import i2f.lru.LruMap;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectItem;
import i2f.turbo.idea.plugin.inject.data.LanguageInjectJavaMetadata;
import i2f.turbo.idea.plugin.inject.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/4/17 14:00
 * @desc
 */
public class JavaMetadataResolver {

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


    protected static final LruMap<String,Map.Entry<Long,PsiClass>> CACHE_CLASS=new LruMap<>(300);

    public static PsiClass findClassByName(Project project, String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }
        String cacheKey=project.getProjectFilePath()+"#"+className;
        Map.Entry<Long, PsiClass> cacheEntry = CACHE_CLASS.get(cacheKey);
        if(cacheEntry!=null){
            if(cacheEntry.getKey()<System.currentTimeMillis()){
                return cacheEntry.getValue();
            }else{
                CACHE_CLASS.remove(cacheKey);
            }
        }
        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(project);
        JavaPsiFacade instance = JavaPsiFacade.getInstance(project);
        PsiClass clazz = instance.findClass(className, searchScope);
        if (clazz == null) {
            PsiShortNamesCache shortNamesCache = PsiShortNamesCache.getInstance(project);
            @NotNull PsiClass[] arr = shortNamesCache.getClassesByName(className, searchScope);
            if (arr != null && arr.length > 0) {
                clazz = arr[0];
            }
        }
        CACHE_CLASS.put(cacheKey,new AbstractMap.SimpleEntry<>(System.currentTimeMillis()+15*1000,clazz));
        return clazz;
    }


    public static void fillJavaMetadata(LanguageInjectItem item, Map<String, Object> context, Project project) {
        LanguageInjectJavaMetadata javaMetadata = item.getJavaMetadata();
        if(javaMetadata!=null){
            Map<String, Object> javaMetadataMap = new HashMap<>();
            context.put("javaMetadata", javaMetadataMap);

            PsiClass clazz=null;
            String className = javaMetadata.getClassName();
            if (!StringUtils.isEmpty(className)) {
                clazz = JavaMetadataResolver.findClassByName(project, className);
                if (clazz != null) {
                    Map<String, Object> metadata = JavaMetadataResolver.getClassMetadata(clazz);
                    javaMetadataMap.put("javaClass", metadata);
                }
            }

            String methodName = javaMetadata.getMethodName();
            if (!StringUtils.isEmpty(methodName)) {
                int idx = methodName.lastIndexOf(".");
                if (idx >= 0) {
                    className = methodName.substring(0, idx);
                    methodName = methodName.substring(idx + 1);
                    clazz = JavaMetadataResolver.findClassByName(project, className);
                }
                if (clazz != null) {
                    PsiMethod[] allMethods = clazz.getAllMethods();
                    for (PsiMethod method : allMethods) {
                        if (method.getName().equals(methodName)) {
                            Map<String, Object> metadata = JavaMetadataResolver.getMethodMetadata(method);
                            javaMetadataMap.put("javaMethod", metadata);
                        }
                    }
                }
            }
        }
    }
}
