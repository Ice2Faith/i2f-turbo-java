package i2f.turbo.idea.plugin.jdbc.procedure.xml;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlElement;
import com.intellij.xml.XmlAttributeDescriptor;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/8/11 14:01
 * @desc
 */
public class AnyAttributeDescriptor implements XmlAttributeDescriptor {
    private final String myName;
    private final PsiElement myDeclaration;

    AnyAttributeDescriptor(String name, @Nullable PsiElement declaration) {
        myName = name;
        myDeclaration = declaration;
    }

    // ===== PsiMetaData =====
    @Override
    public void init(PsiElement element) {
    }

    @Override
    public PsiElement getDeclaration() {
        return myDeclaration;
    }

    @Override
    public String getName(PsiElement context) {
        return myName;
    }

    @Override
    public boolean hasIdRefType() {
        return false;
    }

    @Override
    public @Nullable @NlsContexts.DetailedDescription String validateValue(XmlElement xmlElement, String s) {
        return null;
    }

    @Override
    public @NlsSafe String getName() {
        return myName;
    }

    // ===== XmlAttributeDescriptor =====
    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean hasIdType() {
        return false;
    }

    @Override
    public boolean isEnumerated() {
        return false;
    }

    @Nullable
    @Override
    public String[] getEnumeratedValues() {
        return null;
    }

    @Nullable
    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public boolean isFixed() {
        return false;
    }
}
