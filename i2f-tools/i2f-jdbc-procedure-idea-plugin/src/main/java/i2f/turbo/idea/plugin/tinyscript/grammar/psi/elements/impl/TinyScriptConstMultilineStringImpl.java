// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConstMultilineString;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.TERM_CONST_STRING_MULTILINE;
import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.TERM_CONST_STRING_MULTILINE_QUOTE;

public class TinyScriptConstMultilineStringImpl extends TinyScriptPsiElement implements TinyScriptConstMultilineString {

    public TinyScriptConstMultilineStringImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitConstMultilineString(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
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
