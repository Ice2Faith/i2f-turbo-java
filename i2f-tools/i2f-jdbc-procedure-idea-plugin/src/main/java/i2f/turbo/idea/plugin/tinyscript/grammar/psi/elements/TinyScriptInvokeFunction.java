// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TinyScriptInvokeFunction extends PsiElement {

    @NotNull
    List<TinyScriptFunctionCall> getFunctionCallList();

    @Nullable
    TinyScriptRefCall getRefCall();

}
