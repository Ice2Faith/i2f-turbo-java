// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.impl.FunicPsiImplUtil;

public class FunicCommentSegmentImpl extends FunicPsiElement implements FunicCommentSegment {

    public FunicCommentSegmentImpl(ASTNode node) {
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
