// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface OgnlOperatorSegment extends PsiElement {

    @Nullable
    OgnlAssignRightPart getAssignRightPart();

    @Nullable
    OgnlBitOperatorPart getBitOperatorPart();

    @Nullable
    OgnlChainSubExpress getChainSubExpress();

    @NotNull
    List<OgnlCommaExpressRightPart> getCommaExpressRightPartList();

    @Nullable
    OgnlCompareOperatorPart getCompareOperatorPart();

    @Nullable
    OgnlExpress getExpress();

    @Nullable
    OgnlInstanceFunctionCallRightPart getInstanceFunctionCallRightPart();

    @Nullable
    OgnlLogicalLinkHighOperatorPart getLogicalLinkHighOperatorPart();

    @Nullable
    OgnlLogicalLinkLowOperatorPart getLogicalLinkLowOperatorPart();

    @Nullable
    OgnlMathAddSubOperatorPart getMathAddSubOperatorPart();

    @Nullable
    OgnlMathMulDivOperatorPart getMathMulDivOperatorPart();

    @Nullable
    OgnlProjectingAcrossCollectionExpress getProjectingAcrossCollectionExpress();

    @Nullable
    OgnlPropertyExpress getPropertyExpress();

    @Nullable
    OgnlSelectFromCollectionExpress getSelectFromCollectionExpress();

    @Nullable
    OgnlSquareExpress getSquareExpress();

    @Nullable
    OgnlThirdOperateRightPart getThirdOperateRightPart();

}
