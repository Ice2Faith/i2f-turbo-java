// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicAwaitExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicExpress;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunicAwaitExpressImpl extends FunicPsiElement implements FunicAwaitExpress {

  public FunicAwaitExpressImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FunicVisitor visitor) {
    visitor.visitAwaitExpress(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<FunicExpress> getExpressList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicExpress.class);
  }

}
