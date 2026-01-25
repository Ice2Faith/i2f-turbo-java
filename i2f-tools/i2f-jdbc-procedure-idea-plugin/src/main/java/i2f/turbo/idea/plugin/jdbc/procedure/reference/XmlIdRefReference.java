package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import i2f.turbo.idea.plugin.jdbc.procedure.JdbcProcedureXmlProcedureJumpSourceFileAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.print.attribute.standard.PageRanges;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2026/1/20 10:32
 * @desc
 */
public class XmlIdRefReference extends PsiReferenceBase<XmlAttributeValue> implements PsiPolyVariantReference {
    public static final Logger log = Logger.getInstance(XmlIdRefReference.class);

    public XmlIdRefReference(@NotNull XmlAttributeValue element) {
        super(element, TextRange.from(1, element.getValue().length())); // skip quotes
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        PsiElement parent = myElement.getParent();
        if(!(parent instanceof XmlAttribute)){
            return ResolveResult.EMPTY_ARRAY;
        }
        XmlAttribute xmlAttribute = (XmlAttribute) parent;
        if(!"refid".equals(xmlAttribute.getName())) {
            return new ResolveResult[0];
        }

        String value = getValue();

//        log.warn("xml-id-ref:"+value);

        Map.Entry<VirtualFile, Integer> entry = JdbcProcedureXmlProcedureJumpSourceFileAction.getProcedureFileByProcedureId(value);
        if (entry == null) {
            return new ResolveResult[0];
        }

        VirtualFile virtualFile = entry.getKey();

//        log.warn("xml-id-vfile:"+virtualFile);

        PsiFile psiFile = PsiManager.getInstance(myElement.getProject()).findFile(virtualFile);

//        log.warn("xml-id-pfile:"+psiFile);
        if (!(psiFile instanceof XmlFile)) {
            return new ResolveResult[0];
        }

        XmlFile xmlFile = (XmlFile) psiFile;

        // 查找该文件中 id=value 的属性
        XmlAttribute target = findIdAttributeInFile(xmlFile, value);
//        log.warn("xml-id-target:"+target);
        if (target != null) {
            return new ResolveResult[]{new PsiElementResolveResult(target)};
        }

        return new ResolveResult[0];
    }

    @Nullable
    private XmlAttribute findIdAttributeInFile(XmlFile xmlFile, String idValue) {
        // 简化：遍历所有元素的 id 属性
        AtomicReference<XmlAttribute> ref = new AtomicReference<>();
        PsiTreeUtil.processElements(xmlFile, element -> {
            if (element instanceof XmlAttribute) {
                XmlAttribute attribute = (XmlAttribute) element;
                String name = attribute.getName();
                if ("id".equals(name)) {
                    if (Objects.equals(idValue, attribute.getValue())) {
                        ref.set(attribute);
                        return false;
                    }
                }
            }
            return true;
        });
        return ref.get();
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    @Override
    public boolean isSoft() {
        return true; // 软引用，找不到引用也不要报红色提示
    }

    @Override
    public @NotNull Object @NotNull [] getVariants() {
        return new Object[0];
    }

    // 支持 Find Usages：当点击 id 时，查找所有 refid 引用
    @Override
    public boolean isReferenceTo(PsiElement element) {
        if (!(element instanceof XmlAttributeValue)) {
            return false;
        }
        XmlAttributeValue attributeValue = (XmlAttributeValue) element;
        PsiElement parent = attributeValue.getParent();
        if(!(parent instanceof XmlAttribute)) {
            return false;
        }
        XmlAttribute attr = (XmlAttribute) parent;
        if (!"id".equals(attr.getName())) {
            return false;
        }
        String targetId = attr.getValue();
        String currentRefId = getValue();
        return Objects.equals(targetId, currentRefId);
    }
}
