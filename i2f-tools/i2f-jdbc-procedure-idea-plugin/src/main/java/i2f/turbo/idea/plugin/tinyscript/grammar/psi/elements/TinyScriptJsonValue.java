// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TinyScriptJsonValue extends PsiElement {

  @Nullable
  TinyScriptConstValue getConstValue();

  @Nullable
  TinyScriptInvokeFunction getInvokeFunction();

  @Nullable
  TinyScriptJsonArrayValue getJsonArrayValue();

  @Nullable
  TinyScriptJsonMapValue getJsonMapValue();

  @Nullable
  TinyScriptRefValue getRefValue();

}
