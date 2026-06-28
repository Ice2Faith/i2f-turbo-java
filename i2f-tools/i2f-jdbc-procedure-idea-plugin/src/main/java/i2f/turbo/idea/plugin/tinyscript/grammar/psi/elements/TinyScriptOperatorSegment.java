// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TinyScriptOperatorSegment extends PsiElement {

    @Nullable
    TinyScriptCastInstanceOfRightPart getCastInstanceOfRightPart();

    @Nullable
    TinyScriptCompareOperatorPart getCompareOperatorPart();

    @Nullable
    TinyScriptExpress getExpress();

    @Nullable
    TinyScriptLogicalLinkOperatorPart getLogicalLinkOperatorPart();

    @Nullable
    TinyScriptMathAddSubOperatorPart getMathAddSubOperatorPart();

    @Nullable
    TinyScriptMathMulDivOperatorPart getMathMulDivOperatorPart();

    @Nullable
    TinyScriptPercentRightPart getPercentRightPart();

    @NotNull
    List<TinyScriptPipelineFunctionSegment> getPipelineFunctionSegmentList();

    @Nullable
    TinyScriptSquareQuoteRightPart getSquareQuoteRightPart();

    @Nullable
    TinyScriptThirdOperateRightPart getThirdOperateRightPart();

}
