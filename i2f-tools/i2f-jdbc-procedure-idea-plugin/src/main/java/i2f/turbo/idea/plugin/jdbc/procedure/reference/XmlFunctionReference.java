package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.*;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/1/20 8:46
 * @desc
 */
public class XmlFunctionReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    public static final Logger log = Logger.getInstance(XmlFunctionReference.class);


    public XmlFunctionReference(@NotNull PsiElement element) {
        super(element);
    }


    @Override
    public @NotNull ResolveResult [] multiResolve(boolean incompleteCode) {
        List<PsiElement> list = CompletionHelper.getXmlFileFunctionsPsiElementsFast(myElement.getProject(), myElement.getText());
        if(list==null){
            return new ResolveResult[0];
        }
        ResolveResult[] results = new ResolveResult[list.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = new PsiElementResolveResult(list.get(i));
        }
        return results;
    }

    @Override
    public PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

}
