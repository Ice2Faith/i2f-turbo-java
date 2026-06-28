// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.IDENTIFIER;

public class FunicKeyValueExpressImpl extends FunicPsiElement implements FunicKeyValueExpress {

    public FunicKeyValueExpressImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitKeyValueExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicConstRenderString getConstRenderString() {
        return findChildByClass(FunicConstRenderString.class);
    }

    @Override
    @Nullable
    public FunicConstString getConstString() {
        return findChildByClass(FunicConstString.class);
    }

    @Override
    @Nullable
    public FunicExpress getExpress() {
        return findChildByClass(FunicExpress.class);
    }

    @Override
    @Nullable
    public FunicVariableValue getVariableValue() {
        return findChildByClass(FunicVariableValue.class);
    }

    @Override
    @Nullable
    public PsiElement getIdentifier() {
        return findChildByType(IDENTIFIER);
    }

}
