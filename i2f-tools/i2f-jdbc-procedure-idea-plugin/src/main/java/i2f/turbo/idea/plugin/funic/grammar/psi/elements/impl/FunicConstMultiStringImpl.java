// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.impl.FunicPsiImplUtil;

public class FunicConstMultiStringImpl extends FunicPsiElement implements FunicConstMultiString {

    public FunicConstMultiStringImpl(ASTNode node) {
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
