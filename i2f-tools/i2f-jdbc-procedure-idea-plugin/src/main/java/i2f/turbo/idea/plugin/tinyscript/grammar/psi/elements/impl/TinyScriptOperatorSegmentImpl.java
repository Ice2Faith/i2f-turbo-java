// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptExpress;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptOperatorSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptPipelineFunctionSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TinyScriptOperatorSegmentImpl extends TinyScriptPsiElement implements TinyScriptOperatorSegment {

    public TinyScriptOperatorSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitOperatorSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public TinyScriptExpress getExpress() {
        return findChildByClass(TinyScriptExpress.class);
    }

    @Override
    @NotNull
    public List<TinyScriptPipelineFunctionSegment> getPipelineFunctionSegmentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptPipelineFunctionSegment.class);
    }

}
