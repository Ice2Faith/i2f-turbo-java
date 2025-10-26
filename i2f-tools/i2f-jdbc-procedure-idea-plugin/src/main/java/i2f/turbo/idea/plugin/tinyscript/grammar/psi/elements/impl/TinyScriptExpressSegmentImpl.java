// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.*;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.impl.TinyScriptPsiImplUtil;

public class TinyScriptExpressSegmentImpl extends TinyScriptPsiElement implements TinyScriptExpressSegment {

  public TinyScriptExpressSegmentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TinyScriptVisitor visitor) {
    visitor.visitExpressSegment(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TinyScriptConstValue getConstValue() {
    return findChildByClass(TinyScriptConstValue.class);
  }

  @Override
  @Nullable
  public TinyScriptControlSegment getControlSegment() {
    return findChildByClass(TinyScriptControlSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptDebuggerSegment getDebuggerSegment() {
    return findChildByClass(TinyScriptDebuggerSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptDeclareFunction getDeclareFunction() {
    return findChildByClass(TinyScriptDeclareFunction.class);
  }

  @Override
  @Nullable
  public TinyScriptEqualValue getEqualValue() {
    return findChildByClass(TinyScriptEqualValue.class);
  }

  @Override
  @Nullable
  public TinyScriptForSegment getForSegment() {
    return findChildByClass(TinyScriptForSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptForeachSegment getForeachSegment() {
    return findChildByClass(TinyScriptForeachSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptIfSegment getIfSegment() {
    return findChildByClass(TinyScriptIfSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptInvokeFunction getInvokeFunction() {
    return findChildByClass(TinyScriptInvokeFunction.class);
  }

  @Override
  @Nullable
  public TinyScriptJsonValue getJsonValue() {
    return findChildByClass(TinyScriptJsonValue.class);
  }

  @Override
  @Nullable
  public TinyScriptNegtiveSegment getNegtiveSegment() {
    return findChildByClass(TinyScriptNegtiveSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptNewInstance getNewInstance() {
    return findChildByClass(TinyScriptNewInstance.class);
  }

  @Override
  @Nullable
  public TinyScriptParenSegment getParenSegment() {
    return findChildByClass(TinyScriptParenSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptPrefixOperatorSegment getPrefixOperatorSegment() {
    return findChildByClass(TinyScriptPrefixOperatorSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptRefValue getRefValue() {
    return findChildByClass(TinyScriptRefValue.class);
  }

  @Override
  @Nullable
  public TinyScriptStaticEnumValue getStaticEnumValue() {
    return findChildByClass(TinyScriptStaticEnumValue.class);
  }

  @Override
  @Nullable
  public TinyScriptThrowSegment getThrowSegment() {
    return findChildByClass(TinyScriptThrowSegment.class);
  }

  @Override
  @Nullable
  public TinyScriptTrySegment getTrySegment() {
    return findChildByClass(TinyScriptTrySegment.class);
  }

  @Override
  @Nullable
  public TinyScriptWhileSegment getWhileSegment() {
    return findChildByClass(TinyScriptWhileSegment.class);
  }

}
