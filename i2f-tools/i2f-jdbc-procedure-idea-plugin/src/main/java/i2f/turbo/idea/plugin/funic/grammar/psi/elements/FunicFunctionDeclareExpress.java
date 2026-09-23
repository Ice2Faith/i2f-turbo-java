// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FunicFunctionDeclareExpress extends PsiElement {

    @NotNull
    FunicFunctionDeclareParameters getFunctionDeclareParameters();

    @Nullable
    FunicFunctionDeclareReturn getFunctionDeclareReturn();

    @NotNull
    FunicScriptBlock getScriptBlock();

    @NotNull
    PsiElement getIdentifier();

}
