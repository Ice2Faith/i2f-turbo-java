// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicNewInstanceExpressImpl extends FunicPsiElement implements FunicNewInstanceExpress {

    public FunicNewInstanceExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitNewInstanceExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public FunicFullName getFullName() {
        return findNotNullChildByClass(FunicFullName.class);
    }

    @Override
    @NotNull
    public FunicFunctionArguments getFunctionArguments() {
        return findNotNullChildByClass(FunicFunctionArguments.class);
    }

    @Override
    @Nullable
    public FunicMapValueExpress getMapValueExpress() {
        return findChildByClass(FunicMapValueExpress.class);
    }

}
