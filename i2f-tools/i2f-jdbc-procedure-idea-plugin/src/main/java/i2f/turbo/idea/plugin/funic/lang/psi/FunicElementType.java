package i2f.turbo.idea.plugin.funic.lang.psi;

import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funic.FunicLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FunicElementType extends IElementType {
    public FunicElementType(@NonNls @NotNull String debugName) {
        super(debugName, FunicLanguage.INSTANCE);
    }
}
