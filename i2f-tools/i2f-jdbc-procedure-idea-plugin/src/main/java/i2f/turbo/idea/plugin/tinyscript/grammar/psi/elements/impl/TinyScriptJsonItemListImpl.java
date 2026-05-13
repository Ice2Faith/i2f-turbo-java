// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptExpress;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptJsonItemList;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptVisitor;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TinyScriptJsonItemListImpl extends TinyScriptPsiElement implements TinyScriptJsonItemList {

    public TinyScriptJsonItemListImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitJsonItemList(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<TinyScriptExpress> getExpressList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, TinyScriptExpress.class);
    }

}
