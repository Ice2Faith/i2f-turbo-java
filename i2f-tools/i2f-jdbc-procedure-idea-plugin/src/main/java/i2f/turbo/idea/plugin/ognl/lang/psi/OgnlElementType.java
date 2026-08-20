package i2f.turbo.idea.plugin.ognl.lang.psi;

import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.ognl.OgnlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OgnlElementType extends IElementType {
    public OgnlElementType(@NonNls @NotNull String debugName) {
        super(debugName, OgnlLanguage.INSTANCE);
    }
}
