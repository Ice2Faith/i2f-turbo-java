// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicCommentSegment;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_COMMENT_MULTI_LINE;
import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_COMMENT_SINGLE_LINE;

public class FunicCommentSegmentImpl extends FunicPsiElement implements FunicCommentSegment {

    public FunicCommentSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitCommentSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermCommentMultiLine() {
        return findChildByType(TERM_COMMENT_MULTI_LINE);
    }

    @Override
    @Nullable
    public PsiElement getTermCommentSingleLine() {
        return findChildByType(TERM_COMMENT_SINGLE_LINE);
    }

}
