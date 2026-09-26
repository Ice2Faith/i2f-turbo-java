// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlMapCollectionExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlMapPairs;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlTypeReference;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OgnlMapCollectionExpressImpl extends OgnlPsiElement implements OgnlMapCollectionExpress {

    public OgnlMapCollectionExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitMapCollectionExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public OgnlMapPairs getMapPairs() {
        return findChildByClass(OgnlMapPairs.class);
    }

    @Override
    @Nullable
    public OgnlTypeReference getTypeReference() {
        return findChildByClass(OgnlTypeReference.class);
    }

}
