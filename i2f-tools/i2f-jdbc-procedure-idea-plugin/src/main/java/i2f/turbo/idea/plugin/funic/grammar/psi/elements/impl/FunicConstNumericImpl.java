// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstFloat;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstNumber;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstNumeric;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicConstNumericImpl extends FunicPsiElement implements FunicConstNumeric {

    public FunicConstNumericImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstNumeric(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicConstFloat getConstFloat() {
        return findChildByClass(FunicConstFloat.class);
    }

    @Override
    @Nullable
    public FunicConstNumber getConstNumber() {
        return findChildByClass(FunicConstNumber.class);
    }

}
