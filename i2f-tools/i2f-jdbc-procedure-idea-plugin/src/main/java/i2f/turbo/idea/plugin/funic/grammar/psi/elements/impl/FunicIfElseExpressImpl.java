// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConditionBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicIfElseExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicScriptBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunicIfElseExpressImpl extends FunicPsiElement implements FunicIfElseExpress {

    public FunicIfElseExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitIfElseExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunicConditionBlock> getConditionBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicConditionBlock.class);
    }

    @Override
    @NotNull
    public List<FunicScriptBlock> getScriptBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicScriptBlock.class);
    }

}
