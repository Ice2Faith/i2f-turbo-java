package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.GenericAttributeValue;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptFunctionCall;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/6/26 21:24
 * @desc
 */
public class JdbcProcedureRefidLineMarkerProvider extends RelatedItemLineMarkerProvider {
    public static final Logger log = Logger.getInstance(JdbcProcedureRefidLineMarkerProvider.class);

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        addXmlRefidMarkers(element, result);

    }

    public void addXmlRefidMarkers(PsiElement element, Collection<? super RelatedItemLineMarkerInfo<?>> result) {
        //        log.warn("xml-line-marker-elem:" + element.getClass());
        String refid = null;
        if (element instanceof XmlAttribute) {

            PsiFile psiFile = element.getContainingFile();
            if (psiFile == null) {
                return;
            }
//        log.warn("xml-line-marker-file:" + psiFile.getClass());
            if (!(psiFile instanceof XmlFile)) {
                return;
            }

            XmlAttribute xmlAttribute = (XmlAttribute) element;
            String attrName = xmlAttribute.getName();
//        log.warn("xml-line-marker-attr:" + attrName);
            if (!"refid".equals(attrName)) {
                return;
            }

            XmlTag xmlTag = xmlAttribute.getParent();
            if (xmlTag == null) {
                return;
            }

            String name = xmlTag.getName();
            if (!Arrays.asList(
                    "procedure-call",
                    "function-call",
                    "script-include").contains(name)) {
                return;
            }

            refid = xmlAttribute.getValue();
        } else if (element instanceof TinyScriptFunctionCall) {
            TinyScriptFunctionCall call = (TinyScriptFunctionCall) element;
            PsiElement naming = call.getNaming();
            refid = naming.getText();
        }

//        log.warn("xml-line-marker-refid:" + refid);
        if (refid == null || refid.isEmpty()) {
            return;
        }


        Project project = element.getProject();

        List<PsiElement> targets = new ArrayList<>();

        GlobalSearchScope scope = GlobalSearchScope.everythingScope(project);
//        Module module = ModuleUtilCore.findModuleForPsiElement(element);
//        if (null != module) {
//            scope = GlobalSearchScope.moduleScope(module);
//        }

        DomService domService = DomService.getInstance();
        List<DomFileElement<ProcedureDomElement>> xmlFiles = domService.getFileElements(ProcedureDomElement.class, project, scope);

//        log.warn("xml-line-marker-files:" + xmlFiles.size());
        if (xmlFiles == null || xmlFiles.isEmpty()) {
            return;
        }

        for (DomFileElement<ProcedureDomElement> item : xmlFiles) {
            ProcedureDomElement rootElement = item.getRootElement();
            GenericAttributeValue<String> attrValue = rootElement.getId();
            if (attrValue == null) {
                continue;
            }
            String id = attrValue.getRawText();
            if (id == null || id.isEmpty()) {
                continue;
            }
            XmlTag psiTag = attrValue.getXmlTag();
            if (psiTag == null) {
                continue;
            }
            if (refid.equals(id)) {
                targets.add(psiTag);
            }
            searchInChildren(refid, psiTag.getChildren(), 0, targets);
        }

        if (targets.isEmpty()) {
            return;
        }

        NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(XProc4jConsts.ICON)
                .setTargets(targets)
                .setTooltipText("Navigate to " + XProc4jConsts.NAME + " file.");

        result.add(builder.createLineMarkerInfo(element.getFirstChild()));
    }

    public void searchInChildren(String refid, PsiElement[] list, int level, List<PsiElement> targets) {
        if (level > 3) {
            return;
        }
        if (list == null || list.length == 0) {
            return;
        }
        for (PsiElement item : list) {
            if (!(item instanceof XmlTag)) {
                continue;
            }
            XmlTag tag = (XmlTag) item;
            String tagName = tag.getName();
            if ("script-segment".equals(tagName)
                    || "procedure".equals(tagName)) {
                XmlAttribute idAttr = tag.getAttribute("id");
                if (idAttr != null) {
                    String id = idAttr.getValue();
                    if (Objects.equals(refid, id)) {
                        targets.add(tag);
                    }
                }
            }
            PsiElement[] children = item.getChildren();
            searchInChildren(refid, children, level + 1, targets);
        }
    }
}
