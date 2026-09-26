// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlFullName;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlFunctionArguments;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlNewInstanceExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;

public class OgnlNewInstanceExpressImpl extends OgnlPsiElement implements OgnlNewInstanceExpress {

    public OgnlNewInstanceExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitNewInstanceExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public OgnlFullName getFullName() {
        return findNotNullChildByClass(OgnlFullName.class);
    }

    @Override
    @NotNull
    public OgnlFunctionArguments getFunctionArguments() {
        return findNotNullChildByClass(OgnlFunctionArguments.class);
    }

}
