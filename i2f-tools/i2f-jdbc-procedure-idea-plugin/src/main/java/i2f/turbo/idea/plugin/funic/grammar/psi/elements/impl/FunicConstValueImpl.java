// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicConstValueImpl extends FunicPsiElement implements FunicConstValue {

    public FunicConstValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstValue(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicConstBoolean getConstBoolean() {
        return findChildByClass(FunicConstBoolean.class);
    }

    @Override
    @Nullable
    public FunicConstCharSequence getConstCharSequence() {
        return findChildByClass(FunicConstCharSequence.class);
    }

    @Override
    @Nullable
    public FunicConstMultiString getConstMultiString() {
        return findChildByClass(FunicConstMultiString.class);
    }

    @Override
    @Nullable
    public FunicConstNull getConstNull() {
        return findChildByClass(FunicConstNull.class);
    }

    @Override
    @Nullable
    public FunicConstNumeric getConstNumeric() {
        return findChildByClass(FunicConstNumeric.class);
    }

}
