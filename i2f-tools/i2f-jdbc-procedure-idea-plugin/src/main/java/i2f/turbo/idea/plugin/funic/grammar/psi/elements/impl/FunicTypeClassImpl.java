// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicTypeClass;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_CONST_CLASS;

public class FunicTypeClassImpl extends FunicPsiElement implements FunicTypeClass {

    public FunicTypeClassImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitTypeClass(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public PsiElement getTermConstClass() {
        return findNotNullChildByType(TERM_CONST_CLASS);
    }

}
