package i2f.turbo.idea.plugin.tinyscript.lang.psi;

import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TinyScriptElementType extends IElementType {
    public TinyScriptElementType(@NonNls @NotNull String debugName) {
        super(debugName, TinyScriptLanguage.INSTANCE);
    }
}
