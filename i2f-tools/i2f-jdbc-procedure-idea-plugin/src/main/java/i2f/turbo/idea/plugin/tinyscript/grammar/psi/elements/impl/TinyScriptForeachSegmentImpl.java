// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

public class TinyScriptForeachSegmentImpl extends TinyScriptPsiElement implements TinyScriptForeachSegment {

    public TinyScriptForeachSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitForeachSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public TinyScriptExpress getExpress() {
        return findNotNullChildByClass(TinyScriptExpress.class);
    }

    @Override
    @NotNull
    public TinyScriptNamingBlock getNamingBlock() {
        return findNotNullChildByClass(TinyScriptNamingBlock.class);
    }

    @Override
    @NotNull
    public TinyScriptScriptBlock getScriptBlock() {
        return findNotNullChildByClass(TinyScriptScriptBlock.class);
    }

}
