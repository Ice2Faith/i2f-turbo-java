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

public class OgnlConstFloatImpl extends OgnlPsiElement implements OgnlConstFloat {

    public OgnlConstFloatImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitConstFloat(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberFloat() {
        return findChildByType(TERM_CONST_NUMBER_FLOAT);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberScien1() {
        return findChildByType(TERM_CONST_NUMBER_SCIEN_1);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberScien2() {
        return findChildByType(TERM_CONST_NUMBER_SCIEN_2);
    }

}
