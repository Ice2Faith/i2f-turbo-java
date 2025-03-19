package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.actions.EditorActionUtil;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.xml.XmlAttributeReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import i2f.jdbc.procedure.context.ProcedureMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JdbcProcedureXmlPsiReferenceContributor extends PsiReferenceContributor {
    public static final Logger log = Logger.getInstance(JdbcProcedureXmlPsiReferenceContributor.class);

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlAttributeValue.class),new JdbcProcedureXmlPsiReferenceProvider());
    }

    public static class JdbcProcedureXmlPsiReferenceProvider extends PsiReferenceProvider {
        @NotNull
        @Override
        public PsiReference [] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
//            log.warn("xml-psi-reference begin :"+element);
            if (!(element instanceof XmlAttributeValue)) {
                return PsiReference.EMPTY_ARRAY;
            }

            XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) element;
            XmlAttribute xmlAttribute = PsiTreeUtil.getParentOfType(element, XmlAttribute.class, false);
//            log.warn("xml-psi-reference attribute :"+xmlAttribute);
            String name = xmlAttribute.getName();
//            log.warn("xml-psi-reference attr-name :"+name);
            if("refid".equals(name)
            ||"id".equals(name)){
                return createReference(xmlAttributeValue);
            }

            return PsiReference.EMPTY_ARRAY;
        }

        private PsiReference[] createReference(XmlAttributeValue xmlAttributeValue) {
            String value = xmlAttributeValue.getValue();
//            log.warn("xml-psi-reference attr-value :"+value);
            if(value==null || value.isEmpty()){
                return PsiReference.EMPTY_ARRAY;
            }
            ProcedureMeta meta = JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP.get(value);
//            log.warn("xml-psi-reference attr-value-meta :"+meta);
            if(meta==null){
                return PsiReference.EMPTY_ARRAY;
            }
            List<PsiReference> ret=new ArrayList<>();
            TextRange textRange = xmlAttributeValue.getTextRange();
            TextRange contentRange = new TextRange(
                    textRange.getStartOffset() + 1,
                    textRange.getEndOffset() - 1
            );
            VirtualFile file = (VirtualFile) meta.getTarget();
//            log.warn("xml-psi-reference attr-value-vfile :"+file);
            PsiFile psiFile = PsiManager.getInstance(xmlAttributeValue.getProject()).findFile(file);
//            log.warn("xml-psi-reference attr-value-pfile :"+psiFile);
            ret.add(new JdbcProcedureIdReference(xmlAttributeValue, contentRange, psiFile));
            return ret.toArray(new PsiReference[0]);

        }
    }

    public static class JdbcProcedureIdReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
        protected PsiFile targetPisElement;
        public JdbcProcedureIdReference(@NotNull PsiElement element, TextRange textRange,PsiFile targetPisElement) {
            super(element, textRange);
            this.targetPisElement=targetPisElement;
        }

        @NotNull
        @Override
        public ResolveResult [] multiResolve(boolean b) {
            PsiElementResolveResult resolveResult = new PsiElementResolveResult(targetPisElement);
            return new ResolveResult[]{resolveResult};
        }
        @Nullable
        @Override
        public PsiElement resolve() {
            ResolveResult[] resolveResults = multiResolve(false);
            if(resolveResults==null || resolveResults.length==0){
                return null;
            }
            return resolveResults[0].getElement();
        }

    }
}
