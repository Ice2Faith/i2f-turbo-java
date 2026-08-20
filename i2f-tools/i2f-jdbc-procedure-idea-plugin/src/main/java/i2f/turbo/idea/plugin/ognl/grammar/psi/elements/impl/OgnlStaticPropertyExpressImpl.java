// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlStaticPropertyExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlTypeReference;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;

import static i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes.IDENTIFIER;

public class OgnlStaticPropertyExpressImpl extends OgnlPsiElement implements OgnlStaticPropertyExpress {

    public OgnlStaticPropertyExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitStaticPropertyExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public OgnlTypeReference getTypeReference() {
        return findNotNullChildByClass(OgnlTypeReference.class);
    }

    @Override
    @NotNull
    public PsiElement getIdentifier() {
        return findNotNullChildByType(IDENTIFIER);
    }

}
