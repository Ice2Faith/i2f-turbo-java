package i2f.turbo.idea.plugin.jdbc.procedure.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.Language;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import i2f.extension.antlr4.script.tiny.impl.context.TinyScriptFunctions;
import i2f.jdbc.procedure.context.ContextFunctions;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.node.impl.tinyscript.ExecContextMethodProvider;
import i2f.jdbc.procedure.node.impl.tinyscript.ExecutorMethodProvider;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import i2f.turbo.idea.plugin.jdbc.procedure.JdbcProcedureProjectMetaHolder;
import i2f.turbo.idea.plugin.jdbc.procedure.XProc4jConsts;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/10/24 10:18
 */
public class CompletionHelper {

    public static final Logger log = Logger.getInstance(CompletionHelper.class);

    public static final AtomicReference<Map<String, LookupElement>> lastFunctions = new AtomicReference<>();
    public static final AtomicLong lastUpdateFunctionsMillSeconds = new AtomicLong(0);

    public static Map<String, LookupElement> getXmlFileFunctionsFast(PsiElement position) {
        if (position == null) {
            return lastFunctions.get();
        }
        long cts = System.currentTimeMillis();
        if ((cts - lastUpdateFunctionsMillSeconds.get()) < 1200) {
            return lastFunctions.get();
        }

        lastUpdateFunctionsMillSeconds.set(cts);
        lastFunctions.updateAndGet((v) -> {
            Map<String, LookupElement> functions = getXmlFileFunctions(position);

            return functions;
        });

        return lastFunctions.get();
    }

    public static Map<String, LookupElement> getXmlFileFunctions(PsiElement position) {
        Map<String, LookupElement> ret = new LinkedHashMap<>();
        Project project = position.getProject();
        JavaPsiFacade instance = JavaPsiFacade.getInstance(project);
        GlobalSearchScope searchScope = null;
        Module module = ModuleUtilCore.findModuleForPsiElement(position);
        if (module != null) {
            searchScope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, true);
        } else {
            searchScope = GlobalSearchScope.everythingScope(project);
        }
        Set<String> processClassNameSet = new HashSet<>();
        String[] classNames = {
                "i2f.jdbc.procedure.context.ContextFunctions",
                "i2f.extension.antlr4.script.tiny.impl.context.TinyScriptFunctions",
                "i2f.jdbc.procedure.node.impl.tinyscript.ExecContextMethodProvider",
                "i2f.jdbc.procedure.node.impl.tinyscript.ExecutorMethodProvider"
        };
        for (String className : classNames) {
            PsiClass psiClass = instance.findClass(className, searchScope);
//            log.warn("any-help find-class: " + psiClass);
            if (psiClass != null) {
                String simpleName = className;
                if (simpleName.contains(".")) {
                    simpleName = simpleName.substring(simpleName.lastIndexOf(".") + 1);
                }
                processClassNameSet.add(simpleName);
                PsiMethod[] allMethods = psiClass.getAllMethods();
                if (allMethods != null) {
                    for (PsiMethod item : allMethods) {
                        String name = item.getName();
                        if (name.contains("$")) {
                            continue;
                        }
                        StringBuilder completionBuilder = new StringBuilder();
                        StringBuilder signatureBuilder = new StringBuilder();
                        PsiType returnType = item.getReturnType();
                        String returnText = returnType == null ? "?" : returnType.getPresentableText();
                        signatureBuilder.append(returnText).append(" ").append(name).append("(");
                        completionBuilder.append(name).append("(");
                        PsiParameterList parameterList = item.getParameterList();
                        PsiParameter[] parameters = parameterList.getParameters();
                        boolean isFirst = true;
                        for (PsiParameter parameter : parameters) {
                            PsiType parameterType = parameter.getType();
                            String paramText = parameterType == null ? "?" : parameterType.getPresentableText();
                            if (!isFirst) {
                                signatureBuilder.append(", ");
                                completionBuilder.append(", ");
                            }
                            signatureBuilder.append(paramText);
                            completionBuilder.append("");
                            isFirst = false;
                        }
                        signatureBuilder.append(")");
                        String signature = signatureBuilder.toString();
                        LookupElement elem = LookupElementBuilder.create(completionBuilder.toString())
                                .withTypeText("Functions")
                                .withPresentableText(signature)
                                .withTailText(simpleName)
                                .withIcon(XProc4jConsts.ICON)
                                .withItemTextItalic(true);
                        ret.put(signature, elem);
                    }
                }
            }
        }
        Class[] classes = {
                ContextFunctions.class,
                TinyScriptFunctions.class,
                ExecContextMethodProvider.class,
                ExecutorMethodProvider.class,
        };
        for (Class clazz : classes) {
            String simpleName = clazz.getSimpleName();
            if (processClassNameSet.contains(simpleName)) {
                continue;
            }
            Set<Method> list = new LinkedHashSet<>();
            if (clazz != null) {
                Method[] methods = clazz.getDeclaredMethods();
                if (methods != null) {
                    list.addAll(Arrays.asList(methods));
                }
            }
            if (clazz != null) {
                Method[] methods = clazz.getMethods();
                if (methods != null) {
                    list.addAll(Arrays.asList(methods));
                }
            }
            for (Method item : list) {
                String name = item.getName();
                if (name.contains("$")) {
                    continue;
                }
                StringBuilder completionBuilder = new StringBuilder();
                StringBuilder signatureBuilder = new StringBuilder();
                Class<?> returnType = item.getReturnType();
                String returnText = returnType == null ? "?" : returnType.getSimpleName();
                signatureBuilder.append(returnText).append(" ").append(name).append("(");
                completionBuilder.append(name).append("(");
                Parameter[] parameters = item.getParameters();
                boolean isFirst = true;
                for (Parameter parameter : parameters) {
                    Class<?> parameterType = parameter.getType();
                    String paramText = parameterType == null ? "?" : parameterType.getSimpleName();
                    if (!isFirst) {
                        signatureBuilder.append(", ");
                        completionBuilder.append(", ");
                    }
                    signatureBuilder.append(paramText);
                    completionBuilder.append("");
                    isFirst = false;
                }
                signatureBuilder.append(")");
                completionBuilder.append(")");
                String signature = signatureBuilder.toString();
                LookupElement elem = LookupElementBuilder.create(completionBuilder.toString())
                        .withTypeText("Functions")
                        .withPresentableText(signature)
                        .withTailText(clazz.getSimpleName())
                        .withIcon(XProc4jConsts.ICON)
                        .withItemTextItalic(true);
                ret.put(signature, elem);
            }
        }

        for (Map.Entry<String, ProcedureMeta> entry : JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.entrySet()) {
            ProcedureMeta meta = entry.getValue();
            StringBuilder completionBuilder = new StringBuilder();
            StringBuilder signatureBuilder = new StringBuilder();
            signatureBuilder.append(meta.getName()).append("(");
            completionBuilder.append(meta.getName()).append("(");
            List<String> parameters = meta.getArguments();
            boolean isFirst = true;
            for (String parameter : parameters) {
                if (!isFirst) {
                    signatureBuilder.append(", ");
                    completionBuilder.append(", ");
                }
                signatureBuilder.append(parameter);
                completionBuilder.append(parameter).append(": ");
                isFirst = false;
            }
            signatureBuilder.append(")");
            completionBuilder.append(")");
            String signature = signatureBuilder.toString();
            LookupElement elem = LookupElementBuilder.create(completionBuilder.toString())
                    .withTypeText("Procedures")
                    .withPresentableText(signature)
                    .withIcon(XProc4jConsts.ICON)
                    .withItemTextItalic(true);
            ret.put(signature, elem);
        }

        return ret;
    }


    public static final AtomicLong lastUpdateMillSeconds = new AtomicLong(0);
    public static final AtomicReference<Set<String>> lastVariables = new AtomicReference<>();
    public static final AtomicReference<Set<String>> lastSqlIdentifiers = new AtomicReference<>();

    public static Set<String> getXmlFileSqlIdentifiersFast(PsiElement position) {
        getXmlFileVariablesFast(position);
        return lastSqlIdentifiers.get();
    }

    public static Set<String> getXmlFileVariablesFast(PsiElement position) {
        if (position == null) {
            return lastVariables.get();
        }
        long cts = System.currentTimeMillis();
        if ((cts - lastUpdateMillSeconds.get()) < 1500) {
            return lastVariables.get();
        }
        lastUpdateMillSeconds.set(cts);
        lastVariables.updateAndGet((v) -> {
            Set<String> variables = new LinkedHashSet<>();
            Set<String> sqlIdentifiers = new LinkedHashSet<>();
            PsiElement root = getRootElement(position, XmlTag.class);
            if (root == null) {
                root = position.getContainingFile();
            }
            getXmlFileVariables(root, position, variables, sqlIdentifiers);
            lastSqlIdentifiers.set(sqlIdentifiers);
            return variables;
        });

        return lastVariables.get();
    }

    public static void getXmlFileVariables(PsiElement elem, PsiElement stopElem, Set<String> variables, Set<String> sqlIdentifiers) {
        if (elem == null) {
            return;
        }
        if (elem == stopElem) {
            return;
        }
        if (elem instanceof XmlAttribute) {
            XmlAttribute attribute = (XmlAttribute) elem;
            String name = attribute.getName();
            // result 出来的变量
            if (Arrays.asList("result", "item").contains(name)
                    || (name != null && name.contains("result."))) {
                String value = attribute.getValue();
                if (value != null && value.matches("[a-zA-Z0-9\\-_\\$\\.]+")) {
                    variables.add(value.trim());
                }
            } else {
                String value = attribute.getValue();
                if (value != null) {
                    getDolarVaraibles(value, variables);
                }
            }
        } else if (elem instanceof XmlTag) {
            XmlTag tag = (XmlTag) elem;
            String name = tag.getName();
            // 过程声明的入参
            if (Arrays.asList("procedure", "script-segment").contains(name)) {
                XmlAttribute[] attributes = tag.getAttributes();
                if (attributes != null) {
                    for (XmlAttribute item : attributes) {
                        String attrName = item.getName();
                        if (attrName == null) {
                            continue;
                        }
                        int idx = attrName.indexOf(".");
                        if (idx >= 0) {
                            attrName = attrName.substring(0, idx);
                        }
                        if (attrName.isEmpty()) {
                            continue;
                        }
                        if (!Arrays.asList("return", "refid", "id", "param-share").contains(attrName)) {
                            if (attrName.matches("[a-zA-Z0-9\\-_\\$\\.]+")) {
                                variables.add(attrName.trim());
                            }
                        }
                    }
                }
            }
            try {
                XmlAttribute[] attributes = tag.getAttributes();
                if (attributes != null) {
                    for (XmlAttribute item : attributes) {
                        getXmlFileVariables(item, stopElem, variables, sqlIdentifiers);
                    }
                }
            } catch (Exception e) {

            }

            // 内部的占位符变量
            String text = tag.getText();
            getDolarVaraibles(text, variables);

            // 处理TS的赋值语句
            if (Arrays.asList("lang-eval-ts", "lang-eval-tinyscript").contains(name)) {
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "[a-zA-Z0-9\\-_\\$\\.]+\\s*=");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    str = str.substring(0, str.length() - 1);
                    variables.add(str.trim());
                }
            }

            // 处理groovy的赋值语句
            if (Arrays.asList("lang-eval-groovy").contains(name)) {
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "params\\.[a-zA-Z0-9\\-_\\$\\.]+\\s*=");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    str = str.substring("params.".length(), str.length() - 1);
                    variables.add(str.trim());
                }
            }

            // 处理java的赋值语句
            if (Arrays.asList("lang-eval-java", "lang-java-body").contains(name)) {
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "params\\.put\\(\"[a-zA-Z0-9\\-_\\$\\.]+\"\\s*,\\)");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    int idx = str.indexOf("\"");
                    int eidx = str.lastIndexOf("\"");
                    str = str.substring(idx + 1, eidx);
                    variables.add(str.trim());
                }
            }

            // 处理SQL节点
            boolean isSqlTag = false;
            if (!isSqlTag) {
                String langAttrValue = tag.getAttributeValue("_lang");
                if (langAttrValue != null) {
                    langAttrValue = langAttrValue.trim().toLowerCase();
                    if (Arrays.asList("sql", "sql92", "sql99",
                            "mysql", "oracle", "postgre",
                            "mssql", "mariadb", "sqlserver",
                            "sqlite", "sqlite3", "h2").contains(langAttrValue)) {
                        isSqlTag = true;
                    }
                }
            }
            if (!isSqlTag) {
                if (name.startsWith("sql-") || name.endsWith("-sql")) {
                    isSqlTag = true;
                }
            }

            if (isSqlTag) {
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "[a-zA-Z0-9_]+");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    sqlIdentifiers.add(str);
                }
            }

            // 子元素
            try {
                PsiElement[] children = tag.getChildren();
                if (children != null) {
                    for (PsiElement item : children) {
                        getXmlFileVariables(item, stopElem, variables, sqlIdentifiers);
                    }
                }
            } catch (Exception e) {

            }
        }


    }

    public static void getDolarVaraibles(String text, Set<String> variables) {
        if (text == null || "".equals(text)) {
            return;
        }
        List<RegexMatchItem> list = RegexUtil.regexFinds(text, "[\\$#](\\!)?\\{[a-zA-Z0-9\\-_\\$\\.]+\\}");
        for (RegexMatchItem item : list) {
            String str = item.matchStr;
            int idx = str.indexOf("{");
            str = str.substring(idx + 1, str.length() - 1);
            variables.add(str.trim());
        }
    }


    public static Set<CompletionScope> completionTypes(PsiElement elem) {
        Set<CompletionScope> result = new HashSet<>();
        completionTypesNext(elem, result, 0);
        return result;
    }

    protected static void completionTypesNext(PsiElement elem, Set<CompletionScope> result, int level) {
        if (elem == null) {
            return;
        }
//        log.warn("any-help type-next:" + elem.getClass());
        if (level >= 10) {
            PsiFile psiFile = elem.getContainingFile();
            completionTypesByPsiFile(psiFile, result);
            return;
        }
        if (elem instanceof XmlAttributeValue) {
//            log.warn("any-help xml-attr-value:" + elem.getClass());
            XmlAttributeValue attrValue = (XmlAttributeValue) elem;
            PsiElement parent = attrValue.getParent();
//            log.warn("any-help xml-attr-value-parent:" + parent.getClass());
            if (parent != null) {
                if (parent instanceof XmlAttribute) {
                    XmlAttribute attr = (XmlAttribute) parent;
                    String name = attr.getName();
//                    log.warn("any-help xml-attr-name:" + name);
                    if (name == null) {
                        name = "";
                    }
                    String[] arr = name.split("[.:;]");
//                    log.warn("any-help xml-attr-name-split:" + Arrays.toString(arr));
                    if (arr.length >= 2) {
                        if (Arrays.asList("eval-ts", "eval-tinyscript").contains(arr[1])) {
                            result.add(CompletionScope.FUNCTIONS);
                            result.add(CompletionScope.SQL_IDENTIFIER);
                            result.add(CompletionScope.VARIABLES);
                            result.add(CompletionScope.TINY_SCRIPT);
                            result.add(CompletionScope.SQL);
                            return;
                        }
                    }
                    if (name.equals("method")) {
                        result.add(CompletionScope.FUNCTIONS);
                        return;
                    }
                }
            }
        }
        if (elem instanceof XmlTag) {
            XmlTag tag = (XmlTag) elem;
            String name = tag.getName();
            if (name == null) {
                name = "";
            }
//            log.warn("any-help type-next-tag:" + name);
            String lang = tag.getAttributeValue("_lang");
            if (lang != null) {
                lang = lang.trim().toLowerCase();
                if (Arrays.asList("sql", "sql92", "sql99",
                        "mysql", "oracle", "postgre",
                        "mssql", "mariadb", "sqlserver",
                        "sqlite", "sqlite3", "h2").contains(lang)) {
                    result.add(CompletionScope.SQL_IDENTIFIER);
                    result.add(CompletionScope.VARIABLES);
                    result.add(CompletionScope.SQL);
                    return;
                }
            }

            if (name.startsWith("sql-")
                    || name.endsWith("-sql")) {
                result.add(CompletionScope.SQL_IDENTIFIER);
                result.add(CompletionScope.VARIABLES);
                result.add(CompletionScope.SQL);
                return;
            }

            if (name.contains("eval-ts")
                    || name.contains("eval-tinyscript")) {
                result.add(CompletionScope.FUNCTIONS);
                result.add(CompletionScope.SQL_IDENTIFIER);
                result.add(CompletionScope.VARIABLES);
                result.add(CompletionScope.TINY_SCRIPT);
                result.add(CompletionScope.SQL);
                return;
            }
        }

        if (elem instanceof XmlElement) {
            result.add(CompletionScope.VARIABLES);
        }
        if (elem instanceof PsiFile) {
            PsiFile psiFile = (PsiFile) elem;
            completionTypesByPsiFile(psiFile, result);
        }
        PsiElement parent = elem.getParent();
        if (parent != null) {
            completionTypesNext(parent, result, level + 1);
        } else {
            PsiElement context = elem.getContext();
            if (context != null) {
//                log.warn("any-help elem-context:" + context.getClass());
                completionTypesNext(context, result, level + 1);
            }
        }


    }

    public static void completionTypesByPsiFile(PsiFile psiFile, Set<CompletionScope> result) {
        if (psiFile == null) {
            return;
        }
        Language language = psiFile.getLanguage();
        if (language != null) {
            if ("xml".equalsIgnoreCase(language.getID())) {
                result.add(CompletionScope.VARIABLES);
            } else if ("sql".equalsIgnoreCase(language.getID())) {
                result.add(CompletionScope.SQL);
                result.add(CompletionScope.SQL_IDENTIFIER);
            }
        }
        VirtualFile virtualFile = psiFile.getVirtualFile();
        if (virtualFile != null) {
            String extension = virtualFile.getExtension();
            if ("xml".equalsIgnoreCase(extension)
                    || ".xml".equalsIgnoreCase(extension)) {
                result.add(CompletionScope.VARIABLES);
            } else if ("sql".equalsIgnoreCase(extension)
                    || ".sql".equalsIgnoreCase(extension)) {
                result.add(CompletionScope.SQL);
                result.add(CompletionScope.SQL_IDENTIFIER);
            }
        }
    }


    public static <T extends PsiElement> T getRootElement(PsiElement element, Class<T> searchType) {
        AtomicReference<T> ref = new AtomicReference<>();
        getRootElementNext(element, searchType, ref);
        return ref.get();
    }

    protected static <T extends PsiElement> void getRootElementNext(PsiElement element, Class<T> searchType, AtomicReference<T> result) {
        if (element == null) {
            return;
        }
        if (searchType != null) {
            if (searchType.isAssignableFrom(element.getClass())) {
                result.set((T) element);
            }
        } else {
            result.set((T) element);
        }
        PsiElement parent = element.getParent();
        if (parent != null) {
            getRootElementNext(parent, searchType, result);
        } else {
            PsiElement context = element.getContext();
            getRootElementNext(context, searchType, result);
        }
    }
}
