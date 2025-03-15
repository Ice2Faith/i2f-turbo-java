package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTokenType;
import i2f.jdbc.procedure.context.ProcedureMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
                    if (completions != null) {
                        for (String attr : completions) {
                            result.addElement(LookupElementBuilder.create(name + attr));
                        }
                        return;
                    }
                } else {
                    XmlTag parentTag = attribute.getParent();
                    if (parentTag != null) {
                        String tagName = parentTag.getName();
//                        log.warn("xml-attr completion tag-name:" + tagName);
                        List<String> completions = new ArrayList<>();
                        if(Arrays.asList("procedure-call","function-call").contains(tagName)){
//                            log.warn("xml-attr completion call-node tag-name:" + tagName);

                            XmlAttribute refidAttr = parentTag.getAttribute("refid");
//                            log.warn("xml-attr completion call-node refid-attribute:" + refidAttr);

                            if(refidAttr!=null){
                                String refid = refidAttr.getValue();
//                                log.warn("xml-attr completion call-node refid-attribute-id:" + refid);
                                ProcedureMeta meta = JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.get(refid);
//                                log.warn("xml-attr completion call-node refid-attribute-meta:" + meta);
                                if(meta!=null){
                                    List<String> arguments = meta.getArguments();
                                    for (String argument : arguments) {
                                        completions.add(argument);
                                        List<String> features = meta.getArgumentFeatures().get(argument);
                                        if(features!=null && !features.isEmpty()){
                                            completions.add(argument+"."+String.join(".",features));
                                        }
                                    }
                                }
                            }
                        }

                        if(completions.isEmpty()){
                            completions.addAll(Arrays.asList(
                                    "_lang", "id", "refid", "__file", "__line",
                                    "return", "result", "value"
                            ));
                        }

                        if (completions != null) {
                            for (String attr : completions) {
                                result.addElement(LookupElementBuilder.create(attr));
                            }
                            return;
                        }
                    }
                }
            }

        }else if(type == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN){
//            log.warn("xml-attr-value completion:" + position.getClass() + "," + type.getClass() + ":" + type);

            XmlAttribute attribute = PsiTreeUtil.getParentOfType(position, XmlAttribute.class, false);
            if(attribute==null){
                return;
            }
//            log.warn("xml-attr-value completion attribute:" + attribute.getClass() + "," + attribute + ":" + attribute.getText() + "==>" + attribute.getName());

            XmlTag xmlTag = PsiTreeUtil.getParentOfType(attribute, XmlTag.class, false);

            String attributeName = attribute.getName();

            if(Arrays.asList("refid").contains(attributeName)) {
                if(xmlTag==null){
                    return;
                }
                String tagName = xmlTag.getName();
                if(Arrays.asList("procedure-call","function-call").contains(tagName)){
                    List<String> completions = new ArrayList<>();
                    for (Map.Entry<String, ProcedureMeta> entry : JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.entrySet()) {
                        ProcedureMeta value = entry.getValue();
                        completions.add(value.getName());
                    }
                    if (completions != null) {
                        for (String attr : completions) {
                            result.addElement(LookupElementBuilder.create(attr));
                        }
                        return;
                    }
                }


            }
        }
    }


}
