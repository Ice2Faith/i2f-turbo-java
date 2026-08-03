// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.*;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funvi.lang.psi.impl.FunviPsiImplUtil;

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
