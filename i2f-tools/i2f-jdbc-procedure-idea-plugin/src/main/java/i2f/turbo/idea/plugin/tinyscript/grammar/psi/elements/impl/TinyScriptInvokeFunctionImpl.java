// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptFunctionCall;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptInvokeFunction;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptRefCall;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TinyScriptInvokeFunctionImpl extends TinyScriptPsiElement implements TinyScriptInvokeFunction {

    public TinyScriptInvokeFunctionImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitInvokeFunction(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<TinyScriptFunctionCall> getFunctionCallList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptFunctionCall.class);
    }

    @Override
    @Nullable
    public TinyScriptRefCall getRefCall() {
        return findChildByClass(TinyScriptRefCall.class);
    }

}
