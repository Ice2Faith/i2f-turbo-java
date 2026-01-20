package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/1/20 8:43
 * @desc
 */
public class XmlTagReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement()
                        .withParent(PlatformPatterns.psiElement(XmlTag.class)),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        if (!(element instanceof XmlToken)) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        XmlToken token = (XmlToken) element;
                        boolean isSupport=true;
                        PsiFile containingFile = element.getContainingFile();
                        if(containingFile instanceof XmlFile) {
                            XmlFile xmlFile = (XmlFile) containingFile;
                            XmlTag rootTag = xmlFile.getRootTag();
                            if(rootTag!=null) {
                                String name = rootTag.getName();
                                if (!"procedure".equals(name)) {
                                    isSupport = false;
                                }
                            }
                        }
                        if(token.getTokenType() == XmlTokenType.XML_NAME && isSupport) {
                            return new PsiReference[]{new XmlTagReference(token)};
                        }

                        return PsiReference.EMPTY_ARRAY;
                    }
                }
        );
    }
}
