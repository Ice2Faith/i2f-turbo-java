// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicCatchBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicScriptBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicTryCatchFinallyExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunicTryCatchFinallyExpressImpl extends FunicPsiElement implements FunicTryCatchFinallyExpress {

    public FunicTryCatchFinallyExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitTryCatchFinallyExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunicCatchBlock> getCatchBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicCatchBlock.class);
    }

    @Override
    @NotNull
    public List<FunicScriptBlock> getScriptBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicScriptBlock.class);
    }

}
