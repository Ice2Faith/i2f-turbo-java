// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunviSegmentImpl extends FunviPsiElement implements FunviSegment {

    public FunviSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunviBlock getBlock() {
        return findChildByClass(FunviBlock.class);
    }

    @Override
    @Nullable
    public FunviTextSegment getTextSegment() {
        return findChildByClass(FunviTextSegment.class);
    }

    @Override
    @Nullable
    public FunviValue getValue() {
        return findChildByClass(FunviValue.class);
    }

}
