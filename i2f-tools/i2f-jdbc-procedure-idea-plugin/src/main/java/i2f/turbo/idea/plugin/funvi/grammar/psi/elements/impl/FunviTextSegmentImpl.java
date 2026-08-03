// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviContent;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviTextSegment;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviVisitor;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunviTextSegmentImpl extends FunviPsiElement implements FunviTextSegment {

    public FunviTextSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitTextSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunviContent> getContentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunviContent.class);
    }

}
