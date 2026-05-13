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

public class TinyScriptTrySegmentImpl extends TinyScriptPsiElement implements TinyScriptTrySegment {

    public TinyScriptTrySegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitTrySegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<TinyScriptCatchBodyBlock> getCatchBodyBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptCatchBodyBlock.class);
    }

    @Override
    @NotNull
    public List<TinyScriptClassNameBlock> getClassNameBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptClassNameBlock.class);
    }

    @Override
    @Nullable
    public TinyScriptFinallyBodyBlock getFinallyBodyBlock() {
        return findChildByClass(TinyScriptFinallyBodyBlock.class);
    }

    @Override
    @NotNull
    public List<TinyScriptNamingBlock> getNamingBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptNamingBlock.class);
    }

    @Override
    @NotNull
    public TinyScriptTryBodyBlock getTryBodyBlock() {
        return findNotNullChildByClass(TinyScriptTryBodyBlock.class);
    }

}
