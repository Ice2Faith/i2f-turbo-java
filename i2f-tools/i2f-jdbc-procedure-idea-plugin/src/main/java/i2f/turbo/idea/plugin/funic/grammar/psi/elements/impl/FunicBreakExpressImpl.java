// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicBreakExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

public class FunicBreakExpressImpl extends FunicPsiElement implements FunicBreakExpress {

  public FunicBreakExpressImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FunicVisitor visitor) {
    visitor.visitBreakExpress(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
    else super.accept(visitor);
  }

}
