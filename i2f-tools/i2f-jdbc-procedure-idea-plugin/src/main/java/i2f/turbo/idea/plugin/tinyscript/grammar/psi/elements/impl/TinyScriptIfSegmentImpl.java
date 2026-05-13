// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConditionBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptIfSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptScriptBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TinyScriptIfSegmentImpl extends TinyScriptPsiElement implements TinyScriptIfSegment {

    public TinyScriptIfSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitIfSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<TinyScriptConditionBlock> getConditionBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptConditionBlock.class);
    }

    @Override
    @NotNull
    public List<TinyScriptScriptBlock> getScriptBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptScriptBlock.class);
    }

}
