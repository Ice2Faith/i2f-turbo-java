// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlGlobalFunctionCall;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlStaticFunctionCall;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlTypeReference;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;

public class OgnlStaticFunctionCallImpl extends OgnlPsiElement implements OgnlStaticFunctionCall {

    public OgnlStaticFunctionCallImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitStaticFunctionCall(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public OgnlGlobalFunctionCall getGlobalFunctionCall() {
        return findNotNullChildByClass(OgnlGlobalFunctionCall.class);
    }

    @Override
    @NotNull
    public OgnlTypeReference getTypeReference() {
        return findNotNullChildByClass(OgnlTypeReference.class);
    }

}
