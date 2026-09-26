package i2f.turbo.idea.plugin.ognl.lang.psi;

import com.intellij.psi.tree.IElementType;
import i2f.turbo.idea.plugin.ognl.OgnlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OgnlTokenType extends IElementType {
    public OgnlTokenType(@NonNls @NotNull String debugName) {
        super(debugName, OgnlLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "." + super.toString();
    }
}
