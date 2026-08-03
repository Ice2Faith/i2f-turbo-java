// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
