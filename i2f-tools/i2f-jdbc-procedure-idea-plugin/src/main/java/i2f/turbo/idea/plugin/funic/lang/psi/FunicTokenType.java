package i2f.turbo.idea.plugin.funic.lang.psi;

import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funic.FunicLanguage;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FunicTokenType extends IElementType {
    public FunicTokenType(@NonNls @NotNull String debugName) {
        super(debugName, FunicLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "." + super.toString();
    }
}
