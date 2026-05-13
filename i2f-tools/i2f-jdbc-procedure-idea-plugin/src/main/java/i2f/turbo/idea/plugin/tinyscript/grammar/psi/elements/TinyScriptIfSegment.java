// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TinyScriptIfSegment extends PsiElement {

    @NotNull
    List<TinyScriptConditionBlock> getConditionBlockList();

    @NotNull
    List<TinyScriptScriptBlock> getScriptBlockList();

}
