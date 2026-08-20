// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlCommaExpressRightPart;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlListCollectionExpress;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.OgnlVisitor;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OgnlListCollectionExpressImpl extends OgnlPsiElement implements OgnlListCollectionExpress {

    public OgnlListCollectionExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitListCollectionExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<OgnlCommaExpressRightPart> getCommaExpressRightPartList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, OgnlCommaExpressRightPart.class);
    }

    @Override
    @Nullable
    public OgnlExpress getExpress() {
        return findChildByClass(OgnlExpress.class);
    }

}
