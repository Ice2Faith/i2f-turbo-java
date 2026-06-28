package i2f.turbo.idea.plugin.funic.lang.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2025/3/13 20:00
 * @desc
 */
public class FunicPsiElement extends ASTWrapperPsiElement {
    public FunicPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getNode().getElementType() + "@" + getText() + ")";
    }
}
