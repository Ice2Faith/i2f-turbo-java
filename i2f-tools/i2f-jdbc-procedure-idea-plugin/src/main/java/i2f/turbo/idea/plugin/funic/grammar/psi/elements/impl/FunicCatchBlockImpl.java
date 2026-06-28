// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicCatchBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicFullName;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicScriptBlock;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.IDENTIFIER;

public class FunicCatchBlockImpl extends FunicPsiElement implements FunicCatchBlock {

    public FunicCatchBlockImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitCatchBlock(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunicFullName> getFullNameList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicFullName.class);
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
