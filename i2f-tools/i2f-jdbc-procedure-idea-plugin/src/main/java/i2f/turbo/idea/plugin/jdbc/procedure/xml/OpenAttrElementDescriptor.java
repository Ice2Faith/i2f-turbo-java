package i2f.turbo.idea.plugin.jdbc.procedure.xml;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlElementDescriptor;
import com.intellij.xml.XmlElementsGroup;
import com.intellij.xml.XmlNSDescriptor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/8/11 14:01
 * @desc
 */
public class OpenAttrElementDescriptor implements XmlElementDescriptor {
    private final XmlElementDescriptor myDelegate;
    private final boolean myAnyAttributeAllowed;

    public OpenAttrElementDescriptor(XmlElementDescriptor delegate, boolean anyAttributeAllowed) {
        myDelegate = delegate;
        myAnyAttributeAllowed = anyAttributeAllowed;
    }

    // ===== PsiMetaData（init 是上一版漏掉的，211 同样要求实现）=====
    @Override
    public PsiElement getDeclaration() {
        return myDelegate.getDeclaration();
    }

    @Override
    public String getName(PsiElement context) {
        return myDelegate.getName(context);
    }

    @Override
    public @NonNls String getQualifiedName() {
        return myDelegate.getQualifiedName();
    }

    @Override
    public String getDefaultName() {
        return myDelegate.getDefaultName();
    }

    @Override
    public void init(PsiElement element) {
        myDelegate.init(element);
    }

    // ===== 子节点/命名空间等：原封不动走 DTD =====
    @Override
    public XmlElementDescriptor[] getElementsDescriptors(XmlTag context) {
        return myDelegate.getElementsDescriptors(context);
    }

    @Override
    public XmlElementDescriptor getElementDescriptor(XmlTag child, XmlTag context) {
        return myDelegate.getElementDescriptor(child, context);
    }

    @Override
    public XmlAttributeDescriptor[] getAttributesDescriptors(@Nullable XmlTag context) {
        return myDelegate.getAttributesDescriptors(context);
    }

    @Override
    public XmlNSDescriptor getNSDescriptor() {
        return myDelegate.getNSDescriptor();
    }

    @Override
    public @Nullable XmlElementsGroup getTopGroup() {
        return myDelegate.getTopGroup();
    }

    @Override
    public int getContentType() {
        return myDelegate.getContentType();
    }

    @Override
    public @Nullable String getDefaultValue() {
        return myDelegate.getDefaultValue();
    }

    @Override
    public @NlsSafe String getName() {
        return myDelegate.getName();
    }

    // ===== 属性：DTD 不认识的返回宽容 descriptor => 不报红 =====
    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(XmlAttribute attribute) {
        return resolve(attribute.getName(), attribute.getParent(), attribute);
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, @Nullable XmlTag context) {
        return resolve(attributeName, context, context);
    }

    private XmlAttributeDescriptor resolve(String name, @Nullable XmlTag context, PsiElement declaration) {
        // 1) 精确命中 DTD 声明（含 DTD 里已显式声明的带后缀属性，如 value.visit、result.trim）
        XmlAttributeDescriptor d = myDelegate.getAttributeDescriptor(name, context);
        if (d != null) {
            return d;
        }

        // 2) XProc4j 属性修饰符链：value.int / value.string.long.int / Ic_X.eval-ts
        //    基属性 = 第一个 '.' 之前的部分；基属性在 DTD 中声明过 => 整条链合法
        int dot = name.indexOf('.');
        if (dot > 0) {
            String base = name.substring(0, dot);
            XmlAttributeDescriptor baseDescriptor = myDelegate.getAttributeDescriptor(base, context);
            if (baseDescriptor != null) {
                // 直接复用基属性 descriptor：值校验、语义与基属性一致
                return baseDescriptor;
            }
        }

        // 3) 开放 tag：其余任意属性放行（procedure-call 的 Ic_* 等动态传参）
        if (myAnyAttributeAllowed) {
            return new AnyAttributeDescriptor(name, declaration);
        }

        // 4) 非开放 tag 且既不是修饰符链也不在白名单 -> 保持报红
        return null;
    }
}
