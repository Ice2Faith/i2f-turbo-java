// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicCommentSegment;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicSegment;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicSegmentImpl extends FunicPsiElement implements FunicSegment {

    public FunicSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicCommentSegment getCommentSegment() {
        return findChildByClass(FunicCommentSegment.class);
    }

    @Override
    @Nullable
    public FunicExpress getExpress() {
        return findChildByClass(FunicExpress.class);
    }

}
