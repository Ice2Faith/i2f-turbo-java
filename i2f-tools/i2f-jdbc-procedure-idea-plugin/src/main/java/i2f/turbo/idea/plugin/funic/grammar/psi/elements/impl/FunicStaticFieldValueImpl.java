// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicStaticFieldValue;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicTypeMember;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.IDENTIFIER;

public class FunicStaticFieldValueImpl extends FunicPsiElement implements FunicStaticFieldValue {

    public FunicStaticFieldValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitStaticFieldValue(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public FunicTypeMember getTypeMember() {
        return findNotNullChildByClass(FunicTypeMember.class);
    }

    @Override
    @NotNull
    public PsiElement getIdentifier() {
        return findNotNullChildByType(IDENTIFIER);
    }

}
