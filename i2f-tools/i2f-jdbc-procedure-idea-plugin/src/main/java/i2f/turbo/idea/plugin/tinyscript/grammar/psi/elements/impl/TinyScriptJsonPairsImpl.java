// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptJsonPair;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptJsonPairs;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TinyScriptJsonPairsImpl extends TinyScriptPsiElement implements TinyScriptJsonPairs {

    public TinyScriptJsonPairsImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitJsonPairs(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<TinyScriptJsonPair> getJsonPairList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptJsonPair.class);
    }

}
