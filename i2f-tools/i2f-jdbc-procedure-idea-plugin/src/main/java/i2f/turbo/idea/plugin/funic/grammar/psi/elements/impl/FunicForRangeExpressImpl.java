// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicForRangeExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicScriptBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.IDENTIFIER;

public class FunicForRangeExpressImpl extends FunicPsiElement implements FunicForRangeExpress {

    public FunicForRangeExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitForRangeExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunicExpress> getExpressList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicExpress.class);
    }

    @Override
    @NotNull
    public FunicScriptBlock getScriptBlock() {
        return findNotNullChildByClass(FunicScriptBlock.class);
    }

    @Override
    @NotNull
    public PsiElement getIdentifier() {
        return findNotNullChildByType(IDENTIFIER);
    }

}
