// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptCommentSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.TERM_COMMENT_MULTI_LINE;
import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.TERM_COMMENT_SINGLE_LINE;

public class TinyScriptCommentSegmentImpl extends TinyScriptPsiElement implements TinyScriptCommentSegment {

    public TinyScriptCommentSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitCommentSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermCommentMultiLine() {
        return findChildByType(TERM_COMMENT_MULTI_LINE);
    }

    @Override
    @Nullable
    public PsiElement getTermCommentSingleLine() {
        return findChildByType(TERM_COMMENT_SINGLE_LINE);
    }

}
