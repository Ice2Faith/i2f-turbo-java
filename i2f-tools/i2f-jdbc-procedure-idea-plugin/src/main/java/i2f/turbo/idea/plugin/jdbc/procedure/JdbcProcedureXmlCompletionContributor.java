package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.lru.LruMap;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexFindPartMeta;
import i2f.match.regex.data.RegexMatchItem;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptConsts;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.sql.JDBCType;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Ice2Faith
 * @date 2025/3/13 22:56
 * @desc
 */
public class JdbcProcedureXmlCompletionContributor extends CompletionContributor {
    public static final Logger log = Logger.getInstance(JdbcProcedureXmlCompletionContributor.class);

    {
        JdbcProcedureProjectMetaHolder.refreshThreadTask();
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
//        log.warn("xml-attr completion override");
        completion(parameters, result);
    }

    public void completion(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
//        log.warn("xml-attr completion trigger:" + parameters);
        Project project = parameters.getOriginalFile().getProject();
        PsiElement position = parameters.getPosition();
//        log.warn("xml-attr completion-0:" + position.getClass());

        ASTNode node = position.getNode();
        if (node == null) {
            return;
        }
//        log.warn("xml-attr completion-1:" + position.getClass());

        IElementType type = node.getElementType();
        if (type == XmlTokenType.XML_NAME) {


//        log.warn("xml-attr completion-2:" + position.getClass() + "," + type.getClass() + ":" + type);

            String nodeText = node.getText();
//        log.warn("xml-attr node-text:" + nodeText);

            PsiElement parent = position.getParent();
            if (parent instanceof XmlTag) {

            } else if (parent instanceof XmlAttribute) {
//                log.warn("xml-attr completion:" + position.getClass() + "," + type.getClass() + ":" + type);

                XmlAttribute attribute = (XmlAttribute) parent;
                String name = attribute.getName();
                if (name != null) {
                    if (name.endsWith("IntellijIdeaRulezzz")) {
                        name = name.substring(0, name.length() - "IntellijIdeaRulezzz".length());
                    }
                    int idx = name.lastIndexOf("IntellijIdeaRule");
                    if (idx >= 0) {
                        name = name.substring(0, idx);
                    }
                }
//                log.warn("xml-attr completion tag-attr:" + name);
                if (name != null && name.endsWith(".")) {
                    List<String> completions = JdbcProcedureProjectMetaHolder.FEATURES;
                    if(name.startsWith("type")
                    ||name.startsWith("rollback-for")
                    ||name.startsWith("no-rollback-for")){
                        completions=new ArrayList<>();
                        completions.addAll(JdbcProcedureProjectMetaHolder.FEATURES_CAUSE);
                        completions.addAll(JdbcProcedureProjectMetaHolder.FEATURES);
                    }
                    if (completions != null) {
                        for (String attr : completions) {
                            result.addElement(LookupElementBuilder.create(name + attr).withIcon(XProc4jConsts.ICON));
                        }
                        return;
                    }
                } else {
                    XmlTag parentTag = attribute.getParent();
                    if (parentTag != null) {
                        String tagName = parentTag.getName();
//                        log.warn("xml-attr completion tag-name:" + tagName);
                        List<String> completions = new ArrayList<>();
                        StringBuilder completionAllArgs=new StringBuilder();
                        if (Arrays.asList("procedure-call", "function-call","script-include").contains(tagName)) {
//                            log.warn("xml-attr completion call-node tag-name:" + tagName);

                            XmlAttribute refidAttr = parentTag.getAttribute("refid");
//                            log.warn("xml-attr completion call-node refid-attribute:" + refidAttr);

                            if (refidAttr != null) {
                                String refid = refidAttr.getValue();
//                                log.warn("xml-attr completion call-node refid-attribute-id:" + refid);
                                ProcedureMeta meta = JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.get(refid);
//                                log.warn("xml-attr completion call-node refid-attribute-meta:" + meta);
                                if (meta != null) {
                                    List<String> arguments = meta.getArguments();
                                    for (String argument : arguments) {
                                        completions.add(argument);
                                        List<String> features = meta.getArgumentFeatures().get(argument);
                                        if (features != null && !features.isEmpty()) {
                                            completions.add(argument + "." + String.join(".", features));
                                            completionAllArgs.append(argument + "." + String.join(".", features)+"=\"\"").append("\n");
                                        }else{
                                            completionAllArgs.append(argument+"=\"\"").append("\n");
                                        }
                                    }
                                }
                            }
                        }

                        if (completions.isEmpty()) {
                            completions.addAll(Arrays.asList(
                                    "_lang", "id", "refid", "__file", "__line",
                                    "return", "result", "value"
                            ));
                        }

                        if(completionAllArgs.length()>0){
                            String completionItem = completionAllArgs.toString();
                            result.addElement(LookupElementBuilder.create(completionItem).withIcon(XProc4jConsts.ICON).withItemTextItalic(true));
                        }
                        if (completions != null) {
                            for (String attr : completions) {
                                result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                            }
                            return;
                        }
                    }
                }
            }

        } else if (type == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN) {
//            log.warn("xml-attr-value completion:" + position.getClass() + "," + type.getClass() + ":" + type);

            XmlAttribute attribute = PsiTreeUtil.getParentOfType(position, XmlAttribute.class, false);
            if (attribute == null) {
                return;
            }
//            log.warn("xml-attr-value completion attribute:" + attribute.getClass() + "," + attribute + ":" + attribute.getText() + "==>" + attribute.getName());

            XmlTag xmlTag = PsiTreeUtil.getParentOfType(attribute, XmlTag.class, false);

            String attributeName = attribute.getName();

            if (Arrays.asList("refid").contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                if (Arrays.asList("procedure-call", "function-call","script-include").contains(tagName)) {
                    List<String> completions = new ArrayList<>();
                    for (Map.Entry<String, ProcedureMeta> entry : JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.entrySet()) {
                        ProcedureMeta value = entry.getValue();
                        completions.add(value.getName());
                    }
                    if (completions != null) {
                        for (String attr : completions) {
                            result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                        }
                        return;
                    }
                }


            } else if (Arrays.asList("propagation").contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "REQUIRED",
                        "SUPPORTS",
                        "MANDATORY",
                        "REQUIRES_NEW",
                        "NOT_SUPPORTED",
                        "NEVER",
                        "NESTED"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList("isolation").contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "TRANSACTION_NONE",
                        "TRANSACTION_READ_UNCOMMITTED",
                        "TRANSACTION_READ_COMMITTED",
                        "TRANSACTION_REPEATABLE_READ",
                        "TRANSACTION_SERIALIZABLE"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "read-only",
                    "await",
                    "params_share",
                    "limited",
                    "accept-batch",
                    "before-truncate"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "true",
                        "false"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "time-unit"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "NANOSECONDS",
                        "MICROSECONDS",
                        "MILLISECONDS",
                        "SECONDS",
                        "MINUTES",
                        "HOURS",
                        "DAYS"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "jdbc-type"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                JDBCType[] arr = JDBCType.class.getEnumConstants();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.stream(arr).map(e->e.name()).collect(Collectors.toList()));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "pattern"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "yyyy-MM-dd HH:mm:ss.SSS",
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd HH:mm",
                        "yyyy-MM-dd",
                        "yyyy-MM",
                        "yyyy",
                        "HH:mm:ss",
                        "yyyyMMdd",
                        "yyyyMM"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "method"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "replace",
                        "replaceAll",
                        "contains",
                        "startsWith",
                        "endsWith",
                        "indexOf",
                        "lastIndexOf",
                        "substring",
                        "parseInteger",
                        "parseDouble",
                        "parseLong",
                        "parseFloat",
                        "parseDouble",
                        "add",
                        "containsKey",
                        "remove",
                        "get",
                        "set",
                        "Date.new",
                        "String.valueOf",
                        "size",
                        "length"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "database", "databases"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "mysql",
                        "oracle",
                        "mariadb",
                        "gbase",
                        "dm",
                        "postgre"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "datasource", "datasources"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "primary",
                        "master",
                        "main",
                        "slave"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            } else if (Arrays.asList(
                    "result-type"
            ).contains(attributeName)) {
                if (xmlTag == null) {
                    return;
                }
                String tagName = xmlTag.getName();
                List<String> completions = new ArrayList<>();
                completions.addAll(Arrays.asList(
                        "int",
                        "long",
                        "string",
                        "boolean",
                        "double",
                        "float"
                ));
                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            }else{
                XmlTag root = PsiTreeUtil.getTopmostParentOfType(position, XmlTag.class);
                if (root == null) {
                    return;
                }
                // 控制遍历频率，避免CPU过高
                Set<String> completions = lastVariables.updateAndGet((v) -> {
                    Set<String> ret = new LinkedHashSet<>();
                    long cts = System.currentTimeMillis();
                    if((cts-lastUpdateMillSeconds.get())<500){
                        return v;
                    }
                    getXmlFileVariables(root,position,ret);
                    lastUpdateMillSeconds.set(cts);
                    return ret;
                });

                if (completions != null) {
                    for (String attr : completions) {
                        result.addElement(LookupElementBuilder.create(attr).withIcon(XProc4jConsts.ICON));
                    }
                    return;
                }

            }
        }
    }

    public static final AtomicLong lastUpdateMillSeconds=new AtomicLong(0);
    public static final AtomicReference<Set<String>> lastVariables=new AtomicReference<>();

    public static void getXmlFileVariables(PsiElement elem,PsiElement stopElem, Set<String> variables){
        if(elem==null){
            return;
        }
        if(elem==stopElem){
            return;
        }
        if(elem instanceof XmlAttribute){
            XmlAttribute attribute = (XmlAttribute) elem;
            String name = attribute.getName();
            // result 出来的变量
            if("result".equals(name)){
                String value = attribute.getValue();
                if(value!=null && value.matches("[a-zA-Z0-9\\-_\\$\\.]+")){
                    variables.add(value.trim());
                }
            }else{
                String value = attribute.getValue();
                if(value!=null){
                    getDolarVaraibles(value,variables);
                }
            }
        }else if(elem instanceof XmlTag){
            XmlTag tag = (XmlTag) elem;
            String name = tag.getName();
            // 过程声明的入参
            if(Arrays.asList("procedure","script-segment").contains(name)){
                XmlAttribute[] attributes = tag.getAttributes();
                if(attributes!=null){
                    for (XmlAttribute item : attributes) {
                        String attrName = item.getName();
                        if(attrName==null){
                            continue;
                        }
                        int idx=attrName.indexOf(".");
                        if(idx>=0){
                            attrName=attrName.substring(0,idx);
                        }
                        if(attrName.isEmpty()){
                            continue;
                        }
                        if(!Arrays.asList("return","refid","id","param-share").contains(attrName)){
                            if(attrName.matches("[a-zA-Z0-9\\-_\\$\\.]+")){
                                variables.add(attrName.trim());
                            }
                        }
                    }
                }
            }
            try {
                XmlAttribute[] attributes = tag.getAttributes();
                if(attributes!=null){
                    for (XmlAttribute item : attributes) {
                        getXmlFileVariables(item,stopElem,variables);
                    }
                }
            } catch (Exception e) {

            }

            // 内部的占位符变量
            String text = tag.getText();
            getDolarVaraibles(text,variables);

            // 处理TS的赋值语句
            if(Arrays.asList("lang-eval-ts","lang-eval-tinyscript").contains(name)){
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "[a-zA-Z0-9\\-_\\$\\.]+\\s*=");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    str=str.substring(0,str.length()-1);
                    variables.add(str.trim());
                }
            }

            // 处理groovy的赋值语句
            if(Arrays.asList("lang-eval-groovy").contains(name)){
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "params\\.[a-zA-Z0-9\\-_\\$\\.]+\\s*=");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    str=str.substring("params.".length(),str.length()-1);
                    variables.add(str.trim());
                }
            }

            // 处理java的赋值语句
            if(Arrays.asList("lang-eval-java","lang-java-body").contains(name)){
                List<RegexMatchItem> list = RegexUtil.regexFinds(text, "params\\.put\\(\"[a-zA-Z0-9\\-_\\$\\.]+\"\\s*,\\)");
                for (RegexMatchItem item : list) {
                    String str = item.matchStr;
                    int idx=str.indexOf("\"");
                    int eidx=str.lastIndexOf("\"");
                    str=str.substring(idx+1,eidx);
                    variables.add(str.trim());
                }
            }

            // 子元素
            try {
                PsiElement[] children = tag.getChildren();
                if(children!=null){
                    for (PsiElement item : children) {
                        getXmlFileVariables(item,stopElem,variables);
                    }
                }
            } catch (Exception e) {

            }
        }


    }

    public static void getDolarVaraibles(String text,Set<String> variables){
        if(text==null || "".equals(text)){
            return;
        }
        List<RegexMatchItem> list = RegexUtil.regexFinds(text, "[\\$#](\\!)?\\{[a-zA-Z0-9\\-_\\$\\.]+\\}");
        for (RegexMatchItem item : list) {
            String str = item.matchStr;
            int idx=str.indexOf("{");
            str=str.substring(idx+1,str.length()-1);
            variables.add(str.trim());
        }
    }

}
