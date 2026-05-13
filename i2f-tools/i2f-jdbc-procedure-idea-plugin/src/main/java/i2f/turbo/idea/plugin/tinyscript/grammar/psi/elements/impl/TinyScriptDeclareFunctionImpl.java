// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptDeclareFunction;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptParameterList;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptScriptBlock;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.NAMING;

public class TinyScriptDeclareFunctionImpl extends TinyScriptPsiElement implements TinyScriptDeclareFunction {

    public TinyScriptDeclareFunctionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitDeclareFunction(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public TinyScriptParameterList getParameterList() {
        return findChildByClass(TinyScriptParameterList.class);
    }

    @Override
    @NotNull
    public TinyScriptScriptBlock getScriptBlock() {
        return findNotNullChildByClass(TinyScriptScriptBlock.class);
    }

    @Override
    @NotNull
    public PsiElement getNaming() {
        return findNotNullChildByType(NAMING);
    }

}
