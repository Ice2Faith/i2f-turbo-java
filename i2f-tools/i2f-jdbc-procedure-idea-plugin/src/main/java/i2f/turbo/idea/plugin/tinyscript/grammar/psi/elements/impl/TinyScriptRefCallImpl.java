// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptFunctionCall;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptRefCall;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptRefValue;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

public class TinyScriptRefCallImpl extends TinyScriptPsiElement implements TinyScriptRefCall {

    public TinyScriptRefCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitRefCall(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public TinyScriptFunctionCall getFunctionCall() {
        return findNotNullChildByClass(TinyScriptFunctionCall.class);
    }

    @Override
    @NotNull
    public TinyScriptRefValue getRefValue() {
        return findNotNullChildByClass(TinyScriptRefValue.class);
    }

}
