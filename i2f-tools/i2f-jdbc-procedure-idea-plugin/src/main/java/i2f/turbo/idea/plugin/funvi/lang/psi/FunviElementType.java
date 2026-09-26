package i2f.turbo.idea.plugin.funvi.lang.psi;

import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.funvi.FunviLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class FunviElementType extends IElementType {
    public FunviElementType(@NonNls @NotNull String debugName) {
        super(debugName, FunviLanguage.INSTANCE);
    }
}
