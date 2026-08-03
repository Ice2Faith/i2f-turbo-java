// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviParameter;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviVisitor;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.TERM_REF_VALUE;

public class FunviParameterImpl extends FunviPsiElement implements FunviParameter {

    public FunviParameterImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitParameter(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermRefValue() {
        return findChildByType(TERM_REF_VALUE);
    }

}
