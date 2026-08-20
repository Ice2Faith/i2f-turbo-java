// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes.*;

import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.ognl.lang.psi.impl.OgnlPsiImplUtil;

public class OgnlChainSubExpressImpl extends OgnlPsiElement implements OgnlChainSubExpress {

    public OgnlChainSubExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitChainSubExpress(this);
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
