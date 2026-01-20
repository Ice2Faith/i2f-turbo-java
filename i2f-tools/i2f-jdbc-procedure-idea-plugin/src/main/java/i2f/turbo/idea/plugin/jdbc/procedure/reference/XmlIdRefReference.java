package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.util.indexing.ID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2026/1/20 10:32
 * @desc
 */
public class XmlIdRefReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {

    private static final ID<String, Void> ID_INDEX = ID.create("XmlIdRef.id");
    private static final ID<String, Void> REFID_INDEX = ID.create("XmlIdRef.refid");

    public XmlIdRefReference(@NotNull XmlAttributeValue element) {
        super(element, TextRange.from(1, element.getValue().length() - 2)); // skip quotes
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String value = getValue();
        if (value == null) {
            return new ResolveResult[0];
        }
        List<ResolveResult> results = new ArrayList<>();

        // 搜索所有 XML 文件中的 id 属性值匹配
        GlobalSearchScope scope = GlobalSearchScope.projectScope(myElement.getProject());
        Collection<VirtualFile> files = FileBasedIndex.getInstance().getContainingFiles(ID_INDEX, value, scope);

        for (VirtualFile file : files) {
            PsiFile psiFile = PsiManager.getInstance(myElement.getProject()).findFile(file);
            if (!(psiFile instanceof XmlFile)) continue;

            // 查找该文件中 id=value 的属性
            XmlAttribute target = findIdAttributeInFile((XmlFile) psiFile, value);
            if (target != null) {
                results.add(new PsiElementResolveResult(target));
            }
        }

        return results.toArray(new ResolveResult[0]);
    }

    @Nullable
    private XmlAttribute findIdAttributeInFile(XmlFile xmlFile, String idValue) {
        // 简化：遍历所有元素的 id 属性
        return PsiTreeUtil.processElements(xmlFile, element -> {
            if (element instanceof XmlAttribute attr && "id".equals(attr.getName()) && idValue.equals(attr.getValue())) {
                return false; // stop and return
            }
            return true;
        }) ? null : // 如果没找到返回 null
                // 实际需自定义查找逻辑，这里简化
                null;
    }

    // 更健壮的做法：使用 PsiRecursiveElementWalkingVisitor
    // 此处为简化，实际建议用 visitor 遍历

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    @Override
    public @NotNull Object @NotNull [] getVariants() {
        return new Object[0];
    }

    // 支持 Find Usages：当点击 id 时，查找所有 refid 引用
    @Override
    public boolean isReferenceTo(PsiElement element) {
        if (!(element instanceof XmlAttribute attr) || !"id".equals(attr.getName())) {
            return false;
        }
        String targetId = attr.getValue();
        String currentRefId = getValue();
        return Objects.equals(targetId, currentRefId);
    }
}
