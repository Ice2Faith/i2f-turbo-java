// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicFullName;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicTypeClass;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicTypeMember;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FunicTypeMemberImpl extends FunicPsiElement implements FunicTypeMember {

    public FunicTypeMemberImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitTypeMember(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicFullName getFullName() {
        return findChildByClass(FunicFullName.class);
    }

    @Override
    @Nullable
    public FunicTypeClass getTypeClass() {
        return findChildByClass(FunicTypeClass.class);
    }

}
