// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes.*;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviPsiElement;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funvi.lang.psi.impl.FunviPsiImplUtil;

public class FunviTextSegmentImpl extends FunviPsiElement implements FunviTextSegment {

    public FunviTextSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunviVisitor visitor) {
        visitor.visitTextSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunviVisitor) accept((FunviVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunviContent> getContentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunviContent.class);
    }

}
