// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptScriptBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptTryBodyBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

public class TinyScriptTryBodyBlockImpl extends TinyScriptPsiElement implements TinyScriptTryBodyBlock {

    public TinyScriptTryBodyBlockImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitTryBodyBlock(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public TinyScriptScriptBlock getScriptBlock() {
        return findNotNullChildByClass(TinyScriptScriptBlock.class);
    }

}
