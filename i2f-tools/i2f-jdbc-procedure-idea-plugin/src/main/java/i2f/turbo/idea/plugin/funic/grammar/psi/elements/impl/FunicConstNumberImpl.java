// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstNumber;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

public class FunicConstNumberImpl extends FunicPsiElement implements FunicConstNumber {

    public FunicConstNumberImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstNumber(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumber() {
        return findChildByType(TERM_CONST_NUMBER);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberBin() {
        return findChildByType(TERM_CONST_NUMBER_BIN);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberHex() {
        return findChildByType(TERM_CONST_NUMBER_HEX);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberOtc() {
        return findChildByType(TERM_CONST_NUMBER_OTC);
    }

}
