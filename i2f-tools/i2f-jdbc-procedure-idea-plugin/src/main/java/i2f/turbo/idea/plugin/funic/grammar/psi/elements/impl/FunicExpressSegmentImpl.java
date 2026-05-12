// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.impl.FunicPsiImplUtil;

public class FunicExpressSegmentImpl extends FunicPsiElement implements FunicExpressSegment {

    public FunicExpressSegmentImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitExpressSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicAwaitExpress getAwaitExpress() {
        return findChildByClass(FunicAwaitExpress.class);
    }

    @Override
    @Nullable
    public FunicBreakExpress getBreakExpress() {
        return findChildByClass(FunicBreakExpress.class);
    }

    @Override
    @Nullable
    public FunicCircleExpress getCircleExpress() {
        return findChildByClass(FunicCircleExpress.class);
    }

    @Override
    @Nullable
    public FunicContinueExpress getContinueExpress() {
        return findChildByClass(FunicContinueExpress.class);
    }

    @Override
    @Nullable
    public FunicDebuggerExpress getDebuggerExpress() {
        return findChildByClass(FunicDebuggerExpress.class);
    }

    @Override
    @Nullable
    public FunicDoWhileExpress getDoWhileExpress() {
        return findChildByClass(FunicDoWhileExpress.class);
    }

    @Override
    @Nullable
    public FunicExpress getExpress() {
        return findChildByClass(FunicExpress.class);
    }

    @Override
    @Nullable
    public FunicExtractExpress getExtractExpress() {
        return findChildByClass(FunicExtractExpress.class);
    }

    @Override
    @Nullable
    public FunicForLoopExpress getForLoopExpress() {
        return findChildByClass(FunicForLoopExpress.class);
    }

    @Override
    @Nullable
    public FunicForRangeExpress getForRangeExpress() {
        return findChildByClass(FunicForRangeExpress.class);
    }

    @Override
    @Nullable
    public FunicForeachExpress getForeachExpress() {
        return findChildByClass(FunicForeachExpress.class);
    }

    @Override
    @Nullable
    public FunicFunctionDeclareExpress getFunctionDeclareExpress() {
        return findChildByClass(FunicFunctionDeclareExpress.class);
    }

    @Override
    @Nullable
    public FunicGlobalFunctionCall getGlobalFunctionCall() {
        return findChildByClass(FunicGlobalFunctionCall.class);
    }

    @Override
    @Nullable
    public FunicGoRunExpress getGoRunExpress() {
        return findChildByClass(FunicGoRunExpress.class);
    }

    @Override
    @Nullable
    public FunicIfElseExpress getIfElseExpress() {
        return findChildByClass(FunicIfElseExpress.class);
    }

    @Override
    @Nullable
    public FunicImportExpress getImportExpress() {
        return findChildByClass(FunicImportExpress.class);
    }

    @Override
    @Nullable
    public FunicIncrDecrPrefixOperatorPart getIncrDecrPrefixOperatorPart() {
        return findChildByClass(FunicIncrDecrPrefixOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicLambdaExpress getLambdaExpress() {
        return findChildByClass(FunicLambdaExpress.class);
    }

    @Override
    @Nullable
    public FunicListValueExpress getListValueExpress() {
        return findChildByClass(FunicListValueExpress.class);
    }

    @Override
    @Nullable
    public FunicMapValueExpress getMapValueExpress() {
        return findChildByClass(FunicMapValueExpress.class);
    }

    @Override
    @Nullable
    public FunicNewArrayExpress getNewArrayExpress() {
        return findChildByClass(FunicNewArrayExpress.class);
    }

    @Override
    @Nullable
    public FunicNewInstanceExpress getNewInstanceExpress() {
        return findChildByClass(FunicNewInstanceExpress.class);
    }

    @Override
    @Nullable
    public FunicPrefixOperatorPart getPrefixOperatorPart() {
        return findChildByClass(FunicPrefixOperatorPart.class);
    }

    @Override
    @Nullable
    public FunicReturnExpress getReturnExpress() {
        return findChildByClass(FunicReturnExpress.class);
    }

    @Override
    @Nullable
    public FunicScriptBlock getScriptBlock() {
        return findChildByClass(FunicScriptBlock.class);
    }

    @Override
    @Nullable
    public FunicStaticFieldValue getStaticFieldValue() {
        return findChildByClass(FunicStaticFieldValue.class);
    }

    @Override
    @Nullable
    public FunicStaticFunctionCall getStaticFunctionCall() {
        return findChildByClass(FunicStaticFunctionCall.class);
    }

    @Override
    @Nullable
    public FunicSynchronizedExpress getSynchronizedExpress() {
        return findChildByClass(FunicSynchronizedExpress.class);
    }

    @Override
    @Nullable
    public FunicThrowExpress getThrowExpress() {
        return findChildByClass(FunicThrowExpress.class);
    }

    @Override
    @Nullable
    public FunicTryCatchFinallyExpress getTryCatchFinallyExpress() {
        return findChildByClass(FunicTryCatchFinallyExpress.class);
    }

    @Override
    @Nullable
    public FunicValueSegment getValueSegment() {
        return findChildByClass(FunicValueSegment.class);
    }

    @Override
    @Nullable
    public FunicWhileExpress getWhileExpress() {
        return findChildByClass(FunicWhileExpress.class);
    }

}
