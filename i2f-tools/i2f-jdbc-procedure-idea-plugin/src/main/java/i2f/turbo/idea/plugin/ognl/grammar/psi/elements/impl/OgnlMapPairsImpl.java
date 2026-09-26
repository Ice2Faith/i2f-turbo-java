// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlMapPair;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlMapPairs;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OgnlMapPairsImpl extends OgnlPsiElement implements OgnlMapPairs {

    public OgnlMapPairsImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitMapPairs(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<OgnlMapPair> getMapPairList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, OgnlMapPair.class);
    }

}
