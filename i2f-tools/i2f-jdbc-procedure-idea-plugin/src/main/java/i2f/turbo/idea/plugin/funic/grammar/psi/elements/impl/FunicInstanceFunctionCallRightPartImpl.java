// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicFunctionArguments;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicFunctionName;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicInstanceFunctionCallRightPart;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

public class FunicInstanceFunctionCallRightPartImpl extends FunicPsiElement implements FunicInstanceFunctionCallRightPart {

    public FunicInstanceFunctionCallRightPartImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitInstanceFunctionCallRightPart(this);
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

}
