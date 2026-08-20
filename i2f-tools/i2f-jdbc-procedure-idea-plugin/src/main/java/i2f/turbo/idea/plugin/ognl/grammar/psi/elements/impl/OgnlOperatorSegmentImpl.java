// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.ognl.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.ognl.lang.psi.OgnlPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OgnlOperatorSegmentImpl extends OgnlPsiElement implements OgnlOperatorSegment {

    public OgnlOperatorSegmentImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull OgnlVisitor visitor) {
        visitor.visitOperatorSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof OgnlVisitor) accept((OgnlVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public OgnlAssignRightPart getAssignRightPart() {
        return findChildByClass(OgnlAssignRightPart.class);
    }

    @Override
    @Nullable
    public OgnlBitOperatorPart getBitOperatorPart() {
        return findChildByClass(OgnlBitOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlChainSubExpress getChainSubExpress() {
        return findChildByClass(OgnlChainSubExpress.class);
    }

    @Override
    @NotNull
    public List<OgnlCommaExpressRightPart> getCommaExpressRightPartList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, OgnlCommaExpressRightPart.class);
    }

    @Override
    @Nullable
    public OgnlCompareOperatorPart getCompareOperatorPart() {
        return findChildByClass(OgnlCompareOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlExpress getExpress() {
        return findChildByClass(OgnlExpress.class);
    }

    @Override
    @Nullable
    public OgnlInstanceFunctionCallRightPart getInstanceFunctionCallRightPart() {
        return findChildByClass(OgnlInstanceFunctionCallRightPart.class);
    }

    @Override
    @Nullable
    public OgnlLogicalLinkHighOperatorPart getLogicalLinkHighOperatorPart() {
        return findChildByClass(OgnlLogicalLinkHighOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlLogicalLinkLowOperatorPart getLogicalLinkLowOperatorPart() {
        return findChildByClass(OgnlLogicalLinkLowOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlMathAddSubOperatorPart getMathAddSubOperatorPart() {
        return findChildByClass(OgnlMathAddSubOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlMathMulDivOperatorPart getMathMulDivOperatorPart() {
        return findChildByClass(OgnlMathMulDivOperatorPart.class);
    }

    @Override
    @Nullable
    public OgnlProjectingAcrossCollectionExpress getProjectingAcrossCollectionExpress() {
        return findChildByClass(OgnlProjectingAcrossCollectionExpress.class);
    }

    @Override
    @Nullable
    public OgnlPropertyExpress getPropertyExpress() {
        return findChildByClass(OgnlPropertyExpress.class);
    }

    @Override
    @Nullable
    public OgnlSelectFromCollectionExpress getSelectFromCollectionExpress() {
        return findChildByClass(OgnlSelectFromCollectionExpress.class);
    }

    @Override
    @Nullable
    public OgnlSquareExpress getSquareExpress() {
        return findChildByClass(OgnlSquareExpress.class);
    }

    @Override
    @Nullable
    public OgnlThirdOperateRightPart getThirdOperateRightPart() {
        return findChildByClass(OgnlThirdOperateRightPart.class);
    }

}
