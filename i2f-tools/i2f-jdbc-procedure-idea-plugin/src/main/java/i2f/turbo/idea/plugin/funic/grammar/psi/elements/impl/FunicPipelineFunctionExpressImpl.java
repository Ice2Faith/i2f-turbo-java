// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicPipelineFunctionExpressImpl extends FunicPsiElement implements FunicPipelineFunctionExpress {

    public FunicPipelineFunctionExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitPipelineFunctionExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicFunctionName getFunctionName() {
        return findChildByClass(FunicFunctionName.class);
    }

    @Override
    @Nullable
    public FunicGlobalFunctionCall getGlobalFunctionCall() {
        return findChildByClass(FunicGlobalFunctionCall.class);
    }

    @Override
    @Nullable
    public FunicStaticFieldValue getStaticFieldValue() {
        return findChildByClass(FunicStaticFieldValue.class);
    }

    @Override
    @Nullable
    public FunicStaticFunctionCall getStaticFunctionCall() {
        return findChildByClass(FunicStaticFunctionCall.class);
    }

}
