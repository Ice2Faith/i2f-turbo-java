package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptFunctionCall;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptRefCall;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/1/20 8:43
 * @desc
 */
public class XmlFunctionReferenceContributor extends PsiReferenceContributor {
    public static final Logger log = Logger.getInstance(XmlFunctionReferenceContributor.class);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(TinyScriptTypes.NAMING),
                new PsiReferenceProvider() {
                    @Override
                    public @NotNull PsiReference [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        log.warn("xml-func:"+" : "+element.getClass()+" : "+element.getText());

                        PsiElement parent = element.getParent();
                        log.warn("xml-func-pparent:"+" : "+parent.getClass()+" : "+parent.getText());
                        if(parent!=null && (parent instanceof TinyScriptRefCall)){
                            return PsiReference.EMPTY_ARRAY;
                        }


                        return new PsiReference[]{new XmlFunctionReference(element)};

                    }
                }
        );
    }
}
