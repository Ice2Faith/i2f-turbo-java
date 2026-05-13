// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicAssignRightPart;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

public class FunicAssignRightPartImpl extends FunicPsiElement implements FunicAssignRightPart {

  public FunicAssignRightPartImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FunicVisitor visitor) {
    visitor.visitAssignRightPart(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public FunicExpress getExpress() {
    return findNotNullChildByClass(FunicExpress.class);
  }

}
