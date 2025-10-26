// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TinyScriptExpressSegment extends PsiElement {

  @Nullable
  TinyScriptConstValue getConstValue();

  @Nullable
  TinyScriptControlSegment getControlSegment();

  @Nullable
  TinyScriptDebuggerSegment getDebuggerSegment();

  @Nullable
  TinyScriptDeclareFunction getDeclareFunction();

  @Nullable
  TinyScriptEqualValue getEqualValue();

  @Nullable
  TinyScriptForSegment getForSegment();

  @Nullable
  TinyScriptForeachSegment getForeachSegment();

  @Nullable
  TinyScriptIfSegment getIfSegment();

  @Nullable
  TinyScriptInvokeFunction getInvokeFunction();

  @Nullable
  TinyScriptJsonValue getJsonValue();

  @Nullable
  TinyScriptNegtiveSegment getNegtiveSegment();

  @Nullable
  TinyScriptNewInstance getNewInstance();

  @Nullable
  TinyScriptParenSegment getParenSegment();

  @Nullable
  TinyScriptPrefixOperatorSegment getPrefixOperatorSegment();

  @Nullable
  TinyScriptRefValue getRefValue();

  @Nullable
  TinyScriptStaticEnumValue getStaticEnumValue();

  @Nullable
  TinyScriptThrowSegment getThrowSegment();

  @Nullable
  TinyScriptTrySegment getTrySegment();

  @Nullable
  TinyScriptWhileSegment getWhileSegment();

}
