// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FunicExpressSegment extends PsiElement {

    @Nullable
    FunicAwaitExpress getAwaitExpress();

    @Nullable
    FunicBreakExpress getBreakExpress();

    @Nullable
    FunicCircleExpress getCircleExpress();

    @Nullable
    FunicContinueExpress getContinueExpress();

    @Nullable
    FunicDebuggerExpress getDebuggerExpress();

    @Nullable
    FunicDoWhileExpress getDoWhileExpress();

    @Nullable
    FunicExpress getExpress();

    @Nullable
    FunicExtractExpress getExtractExpress();

    @Nullable
    FunicForLoopExpress getForLoopExpress();

    @Nullable
    FunicForRangeExpress getForRangeExpress();

    @Nullable
    FunicForeachExpress getForeachExpress();

    @Nullable
    FunicFunctionDeclareExpress getFunctionDeclareExpress();

    @Nullable
    FunicGlobalFunctionCall getGlobalFunctionCall();

    @Nullable
    FunicGoRunExpress getGoRunExpress();

    @Nullable
    FunicIfElseExpress getIfElseExpress();

    @Nullable
    FunicImportExpress getImportExpress();

    @Nullable
    FunicIncrDecrPrefixOperatorPart getIncrDecrPrefixOperatorPart();

    @Nullable
    FunicLambdaExpress getLambdaExpress();

    @Nullable
    FunicListValueExpress getListValueExpress();

    @Nullable
    FunicMapValueExpress getMapValueExpress();

    @Nullable
    FunicNewArrayExpress getNewArrayExpress();

    @Nullable
    FunicNewInstanceExpress getNewInstanceExpress();

    @Nullable
    FunicPrefixOperatorPart getPrefixOperatorPart();

    @Nullable
    FunicReturnExpress getReturnExpress();

    @Nullable
    FunicScriptBlock getScriptBlock();

    @Nullable
    FunicStaticFieldValue getStaticFieldValue();

    @Nullable
    FunicStaticFunctionCall getStaticFunctionCall();

    @Nullable
    FunicSynchronizedExpress getSynchronizedExpress();

    @Nullable
    FunicThrowExpress getThrowExpress();

    @Nullable
    FunicTryCatchFinallyExpress getTryCatchFinallyExpress();

    @Nullable
    FunicValueSegment getValueSegment();

    @Nullable
    FunicWhileExpress getWhileExpress();

}
