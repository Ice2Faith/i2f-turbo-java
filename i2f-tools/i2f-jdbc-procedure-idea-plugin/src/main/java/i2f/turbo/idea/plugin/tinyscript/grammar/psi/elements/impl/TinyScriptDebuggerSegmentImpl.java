// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConditionBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptDebuggerSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptNamingBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TinyScriptDebuggerSegmentImpl extends TinyScriptPsiElement implements TinyScriptDebuggerSegment {

    public TinyScriptDebuggerSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitDebuggerSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public TinyScriptConditionBlock getConditionBlock() {
        return findChildByClass(TinyScriptConditionBlock.class);
    }

    @Override
    @Nullable
    public TinyScriptNamingBlock getNamingBlock() {
        return findChildByClass(TinyScriptNamingBlock.class);
    }

}
