// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviBlockBody;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviElseBlock;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviParameters;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviVisitor;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.TERM_BLOCK_ELSE;

public class FunviElseBlockImpl extends FunviPsiElement implements FunviElseBlock {

    public FunviElseBlockImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitElseBlock(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public FunviBlockBody getBlockBody() {
        return findNotNullChildByClass(FunviBlockBody.class);
    }

    @Override
    @Nullable
    public FunviParameters getParameters() {
        return findChildByClass(FunviParameters.class);
    }

    @Override
    @NotNull
    public PsiElement getTermBlockElse() {
        return findNotNullChildByType(TERM_BLOCK_ELSE);
    }

}
