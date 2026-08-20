// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVariableExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;

import static i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes.IDENTIFIER;

public class OgnlVariableExpressImpl extends OgnlPsiElement implements OgnlVariableExpress {

    public OgnlVariableExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitVariableExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public PsiElement getIdentifier() {
        return findNotNullChildByType(IDENTIFIER);
    }

}
