package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/1/20 10:30
 * @desc
 */
public class XmlIdRefReferenceContributor extends PsiReferenceContributor {
    public static final Logger log = Logger.getInstance(XmlIdRefReferenceContributor.class);

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        // 处理 refid -> id 的跳转
        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue()
                        .withParent(XmlPatterns.xmlAttribute("refid")),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        if (!(element instanceof XmlAttributeValue)) {
                            return PsiReference.EMPTY_ARRAY;
                        }
//                        String text = element.getText();
//                        log.warn("xml-refid:" + " : " + element.getClass() + text);

                        boolean isSupport = true;
                        PsiFile containingFile = element.getContainingFile();
                        if (containingFile instanceof XmlFile) {
                            XmlFile xmlFile = (XmlFile) containingFile;
                            XmlTag rootTag = xmlFile.getRootTag();
                            if (rootTag != null) {
                                String name = rootTag.getName();
                                if (!"procedure".equals(name)) {
                                    isSupport = false;
                                }
                            }
                        }
                        if(isSupport) {
                            return new PsiReference[]{new XmlIdRefReference((XmlAttributeValue) element)};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                }
        );

        // 处理 id -> 所有 refid 的反向引用（用于 Find Usages）
        registrar.registerReferenceProvider(
                XmlPatterns.xmlAttributeValue()
                        .withParent(XmlPatterns.xmlAttribute("id")),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                           @NotNull ProcessingContext context) {
                        if (!(element instanceof XmlAttributeValue)) {
                            return PsiReference.EMPTY_ARRAY;
                        }
//                        String text = element.getText();
//                        log.warn("xml-id:" + " : " + element.getClass() + text);
                        boolean isSupport = true;
                        PsiFile containingFile = element.getContainingFile();
                        if (containingFile instanceof XmlFile) {
                            XmlFile xmlFile = (XmlFile) containingFile;
                            XmlTag rootTag = xmlFile.getRootTag();
                            if (rootTag != null) {
                                String name = rootTag.getName();
                                if (!"procedure".equals(name)) {
                                    isSupport = false;
                                }
                            }
                        }
                        if(isSupport) {
                            return new PsiReference[]{new XmlIdRefReference((XmlAttributeValue) element)};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                }
        );
    }
}
