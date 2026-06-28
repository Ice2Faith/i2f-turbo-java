// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicThirdOperateRightPart;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunicThirdOperateRightPartImpl extends FunicPsiElement implements FunicThirdOperateRightPart {

    public FunicThirdOperateRightPartImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitThirdOperateRightPart(this);
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

}
