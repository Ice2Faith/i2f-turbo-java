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

public class TinyScriptEqualValueImpl extends TinyScriptPsiElement implements TinyScriptEqualValue {

  public TinyScriptEqualValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TinyScriptVisitor visitor) {
    visitor.visitEqualValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TinyScriptExpress getExpress() {
    return findNotNullChildByClass(TinyScriptExpress.class);
  }

  @Override
  @Nullable
  public TinyScriptExtractExpress getExtractExpress() {
    return findChildByClass(TinyScriptExtractExpress.class);
  }

  @Override
  @Nullable
  public TinyScriptStaticEnumValue getStaticEnumValue() {
    return findChildByClass(TinyScriptStaticEnumValue.class);
  }

  @Override
  @Nullable
  public PsiElement getNaming() {
    return findChildByType(NAMING);
  }

  @Override
  @Nullable
  public PsiElement getRouteNaming() {
    return findChildByType(ROUTE_NAMING);
  }

}
