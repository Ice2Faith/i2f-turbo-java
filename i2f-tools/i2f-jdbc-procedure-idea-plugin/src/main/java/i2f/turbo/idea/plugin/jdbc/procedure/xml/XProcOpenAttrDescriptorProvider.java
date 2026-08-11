package i2f.turbo.idea.plugin.jdbc.procedure.xml;

import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlNSDescriptor;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/8/11 14:00
 * @desc
 */
public class XProcOpenAttrDescriptorProvider implements XmlElementDescriptorProvider {

    /**
     * 允许任意动态属性的 tag
     */
    private static final Set<String> OPEN_ATTR_TAGS = new HashSet<>(Arrays.asList(
            "procedure-call",
            "function-call",
            "procedure", "script-segment", "script-include",
            "lang-new-params", "lang-println", "lang-thread-pool-submit",
            "event-send", "event-publish"
    ));

    @Nullable
    @Override
    public XmlElementDescriptor getDescriptor(XmlTag tag) {
        if (!OPEN_ATTR_TAGS.contains(tag.getName())) {
            return null; // 其他节点 -> 原样走 DTD
        }
        if (!isXProcFile(tag)) {
            return null;
        }

        XmlNSDescriptor ns = tag.getNSDescriptor(tag.getNamespace(), true);
        if (ns == null) {
            ns = tag.getNSDescriptor(tag.getNamespace(), false); // DTD 无命名空间，strict=false 兜底
        }
        XmlElementDescriptor dtd = (ns == null) ? null : ns.getElementDescriptor(tag);
        return (dtd == null) ? null : new OpenAttrElementDescriptor(dtd);
    }

    private static boolean isXProcFile(XmlTag tag) {
        PsiFile file = tag.getContainingFile();
        if (!(file instanceof XmlFile)) {
            return false;
        }
        XmlFile xmlFile = (XmlFile) file;

        XmlTag root = xmlFile.getRootTag();
        return root != null && "procedure".equals(root.getName());
    }
}
