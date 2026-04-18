// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface TinyScriptTrySegment extends PsiElement {

    @NotNull
    List<TinyScriptCatchBodyBlock> getCatchBodyBlockList();

    @NotNull
    List<TinyScriptClassNameBlock> getClassNameBlockList();

    @Nullable
    TinyScriptFinallyBodyBlock getFinallyBodyBlock();

    @NotNull
    List<TinyScriptNamingBlock> getNamingBlockList();

    @NotNull
    TinyScriptTryBodyBlock getTryBodyBlock();

}
