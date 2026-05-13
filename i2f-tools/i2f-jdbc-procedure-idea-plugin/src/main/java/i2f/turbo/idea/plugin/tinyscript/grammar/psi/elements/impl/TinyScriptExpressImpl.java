// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptExpress;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptExpressSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptOperatorSegment;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TinyScriptExpressImpl extends TinyScriptPsiElement implements TinyScriptExpress {

    public TinyScriptExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public TinyScriptExpressSegment getExpressSegment() {
        return findNotNullChildByClass(TinyScriptExpressSegment.class);
    }

    @Override
    @NotNull
    public List<TinyScriptOperatorSegment> getOperatorSegmentList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptOperatorSegment.class);
    }

}
