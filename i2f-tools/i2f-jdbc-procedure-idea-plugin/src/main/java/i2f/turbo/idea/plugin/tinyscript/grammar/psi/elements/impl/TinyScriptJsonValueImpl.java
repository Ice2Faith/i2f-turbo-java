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

public class TinyScriptJsonValueImpl extends TinyScriptPsiElement implements TinyScriptJsonValue {

    public TinyScriptJsonValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitJsonValue(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public TinyScriptConstValue getConstValue() {
        return findChildByClass(TinyScriptConstValue.class);
    }

    @Override
    @Nullable
    public TinyScriptInvokeFunction getInvokeFunction() {
        return findChildByClass(TinyScriptInvokeFunction.class);
    }

    @Override
    @Nullable
    public TinyScriptJsonArrayValue getJsonArrayValue() {
        return findChildByClass(TinyScriptJsonArrayValue.class);
    }

    @Override
    @Nullable
    public TinyScriptJsonMapValue getJsonMapValue() {
        return findChildByClass(TinyScriptJsonMapValue.class);
    }

    @Override
    @Nullable
    public TinyScriptRefValue getRefValue() {
        return findChildByClass(TinyScriptRefValue.class);
    }

}
