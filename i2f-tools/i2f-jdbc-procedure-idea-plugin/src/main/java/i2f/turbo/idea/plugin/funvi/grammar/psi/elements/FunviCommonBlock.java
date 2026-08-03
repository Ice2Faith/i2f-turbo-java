// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FunviCommonBlock extends PsiElement {

    @Nullable
    FunviBlockBody getBlockBody();

    @NotNull
    FunviBlockHeadSegment getBlockHeadSegment();

    @Nullable
    FunviParameters getParameters();

    @Nullable
    PsiElement getTermBlockEnd();

}
