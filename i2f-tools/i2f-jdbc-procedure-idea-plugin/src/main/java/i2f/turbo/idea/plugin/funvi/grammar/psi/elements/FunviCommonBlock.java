// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

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
