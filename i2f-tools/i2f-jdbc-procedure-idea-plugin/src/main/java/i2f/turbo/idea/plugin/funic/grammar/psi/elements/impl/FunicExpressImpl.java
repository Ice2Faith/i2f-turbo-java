// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpressSegment;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicOperatorSegment;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunicExpressImpl extends FunicPsiElement implements FunicExpress {

    public FunicExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public FunicExpressSegment getExpressSegment() {
        return findNotNullChildByClass(FunicExpressSegment.class);
    }

    @Override
    @NotNull
    public List<FunicOperatorSegment> getOperatorSegmentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicOperatorSegment.class);
    }

}
