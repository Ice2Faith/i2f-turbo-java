package i2f.turbo.idea.plugin.jdbc.procedure.xml;

import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.xml.XmlElementDescriptorProvider;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlNSDescriptor;
import i2f.jdbc.procedure.consts.TagConsts;
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
            TagConsts.PROCEDURE_CALL,
            TagConsts.FUNCTION_CALL,
            TagConsts.PROCEDURE,
            TagConsts.SCRIPT_SEGMENT,
            TagConsts.SCRIPT_INCLUDE,
            TagConsts.LANG_NEW_PARAMS,
            TagConsts.LANG_PRINTLN,
            TagConsts.LANG_PRINTF,
            TagConsts.LANG_THREAD_POOL_SUBMIT,
            TagConsts.EVENT_SEND,
            TagConsts.EVENT_PUBLISH
    ));

    @Nullable
    @Override
    public XmlElementDescriptor getDescriptor(XmlTag tag) {
        if (!isXProcFile(tag)) {
            return null;
        }

        // 仍走之前验证过的路径：直接向 DTD 的 NS descriptor 要 descriptor，
        // 绝不调 tag.getDescriptor()（会重入本扩展点被短路成 null）
        XmlNSDescriptor ns = tag.getNSDescriptor(tag.getNamespace(), true);
        if (ns == null) {
            ns = tag.getNSDescriptor(tag.getNamespace(), false);
        }
        XmlElementDescriptor dtd = (ns == null) ? null : ns.getElementDescriptor(tag);
        if (dtd == null) {
            return null;
        }

        boolean anyAttrAllowed = OPEN_ATTR_TAGS.contains(tag.getName());
        return new OpenAttrElementDescriptor(dtd, anyAttrAllowed);
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
