// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FunviIfBlock extends PsiElement {

    @NotNull
    FunviBlockBody getBlockBody();

    @NotNull
    List<FunviElseBlock> getElseBlockList();

    @Nullable
    FunviParameters getParameters();

    @NotNull
    PsiElement getTermBlockEnd();

    @NotNull
    PsiElement getTermBlockIf();

}
