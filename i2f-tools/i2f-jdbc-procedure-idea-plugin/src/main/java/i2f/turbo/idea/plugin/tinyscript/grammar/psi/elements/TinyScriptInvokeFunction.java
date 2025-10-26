// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TinyScriptInvokeFunction extends PsiElement {

  @NotNull
  List<TinyScriptFunctionCall> getFunctionCallList();

  @Nullable
  TinyScriptRefCall getRefCall();

}
