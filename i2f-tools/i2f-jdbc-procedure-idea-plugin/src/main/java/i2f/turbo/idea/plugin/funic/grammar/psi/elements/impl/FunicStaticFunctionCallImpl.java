// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

public class FunicStaticFunctionCallImpl extends FunicPsiElement implements FunicStaticFunctionCall {

    public FunicStaticFunctionCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitStaticFunctionCall(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public FunicFunctionArguments getFunctionArguments() {
        return findNotNullChildByClass(FunicFunctionArguments.class);
    }

    @Override
    @NotNull
    public FunicFunctionName getFunctionName() {
        return findNotNullChildByClass(FunicFunctionName.class);
    }

    @Override
    @NotNull
    public FunicTypeMember getTypeMember() {
        return findNotNullChildByClass(FunicTypeMember.class);
    }

}
