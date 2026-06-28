// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstRenderString;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_CONST_STRING_RENDER;
import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_CONST_STRING_RENDER_SINGLE;

public class FunicConstRenderStringImpl extends FunicPsiElement implements FunicConstRenderString {

    public FunicConstRenderStringImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstRenderString(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermConstStringRender() {
        return findChildByType(TERM_CONST_STRING_RENDER);
    }

    @Override
    @Nullable
    public PsiElement getTermConstStringRenderSingle() {
        return findChildByType(TERM_CONST_STRING_RENDER_SINGLE);
    }

}
