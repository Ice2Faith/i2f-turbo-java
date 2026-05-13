// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface TinyScriptConstValue extends PsiElement {

    @Nullable
    TinyScriptBinNumber getBinNumber();

    @Nullable
    TinyScriptConstBool getConstBool();

    @Nullable
    TinyScriptConstClass getConstClass();

    @Nullable
    TinyScriptConstMultilineString getConstMultilineString();

    @Nullable
    TinyScriptConstNull getConstNull();

    @Nullable
    TinyScriptConstRenderString getConstRenderString();

    @Nullable
    TinyScriptConstString getConstString();

    @Nullable
    TinyScriptDecNumber getDecNumber();

    @Nullable
    TinyScriptHexNumber getHexNumber();

    @Nullable
    TinyScriptOtcNumber getOtcNumber();

}
