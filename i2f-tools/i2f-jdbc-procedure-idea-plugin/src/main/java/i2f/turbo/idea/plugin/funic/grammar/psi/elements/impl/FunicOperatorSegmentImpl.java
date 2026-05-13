// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunicOperatorSegmentImpl extends FunicPsiElement implements FunicOperatorSegment {

    public FunicOperatorSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitOperatorSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicAssignRightPart getAssignRightPart() {
        return findChildByClass(FunicAssignRightPart.class);
    }

    @Override
    @Nullable
    public FunicBitOperatorPart getBitOperatorPart() {
        return findChildByClass(FunicBitOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicCastAsRightPart getCastAsRightPart() {
        return findChildByClass(FunicCastAsRightPart.class);
    }

    @Override
    @Nullable
    public FunicCompareOperatorPart getCompareOperatorPart() {
        return findChildByClass(FunicCompareOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicExpress getExpress() {
        return findChildByClass(FunicExpress.class);
    }

    @Override
    @Nullable
    public FunicFactorRightPart getFactorRightPart() {
        return findChildByClass(FunicFactorRightPart.class);
    }

    @Override
    @Nullable
    public FunicIncrDecrAfterRightPart getIncrDecrAfterRightPart() {
        return findChildByClass(FunicIncrDecrAfterRightPart.class);
    }

    @Override
    @Nullable
    public FunicInstanceFieldValueRightPart getInstanceFieldValueRightPart() {
        return findChildByClass(FunicInstanceFieldValueRightPart.class);
    }

    @Override
    @Nullable
    public FunicInstanceFunctionCallRightPart getInstanceFunctionCallRightPart() {
        return findChildByClass(FunicInstanceFunctionCallRightPart.class);
    }

    @Override
    @Nullable
    public FunicLogicalLinkOperatorPart getLogicalLinkOperatorPart() {
        return findChildByClass(FunicLogicalLinkOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicMathAddSubOperatorPart getMathAddSubOperatorPart() {
        return findChildByClass(FunicMathAddSubOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicMathMulDivOperatorPart getMathMulDivOperatorPart() {
        return findChildByClass(FunicMathMulDivOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicPercentRightPart getPercentRightPart() {
        return findChildByClass(FunicPercentRightPart.class);
    }

    @Override
    @NotNull
    public List<FunicPipelineFunctionExpress> getPipelineFunctionExpressList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicPipelineFunctionExpress.class);
    }

    @Override
    @Nullable
    public FunicSquareQuoteRightPart getSquareQuoteRightPart() {
        return findChildByClass(FunicSquareQuoteRightPart.class);
    }

    @Override
    @Nullable
    public FunicThirdOperateRightPart getThirdOperateRightPart() {
        return findChildByClass(FunicThirdOperateRightPart.class);
    }

}
