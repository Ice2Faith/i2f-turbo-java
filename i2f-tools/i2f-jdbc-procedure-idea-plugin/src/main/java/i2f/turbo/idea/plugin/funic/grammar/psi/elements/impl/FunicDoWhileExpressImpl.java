// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConditionBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicDoWhileExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicScriptBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

public class FunicDoWhileExpressImpl extends FunicPsiElement implements FunicDoWhileExpress {

    public FunicDoWhileExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitDoWhileExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public FunicConditionBlock getConditionBlock() {
        return findNotNullChildByClass(FunicConditionBlock.class);
    }

    @Override
    @NotNull
    public FunicScriptBlock getScriptBlock() {
        return findNotNullChildByClass(FunicScriptBlock.class);
    }

}
