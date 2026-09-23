// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlConstString;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlMapPair;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes.IDENTIFIER;

public class OgnlMapPairImpl extends OgnlPsiElement implements OgnlMapPair {

    public OgnlMapPairImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitMapPair(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public OgnlConstString getConstString() {
        return findChildByClass(OgnlConstString.class);
    }

    @Override
    @NotNull
    public OgnlExpress getExpress() {
        return findNotNullChildByClass(OgnlExpress.class);
    }

    @Override
    @Nullable
    public PsiElement getIdentifier() {
        return findChildByType(IDENTIFIER);
    }

}
