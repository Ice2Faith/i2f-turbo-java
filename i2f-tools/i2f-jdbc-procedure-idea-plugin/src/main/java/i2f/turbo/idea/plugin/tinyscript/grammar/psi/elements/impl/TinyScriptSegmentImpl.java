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

public class TinyScriptSegmentImpl extends TinyScriptPsiElement implements TinyScriptSegment {

    public TinyScriptSegmentImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull TinyScriptVisitor visitor) {
        visitor.visitSegment(this);
    }

    @Override
    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof TinyScriptVisitor) accept((TinyScriptVisitor) visitor);
        else super.accept(visitor);
    }

    @Override
    @Nullable
    public TinyScriptCommentSegment getCommentSegment() {
        return findChildByClass(TinyScriptCommentSegment.class);
    }

    @Override
    @Nullable
    public TinyScriptExpress getExpress() {
        return findChildByClass(TinyScriptExpress.class);
    }

}
