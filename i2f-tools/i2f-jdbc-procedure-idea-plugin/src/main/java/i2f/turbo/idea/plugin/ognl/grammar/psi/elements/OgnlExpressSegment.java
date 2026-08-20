// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.ognl.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface OgnlExpressSegment extends PsiElement {

    @Nullable
    OgnlCircleExpress getCircleExpress();

    @Nullable
    OgnlConstExpress getConstExpress();

    @Nullable
    OgnlEvaluateExpress getEvaluateExpress();

    @Nullable
    OgnlExpress getExpress();

    @Nullable
    OgnlGlobalFunctionCall getGlobalFunctionCall();

    @Nullable
    OgnlListCollectionExpress getListCollectionExpress();

    @Nullable
    OgnlMapCollectionExpress getMapCollectionExpress();

    @Nullable
    OgnlNewArrayExpress getNewArrayExpress();

    @Nullable
    OgnlNewInstanceExpress getNewInstanceExpress();

    @Nullable
    OgnlPrefixOperatorPart getPrefixOperatorPart();

    @Nullable
    OgnlPseudoLambdaExpress getPseudoLambdaExpress();

    @Nullable
    OgnlReferenceVariableExpress getReferenceVariableExpress();

    @Nullable
    OgnlStaticFunctionCall getStaticFunctionCall();

    @Nullable
    OgnlStaticPropertyExpress getStaticPropertyExpress();

    @Nullable
    OgnlVariableExpress getVariableExpress();

}
