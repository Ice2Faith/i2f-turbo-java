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

public class OgnlExpressSegmentImpl extends OgnlPsiElement implements OgnlExpressSegment {

    public OgnlExpressSegmentImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitExpressSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public OgnlCircleExpress getCircleExpress() {
        return findChildByClass(OgnlCircleExpress.class);
    }

    @Override
    @Nullable
    public OgnlConstExpress getConstExpress() {
        return findChildByClass(OgnlConstExpress.class);
    }

    @Override
    @Nullable
    public OgnlEvaluateExpress getEvaluateExpress() {
        return findChildByClass(OgnlEvaluateExpress.class);
    }

    @Override
    @Nullable
    public OgnlExpress getExpress() {
        return findChildByClass(OgnlExpress.class);
    }

    @Override
    @Nullable
    public OgnlGlobalFunctionCall getGlobalFunctionCall() {
        return findChildByClass(OgnlGlobalFunctionCall.class);
    }

    @Override
    @Nullable
    public OgnlListCollectionExpress getListCollectionExpress() {
        return findChildByClass(OgnlListCollectionExpress.class);
    }

    @Override
    @Nullable
    public OgnlMapCollectionExpress getMapCollectionExpress() {
        return findChildByClass(OgnlMapCollectionExpress.class);
    }

    @Override
    @Nullable
    public OgnlNewArrayExpress getNewArrayExpress() {
        return findChildByClass(OgnlNewArrayExpress.class);
    }

    @Override
    @Nullable
    public OgnlNewInstanceExpress getNewInstanceExpress() {
        return findChildByClass(OgnlNewInstanceExpress.class);
    }

    @Override
    @Nullable
    public OgnlPrefixOperatorPart getPrefixOperatorPart() {
        return findChildByClass(OgnlPrefixOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlPseudoLambdaExpress getPseudoLambdaExpress() {
        return findChildByClass(OgnlPseudoLambdaExpress.class);
    }

    @Override
    @Nullable
    public OgnlReferenceVariableExpress getReferenceVariableExpress() {
        return findChildByClass(OgnlReferenceVariableExpress.class);
    }

    @Override
    @Nullable
    public OgnlStaticFunctionCall getStaticFunctionCall() {
        return findChildByClass(OgnlStaticFunctionCall.class);
    }

    @Override
    @Nullable
    public OgnlStaticPropertyExpress getStaticPropertyExpress() {
        return findChildByClass(OgnlStaticPropertyExpress.class);
    }

    @Override
    @Nullable
    public OgnlVariableExpress getVariableExpress() {
        return findChildByClass(OgnlVariableExpress.class);
    }

}
