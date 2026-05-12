// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes.*;

import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.*;
import i2f.turbo.idea.plugin.funic.lang.psi.impl.FunicPsiImplUtil;

public class FunicValueSegmentImpl extends FunicPsiElement implements FunicValueSegment {

    public FunicValueSegmentImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitValueSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public FunicConstValue getConstValue() {
        return findChildByClass(FunicConstValue.class);
    }

    @Override
    @Nullable
    public FunicRefValue getRefValue() {
        return findChildByClass(FunicRefValue.class);
    }

    @Override
    @Nullable
    public FunicTypeClass getTypeClass() {
        return findChildByClass(FunicTypeClass.class);
    }

    @Override
    @Nullable
    public FunicVariableValue getVariableValue() {
        return findChildByClass(FunicVariableValue.class);
    }

}
