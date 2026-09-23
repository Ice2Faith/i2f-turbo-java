// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlPseudoLambdaExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;

public class OgnlPseudoLambdaExpressImpl extends OgnlPsiElement implements OgnlPseudoLambdaExpress {

    public OgnlPseudoLambdaExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitPseudoLambdaExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public OgnlExpress getExpress() {
        return findNotNullChildByClass(OgnlExpress.class);
    }

}
