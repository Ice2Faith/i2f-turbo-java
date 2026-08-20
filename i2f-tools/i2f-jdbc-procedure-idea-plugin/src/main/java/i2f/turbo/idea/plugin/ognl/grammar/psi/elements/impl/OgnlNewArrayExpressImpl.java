// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OgnlNewArrayExpressImpl extends OgnlPsiElement implements OgnlNewArrayExpress {

    public OgnlNewArrayExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitNewArrayExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public OgnlConstNumber getConstNumber() {
        return findChildByClass(OgnlConstNumber.class);
    }

    @Override
    @NotNull
    public OgnlFullName getFullName() {
        return findNotNullChildByClass(OgnlFullName.class);
    }

    @Override
    @Nullable
    public OgnlListCollectionExpress getListCollectionExpress() {
        return findChildByClass(OgnlListCollectionExpress.class);
    }

}
