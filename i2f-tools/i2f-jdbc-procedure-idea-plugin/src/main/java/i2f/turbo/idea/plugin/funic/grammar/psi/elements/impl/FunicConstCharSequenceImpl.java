// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstCharSequence;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstRenderString;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstString;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicConstCharSequenceImpl extends FunicPsiElement implements FunicConstCharSequence {

    public FunicConstCharSequenceImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstCharSequence(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicConstRenderString getConstRenderString() {
        return findChildByClass(FunicConstRenderString.class);
    }

    @Override
    @Nullable
    public FunicConstString getConstString() {
        return findChildByClass(FunicConstString.class);
    }

}
