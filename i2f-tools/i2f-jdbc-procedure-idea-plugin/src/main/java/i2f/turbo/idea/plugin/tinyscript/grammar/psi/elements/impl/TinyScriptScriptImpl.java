// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TinyScriptScriptImpl extends TinyScriptPsiElement implements TinyScriptScript {

    public TinyScriptScriptImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitScript(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public TinyScriptCommentSegment getCommentSegment() {
        return findChildByClass(TinyScriptCommentSegment.class);
    }

    @Override
    @Nullable
    public TinyScriptExpress getExpress() {
        return findChildByClass(TinyScriptExpress.class);
    }

    @Override
    @NotNull
    public List<TinyScriptSegment> getSegmentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptSegment.class);
    }

}
