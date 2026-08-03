// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviBlockHeadSegment;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviVisitor;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import org.jetbrains.annotations.NotNull;

import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.TERM_BLOCK_HEAD;

public class FunviBlockHeadSegmentImpl extends FunviPsiElement implements FunviBlockHeadSegment {

    public FunviBlockHeadSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitBlockHeadSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public PsiElement getTermBlockHead() {
        return findNotNullChildByType(TERM_BLOCK_HEAD);
    }

}
