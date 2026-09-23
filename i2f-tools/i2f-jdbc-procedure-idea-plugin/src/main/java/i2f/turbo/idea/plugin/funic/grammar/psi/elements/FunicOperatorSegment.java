// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FunicOperatorSegment extends PsiElement {

    @Nullable
    FunicAssignRightPart getAssignRightPart();

    @Nullable
    FunicBitOperatorPart getBitOperatorPart();

    @Nullable
    FunicCastAsRightPart getCastAsRightPart();

    @Nullable
    FunicCompareOperatorPart getCompareOperatorPart();

    @Nullable
    FunicExpress getExpress();

    @Nullable
    FunicFactorRightPart getFactorRightPart();

    @Nullable
    FunicIncrDecrAfterRightPart getIncrDecrAfterRightPart();

    @Nullable
    FunicInstanceFieldValueRightPart getInstanceFieldValueRightPart();

    @Nullable
    FunicInstanceFunctionCallRightPart getInstanceFunctionCallRightPart();

    @Nullable
    FunicLogicalLinkOperatorPart getLogicalLinkOperatorPart();

    @Nullable
    FunicMathAddSubOperatorPart getMathAddSubOperatorPart();

    @Nullable
    FunicMathMulDivOperatorPart getMathMulDivOperatorPart();

    @Nullable
    FunicPercentRightPart getPercentRightPart();

    @NotNull
    List<FunicPipelineFunctionExpress> getPipelineFunctionExpressList();

    @Nullable
    FunicSquareQuoteRightPart getSquareQuoteRightPart();

    @Nullable
    FunicThirdOperateRightPart getThirdOperateRightPart();

}
