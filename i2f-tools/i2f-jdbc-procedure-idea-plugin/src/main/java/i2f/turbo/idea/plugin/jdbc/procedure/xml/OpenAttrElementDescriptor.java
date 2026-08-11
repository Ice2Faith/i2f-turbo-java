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

    OpenAttrElementDescriptor(XmlElementDescriptor delegate) {
        myDelegate = delegate;
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
        XmlAttributeDescriptor d = myDelegate.getAttributeDescriptor(attribute);
        return (d != null) ? d : new AnyAttributeDescriptor(attribute.getName(), attribute);
    }

    @Nullable
    @Override
    public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, @Nullable XmlTag context) {
        XmlAttributeDescriptor d = myDelegate.getAttributeDescriptor(attributeName, context);
        return (d != null) ? d : new AnyAttributeDescriptor(attributeName, context);
    }
}
