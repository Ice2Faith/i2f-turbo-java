package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.json.JsonBundle;
import com.intellij.lang.HelpID;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JdbcProcedureXmlFindUsagesProvider implements FindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof XmlAttributeValue;
    }

    @Override
    public @Nullable
    @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return HelpID.FIND_OTHER_USAGES;
    }

    @Override
    public @Nls
    @NotNull String getType(@NotNull PsiElement psiElement) {
        return "usages-reference";
    }

    @Override
    public @Nls
    @NotNull String getDescriptiveName(@NotNull PsiElement psiElement) {
        if (!(psiElement instanceof XmlAttributeValue)) {
            return JsonBundle.message("unnamed.desc");
        }
        XmlAttributeValue element = (XmlAttributeValue) psiElement;
        String value = element.getValue();
        if (value == null || value.isEmpty()) {
            return JsonBundle.message("unnamed.desc");
        }
        return value;
    }

    @Override
    public @Nls
    @NotNull String getNodeText(@NotNull PsiElement psiElement, boolean b) {
        return getDescriptiveName(psiElement);
    }
}
