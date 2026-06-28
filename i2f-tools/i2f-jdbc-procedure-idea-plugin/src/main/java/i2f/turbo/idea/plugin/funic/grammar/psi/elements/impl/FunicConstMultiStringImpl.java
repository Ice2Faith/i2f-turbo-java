// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicConstMultiString;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_CONST_STRING_MULTILINE;
import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.TERM_CONST_STRING_MULTILINE_QUOTE;

public class FunicConstMultiStringImpl extends FunicPsiElement implements FunicConstMultiString {

    public FunicConstMultiStringImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitConstMultiString(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermConstStringMultiline() {
        return findChildByType(TERM_CONST_STRING_MULTILINE);
    }

    @Override
    @Nullable
    public PsiElement getTermConstStringMultilineQuote() {
        return findChildByType(TERM_CONST_STRING_MULTILINE_QUOTE);
    }

}
