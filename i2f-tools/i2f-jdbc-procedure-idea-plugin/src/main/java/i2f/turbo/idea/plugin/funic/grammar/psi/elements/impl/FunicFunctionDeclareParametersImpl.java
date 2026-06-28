// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funic.grammar.psi.elements.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicFunctionDeclareParameters;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicFunctionParameter;
import i2f.turbo.idea.plugin.funic.grammar.psi.elements.FunicVisitor;
import i2f.turbo.idea.plugin.funic.lang.psi.FunicPsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FunicFunctionDeclareParametersImpl extends FunicPsiElement implements FunicFunctionDeclareParameters {

    public FunicFunctionDeclareParametersImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull FunicVisitor visitor) {
        visitor.visitFunctionDeclareParameters(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof FunicVisitor) accept((FunicVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @NotNull
    public List<FunicFunctionParameter> getFunctionParameterList() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, FunicFunctionParameter.class);
    }

}
