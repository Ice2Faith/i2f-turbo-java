// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviContent;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.FunviVisitor;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.*;

public class FunviContentImpl extends FunviPsiElement implements FunviContent {

    public FunviContentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitContent(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermIdentifier() {
        return findChildByType(TERM_IDENTIFIER);
    }

    @Override
    @Nullable
    public PsiElement getTermText() {
        return findChildByType(TERM_TEXT);
    }

    @Override
    @Nullable
    public PsiElement getTermWhitespace() {
        return findChildByType(TERM_WHITESPACE);
    }

}
