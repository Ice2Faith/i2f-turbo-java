// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes.*;

import i2f.turbo.idea.plugin.tinyscript.lang.psi.TinyScriptPsiElement;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.tinyscript.lang.psi.impl.TinyScriptPsiImplUtil;

public class TinyScriptDecNumberImpl extends TinyScriptPsiElement implements TinyScriptDecNumber {

    public TinyScriptDecNumberImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitDecNumber(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumber() {
        return findChildByType(TERM_CONST_NUMBER);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberFloat() {
        return findChildByType(TERM_CONST_NUMBER_FLOAT);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberScien1() {
        return findChildByType(TERM_CONST_NUMBER_SCIEN_1);
    }

    @Override
    @Nullable
    public PsiElement getTermConstNumberScien2() {
        return findChildByType(TERM_CONST_NUMBER_SCIEN_2);
    }

    @Override
    @Nullable
    public PsiElement getTermDigit() {
        return findChildByType(TERM_DIGIT);
    }

}
