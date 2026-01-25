package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/1/20 8:43
 * @desc
 */
public class XmlTagReferenceContributor extends PsiReferenceContributor {
    public static final Logger log = Logger.getInstance(XmlTagReferenceContributor.class);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                XmlPatterns.xmlTag(),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
//                         String text = element.getText();
//                        log.warn("xml-tag:"+" : "+element.getClass()+text);

                        if (!(element instanceof XmlTag)) {
                            return PsiReference.EMPTY_ARRAY;
                        }

                        XmlTag xmlTag = (XmlTag) element;

                        boolean isSupport = true;
                        PsiFile containingFile = element.getContainingFile();
//                        log.warn("xml-tag-file : "+containingFile);
                        if (containingFile instanceof XmlFile) {
                            XmlFile xmlFile = (XmlFile) containingFile;
                            XmlTag rootTag = xmlFile.getRootTag();
//                            log.warn("xml-tag-root:"+rootTag);
                            if (rootTag != null) {
                                String name = rootTag.getName();
//                                log.warn("xml-tag-root-name:"+name);
                                if (!"procedure".equals(name)) {
                                    isSupport = false;
                                }
                            }
                        }
//                        log.warn("xml-tag-support:"+isSupport);
                        if (isSupport) {
                            return new PsiReference[]{new XmlTagReference(xmlTag)};
                        }

                        return PsiReference.EMPTY_ARRAY;
                    }
                }
        );
    }
}
