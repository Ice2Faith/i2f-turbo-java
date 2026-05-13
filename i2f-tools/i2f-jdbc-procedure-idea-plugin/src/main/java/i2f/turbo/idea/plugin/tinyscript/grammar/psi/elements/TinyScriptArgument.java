// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TinyScriptArgument extends PsiElement {

    @NotNull
    TinyScriptArgumentValue getArgumentValue();

    @Nullable
    TinyScriptConstString getConstString();

    @Nullable
    PsiElement getNaming();

}
