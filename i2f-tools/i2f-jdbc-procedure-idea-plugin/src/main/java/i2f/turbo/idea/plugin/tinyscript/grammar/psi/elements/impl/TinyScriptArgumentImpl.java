// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptArgument;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptArgumentValue;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConstString;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.NAMING;

public class TinyScriptArgumentImpl extends TinyScriptPsiElement implements TinyScriptArgument {

    public TinyScriptArgumentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitArgument(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public TinyScriptArgumentValue getArgumentValue() {
        return findNotNullChildByClass(TinyScriptArgumentValue.class);
    }

    @Override
    @Nullable
    public TinyScriptConstString getConstString() {
        return findChildByClass(TinyScriptConstString.class);
    }

    @Override
    @Nullable
    public PsiElement getNaming() {
        return findChildByType(NAMING);
    }

}
