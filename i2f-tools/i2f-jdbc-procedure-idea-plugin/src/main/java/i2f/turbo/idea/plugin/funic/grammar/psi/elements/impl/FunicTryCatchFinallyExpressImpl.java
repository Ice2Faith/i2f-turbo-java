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

public class FunicTryCatchFinallyExpressImpl extends FunicPsiElement implements FunicTryCatchFinallyExpress {

    public FunicTryCatchFinallyExpressImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitTryCatchFinallyExpress(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunicCatchBlock> getCatchBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicCatchBlock.class);
    }

    @Override
    @NotNull
    public List<FunicScriptBlock> getScriptBlockList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicScriptBlock.class);
    }

}
