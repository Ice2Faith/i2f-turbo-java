package i2f.turbo.idea.plugin.tinyscript.lang.psi;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TinyScriptTokenType extends IElementType {
    public TinyScriptTokenType(@NonNls @NotNull String debugName) {
        super(debugName, TinyScriptLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "." + super.toString();
    }
}
