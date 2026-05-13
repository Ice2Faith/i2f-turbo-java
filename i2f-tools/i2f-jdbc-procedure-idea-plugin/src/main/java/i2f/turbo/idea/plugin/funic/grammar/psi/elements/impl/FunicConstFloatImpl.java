// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstFloat;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

public class FunicConstFloatImpl extends FunicPsiElement implements FunicConstFloat {

    public FunicConstFloatImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstFloat(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberFloat() {
        return findChildByType(TERM_CONST_NUMBER_FLOAT);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberScien1() {
        return findChildByType(TERM_CONST_NUMBER_SCIEN_1);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberScien2() {
        return findChildByType(TERM_CONST_NUMBER_SCIEN_2);
    }

}
