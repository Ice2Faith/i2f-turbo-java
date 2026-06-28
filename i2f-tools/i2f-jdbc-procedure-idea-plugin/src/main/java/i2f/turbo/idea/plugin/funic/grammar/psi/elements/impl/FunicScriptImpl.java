// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunicScriptImpl extends FunicPsiElement implements FunicScript {

    public FunicScriptImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitScript(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicCommentSegment getCommentSegment() {
        return findChildByClass(FunicCommentSegment.class);
    }

    @Override
    @Nullable
    public FunicExpress getExpress() {
        return findChildByClass(FunicExpress.class);
    }

    @Override
    @NotNull
    public List<FunicSegment> getSegmentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicSegment.class);
    }

}
